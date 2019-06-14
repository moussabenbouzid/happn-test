package fr.happn.poi.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import fr.happn.poi.model.Poi;
import fr.happn.poi.model.Result;

@Component
public class PoiServiceImpl implements PoiService {
	
    private static final Logger logger = LogManager.getLogger(PoiServiceImpl.class);

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
	public Result getMostFilledAreas(int nbZones) {
		
		
		return null;
	}

}
