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

package com.cqgk.clerk.zxing.decoding;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.cqgk.clerk.zxing.CamerBaseActivity;
import com.cqgk.clerk.zxing.camera.CameraManager;
import com.cqgk.clerk.zxing.camera.PlanarYUVLuminanceSource;
import com.cqgk.clerk.R;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;

final class DecodeHandler extends Handler {

	private static final String TAG = DecodeHandler.class.getSimpleName();

	private final CamerBaseActivity activity;
	private final MultiFormatReader multiFormatReader;

	DecodeHandler(CamerBaseActivity activity,
			Hashtable<DecodeHintType, Object> hints) {
		multiFormatReader = new MultiFormatReader();
		multiFormatReader.setHints(hints);
		this.activity = activity;
	}

	@Override
	public void handleMessage(Message message) {
	    switch (message.what) {
	      case R.id.decode:
	        //Log.d(TAG, "Got decode message");
	        decode((byte[]) message.obj, message.arg1, message.arg2);
	        break;
	      case R.id.quit:
	        Looper.myLooper().quit();
	        break;
	    }
	  }

	/**
	 * Decode the data within the viewfinder rectangle, and time how long it
	 * took. For efficiency, reuse the same reader objects from one decode to
	 * the next.
	 * 
	 * @param data
	 *            The YUV preview frame.
	 * @param width
	 *            The width of the preview frame.
	 * @param height
	 *            The height of the preview frame.
	 */
	private void decode(byte[] data, int width, int height) {
		long start = System.currentTimeMillis();
		Result rawResult = null;

		byte[] rotatedData = new byte[data.length];
		 for (int y = 0; y < height; y++) {
		 for (int x = 0; x < width; x++)
		 rotatedData[x * height + height - y - 1] = data[x + y * width];
		 }
		 int tmp = width; // Here we are swapping, that's the difference to #11
		 width = height;
		 height = tmp;
		 data = rotatedData;

		PlanarYUVLuminanceSource source = CameraManager.get().buildLuminanceSource(data, width, height);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		//BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
		try {
			rawResult = multiFormatReader.decodeWithState(bitmap);
		} catch (ReaderException re) {
			// continue
		} finally {
			multiFormatReader.reset();
		}

		if (rawResult != null) {

			long end = System.currentTimeMillis();
			Log.d(TAG, "Found barcode (" + (end - start) + " ms):\n"
					+ rawResult.toString());
			Message message = Message.obtain(activity.getHandler(),
					R.id.decode_succeeded, rawResult);
			Bundle bundle = new Bundle();
			bundleThumbnail(source,bundle);
			//bundle.putParcelable(DecodeThread.BARCODE_BITMAP,source.renderCroppedGreyscaleBitmap());
			message.setData(bundle);
			message.sendToTarget();
		} else {

			Message message = Message.obtain(activity.getHandler(),
					R.id.decode_failed);
			message.sendToTarget();
		}
	}


	private static void bundleThumbnail(PlanarYUVLuminanceSource source, Bundle bundle) {
		int[] pixels = source.renderThumbnail();
		int width = source.getThumbnailWidth();
		int height = source.getThumbnailHeight();
		Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
		bundle.putByteArray(DecodeThread.BARCODE_BITMAP, out.toByteArray());
		bundle.putFloat(DecodeThread.BARCODE_SCALED_FACTOR, (float) width / source.getWidth());
	}

	private void decodePic(String pathName){

	}

	private void decodePic(String pathName,int sampleSize){
		Result rawresult=null;
		Handler handler = activity.getHandler();

	}

}
