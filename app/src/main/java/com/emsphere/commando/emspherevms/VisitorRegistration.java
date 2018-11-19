package com.emsphere.commando.emspherevms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
import com.emsphere.commando.emspherevms.pojos.NewRegistrationPojo;
import com.emsphere.commando.emspherevms.pojos.NewRegistrationResponsePojo;
import com.emsphere.commando.emspherevms.pojos.UpdateNewVisitor;
import com.emsphere.commando.emspherevms.pojos.ValidateVisitOrInviteCodePojo;
import com.emsphere.commando.emspherevms.pojos.VisitorSendData;
import com.emsphere.commando.emspherevms.pojos.Vstr_entry_dtls;
import com.emsphere.commando.emspherevms.pojos.Vstr_reg_dtls;
import com.emsphere.commando.emspherevms.rest.ApiClient;
import com.emsphere.commando.emspherevms.rest.ApiInterface;
import com.loopj.android.http.Base64;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by seedcommando_10 on 1/18/2017.
 */

public class VisitorRegistration extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private static final int CAMERA_REQUEST = 1;
    ArrayList<String> visittype1 = new ArrayList<String>();
    ArrayList<String> selectvisittype = new ArrayList<String>();
    ArrayList<String> selectvisittypeids = new ArrayList<String>();
    // public static final String URL_GET_ALL = "http://seedmanagement.cloudapp.net/VMS_Mobile_Service/RegisterVisitorDetails.ashx";
    EditText f_name, l_name, mobile_no, email_id, idno, addrees, representing, remark;/*visitortype, idproof;*/
    Spinner visitortype, idproof;
    Button btn_next;
    Fields fields;
    HashMap<String, Object> hm;
    Bundle b;
    Toolbar toolbar;
    ImageView image;
    Bitmap photo=null,photo1=null;
    String encodedImage;
    Snackbar snackbar;
    LinearLayout linearLayout;
    HttpHandler sh = null;
    String res_status = null, jsonStr, Id, master_response, res_message, vstr_reg_Id="", vsre_enty_id="";
    boolean connection1;
    String visitortypeId="",ApTime="",Areaid="",personid="";
    ArrayList<String> arrayList = new ArrayList<String>();

    /*{
            "Pancard",
            "Aadhar Card",
            "Voter Card",
            "other",
            "Please Select ID type"
    };*/
    private ApiInterface apiService;
    ProgressDialog pd1, pd;
    Bundle bundle;
    int codeid=0;
    ArrayList<Vstr_entry_dtls> vstr_entry_dtls;
    byte[] encodeByte=null;
    HashMap<String, Object> hashMap;

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
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        arrayList.add("Pancard");
        arrayList.add("Aadhar Card");
        arrayList.add("Voter Card");
        arrayList.add("other");
        arrayList.add("Please Select ID type");

        apiService = ApiClient.getClient().create(ApiInterface.class);
        //for mandatory fields

        TextView Name_first = (TextView) findViewById(R.id.textView_name);
        String email1 = "First Name";
        String color = "* ";
        SpannableStringBuilder builder1 = new SpannableStringBuilder();
        builder1.append(color);
        int start = builder1.length();
        builder1.append(email1);
        int end = builder1.length();
        builder1.setSpan(new ForegroundColorSpan(Color.RED), 0, start,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Name_first.setText(builder1);
        TextView Name_last = (TextView) findViewById(R.id.textView_lname);
        String lname = "Last Name";
        String color1 = "* ";
        SpannableStringBuilder builder2 = new SpannableStringBuilder();
        builder2.append(color1);
        int start1 = builder2.length();
        builder2.append(lname);
        builder2.setSpan(new ForegroundColorSpan(Color.RED), 0, start1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Name_last.setText(builder2);
        TextView mobile_no1 = (TextView) findViewById(R.id.textView_mobileno);
        String mobno = "Mobile Number";
        SpannableStringBuilder builder3 = new SpannableStringBuilder();
        builder3.append(color1);
        int start3 = builder3.length();
        builder3.append(mobno);
        builder3.setSpan(new ForegroundColorSpan(Color.RED), 0, start3,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mobile_no1.setText(builder3);
        TextView visittype = (TextView) findViewById(R.id.textView_visittype);
        String visit = "Visit Type";
        SpannableStringBuilder builder4 = new SpannableStringBuilder();
        builder4.append(color1);
        int start4 = builder4.length();
        builder4.append(visit);
        builder4.setSpan(new ForegroundColorSpan(Color.RED), 0, start4,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        visittype.setText(builder4);
        TextView Idcardtype = (TextView) findViewById(R.id.textView_idcardtype);
        String idcard = "ID Card Type";
        SpannableStringBuilder builder5 = new SpannableStringBuilder();
        builder5.append(color1);
        int start5 = builder5.length();
        builder5.append(idcard);
        builder5.setSpan(new ForegroundColorSpan(Color.RED), 0, start5,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Idcardtype.setText(builder5);
        TextView Idcardno = (TextView) findViewById(R.id.textView_idcardnumber);
        String idcardno = "ID Card Number";
        SpannableStringBuilder builder6 = new SpannableStringBuilder();
        builder6.append(color1);
        int start6 = builder6.length();
        builder6.append(idcardno);
        builder6.setSpan(new ForegroundColorSpan(Color.RED), 0, start6,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Idcardno.setText(builder6);
        linearLayout = (LinearLayout) findViewById(R.id.activity_main);
        image = (ImageView) findViewById(R.id.imageView_reg);
        f_name = (EditText) findViewById(R.id.editText_name);
        l_name = (EditText) findViewById(R.id.editText_lname);
        mobile_no = (EditText) findViewById(R.id.editText_mobileno);
        email_id = (EditText) findViewById(R.id.editText_emailid);
        addrees = (EditText) findViewById(R.id.editText1_address);
        representing = (EditText) findViewById(R.id.editTextreprenting);
        remark = (EditText) findViewById(R.id.editText_remark);
        idno = (EditText) findViewById(R.id.editText_idcardnumber);
        visitortype = (Spinner) findViewById(R.id.editText_visittype);
        idproof = (Spinner) findViewById(R.id.editText_idcardtype);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, arrayList) {
            @Override
            public int getCount() {
                int c = super.getCount();
                return c > 0 ? c - 1 : c;
            }
        };
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        idproof.setAdapter(adapter1);
        idproof.setSelection(adapter1.getCount());
        idproof.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                if (idproof.getSelectedItem() == "Please Select ID type") {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
                }
                /*else{
                    Toast.makeText(VisitorRegistration.this,  idproof.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        btn_next = (Button) findViewById(R.id.button_next);
        image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });


        bundle = getIntent().getExtras();
        try {
            if (!bundle.isEmpty()) {
                 hashMap = (HashMap<String, Object>) bundle.getSerializable("response");
                ValidateVisitOrInviteCodePojo response = (ValidateVisitOrInviteCodePojo) hashMap.get("response");
                vstr_reg_Id = response.getVisitorRegistrationId();
                vsre_enty_id = response.getVisitorEntryId();
                codeid=Integer.parseInt(response.getCodeId());
                ApTime=response.getAppointmentDateTime();
                Areaid=response.getVisitingAreaId();
                personid=response.getVisitingPersonId();
                if (response.getVisitorRegistrationId().equals("0") && response.getVisitorEntryId().equals("0") ) {

                    mobile_no.setText(response.getContactNo());
                    email_id.setText(response.getEmailAddress());

                } else {

                    vstr_entry_dtls = response.getVstr_entry_dtls();
                    for (Vstr_reg_dtls v : response.getVstr_reg_dtls()
                            ) {
                        f_name.setText(v.getFname());
                        l_name.setText(v.getLastname());
                        mobile_no.setText(v.getMobile());
                        email_id.setText(v.getEmail_id());
                        remark.setText(v.getRemark());
                        addrees.setText(v.getAddress());
                        idno.setText(v.getIdcard_number());
                        encodeByte = Base64.decode(v.getImage(), Base64.DEFAULT);
                      Bitmap  bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                        // RoundedBitmapDrawable conv_bm = RoundImage.getRoundedBitmap(bitmap);

                        photo1 = RoundImage.getCircularBitmap(bitmap, 5);
                        image.setImageBitmap(photo1);
                        //photo=bitmap1;
                        //idproof.setAdapter(v.getIdcard_type());
                        representing.setText(v.getRepresenting());
                        visitortypeId = v.getVisit_type();
                       // ArrayAdapter myAdap = (ArrayAdapter) idproof.getAdapter(); //cast to an ArrayAdapter
                        Log.e("StringData",v.getIdcard_type());
                          //int spinnerPosition = myAdap.getPosition(v.getIdcard_type());


                        idproof.setSelection(arrayList.indexOf(v.getIdcard_type()));


                    }


                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (ConnectionCheck.isNetworkAvailable(getApplicationContext())) {

            if (ViewDialog.sharedPref.contains("LocationName")) {
                new GetDetails().execute();

            } else {
                ViewDialog.alertdialog("Please set location configuration", VisitorRegistration.this);
            }
        } else {
            Toast.makeText(getApplicationContext(), "No Internet connection. Your device is currently not connected to the Internet", Toast.LENGTH_LONG).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == CAMERA_REQUEST) {
                photo = (Bitmap) data.getExtras().get("data");
                // RoundedBitmapDrawable conv_bm1 = RoundImage.getRounded(photo);
                Bitmap bitmap1 = RoundImage.getCircularBitmap(photo, 5);
                image.setImageBitmap(bitmap1);
                // image.setImageDrawable(conv_bm1);
            } else {
                Toast.makeText(VisitorRegistration.this, "please take photo again", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void passdata(View view) {
        if (master_response.equals("success")) {
            getdata();
        } else {
            Toast.makeText(VisitorRegistration.this, "Wait... json data downloading", Toast.LENGTH_LONG).show();
        }
    }

    public void getdata() {
        try {


            String name = f_name.getText().toString();
            String last = l_name.getText().toString();
            String mno = mobile_no.getText().toString().trim();
            String email = email_id.getText().toString().trim();
            long vistype = Long.parseLong(selectvisittypeids.get(visitortype.getSelectedItemPosition()));
            String vistype1 = visitortype.getSelectedItem().toString();
            String idprof = idproof.getSelectedItem().toString();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                try {
                    if (photo != null) {
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();
                        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    } else {
                        Toast toastphoto = Toast.makeText(VisitorRegistration.this, "please take a photo, To take a photo click on photo", Toast.LENGTH_LONG);
                        toastphoto.setGravity(Gravity.CENTER, 0, 0);
                        toastphoto.show();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            String idnumber = idno.getText().toString();
            if (Utility.isNotNull(name) && Utility.isNotNull(last) && Utility.isNotNull(mno)
                    && Utility.isNotNull(vistype1) && Utility.spinnervalid(vistype1) && Utility.isNotNull(idprof) && Utility.spinnervalid(idprof) && Utility.isNotNull(idnumber) && Utility.isNotNull(encodedImage)) {
                if (mno.length() == 10) {
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
                        fields.setCodeid(String.valueOf(codeid));
                        // send Image as string
                        fields.setImage(encodedImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    hm = new HashMap<String, Object>();
                    hm.put("response_fields", fields);
                    //Bundle object is created
                    b = new Bundle();
                    b.putSerializable("response_fields", hm);
                   // b.putSerializable("response_fields", hashMap);

                    if (ConnectionCheck.isNetworkAvailable(getApplicationContext())) {
                        //new SendPostRequest().execute()

                        if (ViewDialog.sharedPref.contains("LocationName")) {
                            if (vsre_enty_id.isEmpty() && vstr_reg_Id.isEmpty()) {
                                RegisterNewUser();
                            } else {

                                if (vsre_enty_id.equals("0") && vstr_reg_Id.equals("0")) {
                                    RegisterNewUser();
                                } else {
                                    updateRegisterUser();
                                }
                            }

                        } else {
                            ViewDialog.alertdialog("Please set location configuration", VisitorRegistration.this);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet connection. Your device is currently not connected to the Internet", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast toastmb = Toast.makeText(getApplicationContext(), "Please enter valid mobile number", Toast.LENGTH_LONG);
                    toastmb.setGravity(Gravity.CENTER, 0, 0);
                    toastmb.show();
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Please fill all manadatory fields", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            // ProgresDailogClass.startProgresDailog(getApplicationContext());
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
                connection1 = ConnectionCheck.isConnectedToServer(Urls.URL_GET_ALL_visit_reg, timeout);
                Log.d("connection@@#@@@@", "" + connection1);
                if (connection1) {
                    Log.d("connection@@#@@@@", "gugugu");

                    try {
                        url = new URL(Urls.URL_GET_ALL_visit_reg);
                        connection = (HttpURLConnection) url.openConnection();
                        // Log.d("1", "gugugu");
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Accept", "application/json");
                        connection.setRequestProperty("Content-Type", "application/json");
                        connection.setRequestProperty("Content-Language", "en-US");
                        connection.setUseCaches(false);
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        connection.connect();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("fname", fields.getFirstname());
                        jsonObject.put("Lastname", fields.getLastname());
                        jsonObject.put("mobile", fields.getMobile());
                        jsonObject.put("image", fields.getImage());
                        jsonObject.put("EmailId", fields.getEmailId());
                        jsonObject.put("visittype", fields.getVisitTypes().toString());
                        jsonObject.put("Idcardtype", fields.getIdcardtype());
                        jsonObject.put("Idcardnumber", fields.getIdcardnumber());
                        jsonObject.put("Address", fields.getAddress());
                        jsonObject.put("Representing", fields.getRepresenting());
                        jsonObject.put("Remark", fields.getRemark());
                        System.out.println(jsonObject);
                        OutputStream os = connection.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(
                                new OutputStreamWriter(os, "UTF-8"));
                        writer.write(jsonObject.toString());
                        writer.flush();
                        writer.close();
                        os.close();
                        int responseCode = connection.getResponseCode();
                        if (responseCode == HttpsURLConnection.HTTP_OK) {
                            sh = new HttpHandler();
                            jsonStr = sh.makeServiceCall(connection);
                            JSONObject jsonObj = new JSONObject(jsonStr);
                            res_status = jsonObj.getString("response_status");
                            res_message = jsonObj.getString("response_message");
                            Id = jsonObj.getString("visitor_registration_id");
                        } else {
                            return new String("false : " + responseCode);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        return new String("Exception: " + e.getMessage());
                    }
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //ProgresDailogClass.stopProgresDailog(getApplicationContext());
            if (pd.isShowing()) {
                pd.dismiss();
            }
            if (connection1) {
                if (res_status.equals("success")) {
                    try {
                        //  Toast.makeText(VisitorRegistration.this,"data added successfully",Toast.LENGTH_LONG).show();
                        Intent inew = new Intent(getApplicationContext(), VisitorEntry.class);
                        inew.putExtra("Id", Id);
                        inew.putExtras(b);
                        startActivity(inew);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } else {
                    snackbar = Snackbar.make(linearLayout, " Data not added,  Reason:" + res_message, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            } else {
                Toast.makeText(VisitorRegistration.this, "server not Running,Please try after some time", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class GetDetails extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(VisitorRegistration.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();

            //Toast.makeText(VisitorRegistration.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler_spinner sh = new HttpHandler_spinner();
            String jsonStr = sh.makeServiceCall(Urls.getDetail_Url_visit_reg);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    master_response = jsonObj.getString("response_status");
                    // Getting JSON Array node
                    JSONArray visittypes = jsonObj.getJSONArray("visitor_type_master");
                    //visittype1.add("Select visit type");
                    selectvisittype.add("Select visit type");
                    selectvisittypeids.add("Select visit type");
                    for (int i = 0; i < visittypes.length(); i++) {
                        JSONObject c = visittypes.getJSONObject(i);
                        String visits = c.getString("visitor_code");
                        selectvisittype.add(c.getString("visitor_code"));
                        selectvisittypeids.add(c.getString("visitor_type_Id"));
                        // visittype1.add(visits);
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


            try {
                if (master_response.equals("success")) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(VisitorRegistration.this, R.layout.spinner_item, selectvisittype);//{
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    visitortype.setAdapter(adapter);
                    visitortype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view,
                                                   int position, long id) {
                            // TODO Auto-generated method stub
                            if (visitortype.getSelectedItem() == "Select visit type") {
                                ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
                            } /*else {
                                Toast.makeText(VisitorRegistration.this, visitortype.getSelectedItem().toString()+selectvisittypeids.get(position), Toast.LENGTH_LONG).show();
                            }*/
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });
                    visitortype.setAdapter(adapter);

                    try {
                        if(!visitortypeId.isEmpty()){

                               visitortype.setSelection(selectvisittype.indexOf(visitortypeId));

                        }
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                   // visitortype.setSelection(visitortypeId);
                } else {
                    Toast.makeText(VisitorRegistration.this, "json data not get", Toast.LENGTH_LONG).show();
                }
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            }
        }
    }

    public void RegisterNewUser() {
        pd1 = new ProgressDialog(VisitorRegistration.this);
        pd1.setMessage("Loading....");
        pd1.setCanceledOnTouchOutside(false);
        pd1.show();

        NewRegistrationPojo jsonObject = new NewRegistrationPojo();
        try {
            jsonObject.setFname(fields.getFirstname());
            jsonObject.setLastname(fields.getLastname());
            jsonObject.setMobile(fields.getMobile());
            jsonObject.setImage(fields.getImage());
            jsonObject.setEmailId(fields.getEmailId());
            jsonObject.setVisittype(fields.getVisitTypes().toString());
            jsonObject.setIdcardtype(fields.getIdcardtype());
            jsonObject.setIdcardnumber(fields.getIdcardnumber());
            jsonObject.setAddress(fields.getAddress());
            jsonObject.setRepresenting(fields.getRepresenting());
            jsonObject.setRemark(fields.getRemark());
            jsonObject.setLocation_id(ViewDialog.get_session("LocationId"));
            jsonObject.setCode_id(fields.getCodeid());
        } catch (Exception e) {
            e.printStackTrace();
        }


        retrofit2.Call<NewRegistrationResponsePojo> call = apiService.NewVisitor(jsonObject);
        call.enqueue(new Callback<NewRegistrationResponsePojo>() {
            @Override
            public void onResponse(retrofit2.Call<NewRegistrationResponsePojo> call, Response<NewRegistrationResponsePojo> response) {
                pd1.dismiss();
                //vList.clear();
                JSONObject data = null;

                if (response.isSuccessful()) {
                    //Log.d("User ID1: ", response.body().toString());
                    if (response.body().getResponse_status().equals("success")) {


                        try {
                            //  Toast.makeText(VisitorRegistration.this,"data added successfully",Toast.LENGTH_LONG).show();
                            ViewDialog.set_session("is_invite_code","0");
                            Intent inew = new Intent(getApplicationContext(), VisitorEntry.class);
                            inew.putExtra("Id", response.body().getVisitor_registration_id());
                            inew.putExtra("ApTime", ApTime);
                            inew.putExtra("AreaId", Areaid);
                            inew.putExtra("personId", personid);
                            inew.putExtras(b);
                            startActivity(inew);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }


                        //startActivity(new Intent(MainActivity.this, ManagerActivity.class));

                    } else {
                        ViewDialog.alertdialog(response.body().getResponse_message(), VisitorRegistration.this);

                    }
                } else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            ViewDialog.alertdialog("File or directory not found", VisitorRegistration.this);
                            break;
                        case 500:
                            ViewDialog.alertdialog("server broken", VisitorRegistration.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            ViewDialog.alertdialog("unknown error", VisitorRegistration.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

            }

            @Override
            public void onFailure(retrofit2.Call<NewRegistrationResponsePojo> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                pd1.dismiss();


            }
        });
    }

    public void updateRegisterUser() {
        pd1 = new ProgressDialog(VisitorRegistration.this);
        pd1.setMessage("Loading....");
        pd1.setCanceledOnTouchOutside(false);
        pd1.show();

        UpdateNewVisitor jsonObject = new UpdateNewVisitor();
        try {
            jsonObject.setFname(fields.getFirstname());
            jsonObject.setLastname(fields.getLastname());
            jsonObject.setMobile(fields.getMobile());
            jsonObject.setImage(fields.getImage());
            jsonObject.setEmailId(fields.getEmailId());
            jsonObject.setVisittype(fields.getVisitTypes().toString());
            jsonObject.setIdcard_type(fields.getIdcardtype());
            jsonObject.setIdcard_number(fields.getIdcardnumber());
            jsonObject.setAddress(fields.getAddress());
            jsonObject.setRepresenting(fields.getRepresenting());
            jsonObject.setRemark(fields.getRemark());
            jsonObject.setVisitor_reg_id(vstr_reg_Id);
            jsonObject.setIsmobile_verified("0");
            //Log.e("updatedata",jsonObject.getFname()+""+jsonObject.getAddress()+""+jsonObject.getMobile()+""+jsonObject.getLastname()+""+jsonObject.getIdcardnumber()+""+jsonObject.getIdcardtype()+""+jsonObject.getEmailId()+""+jsonObject.getLocation_id()+""+jsonObject.getRemark()+""+jsonObject.getRepresenting()+""+jsonObject.getVisittype());
        } catch (Exception e) {
            e.printStackTrace();
        }


        retrofit2.Call<NewRegistrationResponsePojo> call = apiService.updateVisitorRegistration(jsonObject);
        call.enqueue(new Callback<NewRegistrationResponsePojo>() {
            @Override
            public void onResponse(retrofit2.Call<NewRegistrationResponsePojo> call, Response<NewRegistrationResponsePojo> response) {
                pd1.dismiss();
                //vList.clear();
                JSONObject data = null;

                if (response.isSuccessful()) {
                    //Log.d("User ID1: ", response.body().toString());
                    if (response.body().getResponse_status().equals("success")) {


                        try {
                            HashMap<String, Object> hm = new HashMap<String, Object>();
                            hm.put("response_fields", fields);
                            //Bundle object is created
                            // Bundle bundle= new Bundle();
                            //bundle.putSerializable("response",hm);
                            bundle.putSerializable("response_fields", hm);
                            //  Toast.makeText(VisitorRegistration.this,"data added successfully",Toast.LENGTH_LONG).show();
                            Intent inew = new Intent(getApplicationContext(), VisitorEntry.class);
                            //inew.putExtra("fields", fields);
                            inew.putExtra("ApTime", "");
                            inew.putExtra("AreaId", "");
                            inew.putExtra("personId", "");
                            inew.putExtras(bundle);
                            startActivity(inew);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }


                        //startActivity(new Intent(MainActivity.this, ManagerActivity.class));

                    } else {
                        ViewDialog.alertdialog(response.body().getResponse_message(), VisitorRegistration.this);

                    }
                } else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            ViewDialog.alertdialog("File or directory not found", VisitorRegistration.this);
                            break;
                        case 500:
                            ViewDialog.alertdialog("server broken", VisitorRegistration.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            ViewDialog.alertdialog("unknown error", VisitorRegistration.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

            }

            @Override
            public void onFailure(retrofit2.Call<NewRegistrationResponsePojo> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                pd1.dismiss();


            }
        });
    }
}

