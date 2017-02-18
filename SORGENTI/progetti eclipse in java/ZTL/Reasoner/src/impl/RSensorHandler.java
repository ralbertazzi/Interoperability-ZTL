package impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import ModelClasses.Fine;
import ModelClasses.OntologyProvider;
import ModelClasses.Pass;
import ModelClasses.Person;
import ModelClasses.SensorData;
import sofia_kp.KPICore;
import sofia_kp.SIBResponse;
import sofia_kp.SSAP_sparql_response;
import sofia_kp.iKPIC_subscribeHandler2;

public class RSensorHandler implements iKPIC_subscribeHandler2 {

	private KPICore kpiCore;
	private SIBResponse resp;
	private List<Pass> passesCache;
	private int fineId;

	public RSensorHandler(KPICore kpiCore, SIBResponse resp, List<Pass> passesCache) {
		super();
		this.kpiCore = kpiCore;
		this.resp = resp;
		this.fineId = 0;
		this.passesCache = passesCache;
	}

	@Override
	public void kpic_RDFEventHandler(Vector<Vector<String>> newTriples, Vector<Vector<String>> oldTriples,
			String indSequence, String subID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void kpic_SPARQLEventHandler(SSAP_sparql_response newResults, SSAP_sparql_response oldResults,
			String indSequence, String subID) {
		// TODO Auto-generated method stub

		SensorData s = new SensorData();

		Vector<String[]> row = null;
		while (newResults.hasNext()) {
			newResults.next();
			row = newResults.getRow();
			boolean idFound = false, plateFound = false, timestampFound = false;
			for (String[] r : row) {
				if (idFound) {
					s.setId(Integer.parseInt(r[2]));
					idFound = false;
				} else if (plateFound) {
					s.setPlate(r[2]);
					plateFound = false;
				} else if (timestampFound) {
					s.setTimestamp(Long.parseLong(r[2]));
					timestampFound = false;
				}

				if (r[2].contains("sensorHasId")) {
					idFound = true;
				} else if (r[2].contains("sensorHasPlate")) {
					plateFound = true;
				} else if (r[2].contains("hasTimestamp")) {
					timestampFound = true;
				}
			}
		}

		if(isMulta(s.getPlate(),s.getTimestamp())){ //se c'� la multa
			this.fineId++;
			Fine f = new Fine();
			//String query = "SELECT DISTINCT ?s WHERE {  ?s ?p ?o . FILTER regex(str(?p), \"hasVehicle\") . FILTER ( ?o = <http://ztl#Vehicle_"+ s.getPlate() +"> )}";
			String query = ReasonerMain.prefix + "SELECT ?s "
						+ "WHERE { ?s ns:hasVehicle ns:Vehicle_"+s.getPlate()+"}";
            resp = kpiCore.querySPARQL(query);
            String pUri = "anonymous";
            if (!resp.isConfirmed())
                return;

            Vector<String[]> rowPlate = null;
            SSAP_sparql_response sqr = resp.sparqlquery_results;

            while (sqr.hasNext()) {
                sqr.next();
                rowPlate = sqr.getRow();
                for (String[] r : rowPlate) {
                    pUri=r[2];
                }
            }
			
			// insert triples
            if(pUri.equals("anonymous"))
            	f.setDescription("Unauthorized Anonymous access: "+s.getPlate());
            else
            {
            	f.setDescription("Unauthorized access");
            	pUri = pUri.replaceAll(OntologyProvider.toTripleFormat(""), "");
            }
            
            f.setId(fineId);
			f.setAmount(50);
            Vector<Vector<String>> all = f.toTriple();
            all.add(OntologyProvider.generateTriple("Fine_"+f.getId(),"hasTargetSensor", "Sensor_"+s.getTimestamp(),"uri"));
            
			if(!pUri.equals("anonymous"))
            {
	            all.add(OntologyProvider.generateTriple("Fine_"+f.getId(),"hasTargetVehicle", "Vehicle_"+s.getPlate(),"uri"));
	            all.add(OntologyProvider.generateTriple("Fine_"+f.getId(),"hasTargetPerson", pUri,"uri"));
            }
           
            
            
            resp = kpiCore.insert(all);
            if(!resp.isConfirmed()){
                return;
            }
			
		}
		

	}

	@Override
	public void kpic_UnsubscribeEventHandler(String sub_ID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void kpic_ExceptionEventHandler(Throwable SocketException) {
		// TODO Auto-generated method stub

	}
	
	private boolean isMulta(String plate, long ts){
		//String query = "SELECT DISTINCT ?o WHERE { ?s ?p ?o . FILTER regex(str(?s), \""+plate+"\") .   FILTER regex(str(?p), \"hasCategory\") . FILTER ( ?p != <rdf:type> ) }";
		String query = ReasonerMain.prefix + "SELECT ?o WHERE { ?s ns:hasPlate \""+plate+"\" . ?s ns:hasCategory ?o  }";
		resp = kpiCore.querySPARQL(query);
        String cat = "anonymous";
        if (!resp.isConfirmed())
            return false;

        Vector<String[]> rowPlate = null;
        SSAP_sparql_response sqr = resp.sparqlquery_results;

        while (sqr.hasNext()) {
            sqr.next();
            rowPlate = sqr.getRow();
            for (String[] r : rowPlate) {
                cat=r[2];
            }
        }
        if(cat.equals("anonymous"))
        	return true;
        if(!cat.equals("Private"))
        	return false;
        else{
        	//String query2 = "SELECT DISTINCT ?o WHERE { ?s ?p ?o . FILTER regex(str(?s), \""+plate+"\") .   FILTER regex(str(?p), \"hasPass\") . FILTER ( ?p != <rdf:type> ) }";
        	String query2 = ReasonerMain.prefix+"SELECT ?o WHERE { ?s ns:hasPlate \""+plate+"\" . ?s ns:hasPass ?o }";
        	resp = kpiCore.querySPARQL(query2);
            List<String> passesId = new ArrayList<String>();
            if (!resp.isConfirmed())
                return false;

            Vector<String[]> rowPlate2 = null;
            SSAP_sparql_response sqr2 = resp.sparqlquery_results;

            while (sqr2.hasNext()) {
                sqr2.next();
                rowPlate2 = sqr2.getRow();
                for (String[] r2 : rowPlate2) {
                    passesId.add(r2[2]);
                }
            }
            
            if(passesId.isEmpty()){
            	return true;
            }else{
            	for(String st:passesId){
            		for(Pass px:this.passesCache){
            			if(st.equals(OntologyProvider.toTripleFormat("Pass_"+px.getTicket())) && ts<px.getDeadline()){
            				return false;
            			}
            		}
            	}
            	return true;
            }
            
        }
	}

}
