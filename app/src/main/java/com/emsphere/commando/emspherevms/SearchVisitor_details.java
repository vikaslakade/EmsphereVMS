package com.emsphere.commando.emspherevms;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.emsphere.commando.emspherevms.pojos.CommanResponsePojo;
import com.emsphere.commando.emspherevms.pojos.Expc_Vstr_Details;
import com.emsphere.commando.emspherevms.pojos.ExpectedVisitorPoJo;
import com.emsphere.commando.emspherevms.pojos.ExpectedVisitorSendData;
import com.emsphere.commando.emspherevms.pojos.VisitorSendData;
import com.emsphere.commando.emspherevms.rest.ApiClient;
import com.emsphere.commando.emspherevms.rest.ApiInterface;
import com.loopj.android.http.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import app.Config;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by seedcommando_10 on 1/19/2017.
 */

public class SearchVisitor_details extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
   // ArrayList< String> personmeet1=new ArrayList<String>();
    ArrayList< String>  names=new ArrayList<String>();
    ArrayList< String> ids=new ArrayList<String>();
    ArrayList< String>  selectarea=new ArrayList<String>();
    ArrayList< String> selectids=new ArrayList<String>();
    ArrayList< String>  selectpurpose=new ArrayList<String>();
    ArrayList< String> selectpurposeids=new ArrayList<String>();
   // String[] names;
    //String[] ids;
    //ArrayList< String> area1=new ArrayList<String>();
   // ArrayList< String> purposemaster1=new ArrayList<String>();
    TextView visitor_name, mobilenumber,reprents;
    ImageView iv;
    EditText  time, duration, material1, material2, serialno1, serialno2; /*visitpurpose;*/
     Spinner visitpurpose,personmeet,area;//,personmeet,area;
    Button submit;
    Fields fields;
    Bundle bn;
    HashMap<String, Object> getobj;
    Fields d;
    String fname, mbno, lname,re_prents;
    android.support.v7.widget.Toolbar t4;
    Spinner spinner;
    Bitmap bitmap;
    LinearLayout linearLayout;
    ProgressDialog pd;
    HttpHandler sh = null;
    String jsonStr;
    String res_status=null;
    boolean connection;
    String id;
    Snackbar snackbar;
    String response_message;
    Fields Notifields;
    String visit_purpose;
    String person_to_meet_id;
    String[] spinnerValue = {
            "Employee",
            "Interviewee",
            "security",
            "other",
            "Please Select Visit type"
    };
    SharedPreferences preferences;
    LinearLayout meet_to_person,validate;
    String usertype;
    String img_vE;
    String Empoyee_Id;
    private ApiInterface apiService;
    ProgressDialog pd1;
    Dialog    dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.visit_details);
            linearLayout=(LinearLayout) findViewById(R.id.linear_layout);
            personmeet = (Spinner) findViewById(R.id.persontomeet);
            duration = (EditText) findViewById(R.id.editText_duration);
            mobilenumber = (TextView) findViewById(R.id.mbn);
            material1 = (EditText) findViewById(R.id.editText_materialno1);
            material2 = (EditText) findViewById(R.id.editText_materialno2);
            area = (Spinner) findViewById(R.id.editText_area);
            serialno1 = (EditText) findViewById(R.id.editText_serialno1);
            serialno2 = (EditText) findViewById(R.id.editText_serialno2);
            submit = (Button) findViewById(R.id.visitor_entry_submit);
            visitor_name = (TextView) findViewById(R.id.textView5);
            iv = (ImageView) findViewById(R.id.imageView3);
            time = (EditText) findViewById(R.id.editText_time);
            visitpurpose = (Spinner) findViewById(R.id.visit_purpose);
            validate=(LinearLayout)findViewById(R.id.validate);
            validate.setVisibility(View.GONE);
            apiService = ApiClient.getClient().create(ApiInterface.class);
            //getExpectedVisitor();
            //mandotory fields
            TextView visitpurpose=(TextView) findViewById(R.id.textView10);
            String v_porpose="Visit Purpose";
             String color1="* ";
            SpannableStringBuilder builder1 = new SpannableStringBuilder();
            builder1.append(color1);
            int start1 = builder1.length();
            builder1.append(v_porpose);
            builder1.setSpan(new ForegroundColorSpan(Color.RED), 0,start1 ,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            visitpurpose.setText(builder1);
            TextView meet=(TextView) findViewById(R.id.textView_personmeet);
            String meetperson="Person To Meet";
            SpannableStringBuilder builder2 = new SpannableStringBuilder();
            builder2.append(color1);
            int start2 = builder2.length();
            builder2.append(meetperson);
            builder2.setSpan(new ForegroundColorSpan(Color.RED), 0,start2 ,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            meet.setText(builder2);
            TextView time1=(TextView) findViewById(R.id.textView_time1);
            String time_name="Time";
            SpannableStringBuilder builder3 = new SpannableStringBuilder();
            builder3.append(color1);
            int start3 = builder3.length();
            builder3.append(time_name);
            builder3.setSpan(new ForegroundColorSpan(Color.RED), 0,start3 ,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            time1.setText(builder3);
            TextView duration=(TextView) findViewById(R.id.duration);
            String duration_name="Duration(hr)";
            SpannableStringBuilder builder4 = new SpannableStringBuilder();
            builder4.append(color1);
            int start4 = builder4.length();
            builder4.append(duration_name);
            builder4.setSpan(new ForegroundColorSpan(Color.RED), 0,start4 ,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            duration.setText(builder4);
            //set date to text
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.US);
            Calendar c = Calendar.getInstance();
            String timetime=sdf.format(c.getTime());
            DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
            String _time=dateFormat.format(Calendar.getInstance().getTime());
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateFormat.parse(_time));
            cal.add(Calendar.MINUTE, 10);
            String newTime =dateFormat.format(cal.getTime());
            time.setText(timetime+" "+newTime);
            t4 = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar3);
            setSupportActionBar(t4);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            bn = new Bundle();
            bn = getIntent().getExtras();
            getobj = new HashMap<String, Object>();
            d = (Fields) bn.getSerializable("bundleobj");
           // d = (Fields) getobj.get("data");
            //String f= (String) getobj.get("Id");
            fname = d.getFirstname();
            lname = d.getLastname();
            visitor_name.setText(fname+" "+lname);
            mbno = d.getMobile();
            mobilenumber.setText(mbno);
            reprents=(TextView) findViewById(R.id.textView_reprents);
            re_prents=d.getRepresenting();
            reprents.setText(re_prents);
            img_vE = d.getImage();
            //retrieve and set to bitmap
            /*if(img_vE.isEmpty()) {*/
                //byte[] encodeByte = Base64.decode(img_vE, Base64.DEFAULT);
                //bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
               // RoundedBitmapDrawable conv_bm = RoundImage.getRoundedBitmap(bitmap);
               // iv.setImageDrawable(conv_bm);
            Bitmap bitmap1 = RoundImage.getCircularBitmap(SearchExistingVisitor.getBitmapFromMemCache("image"), 5);
            iv.setImageBitmap(bitmap1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if(ConnectionCheck.isNetworkAvailable(getApplicationContext())) {
            new GetDetails().execute();
        }else {
            Toast.makeText(getApplicationContext(),"No Internet connection. Your device is currently not connected to the Internet",Toast.LENGTH_LONG).show();
        }

        preferences=getSharedPreferences(Config.SHARED_PREF,0);
           try {
               Empoyee_Id = preferences.getString("employee_id", "");
               usertype = preferences.getString("is_admin", "");
               if (usertype.equals("0")) {
                   meet_to_person = (LinearLayout) findViewById(R.id.meet_to_person);
                   //validate=(LinearLayout) findViewById(R.id.validate);
                   meet_to_person.setVisibility(View.GONE);
                   // validate.setVisibility(View.GONE);
               }
           }catch (Exception ex){
               ex.printStackTrace();
           }
    }

    public void getdata() {
        String du_ration = duration.getText().toString();
        String person_meet = personmeet.getSelectedItem().toString();
        String visit_purpose = visitpurpose.getSelectedItem().toString();
        String ti_me = time.getText().toString();
        String are_a = area.getSelectedItem().toString();
        if (Utility.isNotNull(du_ration)&&Utility.spinnervalid(visit_purpose) && Utility.isNotNull(visit_purpose) && Utility.isNotNull(ti_me)) {

            if(usertype.equals("1")) {
                if (Utility.isNotNull(person_meet) && Utility.spinnervalid(person_meet)) {
        fields = new Fields();
        fields.setDuration(duration.getText().toString());
        fields.setPersonmeet(Long.parseLong(ids.get(personmeet.getSelectedItemPosition())));
        fields.setMaterial1(material1.getText().toString());
        fields.setMaterial2(material2.getText().toString());
                    if(area.getSelectedItem().equals("Select Area")){
                        fields.setArea1(Long.parseLong("0"));
                    }else {
                        fields.setArea1(Long.parseLong(selectids.get(area.getSelectedItemPosition())));
                    }
       // fields.setArea1(Long.parseLong(selectids.get(area.getSelectedItemPosition())));

        fields.setSerialNo1(serialno1.getText().toString());
        fields.setSerialNo2(serialno2.getText().toString());

        fields.setVisitpupose(Long.parseLong(selectpurposeids.get(visitpurpose.getSelectedItemPosition())));
        fields.setTime(time.getText().toString());
                } else {
                    Toast.makeText(SearchVisitor_details.this,"Please fill all manadatory fields",Toast.LENGTH_LONG).show();
                }
            }else {
                fields = new Fields();
                fields.setDuration(duration.getText().toString());
                fields.setPersonmeet(Long.parseLong(Empoyee_Id));
                //fields.setPersonmeet(Long.parseLong(" "));
                fields.setMaterial1(material1.getText().toString());
                fields.setMaterial2(material2.getText().toString());
                if(area.getSelectedItem().equals("Select Area")){
                    fields.setArea1(Long.parseLong("0"));
                }else {
                    fields.setArea1(Long.parseLong(selectids.get(area.getSelectedItemPosition())));
                }
               // fields.setArea1(Long.parseLong(selectids.get(area.getSelectedItemPosition())));
                fields.setSerialNo1(serialno1.getText().toString());
                fields.setSerialNo2(serialno2.getText().toString());
                fields.setVisitpupose(Long.parseLong(selectpurposeids.get(visitpurpose.getSelectedItemPosition())));
                fields.setTime(time.getText().toString());
            }

            if(ConnectionCheck.isNetworkAvailable(getApplicationContext())) {
               // new SendPostRequest().execute();
                if(ViewDialog.sharedPref.contains("LocationName")) {
                    getExpectedVisitor();
                }else {
                    ViewDialog.alertdialog("Please set location configuration",SearchVisitor_details.this);
                }
            }else {
                Toast.makeText(getApplicationContext(),"No Internet connection. Your device is currently not connected to the Internet",Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(SearchVisitor_details.this,"Please fill all manadatory fields",Toast.LENGTH_LONG).show();
        }
    }
    public void visitorEntrySubmit(View v) {
        getdata();
    }
    public class SendPostRequest extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            pd = new ProgressDialog(SearchVisitor_details.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }
        protected String doInBackground(String... arg0) {
            try {
                 URL url = new URL(Urls.URL_SaveVisitorEntryDetails);
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
                postDataParams.put("VisitorRegistrationId", d.getVisitorRegistrationId());
                postDataParams.put("duration", fields.getDuration());
                postDataParams.put("material1", fields.getMaterial1());
                postDataParams.put("material2", fields.getMaterial2());
                postDataParams.put("persontomeet", fields.getPersonmeet().toString());
                postDataParams.put("serialNo1", fields.getSerialNo1());
                postDataParams.put("serialNo2", fields.getSerialNo2());
                postDataParams.put("Area", fields.getArea1().toString());
                postDataParams.put("time", fields.getTime());
                postDataParams.put("Visitpurpose", fields.getVisitpupose().toString());
                Log.e("params123", postDataParams.toString());
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
            Toast.makeText(getApplicationContext(), res_status+":"+response_message, Toast.LENGTH_LONG).show();
            //System.out.println(result);
            if (res_status.equals("success")) {
                JSONObject data = null;
                //System.out.println(result);
                try {
                        try {
                            data = new JSONObject();
                            data.put("name", fname);
                            data.put("lname", lname);
                            data.put("image", img_vE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    //ViewDialog alert = new ViewDialog(getApplicationContext());
                        showDialog( data.toString());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } else {
                    //Initializing snackbar using Snacbar.make() method
                    snackbar = Snackbar.make(getCurrentFocus(), "Data not added,Reason:"+response_message, Snackbar.LENGTH_LONG);
                    //Displaying the snackbar using the show method()
                    snackbar.show();
                }
            }
        }

    public void getExpectedVisitor() {
        pd1 = new ProgressDialog(SearchVisitor_details.this);
        pd1.setMessage("Loading....");
        pd1.setCanceledOnTouchOutside(false);
        pd1.show();

        VisitorSendData jsonObject = new  VisitorSendData();
        try {
            jsonObject.setArea( fields.getArea1().toString());
            jsonObject.setDuration(fields.getDuration());
            jsonObject.setMaterial1(fields.getSerialNo1());
            jsonObject.setVisitorRegistrationId( d.getVisitorRegistrationId());
            jsonObject.setMaterial2(fields.getMaterial2());
            jsonObject.setPersontomeet(fields.getPersonmeet().toString());
            jsonObject.setSerialNo1( fields.getSerialNo1());
            jsonObject.setSerialNo2(fields.getSerialNo2());

            jsonObject.setTime(fields.getTime());
            jsonObject.setVisitpurpose(fields.getVisitpupose().toString());
            jsonObject.setLocation_id(ViewDialog.get_session("LocationId"));
            jsonObject.setCode_id("0");
        } catch (Exception e) {
            e.printStackTrace();
        }



        retrofit2.Call<CommanResponsePojo> call = apiService.sendExpectedVisitor(jsonObject);
        call.enqueue(new Callback<CommanResponsePojo>() {
            @Override
            public void onResponse(retrofit2.Call<CommanResponsePojo> call, Response<CommanResponsePojo> response) {
                pd1.dismiss();
                //vList.clear();
                JSONObject data = null;

                if (response.isSuccessful()) {
                    //Log.d("User ID1: ", response.body().toString());
                    if (response.body().getResponse_status().equals("success")) {




                        try {
                            try {
                                data = new JSONObject();
                                data.put("name", fname);
                                data.put("lname", lname);
                                //data.put("image", SearchExistingVisitor.getBitmapFromMemCache("image"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //ViewDialog alert = new ViewDialog(getApplicationContext());
                            showDialog( data.toString());
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }




                        //startActivity(new Intent(MainActivity.this, ManagerActivity.class));

                    } else {
                        ViewDialog.alertdialog(response.body().getResponse_message(), SearchVisitor_details.this);

                    }
                } else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            ViewDialog.alertdialog("File or directory not found", SearchVisitor_details.this);
                            break;
                        case 500:
                            ViewDialog.alertdialog("server broken", SearchVisitor_details.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            ViewDialog.alertdialog("unknown error", SearchVisitor_details.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

            }

            @Override
            public void onFailure(retrofit2.Call<CommanResponsePojo> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                pd1.dismiss();


            }
        });
    }

    private class GetDetails extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SearchVisitor_details.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
            //Toast.makeText(SearchVisitor_details.this,"Json Data is downloading",Toast.LENGTH_LONG).show();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler_spinner sh = new HttpHandler_spinner();
            String jsonStr = sh.makeServiceCall(Urls.URL_GetMasterDataList);
            //Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    JSONArray prsontomeet = jsonObj.getJSONArray("person_to_meet");
                    // names=new String[prsontomeet.length()+1];
                    // ids=new String[prsontomeet.length()+1];
                    //names[0]="Person to Meet";
                    //ids[0]="0";
                    // Getting JSON Array node
                    JSONArray Area = jsonObj.getJSONArray("area_master");
                    JSONArray purpose_master = jsonObj.getJSONArray("purpose_master");
                   // personmeet1.add("Person to Meet");
                   names.add("Person to Meet");
                    ids.add("Person to Meet");
                    for (int i = 0; i < prsontomeet.length(); i++) {
                        JSONObject c = prsontomeet.getJSONObject(i);
                        String id=c.getString("person_Id");
                        String Name= c.getString("person_Name");
                       // personmeet1.add(Name);
                        names.add(c.getString("person_Name"));
                        ids.add(c.getString("person_Id"));
                    }
                    selectarea.add("Select Area");
                    selectids.add("Select Area");
                    for (int i = 0; i < Area.length(); i++) {
                        JSONObject c = Area.getJSONObject(i);
                        String Name= c.getString("area_name");
                       // Log.d("@@@@@@@data@@@@@",Name);
                       // area1.add(Name);
                        selectarea.add(c.getString("area_name"));
                        selectids.add(c.getString("area_Id"));
                    }
                    selectpurpose.add("Select visit purpose");
                    selectpurposeids.add("Select visit purpose");
                    for (int i = 0; i < purpose_master.length(); i++) {
                        //Log.d("data",contacts);
                        JSONObject c = purpose_master.getJSONObject(i);
                        String Name= c.getString("purpose_desc");
                       // Log.d("@@@@@@@data@@@@@",Name);
                        selectpurpose.add(c.getString("purpose_desc"));
                        selectpurposeids.add(c.getString("purpose_Id"));
                        //purposemaster1.add(Name);
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
            if (pd.isShowing()) {
                pd.dismiss();
            }
            ArrayAdapter<String>  adapter = new ArrayAdapter<String>(SearchVisitor_details.this, R.layout.spinner_item, names);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            personmeet.setAdapter(adapter);
            personmeet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    // TODO Auto-generated method stub
                    if(personmeet.getSelectedItem() == "Person to Meet")
                    {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
                    }
                    /*else{
                        Toast.makeText(SearchVisitor_details.this, personmeet.getSelectedItem().toString()+ids.get(position), Toast.LENGTH_LONG).show();
                    }*/
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });
            personmeet.setAdapter(adapter);
            ArrayAdapter<String>  adapter1 = new ArrayAdapter<String>(SearchVisitor_details.this, R.layout.spinner_item, selectarea);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter1);
            area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    // TODO Auto-generated method stub
                    if(area.getSelectedItem() == "Select Area")
                    {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
                    }
                   /* else{
                        Toast.makeText(SearchVisitor_details.this, area.getSelectedItem().toString()+selectids.get(position), Toast.LENGTH_LONG).show();
                    }*/
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                    }
            });
            area.setAdapter(adapter1);
            ArrayAdapter<String>  adapter2 = new ArrayAdapter<String>(SearchVisitor_details.this, R.layout.spinner_item, selectpurpose);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            visitpurpose.setAdapter(adapter2);
            visitpurpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    // TODO Auto-generated method stub
                    if(visitpurpose.getSelectedItem() == "Select visit purpose")
                    {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
                    }
                    /*else{
                        Toast.makeText(SearchVisitor_details.this, visitpurpose.getSelectedItem().toString()+selectpurposeids.get(position), Toast.LENGTH_LONG).show();
                    }*/
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });
            visitpurpose.setAdapter(adapter2);
        }
    }
    public class SendPostNotificationRequest extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            pd = new ProgressDialog(SearchVisitor_details.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }
        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL("http://seedmanagement.cloudapp.net/VMS_Mobile_Service/CheckVisitorAuthorization.ashx");
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

                postDataParams.put("employee_id", arg0[0]);
                postDataParams.put("visitor_reg_id", getobj.get("Id"));
                postDataParams.put("person_to_meet_id", person_to_meet_id);
                postDataParams.put("purpose_to_visit", visit_purpose);

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
            Toast.makeText(getApplicationContext(), res_status + ":" + response_message, Toast.LENGTH_LONG).show();
            //System.out.println(result);
            try {
                if (res_status.equals("success")) {
                    //JSONObject data = null;
                    //System.out.println(result);
                    Toast.makeText(getApplicationContext(), response_message, Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), response_message, Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void getNotificationData(){
         Notifields=new Fields();
        Notifields.setPersonmeet(personmeet.getSelectedItemId());
         visit_purpose = visitpurpose.getSelectedItem().toString();
    }

    public void showDialog( String data){
        // System.out.println("@@@@#####@##@#@#"+data);
        try{
           dialog = new Dialog(this);
            //Dialog dialog1 = new Dialog(mContext, R.style.FullHeightDialog);
            // Window window = dialog.getWindow();
            //window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            //dialog.getWindow().setLayout(LinearLayoutCompat.LayoutParams.FILL_PARENT, LinearLayoutCompat.LayoutParams.FILL_PARENT);
            // Window window = dialog.getWindow();
            // window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.setContentView(R.layout.dialogs);
            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());
            ImageView imageView=(ImageView) dialog.findViewById(R.id.imageView3);
            TextView textView = (TextView) dialog.findViewById(R.id.textView8);
            // CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.checkBox2);
            TextView name = (TextView) dialog.findViewById(R.id.textView);
            try {
              JSONObject  obj = new JSONObject(data);
                //System.out.println("@@@@#####@##@#@#"+obj);
                //Fields d = (Fields)obj .get("data");
                // String nm=d.getFirstname();
                String nm=obj.getString("name");
                //String lnm=d.getLastname();
                String lnm=obj.getString("lname");
                name.setText(nm+" "+lnm);
                //String img_vE = obj.getString("image");
                Bitmap bitmap1 = RoundImage.getCircularBitmap(SearchExistingVisitor.getBitmapFromMemCache("image"), 5);
                imageView.setImageBitmap(bitmap1);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            // checkBox.setChecked(true);
            SimpleDateFormat df1 = new SimpleDateFormat("EEEE,dd-MMM-yyyy");
            String formattedDate1 = df1.format(c.getTime());
            //AlertDialog.Builder builder=new AlertDialog.Builder(this);
           // builder.setCancelable(true);
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            String formattedDate = df.format(c.getTime());
            textView.setText(formattedDate1+"\n"+formattedDate);
            TextView text = (TextView) dialog.findViewById(R.id.textView12);
            text.setText("Submitted Succesfully");
            Button dialogButton = (Button) dialog.findViewById(R.id.button2);
            dialog.show();
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preferences=getSharedPreferences(Config.SHARED_PREF,0);
                    usertype=preferences.getString("is_admin","");
                    if(usertype.equals("1")) {
                        Intent intent = new Intent(getApplicationContext(), Home_activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        dialog.dismiss();
                        finish();
                        startActivity(intent);
                        // activity.finishAffinity();

                    }else {
                        Intent intent = new Intent(SearchVisitor_details.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        dialog.dismiss();
                        finish();
                       startActivity(intent);

                        // activity.finishAffinity();

                    }
                }
            });

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(dialog !=null){
            dialog.dismiss();
        }
    }
}
