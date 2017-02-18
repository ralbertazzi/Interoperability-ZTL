package ModelClasses;

import java.util.Vector;

public class Fine implements SibSerializable {
	private int id; //literal, ID
	private int amount; //literal
	private String description; //literal
	private Vehicle targetVehicle = null; 
	private Person targetPerson = null; 
	private SensorData targetSensorData = null; 
	
	public Fine(int id, int amount, String description, Vehicle targetVehicle, Person targetPerson,
			SensorData targetSensorData) {
		this.id = id;
		this.amount = amount;
		this.description = description;
		this.targetVehicle = targetVehicle;
		this.targetPerson = targetPerson;
		this.targetSensorData = targetSensorData;
	}
	
	

	public Fine() {
		super();
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Vehicle getTargetVehicle() {
		return targetVehicle;
	}

	public void setTargetVehicle(Vehicle targetVehicle) {
		this.targetVehicle = targetVehicle;
	}

	public Person getTargetPerson() {
		return targetPerson;
	}

	public void setTargetPerson(Person targetPerson) {
		this.targetPerson = targetPerson;
	}

	public SensorData getTargetSensorData() {
		return targetSensorData;
	}

	public void setTargetSensorData(SensorData targetSensorData) {
		this.targetSensorData = targetSensorData;
	}

	@Override
	public String toString() {
		return "Fine [id=" + id + ", amount=" + amount + ", description=" + description + ", targetVehicle="
				+ targetVehicle + ", targetPerson=" + targetPerson + ", targetSensorData=" + targetSensorData + "]";
	}
	
	public Vector<Vector<String>> toTriple(){
		Vector<Vector<String>> triples = new Vector<Vector<String>>();
		triples.add(OntologyProvider.generateClassTriple("Fine_"+getId(), "rdf:type", "Fine"));
		triples.add(OntologyProvider.generateTriple("Fine_"+getId(), "fineHasId", ""+getId(), "literal"));
		triples.add(OntologyProvider.generateTriple("Fine_"+getId(), "hasDescription", getDescription(), "literal"));
		triples.add(OntologyProvider.generateTriple("Fine_"+getId(), "hasAmount", ""+getAmount(), "literal"));
		if (getTargetSensorData() != null)
			triples.add(OntologyProvider.generateTriple("Fine_"+getId(), "hasTargetSensor", "Sensor_"+getTargetSensorData().getId(), "uri"));
		if(getTargetVehicle()!=null)
			triples.add(OntologyProvider.generateTriple("Fine_"+getId(), "hasTargetVehicle", "Vehicle_"+getTargetVehicle().getPlate(), "uri"));
		if(getTargetPerson()!=null)
			triples.add(OntologyProvider.generateTriple("Fine_"+getId(), "hasTargetPerson", "Person_"+getTargetPerson().getFiscalCode(), "uri"));

		
		return triples;
	}
	
}
