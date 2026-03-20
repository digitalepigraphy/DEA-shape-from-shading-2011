package edu.ufl.digitalworlds.dea.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import edu.ufl.digitalworlds.gui.DWApp;
import edu.ufl.digitalworlds.dea.database.DatabaseField;
import edu.ufl.digitalworlds.dea.database.DatabaseString;
import edu.ufl.digitalworlds.dea.media.Images;


public class TabPanel extends JPanel implements ActionListener, TreeSelectionListener
{
	public static JTabbedPane tabbedPane;
	
	JPanel search_panel;
	JPanel input_panel;
	
	JPanel search_panel_main;
	JButton browse_button;
	JButton keyword_button;
	JButton geographical_search_button;
	
	JPanel search_logos;
	JPanel search_panel_keyword;
	JButton back_button;
	JCheckBox hard_search_box;
	public static JButton search_keyword_button;
	
	JPanel search_panel_browse;
	JButton back_button2;
	JTree browse_tree;
	JButton search_browse_button;
	
	JTextField username_textfield;
	JPasswordField password_textfield;
	JButton login_button;
	JButton new_inscription_button;
	
	
	
	public static List<SearchResults> results_panel;
	int search_counter=0;
	
	TabPanel()
	{
		results_panel=new ArrayList<SearchResults>();
		
		setLayout(new GridLayout(0,1));
		
		EmptyBorder eb = new EmptyBorder(5,5,5,5);
        BevelBorder bb = new BevelBorder(BevelBorder.LOWERED);
        //setBorder(new CompoundBorder(eb,bb));
		
        JPanel p1_=new JPanel(new BorderLayout());
        p1_.setBorder(new EtchedBorder());
        //p1_.setBorder(new TitledBorder(new EtchedBorder(), "Controls"));
        
        
		tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("serif", Font.PLAIN, 12));

            
        //The main panel has a tabbedPane on the top and a line on th bottom
        p1_.add(tabbedPane,BorderLayout.CENTER);
        p1_.add(new JLabel("________________________________________"), BorderLayout.SOUTH);
        add(p1_);

        //Search panel
        search_panel=new JPanel(new BorderLayout());
        search_panel_main=new JPanel(new GridBagLayout());
        browse_button=new JButton("Browse Collections",Images.search_collections_icon);
       	DEAapp.addToGridBag(search_panel_main, browse_button, 0, 0, 1, 1, 1.0, 1.0);
       	keyword_button=new JButton("Search by keywords",Images.search_keywords_icon);
       	DEAapp.addToGridBag(search_panel_main,keyword_button, 0, 1, 1, 1, 1.0, 1.0);
       	geographical_search_button=new JButton("Geographical search",Images.earth_icon);
       	
       	DEAapp.addToGridBag(search_panel_main,geographical_search_button, 0, 2, 1, 1, 1.0, 1.0);
       	keyword_button.addActionListener(this);
       	browse_button.addActionListener(this);
       	geographical_search_button.addActionListener(this);
       	search_panel.add(search_panel_main,BorderLayout.NORTH);
       	
       	search_logos=new JPanel(new GridLayout(0,1));
       	search_logos.add(new JLabel(Images.uf_icon));
       	search_logos.add(new JLabel(Images.neh_icon));
       	
       	search_panel.add(search_logos,BorderLayout.SOUTH);
       	
       	search_panel_keyword=new JPanel(new GridBagLayout());
       	p1_=new JPanel(new BorderLayout());
       	back_button=new JButton("Back to main search",Images.back_icon);
       	back_button.addActionListener(this);
       	p1_.add(back_button,BorderLayout.WEST);
       	DEAapp.addToGridBag(search_panel_keyword, p1_, 0, 0, 1, 1, 1.0, 1.0);
       	/*p1_=new JPanel(new BorderLayout());
       	p1_.add(new JLabel("Date: "),BorderLayout.WEST);
       	p1_.add(new JTextField(),BorderLayout.CENTER);
       	DEAapp.addToGridBag(search_panel_keyword,p1_, 0, 1, 1, 1, 1.0, 1.0);
       	p1_=new JPanel(new BorderLayout());
       	p1_.add(new JLabel("Collection: "),BorderLayout.WEST);
       	p1_.add(new JTextField(),BorderLayout.CENTER);
       	DEAapp.addToGridBag(search_panel_keyword,p1_, 0, 2, 1, 1, 1.0, 1.0);
       	p1_=new JPanel(new BorderLayout());
       	p1_.add(new JLabel("Location: "),BorderLayout.WEST);
       	p1_.add(new JTextField(),BorderLayout.CENTER);
       	DEAapp.addToGridBag(search_panel_keyword,p1_, 0, 3, 1, 1, 1.0, 1.0);
       	p1_=new JPanel(new BorderLayout());
       	p1_.add(new JLabel("IG: "),BorderLayout.WEST);
       	p1_.add(new JTextField(),BorderLayout.CENTER);
       	DEAapp.addToGridBag(search_panel_keyword,p1_, 0, 4, 1, 1, 1.0, 1.0);
       	p1_=new JPanel(new BorderLayout());
       	p1_.add(new JLabel("Text: "),BorderLayout.WEST);
       	p1_.add(new JTextField(),BorderLayout.CENTER);
       	DEAapp.addToGridBag(search_panel_keyword,p1_, 0, 5, 1, 1, 1.0, 1.0);*/
       	
       	
       	search_keyword_button=new JButton("Search",Images.search_keywords_icon);
       	search_keyword_button.addActionListener(this);
       	DEAapp.addToGridBag(search_panel_keyword,DEAapp.data.getSearchFields(), 0, 1, 1, 1, 1.0, 1.0);
       	
       	hard_search_box=new JCheckBox("Perform hard search (fewer results)");
       	hard_search_box.setToolTipText("All the given keywords will be present in the results.");
       	DEAapp.addToGridBag(search_panel_keyword,hard_search_box, 0, 2, 1, 1, 1.0, 1.0);
       	DEAapp.addToGridBag(search_panel_keyword,new JLabel(" "), 0, 3, 1, 1, 1.0, 1.0);
       	DEAapp.addToGridBag(search_panel_keyword,search_keyword_button, 0, 4, 1, 1, 1.0, 1.0);
       	
       	
       	//Browse panel
       	search_panel_browse=new JPanel(new BorderLayout());
       	p1_=new JPanel(new BorderLayout());
       	back_button2=new JButton("Back to main search",Images.back_icon);
       	back_button2.addActionListener(this);
       	p1_.add(back_button2,BorderLayout.WEST);
       	search_panel_browse.add(p1_,BorderLayout.NORTH);
       	
       	/*{
       		DefaultMutableTreeNode a[]=new DefaultMutableTreeNode[1];
       		a[0]=new DefaultMutableTreeNode("root");
       		browse_tree=new JTree(a);
       	}*/
       	DefaultMutableTreeNode root=new DefaultMutableTreeNode("DEA database");
       	browse_tree=new JTree(root);
       	browse_tree.addTreeSelectionListener(this);
       	browse_tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

       	
       	for(int i=0;i<DEAapp.data.field_groups.length;i++)
       	{
       		DefaultMutableTreeNode a=DEAapp.data.field_groups[i].getTreeNode();
       		int c=0;
       		
       		for(int j=0;j<DEAapp.data.fields.length;j++)
       		{
       			if(DEAapp.data.fields[j].getGroupID()==DEAapp.data.field_groups[i].getID())
       			{
       				//DefaultMutableTreeNode b=new DefaultMutableTreeNode(DEAapp.data.fields[j].getName());
       				//a.add(b);
       				if(DEAapp.data.fields[j].getTreeNode()!=null)
       				{
       					a.add(DEAapp.data.fields[j].getTreeNode());
       					c+=1;
       				}
       			}
       		}
       		if(c>0)
       		{
       			DatabaseField.TreeCleaning(DEAapp.data.field_groups[i],root);
       		}
       	}
       
       	
        
        JScrollPane treeView = new JScrollPane(browse_tree);
        search_panel_browse.add(treeView,BorderLayout.CENTER);
       	
        search_browse_button=new JButton("Browse selected items",Images.search_collections_icon);
        search_browse_button.setEnabled(false);
       	search_browse_button.addActionListener(this);
       	search_panel_browse.add(search_browse_button,BorderLayout.SOUTH);
       	
        //Input panel
        input_panel=new JPanel(new BorderLayout());
        p1_=new JPanel(new GridBagLayout());
        
        JPanel loginpanel=new JPanel(new GridBagLayout());
        loginpanel.setBorder(new TitledBorder("DEA Account"));
        DEAapp.addToGridBag(loginpanel, new JLabel("e-mail:"), 0, 0, 1, 1, 1.0, 1.0);   
        username_textfield=new JTextField(); 
        DEAapp.addToGridBag(loginpanel, username_textfield, 0, 1, 1, 1, 1.0, 1.0);
        DEAapp.addToGridBag(loginpanel, new JLabel("DEA password:"), 0, 2, 1, 1, 1.0, 1.0);
        password_textfield=new JPasswordField();
        DEAapp.addToGridBag(loginpanel, password_textfield, 0, 3, 1, 1, 1.0, 1.0);
        login_button=new JButton("Login");
        login_button.addActionListener(this);
        DEAapp.addToGridBag(loginpanel, login_button, 0, 4, 1, 1, 1.0, 1.0);
        
        new_inscription_button=new JButton("Create a new epigraphical entry",Images.input_icon);
        new_inscription_button.addActionListener(this);
        DEAapp.addToGridBag(p1_, loginpanel, 0, 0, 1, 1, 1.0, 1.0);  
        DEAapp.addToGridBag(p1_, new JLabel(" "), 0, 1, 1, 1, 1.0, 1.0);
        DEAapp.addToGridBag(p1_, new_inscription_button, 0, 2, 1, 1, 1.0, 1.0);        
        //DEAapp.addToGridBag(p1_, new JLabel(" "), 0, 1, 1, 1, 1.0, 1.0);        
        
        input_panel.add(p1_,BorderLayout.NORTH);
        
       	search_logos=new JPanel(new GridLayout(0,1));
       	search_logos.add(new JLabel(Images.uf_icon));
       	search_logos.add(new JLabel(Images.neh_icon));

       	input_panel.add(search_logos,BorderLayout.SOUTH);
        
        tabbedPane.addTab("Input",Images.input_icon, input_panel);
        
        tabbedPane.addTab("Search",Images.search_icon, search_panel);
        
        tabbedPane.setSelectedIndex(1);
        tabbedPane.setToolTipTextAt(1, "Use this tab to search for an inscription.");
        tabbedPane.setToolTipTextAt(0, "Add a new inscription to the system.");
        
        //Results panel
        /*results_panel=new JPanel[3];
        for(int i=0;i<results_panel.length;i++)
        {
        	results_panel[i]=new JPanel(new GridBagLayout());
        	
        	List<String> ids=new ArrayList<String>();
        	
        	DEAapp.addToGridBag(results_panel[i], new SearchResults(ids), 0, 0, 1, 1, 1.0, 1.0);        
        	
        	tabbedPane.addTab("Results "+(i+1), results_panel[i]);
        	tabbedPane.setToolTipTextAt(i+2,"Search Results "+(i+1)+".");
        }*/
        
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==keyword_button)
		{
			search_panel.removeAll();
			search_panel.add(search_panel_keyword,BorderLayout.NORTH);
			//search_panel.revalidate();
			repaint();
		}
		else if(e.getSource()==back_button)
		{
			search_panel.removeAll();
			search_panel.add(search_panel_main,BorderLayout.NORTH);
			search_panel.add(search_logos,BorderLayout.SOUTH);
			//search_panel.revalidate();
			repaint();
		}
		else if(e.getSource()==browse_button)
		{
			search_panel.removeAll();
			search_panel.add(search_panel_browse,BorderLayout.NORTH);
			//search_panel.revalidate();
			repaint();
		}
		else if(e.getSource()==back_button2)
		{
			search_panel.removeAll();
			search_panel.add(search_panel_main,BorderLayout.NORTH);
			search_panel.add(search_logos,BorderLayout.SOUTH);
			//search_panel.revalidate();
			repaint();
		}
		else if(e.getSource()==login_button)
		{
			if(username_textfield.getText().compareTo("admin@digitalepigraphy.org")==0 &&
			   password_textfield.getText().compareTo("ccc")==0)
			{
				DEAapp.data.user_id="admin";
				username_textfield.setText("");
				password_textfield.setText("");
				login_button.setEnabled(false);
				login_button.setText("Connected!");
			}
			else DEAapp.data.user_id="";
		}
		else if(e.getSource()==new_inscription_button)
		{
			if(DEAapp.data.user_id.length()==0)
			{
				DEAapp.showInformationDialog("Login required", "<html>Please login first. If you want to become an editor please contact<br>the administrators regarding the policies and procedures.<br>We would be happy to welcome you to our editorial board!");		
			}
			else
			{
			new_inscription_button.setEnabled(false);
			class R implements Runnable
			{
				public void run() {
					
					DEAapp.status_bar.setStatus("Creating new record...",1);
					new Thread(DEAapp.status_bar).start();
					
				if(DEAapp.data.newInscription())
				{
					DatabaseString ds=new DatabaseString(DEAapp.data.current_record);
		    		  ds.setFieldID(25);
		    		  ds.setOriginalValue("");
		    		  DEAapp.data.current_record.text_values.add(ds);
		    		  
					DEAapp.data.setNowEditing(DEAapp.data.current_record);
					DEAapp.data.getImagePanel();
					DEAapp.data.getRecordPanel();
					DEAapp.status_bar.setStatus("New entry created.");
					DEAapp.viewer_panel.tabbedPane.setSelectedIndex(2);
					DEAapp.viewer_panel.repaint();
				}
				else DEAapp.status_bar.setStatus("ERROR: New entry cannot be created.");
				new_inscription_button.setEnabled(true);
				DEAapp.status_bar.stop();
	        	DEAapp.status_bar.setStatus(0);
	        	
				}
			};
			new Thread(new R()).start();
			}
		}
		
		
		else if(e.getSource()==search_keyword_button || e.getSource()==search_browse_button)
		{

			search_counter+=1;
			search_keyword_button.setEnabled(false);
				class R implements Runnable
				{
					public void run() {
						DEAapp.status_bar.setStatus("Searching...",1);
						new Thread(DEAapp.status_bar).start();
						List<String> ids=new ArrayList<String>();
						List<Integer> matches=new ArrayList<Integer>();
						if(hard_search_box.isSelected())DEAapp.data.searchRecord(ids,matches,"and");
						else DEAapp.data.searchRecord(ids,matches,"or");
			        	SearchResults sr=new SearchResults(ids,matches);
			        	results_panel.add(sr);
			        	tabbedPane.addTab("Results "+search_counter, sr);
			        	tabbedPane.setToolTipTextAt(sr.getTabID(),"Search Results "+search_counter+".");
			        	tabbedPane.setSelectedIndex(sr.getTabID());
			        	DEAapp.status_bar.stop();
			        	DEAapp.status_bar.setStatus("Result: "+ids.size()+" records found.", 0);
			        	search_keyword_button.setEnabled(true);
					}
				};
				new Thread(new R()).start();
		}
		else if(e.getSource()==geographical_search_button)
		{
			DEAapp.showInformationDialog("Geographical Search", "<html>In this version the geographical search can be performed<br>through: Browse collections > DEA database > Locations.");
		}
	}

	public void valueChanged(TreeSelectionEvent e) {
		TreePath p=e.getPath();
		DefaultMutableTreeNode n=(DefaultMutableTreeNode)p.getLastPathComponent();
		if(n.isLeaf())
		{
			DefaultMutableTreeNode parent=(DefaultMutableTreeNode) n.getParent();
			String field_name=(String)parent.getUserObject();
			for(int i=0;i<DEAapp.data.fields.length;i++)
			{
				if(field_name.compareTo(DEAapp.data.fields[i].getName())==0)
				{
					DEAapp.data.fields[i].getTextField().setSelectedItem((String)n.getUserObject());
					DEAapp.data.fields[i].setSearchField(true);
				}
				else
				{
					DEAapp.data.fields[i].getTextField().setSelectedItem("");
					DEAapp.data.fields[i].setSearchField(false);
				}
			}
			
			DEAapp.data.getSearchFields();
			
			search_browse_button.setEnabled(true);
		}
		else search_browse_button.setEnabled(false);
	}
}