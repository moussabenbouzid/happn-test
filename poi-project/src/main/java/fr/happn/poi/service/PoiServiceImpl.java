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

import fr.happn.poi.model.Poi;
import fr.happn.poi.model.PoiRound;
import fr.happn.poi.model.Result;

@Component
public class PoiServiceImpl implements PoiService {
	
    private static final Logger logger = LogManager.getLogger(PoiServiceImpl.class);
    private static final String SEPARATOR_COMMA = ", ";
    private static final String SEPARATOR_COLONS = ":";
    private static final String MIN_LAT = "minLat";
    private static final String MAX_LAT = "maxLat";
    private static final String MIN_LON = "minLon";
    private static final String MAX_LON = "maxLon";

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
				String[] split = line.split(" ");
				Poi poi = new Poi(split[0], Float.parseFloat(split[1]), Float.parseFloat(split[2]));
				result.add(poi);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	@Override
	public Result calculPoisByZone(float minLat, float maxLat, float minLon, float maxLon, List<Poi> listPois) {
		
		List<Poi> poisByZone = new ArrayList<>();
		for(Poi poi : listPois) {
			if(poi.getLat() > minLat && poi.getLat() < maxLat && poi.getLon() > minLon && poi.getLon() < maxLon) {
				poisByZone.add(poi);
			}
		}
		return new Result(poisByZone, poisByZone.size());
	}

	@Override
	public Set<String> getMostFilledAreas(int nbZones, List<Poi> listPois) {
		
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
		}
		for(PoiRound poiRound : listPoiRounded) {
			System.out.println(poiRound.toString());
		}
		Map<String, Integer> mapPoiSorted = this.countAreasFilled(listPoiRounded);
		mapPoiSorted.forEach((k,v)->System.out.println("Key : " + k + " Value : " + v));
		Map<String , Integer> myNewMap = mapPoiSorted.entrySet().stream()
			    .limit(nbZones)
			    .collect(LinkedHashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);
		
		return myNewMap.keySet();
	}
	
	private float roundLowHalf(float value) {
		value = value < 0?value<-0.5f?-1f:-0.5f:value<0.5?0f:0.5f;
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
		DecimalFormat df = (DecimalFormat)nf;
		df.setMaximumFractionDigits(1);
		return Float.valueOf(df.format(value));
	}
	
	private float getDecimalValue(float value) {
		value = value - (int)value;
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
		DecimalFormat df = (DecimalFormat)nf;
		df.setMaximumFractionDigits(1);
		return Float.valueOf(df.format(value));
	}
	
	
	private Map<String, Integer> countAreasFilled(List<PoiRound> listPoiRound){
		Map<String, Integer> mapCountAreasFilled = new HashMap<>();
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
		LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();
		mapCountAreasFilled.entrySet()
	    .stream()
	    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
	    .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
		return reverseSortedMap;
	}
	
	private Map<String, Integer> fillMapAreas(Map<String, Integer> mapCountAreasFilled, float latMin, float latMax, float lonMin, float lonMax){
		mapCountAreasFilled.put(MIN_LAT+ SEPARATOR_COLONS +latMin+ SEPARATOR_COMMA + MAX_LAT + SEPARATOR_COLONS +latMax
				+SEPARATOR_COMMA +MIN_LON+ SEPARATOR_COLONS+lonMin+SEPARATOR_COMMA+MAX_LON+ SEPARATOR_COLONS +lonMax,
				mapCountAreasFilled.get(MIN_LAT + SEPARATOR_COLONS +latMin+SEPARATOR_COMMA+MAX_LAT+ SEPARATOR_COLONS +latMax
						+SEPARATOR_COMMA+MIN_LON+ SEPARATOR_COLONS + lonMin + SEPARATOR_COMMA + MAX_LON + SEPARATOR_COLONS + lonMax) != null? 
						mapCountAreasFilled.get(MIN_LAT + SEPARATOR_COLONS + latMin + SEPARATOR_COMMA + MAX_LAT + SEPARATOR_COLONS + latMax 
								+SEPARATOR_COMMA + MIN_LON + SEPARATOR_COLONS + lonMin + SEPARATOR_COMMA + MAX_LON + SEPARATOR_COLONS + lonMax)+1:1);
		return mapCountAreasFilled;
		}


}
