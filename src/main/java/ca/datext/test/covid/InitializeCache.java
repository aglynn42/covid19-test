package ca.datext.test.covid;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.map.mutable.MutableMapFactoryImpl;
import org.eclipse.collections.impl.tuple.Tuples;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;

import ca.datext.test.covid.domain.Country;
import ca.datext.test.covid.domain.ISO3166Codes;

public class InitializeCache {
	private static Country[] countries;
	public static MutableMap<String, Country> countryMap;
	public static MutableMap<String, ISO3166Codes> codesMap;

	public static void buildCountryCache() {
		Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFeature.class));

		WebTarget webTarget = client.target("https://api.covid19api.com").path("/summary");
		MutableMap<String, Country> map = MutableMapFactoryImpl.INSTANCE.empty();
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		countries = response.readEntity(Country[].class);
		for (Country country : countries) {
			map.add(Tuples.pair(country.shortForm, country));
		}

	}

	public static void buildISOCache() {
		Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFeature.class));

		WebTarget webTarget = client.target("https://restcountries.eu").path("/rest/v2/all");
		MutableMap<String, ISO3166Codes> map = MutableMapFactoryImpl.INSTANCE.empty();
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		ISO3166Codes[] codes = response.readEntity(ISO3166Codes[].class);
		for (ISO3166Codes code : codes) {
			map.add(Tuples.pair(ISO3166Codes.name, code));
		}
	}
}
