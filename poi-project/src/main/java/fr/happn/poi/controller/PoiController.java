package fr.happn.poi.controller;


import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fr.happn.poi.model.Poi;
import fr.happn.poi.model.Result;
import fr.happn.poi.service.PoiService;
import fr.happn.poi.service.PoiServiceImpl;

@RestController
public class PoiController {

	private static final Logger logger = LogManager.getLogger(PoiController.class);

	@Autowired
	private PoiService service;

	@PostMapping("/parse")
	public List<Poi> parsePoi(@RequestBody MultipartFile multipart, HttpServletRequest request) {

		List<Poi> listPois = service.parsePoi(multipart);
		request.getSession().setAttribute("listPois", listPois);
		return listPois;

	}
	
	@GetMapping("/getAll")
	public List<Poi> getAllPois(HttpServletRequest request){
		return (List<Poi>) request.getSession().getAttribute("listPois");
	}
	
	@GetMapping("/getById/{id}")
	public Poi getPoiById(@PathVariable String id, HttpServletRequest request){
		List<Poi> listPois = (List<Poi>) request.getSession().getAttribute("listPois");
		return listPois.get(0);
	}
	

	@GetMapping("/poisbyzone/{minLat}/{maxLat}/{mintLon}/{maxLon}")
	public Result calculPoisByZone(@PathVariable float minLat,@PathVariable float maxLat,
			@PathVariable float mintLon,@PathVariable float maxLon, HttpServletRequest request){

		List<Poi> listPois = (List<Poi>) request.getSession().getAttribute("listPois");
		if(listPois == null) {
			return new Result();
		}
		return service.calculPoisByZone(minLat, maxLat, mintLon, maxLon, listPois);
	}
	
	@GetMapping("MostFilledAreas/{nbZones}")
	public Set<String> getMostFilledAreas(@PathVariable int nbZones, HttpServletRequest request) {
		List<Poi> listPois = (List<Poi>) request.getSession().getAttribute("listPois");
		if(listPois == null) {
			return null;
		}
		return service.getMostFilledAreas(nbZones, listPois);
	}





}
