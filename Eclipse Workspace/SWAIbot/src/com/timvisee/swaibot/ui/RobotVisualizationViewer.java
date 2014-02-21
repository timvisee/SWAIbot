package com.timvisee.swaibot.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.timvisee.swaibot.MainFrame;
import com.timvisee.swaibot.robot.Robot;

public class RobotVisualizationViewer extends JPanel {

	private static final long serialVersionUID = -608446646081986615L;
	
	/** @var t Media display panel title */
	private String t;
	private Robot r;
	
	/**
	 * Constructor
	 * @param t Media display title
	 */
	public RobotVisualizationViewer(String t, Robot r) {
		// Construct the parent class
		super();
		
		// Store the title
		this.t = t;
		this.r = r;
		
		// TODO: Initialise the component
		
		setPreferredSize(new Dimension(240, 160));
		
		// Set the background color of the media display panel
		setBackground(new Color(255, 255, 255));
		
		// Create a border
		setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
		
		Timer ti = new Timer(250, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		ti.setRepeats(true);
		ti.start();
	}
	
	/**
	 * Get the title of the media display panel
	 * @return
	 */
	public String getTitle() {
		return this.t;
	}
	
	/**
	 * Set the title of the media display component
	 * @param t Title
	 */
	public void setTitle(String t) {
		this.t = t;
	}
	
	/**
	 * Called on paint event
	 */
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;

        if(MainFrame.instance.ct == null) {
    		// Draw the background
    		Area area = new Area(new Rectangle2D.Float(0, 0, 1000, 1000));
    		g2.setColor(new Color(0, 0, 0, 180));
    		g2.fill(area);
            drawTitle(g);
        	return;
        }
        if(!MainFrame.instance.ct.connected) {
        	// Draw the background
    		Area area = new Area(new Rectangle2D.Float(0, 0, 1000, 1000));
    		g2.setColor(new Color(0, 0, 0, 180));
    		g2.fill(area);
            drawTitle(g);
        	return;
        }
        

        double legLength = 35;
        int rootx = 120;
        int rooty = 70;
        
        // Draw the limbs
        drawLimb(g2, rootx, rooty - 50, rootx, rooty, true);

        double anglert = (double) (r.getTopRightLegAngle() - 90)/180.0*Math.PI;
        int xrt = (int) ((legLength)*Math.cos(-anglert)) * -1;
        int yrt = (int) ((legLength)*Math.sin(-anglert));
        drawLimb(g2, rootx, rooty, rootx + xrt, rooty + yrt, false);
        
        double anglelt = (double) (r.getTopLeftLegAngle() - 90)/180.0*Math.PI;
        int xlt = (int) ((legLength)*Math.cos(-anglelt)) * -1;
        int ylt = (int) ((legLength)*Math.sin(-anglelt));
        drawLimb(g2, rootx, rooty, rootx + xlt, rooty + ylt, true);

        double anglerb = (double) (r.getBottomRightLegAngle() + r.getTopRightLegAngle() - 90)/180.0*Math.PI;
        int xrb = (int) ((legLength)*Math.cos(-anglerb)) * -1;
        int yrb = (int) ((legLength)*Math.sin(-anglerb));
        drawLimb(g2, rootx + xrt, rooty + yrt, rootx + xrt + xrb, rooty + yrt + yrb, false);

        double anglelb = (double) (r.getBottomLeftLegAngle() + r.getTopLeftLegAngle() - 90)/180.0*Math.PI;
        int xlb = (int) ((legLength)*Math.cos(-anglelb)) * -1;
        int ylb = (int) ((legLength)*Math.sin(-anglelb));
        drawLimb(g2, rootx + xlt, rooty + ylt, rootx + xlt + xlb, rooty + ylt + ylb, true);
        
        // Draw the title into the component
        drawTitle(g);
    }
	
	/**
	 * Draw the title in the panel
	 * @param g Graphics instance
	 */
	public void drawTitle(Graphics g) {
	    Graphics2D g2d = (Graphics2D) g.create();
	    
	    // Set the default margin
		final int txtVertMargin = 4;
		final int txtHorMargin = 5;
		
		// Get the font width and height
		FontMetrics fm = g2d.getFontMetrics();
		int txtHeight = fm.getFont().getSize() - 2;
		int txtWidth = fm.stringWidth(this.t);
		
		// Draw the background
		Area area = new Area(new RoundRectangle2D.Float(-5, -5, txtWidth + txtHorMargin * 2 + 5, txtHeight + txtVertMargin * 2 + 5, 10, 10));
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2d.setColor(new Color(0, 0, 0, 180));
	    g2d.fill(area);
		
	    // Draw the string
	    g2d.setColor(Color.WHITE);
		g2d.drawString(this.t, txtHorMargin, txtHeight + txtVertMargin);
		
		// Dispose the graphics
	    g2d.dispose();
	}
	
	/**
	 * Draw a dot
	 * @param g Graphics component to draw the dot on
	 * @param x X coord
	 * @param y Y coord
	 */
	public void drawDot(Graphics g, int x, int y) {
		g.drawLine(x, y, x, y);
	}
	
	/**
	 * Draw a limb
	 * @param g Graphics component to draw the limb on
	 * @param x X coord
	 * @param y Y coord
	 * @param x2 Second x coord
	 * @param y2 Second y coord
	 */
	public void drawLimb(Graphics2D g, int x, int y, int x2, int y2, boolean inFront) {
        // Set the thickness and the color of the limb
		g.setStroke(new BasicStroke(3));
		g.setColor(new Color(0, 0, 0));
		
		// Draw the line of the limb itself
		g.drawLine(x, y, x2, y2);
		
		// Set the thickness and the color of the limb joints
        g.setStroke(new BasicStroke(5));
        if(inFront)
        	g.setColor(new Color(255, 0, 0));
        else
        	g.setColor(new Color(255 / 2, 0, 0));
        
        // Draw the limb joints
		drawDot(g, x, y);
		drawDot(g, x2, y2);
		
		// Reset the line thickness and color
        g.setStroke(new BasicStroke(1));
        g.setColor(new Color(0, 0, 0));
	}
}
