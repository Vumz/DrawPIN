package com.example.vamsee.drawapp;

/**
 * Created by vgangaram on 8/30/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.util.Log;
import android.widget.Toast;
import android.os.Handler;

public class DrawingView extends SurfaceView {
    SurfaceHolder holder;
    Paint paint;
    float prevX = -1f;
    float prevY = -1f;
    int numDigits = 0;
    boolean confirmMode = false;
    boolean touchReleased = false;
    boolean doneDrawing = false;

    SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            initOffScreenBuffer();
            flushBuffer();
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        }
    };
    private Bitmap bitmap;
    private Canvas offScreen;

    public DrawingView(Context context) {
        super(context);
        init(null, 0);
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public void setConfirmMode() {
        confirmMode = true;
        paint.setColor(Color.WHITE);
    }
    private void init(AttributeSet attrs, int defStyle) {
        holder = getHolder();
        holder.addCallback(callback);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(4f);
    }

    private void initOffScreenBuffer() {
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);
        offScreen = new Canvas(bitmap);
        offScreen.drawColor(Color.WHITE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (doneDrawing) {
            ((Activity)(this.getContext())).finish();
            return false;
        }
        if (holder.getSurface().isValid()) {
            float x = event.getX();
            float y = event.getY();
            int action = event.getAction();

            if (prevX >= 0f && prevY >= 0f && action != MotionEvent.ACTION_DOWN) {
                offScreen.drawLine(prevX, prevY, x, y, paint);
                flushBuffer();
                touchReleased = false;
            }
            if (action == MotionEvent.ACTION_UP) {
                touchReleased = true;

                //after .5sec, clear the screen, store the coordinates and go to next digit
                //if all 4 digits of the PIN are drawn, either save them or compare them
                Handler handler = new Handler();
                handler.postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        if (touchReleased) {
                            touchReleased = false;
                            //clear the screen
                            initOffScreenBuffer();
                            flushBuffer();
                            numDigits++;
                            Log.d("DRAWAPP", "NUM DIGIT = " + numDigits);
                            if (numDigits == 4) {
                                numDigits = 0;
                                if (confirmMode == true) {
                                    //compare PINs
                                    if (true) {
                                        Toast.makeText(getContext(), "SUCCESSFUL", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "RETRY", Toast.LENGTH_SHORT).show();
                                    }
                                    doneDrawing = true;
                                }
                                else {
                                    //save the new PIN as an arraylist of pairs
                                    confirmMode = true;
                                    paint.setColor(Color.WHITE);
                                    Toast.makeText(getContext(), "Please confirm", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    }
                }, 500);

            }
            prevX = x;
            prevY = y;
            return true;
        }
        return false;
    }

    public void setPaintColor(int color) {
        paint.setColor(color);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void reset() {
        offScreen.drawColor(Color.WHITE);
        flushBuffer();
    }

    public void flushBuffer() {
        Canvas canvas = holder.lockCanvas();
        canvas.drawBitmap(bitmap, 0, 0, paint);
        holder.unlockCanvasAndPost(canvas);
    }
}