package com.example.sohaila.androidlabs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MessageFragment extends Fragment  {

    TextView msgView;
    TextView idView;
    Button deleteBtn;
    String myMsg;
    int myId;
    long dbID;
    ChatWindow chatWindow;

    public MessageFragment() {
        //default constructor
    }

    public static MessageFragment newInstance()
    {
        MessageFragment myFragment = new MessageFragment();
        return myFragment;
    }

    public void setChatWindow(ChatWindow chatWindow){
        this.chatWindow = chatWindow;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            myMsg = bundle.getString("chatMsg");
            myId = bundle.getInt("Id");
            dbID = bundle.getLong("dbId");
            Log.i("MessageFragment", myMsg);
        }
    }

    @Override

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.activity_message_fragment, container, false);

        msgView = (TextView) view.findViewById(R.id.message_text);
        msgView.setText(myMsg);
        idView = (TextView) view.findViewById(R.id.msgId);
        idView.setText(Integer.toString(myId));

        deleteBtn = (Button) view.findViewById(R.id.deleteMsg);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chatWindow != null){
                    //tablet
                    chatWindow.deleteMessage(myId);
                    getActivity().getFragmentManager().popBackStack();
                }

                else{

                    Log.i("tag","hello");
                    Intent intent = new Intent();
                    intent.putExtra("deleteMsgId", myId);
                    getActivity().setResult(10, intent);
                    getActivity().finish();
                }
            }

        });

        return view;
    }

}