package fr.happn.poi.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import fr.happn.poi.model.Poi;
import fr.happn.poi.model.Result;

public interface PoiService {

	public List<Poi> parsePoi(MultipartFile multipart);
	public Result calculPoisByZone(float minLat, float maxLat, float mintLon, float maxLon, List<Poi> listPois);
	public Result getMostFilledAreas(int nbZones);
	
	
}
