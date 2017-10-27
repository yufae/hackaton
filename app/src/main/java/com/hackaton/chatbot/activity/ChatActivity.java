package com.hackaton.chatbot.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import com.hackaton.chatbot.ChatBot;
import com.hackaton.chatbot.model.Message;
import com.hackaton.chatbot.model.User;
import com.hackaton.cloudant.nosql.CloudantConnector;
import com.hackaton.cloudant.nosql.Food;
import com.hackaton.cloudant.nosql.Ingredient;
import com.hackaton.languageunderstanding.LanguageUnderstanding;
import com.hackaton.visualrecognition.R;
import com.hackaton.visualrecognition.activity.BaseActivity;
import com.hackaton.visualrecognition.activity.Result;
import com.hackaton.visualrecognition.data.Class;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BaseActivity {

    private MessagesListAdapter<Message> messagesAdapter;
    private MessagesList messagesList;
    private MessageInput messageInput;
    public final static User USER = new User("user", "User");
    public final static User CHATBOT = new User("bot", "ChatBot");
    private static final ChatBot BOT = ChatBot.getInstance();
    public static boolean categoryCaught = false;
    public static boolean intentCaught = false;
    private Context context;

    private static final float MIN_SCORE = 0.7f;

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

                // send user message
                String messageInput = input.toString();
                Message message = new Message("user", USER, input.toString());
                Log.i("ACN", "conversation: " + input.toString());
                addMessage(message);
                List<String> responses = BOT.sendMessage(messageInput);
                if (responses != null) {
                    for (String responseString : responses) {
                        Log.i("ACN", "conversation: " + responseString);
                        Message response = new Message("watson", CHATBOT, responseString);
                        addMessage(response);
                    }
                }
                Log.i("ACN", "intentCaught = " + intentCaught);

                // intent is caught and natural language understanding process
                if (intentCaught) {
                    LanguageUnderstanding languageUnderstanding = new LanguageUnderstanding();
                    List<Class> keywords = languageUnderstanding.getKeywords(messageInput);
                    for (Class keyword : keywords) {
                        Log.i("keyword", "keyword: " + keyword.getClassName());
                    }

                    // category is caught
                    if (categoryCaught) {
                        List<String> keywordNames = new ArrayList<String>();
                        for (Class keyword : keywords) {
                            if (keyword.getScore() >= MIN_SCORE)
                                keywordNames.add(keyword.getClassName());
                        }
                        Log.i("ACN", "CategoryCaught = " + categoryCaught);

                        // get allergens
                        List<Food> allergenNameList = findAllergens(keywordNames);

                        // send foods
                        List<String> keywordSendResults = BOT.sendKeywords(allergenNameList);
                        for (String keywordSendResult : keywordSendResults) {
                            Message keywordSendResultMessage = new Message("watson", CHATBOT, keywordSendResult);
                            addMessage(keywordSendResultMessage);
                        }

                        // go to resultactivity
                        Intent resultIntent = new Intent(context, Result.class);
                        Food resultFood = new Food();
                        if (allergenNameList.size() >= 1) {
                            resultFood = allergenNameList.get(0);
                        }
                        resultIntent.putExtra("resultChat", resultFood);
                        setResult(Activity.RESULT_OK, resultIntent);
                        startActivity(resultIntent);
                    }
                }
                return true;
            }
        });
    }

    private List<Food> findAllergens(List<String> keywords) {
        CloudantConnector cc = new CloudantConnector();
        List<Food> foods = cc.getFoodsByName(keywords);
        List<String> userAllergens = getUserAllergens();
        List<Food> detectedAllergenFood = new ArrayList<Food>();
        if (foods != null) {
            for (Food food : foods) {
                for (Ingredient ingredient : food.getIngredient()) {
                    if (userAllergens.contains(ingredient.getId())) {
                        food.getDetectedAllergens().add(ingredient);
                        if (!detectedAllergenFood.contains(food)) {
                            detectedAllergenFood.add(food);
                        }
                    }
                }
            }
        }
        return detectedAllergenFood;
    }

    private void intialize() {
        this.messagesList = (MessagesList) findViewById(R.id.messagesList);
        this.messagesAdapter = new MessagesListAdapter<Message>("user", null);
        this.messagesList.setAdapter(this.messagesAdapter);
        this.messageInput = (MessageInput) findViewById(R.id.inputMessage);
        List<String> responses = BOT.sendMessage("hi");
        for (String responseString : responses) {
            Log.i("ACN", "conversation: " + responseString);
            Message response = new Message("watson", CHATBOT, responseString);
            this.addMessage(response);
        }
    }

    private void addMessage(Message message) {
        this.messagesAdapter.addToStart(message, true);
    }
}
