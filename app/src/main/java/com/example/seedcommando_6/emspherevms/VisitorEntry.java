package com.example.seedcommando_6.emspherevms;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by seedcommando_10 on 1/19/2017.
 */

public class VisitorEntry extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    ArrayList< String> purposemaster1=new ArrayList<String>();

    ArrayList< String> personmeet1=new ArrayList<String>();
    ArrayList< String> visitpurpose1=new ArrayList<String>();
    ArrayList< String> area1=new ArrayList<String>();
    public static final String URL_GET_ALL = "http://seedmanagement.cloudapp.net/VMS_Mobile_Service/SaveVisitorEntryDetails.ashx";

    TextView visitor_name,mobilenumber,time,reprents;
    ImageView iv;
    EditText duration,material1,material2,serialno1,serialno2;/*visitpurpose;*/
   // Spinner visitpurpose;
    Button submit;
    // Progress Dialog Object
    Fields fields;
     Bundle bn;
    HashMap<String,Object> getobj;

    String fname, mbno,lnm,re_prents;
    android.support.v7.widget.Toolbar t4;
    Spinner visitpurpose,personmeet,area;//,personmeet,area;
     LinearLayout relativeLayout;
    Bitmap bitmap;
    String img_vE;
    ProgressDialog pd;
    Fields d;
    Snackbar snackbar;
    HttpHandler sh = null;
    String jsonStr;
    String res_status=null;
    boolean connection;
    String id;
    String response_message;
    String[] spinnerValue = {
            "Employee",
            "Interviewee",
            "security",
            "other",
            "Please Select Visit type"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.visit_details);

        //visitpurpose = (Spinner)findViewById(R.id.dynamic_spinner);

        relativeLayout=(LinearLayout) findViewById(R.id.visitdetails);



           // personmeet = (EditText) findViewById(R.id.persontomeet);
            personmeet = (Spinner) findViewById(R.id.persontomeet);
            duration = (EditText) findViewById(R.id.editText_duration);
            mobilenumber = (TextView) findViewById(R.id.mbn);
            material1 = (EditText) findViewById(R.id.editText_materialno1);
            material2 = (EditText) findViewById(R.id.editText_materialno2);
           // area = (EditText) findViewById(R.id.editText_area);
            area = (Spinner) findViewById(R.id.editText_area);
            serialno1= (EditText) findViewById(R.id.editText_serialno1);
            serialno2 = (EditText) findViewById(R.id.editText_serialno2);
            submit=(Button) findViewById(R.id.visitor_entry_submit);
            visitor_name=(TextView) findViewById(R.id.textView5);
            iv=(ImageView) findViewById(R.id.imageView3);
            time=(TextView) findViewById(R.id.editText_time);

            //visitpurpose=(EditText) findViewById(R.id.visit_purpose);
            visitpurpose = (Spinner) findViewById(R.id.visit_purpose);

            //mandotory fields
            TextView visitpurpose=(TextView) findViewById(R.id.textView10);
            String v_porpose="Visit Purpose";
            String color1="* ";
            SpannableStringBuilder builder1 = new SpannableStringBuilder();

            builder1.append(color1);
            int start1 = builder1.length();
            builder1.append(v_porpose);

            //int end = builder1.length();

            builder1.setSpan(new ForegroundColorSpan(Color.RED), 0,start1 ,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            visitpurpose.setText(builder1);
            TextView meet=(TextView) findViewById(R.id.textView_personmeet);
            String meetperson="Person To Meet";
            //String color1="* ";
            SpannableStringBuilder builder2 = new SpannableStringBuilder();

            builder2.append(color1);
            int start2 = builder2.length();
            builder2.append(meetperson);

            //int end = builder1.length();

            builder2.setSpan(new ForegroundColorSpan(Color.RED), 0,start2 ,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            meet.setText(builder2);
            TextView time1=(TextView) findViewById(R.id.textView_time1);
            String time_name="Time";
            //String color1="* ";
            SpannableStringBuilder builder3 = new SpannableStringBuilder();

            builder3.append(color1);
            int start3 = builder3.length();
            builder3.append(time_name);

            //int end = builder1.length();

            builder3.setSpan(new ForegroundColorSpan(Color.RED), 0,start3 ,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            time1.setText(builder3);
            TextView duration=(TextView) findViewById(R.id.duration);
            String duration_name="Duration(hr)";
            //String color1="* ";
            SpannableStringBuilder builder4 = new SpannableStringBuilder();

            builder4.append(color1);
            int start4 = builder4.length();
            builder4.append(duration_name);

            //int end = builder1.length();

            builder4.setSpan(new ForegroundColorSpan(Color.RED), 0,start4 ,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            duration.setText(builder4);

            //set date to text
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.US);
            Calendar c = Calendar.getInstance();
            String timetime=sdf.format(c.getTime());
            //Log.e("time",timetime);
            // DateFormat timeInstance = SimpleDateFormat.getTimeInstance();
            DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
            String _time=dateFormat.format(Calendar.getInstance().getTime());
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateFormat.parse(_time));
            cal.add(Calendar.MINUTE, 10);
            String newTime =dateFormat.format(cal.getTime());
            // Log.e("time",newTime);
            //String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            // textView is the TextView view that should display it
            // Log.e("@@@time@@@",currentDateTimeString);

            time.setText(timetime+" "+newTime);

            t4 = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar3);
            setSupportActionBar(t4);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            bn = new Bundle();
            bn = getIntent().getExtras();
            getobj = new HashMap<String, Object>();
            getobj = (HashMap<String, Object>) bn.getSerializable("bundleobj");
            d = (Fields) getobj.get("data");
            fname = d.getFirstname();
             id=(String) bn.get("Id");
            //System.out.println("rfdffgfgfg"+id);
           // System.out.println(fname);
            lnm=d.getLastname();
            visitor_name.setText(fname+" "+lnm);
            lnm=d.getLastname();
            mbno = d.getMobile();
           // System.out.println(mbno);

            //set data to the text view
            mobilenumber.setText(mbno);
            reprents=(TextView) findViewById(R.id.textView_reprents);
            re_prents=d.getRepresenting();
            reprents.setText(re_prents);
             img_vE = d.getImage();
            //retrieve and set to bitmap
             byte[] encodeByte = Base64.decode(img_vE, Base64.DEFAULT);
             bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            RoundedBitmapDrawable conv_bm = RoundImage.getRoundedBitmap(bitmap);
            iv.setImageDrawable(conv_bm);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if(ConnectionCheck.isNetworkAvailable(getApplicationContext())) {
            new GetDetails().execute();
        }else {
            Toast.makeText(getApplicationContext(),"No internet connection.your device is not connected to internet",Toast.LENGTH_LONG).show();
        }

    }
    public  void getdata() {
        String du_ration = duration.getText().toString();
        String person_meet = personmeet.getSelectedItem().toString();
        String visit_purpose = visitpurpose.getSelectedItem().toString();
        String ti_me = time.getText().toString();
        String _Area = area.getSelectedItem().toString();
        if (Utility.isNotNull(du_ration) && Utility.isNotNull(person_meet)&&Utility.spinnervalid(person_meet)&&Utility.spinnervalid(visit_purpose)&& Utility.isNotNull(visit_purpose) && Utility.isNotNull(ti_me) ) {
            fields = new Fields();
            fields.setDuration(du_ration);
            fields.setPersonmeet(personmeet.getSelectedItemId());
            fields.setMaterial1(material1.getText().toString());
            fields.setMaterial2(material2.getText().toString());
            fields.setArea1(area.getSelectedItemId());
            fields.setSerialNo1(serialno1.getText().toString());
            fields.setSerialNo2(serialno2.getText().toString());
            fields.setVisitpupose(visitpurpose.getSelectedItemId());
            fields.setTime(time.getText().toString());
            if(ConnectionCheck.isNetworkAvailable(getApplicationContext())) {
                new SendPostRequest().execute();
            }else {
                Toast.makeText(getApplicationContext(),"No internet connection.your device is not connected to internet",Toast.LENGTH_LONG).show();
            }
            // Toast.makeText(this," next",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(VisitorEntry.this,"Please fill all manadatory fields",Toast.LENGTH_LONG).show();
        }
    }

    public void visitorEntrySubmit(View v)
    {
        getdata();

       /* Toast.makeText(this,"Visit is Confirm",Toast.LENGTH_LONG).show();*/
    }
    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){
            pd = new ProgressDialog(VisitorEntry.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... arg0) {

            try {
                try {
                    int timeout = 15000;
                    connection = ConnectionCheck.isConnectedToServer(URL_GET_ALL, timeout);
                    // System.out.println("response code"+conn.getResponseCode());
                   // Log.d("connection@@#@@@@", "" + connection);

                } catch (Exception e) {
                    return "false";
                }
                try {
                    URL url = new URL(URL_GET_ALL);
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

                    postDataParams.put("VisitorRegistrationId", id);
                    postDataParams.put("duration", fields.getDuration());
                    postDataParams.put("material1", fields.getMaterial1());
                    postDataParams.put("material2", fields.getMaterial2());
                    postDataParams.put("persontomeet", fields.getPersonmeet().toString());
                    postDataParams.put("serialNo1", fields.getSerialNo1());
                    postDataParams.put("serialNo2", fields.getSerialNo2());
                    postDataParams.put("Area", fields.getArea1().toString());
                    postDataParams.put("time", fields.getTime());
                    postDataParams.put("Visitpurpose", fields.getVisitpupose().toString());

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
                        res_status = jsonObj.getString("response_status");
                         response_message=jsonObj.getString("response_message");
                        //System.out.println("@@@@@##########" + conn.getResponseCode());
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
            Toast.makeText(getApplicationContext(), res_status+":"+response_message, Toast.LENGTH_LONG).show();
            JSONObject data = null;
            //System.out.println(result);

           if (connection) {
                if (result.equals("success")) {
                    try {
                        try {
                            data = new JSONObject();
                            data.put("name", fname);
                            data.put("lname", lnm);
                            data.put("image", img_vE);
                            // data.put("name", fname);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        ViewDialog alert = new ViewDialog(getApplicationContext());
                        alert.showDialog(VisitorEntry.this, data.toString());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } else {
                    //Initializing snackbar using Snacbar.make() method
                    snackbar = Snackbar.make(getCurrentFocus(), "Data not added,Reason: "+response_message, Snackbar.LENGTH_LONG);
                    //Displaying the snackbar using the show method()
                    snackbar.show();
                }
            }
                else {
                Toast.makeText(VisitorEntry.this, "server not Running,Please try after some time", Toast.LENGTH_LONG).show();
            }

        }

    }
    private class GetDetails extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(VisitorEntry.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler_spinner sh = new HttpHandler_spinner();
            // Making a request to url and getting response
            String url = "http://seedmanagement.cloudapp.net/VMS_Mobile_Service/GetMasterDataList.ashx";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    JSONArray prsontomeet = jsonObj.getJSONArray("person_to_meet");
                    // Getting JSON Array node
                    JSONArray Area = jsonObj.getJSONArray("area_master");
                    JSONArray purpose_master = jsonObj.getJSONArray("purpose_master");
                    // looping through All Contacts
                    personmeet1.add("Person to Meet");
                    for (int i = 0; i < prsontomeet.length(); i++) {
                        //Log.d("data",contacts);
                        JSONObject c = prsontomeet.getJSONObject(i);
                        String Name= c.getString("person_Name");
                        Log.d("@@@@@@@data@@@@@",Name);


                        personmeet1.add(Name);


                    }
                    area1.add("Select Area");
                    for (int i = 0; i < Area.length(); i++) {
                        //Log.d("data",contacts);
                        JSONObject c = Area.getJSONObject(i);
                        String Name= c.getString("area_name");
                        Log.d("@@@@@@@data@@@@@",Name);


                        area1.add(Name);


                    }
                    purposemaster1.add("Select visit purpose");
                    for (int i = 0; i < purpose_master.length(); i++) {
                        //Log.d("data",contacts);
                        JSONObject c = purpose_master.getJSONObject(i);
                        String Name= c.getString("purpose_desc");
                        Log.d("@@@@@@@data@@@@@",Name);


                        purposemaster1.add(Name);


                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // contactList.add("Person to Meet");

            ArrayAdapter<String>  adapter = new ArrayAdapter<String>(VisitorEntry.this, R.layout.spinner_item, personmeet1);


            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // adapter.add("Person to Meet");
            personmeet.setAdapter(adapter);
            // visitortype.setSelection(adapter.getCount());

            personmeet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    // TODO Auto-generated method stub

                    if(personmeet.getSelectedItem() == "Person to Meet")
                    {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);

                    }
                    else{

                        Toast.makeText(VisitorEntry.this, personmeet.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub

                }
            });


            personmeet.setAdapter(adapter);


            ArrayAdapter<String>  adapter1 = new ArrayAdapter<String>(VisitorEntry.this, R.layout.spinner_item, area1);


            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // adapter.add("Person to Meet");
            area.setAdapter(adapter1);
            // visitortype.setSelection(adapter.getCount());

            area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    // TODO Auto-generated method stub

                    if(area.getSelectedItem() == "Select Area")
                    {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);

                    }
                    else{

                        Toast.makeText(VisitorEntry.this, area.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub

                }
            });


            area.setAdapter(adapter1);

            ArrayAdapter<String>  adapter2 = new ArrayAdapter<String>(VisitorEntry.this, R.layout.spinner_item, purposemaster1);


            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // adapter.add("Person to Meet");
            visitpurpose.setAdapter(adapter2);
            // visitortype.setSelection(adapter.getCount());

            visitpurpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    // TODO Auto-generated method stub

                    if(visitpurpose.getSelectedItem() == "Select visit purpose")
                    {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);

                    }
                    else{

                        Toast.makeText(VisitorEntry.this, visitpurpose.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub

                }
            });


            visitpurpose.setAdapter(adapter2);


        }
    }
}

