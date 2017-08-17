package com.leilei.guoshujinfu.mylearning.util.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.view.Surface;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/10
 */

public class CameraUtil {
    public static final double MAX_ASPECT_DISTORTION = 0.15;
    public static final int  MIN_PREVIEW_PIXELS = 480*320;


    public static Camera openCamera(int cameraId) {
        Camera camera = null;
        try {
            camera = Camera.open(cameraId);
        } catch (Exception e) {

        }
        return camera;
    }
    public static boolean hasCameraonDevicef(Context context) {
        if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean isSupportFocusMode(Camera.Parameters parameters, String exceptFocuesMode) {
        boolean isSupport = false;
        List<String> supportFocusModes = parameters.getSupportedFocusModes();
        if(supportFocusModes != null) {
            for(String supportedFocusMode: supportFocusModes) {
                if(exceptFocuesMode.equals(supportedFocusMode)) {
                    isSupport = true;
                    break;
                }
            }
        }
        return isSupport;
    }
    public static boolean isSupportFlashMode(Camera.Parameters parameters, String exceptFlashMode) {
        boolean isSupport = false;
        List<String> supportFlashModes = parameters.getSupportedFlashModes();
        if(supportFlashModes != null) {
            for(String supportFlashMode: supportFlashModes) {
                if(exceptFlashMode.equals(supportFlashMode)) {
                    isSupport = true;
                    break;
                }
            }
        }
        return isSupport;
    }

    public static Integer getSuitablePictureFormat(Camera.Parameters parameters, Integer exceptPictureFormat) {
        int pictureFormat = parameters.getPictureFormat();
        List<Integer> supportPictureFormats = parameters.getSupportedPictureFormats();
        for(Integer supportPictureFormat: supportPictureFormats) {
            if(exceptPictureFormat == supportPictureFormat) {
                pictureFormat = exceptPictureFormat;
                break;
            }
        }
        return pictureFormat;
    }

    public static Camera.Size getSuitablePictureSize(Camera.Parameters parameters, double screenAspectRatio) {
        Camera.Size  pictureSize = parameters.getPictureSize();
        List<Camera.Size> supportPictureSizes = parameters.getSupportedPictureSizes();
        Collections.sort(supportPictureSizes, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size o1, Camera.Size o2) {
                int result1 = o1.width * o1.height;
                int result2 = o2.width * o2.height;
                if(result1 > result2) {
                    return -1;
                }
                if(result1 < result2) {
                    return 1;
                }
                return  0;
            }
        });
        Iterator<Camera.Size> iterator = supportPictureSizes.iterator();
        while (iterator.hasNext()) {
            Camera.Size currentSize = iterator.next();
            int currentWidth = currentSize.width;
            int currentHeight = currentSize.height;
            if(currentWidth > currentHeight) {
                currentWidth = currentHeight + currentWidth;
                currentHeight = currentWidth - currentHeight;
                currentWidth = currentWidth - currentHeight;
            }
            double currentAspectRatio = (double) currentWidth /currentHeight;
            double distortion = Math.abs(currentAspectRatio - screenAspectRatio);
            if(distortion > MAX_ASPECT_DISTORTION) {
                iterator.remove();
                continue;
            }
        }
        if(supportPictureSizes.size() > 0) {
            pictureSize = supportPictureSizes.get(0);
        }
        return pictureSize;
    }
    public static Camera.Size getSuitablePreview(Camera.Parameters parameters, int screenWidth, int screenHeight, double screenAspectRatio) {
        Camera.Size previewSize = parameters.getPreviewSize();
        List<Camera.Size> supportPreviewSizes = parameters.getSupportedPreviewSizes();
        Collections.sort(supportPreviewSizes, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size o1, Camera.Size o2) {
                double result1 = o1.width * o1.height;
                double result2 = o2.width * o2.height;
                if (result1 > result2) {
                    return -1;
                }
                if (result1 < result2) {
                    return 1;
                }

                return 0;
            }
        });
        Iterator<Camera.Size> iterator = supportPreviewSizes.iterator();
        while (iterator.hasNext()) {
            Camera.Size currentSize = iterator.next();
            int currentWidth = currentSize.width;
            int currentHeight = currentSize.height;
            if(currentWidth == screenWidth && currentHeight == screenHeight) {
                previewSize = currentSize;
                break;
            }
            if(currentWidth * currentHeight < MIN_PREVIEW_PIXELS) {
                iterator.remove();
                continue;
            }
            if (currentWidth < currentHeight) {
                currentWidth = currentWidth + currentHeight;
                currentHeight = currentWidth - currentHeight;
                currentWidth = currentWidth - currentHeight;
            }
            double currentAspectRatio = (double) currentWidth / currentHeight;
            double distortion = Math.abs(currentAspectRatio - screenAspectRatio);
            if (distortion > MAX_ASPECT_DISTORTION) {
                iterator.remove();
                continue;
            }
        }
        if(supportPreviewSizes.size() > 0) {
            previewSize = supportPreviewSizes.get(0);
        }
        return  previewSize;
    }
    public static int setOrientation(Camera camera, int cameraId, int rotation) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int degree = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;

        }
        int result;
        if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degree) % 360;
            result = (360 - result) % 360;

        } else {
            result = (info.orientation - degree + 360) % 360;
        }
        camera.setDisplayOrientation(result);
        return result;
    }
    //等比压缩图片
    public static Bitmap compressBitmapInSize(String filePath, int maxWidth, int maxHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只获取图片边界
        BitmapFactory.decodeFile(filePath, options);
        int outWidth = options.outWidth;//原图片宽度
        int outHeight = options.outHeight;//原图片高度
        int wSampleSize = 1;//宽度采样比
        int hSampleSize = 1;//高度采样比
        if (outWidth > outHeight) {
            outWidth = outHeight + outWidth;
            outHeight = outWidth - outHeight;
            outWidth = outWidth - outHeight;
        }
        while ((outWidth / wSampleSize) > maxWidth) {
            wSampleSize *= 2;
        }
        while ((outHeight / hSampleSize) > maxHeight) {
            hSampleSize *= 2;
        }
        options.inSampleSize = Math.max(wSampleSize, hSampleSize);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(filePath, options);
    }

}
