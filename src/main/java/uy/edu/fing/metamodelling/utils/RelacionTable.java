package uy.edu.fing.metamodelling.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;


public class RelacionTable extends JPanel{

	private static final long serialVersionUID = 1L;
	private	JTable		table;
	private	JScrollPane scrollPane;
	private RelacionTableModel modelo;
	private List<Metamodelling> relacionesEnModelo;

	public void setText(String text){
		//modelo.
	}
	public RelacionTable(String titulo){

		relacionesEnModelo = new ArrayList<Metamodelling>();
		modelo = new RelacionTableModel();
		modelo.addColumn(titulo);

		setLayout( new BorderLayout() );

		table = new JTable( modelo );
		table.addMouseListener(new MouseAdapter() 
		{
			public void mouseClicked(MouseEvent e) 
			{
				int fila = table.rowAtPoint(e.getPoint());
				int columna = table.columnAtPoint(e.getPoint());
				// No elimino las relaciones tipo EXTENSION
				if ((fila > -1) && (columna > -1)){
					String mensaje, titulo;
					mensaje = "Do you want to delete relation " + relacionesEnModelo.get(fila).toStringCorto()+"?";
					titulo = "Delete relation";
					int result = JOptionPane.showConfirmDialog(null, mensaje, titulo, JOptionPane.OK_CANCEL_OPTION);
					if(result==0){

						removeData(fila);
					}
				}
			}
		});

		table.setShowHorizontalLines( false );
		table.setRowSelectionAllowed( true );
		table.setColumnSelectionAllowed( true );

		table.setAlignmentY(CENTER_ALIGNMENT);

		table.setSelectionForeground( Color.white );
		table.setSelectionBackground( Color.gray );

		scrollPane = new JScrollPane(table);
		add( scrollPane, BorderLayout.CENTER );
	}

	/**
	 * Limpia toda la tabla y agrega las nuevas relaciones
	 * @param relaciones
	 */
	public void addData(List<Metamodelling> relaciones)
	{
		if(relacionesEnModelo==null){
			relacionesEnModelo = new ArrayList<Metamodelling>();
		}
		relacionesEnModelo.clear();
		relacionesEnModelo= relaciones;

		modelo.clear();
		modelo.addRows(relaciones);
	}
	/**
	 * Agrega la relacion al modelo y a la lista de relaciones.
	 * @param r
	 */
	public void addData(Metamodelling r){
		relacionesEnModelo.add(r);
		modelo.addRow(r);
	}

	public void removeData(int index){
		Metamodelling r = relacionesEnModelo.get(index);
		try {
			r.delete();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return;
		}
		//relacionesEnModelo.remove(index);
		//modelo.removeRow(index);
	}


	public void removeData(Metamodelling r){
		int index = relacionesEnModelo.indexOf(r);
		try {
			r.delete();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return;
		}
		relacionesEnModelo.remove(index);
		modelo.removeRow(index);
	}

}
