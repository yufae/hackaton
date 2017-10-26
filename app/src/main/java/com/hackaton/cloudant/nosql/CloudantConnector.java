package com.hackaton.cloudant.nosql;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;

public class CloudantConnector {
	private static final String CLOUNDANT_URL = "https://e399e3cb-1ba0-4550-9a86-04e5cb63a7f0-bluemix:2b8c16725df0248773581061deb76dfd7c0dbe26ff1ea183ab0d30fccdf5cc0a@e399e3cb-1ba0-4550-9a86-04e5cb63a7f0-bluemix.cloudant.com";
	private static final String DB_NAME = "foods";
	
	public static Database getInstance(){
		Database db = null;
		try {
			CloudantClient client = ClientBuilder.url(new URL(CLOUNDANT_URL)).build();
			db = client.database(DB_NAME, true);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return db;
	}
	
	public List<Allergen> getAllergens(){
		Allergens allerges = CloudantConnector.getInstance().find(Allergens.class, "allergens");
		return allerges.getAllergen_list();
	}
	
	public static Food getIngridents(String foodName){
		Food food = CloudantConnector.getInstance().find(Food.class, foodName);
		return food;
	}
	
	public List<String> getCrossAllergens(List<Allergen> allergens){
		List<String> crossAllergen = new ArrayList<String>();
		if(allergens != null){
			Database db = CloudantConnector.getInstance();
			for (Allergen allergen : allergens) {
				CrossAllergen cross_allergens = db.find(CrossAllergen.class, allergen.getId());
				if(cross_allergens != null){
					crossAllergen.addAll(cross_allergens.getCrossAllergen());
				}
			}
		}
		return crossAllergen;
	}
	
	public static void saveRecentProfile(List<String> personalAllergens){
		RecentProfile recentProfile = getRecentProfile();
		if(recentProfile !=null){
			CloudantConnector.getInstance().remove(recentProfile);
		}
		recentProfile = new RecentProfile();
		recentProfile.set_id("RecentProfile");
		recentProfile.setPersonalAllergens(personalAllergens);
		CloudantConnector.getInstance().save(recentProfile);
	}
	
	public static RecentProfile getRecentProfile(){
		RecentProfile recentProfile = null;
		try{
			recentProfile = CloudantConnector.getInstance().find(RecentProfile.class, "RecentProfile");
		} catch(Exception e){
			System.out.println("No_data found!");
		}
		return recentProfile;
	}
	
	public static void main(String[] args) {
		try {
			CloudantClient client = ClientBuilder.url(new URL(CLOUNDANT_URL)).build();
			Database db = client.database(DB_NAME, true);
			
			// Get Allergen List
			Allergens all_allergens = db.find(Allergens.class, "allergens");
			System.out.println(all_allergens.toString());
			
			// Get CrossAllergen of Shell Fish
			CrossAllergen cross_allergens = db.find(CrossAllergen.class, "shell_fish");
			System.out.println(cross_allergens.getCrossAllergen());
			
			// Get Meat Ball Ingredient
			Food food = db.find(Food.class, "meatball");
			getIngridents("meatball");
			System.out.println(food.getIngredient());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
