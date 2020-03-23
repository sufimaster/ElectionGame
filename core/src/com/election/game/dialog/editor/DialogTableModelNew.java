package com.election.game.dialog.editor;

import java.util.Iterator;
import java.util.Map;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import com.election.game.dialog.DialogModel;

public class DialogTableModelNew extends AbstractTableModel {

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
	
    public DialogTableModelNew(Map<String, DialogModel> dialogLines) {
    	super();
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
    	if(dialogLines == null)
    		return 0;
    	
    	return this.dialogLines.size();
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
    
    private int getRowNumber(String dialogKey) {
    	Iterator<String> itr = dialogLines.keySet().iterator();
    	int idx=0;
    	
    	while(itr.hasNext()) {
    		String key = itr.next();
    		if(dialogKey.equals(key)) {
    			return idx;
    		}
    		idx++;
    	}
    	
    	return -1;
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
    
    public void addDialog(DialogModel model) {
    	insertPerson(getRowCount(), model);
    }
    
    public void insertPerson(int row, DialogModel model) {
    	
    	String key = model.getId();
    	dialogLines.put(key, model);
        fireTableRowsInserted(row, row);
    }
    
    public void removePerson(	String key) {
    	dialogLines.remove(key);
    	int row = getRowNumber(key);
    	fireTableRowsDeleted(row, row);
    }
}
