package com.emsphere.commando.emspherevms;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

/**
 * Created by seedcommando_6 on 2/25/2017.
 */

public class RoundImage {


    public static RoundedBitmapDrawable getRoundedBitmap(Bitmap bitmap)
    {
        Resources mResources;
        int bitmapWidthImage = bitmap.getWidth();
            int bitmapHeightImage = bitmap.getHeight();
            int borderWidthHalfImage = 4;

            int bitmapRadiusImage = Math.min(bitmapWidthImage,bitmapHeightImage)/2;
            int bitmapSquareWidthImage = Math.min(bitmapWidthImage,bitmapHeightImage);
            int newBitmapSquareWidthImage = bitmapSquareWidthImage+borderWidthHalfImage;

            Bitmap roundedImageBitmap = Bitmap.createBitmap(newBitmapSquareWidthImage,newBitmapSquareWidthImage,Bitmap.Config.ARGB_8888);
            Canvas mcanvas = new Canvas(roundedImageBitmap);
            mcanvas.drawColor(Color.RED);
               /* final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                final RectF rectF = new RectF(rect);*/

            int i = borderWidthHalfImage + bitmapSquareWidthImage - bitmapWidthImage;
            int j = borderWidthHalfImage + bitmapSquareWidthImage - bitmapHeightImage;
               /* int i =bitmapWidthImage;
                int j =bitmapHeightImage;*/

            mcanvas.drawBitmap(bitmap, i, j, null);
            String white = "#f8d287";
           int color=Color.parseColor(white);
            Paint borderImagePaint = new Paint();
            borderImagePaint.setStyle(Paint.Style.STROKE);
            borderImagePaint.setStrokeWidth(borderWidthHalfImage*2);
            borderImagePaint.setColor(color);
            mcanvas.drawCircle(mcanvas.getWidth()/2, mcanvas.getWidth()/2, newBitmapSquareWidthImage/2, borderImagePaint);
            mResources=Resources.getSystem();
            RoundedBitmapDrawable roundedImageBitmapDrawable = RoundedBitmapDrawableFactory.create(mResources,roundedImageBitmap);
            roundedImageBitmapDrawable.setCornerRadius(bitmapRadiusImage);
            roundedImageBitmapDrawable.setAntiAlias(true);
            return roundedImageBitmapDrawable;
    }
        public static RoundedBitmapDrawable getRounded(Bitmap bitmap)
        {
            Resources mResources;
            int bitmapWidthImage = bitmap.getWidth();
                int bitmapHeightImage = bitmap.getHeight();
                int borderWidthHalfImage = 4;
            int bitmapRadiusImage = Math.min(bitmapWidthImage,bitmapHeightImage)/2;
                int bitmapSquareWidthImage = Math.min(bitmapWidthImage,bitmapHeightImage);
                int newBitmapSquareWidthImage = bitmapSquareWidthImage+borderWidthHalfImage;
            Bitmap roundedImageBitmap = Bitmap.createBitmap(newBitmapSquareWidthImage,newBitmapSquareWidthImage,Bitmap.Config.ARGB_8888);
                Canvas mcanvas = new Canvas(roundedImageBitmap);
                mcanvas.drawColor(Color.RED);
               /* final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                final RectF rectF = new RectF(rect);*/

            int i = borderWidthHalfImage + bitmapSquareWidthImage - bitmapWidthImage;
                int j = borderWidthHalfImage + bitmapSquareWidthImage - bitmapHeightImage;
               /* int i =bitmapWidthImage;
                int j =bitmapHeightImage;*/
            mcanvas.drawBitmap(bitmap, i, j, null);
            String white = "#f8d287";
            int color=Color.parseColor(white);
                Paint borderImagePaint = new Paint();
                borderImagePaint.setStyle(Paint.Style.STROKE);
                borderImagePaint.setStrokeWidth(borderWidthHalfImage);
                borderImagePaint.setColor(color);
                mcanvas.drawCircle(mcanvas.getWidth()/2, mcanvas.getWidth()/2, newBitmapSquareWidthImage/2, borderImagePaint);
                 mResources=Resources.getSystem();
                RoundedBitmapDrawable roundedImageBitmapDrawable = RoundedBitmapDrawableFactory.create(mResources,roundedImageBitmap);
                roundedImageBitmapDrawable.setCornerRadius(bitmapRadiusImage);
                roundedImageBitmapDrawable.setAntiAlias(true);
                return roundedImageBitmapDrawable;
        }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
    {
        int     width           = bm.getWidth();
        int     height          = bm.getHeight();
        float   scaleWidth      = ((float) newWidth) / width;
        float   scaleHeight     = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }
    public static Bitmap getCircularBitmap(Bitmap bitmap , int borderWidth) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }
        Bitmap resizedBitmap = getResizedBitmap(bitmap, 140, 140);
        int width = resizedBitmap.getWidth() + borderWidth;
        int height = resizedBitmap.getHeight() + borderWidth;
        String white = "#f8d287";
        int color=Color.parseColor(white);
        Bitmap canvasBitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(resizedBitmap, Shader.TileMode.CLAMP,  Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        Canvas canvas = new Canvas(canvasBitmap);
        float radius = width > height ? ((float) height) / 2f: ((float) width) / 2f;
        canvas.drawCircle(width / 2, height / 2, radius, paint);
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(borderWidth);
        canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2,  paint);
        return canvasBitmap;
    }


}
