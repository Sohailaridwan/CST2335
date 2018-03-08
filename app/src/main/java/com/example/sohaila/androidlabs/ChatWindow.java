package com.example.sohaila.androidlabs;

import android.app.Activity;
import android.content.Context;
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

import java.util.ArrayList;

public class ChatWindow extends Activity {

    ArrayList<String> chat_messages;
    ListView chat_listview;
    EditText chat_edit_text;
    Button chat_send_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        chat_messages = new ArrayList<String>();
        chat_listview = (ListView) findViewById(R.id.chatlist);
        chat_edit_text = (EditText) findViewById(R.id.chat_edit_text);
        chat_send_btn = (Button) findViewById(R.id.chat_send_btn);

        final ChatAdapter message_adapter = new ChatAdapter(this);
        chat_listview.setAdapter(message_adapter);

        chat_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chat_messages.add(chat_edit_text.getText().toString());
                chat_edit_text.setText("");
                message_adapter.notifyDataSetChanged();
            }
        });
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
