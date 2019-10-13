package com.successpoint.wingo.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.successpoint.wingo.App;
import com.successpoint.wingo.R;

public class CommonMethods {

    public static void showLoadingWithRequest(Context context, ApiRequest apiRequest, int popupResource) {
        View view;
        AlertDialog.Builder builder = new AlertDialog.Builder(apiRequest.getContext());
        view = LayoutInflater.from(context).inflate(popupResource, null);
        builder.setView(view);
        builder.setCancelable(false);

        if (apiRequest.getWaitingAlertDialog() == null) {
//        if(!((Activity) context).isFinishing()) {
//            apiRequest.setContext(context);
//        }
//            gifView1.pause();
//            gifView1.setGifResource(R.mipmap.gif1);
//            gifView1.getGifResource();

//            LottieAnimationView lottiLoading;
//            lottiLoading = view.findViewById(R.id.lottiLoading);

            apiRequest.setWaitingAlertDialog(builder.create());

//            lottiLoading.animate();
            apiRequest.getWaitingAlertDialog().getWindow().setBackgroundDrawable(
                    new ColorDrawable(context.getResources().getColor(R.color.colorTransparent)));
            if (!((Activity) context).isDestroyed()) {
                apiRequest.getWaitingAlertDialog().show();
            }
        } else if (!apiRequest.getWaitingAlertDialog().isShowing()) {
            apiRequest.setWaitingAlertDialog(builder.create());

//            lottiLoading.animate();
            apiRequest.getWaitingAlertDialog().getWindow().setBackgroundDrawable(
                    new ColorDrawable(context.getResources().getColor(R.color.colorTransparent)));
            if (!((Activity) context).isDestroyed()) {
                apiRequest.getWaitingAlertDialog().show();
            }
        }
    }

    public static void endLoadingWithRequest(ApiRequest apiRequest) {
        if (apiRequest.getWaitingAlertDialog() != null && apiRequest.getWaitingAlertDialog().isShowing()) {
            apiRequest.getWaitingAlertDialog().dismiss();
            apiRequest.setWaitingAlertDialog(null);
        }
    }

    public static void LogMe(String msg){
        Log.i("Log me :",msg);
    }
    
    public static boolean validateInputs(EditText[] inputs){
        for (EditText i :
                inputs) {
            if (i != null && i.getText().toString().trim().length() == 0) {
                i.setError(App.getAppContext().getString(R.string.required));
                i.requestFocus();
                return false;
            }
        }
        return true;
    }

    public static AlertDialog createCustomDialog(Context context, int view) {
        AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertStyle))
                .setView(view)
                .create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        return alertDialog;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getActualPath(final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(App.getAppContext(), uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(App.getAppContext(), contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(App.getAppContext(), contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(App.getAppContext(), uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
}
