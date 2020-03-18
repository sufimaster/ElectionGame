package com.election.game.dialog.editor;

import java.util.Iterator;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.election.game.dialog.DialogModel;

public class DialogTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2036151075658929072L;

	private final Map<String, DialogModel> dialogLines;

	private final String[] columnNames = new String[] {
            "dialogId", "dialogValue"
    };
    private final Class[] columnClass = new Class[] {
        String.class, String.class
    };
	
    public DialogTableModel(Map<String, DialogModel> dialogLines) {
    	this.dialogLines = dialogLines;
    }
	
    @Override
    public String getColumnName(int column)
    {
        return columnNames[column];
    }
 
    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return columnClass[columnIndex];
    }
 
    @Override
    public int getColumnCount()
    {
        return columnNames.length;
    }
 
    @Override
    public int getRowCount()
    {
        return dialogLines.size();
    }
    
    private DialogModel getDialogLine(int rowIndex) {
    	Iterator<String> itr = dialogLines.keySet().iterator();
    	int idx=0;
    	
    	while(itr.hasNext()) {
    		String key = itr.next();
    		if(idx == rowIndex) {
    			return dialogLines.get(key);
    		}
    		idx++;
    	}
    	
    	return null;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
    	
    	
    	
        DialogModel row = getDialogLine(rowIndex);//dialogLines.get("C" + rowIndex);
        
        if( row == null) {
        	
        	return null;
        }
        
        if(0 == columnIndex) {
            return row.getId();
        }
        else if(1 == columnIndex) {
            return row.getValue();
        }

        return null;
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
    	return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    	DialogModel row = getDialogLine(rowIndex);
    	
    	if(row == null) {
    		return;
    	}
    	
    	if( 0== columnIndex) {
    		row.setId((String) aValue);
    	}
    	else if(1==columnIndex) {
    		row.setValue((String)aValue);
    	}
    	
    }
}
