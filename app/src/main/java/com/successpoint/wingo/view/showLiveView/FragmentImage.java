package com.successpoint.wingo.view.showLiveView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.UserModelObject;
import com.successpoint.wingo.view.mainactivity.fragments.Profile.AllActivitiesUserList.ShowUserActivities;

import java.io.File;
import java.io.FileInputStream;

import androidx.fragment.app.Fragment;

public class FragmentImage extends Fragment {

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_test, container, false);
            ImageView imageView = view.findViewById(R.id.image);
            Glide.with(getActivity()).load(getArguments().getString("url")).into(imageView);
        }
        return view;
    }
}
