package com.example.eforezan.kuevents;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    private EditText eEmailfield;
    private EditText ePassfield;
    private Button eRegbutton;

    private FirebaseAuth eAuth;
    private FirebaseAuth.AuthStateListener eAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eEmailfield = (EditText) findViewById(R.id.email_field);
        ePassfield = (EditText) findViewById(R.id.pass_field);
        eRegbutton = (Button) findViewById(R.id.reg_button);

        eAuth = FirebaseAuth.getInstance();


        eAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent("com.example.eforezan.kuevents.HomePage");
                    startActivity(intent);

                }
            }
        };

        onClickListenerButton();



    }

    @Override
    protected void onStart() {
        super.onStart();
        eAuth.addAuthStateListener(eAuthListener);
    }

    public void onClickListenerButton() {
        eRegbutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startSignin();

                    }
                }

        );


    }

    void startSignin() {
        String email = eEmailfield.getText().toString();
        String password = ePassfield.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(MainActivity.this, "Fields are empty", Toast.LENGTH_LONG).show();
        } else {
            eAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Sign In Problem.", Toast.LENGTH_LONG).show();
                    }
                }
            });


        }


    }
}