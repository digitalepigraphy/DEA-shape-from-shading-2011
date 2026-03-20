package edu.ufl.digitalworlds.dea.utils;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class File3DFilter extends FileFilter {
 
	public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
	
    //Accept all directories and all wrl files.
	@Override
	public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
 
        String extension=File3DFilter.getExtension(f);
        if (extension != null) {
            if (extension.compareTo("wrl")==0) {
                    return true;
            } else {
                return false;
            }
        }
 
        return false;
    }
 
    //The description of this filter
    public String getDescription() {
        return "VRML files (.wrl)";
    }


}
