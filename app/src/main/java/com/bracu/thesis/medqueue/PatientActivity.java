package com.bracu.thesis.medqueue;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bracu.thesis.medqueue.app.AppConfig;
import com.bracu.thesis.medqueue.app.AppController;
import com.bracu.thesis.medqueue.helper.SQLiteHandler;
import com.bracu.thesis.medqueue.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PatientActivity extends ActionBarActivity {

    private String TAG = PatientActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private SessionManager session;
    public List<PrescriptionModel> prescription;
    public List<PrescriptionModel> test;
    private TextView tv;
    ListView lvPres;
    ListView lvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        lvPres = (ListView)findViewById(R.id.listView2);
        lvTest = (ListView) findViewById(R.id.listView3);
        tv = (TextView)findViewById(R.id.tvWelcome);

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        if(!session.isLoggedIn()){
            logoutUser();
        }

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        prescription = new ArrayList<PrescriptionModel>();
        test = new ArrayList<PrescriptionModel>();

        HashMap<String, String> user = db.getUserDetails();

        final String name = user.get("name");
        final String uid = user.get("uid");
        tv.setText("Welcome "+name.toUpperCase()+"!");
        fetchPres(uid);
        lvPres.setAdapter(new PrescriptionAdapter(this, prescription));
        fetchTest(uid);
        lvTest.setAdapter(new PrescriptionAdapter(this, test));
    }

    private void fetchTest(final String uid) {
        String tag_request = "getTest";
        pDialog.setMessage("Fetching Test");
        showDialog();
        String uri = AppConfig.URL_LOGIN+"index.php?tag=fetchTest&uid="+uid;

        StringRequest strRequest = new StringRequest(Method.GET, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d(TAG, "Response" + s.toString());
                        hideDialog();
                        try{
                            JSONObject jObj = new JSONObject(s);
                            boolean error = jObj.getBoolean("error");
                            if(!error){
                                JSONArray responseArray = jObj.getJSONArray("response");
                                for(int i=1; i < responseArray.length(); i++){
                                    JSONObject a = responseArray.getJSONObject(i);
                                    PrescriptionModel tm = new PrescriptionModel(a.getString("name"),"", a.getString("notes"));
                                    test.add(tm);
                                }
                            }else{
                                String errorMsg = jObj.getString("error msg");
                                Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });
        AppController.getInstance().addToRequestQueue(strRequest,tag_request);
    }

    private void fetchPres(final String uid) {
        String tag_request = "getPrescription";
        pDialog.setMessage("Fetching Prescription");
        showDialog();
        String uri = AppConfig.URL_LOGIN+"index.php?tag=fetchPres&uid="+uid;

        StringRequest strRequest = new StringRequest(Method.GET, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d(TAG, "Response" + s.toString());
                        hideDialog();
                        try{
                            JSONObject jObj = new JSONObject(s);
                            boolean error = jObj.getBoolean("error");
                            if(!error){
                                JSONArray responseArray = jObj.getJSONArray("response");
                                for(int i=1; i < responseArray.length(); i++){
                                    JSONObject a = responseArray.getJSONObject(i);
                                    PrescriptionModel pm = new PrescriptionModel("Medicine: "+a.getString("med_name"), "Frequency: "+a.getString("dose"), "Dose: "+a.getString("freq"));
                                    prescription.add(pm);
                                }
                            }else{
                                String errorMsg = jObj.getString("error msg");
                                Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });
        AppController.getInstance().addToRequestQueue(strRequest,tag_request);
    }

    private void logoutUser() {
        session.setLogin(false, false);
        db.deleteUsers();

        Intent intent = new Intent(PatientActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_patient, menu);
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
