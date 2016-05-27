/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cqgk.clerk.zxing.camera;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.cqgk.clerk.utils.DisplayUtil;


final class CameraConfigurationManager {

    private static final String TAG = CameraConfigurationManager.class
            .getSimpleName();

    private static final int TEN_DESIRED_ZOOM = 2;
    private static final int DESIRED_SHARPNESS = 30;

    private static final Pattern COMMA_PATTERN = Pattern.compile(",");

    private final Context context;
    private Point screenResolution;
    private Point cameraResolution;
    private int previewFormat;
    private String previewFormatString;

    CameraConfigurationManager(Context context) {
        this.context = context;
    }

    /**
     * Reads, one time, values from the camera that are needed by the app.
     */
    void initFromCameraParameters(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        previewFormat = parameters.getPreviewFormat();
        previewFormatString = parameters.get("preview-format");
        Log.d(TAG, "Default preview format: " + previewFormat + '/'
                + previewFormatString);
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        screenResolution = new Point(display.getWidth(), display.getHeight());
        //screenResolution = new Point(display.getWidth(), display.getHeight());
        Log.d(TAG, "Screen resolution: " + screenResolution);

        Point screenResolutionForCamera = new Point();
        screenResolutionForCamera.x = screenResolution.x;
        screenResolutionForCamera.y = screenResolution.y;

        // preview size is always something like 480*320, other 320*480
        if (screenResolution.x < screenResolution.y) {
            screenResolutionForCamera.x = screenResolution.y;
            screenResolutionForCamera.y = screenResolution.x;
        }

        cameraResolution = getCameraResolution(parameters, screenResolutionForCamera);
        Log.d(TAG, "Camera resolution: " + cameraResolution);
    }

    /**
     * Sets the camera up to take preview images which are used for both preview
     * and decoding. We detect the preview format here so that
     * buildLuminanceSource() can build an appropriate LuminanceSource subclass.
     * In the future we may want to force YUV420SP as it's the smallest, and the
     * planar Y can be used for barcode scanning without a copy in some cases.
     */
    void setDesiredCameraParameters(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        Log.d(TAG, "Setting preview size: " + cameraResolution);
        parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
        setFlash(parameters);
        setZoom(parameters, TEN_DESIRED_ZOOM);
        camera.setDisplayOrientation(90);
        camera.setParameters(parameters);
    }

    protected void setDisplayOrientation(Camera camera, int angle) {

        Method downPolymorphic;

        try {

            downPolymorphic = camera.getClass().getMethod(
                    "setDisplayOrientation", new Class[]{int.class});

            if (downPolymorphic != null)

                downPolymorphic.invoke(camera, new Object[]{angle});

        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    Point getCameraResolution() {
        return cameraResolution;
    }

    Point getScreenResolution() {
        return screenResolution;
    }

    int getPreviewFormat() {
        return previewFormat;
    }

    String getPreviewFormatString() {
        return previewFormatString;
    }

    private static Point getCameraResolution(Camera.Parameters parameters,
                                             Point screenResolution) {

        String previewSizeValueString = parameters.get("preview-size-values");
        // saw this on Xperia
        if (previewSizeValueString == null) {
            previewSizeValueString = parameters.get("preview-size-value");
        }

        Point cameraResolution = null;

        if (previewSizeValueString != null) {
            Log.d(TAG, "preview-size-values parameter: "
                    + previewSizeValueString);
            cameraResolution = findBestPreviewSizeValue(previewSizeValueString,
                    screenResolution);
        }

        if (cameraResolution == null) {
            cameraResolution = new Point((screenResolution.x >> 3) << 3,
                    (screenResolution.y >> 3) << 3);
        }

        return cameraResolution;
    }

    private static Point findBestPreviewSizeValue(
            CharSequence previewSizeValueString, Point screenResolution) {
        int bestX = 0;
        int bestY = 0;
        int diff = Integer.MAX_VALUE;
        for (String previewSize : COMMA_PATTERN.split(previewSizeValueString)) {

            previewSize = previewSize.trim();
            int dimPosition = previewSize.indexOf('x');
            if (dimPosition < 0) {
                Log.w(TAG, "Bad preview-size: " + previewSize);
                continue;
            }

            int newX;
            int newY;
            try {
                newX = Integer.parseInt(previewSize.substring(0, dimPosition));
                newY = Integer.parseInt(previewSize.substring(dimPosition + 1));
            } catch (NumberFormatException nfe) {
                Log.w(TAG, "Bad preview-size: " + previewSize);
                continue;
            }

            int newDiff = Math.abs(newX - screenResolution.x)
                    + Math.abs(newY - screenResolution.y);
            if (newDiff == 0) {
                bestX = newX;
                bestY = newY;
                break;
            } else if (newDiff < diff) {
                bestX = newX;
                bestY = newY;
                diff = newDiff;
            }

        }

        if (bestX > 0 && bestY > 0) {
            return new Point(bestX, bestY);
        }
        return null;
    }

    private static int findBestMotZoomValue(CharSequence stringValues,
                                            int tenDesiredZoom) {
        int tenBestValue = 0;
        for (String stringValue : COMMA_PATTERN.split(stringValues)) {
            stringValue = stringValue.trim();
            double value;
            try {
                value = Double.parseDouble(stringValue);
            } catch (NumberFormatException nfe) {
                return tenDesiredZoom;
            }
            int tenValue = (int) (10.0 * value);
            if (Math.abs(tenDesiredZoom - value) < Math.abs(tenDesiredZoom
                    - tenBestValue)) {
                tenBestValue = tenValue;
            }
        }
        return tenBestValue;
    }

    private void setFlash(Camera.Parameters parameters) {
        // FIXME: This is a hack to turn the flash off on the Samsung Galaxy.
        // And this is a hack-hack to work around a different value on the
        // Behold II
        // Restrict Behold II check to Cupcake, per Samsung's advice
        // if (Build.MODEL.contains("Behold II") &&
        // CameraManager.SDK_INT == Build.VERSION_CODES.CUPCAKE) {
        if (Build.MODEL.contains("Behold II") && CameraManager.SDK_INT == 3) { // 3
            // =
            // Cupcake
            parameters.set("flash-value", 1);
        } else {
            parameters.set("flash-value", 2);
        }
        // This is the standard setting to turn the flash off that all devices
        // should honor.
        parameters.set("flash-mode", "off");
    }

    public static void setZoom(Camera.Parameters parameters, double targetZoomRatio) {
        if (parameters.isZoomSupported()) {
            Integer zoom = indexOfClosestZoom(parameters, targetZoomRatio);
            if (zoom == null) {
                return;
            }
            if (parameters.getZoom() == zoom) {
                Log.i(TAG, "Zoom is already set to " + zoom);
            } else {
                Log.i(TAG, "Setting zoom to " + zoom);
                parameters.setZoom(zoom);
            }
        } else {
            Log.i(TAG, "Zoom is not supported");
        }
    }

    private static Integer indexOfClosestZoom(Camera.Parameters parameters, double targetZoomRatio) {
        List<Integer> ratios = parameters.getZoomRatios();
        Log.i(TAG, "Zoom ratios: " + ratios);
        int maxZoom = parameters.getMaxZoom();
        if (ratios == null || ratios.isEmpty() || ratios.size() != maxZoom + 1) {
            Log.w(TAG, "Invalid zoom ratios!");
            return null;
        }
        double target100 = 100.0 * targetZoomRatio;
        double smallestDiff = Double.POSITIVE_INFINITY;
        int closestIndex = 0;
        for (int i = 0; i < ratios.size(); i++) {
            double diff = Math.abs(ratios.get(i) - target100);
            if (diff < smallestDiff) {
                smallestDiff = diff;
                closestIndex = i;
            }
        }
        Log.i(TAG, "Chose zoom ratio of " + (ratios.get(closestIndex) / 100.0));
        return closestIndex;
    }

//    private void setZoom(Camera.Parameters parameters) {
//
//
//        String zoomSupportedString = parameters.get("zoom-supported");
//        if (zoomSupportedString != null
//                && !Boolean.parseBoolean(zoomSupportedString)) {
//            return;
//        }
//
//        int tenDesiredZoom = TEN_DESIRED_ZOOM;
//
//        String maxZoomString = parameters.get("max-zoom");
//        if (maxZoomString != null) {
//            try {
//                int tenMaxZoom = (int) (10.0 * Double
//                        .parseDouble(maxZoomString));
//                if (tenDesiredZoom > tenMaxZoom) {
//                    tenDesiredZoom = tenMaxZoom;
//                }
//            } catch (NumberFormatException nfe) {
//                Log.w(TAG, "Bad max-zoom: " + maxZoomString);
//            }
//        }
//
//        String takingPictureZoomMaxString = parameters
//                .get("taking-picture-zoom-max");
//        if (takingPictureZoomMaxString != null) {
//            try {
//                int tenMaxZoom = Integer.parseInt(takingPictureZoomMaxString);
//                if (tenDesiredZoom > tenMaxZoom) {
//                    tenDesiredZoom = tenMaxZoom;
//                }
//            } catch (NumberFormatException nfe) {
//                Log.w(TAG, "Bad taking-picture-zoom-max: "
//                        + takingPictureZoomMaxString);
//            }
//        }
//
//        String motZoomValuesString = parameters.get("mot-zoom-values");
//        if (motZoomValuesString != null) {
//            tenDesiredZoom = findBestMotZoomValue(motZoomValuesString,
//                    tenDesiredZoom);
//        }
//
//        String motZoomStepString = parameters.get("mot-zoom-step");
//        if (motZoomStepString != null) {
//            try {
//                double motZoomStep = Double.parseDouble(motZoomStepString
//                        .trim());
//                int tenZoomStep = (int) (10.0 * motZoomStep);
//                if (tenZoomStep > 1) {
//                    tenDesiredZoom -= tenDesiredZoom % tenZoomStep;
//                }
//            } catch (NumberFormatException nfe) {
//                // continue
//            }
//        }
//
//        // Set zoom. This helps encourage the user to pull back.
//        // Some devices like the Behold have a zoom parameter
//        if (maxZoomString != null || motZoomValuesString != null) {
//            parameters.set("zoom", String.valueOf(tenDesiredZoom / 10.0));
//        }
//
//        // Most devices, like the Hero, appear to expose this zoom parameter.
//        // It takes on values like "27" which appears to mean 2.7x zoom
//        if (takingPictureZoomMaxString != null) {
//            parameters.set("taking-picture-zoom", tenDesiredZoom);
//        }
//    }

	/*
     * private void setSharpness(Camera.Parameters parameters) {
	 * 
	 * int desiredSharpness = DESIRED_SHARPNESS;
	 * 
	 * String maxSharpnessString = parameters.get("sharpness-max"); if
	 * (maxSharpnessString != null) { try { int maxSharpness =
	 * Integer.parseInt(maxSharpnessString); if (desiredSharpness >
	 * maxSharpness) { desiredSharpness = maxSharpness; } } catch
	 * (NumberFormatException nfe) { Log.w(TAG, "Bad sharpness-max: " +
	 * maxSharpnessString); } }
	 * 
	 * parameters.set("sharpness", desiredSharpness); }
	 */

    /**
     * 预览尺寸与给定的宽高尺寸比较器。首先比较宽高的比例，在宽高比相同的情况下，根据宽和高的最小差进行比较。
     */
    private static class SizeComparator implements Comparator<Camera.Size> {

        private final int width;
        private final int height;
        private final float ratio;

        SizeComparator(int width, int height) {
            if (width < height) {
                this.width = height;
                this.height = width;
            } else {
                this.width = width;
                this.height = height;
            }
            this.ratio = (float) this.height / this.width;
        }

        @Override
        public int compare(Camera.Size size1, Camera.Size size2) {
            int width1 = size1.width;
            int height1 = size1.height;
            int width2 = size2.width;
            int height2 = size2.height;

            float ratio1 = Math.abs((float) height1 / width1 - ratio);
            float ratio2 = Math.abs((float) height2 / width2 - ratio);
            int result = Float.compare(ratio1, ratio2);
            if (result != 0) {
                return result;
            } else {
                int minGap1 = Math.abs(width - width1) + Math.abs(height - height1);
                int minGap2 = Math.abs(width - width2) + Math.abs(height - height2);
                return minGap1 - minGap2;
            }
        }
    }

    /**
     * 通过对比得到与宽高比最接近的尺寸（如果有相同尺寸，优先选择）
     *
     * @param surfaceWidth 需要被进行对比的原宽
     * @param surfaceHeight 需要被进行对比的原高
     * @param preSizeList 需要对比的预览尺寸列表
     * @return 得到与原宽高比例最接近的尺寸
     */
    protected Camera.Size findCloselySize(int surfaceWidth, int surfaceHeight, List<Camera.Size> preSizeList) {
        Collections.sort(preSizeList, new SizeComparator(surfaceWidth, surfaceHeight));
        return preSizeList.get(0);
    }
}
