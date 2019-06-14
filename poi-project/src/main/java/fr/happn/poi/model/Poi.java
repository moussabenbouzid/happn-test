package fr.happn.poi.model;

public class Poi {

	private String id;
	private float lat;
	private float lon;
	
	public Poi() {}
	
	public Poi(String id, float lat, float lon) {
		super();
		this.id = id;
		this.lat = lat;
		this.lon = lon;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public float getLat() {
		return lat;
	}
	public void setLat(float lat) {
		this.lat = lat;
	}
	public float getLon() {
		return lon;
	}
	public void setLon(float lon) {
		this.lon = lon;
	}
	
	
}
