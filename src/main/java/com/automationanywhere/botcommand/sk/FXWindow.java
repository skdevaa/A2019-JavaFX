package com.automationanywhere.botcommand.sk;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.border.Border;

import javafx.embed.swing.JFXPanel;

public class FXWindow {

	
	private JFrame frame;
	private JFXPanel fxpanel;
	private int width;
	private int height;
	private String title;
	
	public FXWindow(String title,int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		
		this.frame = new JFrame(title);  
		
		frame.setSize(this.width, this.height);
		frame.setLocation(400, 500);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
		ImageIcon icon = new ImageIcon(this.getClass().getResource("/icons/logo.png"));
		frame.setIconImage(icon.getImage());
        fxpanel = new JFXPanel();
        fxpanel.setLayout(new BorderLayout());
        fxpanel.setOpaque(false);
        Border border = BorderFactory.createMatteBorder(2,14,2,2,new java.awt.Color(19,58,101));
        fxpanel.setBorder(border);
        fxpanel.setPreferredSize(new Dimension(this.width*10, this.height*10));
        frame.add(fxpanel);
    
	}
	
	public JFXPanel getPanel() {
		return this.fxpanel;
	}
	
	public JFrame getFrame() {
		return this.frame;
	}
}
