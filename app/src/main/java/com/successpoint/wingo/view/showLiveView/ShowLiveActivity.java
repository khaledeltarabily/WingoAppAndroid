package com.successpoint.wingo.view.showLiveView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.vision.text.Line;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.red5pro.streaming.R5Connection;
import com.red5pro.streaming.R5Stream;
import com.red5pro.streaming.R5StreamProtocol;
import com.red5pro.streaming.config.R5Configuration;
import com.red5pro.streaming.event.R5ConnectionEvent;
import com.red5pro.streaming.event.R5ConnectionListener;
import com.red5pro.streaming.media.R5AudioController;
import com.red5pro.streaming.source.R5Camera;
import com.red5pro.streaming.source.R5Microphone;
import com.red5pro.streaming.view.R5VideoView;
import com.successpoint.wingo.App;
import com.successpoint.wingo.R;
import com.successpoint.wingo.SplashActivity;
import com.successpoint.wingo.model.CommentModel;
import com.successpoint.wingo.model.GiftModel;
import com.successpoint.wingo.model.GlobalGiftModel;
import com.successpoint.wingo.model.PopularMainModel;
import com.successpoint.wingo.model.TopFansModel;
import com.successpoint.wingo.model.TopTrendingModel;
import com.successpoint.wingo.model.UserModelObject;
import com.successpoint.wingo.utils.Mainsharedprefs;
import com.successpoint.wingo.view.ChatFragment.MainChatsRecycler;
import com.successpoint.wingo.view.LiveData.tests.PublishTest.PublishTest;
import com.successpoint.wingo.view.LiveData.tests.SubscribeTest.SubscribeTest;
import com.successpoint.wingo.view.mainactivity.fragments.Profile.ActivityProfile.ProfileActivity;
import com.successpoint.wingo.view.mainactivity.fragments.Profile.AllActivitiesUserList.ShowUserActivities;
import com.successpoint.wingo.view.mainsign.MainSignActivity;
import com.successpoint.wingo.view.toptrending.adapter.TopTrendingAdapter;

import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class ShowLiveActivity extends MvpFragment<ShowLiveView, ShowLivePresenter> implements ShowLiveView , R5ConnectionListener {
        protected R5VideoView display;
        protected R5Stream subscribe;

        @Override
        public void onConnectionEvent(R5ConnectionEvent event) {
            Log.d("Subscriber", ":onConnectionEvent " + event.name());
            if (event.name() == R5ConnectionEvent.LICENSE_ERROR.name()) {
                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setTitle("Error");
                        alertDialog.setMessage("License is Invalid");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK",
                                new DialogInterface.OnClickListener()

                                {
                                    public void onClick (DialogInterface dialog, int which){
                                        dialog.dismiss();
                                    }
                                }

                        );
                        alertDialog.show();
                    }
                });
            }
            else if (event.name() == R5ConnectionEvent.START_STREAMING.name()){
//            subscribe.setFrameListener(new R5FrameListener() {
//                @Override
//                public void onBytesReceived(byte[] bytes, int i, int i1) {
//                    Uncomment for framelistener performance test
//                }
//            });
            }
        }


        public void Subscribe(String sample_text){

            //Create the configuration from the tests.xml
            R5Configuration config = new R5Configuration(R5StreamProtocol.RTSP,
                    TestContent.GetPropertyString("host"),
                    TestContent.GetPropertyInt("port"),
                    TestContent.GetPropertyString("context"),
                    TestContent.GetPropertyFloat("subscribe_buffer_time"));
            config.setLicenseKey(TestContent.GetPropertyString("license_key"));
            config.setBundleID(getActivity().getPackageName());

            R5Connection connection = new R5Connection(config);

            //setup a new stream using the connection
            subscribe = new R5Stream(connection);

            //Some devices can't handle rapid reuse of the audio controller, and will crash
            //Recreation of the controller assures that the example will always be stable
            subscribe.audioController = new R5AudioController();
            subscribe.audioController.sampleRate = TestContent.GetPropertyInt("sample_rate");

            subscribe.client = this;
            subscribe.setListener(this);

            //show all logging
            subscribe.setLogLevel(R5Stream.LOG_LEVEL_DEBUG);

            //display.setZOrderOnTop(true);
            display.attachStream(subscribe);

            display.showDebugView(TestContent.GetPropertyBool("debug_view"));

            subscribe.play(sample_text);

        }

        protected void updateOrientation(int value) {
            value += 90;
            Log.d("SubscribeTest", "update orientation to: " + value);
            display.setStreamRotation(value);
        }

        public void onMetaData(String metadata) {
            Log.d("SubscribeTest", "Metadata receieved: " + metadata);
            String[] props = metadata.split(";");
            for (String s : props) {
                String[] kv = s.split("=");
                if (kv[0].equalsIgnoreCase("orientation")) {
                    updateOrientation(Integer.parseInt(kv[1]));
                }
            }
        }

        public void onStreamSend(String msg){
            Log.d("SubscribeTest", "GOT MSG");
        }

        @Override
        public void onStop() {
            if(subscribe != null) {
                subscribe.stop();
                subscribe = null;
            }

            if (display != null) {
                display.attachStream(null);
            }

            super.onStop();
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
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.live_chat, container, false);
            all_comments_recycler = new ArrayList<>();
            all_viewers_recycler_users = new ArrayList<>();
            Log.e("DATATA", "STARTED");
            dialog_gift = new Dialog(getContext(), R.style.Theme_Dialog);
            dialog_topfans = new Dialog(getContext(), R.style.Theme_Dialog);
            dialog_guests = new Dialog(getContext(), R.style.Theme_Dialog);
            dialog_user_bottom = new Dialog(getContext(), R.style.Theme_Dialog);

            display = (R5VideoView) view.findViewById(R.id.videoView);
            Live_Id = getArguments().getString("Url");
            Subscribe(Live_Id);

            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            top_fans_list_today = new ArrayList<>();
            top_fans_list = new ArrayList<>();
            user_guests_data_list = new ArrayList<>();
            user_waiting_guests_data_list = new ArrayList<>();
            live_user_data = new UserModelObject();
            user_id = view.findViewById(R.id.user_id);
            user_name = view.findViewById(R.id.user_name);
            user_level = view.findViewById(R.id.user_level);
            Diamonds = view.findViewById(R.id.wings);
            text_write_comment = view.findViewById(R.id.text_write_comment);
            user_image = view.findViewById(R.id.user_image);
            follow_btn = view.findViewById(R.id.follow_btn);
            show_all_views = view.findViewById(R.id.show_all_views);
            close = view.findViewById(R.id.close);
            Guests = view.findViewById(R.id.Guests);
            Share = view.findViewById(R.id.Share);
            Gifts = view.findViewById(R.id.Gifts);
            all_viewers_recycler = view.findViewById(R.id.all_viewers_recycler);
            Comments_recycler = view.findViewById(R.id.Comments_recycler);
            write_comment = view.findViewById(R.id.write_comment);
            write_comment_container = view.findViewById(R.id.write_comment_container);
            fly_comment_action = view.findViewById(R.id.fly_comment_action);
            AddComment = view.findViewById(R.id.AddComment);

            Guests.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AllGuestsData();
                }
            });
            live_user_data = App.userModelObject_of_Project;
            if (live_user_data.getImages().size() > 0)
                Glide.with(getContext()).load(live_user_data.getImages().get(0)).into(user_image);
            user_name.setText(live_user_data.getName());
            user_id.setText(live_user_data.getUser_id());
            Diamonds.setText(live_user_data.getDiamonds() + "");

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
            presenter.RetrievedDeletedWaitingGuests(Live_Id, null);
            presenter.RetrievedDeletedGuests(Live_Id, null);
            presenter.RetrieveGift(Live_Id);
//        presenter.RetrieveGlobalGift(Live_Id);
            presenter.RetrieveGlobalAllGift();


            ShowAllViewersAdapter adapter = new ShowAllViewersAdapter(getContext(), all_viewers_recycler_users, new ShowAllViewersAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(UserModelObject item) {
                    UserDataDialogFromBottom(item.getUser_id());
                }
            });
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false);
            all_viewers_recycler.setLayoutManager(gridLayoutManager);
            all_viewers_recycler.setAdapter(adapter);

            text_write_comment.setOnClickListener(view -> write_comment_container.setVisibility(View.VISIBLE));
            view.findViewById(R.id.Comment).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    write_comment_container.setVisibility(View.GONE);
                }
            });
            AddComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.AddComment(Live_Id, write_comment.getText().toString(), false);
                }
            });
            Share.setOnClickListener(view -> {
                String shareBody = "Iam Waiting You at Wingo :  " + Live_Id;
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share"));
            });

            CommentsLiveAdapter adapterss = new CommentsLiveAdapter(getContext(), all_comments_recycler);
            Comments_recycler.setAdapter(adapterss);
            LinearLayoutManager gridLayoutManagersss = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
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
            presenter.SetSeenToServerView(Live_Id);
            presenter.GetAllCurrentViewers(Live_Id);
            presenter.GetLastComments(Live_Id);
        }
        return view;
    }


    @NonNull
    @Override
    public ShowLivePresenter createPresenter() {
        return new ShowLivePresenter(getContext(),this);
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
        ShowAllViewersAdapter adapter = new ShowAllViewersAdapter(getContext(), all_viewers_recycler_users, new ShowAllViewersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(UserModelObject item) {
                UserDataDialogFromBottom(item.getUser_id());
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3, RecyclerView.VERTICAL,false);
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
        CommentsLiveAdapter adapterss = new CommentsLiveAdapter(getContext(), all_comments_recycler);
        Comments_recycler.setAdapter(adapterss);
        LinearLayoutManager gridLayoutManagersss = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
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
            Intent intent = new Intent(getContext(), ShowUserActivities.class);
            intent.putExtra("type","fans");
            if (!data.getUser_id().equals(App.userModelObject_of_Project.getUser_id()))
                intent.putExtra("data",data.getUser_id());
            startActivity(intent);
        });
        followings_count_bottom_container.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ShowUserActivities.class);
            intent.putExtra("type","following");
            if (!data.getUser_id().equals(App.userModelObject_of_Project.getUser_id()))
                intent.putExtra("user_id",data.getUser_id());

            startActivity(intent);
        });
        sends_count_bottom_container.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ShowUserActivities.class);
            intent.putExtra("type","sends");
            if (!data.getUser_id().equals(App.userModelObject_of_Project.getUser_id()))
                intent.putExtra("user_id",data.getUser_id());

            startActivity(intent);
        });
        wings_count_bottom_container.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ShowUserActivities.class);
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
        user_image_fly = view.findViewById(R.id.user_image_fly);
        user_name_fly = view.findViewById(R.id.user_name_fly);
//            user_level_fly = view.findViewById(R.id.user_level_fly);
        comment_text_fly = view.findViewById(R.id.comment_tect_fly);
        container_comment_fly = view.findViewById(R.id.container_comment_fly);

        Glide.with(getContext()).load(model.getUser_name()).into(user_image_fly);
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

        container_gift_dialog = view.findViewById(R.id.container_gift_dialog);
        image_gift_fly = view.findViewById(R.id.first_gift_image);
//        count_gift_fly = view.findViewById(R.id.count_gift_fly);
        user_send_gift_fly =view.findViewById(R.id.first_gift_user_name);
        gift_name_fly =view.findViewById(R.id.first_gift_text);


        Glide.with(getContext()).load(model.getImage_url()).into(image_gift_fly);
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
        text_shape = view.findViewById(R.id.text_vip_gift);
        text_shape.setText(text);
        view.findViewById(R.id.text_vip_gift_container).setVisibility(View.VISIBLE);

        TranslateAnimation animation = new TranslateAnimation(0.0f, width, 0.0f, 0.0f); // new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation.setDuration(4000); // animation duration
        animation.setFillAfter(true);
        text_shape .startAnimation(animation);

    }
    private void FlyGiftDialogFeatured(GiftModel model) {
        ImageView image_gift_fly;
        TextView count_gift_fly,user_send_gift_fly,gift_name_fly;

        image_gift_fly = view.findViewById(R.id.image_gift_fly);
        count_gift_fly =view.findViewById(R.id.count_gift_fly);
        user_send_gift_fly =view.findViewById(R.id.user_send_gift_fly);
        gift_name_fly =view.findViewById(R.id.gift_name_fly);


        Glide.with(getContext()).load(model.getImage_url()).into(image_gift_fly);
        count_gift_fly.setText(model.getGiftCount());
        gift_name_fly.setText(model.getGiftName());
        user_send_gift_fly.setText(model.getUserName());
    }
    private void AllViewersDialogData() {
        Log.e("DATAT","STARTDIALOG");
        RecyclerView list_recycler;
        final Dialog dialog = new Dialog(getContext(),R.style.Theme_Dialog);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.list_view_data);
        list_recycler = dialog.findViewById(R.id.recycler);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        list_recycler.setLayoutManager(gridLayoutManager);
        ShowAllViewersDialogListAdapter adapter = new ShowAllViewersDialogListAdapter(getContext(), all_viewers_recycler_users, new ShowAllViewersDialogListAdapter.OnItemClickListener() {
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
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        list_recycler.setLayoutManager(layoutManager2);
        TopFansAdapter adapter = new TopFansAdapter(getContext(),top_fans_list);
        Log.e("DATATA","DDDDDDcvvv"+top_fans_list.size());
        list_recycler.setAdapter(adapter);
    }
    private void updateAllTodayTopFansData(ArrayList<UserModelObject> list) {
        top_fans_list_today = list;
        RecyclerView list_recycler = dialog_topfans.findViewById(R.id.list_recycler);
        Log.e("DATATA","DDDDDD"+top_fans_list_today.size());
        TopFansAdapter adapter = new TopFansAdapter(getContext(),top_fans_list_today);
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
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        list_recycler.setLayoutManager(layoutManager2);
        TopFansAdapter adapter = new TopFansAdapter(getContext(),top_fans_list);
        list_recycler.setAdapter(adapter);

        top_fans.setOnClickListener(view -> {
            Log.e("DATATA","DATATA"+top_fans_list.size()+"SIZE");
            TopFansAdapter adapter1 = new TopFansAdapter(getContext(),top_fans_list);
            list_recycler.setAdapter(adapter1);
        });
        today_top_Fans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("DATATA","DATATA"+top_fans_list_today.size()+"SIZE");
                TopFansAdapter adapter = new TopFansAdapter(getContext(),top_fans_list_today);
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
            GustsUserAdapter adapter = new GustsUserAdapter(getContext(), user_guests_data_list,Live_Id);
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
            WaitingGuestsAdapter adapter = new WaitingGuestsAdapter(getContext(), user_waiting_guests_data_list,Live_Id);
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
        LinearLayoutManager layoutManager22 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        guests.setLayoutManager(layoutManager22);
        GustsUserAdapter adapter22 = new GustsUserAdapter(getContext(),user_guests_data_list,Live_Id);
        guests.setAdapter(adapter22);

        waiting_lists.setNestedScrollingEnabled(true);
        waiting_lists.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        waiting_lists.setLayoutManager(layoutManager2);
        WaitingGuestsAdapter adapter = new WaitingGuestsAdapter(getContext(),user_waiting_guests_data_list,Live_Id);
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
        dialog_gift = new Dialog(getContext(),R.style.Theme_Dialog);
        dialog_gift.setCancelable(true);
        dialog_gift.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_gift.setContentView(R.layout.gift);
        gift_tab = dialog_gift.findViewById(R.id.gift_tab);
        my_diamonds = dialog_gift.findViewById(R.id.my_diamonds);
        count_gift = dialog_gift.findViewById(R.id.count_gift);
        Send = dialog_gift.findViewById(R.id.Send);
        gifts_recycler_points = dialog_gift.findViewById(R.id.gifts_recycler_points);
        final GiftsDataDialogAdapter adapter = new GiftsDataDialogAdapter(getContext(), arr_gifts, new GiftsDataDialogAdapter.OnItemClickListener() {
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2, RecyclerView.HORIZONTAL,false);
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
        ImageView user_image = view.findViewById(R.id.user_image_vip);
        TextView text = view.findViewById(R.id.user_name_vip);
        LinearLayout container = view.findViewById(R.id.container_vip);
        ImageView vip_image = view.findViewById(R.id.vip_image);
        Glide.with(getContext()).load(userModelObject.getImages().get(0)).into(user_image);
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