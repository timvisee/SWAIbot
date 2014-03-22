package com.timvisee.swaibot.ui;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MyTextArea extends JScrollPane {
	
	private static final long serialVersionUID = 4705485635062939818L;
	
	/** @var area Text area instance */
	private JTextArea area;
	
	/**
	 * Constructor
	 */
	public MyTextArea() {
		// Construct the parent class
		super();
		
		// Instantiate the text area
		this.area = new JTextArea();
		
		// Change some text area properties
		this.area.setLineWrap(false);
		this.area.setWrapStyleWord(true);
		
		// Change the viewport view
		this.setViewportView(this.area);
	}
	
	/**
	 * Get the text area instance
	 * @return Text area instance
	 */
	public JTextArea getTextArea() {
		return this.area;
	}
}
