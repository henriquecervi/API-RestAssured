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
	
	@Test //forma de utilizar a senha basica quando se tem popUP, direto na URL
	public void deveFazerAutenticacaoBasica() {
		
		given()
			.log().all()
		.when()
			.get("https://admin:senha@restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
		;
		
	}
	
	@Test //desta forma utilizamos o auth.basic com usuário e senha
	public void deveFazerAutenticacaoBasica2() {
		given()
			.log().all()
			.auth().basic("admin", "senha")
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;
	}
	
	@Test
	public void deveFazerAutenticacaoBasicaChallenge() {
		given()
			.log().all()
			.auth().preemptive().basic("admin", "senha")
		.when()
			.get("https://restapi.wcaquino.me/basicauth2")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;
		
	}
	

}
