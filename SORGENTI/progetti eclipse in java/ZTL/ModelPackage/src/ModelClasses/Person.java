package ModelClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Person implements SibSerializable {

	// private String personInstanceName;
	private String fiscalCode; // uri of Person
	private String name;
	private String surname;
	private String address;
	private String email;
	private List<Vehicle> vehicles = new ArrayList<Vehicle>();

	public Person(String fiscalCode, String name, String surname, String address, String email,
			Vehicle vehicle) {

		this.fiscalCode = fiscalCode;
		this.name = name;
		this.surname = surname;
		this.address = address;
		this.email = email;
		this.vehicles.add(vehicle);
	}

	public Person() {
		super();
	}

	public String getFiscalCode() {
		return fiscalCode;
	}

	public void setFiscalCode(String fiscalCode) {
		this.fiscalCode = fiscalCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<Vehicle> vehicles) {
		if (vehicles != null)
			this.vehicles = vehicles;
	}

	public void addVehicle(Vehicle v) {
		if (v != null)
			this.vehicles.add(v);
	}

	@Override
	public String toString() {
		return "Person [fiscalCode=" + fiscalCode + ", name=" + name + ", surname=" + surname + ", address=" + address
				+ ", email=" + email + ", vehicles=" + vehicles + "]";
	}

	public Vector<Vector<String>> toTriple() {
		Vector<Vector<String>> triples = new Vector<Vector<String>>();
		triples.add(OntologyProvider.generateClassTriple("Person_" + getFiscalCode(), "rdf:type", "Person"));
		triples.add(OntologyProvider.generateTriple("Person_" + getFiscalCode(), "hasFiscalCode", getFiscalCode(),
				"literal"));
		triples.add(OntologyProvider.generateTriple("Person_" + getFiscalCode(), "hasName", getName(), "literal"));
		triples.add(
				OntologyProvider.generateTriple("Person_" + getFiscalCode(), "hasSurname", getSurname(), "literal"));
		triples.add(
				OntologyProvider.generateTriple("Person_" + getFiscalCode(), "hasAddress", getAddress(), "literal"));
		triples.add(OntologyProvider.generateTriple("Person_" + getFiscalCode(), "hasEmail", getEmail(), "literal"));
		if (!getVehicles().isEmpty()) {
			for (Vehicle v : getVehicles()) {
				if (v != null)
					triples.add(OntologyProvider.generateTriple("Person_" + getFiscalCode(), "hasVehicle",
							"Vehicle_" + v.getPlate(), "uri"));
			}
		}

		return triples;
	}

}
