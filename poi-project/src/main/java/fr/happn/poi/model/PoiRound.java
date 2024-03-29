package fr.happn.poi.model;

import org.springframework.stereotype.Repository;

@Repository
public class PoiRound {
	
	private boolean isLatRound;
	private boolean isLonRound;
	private Poi poi;
	public boolean isLatRound() {
		return isLatRound;
	}
	public void setLatRound(boolean isLatRound) {
		this.isLatRound = isLatRound;
	}
	public boolean isLonRound() {
		return isLonRound;
	}
	public void setLonRound(boolean isLonRound) {
		this.isLonRound = isLonRound;
	}
	public PoiRound(boolean isLatRound, boolean isLonRound) {
		super();
		this.isLatRound = isLatRound;
		this.isLonRound = isLonRound;
	}
	public PoiRound() {
		super();
	}
	public Poi getPoi() {
		return poi;
	}
	public void setPoi(Poi poi) {
		this.poi = poi;
	}
	@Override
	public String toString() {
		return "PoiRound [isLatRound=" + isLatRound + ", isLonRound=" + isLonRound + ", poi=" + poi + "]";
	}
	
	

}
