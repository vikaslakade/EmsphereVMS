package activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.emsphere.commando.emspherevms.HomeActivity;
import com.emsphere.commando.emspherevms.HttpHandler;
import com.emsphere.commando.emspherevms.R;
import com.emsphere.commando.emspherevms.RoundImage;
import com.loopj.android.http.Base64;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by commando1 on 8/18/2017.
 */

public class Home extends AppCompatActivity {
  TextView name,mobile_no,purpose,representing;
    EditText response;
    Button allow,nothanks;
    ProgressDialog pd;
    String res;
    HttpHandler sh;
    String jsonStr;
    String res_status,response_message;
    String visitorimage;
    ImageView imageView;
    String Visitor_entry_Id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data);

        name=(TextView)findViewById(R.id.name_textview);
        mobile_no=(TextView)findViewById(R.id.phn_textview);
        representing=(TextView)findViewById(R.id.represnt_name_textview);
      purpose=(TextView)findViewById(R.id.purpose_datatextview);
        allow=(Button)findViewById(R.id.button3);
        nothanks=(Button)findViewById(R.id.no_thanksbutton);
        response=(EditText)findViewById(R.id.responce_editText);
        imageView=(ImageView) findViewById(R.id.imageView_pic);


//get notification data info
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                Log.e("Bundle data", bundle.toString());
                //TextView textView=(TextView) findViewById(R.id.textView);
                String data1 = bundle.getString("name");
                // textView.setText(data1);
                name.setText(bundle.getString("name"));
                mobile_no.setText(bundle.getString("mobile_no"));
                purpose.setText(bundle.getString("purpose"));
                representing.setText(bundle.getString("representing"));
                String Notification_Id = bundle.getString("notification_id");
                Visitor_entry_Id = bundle.getString("vstr_entry_id");

                if (!Notification_Id.isEmpty() && !Notification_Id.equals("")) {
                    new SendPostNotificationRequestForRecievedStatus().execute(Notification_Id);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        }
    public void onClickallow(View view){
         res=response.getText().toString();
        if(res.isEmpty()){

            Toast.makeText(this,"plese send response",Toast.LENGTH_LONG).show();
        }
        else {
            new SendPostRequest().execute("1");
        }

    }
    public void onClickNothanks(View view){
         res=response.getText().toString();
        if(res.isEmpty()){

            Toast.makeText(this,"please send response",Toast.LENGTH_LONG).show();
        }else {
            new SendPostRequest().execute("0");
        }

    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){
            pd = new ProgressDialog(Home.this);
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
                    URL url = new URL(" http://seedmanagement.cloudapp.net/VMS_Mobile_Service/SaveExpectedVisitorAuthorization.ashx");
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
                    postDataParams.put("visitor_entry_id",Visitor_entry_Id);
                    postDataParams.put("answer_response",res);
                    postDataParams.put("allow_donotallow",arg0[0]);
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
            return res_status.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            if (pd.isShowing()) {
                pd.dismiss();
            }
            startActivity(new Intent(Home.this, HomeActivity.class));
            //Toast.makeText(getApplicationContext(), res_status+":"+response_message, Toast.LENGTH_LONG).show();

        }
    }

    public class SendPostNotificationRequestForRecievedStatus extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            pd = new ProgressDialog(Home.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }
        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL("http://seedmanagement.cloudapp.net/VMS_Mobile_Service/SaveFCMReceivedStatus.ashx");
                //URL url = new URL("http://172.16.50.10:8080/mywebservices/WebService/add"); // here is your URL path
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.connect();
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("notification_id", arg0[0]);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(postDataParams.toString());
                //System.out.println("Writer data" + writer);
                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        sh = new HttpHandler();
                        jsonStr = sh.makeServiceCall(conn);
                        // Log.e("qwoiopoipjlkljbhcgdvf", jsonStr);
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        res_status = jsonObj.getString("response_status");
                        response_message=jsonObj.getString("response_message");
                         visitorimage=jsonObj.getString("VisitorImage");
                        //System.out.println("@@@@@##########" + conn.getResponseCode());
                    } else {
                        return new String("false : " + responseCode);
                    }
                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
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


            //Toast.makeText(getApplicationContext(), res_status+":"+response_message, Toast.LENGTH_LONG).show();
            //System.out.println(result);

            if (res_status.equals("success")) {
                //JSONObject data = null;
                //System.out.println(result);
                if(!visitorimage.isEmpty()&&!visitorimage.equals("")) {
                    //byte[] encodeByte = Base64.decode(visitorimage, Base64.DEFAULT);
                    //Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                    //RoundedBitmapDrawable conv_bm = RoundImage.getRoundedBitmap(bitmap);
                    //imageView.setImageDrawable(conv_bm);
                    byte[] decodedString = android.util.Base64.decode(visitorimage, android.util.Base64.DEFAULT);
                    //byte[] encodeByte = com.loopj.android.http.Base64.decode(img, com.loopj.android.http.Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    //RoundedBitmapDrawable conv_bm = RoundImage.getRoundedBitmap(bitmap);
                    Bitmap bitmap1 = RoundImage.getCircularBitmap(bitmap, 5);
                    imageView.setImageBitmap(bitmap1);
                    //holder.imageView.setImageBitmap(bitmap1);
                    Toast.makeText(getApplicationContext(), response_message, Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(),"Image Data not Available",Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getApplicationContext(),response_message,Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG).show();

            }
        }
    }

}
