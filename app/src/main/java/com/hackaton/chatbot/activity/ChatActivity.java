package com.hackaton.chatbot.activity;

import android.content.Context;
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
                        List<String> allergenNameList = findAllergens(keywords);
                        
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

    private List<String> findAllergens(List<String> keywords) {
        CloudantConnector cc = new CloudantConnector();
        List<Food> foods = cc.getFoodsByName(keywords);
        List<Ingredient> allIngredients = new ArrayList<Ingredient>();
        for(Food food : foods){
            allIngredients.addAll(food.getIngredient());
        }
        List<String> userAllergens = getUserAllergens();
        List<String> detectedAllergenNames = new ArrayList<String>();
        for(String userAllergen : userAllergens){
            for(Ingredient ingrident : allIngredients){
                if(userAllergen.equalsIgnoreCase(ingrident.getId())){
                    detectedAllergenNames.add(ingrident.getName());
                }
            }
        }
        return detectedAllergenNames;
    }

    private void intialize() {
        this.messagesList = (MessagesList) findViewById(R.id.messagesList);
        this.messagesAdapter = new MessagesListAdapter<Message>("user", null);
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
