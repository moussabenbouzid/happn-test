package fr.happn.poi.service;

import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import fr.happn.poi.model.Area;
import fr.happn.poi.model.Poi;

public interface PoiService {

	public List<Poi> parsePoi(MultipartFile multipart);
	public Double calculPoisByZone(float minLat, float minLon, List<Poi> listPois);
	public Set<Area> getMostFilledAreas(int nbZones, List<Poi> listPois);
	
}
