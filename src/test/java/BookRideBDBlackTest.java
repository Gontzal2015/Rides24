import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import dataAccess.DataAccess;
import domain.Driver;
import domain.Ride;
import domain.Traveler;
import testOperations.TestDataAccess;

public class BookRideBDBlackTest {
//comentario Prueba
	// sut:system under test
	static DataAccess sut = new DataAccess();

	// additional operations needed to execute the test
	static TestDataAccess testDA = new TestDataAccess();
	
	//-----------------------------------------
	//			TEST CASOS DE PRUEBA
	//-----------------------------------------
	
	@Test
	//sut.bookRide: Todos los datos son correctos. Debe devolver true.
	public void test1() {
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
	
	@Test
	//sut.bookRide: El username = null. Debe devolver false.
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
			
			//Se llama al método bookRide con username = null 
			sut.open();
			result = sut.bookRide(null, r, 1, 1.0);
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
	//sut.bookRide:El viajero no existe en la BD. Debe devolver false.
	public void test3() {
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
			
			//Se llama al método bookRide, el viajero no esta en la BD
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
	//sut.bookRide: El ride = null. Debe devolver false.
	public void test4() {
		boolean result = false;
		Driver d = null;
		Traveler t = null;
		try {
			//Se crea un viajero y conductor para la BD
			testDA.open();
			t = testDA.createTraveler("Haizea", "123", 200);
			d = testDA.createDriver("Javier", "456");
			testDA.close();
			
			//Se llama al método bookRide con ride = null
			sut.open();
			result = sut.bookRide("Haizea", null, 1, 1.0);
			sut.close();
			
			//Resultado esperado: false
			assertFalse(result);
		} catch (Exception e) {
			sut.close();
			fail();
		} finally {
			testDA.open();
			testDA.removeDriver(d.getUsername());
			testDA.removeTraveler(t.getUsername());
			testDA.close();
		}
	}
	
	@Test
	//sut.bookRide: El ride no existe en la BD. Debe devolver false, pero lanza excepción.
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
			testDA.close();
			
			//Se crea un ride
			Ride r = new Ride("Donostia", "Bilbao", rideDate, 3, 5.0, d);

			
			//Se llama al método bookRide, el ride no está en la BD
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
			testDA.removeDriver(d.getUsername());
			testDA.removeTraveler(t.getUsername());
			testDA.close();
		}
	}
	@Test
	//sut.bookRide: El número de asientos a reservar es negativo. Debe devolver false.
	public void test6() {
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
			
			//Se llama al método bookRide, el número de asientos es -5
			sut.open();
			result = sut.bookRide("Haizea", r, -5, 1.0);
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
	//sut.bookRide: El número de asientos a reservar es mayor al disponible. Debe devolver false.
	public void test7() {
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
			
			//Se llama al método bookRide, el número de asientos a reservar (10) es mayor que el disponible (3)
			sut.open();
			result = sut.bookRide("Haizea", r, 10, 1.0);
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
	//sut.bookRide: El descuento a aplicar es negativo. Debe devolver false.
	public void test8() {
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
			
			//Se llama al método bookRide, el descuento es negativo: -5.0
			sut.open();
			result = sut.bookRide("Haizea", r, 1, -5.0);
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
	//sut.bookRide: El descuento es mayor que el precio del viaje. Debe devolver false.
	public void test9() {
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
			
			//Se llama al método bookRide, el descuento (20.0) es mayor al precio del viaje (5.0)
			sut.open();
			result = sut.bookRide("Haizea", r, 1, 20.0);
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
	//sut.bookRide: El viajero no tiene suficiente dinero para pagar el viaje. Debe devolver false.
	public void test10() {
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
			
			//Se llama al método bookRide, el viajero no tiene suficiente dinero para pagar
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	//-----------------------------------------
	//			TEST VALORES LÍMITE
	//-----------------------------------------

	
	//TEST SEATS
	
	@Test
	//sut.bookRide: El número de asientos a reservar es negativo. Debe devolver false.
	public void test11() {
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
			Ride r = testDA.createRideForDriver("Donostia", "Bilbao", rideDate, 10, 20.0, d);
			testDA.close();
			
			//Se llama al método bookRide, el número de asientos es -1
			sut.open();
			result = sut.bookRide("Haizea", r, -1, 1.0);
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
	//sut.bookRide: El número de asientos a reservar es 0. Debe devolver false.
	public void test12() {
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
			Ride r = testDA.createRideForDriver("Donostia", "Bilbao", rideDate, 10, 20.0, d);
			testDA.close();
			
			//Se llama al método bookRide, el número de asientos es 0
			sut.open();
			result = sut.bookRide("Haizea", r, 0, 1.0);
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
	//sut.bookRide: El número de asientos a reservar es el mínimo. Debe devolver true.
	public void test13() {
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
			Ride r = testDA.createRideForDriver("Donostia", "Bilbao", rideDate, 10, 20.0, d);
			testDA.close();
			
			//Se llama al método bookRide, el número de asientos es 1
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
	
	@Test
	//sut.bookRide: El número de asientos a reservar es el 1 menos del máximo disponibles. Debe devolver true.
		public void test14() {
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
				Ride r = testDA.createRideForDriver("Donostia", "Bilbao", rideDate, 10, 20.0, d);
				testDA.close();
				
				//Se llama al método bookRide, el número de asientos es 9 (hay 10 disponibles)
				sut.open();
				result = sut.bookRide("Haizea", r, 9, 1.0);
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
	
	@Test
	//sut.bookRide: El número de asientos a reservar es el número máximo disponibles. Debe devolver true.
		public void test15() {
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
				Ride r = testDA.createRideForDriver("Donostia", "Bilbao", rideDate, 10, 20.0, d);
				testDA.close();
				
				//Se llama al método bookRide, el número de asientos es 10 (hay 10 disponibles)
				sut.open();
				result = sut.bookRide("Haizea", r, 10, 1.0);
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
	@Test
	//sut.bookRide: El número de asientos a reservar es el 1 más del máximo disponibles. Debe devolver false.
		public void test16() {
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
				Ride r = testDA.createRideForDriver("Donostia", "Bilbao", rideDate, 10, 20.0, d);
				testDA.close();
				
				//Se llama al método bookRide, el número de asientos es 11 (hay 10 disponibles)
				sut.open();
				result = sut.bookRide("Haizea", r, 11, 1.0);
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
	
	
	
	//TEST DESK
	
	
	@Test
	//sut.bookRide: El descuento a aplicar es negativo. Debe devolver false.
	public void test17() {
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
			Ride r = testDA.createRideForDriver("Donostia", "Bilbao", rideDate, 10, 20.0, d);
			testDA.close();
			
			//Se llama al método bookRide, el descuento es negativo: -1.0
			sut.open();
			result = sut.bookRide("Haizea", r, 1, -1.0);
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
	//sut.bookRide: El descuento a aplicar es 0. Debe devolver true.
	public void test18() {
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
			Ride r = testDA.createRideForDriver("Donostia", "Bilbao", rideDate, 10, 20.0, d);
			testDA.close();
			
			//Se llama al método bookRide, el descuento es: 0.0
			sut.open();
			result = sut.bookRide("Haizea", r, 1, 0.0);
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
	
	@Test
	//sut.bookRide: El descuento a aplicar es 1 mayor al mínimo. Debe devolver true.
	public void test19() {
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
			Ride r = testDA.createRideForDriver("Donostia", "Bilbao", rideDate, 10, 20.0, d);
			testDA.close();
			
			//Se llama al método bookRide, el descuento es: 1.0
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
	
	@Test
	//sut.bookRide: El descuento a aplicar es 1 menos que el precio total a pagar. Debe devolver true.
	public void test20() {
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
			Ride r = testDA.createRideForDriver("Donostia", "Bilbao", rideDate, 10, 20.0, d);
			testDA.close();
			
			//Se llama al método bookRide, el descuento es: 19.0
			sut.open();
			result = sut.bookRide("Haizea", r, 1, 19.0);
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
	
	@Test
	//sut.bookRide: El descuento a aplicar es el precio total a pagar. Debe devolver false.
	public void test21() {
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
			Ride r = testDA.createRideForDriver("Donostia", "Bilbao", rideDate, 10, 20.0, d);
			testDA.close();
			
			//Se llama al método bookRide, el descuento es: 20.0
			sut.open();
			result = sut.bookRide("Haizea", r, 1, 20.0);
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
	//sut.bookRide: El descuento a aplicar es 1 más que el precio total a pagar. Debe devolver false.
	public void test22() {
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
			Ride r = testDA.createRideForDriver("Donostia", "Bilbao", rideDate, 10, 20.0, d);
			testDA.close();
			
			//Se llama al método bookRide, el descuento es: 21.0
			sut.open();
			result = sut.bookRide("Haizea", r, 1, 21.0);
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

	
	
	//TEST MONEY
	
	
	@Test
	//sut.bookRide: El viajero tiene 1 menos que el total a pagar. Debe devolver false.
	public void test23() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate=null;
		boolean result = false;
		Driver d = null;
		Traveler t = null;
		try {
			rideDate = sdf.parse("11/03/2026");
			//Se crea un viajero, conductor y viaje para la BD
			testDA.open();
			t = testDA.createTraveler("Haizea", "123", 199);
			d = testDA.createDriver("Javier", "456");
			Ride r = testDA.createRideForDriver("Donostia", "Bilbao", rideDate, 10, 20.0, d);
			testDA.close();
			
			//Se llama al método bookRide, el viajero no tiene suficiente dinero para pagar
			sut.open();
			result = sut.bookRide("Haizea", r, 10, 0.0);
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
	//sut.bookRide: El viajero tiene justo que el total a pagar. Debe devolver true.
	public void test24() {
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
			Ride r = testDA.createRideForDriver("Donostia", "Bilbao", rideDate, 10, 20.0, d);
			testDA.close();
			
			//Se llama al método bookRide, el viajero tiene suficiente dinero para pagar
			sut.open();
			result = sut.bookRide("Haizea", r, 10, 0.0);
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
	
	@Test
	//sut.bookRide: El viajero tiene 1 mas que el total a pagar. Debe devolver true.
	public void test25() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate=null;
		boolean result = false;
		Driver d = null;
		Traveler t = null;
		try {
			rideDate = sdf.parse("11/03/2026");
			//Se crea un viajero, conductor y viaje para la BD
			testDA.open();
			t = testDA.createTraveler("Haizea", "123", 201);
			d = testDA.createDriver("Javier", "456");
			Ride r = testDA.createRideForDriver("Donostia", "Bilbao", rideDate, 10, 20.0, d);
			testDA.close();
			
			//Se llama al método bookRide, el viajero tiene suficiente dinero para pagar
			sut.open();
			result = sut.bookRide("Haizea", r, 10, 0.0);
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
