package com.hackaton.visualrecognition.db;

import java.util.List;

public class CrossAllergen {
	private String _id;
	private String _rev;
	private List<String> cross_allergen;

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

	public List<String> getCrossAllergen() {
		return cross_allergen;
	}

	public void setCrossAllergen(List<String> crossAllergen) {
		this.cross_allergen = crossAllergen;
	}
}
