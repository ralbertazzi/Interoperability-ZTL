package ModelClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Vehicle implements SibSerializable {
	private String plate;
	private String category;
	private List<Pass> passes = new ArrayList<Pass>();
	
	public Vehicle(String plate, String category, Pass pass) {
		this.plate = plate;
		this.category = category;
		this.passes.add(pass);
	}

	public Vehicle() {
		super();
	}

	public String getPlate() {
		return plate;
	}
	public void setPlate(String plate) {
		this.plate = plate;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public List<Pass> getPasses() {
		return passes;
	}
	public void setPasses(List<Pass> passes) {
		if(passes!=null)
			this.passes = passes;
	}
	public void addPass(Pass p){
		if(p!=null)
			this.passes.add(p);
	}
	
	@Override
	public String toString() {
		return "Vehicle [plate=" + plate + ", category=" + category + ", passes=" + passes + "]";
	}
	public Vector<Vector<String>> toTriple(){
		Vector<Vector<String>> triples = new Vector<Vector<String>>();
		triples.add(OntologyProvider.generateClassTriple("Vehicle_"+getPlate(), "rdf:type", "Vehicle"));
		triples.add(OntologyProvider.generateTriple("Vehicle_"+getPlate(), "hasPlate", getPlate(), "literal"));
		triples.add(OntologyProvider.generateTriple("Vehicle_"+getPlate(), "hasCategory", getCategory(), "literal"));
		if(!getPasses().isEmpty()){
			for(Pass p:getPasses()){
				if(p!=null)
					triples.add(OntologyProvider.generateTriple("Vehicle_"+getPlate(), "hasPass", ""+"Pass_"+p.getTicket(), "uri"));

			}
		}
		
		return triples;
	}
}
