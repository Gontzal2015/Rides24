package adapter;

import businessLogic.BLFacadeImplementation;
import businessLogic.BLFacade;
import domain.Driver;

public class AdapterTest {
	public static void main(String[]	args)	{
//		the	BL	is	local
	boolean isLocal =	true;
	BLFacade	blFacade =	new BLFacadeImplementation();
	Driver	d= blFacade. getDriver("Urtzi");
	DriverTable	dt=new	DriverTable(d);
	dt.setVisible(true);
	}

}
