package com.example.trungdinh.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trungdinh.myapplication.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by TrungDinh on 4/13/2017.
 */

public class SignUpActivity extends AppCompatActivity {

    private static int Result_Code = 10;
    // button
    Button btDangNhapKy;
    // edittext
    EditText edtTen , edtPassword;
    // Firebase root;
    FirebaseAuth auth;
    DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    ProgressDialog progressDialog;

    // Textview error
    TextView tvShowError;
    TextView btBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        // ánh xạ

        // button
        btDangNhapKy = (Button ) findViewById(R.id.btDangKy);
        btBack = (TextView) findViewById(R.id.btBack);
        edtTen = (EditText) findViewById(R.id.edtTen);
        edtPassword = (EditText) findViewById(R.id.edtPassWord);

        // textview
        tvShowError = (TextView) findViewById(R.id.showError);

        tvShowError.requestFocus();




        btDangNhapKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmty()){
                    if(CheckFormatEmail()){
                        auth.createUserWithEmailAndPassword(edtTen.getText().toString(),edtPassword.getText().toString()).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {

                            public void onComplete( Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    if(createAccountInDatabase()){
                                        Toast.makeText(SignUpActivity.this,"Successfully: ",Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(SignUpActivity.this,"Error: ",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

            }
        });

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setResult(10);
                finish();
              //  overridePendingTransition(R.anim.activity_push_up_in,R.anim.activity_push_up_out);
            }
        });

    }

    // lưu thông tin user

    public boolean createAccountInDatabase(){
        if(auth.getCurrentUser() != null){
            FirebaseUser user = auth.getCurrentUser();
            User account = new User(user.getUid(),user.getEmail(),"images");
            Log.d("giatri:",""+user.getUid()+":"+user.getEmail()+":"+user.getDisplayName());
            root.child("user").push().setValue(account);
            return true;
        }

        return false;
    }


    // kiểm tra dữ liệu đúng định dạng email

    public boolean CheckFormatEmail(){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String username = edtTen.getText().toString().trim();
        if(username.matches(emailPattern)){
            tvShowError.setVisibility(View.GONE);
            return true;
        }else{
            tvShowError.setText("Vui lòng nhập đúng định dạng email");
            tvShowError.setVisibility(View.VISIBLE);
            edtTen.requestFocus();
            return false;
        }
    }


    // kiểm tra dữ liệu không được để trống

    public boolean isEmty(){
        if(edtTen.getText().toString().equals("") || edtPassword.getText().toString().equals("")){
            tvShowError.setText("Vui lòng không được bỏ trống");
            tvShowError.setVisibility(View.VISIBLE);
            return false;
        }else {
            tvShowError.setVisibility(View.GONE);
        }
        return true;
    }
}
