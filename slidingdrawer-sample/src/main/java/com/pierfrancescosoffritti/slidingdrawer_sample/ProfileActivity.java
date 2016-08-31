package com.pierfrancescosoffritti.slidingdrawer_sample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pierfrancescosoffritti.slidingdrawer_sample.dummy.ChatAdapter;

import java.util.ArrayList;
import java.util.Random;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView userName;
    private ImageView call, videoCall;
    private String user1 = "Mayur", user2 = "Pritam";
    private EditText messageEditText;
    private ChatAdapter chatAdapter;
    private ListView msgListView;
    private ArrayList<ChatMessage> chatlist;
    private Random random;
    private FloatingActionButton sendButton, videoCallButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        userName = (TextView) findViewById(R.id.tvName);
        call = (ImageView) findViewById(R.id.ivCall);
        videoCall = (ImageView) findViewById(R.id.ivVideoCall);
        messageEditText = (EditText) findViewById(R.id.messageEditText);
        random = new Random();
        sendButton = (FloatingActionButton) findViewById(R.id.sendMessageButton);
        videoCallButton = (FloatingActionButton) findViewById(R.id.videoCall);
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0){
                    videoCallButton.setVisibility(View.GONE);
                    sendButton.setVisibility(View.VISIBLE);
                } else {
                    videoCallButton.setVisibility(View.VISIBLE);
                    sendButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        msgListView = (ListView) findViewById(R.id.msgListView);

        sendButton.setOnClickListener(this);

        // ----Set autoscroll of listview when a new message arrives----//
        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);

        chatlist = new ArrayList<ChatMessage>();
        chatAdapter = new ChatAdapter(ProfileActivity.this, chatlist);
        msgListView.setAdapter(chatAdapter);
        String callType = getIntent().getExtras().getString("ChatOrCall");
        if(callType.equals("Call")){
            userName.setText("Connecting Mayur...");
            call.setImageResource(R.drawable.end);
            videoCall.setVisibility(View.GONE);
        } else if (callType.equals("Chat")){
            userName.setText("Mayur Lathkar");
            call.setImageResource(R.drawable.call);
            videoCall.setVisibility(View.VISIBLE);
        }
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(findViewById(R.id.profileLayout).getApplicationWindowToken(), 1);
//        messageEditText.setShowSoftInputOnFocus(false);
    }

    @Override
    public void onClick(View view) {
        sendTextMessage(view);
    }

    public void sendTextMessage(View v) {
        String message = messageEditText.getEditableText().toString();
        if (!message.equalsIgnoreCase("")) {
            final ChatMessage chatMessage = new ChatMessage(user1, user2,
                    message, "" + random.nextInt(1000), true);
            chatMessage.setMsgID();
            chatMessage.body = message;
            chatMessage.Date = ChatMethods.getCurrentDate();
            chatMessage.Time = ChatMethods.getCurrentTime();
            messageEditText.setText("");
            chatAdapter.add(chatMessage);
            chatAdapter.notifyDataSetChanged();
        }
    }
}
