package service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.emsphere.commando.emspherevms.HttpHandler;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import app.Config;

/**
 * Created by commando1 on 4/24/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    HttpHandler sh;
  String jsonStr;
    public SharedPreferences pref;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
       Log.e("refreshedToken",refreshedToken);
        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        Log.e(TAG, "sendmethod called");
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
       new SendPostRequest().execute(token);

    }
    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            /*pd = new ProgressDialog(VisitorEntry.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();*/
        }

        protected String doInBackground(String... params) {

            try {

                try {
                    //http://10.0.2.2:3000/ID/
                    URL url = new URL("http://14.141.60.217/ID/");
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

                    postDataParams.put("username", "Soumya2");
                    postDataParams.put("token",params[0]);


                    // Log.e("params", postDataParams.toString());

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    //writer.write(getPostDataString(postDataParams));
                    //os.write(postDataParams.getByt);
                    writer.write(postDataParams.toString());
                    //Log.e("Write", writer.toString());

                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = conn.getResponseCode();

                    if (responseCode == HttpsURLConnection.HTTP_OK) {

                        sh = new HttpHandler();
                        jsonStr = sh.makeServiceCall(conn);
                        // Log.e("qwoiopoipjlkljbhcgdvf", jsonStr);
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        // res_status = jsonObj.getString("response_status");
                        // response_message=jsonObj.getString("response_message");
                        //System.out.println("@@@@@##########" + conn.getResponseCode());
                    } else {
                        return new String("false : " + responseCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }

    private void storeRegIdInPref(String token) {
     pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        Log.e(TAG, "store method called");
        editor.commit();

    }
}
