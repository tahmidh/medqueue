package com.bracu.thesis.medqueue;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.bracu.thesis.medqueue.helper.SessionManager;


public class LoginActivity extends Activity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnLogin;
    private EditText inputEmail;
    private EditText inputPassword;
    private RadioButton rbDoctor;
    private RadioButton rbPatient;
    private ProgressDialog pDialog;
    private SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        rbDoctor = (RadioButton) findViewById(R.id.radioBtnDoctor);
        rbPatient = (RadioButton) findViewById(R.id.radioBtnPatient);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()){
            if(session.isDoctor()){
                // User is already logged in. Take him to main activity
                Intent intent = new Intent(LoginActivity.this, DoctorActivity.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(LoginActivity.this, PatientActivity.class);
                startActivity(intent);
                finish();
            }
        }


    }



}
