package adapter;

import javax.swing.table.AbstractTableModel;

import domain.Driver;
import domain.Ride;

import java.util.List;

public class DriverAdapter extends AbstractTableModel {

    private Driver driver;
    private List<Ride> rides;

    private final String[] columnNames = {
            "Origin", "Destination", "Date", "Places", "Price"
    };

    public DriverAdapter(Driver driver) {
        this.driver = driver;
        this.rides = driver.getCreatedRides(); 
    }

    @Override
    public int getRowCount() {
        return rides.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        Ride r = rides.get(row);

        switch (col) {
            case 0: return r.getFrom();
            case 1: return r.getTo();
            case 2: return r.getDate();
            case 3: return r.getnPlaces();
            case 4: return r.getPrice();
            default: return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int col) {
        switch (col) {
            case 2: return java.util.Date.class;
            case 3: return Integer.class;
            case 4: return Double.class;
            default: return String.class;
        }
    }
}
