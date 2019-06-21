package fr.happn.poi;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import fr.happn.poi.controller.PoiController;
import fr.happn.poi.model.Poi;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PoiControllerTest {
	
	@Autowired
	PoiController poiCtrl;
	
	HttpServletRequest request;
	HttpServletResponse response;
	
	File file;
	MultipartFile multipart;
	
	private static List<Poi> listPois;
	private static Set<String> set;
	
	@Before
	public void init() {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		initializePois();
	}

	@Test
	public void parseTest() throws IOException {
		file = new File("src/main/resources/static/poi.tsv");
		InputStream targetStream = new FileInputStream(file);
	    multipart =  new MockMultipartFile("tsvFile", targetStream);
	    List<Poi> result = poiCtrl.parsePoi(multipart, request, response);
		assertEquals(listPois, result);
	}
	
	@Test
	public void calculPoisByZoneTest(){
		request.getSession().setAttribute("listPois", listPois);
		Double result = poiCtrl.calculPoisByZone(6.5f, -7f, request, response);
		assertEquals(2, result, 0.001);
	}
	
	@Test
	public void getMostFilledAreasTest() {
		set = initializeAreas();
		request.getSession().setAttribute("listPois", listPois);
		Set<String> result = poiCtrl.getMostFilledAreas(2, request, response);
		assertEquals(set, result);
	}
	
	private void initializePois(){
		listPois = new ArrayList<Poi>();
		listPois.add(new Poi("id1", -48.6f, -37.7f));
		listPois.add(new Poi("id2", -27.1f, 8.4f));
		listPois.add(new Poi("id3", 6.6f, -6.9f));
		listPois.add(new Poi("id4", -2.3f, 38.3f));
		listPois.add(new Poi("id5", 6.8f, -6.9f));
		listPois.add(new Poi("id6", -2.5f, 38.3f));
		listPois.add(new Poi("id7", 0.1f, -0.1f));
		listPois.add(new Poi("id8", -2.1f, 38.1f));
	}
	
	private Set<String> initializeAreas() {
		set = new HashSet<String>();
		set.add("minLat:-2.5, maxLat:-2.0, minLon:38.0, maxLon:38.5");
		set.add("minLat:6.5, maxLat:7.0, minLon:-7.0, maxLon:-6.5");
		return set;
	}
	

}
