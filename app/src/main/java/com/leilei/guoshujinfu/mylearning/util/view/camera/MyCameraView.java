package com.leilei.guoshujinfu.mylearning.util.view.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.leilei.guoshujinfu.mylearning.util.camera.CameraUtil;
import com.leilei.guoshujinfu.mylearning.util.metrics.MetricsUtil;
import com.leilei.guoshujinfu.mylearning.util.storage.StorageUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/10
 */

public class MyCameraView extends SurfaceView  implements SurfaceHolder.Callback{
    private Context mContext;
    private Camera mCamera;
    private Camera.Parameters mParameters;
    private int mCameraFacinng = Camera.CameraInfo.CAMERA_FACING_BACK;
    private int mCameraWidth;
    private int mCameraHeight;
    private double mAspect;
    private int mRotation;
    private OnFlashModeChangerListener mOnFlashModeChangerListener;
    private onTakePictureListener mOnTakePictureListener;


    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try {
                mCamera.stopPreview();
                File file = StorageUtil.createImgFile();
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
                bitmap.recycle();
                bitmap = null;
                if(mOnTakePictureListener != null) {
                    mOnTakePictureListener.onTakePicture(true, file.getAbsolutePath());
                }
                mCamera.startPreview();

            }catch (Exception e) {
                if(mOnTakePictureListener != null) {
                    mOnTakePictureListener.onTakePicture(false, null);
                }
                releaseCamera();
                openCamera();
                setCameraParameters();

            }
        }
    };
    public MyCameraView(Context context){
        super(context);
        init(context);
    }
    public MyCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public MyCameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context){
        mContext = context;
        mCameraWidth = MetricsUtil.getScreenWidth(context);
        mCameraHeight = MetricsUtil.getScreenHeight(context);
        mAspect = (double) mCameraHeight / (double) mCameraWidth;
        mRotation = ((Activity) mContext).getWindowManager().getDefaultDisplay().getRotation();
        SurfaceHolder holder = this.getHolder();
        holder.addCallback(this);
        if(mContext instanceof  onTakePictureListener) {
            mOnTakePictureListener = ((onTakePictureListener) mContext);
        }
        if(mContext instanceof  OnFlashModeChangerListener) {
            mOnFlashModeChangerListener = ((OnFlashModeChangerListener) mContext);
        }

    }
    private void openCamera() {
        mCamera = CameraUtil.openCamera(mCameraFacinng);
        if(mCamera != null) {
            try {
                mCamera.setPreviewDisplay(this.getHolder());
                mParameters = mCamera.getParameters();
            } catch (Exception e) {

            }
        }
    }
    private void setCameraParameters() {
        try{
            if(mCamera != null) {
                mParameters.setZoom(0);


                if(CameraUtil.isSupportFocusMode(mParameters, Camera.Parameters.FOCUS_MODE_AUTO)){
                    mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                } else {
                    mParameters.setFocusMode(mParameters.getFocusMode());
                }
                if(CameraUtil.isSupportFlashMode(mParameters, Camera.Parameters.FLASH_MODE_AUTO)){
                    if(mOnFlashModeChangerListener != null) {
                        mOnFlashModeChangerListener.onFlashMoChange(Camera.Parameters.FLASH_MODE_AUTO);
                    }
                } else if(CameraUtil.isSupportFlashMode(mParameters, Camera.Parameters.FLASH_MODE_OFF)) {
                    if(mOnFlashModeChangerListener != null) {
                        mOnFlashModeChangerListener.onFlashMoChange(Camera.Parameters.FLASH_MODE_OFF);
                    }
                }
                mParameters.setPictureFormat(CameraUtil.getSuitablePictureFormat(mParameters, ImageFormat.JPEG));
                Camera.Size pictureSize = CameraUtil.getSuitablePictureSize(mParameters, mAspect);
                mParameters.setPictureSize(pictureSize.width, pictureSize.height);
                Camera.Size previewSize = CameraUtil.getSuitablePreview(mParameters, mCameraWidth, mCameraHeight, mAspect);
                mParameters.setPreviewSize(previewSize.width, previewSize.height);
                if(mCameraFacinng == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    mParameters.setRotation(90);
                } else {
                    mParameters.setRotation(270);
                }





                mCamera.setParameters(mParameters);
                CameraUtil.setOrientation(mCamera, mCameraFacinng, mRotation);
                mCamera.startPreview();
                autoFocus(mCameraWidth / 2, mCameraHeight / 2);

            }
        }catch(Exception e) {

        }

    }
    public void switchCameraFacing() {
        releaseCamera();
        if(mCameraFacinng == Camera.CameraInfo.CAMERA_FACING_BACK) {
            mCameraFacinng = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else if(mCameraFacinng == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            mCameraFacinng = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        openCamera();
        setCameraParameters();
    }
    public void changeFlashMode(String flashMode) {
        if(mParameters == null) {
            return;
        }
        if(CameraUtil.isSupportFlashMode(mParameters, flashMode)) {
            mParameters.setFlashMode(flashMode);
            mCamera.setParameters(mParameters);
            mOnFlashModeChangerListener.onFlashMoChange(flashMode);
        }
    }
    public void autoFocus(int rawX, int rawY) {
        if(mCamera != null && mParameters != null) {
            try {
                int maxNumFocusAreas = mParameters.getMaxNumFocusAreas();
                if(mParameters.getFocusMode() == Camera.Parameters.FOCUS_MODE_AUTO && maxNumFocusAreas > 0) {
                    int halfOfCameraWidth = mCameraWidth / 2;
                    int halfOfCameraHeight = mCameraHeight / 2;
                    double scaleSizeX = 1000 / (double) halfOfCameraWidth;
                    double scaleSizeY  = 1000 / (double) halfOfCameraHeight;

                    int focusPivotX = (int) ((rawX - halfOfCameraWidth) * scaleSizeX);
                    int focusPivotY = (int) ((rawY - halfOfCameraHeight) * scaleSizeY);
                    int left, top, right, bottom;
                    if (focusPivotX <= -900) {
                        left = -1000;
                        right = focusPivotX + 100;
                    } else if (focusPivotX >= 900) {
                        left = focusPivotX - 100;
                        right = 1000;
                    } else {
                        left = focusPivotX - 100;
                        right = focusPivotX + 100;
                    }
                    if (focusPivotY <= -900) {
                        top = -1000;
                        bottom = focusPivotY + 100;
                    } else if (focusPivotY >= 900) {
                        top = focusPivotY - 100;
                        bottom = 1000;
                    } else {
                        top = focusPivotY - 100;
                        bottom = focusPivotY + 100;
                    }
                    Rect focusAreaRect = new Rect(left, top, right, bottom);
                    Camera.Area area = new Camera.Area(focusAreaRect, 1000);
                    List<Camera.Area> focusAreas = new ArrayList<>();
                    focusAreas.add(area);
                    mParameters.setFocusAreas(focusAreas);
                    mCamera.setParameters(mParameters);
                    mCamera.cancelAutoFocus();
                    mCamera.autoFocus(new Camera.AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {
                        }
                    });
                } else {

                }


            } catch (Exception e) {

            }

        }
    }
    public void takePicture() {
        try {
            mCamera.takePicture(null, null, mPictureCallback);

        } catch (Exception e) {
            if(mOnTakePictureListener != null) {
                mOnTakePictureListener.onTakePicture(false, null);
            }
            releaseCamera();
            openCamera();
            setCameraParameters();

        }

    }
    private void releaseCamera() {
        if(mCamera != null) {
            try {
                mCamera.release();
                mCamera = null;
            } catch (Exception e) {

            }
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(!CameraUtil.hasCameraonDevicef(mContext)) {

        } else {
            openCamera();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        setCameraParameters();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();

    }

    public interface OnFlashModeChangerListener {
        void onFlashMoChange(String flashMode);
    }
    public interface onTakePictureListener {
        void onTakePicture(boolean isSuccess, String filePath);
    }
}
