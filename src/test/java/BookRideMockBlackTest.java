import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Driver;
import domain.Ride;
import domain.Traveler;

public class BookRideMockBlackTest {

	static DataAccess sut;

	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected EntityManagerFactory entityManagerFactory;
	@Mock
	protected EntityManager db;
	@Mock
	protected EntityTransaction et;
//comentario prueba
	@Before
	public void init() {
		MockitoAnnotations.openMocks(this);
		persistenceMock = Mockito.mockStatic(Persistence.class);
		persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
				.thenReturn(entityManagerFactory);

		Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
		Mockito.doReturn(et).when(db).getTransaction();
		sut = new DataAccess(db);
	}

	@After
	public void tearDown() {
		persistenceMock.close();
	}

	@Mock
	protected TypedQuery<Traveler> typedQueryTraveler;

	// -----------------------------------------
	// TEST CASOS DE PRUEBA
	// -----------------------------------------

	@Test
	// sut.bookRide: Todos los datos son correctos. Debe devolver true.
	public void test1() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		boolean result = false;

		Driver d = null;
		Ride r = null;
		Traveler t = null;

		String username = "Haizea";
		String driverUsername = "Javier";
		String from = "Donostia";
		String to = "Bilbao";
		int nPlaces = 3;
		double desk = 5.0;

		try {
			rideDate = sdf.parse("11/03/2026");

			t = new Traveler(username, "123");
			t.setMoney(200);
			d = new Driver(driverUsername, "456");
			r = new Ride(from, to, rideDate, nPlaces, desk, d);

			// Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(t));

			// Simulación de que existe r
			Mockito.when(db.find(Ride.class, r.getRideNumber())).thenReturn(r);

			// Se llama al método bookRide
			sut.open();
			result = sut.bookRide(username, r, 1, 1.0);
			sut.close();

			// Resultado esperado: True
			assertTrue(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
	}

	@Test
	// sut.bookRide: El username = null. Debe devolver false.
	public void test2() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		boolean result = false;

		Driver d = null;
		Ride r = null;
		Traveler t = null;

		String username = null;
		String driverUsername = "Javier";
		String from = "Donostia";
		String to = "Bilbao";
		int nPlaces = 3;
		double desk = 5.0;

		try {
			rideDate = sdf.parse("11/03/2026");

			t = new Traveler(username, "123");
			t.setMoney(200);
			d = new Driver(driverUsername, "456");
			r = new Ride(from, to, rideDate, nPlaces, desk, d);

			// Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.emptyList());

			// Simulación de que existe r
			Mockito.when(db.find(Ride.class, r.getRideNumber())).thenReturn(r);

			// Se llama al método bookRide con un traveler=null
			sut.open();
			result = sut.bookRide(username, r, 1, 1.0);
			sut.close();

			// Resultado esperado: False
			assertFalse(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
	}

	@Test
	// sut.bookRide:El viajero no existe en la BD. Debe devolver false.
	public void test3() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		boolean result = false;

		Driver d = null;
		Ride r = null;
		Traveler t = null;

		String username = "Haizea";
		String driverUsername = "Javier";
		String from = "Donostia";
		String to = "Bilbao";
		int nPlaces = 3;
		double desk = 5.0;

		try {
			rideDate = sdf.parse("11/03/2026");

			t = new Traveler(username, "123");
			t.setMoney(200);
			d = new Driver(driverUsername, "456");
			r = new Ride(from, to, rideDate, nPlaces, desk, d);

			// Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.emptyList());

			// Simulación de que existe r
			Mockito.when(db.find(Ride.class, r.getRideNumber())).thenReturn(r);

			// Se llama al método bookRide con un traveler que no existe en la BD
			sut.open();
			result = sut.bookRide(username, r, 1, 1.0);
			sut.close();

			// Resultado esperado: False
			assertFalse(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
	}

	@Test
	// sut.bookRide: El ride = null. Debe devolver false.
	public void test4() {

		boolean result = false;

		Ride r = null;
		Traveler t = null;

		String username = "Haizea";

		try {

			t = new Traveler(username, "123");
			t.setMoney(200);

			// Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(t));

			// Se llama al método bookRide con un traveler que no existe en la BD
			sut.open();
			result = sut.bookRide(username, r, 1, 1.0);
			sut.close();

			// Resultado esperado: False
			assertFalse(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
	}

	@Test
	// sut.bookRide: El ride no existe en la BD. Debe devolver false, pero lanza
	// excepción.
	public void test5() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		boolean result = false;

		Driver d = null;
		Ride r = null;
		Traveler t = null;

		String username = "Haizea";
		String driverUsername = "Javier";
		String from = "Donostia";
		String to = "Bilbao";
		int nPlaces = 3;
		double desk = 5.0;

		try {
			rideDate = sdf.parse("11/03/2026");

			t = new Traveler(username, "123");
			t.setMoney(200);
			d = new Driver(driverUsername, "456");
			r = new Ride(from, to, rideDate, nPlaces, desk, d);

			// Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(t));

			// Simulación de que existe r
			Mockito.when(db.find(Ride.class, r.getRideNumber())).thenReturn(null);

			// Se llama al método bookRide
			sut.open();
			result = sut.bookRide(username, r, 1, 1.0);
			sut.close();

			// Resultado esperado: False
			assertFalse(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}

	}

	@Test
	// sut.bookRide: El número de asientos a reservar es negativo. Debe devolver
	// false.
	public void test6() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		boolean result = false;

		Driver d = null;
		Ride r = null;
		Traveler t = null;

		String username = "Haizea";
		String driverUsername = "Javier";
		String from = "Donostia";
		String to = "Bilbao";
		int nPlaces = 3;
		double desk = 5.0;

		try {
			rideDate = sdf.parse("11/03/2026");

			t = new Traveler(username, "123");
			t.setMoney(200);
			d = new Driver(driverUsername, "456");
			r = new Ride(from, to, rideDate, nPlaces, desk, d);

			// Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(t));

			// Simulación de que existe r
			Mockito.when(db.find(Ride.class, r.getRideNumber())).thenReturn(r);

			// Se llama al método bookRide, el número de asientos es -5
			sut.open();
			result = sut.bookRide(username, r, -5, 1.0);
			sut.close();

			// Resultado esperado: False
			assertFalse(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
	}

	@Test
	// sut.bookRide: El número de asientos a reservar es mayor al disponible. Debe
	// devolver false.
	public void test7() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		boolean result = false;

		Driver d = null;
		Ride r = null;
		Traveler t = null;

		String username = "Haizea";
		String driverUsername = "Javier";
		String from = "Donostia";
		String to = "Bilbao";
		int nPlaces = 3;
		double desk = 5.0;

		try {
			rideDate = sdf.parse("11/03/2026");

			t = new Traveler(username, "123");
			t.setMoney(200);
			d = new Driver(driverUsername, "456");
			r = new Ride(from, to, rideDate, nPlaces, desk, d);

			// Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(t));

			// Simulación de que existe r
			Mockito.when(db.find(Ride.class, r.getRideNumber())).thenReturn(r);

			// Se llama al método bookRide, el número de asientos a reservar (10) es mayor
			// que el disponible (3)
			sut.open();
			result = sut.bookRide(username, r, 10, 1.0);
			sut.close();

			// Resultado esperado: False
			assertFalse(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
	}

	@Test
	// sut.bookRide: El descuento a aplicar es negativo. Debe devolver false.
	public void test8() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		boolean result = false;

		Driver d = null;
		Ride r = null;
		Traveler t = null;

		String username = "Haizea";
		String driverUsername = "Javier";
		String from = "Donostia";
		String to = "Bilbao";
		int nPlaces = 3;
		double desk = 5.0;

		try {
			rideDate = sdf.parse("11/03/2026");

			t = new Traveler(username, "123");
			t.setMoney(200);
			d = new Driver(driverUsername, "456");
			r = new Ride(from, to, rideDate, nPlaces, desk, d);

			// Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(t));

			// Simulación de que existe r
			Mockito.when(db.find(Ride.class, r.getRideNumber())).thenReturn(r);

			// Se llama al método bookRide, el descuento es negativo: -5.0
			sut.open();
			result = sut.bookRide(username, r, 1, -5.0);
			sut.close();

			// Resultado esperado: False
			assertFalse(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
	}

	@Test
	// sut.bookRide: El descuento es mayor que el precio del viaje. Debe devolver
	// false.
	public void test9() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		boolean result = false;

		Driver d = null;
		Ride r = null;
		Traveler t = null;

		String username = "Haizea";
		String driverUsername = "Javier";
		String from = "Donostia";
		String to = "Bilbao";
		int nPlaces = 3;
		double desk = 5.0;

		try {
			rideDate = sdf.parse("11/03/2026");

			t = new Traveler(username, "123");
			t.setMoney(200);
			d = new Driver(driverUsername, "456");
			r = new Ride(from, to, rideDate, nPlaces, desk, d);

			// Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(t));

			// Simulación de que existe r
			Mockito.when(db.find(Ride.class, r.getRideNumber())).thenReturn(r);

			// Se llama al método bookRide, el descuento (20.0) es mayor al precio del viaje
			// (5.0)
			sut.open();
			result = sut.bookRide(username, r, 1, 20.0);
			sut.close();

			// Resultado esperado: False
			assertFalse(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
	}

	@Test
	// sut.bookRide: El viajero no tiene suficiente dinero para pagar el viaje. Debe
	// devolver false.
	public void test10() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		boolean result = false;

		Driver d = null;
		Ride r = null;
		Traveler t = null;

		String username = "Haizea";
		String driverUsername = "Javier";
		String from = "Donostia";
		String to = "Bilbao";
		int nPlaces = 3;
		double desk = 5.0;

		try {
			rideDate = sdf.parse("11/03/2026");

			t = new Traveler(username, "123");
			t.setMoney(0);
			d = new Driver(driverUsername, "456");
			r = new Ride(from, to, rideDate, nPlaces, desk, d);

			// Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(t));

			// Simulación de que existe r
			Mockito.when(db.find(Ride.class, r.getRideNumber())).thenReturn(r);

			// Se llama al método bookRide, el viajero no tiene suficiente dinero para pagar
			sut.open();
			result = sut.bookRide(username, r, 1, 1.0);
			sut.close();

			// Resultado esperado: False
			assertFalse(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
	}

	// -----------------------------------------
	// TEST VALORES LÍMITE
	// -----------------------------------------

	// TEST SEATS

	@Test
	// sut.bookRide: El número de asientos a reservar es negativo. Debe devolver
	// false.
	public void test11() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		boolean result = false;

		Driver d = null;
		Ride r = null;
		Traveler t = null;

		String username = "Haizea";
		String driverUsername = "Javier";
		String from = "Donostia";
		String to = "Bilbao";
		int nPlaces = 10;
		double desk = 20.0;

		try {
			rideDate = sdf.parse("11/03/2026");

			t = new Traveler(username, "123");
			t.setMoney(200);
			d = new Driver(driverUsername, "456");
			r = new Ride(from, to, rideDate, nPlaces, desk, d);

			// Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(t));

			// Simulación de que existe r
			Mockito.when(db.find(Ride.class, r.getRideNumber())).thenReturn(r);

			// Se llama al método bookRide, el número de asientos es -1
			sut.open();
			result = sut.bookRide(username, r, -1, 1.0);
			sut.close();

			// Resultado esperado: False
			assertFalse(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
	}

	@Test
	// sut.bookRide: El número de asientos a reservar es 0. Debe devolver false.
	public void test12() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		boolean result = false;

		Driver d = null;
		Ride r = null;
		Traveler t = null;

		String username = "Haizea";
		String driverUsername = "Javier";
		String from = "Donostia";
		String to = "Bilbao";
		int nPlaces = 10;
		double desk = 20.0;
		try {
			rideDate = sdf.parse("11/03/2026");

			t = new Traveler(username, "123");
			t.setMoney(200);
			d = new Driver(driverUsername, "456");
			r = new Ride(from, to, rideDate, nPlaces, desk, d);

			// Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(t));

			// Simulación de que existe r
			Mockito.when(db.find(Ride.class, r.getRideNumber())).thenReturn(r);

			// Se llama al método bookRide, el número de asientos es 0
			sut.open();
			result = sut.bookRide(username, r, 0, 1.0);
			sut.close();

			// Resultado esperado: False
			assertFalse(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
	}

	@Test
	// sut.bookRide: El número de asientos a reservar es el mínimo. Debe devolver
	// true.
	public void test13() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		boolean result = false;

		Driver d = null;
		Ride r = null;
		Traveler t = null;

		String username = "Haizea";
		String driverUsername = "Javier";
		String from = "Donostia";
		String to = "Bilbao";
		int nPlaces = 10;
		double desk = 20.0;

		try {
			rideDate = sdf.parse("11/03/2026");

			t = new Traveler(username, "123");
			t.setMoney(200);
			d = new Driver(driverUsername, "456");
			r = new Ride(from, to, rideDate, nPlaces, desk, d);

			// Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(t));

			// Simulación de que existe r
			Mockito.when(db.find(Ride.class, r.getRideNumber())).thenReturn(r);

			// Se llama al método bookRide, el número de asientos es 1
			sut.open();
			result = sut.bookRide(username, r, 1, 1.0);
			sut.close();

			// Resultado esperado: True
			assertTrue(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
	}

	@Test
	// sut.bookRide: El número de asientos a reservar es el 1 menos del máximo
	// disponibles. Debe devolver true.
	public void test14() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		boolean result = false;

		Driver d = null;
		Ride r = null;
		Traveler t = null;

		String username = "Haizea";
		String driverUsername = "Javier";
		String from = "Donostia";
		String to = "Bilbao";
		int nPlaces = 10;
		double desk = 20.0;

		try {
			rideDate = sdf.parse("11/03/2026");

			t = new Traveler(username, "123");
			t.setMoney(200);
			d = new Driver(driverUsername, "456");
			r = new Ride(from, to, rideDate, nPlaces, desk, d);

			// Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(t));

			// Simulación de que existe r
			Mockito.when(db.find(Ride.class, r.getRideNumber())).thenReturn(r);

			// Se llama al método bookRide, el número de asientos es 9 (hay 10 disponibles)
			sut.open();
			result = sut.bookRide(username, r, 9, 1.0);
			sut.close();

			// Resultado esperado: True
			assertTrue(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}

	}

	@Test
	// sut.bookRide: El número de asientos a reservar es el número máximo
	// disponibles. Debe devolver true.
	public void test15() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		boolean result = false;

		Driver d = null;
		Ride r = null;
		Traveler t = null;

		String username = "Haizea";
		String driverUsername = "Javier";
		String from = "Donostia";
		String to = "Bilbao";
		int nPlaces = 10;
		double desk = 20.0;

		try {
			rideDate = sdf.parse("11/03/2026");

			t = new Traveler(username, "123");
			t.setMoney(200);
			d = new Driver(driverUsername, "456");
			r = new Ride(from, to, rideDate, nPlaces, desk, d);

			// Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(t));

			// Simulación de que existe r
			Mockito.when(db.find(Ride.class, r.getRideNumber())).thenReturn(r);

			// Se llama al método bookRide, el número de asientos es 10 (hay 10 disponibles)
			sut.open();
			result = sut.bookRide(username, r, 10, 1.0);
			sut.close();

			// Resultado esperado: True
			assertTrue(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
	}

	@Test
	// sut.bookRide: El número de asientos a reservar es el 1 más del máximo
	// disponibles. Debe devolver false.
	public void test16() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate=null;
		boolean result = false;
		
		Driver d = null;
		Ride r = null;
		Traveler t = null;
		
		String username = "Haizea";
		String driverUsername = "Javier";
		String from = "Donostia";
		String to = "Bilbao";
		int nPlaces = 10;
		double desk = 20.0;
		
		try {
			rideDate = sdf.parse("11/03/2026");
			
			t = new Traveler(username, "123");
			t.setMoney(200);
			d = new Driver(driverUsername, "456");
			r = new Ride(from, to, rideDate, nPlaces, desk, d);
			
			//Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(t));
			
			//Simulación de que existe r
			Mockito.when(db.find(Ride.class, r.getRideNumber())).thenReturn(r);
			
			//Se llama al método bookRide, el número de asientos es 11 (hay 10 disponibles)
			sut.open();
			result = sut.bookRide(username, r, 11, 1.0);
			sut.close();
			
			//Resultado esperado: False
			assertFalse(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
	}

	
	
	//TEST DESK
	
	
	@Test
	//sut.bookRide: El descuento a aplicar es negativo. Debe devolver false.
	public void test17() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		boolean result = false;

		Driver d = null;
		Ride r = null;
		Traveler t = null;

		String username = "Haizea";
		String driverUsername = "Javier";
		String from = "Donostia";
		String to = "Bilbao";
		int nPlaces = 10;
		double desk = 20.0;

		try {
			rideDate = sdf.parse("11/03/2026");

			t = new Traveler(username, "123");
			t.setMoney(200);
			d = new Driver(driverUsername, "456");
			r = new Ride(from, to, rideDate, nPlaces, desk, d);

			// Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(t));

			// Simulación de que existe r
			Mockito.when(db.find(Ride.class, r.getRideNumber())).thenReturn(r);

			//Se llama al método bookRide, el descuento es negativo: -1.0
			sut.open();
			result = sut.bookRide(username, r, 1, -1.0);
			sut.close();

			// Resultado esperado: False
			assertFalse(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
	}
	
	@Test
	//sut.bookRide: El descuento a aplicar es 0. Debe devolver true.
	public void test18() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		boolean result = false;

		Driver d = null;
		Ride r = null;
		Traveler t = null;

		String username = "Haizea";
		String driverUsername = "Javier";
		String from = "Donostia";
		String to = "Bilbao";
		int nPlaces = 10;
		double desk = 20.0;

		try {
			rideDate = sdf.parse("11/03/2026");

			t = new Traveler(username, "123");
			t.setMoney(200);
			d = new Driver(driverUsername, "456");
			r = new Ride(from, to, rideDate, nPlaces, desk, d);

			// Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(t));

			// Simulación de que existe r
			Mockito.when(db.find(Ride.class, r.getRideNumber())).thenReturn(r);

			//Se llama al método bookRide, el descuento es: 0.0
			sut.open();
			result = sut.bookRide(username, r, 1, 0.0);
			sut.close();

			// Resultado esperado: True
			assertTrue(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
	}
	
	@Test
	//sut.bookRide: El descuento a aplicar es 1 mayor al mínimo. Debe devolver true.
	public void test19() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		boolean result = false;

		Driver d = null;
		Ride r = null;
		Traveler t = null;

		String username = "Haizea";
		String driverUsername = "Javier";
		String from = "Donostia";
		String to = "Bilbao";
		int nPlaces = 10;
		double desk = 20.0;

		try {
			rideDate = sdf.parse("11/03/2026");

			t = new Traveler(username, "123");
			t.setMoney(200);
			d = new Driver(driverUsername, "456");
			r = new Ride(from, to, rideDate, nPlaces, desk, d);

			// Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(t));

			// Simulación de que existe r
			Mockito.when(db.find(Ride.class, r.getRideNumber())).thenReturn(r);

			//Se llama al método bookRide, el descuento es: 1.0
			sut.open();
			result = sut.bookRide(username, r, 1, 1.0);
			sut.close();

			// Resultado esperado: True
			assertTrue(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
	}
	
	@Test
	//sut.bookRide: El descuento a aplicar es 1 menos que el precio total a pagar. Debe devolver true.
	public void test20() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		boolean result = false;

		Driver d = null;
		Ride r = null;
		Traveler t = null;

		String username = "Haizea";
		String driverUsername = "Javier";
		String from = "Donostia";
		String to = "Bilbao";
		int nPlaces = 10;
		double desk = 20.0;

		try {
			rideDate = sdf.parse("11/03/2026");

			t = new Traveler(username, "123");
			t.setMoney(200);
			d = new Driver(driverUsername, "456");
			r = new Ride(from, to, rideDate, nPlaces, desk, d);

			// Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(t));

			// Simulación de que existe r
			Mockito.when(db.find(Ride.class, r.getRideNumber())).thenReturn(r);

			//Se llama al método bookRide, el descuento es: 19.0
			sut.open();
			result = sut.bookRide(username, r, 1, 19.0);
			sut.close();

			// Resultado esperado: True
			assertTrue(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
	}
	
	@Test
	//sut.bookRide: El descuento a aplicar es el precio total a pagar. Debe devolver false.
	public void test21() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		boolean result = false;

		Driver d = null;
		Ride r = null;
		Traveler t = null;

		String username = "Haizea";
		String driverUsername = "Javier";
		String from = "Donostia";
		String to = "Bilbao";
		int nPlaces = 10;
		double desk = 20.0;

		try {
			rideDate = sdf.parse("11/03/2026");

			t = new Traveler(username, "123");
			t.setMoney(200);
			d = new Driver(driverUsername, "456");
			r = new Ride(from, to, rideDate, nPlaces, desk, d);

			// Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(t));

			// Simulación de que existe r
			Mockito.when(db.find(Ride.class, r.getRideNumber())).thenReturn(r);

			//Se llama al método bookRide, el descuento es: 20.0
			sut.open();
			result = sut.bookRide(username, r, 1, 20.0);
			sut.close();

			// Resultado esperado: False
			assertFalse(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
	}
	
	@Test
	//sut.bookRide: El descuento a aplicar es 1 más que el precio total a pagar. Debe devolver false.
	public void test22() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		boolean result = false;

		Driver d = null;
		Ride r = null;
		Traveler t = null;

		String username = "Haizea";
		String driverUsername = "Javier";
		String from = "Donostia";
		String to = "Bilbao";
		int nPlaces = 10;
		double desk = 20.0;

		try {
			rideDate = sdf.parse("11/03/2026");

			t = new Traveler(username, "123");
			t.setMoney(200);
			d = new Driver(driverUsername, "456");
			r = new Ride(from, to, rideDate, nPlaces, desk, d);

			// Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(t));

			// Simulación de que existe r
			Mockito.when(db.find(Ride.class, r.getRideNumber())).thenReturn(r);

			//Se llama al método bookRide, el descuento es: 21.0
			sut.open();
			result = sut.bookRide(username, r, 1, 21.0);
			sut.close();

			// Resultado esperado: False
			assertFalse(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
	}

	
	
	//TEST MONEY
	
	
	@Test
	//sut.bookRide: El viajero tiene 1 menos que el total a pagar. Debe devolver false.
	public void test23() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		boolean result = false;

		Driver d = null;
		Ride r = null;
		Traveler t = null;

		String username = "Haizea";
		String driverUsername = "Javier";
		String from = "Donostia";
		String to = "Bilbao";
		int nPlaces = 10;
		double desk = 20.0;

		try {
			rideDate = sdf.parse("11/03/2026");

			t = new Traveler(username, "123");
			t.setMoney(199);
			d = new Driver(driverUsername, "456");
			r = new Ride(from, to, rideDate, nPlaces, desk, d);

			// Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(t));

			// Simulación de que existe r
			Mockito.when(db.find(Ride.class, r.getRideNumber())).thenReturn(r);

			// Se llama al método bookRide, el viajero no tiene suficiente dinero para pagar
			sut.open();
			result = sut.bookRide(username, r, 10, 0.0);
			sut.close();

			// Resultado esperado: False
			assertFalse(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
	}
	
	@Test
	//sut.bookRide: El viajero tiene justo que el total a pagar. Debe devolver true.
	public void test24() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		boolean result = false;

		Driver d = null;
		Ride r = null;
		Traveler t = null;

		String username = "Haizea";
		String driverUsername = "Javier";
		String from = "Donostia";
		String to = "Bilbao";
		int nPlaces = 10;
		double desk = 20.0;

		try {
			rideDate = sdf.parse("11/03/2026");

			t = new Traveler(username, "123");
			t.setMoney(200);
			d = new Driver(driverUsername, "456");
			r = new Ride(from, to, rideDate, nPlaces, desk, d);

			// Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(t));

			// Simulación de que existe r
			Mockito.when(db.find(Ride.class, r.getRideNumber())).thenReturn(r);

			// Se llama al método bookRide, el viajero tiene suficiente dinero para pagar
			sut.open();
			result = sut.bookRide(username, r, 10, 0.0);
			sut.close();

			// Resultado esperado: True
			assertTrue(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
	}
	
	@Test
	//sut.bookRide: El viajero tiene 1 mas que el total a pagar. Debe devolver true.
	public void test25() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		boolean result = false;

		Driver d = null;
		Ride r = null;
		Traveler t = null;

		String username = "Haizea";
		String driverUsername = "Javier";
		String from = "Donostia";
		String to = "Bilbao";
		int nPlaces = 10;
		double desk = 20.0;

		try {
			rideDate = sdf.parse("11/03/2026");

			t = new Traveler(username, "123");
			t.setMoney(201);
			d = new Driver(driverUsername, "456");
			r = new Ride(from, to, rideDate, nPlaces, desk, d);

			// Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(t));

			// Simulación de que existe r
			Mockito.when(db.find(Ride.class, r.getRideNumber())).thenReturn(r);

			// Se llama al método bookRide, el viajero tiene suficiente dinero para pagar
			sut.open();
			result = sut.bookRide(username, r, 10, 0.0);
			sut.close();

			// Resultado esperado: True
			assertTrue(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
	}

}
