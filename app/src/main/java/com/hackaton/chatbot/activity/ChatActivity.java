package com.hackaton.chatbot.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import com.hackaton.chatbot.ChatBot;
import com.hackaton.chatbot.model.Message;
import com.hackaton.chatbot.model.User;
import com.hackaton.languageunderstanding.LanguageUnderstanding;
import com.hackaton.visualrecognition.R;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.List;

public class ChatActivity extends Activity {

    private MessagesListAdapter<Message> messagesAdapter;
    private MessagesList messagesList;
    private MessageInput messageInput;
    public final static User USER = new User("user", "User");
    public final static User CHATBOT = new User("CHAT_BOT", "ChatBot");
    private final static ChatBot CHAT_BOT = ChatBot.getInstance();
    public static boolean categoryCaught = false;
    public static boolean intentCaught = false;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.context = this.getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.intialize();

        this.messageInput.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                String messageInput = input.toString();
                Message message = new Message("user", USER, input.toString());
                Log.i("ACN", "conversation: " + input.toString());
                addMessage(message);
                List<String> responses = CHAT_BOT.sendMessage(messageInput);
                Log.i("ACN", "intentCaught = " + intentCaught);
                if (intentCaught) {
                    LanguageUnderstanding languageUnderstanding = new LanguageUnderstanding();
                    List<String> keywords = languageUnderstanding.getKeywords(messageInput);
                    Log.i("ACN", "CategoryCaught = " + categoryCaught);
                    if (categoryCaught) {

                    } else {
                        for (String responseString : responses) {
                            Log.i("ACN", "conversation: " + responseString);
                            Message response = new Message("watson", CHATBOT, responseString);
                            addMessage(response);
                        }
                    }
                } else {
                    for (String responseString : responses) {
                        Log.i("ACN", "conversation: " + responseString);
                        Message response = new Message("watson", CHATBOT, responseString);
                        addMessage(response);
                    }
                }
                return true;
            }
        });
    }

    private void intialize() {
        this.messagesList = (MessagesList) findViewById(R.id.messagesList);
        this.messagesAdapter = new MessagesListAdapter<Message>("chat", null);
        this.messagesList.setAdapter(this.messagesAdapter);
        this.messageInput = (MessageInput) findViewById(R.id.inputMessage);
        List<String> responses = CHAT_BOT.sendMessage("hi");
        for (String responseString : responses) {
            Log.i("ACN", "conversation: " + responseString);
            Message response = new Message("watson", CHATBOT, responseString);
            this.addMessage(response);
        }
    }

    private void addMessage(Message message) {
        this.messagesAdapter.addToStart(message, true);
    }

    private void clear() {
        this.messagesAdapter.clear();
    }
}