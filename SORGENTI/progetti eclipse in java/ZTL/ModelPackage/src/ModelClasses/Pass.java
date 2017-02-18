package ModelClasses;

import java.text.SimpleDateFormat;
import java.util.Vector;

public class Pass implements SibSerializable {

	private long deadline;
	private long ticket;
	private String type;

	public Pass() {
		super();
	}

	public Pass(long deadline, long ticket, String type) {
		super();
		this.deadline = deadline;
		this.ticket = ticket;
		this.type = type;
	}

	@Override
	public String toString() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return "Pass [deadline=" + sdfDate.format(deadline) + ", ticket=" + ticket + ", type=" + type + "]";
	}

	public long getDeadline() {
		return deadline;
	}

	public void setDeadline(long deadline) {
		this.deadline = deadline;
	}

	public long getTicket() {
		return ticket;
	}

	public void setTicket(long ticket) {
		this.ticket = ticket;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Vector<Vector<String>> toTriple(){
		Vector<Vector<String>> triples = new Vector<Vector<String>>();
		triples.add(OntologyProvider.generateClassTriple("Pass_"+getTicket(), "rdf:type", "Pass"));
		triples.add(OntologyProvider.generateTriple("Pass_"+getTicket(), "hasTicket", ""+getTicket(), "literal"));
		triples.add(OntologyProvider.generateTriple("Pass_"+getTicket(), "hasType", getType(), "literal"));
		triples.add(OntologyProvider.generateTriple("Pass_"+getTicket(), "hasDeadline", ""+getDeadline(), "literal"));
		
		
		return triples;
	}
}
