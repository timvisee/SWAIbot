package com.timvisee.swaibot;

import com.timvisee.swaibot.ai.AICore;
import com.timvisee.swaibot.log.Log;
import com.timvisee.swaibot.progress.ProgressDialog;
import com.timvisee.swaibot.robot.Robot;
import com.timvisee.swaibot.util.FrameUtils;

public class Core {
	
	public static Core instance;
	
	public static Log log;
	public static Log packetLog;
	
	private static Thread t;
	private static MainFrame mf;
	
	private AICore aic;
	
	public static ProgressDialog pd;
	
	public Core() {
		instance = this;
		
		// Set the LAF to the systems default
		FrameUtils.setToDefaultLookAndFeel();
		
		// Initialize and show a loading frame
		pd = new ProgressDialog(null, SWAIbot.APP_NAME, "Initializing...");
		pd.setVisible(true);
		
		// Instantiate the log
		log = new Log();
		log.log("Initializing...");
		packetLog = new Log();
		
		// Initialize the AI Core
		aic = new AICore();
		
		new SWAIbot();
		
		// Create a thread to run the main frame instance in
		t = new Thread(
				new Runnable() {
					@Override
					public void run() {
						mf = new MainFrame();
					}
				});
		t.start();
	}
	
	@SuppressWarnings({ "static-access", "deprecation" })
	public static void restart() {
		if(t != null) {
			if(mf.instance.ct != null)
				mf.instance.ct.disconnect();
			mf.instance.ctt.stop();
			mf.instance.rtt.stop();
			mf.dispose();
			t.stop();
		}
		
		new Core();
	}
	
	public AICore getAICore() {
		return this.aic;
	}
	
	public MainFrame getMainFrame() {
		return this.mf;
	}
	
	public Robot getRobot() {
		return getMainFrame().r;
	}
}
