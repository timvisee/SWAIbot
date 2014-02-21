package com.timvisee.swaibot.log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Log {
	
	private List<String> log = new ArrayList<String>();
	private List<LogListener> listeners = new ArrayList<LogListener>();
	
	/**
	 * Constructor
	 */
	public Log() {
		// Reset the log arraylist
		this.log = new ArrayList<String>();
	}
	
	/**
	 * Log something
	 * @param text Text to log
	 */
	public void log(String text) {
		Calendar now = Calendar.getInstance();
		DecimalFormat df = new DecimalFormat("00");
		String timeStr = df.format(now.get(Calendar.HOUR_OF_DAY)) + ":" + df.format(now.get(Calendar.MINUTE)) + ":" + df.format(now.get(Calendar.SECOND));
		
		text = "[" + timeStr + "] " + text.trim();
		
		this.log.add(text);
		
		for(LogListener l : this.listeners)
			l.log(text);
	}
	
	/**
	 * Get the log
	 * @return Log
	 */
	public List<String> getLog() {
		return this.log;
	}
	
	/**
	 * Set the log
	 * @param log The log
	 */
	public void setLog(List<String> log) {
		this.log = log;
		
		for(LogListener l : listeners)
			l.setLog(log);
	}
	/**
	 * Clear the log
	 */
	public void clear() {
		this.log.clear();
		
		for(LogListener l : this.listeners)
			l.clear();
	}
	
	/**
	 * Register a listener
	 * @param l Listener to register
	 */
	public void registerListener(LogListener l) {
		this.listeners.add(l);
		
		l.setLog(this.log);
	}
	
	/**
	 * Unregister a listener
	 * @param l Listener to unregister
	 * @return False if no listener was unregistered
	 */
	public boolean unregisterListener(LogListener l) {
		return this.listeners.remove(l);
	}
}
