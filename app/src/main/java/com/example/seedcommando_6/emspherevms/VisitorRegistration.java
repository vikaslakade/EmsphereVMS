package com.example.seedcommando_6.emspherevms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.Base64;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by seedcommando_10 on 1/18/2017.
 */

public class VisitorRegistration extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private static final int CAMERA_REQUEST = 1;
    ArrayList< String> visittype1=new ArrayList<String>();
    public static final String URL_GET_ALL = "http://seedmanagement.cloudapp.net/VMS_Mobile_Service/RegisterVisitorDetails.ashx";
    EditText f_name, l_name, mobile_no, email_id, idno, addrees, representing, remark;/*visitortype, idproof;*/
    Spinner visitortype, idproof;
    Button btn_next;
    Fields fields;
    HashMap<String, Object> hm;
    Bundle b;
    Toolbar toolbar;
    ImageView image;
    Bitmap photo;
    String encodedImage;
    ProgressDialog pd;
    Snackbar snackbar;
    LinearLayout linearLayout;
    HttpHandler sh = null;
    String jsonStr;
    String res_status=null;
    boolean connection1;
    String Id;
    String master_response;
    String res_message;
    String[] IDspinnerValue = {
            "Pancard",
            "Aadhar Card",
            "Voter Card",
            "other",
            "Please Select ID type"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.visit_registration);
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar = (Toolbar) findViewById(R.id.toolbar_visitreg);
            //setActionBar(toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            //toolbar.setNavigationIcon(R.drawable.ic_action_name);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        //for mandatory fields
        TextView Name_first=(TextView) findViewById(R.id.textView_name);
        String email1="First Name";
        String color="* ";
        SpannableStringBuilder builder1 = new SpannableStringBuilder();

        builder1.append(color);
        int start = builder1.length();
        builder1.append(email1);

        int end = builder1.length();

        builder1.setSpan(new ForegroundColorSpan(Color.RED), 0,start ,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        Name_first.setText(builder1);
        TextView Name_last=(TextView) findViewById(R.id.textView_lname);
        String lname="Last Name";
        String color1="* ";
        SpannableStringBuilder builder2 = new SpannableStringBuilder();

        builder2.append(color1);
        int start1 = builder2.length();
        builder2.append(lname);

       // int end1 = builder1.length();

        builder2.setSpan(new ForegroundColorSpan(Color.RED), 0,start1 ,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        Name_last.setText(builder2);

        TextView mobile_no1=(TextView) findViewById(R.id.textView_mobileno);
        String mobno="Mobile Number";
       // String color1="* ";
        SpannableStringBuilder builder3 = new SpannableStringBuilder();

        builder3.append(color1);
        int start3 = builder3.length();
        builder3.append(mobno);

        //int end = builder1.length();

        builder3.setSpan(new ForegroundColorSpan(Color.RED), 0,start3 ,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        mobile_no1.setText(builder3);
        TextView visittype=(TextView) findViewById(R.id.textView_visittype);
        String visit="Visit Type";
        // String color1="* ";
        SpannableStringBuilder builder4 = new SpannableStringBuilder();

        builder4.append(color1);
        int start4 = builder4.length();
        builder4.append(visit);

        //int end = builder1.length();

        builder4.setSpan(new ForegroundColorSpan(Color.RED), 0,start4 ,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        visittype.setText(builder4);
        TextView Idcardtype=(TextView) findViewById(R.id.textView_idcardtype);
        String idcard="ID Card Type";
        // String color1="* ";
        SpannableStringBuilder builder5 = new SpannableStringBuilder();

        builder5.append(color1);
        int start5 = builder5.length();
        builder5.append(idcard);

        //int end = builder1.length();

        builder5.setSpan(new ForegroundColorSpan(Color.RED), 0,start5 ,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        Idcardtype.setText(builder5);

        TextView Idcardno=(TextView) findViewById(R.id.textView_idcardnumber);
        String idcardno="ID Card Number";
        // String color1="* ";
        SpannableStringBuilder builder6 = new SpannableStringBuilder();

        builder6.append(color1);
        int start6 = builder6.length();
        builder6.append(idcardno);

        //int end = builder1.length();

        builder6.setSpan(new ForegroundColorSpan(Color.RED), 0,start6 ,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        Idcardno.setText(builder6);



        linearLayout=(LinearLayout) findViewById(R.id.activity_main);
        image=(ImageView) findViewById(R.id.imageView_reg);
       // cam=(ImageView) findViewById(R.id.imageView_camera);
        f_name = (EditText) findViewById(R.id.editText_name);
        l_name = (EditText) findViewById(R.id.editText_lname);
        mobile_no = (EditText) findViewById(R.id.editText_mobileno);
        email_id = (EditText) findViewById(R.id.editText_emailid);
        addrees = (EditText) findViewById(R.id.editText1_address);
        representing = (EditText) findViewById(R.id.editTextreprenting);
        remark = (EditText) findViewById(R.id.editText_remark);
        idno = (EditText) findViewById(R.id.editText_idcardnumber);
       // visitortype = (EditText) findViewById(R.id.editText_visittype);
        visitortype = (Spinner) findViewById(R.id.editText_visittype);

        idproof = (Spinner) findViewById(R.id.editText_idcardtype);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, IDspinnerValue) {

            @Override
            public int getCount() {
                int c = super.getCount();
                return c > 0 ? c - 1 : c;
                //if (visitortype.getSelectedItemPosition() < c - 1) return c;
                // return c > 0 ? c - 1 : c;
                //return count > 0 ? count - 1 : count;
            }
        };


        //visitortype.setPrompt("Select your favorite Planet!");
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        idproof.setAdapter(adapter1);
        idproof.setSelection(adapter1.getCount());

        idproof.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                if( idproof.getSelectedItem() == "Please Select ID type")
                {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);

                }
                else{

                    Toast.makeText(VisitorRegistration.this,  idproof.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        btn_next = (Button) findViewById(R.id.button_next);
        // btnnext.setOnClickListener(this);
        image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v ) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra("android.intent.extras.CAMERA_FACING",1);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        if(ConnectionCheck.isNetworkAvailable(getApplicationContext())) {
            new GetDetails().execute();
        }else {
            Toast.makeText(getApplicationContext(),"No internet connection.your device is not connected to internet",Toast.LENGTH_LONG).show();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {

            if (requestCode == CAMERA_REQUEST) {
                photo = (Bitmap) data.getExtras().get("data");
                RoundedBitmapDrawable conv_bm1 = RoundImage.getRounded(photo);
                //photo=(Bitmap) data.getExtras().get("data");
                image.setImageDrawable(conv_bm1);
               // image.setImageBitmap(photo);

            } else {
                Toast.makeText(VisitorRegistration.this, "please take photo again", Toast.LENGTH_LONG).show();
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void passdata(View view) {
        if(master_response.equals("success")) {
            getdata();
        }else {
            Toast.makeText(VisitorRegistration.this,"Wait... json data downloading",Toast.LENGTH_LONG).show();
        }

    }
    public void getdata() {
        String name = f_name.getText().toString();
        String last = l_name.getText().toString();
        String mno = mobile_no.getText().toString().trim();
       String email=email_id.getText().toString().trim();
        long vistype=visitortype.getSelectedItemId();
         String vistype1=visitortype.getSelectedItem().toString();
        String idprof=idproof.getSelectedItem().toString();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            try {
                if(photo !=null) {
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                    encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                }else {

                  Toast toastphoto=  Toast.makeText(VisitorRegistration.this,"please take a photo",Toast.LENGTH_LONG);
                    toastphoto.setGravity(Gravity.CENTER, 0, 0);
                    toastphoto.show();
                }
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
        String idnumber=idno.getText().toString();
    if(Utility.isNotNull(name)&&Utility.isNotNull(last)&&Utility.isNotNull(mno)
            &&Utility.isNotNull(vistype1)&&Utility.spinnervalid(vistype1)&&Utility.isNotNull(idprof)&&Utility.spinnervalid(idprof)&&Utility.isNotNull(idnumber)&&Utility.isNotNull(encodedImage))
    {
        if (mno.length()==10) {
        try {
            //data set to feild class
            fields = new Fields();
            fields.setFirstname(name);
            fields.setLastname(last);
            fields.setMobile(mno);
            fields.setEmailId(email);
            fields.setRepresenting(representing.getText().toString());
            fields.setRemark(remark.getText().toString());
            fields.setAddress(addrees.getText().toString());
            fields.setIdcardnumber(idnumber);
            fields.setIdcardtype(idprof);
           fields.setVisitTypes(vistype);
            // send Image as string
            fields.setImage(encodedImage);
           }catch (Exception e){
               e.printStackTrace();
           }
            // Toast.makeText(this," next",Toast.LENGTH_LONG).show();
            hm = new HashMap<String, Object>();
            hm.put("data", fields);

            //Bundle object is created
            b = new Bundle();
            b.putSerializable("bundleobj", hm);
            /*Intent inew = new Intent(getApplicationContext(), VisitorEntry.class);
            inew.putExtras(b);
            // System.out.println("data is"+inew);
            startActivity(inew);*/
          if(ConnectionCheck.isNetworkAvailable(getApplicationContext())) {
              new SendPostRequest().execute();
          }else {
              Toast.makeText(getApplicationContext(),"No internet connection.your device is not connected to internet",Toast.LENGTH_LONG).show();
          }

        } else {
          Toast toastmb=  Toast.makeText(getApplicationContext(), "Please enter valid mobile number", Toast.LENGTH_LONG);
            toastmb.setGravity(Gravity.CENTER, 0, 0);
            toastmb.show();
        }
    }
    else{
       Toast toast= Toast.makeText(getApplicationContext(), "Please fill all manadatory fields", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){
            pd = new ProgressDialog(VisitorRegistration.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... arg0) {
            URL url;
            HttpURLConnection connection = null;

              try {

                  int timeout = 15000;
                  connection1 = ConnectionCheck.isConnectedToServer(URL_GET_ALL, timeout);
                  // System.out.println("response code"+conn.getResponseCode());
                  Log.d("connection@@#@@@@", "" + connection1);

                  if (connection1) {
                      Log.d("connection@@#@@@@", "gugugu");

                      try {
                          url = new URL(URL_GET_ALL);


                          connection = (HttpURLConnection) url.openConnection();
                         // Log.d("1", "gugugu");
                          connection.setRequestMethod("POST");
                          connection.setRequestProperty("Accept", "application/json");
                          connection.setRequestProperty("Content-Type", "application/json");
                          connection.setRequestProperty("Content-Language", "en-US");
                         // Log.d("2", "gugugu");
                          connection.setUseCaches(false);
                          connection.setDoInput(true);
                          connection.setDoOutput(true);
                          connection.connect();
                          JSONObject jsonObject=new JSONObject();
                         // Log.d("3", "gugugu");
                          //Log.d("4", "gugugu");
                          jsonObject.put("fname", fields.getFirstname());
                         // Log.d("5", "gugugu");
                          jsonObject.put("Lastname", fields.getLastname());
                         // Log.d("6", "gugugu");
                          jsonObject.put("mobile", fields.getMobile());
                         // Log.d("7", "gugugu");
                          jsonObject.put("image", " ");
                          //Log.d("8", "gugugu");
                          jsonObject.put("EmailId", fields.getEmailId());
                          //Log.d("9", "gugugu");
                          jsonObject.put("visittype", fields.getVisitTypes().toString());
                         // Log.d("3", "gugugu");
                          jsonObject.put("Idcardtype", fields.getIdcardtype());
                         // Log.d("3", "gugugu");
                          jsonObject.put("Idcardnumber", fields.getIdcardnumber());
                         // Log.d("3", "gugugu");
                          jsonObject.put("Address", fields.getAddress());
                         // Log.d("3", "gugugu");
                          jsonObject.put("Representing", fields.getRepresenting());
                         // Log.d("3", "gugugu");
                          jsonObject.put("Remark", fields.getRemark());
                         // Log.d("3", "gugugu");


                        //Log.e("params", jsonObject.toString());

                          OutputStream os = connection.getOutputStream();
                          BufferedWriter writer = new BufferedWriter(
                                  new OutputStreamWriter(os, "UTF-8"));

                           writer.write(jsonObject.toString());
                         // Log.e("Write", writer.toString());

                          writer.flush();
                          writer.close();
                          os.close();

                          int responseCode = connection.getResponseCode();
                           // Log.d("response code "," "+responseCode);
                          if (responseCode == HttpsURLConnection.HTTP_OK) {
                              sh = new HttpHandler();
                              jsonStr = sh.makeServiceCall(connection);
                              //Log.e("qwoiopoipjlkljbhcgdvf", jsonStr);
                              JSONObject jsonObj = new JSONObject(jsonStr);
                              //String res_message=jsonObj.getString("response_message");
                              res_status = jsonObj.getString("response_status");
                               res_message=jsonObj.getString("response_message");
                             // Log.d("Status ",res_status);
                              Id = jsonObj.getString("visitor_registration_id");

                              //System.out.println("@@@@@##########" + connection.getResponseCode());
                          } else {
                              return new String("false : " + responseCode);
                          }
                      } catch (MalformedURLException e) {
                          e.printStackTrace();
                      } catch (Exception e) {
                          return new String("Exception: " + e.getMessage());
                      }
                  }else {
                      return null;
                  }


            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (pd.isShowing()) {
                pd.dismiss();
            }
            if (connection1) {
                if (res_status.equals("success")) {
                    try {
                       /* if (pd.isShowing()) {
                            pd.dismiss();
                        }*/
                        Toast.makeText(VisitorRegistration.this,"data added successfully",Toast.LENGTH_LONG).show();
                        //hm.put("id",Id);
                        Intent inew = new Intent(getApplicationContext(), VisitorEntry.class);
                        inew.putExtra("Id",Id);
                        inew.putExtras(b);
                        startActivity(inew);

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } else {
                    //Initializing snackbar using Snacbar.make() method

                    snackbar = Snackbar.make(linearLayout, " Data not added,  Reason:"+res_message, Snackbar.LENGTH_LONG);
                    //Displaying the snackbar using the show method()
                    snackbar.show();
                }
            }
            else {
                Toast.makeText(VisitorRegistration.this, "server not Running,Please try after some time", Toast.LENGTH_LONG).show();
            }


        }
    }
    private class GetDetails extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(VisitorRegistration.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

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
                    master_response = jsonObj.getString("response_status");
                   // Log.e("response_status", master_response);
                    // Getting JSON Array node
                    JSONArray visittypes = jsonObj.getJSONArray("visitor_type_master");
                    visittype1.add("Select visit type");
                    for (int i = 0; i < visittypes.length(); i++) {
                        //Log.d("data",contacts);
                        JSONObject c = visittypes.getJSONObject(i);
                        String visits = c.getString("visitor_code");
                       // Log.e("@@@@@@@data@@@@@", visits);
                        visittype1.add(visits);
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
            try {
                if (master_response.equals("success")) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(VisitorRegistration.this, R.layout.spinner_item, visittype1);//{

               /* @Override
                public int getCount() {
                    int c = super.getCount();
                    return c > 0 ? c - 1 : c;

                }
            };*/
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // adapter.add("Person to Meet");
                    visitortype.setAdapter(adapter);
                    // visitortype.setSelection(adapter.getCount());

                    visitortype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view,
                                                   int position, long id) {
                            // TODO Auto-generated method stub

                            if (visitortype.getSelectedItem() == "Select visit type") {
                                ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);

                            } else {

                                Toast.makeText(VisitorRegistration.this, visitortype.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub

                        }
                    });


                    visitortype.setAdapter(adapter);


                } else {
                    Toast.makeText(VisitorRegistration.this, "json data not get", Toast.LENGTH_LONG).show();
                }
            }catch (NullPointerException ne){
                ne.printStackTrace();
            }
        }
    }

}

