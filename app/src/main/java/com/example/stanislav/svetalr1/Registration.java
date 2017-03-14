package com.example.stanislav.svetalr1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Registration extends AppCompatActivity implements View.OnClickListener {
    EditText reglog, regpassw,regemail;
    Button regbut;
    DB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        reglog=(EditText)findViewById(R.id.reglog);
        regpassw=(EditText)findViewById(R.id.regpassw);
        regemail=(EditText)findViewById(R.id.regemail);
        regbut=(Button)findViewById(R.id.regbut);
        regbut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.regbut:
                db=new DB(this);
                db.open();
                db.addRec(reglog.getText().toString(), regpassw.getText().toString(), regemail.getText().toString());
                db.close();
                finish();
            break;
        }
    }
}
