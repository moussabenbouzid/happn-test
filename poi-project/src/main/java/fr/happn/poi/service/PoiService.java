package fr.happn.poi.service;

import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import fr.happn.poi.model.Poi;
import fr.happn.poi.model.Result;

public interface PoiService {

	public List<Poi> parsePoi(MultipartFile multipart);
	public Result calculPoisByZone(float minLat, float minLon, List<Poi> listPois);
	public Set<String> getMostFilledAreas(int nbZones, List<Poi> listPois);
	
	
}
