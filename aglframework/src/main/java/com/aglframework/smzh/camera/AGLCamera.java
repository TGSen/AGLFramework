package com.aglframework.smzh.camera;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import com.aglframework.smzh.AGLView;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("deprecation")
public class AGLCamera {

    private Camera camera;
    private int cameraId;
    private AGLView aglView;
    private int previewWidth;
    private int previewHeight;


    public AGLCamera(AGLView aglView, int width, int height) {
        this.aglView = aglView;
        this.previewWidth = width;
        this.previewHeight = height;
        if (Camera.getNumberOfCameras() > 1) {
            cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
    }

    public AGLCamera(AGLView aglView) {
        this(aglView, 0, 0);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public void open() {
        if (camera == null) {
            camera = Camera.open(cameraId);
            Camera.Parameters parameters = camera.getParameters();

            List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
            int width = 0;
            int height = 0;
            if (sizeList.size() > 1) {
                for (Camera.Size cur : sizeList) {
                    if (cur.width == previewHeight && cur.height == previewWidth) {
                        width = previewHeight;
                        height = previewWidth;
                        break;
                    }
                }
                if (width == 0 || height == 0) {
                    width = sizeList.get(0).height;
                    height = sizeList.get(0).width;
                }
            }

            try {
                parameters.setPreviewSize(width, height);
                if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                }
                camera.setParameters(parameters);
                camera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }


            aglView.setRendererSource(new SourceCamera(aglView.getContext(), this, new SurfaceTexture.OnFrameAvailableListener() {
                @Override
                public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                    aglView.requestRender();
                }
            }));


        }
    }

    public void close() {
        camera.stopPreview();
        camera.release();
        camera = null;
        aglView.clear();

    }

    public void switchCamera() {
        cameraId = (cameraId + 1) % 2;
        close();
        open();
    }


    public void startPreview(SurfaceTexture surfaceTexture) {
        try {
            camera.setPreviewTexture(surfaceTexture);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Camera.Parameters getParameter() {
        return camera.getParameters();
    }


    public int getCameraId() {
        return cameraId;
    }
}
