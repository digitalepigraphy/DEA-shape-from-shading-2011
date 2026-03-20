package edu.ufl.digitalworlds.dea.gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JPanel;



import edu.ufl.digitalworlds.*;
import edu.ufl.digitalworlds.dea.database.*;
import edu.ufl.digitalworlds.dea.media.Images;
import edu.ufl.digitalworlds.dea.media.ResourceRetriever;
import edu.ufl.digitalworlds.dea.utils.javaFXcheck;
import edu.ufl.digitalworlds.gui.DWApp;




public class DEAapp extends DWApp
{

	public static String most_recent_path="";
	public static String default_writing_path="";
	
	public static Database data=null;
	public static TabPanel tab_panel;
	public static StatusBar status_bar=null;
	ButtonBar button_bar;
	public static ViewerPanel viewer_panel=null;
	
	
	public void GUIsetup(JPanel p_root) {
		
			
		
		//openURL("http://epigraphy.packhum.org/inscriptions/oi?ikey=28560");
		
		setLoadingProgress("Loading Resources ...",20);
		
		Images.loadAll();
		
		javaFXcheck.check();
		
		setLoadingProgress("Connecting to the database (please wait)...",40);
		
		data=new Database();
		
		setLoadingProgress("Connected. Retrieving field groups...",60);
		
		data.getFieldGroups();
		
		setLoadingProgress("Connected. Retrieving fields...",65);
		
		data.getFields();
		
		setLoadingProgress("Initializing ...",80);
		
		tab_panel=new TabPanel();
		p_root.add(new TabPanel(), BorderLayout.EAST);
		
		
		JPanel main_panel=new JPanel(new BorderLayout());
		viewer_panel=new ViewerPanel();
		viewer_panel.viewer3d.set3DObject(null);
		main_panel.add(viewer_panel,BorderLayout.CENTER);
		button_bar=new ButtonBar();
		main_panel.add(button_bar,BorderLayout.NORTH);
		status_bar=new StatusBar();
		main_panel.add(status_bar,BorderLayout.SOUTH);
		
		if(data.isSessionConnected()) status_bar.setStatus("Connected to the database. (ID="+data.getSessionID()+", IP="+data.getSessionIP()+")");
		else status_bar.setStatus("ERROR: Connection to the database failed.");
		
		p_root.add(main_panel, BorderLayout.CENTER);
	}
	
	
	public void MenuGUIsetup(JMenuBar menuBar)
	{
		
	}

	public static boolean setWritingPath()
	{
		if(default_writing_path.length()==0)
		{
			File f=new File("DEAtemp.png");
			boolean r=false;
			try {
				Image i=Images.DEAicon.getImage();
				BufferedImage bi=new BufferedImage(Images.DEAicon.getIconWidth(),Images.DEAicon.getIconHeight(),BufferedImage.TYPE_INT_RGB);
				Graphics g = bi.createGraphics();
				g.drawImage(i, 0, 0, null);
			    g.dispose();
				
				ImageIO.write(bi, "png", f);
				default_writing_path=(new File(f.getAbsolutePath())).getParent()+System.getProperty("file.separator");
				r=true;
			} catch (Exception e) 
			{
				r=false;
			}
			
			if(r==false)
			{
				JFileChooser chooser = new JFileChooser();
	            chooser.setFileHidingEnabled(false);
	            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	            chooser.setMultiSelectionEnabled(false);
	            chooser.setDialogType(JFileChooser.OPEN_DIALOG);
	            chooser.setDialogTitle("Choose a directory to store temporary files...");
	            chooser.setApproveButtonText("Ok");
	            if (chooser.showOpenDialog(DEAapp.app)== JFileChooser.APPROVE_OPTION) 
	            {  
	            	
					try {
						f=new File(chooser.getSelectedFile().getAbsolutePath()+System.getProperty("file.separator")+"DEAtemp.png");
		            	Image i=Images.DEAicon.getImage();
						BufferedImage bi=new BufferedImage(Images.DEAicon.getIconWidth(),Images.DEAicon.getIconHeight(),BufferedImage.TYPE_INT_RGB);
						Graphics g = bi.createGraphics();
						g.drawImage(i, 0, 0, null);
					    g.dispose();	
						ImageIO.write(bi, "png", f);
						default_writing_path=(new File(f.getAbsolutePath())).getParent()+System.getProperty("file.separator");
						return true;
					} catch (Exception e) {
						return false;
					}
	            }
	            else return false;
		        
			}
			
			return r;
		}
		else return true;
	}
	
//	public void GUIitemStateChanged(ItemEvent e){}
//	public void GUIactionPerformed(ActionEvent e){}
//	public void updateLookAndFeel(){SwingUtilities.updateComponentTreeUI(...);}
//  public void GUIclosing(){}

		
	
	public static void main(String args[]) {
    	createMainFrame("Digital Epigraphy and Archaeology Project");
    	app=new DEAapp();
    	setFrameSize(930,610,ResourceRetriever.fromFile("data/DEAicon_32.png"));
    }

}