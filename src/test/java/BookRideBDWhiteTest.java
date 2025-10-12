import dataAccess.DataAccess;
import domain.Driver;
import domain.Ride;
import domain.Traveler;
import testOperations.TestDataAccess;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class BookRideBDWhiteTest {
	
	//sut:system under test
	static DataAccess sut = new DataAccess();
	
	//additional operations needed to execute the test
	static TestDataAccess testDA = new TestDataAccess();
	
	@Test
	//sut.bookRide: Se produce una excepción al ejecutar el método. Debe devolver false.
	public void test1() {
		boolean result = false;
		try {
			//Se llama al método bookRide con un ride = null
			sut.open();
			result = sut.bookRide("Haizea", null, 1, 1.0);
			sut.close();
			
			//Resultado esperado: false
			assertFalse(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
	}
	
	@Test
	//sut.bookRide: El viajero no existe en la BD. Debe devolver false.
	public void test2() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate=null;
		boolean result = false;
		Driver d = null;
		try {
			rideDate = sdf.parse("11/03/2026");
			//Se crea un conductor y viaje para la BD
			testDA.open();
			d = testDA.createDriver("Javier", "456");
			Ride r = testDA.createRideForDriver("Donostia", "Bilbao", rideDate, 3, 5.0, d);
			testDA.close();
			
			//Se llama al método bookRide con un traveler que no existe
			sut.open();
			result = sut.bookRide("Haizea", r, 1, 1.0);
			sut.close();
			
			//Resultado esperado: false
			assertFalse(result);
		} catch (Exception e) {
			sut.close();
			fail();
		} finally {
			testDA.open();
			testDA.removeRide(d.getUsername(), "Donostia", "Bilbao", rideDate);
			testDA.removeDriver(d.getUsername());
			testDA.close();
		}
		
	}
	
	@Test
	//sut.bookRide: El viajero existe en la BD, pero pide más asientos de los disponibles. Debe devolver false.
	public void test3() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate=null;
		boolean result = false;
		Driver d = null;
		Traveler t = null;
		try {
			rideDate = sdf.parse("11/03/2026");
			//Se crea un viajero, conductor y viaje para la BD
			testDA.open();
			t = testDA.createTraveler("Haizea", "123", 200);
			d = testDA.createDriver("Javier", "456");
			Ride r = testDA.createRideForDriver("Donostia", "Bilbao", rideDate, 3, 5.0, d);
			testDA.close();
			
			//Se llama al método bookRide con un traveler que pide más asientos que los disponibles
			sut.open();
			result = sut.bookRide("Haizea", r, 5, 1.0);
			sut.close();
			
			//Resultado esperado: false
			assertFalse(result);
		} catch (Exception e) {
			sut.close();
			fail();
		} finally {
			testDA.open();
			testDA.removeRide(d.getUsername(), "Donostia", "Bilbao", rideDate);
			testDA.removeDriver(d.getUsername());
			testDA.removeTraveler(t.getUsername());
			testDA.close();
		}
	}
	
	@Test
	//sut.bookRide: El viajero existe en la BD pero no tiene suficiente dinero para pagar. Debe devolver false.
	public void test4() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate=null;
		boolean result = false;
		Driver d = null;
		Traveler t = null;
		try {
			rideDate = sdf.parse("11/03/2026");
			//Se crea un viajero, conductor y viaje para la BD
			testDA.open();
			t = testDA.createTraveler("Haizea", "123", 0);
			d = testDA.createDriver("Javier", "456");
			Ride r = testDA.createRideForDriver("Donostia", "Bilbao", rideDate, 3, 5.0, d);
			testDA.close();
			
			//Se llama al método bookRide 
			sut.open();
			result = sut.bookRide("Haizea", r, 1, 1.0);
			sut.close();
			
			//Resultado esperado: false
			assertFalse(result);
		} catch (Exception e) {
			sut.close();
			fail();
		} finally {
			testDA.open();
			testDA.removeRide(d.getUsername(), "Donostia", "Bilbao", rideDate);
			testDA.removeDriver(d.getUsername());
			testDA.removeTraveler(t.getUsername());
			testDA.close();
		}

	}
	
	@Test
	//sut.bookRide: Todos los datos son correctos. Debe devolver true.
	public void test5() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate=null;
		boolean result = false;
		Driver d = null;
		Traveler t = null;
		try {
			rideDate = sdf.parse("11/03/2026");
			//Se crea un viajero, conductor y viaje para la BD
			testDA.open();
			t = testDA.createTraveler("Haizea", "123", 200);
			d = testDA.createDriver("Javier", "456");
			Ride r = testDA.createRideForDriver("Donostia", "Bilbao", rideDate, 3, 5.0, d);
			testDA.close();
			
			//Se llama al método bookRide 
			sut.open();
			result = sut.bookRide("Haizea", r, 1, 1.0);
			sut.close();
			
			//Resultado esperado: true
			assertTrue(result);
		} catch (Exception e) {
			sut.close();
			fail();
		} finally {
			testDA.open();
			testDA.removeRide(d.getUsername(), "Donostia", "Bilbao", rideDate);
			testDA.removeDriver(d.getUsername());
			testDA.removeTraveler(t.getUsername());
			testDA.close();
		}
	}

	
}
