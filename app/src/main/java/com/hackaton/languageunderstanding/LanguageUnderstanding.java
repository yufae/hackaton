package com.hackaton.languageunderstanding;

import android.util.Log;

import com.hackaton.chatbot.activity.ChatActivity;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.CategoriesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.CategoriesResult;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsResult;

import java.util.ArrayList;
import java.util.List;

public class LanguageUnderstanding {
    private NaturalLanguageUnderstanding service;
    private final static String USERNAME = "4e891c46-94a4-4248-8d61-1c284a9c8eb5";
    private final static String PASSWORD = "i1PwXTTcPRci";

    public LanguageUnderstanding() {
        this.service = new NaturalLanguageUnderstanding(NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27, USERNAME, PASSWORD);
    }

    public List<String> getKeywords(String message) {
        List<String> keywords = new ArrayList<String>();
        CategoriesOptions categoriesOptions = new CategoriesOptions();
        KeywordsOptions keywordsOptions = new KeywordsOptions.Builder().sentiment(true).build();
        Features features = new Features.Builder().keywords(keywordsOptions).categories(categoriesOptions).build();
        AnalyzeOptions analyzeOptions = new AnalyzeOptions.Builder().text(message).features(features).build();
        AnalysisResults analysisResults = this.service.analyze(analyzeOptions).execute();
        List<CategoriesResult> categoriesResults = analysisResults.getCategories();
        for (CategoriesResult categoriesResult : categoriesResults) {
            String categoryName = categoriesResult.getLabel();
            Log.i("ACN", "category: " + categoryName);
            if (categoryName.contains("food") || categoryName.contains("drink")) {
                ChatActivity.categoryCaught = true;
                List<KeywordsResult> keywordsResults = analysisResults.getKeywords();
                for (KeywordsResult keywordsResult : keywordsResults) {
                    String keywordName = keywordsResult.getText();
                    Log.i("ACN", "keyword: " + keywordName);
                    keywords.add(keywordName);
                }
                return keywords;
            }
        }
        return null;
    }
}
