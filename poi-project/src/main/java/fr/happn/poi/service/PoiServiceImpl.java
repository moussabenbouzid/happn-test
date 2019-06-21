package fr.happn.poi.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import fr.happn.poi.model.Area;
import fr.happn.poi.model.Poi;
import fr.happn.poi.model.PoiRound;

@Component
public class PoiServiceImpl implements PoiService {
	
    private static final Logger logger = LogManager.getLogger(PoiServiceImpl.class);
    private static final String SEPARATOR_TAB = " ";

    /**
     * Parse un fichier d entree au format TSV
     * @param multipart fichier d entree a parser au format TSV
     * @return liste de POI (points d interet)
     */
	@Override
	public List<Poi> parsePoi(MultipartFile multipart) {

		BufferedReader br;
		List<Poi> result = new ArrayList<>();
		try {
			String line;
			boolean isFirstLine = true;
			InputStream is = multipart.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			br.skip(1);
			while ((line = br.readLine()) != null) {
				if(isFirstLine) {
					isFirstLine = false;
					continue;
				}
				String[] split = line.split(SEPARATOR_TAB);
				Poi poi = new Poi(split[0], Float.parseFloat(split[1]), Float.parseFloat(split[2]));
				result.add(poi);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	/**
	 * Calcul du nombre de POI (points d interets) dans une zone 
	 * @param minLat latitude minimale
	 * @param minLon longitude minimale
	 * @param listPois les points d interet
	 * @return les n zones les plus denses
	 */
	@Override
	public Double calculPoisByZone(float minLat, float minLon, List<Poi> listPois) {
		double compteur = 0;
		for(Poi poi : listPois) {
			if(poi.getLat() > minLat && poi.getLon() > minLon) {
				compteur++;
			}
		}
		return compteur;
	}
	
	/**
	 * Recupere les n zones les plus denses
	 * @param nbZones le nombre de zones les plus denses a recuperer
	 * @param listPois les points d interet
	 * @return les n zones les plus denses
	 */
	@Override
	public Set<Area> getMostFilledAreas(int nbZones, List<Poi> listPois) {
		
		List<PoiRound> listPoiRounded = new ArrayList<>();
		for(Poi poi : listPois) {
			int intPart = (int)poi.getLat();
			float latRounded = intPart + this.roundLowHalf(this.getDecimalValue(poi.getLat()));
			float lonRounded = (int)poi.getLon() + this.roundLowHalf(this.getDecimalValue(poi.getLon()));
			PoiRound poiRound = new PoiRound();
			poiRound.setPoi(new Poi(latRounded, lonRounded));
			poiRound.setLatRound(latRounded != poi.getLat()?true:false);
			poiRound.setLonRound(lonRounded != poi.getLon()?true:false);
			listPoiRounded.add(poiRound);
			logger.debug(poiRound.toString());
		}
		Map<Area, Integer> mapPoiSorted = this.countAreasFilled(listPoiRounded);
		mapPoiSorted.forEach((k,v)->logger.debug("Key : " + k.toString() + " Value : " + v));
		Map<Area , Integer> result = mapPoiSorted.entrySet().stream()
			    .limit(nbZones)
			    .collect(LinkedHashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);
		
		return result.keySet();
	}
	
	/**
	 * Arrondit un nombre decimal au demi entier inferieur le plus proche
	 * @param value le decimal a arrondir
	 * @return le decimal arrondi
	 */
	private float roundLowHalf(float value) {
		value = value < 0?value<-0.5f?-1f:-0.5f:value<0.5?0f:0.5f;
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
		DecimalFormat df = (DecimalFormat)nf;
		df.setMaximumFractionDigits(1);
		return Float.valueOf(df.format(value));
	}
	
	/**
	 * Recupere la partie decimale d un nombre decimal
	 * @param value le nombre decimal
	 * @return la partie decimale du nombre decimal
	 */
	private float getDecimalValue(float value) {
		value = value - (int)value;
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
		DecimalFormat df = (DecimalFormat)nf;
		df.setMaximumFractionDigits(1);
		return Float.valueOf(df.format(value));
	}
	
	/**
	 * recupere les zones et le nombre de POI (points d interets) pour chaque zone
	 * @param listPoiRound
	 * @return les zones et le nombre de POI (points d interets) dans chacune
	 */
	private Map<Area, Integer> countAreasFilled(List<PoiRound> listPoiRound){
		Map<Area, Integer> mapCountAreasFilled = new HashMap<>();
		for(PoiRound poiRound : listPoiRound) {
			float lat = poiRound.getPoi().getLat();
			float latMin = lat-0.5f;
			float latMax = lat+0.5f;
			float lon = poiRound.getPoi().getLon();
			float lonMin = lon - 0.5f;
			float lonMax = lon + 0.5f;
			if(poiRound.isLatRound() && poiRound.isLonRound()) {
				fillMapAreas(mapCountAreasFilled, lat, latMax, lon, lonMax);
			}
			else if(!poiRound.isLatRound() && poiRound.isLonRound()) {
				fillMapAreas(mapCountAreasFilled, latMin, lat, lon, lonMax);
				fillMapAreas(mapCountAreasFilled, lat, latMax, lon, lonMax);
			}
			else if(poiRound.isLatRound() && !poiRound.isLonRound()) {
				fillMapAreas(mapCountAreasFilled, lat, latMax, lonMin, lon);
				fillMapAreas(mapCountAreasFilled, lat, latMax, lon, lonMax);
			}
			else {
				fillMapAreas(mapCountAreasFilled, latMin, lat, lon, lonMax);
				fillMapAreas(mapCountAreasFilled, lat, latMax, lon, lonMax);
				fillMapAreas(mapCountAreasFilled, lat, latMax, lonMin, lon);
				fillMapAreas(mapCountAreasFilled, latMin, lat, lonMin, lon);
			}
			
		}
		LinkedHashMap<Area, Integer> reverseSortedMap = new LinkedHashMap<>();
		mapCountAreasFilled.entrySet()
	    .stream()
	    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
	    .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
		return reverseSortedMap;
	}
	
	/**
	 * Renseigne les informations d une zone avec le nombre de POI associes
	 * @param mapCountAreasFilled la map contenant les zones et le nombre points d interets associes
	 * @param latMin latitude minimale 
	 * @param latMax latitude maximale
	 * @param lonMin longitude minimale
	 * @param lonMax longitude maximale
	 * @return la map remplie avec la nouvelle zone et le nombre de POI dedans
	 */
	private Map<Area, Integer> fillMapAreas(Map<Area, Integer> mapCountAreasFilled, float latMin, float latMax, float lonMin, float lonMax){
		Area area = new Area(latMin, latMax, lonMin, lonMax);
		mapCountAreasFilled.put(area, mapCountAreasFilled.get(area) != null? mapCountAreasFilled.get(area)+1:1);
		return mapCountAreasFilled;
		}

}
