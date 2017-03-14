package com.example.stanislav.svetalr1;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class Logo extends Activity {
    TextView txtLogo;
    ImageView imgV;
    Thread t;
    private AnimationDrawable mAnimationDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        //txtLogo=(TextView)findViewById(R.id.txtLogo);
        //Animation anim=null;
        //anim= AnimationUtils.loadAnimation(this,R.anim.text_view_anim);
        //txtLogo.startAnimation(anim);
        imgV=(ImageView)findViewById(R.id.imageView);
        imgV.setBackgroundResource(R.drawable.calendaranumation);

        mAnimationDrawable=(AnimationDrawable)imgV.getBackground();
        t=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(3);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();
        //finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAnimationDrawable.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
