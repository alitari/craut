package de.craut.service;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.craut.ServiceTestContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { ServiceTestContext.class })
@ActiveProfiles("test")
public abstract class AbstractServiceIntegrationTest {

	@Autowired
	protected RouteService routeService;

	@Autowired
	protected ActivityService activityService;

	@Before
	public void setup() {
	}

}
