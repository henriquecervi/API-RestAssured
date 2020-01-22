package br.com.henrique.rest;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundoTeste {

	@Test
	public void testeOlaMundo() {

		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
		System.out.println(response.getBody().asString());

		if (response.getStatusCode() == 200) {
			System.out.println(response.statusCode());

		} else {
			System.out.println("Status diferente de 200.");
		}

		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);

		System.out.println("Teste 1");

	}

	@Test
	public void testeOlaMundoDois() {

		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
		Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
		Assert.assertTrue(response.statusCode() == 200);

		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);

		System.out.println("Teste 2.");
	}

	@Test
	public void devoConhecerOutrasFormasRestAssured() {
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);

		get("http://restapi.wcaquino.me/ola").then().statusCode(200);

		System.out.println("Teste 3");

		/*
		 * forma simplificada de utilizar, estilo linguagem Gherkin. O RestAssured está
		 * importado como *, por isso não é utitilizado no começo.
		 */

		given() // Pré condições.
				.when() // ação de fato
				.get("http://restapi.wcaquino.me/ola").then() // Assertivas.
				// .assertThat() // Apenas para leitura, não muda em nada o código.
				.statusCode(200);

	}

	@Test
	public void deveConhecerMatchersHamcrest() {

//		assertThat("Maria", Matchers.is("Maria"));
//		assertThat(128, Matchers.is(128));
//		assertThat(128d, Matchers.greaterThan(120d));
//		List<Integer> numerosImpares = Arrays.asList(1,3,5,7,9);
//		assertThat(numerosImpares, Matchers.hasSize(9));
//		assertThat(numerosImpares, Matchers.contains(1,3,5,7,9));
//		assertThat(numerosImpares, Matchers.containsInAnyOrder(1,3,9,7,5));

		Assert.assertThat("Maria", not("João"));

		Assert.assertThat("Joaquina", allOf(startsWith("Joa"), endsWith("ina"), containsString("qui"))); // tem que
																											// escrever
																											// o starts,
																											// ends e
																											// contains.

	}
	
	@Test
	public void deveTestarOBody() {
		
		given()
		.when()
			.get("http://restapi.wcaquino.me/ola")
		.then()
			.statusCode(200)
			.body(Matchers.is("Ola Mundo!"))
			.body(Matchers.containsString("Mundo"))
			.body(is(not(nullValue())));
			
	}
	
}
