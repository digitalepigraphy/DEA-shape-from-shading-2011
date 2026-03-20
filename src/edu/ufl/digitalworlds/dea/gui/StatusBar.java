package edu.ufl.digitalworlds.dea.gui;

import java.awt.GridLayout;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class StatusBar extends JPanel implements Runnable
{
	public synchronized void setStatus(String txt)
	{
		label.setText(txt);
	}
	
	
	public synchronized void setStatus(int progress)
	{
		progress_bar.setValue(progress);
	}
	
	public synchronized void setStatus(String txt, int progress)
	{
		label.setText(txt);
		progress_bar.setValue(progress);
	}
	
	JLabel label;
	JProgressBar progress_bar;
	
	StatusBar()
	{
		label=new JLabel("Application launched.");
		label.setToolTipText("This area displays status messages.");
		
		progress_bar=new JProgressBar();
		progress_bar.setMaximum(100);
        progress_bar.setValue(0);
        progress_bar.setToolTipText("This bar shows the progress of the current process.");
        
        setLayout(new GridLayout(1,2));
        add(label);
        add(progress_bar);
	}


	public void run() {
		stopped=false;
		while(stopped==false)
		{
			if(progress_bar.getValue()<30)
			{
				setStatus(progress_bar.getValue()+1);
				try{Thread.sleep(250);}catch(Exception e){}
			}
			else if(progress_bar.getValue()<50)
			{
				setStatus(progress_bar.getValue()+1);
				try{Thread.sleep(500);}catch(Exception e){}
			}
			else if(progress_bar.getValue()<65)
			{
				setStatus(progress_bar.getValue()+1);
				try{Thread.sleep(750);}catch(Exception e){}
			}
			else if(progress_bar.getValue()<75)
			{
				setStatus(progress_bar.getValue()+1);
				try{Thread.sleep(1000);}catch(Exception e){}
			}
			else if(progress_bar.getValue()<85)
			{
				setStatus(progress_bar.getValue()+1);
				try{Thread.sleep(1250);}catch(Exception e){}
			}
			else if(progress_bar.getValue()<95)
			{
				setStatus(progress_bar.getValue()+1);
				try{Thread.sleep(2000);}catch(Exception e){}
			}
			else if(progress_bar.getValue()<97)
			{
				setStatus(progress_bar.getValue()+1);
				try{Thread.sleep(4000);}catch(Exception e){}
			}
			else if(progress_bar.getValue()<99)
			{
				setStatus(progress_bar.getValue()+1);
				try{Thread.sleep(8000);}catch(Exception e){}
			}
			else stopped=true;
	
		}
	}
	
	boolean stopped=true;
	
	public void stop()
	{
		stopped=true;
	}
}