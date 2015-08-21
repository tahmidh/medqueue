package com.bracu.thesis.medqueue;

import com.bracu.thesis.medqueue.app.AppConfig;
import com.bracu.thesis.medqueue.app.AppController;
import com.bracu.thesis.medqueue.helper.SQLiteHandler;
import com.bracu.thesis.medqueue.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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
    private SQLiteHandler db;


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
        db = new SQLiteHandler(getApplicationContext());

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

    private void checkLogin(final String email, final String password, final int selectedId) {
        String tag_string_request = "req_login";

        pDialog.setMessage("Loading..");
        showDialog();

        String type;
        if(selectedId == R.id.radioBtnDoctor){
            type = "doctor";
        }else{
            type = "patient";
        }
        String uri = AppConfig.URL_LOGIN+"?tag=login&email="+email+"&password="+password+"&type="+type;

        StringRequest strReq = new StringRequest(Method.GET,
                                                 uri,
                                                 new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    String name = jObj.getString("name");
                    String mail = jObj.getString("email");
                    String type = jObj.getString("type");
                    String uid = jObj.getString("uid");
                    Log.d(TAG, "uid:"+uid);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        db.addUser(name,mail,type,uid);
                        if (selectedId == R.id.radioBtnDoctor) {
                            session.setLogin(true, true);
                            Intent intent = new Intent(LoginActivity.this, DoctorActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            session.setLogin(true, false);
                            Intent intent = new Intent(LoginActivity.this, PatientActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg+" "+"Error", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "Login Error: " + volleyError.getMessage());
                Toast.makeText(getApplicationContext(), volleyError.getMessage()+" "+"Error", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_request);
    }

    private void showDialog(){
        if(!pDialog.isShowing()){
            pDialog.show();
        }
    }
    private void hideDialog(){
        if(pDialog.isShowing()){
            pDialog.dismiss();
        }
    }
}
