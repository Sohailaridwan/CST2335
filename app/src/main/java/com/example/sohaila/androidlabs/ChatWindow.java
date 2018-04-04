package com.example.sohaila.androidlabs;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import android.widget.Toast;

public class ChatWindow extends Activity {

    protected static final String ACTIVITY_NAME = "ChatWindow";
    boolean isTab;
    FrameLayout frameLayout;
    String TAG = "activity_chat_window.xml";
    ArrayList<String> chat_messages;
    ListView chat_listview;
    EditText chat_edit_text;
    Button chat_send_btn;
    SQLiteDatabase db;
    int deleteId;
    long deleteBDid;
    ChatDatabaseHelper chatHelper;
    ContentValues newValues = new ContentValues();
    Cursor cursor;
    ChatWindow myActivity;
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
        frameLayout = (FrameLayout)findViewById(R.id.chatlist_frameview);

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

        messageAdapter = new ChatAdapter( this );
        chat_listview.setAdapter (messageAdapter);

        chatHelper = new ChatDatabaseHelper(ChatWindow.this);
        //SQLiteDatabase chatDB = chatHelper.getMdb();
        newValues = new ContentValues();

        chatHelper.open();
        cursor = chatHelper.getChatMessages();

//        if(cursor.moveToFirst()){
//            do{
//                String msg = chatHelper.getMessageFromCursor(cursor);
//                Log.i(ACTIVITY_NAME, "SQL Message: " +  msg);
//                Log.i(ACTIVITY_NAME, "Cursor's column count=" + cursor.getColumnCount());
//                cursor.moveToNext();
//            }while(!cursor.isAfterLast());
//            messageAdapter.notifyDataSetChanged();
//        }

        for (int i=0; i<cursor.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME, cursor.getColumnName(i));
        }

        myActivity = this;

        chat_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override

            public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {

                Object o = messageAdapter.getItem(position);

                String s = (String)o;

                Toast.makeText(getBaseContext(),s, Toast.LENGTH_SHORT).show();
                if(isTab){
                    // if the app is running on a tablet
                    MessageFragment fragment = new MessageFragment();
                    //   fragment.setChatWindow(ownActivity);
                    fragment.setChatWindow(myActivity);
                    Bundle bundle = new Bundle();
                    bundle.putString("chatMsg", s);
                    bundle.putInt("Id",position);
                    //bundle.putLong("dbId",id);
                    fragment.setArguments(bundle);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.chatlist_frameview, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                /* sending the activity to the newly created MessageDetails class */
                else{
                    Intent intent = new Intent(getApplicationContext(), MessageDetails.class);
                    intent.putExtra("chatMsg",s);
                    intent.putExtra("Id", position);
                    //intent.putExtra("dbId",id);
                    startActivityForResult(intent, 10);
                }
            }
        });

        chat_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chat_messages.add(chat_edit_text.getText().toString());

                ContentValues contentValues = new ContentValues();
                contentValues.put(ChatDatabaseHelper.COLUMN_MESSAGE, chat_edit_text.getText().toString());
                db.insert(ChatDatabaseHelper.TABLE_MESSAGES, "", contentValues);

                chat_edit_text.setText("");
                cursor = chatHelper.getChatMessages();
                messageAdapter.notifyDataSetChanged();
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
            chat_messages.remove(deleteId);
            cursor = chatHelper.getChatMessages();
            messageAdapter.notifyDataSetChanged();
//            deleteMessage(deleteId);
            Log.i(String.valueOf(ChatWindow.this), String.valueOf(chat_messages.size()));
        }
    }
    public void deleteMessage(int id){
        long deleteDBIdTab = messageAdapter.getItemId(id);
        chatHelper.remove(deleteDBIdTab);
        chat_messages.remove(id);
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

        public long getItemId(int position){
            cursor.moveToPosition(position);
            return chatHelper.getIdFromCursor(cursor);
        }
    }
}
