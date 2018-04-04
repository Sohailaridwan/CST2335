package com.example.sohaila.androidlabs;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.content.ContentValues;
import android.content.Context;
import java.util.ArrayList;

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    public static final String CHAT_TABLE = "messages";
    public static final String KEY_ID = "id";
    public static final String KEY_MESSAGE = "message";
    // string array which will return the chat table fields
    public static final String[] CHAT_FIELDS = new String[]{
            KEY_ID,
            KEY_MESSAGE
    };


    protected static final String ACTIVITY_NAME = "ChatDatabaseHelper";

    private static String DATABASE_NAME = "messages.db";
    private static int VERSION_NUM = 5;

    public static final String TABLE_MESSAGES = "messages";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MESSAGE = "message";
    public static final String READALL_MESSAGE_TABLE = "SELECT " + COLUMN_ID + ", " + COLUMN_MESSAGE
            + " FROM " + TABLE_MESSAGES;


    public static final String DATABASE_CREATE = "create table "
            + TABLE_MESSAGES + "( " + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_MESSAGE
            + " text not null);";
    Context context;
    SQLiteDatabase mdb;

    public ChatDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
        Log.i(ACTIVITY_NAME, "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        onCreate(sqLiteDatabase);
        Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);
    }
    // open database
    public ChatDatabaseHelper open() {
        if(mdb == null){
            mdb = getWritableDatabase();
        }

        return this;
    }

    public void close(){
        if(mdb != null){
            mdb.close();
        }
    }

    // retrieving data
    public Cursor getChatMessages(){
        return mdb.query(CHAT_TABLE, CHAT_FIELDS, null, null, null, null, null);
    }

    public String getMessageFromCursor(Cursor cursor){
        String msg = cursor.getString(cursor.getColumnIndex(KEY_MESSAGE));
        return msg;
    }


    public long getIdFromCursor(Cursor cursor){
        long id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
        return id;
    }

    public void insert(ContentValues content){
        mdb.insert(CHAT_TABLE, null, content);
    }

    public void remove(long id){
        int deletedRecrod =  mdb.delete(CHAT_TABLE, KEY_ID + "=" + id, null);
        Log.i("Deleted ",Integer.toString(deletedRecrod));
    }

    public ArrayList<String> getAllMessages(SQLiteDatabase db){
        ArrayList<String> messages = new ArrayList<String>();

        Cursor cursor;
        cursor = db.rawQuery(ChatDatabaseHelper.READALL_MESSAGE_TABLE, null);
        int messageIndex = cursor.getColumnIndex(ChatDatabaseHelper.COLUMN_MESSAGE);

        Log.i(ACTIVITY_NAME, "Cursor's column count = " + cursor.getColumnCount());
        // Then use a for loop to print out the name of each column returned by the cursor.
        for (int colIndex = 0; colIndex < cursor.getColumnCount(); colIndex++) {
            Log.i(ACTIVITY_NAME, "Column name of " + colIndex + " = " + cursor.getColumnName(colIndex));
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            messages.add(cursor.getString(messageIndex));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + cursor.getString(messageIndex));
            cursor.moveToNext();
        }

        return messages;
    }
}
