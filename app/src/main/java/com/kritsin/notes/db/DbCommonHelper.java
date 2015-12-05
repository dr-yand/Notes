package com.kritsin.notes.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbCommonHelper extends SQLiteOpenHelper {
	private Context mContext;
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "notes.db";

    public static final String USERS_TABLE_NAME = "users";
    private static final String USERS_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + USERS_TABLE_NAME + " ("
                    + " id integer primary key AUTOINCREMENT NOT NULL, "
                    + " server_id integer,"
                    + " name TEXT"
                    +" );";

	public static final String NOTES_TABLE_NAME = "notes";
	private static final String NOTES_TABLE_CREATE =
			"CREATE TABLE IF NOT EXISTS " + NOTES_TABLE_NAME + " ("
					+ " id integer primary key AUTOINCREMENT NOT NULL, "
                    + " user_id integer default 0, "
                    + " server_id integer default 0, "
					+ " note TEXT, "
                    + " verify integer default 0, "
                    + " unique(server_id) ON CONFLICT REPLACE);";
    
    private static SQLiteDatabase mDb;
    private static SQLiteDatabase mDbReadable;

    public DbCommonHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mContext = context;
    }
    public void onCreate(SQLiteDatabase db) { 
        db.execSQL(NOTES_TABLE_CREATE);
        db.execSQL(USERS_TABLE_CREATE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
        db.execSQL("drop table  IF EXISTS " + NOTES_TABLE_NAME);
        db.execSQL("drop table  IF EXISTS " + USERS_TABLE_NAME);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    
    public SQLiteDatabase open(){
    	if(mDb==null)
    		mDb = getWritableDatabase();
    	return mDb; 
    }

    public SQLiteDatabase openReadable(){
        if(mDbReadable==null)
            mDbReadable = getReadableDatabase();
        return mDbReadable;
    }

    public void close(){

    }
}
