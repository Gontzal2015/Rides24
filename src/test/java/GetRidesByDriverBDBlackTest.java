import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import dataAccess.DataAccess;
import domain.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import testOperations.TestDataAccess;
import domain.Driver;

import org.junit.Test;


public class GetRidesByDriverBDBlackTest {

static DataAccess sut = new DataAccess();
	
	static TestDataAccess testDA =new TestDataAccess();
	
	private String driver;
	private String pass;

	private List<Ride> rides;

	@Test
	//The driver has one active ride
	public void test1() {
		Boolean DriverCreated=false;
		testDA.open();
		driver="Iñigo";
		pass="1234";
		List<Ride> esperado = new ArrayList<Ride>();

		String rideFrom="Donostia";
		String rideTo="Zarautz";
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate=null;;
		try {
			rideDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {

			testDA.open();

			if(testDA.createDriver(driver, pass)!=null) {
				DriverCreated=true;
			}
			testDA.close();
			sut.open();
			Ride r=sut.createRide(rideFrom, rideTo, rideDate, 0, 0, driver);
			esperado.add(r);

			
			rides=sut.getRidesByDriver(driver);
			sut.close();
			assertEquals(esperado, rides);
			
			
		}
		catch(Exception e) {
		fail();
		}
		finally {
			
			testDA.open();
			testDA.removeRide(driver, rideFrom, rideTo, rideDate);
			if(DriverCreated) {
				
				testDA.removeDriver(driver);

			}

			testDA.close();
		}
	}
	
	
	@Test
	//Driver is null
	public void test2() {
		
		driver = null;
		
		try {
			sut.open();
			rides=sut.getRidesByDriver(driver);
			assertNull(rides);
			
		}
		catch(Exception e) {
			fail();
		}
		finally {
			sut.close();
		}
	}
	
	
	@Test
	//Driver name does not exist in the DB
	public void test3() {
		
		driver = "Eider";
		
		try {
			sut.open();
			rides=sut.getRidesByDriver(driver);
			assertNull(rides);
			
		}
		catch(Exception e) {
			fail();
		}
		finally {
			sut.close();
		}
	}
	
	@Test
	//Driver exists in DB but has no drives
	public void test4() {
		Boolean DriverCreated=false;
		testDA.open();
		driver="Leire";
		pass="1234";
		List<Ride> esperado = new ArrayList<Ride>();
		try {

			testDA.open();

			if(testDA.createDriver(driver, pass)!=null) {
				DriverCreated=true;
			}
			testDA.close();
			sut.open();
			rides=sut.getRidesByDriver(driver);
			sut.close();
			assertEquals(esperado, rides);
			
			
		}
		catch(Exception e) {
		fail();
		}
		finally {

			if(DriverCreated) {
				testDA.open();
				testDA.removeDriver(driver);
				testDA.close();
			}


		}
	}
	
	@Test
	//The driver does not have any active rides
	public void test5() {
		Boolean DriverCreated=false;
		testDA.open();
		driver="Xiomara";
		pass="1234";
		List<Ride> esperado = new ArrayList<Ride>();

		String rideFrom="Donostia";
		String rideTo="Zarautz";
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate=null;;
		try {
			rideDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {

			testDA.open();

			if(testDA.createDriver(driver, pass)!=null) {
				DriverCreated=true;
			}
			testDA.close();
			sut.open();
			Ride r=sut.createRide(rideFrom, rideTo, rideDate, 0, 0, driver);
			r.setActive(false);
			
			rides=sut.getRidesByDriver(driver);
			sut.close();
			assertEquals(esperado, rides);
			
			
		}
		catch(Exception e) {
		fail();
		}
		finally {
			
			testDA.open();
			testDA.removeRide(driver, rideFrom, rideTo, rideDate);
			if(DriverCreated) {
				
				testDA.removeDriver(driver);

			}

			testDA.close();
		}
	
	
	}
}