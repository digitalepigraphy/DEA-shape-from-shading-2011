package edu.ufl.digitalworlds.dea.database;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.ufl.digitalworlds.net.HTTPXMLComEvent;
import edu.ufl.digitalworlds.net.HTTPXMLCommunication;
import edu.ufl.digitalworlds.dea.gui.DEAapp;
import edu.ufl.digitalworlds.dea.utils.FileImageFilter;
import edu.ufl.digitalworlds.dea.utils.FileInfoFilter;
import edu.ufl.digitalworlds.dea.gui.JBrowserPanel;
import edu.ufl.digitalworlds.dea.gui.Viewer2D;
import edu.ufl.digitalworlds.dea.gui.ResultIcon;
import edu.ufl.digitalworlds.dea.gui.ImagePreviewer;
import edu.ufl.digitalworlds.dea.utils.javaFXcheck;
import edu.ufl.digitalworlds.dea.media.Images;


public class DatabaseRecord implements ActionListener, ChangeListener
{
	String id;
	public List<DatabaseString> text_values;
	public List<DatabaseImage> images;
	public Database3DObject obj3D=null;
	
	JPanel thumb_panel;
	JPanel thumb_panel2;
	JPanel main_panel;
	JPanel image_panel;
	
	JComboBox history_box;
	
	JButton save_changes_button;
	
	JButton edit_button;
	JButton add_photo_button;
	JButton download_photo_button;
	String most_recent_downloading_photo_path="";
	
	JButton download_info_button;
	String most_recent_downloading_info_path="";
	
	
	ResultIcon result_icon;
	
	JSlider zoom_slider;
	JButton previous_image;
	JButton next_image;
	Viewer2D viewer2d;
	
	boolean strings_downloaded=false;
	boolean strings_started_downloading=false;

	
	public boolean stringsDownloaded(){return strings_downloaded;}
	private void setStringsDownloaded(boolean strings_downloaded){this.strings_downloaded=strings_downloaded;}
	public boolean stringsStartedDownloading(){return strings_started_downloading;}
	
	private synchronized boolean startDownloading()
	{
		if(strings_started_downloading==true) return false;
		else
		{
			strings_started_downloading=true;
			return true;
		}
	}
	
	public ResultIcon getResultIcon(){return result_icon;}
	
	public DatabaseRecord(String id)
	{
		this.id=id;
		result_icon=new ResultIcon(this,80,100);
		thumb_panel=new JPanel(new BorderLayout());
		thumb_panel2=new JPanel(new BorderLayout());
		main_panel=new JPanel(new BorderLayout());
		image_panel=new JPanel(new BorderLayout());
		
		save_changes_button=new JButton("Save changes", Images.update_icon);
		save_changes_button.addActionListener(this);
		edit_button=new JButton("Edit epigraphical information",Images.edit_icon);
        edit_button.addActionListener(this);
        add_photo_button=new JButton("Add photographs",Images.photograph_icon);
        add_photo_button.addActionListener(this);
        download_photo_button=new JButton("Download", Images.download_icon);
		download_photo_button.addActionListener(this);
		download_info_button=new JButton("Download", Images.download_icon);
		download_info_button.addActionListener(this);
		
		text_values=new ArrayList<DatabaseString>();
		images=new ArrayList<DatabaseImage>();
		viewer2d=new Viewer2D();
		history_box=new JComboBox();
		
	}
	
	public String getID(){return id;}
	
	public int getValueIndex(int field_id)
	{
		int ret=-1;
		for(int i=0;i<text_values.size() && ret==-1;i++)
		{
			if(text_values.get(i).getFieldID()==field_id)ret=i;
		}
		return ret;
	}
	
	public int getImageIndex(String image_id)
	{
		int ret=-1;
		for(int i=0;i<images.size() && ret==-1;i++)
		{
			if(images.get(i).getID().compareTo(image_id)==0)ret=i;
		}
		return ret;
	}

	public void downloadImages()
	{
		class R implements Runnable
		{
			public void run() {
				for(int i=0;i<images.size();i++)
				{
					if(images.get(i).startedDownloading()==false)
					{
						images.get(i).downloadImageSerial();
						viewer2d.repaint();
					}
				}
			}
		};
		new Thread(new R()).start();
	}
	
	public void downloadHistoryFileSerial()
	{
		HTTPXMLComEvent e=HTTPXMLCommunication.sendRequest(Database.db_url+"history/"+getID()+".xml");
		if(e.wasSuccessful())
		{
			history_box.removeAllItems();
			history_box.addItem("Record history");
			NodeList nList = e.getDocument().getElementsByTagName("event");
			//System.out.println("-----------------------");
			
			int i=0;
			for (int temp = 0; temp < nList.getLength(); temp++) {
	 
			   Node nNode = nList.item(temp);
			   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
			      Element eElement = (Element) nNode;
			      String s="";
			      s=s+Database.getTagValue("created", eElement)+" ";
			      s=s+Database.getTagValue("session_id", eElement)+" ";
			      s=s+Database.getTagValue("field_value", eElement)+" ";
			      s=s+"("+Database.getTagValue("field_id", eElement)+")";
			      
			      
			      history_box.addItem(s);
			   }
			   i+=1;
			}
		}
			
	
	}

	public void downloadHistoryFile()
	{
		class R implements Runnable
		{
			public void run() {
				downloadHistoryFileSerial();
			}
		};
		new Thread(new R()).start();
	}
	
	public void downloadRecordFile()
	{
		if(startDownloading()==false) return;
		
		HTTPXMLComEvent e=HTTPXMLCommunication.sendRequest(Database.db_url+"records/"+getID()+".xml");
		if(e.wasSuccessful())
		{
			NodeList nList = e.getDocument().getElementsByTagName("field");
			//System.out.println("-----------------------");
			
			int i=0;
			for (int temp = 0; temp < nList.getLength(); temp++) {
	 
			   Node nNode = nList.item(temp);
			   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
			      Element eElement = (Element) nNode;
			      int field_id=Integer.parseInt(Database.getTagValue("field_id", eElement));
			      String field_value=Database.getTagValue("field_value", eElement);
			      
			      
			      if(getValueIndex(field_id)==-1 && field_id!=1000 && field_id!=2000) //store the most recent update of this field
			      {
			    	  //System.out.println(field_id+": "+field_value);
			    		  DatabaseString ds=new DatabaseString(this);
			    		  ds.setFieldID(field_id);
			    		  ds.setOriginalValue(field_value);
			    		  if(field_value.length()==0) ds.setDeletedByUser(true);
			    		  text_values.add(ds);
			      }
			      else if(field_id==1000 && getImageIndex(field_value)==-1) //store all images
			      {
			    	  DatabaseImage di=new DatabaseImage(field_value,this);
			    	  if(images.size()==0)
			    	  {
			    		  di.downloadThumbnailSerial();
			    		  viewer2d.setImage(di);
			    	  }
			    	  else
			    	  {
			    		  di.downloadThumbnailSilent();
			    	  }
			    	  images.add(di);
			      }
			      else if(field_id==2000 && obj3D==null) //store all images
			      {
			    	  obj3D=new Database3DObject(field_value,this);
			    	  
			    	  obj3D.downloadThumbnailSerial();

			    	  obj3D.downloadImageSilent();
			      }
			      i+=1;
			     }
			}
			
			if(getValueIndex(25)==-1)//text_url
			{
				DatabaseString ds=new DatabaseString(this);
	    		  ds.setFieldID(25);
	    		  ds.setOriginalValue("");
	    		  text_values.add(ds);
			}
			
			setStringsDownloaded(true);
			downloadHistoryFile();
		}else downloadRecord_();
	}
		
	private void downloadRecord_()
	{
		if(DEAapp.data==null) return;
		
		String keys[]=new String[2];
		String values[]=new String[2];
		keys[0]="session_id";values[0]=DEAapp.data.getSessionID();
		keys[1]="record_id";values[1]=getID();
		HTTPXMLComEvent e=HTTPXMLCommunication.sendRequest(Database.db_url+"get_record.php",keys,values);
		if(e.wasSuccessful())
		{
			NodeList nList = e.getDocument().getElementsByTagName("field");
			//System.out.println("-----------------------");
			
			
			int i=0;
			for (int temp = 0; temp < nList.getLength(); temp++) {
	 
			   Node nNode = nList.item(temp);
			   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
			      Element eElement = (Element) nNode;
			      int field_id=Integer.parseInt(Database.getTagValue("field_id", eElement));
			      String field_value=Database.getTagValue("field_value", eElement);
			
			      if(getValueIndex(field_id)==-1  && field_id!=1000  && field_id!=2000) //store the most recent update of this field
			      {
			    	  //System.out.println(field_id+": "+field_value);
			    		  DatabaseString ds=new DatabaseString(this);
			    		  ds.setFieldID(field_id);
			    		  ds.setOriginalValue(field_value);
			    		  if(field_value.length()==0) ds.setDeletedByUser(true);
			    		  text_values.add(ds);
			      }
			      else if(field_id==1000 && getImageIndex(field_value)==-1) //store all images
			      {
			    	  DatabaseImage di=new DatabaseImage(field_value,this);
			    	  if(images.size()==0)
			    	  {
			    		  di.downloadThumbnailSerial();
			    		  viewer2d.setImage(di);
			    	  }
			    	  else
			    	  {
			    		  di.downloadThumbnailSilent();
			    	  }
			    	  images.add(di);
			      }
			      else if(field_id==2000 && obj3D==null) //store all images
			      {
			    	  obj3D=new Database3DObject(field_value,this);
			    	  
			    	  obj3D.downloadThumbnailSerial();
			    	  DEAapp.viewer_panel.viewer3d.set3DObject(obj3D);
			    	  obj3D.downloadImageSilent();
			      }
			      i+=1;
			     }
			}
			
			if(getValueIndex(25)==-1)//text_url
			{
				DatabaseString ds=new DatabaseString(this);
	    		  ds.setFieldID(25);
	    		  ds.setOriginalValue("");
	    		  text_values.add(ds);
			}
			
			setStringsDownloaded(true);
			downloadHistoryFile();
		}
		
	}

	
	public JPanel getThumbPanel()
	{
		thumb_panel.removeAll();
		
		JPanel p=new JPanel(new GridBagLayout());
		
		for(int i=0;i<images.size();i++)
		{
			DEAapp.addToGridBag(p,images.get(i).thumb_button, 0, i, 1, 1, 1.0, 1.0);
		}
		thumb_panel.add(p,BorderLayout.NORTH);
		
		thumb_panel.repaint();
		return thumb_panel;
	}
	
	public JPanel getThumbPanel2()
	{
		thumb_panel2.removeAll();
		
		JPanel p=new JPanel(new GridBagLayout());
		
		for(int i=0;i<images.size();i++)
		{
			DEAapp.addToGridBag(p,images.get(i).thumb_button2, 0, i, 1, 1, 1.0, 1.0);
		}
		thumb_panel2.add(p,BorderLayout.NORTH);
		
		thumb_panel2.repaint();
		return thumb_panel2;
	}
	
	public JPanel getImagePanel()
	{
		image_panel.removeAll();
		JPanel top_panel=new JPanel(new BorderLayout());
		JPanel top_button_panel=new JPanel(new GridBagLayout());

		DEAapp.addToGridBag(top_button_panel,new JButton("Analysis", Images.analysis_icon), 0, 0, 1, 1, 1.0, 1.0);
		DEAapp.addToGridBag(top_button_panel,download_photo_button, 1, 0, 1, 1, 1.0, 1.0);
		DEAapp.addToGridBag(top_button_panel,add_photo_button, 2, 0, 1, 1, 1.0, 1.0);
		
		top_panel.add(top_button_panel,BorderLayout.EAST);
		image_panel.add(top_panel,BorderLayout.NORTH);
		
		JPanel central_panel=new JPanel(new BorderLayout());
		
		JPanel main_image_panel=new JPanel(new BorderLayout());
		
		main_image_panel.add(viewer2d,BorderLayout.CENTER);
		
		JPanel low_panel=new JPanel(new GridBagLayout());
		
		JPanel sld_panel=new JPanel(new BorderLayout());
		zoom_slider=new JSlider(JSlider.HORIZONTAL,0,500,0);
		zoom_slider.addChangeListener(this);
		sld_panel.add(zoom_slider,BorderLayout.CENTER);
		sld_panel.add(new JLabel(Images.zoom_icon),BorderLayout.WEST);
		
		DEAapp.addToGridBag(low_panel,sld_panel, 0, 0, 1, 1, 1.0, 1.0);
		
		previous_image=new JButton("Previous");
		previous_image.addActionListener(this);
		DEAapp.addToGridBag(low_panel,previous_image, 1, 0, 1, 1, 1.0, 1.0);
		next_image=new JButton("Next");
		next_image.addActionListener(this);
		DEAapp.addToGridBag(low_panel,next_image, 2, 0, 1, 1, 1.0, 1.0);
		
		
		main_image_panel.add(low_panel,BorderLayout.SOUTH);
		
		central_panel.add(main_image_panel,BorderLayout.CENTER);
		
		JScrollPane scrollpane = new JScrollPane(getThumbPanel2());
		central_panel.add(scrollpane,BorderLayout.WEST);
		
		image_panel.add(central_panel,BorderLayout.CENTER);
		
		return image_panel;
	}
	
	public JPanel getMainPanel()
	{
		main_panel.removeAll();
		
		JPanel top_panel=new JPanel(new BorderLayout());
		JPanel top_button_panel=new JPanel(new GridBagLayout());
		
		if(DEAapp.data.isNowEditing()==this)
		{
		JPanel p1_=new JPanel(new BorderLayout());
        //p1_.setBorder(new TitledBorder(new EtchedBorder(), "Add fields:"));
        
		if(DEAapp.data.record_combo_box==null)
		{
       	DEAapp.data.record_combo_box=new JComboBox();
       	

       	class MyCellRenderer extends JLabel implements ListCellRenderer {
            public MyCellRenderer() {
                setOpaque(true);
            }
            public Component getListCellRendererComponent(
                JList list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus)
            {
                setText(value.toString());
                
                setBackground(Color.white);
                
                if(((DatabaseField)value).isGroup()) {setFont(getFont().deriveFont(Font.BOLD));}
                else setFont(getFont().deriveFont(Font.PLAIN));
                
                return this;
            }
        }
       	
       	DEAapp.data.record_combo_box.setRenderer(new MyCellRenderer());
		}
		else
		{
			DEAapp.data.record_combo_box.removeAllItems();
		}
		
		DatabaseField f=new DatabaseField();
       	f.setName("Select a field...");
       	f.setGroupID(100);
       	DEAapp.data.record_combo_box.addItem(f);
       	
       	
       	for(int j=0;j<DEAapp.data.field_groups.length;j++)
		{
       		DEAapp.data.record_combo_box.addItem(DEAapp.data.field_groups[j]);
       		
       		for(int i=0;i<DEAapp.data.fields.length;i++)
       		{
       			
				
       			if(DEAapp.data.field_groups[j].getID()==DEAapp.data.fields[i].getGroupID())
       			{
       				int vid=getValueIndex(DEAapp.data.fields[i].getID());
       				if(vid==-1 || text_values.get(vid).getDeletedByUser())
       					DEAapp.data.record_combo_box.addItem(DEAapp.data.fields[i]);
       			}
       		}
		}
       	
       	p1_.add(DEAapp.data.record_combo_box,BorderLayout.CENTER);
       	p1_.add(DEAapp.data.record_add_this_field_button,BorderLayout.EAST);
       	
       	
       	top_panel.add(p1_,BorderLayout.WEST);
		}//if(isNowEditing())
	
		DEAapp.addToGridBag(top_button_panel,download_info_button, 1, 0, 1, 1, 1.0, 1.0);
		if(DEAapp.data.isNowEditing()==this)
			DEAapp.addToGridBag(top_button_panel,save_changes_button, 2, 0, 1, 1, 1.0, 1.0);
		else DEAapp.addToGridBag(top_button_panel,edit_button, 2, 0, 1, 1, 1.0, 1.0);
		
		top_panel.add(top_button_panel,BorderLayout.EAST);
		main_panel.add(top_panel,BorderLayout.NORTH);
		
		JPanel central_panel=new JPanel(new BorderLayout());
		JPanel p=new JPanel(new GridBagLayout());
		JPanel two_column_panel=new JPanel(new GridBagLayout());
		JPanel left_column_=new JPanel(new BorderLayout());
		JPanel left_column=new JPanel(new GridBagLayout());
		
		left_column_.add(left_column,BorderLayout.NORTH);
		JPanel right_column_=new JPanel(new BorderLayout());
		JPanel right_column=new JPanel(new GridBagLayout());
		right_column_.add(right_column,BorderLayout.NORTH);
		
		DEAapp.addToGridBag(two_column_panel,left_column, 0, 0, 1, 1, 1.0, 1.0);
		DEAapp.addToGridBag(two_column_panel,right_column, 1, 0, 1, 1, 1.0, 1.0);
		
		int left_line_counter=0;
		int right_line_counter=0;
		int left_counter=0;
		int right_counter=0;
		
		for(int j=0;j<DEAapp.data.field_groups.length;j++)
		{
			JPanel p_in=new JPanel(new GridBagLayout());
			p_in.setBorder(new TitledBorder(new EtchedBorder(), DEAapp.data.field_groups[j].getName()));
			int c_in=0;
			
			if(j==0)
			{
				JPanel p__=new JPanel(new BorderLayout());
				p__.add(new JLabel("DEA ID:"),BorderLayout.WEST);
		       	JTextField tf=new JTextField(id);
		       	tf.setEditable(false);
				p__.add(tf,BorderLayout.CENTER);
		    	
		    	DEAapp.addToGridBag(p_in,p__, 0, c_in, 1, 1, 1.0, 1.0);
				c_in+=1;
			}
			
			for(int i=0;i<DEAapp.data.fields.length;i++)
			{
				
				if(DEAapp.data.field_groups[j].getID()==DEAapp.data.fields[i].getGroupID())
				{
					int vid=getValueIndex(DEAapp.data.fields[i].getID());
					if(vid!=-1 && text_values.get(vid).getDeletedByUser()==false)
					{
						JPanel p__=new JPanel(new BorderLayout());
						p__.add(new JLabel(DEAapp.data.fields[i].getName()+":"),BorderLayout.WEST);
						
						//if(isNowEditing()) text_values.get(vid).getTextField().setEditable(true);
						//else text_values.get(vid).getTextField().setEditable(false);
							
						if(DEAapp.data.isNowEditing()==this) p__.add(text_values.get(vid).getComboBox(),BorderLayout.CENTER);
						else p__.add(text_values.get(vid).getTextBox(),BorderLayout.CENTER);
			    	
						if(DEAapp.data.isNowEditing()==this)
							p__.add(text_values.get(vid).getRemoveButton(),BorderLayout.EAST);
			    	
						DEAapp.addToGridBag(p_in,p__, 0, c_in, 1, 1, 1.0, 1.0);
						c_in+=1;
					}
				}
			}
			if(c_in>0)
			{
				if(left_line_counter<=right_line_counter)
				{
					DEAapp.addToGridBag(left_column,p_in, 0, left_counter, 1, 1, 1.0, 1.0);
					left_line_counter+=c_in;
					left_counter+=1;
				}
				else
				{
					DEAapp.addToGridBag(right_column,p_in, 0, right_counter, 1, 1, 1.0, 1.0);
					right_line_counter+=c_in;
					right_counter+=1;
				}
				    	
			}
		}
		
		
		DEAapp.addToGridBag(p,two_column_panel, 0, 0, 1, 1, 1.0, 1.0);
		
		JPanel text_panel=new JPanel(new BorderLayout());
		text_panel.setBorder(new TitledBorder(new EtchedBorder(),"Text"));
		
		int vid=getValueIndex(25);
		if(vid!=-1)
		{
			//JTextArea text_area=new JTextArea();
			//text_area.setRows(10);
			
			JPanel p_t=new JPanel(new BorderLayout());
			
			if(DEAapp.data.isNowEditing()==this) p_t.add(text_values.get(vid).getComboBox(),BorderLayout.CENTER);
			else p_t.add(text_values.get(vid).getTextBox(),BorderLayout.CENTER);
			
			p_t.add(new JLabel("URL:",Images.url_icon,JLabel.LEFT),BorderLayout.WEST);
			text_panel.add(p_t, BorderLayout.NORTH);
			
			if(text_values.get(vid).getOriginalValue().length()>0)
			{
				/*DEAHtmlRendererContext rcontext;
				HtmlPanel text_area= new HtmlPanel();
				UserAgentContext ucontext = new SimpleUserAgentContext();
				rcontext = new DEAHtmlRendererContext(text_area, ucontext);
			    try {
					rcontext.navigate(new URL(text_values.get(vid).getOriginalValue()),null);//"http://epigraphy.packhum.org/inscriptions/oi?ikey=28560"), null);
				} catch (MalformedURLException e) {}*/
				
				if(javaFXcheck.isAvailable())
				{
					JBrowserPanel text_area=new JBrowserPanel(text_values.get(vid).getOriginalValue());
					text_panel.add(text_area, BorderLayout.CENTER);
				}
			}
			else
			{
				JTextArea text_area=new JTextArea();
				text_area.setEditable(false);
				text_area.setRows(10);
				text_panel.add(text_area, BorderLayout.CENTER);
			}
		}
		
		DEAapp.addToGridBag(p,text_panel, 0, 1, 2, 1, 1.0, 1.0);
		
		DEAapp.addToGridBag(p,new JLabel(" "), 0, 2, 2, 1, 1.0, 1.0);
		DEAapp.addToGridBag(p,history_box, 0, 3, 2, 1, 1.0, 1.0);
		
		central_panel.add(p,BorderLayout.NORTH);
	
		JPanel pp=new JPanel(new BorderLayout());
		pp.add(central_panel, BorderLayout.CENTER);
		pp.add(getThumbPanel(), BorderLayout.WEST);
		
		JScrollPane scrollpane = new JScrollPane(pp);
		
		main_panel.add(scrollpane,BorderLayout.CENTER);
		
		main_panel.repaint();
		return main_panel;
	}

	public boolean uploadRecordSerial()
	{
		String keys[]=new String[4];
		String values[]=new String[4];
		keys[0]="session_id";values[0]=DEAapp.data.getSessionID();
		keys[1]="record_id";values[1]=id;
		
		save_changes_button.setEnabled(false);
	    DEAapp.status_bar.setStatus("Updating record...",1);
		new Thread(DEAapp.status_bar).start();
		
		boolean ret=true;
		
		for(int i=0;i<text_values.size();i++)
		{
			if(text_values.get(i).getOriginalValue().compareTo((String)(text_values.get(i).getComboBox().getSelectedItem()))!=0)
			{
				keys[2]="field_id";values[2]=""+text_values.get(i).getFieldID();
				keys[3]="field_value";values[3]=(String)(text_values.get(i).getComboBox().getSelectedItem());
				HTTPXMLComEvent e=HTTPXMLCommunication.sendRequest(Database.db_url+"upload_record.php",keys,values);
				if(e.wasSuccessful()){}else ret=false;
			}
		}
		
		text_values=new ArrayList<DatabaseString>();
		//images=new ArrayList<DatabaseImage>();
		
		downloadRecord_();
		this.getResultIcon().updateImage();
		DEAapp.status_bar.stop();
		
		if(ret)DEAapp.status_bar.setStatus("Record updated successfully.",0);
		else DEAapp.status_bar.setStatus("Error while updating record.",0);
		
		save_changes_button.setEnabled(true);
		DEAapp.data.setNowEditing(null);
		DEAapp.data.getRecordPanel();
		DEAapp.viewer_panel.repaint();
		return ret;
	}
	
	
	public void uploadRecord()
	{
		class R implements Runnable
		{
			DatabaseRecord rec;
			
			R(DatabaseRecord rec)
			{this.rec=rec;}
			public void run() {
				rec.uploadRecordSerial();
			}
		};
		new Thread(new R(this)).start();
	}
	
	public BufferedImage getThumbnail(File f) throws FileNotFoundException, IOException
	{
		ImageIcon i = new ImageIcon(ImageIO.read(new FileInputStream(f)));
		int h=i.getIconHeight();
		int w=i.getIconWidth();
		if(h>w){w=(w*100)/h;h=100;}
		else{h=(h*100)/w;w=100;}
		Image thumbnail=i.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
		BufferedImage bi=new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.createGraphics();
		g.drawImage(thumbnail, 0, 0, null);
	    g.dispose();
		return bi;
	}
	
	public BufferedImage getSizeLimitedImage(File f,int bytelimit) throws FileNotFoundException, IOException
	{
		ImageIcon i = new ImageIcon(ImageIO.read(new FileInputStream(f)));
		int h=i.getIconHeight();
		int w=i.getIconWidth();
		
		int sz=900;
		boolean done=false;
		BufferedImage bi=null;
		
		for(;done==false;)
		{
			if(h>w){w=(w*sz)/h;h=sz;}
			else{h=(h*sz)/w;w=sz;}
			Image thumbnail=i.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
			bi=new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
			Graphics g = bi.createGraphics();
			g.drawImage(thumbnail, 0, 0, null);
		    g.dispose();
		    
		    File thumbfile=new File(DEAapp.default_writing_path+"DEAtemp.png");
			ImageIO.write(bi, "png", thumbfile);
		    if(thumbfile.length()<bytelimit) 
		    {
		    	done=true;
		    	//System.out.println(thumbfile.length());
		    }
		    else sz-=50;
		}
		
		
		return bi;
	}

	public void uploadImageSerial(File upfile)
	{
		DEAapp.status_bar.setStatus("Adding image...",1);
		new Thread(DEAapp.status_bar).start();
		int err=0;
		
		if(DEAapp.setWritingPath())
		{
		    try {
		    		BufferedImage bi=getSizeLimitedImage(upfile,1000000);
		    	
		    	File scaledfile=new File(DEAapp.default_writing_path+"DEAtemp.png");
				ImageIO.write(bi, "png", scaledfile);
				String keys[]=new String[2];keys[0]="session_id";keys[1]="file";
				String values[]=new String[2];values[0]=DEAapp.data.getSessionID();values[1]="search.png";
				HTTPXMLComEvent e=HTTPXMLCommunication.sendFileRequest(Database.db_url+"upload_image.php",keys,values,scaledfile);
				
				if(e.wasSuccessful())
				{
					NodeList nList = e.getDocument().getElementsByTagName("image");
					//System.out.println("-----------------------");
			 
					String image_id="";
					for (int temp = 0; temp < nList.getLength(); temp++) {
			 
					   Node nNode = nList.item(temp);
					   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			 
					      Element eElement = (Element) nNode;
			 
					      image_id=Database.getTagValue("id", eElement);
					     }
					}
			
					
					bi=getThumbnail(upfile);
					File thumbfile=new File(DEAapp.default_writing_path+"DEAtemp.png");
					ImageIO.write(bi, "png", thumbfile);
					String keys_th[]=new String[3];keys_th[0]="session_id";keys_th[1]="image_id";keys_th[2]="file";
					String values_th[]=new String[3];values_th[0]=DEAapp.data.getSessionID();values_th[1]=image_id;values_th[2]="search.png";
					HTTPXMLComEvent e_th=HTTPXMLCommunication.sendFileRequest(Database.db_url+"upload_thumbnail.php",keys_th,values_th,thumbfile);
					if(e_th.wasSuccessful())
					{
						String keys_r[]=new String[4];
						String values_r[]=new String[4];
						keys_r[0]="session_id";values_r[0]=DEAapp.data.getSessionID();
						keys_r[1]="record_id";values_r[1]=id;
						keys_r[2]="field_id";values_r[2]="1000";//images
						keys_r[3]="field_value";values_r[3]=image_id;
						HTTPXMLComEvent e_r=HTTPXMLCommunication.sendRequest(Database.db_url+"upload_record2.php",keys_r,values_r);
						if(e_r.wasSuccessful())
						{ 
							downloadRecord_();
							downloadImages();
							this.result_icon.updateImage();
							getMainPanel();
							getImagePanel();
							DEAapp.viewer_panel.repaint();
							DEAapp.tab_panel.repaint();
						}
						else err=5;
					}
					else 
					{
						err=4;
					}
				}
				else
				{
					err=3;
				}
				
		    } catch (Exception e) {
				err=2;
				}
		}
		else
		{
			err=1;
		}
		
		
		DEAapp.status_bar.stop();
		if(err==0)
			DEAapp.status_bar.setStatus("Image added succesfully.", 0);
		else DEAapp.status_bar.setStatus("ERROR "+err+": Image was not added.", 0); 
	}
	
	public void uploadImage(File upfile)
	{
		class R implements Runnable
		{
			DatabaseRecord rec;
			File upfile;
			
			R(DatabaseRecord rec, File upfile)
			{this.rec=rec;this.upfile=upfile;}
			public void run() {
				rec.uploadImageSerial(upfile);
			}
		};
		new Thread(new R(this,upfile)).start();
	}
	
	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==save_changes_button)
		{
			uploadRecord(); 
		}
		else if(e.getSource()==edit_button)
		{
			if(DEAapp.data.user_id.length()==0)
			{
				DEAapp.showInformationDialog("Login required", "<html>Please login first. If you want to become an editor please contact<br>the administrators regarding the policies and procedures.<br>We would be happy to welcome you to our editorial board!");		
			}
			else
			{
			DEAapp.data.setNowEditing(this);
			for(int i=0;i<DEAapp.data.fields.length;i++)
			{
				DEAapp.data.fields[i].record_text_field.setSelectedItem("");
			}
			for(int i=0;i<text_values.size();i++)
			{
				text_values.get(i).getComboBox().setSelectedItem(text_values.get(i).getOriginalValue());
			}
			DEAapp.data.getRecordPanel();
			}
		}
		else if(e.getSource()==add_photo_button)
		{
			if(DEAapp.data.user_id.length()==0)
			{
				DEAapp.showInformationDialog("Login required", "<html>Please login first. If you want to become an editor please contact<br>the administrators regarding the policies and procedures.<br>We would be happy to welcome you to our editorial board!");		
			}
			else
			{
			JFileChooser chooser = new JFileChooser();
            chooser.setFileHidingEnabled(false);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setMultiSelectionEnabled(false);
            chooser.setDialogType(JFileChooser.OPEN_DIALOG);
            if(DEAapp.most_recent_path.length()>0)
				chooser.setCurrentDirectory(new File(DEAapp.most_recent_path));
            chooser.setDialogTitle("Choose an image file...");
            ImagePreviewer previewer = new ImagePreviewer(chooser);
	        previewer.setToolTipText("This is a quick preview of the selected image.");
	        chooser.setAccessory(previewer);
	        chooser.setApproveButtonText("Upload image"); 
	        
            if (chooser.showOpenDialog(main_panel)== JFileChooser.APPROVE_OPTION) 
            {  
            	DEAapp.most_recent_path=chooser.getCurrentDirectory().getAbsolutePath();
				
            	if(previewer.thumbnail==null)
            	{
            		DEAapp.showErrorDialog("Image Format Error", "This image file format is not supported.");
            	}
            	else
            	{
            		//DEAapp.data.uploadImageThread(chooser.getSelectedFile());
            		uploadImage(chooser.getSelectedFile());
            	}
            }
			}
            
		}
		else if(e.getSource()==previous_image)
		{
			if(viewer2d.im!=null)
			{
				int idx=getImageIndex(viewer2d.im.getID());
				if(idx==-1)
				{
					if(images.size()==0) viewer2d.setImage(null);
					else viewer2d.setImage(images.get(0));
				}
				else
				{
					idx-=1;
					if(idx<0) viewer2d.setImage(images.get(images.size()-1));
					else viewer2d.setImage(images.get(idx));
				}
			}
		}
		else if(e.getSource()==next_image)
		{
			if(viewer2d.im!=null)
			{
				int idx=getImageIndex(viewer2d.im.getID());
				if(idx==-1)
				{
					if(images.size()==0) viewer2d.setImage(null);
					else viewer2d.setImage(images.get(0));
				}
				else
				{
					idx+=1;
					if(idx>=images.size()) viewer2d.setImage(images.get(0));
					else viewer2d.setImage(images.get(idx));
				}
			}
		}
		else if(e.getSource()==download_photo_button)
		{
			JFileChooser chooser = new JFileChooser();
            chooser.setFileHidingEnabled(false);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setMultiSelectionEnabled(false);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.addChoosableFileFilter(new FileImageFilter());
 
            chooser.setDialogType(JFileChooser.SAVE_DIALOG);
            if(most_recent_downloading_photo_path.length()==0)
            	most_recent_downloading_photo_path=DEAapp.most_recent_path;
       
            if(most_recent_downloading_photo_path.length()>0)
				chooser.setCurrentDirectory(new File(most_recent_downloading_photo_path));
            chooser.setDialogTitle("Download image file...");
            chooser.setApproveButtonText("Save file"); 
	        
            if (chooser.showSaveDialog(DEAapp.app)== JFileChooser.APPROVE_OPTION) 
            {
            	most_recent_downloading_photo_path=chooser.getCurrentDirectory().getAbsolutePath();
             String extension=FileImageFilter.getExtension(chooser.getSelectedFile());
             if (extension != null && extension.compareTo("png")==0) 
             {
            	 viewer2d.savePNG(chooser.getSelectedFile().getPath());
             } else 
             {
                 viewer2d.savePNG(chooser.getSelectedFile().getPath()+".png");
             }
             
           }
		}else if(e.getSource()==download_info_button)
		{
			JFileChooser chooser = new JFileChooser();
            chooser.setFileHidingEnabled(false);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setMultiSelectionEnabled(false);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.addChoosableFileFilter(new FileInfoFilter());
 
            chooser.setDialogType(JFileChooser.SAVE_DIALOG);
            if(most_recent_downloading_photo_path.length()==0)
            	most_recent_downloading_photo_path=DEAapp.most_recent_path;
       
            if(most_recent_downloading_photo_path.length()>0)
				chooser.setCurrentDirectory(new File(most_recent_downloading_photo_path));
            chooser.setDialogTitle("Download info file...");
            chooser.setApproveButtonText("Save file"); 
	        
            if (chooser.showSaveDialog(DEAapp.app)== JFileChooser.APPROVE_OPTION) 
            {
            	most_recent_downloading_photo_path=chooser.getCurrentDirectory().getAbsolutePath();
             String extension=FileInfoFilter.getExtension(chooser.getSelectedFile());
             if (extension != null && extension.compareTo("xml")==0) 
             {
            	 saveXML(chooser.getSelectedFile().getPath());
             } else 
             {
                 saveXML(chooser.getSelectedFile().getPath()+".xml");
             }
             
           }
		}
	}
	
	public void stateChanged(ChangeEvent e) {
		if(e.getSource()==zoom_slider)
		{
			viewer2d.setZoom(zoom_slider.getValue());
		}
	}
	
	public void saveXML(String filename)
	{
		File f=new File(filename);
		if(f.exists() && !DEAapp.showConfirmDialog("Overwrite existing file?", "The selected file already exists. Do you want to overwrite existing file?")) return;
			
		try {
			
			FileWriter fstream;
			fstream = new FileWriter(f);
			PrintWriter out = new PrintWriter(fstream);
			out.println("<?xml version=\"1.0\"?>");
			out.println("<inscription>");
			out.println("  <field>");
			out.println("     <title>DEA ID</title>");
			out.println("     <value>"+this.id+"</value>");
			out.println("  </field>");
			int fid;
			for(int i=0;i<text_values.size();i++)
			{
				DatabaseString ds=text_values.get(i);
				if(!ds.getDeletedByUser()&&ds.getOriginalValue().length()!=0)
				{
					fid=DEAapp.data.getFieldIndex(ds.getFieldID());
					out.println("  <field>");
					out.println("     <title>"+DEAapp.data.fields[fid].name+"</title>");
					out.println("     <value>"+ds.getOriginalValue()+"</value>");
					out.println("  </field>");
				}
			}
			out.print("</inscription>"); 
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			DEAapp.showErrorDialog("Error","The file could not be saved in this folder!");
		}
	}
}
