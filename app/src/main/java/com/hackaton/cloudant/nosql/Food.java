package com.hackaton.cloudant.nosql;

import java.util.List;

public class Food {
	private String _id;
	private String _rev;
	private List<String> ingredient;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String get_rev() {
		return _rev;
	}
	public void set_rev(String _rev) {
		this._rev = _rev;
	}
	public List<String> getIngredient() {
		return ingredient;
	}
	public void setIngredient(List<String> icerik) {
		this.ingredient = icerik;
	}
}