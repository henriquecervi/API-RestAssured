package br.com.henrique.rest;

import static io.restassured.RestAssured.given;

import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class EnviaDadosTest {

	@Test
	public void deveEnviarValorViaQuery () {
		
 		 given()
 		 	.log().all()
		.when()
			.get("https://restapi.wcaquino.me/v2/users?format=json")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.JSON)
		;
		
	}
}
