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
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

public class GetRidesByDriverMockBlackTest {
	
static DataAccess sut;
	
	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected  EntityManagerFactory entityManagerFactory;
	@Mock
	protected  EntityManager db;
	@Mock
    protected  EntityTransaction  et;
	
	private String nombre;
	private String pass;
	
	private Driver driver;

	private List<Ride> rides;
	


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
	
	
	@Test
	// Caso en el que el conductor si tiene viajes activos
	public void test1() {
	    nombre = "Iñigo";
	    pass = "1234";
	    List<Ride> r = new ArrayList<Ride>();

		String rideFrom="Donostia";
		String rideTo="Zarautz";
		
		String driverUserName=null;

		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate=null;
		try {
			rideDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    driver = new Driver(nombre, pass);
	    Ride viaje= new Ride(rideFrom, rideTo, rideDate, 0, 0, driver);

	    r.add(viaje);

	    driver.setCreatedRides(r);

	    @SuppressWarnings("unchecked")
	    TypedQuery<Driver> query = (TypedQuery<Driver>) Mockito.mock(TypedQuery.class);

	    try {
	        Mockito.when(db.createQuery(Mockito.anyString(), Mockito.eq(Driver.class))).thenReturn(query);
	        Mockito.when(query.getSingleResult()).thenReturn(driver);

	        sut.open();
	        rides = sut.getRidesByDriver(nombre);
	        sut.close();

	        assertEquals(r, rides);
	    } catch (Exception e) {
	        e.printStackTrace();
	        fail();
	    } finally {
	        sut.close();
	    }
	}
	
	@Test
	//Caso en el que pasamos un argumento null
	public void test2() {
		nombre=null;
		try {
		
		//invoke System Under Test (sut)  
		sut.open();
		rides= sut.getRidesByDriver(nombre);
		sut.close();
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
	//Caso en el que el conductor no existe en la base de datos
	public void test3() {
		nombre="Eider";
		
	    @SuppressWarnings("unchecked")
	    TypedQuery<Driver> query = (TypedQuery<Driver>) Mockito.mock(TypedQuery.class);
		try {


        Mockito.when(db.createQuery(Mockito.anyString(), Mockito.eq(Driver.class))).thenReturn(query);
        Mockito.when(query.getSingleResult()).thenReturn(null);
		
		//invoke System Under Test (sut)  
		sut.open();
		rides= sut.getRidesByDriver(nombre);
		sut.close();
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
	// Caso en el que el conductor no tiene viajes en la base de datos
	public void test4() {
	    nombre = "Leire";
	    pass = "1234";
	    List<Ride> r = new ArrayList<Ride>();
	    driver = new Driver(nombre, pass);
	    driver.setCreatedRides(r);

	    @SuppressWarnings("unchecked")
	    TypedQuery<Driver> query = (TypedQuery<Driver>) Mockito.mock(TypedQuery.class);

	    try {
	        Mockito.when(db.createQuery(Mockito.anyString(), Mockito.eq(Driver.class))).thenReturn(query);
	        Mockito.when(query.getSingleResult()).thenReturn(driver);

	        sut.open();
	        rides = sut.getRidesByDriver(nombre);
	        sut.close();

	        assertEquals(r, rides);
	    } catch (Exception e) {
	        e.printStackTrace();
	        fail();
	    } finally {
	        sut.close();
	    }
	}
	
	@Test
	// Caso en el que el conductor no tiene viajes activos
	public void test5() {
	    nombre = "Xiomara";
	    pass = "1234";
	    List<Ride> r = new ArrayList<Ride>();
	    List<Ride> r2 = new ArrayList<Ride>();
		String rideFrom="Donostia";
		String rideTo="Zarautz";
		
		String driverUserName=null;

		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate=null;;
		try {
			rideDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    driver = new Driver(nombre, pass);
	    Ride viaje= new Ride(rideFrom, rideTo, rideDate, 0, 0, driver);
	    viaje.setActive(false);
	    r2.add(viaje);

	    driver.setCreatedRides(r2);

	    @SuppressWarnings("unchecked")
	    TypedQuery<Driver> query = (TypedQuery<Driver>) Mockito.mock(TypedQuery.class);

	    try {
	        Mockito.when(db.createQuery(Mockito.anyString(), Mockito.eq(Driver.class))).thenReturn(query);
	        Mockito.when(query.getSingleResult()).thenReturn(driver);

	        sut.open();
	        rides = sut.getRidesByDriver(nombre);
	        sut.close();

	        assertEquals(r, rides);
	    } catch (Exception e) {
	        e.printStackTrace();
	        fail();
	    } finally {
	        sut.close();
	    }
	}
	
	
	

}
