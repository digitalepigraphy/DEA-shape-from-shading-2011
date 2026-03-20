package edu.ufl.digitalworlds.dea.gui;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

class ButtonBar extends JPanel
{
	JButton search_button;
	
	ButtonBar()
	{
		setLayout(new BorderLayout());
		EmptyBorder eb = new EmptyBorder(2,2,2,2);
        BevelBorder bb = new BevelBorder(BevelBorder.RAISED);
        setBorder(new CompoundBorder(eb,bb));
		
        JPanel p1=new JPanel(new GridBagLayout());
        
        //DEAapp.addToGridBag(p1, new JButton("Button1",DEAapp.search_icon), 0, 0, 1, 1, 1.0, 1.0);        
        //DEAapp.addToGridBag(p1, new JButton("Button2",DEAapp.search_icon), 1, 0, 1, 1, 1.0, 1.0);        
        //DEAapp.addToGridBag(p1, new JButton("Button3",DEAapp.search_icon), 2, 0, 1, 1, 1.0, 1.0);  
        
        add(p1,BorderLayout.CENTER);
	}
	
	
}