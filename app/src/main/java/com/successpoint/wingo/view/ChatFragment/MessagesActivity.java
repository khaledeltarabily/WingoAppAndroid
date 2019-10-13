package com.successpoint.wingo.view.ChatFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.successpoint.wingo.AllUrls;
import com.successpoint.wingo.App;
import com.successpoint.wingo.model.MainChats;
import com.successpoint.wingo.model.MessageModel;
import com.successpoint.wingo.utils.UtilsFirebase.DataFromFirebaseOnAdded;
import com.successpoint.wingo.utils.UtilsFirebase.DataFromFirebaseOnGet;
import com.successpoint.wingo.utils.UtilsFirebase.setDataToServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.successpoint.wingo.R;

public class MessagesActivity extends MvpActivity<MessagesView, MessagesPresenter> implements MessagesView {
    EditText Comment_Text;
    ImageView imageSend;
    MainChats mainModel;
    TextView name;
    RecyclerView recyclerView;
    MessagesRecycler adapter;
    ArrayList<MessageModel> List_Data;

    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_chat_inside);

        ((ImageView)findViewById(R.id.image_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent localIntent = new Intent("android.intent.action.GET_CONTENT");
                localIntent.setType("image/*");
                startActivityForResult(localIntent, 1);
            }
        });
        List_Data = new ArrayList<>();
        Comment_Text=findViewById(R.id.Comment_Text);
        imageSend=findViewById(R.id.imageSend);
        recyclerView=(RecyclerView)findViewById(R.id.recycler);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager2);
        Bundle bundle = getIntent().getExtras();
        mainModel = (MainChats) bundle.getSerializable("model");
        adapter = new MessagesRecycler(this,List_Data,mainModel.getUser_data());
        recyclerView.setAdapter(adapter);
        name = findViewById(R.id.name);
        name.setText(mainModel.getUser_data().getName());
        presenter.GetChatMessages(bundle.getString("chat_id"));
        imageSend.setOnClickListener(view -> {
            Log.e("Done","clicked");
            if (!Comment_Text.getText().toString().trim().isEmpty()) {
                Log.e("Done","clicked inside");
                HashMap<String,String> hash = new HashMap<>();
                hash.put("Message_Text",Comment_Text.getText().toString());
                hash.put("Message_Time","Now");
                hash.put("isImage", String.valueOf(false));
                hash.put("Sender_Id",App.userModelObject_of_Project.getUser_id());
                String valueV= FirebaseDatabase.getInstance().getReference().push().getKey();
                setDataToServer data = new setDataToServer(MessagesActivity.this, AllUrls.GetMessageUrlFromMessageId("")+"/"+valueV, hash);
                try {
                    data.Data(new DataFromFirebaseOnAdded() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            setDataToServer data_inside = new setDataToServer(MessagesActivity.this, AllUrls.GetChatAllMessagesUrlById(mainModel.getChat_id())
                                    +"/"+valueV, "");
                            try {
                                data_inside.Data(new DataFromFirebaseOnAdded() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        setDataToServer data_inside = new setDataToServer(MessagesActivity.this, AllUrls.ListenToChatLastMessage(mainModel.getChat_id())
                                                , valueV);
                                        try {
                                            data_inside.Data(new DataFromFirebaseOnAdded() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                }

                                                @Override
                                                public void onFailure(Exception e) {

                                                }
                                            });
                                        } catch (Exception e) {

                                        }
                                    }

                                    @Override
                                    public void onFailure(Exception e) {

                                    }
                                });
                            } catch (Exception e) {

                            }

                        }

                        @Override
                        public void onFailure(Exception e) {

                        }
                    });

                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public MessagesPresenter createPresenter() {
        return new MessagesPresenter(this,this);
    }

    @Override
    public void GetNewChatAdded(ArrayList<MessageModel> object) {
        Log.e("Done",object.size()+"Size");
        List_Data.addAll(object);
        adapter.notifyDataSetChanged();
    }
    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        if ((paramInt1 == 1) && (paramInt2 == -1)) {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), paramIntent.getData());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
            byte[] datax = baos.toByteArray();
            String valueV= FirebaseDatabase.getInstance().getReference().push().getKey();
            UploadTask uploadTask = FirebaseStorage.getInstance().getReference()
                    .child(AllUrls.GetMessageUrlFromMessageId("")+"/"+valueV + ".jpg").putBytes(datax);
            uploadTask.addOnSuccessListener(taskSnapshot -> {

                HashMap<String,String> hash = new HashMap<>();
                hash.put("Message_Text","image");
                hash.put("Message_Time","Now");
                hash.put("isImage","true");
                hash.put("Sender_Id",App.userModelObject_of_Project.getUser_id());
                setDataToServer data = new setDataToServer(MessagesActivity.this, AllUrls.GetMessageUrlFromMessageId("")+"/"+valueV, hash);
                try {
                    data.Data(new DataFromFirebaseOnAdded() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            setDataToServer data_inside = new setDataToServer(MessagesActivity.this, AllUrls.GetChatAllMessagesUrlById(mainModel.getChat_id())
                                    +"/"+valueV, "");
                            try {
                                data_inside.Data(new DataFromFirebaseOnAdded() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        setDataToServer data_inside = new setDataToServer(MessagesActivity.this, AllUrls.ListenToChatLastMessage(mainModel.getChat_id())
                                                , valueV);
                                        try {
                                            data_inside.Data(new DataFromFirebaseOnAdded() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                }

                                                @Override
                                                public void onFailure(Exception e) {

                                                }
                                            });
                                        } catch (Exception e) {

                                        }
                                    }

                                    @Override
                                    public void onFailure(Exception e) {

                                    }
                                });
                            } catch (Exception e) {

                            }

                        }

                        @Override
                        public void onFailure(Exception e) {

                        }
                    });

                } catch (Exception e) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("res", e.toString());
                }
            });

        }
    }

}
