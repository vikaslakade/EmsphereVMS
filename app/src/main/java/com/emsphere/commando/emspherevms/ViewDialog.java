package com.emsphere.commando.emspherevms;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.Base64;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import app.Config;

/**
 * Created by seedcommando_6 on 2/27/2017.
 */
public class ViewDialog extends Application {
    JSONObject obj;
    Context mContext;
    SharedPreferences preferences;
    String usertype;
    String Empoyee_Id;
    Dialog dialog;
    String PREFS_NAME = "VMS_vik";
    public static SharedPreferences sharedPref;
    public static SharedPreferences.Editor editor;
    public  ViewDialog()
    {


    }

    public  ViewDialog(Context context)
    {
        this.mContext=context;
        //sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

    }
    public void onCreate() {
        super.onCreate();
        //this.mContext=context;

        mContext = getApplicationContext();
        sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        //aesAlgorithm=new AESAlgorithm();
    }
    public void showDialog(final Activity activity, String data){
       // System.out.println("@@@@#####@##@#@#"+data);
        try{
        dialog = new Dialog(activity);
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
            obj = new JSONObject(data);
            //System.out.println("@@@@#####@##@#@#"+obj);
            //Fields d = (Fields)obj .get("data");
           // String nm=d.getFirstname();
            String nm=obj.getString("name");
            //String lnm=d.getLastname();
            String lnm=obj.getString("lname");
            name.setText(nm+" "+lnm);
            String img_vE = obj.getString("image");
            //retrieve and set to bitmap
            byte[] encodeByte = Base64.decode(img_vE, Base64.DEFAULT);
          Bitmap  bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            RoundedBitmapDrawable conv_bm = RoundImage.getRoundedBitmap(bitmap);
            imageView.setImageDrawable(conv_bm);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        // checkBox.setChecked(true);
        SimpleDateFormat df1 = new SimpleDateFormat("EEEE,dd-MMM-yyyy");
        String formattedDate1 = df1.format(c.getTime());
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setCancelable(true);
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
                preferences=activity.getSharedPreferences(Config.SHARED_PREF,0);
                usertype=preferences.getString("is_admin","");
                if(usertype.equals("1")) {
                    Intent intent = new Intent(activity.getApplicationContext(), Home_activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    dialog.dismiss();

                    mContext.startActivity(intent);
                   // activity.finishAffinity();

                }else {
                    Intent intent = new Intent(activity.getApplicationContext(), HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    dialog.dismiss();
                    mContext.startActivity(intent);
                   // activity.finishAffinity();

                }
            }
        });

    }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void alertdialog( String msg, final Context context1) {

        final String session;
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context1);
        //String str1=msg+";";
        // msg = msg.Replace("\r", " ").Replace("\n", " ");
        msg=msg.replace("\\r",".").replace("\\n","");
        // String newstr= msg.replace("[\r\n]", ".");
        // newstr =  newstr.replace("\r", ".").replace("\r", "");
        //msg.replace("\r\n", ".");
        session=msg;
        alertDialogBuilder.setMessage(msg);

        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        /*if(session.equals(ForSessionExpire)){
                            if(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("AllowLoginUsingOTP")).equals("1")) {
                                Intent i = new Intent(context1, OTPLoginActivity.class);
                                // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |

                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                context1.startActivity(i);
                            }else {
                                Intent intent = new Intent(context1, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |

                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context1.startActivity(intent);
                            }


                        }*/
                        //Toast.makeText(context, "You clicked yes button", Toast.LENGTH_LONG).show();

                    }

                });

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void set_session(String key, String value) {
       // editor = sharedPref.edit();
        Log.e("","set_session->" + key + ":" + value);
        editor.putString(key, value);
        editor.commit();
    }
    public static String get_session(String key) {

        String str = "";
        if (sharedPref.contains(key))
            str = sharedPref.getString(key,"");
        Log.e("","get_session->" + key + ":" + str);
        return str;
    }
}
