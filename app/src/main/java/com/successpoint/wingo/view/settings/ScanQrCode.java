package com.successpoint.wingo.view.settings;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.successpoint.wingo.R;
import java.io.IOException;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class ScanQrCode extends Fragment {
    private View view;
    SurfaceView surfaceView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    public int CAMERA_PERMISSION_CAMERA = 444;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_scan_other, container, false);
            surfaceView = view.findViewById(R.id.surfaceView);

            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest
                            .permission
                            .CAMERA)
                    != PackageManager
                    .PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest
                                .permission
                                .CAMERA)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission
                                    .CAMERA},
                            CAMERA_PERMISSION_CAMERA);

                    // CAMERA_PERMISSION_CAMERA is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }


            getActivity()
                    .getPackageManager()
                    .hasSystemFeature
                            (PackageManager
                                    .FEATURE_CAMERA_FLASH);

            barcodeDetector =
                    new BarcodeDetector.Builder(getContext())
                            .setBarcodeFormats(Barcode.ALL_FORMATS)
                            .build();

            cameraSource = new CameraSource
                    .Builder(getContext(), barcodeDetector)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedFps(35.0f)
                    .setRequestedPreviewSize(960, 960)
                    .setAutoFocusEnabled(true)
                    .build();

            //setupButtons();


            surfaceView.getHolder().addCallback(new SurfaceHolder
                    .Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {
                        if (ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            //      TODO: CONSIDER CALLING
                            //ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.


                            return;
                        }
                        cameraSource.start(surfaceView
                                .getHolder());
                    } catch (IOException ie) {
                        Log.e("CAMERA SOURCE", ie
                                .getMessage());
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder,
                                           int format,
                                           int width,
                                           int height) {
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });
            barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
                @Override
                public void release() {
                }

                @Override
                public void receiveDetections(Detector.Detections<Barcode> detections) {
                    final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                    if (barcodes
                            .size() != 0) {
//                        barcodeInfo
//                                .post(new Runnable() {    // Use the post method of the TextView
//                                    public void run() {
//                                        barcodeInfo.setText(barcodes
//                                                .valueAt(0)
//                                                .displayValue
//                                        );
//                                        myRef.setValue(barcodes
//                                                .valueAt(0)
//                                                .displayValue
//                                        );
//                                    }
//                                });
                    }
                }
            });
        }
/*
    public void FLASH_ON()
    {
        cam = Camera.open();
        p = cam.getParameters();
        p.setFlashMode(Camera
                .Parameters
                .FLASH_MODE_TORCH);
        cam.setParameters(p);
        cam.startPreview();
    }

    public void FLASH_OFF()
    {cam.stopPreview();
        cam.release();}

    public void setupButtons()
    {
        ImageButton flash = (ImageButton)findViewById(R.id.flash);
        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (position == false)
                {
                    FLASH_ON();
                    position = true;
                } if (position == true)
                {
                    FLASH_OFF();
                    position = false;
                }
            }
        });
    }*/
return view;
    }
        @Override
        public void onRequestPermissionsResult(int requestCode,
        String permissions[],
        int[] grantResults) {
            switch (requestCode) {
                case 444: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0]
                            == PackageManager
                            .PERMISSION_GRANTED) {



                    } else {
                        if (ContextCompat.checkSelfPermission(getContext(),
                                Manifest
                                        .permission
                                        .CAMERA)
                                != PackageManager
                                .PERMISSION_GRANTED) {

                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                    Manifest
                                            .permission
                                            .CAMERA)) {

                                // Show an explanation to the user *asynchronously* -- don't block
                                // this thread waiting for the user's response! After the user
                                // sees the explanation, try again to request the permission.

                            } else {

                                // No explanation needed, we can request the permission.

                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission
                                                .CAMERA},
                                        CAMERA_PERMISSION_CAMERA);

                                // CAMERA_PERMISSION_CAMERA is an
                                // app-defined int constant. The callback method gets the
                                // result of the request.
                            }
                        }
                    }
                    return;
                }
            }
        }
}