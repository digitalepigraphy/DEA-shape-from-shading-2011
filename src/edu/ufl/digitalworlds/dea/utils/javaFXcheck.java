package edu.ufl.digitalworlds.dea.utils;

public class javaFXcheck {

	public static boolean javaFX2_2_is_available=false;
	
	public static void check()
	{
		try {
			Class.forName("javafx.embed.swing.JFXPanel");
			javaFX2_2_is_available=true;
		}
		catch (ClassNotFoundException exception) {
			javaFX2_2_is_available=false;
		}
	}
	
	public static boolean isAvailable(){return javaFX2_2_is_available;}
}
