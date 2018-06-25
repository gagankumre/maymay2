package com.example.gagan.maymay_test1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText emaillogin;
    private EditText passwordlogin;
    private Button loginbtn;
    private Button createnew;

    private ProgressBar progressBar;

    private FirebaseAuth auth;


    protected  void  onStart() {
        super.onStart();
        FirebaseUser currentuser= auth.getCurrentUser();
        if (currentuser!=null){

            sendtomain();
        }
    }

    private void sendtomain() {

        Intent intent = new Intent(Login.this,Home.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth=FirebaseAuth.getInstance();

        progressBar=findViewById(R.id.loginprogressBar);

        emaillogin= findViewById(R.id.emaillogin);
        passwordlogin = findViewById(R.id.passwordlogin);
        loginbtn =findViewById(R.id.loginbutton);
        createnew =findViewById(R.id.createnewaccount);


        emaillogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emaillogin.setCursorVisible(true);
            }
        });
        passwordlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordlogin.setCursorVisible(true);
            }
        });



        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emaillogin.setCursorVisible(false);
                passwordlogin.setCursorVisible(false);


                String email = emaillogin.getText().toString();
                String password = passwordlogin.getText().toString();

                if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){

                    progressBar.setVisibility(View.VISIBLE);

                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.INVISIBLE);
                                sendtomain();
                                finish();
                            }
                            else{
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(Login.this,"Error:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Login.this,"Email or Password can't be empty", Toast.LENGTH_SHORT).show();

                }
            }
        });

        createnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });
    }
}
