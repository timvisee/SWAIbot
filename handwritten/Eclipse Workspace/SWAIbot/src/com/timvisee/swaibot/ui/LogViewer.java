package com.timvisee.swaibot.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import com.timvisee.swaibot.log.LogListener;

public class LogViewer extends JPanel implements LogListener {
	
	private static final long serialVersionUID = -6948374693931958120L;
	
	private JScrollPane sp;
	private JList<String> l;

	/**
	 * Constructor
	 */
	public LogViewer() {
		// Construct the parent class
		super();
		
		setLayout(new BorderLayout());
		
		// Add a JList
		this.l = new JList<String>(new DefaultListModel<String>());
		this.sp = new JScrollPane(this.l);
		add(this.sp);
		
		// Set the preferred size of the text area
		super.setPreferredSize(new Dimension(250, 200));
	}
	
	/**
	 * Scroll to the bottom of the text area
	 */
	public void scrollToBottom() {
		JScrollBar vert = this.sp.getVerticalScrollBar();
		vert.setValue(vert.getMaximum());
	}

	@Override
	public void log(String txt) {
		// Add the line to the log
		DefaultListModel<String> dlm = (DefaultListModel<String>) this.l.getModel();
		dlm.addElement(txt);
		
		// Scroll to the bottom
		scrollToBottom();
	}

	@Override
	public void clear() {
		// Clear the text area
		DefaultListModel<String> dlm = (DefaultListModel<String>) this.l.getModel();
		dlm.clear();
	}
	
	@Override
	public void setLog(List<String> log) {
		// Clear the text area
		clear();
		
		DefaultListModel<String> dlm = (DefaultListModel<String>) this.l.getModel();
		
		// Set the log lines
		for(String txt : log)
			dlm.addElement(txt);
		
		// Scroll to the bottom
		scrollToBottom();
	}
}
