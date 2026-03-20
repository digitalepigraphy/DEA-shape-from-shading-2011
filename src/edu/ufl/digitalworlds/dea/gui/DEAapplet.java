package edu.ufl.digitalworlds.dea.gui;

import java.io.IOException;

import edu.ufl.digitalworlds.*;
import edu.ufl.digitalworlds.dea.media.ResourceRetriever;
import edu.ufl.digitalworlds.gui.DWApplet;

public class DEAapplet extends DWApplet
{
	public void init()
	{
		createMainFrame("Digital Epigraphy and Archaeology Project");
    	DEAapp.app=new DEAapp();
    	setFrameSize(730,570,ResourceRetriever.fromFile("data/DEAicon_32.png"));
	}
}