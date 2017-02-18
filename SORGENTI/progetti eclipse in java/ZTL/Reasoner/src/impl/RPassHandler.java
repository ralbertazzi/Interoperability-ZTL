package impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import ModelClasses.Pass;
import sofia_kp.KPICore;
import sofia_kp.SIBResponse;
import sofia_kp.SSAP_sparql_response;
import sofia_kp.iKPIC_subscribeHandler2;

public class RPassHandler implements iKPIC_subscribeHandler2 {

	private List<Pass> passesCache;
	
	public RPassHandler(List<Pass> passesCache) {
		super();
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
		Pass p = new Pass();
		
		
		Vector<String[]> row = null;
		while (newResults.hasNext()) {
			newResults.next();
			row = newResults.getRow();
			boolean ticketFound=false, typeFound=false, deadlineFound=false;
			for (String[] r : row) {
				if(ticketFound){
					p.setTicket(Long.parseLong(r[2]));
					ticketFound = false;
				}else if(typeFound){
					p.setType(r[2]);
					typeFound = false;
				}else if(deadlineFound){
					p.setDeadline(Long.parseLong(r[2]));
					deadlineFound = false;
				}
				
				if(r[2].contains("hasTicket")){
					ticketFound = true;
				}else if(r[2].contains("hasType")){
					typeFound = true;
				}else if(r[2].contains("hasDeadline")){
					deadlineFound = true;
				}
			}
		}
		this.passesCache.add(p);
	}

	@Override
	public void kpic_UnsubscribeEventHandler(String sub_ID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void kpic_ExceptionEventHandler(Throwable SocketException) {
		// TODO Auto-generated method stub

	}

}
