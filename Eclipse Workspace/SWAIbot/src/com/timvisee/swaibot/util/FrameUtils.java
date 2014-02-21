package com.timvisee.swaibot.util;

import javax.swing.UIManager;

public class FrameUtils {

	/**
	 * Set the current look and feel to the system's default
	 */
	public static void setToDefaultLookAndFeel() {
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { }
	}
}
