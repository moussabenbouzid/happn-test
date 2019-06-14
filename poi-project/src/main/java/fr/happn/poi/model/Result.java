package fr.happn.poi.model;

import java.util.List;

public class Result {

	private List<Poi> listPoi;
	private int nbZones;
	
	public Result() {}
	
	public Result(List<Poi> listPoi, int nbZones) {
		super();
		this.listPoi = listPoi;
		this.nbZones = nbZones;
	}
	public List<Poi> getListPoi() {
		return listPoi;
	}
	public void setListPoi(List<Poi> listPoi) {
		this.listPoi = listPoi;
	}
	public int getNbZones() {
		return nbZones;
	}
	public void setNbZones(int nbZones) {
		this.nbZones = nbZones;
	}
	
	
}
