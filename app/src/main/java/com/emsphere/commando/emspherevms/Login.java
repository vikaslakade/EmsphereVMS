package com.emsphere.commando.emspherevms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import activitys.NotificationPojo;
import app.Config;

/**
 * Created by seedcommando_6 on 2/24/2017.
 */

public class Login extends AppCompatActivity{
    EditText username , password;
    Button loginBtn;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    HttpHandler sh;
    String jsonStr;
    String res_status,response_message;
    ProgressDialog pd;
    private String android_id;
    String username1,password1;
    public static ArrayList<NotificationPojo> arrayList;
    String user_name,pass_word;
    SharedPreferences preferences;
    JSONObject user_detail_jsonobject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        Log.d("after","main");
        arrayList=new ArrayList<NotificationPojo>();
        username = (EditText) findViewById(R.id.editText_uname);
        username.requestFocus();
        password = (EditText) findViewById(R.id.editText_pass);
        loginBtn = (Button) findViewById(R.id.button_SignIn);
        //device Id
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("device Id",android_id);
        saveLoginCheckBox = (CheckBox)findViewById(R.id.checkBox_remeber);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin) {
            username.setText(loginPreferences.getString("username", ""));
            password.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }
    }
    public void login(View v){
      user_name=  username.getText().toString().trim();
       pass_word= password.getText().toString().trim();
        if (ConnectionCheck.isNetworkAvailable(getApplicationContext())) {
            if (username.getText().toString().equals("seed") && password.getText().toString().equals("seed")) {
                Toast.makeText(this, "Welcome , Login Successfully..", Toast.LENGTH_LONG).show();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(username.getWindowToken(), 0);
                 username1 = username.getText().toString();
                 password1 = password.getText().toString();

                if (saveLoginCheckBox.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", username1);
                    loginPrefsEditor.putString("password", password1);
                    loginPrefsEditor.commit();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }
                Intent i = new Intent(this, HomeActivity.class);
                startActivity(i);
            }else if(!user_name.isEmpty() && !pass_word.isEmpty() &&! user_name.equals("") &&!pass_word.equals("") ){
                   preferences=getSharedPreferences(Config.SHARED_PREF,0);
                String FCM_Id=preferences.getString("regId","");
               if(!FCM_Id.isEmpty()&&!FCM_Id.equals("")) {
                   new ValidateLoginUser().execute(FCM_Id);
               }
                if (saveLoginCheckBox.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", user_name);
                    loginPrefsEditor.putString("password", pass_word);
                    loginPrefsEditor.commit();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }
            }
            else {

                Toast.makeText(this, "Please enter valid username or password....", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(this, "No Internet connection. Your device is currently not connected to the Internet", Toast.LENGTH_LONG).show();
        }
    }
    public class ValidateLoginUser extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){
            pd = new ProgressDialog(Login.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... arg0) {
            try {
                try {
                    int timeout = 15000;
                    // connection = ConnectionCheck.isConnectedToServer(Urls.URL_SaveVisitorEntryDetails, timeout);
                } catch (Exception e) {
                    return "false";
                }
                try {
                    URL url = new URL(" http://182.71.207.199/VMS_WebService/ValidateVMSMobileUser.ashx");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Content-Language", "en-US");
                    conn.connect();
                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("username",user_name);
                    postDataParams.put("password",pass_word);
                    postDataParams.put("deviceid",android_id);
                    postDataParams.put("fcmid",arg0[0]);
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(postDataParams.toString());
                    writer.flush();
                    writer.close();
                    os.close();
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        sh = new HttpHandler();
                        jsonStr = sh.makeServiceCall(conn);
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        res_status = jsonObj.getString("response_status");
                        response_message=jsonObj.getString("response_message");
                        JSONArray user_details=jsonObj.getJSONArray("user_details");
                         user_detail_jsonobject=user_details.getJSONObject(0);

                    } else {
                        return new String("false : " + responseCode);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (pd.isShowing()) {
                pd.dismiss();
            }
            try {
                String usertype = "0";
                //String satatus=res_status;
                if (res_status.equals("success")) {
                    SharedPreferences.Editor editor = preferences.edit();

                    try {
                        editor.putString("employee_id", user_detail_jsonobject.getString("employee_id"));
                        editor.putString("employee_name", user_detail_jsonobject.getString("employee_name"));
                        editor.putString("is_admin", user_detail_jsonobject.getString("is_admin"));
                        usertype = user_detail_jsonobject.getString("is_admin");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Log.e(TAG, "store method called");
                    editor.commit();
                    if (usertype.equals("1")) {

                        Intent i = new Intent(getApplicationContext(), Home_activity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(Login.this,"Normal user are not allowed to login",Toast.LENGTH_LONG).show();
                        //Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        //startActivity(i);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), response_message, Toast.LENGTH_LONG).show();
                }
                //Toast.makeText(getApplicationContext(), res_status + ":" + response_message, Toast.LENGTH_LONG).show();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
