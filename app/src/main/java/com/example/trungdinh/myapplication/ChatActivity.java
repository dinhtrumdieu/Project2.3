package com.example.trungdinh.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.trungdinh.myapplication.adapter.ChatArrayAdapter;
import com.example.trungdinh.myapplication.models.ChatMessage;
import com.example.trungdinh.myapplication.models.Message;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by TrungDinh on 3/24/2017.
 */

public class ChatActivity extends AppCompatActivity {

    DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    ImageView  btChat , btAddImage;
    EditText edtInput;
    ListView lvChat;
    ChatArrayAdapter adapter;
    List<ChatMessage> list;
    String id , idMy;
    String name , images , nameMy;

    int check = 0;

    // các đối tượng của sentImages;
    static boolean tam = false;
    Bitmap bitmapresult;
    ImageView imageSent;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    // storeage
    final StorageReference storageRef = storage.getReference();
    private static int RESULT_LOAD_IMAGES = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_chat);


        id = getIntent().getStringExtra("IdFriend");
        idMy = getIntent().getStringExtra("IdMy");
        nameMy = getIntent().getStringExtra("nameMy");
        name = getIntent().getStringExtra("name");
        images = getIntent().getStringExtra("images");
        // ánh xạ
        AnhXa();
        OverScrollDecoratorHelper.setUpOverScroll(lvChat);

        list = new ArrayList<>();
        adapter = new ChatArrayAdapter(this , R.layout.chat,list);
        lvChat.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lvChat.setAdapter(adapter);
        // scroll listview
        lvChat.setStackFromBottom(false);


        // lấy dữ liệu
        root.child("message").child(idMy).child(id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ChatMessage message = dataSnapshot.getValue(ChatMessage.class);

                    list.add(message);
                    if(list.size() > 9 && check  == 0){
                        check = 1;
                        lvChat.setStackFromBottom(true);
                    }
                    adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // sự kiện gửi ảnh
        imageSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent , RESULT_LOAD_IMAGES);
            }
        });

        btChat.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  String time = xulyTime();
                  Log.d("toigian",Calendar.getInstance().getTime().toString().substring(0,16));

                  root.child("message").child(idMy).child(id).push().setValue(new ChatMessage(edtInput.getText().toString(),nameMy,false,false));
                  root.child("message").child(id).child(idMy).push().setValue(new ChatMessage(edtInput.getText().toString(),nameMy,true,false));


                  //Map<String, Object> nickname = new HashMap<String, Object>();
                  //nickname.put("ghichu", "Android là hệ điều hành phổ biến trên điện thoại di động");

                  root.child("UserMessage").child(idMy).child(id).setValue(new Message(id,name , edtInput.getText().toString(),time,images));
                  root.child("UserMessage").child(id).child(idMy).setValue(new Message(idMy,nameMy , edtInput.getText().toString(),time,images));
                  edtInput.setText("");
              }
        });

        // nhận sự kiện phím enter hoặc xuống dòng
        edtInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    root.child("message").child(idMy).child(id).push().setValue(new ChatMessage(edtInput.getText().toString(),"Trung Định",false,false));
                    root.child("message").child(id).child(idMy).push().setValue(new ChatMessage(edtInput.getText().toString(),"Trung Định",true,false));
                    edtInput.setText("");
                    return true;
                }
                return false;
            }
        });


        edtInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("HasFocus",""+hasFocus);
                if(!hasFocus){
                    hideKeyboard(v);
                }
            }
        });

        // bắt sự kiện editText có nhập dữ liệu
        edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    btAddImage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    btAddImage.setVisibility(View.GONE);
                    btChat.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                    if(s.length()> 0){
                        btChat.setVisibility(View.VISIBLE);
                        btAddImage.setVisibility(View.GONE);
                    }else{
                        btChat.setVisibility(View.GONE);
                        btAddImage.setVisibility(View.VISIBLE);

                    }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(tam){
            tam = false;
            Calendar calendar = Calendar.getInstance();
            StorageReference mountainsRef = storageRef.child("images"+ calendar.getTimeInMillis());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapresult.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data1 = baos.toByteArray();

            UploadTask uploadTask = mountainsRef.putBytes(data1);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(ChatActivity.this ,"Thất bại",Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String time = xulyTime();
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    root.child("message").child(idMy).child(id).push().setValue(new ChatMessage(downloadUrl.toString(),name,false,true));
                    root.child("message").child(id).child(idMy).push().setValue(new ChatMessage(downloadUrl.toString(),name,true,true));

                    root.child("UserMessage").child(idMy).child(id).setValue(new Message(id,name ,"[hình ảnh]",time,images));
                    root.child("UserMessage").child(id).child(idMy).setValue(new Message(idMy,nameMy ,"[hình ảnh]",time,images));

                    Toast.makeText(ChatActivity.this ,"Thành công",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void AnhXa(){
        btChat = (ImageView) findViewById(R.id.btChat);
        btAddImage = (ImageView) findViewById(R.id.btAddImage);
        edtInput = (EditText) findViewById(R.id.input);
        lvChat = (ListView) findViewById(R.id.list_chat);

        // send
        imageSent = (ImageView) findViewById(R.id.imagesSent);
    }

    // hide keyboard
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGES && resultCode == RESULT_OK && data != null){
            bitmapresult = (Bitmap) data.getExtras().get("data");
            tam = true;
        }
    }

    public String xulyTime(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM, hh:mm");
        return sdf.format(date.getTime());
    }
}
