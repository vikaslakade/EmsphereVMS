package com.emsphere.commando.emspherevms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

   ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

           try {


               setContentView(R.layout.slider);
              /* progressDialog = new ProgressDialog(MainActivity.this);
               progressDialog.setCancelable(false);
               //WindowManager.LayoutParams lp = progressDialog.getWindow().getAttributes();
               progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
              // progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
               progressDialog.getWindow().setGravity(Gravity.CENTER);
               progressDialog.show();*/
               progressDialog=ProgressDialog.show(this,null,null);
               progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
               progressDialog.setContentView(new ProgressBar(this));

        /*Intent intent=new Intent(MainActivity.this,Login.class);
        startActivity(intent);*/
               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       if (progressDialog.isShowing()) {
                           progressDialog.dismiss();

                       }

                       Intent intent = new Intent(MainActivity.this, Login.class);
                       startActivity(intent);
                   }
               }, 5000);
           }catch (Exception e){
               e.printStackTrace();
           }


    }

    }

