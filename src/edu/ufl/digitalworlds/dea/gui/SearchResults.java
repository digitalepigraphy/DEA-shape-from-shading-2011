package edu.ufl.digitalworlds.dea.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import edu.ufl.digitalworlds.dea.database.DatabaseRecord;

class SearchResults extends JPanel implements ActionListener, ListSelectionListener
{
    JTable table;
    TableModel dataModel;
    public List<DatabaseRecord> records;
    List<Integer> matches;
    JButton close_button;
    int selected_row=-1;
    
	SearchResults(List<String> ids, List<Integer> matches)
	{
		close_button=new JButton("x");
		close_button.addActionListener(this);
		records=new ArrayList<DatabaseRecord>();
		this.matches=matches;
		for(int i=0;i<ids.size();i++)
		{
			DatabaseRecord rec=DEAapp.data.getRecord(ids.get(i));
			if(rec==null)
			{
				rec=new DatabaseRecord(ids.get(i));
				DEAapp.data.global_records.add(rec);
				records.add(rec);
			}
			else
			{
				records.add(rec);
			}
		}
		setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        JPanel p=new JPanel(new BorderLayout());
        p.add(close_button,BorderLayout.EAST);
        if(ids.size()==0) p.add(new JLabel("Search Results"),BorderLayout.CENTER);
        else p.add(new JLabel("Search Results ("+ids.size()+" records found)"),BorderLayout.CENTER);
        
        add(p,BorderLayout.NORTH);
        
		if(ids.size()==0) 
		{
			JPanel p2=new JPanel(new BorderLayout());
			p2.setBackground(Color.WHITE);
			p2.add(new JLabel("No records were found to match your criteria..."), BorderLayout.NORTH);
			add(p2);
			return;
		}
		
		final String[] names = {"#", "Inscription"};

        
          
		dataModel = new AbstractTableModel() {
            public int getColumnCount() { return names.length; }
            public int getRowCount() { return records.size();}
            public Object getValueAt(int row, int col) { 
                 
            	if (col==0) {
            		return ""+(row+1);
            	} else if (col == 1) {
            		TableColumn tc=table.getColumn("Inscription");
            		if(records.get(row).getResultIcon().getIconWidth()!=tc.getWidth()) records.get(row).getResultIcon().setSize(tc.getWidth(),100);
                    return records.get(row).getResultIcon().im;
                } 
            	   else return "";
                
            }
            public String getColumnName(int col) {return names[col]; }
            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
            public boolean isCellEditable(int row, int col) {
                //return col!=0 ? false: true;
            	return false;
            }
            public void setValueAt(Object aValue, int row, int col) {
                
            	/*if(col==0)
            	{
            		fanDTasia.list_of_volumes.GetVolume(row).participate=aValue;
            	}*/
            	
            }
        };

        table = new JTable(dataModel);
        
        class myTableCellRenderer extends DefaultTableCellRenderer
        {
        	Color clr[];
        	List<Integer> matches;
        	myTableCellRenderer(List<Integer> matches)
        	{
        		this.matches=matches;
        		clr=new Color[101];
        		for(int i=0;i<101;i++)clr[i]=new Color((255*i)/100,(255*i)/100,(255*i)/100);
        	}
        	
        	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        	{
        		Component c=super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        		
        		if(isSelected==false)
        		{
        			//System.out.println(""+row+" "+matches.get(row).intValue());
        			c.setBackground(clr[matches.get(row).intValue()]);
        		}
        		return c;
        	}
        }
        
        table.setDefaultRenderer(String.class, new myTableCellRenderer(matches));
        
        table.setRowHeight(100);
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        TableColumn col = table.getColumn("Inscription");
        col.setWidth(80);
        //col.setMinWidth(80);
        //col.setMaxWidth(80);
        col = table.getColumn("#");
        col.setWidth(30);
        col.setMinWidth(30);
        col.setMaxWidth(30);
        
//       table.sizeColumnsToFit(0);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        JScrollPane scrollpane = new JScrollPane(table);
        //table.getColumnModel().getSelectionModel().addListSelectionListener(this);
        table.getSelectionModel().addListSelectionListener(this);
        //scrollpane.setLayout(new BorderLayout());
        add(scrollpane);
        silentDownloadAll();
	}
	
	private void silentDownloadAll()
	{
		class R implements Runnable
		{
			public void run() {
				for(int i=0;i<records.size();i++)
				{
					if(records.get(i).stringsStartedDownloading()==false)
					{
						records.get(i).downloadRecordFile();
						records.get(i).getResultIcon().updateImage();
						table.repaint();
					}
				}
			}
		};
		new Thread(new R()).start();
	}

	public int getTabID()
	{
		int ret=-1;
		boolean found=false;
		for(int i=0;i<DEAapp.tab_panel.tabbedPane.getComponentCount() && found==false;i++)
		{
			if(DEAapp.tab_panel.tabbedPane.getComponentAt(i).equals(this))
			{
				found=true;
				ret=i;
			}
		}
		return ret;
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==close_button)
		{
			int i=getTabID();
			if(i!=-1)
			{
				DEAapp.tab_panel.tabbedPane.remove(i);
				DEAapp.tab_panel.tabbedPane.repaint();
			}
				
		}
		
	}

	public void valueChanged(ListSelectionEvent e) {

		if (e.getSource() == table.getSelectionModel() && table.getRowSelectionAllowed() ){
	            // Row selection changed
	            int r=table.getSelectedRow();
	        	
	            if(selected_row!=r)
	            {
	            	selected_row=r;
	            	if(records.get(r).stringsDownloaded())
	            	{
	            		DEAapp.data.current_record=records.get(r);
		            	DEAapp.data.current_record.downloadImages();
		            	DEAapp.data.getRecordPanel();
		            	DEAapp.data.getImagePanel();
		            	DEAapp.viewer_panel.viewer3d.redraw();
						DEAapp.status_bar.setStatus("Record: "+records.get(r).getID());
	            	}
	            	else 
	            	{
	            		class R implements Runnable{
	            			int r;
	            			R(int r)
	            			{
	            				this.r=r;
	            			}
							public void run() {
								new Thread(DEAapp.status_bar).start();
								if(records.get(r).stringsStartedDownloading()==false)
				            	{
			            			records.get(r).downloadRecordFile();
				            		DEAapp.data.current_record=records.get(r);
				            	}
				            	else
				            	{
				            		while(records.get(r).stringsDownloaded()==false)
				            		{
				            			try {
											Thread.sleep(100);
										} catch (InterruptedException e1) {}
				            		}
				            		DEAapp.data.current_record=records.get(r);
				            	}
								DEAapp.status_bar.stop();
								DEAapp.status_bar.setStatus(0);

				            	DEAapp.data.current_record.downloadImages();
				            	DEAapp.data.getRecordPanel();
				            	DEAapp.data.getImagePanel();
				            	DEAapp.viewer_panel.viewer3d.redraw();
								DEAapp.status_bar.setStatus("Record: "+records.get(r).getID());
							}
	            		
	            		};
	            		Thread t=new Thread(new R(r));
	            		t.setPriority(Thread.MAX_PRIORITY);
	            		t.start();
	            		
	            		
	            	}
	            	
				}
	        }
	}
}