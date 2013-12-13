package com.devoverflow.reimagined.needs.res;

import java.util.logging.Logger;

public class NeedsLogger {
	private Logger mLogger; //nLogger;
	
	public NeedsLogger() {
		mLogger = Logger.getLogger("Minecraft");
	}
	
	public void e(String tag, String err) {
		mLogger.severe("[" + tag + "]: " + err);
	}
	
	public void i(String tag, String inf) {
		mLogger.info("[" + tag + "]: " + inf);
	}
}
