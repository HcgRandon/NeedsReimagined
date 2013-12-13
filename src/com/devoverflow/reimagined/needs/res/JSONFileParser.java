package com.devoverflow.reimagined.needs.res;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.devoverflow.reimagined.needs.Needs;
import com.devoverflow.reimagined.needs.res.json.JSONException;

public class JSONFileParser {
	private NeedsLogger Log;
	private File file;
	public JSONHelper json;
	private String LOG_TAG = Needs.STATIC_LOG_TAG + "->JSONFileParser";
	
	//worlds
	private parseError parseerror = null;
	
	public JSONFileParser(File tobeparsed) {
		this.file     = tobeparsed;
		this.Log      = new NeedsLogger();
		this.json     = new JSONHelper();
		
		String string = getFileContents();
		if (!string.isEmpty()) parseJSON(string);
	}
	
	class parseError {
		private String error_string;
		
		public parseError(String error) {
			this.error_string = error;
			Log.e(LOG_TAG, error);
		}
		
		public String getError() {
			return error_string;
		}
	}
	
	private void setError(String error) {
		this.parseerror = new parseError(error);
	}
	
	public parseError error() {
		return this.parseerror;
	}
	
	private String getFileContents() {
		StringBuffer fileContent = new StringBuffer("");
		try {
			FileInputStream fis = new FileInputStream(this.file);
			byte[] buffer = new byte[1024];
		
			while (fis.read(buffer) != -1) {
				fileContent.append(new String(buffer));
			}
			fis.close();
		} catch (FileNotFoundException e) {
			Log.i(LOG_TAG, "File doesn't exists");
		} catch (IOException e) {
			setError("An IOException occured");
		}
		return fileContent.toString();
	}
	
	private void parseJSON(String raw) {
		try  {
			json = new JSONHelper(raw);
		} catch(JSONException e) {
			if (raw.isEmpty()) {
				json = new JSONHelper();
				save();
			} else if (raw.substring(raw.indexOf("{") + 1, raw.lastIndexOf("}")) == null) {
				json = new JSONHelper();
				save();
			} else {
				setError("JSON parse failed, File not empty using in-ram settings");
			}
		}
	}
	
	public void save() {
		if (this.parseerror != null) {
			return;
		}
		try {
			FileOutputStream fos = new FileOutputStream(this.file);
			fos.write(this.json.toJsonString(2).getBytes());
			fos.close();
		} catch (IOException e) {
			setError("IOException whilst saving file.");
			e.printStackTrace();
		}
	}
	
}
