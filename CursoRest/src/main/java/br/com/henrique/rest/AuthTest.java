package br.com.henrique.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.Test;


// chave key do weathermap.org, 388e21e4e65fdd86f5ce795c145fbaff

public class AuthTest {
	
	@Test
	public void deveAcessarSWAPI() {
		
		given()
			.log().all()
		.when()
			.get("https://swapi.co/api/people/1/")
		.then()
			.statusCode(200)
			.log().all()
			.body("name", is("Luke Skywalker"))
		;
	}
	
	@Test
	public void deveVerificarClimaCidade() {
		
		given()
			.log().all()
			.queryParam("q", "Santo Andre,BR")
			.queryParam("appid", "388e21e4e65fdd86f5ce795c145fbaff")
			.queryParam("units", "metric")
		.when()
			.get("http://api.openweathermap.org/data/2.5/weather")
		.then()
			.log().all()
			.body("name", is("Santo Andre"))
			.body("sys.country", is("BR"))
			.body("coord.lon", is(-46.54f))
			.body("main.temp", lessThan(32f))
		;
	}
	

}
