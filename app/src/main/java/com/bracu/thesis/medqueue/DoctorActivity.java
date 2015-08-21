package com.bracu.thesis.medqueue;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.bracu.thesis.medqueue.app.AppConfig;
import com.bracu.thesis.medqueue.app.AppController;
import com.bracu.thesis.medqueue.helper.SQLiteHandler;
import com.bracu.thesis.medqueue.helper.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DoctorActivity extends ActionBarActivity {

    private String TAG = DoctorActivity.class.getSimpleName();
    private TextView txtName;
    private TextView txtEmail;
    private SessionManager session;
    private SQLiteHandler db;
    private ProgressDialog pDialog;
    public List<AppointmentModel> appoint;
    private EditText et;
    private Button btn;
    ListView lvappoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        et = (EditText)findViewById(R.id.editText);
        btn = (Button)findViewById(R.id.button);

        lvappoint = (ListView)findViewById(R.id.listView);
        txtName = (TextView) findViewById(R.id.docname);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        if(!session.isLoggedIn()){
            logoutUser();
        }
        appoint = new ArrayList<AppointmentModel>();
        HashMap<String, String> user = db.getUserDetails();

        final String name = user.get("name");
        final String uid = user.get("uid");

        txtName.setText("Welcome " + name + "!");
        fetchSchedule(uid);
        lvappoint.setAdapter(new AppointmentAdapter(this, appoint));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String delay = et.getText().toString();
                String uri = AppConfig.URL_LOGIN+"db_res.php?db_res.php?id="+uid+"&minute="+delay+"&docName="+name;
//                StringRequest sReq = new StringRequest(Method.GET, uri, null,null);
//                AppController.getInstance().addToRequestQueue(sReq);
                Toast.makeText(getApplicationContext(),"Sending..", Toast.LENGTH_SHORT).show();
                et.setText("");
            }
        });
    }

    private void fetchSchedule(final String uid) {
        String tag_request = "getAppointments";

        pDialog.setMessage("Fetching Appointment Schedule");
        showDialog();

        String uri = AppConfig.URL_LOGIN+"index.php?tag=fetchApp&uid="+uid;
        StringRequest strReq = new StringRequest(Method.GET, uri,new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                Log.d(TAG, "fetch response " + s.toString());
                hideDialog();
                try{
                    JSONObject jObj = new JSONObject(s);
                    boolean error = jObj.getBoolean("error");
                    if(!error){
                        JSONArray responseArray = jObj.getJSONArray("response");
                        for (int i= 1; i < responseArray.length(); i++){
                            JSONObject a = responseArray.getJSONObject(i);
//                            Log.d(TAG, a.getString("book_date")+" "+a.getString("start_time")+" "+ a.getString("services_name"));
                            AppointmentModel am = new AppointmentModel(a.getString("book_date"), a.getString("start_time"), a.getString("services_name"),a.getString("app_fname"));
                            appoint.add(am);
                        }
                    }else{
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq,tag_request);
    }

    private void logoutUser() {

        session.setLogin(false, false);
        db.deleteUsers();

        Intent intent = new Intent(DoctorActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_doctor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logoutUser();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
