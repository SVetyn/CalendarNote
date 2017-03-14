package com.example.stanislav.svetalr1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddNotes extends AppCompatActivity implements View.OnClickListener{
    Button addbtn;
    EditText captTxt;
    TimePicker timeTxt;
    String user,date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        addbtn=(Button)findViewById(R.id.btnAddNote);
        addbtn.setOnClickListener(this);

        Intent intent=getIntent();
        user=intent.getStringExtra("user");
        date=intent.getStringExtra("date");

        captTxt=(EditText)findViewById(R.id.captionTxt);
        timeTxt=(TimePicker)findViewById(R.id.timePicker);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddNote:
                String time=String.valueOf(timeTxt.getCurrentHour())+":"+String.valueOf(timeTxt.getCurrentMinute());
                DB db=new DB(this);
                db.open();
                db.addNote(user,captTxt.getText().toString(),time,date);
                db.close();
                Intent intent=new Intent(this,CalendarActivity.class);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }
}
