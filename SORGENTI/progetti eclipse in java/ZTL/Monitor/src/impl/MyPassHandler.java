package impl;

import java.util.Vector;

import javax.swing.JTextArea;

import sofia_kp.SSAP_sparql_response;
import sofia_kp.iKPIC_subscribeHandler2;

public class MyPassHandler implements iKPIC_subscribeHandler2 {

	private JTextArea txtVehicles;
	
	public MyPassHandler(JTextArea txtVehicles) {
		super();
		this.txtVehicles = txtVehicles;
	}

	@Override
	public void kpic_RDFEventHandler(Vector<Vector<String>> newTriples, Vector<Vector<String>> oldTriples,
			String indSequence, String subID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void kpic_SPARQLEventHandler(SSAP_sparql_response newResults, SSAP_sparql_response oldResults,
			String indSequence, String subID) {

		Vector<String[]> row = null;
		
		while (newResults.hasNext()) {
			newResults.next();
			row = newResults.getRow();
			for (String[] r : row) {
				txtVehicles.append(r[2]+" ");
			}
			txtVehicles.append("\n");
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

}
