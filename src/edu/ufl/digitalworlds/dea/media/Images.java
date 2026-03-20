package edu.ufl.digitalworlds.dea.media;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Images
{
	public static ImageIcon search_icon;
	public static ImageIcon input_icon;	
	public static ImageIcon object3d_icon;
	public static ImageIcon images_icon;
	public static ImageIcon information_icon;
	public static ImageIcon DEAicon;
	public static ImageIcon noimage_icon;
	public static ImageIcon search_collections_icon;
	public static ImageIcon search_keywords_icon;
	public static ImageIcon neh_icon;
	public static ImageIcon uf_icon;
	public static ImageIcon back_icon;
	public static ImageIcon photograph_icon;
	public static ImageIcon scanner_icon;
	public static ImageIcon icon3d_icon;
	public static ImageIcon edit_icon;
	public static ImageIcon earth_icon;
	public static ImageIcon download_icon;
	public static ImageIcon update_icon;
	public static ImageIcon zoom_icon;
	public static ImageIcon loading_icon;
	public static ImageIcon translate_icon;
	public static ImageIcon rotate_icon;
	public static ImageIcon light_icon;
	public static ImageIcon url_icon;
	public static ImageIcon analysis_icon;
	
	public static ImageIcon no3d;
	
	
	public static void loadAll()
	{
		try {
			search_icon=imageFromFile("data/search.png");
			input_icon=imageFromFile("data/input.png");
			object3d_icon=imageFromFile("data/3dobject.png");
			images_icon=imageFromFile("data/images.png");
			information_icon=imageFromFile("data/information.png");
			DEAicon=imageFromFile("data/DEAicon_64.png");
			noimage_icon=imageFromFile("data/noimage.png");
			search_collections_icon=imageFromFile("data/search_collections.png");
			search_keywords_icon=imageFromFile("data/search_keywords.png");
			neh_icon=imageFromFile("data/neh_logo.png");
			uf_icon=imageFromFile("data/uf_logo.png");
			back_icon=imageFromFile("data/back.png");
			photograph_icon=imageFromFile("data/photograph.png");
			scanner_icon=imageFromFile("data/scanner.png");
			icon3d_icon=imageFromFile("data/3dicon.png");
			edit_icon=imageFromFile("data/edit.png");
			earth_icon=imageFromFile("data/earth.png");
			download_icon=imageFromFile("data/download.png");
			update_icon=imageFromFile("data/update.png");
			zoom_icon=imageFromFile("data/zoom.png");
			loading_icon=imageFromFile("data/loading.png");
			no3d=imageFromFile("data/no3d.png");
			translate_icon=imageFromFile("data/translate.png");
			rotate_icon=imageFromFile("data/rotate.png");
			light_icon=imageFromFile("data/light.png");
			url_icon=imageFromFile("data/url.png");
			analysis_icon=imageFromFile("data/analysis.png");
			
			} catch (IOException e) {}
	}
	
	public static ImageIcon imageFromFile(String filename) throws IOException
	{
		return new ImageIcon(ImageIO.read(ResourceRetriever.fromFile(filename)));
	}
}