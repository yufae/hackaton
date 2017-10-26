package com.hackaton.chatbot;

import android.util.Log;

import com.hackaton.chatbot.activity.ChatActivity;
import com.ibm.watson.developer_cloud.conversation.v1.Conversation;
import com.ibm.watson.developer_cloud.conversation.v1.model.Context;
import com.ibm.watson.developer_cloud.conversation.v1.model.GetDialogNodeOptions;
import com.ibm.watson.developer_cloud.conversation.v1.model.InputData;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.conversation.v1.model.RuntimeIntent;

import java.util.ArrayList;
import java.util.List;

public class ChatBot {
    private static ChatBot instance;
    private Conversation service;
    private static Context context;

    // credentials
    private final String VERSION = Conversation.VERSION_DATE_2017_05_26;
    private final String USERNAME = "4fb9020e-8065-4186-bf49-aa8b559d9fa0";
    private final String PASSWORD = "XtIFzlZnfmaf";
    private final String WORKSPACE = "ef32b504-2897-4184-8884-0a3162046606";

    private ChatBot() {
        this.service = new Conversation(VERSION, USERNAME, PASSWORD);
    }

    public static ChatBot getInstance() {
        if (instance == null)
            return new ChatBot();
        else
            return instance;
    }

    public List<String> sendMessage(String inputMessage) {
        List<String> responses = new ArrayList<String>();

        InputData.Builder input = new InputData.Builder(inputMessage);
        MessageOptions.Builder optionsBuilder = new MessageOptions.Builder(WORKSPACE).input(input.build());
        if (context != null) {
            context.put("my_list", new String[]{"avocado"});
            optionsBuilder.context(context);
        }

        MessageOptions options = optionsBuilder.build();
        Log.i("CHATBOT", "Message: " + options.toString());
        MessageResponse response = this.service.message(options).execute();
        Log.i("CHATBOT", "Response: " + response.toString());
        if (context != null)
            Log.i("CHATBOT", "Context: " + context.toString());
        List<RuntimeIntent> intents = response.getIntents();
        for (RuntimeIntent intent : intents) {
            String intentName = intent.getIntent();
            Log.i("ACN", "intent: " + intentName);
            if (intentName.equalsIgnoreCase("food")) {
                ChatActivity.intentCaught = true;
                return null;
            }
        }
        if (context == null)
            context = response.getContext();

        if (!ChatActivity.intentCaught) {
            List<String> responseMessages = response.getOutput().getText();
            for (String responseString : responseMessages)
                responses.add(responseString);
        }
        return responses;
    }
}
