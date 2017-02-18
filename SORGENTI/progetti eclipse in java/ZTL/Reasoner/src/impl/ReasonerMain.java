 package impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import ModelClasses.Pass;
import ModelClasses.Person;
import ModelClasses.Vehicle;
import sofia_kp.KPICore;
import sofia_kp.SIBResponse;
import sofia_kp.SSAP_sparql_response;

public class ReasonerMain {
	
	public static String prefix = "PREFIX  ns:<http://ztl#>";
	
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("Usage: reasoner.jar SIB_address SIB_port");
			System.exit(-1);
		}
		String address = args[0];
		int port = -1;
		try {
			port = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.err.println("Port must be integer value");
			System.exit(-1);
		}
		System.out.println("Reasoner started...");

		// reasoner data cache
		List<Pass> passesCache = new ArrayList<Pass>();
		
		// sib instance
		KPICore kpiCore = new KPICore(address, port, "ztl");
		SIBResponse resp = null;

		// join
		resp = kpiCore.join();
		if (!resp.isConfirmed()) {
			System.err.println("Error joining the SIB");
			System.exit(-1);
		}

		String passSubId = null;
		String sensorSubId = null;

		// INIT DATA QUERY
		//TODO: prende tutte le triple dei veicoli e delle persone (fisse da init) 
		//-->PRESUPPOSTO E' CHE OGNI VOLTA CHE SI CHIUDE IL REASONER SI DEVE BRUCIARE LA SIB
		//e wrappa tutto in classi di modello, poi riempie le liste
		//TODO --> QUERY INIT SOLO SE GLI DIAMO I PASS GIA IN SIB ALL'AVVIO (SE CI SONO...)
		
		
		RSensorHandler sensorHandler = new RSensorHandler(kpiCore, resp, passesCache);
		RPassHandler passHandler = new RPassHandler(passesCache);
		
		// SUBSCRPTION PASS
		//String passPattern = "SELECT DISTINCT ?s ?p ?o  WHERE {   ?s ?p ?o .   FILTER regex(str(?s), \"Pass_\") .   FILTER ( ?p != <rdf:type> ) }";
		String passPattern = prefix+"SELECT ?s ?p ?o  WHERE {   ?s ?p ?o . ?s <rdf:type> ns:Pass }";
		resp = kpiCore.subscribeSPARQL(passPattern, passHandler);
		passSubId = resp.subscription_id;

		// SUBSCRIPTION FINE
		//String sensorPattern = "SELECT DISTINCT ?s ?p ?o  WHERE {   ?s ?p ?o .   FILTER regex(str(?s), \"Sensor_\") .   FILTER ( ?p != <rdf:type> ) }";
		String sensorPattern = prefix+"SELECT DISTINCT ?s ?p ?o  WHERE {   ?s ?p ?o . ?s <rdf:type> ns:Sensor }";
		resp = kpiCore.subscribeSPARQL(sensorPattern, sensorHandler);
		sensorSubId = resp.subscription_id;

		// TODO leave sib + unsubscriptions

		/*
		 * TODO il reasoner deve fare i "ragionamenti" ---> � un aggregatore
		 * fa una query iniziale in cui ottiene i dati di init e si crea una sua
		 * cache dei dati, poi si sottoscrive agli inserimenti di nuovi pass e
		 * quando arrivano lui aggiorna la sua cache dati inserendo le nuove
		 * istanze autorizzate. Si sottoscrive anche ai sensorData rilevati e
		 * per ognuno di questi: - si prende la targa e fa una query sparql sui
		 * veicoli e trova il veicolo con quella targa -- se il veicolo non �
		 * trovato, parte una multa "anonima" a quella targa --> di fatto sar�
		 * una simulazione di segnalazione all'ente competente che sa
		 * riconoscere la targa (es: targa estera) noi non gestiamo questa cosa
		 * ma generiamo semplicemente una multa anonima -- se il veicolo �
		 * trovato, si fa una nuova query sparql su tutti gli abbonamenti di
		 * quel veicolo e si confronta il timestamp del sensorData con la
		 * deadline degli abbonamenti. siccome il sistema terr� traccia di
		 * tutti gli abbonamenti di un veicolo nel corso del tempo, al primo
		 * abbonamento trovato con deadline superiore alla data di rilevamento
		 * del sensore il veicolo � "salvo" e i calcoli interrotti. se non ci
		 * sono abbonamenti, o se tutti hanno deadline gi� passata, il
		 * reasoner genera una multa, la scrive sulla sib sotto forma di triple
		 * e il monitor, che sar� sottoscritto alle multe, la mostrer� a
		 * video.
		 * 
		 */

	}

}
