package impl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import sofia_kp.KPICore;
import sofia_kp.SIBResponse;
import sofia_kp.SSAP_sparql_response;

public class Monitor extends JFrame {
	private static final long serialVersionUID = 1L;

	// sib instance
	private KPICore kpiCore;
	private SIBResponse resp;

	private MyFineHandler fineHandler;
	private MyPassHandler passHandler;
	
	private String passSubId = null;
	private String fineSubId = null;
	
	private JLabel lblVehicles = new JLabel("ALL PASSES");
	private JLabel lblFines = new JLabel("ALL FINES");
	private JScrollPane v;
	private JScrollPane f;
	private JTextArea txtVehicles = new JTextArea(15, 15);
	private JTextArea txtFines = new JTextArea(15, 15);

	private JPanel pan1 = new JPanel(new GridLayout(1, 2, 10, 10));
	private JPanel pan2 = new JPanel(new GridLayout(1, 2, 10, 10));
	private JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

	public Monitor(String address, int port) {
		super("Monitor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(1000, 200));
		setResizable(false);
		setLayout(new BorderLayout(10, 10));

		kpiCore = new KPICore(address, port, "ztl");
		resp = null;

		fineHandler = new MyFineHandler(this.txtFines);
		passHandler = new MyPassHandler(this.txtVehicles);
		
		// join
		resp = kpiCore.join();
		if (!resp.isConfirmed()) {
			System.err.println("Error joining the SIB");
			System.exit(-1);
		}

		f = new JScrollPane(txtFines);
		f.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		v = new JScrollPane(txtVehicles);
		f.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		pan1.add(lblFines);
		pan1.add(lblVehicles);
		pan2.add(f);
		pan2.add(v);

		mainPanel.add(pan1, BorderLayout.NORTH);
		mainPanel.add(pan2, BorderLayout.CENTER);

		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(mainPanel);

		// pack();
		this.addWindowListener(new WindowListener() {
			public void windowClosing(WindowEvent e){
				//unsubscribe
				if(passSubId!=null)
					kpiCore.unsubscribe(passSubId);

				if(fineSubId!=null)
					kpiCore.unsubscribe(fineSubId);
				
				//leave
				resp = kpiCore.leave();
				if (!resp.isConfirmed()) {
					JOptionPane.showMessageDialog(null, "Error when leaving SIB");
				}else
					JOptionPane.showMessageDialog(null, "Disconnected.");
				
				System.exit(0);
			}
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void windowDeactivated(WindowEvent e) {
				
			}
		});

		//TODO test sottoscrizioni

//		//INIT DATA QUERY
//		String query = "SELECT ?s ?p ?o  WHERE { ?s ?p ?o . FILTER regex(str(?s), \"Pass|Vehicle\") . FILTER regex(str(?p), \"hasPass|hasDeadline|hasType|hasTicket\")}"; // Execute the query
//		resp = kpiCore.querySPARQL(query);
//		
//		if (!resp.isConfirmed()) {
//			System.out.println("SPARQL Query error!");
//		} else {
//			Vector<String[]> row = null;
//			SSAP_sparql_response sqr = resp.sparqlquery_results;
//
//			while (sqr.hasNext()) {
//				sqr.next();
//				row = sqr.getRow();
//				for (String[] r : row) {
//					txtVehicles.append(r[2]+" ");
//				}
//				txtVehicles.append("\n");
//			}
//		}
		
		//SUBSCRPTION PASS
		String query = "SELECT ?s ?p ?o  WHERE { ?s ?p ?o . FILTER regex(str(?s), \"Pass|Vehicle\") . FILTER regex(str(?p), \"hasPass|hasDeadline|hasType|hasTicket\")}"; // Execute the query
		resp = kpiCore.subscribeSPARQL(/*passPattern*/query, passHandler);
		passSubId = resp.subscription_id;
		
		//SUBSCRIPTION FINE
		String finePattern = "SELECT DISTINCT ?s ?p ?o WHERE { ?s ?p ?o . FILTER regex(str(?s), \"Fine_\") . FILTER ( ?p != <rdf:type> ) }";
		resp = kpiCore.subscribeSPARQL(finePattern, fineHandler);
		fineSubId = resp.subscription_id;
		
	}

}
