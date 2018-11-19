package com.emsphere.commando.emspherevms;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by commando1 on 4/20/2017.
 */

public class VisitorDataAdapter extends RecyclerView.Adapter<VisitorDataAdapter.MyViewHolder> {
    private List<VisitorData> visitsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
       public TextView name, mobileno, purpose,apmt_time;
        ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.textView_name);
            mobileno = (TextView) view.findViewById(R.id.textView_mobile_no);
            purpose = (TextView) view.findViewById(R.id.textView_purpose);
            apmt_time= (TextView) view.findViewById(R.id.textView_apmt_time);
            imageView=(ImageView)view.findViewById(R.id.imageView3);
        }
    }
    public VisitorDataAdapter(List<VisitorData> visitsList) {
        this.visitsList = visitsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.visitor_list_row, parent, false);

        return new MyViewHolder(itemView);//visitor_list_row
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        VisitorData data = visitsList.get(position);
        holder.name.setText(data.getName());
        Log.e("name",data.getName());
        holder.mobileno.setText(data.getMobileno());
        holder.purpose.setText(data.getPurpose());
        holder.apmt_time.setText(data.getAppointmenttime());
        try {
            String img = data.getImage();
            if(img.isEmpty()&&img.equals("")){

            }else {
                byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
                //byte[] encodeByte = com.loopj.android.http.Base64.decode(img, com.loopj.android.http.Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                //RoundedBitmapDrawable conv_bm = RoundImage.getRoundedBitmap(bitmap);
                Bitmap bitmap1 = RoundImage.getCircularBitmap(bitmap, 5);
                holder.imageView.setImageBitmap(bitmap1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
       // holder.imageView.setImageBitmap(bitmap);
    }
    /*public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }*/

    @Override
    public int getItemCount() {
        return visitsList.size();
    }
    private Bitmap createRoundedBitmap(Bitmap getbi, int i,int neededWidth,int neededHeight) {
        if (getbi == null) {
            return null;
        }
        Rect rect = new Rect(0, 0, neededWidth, neededHeight);
        Rect bmRect = new Rect(0, 0, getbi.getWidth(), getbi.getHeight());
        // create output bitmap

        Bitmap output = Bitmap.createBitmap(neededWidth, neededHeight, Bitmap.Config.ARGB_8888);

        // assign canvas with output bitmap
        Canvas canvas = new Canvas(output);
        canvas.drawARGB(0,0, 0,0);

        // initialize paint
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        // draw rounded rect to bitmap
        paint.setColor(0xFFFFFFFF);
        canvas.drawRoundRect(new RectF(rect), i, i, paint);

        // copy original bitmap to rounded area
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(getbi, bmRect , rect, paint);

        return output;
    }
}
