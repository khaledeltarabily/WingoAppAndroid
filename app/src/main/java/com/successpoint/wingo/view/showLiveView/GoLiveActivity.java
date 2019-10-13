package com.successpoint.wingo.view.showLiveView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.red5pro.streaming.R5Connection;
import com.red5pro.streaming.R5Stream;
import com.red5pro.streaming.R5StreamProtocol;
import com.red5pro.streaming.config.R5Configuration;
import com.red5pro.streaming.event.R5ConnectionEvent;
import com.red5pro.streaming.event.R5ConnectionListener;
import com.red5pro.streaming.source.R5Camera;
import com.red5pro.streaming.source.R5Microphone;
import com.red5pro.streaming.view.R5VideoView;
import com.successpoint.wingo.App;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.CommentModel;
import com.successpoint.wingo.model.GiftModel;
import com.successpoint.wingo.model.GlobalGiftModel;
import com.successpoint.wingo.model.UserModelObject;
import com.successpoint.wingo.view.mainactivity.fragments.Profile.AllActivitiesUserList.ShowUserActivities;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class GoLiveActivity extends MvpActivity<ShowLiveView, ShowLivePresenter> implements ShowLiveView , R5ConnectionListener {
    protected R5VideoView preview;
    protected R5Stream publish;
    protected Camera cam;
    protected R5Camera camera;
    protected int camOrientation;

    protected PublishTestListener publishTestListener;

    public GoLiveActivity(){

    }

    @Override
    public void onConnectionEvent(R5ConnectionEvent event) {
        Log.d("Publisher", ":onConnectionEvent " + event.name() + " - " + event.message);
        if (event.name() == R5ConnectionEvent.LICENSE_ERROR.name()) {
            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    AlertDialog alertDialog = new AlertDialog.Builder(GoLiveActivity.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("License is Invalid");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK",
                            (dialog, which) -> dialog.dismiss()

                    );
                    alertDialog.show();
                }
            });
        }
        else if (event.name() == R5ConnectionEvent.START_STREAMING.name()){
//            publish.setFrameListener(new R5FrameListener() {
//                @Override
//                public void onBytesReceived(byte[] bytes, int i, int i1) {
//                    Uncomment for framelistener performance test
//                }
//            });
        }
        else if (event.name() == R5ConnectionEvent.BUFFER_FLUSH_START.name()) {
            if (publishTestListener != null) {
                publishTestListener.onPublishFlushBufferStart();
            }
        }
        else if (event.name() == R5ConnectionEvent.BUFFER_FLUSH_EMPTY.name() ||
                event.name() == R5ConnectionEvent.DISCONNECTED.name()) {
            if (publishTestListener != null) {
                publishTestListener.onPublishFlushBufferComplete();;
                publishTestListener = null;
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
    }


    protected void publish(){
        String b = getPackageName();

        //Create the configuration from the values.xml
        R5Configuration config = new R5Configuration(R5StreamProtocol.RTSP,
                TestContent.GetPropertyString("host"),
                TestContent.GetPropertyInt("port"),
                TestContent.GetPropertyString("context"),
                TestContent.GetPropertyFloat("publish_buffer_time"));
        config.setLicenseKey(TestContent.GetPropertyString("license_key"));
        config.setBundleID(b);

        R5Connection connection = new R5Connection(config);

        //setup a new stream using the connection
        publish = new R5Stream(connection);

        publish.audioController.sampleRate =  TestContent.GetPropertyInt("sample_rate");

        //show all logging
        publish.setLogLevel(R5Stream.LOG_LEVEL_DEBUG);

        if(TestContent.GetPropertyBool("audio_on")) {
            //attach a microphone
            attachMic();
        }

        preview.attachStream(publish);

        if(TestContent.GetPropertyBool("video_on")) {
            //attach a camera video source
            attachCamera();
        }

        preview.showDebugView(TestContent.GetPropertyBool("debug_view"));

        publish.setListener(GoLiveActivity.this);
        publish.publish(TestContent.GetPropertyString("stream1"), getPublishRecordType());

        if(TestContent.GetPropertyBool("video_on")) {
            cam.startPreview();
        }

    }

    protected void attachCamera(){
        cam = openFrontFacingCameraGingerbread();
        cam.setDisplayOrientation((camOrientation + 180) % 360);

        camera = new R5Camera(cam, TestContent.GetPropertyInt("camera_width"), TestContent.GetPropertyInt("camera_height"));
        camera.setBitrate(TestContent.GetPropertyInt("bitrate"));
        camera.setOrientation(camOrientation);
        camera.setFramerate(TestContent.GetPropertyInt("fps"));
        publish.attachCamera(camera);
    }

    protected void attachMic(){
        R5Microphone mic = new R5Microphone();
        publish.attachMic(mic);
    }

    protected Camera openFrontFacingCameraGingerbread() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
                    camOrientation = cameraInfo.orientation;
                    applyDeviceRotation();
                    break;
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }

        return cam;
    }

    protected void applyDeviceRotation(){
        int rotation = GoLiveActivity.this.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 270; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 90; break;
        }

        Rect screenSize = new Rect();
        GoLiveActivity.this.getWindowManager().getDefaultDisplay().getRectSize(screenSize);
        float screenAR = (screenSize.width()*1.0f) / (screenSize.height()*1.0f);
        if( (screenAR > 1 && degrees%180 == 0) || (screenAR < 1 && degrees%180 > 0) )
            degrees += 180;

        System.out.println("Apply Device Rotation: " + rotation + ", degrees: " + degrees);

        camOrientation += degrees;

        camOrientation = camOrientation%360;
    }

    public void stopPublish(PublishTestListener listener) {

        publishTestListener = listener;
        if (publish != null) {
            publish.stop();

            if(publish.getVideoSource() != null) {
                Camera c = ((R5Camera) publish.getVideoSource()).getCamera();
                c.stopPreview();
                c.release();
            }
            publish = null;
        }

    }

    public Boolean isPublisherTest () {
        return false;
    }
    public Boolean shouldClean() {
        return true;
    }
    public R5Stream.RecordType getPublishRecordType () {
        String type = TestContent.GetPropertyString("record_mode");
        if (type.equals("Record")) {
            return R5Stream.RecordType.Record;
        } else if (type.equals("Append")) {
            return R5Stream.RecordType.Append;
        }
        return R5Stream.RecordType.Live;
    }



    TextView user_name,user_id,user_level,Diamonds,text_write_comment;
    Button follow_btn;
    ImageView user_image,show_all_views,close,Guests,Share,Gifts;
    RecyclerView all_viewers_recycler,Comments_recycler;
    EditText write_comment;
    LinearLayout write_comment_container;
    ImageView fly_comment_action,AddComment;
    String Live_Id = App.userModelObject_of_Project.getUser_id();

    UserModelObject live_user_data;


    ArrayList<UserModelObject> all_viewers_recycler_users;
    ArrayList<CommentModel> all_comments_recycler;


    ArrayList<GiftModel> arr_gifts = new ArrayList<>();
    int  SELECTED_GIFT_POSITION = 0;

    Dialog dialog_gift ;
    Dialog dialog_topfans;
    Dialog dialog_guests ;
    Dialog dialog_user_bottom;


    ArrayList<UserModelObject> user_guests_data_list ;
    ArrayList<UserModelObject> user_waiting_guests_data_list ;
    ArrayList<UserModelObject> top_fans_list_today ;
    ArrayList<UserModelObject> top_fans_list ;
    ArrayList<String> users_guests_IDs = new ArrayList<>();
    int width = 0;
    ArrayList<String> users_waiting_guests_IDs = new ArrayList<>();
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.live_chat);
            all_comments_recycler = new ArrayList<>();
            all_viewers_recycler_users = new ArrayList<>();
            Log.e("DATATA","STARTED");
            dialog_gift = new Dialog(GoLiveActivity.this,R.style.Theme_Dialog);
            dialog_topfans = new Dialog(GoLiveActivity.this,R.style.Theme_Dialog);
            dialog_guests = new Dialog(GoLiveActivity.this,R.style.Theme_Dialog);
            dialog_user_bottom = new Dialog(GoLiveActivity.this,R.style.Theme_Dialog);

            preview = (R5VideoView)findViewById(R.id.videoPreview);

            publish();

            Display display = GoLiveActivity.this.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
         width = size.x;
        top_fans_list_today = new ArrayList<>();
        top_fans_list = new ArrayList<>();
        user_guests_data_list = new ArrayList<>();
        user_waiting_guests_data_list = new ArrayList<>();
        live_user_data = new UserModelObject();
        user_id = findViewById(R.id.user_id);
        user_name = findViewById(R.id.user_name);
        user_level = findViewById(R.id.user_level);
        Diamonds = findViewById(R.id.wings);
        text_write_comment = findViewById(R.id.text_write_comment);
        user_image = findViewById(R.id.user_image);
        follow_btn = findViewById(R.id.follow_btn);
        show_all_views = findViewById(R.id.show_all_views);
        close = findViewById(R.id.close);
        Guests = findViewById(R.id.Guests);
        Share = findViewById(R.id.Share);
        Gifts = findViewById(R.id.Gifts);
        all_viewers_recycler = findViewById(R.id.all_viewers_recycler);
        Comments_recycler = findViewById(R.id.Comments_recycler);
        write_comment = findViewById(R.id.write_comment);
        write_comment_container = findViewById(R.id.write_comment_container);
        fly_comment_action = findViewById(R.id.fly_comment_action);
        AddComment = findViewById(R.id.AddComment);

        Guests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllGuestsData();
            }
        });
            live_user_data = App.userModelObject_of_Project;
            if (live_user_data.getImages().size()>0)
                Glide.with(GoLiveActivity.this).load(live_user_data.getImages().get(0)).into(user_image);
        user_name.setText(live_user_data.getName());
        user_id.setText(live_user_data.getUser_id());
        Diamonds.setText(live_user_data.getDiamonds()+"");

        show_all_views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               AllViewersDialogData();
            }
        });
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDataDialogFromBottom(App.userModelObject_of_Project.getUser_id());
            }
        });
        presenter.RetrievedDeletedWaitingGuests(Live_Id,null);
        presenter.RetrievedDeletedGuests(Live_Id,null);
        presenter.RetrieveGift(Live_Id);
//        presenter.RetrieveGlobalGift(Live_Id);
            presenter.RetrieveGlobalAllGift();



            ShowAllViewersAdapter adapter = new ShowAllViewersAdapter(GoLiveActivity.this, all_viewers_recycler_users, new ShowAllViewersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(UserModelObject item) {
                UserDataDialogFromBottom(item.getUser_id());
            }
        });
            GridLayoutManager gridLayoutManager = new GridLayoutManager(GoLiveActivity.this,3, RecyclerView.VERTICAL,false);
            all_viewers_recycler.setLayoutManager(gridLayoutManager);
            all_viewers_recycler.setAdapter(adapter);

        text_write_comment.setOnClickListener(view -> write_comment_container.setVisibility(View.VISIBLE));
            findViewById(R.id.Comment).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    write_comment_container.setVisibility(View.GONE);
                }
            });
        AddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.AddComment(Live_Id,write_comment.getText().toString(),false);
            }
        });
        Share.setOnClickListener(view -> {
            String shareBody = "Iam Waiting You at Wingo :  "+Live_Id;
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share"));
        });

        CommentsLiveAdapter adapterss = new CommentsLiveAdapter(GoLiveActivity.this, all_comments_recycler);
        Comments_recycler.setAdapter(adapterss);
            LinearLayoutManager gridLayoutManagersss = new LinearLayoutManager(GoLiveActivity.this, RecyclerView.VERTICAL,false);
        Comments_recycler.setLayoutManager(gridLayoutManagersss);


        Gifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GiftDialog();
            }
        });
        Diamonds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllTopFansData();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.SetCloseToServerView(Live_Id);
            }
        });
        presenter. SetSeenToServerView(Live_Id);
        presenter.GetAllCurrentViewers(Live_Id);
        presenter.GetLastComments(Live_Id);
        }


    @NonNull
    @Override
    public ShowLivePresenter createPresenter() {
        return new ShowLivePresenter(GoLiveActivity.this, GoLiveActivity.this);
    }


    @Override
    public void RetrievedTopFans(ArrayList<UserModelObject> data) {
        Log.e("DATATA","HHHHEREREREFANS");
        top_fans_list = data;
        updateAllTopFansData(data);

    }

    @Override
    public void RetrievedTodayTopFans(ArrayList<UserModelObject> data) {
            Log.e("DATATA","HHHHERERERE");
        top_fans_list_today = data;
        updateAllTodayTopFansData(data);
    }

    @Override
    public void RetrievedGuests(ArrayList<UserModelObject> data) {
        for (int i = 0 ; i < data.size() ; i ++)
            users_guests_IDs.add(data.get(i).getUser_id());
        user_guests_data_list.addAll(data);
        updateAllGuestsData();
    }

    @Override
    public void RetrievedWaitingGuests(ArrayList<UserModelObject> data) {
        for (int i = 0 ; i < data.size() ; i ++)
            users_waiting_guests_IDs.add(data.get(i).getUser_id());
        user_waiting_guests_data_list.addAll(data);
        updateAllWaitingGuestsData();
    }

    @Override
    public void RetrievedDeletedGuests(UserModelObject data) {
        users_guests_IDs.remove(data.getUser_id());
        user_guests_data_list.remove(data);
        updateAllGuestsData();
    }

    @Override
    public void RetrievedDeletedWaitingGuests(UserModelObject data) {
        users_waiting_guests_IDs.remove(data.getUser_id());
        user_waiting_guests_data_list.remove(data);
        updateAllWaitingGuestsData();
    }

    @Override
    public void GetGifts(ArrayList<GiftModel> model) {
            arr_gifts = model;
//            GiftDialog();
    }

    @Override
    public void GetClickedUserData(UserModelObject response) {
        UpdateUserDataDialogFromBottom(response);
    }

    @Override
    public void GetAllCurrentViewers(ArrayList<UserModelObject> user) {
            Log.e("DATATA",user+"USER");
        for (int i = 0 ; i < user.size() ; i++){
            if (user.get(i).getVip() != 0){
                DialogVIP(user.get(i));
            }
        }
        all_viewers_recycler_users.addAll(user);
        ShowAllViewersAdapter adapter = new ShowAllViewersAdapter(GoLiveActivity.this, all_viewers_recycler_users, new ShowAllViewersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(UserModelObject item) {
                UserDataDialogFromBottom(item.getUser_id());
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(GoLiveActivity.this,3, RecyclerView.VERTICAL,false);
        all_viewers_recycler.setLayoutManager(gridLayoutManager);
        all_viewers_recycler.setAdapter(adapter);

    }

    @Override
    public void GetNewAddedViewer(UserModelObject user) {
        all_viewers_recycler_users.add(user);
    }

    @Override
    public void GetDeletedAddedViewer(UserModelObject user) {
        all_viewers_recycler_users.add(user);
    }

    @Override
    public void GetLastComments(CommentModel model) {
//        if (model.isFly())
            FlyCommentData(model);
        all_comments_recycler.add(model);
        CommentsLiveAdapter adapterss = new CommentsLiveAdapter(GoLiveActivity.this, all_comments_recycler);
        Comments_recycler.setAdapter(adapterss);
        LinearLayoutManager gridLayoutManagersss = new LinearLayoutManager(GoLiveActivity.this, RecyclerView.VERTICAL,false);
        Comments_recycler.setLayoutManager(gridLayoutManagersss);
    }

    @Override
    public void RetrieveGift(GiftModel model) {
        if (model.getGiftName().equals("giftname")){
            FlyGiftDialogFeatured(model);
        }
        else
            FlyGiftDialogBottom(model);
    }

    @Override
    public void RetrieveGlobalGift(GlobalGiftModel model) {
        RetrieveGlobalGift(Live_Id);
    }

    @Override
    public void RetrieveVipUser(UserModelObject model) {

    }
    private void UpdateUserDataDialogFromBottom(UserModelObject data) {
        TextView user_name_bottom,fans_count_bottom,followings_count_bottom,sends_count_bottom,wings_count_bottom,green_bottom,foshia_bottom;
        ImageView user_image_bottom;
        LinearLayout fans_count_bottom_container,followings_count_bottom_container,sends_count_bottom_container,wings_count_bottom_container;
        Button follow_bottom,chat_bottom,vip_bottom_image;

        user_name_bottom = dialog_user_bottom.findViewById(R.id.user_name_bottom);
        fans_count_bottom = dialog_user_bottom.findViewById(R.id.fans_count_bottom);
        followings_count_bottom = dialog_user_bottom.findViewById(R.id.followings_count_bottom);
        sends_count_bottom = dialog_user_bottom.findViewById(R.id.sends_count_bottom);
        wings_count_bottom = dialog_user_bottom.findViewById(R.id.wings_count_bottom);
        fans_count_bottom_container = dialog_user_bottom.findViewById(R.id.fans_count_bottom_container);
        followings_count_bottom_container = dialog_user_bottom.findViewById(R.id.followings_count_bottom_container);
        sends_count_bottom_container = dialog_user_bottom.findViewById(R.id.sends_count_bottom_container);
        wings_count_bottom_container = dialog_user_bottom.findViewById(R.id.wings_count_bottom_container);
        follow_bottom = dialog_user_bottom.findViewById(R.id.follow_bottom);
        chat_bottom = dialog_user_bottom.findViewById(R.id.chat_bottom);
        green_bottom = dialog_user_bottom.findViewById(R.id.green_bottom);
        foshia_bottom = dialog_user_bottom.findViewById(R.id.foshia_bottom);
//        vip_bottom_image = dialog_user_bottom.findViewById(R.id.vip_bottom_image);

        user_name_bottom.setText(data.getName());
        fans_count_bottom.setText(data.getFans()+"");
        followings_count_bottom.setText(data.getFollowing()+"");
        wings_count_bottom.setText(data.getWings()+"");
        sends_count_bottom.setText(data.getSend()+"");
        green_bottom.setText(data.getLevel()+"");
        foshia_bottom.setText(data.getAge()+"");
//            if (data.getVip() == 0 )
//                vip_bottom_image.setImageResource(0);
//            else if(data.getVip() == 1 )
//                vip_bottom_image.setImageResource(0);
//            else
//                vip_bottom_image.setImageResource(0);
        chat_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        follow_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        fans_count_bottom_container.setOnClickListener(v -> {
            Intent intent = new Intent(GoLiveActivity.this, ShowUserActivities.class);
            intent.putExtra("type","fans");
            if (!data.getUser_id().equals(App.userModelObject_of_Project.getUser_id()))
                intent.putExtra("data",data.getUser_id());
            startActivity(intent);
        });
        followings_count_bottom_container.setOnClickListener(v -> {
            Intent intent = new Intent(GoLiveActivity.this, ShowUserActivities.class);
            intent.putExtra("type","following");
            if (!data.getUser_id().equals(App.userModelObject_of_Project.getUser_id()))
                intent.putExtra("user_id",data.getUser_id());

            startActivity(intent);
        });
        sends_count_bottom_container.setOnClickListener(v -> {
            Intent intent = new Intent(GoLiveActivity.this, ShowUserActivities.class);
            intent.putExtra("type","sends");
            if (!data.getUser_id().equals(App.userModelObject_of_Project.getUser_id()))
                intent.putExtra("user_id",data.getUser_id());

            startActivity(intent);
        });
        wings_count_bottom_container.setOnClickListener(v -> {
            Intent intent = new Intent(GoLiveActivity.this, ShowUserActivities.class);
            intent.putExtra("type","wings");
            if (!data.getUser_id().equals(App.userModelObject_of_Project.getUser_id()))
                intent.putExtra("user_id",data.getUser_id());
            startActivity(intent);
        });
    }
    private void UserDataDialogFromBottom(String user_id) {
        presenter.GetClickedUserData( user_id);
        dialog_user_bottom.setCancelable(true);
        dialog_user_bottom.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_user_bottom.setContentView(R.layout.profile_in_live_stream);
        final Window dialogWindow = dialog_user_bottom.getWindow();
        final WindowManager.LayoutParams dialogWindowAttributes = dialogWindow.getAttributes();
        final WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogWindowAttributes);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity =  Gravity.BOTTOM;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog_user_bottom.show();
        Log.e("DATATA","DATAHERE"+user_id);
    }
    private void FlyCommentData(CommentModel model) {
        ImageView user_image_fly;
        LinearLayout container_comment_fly;
        TextView user_name_fly,user_level_fly,comment_text_fly;
        user_image_fly = findViewById(R.id.user_image_fly);
        user_name_fly = findViewById(R.id.user_name_fly);
//            user_level_fly = findViewById(R.id.user_level_fly);
        comment_text_fly = findViewById(R.id.comment_tect_fly);
        container_comment_fly = findViewById(R.id.container_comment_fly);

        Glide.with(GoLiveActivity.this).load(model.getUser_name()).into(user_image_fly);
//            user_level_fly.setText(model.getUser_level()+"");
        user_name_fly.setText(model.getUser_name());
        comment_text_fly.setText(model.getComment_text());

        container_comment_fly.setVisibility(View.VISIBLE);


        TranslateAnimation animation = new TranslateAnimation(0.0f, width, 0.0f, 0.0f); // new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation.setDuration(4000); // animation duration
        animation.setFillAfter(true);
        container_comment_fly .startAnimation(animation);//your_view for mine is imageView

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            container_comment_fly.setVisibility(View.GONE);
        }, 4000);
    }
    private void FlyGiftDialogBottom(GiftModel model) {
        ImageView image_gift_fly;
        LinearLayout container_gift_dialog;
        TextView count_gift_fly,user_send_gift_fly,gift_name_fly;

        container_gift_dialog = findViewById(R.id.container_gift_dialog);
        image_gift_fly = findViewById(R.id.first_gift_image);
//        count_gift_fly = findViewById(R.id.count_gift_fly);
        user_send_gift_fly =findViewById(R.id.first_gift_user_name);
        gift_name_fly =findViewById(R.id.first_gift_text);


        Glide.with(GoLiveActivity.this).load(model.getImage_url()).into(image_gift_fly);
//        count_gift_fly.setText(model.getGiftCount());
        gift_name_fly.setText(model.getGiftName());
        user_send_gift_fly.setText(model.getUserName());

        container_gift_dialog.setVisibility(View.VISIBLE);


        Handler handler = new Handler();
        handler.postDelayed(() -> {
            container_gift_dialog.setVisibility(View.GONE);
        }, 2500);

    }
    private void RetrieveGlobalGift(String text) {
        TextView text_shape;
        text_shape = findViewById(R.id.text_vip_gift);
        text_shape.setText(text);
        findViewById(R.id.text_vip_gift_container).setVisibility(View.VISIBLE);

        TranslateAnimation animation = new TranslateAnimation(0.0f, width, 0.0f, 0.0f); // new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation.setDuration(4000); // animation duration
        animation.setFillAfter(true);
        text_shape .startAnimation(animation);

    }
    private void FlyGiftDialogFeatured(GiftModel model) {
        ImageView image_gift_fly;
        TextView count_gift_fly,user_send_gift_fly,gift_name_fly;

        image_gift_fly = findViewById(R.id.image_gift_fly);
        count_gift_fly =findViewById(R.id.count_gift_fly);
        user_send_gift_fly =findViewById(R.id.user_send_gift_fly);
        gift_name_fly =findViewById(R.id.gift_name_fly);


        Glide.with(GoLiveActivity.this).load(model.getImage_url()).into(image_gift_fly);
        count_gift_fly.setText(model.getGiftCount());
        gift_name_fly.setText(model.getGiftName());
        user_send_gift_fly.setText(model.getUserName());
    }
    private void AllViewersDialogData() {
        Log.e("DATAT","STARTDIALOG");
        RecyclerView list_recycler;
        final Dialog dialog = new Dialog(GoLiveActivity.this,R.style.Theme_Dialog);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.list_view_data);
        list_recycler = dialog.findViewById(R.id.recycler);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(GoLiveActivity.this, RecyclerView.VERTICAL,false);
        list_recycler.setLayoutManager(gridLayoutManager);
        ShowAllViewersDialogListAdapter adapter = new ShowAllViewersDialogListAdapter(GoLiveActivity.this, all_viewers_recycler_users, new ShowAllViewersDialogListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(UserModelObject item) {
                Log.e("DATATA","CLICKEDnownow");
                UserDataDialogFromBottom(item.getUser_id());
            }
        });

        list_recycler.setAdapter(adapter);

        final Window dialogWindow = dialog.getWindow();
        final WindowManager.LayoutParams dialogWindowAttributes = dialogWindow.getAttributes();
        final WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogWindowAttributes);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity =  Gravity.BOTTOM;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        Log.e("DATAT","STARTDIALOG");
        dialog.show();
    }
    private void updateAllTopFansData(ArrayList<UserModelObject> list) {
        top_fans_list = list;
        RecyclerView list_recycler = dialog_topfans.findViewById(R.id.list_recycler);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(GoLiveActivity.this, LinearLayoutManager.VERTICAL, false);
        list_recycler.setLayoutManager(layoutManager2);
        TopFansAdapter adapter = new TopFansAdapter(GoLiveActivity.this,top_fans_list);
        Log.e("DATATA","DDDDDDcvvv"+top_fans_list.size());
        list_recycler.setAdapter(adapter);
    }
    private void updateAllTodayTopFansData(ArrayList<UserModelObject> list) {
        top_fans_list_today = list;
        RecyclerView list_recycler = dialog_topfans.findViewById(R.id.list_recycler);
        Log.e("DATATA","DDDDDD"+top_fans_list_today.size());
        TopFansAdapter adapter = new TopFansAdapter(GoLiveActivity.this,top_fans_list_today);
        list_recycler.setAdapter(adapter);
    }
    private void AllTopFansData() {
        RecyclerView list_recycler;
        TextView top_fans,today_top_Fans;
        presenter.RetrievedTopFans(live_user_data.getUser_id());
        presenter.RetrievedTodayTopFans(live_user_data.getUser_id());
        dialog_topfans.setCancelable(true);
        dialog_topfans.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_topfans.setContentView(R.layout.topfansday_and_topfans);
        list_recycler = dialog_topfans.findViewById(R.id.list_recycler);
        top_fans = dialog_topfans.findViewById(R.id.top_fans);
        today_top_Fans = dialog_topfans.findViewById(R.id.today_top_Fans);
        list_recycler.setNestedScrollingEnabled(true);
        list_recycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(GoLiveActivity.this, LinearLayoutManager.VERTICAL, false);
        list_recycler.setLayoutManager(layoutManager2);
        TopFansAdapter adapter = new TopFansAdapter(GoLiveActivity.this,top_fans_list);
        list_recycler.setAdapter(adapter);

        top_fans.setOnClickListener(view -> {
            Log.e("DATATA","DATATA"+top_fans_list.size()+"SIZE");
            TopFansAdapter adapter1 = new TopFansAdapter(GoLiveActivity.this,top_fans_list);
            list_recycler.setAdapter(adapter1);
        });
        today_top_Fans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("DATATA","DATATA"+top_fans_list_today.size()+"SIZE");
                TopFansAdapter adapter = new TopFansAdapter(GoLiveActivity.this,top_fans_list_today);
                list_recycler.setAdapter(adapter);
            }
        });


        final Window dialogWindow = dialog_topfans.getWindow();
        final WindowManager.LayoutParams dialogWindowAttributes = dialogWindow.getAttributes();
        final WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogWindowAttributes);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity =  Gravity.BOTTOM;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog_topfans.show();
    }

    private void updateAllGuestsData() {
        if (users_guests_IDs.contains(App.userModelObject_of_Project.getUser_id()) ||
                users_waiting_guests_IDs.contains(App.userModelObject_of_Project.getUser_id())) {
            dialog_guests.findViewById(R.id.waiting_guest_recycler).setVisibility(View.INVISIBLE);
        }
        else {
            dialog_guests.findViewById(R.id.waiting_guest_recycler).setVisibility(View.VISIBLE);
        }
        if ( dialog_guests.isShowing() ) {
            RecyclerView guest_recycler = dialog_guests.findViewById(R.id.guest);
            GustsUserAdapter adapter = new GustsUserAdapter(GoLiveActivity.this, user_guests_data_list,Live_Id);
            guest_recycler.setAdapter(adapter);
        }
    } private void updateAllWaitingGuestsData() {
        if (users_guests_IDs.contains(App.userModelObject_of_Project.getUser_id()) ||
                users_waiting_guests_IDs.contains(App.userModelObject_of_Project.getUser_id())) {
            dialog_guests.findViewById(R.id.waiting_guest_recycler).setVisibility(View.INVISIBLE);
        }
        else {
            dialog_guests.findViewById(R.id.waiting_guest_recycler).setVisibility(View.VISIBLE);
        }
        if ( dialog_guests.isShowing() ) {
            RecyclerView waiting_guest_recycler = dialog_guests.findViewById(R.id.waiting_guest_recycler);
            WaitingGuestsAdapter adapter = new WaitingGuestsAdapter(GoLiveActivity.this, user_waiting_guests_data_list,Live_Id);
            waiting_guest_recycler.setAdapter(adapter);
        }
    }
    int positionSelected = -1;
    private void AllGuestsData() {
        Button Join_btn;
        RecyclerView guests,waiting_lists;
        presenter.RetrievedGuests(live_user_data.getUser_id());
        presenter.RetrievedWaitingGuests(live_user_data.getUser_id());
        dialog_guests.setCancelable(true);
        dialog_guests.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_guests.setContentView(R.layout.guest_waiting_list);
        Join_btn = dialog_guests.findViewById(R.id.Join_btn);
        guests = dialog_guests.findViewById(R.id.guest);
        waiting_lists = dialog_guests.findViewById(R.id.waiting_guest_recycler);
        if (users_guests_IDs.contains(App.userModelObject_of_Project.getUser_id()) ||
                users_waiting_guests_IDs.contains(App.userModelObject_of_Project.getUser_id())) {
            Join_btn.setVisibility(View.INVISIBLE);
        }
        Join_btn.setOnClickListener(view -> presenter.JoinAsGuest(Live_Id));
        guests.setNestedScrollingEnabled(true);
        guests.setHasFixedSize(true);
        LinearLayoutManager layoutManager22 = new LinearLayoutManager(GoLiveActivity.this, LinearLayoutManager.VERTICAL, false);
        guests.setLayoutManager(layoutManager22);
        GustsUserAdapter adapter22 = new GustsUserAdapter(GoLiveActivity.this,user_guests_data_list,Live_Id);
        guests.setAdapter(adapter22);

        waiting_lists.setNestedScrollingEnabled(true);
        waiting_lists.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(GoLiveActivity.this, LinearLayoutManager.VERTICAL, false);
        waiting_lists.setLayoutManager(layoutManager2);
        WaitingGuestsAdapter adapter = new WaitingGuestsAdapter(GoLiveActivity.this,user_waiting_guests_data_list,Live_Id);
        waiting_lists.setAdapter(adapter);

        final Window dialogWindow = dialog_guests.getWindow();
        final WindowManager.LayoutParams dialogWindowAttributes = dialogWindow.getAttributes();
        final WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogWindowAttributes);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity =  Gravity.BOTTOM;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog_guests.show();
    }
    private void GiftDialog() {
        TextView gift_tab,my_diamonds,count_gift,Send;
        RecyclerView gifts_recycler_points;
        dialog_gift = new Dialog(GoLiveActivity.this,R.style.Theme_Dialog);
        dialog_gift.setCancelable(true);
        dialog_gift.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog_gift.setContentView(R.layout.gift);
        gift_tab = dialog_gift.findViewById(R.id.gift_tab);
        my_diamonds = dialog_gift.findViewById(R.id.my_diamonds);
        count_gift = dialog_gift.findViewById(R.id.count_gift);
        Send = dialog_gift.findViewById(R.id.Send);
        gifts_recycler_points = dialog_gift.findViewById(R.id.gifts_recycler_points);
        final GiftsDataDialogAdapter adapter = new GiftsDataDialogAdapter(GoLiveActivity.this, arr_gifts, new GiftsDataDialogAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                positionSelected = pos;

            }
        },positionSelected);
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("DATATA","here");
                presenter.SetGiftToFirebase(arr_gifts.get(positionSelected),Live_Id,Integer.parseInt(count_gift.getText().toString()));
            }
        });

        my_diamonds.setText(App.userModelObject_of_Project.getDiamonds()+"");
        gifts_recycler_points.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(GoLiveActivity.this,2, RecyclerView.HORIZONTAL,false);
        gifts_recycler_points.setLayoutManager(gridLayoutManager);



        final Window dialogWindow = dialog_gift.getWindow();
        final WindowManager.LayoutParams dialogWindowAttributes = dialogWindow.getAttributes();
        final WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogWindowAttributes);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity =  Gravity.BOTTOM;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog_gift.show();
    }
    private void DialogVIP(UserModelObject userModelObject) {
        ImageView user_image = findViewById(R.id.user_image_vip);
        TextView text = findViewById(R.id.user_name_vip);
        LinearLayout container = findViewById(R.id.container_vip);
        ImageView vip_image = findViewById(R.id.vip_image);
        Glide.with(GoLiveActivity.this).load(userModelObject.getImages().get(0)).into(user_image);
        vip_image.setImageResource(0);
        text.setText(userModelObject.getName());
        container.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            container.setVisibility(View.GONE);
        }, 2500);

    }

    @Override
    public void SelectedGiftPosition(int position) {
        SELECTED_GIFT_POSITION = position;

    }
}

 /*
  Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, "\n "+"نزل التطبيق من جوجل بلاي : "+"https://play.google.com/store/apps/details?id=com.superbekala.android.userapp&hl=en");
                    startActivity(Intent.createChooser(sharingIntent, "Share Text!"));
  */