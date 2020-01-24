package br.com.henrique.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import io.restassured.http.ContentType;

public class HTML {
	
	@Test
	public void deveFazerBuscasComHTML () {
		
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/v2/users")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.HTML)
			.body("html.body.div.table.tbody.tr.size()", is(3))
			.body("html.body.div.table.tbody.tr[1].td[2]", is("25"))
			.appendRootPath("html.body.div.table.tbody")
			.body("tr.find{it.toString().startsWith('2')}.td[1]", is("Maria Joaquina"))
		;
		
	}
	
	@Test
	public void deveFazerBuscasComXpathEmHtml () {
		
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/v2/users?format=clean")
		.then()
			.body(hasXPath("count(//table/tr)", is("4")))
			.body(hasXPath("//td[text() = '2']/../td[2]", is("Maria Joaquina")))
		/*
		 *  utilizamos o // para poder achar a "linha" toda, entre [] para acharmos o
		 * que queremos, // no caso [text () = '2'] que seria o valor do ID, depois
		 * utilizamos /.. para subir nível, // e barra /td novamente e a linha esperada.
		 * comentário da linha 40.
		 */		
			
		;
		
	}

}
