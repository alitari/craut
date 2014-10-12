package de.craut.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.craut.domain.Route;

public class RouteServiceIntegrationTest extends AbstractServiceIntegrationTest {

	@Test
	public void routeCRUD() throws Exception {
		Route freiolsheim = saveRoute("Malsch-Freiolsheim");
		Route fetchRoute = routeService.fetchRoute(freiolsheim.getId());
		assertThat(freiolsheim.getId(), is(fetchRoute.getId()));
		assertThat(freiolsheim.getName(), is("Malsch-Freiolsheim"));
		freiolsheim.setName("UpdatedName");
		routeService.updateRoute(freiolsheim);
		fetchRoute = routeService.fetchRoute(freiolsheim.getId());
		assertThat(fetchRoute.getName(), is("UpdatedName"));
		assertThat(routeService.fetchAllRoutes().isEmpty(), is(false));
		routeService.deleteRoute(fetchRoute.getId());
		assertThat(routeService.fetchAllRoutes().isEmpty(), is(true));

	}

}
