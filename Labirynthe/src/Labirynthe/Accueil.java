package Labirynthe;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.*; 

public class Accueil extends JLayeredPane{
	private JLabel logo;
	private JButton begin;
	private ImageIcon icon;
	public Accueil(String imgicon)
	{
		icon = create(imgicon);
		logo = new JLabel();
		logo.setIcon(icon);
		logo.setSize(400,400);
		begin = new JButton("BEGIN");
		begin.setFont(new Font(Font.SANS_SERIF,Font.BOLD,15));
		begin.setBounds(150,180,100,40);
		Border border = BorderFactory.createRaisedBevelBorder();
		begin.setBorder(border);
		begin.setBackground(new Color(94,127,24));
		begin.setForeground(Color.white);
		begin.setBorderPainted(true);
		begin.setFocusPainted(false);
		add(begin);
		add(logo);
		setSize(400,400);
	}
	private ImageIcon create(String path) {
		java.net.URL imgURL = getClass().getResource(path);
	    if (imgURL != null) {
	        return new ImageIcon(imgURL);
	    } else {
	        System.err.println("Couldn't find file: " + path);
	        return null;
	    }
	}
	public JLabel getLogo() {
		return logo;
	}
	public void setLogo(JLabel logo) {
		this.logo = logo;
	}
	public JButton getBegin() {
		return begin;
	}
	public void setBegin(JButton begin) {
		this.begin = begin;
	}
	
}
 