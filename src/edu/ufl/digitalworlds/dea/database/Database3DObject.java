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

public class Database3DObject
{
	String id;

	public String getID(){return id;}
	public ImageIcon heightmap=null;
	
	
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
	
	
	Database3DObject(String id,DatabaseRecord father)
	{
		this.id=id;
		this.father=father;
	}
	
	public void downloadThumbnailSerial()
	{
		try {
			URL url = new URL(Database.db_url+"heightmaps/"+id+"_thmb.png");
			URLConnection conn =   url.openConnection();
			heightmap=new ImageIcon(ImageIO.read(conn.getInputStream()));
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
			//id="dok";
			id="cuuhd72835gsf6b2";
			id="out_gauss5_sm_imprint";
			id="dok3";
			if(image_url.length()==0) url=new URL(Database.db_url+"heightmaps/"+id+".png");
			else url=new URL(image_url);
			
			URLConnection conn =   url.openConnection();
			ImageIcon heightmap_=new ImageIcon(ImageIO.read(conn.getInputStream()));
			heightmap=heightmap_;
			if(DEAapp.viewer_panel!=null && DEAapp.viewer_panel.viewer3d!=null) DEAapp.viewer_panel.viewer3d.redraw();
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

	
}