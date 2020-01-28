package br.com.henrique.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import groovy.util.XmlParser;
import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.XmlPath.CompatibilityMode;


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
	
	@Test
	public void deveFazerAutenticacaoComTokenJWT () {
		
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "henrique@henrique");
		login.put("senha", "123456");		
	
		// login na API
		// receber o token		
		
	String token =	given()
			.log().all()
			.body(login)			
			.contentType(ContentType.JSON)
		.when()
			.post("http://barrigarest.wcaquino.me/signin")
		.then()
			.log().all()
			
			.statusCode(200)
			.extract().path("token")
		;
	
	// obter contas
		given()
			.log().all()
		.when()
			.get("http://barrigarest.wcaquino.me/contas")
		.then()
			.log().all()
		;
		
			
	}
	
	@Test
	public void deveAcessarAplicacaoWeb () {
		
		String cookie = given()
			.log().all()
			.formParam("email", "henrique@henrique")
			.formParam("senha", "123456")
			.contentType(ContentType.URLENC.withCharset("UTF-8"))
		.when()
			.post("https://srbarriga.herokuapp.com/logar")
		.then()
			.log().all()
			.statusCode(200)
			.extract().header("set-cookie");
			
		;
		
		cookie = cookie.split("=")[1].split(";")[0]; // isso serve para dividir o que você quer buscar.
		System.out.println(cookie);
		
		// obter conta
		
	String body = given()
			.log().all()
			.cookie("connect.sid", cookie)
		.when()
			.get("http://seubarriga.wcaquino.me/contas")
		.then()
			.log().all()
			.statusCode(200)
			.body("html.body.table.tbody.tr[0].td[0]", is("Muitas Contas"))
			.extract().body().asString();
		;
		
		System.out.println("------------------------------");
		XmlPath xmlPath = new XmlPath(CompatibilityMode.HTML, body);
		System.out.println(xmlPath.getString("html.body.table.tbody.tr[0].td[0]"));
	}
	

}
