package impl;

import java.io.File;

import ModelClasses.Person;
import ModelClasses.Vehicle;
import resources.JenaBasedOntologyLoader;
import sofia_kp.KPICore;
import sofia_kp.SIBResponse;

public class InitializerMain {

	public static void main(String[] args) {
		if(args.length != 2){
			System.err.println("Usage: initializer.jar SIB_address SIB_port");
			System.exit(-1);
		}
		String address = args[0];
		int port=-1;
		try{
			port = Integer.parseInt(args[1]);
		}
		catch(NumberFormatException e)
		{
			System.err.println("Port must be integer value");
			System.exit(-1);
		}
		
		System.out.println("Initializer started...");
		
		JenaBasedOntologyLoader ontoLoader = new JenaBasedOntologyLoader(address, port);
		File f = new File("ontologia.xml");
		if(!f.exists())
		{
			System.err.println("ontologia.xml does not exist");
			System.exit(-1);
		}
			
		ontoLoader.LoadOntologyIntoSIB("ontologia.xml");
		
		//sib instance
		KPICore kpiCore = new KPICore(address, port, "ztl");
		SIBResponse resp = null;
		
		//join
		resp = kpiCore.join();
		if(!resp.isConfirmed()){
			System.err.println("Error joining the SIB");
			System.exit(-1);
		}
		
		//---INIT DATA SIB---
		for(int i=1; i<30; i++){
			Vehicle v;
			if(i<10)
				 v = new Vehicle("PP"+i+i+i+"PP", "Private", null);
			else if(i>=10 && i<15)
				v = new Vehicle("MM"+i%10+i%10+i%10+"MM", "Motorbike", null);
			else if(i>=15 && i<20)
				v = new Vehicle("TT"+i%10+i%10+i%10+"TT", "Taxi", null);
			else if(i>=20 && i<25)
				v = new Vehicle("RR"+i%10+i%10+i%10+"RR", "Resident", null);
			else
				v = new Vehicle("BB"+i%10+i%10+i%10+"BB", "Bus", null);
			
			resp = kpiCore.insert(v.toTriple());
			if(!resp.isConfirmed()){
				System.err.println("Insert vehicle "+i+" failed");
			}else{
				System.out.println("Insert vehicle "+i+" successfull");
			}
			
			Person p = new Person("personFiscalCode"+i, "Name"+i, "Surname"+i, "Addr"+i, "Mail"+i, v);
			
			resp = kpiCore.insert(p.toTriple());
			if(!resp.isConfirmed()){
				System.err.println("Insert person "+i+" failed");
			}else{
				System.out.println("Insert person "+i+" successfull");
			}
		}
		
		//leave		
		resp = kpiCore.leave();
		if(!resp.isConfirmed()){
			System.err.println("Error leaving the SIB");
			System.exit(-1);
		}
		
		System.out.println("...Initializer finished");
	}

}
