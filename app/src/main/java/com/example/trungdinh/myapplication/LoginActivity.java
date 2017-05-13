package com.example.trungdinh.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by TrungDinh on 3/16/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private static int Request_Code = 10;
    Button  btDangNhap;
    EditText edtTen , edtPassword;
   // Firebase root;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    // Textview
    TextView tvShowError;
    TextView btDangKy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        // ánh xạ

        btDangNhap = (Button ) findViewById(R.id.btDangNhap);
        edtTen = (EditText) findViewById(R.id.edtTen);
        edtPassword = (EditText) findViewById(R.id.edtPassWord);
        tvShowError = (TextView) findViewById(R.id.showError);
        btDangKy = (TextView) findViewById(R.id.btDangKy);


        edtTen.setText("dinhtrum@gmail.com");
        edtPassword.setText("123456");

        // bắt sự kiện hasfocus of edtTen to
        edtTen.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus){
                   hideKeyboard(v);
                }
            }
        });

        // bắt sự kiện hasfocus of edtPassword
        edtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    hideKeyboard(v);
                }
            }
        });

        btDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(isEmty()){
                   if(CheckFormatEmail()){
                        progressDialog.setMessage("Please wait..");
                        progressDialog.show();
                        auth.signInWithEmailAndPassword(edtTen.getText().toString(),edtPassword.getText().toString()).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful()){
                                    FirebaseUser user = auth.getCurrentUser();
                                    Log.d("giatri",""+user.getUid()+":"+user.getEmail()+":"+user.getDisplayName());
                                    Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                                    intent.putExtra("id",user.getUid());
                                    intent.putExtra("nameMy",user.getEmail().split("@")[0]);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(LoginActivity.this,"Tài khoản hoặc mật khẩu sai!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                   }
               }




            }
        });

        // chuyển tới trang đăng ký
        btDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.slide_out_right, R.anim.slide_out_left);
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivityForResult(intent,Request_Code);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == Request_Code && data != null){
                Log.d("Giatri: ",data.getStringExtra("email").toString()+""+data.getStringExtra("pass").toString());
                edtTen.setText(data.getStringExtra("email").toString());
                edtPassword.setText(data.getStringExtra("pass").toString());
        }

    }

    // hide keyboard
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
