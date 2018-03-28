package com.example.sohaila.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.FrameLayout;
import java.util.ArrayList;
import android.util.Log;
import android.database.Cursor;
import android.content.Intent;
public class ChatWindow extends Activity {

    protected static final String ACTIVITY_NAME = "ChatWindow";
    boolean isTab;
    FrameLayout frameLayout;
    String TAG = "activity_chat_window.xml";
    ArrayList<String> chat_messages;
    ArrayList <String> storeChat = new ArrayList <String>();
    ListView chat_listview;
    EditText chat_edit_text;
    Button chat_send_btn;
    SQLiteDatabase db;
    int deleteId;
    long deleteBDid;
    ChatDatabaseHelper chatHelper;
    ContentValues newValues = new ContentValues();
    Cursor cursor;
    ChatAdapter messageAdapter;
    Button sendBtn;
    EditText editTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        ChatDatabaseHelper dbHelper = new ChatDatabaseHelper(ChatWindow.this);
        db = dbHelper.getWritableDatabase();

        chat_messages = dbHelper.getAllMessages(db);
        chat_listview = (ListView) findViewById(R.id.chatlist);
        chat_edit_text = (EditText) findViewById(R.id.chat_edit_text);
        chat_send_btn = (Button) findViewById(R.id.chat_send_btn);
        frameLayout = (FrameLayout)findViewById(R.id.entryType);

        //phone layout use:
        if(frameLayout == null){
            Log.i(ACTIVITY_NAME, "frame is not loaded");
            isTab = false;
        }
        //tablet layout use:
        else{
            Log.i(ACTIVITY_NAME, "frame is loaded");
            isTab = true;
        }

        final ListView list = (ListView)findViewById(R.id.chatlist);
        messageAdapter = new ChatAdapter( this );
        list.setAdapter (messageAdapter);
        editTxt = (EditText)findViewById(R.id.chat_edit_text);
        sendBtn = (Button)findViewById(R.id.chat_send_btn);

        chatHelper = new ChatDatabaseHelper(ChatWindow.this);
        //SQLiteDatabase chatDB = chatHelper.getMdb();
        newValues = new ContentValues();


        chatHelper.open();
        cursor = chatHelper.getChatMessages();

        if(cursor.moveToFirst()){
            do{
                String msg = chatHelper.getMessageFromCursor(cursor);
                Log.i(ACTIVITY_NAME, "SQL Message: " +  msg);
                Log.i(ACTIVITY_NAME, "Cursor's column count=" + cursor.getColumnCount());
                storeChat.add(msg);
                cursor.moveToNext();
            }while(!cursor.isAfterLast());
            messageAdapter.notifyDataSetChanged();

        }

        for (int i=0; i<cursor.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME, cursor.getColumnName(i));
        }


        final ChatAdapter message_adapter = new ChatAdapter(this);
        chat_listview.setAdapter(message_adapter);

        chat_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chat_messages.add(chat_edit_text.getText().toString());

                ContentValues contentValues = new ContentValues();
                contentValues.put(ChatDatabaseHelper.COLUMN_MESSAGE, chat_edit_text.getText().toString());
                db.insert(ChatDatabaseHelper.TABLE_MESSAGES, "", contentValues);

                chat_edit_text.setText("");
                message_adapter.notifyDataSetChanged();
            }
        });
    }
    public void onActivityResult(int requestCode, int responseCode, Intent data){
        if(requestCode == 10  && responseCode == 10) {
            // received data from fragment to delete the message
            Bundle bundle = data.getExtras();
            deleteId = bundle.getInt("deleteMsgId");
            //deleteBDid = bundle.getLong("deleteDBMsgId");
            deleteBDid = messageAdapter.getItemId(deleteId);
            chatHelper.remove(deleteBDid);
            storeChat.remove(deleteId);
            cursor = chatHelper.getChatMessages();
            messageAdapter.notifyDataSetChanged();
            //deleteMessage(deleteId);
            //Log.i(String.valueOf(ChatWindow.this), String.valueOf(chatList.size()));
        }
    }
    public void deleteMessage(int id){
        long deleteDBIdTab = messageAdapter.getItemId(id);
        chatHelper.remove(deleteDBIdTab);
        storeChat.remove(id);
        messageAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        try {
            db.close();
        } catch (Exception e) {
        }
    }

    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context ctx){
            super(ctx, 0);
        }

        public int getCount(){
            return chat_messages.size();
        }

        public String getItem(int position){
            return chat_messages.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if(position%2==0){
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            }else{
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }

            TextView message_text = (TextView) result.findViewById(R.id.message_text);
            message_text.setText(getItem(position));
            return result;
        }

        public long getId(int position){
            return position;
        }
    }
}
