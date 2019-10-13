
package com.successpoint.wingo.utils;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.successpoint.wingo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.successpoint.wingo.utils.CommonMethods.LogMe;

public class ApiRequest {
    private static ApiRequest firstInstance = null;
    private static boolean firstThread = false;
    final OkHttpClient okHttpClient;
    private AlertDialog waitingAlertDialog;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private Context context;

    public ApiRequest(Context context) {
        this.context = context;
        okHttpClient = getUnsafeOkHttpClient();
        AndroidNetworking.initialize(context, okHttpClient);
    }

    public static synchronized ApiRequest getInstance(Context context) {
        if (firstInstance == null) {
            firstInstance = new ApiRequest(context);
        }
        return firstInstance;
    }

    /**
     * @param url      constant url of service
     * @param model    returned model
     * @param priority priority of request
     * @param callback callback of service if you removed the model replace it with string
     */
    public void createGetRequest(String url, Class model, Priority priority, final ServiceCallback callback) {
        CommonMethods.showLoadingWithRequest(context, this, R.layout.custom_loading_popup);

        if (url != null) Log.i("url", url);
        if (model != null) Log.i("params", model.toString());
        AndroidNetworking.get(url)
                .setPriority(priority)
                .build()
                .getAsObject(model, new ParsedRequestListener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            CommonMethods.endLoadingWithRequest(ApiRequest.this);
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        try {
                            CommonMethods.endLoadingWithRequest(ApiRequest.this);
                            callback.onFail(anError);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * @param url      constant url of service
     * @param priority priority of request
     * @param callback callback of service if you removed the model replace it with string
     */
    public void createGetRequest(String url, Priority priority, final ServiceCallback callback) {

        if (url != null) Log.i("url", url);
        CommonMethods.showLoadingWithRequest(context, this, R.layout.custom_loading_popup);
        AndroidNetworking.get(url)
                .setPriority(priority)
                .build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                try {
                    CommonMethods.endLoadingWithRequest(ApiRequest.this);
                    callback.onSuccess(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                try {
                    CommonMethods.endLoadingWithRequest(ApiRequest.this);
                    callback.onFail(anError);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * @param url      constant url of service
     * @param resModel response model
     * @param model    parameters of your request
     * @param priority priority of request
     * @param callback callback of service if you removed the model replace it with string
     */
    public void createPostRequest(String url, Class resModel, HashMap<String, String> model, Priority priority, final ServiceCallback callback) {

        if (url != null) Log.i("url", url);
        if (model != null) Log.i("params", model.toString());
//        CommonMethods.showLoadingWithRequest(context, this, R.layout.custom_loading_popup);
        AndroidNetworking.post(url)
                .addBodyParameter(model)
                .setPriority(priority)
                .build()
                .getAsObject(resModel, new ParsedRequestListener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            LogMe(response.toString());
//                            CommonMethods.endLoadingWithRequest(ApiRequest.this);
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        try {
                            LogMe(anError.getErrorDetail());
                            CommonMethods.endLoadingWithRequest(ApiRequest.this);
                            callback.onFail(anError);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * @param url      constant url of service
     * @param resModel response model
     * @param model    parameters of your request
     * @param priority priority of request
     * @param callback callback of service if you removed the model replace it with string
     */
    public void createPostRequest(String url, Class resModel, HashMap<String, String> model, Priority priority, boolean isLoading, final ServiceCallback callback) {

        if (url != null) Log.i("url", url);
        if (model != null) Log.i("params", model.toString());
        if (isLoading)
            CommonMethods.showLoadingWithRequest(context, this, R.layout.custom_loading_popup);
        AndroidNetworking.post(url)
                .addBodyParameter(model)
                .setPriority(priority)
                .build()
                .getAsObject(resModel, new ParsedRequestListener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            if (isLoading)
                                CommonMethods.endLoadingWithRequest(ApiRequest.this);
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        try {
                            if (isLoading)
                                CommonMethods.endLoadingWithRequest(ApiRequest.this);
                            callback.onFail(anError);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * @param url      constant url of service
     * @param params   parameters of your request
     * @param priority priority of request
     * @param callback callback of service if you removed the model replace it with string
     */
    public void createPostRequest(String url, HashMap<String, String> params, Priority priority, final ServiceCallback callback) {

        if (url != null) LogMe(url);
        if (params != null) LogMe(params.toString());
        CommonMethods.showLoadingWithRequest(context, this, R.layout.custom_loading_popup);
        AndroidNetworking.post(url)
                .addBodyParameter(params)
                .setPriority(priority)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            LogMe(response);
                            CommonMethods.endLoadingWithRequest(ApiRequest.this);
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        try {
                            LogMe("Error : " + anError);
                            CommonMethods.endLoadingWithRequest(ApiRequest.this);
                            callback.onFail(anError);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * @param url      constant url of service
     * @param params   parameters of your request
     * @param priority priority of request
     * @param callback callback of service if you removed the model replace it with string
     */
    public void createPostRequest(String url, Priority priority, HashMap<String, Object> params, final ServiceCallback callback) {
        if (url != null) Log.i("url", url);
        if (params != null) Log.i("params", params.toString());
        CommonMethods.showLoadingWithRequest(context, this, R.layout.custom_loading_popup);
        AndroidNetworking.post(url)
                .addBodyParameter(params)
                .setPriority(priority)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            CommonMethods.endLoadingWithRequest(ApiRequest.this);
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        try {
                            CommonMethods.endLoadingWithRequest(ApiRequest.this);
                            callback.onFail(anError);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * @param url      constant url of service
     * @param object   object of Body Request
     * @param callback callback of service if you removed the model replace it with string
     */
    public void createPostRequest(String url, Object object, final ServiceCallback callback) {

        if (url != null) Log.i("url", url);
        if (object != null) Log.i("params", object.toString());
        CommonMethods.showLoadingWithRequest(context, this, R.layout.custom_loading_popup);
        AndroidNetworking.post(url)
                .addJSONObjectBody((JSONObject) object)
                .setContentType("application/json")
                .setPriority(Priority.HIGH)
                .addHeaders("Content-Type", "application/json")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            CommonMethods.endLoadingWithRequest(ApiRequest.this);
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        try {
                            CommonMethods.endLoadingWithRequest(ApiRequest.this);
                            callback.onFail(anError);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * @param url         constant url of service
     * @param params      object of Body Request
     * @param showLoading show progress dialog
     * @param callback    callback of service if you removed the model replace it with string
     */
    public void createPostRequest(String url, HashMap<String, String> params, Priority priority, boolean showLoading, final ServiceCallback callback) {

        if (url != null) Log.i("url", url);
        if (params != null) Log.i("params", params.toString());
        if (showLoading) {
            CommonMethods.showLoadingWithRequest(context, this, R.layout.custom_loading_popup);
        }
        AndroidNetworking.post(url)
                .addBodyParameter(params)
                .setPriority(priority)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (showLoading)
                                CommonMethods.endLoadingWithRequest(ApiRequest.this);
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        try {
                            if (showLoading)
                                CommonMethods.endLoadingWithRequest(ApiRequest.this);
                            callback.onFail(anError);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * @param url      constant url of service
     * @param model    parameters of your request
     * @param files    files which you want to upload
     * @param params   parameters of your request
     * @param priority priority of request
     * @param progress progress on the upload
     * @param callback callback of service if you removed the model replace it with string
     */
    public void createUploadRequest(String url, Class model, HashMap<String, File> files, HashMap<String, String> params, Priority priority, final UploadProgress progress, final ServiceCallback callback) {
        CommonMethods.showLoadingWithRequest(context, this, R.layout.custom_loading_popup);
        AndroidNetworking.upload(url)
                .addMultipartFile(files)
                .addMultipartParameter(params)
                .setPriority(priority)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                        progress.onProgress((int) ((bytesUploaded / totalBytes) * 100));
                    }
                }).getAsObject(model, new ParsedRequestListener() {
            @Override
            public void onResponse(Object response) {
                try {
                    CommonMethods.endLoadingWithRequest(ApiRequest.this);
                    callback.onSuccess(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                try {
                    CommonMethods.endLoadingWithRequest(ApiRequest.this);
                    callback.onFail(anError);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * @param url      constant url of service
     * @param files    files which you want to upload
     * @param params   parameters of your request
     * @param priority priority of request
     * @param progress progress on the upload
     * @param callback callback of service if you removed the model replace it with string
     */
    public void createUploadRequest(String url, HashMap<String, File> files, HashMap<String, String> params, boolean showLoading, Priority priority, final UploadProgress progress, final ServiceCallback callback) {

        if (url != null) Log.i("url", url);
        if (params != null) Log.i("params", params.toString());
        if (showLoading) {
            CommonMethods.showLoadingWithRequest(context, this, R.layout.custom_loading_popup);
        }
        AndroidNetworking.upload(url)
                .addMultipartFile(files)
                .addMultipartParameter(params)
                .setPriority(priority)
                .build()
                .setUploadProgressListener((bytesUploaded, totalBytes) -> {
                    // do anything with progress
                    Log.i("started", bytesUploaded + "--" + totalBytes);
                    progress.onProgress((int) (((float) bytesUploaded / (float) totalBytes) * 100));
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (showLoading)
                                CommonMethods.endLoadingWithRequest(ApiRequest.this);
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        try {
                            if (showLoading)
                                CommonMethods.endLoadingWithRequest(ApiRequest.this);
                            callback.onFail(anError);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);
            builder.addInterceptor(chain -> {
                Request newRequest;
                newRequest = chain.request().newBuilder()
//                .addHeader("Accept-Language", language)
                        .addHeader("accept-encoding", "utf-8")
                        .addHeader("device-type", "android")
                        .addHeader("Content-Type", "application/json")
                        .build();

                return chain.proceed(newRequest);
            });
            builder.connectTimeout(120, TimeUnit.SECONDS);
            builder.readTimeout(120, TimeUnit.SECONDS);
            builder.writeTimeout(120, TimeUnit.SECONDS);
            builder.hostnameVerifier((hostname, session) -> true);
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public interface ServiceCallback<T> {
        void onSuccess(T response) throws JSONException;

        void onFail(ANError error) throws JSONException;
    }

    public interface UploadProgress {
        void onProgress(int percent);
    }

    public AlertDialog getWaitingAlertDialog() {
        return waitingAlertDialog;
    }

    public void setWaitingAlertDialog(AlertDialog waitingAlertDialog) {
        this.waitingAlertDialog = waitingAlertDialog;
    }
}
