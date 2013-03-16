/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/
package eu.trentorise.smartcampus.journeyplanner;

import it.sayservice.platform.smartplanner.data.message.Itinerary;
import it.sayservice.platform.smartplanner.data.message.SimpleLeg;
import it.sayservice.platform.smartplanner.data.message.journey.RecurrentJourney;
import it.sayservice.platform.smartplanner.data.message.journey.RecurrentJourneyParameters;
import it.sayservice.platform.smartplanner.data.message.journey.SingleJourney;
import it.sayservice.platform.smartplanner.data.message.otpbeans.TransitTimeTable;
import it.sayservice.platform.smartplanner.data.message.otpbeans.LimitedTransitTimeTable;
import it.sayservice.platform.smartplanner.data.message.otpbeans.Route;
import it.sayservice.platform.smartplanner.data.message.otpbeans.Stop;
import it.sayservice.platform.smartplanner.data.message.otpbeans.StopTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;

import eu.trentorise.smartcampus.journeyplanner.util.HTTPConnector;
import eu.trentorise.smartcampus.journeyplanner.util.HttpMethod;

public class JourneyPlannerConnector {

	private String journeyPlannerURL;

	private static final String JOURNEYPLANNERSERVICE = "/smartcampus-journeyplanner-web/rest/";

	/**
	 * 
	 * @param serverURL
	 *          address of the server to connect to
	 */
	public JourneyPlannerConnector(String serverURL) {
		this.journeyPlannerURL = serverURL + JOURNEYPLANNERSERVICE;
	}

	/**
	 * Generate a list of itineraries generated according to the particular request
	 * @param request a single journey request
	 * @param token an authorization token
	 * @return a list of itineraries
	 * @throws JourneyPlannerConnectorException
	 */
	public List<Itinerary> planSingleJourney(SingleJourney request, String token) throws JourneyPlannerConnectorException {
		try {
		String url = journeyPlannerURL + "plansinglejourney";
		ObjectMapper mapper = new ObjectMapper();
		String req = mapper.writeValueAsString(request);
		String resp = HTTPConnector.doPostWithReturn(HttpMethod.POST, url, null, req, "application/json", "application/json", token, "UTF-8");

		List<Itinerary> result = new ArrayList<Itinerary>();
		List objs = mapper.readValue(resp, List.class);
		for (Object o : (List) objs) {
			Itinerary it = mapper.convertValue(o, Itinerary.class);
			result.add(it);
		}

		return result;
		} catch (Exception e) {
			throw new JourneyPlannerConnectorException(e);
		}
	}
	
	/**
	 * Generate a list of legs generated according to the particular request
	 * @param request a recurrent journey request
	 * @param token an authorization token
	 * @return a list of leg
	 * @throws JourneyPlannerConnectorException
	 */
	public List<SimpleLeg> planRecurrentJourney(RecurrentJourneyParameters request, String token) throws JourneyPlannerConnectorException {
		try {
		String url = journeyPlannerURL + "planrecurrent";
		ObjectMapper mapper = new ObjectMapper();
		String req = mapper.writeValueAsString(request);
		String resp = HTTPConnector.doPostWithReturn(HttpMethod.POST, url, null, req, "application/json", "application/json", token, "UTF-8");

		List<SimpleLeg> result = new ArrayList<SimpleLeg>();
		RecurrentJourney res = mapper.readValue(resp, RecurrentJourney.class);

		return res.getLegs();
		} catch (Exception e) {
			throw new JourneyPlannerConnectorException(e);
		}
	}	
	
	/**
	 * Request the routes managed by an agency
	 * @param agencyId the id of the agency
	 * @param token an authorization token
	 * @return a list of routes
	 * @throws JourneyPlannerConnectorException
	 */
	public List<Route> getRoutes(String agencyId, String token) throws JourneyPlannerConnectorException {
		try {
		String url = journeyPlannerURL + "getroutes/" + agencyId;
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		String resp = HTTPConnector.doGet(url, null, "application/json", "application/json", token, "UTF-8");

		if (resp == null || resp.isEmpty()) {
			return null;
		}

		List<Route> result = new ArrayList<Route>();
		List objs = mapper.readValue(resp, List.class);
		for (Object o : (List) objs) {
			Route r = mapper.convertValue(o, Route.class);
			result.add(r);
		}

		return result;
		} catch (Exception e) {
			throw new JourneyPlannerConnectorException(e);
		}		
	}		
	
	/**
	 * Request the stops on a particular route
	 * @param agencyId the id of the agency
	 * @param routeId the id of the route
	 * @param token an authorization token
	 * @return a list of stops
	 * @throws JourneyPlannerConnectorException
	 */
	public List<Stop> getStops(String agencyId, String routeId, String token) throws JourneyPlannerConnectorException {
		try {
		String url = journeyPlannerURL + "getstops/" + agencyId + "/" + routeId;
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		String resp = HTTPConnector.doGet(url, null, "application/json", "application/json", token, "UTF-8");

		if (resp == null || resp.isEmpty()) {
			return null;
		}

		List<Stop> result = new ArrayList<Stop>();
		List objs = mapper.readValue(resp, List.class);
		for (Object o : (List) objs) {
			Stop s = mapper.convertValue(o, Stop.class);
			result.add(s);
		}

		return result;
		} catch (Exception e) {
			throw new JourneyPlannerConnectorException(e);
		}
	}			
	
	/**
	 * Request the times of a route at a single stop
	 * @param agencyId the id of the agency
	 * @param routeId the id of the route
	 * @param stopId the id of the stop
	 * @param token an authorization token
	 * @return a list of times
	 * @throws JourneyPlannerConnectorException
	 */
	public List<StopTime> getTimetable(String agencyId, String routeId, String stopId, String token) throws JourneyPlannerConnectorException {
		try {
		String url = journeyPlannerURL + "gettimetable/" + agencyId + "/" + routeId + "/" + stopId;
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		String resp = HTTPConnector.doGet(url, null, "application/json", "application/json", token, "UTF-8");

		if (resp == null || resp.isEmpty()) {
			return null;
		}

		List<StopTime> result = new ArrayList<StopTime>();
		List objs = mapper.readValue(resp, List.class);
		for (Object o : (List) objs) {
			StopTime s = mapper.convertValue(o, StopTime.class);
			result.add(s);
		}

		return result;
		} catch (Exception e) {
			throw new JourneyPlannerConnectorException(e);
		}		
	}				
	
	/**
	 * Request a number of trips for a particular stop, starting at the current time
	 * @param agencyId the id of the agency
	 * @param stopId the id of the stop
	 * @param maxResults the number of trips
	 * @param token an authorization token
	 * @return a map with route ids as key and the next trips as values
	 * @throws JourneyPlannerConnectorException
	 */
	public Map<String, LimitedTransitTimeTable> getLimitedTimetable(String agencyId, String stopId, int maxResults, String token) throws JourneyPlannerConnectorException {
		try {
		String url = journeyPlannerURL + "getlimitedtimetable/" + agencyId + "/" + stopId + "/" + maxResults;
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		String resp = HTTPConnector.doGet(url, null, "application/json", "application/json", token, "UTF-8");

		if (resp == null || resp.isEmpty()) {
			return null;
		}
		
		Map<String, LimitedTransitTimeTable> response = new TreeMap<String, LimitedTransitTimeTable>();
		Map<String, Object> map = mapper.readValue(resp, Map.class);
		for (String key: map.keySet()) {
			LimitedTransitTimeTable ltt = mapper.convertValue(map.get(key), LimitedTransitTimeTable.class);
			response.put(key, ltt);
		}
		
		return response;
		} catch (Exception e) {
			throw new JourneyPlannerConnectorException(e);
		}		
	}					
	
	/**
	 * Return the times for a particular route in a temporal interval
	 * @param routeId the id of the route
	 * @param from start time, in milliseconds
	 * @param to end time, in milliseconds
	 * @param token an authorization token
	 * @return
	 * @throws JourneyPlannerConnectorException
	 */
	public TransitTimeTable getTransitTimes(String routeId, long from, long to, String token) throws JourneyPlannerConnectorException {
		try {
		String url = journeyPlannerURL + "gettransittimes/" + routeId + "/" + from + "/" + to;
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		String resp = HTTPConnector.doGet(url, null, "application/json", "application/json", token, "UTF-8");

		if (resp == null || resp.isEmpty()) {
			return null;
		}
		
		TransitTimeTable response = mapper.readValue(resp, TransitTimeTable.class);
		
		return response;
		} catch (Exception e) {
			throw new JourneyPlannerConnectorException(e);
		}		
	}			
	
	
}
