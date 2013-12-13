package com.devoverflow.reimagined.needs.res;

import com.devoverflow.reimagined.needs.res.json.JSONObject;

public class JSONParser {
	private JSONHelper jhelp;
	
	public JSONParser(String JSON) {
		new JSONHelper(JSON);
		//{"worlds": {"superflat": {"gamerules": {"domobspawn": true}}}}
		
	}
	
	public Object get(String dotpath, Object defaultValue) {
		Object currentObject = new Object();
		
		for (String curdot : dotpath.split(".")) {
			if (jhelp.json.has(curdot)) currentObject = jhelp.json.get(curdot);
			else return defaultValue;
			
			if (currentObject instanceof JSONObject) continue;
			else return currentObject;
		}
		
		return null;
	}
	
	public void set(String dotpath, Object value) {
		String[] dotsplit = dotpath.split(".");
		if (dotsplit.length == 0) {
			jhelp.set(dotsplit[0], value);
			return;
		}
		for (int i=0;i<dotsplit.length;i++) {
			if (i != dotsplit.length) {
				//get the next instance
				//instance = new
			}
		}
	}
	
//	private Object fromJSON(Object raw) {
//		if (raw instanceof JSONObject) {}
//			
//	}
}
