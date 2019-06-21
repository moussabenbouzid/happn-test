package fr.happn.poi.model;

import java.io.Serializable;

public class Poi implements Serializable {

	private static final long serialVersionUID = 1L;
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
	
	
	public Poi(float lat, float lon) {
		super();
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

	@Override
	public String toString() {
		return "Poi [id=" + id + ", lat=" + lat + ", lon=" + lon + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + Float.floatToIntBits(lat);
		result = prime * result + Float.floatToIntBits(lon);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Poi other = (Poi) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (Float.floatToIntBits(lat) != Float.floatToIntBits(other.lat))
			return false;
		if (Float.floatToIntBits(lon) != Float.floatToIntBits(other.lon))
			return false;
		return true;
	}
	
	
}
