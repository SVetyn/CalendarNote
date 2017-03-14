package com.example.stanislav.svetalr1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Stanislav on 02.03.2017.
 */

public class DB {
    private static final String DB_NAME = "notedb";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "userTable";
    private static final String DB_TABLE_NOTE = "noteTable";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LOG = "login";
    public static final String COLUMN_PASSW = "password";
    public static final String COLUMN_EMAIL="email";
    public static final String COLUMN_CAPT="capt";
    public static final String COLUMN_TIME="time";
    public static final String COLUMN_DATE="date";


    private static final String DB_CREATE =
            "create table " + DB_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_LOG + " text, " +
                    COLUMN_PASSW + " text, " +
                    COLUMN_EMAIL + " text " +
                    ");";
    private static final String DB_CREATE_NOTE =
            "create table " + DB_TABLE_NOTE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_LOG + " text, " +
                    COLUMN_CAPT + " text, " +
                    COLUMN_DATE + " text, " +
                    COLUMN_TIME + " text " +
                    ");";

    private final Context mCtx;


    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DB(Context ctx) {
        mCtx = ctx;
    }

    // открыть подключение
    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    // закрыть подключение
    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    // получить все данные из таблицы DB_TABLE
    public Cursor getAllData() {
        return mDB.query(DB_TABLE, null, null, null, null, null, null);
    }

    public Cursor getSearchData(CharSequence s, CharSequence psw) {
        String selection=COLUMN_LOG+" = ? AND "+COLUMN_PASSW+" = ?";
        String[] selectionArgs=new String[]{s.toString(),psw.toString()};
        return mDB.query(DB_TABLE, null, selection, selectionArgs, null, null, null);
    }

    public Cursor getUserNote(CharSequence user, CharSequence date){
        String selection=COLUMN_LOG+" = ? AND "+COLUMN_DATE+" = ?";
        String[] selectionArgs=new String[]{user.toString(),date.toString()};
        return mDB.query(DB_TABLE_NOTE, null, selection, selectionArgs, null, null, null);
        //return mDB.query(DB_TABLE_NOTE, null, null, null, null, null, null);
    }

    // добавить запись в DB_TABLE
    public void addRec(String log, String passw, String email) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LOG, log);
        cv.put(COLUMN_PASSW, passw);
        cv.put(COLUMN_EMAIL, email);
        mDB.insert(DB_TABLE, null, cv);
    }

    public void addNote(String log, String capt, String time, String date) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LOG, log);
        cv.put(COLUMN_CAPT, capt);
        cv.put(COLUMN_TIME, time);
        cv.put(COLUMN_DATE, date);
        mDB.insert(DB_TABLE_NOTE, null, cv);
    }

    // удалить запись из DB_TABLE
    public void delRec(long id) {
        mDB.delete(DB_TABLE_NOTE, COLUMN_ID + " = " + id, null);
    }
/*
    public void updateRec(long idRec, String txt, int img, String number, String sex){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TXT, txt);
        cv.put(COLUMN_IMG, img);
        cv.put(COLUMN_NUMBER, number);
        cv.put(COLUMN_SEX, sex);
        mDB.update( DB_TABLE, cv, COLUMN_ID+" = " +idRec, null);
    }
*/
    public Cursor searchById(long id){
        return mDB.query(DB_TABLE, null, COLUMN_ID+" = "+id, null, null, null, null);
    }
    // класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        // создаем и заполняем БД
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
            db.execSQL(DB_CREATE_NOTE);

            /*ContentValues cv = new ContentValues();
            for (int i = 1; i < 50; i++) {
                cv.put(COLUMN_TXT, "sometext " + i);
                if((i%2)==0) {
                    cv.put(COLUMN_IMG, R.drawable.ic_action_contact_man);
                    cv.put(COLUMN_SEX, "man");
                }else{
                    cv.put(COLUMN_IMG, R.drawable.ic_action_contact_woman);
                    cv.put(COLUMN_SEX, "woman");
                }
                cv.put(COLUMN_NUMBER, "38099" + i+"2"+i+"554"+i);
                db.insert(DB_TABLE, null, cv);

            }*/
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
