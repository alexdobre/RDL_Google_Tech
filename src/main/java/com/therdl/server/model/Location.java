package com.therdl.server.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.i18n.client.LocaleInfo;

/**
 * This describes the location where a service offer may be 
 * From a town to a coutry to the internet
 * @author Alex
 *
 */
public class Location implements Serializable{

	private static final long serialVersionUID = 1L;

	private ArrayList<String> countries;
	
	private ArrayList<String> towns;
	
	private ArrayList<String>  regions;
	
	private ArrayList<LocaleInfo> languages;

	public ArrayList<String> getCountries() {
		return countries;
	}

	public void setCountries(ArrayList<String> countries) {
		this.countries = countries;
	}

	public ArrayList<String> getTowns() {
		return towns;
	}

	public void setTowns(ArrayList<String> towns) {
		this.towns = towns;
	}

	public ArrayList<String> getRegions() {
		return regions;
	}

	public void setRegions(ArrayList<String> regions) {
		this.regions = regions;
	}

	public ArrayList<LocaleInfo> getLanguages() {
		return languages;
	}

	public void setLanguages(ArrayList<LocaleInfo> languages) {
		this.languages = languages;
	}
}
