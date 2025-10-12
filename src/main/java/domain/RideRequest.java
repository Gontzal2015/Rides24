package domain;

import java.sql.Date;

public class RideRequest {
    private String from;
    private String to;
    private Date date;
    private int nPlaces;
    private float price;
    private String driverName;
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getnPlaces() {
		return nPlaces;
	}
	public void setnPlaces(int nPlaces) {
		this.nPlaces = nPlaces;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
}
