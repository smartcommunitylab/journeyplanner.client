package eu.trentorise.smartcampus.journeyplanner;

import it.sayservice.platform.smartplanner.data.message.Itinerary;
import it.sayservice.platform.smartplanner.data.message.SimpleLeg;
import it.sayservice.platform.smartplanner.data.message.journey.RecurrentJourneyParameters;
import it.sayservice.platform.smartplanner.data.message.journey.SingleJourney;
import it.sayservice.platform.smartplanner.data.message.otpbeans.TransitTimeTable;
import it.sayservice.platform.smartplanner.data.message.otpbeans.LimitedTransitTimeTable;
import it.sayservice.platform.smartplanner.data.message.otpbeans.Route;
import it.sayservice.platform.smartplanner.data.message.otpbeans.Stop;
import it.sayservice.platform.smartplanner.data.message.otpbeans.StopTime;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import junit.framework.Assert;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/applicationContext.xml")
public class TestClient {

	private static final String AUTH_TOKEN = "";
	@Autowired
	private JourneyPlannerConnector journeyPlannerConnector;	
	
	private Properties props;
	
	@Before
	public void setup() throws Exception {
		props = new Properties();
		props.load(new FileInputStream("src/test/resources/test.properties"));
		Thread.sleep(500);
	}
	
	@Test
	public void test() throws Exception {
		List<Itinerary> singleResults;
		String clientId = "TEST_ID";
		
		
		// plan single
		ObjectMapper mapper = new ObjectMapper();
		String req = props.getProperty("plansinglejourney");
		SingleJourney request = mapper.readValue(req, SingleJourney.class);
		singleResults = journeyPlannerConnector.planSingleJourney(request, AUTH_TOKEN);
		Assert.assertEquals(3, singleResults.size());
		System.out.println(singleResults);
		
		List<Route> r = journeyPlannerConnector.getRoutes("12", AUTH_TOKEN);
		Assert.assertNotNull(r);
		System.out.println(r);
		
		List<Stop> s = journeyPlannerConnector.getStops("12", "05A", AUTH_TOKEN);
		Assert.assertNotNull(s);
		System.out.println(s);
		
		List<StopTime> st = journeyPlannerConnector.getTimetable("12", "05A","20125p", AUTH_TOKEN);
		Assert.assertNotNull(st);
		System.out.println(st);			
		
		req = props.getProperty("planrecurrentjourney");
		RecurrentJourneyParameters rjp = mapper.readValue(req, RecurrentJourneyParameters.class);
		List<SimpleLeg> sl = journeyPlannerConnector.planRecurrentJourney(rjp, AUTH_TOKEN);
		Assert.assertNotNull(sl);
		System.out.println(sl);
		
		Map<String, LimitedTransitTimeTable> tmap = journeyPlannerConnector.getLimitedTimetable("12", "20125p", 3, AUTH_TOKEN);
		Assert.assertNotNull(tmap);
		System.out.println(tmap);
		
		TransitTimeTable btt = journeyPlannerConnector.getTransitTimes("05A", System.currentTimeMillis(), System.currentTimeMillis() + 1000 * 60 * 60, AUTH_TOKEN);
		Assert.assertNotNull(btt);
		System.out.println(btt);
	}
	
}
