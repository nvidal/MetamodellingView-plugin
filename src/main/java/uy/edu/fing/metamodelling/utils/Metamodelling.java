package uy.edu.fing.metamodelling.utils;




import java.io.Serializable;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLMetamodellingAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;




public class Metamodelling extends Object implements Serializable{
	
	private static final long serialVersionUID = 1L;
	//Logger
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(Metamodelling.class);
	
	private OWLIndividual domain;
	private OWLClassExpression range;
	private OWLOntology ontologia;
	
	
	public Metamodelling(OWLIndividual domain, OWLClass range, OWLOntology o) {
		this.domain = domain;
		this.range = range;
		this.ontologia = o;
	}
	
	public Metamodelling(OWLMetamodellingAxiom axiom, OWLOntology o) {
		this.domain = axiom.getMetamodelIndividual();
		this.range = axiom.getModelClass();
		this.ontologia = o;
	}
	
	public void save() {
		OWLOntologyManager manager = FactoryOWLManager.getOWLManager().getOWLOntologyManager();
    	OWLDataFactory factory = manager.getOWLDataFactory();
    	//Creo la relacion de Meta-modeling
    	OWLAxiom axiom = factory.getOWLMetamodellingAxiom(range, domain);
    	//Agrego la relacion a la red y aplico los cambios
        AddAxiom addAxiom = new AddAxiom(this.ontologia, axiom);
        manager.applyChange(addAxiom);
	}
	
	public void delete() {	
		OWLOntologyManager manager = FactoryOWLManager.getOWLManager().getOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		//Obtengo la relacion
    	OWLAxiom axiom = factory.getOWLMetamodellingAxiom(range, domain);
    	//Elimino el metamodeling de la red
    	manager.removeAxiom(this.ontologia, axiom);
	}
	
	@Override
	public String toString(){
			return "Metamodeling: ["+ this.getNombreDomain() +" is metamodel of: " + this.getNombreRange()+"]";
	}
	
	public String getNombreDomain(){
		String nombre = this.getDomain().toString();
		int index = nombre.lastIndexOf("#");
		if (index <0)
			index =0;
		return nombre.substring(index+1, nombre.length()-1);
//		int i_nom = aux.indexOf(this.red.getNombre());
//		if (i_nom > -1){
//			aux = aux.substring(i_nom + this.red.getNombre().length(), aux.length());
//		}
//		return aux;
	}
	public String getNombreRange(){
		String nombre = this.getRange().toString();
		int index = nombre.lastIndexOf("#");
		if (index <0)
			index =0;
		return nombre.substring(index+1, nombre.length()-1);
//		int i_nom = aux.indexOf(this.red.getNombre());
//		if (i_nom > -1){
//			aux = aux.substring(i_nom + this.red.getNombre().length(), aux.length());
//		}
//		return aux;
	}
//	@Override
//	public int getTipoRelacion(){
//		return Relacion.METAMODELING;
//	}
	public OWLIndividual getDomain() {
		return domain;
	}
	public void setDomain(OWLIndividual domain) {
		this.domain = domain;
	}
	public OWLClassExpression getRange() {
		return range;
	}
	public void setRange(OWLClass range) {
		this.range = range;
	}
	public OWLOntology getOntologia() {
		return this.ontologia;
	}
	public void setOntologia(OWLOntology o) {
		this.ontologia = o;
	}
	
	public boolean equals(Object o) {
	    if ( this == o ) 
	    	return true;
	    if ( !(o instanceof Metamodelling) ) 
	    	return false;
	    
	    Metamodelling m = (Metamodelling)o;
	    return ( this.domain == m.getDomain() &&
	    		this.range == m.getRange() );
	}

	
	public String toStringCorto() {
		return "( "+ this.getNombreDomain() +" =m " + this.getNombreRange()+" )";
	}
	
	
}
