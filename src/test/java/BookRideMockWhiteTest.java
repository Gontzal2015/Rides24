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

public class BookRideMockWhiteTest {

static DataAccess sut;
	
	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected  EntityManagerFactory entityManagerFactory;
	@Mock
	protected  EntityManager db;
	@Mock
    protected  EntityTransaction  et;
	

	@Before
    public  void init() {
        MockitoAnnotations.openMocks(this);
        persistenceMock = Mockito.mockStatic(Persistence.class);
		persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
        .thenReturn(entityManagerFactory);
        
        Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
		Mockito.doReturn(et).when(db).getTransaction();
	    sut=new DataAccess(db);
    }
	@After
    public  void tearDown() {
		persistenceMock.close();
    }
	
	
	@Mock
	protected TypedQuery<Traveler> typedQueryTraveler;
	
	
	@Test
	//sut.bookRide: Se produce una excepción al ejecutar el método. Debe devolver false.
	public void test1() {
		boolean result = false;
		try {
			
			//Simulación lanzamiento excepción
			Mockito.when(db.createQuery(Mockito.anyString(), Mockito.eq(Traveler.class))).thenThrow(new RuntimeException("Error simulado"));
			
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
			
			//Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.emptyList());
			
			//Simulación de que existe r
			Mockito.when(db.find(Ride.class, r.getRideNumber())).thenReturn(r);
			
			//Se llama al método bookRide con un traveler que no existe en la BD
			sut.open();
			result = sut.bookRide(username, r, 1, 1.0);
			sut.close();
			
			//Resultado esperado: False
			assertFalse(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
		
	}
	
	@Test
	//sut.bookRide: El viajero existe en la BD, pero pide más asientos de los disponibles. Debe devolver false.
	public void test3() {
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
		int nPlaces = 3;
		double desk = 5.0;
		
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
			
			//Se llama al método bookRide con un traveler que pide más asientos que los disponibles
			sut.open();
			result = sut.bookRide(username, r, 5, 1.0);
			sut.close();
			
			//Resultado esperado: False
			assertFalse(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
	}
	
	@Test
	//sut.bookRide: El viajero existe en la BD pero no tiene suficiente dinero para pagar. Debe devolver false.
	public void test4() {
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
		int nPlaces = 3;
		double desk = 5.0;
		
		try {
			rideDate = sdf.parse("11/03/2026");
			
			t = new Traveler(username, "123");
			t.setMoney(0);
			d = new Driver(driverUsername, "456");
			r = new Ride(from, to, rideDate, nPlaces, desk, d);
			
			//Simulación de getTraveler()
			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.setParameter("username", username)).thenReturn(typedQueryTraveler);
			Mockito.when(typedQueryTraveler.getResultList()).thenReturn(Collections.singletonList(t));
			
			//Simulación de que existe r
			Mockito.when(db.find(Ride.class, r.getRideNumber())).thenReturn(r);
			
			//Se llama al método bookRide 
			sut.open();
			result = sut.bookRide(username, r, 1, 1.0);
			sut.close();
			
			//Resultado esperado: False
			assertFalse(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}

	}
	
	@Test
	//sut.bookRide: Todos los datos son correctos. Debe devolver true.
	public void test5() {
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
		int nPlaces = 3;
		double desk = 5.0;
		
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
			
			//Se llama al método bookRide 
			sut.open();
			result = sut.bookRide(username, r, 1, 1.0);
			sut.close();
			
			//Resultado esperado: True
			assertTrue(result);
		} catch (Exception e) {
			sut.close();
			fail();
		}
	}

	
	
}
