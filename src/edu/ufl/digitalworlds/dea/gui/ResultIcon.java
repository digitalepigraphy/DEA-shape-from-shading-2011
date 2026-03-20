package edu.ufl.digitalworlds.dea.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import edu.ufl.digitalworlds.dea.database.DatabaseRecord;
import edu.ufl.digitalworlds.dea.media.Images;

public class ResultIcon implements Icon {
	
	//String[] fCharStrings; // for efficiency, break the fLabel into one-char strings to be passed to drawString
	//int[] fCharWidths; // Roman characters should be centered when not rotated (Japanese fonts are monospaced)
	//int[] fPosition; // Japanese half-height characters need to be shifted when drawn vertically
	int fWidth, fHeight;//, fCharHeight, fDescent; // Cached for speed
	public ImageIcon im;
	DatabaseRecord father;
	
	public void updateImage()
	{
		BufferedImage image = new BufferedImage(getIconWidth(), getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        paintIcon(null, image.getGraphics(), 0, 0);
        im=new ImageIcon(image);
	}
	
	public void setSize(int w,int h)
	{
		fWidth=w;
		fHeight=h;
		updateImage();
	}
	
/*	public ResultIcon(Component component, String label) {
		this(component, label);
	}*/
	
 	public ResultIcon(DatabaseRecord father, int w, int h) {
		this.father=father;
 		fWidth=w;
		fHeight=h;
		//calcDimensions();
	}
	

	
	/*void recalcDimensions() {
		int wOld = getIconWidth();
		int hOld = getIconHeight();
		calcDimensions();
		//if (wOld != getIconWidth() || hOld != getIconHeight())
			//fComponent.invalidate();
	}
	
    void calcDimensions() {
    	if(fComponent==null) return;
		FontMetrics fm = fComponent.getFontMetrics(fComponent.getFont());
		fCharHeight = fm.getAscent() + fm.getDescent();
		fDescent = fm.getDescent();
		
			int len = fLabel.length();
			char data[] = new char[len];
			fLabel.getChars(0, len, data, 0);
			// if not rotated, width is that of the widest char in the string
			fWidth = 0;
			// we need an array of one-char strings for drawString
			fCharStrings = new String[len];
			fCharWidths = new int[len];
			fPosition = new int[len];
			char ch;
			for (int i = 0; i < len; i++) {
				ch = data[i];
				fCharWidths[i] = fm.charWidth(ch);
				if (fCharWidths[i] > fWidth)
					fWidth = fCharWidths[i];
				fCharStrings[i] = new String(data, i, 1);				
			}
			// and height is the font height * the char count, + one extra leading at the bottom
			fHeight = fCharHeight * len + fDescent;
				
	}*/

  
    public void paintIcon(Component c, Graphics g, int x, int y) {
		
    	
    	//g.fillRect(0, 0, getIconWidth(), getIconHeight());
    	
    	//new ImageIcon(DEAapp.DEAicon.getImage().getScaledInstance(100,100, Image.SCALE_SMOOTH)).paintIcon(c, g, 0, 0);
    	
    	if(father.images.size()==0 || father.stringsDownloaded()==false)
    		Images.noimage_icon.paintIcon(c, g, 0, 0);
    	else
    	{
    		father.images.get(0).thumbnail.paintIcon(c, g, 0, 0);
    	}
    	
    	//g.setColor(c.getForeground());
		//g.setFont(c.getFont());
    	int fs=12;
    	g.setFont(new Font("sansserif", Font.BOLD, fs));
		g.setColor(new Color(0,0,0));
		//g.drawString("DEA:", 102, fs);
		if(father.stringsDownloaded())
		{
			int j=0;
			for(int i=0;i<DEAapp.data.fields.length & j<6;i++)
			{
				int vid=father.getValueIndex(DEAapp.data.fields[i].getID());
				if(vid!=-1 && father.text_values.get(vid).getOriginalValue().length()>0)
				{
					g.drawString(DEAapp.data.fields[i].getName().substring(0, Math.min(5, DEAapp.data.fields[i].getName().length()))+":", 102, (fs+2)*(j+1)-2);
					j+=1;
				}
			}
		}
		else g.drawString("DEA:", 102, fs);
		//g.drawString("Coln:", 102, (fs+2)*2-2);
		//g.drawString("Date:", 102, (fs+2)*3-2);
		
		g.setFont(new Font("sansserif", Font.PLAIN, fs));
		//g.drawString(father.getID(), 142, fs);
		if(father.stringsDownloaded())
		{
			int j=0;
			for(int i=0;i<DEAapp.data.fields.length & j<6;i++)
			{
				int vid=father.getValueIndex(DEAapp.data.fields[i].getID());
				if(vid!=-1 && father.text_values.get(vid).getOriginalValue().length()>0)
				{
					g.drawString(father.text_values.get(vid).getOriginalValue(), 142, (fs+2)*(j+1)-2);
					j+=1;
				}
			}
		}
		else g.drawString(father.getID(), 142, fs);
		//g.drawString("Epidaurus", 135, (fs+2)*2-2);
		//g.drawString("2nd AD", 135, (fs+2)*3-2);
		
		//g.setFont(new Font("sansserif", Font.ITALIC, fs));
		//g.drawString("hhu yyf d rg", 102, (fs+2)*4-2);
		//g.drawString("fsheuonwfe oj", 102, (fs+2)*5-2);
		//g.drawString("ewf ofwenoi few", 102, (fs+2)*6-2);
		//g.drawString("...", 102, (fs+2)*7-2);
			
		/*int yPos = y + fCharHeight;
			for (int i = 0; i < fCharStrings.length; i++) {
				g.drawString(fCharStrings[i], x+((fWidth-fCharWidths[i])/2), yPos);
				yPos += fCharHeight;
			}*/
		}
    
    
    public int getIconWidth() {
		return fWidth;
	}
	
    
    public int getIconHeight() {
		return fHeight;
	}
	
}