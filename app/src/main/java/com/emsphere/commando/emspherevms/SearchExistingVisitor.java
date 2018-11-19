package com.emsphere.commando.emspherevms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.LruCache;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.emsphere.commando.emspherevms.pojos.CommanResponsePojo;
import com.emsphere.commando.emspherevms.pojos.SearchExistingVisitorPojo;
import com.emsphere.commando.emspherevms.pojos.SearchVisitorResponsePojo;
import com.emsphere.commando.emspherevms.pojos.VisitorSendData;
import com.emsphere.commando.emspherevms.pojos.Visitor_List;
import com.emsphere.commando.emspherevms.rest.ApiClient;
import com.emsphere.commando.emspherevms.rest.ApiInterface;
import com.loopj.android.http.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by seedcommando_10 on 1/21/2017.
 */

public class SearchExistingVisitor extends AppCompatActivity {
    EditText mobileno1,firstname;

    Button  search;
    ProgressDialog pd;
    String mobino1,nme;
    HttpHandler sh = null;
    Fields fields1;
    Bundle b;
    HashMap<String,Object> hm1;
    JSONObject  jsonObject1;
    Toolbar toolbar;
    String res_status=null;
    boolean connectionServer;
    public static LruCache<String, Bitmap> mMemoryCache;
    private ApiInterface apiService;
    ProgressDialog pd1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_existing_visitor);
        try {

            toolbar = (Toolbar) findViewById(R.id.toolbar_search);
            //setActionBar(toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            TextView name1=(TextView) findViewById(R.id.textView_fname);
            String first = "First Name";
            String next = "<font color='#EE0000'>*</font>";
            name1.setText(Html.fromHtml(next+" "+first));
            // Font path
            String fontPath = "fonts/helvetica.ttf";
            String fontPatharial = "fonts/arial.ttf";
            Typeface fontRobo = Typeface.createFromAsset(getAssets(), fontPatharial);
            name1.setTypeface(fontRobo );
            //madtory fields
            TextView mbno=(TextView) findViewById(R.id.textView_mnumber);
            String mobtext = "Mobile Number";
            String next1 = "<font color='#EE0000'>*</font>";
            mbno.setText(Html.fromHtml(next1+" "+mobtext));
            mbno.setTypeface(fontRobo );
            firstname=(EditText) findViewById(R.id.editText_fname);
            mobileno1=(EditText) findViewById(R.id.editText_mnumbe);
            search = (Button) findViewById(R.id.button_search);
            apiService = ApiClient.getClient().create(ApiInterface.class);

            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 8;
            mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    // The cache size will be measured in kilobytes rather than
                    // number of items.
                    return bitmap.getByteCount() / 1024;
                }
            };


        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }
    public void search_visitor1(View v) {
        mobino1=mobileno1.getText().toString();
        nme=firstname.getText().toString();
       // System.out.println("@@@@@@####on click data@@@@@@#######"+mobino);
        if(ViewDialog.sharedPref.contains("LocationName")) {
            if (!nme.isEmpty()) {
                if (mobino1.length() == 10) {
                    if (ConnectionCheck.isNetworkAvailable(getApplicationContext())) {
                        // new MyJsonTask().execute();
                        SearchExistingVisitorVisitor();
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet connection. Your device is currently not connected to the Internet", Toast.LENGTH_LONG).show();
                    }
                } else {
                    mobileno1.requestFocus();
                    mobileno1.setError("Please Enter valid number");
                }
            } else {
                firstname.requestFocus();
                firstname.setError("Please enter first name ");
            }
        }else {
            ViewDialog.alertdialog("Please set location configuration",SearchExistingVisitor.this);
        }
    }
    private class MyJsonTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SearchExistingVisitor.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();


        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject jsonObject = new JSONObject();
            try {
                String jsonStr;
                URL url;
                HttpURLConnection connection = null;
                int timeout = 15000;
                connectionServer = ConnectionCheck.isConnectedToServer(Urls.URL_RegisteredVisitorDetails, timeout);
                // System.out.println("response code"+conn.getResponseCode());
                if (connectionServer) {
                    try {
                        //Create connection
                        //  final String URL_GET_ALL = "http://seedmanagement.cloudapp.net/VMS_Mobile_Service/RegisteredVisitorDetails.ashx";
                       url = new URL(Urls.URL_RegisteredVisitorDetails);
                       connection = (HttpURLConnection) url.openConnection();
                       connection.setRequestMethod("POST");
                        connection.setRequestProperty("Accept", "application/json");
                        connection.setRequestProperty("Content-Type", "application/json");
                       connection.setRequestProperty("Content-Language", "en-US");
                        connection.setUseCaches(false);
                       connection.setDoInput(true);
                       connection.setDoOutput(true);
                        JSONObject postDataParams = new JSONObject();
                        postDataParams.put("fname", nme);
                        postDataParams.put("mobile", mobino1);
                        postDataParams.put("all_visitors_flag", "0");
                        Log.e("params", postDataParams.toString());
                        DataOutputStream wr = new DataOutputStream(
                               connection.getOutputStream());
                       wr.writeBytes(postDataParams.toString());
                       wr.flush();
                       wr.close();
                        int responseCode = connection.getResponseCode();
                        if (responseCode == HttpsURLConnection.HTTP_OK) {
                            sh = new HttpHandler();
                            jsonStr = sh.makeServiceCall(connection);
                           // System.out.println(jsonStr);
                           // System.out.println("@@@@@##########" + connection.getResponseCode());
                            try {
                                JSONObject jsonObj = new JSONObject(jsonStr);
                                res_status = jsonObj.getString("response_status");
                                JSONArray jsonArray = jsonObj.getJSONArray("visitor_list");
                                //System.out.println("dgjsdfsdggggggh" + jsonArray);
                                if (res_status.equals("success")) {
                                    try {
                                        //System.out.println(jsonArray);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            jsonObject = jsonArray.getJSONObject(i);
                                            //System.out.println("sddddd" + jsonObject);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    jsonObject = new JSONObject();
                                    jsonObject.put("g", "gvbjhvh");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(SearchExistingVisitor.this,responseCode,Toast.LENGTH_LONG).show();

                            return new String("false : " + responseCode);
                        }
                    } catch (IOException e) {
                        System.out.println("////// called" + connection.getResponseCode());
                        e.printStackTrace();
                    }
                } else {
                    Toast toast = Toast.makeText(SearchExistingVisitor.this, "server not Running,Please try after some time", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            } catch (Exception e) {
                return new String("null");
            }
            return jsonObject.toString();
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            if (connectionServer) {
                if (res_status.equalsIgnoreCase("success")) {
                    try {
                        if (!result.isEmpty()) {
                           // System.out.println("jhftythjhjhhh@#$$%%%^^&&" + result);
                            jsonObject1 = new JSONObject(result);
                            searchgetdata();

                        } else {
                           // System.out.println("toast called");
                            pd.dismiss();
                            Toast.makeText(SearchExistingVisitor.this, "Visitor data not found", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                 Toast toast1= Toast.makeText(SearchExistingVisitor.this, "Visitor data not found", Toast.LENGTH_LONG);
                    toast1.setGravity(Gravity.CENTER, 0, 0);
                    toast1.show();
                }
            }else {
                Toast toast=Toast.makeText(SearchExistingVisitor.this, "server not Running,Please try after some time", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }


    public void SearchExistingVisitorVisitor() {
        pd1 = new ProgressDialog(SearchExistingVisitor.this);
        pd1.setMessage("Loading....");
        pd1.setCanceledOnTouchOutside(false);
        pd1.show();

        SearchExistingVisitorPojo jsonObject= new  SearchExistingVisitorPojo();
        try {
            jsonObject.setAll_visitors_flag("0");
            jsonObject.setFname(nme);
            jsonObject.setMobile(mobino1);
            jsonObject.setLocation_id(ViewDialog.get_session("LocationId"));

        } catch (Exception e) {
            e.printStackTrace();
        }



        retrofit2.Call<SearchVisitorResponsePojo> call = apiService.searchExistingVisitor(jsonObject);
        call.enqueue(new Callback<SearchVisitorResponsePojo>() {
            @Override
            public void onResponse(retrofit2.Call<SearchVisitorResponsePojo> call, Response<SearchVisitorResponsePojo> response) {
                pd1.dismiss();
                //vList.clear();
                JSONObject data = null;

                if (response.isSuccessful()) {
                    //Log.d("User ID1: ", response.body().toString());
                    if (response.body().getResponse_status().equals("success")) {
                        try {
                        for (Visitor_List v: response.body().getVisitor_lists()) {


                            fields1 = new Fields();
                            fields1.setFirstname(v.getFname());
                            // System.out.println("my name /////"+fields1.getFirstname());
                            fields1.setLastname(v.getLastname());
                            fields1.setMobile(v.getMobile());
                            fields1.setEmailId(v.getEmailId());
                            fields1.setRepresenting(v.getRepresenting());
                            fields1.setRemark(v.getRemark());
                            fields1.setAddress(v.getAddress());
                            fields1.setIdcardnumber(v.getIdcardnumber());
                            fields1.setIdcardtype(v.getIdcardtype());
                            fields1.setVisittype(v.getVisittype());
                            fields1.setVisitorRegistrationId(v.getVisitorRegistrationId());
                            byte[] encodeByte = Base64.decode(v.getImage(), Base64.DEFAULT);
                            addBitmapToMemoryCache("image", BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length));

                            b = new Bundle();
                            b.putSerializable("bundleobj", fields1);

                            Intent intentsearch = new Intent(getApplicationContext(), SearchVisitor_details.class);
                            intentsearch.putExtras(b);
                            startActivity(intentsearch);

                        }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }





                        //startActivity(new Intent(MainActivity.this, ManagerActivity.class));

                    } else {
                        ViewDialog.alertdialog(response.body().getResponse_message(), SearchExistingVisitor.this);

                    }
                } else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            ViewDialog.alertdialog("File or directory not found", SearchExistingVisitor.this);
                            break;
                        case 500:
                            ViewDialog.alertdialog("server broken", SearchExistingVisitor.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            ViewDialog.alertdialog("unknown error", SearchExistingVisitor.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

            }

            @Override
            public void onFailure(retrofit2.Call<SearchVisitorResponsePojo> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                pd1.dismiss();


            }
        });
    }
    public  void searchgetdata() {
        try {
            fields1 = new Fields();
            fields1.setFirstname(jsonObject1.getString("fname"));
            // System.out.println("my name /////"+fields1.getFirstname());
            fields1.setLastname(jsonObject1.getString("Lastname"));
            fields1.setMobile(jsonObject1.getString("mobile"));
            fields1.setEmailId(jsonObject1.getString("EmailId"));
            fields1.setRepresenting(jsonObject1.getString("visittype"));
            fields1.setRemark(jsonObject1.getString("Remark"));
            fields1.setAddress(jsonObject1.getString("Idcardtype"));
            fields1.setIdcardnumber(jsonObject1.getString("Address"));
            fields1.setIdcardtype(jsonObject1.getString("Idcardnumber"));
            fields1.setVisittype(jsonObject1.getString("Representing"));
            //fields1.setImage(jsonObject1.getString("image"));

            fields1.setVisitorRegistrationId(jsonObject1.getString("VisitorRegistrationId"));
            byte[] encodeByte = Base64.decode(jsonObject1.getString("image"), Base64.DEFAULT);
            addBitmapToMemoryCache("image", BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length));
           // Log.e("ImageData",fields1.getImage());
           // hm1 = new HashMap<String, Object>();
            //hm1.put("data", fields1);
            //hm1.put("Id", visitorregistrationid);
            //Bundle object is created
            b = new Bundle();
            b.putSerializable("bundleobj", fields1);

            Intent intentsearch = new Intent(getApplicationContext(), SearchVisitor_details.class);
            intentsearch.putExtras(b);
            startActivity(intentsearch);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public static  Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

}






