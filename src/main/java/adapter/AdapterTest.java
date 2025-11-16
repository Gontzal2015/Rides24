package adapter;

import businessLogic.BLFacadeImplementation;
import businessLogic.BLFactory;

import java.net.MalformedURLException;

import businessLogic.BLFacade;
import domain.Driver;

public class AdapterTest {
	public static void main(String[]	args) throws MalformedURLException	{
//		the	BL	is	local
	boolean isLocal =	true;
	BLFacade	blFacade =	new BLFactory().getBusinessLogic(isLocal);
	Driver	d= blFacade. getDriver("Urtzi");
	DriverTable	dt=new	DriverTable(d);
	dt.setVisible(true);
	}
}
