package com.timvisee.swaibot.log;

import java.util.List;

public interface LogListener {
	
	public void log(String text);
	
	public void clear();
	
	public void setLog(List<String> log);
}
