package com.bracu.thesis.medqueue;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bracu.thesis.medqueue.helper.SessionManager;


public class LoginActivity extends Activity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnLogin;
    private EditText inputEmail;
    private EditText inputPassword;
    private RadioGroup radioGroup;
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
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
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
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                int selectedId = radioGroup.getCheckedRadioButtonId();

                if(email.trim().length() > 0 && password.trim().length() > 0 && selectedId != -1){
                    checkLogin(email,password,selectedId);
                }else{
                    Toast.makeText(getApplicationContext(),"Please enter the credentials!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void checkLogin(String email, String password, int selectedId) {
        
    }
}
