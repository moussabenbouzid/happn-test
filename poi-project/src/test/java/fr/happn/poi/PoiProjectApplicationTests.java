package fr.happn.poi;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
public class PoiProjectApplicationTests {
	
	@Autowired
	PoiController poictrl;
	HttpServletRequest request;
	HttpServletResponse response;
	File file;
	MultipartFile multipart;
	
	@Before
	public void init() {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
	}

	@Test
	public void contextLoads() throws IOException {
		File file = new File("src/main/resources/static/poi.tsv");
		InputStream targetStream = new FileInputStream(file);
	    multipart =  new MockMultipartFile("tsvFile", targetStream);
	    System.out.println("mon file " + multipart.getSize());
	    List<Poi> pois = poictrl.parsePoi(multipart, request, response);
		assertEquals("test", "test");
	}

}
