package uy.edu.fing.metamodelling.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.protege.editor.owl.model.event.OWLModelManagerChangeEvent;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLMetamodellingAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyChangeListener;

import uy.edu.fing.metamodelling.utils.FactoryOWLManager;
import uy.edu.fing.metamodelling.utils.Metamodelling;
import uy.edu.fing.metamodelling.utils.RelacionTable;
import uy.edu.fing.metamodelling.utils.WrapperOWLClass;
import uy.edu.fing.metamodelling.utils.WrapperOWLIndividual;

public class MetamodellingViewComponent extends AbstractOWLViewComponent{
	private static final long serialVersionUID = -4515710047558710080L;
	private static final Logger log = Logger.getLogger(MetamodellingViewComponent.class);

	private JPanel panel;

	private JLabel jlabel_titulo;

	private JLabel jlabel_origen;

	private JComboBox<WrapperOWLIndividual> jcombo_origen;

	private JLabel jlabel_destino;

	private JComboBox<WrapperOWLClass> jcombo_destino;

	private JButton jbutton_crear;

	private RelacionTable jtables;
	
	private List<Metamodelling> axiomas;
	
	@Override
	protected void initialiseOWLView() throws Exception {	
		// Cargo el OWLModelManager para que se accesible por el resto de las clases.
		new FactoryOWLManager(getOWLModelManager());
		//Agrego el Listener de eventos.
		getOWLModelManager().addListener(owlModelManagerListener);
		getOWLModelManager().getOWLOntologyManager().addOntologyChangeListener(changeListener);
//		getOWLModelManager().getOWLOntologyManager().addOntologyLoaderListener(loaderListener);
		axiomas = new ArrayList<Metamodelling>();
		
		setLayout(null);
		{
			//setVisible(true);
			createPanel();
			add(panel);	
			panel.setVisible(true);
		}
		
		refreshListClasses();
		refreshListIndividuals();
		refreshAxioms();
	}

	@Override
	protected void disposeOWLView() {
		getOWLModelManager().removeListener(owlModelManagerListener);
//		getOWLModelManager().getOWLOntologyManager().removeOntologyLoaderListener(loaderListener);
		getOWLModelManager().getOWLOntologyManager().removeOntologyChangeListener(changeListener);
		log.info("[Disposed Example View]");
	}

	
	public void createPanel(){
		panel = new JPanel();
		panel.setLayout( null );
		panel.setBounds(0, 0, 900, 540);
		panel.setVisible(true);
		{
			jlabel_titulo = new JLabel("Metamodeling View");
			jlabel_titulo.setBounds(350, 5, 200, 20);
			jlabel_titulo.setFont(new Font("Lucida Grande", Font.BOLD, 16));
			panel.add(jlabel_titulo);

			jlabel_origen = new JLabel("Individual");
			jlabel_origen.setBounds(80, 50, 100, 20);
			panel.add(jlabel_origen);

			jcombo_origen = new JComboBox<WrapperOWLIndividual>();
			jcombo_origen.setBounds(180, 50, 200, 20);
			panel.add(jcombo_origen);

			jlabel_destino = new JLabel("Class");
			jlabel_destino.setBounds(420, 50, 100, 20);
			panel.add(jlabel_destino);

			jcombo_destino = new JComboBox<WrapperOWLClass>();
			jcombo_destino.setBounds(520, 50,200,20);
			panel.add(jcombo_destino);

			jbutton_crear = new JButton("Create axiom");
			jbutton_crear.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					if(addMetamodelling()){
						JOptionPane.showMessageDialog(null, "Axiom was created succesfully");
						jcombo_origen.setSelectedIndex(0);
						jcombo_destino.setSelectedIndex(0);
					}
				}
			});
			jbutton_crear.setBounds(375, 150, 150, 40);
			panel.add(jbutton_crear);
			
			
			
			//LISTA DE METAMODELLINGS
			jtables = new RelacionTable("Metamodeling axioms");
			jtables.setBounds(225, 275, 450, 150);
			panel.add(jtables);
			//++++++++++++++++
		}
	}
	
	
	public boolean addMetamodelling(){
		try{
			if(jcombo_origen.getSelectedIndex()==-1){
				JOptionPane.showMessageDialog(null, "Select an individual as the domain of the axiom");
				return false;
			}
			if(jcombo_destino.getSelectedIndex()==-1){
				JOptionPane.showMessageDialog(null, "Select a class for the range of the axiom");
				return false;
			}

			WrapperOWLIndividual domain = (WrapperOWLIndividual) jcombo_origen.getSelectedItem();
			WrapperOWLClass range = (WrapperOWLClass) jcombo_destino.getSelectedItem();

			OWLOntology onto = getOWLModelManager().getActiveOntology();
			Metamodelling m = new Metamodelling(domain.getIndividual(), range.getClase(), onto);
			
			if (this.axiomas.contains(m)){
				JOptionPane.showMessageDialog(null, "The axiom already exists");
				return false;
			}
			
			m.save();
			this.axiomas.add(m);
			
			//+++++
			
			
			//Agrego la relacion a la lista
			//jtables.addData(m);
			return true;

		}catch(Exception ex){
			log.error(ex.getMessage());
			JOptionPane.showMessageDialog(null, "Error: "+ ex.toString());
			return false;
		}
	}

	
	private OWLModelManagerListener owlModelManagerListener = new OWLModelManagerListener() {
		public void handleChange(OWLModelManagerChangeEvent event) {
			if (getOWLModelManager().getActiveOntology()!=null){
				refreshListClasses();
				refreshListIndividuals();
				refreshAxioms();
			}
		}
	};
	
	private OWLOntologyChangeListener changeListener = new OWLOntologyChangeListener(){

		@Override
		public void ontologiesChanged(List<? extends OWLOntologyChange> arg0)
				throws OWLException {
			refreshListClasses();
			refreshListIndividuals();
			refreshAxioms();
		}
		
	};
	
//	private OWLOntologyLoaderListener loaderListener = new OWLOntologyLoaderListener(){
//
//		@Override
//		public void finishedLoadingOntology(LoadingFinishedEvent arg0) {
//			// TODO Auto-generated method stub
//			log.error("FINISSSH");
//		}
//
//		@Override
//		public void startedLoadingOntology(LoadingStartedEvent arg0) {
//			log.error("SRTARRRR");
//			refreshListClasses();
//			refreshListIndividuals();
//			refreshAxioms();
//			
//		}
//		
//	};
	
	/**
	 * Actualiza las listas de Clases de la distintas tabs.
	 */
	public void refreshListClasses(){
		Map<String, WrapperOWLClass> map = new HashMap<String, WrapperOWLClass>();
		for (OWLOntology o : getOWLModelManager().getActiveOntologies()){
			//if(o != getOWLModelManager().getActiveOntology()){
				for (OWLClass c : o.getClassesInSignature()){
					if(!map.containsKey(c.toString()))
						map.put(c.toString(), new WrapperOWLClass(c, o));
				}
			//}
		}
		// List Metamodelling
		jcombo_destino.removeAllItems();
		for (WrapperOWLClass w : map.values()){
			jcombo_destino.addItem(w);
		}
	}

	
	/**
	 * Actualiza las listas de Individuals
	 */
	public void refreshListIndividuals(){
		Map<String, WrapperOWLIndividual> individuals = new HashMap<String, WrapperOWLIndividual>();
		for (OWLOntology o : getOWLModelManager().getActiveOntologies()){
			for (OWLIndividual i : o.getIndividualsInSignature()){
				if(!individuals.containsKey(i.toString()))
					individuals.put(i.toString(), new WrapperOWLIndividual(i, o));
			}
		}
		jcombo_origen.removeAllItems();
		for (WrapperOWLIndividual i: individuals.values()){
			jcombo_origen.addItem(i);
		}
	}
	
	public void refreshAxioms(){
		OWLOntology o = getOWLModelManager().getActiveOntology();
		if (o== null)
			return;
		
		this.axiomas.clear();
		for (OWLMetamodellingAxiom ma: o.getAxioms(AxiomType.METAMODELLING)){
			Metamodelling m = new Metamodelling(ma.getMetamodelIndividual(), ma.getModelClass().asOWLClass(), o);
			this.axiomas.add(m);
			
		}
		
		jtables.addData(new ArrayList<Metamodelling>(this.axiomas));
//		jtables.repaint();
//		panel.repaint();
//		this.repaint();
	}
}
