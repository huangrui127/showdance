/*
 * Copyright (C) 2013 The Android Open Source Project
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

package gl.live.danceshow.ui.camera;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.annotation.TargetApi;
import android.hardware.Camera;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Camera related utilities.
 */
public class CameraHelper {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /**
     * find largest support size that smaller than w
     *
     * @param sizes
     * @param w
     * @return
     */
    @Nullable
    public static Camera.Size getSquarePreviewSize(List<Camera.Size> sizes, int w) {
        ArrayList<Camera.Size> orderedSize = new ArrayList<Camera.Size>();
        orderedSize.addAll(sizes);
        Collections.sort(orderedSize, new Comparator<Camera.Size>() {
            @Override
            public int compare(@NonNull Camera.Size lhs, @NonNull Camera.Size rhs) {
                return lhs.height < rhs.height ? -1 : (lhs.height == rhs.height ? 0 : 1);
            }
        });
        Camera.Size lastSize = null;
        for (Camera.Size size : orderedSize) {
            if (size.height < w) {
                lastSize = size; // continue
            } else {
                return lastSize;
            }
        }
        return orderedSize.get(orderedSize.size() - 1);
    }

    /**
     * Iterate over supported camera preview sizes to see which one best fits the
     * dimensions of the given view while maintaining the aspect ratio. If none can,
     * be lenient with the aspect ratio.
     *
     * @param sizes Supported camera preview sizes.
     * @param w     The width of the view.
     * @param h     The height of the view.
     * @return Best match camera preview size to fit in the view.
     */
    @Nullable
    public static Camera.Size getOptimalPreviewSize(@Nullable List<Camera.Size> sizes, int w, int h) {
        // Use a very small tolerance because we want an exact match.
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null)
            return null;

        Camera.Size optimalSize = null;

        // Start with max value and refine as we iterate over available preview sizes. This is the
        // minimum difference between view and camera height.
        double minDiff = Double.MAX_VALUE;

        // Target view height
        int targetHeight = h;

        // Try to find a preview size that matches aspect ratio and the target view size.
        // Iterate over all available sizes and pick the largest size that can fit in the view and
        // still maintain the aspect ratio.
        for (Camera.Size size : sizes) {
        	Log.d("guolei", "size = "+size.width+" X "+size.height);
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find preview size that matches the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    /**
     * @return the default camera on the device. Return null if there is no camera on the device.
     */
    @Nullable
    public static Camera getDefaultCameraInstance() {
        return Camera.open();
    }


    /**
     * @return the default rear/back facing camera on the device. Returns null if camera is not
     * available.
     */
    @Nullable
    public static Camera getDefaultBackFacingCameraInstance() {
        return getDefaultCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    /**
     * @return the default front facing camera on the device. Returns null if camera is not
     * available.
     */
    @Nullable
    public static Camera getDefaultFrontFacingCameraInstance() {
        return getDefaultCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
    }


    /**
     * @param position Physical position of the camera i.e Camera.CameraInfo.CAMERA_FACING_FRONT
     *                 or Camera.CameraInfo.CAMERA_FACING_BACK.
     * @return the default camera on the device. Returns null if camera is not available.
     */
    @Nullable
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private static Camera getDefaultCamera(int position) {
        // Find the total number of cameras available
        int mNumberOfCameras = Camera.getNumberOfCameras();

        // Find the ID of the back-facing ("default") camera
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < mNumberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == position) {
                return Camera.open(i);

            }
        }

        return null;
    }

    /**
     * Creates a media file in the {@code Environment.DIRECTORY_PICTURES} directory. The directory
     * is persistent and available to other applications like gallery.
     *
     * @param type Media type. Can be video or image.
     * @return A file object pointing to the newly created file.
     */
    @Nullable
    public static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        if (!Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return null;
        }

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "showdance");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("CameraSample", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

}
