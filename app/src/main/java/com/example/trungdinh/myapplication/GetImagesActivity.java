package com.example.trungdinh.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.trungdinh.myapplication.adapter.CustomListView;
import com.example.trungdinh.myapplication.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TrungDinh on 3/26/2017.
 */

public class GetImagesActivity extends AppCompatActivity {

    Button btGetImages;
    ImageView images;
    private static int RESULT_LOAD_IMAGES = 1;
    DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    // Listview
    ListView lvBanBe;
    CustomListView custom;
    List<User> list ;
    EditText edtInputName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_image);

        // ánh xạ
        images = (ImageView) findViewById(R.id.images);
        btGetImages = (Button) findViewById(R.id.btGetImages);
        lvBanBe = (ListView) findViewById(R.id.lvBanBe);
        edtInputName = (EditText) findViewById(R.id.edtInputName);


         list = new ArrayList<>();
         custom = new CustomListView(this,list);
         lvBanBe.setAdapter(custom);
         getDataFromFireBase();


        // storeage
        StorageReference storageRef = storage.getReference();

        // Create a reference to "mountains.jpg"
        final StorageReference mountainsRef = storageRef.child("mountains123.jpg");


        images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent , RESULT_LOAD_IMAGES);*/

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent , RESULT_LOAD_IMAGES);
            }
        });

        btGetImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                images.setDrawingCacheEnabled(true);
                images.buildDrawingCache();
                Bitmap bitmap = images.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(GetImagesActivity.this ,"Thất bại",Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        // tạo node lên firebase
                        root.child("user").push().setValue(new User("102130101",edtInputName.getText().toString(),downloadUrl.toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(GetImagesActivity.this,"Successfully: ",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(GetImagesActivity.this,"Error: ",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                });
            }
        });
    }

    public void getDataFromFireBase(){
        root.child("user").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                list.add(user);
                custom.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGES && resultCode == RESULT_OK && data != null){

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            images.setImageBitmap(bitmap);

            /*Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            images.setImageBitmap(bmp);*/
        }
    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}
