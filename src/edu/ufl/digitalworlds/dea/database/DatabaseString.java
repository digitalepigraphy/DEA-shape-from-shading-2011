package edu.ufl.digitalworlds.dea.database;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import edu.ufl.digitalworlds.dea.gui.DEAapp;

public class DatabaseString implements ActionListener
{
	private JTextField original_value;
	private int field_id;
	//private JComboBox text_field;
	private JButton remove_button;
	private DatabaseRecord father;
	private boolean deleted_by_user;
	
	
	public DatabaseString(DatabaseRecord father)
	{
		String doc[]={"a","b","c"};
		//text_field=new JComboBox(doc);
		//text_field.setEditable(true);
	original_value=new JTextField("");
	original_value.setEditable(false);
	remove_button=new JButton("x");
	remove_button.setToolTipText("Remove this field.");
	remove_button.addActionListener(this);
	this.father=father;
	//remove_button.addActionListener(this);
	}
	
	public void setDeletedByUser(boolean deleted_by_user){this.deleted_by_user=deleted_by_user;}
	public boolean getDeletedByUser(){return deleted_by_user;}
	
	public int getFieldID(){return field_id;}
	public void setFieldID(int field_id){this.field_id=field_id;}
	public String getOriginalValue(){return original_value.getText();}
	public void setOriginalValue(String original_value){this.original_value.setText(original_value);}
	
	public JComboBox getComboBox()
	{
		int fid=DEAapp.data.getFieldIndex(field_id);
		if(fid!=-1) return DEAapp.data.fields[fid].getRecordTextField();
		else return null;
	}
	public JTextField getTextBox(){return original_value;}
	
	public JButton getRemoveButton(){return remove_button;}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==remove_button)
		{
			int vid=father.getValueIndex(field_id);
			if(vid!=-1)
			{
				if(original_value.getText().length()>0) 
				{
					setDeletedByUser(true);
					getComboBox().setSelectedItem("");
				}
				else father.text_values.remove(vid);
				father.getMainPanel();
			}
		}
	}
	
}