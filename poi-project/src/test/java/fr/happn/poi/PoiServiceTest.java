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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import fr.happn.poi.model.Area;
import fr.happn.poi.model.Poi;
import fr.happn.poi.service.PoiService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PoiServiceTest {
	
	@Autowired
	PoiService poiServ;
	
	File file;
	MultipartFile multipart;
	
	private static List<Poi> listPois;
	private static Set<Area> areas;
	
	@Before
	public void init() {
		initializePois();
	}
	
	@Test
	public void parsePoiTest() throws IOException {
		file = new File("src/main/resources/static/poi.tsv");
		InputStream targetStream = new FileInputStream(file);
	    multipart =  new MockMultipartFile("tsvFile", targetStream);
	    List<Poi> result = poiServ.parsePoi(multipart);
		assertEquals(listPois, result);
	}
	
	@Test
	public void calculPoisByZoneTest() {
		Double result = poiServ.calculPoisByZone(6.5f, -7f, listPois);
		assertEquals(2, result, 0.001);
	}
	
	@Test
	public void getMostFilledAreasTest() {
		areas = initializeAreas();
		Set<Area> result = poiServ.getMostFilledAreas(2, listPois);
		assertEquals(areas, result);
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
	
	private Set<Area> initializeAreas() {
		areas = new HashSet<Area>();
		areas.add(new Area(-2.5f, -2f, 38f, 38.5f));
		areas.add(new Area(6.5f, 7.0f, -7.0f, -6.5f));
		return areas;
	}

}
