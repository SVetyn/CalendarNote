package com.example.stanislav.svetalr1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.IBinder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * Created by Stanislav on 21.03.2017.
 */

public class NotesServiceMessage extends Service {

    NotificationManager nm;
    Notification myNotication;

    @Override
    public void onCreate() {
        super.onCreate();
        nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sendNotes();
        return super.onStartCommand(intent, flags, startId);
    }

    void sendNotes(){

        Notification.Builder builder=new Notification.Builder(this);
        SharedPreferences spref = getSharedPreferences("LoginData",MODE_PRIVATE);
        String user_login = spref.getString("user","");
        String last_session=spref.getString("lastsession","");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate=sdf.format(new Date());

        Intent intent=new Intent(this,CalendarActivity.class);
        intent.putExtra("login",user_login);
        intent.putExtra("date",currentDate);
        PendingIntent pIntent=PendingIntent.getActivity(this,0,intent,0);

        DB db=new DB(this);
        db.open();
        Cursor search_data=db.getUserNote(user_login,currentDate);
        int number_notes=search_data.getCount();
        search_data.close();
        db.close();
        if(user_login.equals(""))
        {
            user_login = "to Calendar Notes.";
        }else
        {
            if(number_notes>0)
            user_login += ".\n You have "+String.valueOf(number_notes)+" notes today.";
            else{
                user_login += ".\n You don`t have notes today.";
            }
        }

        builder.setAutoCancel(false);
        builder.setTicker("Welcome "+user_login);
        builder.setContentTitle("Calendar Notes");
        builder.setContentText("Welcome "+user_login);
        builder.setSmallIcon(R.drawable.ic_message_calendar);
        builder.setContentIntent(pIntent);
        builder.setOngoing(false);
        builder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS);
        if(!last_session.equals(""))
            builder.setSubText("Last session "+last_session);   //API level 16
        builder.setNumber(number_notes);
        builder.setAutoCancel(true);
        builder.build();

        myNotication = builder.build();
        nm.notify(1, myNotication);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
