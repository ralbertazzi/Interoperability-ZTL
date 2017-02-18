package ModelClasses;

import java.util.Vector;

public class OntologyProvider {
	public static String toTripleFormat(String param){
		return "http://ztl#"+param;
	}
	
	public static Vector<String> generateTriple(String id, String property, String value, String objectType){
		Vector<String> t = new Vector<String>();
		t.add(OntologyProvider.toTripleFormat(id));
		t.add(OntologyProvider.toTripleFormat(property));
		if(objectType.equals("uri"))
			t.add(OntologyProvider.toTripleFormat(value));
		else t.add(value);
		t.add("uri"); //subject is always an uri
		t.add(objectType); //"uri" or "literal"
		return t;
	}
	public static Vector<String> generateClassTriple(String id, String property, String value){
		Vector<String> t = new Vector<String>();
		t.add(OntologyProvider.toTripleFormat(id));
		t.add(property);
		t.add(OntologyProvider.toTripleFormat(value));
		t.add("uri"); //subject is always an uri
		t.add("uri"); //"uri" or "literal"
		return t;
	}
}
