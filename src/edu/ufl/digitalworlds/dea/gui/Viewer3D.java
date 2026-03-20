package edu.ufl.digitalworlds.dea.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

import javax.media.opengl.GL2;
import javax.swing.ImageIcon;

import edu.ufl.digitalworlds.dea.database.Database3DObject;
import edu.ufl.digitalworlds.dea.media.Images;
import edu.ufl.digitalworlds.opengl.OpenGLPanel;

public class Viewer3D extends OpenGLPanel
{
	private float view_rotx = 0.0f, view_roty = 0.0f;
	private float light_rotx = 10.0f, light_roty = -60.0f;
	private float trans_x=0.0f, trans_y=0.0f;
	private int prevMouseX, prevMouseY;
	
	float heightmap[][]=null;
	
	private boolean fast_drawing=false;
	
	public void setFastDrawing(boolean fast_drawing){this.fast_drawing=fast_drawing;}
	public boolean isFastDrawing(){return fast_drawing;}
	
	Database3DObject obj3D=null;
	
	public void set3DObject(Database3DObject obj3D)
	{
		if(obj3D==null && this.obj3D!=null)
		{
			this.obj3D=obj3D;
			setHeightMap(Images.no3d);
		}
		else if(obj3D!=null && obj3D!=this.obj3D)
		{
			this.obj3D=obj3D;
			setHeightMap(this.obj3D.heightmap);
		}
		else if(obj3D!=null && obj3D==this.obj3D)
		{
			if(heightmap.length!=obj3D.heightmap.getIconWidth())
				setHeightMap(this.obj3D.heightmap);
		}
	}
	
	private void setHeightMap(ImageIcon i)
	{
		int h=i.getIconHeight();
		int w=i.getIconWidth();
		Image thumbnail=i.getImage();
		BufferedImage bi=new BufferedImage(w,h,BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = bi.createGraphics();
		g.drawImage(thumbnail, 0, 0, null);
		g.dispose();	
		
		heightmap=new float[bi.getWidth()][bi.getHeight()];
		for(int x=0;x<bi.getWidth();x++)
			for(int y=0;y<bi.getHeight();y++)
			{
				heightmap[x][y]=((bi.getRGB(x, bi.getHeight()-y-1) >> 16) & 0xff)*0.00016293f;
			}
		
	}
	
	public void setup()
	{
		//System.out.println("SETUP in");
		GL2 gl=getGL2();
		float pos[] = { 5.0f, 20.0f, 10.0f, 0.0f };
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, pos, 0);
		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_NORMALIZE);	    
		gl.glDepthFunc(GL2.GL_LEQUAL);              
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
		gl.glShadeModel(GL2.GL_SMOOTH);
		//System.out.println("SETUP out");
	}
	
	private boolean slow_step2=false;
	
	public void constructor()
	{
		//System.out.println("Constructor in");
	}
	
	public void destructor()
	{
		//System.out.println("Destructror in");
	}
	
	public void draw() 
	{
		//if(DEAapp.viewer_panel.tabbedPane.getSelectedIndex()!=0) return;
 		
		//System.out.println("DRAW in");
		
		set3DObject(DEAapp.data.current_record.obj3D);
		
		GL2 gl=getGL2();
		
		float pos[] = { 0.0f, 0.0f, 1.0f, 0.0f };
    	
    	float pos2[]= { 0.0f, 0.0f, 0.0f, 0.0f };
    	
    	pos2[0]=pos[0];
    	pos2[1]=(float)(Math.cos(light_rotx*3.1416/180.0)*pos[1]+Math.sin(light_rotx*3.1416/180.0)*pos[2]);
    	pos2[2]=(float)(-Math.sin(light_rotx*3.1416/180.0)*pos[1]+Math.cos(light_rotx*3.1416/180.0)*pos[2]);
    	
    	pos[0]=pos2[0];pos[1]=pos2[1];pos[2]=pos2[2];
    	
    	pos2[1]=pos[1];
    	pos2[0]=(float)(Math.cos(light_roty*3.1416/180.0)*pos[0]+Math.sin(light_roty*3.1416/180.0)*pos[2]);
    	pos2[2]=(float)(-Math.sin(light_roty*3.1416/180.0)*pos[0]+Math.cos(light_roty*3.1416/180.0)*pos[2]);
    	
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, pos2, 0);
    	

		gl.glTranslated(0,0,-3);
    	
		double sc=(1.0+DEAapp.viewer_panel.zoom_slider.getValue()/100.0);
		
		gl.glScaled(sc, sc, sc);
		
    	if(DEAapp.viewer_panel.light_button.isSelected())
    	{
    		if(isMouseButtonPressed(1)||isMouseButtonPressed(2)||isMouseButtonPressed(3))
    		{
    			gl.glDisable(GL2.GL_LIGHTING);
    			gl.glLineWidth(2);
    			gl.glBegin(GL2.GL_LINES);
    			
    			for(int x=-3;x<=3;x++)
    				for(int y=-3;y<=3;y++)
    				{
    					gl.glColor3d(1, 1, 0);
    					gl.glVertex3d(pos2[0]+x*0.3,pos2[1]+y*0.3,pos2[2]);
    					gl.glColor3d(0, 0, 0);
    					gl.glVertex3d(-pos2[0]+x*0.3,-pos2[1]+y*0.3,-pos2[2]);
    				}
    			gl.glEnd();
    			gl.glEnable(GL2.GL_LIGHTING);
    		}
    	}
    	
		
	    gl.glRotated(view_rotx, 1.0, 0.0, 0.0);
	    gl.glRotated(view_roty, 0.0, 1.0, 0.0);
	    //gl.glRotated(view_rotz, 0.0, 0.0, 1.0);
	    gl.glTranslated(0,0,3);   
	
	    gl.glTranslated(trans_x,trans_y,-3);
		
	    if(heightmap!=null)
	    {
	    	//System.out.println("DRAW heightmap!=null");
	//    		list_created=gl.glGenLists(1);
	  //  		gl.glNewList(list_created, GL2.GL_COMPILE_AND_EXECUTE);
	    		gl.glBegin(GL2.GL_QUADS);
	    		gl.glColor3d(1, 1, 1);
		
	    		
				int w=heightmap.length-1;
				int h=heightmap[0].length-1;
				double xx,xx_,yy,yy_;
				if((isFastDrawing()==true)||(isFastDrawing()==false && slow_step2==false))
				{
					//System.out.println("DRAW fast");
					if(isFastDrawing()==false) slow_step2=true;
					
					int stp=3;
					double xx__,yy__;
					for(int x=0;x<w-stp;x+=stp)
					{
						for(int y=0;y<h-stp;y+=stp)
						{
							xx=(x*2.0)/h-1.0;
							xx_=((x+stp)*2.0)/h-1.0;
							xx__=((x+1)*2.0)/h-1.0;
							yy=(y*2.0)/h-1.0;
							yy_=((y+stp)*2.0)/h-1.0;
							yy__=((y+1)*2.0)/h-1.0;
							gl.glNormal3d(-(heightmap[x+1][y]-heightmap[x][y])/(xx__-xx),-(heightmap[x][y+1]-heightmap[x][y])/(yy__-yy),1);
							gl.glVertex3d(xx, yy,heightmap[x][y]);
							
							gl.glNormal3d(-(heightmap[x+1+stp][y]-heightmap[x+stp][y])/(xx__-xx),-(heightmap[x+stp][y+1]-heightmap[x+stp][y])/(yy__-yy),1);
							gl.glVertex3d(xx_, yy,heightmap[x+stp][y]);
							
							gl.glNormal3d(-(heightmap[x+1+stp][y+stp]-heightmap[x+stp][y+stp])/(xx__-xx),-(heightmap[x+stp][y+1+stp]-heightmap[x+stp][y+stp])/(yy__-yy),1);
							gl.glVertex3d(xx_, yy_,heightmap[x+stp][y+stp]);
							
							gl.glNormal3d(-(heightmap[x+1][y+stp]-heightmap[x][y+stp])/(xx__-xx),-(heightmap[x][y+1+stp]-heightmap[x][y+stp])/(yy__-yy),1);
							gl.glVertex3d(xx, yy_,heightmap[x][y+stp]);
							
							//sides
							if(x==0)
							{
								
							}
						}
					}
				}
				else
				{
					//System.out.println("DRAW slow");
					slow_step2=false;
					stopAnimation();
					for(int x=0;x<w-1;x+=1)
					{
						for(int y=0;y<h-1;y+=1)
						{
							xx=(x*2.0)/h-1.0;
							xx_=((x+1)*2.0)/h-1.0;
							yy=(y*2.0)/h-1.0;
							yy_=((y+1)*2.0)/h-1.0;
							gl.glNormal3d(-(heightmap[x+1][y]-heightmap[x][y])/(xx_-xx),-(heightmap[x][y+1]-heightmap[x][y])/(yy_-yy),1);
							gl.glVertex3d(xx, yy,heightmap[x][y]);
							
							gl.glNormal3d(-(heightmap[x+2][y]-heightmap[x+1][y])/(xx_-xx),-(heightmap[x+1][y+1]-heightmap[x+1][y])/(yy_-yy),1);
							gl.glVertex3d(xx_, yy,heightmap[x+1][y]);
							
							gl.glNormal3d(-(heightmap[x+2][y+1]-heightmap[x+1][y+1])/(xx_-xx),-(heightmap[x+1][y+2]-heightmap[x+1][y+1])/(yy_-yy),1);
							gl.glVertex3d(xx_, yy_,heightmap[x+1][y+1]);
							
							gl.glNormal3d(-(heightmap[x+1][y+1]-heightmap[x][y+1])/(xx_-xx),-(heightmap[x][y+2]-heightmap[x][y+1])/(yy_-yy),1);
							gl.glVertex3d(xx, yy_,heightmap[x][y+1]);
						}
					}
				}
				gl.glEnd();
				gl.glEndList();
	    
				//gl.glCallList(list_created);
	    	
		}
		if(slow_step2==true) startAnimation();
	}
	
	public void mouseDragged(int x, int y, MouseEvent e) {

	    Dimension size = e.getComponent().getSize();

	    
	    //if(isMouseButtonPressed(3))
	    if(DEAapp.viewer_panel.rotate_button.isSelected())
	    {
	    	float thetaY = 0.5f*360.0f * ( (float)(x-prevMouseX)/(float)size.width);
	    	float thetaX = 0.5f*360.0f * ( (float)(prevMouseY-y)/(float)size.height);
	    	view_rotx -= thetaX;
	    	view_roty += thetaY;		
	    }
	    else if(DEAapp.viewer_panel.light_button.isSelected())
	    {
	    	float thetaY = 0.5f*360.0f * ( (float)(x-prevMouseX)/(float)size.width);
	    	float thetaX = 0.5f*360.0f * ( (float)(prevMouseY-y)/(float)size.height);
	    	light_rotx += thetaX;
	    	light_roty += thetaY;		
	    	
	    }
	    else if(DEAapp.viewer_panel.translate_button.isSelected())
	    {
	    	float sc=(float)(1.0+DEAapp.viewer_panel.zoom_slider.getValue()/100.0);
	    	
	    	float thetaY = 2.7f * ( (float)(x-prevMouseX)/(float)size.height)/sc;
	    	float thetaX = 2.7f * ( (float)(prevMouseY-y)/(float)size.height)/sc;
	    	trans_x += thetaY;
	    	trans_y += thetaX;		
	    	
	    }
	    
	    prevMouseX = x;
	    prevMouseY = y;

	    setFastDrawing(true);
	    redraw();
	}

	public void mousePressed(int x, int y, MouseEvent e) {
		prevMouseX = x;
	    prevMouseY = y;
	}

	public void mouseReleased(int x,int y,MouseEvent e)
	{
		if(isFastDrawing())
		{
			setFastDrawing(false);
			redraw();
		}
	}
	
	public void saveWRL(String filename)
	{
		File f=new File(filename);
		if(f.exists() && !DEAapp.showConfirmDialog("Overwrite existing file?", "The selected file already exists. Do you want to overwrite existing file?")) return;
			
		try {
			
			int w=(heightmap.length-1);
			int h=(heightmap[0].length-1);
			double xx,xx_,yy,yy_;
			
			FileWriter fstream;
			fstream = new FileWriter(f);
			PrintWriter out = new PrintWriter(fstream);
			out.println("#VRML V2.0 utf8"); 
			out.println("");
			out.println("WorldInfo"); 
			out.println("{"); 
			out.println("   info  \"File exported by Digital Epigraphy Toolbox, www.digitalepigraphy.org\" ");
			out.println("}"); 
			out.println("");  
			out.println("NavigationInfo"); 
			out.println("{"); 
			out.println("   type [ \"EXAMINE\" \"ANY\" ]");
			out.println("   headlight	TRUE"); 
			out.println("   speed 1");
			out.println("}");
			out.println("");
			out.println("Group"); 
			out.println("{"); 
			out.println("children"); 
			out.println("["); 
			out.println("");
			out.println("Transform"); 
			out.println("{"); 
			out.println("translation 0 0 0"); 
			out.println("rotation	0 0 0 0"); 
			out.println("scale 1 1 1"); 
			out.println("scaleOrientation	0 0 1 0"); 
			out.println("children"); 
			out.println("["); 
			out.println("Shape");
			out.println("{");
			out.println("geometry IndexedFaceSet");
			out.println("{"); 
			out.println("convex FALSE"); 
			out.println("solid FALSE"); 
			out.println("coord Coordinate");
			out.println("{");
			out.println("point [");
			float s=100;
			float box_depth=0.2f;
			float min=10000000000f;
			float max=-min;
			float val=0;
			for(int y=0;y<h;y+=1)
			{
				for(int x=0;x<w;x+=1)
				{
					xx=(x*2.0)/h-1.0;
					yy=(y*2.0)/h-1.0;
					//out.print(""+xx*100+" "+yy*100+" "+heightmap[x][y]*100+" ");
					val=heightmap[x][y]*100*s;
					out.printf("%.4f %.4f %.4f ", xx*100,yy*100,val);
					if(min>val)min=val;
					if(max<val)max=val;
				}
			}
			//back
			for(int y=0;y<h;y+=1)
			{
				for(int x=0;x<w;x+=1)
				{
					xx=(x*2.0)/h-1.0;
					yy=(y*2.0)/h-1.0;
					out.printf("%.4f %.4f %.4f ", xx*100,yy*100,min-box_depth*(max-min));
				}
			}
			out.println("]");
			out.println("}");
			out.println("coordIndex [");
			for(int y=0;y<h-1;y+=1)
			{
				for(int x=0;x<w-1;x+=1)		
				{
					out.print((y*w+x)+" "+((y)*w+x+1)+" "+((y+1)*w+x+1)+" -1 ");
					out.print((y*w+x)+" "+((y+1)*w+x+1)+" "+((y+1)*w+x)+" -1 ");
				}
			}
			//back
			for(int y=0;y<h-1;y+=1)
			{
				for(int x=0;x<w-1;x+=1)		
				{
					out.print((w*h+y*w+x)+" "+(w*h+(y+1)*w+x+1)+" "+(w*h+(y)*w+x+1)+" -1 ");
					out.print((w*h+y*w+x)+" "+(w*h+(y+1)*w+x)+" "+(w*h+(y+1)*w+x+1)+" -1 ");
				}
			}
			//left
			for(int y=0;y<h-1;y+=1)
			{
				out.print((w*h+y*w)+" "+((y)*w)+" "+((y+1)*w)+" -1 ");
				out.print((w*h+y*w)+" "+((y+1)*w)+" "+(w*h+(y+1)*w)+" -1 ");	
			}
			//right
			for(int y=0;y<h-1;y+=1)
			{
				out.print((w*h+y*w+w-1)+" "+((y+1)*w+w-1)+" "+((y)*w+w-1)+" -1 ");
				out.print((w*h+y*w+w-1)+" "+(w*h+(y+1)*w+w-1)+" "+((y+1)*w+w-1)+" -1 ");	
			}
			//top
			for(int x=0;x<w-1;x+=1)		
			{
				out.print((w*h+(h-1)*w+x)+" "+((h-1)*w+x+1)+" "+(w*h+(h-1)*w+x+1)+" -1 ");
				out.print((w*h+(h-1)*w+x)+" "+((h-1)*w+x)+" "+((h-1)*w+x+1)+" -1 ");
			}
			//bottom
			for(int x=0;x<w-1;x+=1)		
			{
				out.print((w*h+0*w+x)+" "+(w*h+(0)*w+x+1)+" "+((0)*w+x+1)+" -1 ");
				out.print((w*h+0*w+x)+" "+((0)*w+x+1)+" "+((0)*w+x)+" -1 ");
			}
			out.println("]");
			/*out.println("normal Normal");
			out.println("{");
			out.println("vector [");
			for(int x=0;x<w-1;x+=1)
			{
				for(int y=0;y<h-1;y+=1)
				{
					xx=(x*2.0)/h-1.0;
					xx_=((x+1)*2.0)/h-1.0;
					yy=(y*2.0)/h-1.0;
					yy_=((y+1)*2.0)/h-1.0;
					out.print((-(heightmap[x+1][y]-heightmap[x][y])/(xx_-xx))+" "+(-(heightmap[x][y+1]-heightmap[x][y])/(yy_-yy))+" 1 ");
				}
			}
			out.println("]");
			out.println("}");*/
			out.println("}"); 
			out.println("appearance Appearance");
			out.println("{");
			out.println("material Material");
			out.println("{"); 
			out.println("diffuseColor 1 1 1"); 
			out.println("specularColor 0 0 0"); 
			out.println("emissiveColor 0 0 0"); 
			out.println("shininess 0"); 
			out.println("transparency 0"); 
			out.println("}"); 
			out.println("}");
			out.println("}");
			out.println("]"); 
			out.println("}"); 
			out.println("]"); 
			out.println("}"); 
		    out.close();
		} catch (IOException e) {
			e.printStackTrace();
			DEAapp.showErrorDialog("Error","The file could not be saved in this folder!");
		}
	}
	
}