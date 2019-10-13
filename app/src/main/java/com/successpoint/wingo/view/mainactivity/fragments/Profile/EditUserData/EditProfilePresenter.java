package com.successpoint.wingo.view.mainactivity.fragments.Profile.EditUserData;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.facebook.CallbackManager;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.successpoint.wingo.App;
import com.successpoint.wingo.model.TopFansModel;
import com.successpoint.wingo.model.UserModelObject;
import com.successpoint.wingo.utils.ApiRequest;
import com.successpoint.wingo.utils.Constants;
import com.successpoint.wingo.utils.Mainsharedprefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

import static com.successpoint.wingo.utils.CommonMethods.LogMe;

public class EditProfilePresenter extends MvpBasePresenter<EditProfileView> {
    ApiRequest apiRequest;
    Context context;
    EditProfileView mView;
    public int Image_Gallery_Request = 111;
    public EditProfilePresenter(Context context, EditProfileView mView) {
        this.context = context;
        apiRequest = ApiRequest.getInstance(context);
        this.mView = mView;
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

    public void completeProfile(HashMap<String, String> params) {
        apiRequest.createPostRequest(Constants.AccountUpdate, params, Priority.MEDIUM, new ApiRequest.ServiceCallback<String>() {
            @Override
            public void onSuccess(String response) throws JSONException {
                mView.DataEditedCorrectly(true);
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }

    public void uploadImage(HashMap<String, File> image,int index) {
        HashMap<String,String> params = new HashMap<>();
        params.put("api_token", Constants.api_token);
        params.put("user_token", Mainsharedprefs.getToken());
        params.put("image_index", index+"");
        apiRequest.createUploadRequest(Constants.AccountImage, image, params, true, Priority.MEDIUM, new ApiRequest.UploadProgress() {
            @Override
            public void onProgress(int percent) {

            }
        }, new ApiRequest.ServiceCallback<String>() {
            @Override
            public void onSuccess(String response) throws JSONException {
                mView.ImageUploadedCorrectly(true);
            }

            @Override
            public void onFail(ANError error) throws JSONException {

            }
        });
    }

}