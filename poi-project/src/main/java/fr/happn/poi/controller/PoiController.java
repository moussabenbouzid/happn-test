package fr.happn.poi.controller;


import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fr.happn.poi.model.Area;
import fr.happn.poi.model.Poi;
import fr.happn.poi.service.PoiService;

@RestController
public class PoiController {

	private static final Logger logger = LogManager.getLogger(PoiController.class);
	private static final String POI_ATTRIBUTE = "listPois";

	@Autowired
	private PoiService service;

	/**
	 * Parse un fichier d entree au format TSV
	 * @param multipart fichier d entree a parser au format TSV
	 * @param request requete HTTP
	 * @param response reponse HTTP
	 * @return liste de POI (points d interets)
	 */
	@PostMapping("/parse")
	public List<Poi> parsePoi(@RequestBody MultipartFile multipart, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("DEBUT : Methode parse");
		List<Poi> listPois = service.parsePoi(multipart);
		request.getSession().setAttribute(POI_ATTRIBUTE, listPois);
		logger.debug("FIN : Methode parse");
		return listPois;
	}
	

	/**
	 * Calcul du nombre de POI dans une zone 
	 * @param minLat latitude minimale
	 * @param mintLon longitude minimale
	 * @param request requete HTTP
	 * @param response reponse HTTP
	 * @return le nombre de POI (points d interets) dans la zone
	 */
	@GetMapping("/poisbyzone/{minLat}/{mintLon}")
	public Double calculPoisByZone(@PathVariable float minLat, @PathVariable float mintLon, HttpServletRequest request, HttpServletResponse response){
		logger.debug("DEBUT : Methode poisbyzone");
		List<Poi> listPois = (List<Poi>) request.getSession().getAttribute(POI_ATTRIBUTE);
		if(listPois == null) {
			response.setStatus(HttpStatus.NO_CONTENT.value());
			logger.debug("FIN : Methode poisbyzone");
			return null;
		}
		double compteur = service.calculPoisByZone(minLat, mintLon, listPois);
		logger.debug("FIN : Methode poisbyzone");
		return compteur;
	}
	/**
	 * Recupere les n zones les plus denses
	 * @param nbZones le nombre de zones les plus denses a recuperer
	 * @param request requete HTTP
	 * @param response reponse HTTP
	 * @return les n zones les plus denses
	 */
	@GetMapping("mostfilledareas/{nbZones}")
	public Set<Area> getMostFilledAreas(@PathVariable int nbZones, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("DEBUT : Methode mostfilledareas");
		List<Poi> listPois = (List<Poi>) request.getSession().getAttribute(POI_ATTRIBUTE);
		if(listPois == null) {
			response.setStatus(HttpStatus.NO_CONTENT.value());
			logger.debug("FIN : Methode mostfilledareas");
			return null;
		}
		Set<Area> result = service.getMostFilledAreas(nbZones, listPois);
		logger.debug("FIN : Methode mostfilledareas");
		return result;
	}





}
