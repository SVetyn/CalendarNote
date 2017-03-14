package com.example.stanislav.svetalr1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    EditText loginVar;
    EditText passwordVar;
    CheckBox rememberVar;
    Button loginButtonOk;
    Button loginButtonCancel;
    DB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginVar= (EditText) findViewById(R.id.loginVar);
        passwordVar= (EditText) findViewById(R.id.passwirdVar);
        rememberVar= (CheckBox) findViewById(R.id.rememberVar);
        loginButtonOk= (Button) findViewById(R.id.loginButtonOk);
        loginButtonCancel= (Button) findViewById(R.id.loginButtonCancel);

        loginButtonOk.setOnClickListener(this);
        loginButtonCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch ( v.getId() )
        {
            case R.id.loginButtonOk:
                db=new DB(this);
                db.open();
                String login="", passw="";
                Cursor cursor = db.getSearchData(loginVar.getText(), passwordVar.getText());
                if (cursor.moveToFirst()){
                    int logdata = cursor.getColumnIndex( "login" );
                    int passwdata = cursor.getColumnIndex( "password" );
                    login = cursor.getString(logdata);
                    passw = cursor.getString(passwdata);
                    cursor.close();
                    db.close();
                }
                if(!login.equals("") && !passw.equals("")) {
                    Intent intent = new Intent(this, CalendarActivity.class);
                    SharedPreferences spref = getSharedPreferences("LoginData", MODE_PRIVATE);
                    SharedPreferences.Editor ed=spref.edit();
                    ed.putBoolean("login", true);
                    ed.putString("user", loginVar.getText().toString());
                    if (rememberVar.isChecked()){
                        ed.putBoolean("remember",true);
                    }
                    ed.commit();
                    setResult(RESULT_OK, intent);
                    finish();
                } else{
                    Toast.makeText(this, "In correct login or password!", Toast.LENGTH_LONG).show();
                    passwordVar.setText("");
                }
                break;
            case R.id.loginButtonCancel:
                Intent intent=new Intent(this,Registration.class);
                startActivity(intent);
                break;
        }
    }

    @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==event.KEYCODE_BACK){

            //MainActivity mainActivity=(MainActivity)getParent();
            //mainActivity.finish();
            //finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
