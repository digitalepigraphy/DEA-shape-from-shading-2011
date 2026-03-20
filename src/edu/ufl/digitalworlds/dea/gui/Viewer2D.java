package edu.ufl.digitalworlds.dea.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import edu.ufl.digitalworlds.dea.database.DatabaseImage;
import edu.ufl.digitalworlds.dea.media.Images;

public class Viewer2D extends JComponent implements MouseMotionListener, MouseListener
{
	public DatabaseImage im=null;
	int zoom;
	private int prevMouseX, prevMouseY;
	
	double transX;
	double transY;
	
	double scale;
	
	public Viewer2D()
	{
		reset();
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void reset()
	{
		zoom=0;
		scale=1;
		transX=0;
		transY=0;
	}
	
	public void setZoom(int zoom)
	{
		this.zoom=zoom;
		repaint();
	}
	
	public void setImage(DatabaseImage im)
	{
		this.im=im;
		reset();
		repaint();
	}
	
	public void paint(Graphics g_)
	{
		g_.setColor(Color.BLACK);
		g_.fillRect(0, 0, getWidth(), getHeight());
	
			Image img;
			if(im==null)
			{
				img=Images.noimage_icon.getImage();
			}
			else
			{
				if(im.Downloaded())img=im.img.getImage();
				else img=im.thumbnail.getImage();
			}
			
			int w=img.getWidth(null);
			int h=img.getHeight(null);
			float sc=1;
			if(w>h)
			{
				sc=(getWidth()*1f)/w;
			}
			else
			{
				sc=(getHeight()*1f)/h;
			}
			
			
			
			scale=sc+(getHeight()*zoom)/(h*25.0);
			
			
			g_.drawImage(img, (int)(transX*scale)+(getWidth()-(int)(w*scale))/2,(int)(transY*scale)+(getHeight()-(int)(h*scale))/2,(int)(w*scale),(int)(h*scale),this);
			
		
	}

	
	public void mouseClicked(MouseEvent arg0) {
		
	}

	public void mouseEntered(MouseEvent arg0) {
		
	}

	public void mouseExited(MouseEvent arg0) {
		
	}

	public void mousePressed(MouseEvent arg0) {	
		prevMouseX = arg0.getX();
	    prevMouseY = arg0.getY();
	}

	public void mouseReleased(MouseEvent arg0) {
		
	}

	public void mouseDragged(MouseEvent arg0) {
		int x=arg0.getX();
		int y=arg0.getY();
		
		transX+=(x-prevMouseX+0d)/(0d+scale);
		transY+=(y-prevMouseY+0d)/(0d+scale);
		
		prevMouseX = arg0.getX();
	    prevMouseY = arg0.getY();
	    repaint();
	}

	public void mouseMoved(MouseEvent arg0) {
		
	}
	
	public void savePNG(String filename)
	{
		
		if(im==null) return;
		
		
		File f=new File(filename);
		if(f.exists() && !DEAapp.showConfirmDialog("Overwrite existing file?", "The selected file already exists. Do you want to overwrite existing file?")) return;
			
		try {
			
			Image img;
			if(im.Downloaded())img=im.img.getImage();
			else img=im.thumbnail.getImage();
			ImageIO.write((RenderedImage) img, "png", f);
			
		} catch (IOException e) {
			e.printStackTrace();
			DEAapp.showErrorDialog("Error","The file could not be saved in this folder!");
		}
	}
}
