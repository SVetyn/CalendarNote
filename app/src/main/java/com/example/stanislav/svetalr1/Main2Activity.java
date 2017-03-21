package com.example.stanislav.svetalr1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
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

    File directory;
    final int TYPE_PHOTO=1;
    final int REQUEST_CODE_PHOTO=2;
    ImageView ivPhoto;
    Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        createDirectory();
        calendarVar = (CalendarView) findViewById(R.id.calendarView3);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);

        fab.setOnClickListener(this);
        Intent intent=new Intent(this,Logo.class);
        startActivity(intent);
        Animation anim=null;
        anim= AnimationUtils.loadAnimation(this,R.anim.text_view_anim);
        fab.startAnimation(anim);

        sp = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC,0);
        soundIdClick = sp.load(this,R.raw.buttonsoundeffect,1);



        SharedPreferences sPref=getSharedPreferences( "LoginData",MODE_PRIVATE );
        String savedText = sPref.getString( "rgbackground", "" );

        h=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        ivPhoto.setImageBitmap((Bitmap) msg.obj);
                        break;
                    case 2:
                        ivPhoto.setBackgroundResource(R.drawable.background_calendar_custom_test);
                        break;
                }

            }
        };

        getBackgroundBitmap(savedText,this);
        startService(new Intent(this,NotesServiceMessage.class));
    }

    private void getBackgroundBitmap(final String uri, final Main2Activity parent){

        Thread t4=new Thread(new Runnable() {

            Message msg;

            @Override
            public void run() {

                Bitmap bitmap = null;

                if(!uri.equals("")) {
                    try {

                        bitmap = MediaStore.Images.Media.getBitmap(parent.getContentResolver(), Uri.parse(uri));
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                        msg=h.obtainMessage(1,0,0,bitmap);
                        h.sendMessage(msg);

                    } catch (IOException e) {
                        h.sendEmptyMessage(2);
                        e.printStackTrace();
                    }
                }
            }
        });
        t4.start();
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
        switch (requestCode) {
            case LOGIN_ACTIVITY_DATA:
                if (data == null) {return;}
                loginData = data.getBooleanExtra("name", false);
                if (loginData) Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
                break;
            case REQUEST_CODE_PHOTO:
                if (resultCode==RESULT_OK){
                    SharedPreferences sPref=getSharedPreferences( "LoginData",MODE_PRIVATE );
                    String savedText = sPref.getString( "rgbackground", "" );
                    getBackgroundBitmap(savedText,this);
                }
                break;

        }
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
                ed.putString("lastsession", "");
                ed.commit();
                onResume();
                break;
            case R.id.menubgcolor:
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,generateFileUri(TYPE_PHOTO));
                startActivityForResult(intent,REQUEST_CODE_PHOTO);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sp.release();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String currentDate=sdf.format(new Date());
        SharedPreferences spref = getSharedPreferences("LoginData", MODE_PRIVATE);
        SharedPreferences.Editor ed=spref.edit();
        if(spref.getBoolean("Login",false))
            ed.putString("lastsession", currentDate);
        ed.commit();
    }



    private  Uri generateFileUri(int type){

        File file=null;

        switch (type){
            case TYPE_PHOTO:
                file=new File(directory.getPath()+"/"+"photo_"+ String.valueOf(System.currentTimeMillis())+".jpg");

                break;
        }

        SharedPreferences sPref=getSharedPreferences( "LoginData",MODE_PRIVATE );
        SharedPreferences.Editor ed=sPref.edit();
        ed.putString( "rgbackground",Uri.fromFile(file).toString());
        ed.commit();

        return Uri.fromFile(file);
    }

    private void createDirectory(){
        directory=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"CalendarNoteGT");
        if(!directory.exists()){
            directory.mkdirs();
        }
    }


}
