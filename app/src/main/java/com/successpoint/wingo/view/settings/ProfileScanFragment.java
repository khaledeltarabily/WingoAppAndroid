package com.successpoint.wingo.view.settings;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.successpoint.wingo.App;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.SocialLoginModel;
import com.successpoint.wingo.model.UserModelObject;
import com.successpoint.wingo.utils.ApiRequest;
import com.successpoint.wingo.utils.Constants;
import com.successpoint.wingo.view.mainactivity.fragments.Profile.AllActivitiesUserList.ShowUserActivities;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;

import androidx.fragment.app.Fragment;
import retrofit2.Call;

public class ProfileScanFragment extends Fragment {
    private View view;
    private ImageView user_image;
    private TextView user_name, user_id, wingo_id, gawhra, foshia_icon, fansCount, followingCount;
    ImageView scanImage;
    LinearLayout fans_container, following_container;
    ImageView  facebook, twitter, instgram;
    UserModelObject userModelObject;


    CallbackManager callbackManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_scan_meprofile, container, false);





            fans_container = view.findViewById(R.id.fans_container);
            following_container = view.findViewById(R.id.following_container);
            user_image = view.findViewById(R.id.user_image);
//            twitter = view.findViewById(R.id.twitter);
            facebook = view.findViewById(R.id.face);
            instgram = view.findViewById(R.id.instgram);
            user_id = view.findViewById(R.id.user_id);
            wingo_id = view.findViewById(R.id.wingo_id);
            fansCount = view.findViewById(R.id.fansCount);
            followingCount = view.findViewById(R.id.followingCount);
            userModelObject = App.userModelObject_of_Project;
            Glide.with(getContext()).load(userModelObject.getImages().get(0)).into(user_image);
            Glide.with(getContext()).load(userModelObject.getQr()).into(scanImage);

            user_name.setText(userModelObject.getName());
            user_id.setText(userModelObject.getUser_id());
            wingo_id.setText(userModelObject.getElite_id());
            fansCount.setText(userModelObject.getFans() + "");
            followingCount.setText(userModelObject.getFollowing() + "");

            fans_container.setOnClickListener(view -> {
                Intent intent = new Intent(getContext(), ShowUserActivities.class);
                intent.putExtra("user_id", userModelObject.getUser_id());
                intent.putExtra("type","fans");
                startActivity(intent);
            });
            following_container.setOnClickListener(view -> {
                Intent intent = new Intent(getContext(), ShowUserActivities.class);
                intent.putExtra("user_id", userModelObject.getUser_id());
                intent.putExtra("type","following");

                startActivity(intent);
            });


            facebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FacebookShare();
                }
            });
            twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareTwitter();
                }
            });
            instgram.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createInstagramIntent();
                }
            });

        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void shareTwitter(){

        try {

            FileInputStream fis;
            Bitmap bitmap = ((BitmapDrawable)scanImage.getDrawable()).getBitmap();
            String result = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "", "");
            File file = new File(result);
            if(file.exists()){
                Log.i("FILE", "YES");
            }else{
                Log.i("FILE", "NO");
            }
            Uri uri = Uri.parse(file.getAbsolutePath());
            //Uri uri = Uri.parse("android.resource://com.gobaby.app/drawable/back");
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("/*");
            intent.setClassName("com.twitter.android", "com.twitter.android.PostActivity");
            intent.putExtra(Intent.EXTRA_TEXT, "Thiws is a share message");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(intent);

        } catch (final ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "You don't seem to have twitter installed on this device", Toast.LENGTH_SHORT).show();
        }
    }
    private void createInstagramIntent(){
        Bitmap bitmap = ((BitmapDrawable)scanImage.getDrawable()).getBitmap();
        String result = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "", "");

        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType("image/*");

        // Create the URI from the media
        File media = new File(result);
        Uri uri = Uri.fromFile(media);

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);

        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));
    }
    void FacebookShare(){
        Bitmap bitmap = ((BitmapDrawable)scanImage.getDrawable()).getBitmap();
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
//        ShareButton shareButton = (ShareButton)view.findViewById(R.id.fb_share_button);
//        shareButton.setShareContent(content);
//        callbackManager = CallbackManager.Factory.create();
//        ShareDialog shareDialog = new ShareDialog(this);
//        if (ShareDialog.canShow(ShareLinkContent.class)) {
//            ShareLinkContent linkContent = new ShareLinkContent.Builder()
//                    .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
//                    .build();
//            shareDialog.show(linkContent);
//        }
    }
}
