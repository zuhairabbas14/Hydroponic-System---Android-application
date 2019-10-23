package com.zuhair.zuhair.realscholarshipapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Member;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ChatList extends AppCompatActivity implements RoomListener {

    private String channelID = "IaMrwvsq603OCF7a";
    private String roomName = "observable-room";
    private EditText editText;
    private Scaledrone scaledrone;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

    //    Main2Activity.loadAd();

        editText = (EditText) findViewById(R.id.editText);

        messageAdapter = new MessageAdapter(this);
        messagesView = (ListView) findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            name = mBundle.getString("name");
        } else {
            SharedPreferences mPrefs = this.getSharedPreferences("com.zuhair.zuhair.realscholarshipapp", MODE_PRIVATE);
            name = mPrefs.getString("chatName", "");
        }

        MemberData data = new MemberData(name, getRandomColor());

        scaledrone = new Scaledrone(channelID, data);
        scaledrone.connect(new Listener() {
            @Override
            public void onOpen() {
                System.out.println("Scaledrone connection open");
                // Since the MainActivity itself already implement RoomListener we can pass it as a target
                scaledrone.subscribe(roomName, ChatList.this);
            }

            @Override
            public void onOpenFailure(Exception ex) {
                System.err.println(ex);
            }

            @Override
            public void onFailure(Exception ex) {
                System.err.println(ex);
                tryReconnecting(0);
            }

            @Override
            public void onClosed(String reason) {
                System.err.println(reason);
            }

            private void tryReconnecting(final int reconnectAttempt) {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        final Scaledrone drone = new Scaledrone("IaMrwvsq603OCF7a");
                        drone.connect(new Listener() {
                            @Override
                            public void onOpen() {

                            }

                            @Override
                            public void onOpenFailure(Exception ex) {

                            }

                            @Override
                            public void onFailure(Exception ex) {
                                tryReconnecting(reconnectAttempt + 1);
                            }

                            @Override
                            public void onClosed(String reason) {

                            }
                        });
                        // set everything up again..
                    }
                }, reconnectAttempt * 1000);
            }
        });

    }

    // Successfully connected to Scaledrone room
    @Override
    public void onOpen(Room room) {
        System.out.println("Conneted to room");
    }

    // Connecting to Scaledrone room failed
    @Override
    public void onOpenFailure(Room room, Exception ex) {
        System.err.println(ex);
    }

    // Received a message from Scaledrone room
    @Override
    public void onMessage(Room room, final JsonNode json, final Member member) {
        // To transform the raw JsonNode into a POJO we can use an ObjectMapper

        final ObjectMapper mapper = new ObjectMapper();
        try {
            // member.clientData is a MemberData object, let's parse it as such
            final MemberData data = mapper.treeToValue(member.getClientData(), MemberData.class);
            // if the clientID of the message sender is the same as our's it was sent by us
            boolean belongsToCurrentUser = member.getId().equals(scaledrone.getClientID());
            // since the message body is a simple string in our case we can use json.asText() to parse it as such
            // if it was instead an object we could use a similar pattern to data parsing
            final Message message = new Message(json.asText(), data, belongsToCurrentUser);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageAdapter.add(message);
                    // scroll the ListView to the last added element
                    messagesView.setSelection(messagesView.getCount() - 1);
                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private String getRandomColor() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer("#");
        while(sb.length() < 7){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, 7);
    }

    public void sendMessage(View view) {
        String message = editText.getText().toString();
        if (message.length() > 0) {
            scaledrone.publish("observable-room", message);
            editText.getText().clear();
        }
    }
}

class MemberData {
    private String name;
    private String color;

    public MemberData(String name, String color) {
        this.name = name;
        this.color = color;
    }

    // Add an empty constructor so we can later parse JSON into MemberData using Jackson
    public MemberData() {
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
