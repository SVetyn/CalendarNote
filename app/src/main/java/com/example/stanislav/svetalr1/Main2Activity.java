package com.example.stanislav.svetalr1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CalendarView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{
    boolean loginData=false;
    CalendarView calendarVar;
    final int LOGIN_ACTIVITY_DATA=1;

    final int MAX_STREAMS=1;
    SoundPool sp;
    int soundIdClick;
    int streamIdClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        calendarVar = (CalendarView) findViewById(R.id.calendarView3);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        Intent intent=new Intent(this,Logo.class);
        startActivity(intent);
        Animation anim=null;
        anim= AnimationUtils.loadAnimation(this,R.anim.text_view_anim);
        fab.startAnimation(anim);

        sp = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC,0);
        soundIdClick = sp.load(this,R.raw.buttonsoundeffect,1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences spref = getSharedPreferences("LoginData",MODE_PRIVATE);
        loginData=spref.getBoolean("login",false);
        if(!loginData){
            Intent intent = new Intent(this,MainActivity.class);
            startActivityForResult(intent, LOGIN_ACTIVITY_DATA);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        streamIdClick = sp.play(soundIdClick,1,1,1,0,1);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate=sdf.format(new Date(calendarVar.getDate()));
        Intent intent = new Intent(this, CalendarActivity.class);
        intent.putExtra("date", currentDate);
        SharedPreferences spref = getSharedPreferences("LoginData",MODE_PRIVATE);
        intent.putExtra("login", spref.getString("user",""));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        loginData = data.getBooleanExtra("name",false);
        if(loginData) Toast.makeText(this,"Login",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuexit:
                android.app.DialogFragment newFragment = new ClassForDialogWindows();
                newFragment.show(getFragmentManager(),"Info");
                break;
            case R.id.logoffmenu:
                SharedPreferences spref = getSharedPreferences("LoginData", MODE_PRIVATE);
                SharedPreferences.Editor ed=spref.edit();
                ed.putBoolean("login", false);
                ed.commit();
                onResume();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sp.release();
    }
}
