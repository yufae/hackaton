package com.hackaton.cloudant.nosql;

import java.util.List;

public class RecentProfile {
	private String _id;
	private String _rev;
	private List<String> personalAllergens;

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

	public List<String> getPersonalAllergens() {
		return personalAllergens;
	}

	public void setPersonalAllergens(List<String> personalAllergens) {
		this.personalAllergens = personalAllergens;
	}
}
