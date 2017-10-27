
package com.hackaton.cloudant.nosql;

import java.util.List;

public class Allergens {
	private String _id;
	private String _rev;
	private List<Allergen> allergen_list;

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

	public List<Allergen> getAllergen_list() {
		return allergen_list;
	}

	public void setAllergen_list(List<Allergen> allergen_list) {
		this.allergen_list = allergen_list;
	}

}
