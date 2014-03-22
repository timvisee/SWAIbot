package com.timvisee.swaibot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.Timer;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.timvisee.swaibot.progress.ProgressDialog;
import com.timvisee.swaibot.robot.Robot;
import com.timvisee.swaibot.robot.RobotAITask;
import com.timvisee.swaibot.ui.LogViewer;
import com.timvisee.swaibot.ui.RobotVisualizationViewer;
import com.timvisee.swaibot.util.WebsiteUtils;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -1783995652285885881L;

	public static ConnectionThread ct;
	public static Thread ctt;
	public static RobotAITask rt;
	public static Thread rtt;
	public static Robot r = new Robot();
	
	public static MainFrame instance;
	
	public MainFrame() {
		// Construct the parent class
		super(SWAIbot.APP_NAME + " - v" + SWAIbot.VERSION_NAME);
		
		instance = this;
		
		// Build the frame UI
		buildUI();
		
		// Set the minimum size of the frame
		setPreferredSize(new Dimension(855, 615));
		setMinimumSize(new Dimension(700, 615));
		
		// Pack the frame contents
		pack();
		
		// Set the locatoin by the playform
		setLocationByPlatform(true);
		
		// Enable frame resizing
		setResizable(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// TODO: Start robot AI task

		Core.pd.setStatus("Starting AI Core...");
		Core.instance.log.log("Starting AI Core...");
		rt = new RobotAITask();
		rtt = new Thread(rt);
		rtt.start();
		Core.instance.getAICore().init();
		Core.pd.setStatus("AI Core started!");
		Core.instance.log.log("AI Core started!");

		Core.pd.setStatus("Starting server...");
		Core.instance.log.log("Starting server...");
		ct = new ConnectionThread();
		rt.ct = ct;
		ctt = new Thread(ct);
		ctt.start();
		
		Core.pd.setStatus("Server started!");
		Core.instance.log.log("Server started!");
		
		Core.pd.setStatus("Initialized! Cave Johnson here!");
		Core.pd.dispose();
		Core.instance.log.log("Initialized! Cave Johnson here!");
		
		// Show the frame
		setVisible(true);
	}
	
	/**
	 * Build the main frame UI
	 */
	public void buildUI() {
		// Set the layout of the frame
		setLayout(new BorderLayout(8, 8));
		getRootPane().setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		
		// Add the proper panels to the layout
		add(getConnectionPanel(), BorderLayout.LINE_START);
		add(getMainPanel(), BorderLayout.CENTER);
		add(getLogPanel(), BorderLayout.PAGE_END);
		add(getPreformanceMonitorPanel(), BorderLayout.LINE_END);

		// Build the menu bar
		buildMenuBar();
	}
	
	public JComponent getConnectionPanel() {
		final MainFrame self = this;
		
		// Set up the tab control
		JTabbedPane conTab = new JTabbedPane();
		
		// Set up the wrapper
		JPanel wrapper = new JPanel();
		wrapper.setLayout(new GridBagLayout());
		wrapper.setPreferredSize(new Dimension(175, wrapper.getPreferredSize().height));
		wrapper.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		
		JPanel conPnl = new JPanel();
		conPnl.setBorder(new CompoundBorder(new TitledBorder("Connection"), new EmptyBorder(1, 3, 1, 3)));
		conPnl.setLayout(new GridLayout(3, 1));
		final JButton conBtn = new JButton("Connect");
		conPnl.add(conBtn);
		conBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				conBtn.setEnabled(false);

				Core.instance.pd = new ProgressDialog(self, "Connecting to robot");
				Core.instance.pd.setStatus("Searching the robot...");
				Core.instance.pd.setVisible(true);
				Core.instance.getMainFrame().ct.run();
			}
		});
		final JLabel conTypeLbl = new JLabel("Connection: -");
		conPnl.add(conTypeLbl);
		final JLabel conLbl = new JLabel("Status: Not connected");
		conPnl.add(conLbl);
		
		Timer t = new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(ct != null) {
					if(ct.connected) {
						conTypeLbl.setText("Connection: Simulator");
						conLbl.setText("Status: Connected!");
					} else {
						conTypeLbl.setText("Connection: -");
						conLbl.setText("Status: Not connected");
					}
				}
			}
		});
		t.setInitialDelay(0);
		t.setRepeats(true);
		t.start();

		// Add the control panel to the wrapper with the proper constraints
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;
		wrapper.add(conPnl, c);
		
		// Add the wrapper to the tab control and return the tab control
		conTab.add("Connection", wrapper);
		return conTab;
	}
	
	/**
	 * Get the main panel to put on the frame
	 * @return JPanel Main panel
	 */
	public JComponent getMainPanel() {
		JTabbedPane mainTab = new JTabbedPane();
		
		// Define the robot panel
		JPanel rbtPnl = new JPanel();
		//rbtPnl.setBackground(Color.WHITE);
		rbtPnl.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8));
		
		// Add the media display
		JPanel dispPnl = new JPanel();
		dispPnl.setBorder(new TitledBorder("Robot Visualization"));
		dispPnl.add(getRobotVisualizationViewer());
		rbtPnl.add(dispPnl);
		
		mainTab.addTab("Robot", rbtPnl);
		
		return mainTab;
	}
	
	public JComponent getLogPanel() {
		JTabbedPane logTab = new JTabbedPane();
		logTab.setBackground(Color.WHITE);
		
		// Define the robot panel
		JPanel logPnl = new JPanel();
		logPnl.setLayout(new GridLayout(1, 2));
		logPnl.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		LogViewer logViewer = new LogViewer();
		logViewer.setPreferredSize(new Dimension(240, 160));
		Core.instance.log.registerListener(logViewer);
		JPanel logViewerPnl = new JPanel();
		logViewerPnl.setLayout(new BorderLayout());
		logViewerPnl.setBorder(new TitledBorder("Core Log"));
		logViewerPnl.add(logViewer);
		logPnl.add(logViewerPnl);
		
		LogViewer packetLogViewer = new LogViewer();
		packetLogViewer.setPreferredSize(new Dimension(240, 160));
		Core.instance.packetLog.registerListener(packetLogViewer);
		JPanel packetLogViewerPnl = new JPanel();
		packetLogViewerPnl.setLayout(new BorderLayout());
		packetLogViewerPnl.setBorder(new TitledBorder("Packet Log"));
		packetLogViewerPnl.add(packetLogViewer);
		logPnl.add(packetLogViewerPnl);
		
		logTab.addTab("Log", logPnl);
		
		return logTab;
	}
	
	public JComponent getPreformanceMonitorPanel() {
		JTabbedPane pmTab = new JTabbedPane();
		pmTab.setBackground(Color.WHITE);
		
		// Set up the wrapper
		JPanel wrapper = new JPanel();
		wrapper.setLayout(new GridBagLayout());
		wrapper.setPreferredSize(new Dimension(175, wrapper.getPreferredSize().height));
		wrapper.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		JPanel sysPnl = new JPanel();
		sysPnl.setBorder(new CompoundBorder(new TitledBorder("System"), new EmptyBorder(1, 3, 1, 3)));
		sysPnl.setLayout(new GridBagLayout());
		JPanel rbtPnl = new JPanel();
		rbtPnl.setBorder(new CompoundBorder(new TitledBorder("Robot AI"), new EmptyBorder(1, 3, 1, 3)));
		rbtPnl.setLayout(new GridBagLayout());
		JPanel serverPnl = new JPanel();
		serverPnl.setBorder(new CompoundBorder(new TitledBorder("Server"), new EmptyBorder(1, 3, 1, 3)));
		serverPnl.setLayout(new GridBagLayout());
		JPanel javaVmPnl = new JPanel();
		javaVmPnl.setBorder(new CompoundBorder(new TitledBorder("Java VM"), new EmptyBorder(1, 3, 1, 3)));
		javaVmPnl.setLayout(new GridBagLayout());
		
		GridBagConstraints cViews = new GridBagConstraints();
		cViews.fill = GridBagConstraints.HORIZONTAL;
		cViews.anchor = GridBagConstraints.PAGE_START;
		cViews.weightx = 1;
		cViews.weighty = 1;

		cViews.gridx = 0;
		cViews.gridy = 0;
		sysPnl.add(new JLabel("CPU Cores:"), cViews);
		cViews.gridx = 1;
		cViews.gridy = 0;
		final JLabel cpuCountLbl = new JLabel("-");
		sysPnl.add(cpuCountLbl, cViews);
		
		cViews.gridx = 0;
		cViews.gridy = 1;
		sysPnl.add(new JLabel("CPU:"), cViews);
		cViews.gridx = 1;
		cViews.gridy = 1;
		final JLabel cpuLbl = new JLabel("-");
		sysPnl.add(cpuLbl, cViews);

		cViews.gridx = 0;
		cViews.gridy = 0;
		rbtPnl.add(new JLabel("Rec Moves:"), cViews);
		cViews.gridx = 1;
		cViews.gridy = 0;
		final JLabel rbtMovesLbl = new JLabel("-");
		rbtPnl.add(rbtMovesLbl, cViews);
		cViews.gridx = 0;
		cViews.gridy = 1;
		rbtPnl.add(new JLabel("TPS:"), cViews);
		cViews.gridx = 1;
		cViews.gridy = 1;
		final JLabel rbtFpsLbl = new JLabel("-");
		rbtPnl.add(rbtFpsLbl, cViews);
		
		cViews.gridx = 0;
		cViews.gridy = 0;
		serverPnl.add(new JLabel("Pkts Received:"), cViews);
		cViews.gridx = 1;
		cViews.gridy = 0;
		final JLabel packetReceivedLbl = new JLabel("-");
		serverPnl.add(packetReceivedLbl, cViews);

		cViews.gridx = 0;
		cViews.gridy = 1;
		serverPnl.add(new JLabel("Pkts Send:"), cViews);
		cViews.gridx = 1;
		cViews.gridy = 1;
		final JLabel packetSendLbl = new JLabel("-");
		serverPnl.add(packetSendLbl, cViews);

		cViews.gridx = 0;
		cViews.gridy = 2;
		serverPnl.add(new JLabel("TPS:"), cViews);
		cViews.gridx = 1;
		cViews.gridy = 2;
		final JLabel serverFpsLbl = new JLabel("-");
		serverPnl.add(serverFpsLbl, cViews);

		cViews.gridx = 0;
		cViews.gridy = 0;
		javaVmPnl.add(new JLabel("Used RAM:"), cViews);
		final JLabel ramLbl = new JLabel("-");
		cViews.gridx = 1;
		cViews.gridy = 0;
		javaVmPnl.add(ramLbl, cViews);

		cViews.gridx = 0;
		cViews.gridy = 1;
		javaVmPnl.add(new JLabel("Free RAM:"), cViews);
		cViews.gridx = 1;
		cViews.gridy = 1;
		final JLabel ramFreeLbl = new JLabel("-");
		javaVmPnl.add(ramFreeLbl, cViews);

		cViews.gridx = 0;
		cViews.gridy = 2;
		javaVmPnl.add(new JLabel("Assigned RAM:"), cViews);
		cViews.gridx = 1;
		cViews.gridy = 2;
		final JLabel ramTotLbl = new JLabel("-");
		javaVmPnl.add(ramTotLbl, cViews);

		cViews.gridx = 0;
		cViews.gridy = 3;
		javaVmPnl.add(new JLabel("Max RAM:"), cViews);
		final JLabel ramMaxLbl = new JLabel("?");
		cViews.gridx = 1;
		cViews.gridy = 3;
		javaVmPnl.add(ramMaxLbl, cViews);

		final OperatingSystemMXBean bean = ManagementFactory.getOperatingSystemMXBean();
		final Runtime r = Runtime.getRuntime();
		
		JButton collectGarbageBtn = new JButton("Collect Garbage");
		collectGarbageBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				r.gc();
			}
		});
		cViews.gridx = 0;
		cViews.gridy = 4;
		cViews.gridwidth = 2;
		cViews.insets = new Insets(4, 0, 0, 0);
		javaVmPnl.add(collectGarbageBtn, cViews);
		
		Timer t = new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cpuCountLbl.setText(String.valueOf(bean.getAvailableProcessors()));
				cpuLbl.setText("-");
				
				if(rt != null) {
					rbtFpsLbl.setText(String.valueOf(rt.fps));
					rbtMovesLbl.setText(String.valueOf(Core.instance.getAICore().getMovesCount()));
				}
				
				if(ct != null) {
					packetReceivedLbl.setText(String.valueOf(ct.packetsReceived));
					packetSendLbl.setText(String.valueOf(ct.packetsSend));
					serverFpsLbl.setText(String.valueOf(ct.fps));
				}
				
				DecimalFormat df = new DecimalFormat("#.00");
				double freeRam = (double) r.freeMemory() / 1024 / 1024;
				double totalRam = (double) r.totalMemory() / 1024 / 1024;
				double maxRam = (double) r.maxMemory() / 1024 / 1024;
				double usedRam = totalRam - freeRam;
				ramLbl.setText(df.format(usedRam) + " Mb");
				ramFreeLbl.setText(df.format(freeRam) + " Mb");
				ramTotLbl.setText(df.format(totalRam) + " Mb");
				ramMaxLbl.setText(df.format(maxRam) + " Mb");
			}
		});
		t.setInitialDelay(0);
		t.setRepeats(true);
		t.start();

		// Add the control panel to the wrapper with the proper constraints
		GridBagConstraints cWrapper = new GridBagConstraints();
		cWrapper.fill = GridBagConstraints.HORIZONTAL;
		cWrapper.anchor = GridBagConstraints.PAGE_START;
		cWrapper.weightx = 1;
		cWrapper.weighty = 1;
		cWrapper.gridx = 0;
		cWrapper.gridy = 0;
		wrapper.add(sysPnl, cWrapper);
		cWrapper.gridy = 1;
		wrapper.add(rbtPnl, cWrapper);
		cWrapper.gridy = 2;
		wrapper.add(serverPnl, cWrapper);
		cWrapper.gridy = 3;
		wrapper.add(javaVmPnl, cWrapper);

		// Add the wrapper to the tab control and return the tab control
		pmTab.add("Preformance Monitor", wrapper);
		return pmTab;
	}
	
	/**
	 * Get the media display instance
	 * @return JPanel Media display instance
	 */
	public JPanel getRobotVisualizationViewer() {
		// Instantiate the media display panel
		RobotVisualizationViewer md = new RobotVisualizationViewer("Robot Visualization", r);
		
		// Set the minimum size of the panel
		md.setMinimumSize(new Dimension(240, 160));
		md.setPreferredSize(new Dimension(240, 160));
		
		// Return the panel
		return md;
	}
	
	/**
	 * Build the menu bar
	 */
	public void buildMenuBar() {
		final MainFrame self = this;
		
		// Get the menubar instance
		MenuBar bar = new MenuBar();
		
		Menu botMenu = new Menu("Bot");
		MenuItem newItem = new MenuItem("New");
		newItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Restart the server
				Core.instance.restart();
			}
		});
		botMenu.add(newItem);
		MenuItem loadItem = new MenuItem("Load");
		loadItem.setEnabled(false);
		loadItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//Core.instance.restart();
			}
		});
		botMenu.add(loadItem);
		MenuItem saveItem = new MenuItem("Save");
		saveItem.setEnabled(false);
		saveItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//Core.instance.restart();
			}
		});
		botMenu.add(saveItem);
		botMenu.addSeparator();
		MenuItem exitItem = new MenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(1);
			}
		});
		botMenu.add(exitItem);
		bar.add(botMenu);
		
		Menu helpMenu = new Menu("Help");
		MenuItem siteItem = new MenuItem("Visit Website");
		siteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				WebsiteUtils.openWebpage("http://timvisee.com/");
			}
		});
		helpMenu.add(siteItem);
		helpMenu.addSeparator();
		MenuItem aboutItem = new MenuItem("About");
		aboutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(self, SWAIbot.APP_NAME + " by Tim Visee - timvisee.com", "About", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		helpMenu.add(aboutItem);
		bar.add(helpMenu);
		
		// Update the menubar instance
		setMenuBar(bar);
	}
}
