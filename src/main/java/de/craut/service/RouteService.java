package de.craut.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import de.craut.domain.FileUpload;
import de.craut.domain.FileUpload.Format;
import de.craut.domain.FileUpload.Type;
import de.craut.domain.FileUploadRepository;
import de.craut.domain.Route;
import de.craut.domain.RoutePoint;
import de.craut.domain.RoutePointRepository;
import de.craut.domain.RouteRepository;
import de.craut.util.geocalc.EarthCalc;

@Service
public class RouteService {

	private final RouteRepository routeRepository;
	private final RoutePointRepository routePointRepository;
	private final FileUploadRepository fileUploadRepository;

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	public RouteService(RouteRepository routeRepository, RoutePointRepository routePointRepository, FileUploadRepository fileUploadRepository) {
		this.routeRepository = routeRepository;
		this.routePointRepository = routePointRepository;
		this.fileUploadRepository = fileUploadRepository;
	}

	public Route fetchRoute(Long routeId) {
		return routeRepository.findOne(routeId);

	}

	public Route deleteRoute(Long routeId) {
		Route event = fetchRoute(routeId);
		routeRepository.delete(routeId);
		return event;
	}

	public List<Route> fetchAllRoutes() {
		List<Route> list = new ArrayList<Route>();
		CollectionUtils.addAll(list, routeRepository.findAll().iterator());
		return list;
	}

	public Route saveRoute(String name, List<? extends Point> points) {

		List<RoutePoint> routePoints = new ArrayList<RoutePoint>();
		Route route = new Route(name, new Date());
		int seq = 0;
		double distance = 0;
		for (Point point : points) {
			distance += routePoints.size() == 0 ? 0 : EarthCalc.getDistance(point, routePoints.get(routePoints.size() - 1));
			routePoints.add(new RoutePoint(route, seq, point.getX(), point.getY()));
			seq++;
		}

		RoutePoint firstPoint = routePoints.get(0);
		RoutePoint lastPoint = routePoints.get(routePoints.size() - 1);

		route.setStartLatitude(firstPoint.getLatitude());
		route.setStartLongitude(firstPoint.getLongitude());

		route.setEndLatitude(lastPoint.getLatitude());
		route.setEndLongitude(lastPoint.getLongitude());
		route.setDistance(distance);

		Route savedRoute = routeRepository.save(route);
		routePointRepository.save(routePoints);
		return savedRoute;
	}

	public void updateRoute(Route route) {
		routeRepository.save(route);
	}

	public List<RoutePoint> fetchRoutePoints(Route route) {
		return routePointRepository.findByRoute(route);
	}

	public FileUpload fileUpload(byte[] content, Type type, Format format) {
		String thread = String.valueOf(Thread.currentThread().getId()) + " " + Thread.currentThread().getName();
		Date timeStamp = new Date();
		FileUpload fileUpload = new FileUpload(thread, content.length, content, timeStamp, type, format);
		return fileUploadRepository.save(fileUpload);
	}

	public List<FileUpload> fetchFileUploadsFromToday() {
		Date fromToday = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		return fileUploadRepository.findByInsertDateGreaterThan(fromToday);
	}

}