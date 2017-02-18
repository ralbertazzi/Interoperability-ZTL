package impl;

public class MonitorMain {

	public static void main(String[] args) {
		if(args.length != 2){
			System.err.println("Usage: Monitor.jar SIB_address SIB_port");
			System.exit(-1);
		}
		String address = args[0];
		int port=-1;
		try{
			port = Integer.parseInt(args[1]);
		}
		catch(NumberFormatException e){
			System.err.println("Port must be integer value");
			System.exit(-1);
		}
		Monitor m = new Monitor(address,port);
		m.setVisible(true);
	}

}
