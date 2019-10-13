package com.successpoint.wingo.view.showLiveView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.successpoint.wingo.App;
import com.successpoint.wingo.R;

import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.bumptech.glide.Glide;
import com.successpoint.wingo.utils.ApiRequest;
import com.successpoint.wingo.utils.CommonMethods;
import com.successpoint.wingo.utils.Constants;
import com.successpoint.wingo.utils.Mainsharedprefs;
import com.successpoint.wingo.view.LiveData.TestDetailFragment;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            // create the surface and start camera preview
            if (mCamera == null) {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }
        } catch (IOException e) {
            Log.d(VIEW_LOG_TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void refreshCamera(Camera camera) {
        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        setCamera(camera);
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(VIEW_LOG_TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        refreshCamera(mCamera);
    }

    public void setCamera(Camera camera) {
        //method to set a camera instance
        mCamera = camera;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        // mCamera.release();

    }
}
public class GoLiveView extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private Camera.PictureCallback mPicture;
    //    private Button capture, switchCamera;
    private Context myContext;
    private LinearLayout cameraPreview;
    private boolean cameraFront = false;
    public static Bitmap bitmap;




    private int findFrontFacingCamera() {

        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;

    }

   /* private int findBackFacingCamera() {
        int cameraId = -1;
        //Search for the back facing camera
        //get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        //for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;

            }

        }
        return cameraId;
    }
*/

    public void onResume() {

        super.onResume();

//        chooseCamera();

        if(mCamera == null) {
            mCamera = Camera.open();
            mCamera.setDisplayOrientation(90);
            //          mPicture = getPictureCallback();
            mPreview.refreshCamera(mCamera);
            Log.d("nu", "null");
        }else {

            //Toast.makeText(myContext, "camera used by another application", Toast.LENGTH_SHORT).show();
            Log.d("nu","no null");
        }

    }

    public void chooseCamera() {

        int cameraId = findFrontFacingCamera();
        if (cameraId >= 0) {
            //open the backFacingCamera
            //set a picture callback
            //refresh the preview
            mCamera = Camera.open(cameraId);
            mCamera.setDisplayOrientation(90);
            // mPicture = getPictureCallback();
            mPreview.refreshCamera(mCamera);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //when on Pause, release camera in order to be used from other applications
        releaseCamera();
    }

    private void releaseCamera() {
        // stop and release camera
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }


    ImageView image_cover,clos_btn;
    Button add_cover_btn;
    EditText title;
    public int Image_Gallery_Request = 111;
    ImageView share_whatsapp,share_facebook,share_twitter;
    Button btn_done;
    private Uri photoUri;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.go_live);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        myContext = this;

        mCamera =  Camera.open();
        mCamera.setDisplayOrientation(90);
        cameraPreview = (LinearLayout) findViewById(R.id.cPreview);
        mPreview = new CameraPreview(myContext, mCamera);
        cameraPreview.addView(mPreview);

        int camerasNumber = Camera.getNumberOfCameras();
        if (camerasNumber > 1) {
            //release the old camera instance
            //switch camera, from the front and the back and vice versa

            releaseCamera();
            chooseCamera();
        }
        else {

        }
        mCamera.startPreview();



        btn_done = findViewById(R.id.btn_done);
        image_cover = findViewById(R.id.image_cover);
        clos_btn = findViewById(R.id.clos_btn);
        add_cover_btn = findViewById(R.id.add_cover_btn);
        title = findViewById(R.id.title);
        share_whatsapp = findViewById(R.id.share_whatsapp);
        share_facebook = findViewById(R.id.share_facebook);
        share_twitter = findViewById(R.id.share_twitter);


        image_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhoto();
            }
        });
        share_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        share_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        share_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        clos_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                Log.e("Done","CLICKED");
                HashMap<String, String> params = new HashMap<>();
                params.put("api_token", Constants.api_token);
                params.put("user_token", Mainsharedprefs.getToken());
                params.put("stream_id", App.userModelObject_of_Project.getUser_id());
                params.put("stream_description", title.getText().toString());
                params.put("lat", "31.1");
                params.put("lon", "30.2");
                Log.e("LIVEDATADONE", Constants.api_token);
                Log.e("LIVEDATADONE", Mainsharedprefs.getToken());
                Log.e("LIVEDATADONE", App.userModelObject_of_Project.getUser_id());
                Log.e("LIVEDATADONE", title.getText().toString());
                if (photoUri != null) {
                    HashMap<String, File> image = new HashMap<>();
                    image.put("cover", new File(CommonMethods.getActualPath(photoUri)));
                    completeProfile(params,image);
                }
                else {
                    Toast.makeText(GoLiveView.this , "GoLiveView",Toast.LENGTH_LONG).show();
                }


            }
        });

    }
    public void selectPhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                File PictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String path = PictureDirectory.getPath();
                Uri data = Uri.parse(path);
                photoIntent.setDataAndType(data, "image/*");
                startActivityForResult(photoIntent, Image_Gallery_Request);
            } else {
                String[] permissionRequest = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissionRequest, Image_Gallery_Request);
            }
        } else {
            Intent photoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            File PictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String path = PictureDirectory.getPath();
            Uri data = Uri.parse(path);
            photoIntent.setDataAndType(data, "image/*");
            startActivityForResult(photoIntent, Image_Gallery_Request);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Image_Gallery_Request) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectPhoto();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Gallery_Request) {
            if (resultCode == RESULT_OK) {
                photoUri = data.getData();
                Glide.with(GoLiveView.this)
                        .load(photoUri)
                        .into(image_cover);
            }
        }
    }
        public void completeProfile(HashMap<String, String> params,HashMap<String, File> image) {
            ApiRequest apiRequest = ApiRequest.getInstance(this);
            Log.e("Done","completeProfile");
            apiRequest.createPostRequest(Constants.StreamLiveStart, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<String>() {
                @Override
                public void onSuccess(String response) throws JSONException {
                    uploadImage(image);
                }

                @Override
                public void onFail(ANError error) throws JSONException {
                    Log.e("Done","Errrrror"+error.toString());
                }
            });
    }

    public void uploadImage(HashMap<String, File> image) {
        Log.e("Data","IMAGE");
        HashMap<String,String> params = new HashMap<>();
        params.put("api_token", Constants.api_token);
        params.put("user_token", Mainsharedprefs.getToken());
        ApiRequest apiRequest = ApiRequest.getInstance(this);
        apiRequest.createUploadRequest(Constants.StreamLiveCover, image, params, true, Priority.MEDIUM, new ApiRequest.UploadProgress() {
            @Override
            public void onProgress(int percent) {

            }
        }, new ApiRequest.ServiceCallback<String>() {
            @Override
            public void onSuccess(String response) throws JSONException {
                Resources res = getResources();

                //Load XML TESTS
                TestContent.LoadTests(res.openRawResource(R.raw.tests));

                TestContent.SetTestItem( 1 );

                Intent intent = new Intent(getApplicationContext(),GoLiveActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(TestDetailFragment.ARG_ITEM_ID, 1+"");
                intent.putExtras(bundle);
                startActivity(intent);

                Toast.makeText(GoLiveView.this,"Started",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }
}
