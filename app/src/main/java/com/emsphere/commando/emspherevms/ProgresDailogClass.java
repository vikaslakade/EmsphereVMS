package com.emsphere.commando.emspherevms;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by commando1 on 6/1/2017.
 */

public class ProgresDailogClass {
  static   ProgressDialog progressDialog;
     static boolean startProgresDailog(Context context){
        progressDialog = new ProgressDialog(context.getApplicationContext());
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        return true;
    }
    static boolean stopProgresDailog(Context context){
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        return true;
    }
}
