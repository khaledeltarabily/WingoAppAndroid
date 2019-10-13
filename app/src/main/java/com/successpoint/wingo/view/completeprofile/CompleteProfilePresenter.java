package com.successpoint.wingo.view.completeprofile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.successpoint.wingo.model.UserModelObject;
import com.successpoint.wingo.utils.ApiRequest;
import com.successpoint.wingo.utils.Constants;
import com.successpoint.wingo.utils.Mainsharedprefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import static com.successpoint.wingo.utils.CommonMethods.LogMe;

public class CompleteProfilePresenter extends MvpBasePresenter<CompleteProfileView> {
    Context context;
    CompleteProfileView mView;
    public int Image_Gallery_Request = 111;
    ApiRequest apiRequest;

    public CompleteProfilePresenter(Context context, CompleteProfileView mView) {
        this.context = context;
        this.mView = mView;
        apiRequest = ApiRequest.getInstance(context);
    }

    public void selectPhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                File PictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String path = PictureDirectory.getPath();
                Uri data = Uri.parse(path);
                photoIntent.setDataAndType(data, "image/*");
                ((AppCompatActivity)context).startActivityForResult(photoIntent, Image_Gallery_Request);
            } else {
                String[] permissionRequest = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                ((AppCompatActivity)context).requestPermissions(permissionRequest, Image_Gallery_Request);
            }
        } else {
            Intent photoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            File PictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String path = PictureDirectory.getPath();
            Uri data = Uri.parse(path);
            photoIntent.setDataAndType(data, "image/*");
            ((AppCompatActivity)context).startActivityForResult(photoIntent, Image_Gallery_Request);
        }
    }

    public void completeProfile(HashMap<String, String> params,HashMap<String, File> image) {
        Log.e("Done","completeProfile");
        apiRequest.createPostRequest(Constants.RegisterPhone, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<String>() {
            @Override
            public void onSuccess(String response) throws JSONException {
                Log.e("Done","NoErrrrror" + response);
                JSONObject Obb = new JSONObject(response);
                Mainsharedprefs.saveToken(Obb.getString("user_token"));
                uploadImage(image);
            }

            @Override
            public void onFail(ANError error) throws JSONException {
                Log.e("Done","Errrrror"+error.getResponse());
            }
        });
    }

    public void uploadImage(HashMap<String, File> image) {
        HashMap<String,String> params = new HashMap<>();
        params.put("api_token", Constants.api_token);
        params.put("user_token", Mainsharedprefs.getToken());
        params.put("image_index", "1");

        apiRequest.createUploadRequest(Constants.AccountImage, image, params, true, Priority.MEDIUM, new ApiRequest.UploadProgress() {
            @Override
            public void onProgress(int percent) {

            }
        }, new ApiRequest.ServiceCallback<String>() {
            @Override
            public void onSuccess(String response) throws JSONException {
                GetUserDataFromUserToken();
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }
    public void GetUserDataFromUserToken() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_token", Mainsharedprefs.getToken());
        params.put("api_token", Constants.api_token);
        apiRequest.createPostRequest(Constants.UserProfileFromToken, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<String>() {
            @Override
            public void onSuccess(String response) throws JSONException {
                Log.e("Done","response" + response);
                LogMe(response);
                JSONObject result = new JSONObject(response);
                UserModelObject modelObject = new UserModelObject(result.getJSONObject("content"));
                mView.publishUpdateDone(modelObject);
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }

}
