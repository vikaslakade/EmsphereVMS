package com.emsphere.commando.emspherevms;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import com.emsphere.commando.emspherevms.pojos.LocationDataPojo;
import com.emsphere.commando.emspherevms.pojos.Locations_list;
import com.emsphere.commando.emspherevms.pojos.SearchExistingVisitorPojo;
import com.emsphere.commando.emspherevms.pojos.SearchVisitorResponsePojo;
import com.emsphere.commando.emspherevms.pojos.SendVisitCodePojo;
import com.emsphere.commando.emspherevms.pojos.ValidateVisitOrInviteCodePojo;
import com.emsphere.commando.emspherevms.pojos.Visitor_List;
import com.emsphere.commando.emspherevms.rest.ApiClient;
import com.emsphere.commando.emspherevms.rest.ApiInterface;
import com.loopj.android.http.Base64;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import activitys.NotificationList;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Utils;

/**
 * Created by seedcommando_10 on 1/16/2017.
 */

public class Home_activity extends AppCompatActivity {
   //RelativeLayout add , serarch;
    Button add , serarch,checkCode;
   // ImageButton add , serarch;
   Toolbar toolbar;
    SharedPreferences preferences;
    String name;
    private ApiInterface apiService;
    ProgressDialog pd1;
    ArrayList<String> location_name;
    ArrayList<String> location_id;
     String name1,id1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.vmsvisitorlayout);
        try {
            setContentView(R.layout.vmsvisitorlayout);
          toolbar = (Toolbar) findViewById(R.id.toolbar2);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            add = (Button) findViewById(R.id.button_reg);
           serarch = (Button) findViewById(R.id.button_new_reg);
            checkCode = (Button) findViewById(R.id.button_verify_code);
            apiService = ApiClient.getClient().create(ApiInterface.class);
            ViewDialog.set_session("is_invite_code","0");
            if(ConnectionCheck.isNetworkAvailable(getApplicationContext())) {
                getLocationDetail();
            }else {
                Toast.makeText(getApplicationContext(),"No Internet connection. Your device is currently not connected to the Internet",Toast.LENGTH_LONG).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void OnAddbuttonclick(View v) {
        ViewDialog.set_session("is_invite_code","0");
        Intent intent = new Intent(getApplicationContext(),VisitorRegistration.class);
        startActivity(intent);
    }
    public void OnSearchbuttonclick(View v)
    {
        Intent intent = new Intent(getApplicationContext(),SearchExistingVisitor.class);
        startActivity(intent);
    }
    public void OnVerifybuttonclick(View v)
    {
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.verify_visitor_code_layout);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final EditText code=(EditText) dialog.findViewById(R.id.edittext_code);
        Button submit=(Button) dialog.findViewById(R.id.submitcode);
        //Button cancel=(Button) dialog.findViewById(R.id.cancelcode);
        ImageView cancel=(ImageView) dialog.findViewById(R.id.imageView_close);
        dialog.show();
        dialog.setCancelable(true);
        //dialog.setCanceledOnTouchOutside(false);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!code.getText().toString().isEmpty()){
                    if(ViewDialog.sharedPref.contains("LocationName")) {
                        ValidateVisitCode(code.getText().toString());
                   // Toast.makeText(getApplicationContext(),"Verifyed Successfully",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }else {
                        ViewDialog.alertdialog("Please set location configuration",Home_activity.this);

                        //Toast.makeText(getApplicationContext(),"Please set location configuration",Toast.LENGTH_LONG).show();
                    }
                }else {
                    ViewDialog.alertdialog("Please enter Invite/Visit Code",Home_activity.this);
                }
            }
        });

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notificatin_menu, menu);


        MenuItem item = menu.findItem(R.id.notification);
        item.setVisible(false);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting) {

            locationConfi();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getLocationDetail() {
        pd1 = new ProgressDialog(Home_activity.this);
        pd1.setMessage("Loading....");
        pd1.setCanceledOnTouchOutside(false);
        pd1.show();


         location_name=new ArrayList<>();
         location_id=new ArrayList<>();


        retrofit2.Call<LocationDataPojo> call = apiService.getLocationDetails();
        call.enqueue(new Callback<LocationDataPojo>() {
            @Override
            public void onResponse(retrofit2.Call<LocationDataPojo> call, Response<LocationDataPojo> response) {
                pd1.dismiss();
                //vList.clear();
                JSONObject data = null;
                location_name.clear();
                location_id.clear();

                if (response.isSuccessful()) {
                    //Log.d("User ID1: ", response.body().toString());
                    if (response.body().getResponse_status().equals("success")) {
                        try {
                            location_name.add("Locations");
                            location_id.add("-1");

                            for (Locations_list l : response.body().getLocations_lists()) {
                                location_id.add(l.getLocation_id());
                                location_name.add(l.getLocation_name());
                                Log.e("locationData", "got it");

                            }
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }

                        //startActivity(new Intent(MainActivity.this, ManagerActivity.class));

                    } else {
                        ViewDialog.alertdialog(response.body().getResponse_message(), Home_activity.this);

                    }
                } else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            ViewDialog.alertdialog("File or directory not found", Home_activity.this);
                            break;
                        case 500:
                            ViewDialog.alertdialog("server broken", Home_activity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            ViewDialog.alertdialog("unknown error", Home_activity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

            }

            @Override
            public void onFailure(retrofit2.Call<LocationDataPojo> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                pd1.dismiss();


            }
        });
    }

    public void locationConfi()
    {
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.location_config);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final Spinner code=(Spinner) dialog.findViewById(R.id.spiner_locations);
        Button submit=(Button) dialog.findViewById(R.id.btn_save);
        Button cancel=(Button) dialog.findViewById(R.id.btn_cancel);



        // Spinner click listener
        code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {


                    name1=  parent.getItemAtPosition(position).toString();
                    id1= String.valueOf(location_id.get(position));

                    Toast.makeText(Home_activity.this,name1+""+id1,Toast.LENGTH_LONG).show();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner Drop down elements

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, location_name);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       // code.setPromptId(R.string.lication);

        // attaching data adapter to spinner
        code.setAdapter(dataAdapter);
         if(ViewDialog.sharedPref.contains("LocationName")) {

             ArrayAdapter myAdap = (ArrayAdapter) code.getAdapter(); //cast to an ArrayAdapter

             int spinnerPosition = myAdap.getPosition(ViewDialog.get_session("LocationName"));

//set the default according to value
             code.setSelection(spinnerPosition);
         }

        dialog.show();
        dialog.setCancelable(true);
        //dialog.setCanceledOnTouchOutside(false);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(!name1.equals("Locations")) {


                        ViewDialog.set_session("LocationName", name1);
                        ViewDialog.set_session("LocationId", id1);
                        dialog.dismiss();
                    }else {
                        ViewDialog.alertdialog("Please select Location",Home_activity.this);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }


            }
        });

    }

    public void ValidateVisitCode(String code) {
        pd1 = new ProgressDialog(Home_activity.this);
        pd1.setMessage("Loading....");
        pd1.setCanceledOnTouchOutside(false);
        pd1.show();

        SendVisitCodePojo jsonObject= new  SendVisitCodePojo();
        try {
            jsonObject.setInvite_code_or_visit_code(code);
            jsonObject.setLocation_id(ViewDialog.get_session("LocationId"));
            Log.e("data1111",jsonObject.getInvite_code_or_visit_code()+""+jsonObject.getLocation_id());

        } catch (Exception e) {
            e.printStackTrace();
        }



        retrofit2.Call<ValidateVisitOrInviteCodePojo> call = apiService.validateVisitCode(jsonObject);
        call.enqueue(new Callback<ValidateVisitOrInviteCodePojo>() {
            @Override
            public void onResponse(retrofit2.Call<ValidateVisitOrInviteCodePojo> call, Response<ValidateVisitOrInviteCodePojo> response) {
                pd1.dismiss();
                //vList.clear();
                JSONObject data = null;

                if (response.isSuccessful()) {
                    //Log.d("User ID1: ", response.body().toString());
                    if (response.body().getResponse_status().equals("success")) {
                        ViewDialog.set_session("is_invite_code","1");

                       // if(response.body().getVisitorRegistrationId().equals("0")&&response.body().getVisitorEntryId().equals("0")){
                          HashMap<String, ValidateVisitOrInviteCodePojo>  hm = new HashMap<String, ValidateVisitOrInviteCodePojo>();
                          hm.clear();
                            hm.put("response", response.body());
                            //Bundle object is created
                        //ValidateVisitOrInviteCodePojo vcode;
                       // vcode = (ValidateVisitOrInviteCodePojo) response;
                        Bundle bundle= new Bundle();
                            bundle.putSerializable("response",hm);
                           // Bundle bundle=new Bundle();
                           // bundle.putBundle("response",response);
                       Intent intent =new Intent(Home_activity.this,VisitorRegistration.class);
                       intent.putExtras(bundle);
                            startActivity(intent);
                        /*}else {
                            HashMap<String, Object>  hm = new HashMap<String, Object>();
                            hm.put("Visitor_reg_details", response.body().getVstr_reg_dtls());
                            hm.put("Visitor_entry_details", response.body().getVstr_entry_dtls());
                            //Bundle object is created
                            Bundle bundle= new Bundle();
                            bundle.putSerializable("response",hm);
                            // Bundle bundle=new Bundle();
                            // bundle.putBundle("response",response);
                            startActivity(new Intent(Home_activity.this,VisitorRegistration.class).putExtras(bundle));

                        }*/






                        //startActivity(new Intent(MainActivity.this, ManagerActivity.class));

                    } else {
                        ViewDialog.alertdialog(response.body().getResponse_message(), Home_activity.this);

                    }
                } else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            ViewDialog.alertdialog("File or directory not found", Home_activity.this);
                            break;
                        case 500:
                            ViewDialog.alertdialog("server broken", Home_activity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            ViewDialog.alertdialog("unknown error", Home_activity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

            }

            @Override
            public void onFailure(retrofit2.Call<ValidateVisitOrInviteCodePojo> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                pd1.dismiss();


            }
        });
    }



}
