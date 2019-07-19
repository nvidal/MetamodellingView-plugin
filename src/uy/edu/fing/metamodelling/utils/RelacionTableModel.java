package uy.edu.fing.metamodelling.utils;

import java.util.List;

import javax.swing.table.DefaultTableModel;

public class RelacionTableModel extends DefaultTableModel{
	
	private static final long serialVersionUID = 1L;

	public void addRow(Metamodelling r){
		if (r!=null){
			Object [] fila = new Object[1];
			fila[0] = r.toStringCorto();
			addRow(fila);
		}
	}
	
	public void addRows(List<Metamodelling> rels){
		if (rels != null){
			for (Metamodelling r: rels){
				addRow(r);
			}
		}
	}
	
	public void clear(){
		setRowCount(0);
	}
	

}
