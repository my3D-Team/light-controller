package com.capgemini;

import com.capgemini.dto.LightState;
import com.capgemini.exceptions.ArtNetServiceException;
import com.capgemini.exceptions.LightStateException;
import com.capgemini.services.LightService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LightControllerApplication.class)
@WebAppConfiguration
public class LightControllerApplicationTests {

	/**
	 * Light service.
	 */
	@Autowired
	private LightService lightService;

	@Test
	public void contextLoads() {
	}



}
