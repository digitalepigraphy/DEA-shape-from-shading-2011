package edu.ufl.digitalworlds.dea.database;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import edu.ufl.digitalworlds.dea.gui.DEAapp;
import edu.ufl.digitalworlds.dea.media.Images;

public class DatabaseImage implements ActionListener
{
	String id;

	public String getID(){return id;}
	public ImageIcon thumbnail=null;
	public ImageIcon img=null;
	
	public JButton thumb_button;
	public JButton thumb_button2;
	
	boolean image_downloaded=false;
	boolean image_started_downloading=false;
	String image_url="";
	
	DatabaseRecord father=null;
	
	public boolean Downloaded(){return image_downloaded;}
	private void setDownloaded(boolean image_downloaded){this.image_downloaded=image_downloaded;}
	public boolean startedDownloading(){return image_started_downloading;}
	private synchronized boolean startDownloading()
	{
		if(image_started_downloading==true) return false;
		else
		{
			image_started_downloading=true;
			return true;
		}
	}
	
	public void setImageURL(String image_url){this.image_url=image_url;}
	
	
	DatabaseImage(String id,DatabaseRecord father)
	{
		this.id=id;
		this.father=father;
		thumb_button=new JButton(Images.loading_icon);
		thumb_button.addActionListener(this);
		thumb_button.setEnabled(false);
		thumb_button2=new JButton(Images.loading_icon);
		thumb_button2.addActionListener(this);
		thumb_button2.setEnabled(false);
	}
	
	public void downloadThumbnailSerial()
	{
		try {
			URL url = new URL(Database.db_url+"thumbnails/"+id+".png");
			URLConnection conn =   url.openConnection();
			thumbnail=new ImageIcon(ImageIO.read(conn.getInputStream()));
			thumb_button.setIcon(thumbnail);
			thumb_button2.setIcon(thumbnail);
			/*if(DEAapp.data!=null && DEAapp.data.current_record!=null && DEAapp.data.current_record.equals(father))
			{
				DEAapp.data.getRecordPanel();
				DEAapp.data.getImagePanel();
			}*/
		} catch (Exception e) {System.out.println("Thumbnail downloading ERROR:"+e.toString());}
	}
	
	public void downloadThumbnailSilent()
	{
		class R implements Runnable
		{
			public void run() {
				downloadThumbnailSerial();
			}
		};
		new Thread(new R()).start();

	}
	
	public void downloadImageSerial()
	{
		if(startDownloading()==false)return;
		
		try {
			URL url =null;
			if(image_url.length()==0) url=new URL(Database.db_url+"images/"+id+".png");
			else url=new URL(image_url);
			
			URLConnection conn =   url.openConnection();
			img=new ImageIcon(ImageIO.read(conn.getInputStream()));
			thumb_button.setEnabled(true);
			thumb_button2.setEnabled(true);
			setDownloaded(true);
		} catch (Exception e) {setDownloaded(false);image_started_downloading=false;System.out.println("Image downloading ERROR"+e.toString());}
	}
	
	public void downloadImageSilent()
	{
		class R implements Runnable
		{
			public void run() {
				downloadImageSerial();
			}
		};
		Thread t=new Thread(new R());
		t.setPriority(Thread.MAX_PRIORITY);
		t.start();
	}

	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource()==thumb_button)
		{
			father.zoom_slider.setValue(0);
			father.viewer2d.setImage(this);
			DEAapp.viewer_panel.tabbedPane.setSelectedIndex(1);
			
			//DEAapp.viewer_panel.repaint();
		}
		else if(e.getSource()==thumb_button2)
		{
			father.zoom_slider.setValue(0);
			father.viewer2d.setImage(this);
			DEAapp.viewer_panel.tabbedPane.setSelectedIndex(1);
			//DEAapp.viewer_panel.repaint();
		}
	}
}