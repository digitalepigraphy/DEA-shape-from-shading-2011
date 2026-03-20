package edu.ufl.digitalworlds.dea.database;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.ufl.digitalworlds.net.HTTPXMLComEvent;
import edu.ufl.digitalworlds.net.HTTPXMLCommunication;
import edu.ufl.digitalworlds.dea.gui.DEAapp;

public class DatabaseField implements ActionListener
{
	int id;
	int type_id;
	int ord;
	int group_id=0;
	String name;
	String description;
	String example;
	boolean search_field=false;
	JComboBox text_field;
	JComboBox record_text_field;
	JButton remove_button;
	DefaultMutableTreeNode tree_node;
	
	DatabaseField()
	{
		text_field=new JComboBox();
		text_field.setEditable(true);
		
		record_text_field=new JComboBox();
		record_text_field.setEditable(true);
		
		remove_button=new JButton("x");
		remove_button.setToolTipText("Remove this field from your search.");
		remove_button.addActionListener(this);
		
		tree_node=new DefaultMutableTreeNode("Loading ...");
	}
	
	public DefaultMutableTreeNode getTreeNode(){return tree_node;}
	
	public static synchronized void TreeCleaning(DatabaseField f, DefaultMutableTreeNode root)
	{
		if(root==null)
		{
			DefaultMutableTreeNode parent=(DefaultMutableTreeNode) f.tree_node.getParent();
			f.tree_node.removeFromParent();
			if(parent!=null && parent.getChildCount()==0)
				parent.removeFromParent();
			f.tree_node=null;
		}
		else
		{
			if(f.getTreeNode().getChildCount()>0)
				root.add(f.getTreeNode());
		}
	}
	
	private void downloadSampleValuesSerial()
	{
		
		HTTPXMLComEvent e=HTTPXMLCommunication.sendRequest(Database.db_url+"fields/"+getID()+".xml");
		if(e.wasSuccessful())
		{
			NodeList nList = e.getDocument().getElementsByTagName("samples");
			//System.out.println("-----------------------");
	 
			String sample_value="";
			text_field.removeAllItems();
			text_field.setSelectedItem("");
			record_text_field.removeAllItems();
			record_text_field.setSelectedItem("");
			
			int i=0;
			for (int temp = 0; temp < nList.getLength(); temp++) {
	 
			   Node nNode = nList.item(temp);
			   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
				  Element eElement = (Element) nNode;
			      sample_value=Database.getTagValue("value", eElement);
			      if(sample_value.length()>0)
			      {
			    	  text_field.addItem(sample_value);
			    	  record_text_field.addItem(sample_value);
			    	  tree_node.add(new DefaultMutableTreeNode(sample_value));
			    	  i+=1;
			      }
			      
			     }
			}
			
			if(i==0)
			{
				TreeCleaning(this,null);
			}
		}
	}
	
	public void downloadSampleValues()
	{		
		class R implements Runnable
		{
			public void run() {
				downloadSampleValuesSerial();
			}
		};
		new Thread(new R()).start();	
	}
	
	public int getID(){return id;}
	public void setID(int id){this.id=id;}
	
	public int getType(){return type_id;}
	public void setType(int type_id){this.type_id=type_id;}
	

	public int getOrd(){return ord;}
	public void setOrd(int ord){this.ord=ord;}
	
	public boolean isGroup(){if(group_id==0) return true; else return false;}

	public int getGroupID(){return group_id;}
	public void setGroupID(int group_id){this.group_id=group_id;}
	
	public String getName(){return name;}
	public void setName(String name){this.name=name;tree_node.setUserObject(name);}
	
	public String getDescription(){return description;}
	public void setDescription(String description){this.description=description;}
	
	public String getExample(){return example;}
	public void setExample(String example){this.example=example;}
	
	public boolean isSearchField(){return search_field;}
	public void setSearchField(boolean search_field){this.search_field=search_field;}
	
	public JComboBox getTextField(){return text_field;}	
	public String getText(){return (String) text_field.getSelectedItem();}
	
	public JComboBox getRecordTextField(){return record_text_field;}
	public String getRecordText(){return (String) record_text_field.getSelectedItem();}
	
	public JButton getRemoveButton(){return remove_button;}
	
	public String toString(){if(isGroup()) return name;else return "    "+name;}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==remove_button)
		{
			search_field=false;
			text_field.setSelectedItem("");
			DEAapp.data.getSearchFields();
		}
	}
	
}