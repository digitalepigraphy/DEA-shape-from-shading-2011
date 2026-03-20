package edu.ufl.digitalworlds.dea.database;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.ufl.digitalworlds.net.HTTPXMLComEvent;
import edu.ufl.digitalworlds.net.HTTPXMLComListener;
import edu.ufl.digitalworlds.net.HTTPXMLCommunication;
import edu.ufl.digitalworlds.dea.gui.DEAapp;
import edu.ufl.digitalworlds.dea.gui.TabPanel;

public class Database implements  ActionListener
{
	public static String db_url="http://www.digitalworlds.ufl.edu/angelos/lab/DEA/";
	
	public String user_id="";
	String session_ip="128.0.0.1";
	String session_id="NTCN";
	boolean session_connected=false;
	
	JButton add_this_field_button;
	JComboBox combo_box;
	
	JComboBox record_combo_box;
	JButton record_add_this_field_button;
	
	
	JPanel search_panel;
	JPanel record_panel;
	JPanel image_panel;
	
	public DatabaseField fields[];
	public DatabaseField field_groups[];
	public DatabaseRecord current_record;
	
	
	public List<DatabaseRecord> global_records;
	
	public DatabaseRecord is_now_editing=null;
	public void setNowEditing(DatabaseRecord is_now_editing){this.is_now_editing=is_now_editing;}
	public DatabaseRecord isNowEditing(){return is_now_editing;}
	
	
	public Database()
	{
		global_records=new ArrayList<DatabaseRecord>();
		
		fields=new DatabaseField[0];
		add_this_field_button=new JButton("Add field");
		add_this_field_button.addActionListener(this);
		
		record_add_this_field_button=new JButton("Add field");
		record_add_this_field_button.addActionListener(this);
		
		search_panel=new JPanel(new GridBagLayout());
		record_panel=new JPanel(new BorderLayout());
		image_panel=new JPanel(new BorderLayout());
	
		newSession();
		
		
		current_record=new DatabaseRecord("05e2729b149912bf");//d1a7a25fdaf3010d");//05e2729b149912bf");
		current_record.downloadRecordFile();
		current_record.downloadImages();
		global_records.add(current_record);
	}
	
	
	
	public int getRecordIndex(String record_id)
	{
		int ret=-1;
		for(int i=0;i<global_records.size() && ret==-1;i++)
		{
			if(global_records.get(i).getID().compareTo(record_id)==0)ret=i;
		}
		return ret;
	}
	
	public int getFieldIndex(int field_id)
	{
		int ret=-1;
		for(int i=0;i<fields.length && ret==-1;i++)
		{
			if(fields[i].getID()==field_id)ret=i;
		}
		return ret;
	}
	
	public DatabaseRecord getRecord(String record_id)
	{
		DatabaseRecord ret=null;
		for(int i=0;i<global_records.size() && ret==null;i++)
		{
			if(global_records.get(i).getID().compareTo(record_id)==0)ret=global_records.get(i);
		}
		return ret;
	}
	
	
	public boolean newSession()
	{
		session_connected=false;
		HTTPXMLComEvent e=HTTPXMLCommunication.sendRequest(db_url+"new_session.php");
		if(e.wasSuccessful())
		{
			NodeList nList = e.getDocument().getElementsByTagName("session");
			//System.out.println("-----------------------");
	 
			for (int temp = 0; temp < nList.getLength(); temp++) {
	 
			   Node nNode = nList.item(temp);
			   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
			      Element eElement = (Element) nNode;
	 
			      session_id=getTagValue("id", eElement);
			      session_ip=getTagValue("ip", eElement);
			     }
			}
			
			session_connected=true;
		}
		return session_connected;
	}
	
	public void newSessionSilent()
	{
		class R implements Runnable
		{
			public void run() {
				newSession();
			}
		};
		new Thread(new R()).start();
	}
	
	public boolean newInscription()
	{
		String keys[]=new String[1];keys[0]="session_id";
		String values[]=new String[1];values[0]=getSessionID();
		HTTPXMLComEvent e=HTTPXMLCommunication.sendRequest(db_url+"new_inscription.php",keys,values);
		if(e.wasSuccessful())
		{//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nList = e.getDocument().getElementsByTagName("inscription");
			//System.out.println("-----------------------");
	 
			String id="";
			for (int temp = 0; temp < nList.getLength(); temp++) {
	 
			   Node nNode = nList.item(temp);
			   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
			      Element eElement = (Element) nNode;
	 
			      id=getTagValue("id", eElement);
			     }
			}
			current_record=new DatabaseRecord(id);
			
			return true;
		}else return false;
	}

	
	public boolean getFields()
	{
		HTTPXMLComEvent e=HTTPXMLCommunication.sendRequest(db_url+"fields/index.xml");
		if(e.wasSuccessful())
		{
			NodeList nList = e.getDocument().getElementsByTagName("field");
			//System.out.println("-----------------------");
	 
			fields=new DatabaseField[nList.getLength()];
			int i=0;
			for (int temp = 0; temp < nList.getLength(); temp++) {
	 
			   Node nNode = nList.item(temp);
			   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
				   fields[i]=new DatabaseField();
			      Element eElement = (Element) nNode;
			      fields[i].setID(Integer.parseInt(getTagValue("id", eElement)));
			      fields[i].setType(Integer.parseInt(getTagValue("type_id", eElement)));
			      fields[i].setOrd(Integer.parseInt(getTagValue("ord", eElement)));
			      fields[i].setGroupID(Integer.parseInt(getTagValue("group_id", eElement)));
			      fields[i].setName(getTagValue("name", eElement));
			      fields[i].setDescription(getTagValue("description", eElement));
			      fields[i].setExample(getTagValue("example", eElement));
			      fields[i].downloadSampleValues();
			      i+=1;
			     }
			}

			return true;
		}
		else return false;
	}

	public boolean getFieldGroups()
	{
		HTTPXMLComEvent e=HTTPXMLCommunication.sendRequest(db_url+"fields/groups.xml");
		if(e.wasSuccessful())
		{
			NodeList nList = e.getDocument().getElementsByTagName("field_group");
			//System.out.println("-----------------------");
	 
			field_groups=new DatabaseField[nList.getLength()];
			int i=0;
			for (int temp = 0; temp < nList.getLength(); temp++) {
	 
			   Node nNode = nList.item(temp);
			   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
				   field_groups[i]=new DatabaseField();
			      Element eElement = (Element) nNode;
			      field_groups[i].setID(Integer.parseInt(getTagValue("id", eElement)));
			      field_groups[i].setOrd(Integer.parseInt(getTagValue("ord", eElement)));
			      field_groups[i].setName(getTagValue("name", eElement));
			      i+=1;
			     }
			}

			return true;
		}
		else return false;
	}
	
		
	public void searchRecord(List<String> ids, List<Integer> matches, String conjunction)
	{

		int num_of_fields=0;
		for(int j=0;j<field_groups.length;j++)
		{
			for(int i=0;i<fields.length;i++)
			{
				if(field_groups[j].getID()==fields[i].getGroupID() && fields[i].isSearchField() && fields[i].getText().length()>0)
				{
					num_of_fields+=1;
				}
			}
		}
		
		String keys[]=new String[num_of_fields*2+3];
		String values[]=new String[num_of_fields*2+3];
		keys[0]="session_id";values[0]=getSessionID();
		keys[1]="num_of_fields";values[1]=""+num_of_fields;
		keys[2]="conjunction";values[2]=conjunction;
		
		int c=0;
		for(int j=0;j<field_groups.length;j++)
		{
			for(int i=0;i<fields.length;i++)
			{
				if(field_groups[j].getID()==fields[i].getGroupID() && fields[i].isSearchField() && fields[i].getText().length()>0)
				{
					if(c+1<10)
					{
						keys[c*2+3]="field_id0"+(c+1);
						values[c*2+3]=""+fields[i].getID();
						keys[c*2+1+3]="field_value0"+(c+1);
						values[c*2+1+3]=""+("*"+fields[i].getText()+"*").replace('*', '%').replace('?', '_');
					}
					else
					{
						keys[c*2+3]="field_id"+(c+1);
						values[c*2+3]=""+fields[i].getID();
						keys[c*2+1+3]="field_value"+(c+1);
						values[c*2+1+3]=""+("*"+fields[i].getText()+"*").replace('*', '%').replace('?', '_');
					}
					c+=1;
				}
			}
		}
		
		
		HTTPXMLComEvent e=HTTPXMLCommunication.sendRequest(db_url+"search_record.php",keys,values);
		if(e.wasSuccessful())
		{
			NodeList nList = e.getDocument().getElementsByTagName("record");
			//System.out.println("-----------------------");
			int i=0;
			for (int temp = 0; temp < nList.getLength(); temp++) {
	 
			   Node nNode = nList.item(temp);
			   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
			      Element eElement = (Element) nNode;
			   
			      ids.add(getTagValue("id", eElement));
			      matches.add(new Integer((int)Float.parseFloat(getTagValue("match", eElement))));
			      
			      i+=1;
			     }
			}

		}
	}

	public JPanel getRecordPanel()
	{
		record_panel.removeAll();
		
		record_panel.add(current_record.getMainPanel(),BorderLayout.CENTER);
		
		record_panel.repaint();
		return record_panel;
	}
	
	public JPanel getImagePanel()
	{
		image_panel.removeAll();
		
		image_panel.add(current_record.getImagePanel(),BorderLayout.CENTER);
		
		image_panel.repaint();
		return image_panel;
	}
	
	public JPanel getSearchFields()
	{
		search_panel.removeAll();
		int c=0;
		for(int j=0;j<field_groups.length;j++)
		{
			JPanel p_in=new JPanel(new GridBagLayout());
			p_in.setBorder(new TitledBorder(field_groups[j].getName()));
			int c_in=0;
			for(int i=0;i<fields.length;i++)
			{
				if(field_groups[j].getID()==fields[i].getGroupID() && fields[i].isSearchField())
				{
					JPanel p=new JPanel(new BorderLayout());
					p.add(new JLabel(fields[i].getName()+":"),BorderLayout.WEST);
			       	p.add(fields[i].getTextField(),BorderLayout.CENTER);
			    	p.add(fields[i].getRemoveButton(),BorderLayout.EAST);
			       	DEAapp.addToGridBag(p_in,p, 0, c_in, 1, 1, 1.0, 1.0);
					c_in+=1;
				}
			}
			if(c_in>0)
			{
				DEAapp.addToGridBag(search_panel,p_in, 0, c, 1, 1, 1.0, 1.0);
				c+=1;
			}
			
		}
		JPanel p1_=new JPanel(new BorderLayout());
		if(c==0)
		{
			DEAapp.addToGridBag(search_panel,new JLabel(" "), 0, c, 1, 1, 1.0, 1.0);
			c+=1;
			DEAapp.addToGridBag(search_panel,new JLabel("Your search form does not have any fields!"), 0, c, 1, 1, 1.0, 1.0);
			c+=1;
			DEAapp.addToGridBag(search_panel,new JLabel(" "), 0, c, 1, 1, 1.0, 1.0);
			TabPanel.search_keyword_button.setEnabled(false);
		}
		else  
		{
			DEAapp.addToGridBag(search_panel,new JLabel(" "), 0, c, 1, 1, 1.0, 1.0);
			TabPanel.search_keyword_button.setEnabled(true);
		}
		
		p1_.setBorder(new TitledBorder("Add fields to your search form:"));
        
		if(combo_box==null)
		{
       	combo_box=new JComboBox();
       	
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
       	
       	combo_box.setRenderer(new MyCellRenderer());
		}
		else combo_box.removeAllItems();
		
       	DatabaseField f=new DatabaseField();
       	f.setName("Select a field...");
       	f.setGroupID(100);
       	combo_box.addItem(f);
       	
       	for(int j=0;j<field_groups.length;j++)
		{
       		combo_box.addItem(field_groups[j]);
       		
       		for(int i=0;i<fields.length;i++)
       		{
       			if(field_groups[j].getID()==fields[i].getGroupID() && !fields[i].isSearchField())
       				combo_box.addItem(fields[i]);
       		}
		}
       	
       	p1_.add(combo_box,BorderLayout.CENTER);
       	p1_.add(add_this_field_button,BorderLayout.EAST);
       	
       	DEAapp.addToGridBag(search_panel,p1_, 0, c+1, 1, 1, 1.0, 1.0);
       	DEAapp.addToGridBag(search_panel,new JLabel(" "), 0, c+2, 1, 1, 1.0, 1.0);
       	
       	search_panel.revalidate();
       	search_panel.repaint();
		return search_panel;
	}
		
	public String getSessionID(){return session_id;}
	public String getSessionIP(){return session_ip;}
	public boolean isSessionConnected(){return session_connected;}
	
	public static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
	 
	        Node nValue = (Node) nlList.item(0);
	 
	        if(nValue==null) return "";
	        else return nValue.getNodeValue();
	  }
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==add_this_field_button)
		{
			if(combo_box.getSelectedIndex()!=-1)
			{
				((DatabaseField)(combo_box.getSelectedItem())).setSearchField(true);
				getSearchFields();
			}
		}
		else if(e.getSource()==record_add_this_field_button)
		{
			if(record_combo_box.getSelectedIndex()!=-1)
			{
				int fid=((DatabaseField)(record_combo_box.getSelectedItem())).getID();
				if(fid!=0)
				{
					int vid=current_record.getValueIndex(fid);
					if(vid==-1)
					{
						DatabaseString ds=new DatabaseString(current_record);
						ds.setFieldID(fid);
						current_record.text_values.add(ds);
					}
					else current_record.text_values.get(vid).setDeletedByUser(false);
				}
				current_record.getMainPanel();
			}
		}
	}
		
	
}