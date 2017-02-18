package impl;

public class SensorMain {

	public static void main(String[] args) {
		if(args.length != 2){
			System.err.println("Usage: Sensor.jar SIB_address SIB_port");
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
		
		Sensor s = new Sensor(address,port);
    	s.setVisible(true);	
	}

}
