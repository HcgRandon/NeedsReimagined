package com.devoverflow.reimagined.needs.res;

import com.devoverflow.reimagined.needs.res.json.JSONException;
import com.devoverflow.reimagined.needs.res.json.JSONObject;

public class JSONHelper {
	private String LOG_TAG  = "Needs";
	private NeedsLogger Log = new NeedsLogger();
	public JSONObject json;
	private boolean getAutoAdd = false;
	
	public JSONHelper() {
		json = new JSONObject();
	}
	
	public JSONHelper(boolean getAddMode) {
		this.getAutoAdd = getAddMode;
	}
	
	public JSONHelper(String importJSON) throws JSONException{
		parseInput(importJSON);
	}
	
	public JSONHelper(String importJSON, Boolean getAddMode) throws JSONException{
		parseInput(importJSON);
		this.getAutoAdd = getAddMode;
	}
	
	public JSONObject getJSONObject() {
		return json;
	}
	
	public void setJSONObject(JSONObject json) {
		 this.json = json;
	}
	
	//Insert inputed json into the JSONObject
	private void parseInput(String inputJson) throws JSONException{
		json = new JSONObject(inputJson.substring(inputJson.indexOf("{"), inputJson.lastIndexOf("}") + 1 ));
	}
	
	public void setInteger(String integerName, Integer intValue) {
		try {
			json.put(integerName, intValue);
		} catch (JSONException e) {
			Log.e(LOG_TAG, "JSONHelper -> StackTrace caught -> setInteger [" + intValue + "]");
			e.printStackTrace();
		}
	}
	
	public void setString(String stringName, String stringValue) {
		try {
			json.put(stringName, stringValue);
		} catch (JSONException e) {
			Log.e(LOG_TAG, "JSONHelper -> StackTrace caught -> setString [" + stringName + "]");
			e.printStackTrace();
		}
	}
	
	public void setBoolean(String booleanName, Boolean booleanValue) {
		try {
			json.put(booleanName, booleanValue);
		} catch (JSONException e) {
			Log.e(LOG_TAG, "JSONHelper -> StackTrace caught -> setBoolean [" + booleanName + "]");
			e.printStackTrace();
		}
	}
	
	public void set(String valueName, Object object) throws JSONException {
		json.put(valueName, object);
	}
	
	public void removeValue(String valueName) {
		if (json.has(valueName)) {
			json.remove(valueName);
		}
	}
	
	public Object get(String valueName, Object defaultValue) throws JSONException {
		if (json.has(valueName)) return json.get(valueName);
		else if (getAutoAdd) {
			json.put(valueName, defaultValue);
			return defaultValue;
		}
		else return defaultValue;
	}
	
	public Integer getInteger(String valueName, Integer defaultValue) {
		try {
			if (json.has(valueName)) {
				return json.getInt(valueName);
			}else if (getAutoAdd){
				json.put(valueName, defaultValue);
			}
		} catch (JSONException e) {
			Log.e(LOG_TAG, "JSONHelper -> StackTrace caught -> getInteger [" + valueName + "]");
			e.printStackTrace();
		}
		return defaultValue;
	}
	
	public String getString(String valueName, String defaultValue) {
		try {
			if (json.has(valueName)) {
				return json.getString(valueName);
			}else if (getAutoAdd){
				json.put(valueName, defaultValue);
			}
		} catch (JSONException e) {
			Log.e(LOG_TAG, "JSONHelper -> StackTrace caught -> getString [" + valueName + "]");
			e.printStackTrace();
		}
		return defaultValue;
	}
	
	public Boolean getBoolean(String valueName, Boolean defaultValue) {
		try {
			if (json.has(valueName)) {
				return json.getBoolean(valueName);
			}else if (getAutoAdd){
				json.put(valueName, defaultValue);
			}
		} catch (JSONException e) {
			Log.e(LOG_TAG, "JSONHelper -> StackTrace caught -> getBoolean [" + valueName + "]");
			e.printStackTrace();
		}
		return defaultValue;
	}
	
	public String toJsonString(int indentSpaces) {
		if (json != null) return json.toString(indentSpaces);
		else return null;
	}
}