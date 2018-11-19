package com.emsphere.commando.emspherevms;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emsphere.commando.emspherevms.pojos.Expc_Vstr_Details;
import com.emsphere.commando.emspherevms.pojos.ExpectedVisitorPoJo;
import com.emsphere.commando.emspherevms.pojos.ExpectedVisitorSendData;
import com.emsphere.commando.emspherevms.rest.ApiClient;
import com.emsphere.commando.emspherevms.rest.ApiInterface;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import activitys.NotificationList;
import app.Config;
import retrofit2.Callback;
import retrofit2.Response;
import utils.NotificationUtils;
import utils.Utils;

/**
 * Created by commando1 on 8/18/2017.
 */


    public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
        //RelativeLayout date;
        LinearLayout date;
        private List<VisitorData>vList ;
        private RecyclerView recyclerView;
        private VisitorDataAdapter mAdapter;
        private SimpleDateFormat dateFormatteryear;
        private SimpleDateFormat dateFormatter;
        private DatePickerDialog toDatePickerDialog;
        private DatePickerDialog toDatePickerDialog1;
        TextView year1,textdate;
        private String TAG1 = MainActivity.class.getSimpleName();
        JSONObject c;
        SwipeRefreshLayout mSwipeRefreshLayout;
        String res_status=null;
        HttpHandler sh;

    //for date
    String FormatedDate;
    LayerDrawable icon;
    String Empoyee_Id;
    SharedPreferences preferences;
    ProgressDialog pd;

//notification data

    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtRegId, txtMessage;
    private int mNotificationsCount = 0;
    private static int count=0;
    private ApiInterface apiService;
    ProgressDialog pd1;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.expected_visitor);
            date = (LinearLayout) findViewById(R.id.button_date);
            //date = (RelativeLayout) findViewById(R.id.button_date);
            dateFormatteryear = new SimpleDateFormat("yyyy", Locale.US);
            dateFormatter = new SimpleDateFormat("EEE, MMM dd", Locale.US);
           /* Toolbar t=(Toolbar) findViewById(R.id.toolbar_expected_visitor);
            setSupportActionBar(t);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);*/
            vList = new ArrayList<>();
            setDateTimeField();

            // prepareMovieData();
            year1=(TextView) findViewById(R.id.textView_year);
            textdate=(TextView) findViewById(R.id.textView_date);
            //default data
            Calendar deffaultDate = Calendar.getInstance();
            year1.setText(dateFormatteryear.format(deffaultDate.getTime()));
            int weekday=deffaultDate.get(Calendar.DAY_OF_WEEK);//dayweek+","+
            String dayweek = new DateFormatSymbols().getShortWeekdays()[weekday];
            Log.e("dayweek",dayweek);
            textdate.setText(dateFormatter.format(deffaultDate.getTime()));
            SimpleDateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd");

            //Date d = new Date((year-1900),monthOfYear, dayOfMonth);
            String strDate = dateFormatter1.format(deffaultDate.getTime());
            Log.e("data",strDate);
            FormatedDate=ServerDateFormat(strDate);
            preferences=getSharedPreferences(Config.SHARED_PREF,0);
            Empoyee_Id=preferences.getString("employee_id","");
            apiService = ApiClient.getClient().create(ApiInterface.class);
            getExpectedVisitor();
           // new GetContacts().execute();
            //toolbar
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_expected_visitor);
            setSupportActionBar(toolbar);
            getdate();
            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
            mSwipeRefreshLayout.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            vList.clear();
                            Log.i("log tag", "onRefresh called from SwipeRefreshLayout");
                            // This method performs the actual data-refresh operation.
                            // The method calls setRefreshing(false) when it's finished.
                           // new GetContacts().execute();
                            getExpectedVisitor();
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
            );


            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    // checking for type intent filter
                    if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                        Log.e("vikas","asklajeuireyqweweyytuiouiouiouiouiouiouiouiouiouio");
                        // gcm successfully registered
                        // now subscribe to `global` topic to receive app wide notifications
                        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                        displayFirebaseRegId();

                    } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                        // new push notification is received
                        new FetchCountTask().execute();


                        // updateNotificationsBadge(cont);
                        Log.e("vikas","asklajeuireyqweweyytuiouiouiouiouiouiouiouiouiouiozxcvbnmsddff");

                        String message = intent.getStringExtra("message");

                        Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                        //txtMessage.setText(message);
                    }
                }
            };

            displayFirebaseRegId();
        }
        private void setDateTimeField() {
            date.setOnClickListener(this);

            final Calendar newCalendar = Calendar.getInstance();

            toDatePickerDialog = new DatePickerDialog(this, new android.app.DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                     Log.e("date",newDate.toString());
                   // String mytime="2017-08-28";
                    SimpleDateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
                    Date d = new Date((year-1900),monthOfYear, dayOfMonth);
                    String strDate = dateFormatter1.format(d);
                     Log.e("data",strDate);
                   FormatedDate=ServerDateFormat(strDate);
                    Log.e("date1",FormatedDate);
                    year1.setText(dateFormatteryear.format(newDate.getTime()));
                    int weekday=newDate.get(Calendar.DAY_OF_WEEK);//dayweek+","+
                    String dayweek = new DateFormatSymbols().getShortWeekdays()[weekday];
                    Log.e("dayweek",dayweek);
                    textdate.setText(dateFormatter.format(newDate.getTime()));
                    vList.clear();


                   // new GetContacts().execute();
                    getExpectedVisitor();
                }

            },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        }

        @Override
        public void onClick(View view) {
            if(view == date) {
                toDatePickerDialog.show();
                //toDatePickerDialog1.show();
            }
        }
    public void getdate()
        {
            String date123=year1.getText().toString();
            String ddd=textdate.getText().toString();
            Log.e("date",date123+ddd);
        }
        private class GetContacts extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(HomeActivity.this);
                pd.setMessage("Please wait");
                pd.setCancelable(false);
                pd.show();
               // Toast.makeText(HomeActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();
            }
            @Override
            protected Void doInBackground(Void... arg0) {
                //service code
                JSONObject jsonObject = new JSONObject();
                try {
                    String jsonStr;
                    URL url;
                    HttpURLConnection connection = null;
                    int timeout = 15000;
                    // connectionServer = ConnectionCheck.isConnectedToServer(Urls.URL_RegisteredVisitorDetails, timeout);
                    // System.out.println("response code"+conn.getResponseCode());
                    if (true) {
                        try {
                            //Create connection
                            //  final String URL_GET_ALL = "http://seedmanagement.cloudapp.net/VMS_Mobile_Service/RegisteredVisitorDetails.ashx";
                            url = new URL("http://seedmanagement.cloudapp.net/VMS_Mobile_Service/GetExpectedVisitorsDetails.ashx");
                            connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("POST");
                            connection.setRequestProperty("Accept", "application/json");
                            connection.setRequestProperty("Content-Type", "application/json");
                            connection.setRequestProperty("Content-Language", "en-US");
                            connection.setUseCaches(false);
                            connection.setDoInput(true);
                            connection.setDoOutput(true);
                            JSONObject postDataParams = new JSONObject();
                            postDataParams.put("employee_id",Empoyee_Id);
                            postDataParams.put("from_datetime",FormatedDate);
                            postDataParams.put("to_datetime",FormatedDate);
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
                                System.out.println(jsonStr);
                                System.out.println("@@@@@##########" + connection.getResponseCode());
                                try {
                                    JSONObject jsonObj = new JSONObject(jsonStr);
                                    res_status = jsonObj.getString("response_status");
                                    JSONArray jsonArray = jsonObj.getJSONArray("expc_vstr_details");
                                    System.out.println("dgjsdfsdggggggh" + jsonArray);
                                    if (res_status.equals("success")) {
                                        try {
                                            //System.out.println(jsonArray);
                                            //Gson gson = new Gson();
                                            //VisitorData response = gson.fromJson(jsonArray.toString(), VisitorData.class);


                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                //JSONObject jsonObj1 = jsonArray.getJSONObject(i);
                                                // Log.e("data",jsonObj.toString());
                                                c = jsonArray.getJSONObject(i);
                                                //String id = c.getString("id");
                                                String name = c.getString("visitor_name");
                                                Log.e("data",name );
                                                String mobileno = c.getString("mobile_no");
                                                String purpose1 = c.getString("purpose");
                                                String apointtime = c.getString("visit_intime");
                                                String image=c.getString("visitor_image");
                                                VisitorData visitorData=new VisitorData();
                                                visitorData.setAppointmenttime(apointtime);
                                                visitorData.setMobileno(mobileno);
                                                visitorData.setPurpose(purpose1);
                                                    visitorData.setImage(image);
                                                visitorData.setName(name);
                                                vList.add(visitorData);
                                                Log.e("data",vList.toString() );

                                                System.out.println("sddddd" + jsonObject);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        // jsonObject = new JSONObject();
                                        //jsonObject.put("g", "gvbjhvh");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                //return new String("false : " + responseCode);
                            }
                        } catch (IOException e) {
                            System.out.println("////// called" + connection.getResponseCode());
                            e.printStackTrace();
                        }
                    } else {
                        Toast toast = Toast.makeText(HomeActivity.this, "server not Running,Please try after some time", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                } catch (Exception e) {
                    //return new String("null");
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
                    recyclerView = (RecyclerView) findViewById(R.id.recycleview);

                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    // recyclerView.smoothScrollToPosition(0);
                    // recyclerView.notifyItemInserted(0);
                    mAdapter = new VisitorDataAdapter(vList);
                    mAdapter.notifyItemInserted(0);
                    recyclerView.setAdapter(mAdapter);
                }catch (Exception e){
                    Toast.makeText(HomeActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                }

            }
        }

    public void getExpectedVisitor() {
        pd1 = new ProgressDialog(HomeActivity.this);
        pd1.setMessage("Loading....");
        pd1.setCanceledOnTouchOutside(false);
        pd1.show();

        ExpectedVisitorSendData jsonObject = new ExpectedVisitorSendData();
        try {
            jsonObject.setEmployee_id( Empoyee_Id);
            jsonObject.setFrom_datetime(FormatedDate);
            jsonObject.setTo_datetime(FormatedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }



        retrofit2.Call<ExpectedVisitorPoJo> call = apiService.getExpectedVisitor(jsonObject);
        call.enqueue(new Callback<ExpectedVisitorPoJo>() {
            @Override
            public void onResponse(retrofit2.Call<ExpectedVisitorPoJo> call, Response<ExpectedVisitorPoJo> response) {
                pd1.dismiss();
                vList.clear();

                if (response.isSuccessful()) {
                    //Log.d("User ID1: ", response.body().toString());
                    if (response.body().getResponse_status().equals("success")) {



                        for (Expc_Vstr_Details o : response.body().getExpc_vstr_details()) {

                            VisitorData visitorData=new VisitorData();
                            visitorData.setAppointmenttime(o.getVisit_intime());
                            visitorData.setMobileno(o.getMobile_no());
                            visitorData.setPurpose(o.getPurpose());
                            visitorData.setImage(o.getVisitor_image());
                            visitorData.setName(o.getVisitor_name());
                            vList.add(visitorData);

                        }

                        try {
                            recyclerView = (RecyclerView) findViewById(R.id.recycleview);

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            // recyclerView.smoothScrollToPosition(0);
                            // recyclerView.notifyItemInserted(0);
                            mAdapter = new VisitorDataAdapter(vList);
                            mAdapter.notifyItemInserted(0);
                            recyclerView.setAdapter(mAdapter);
                        }catch (Exception e){
                            Toast.makeText(HomeActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                        }




                        //startActivity(new Intent(MainActivity.this, ManagerActivity.class));

                    } else {
                       ViewDialog.alertdialog(response.body().getResponse_message(), HomeActivity.this);

                    }
                } else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            ViewDialog.alertdialog("File or directory not found", HomeActivity.this);
                            break;
                        case 500:
                            ViewDialog.alertdialog("server broken", HomeActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            ViewDialog.alertdialog("unknown error", HomeActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

            }

            @Override
            public void onFailure(retrofit2.Call<ExpectedVisitorPoJo> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                pd1.dismiss();


            }
        });
    }

    public void gotoHome(View view){
        Intent i = new Intent(this, Home_activity.class);
        startActivity(i);
    }
    public String ServerDateFormat(String dateString){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = fmt.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("MM/dd/yyyy");
        Log.e("date",fmtOut.toString());
        return fmtOut.format(date);
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        /*if (!TextUtils.isEmpty(regId))
            txtRegId.setText("Firebase Reg Id: " + regId);
        else
            txtRegId.setText("Firebase Reg Id is not received yet!");
*/

        //System.out.println("user name:"+name);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    //Setting of notofication icon in toolbar

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notificatin_menu, menu);

        // Get the notifications MenuItem and
        // its LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.notification);
      /*  BitmapDrawable iconBitmap = (BitmapDrawable) item.getIcon();
        LayerDrawable iconLayer = new LayerDrawable(new Drawable[] { iconBitmap });*/
        //setBadgeCount(this, iconLayer, "0");
        icon = (LayerDrawable) item.getIcon();
        MenuItem item1 = menu.findItem(R.id.setting);
        item1.setVisible(false);

        // Update LayerDrawable's BadgeDrawable

        Utils.setBadgeCount(this, icon, String.valueOf(mNotificationsCount));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.notification) {
            // TODO: display unread notifications.
            //  createNotification();
            startActivity(new Intent(getApplicationContext(), NotificationList.class));
            Utils.setBadgeCount(this, icon, "0");
            //updateNotificationsBadge();
            //Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


// update notification

    private void updateNotificationsBadge(int count) {
        mNotificationsCount = count;
        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
    }


    class FetchCountTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            // example count. This is where you'd
            // query your data store for the actual count.
            count++;

            return count;
        }

        @Override
        public void onPostExecute(Integer count) {
            updateNotificationsBadge(count);
        }
    }



}



