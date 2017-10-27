package com.hackaton.chatbot;

import android.util.Log;

import com.hackaton.chatbot.activity.ChatActivity;
import com.hackaton.cloudant.nosql.Food;
import com.ibm.watson.developer_cloud.conversation.v1.Conversation;
import com.ibm.watson.developer_cloud.conversation.v1.model.Context;
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
    private final static String VERSION = Conversation.VERSION_DATE_2017_05_26;
    private final static String USERNAME = "4fb9020e-8065-4186-bf49-aa8b559d9fa0";
    private final static String PASSWORD = "XtIFzlZnfmaf";
    private final static String WORKSPACE = "ef32b504-2897-4184-8884-0a3162046606";

    private ChatBot() {
        this.service = new Conversation(VERSION, USERNAME, PASSWORD);
    }

    public static ChatBot getInstance() {
        if (instance == null)
            return new ChatBot();
        else
            return instance;
    }

    // send messagess
    public List<String> sendMessage(String inputMessage) {
        List<String> responses = new ArrayList<String>();

        InputData.Builder input = new InputData.Builder(inputMessage);
        MessageOptions.Builder optionsBuilder = new MessageOptions.Builder(WORKSPACE).input(input.build());
        if (context != null) {
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



    /*
    public List<String> sendMessage(String inputMessage) {
        List<String> responses = new ArrayList<String>();
        MessageOptions options = new MessageOptions.Builder(WORKSPACE).input(new InputData.Builder(fixMessageinput(inputMessage)).build()).build();
        Log.i("CHATBOT", "Request: " + options.toString());
        MessageResponse response = this.service.message(options).execute();
        Log.i("CHATBOT", "Response: " + response.toString());
        List<RuntimeIntent> intents = response.getIntents();
        for (RuntimeIntent intent : intents) {
            String intentName = intent.getIntent();
            Log.i("ACN", "intent: " + intentName);
            if (intentName.equalsIgnoreCase("food")) {
                ChatActivity.intentCaught = true;
                return null;
            }
        }
        if (context != null)
            context = response.getContext();
        List<String> responseMessages = response.getOutput().getText();
        for (String responseString : responseMessages)
            responses.add(responseString);
        return responses;
    }
    */

    // send keywords as ingredients
    public List<String> sendKeywords(List<Food> foods) {
        List<String> responses = new ArrayList<String>();

        InputData.Builder input = new InputData.Builder(convertListToString(foods));
        MessageOptions.Builder optionsBuilder = new MessageOptions.Builder(WORKSPACE).input(input.build());
        String[] keywordStrings = new String[foods.size()];

        String[] foodNames = new String[foods.size()];
        for (int i = 0; i < foods.size(); i++) {
            foodNames[i] = foods.get(i).getFood_name();
        }

        if (context != null) {
            context.put("food", foodNames);
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

            }
        }
        if (context == null)
            context = response.getContext();

//        if (!ChatActivity.intentCaught) {
            List<String> responseMessages = response.getOutput().getText();
            for (String responseString : responseMessages)
               responses.add(responseString);
//        }
        return responses;
    }

    private String fixMessageinput(String message) {
        return message.trim();
    }

    private String convertListToString(List<Food> foodList){
        StringBuilder sb = new StringBuilder("");
        for(Food f : foodList){
            if(!sb.toString().equalsIgnoreCase("")){
                sb.append(",");
            }
            sb.append(fixMessageinput(f.getFood_name()));
        }
        return sb.toString();
    }


}
