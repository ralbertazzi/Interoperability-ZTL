package ModelClasses;

import java.text.SimpleDateFormat;
import java.util.Vector;

public class SensorData implements SibSerializable {

	private int id;
	private long timestamp;
	private String plate;
	
	public SensorData(int id, long timestamp, String plate) {
		super();
		this.id = id;
		this.timestamp = timestamp;
		this.plate = plate;
	}
	
	public SensorData() {
		super();
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getPlate() {
		return plate;
	}
	
	public void setPlate(String plate) {
		this.plate = plate;
	}
	
	@Override
	public String toString() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return "SensorData [id=" + id + ", timestamp=" + sdfDate.format(timestamp) + ", plate=" + plate + "]";
	}

	public Vector<Vector<String>> toTriple(){
		Vector<Vector<String>> triples = new Vector<Vector<String>>();
		triples.add(OntologyProvider.generateClassTriple("Sensor_"+getTimestamp(), "rdf:type", "Sensor"));
		triples.add(OntologyProvider.generateTriple("Sensor_"+getTimestamp(), "sensorHasId", ""+getId(), "literal"));
		triples.add(OntologyProvider.generateTriple("Sensor_"+getTimestamp(), "sensorHasPlate", getPlate(), "literal"));
		triples.add(OntologyProvider.generateTriple("Sensor_"+getTimestamp(), "hasTimestamp", ""+getTimestamp(), "literal"));
		
		
		return triples;
	}
	
}
