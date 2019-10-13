package com.successpoint.wingo.view.VIP_page;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.successpoint.wingo.App;
import com.successpoint.wingo.R;
import com.successpoint.wingo.model.VipModel;
import com.successpoint.wingo.model.vip_data_model;
import java.util.List;
import androidx.viewpager.widget.ViewPager;

public class VIPActivity extends MvpActivity<VIPView, VIPPresenter> implements VIPView {
    vip_data_model current_model;

    ViewPager images;
    ImageView user_image;
    TextView points,user_name,vip_open,downtime_expired,date_done,buy_btn,renew,bottom_buy_price,bottom_renew_price;
    TextView King,duke,warrior;
    TextView ProfileCard,Vip_medal,vip_bullet,entrance_effect,top_room_viewers,
            chat_bubbles,profile_decoration,guest_fram,speed_upgrade;
    Integer[] valueIdsKing = {
            R.drawable.king,
            R.drawable.king2,
            R.drawable.king3,
            R.drawable.king4,
            R.drawable.king5,
            R.drawable.king6,
            R.drawable.king7,
            R.drawable.king8,
            R.drawable.king9
    };


    Integer[] valueIdsWarrior = {
            R.drawable.warrior,
            R.drawable.warrior2,
            R.drawable.warrior3,
            R.drawable.warrior4,
            R.drawable.warrior5,
            R.drawable.warrior6,
            R.drawable.warrior7
    };


    Integer[] valueIdsDuke = {
            R.drawable.duke,
            R.drawable.duke2,
            R.drawable.duke3,
            R.drawable.duke4,
            R.drawable.duke5,
            R.drawable.duke6,
            R.drawable.duke7
    };
    Integer[] CurrentValue;



    List<Integer> prices_buy , prices_renew;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_vip);

        CurrentValue = valueIdsKing;
        bottom_buy_price = findViewById(R.id.bottom_buy_price);
        renew = findViewById(R.id.renew);
        bottom_renew_price = findViewById(R.id.bottom_renew_price);

        ProfileCard = findViewById(R.id.ProfileCard);
        Vip_medal = findViewById(R.id.Vip_medal);
        vip_bullet = findViewById(R.id.vip_bullet);
        entrance_effect = findViewById(R.id.entrance_effect);
        top_room_viewers = findViewById(R.id.top_room_viewers);
        chat_bubbles = findViewById(R.id.chat_bubbles);
        profile_decoration = findViewById(R.id.profile_decoration);
        guest_fram = findViewById(R.id.guest_fram);
        speed_upgrade = findViewById(R.id.speed_upgrade);
        user_name = findViewById(R.id.user_name);

        images = findViewById(R.id.viewpager);
        points = findViewById(R.id.points);
        user_image = findViewById(R.id.user_image);
        vip_open = findViewById(R.id.vip_open);
        downtime_expired = findViewById(R.id.downtime_expired);
        date_done = findViewById(R.id.date_done);
        buy_btn = findViewById(R.id.buy_btn);
        King = findViewById(R.id.King);
        duke = findViewById(R.id.duke);
        warrior = findViewById(R.id.warrior);
        buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.BuyData(images.getCurrentItem());
            }
        });
        renew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.BuyDataRenew(images.getCurrentItem());
            }
        });
        user_name.setText(App.userModelObject_of_Project.getName());
        vip_open.setText(App.userModelObject_of_Project.getVip()+"");

        King.setOnClickListener(view -> {
            CurrentValue = valueIdsKing;
            King.setTextColor(1);
            duke.setTextColor(0);
            warrior.setTextColor(0);
            guest_fram.setTextColor(0);
            speed_upgrade.setTextColor(1);
            guest_fram.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
            speed_upgrade.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
            images.setCurrentItem(0);
        });
        duke.setOnClickListener(view -> {
            CurrentValue = valueIdsDuke;
            King.setTextColor(0);
            duke.setTextColor(1);
            warrior.setTextColor(0);
            guest_fram.setTextColor(0);
            speed_upgrade.setTextColor(1);
            guest_fram.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
            speed_upgrade.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
            images.setCurrentItem(1);
        });

        ProfileCard.setOnClickListener(view -> ViewDialogData(CurrentValue[0]));
        Vip_medal.setOnClickListener(view -> ViewDialogData(CurrentValue[1]));
        vip_bullet.setOnClickListener(view -> ViewDialogData(CurrentValue[2]));
        entrance_effect.setOnClickListener(view -> ViewDialogData(CurrentValue[3]));
        top_room_viewers.setOnClickListener(view -> ViewDialogData(CurrentValue[4]));
        chat_bubbles.setOnClickListener(view -> ViewDialogData(CurrentValue[5]));
        profile_decoration.setOnClickListener(view -> ViewDialogData(CurrentValue[6]));
        guest_fram.setOnClickListener(view ->  {
            if (CurrentValue == valueIdsKing)
                ViewDialogData(CurrentValue[7]);
        });
//        speed_upgrade.setOnClickListener(view -> {
//            if (CurrentValue == valueIdsKing || CurrentValue == valueIdsDuke)
//                ViewDialogData(CurrentValue[8]);
//        });

        warrior.setOnClickListener(view -> {
            CurrentValue = valueIdsWarrior;
            King.setTextColor(0);
            duke.setTextColor(0);
            warrior.setTextColor(1);
            guest_fram.setTextColor(0);
            speed_upgrade.setTextColor(1);
            guest_fram.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
            speed_upgrade.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
            images.setCurrentItem(2);
        });
        images.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position==0)
                    King.performLongClick();
                else if (position==1)
                    duke.performLongClick();
                else
                    warrior.performLongClick();

                if (position == current_model.getVip()){
                    date_done.setText(current_model.getRenew_date());
                    buy_btn.setText("RENEWAL");
                }
                else {
                    date_done.setText("");
                    buy_btn.setText("Buy");
                }

                bottom_buy_price.setText(prices_buy.get(position));
                bottom_renew_price.setText(prices_renew.get(position));
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        presenter.getVipData();
    }

    @Override
    public VIPPresenter createPresenter() {
        return new VIPPresenter(this,this);
    }

    @Override
    public void BuyDone(boolean Done) {
        presenter.getVipData();
    }

    @Override
    public void DataRetrieved(VipModel data) {
        Log.e("Data","RRRR");
        prices_buy = data.getVip_price();
        prices_renew = data.getVip_renew();
        current_model = data.getData();
        if (images.getCurrentItem() == current_model.getVip()){
            date_done.setText(current_model.getRenew_date());
            buy_btn.setText("RENEWAL");
        }
    }

    void ViewDialogData(int id_value){
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_vip);
        final ImageView ImageView=dialog.findViewById(R.id.image);
        final ImageView close=dialog.findViewById(R.id.cancel);
        ImageView.setImageResource(id_value);
        close.setOnClickListener(view -> dialog.dismiss());
        final Window dialogWindow = dialog.getWindow();
        final WindowManager.LayoutParams dialogWindowAttributes = dialogWindow.getAttributes();
        final WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogWindowAttributes);
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }
}