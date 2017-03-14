package com.example.stanislav.svetalr1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>{
    boolean loginData;
    TextView dateSelect;
    CalendarView calendarVar;
    ListView lvDate;
    DB db;
    SimpleCursorAdapter scAdapter;
    public static String userDate="";
    public static String userLogin="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        dateSelect = (TextView) findViewById(R.id.textView4);
        Intent intent = getIntent();
        dateSelect.setText(intent.getStringExtra("date"));
        userDate=intent.getStringExtra("date");
        userLogin=intent.getStringExtra("login");
        Button butadd = (Button)findViewById(R.id.addbtn);
        butadd.setOnClickListener(this);
        registerForContextMenu(butadd);

        db=new DB(this);
        db.open();
        String[] from=new String[]{DB.COLUMN_TIME,DB.COLUMN_CAPT};
        int[] to=new int[] {R.id.lvTime,R.id.lvCapt};
        //-----------------------------------------
        //-----------------------------------------------
        scAdapter=new SimpleCursorAdapter(this,R.layout.item,null,from,to,0);
        lvDate=(ListView)findViewById(R.id.lvData);
        lvDate.setAdapter(scAdapter);
        registerForContextMenu(lvDate);

        getSupportLoaderManager().initLoader(0,null,this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addbtn:
                Intent intent=new Intent(this, AddNotes.class);
                intent.putExtra("user",userLogin);
                intent.putExtra("date",userDate);
                startActivityForResult(intent,1);
                //Toast.makeText(this,"+",Toast.LENGTH_SHORT).show();
                break;
        };
    }


    @Override
    protected void onResume() {
        super.onResume();
       /* Cursor cursor=db.getUserNote(userLogin,userDate);
        if(cursor.moveToFirst()){
            int logdata = cursor.getColumnIndex( "time" );
            Toast.makeText(this,cursor.getString(logdata),Toast.LENGTH_SHORT).show();
            cursor.close();
        }*/
        //getSupportLoaderManager().getLoader(0).forceLoad();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        getSupportLoaderManager().getLoader(0).forceLoad();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        switch(v.getId()) {
            case R.id.lvData:
                menu.add(0, 1, 0, "Delete");
                break;
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getItemId() == 1) {
            // получаем из пункта контекстного меню данные по пункту списка
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item
                    .getMenuInfo();
            // извлекаем id записи и удаляем соответствующую запись в БД
            db.delRec(acmi.id);
            // получаем новый курсор с данными
            getSupportLoaderManager().getLoader(0).forceLoad();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new MyCursorLoader(this,db);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        scAdapter.swapCursor(data);
    }


    @Override
    public void onLoaderReset(Loader loader) {

    }

    static class MyCursorLoader extends CursorLoader {

        DB db;

        public MyCursorLoader(Context context, DB db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {

            Cursor cursor = db.getUserNote(CalendarActivity.userLogin,CalendarActivity.userDate);
            /*try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            return cursor;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
