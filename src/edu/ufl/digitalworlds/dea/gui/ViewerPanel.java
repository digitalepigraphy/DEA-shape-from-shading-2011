package edu.ufl.digitalworlds.dea.gui;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.ufl.digitalworlds.dea.media.Images;
import edu.ufl.digitalworlds.dea.utils.File3DFilter;

public class ViewerPanel extends JPanel implements ChangeListener, ActionListener
{
	public Viewer3D viewer3d;
	public JTabbedPane tabbedPane;
	JPanel viewer3d_panel;
	JSlider zoom_slider;
	JToggleButton translate_button;
	JToggleButton rotate_button;
	JToggleButton light_button;
	JButton add_3dobject_button;
	JButton download_3dobject_button;
	String most_recent_downloading_3dobject_path="";
	JButton download_info_button;
	String most_recent_downloading_info_path="";
	
	
	ViewerPanel()
	{
		setLayout(new BorderLayout());
		
		tabbedPane=new JTabbedPane(JTabbedPane.LEFT);
		
		viewer3d=new Viewer3D();
		viewer3d.stopAnimation();
		
		
		viewer3d_panel=new JPanel(new BorderLayout());
		zoom_slider=new JSlider(JSlider.HORIZONTAL,0,500,0);
		zoom_slider.setToolTipText("Zoom in/out");
		zoom_slider.addChangeListener(this);
		zoom_slider.addMouseListener(viewer3d);
		
		translate_button=new JToggleButton(Images.translate_icon);
		translate_button.setToolTipText("Move the 3D object by dragging the mouse.");
		translate_button.addActionListener(this);
		rotate_button=new JToggleButton(Images.rotate_icon);
		rotate_button.setToolTipText("Rotate the 3D object by dragging the mouse.");
		rotate_button.setSelected(true);
		rotate_button.addActionListener(this);
		light_button=new JToggleButton(Images.light_icon);
		light_button.setToolTipText("Rotate the light by dragging the mouse.");
		light_button.addActionListener(this);
		
		add_3dobject_button=new JButton("Add 3D opbjects",Images.icon3d_icon);
        add_3dobject_button.addActionListener(this);
        
        download_3dobject_button=new JButton("Download", Images.download_icon);
		download_3dobject_button.addActionListener(this);
        
		tabbedPane.addTab(null,Images.object3d_icon, get3DObjectPanel());
		tabbedPane.setToolTipTextAt(0, "View the inscription in 3D.");
		
		tabbedPane.addTab(null,Images.images_icon, DEAapp.data.getImagePanel());
		tabbedPane.setToolTipTextAt(1, "View pictures of the inscription.");
		
		tabbedPane.addTab(null,Images.information_icon, DEAapp.data.getRecordPanel());
		tabbedPane.setToolTipTextAt(2, "View information about this inscription.");
		
		add(tabbedPane,BorderLayout.CENTER);
			
	}
	
	public JPanel get3DObjectPanel()
	{
		viewer3d_panel.removeAll();
		JPanel top_panel=new JPanel(new BorderLayout());
		JPanel top_button_panel=new JPanel(new GridBagLayout());
		DEAapp.addToGridBag(top_button_panel,new JButton("Analysis", Images.analysis_icon), 0, 0, 1, 1, 1.0, 1.0);
		DEAapp.addToGridBag(top_button_panel,download_3dobject_button, 1, 0, 1, 1, 1.0, 1.0);
		DEAapp.addToGridBag(top_button_panel,add_3dobject_button, 2, 0, 1, 1, 1.0, 1.0);
		DEAapp.addToGridBag(top_button_panel, new JButton("3D from 2D scans",Images.scanner_icon), 3, 0, 1, 1, 1.0, 1.0);        
        
		
		top_panel.add(top_button_panel,BorderLayout.EAST);
		viewer3d_panel.add(top_panel,BorderLayout.NORTH);
		
		JPanel central_panel=new JPanel(new BorderLayout());
		
		JPanel main_image_panel=new JPanel(new BorderLayout());
		
		main_image_panel.add(viewer3d,BorderLayout.CENTER);
		
		JPanel low_panel=new JPanel(new GridBagLayout());
		
		JPanel sld_panel=new JPanel(new BorderLayout());
		
		sld_panel.add(zoom_slider,BorderLayout.CENTER);
		sld_panel.add(new JLabel(Images.zoom_icon),BorderLayout.WEST);
		
		DEAapp.addToGridBag(low_panel,sld_panel, 0, 0, 1, 1, 1.0, 1.0);
		DEAapp.addToGridBag(low_panel,translate_button, 1, 0, 1, 1, 1.0, 1.0);
		DEAapp.addToGridBag(low_panel,rotate_button, 2, 0, 1, 1, 1.0, 1.0);
		DEAapp.addToGridBag(low_panel,light_button, 3, 0, 1, 1, 1.0, 1.0);
		
		
		main_image_panel.add(low_panel,BorderLayout.SOUTH);
		
		central_panel.add(main_image_panel,BorderLayout.CENTER);
		
		
		viewer3d_panel.add(central_panel,BorderLayout.CENTER);
		
		return viewer3d_panel;
	}

	public void stateChanged(ChangeEvent e) {
		if(e.getSource()==zoom_slider)
		{
			viewer3d.setFastDrawing(true);
			viewer3d.redraw();
		}
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==translate_button)
		{
			if(translate_button.isSelected())
			{
				rotate_button.setSelected(false);
				light_button.setSelected(false);
			}
			else translate_button.setSelected(true);
		}
		else if(e.getSource()==rotate_button)
		{
			if(rotate_button.isSelected())
			{
				translate_button.setSelected(false);
				light_button.setSelected(false);
			}
			else rotate_button.setSelected(true);
		}
		else if(e.getSource()==light_button)
		{
			if(light_button.isSelected())
			{
				rotate_button.setSelected(false);
			    translate_button.setSelected(false);
			}
			else light_button.setSelected(true);
		}
		else if(e.getSource()==add_3dobject_button)
		{
			JFileChooser chooser = new JFileChooser();
            chooser.setFileHidingEnabled(false);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setMultiSelectionEnabled(false);
            chooser.setDialogType(JFileChooser.OPEN_DIALOG);
            if(DEAapp.most_recent_path.length()>0)
				chooser.setCurrentDirectory(new File(DEAapp.most_recent_path));
            chooser.setDialogTitle("Choose a 3D object file...");
            Object3DPreviewer previewer = new Object3DPreviewer(chooser);
	        previewer.setToolTipText("This is a quick preview of the selected 3D object.");
	        chooser.setAccessory(previewer);
	        chooser.setApproveButtonText("Upload file"); 
	        
            
            if (chooser.showOpenDialog(this)== JFileChooser.APPROVE_OPTION) 
            {  
            	DEAapp.most_recent_path=chooser.getCurrentDirectory().getAbsolutePath();
				
            	if(previewer.thumbnail==null)
            	{
            		DEAapp.showErrorDialog("3D Object Format Error", "This 3D object format is not supported.");
            	}
            	else
            	{
            		//DEAapp.data.uploadImageThread(chooser.getSelectedFile());
            	}
            }
		}
		else if(e.getSource()==download_3dobject_button)
		{
			JFileChooser chooser = new JFileChooser();
            chooser.setFileHidingEnabled(false);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setMultiSelectionEnabled(false);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.addChoosableFileFilter(new File3DFilter());
 
            chooser.setDialogType(JFileChooser.SAVE_DIALOG);
            if(most_recent_downloading_3dobject_path.length()==0)
            	most_recent_downloading_3dobject_path=DEAapp.most_recent_path;
       
            if(most_recent_downloading_3dobject_path.length()>0)
				chooser.setCurrentDirectory(new File(most_recent_downloading_3dobject_path));
            chooser.setDialogTitle("Download 3D object file...");
            chooser.setApproveButtonText("Save file"); 
	        
            if (chooser.showSaveDialog(this)== JFileChooser.APPROVE_OPTION) 
            {
            	most_recent_downloading_3dobject_path=chooser.getCurrentDirectory().getAbsolutePath();
             String extension=File3DFilter.getExtension(chooser.getSelectedFile());
             if (extension != null && extension.compareTo("wrl")==0) 
             {
            	 viewer3d.saveWRL(chooser.getSelectedFile().getPath());
             } else 
             {
                 viewer3d.saveWRL(chooser.getSelectedFile().getPath()+".wrl");
             }
             
           }
            
		}
	}
	
}