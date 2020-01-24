package br.com.henrique.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.io.File;

import org.junit.Test;


public class FileTest {
	
	@Test
	public void deveObrigarEnvioArquivo () {
		given()
			.log().all()
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(404) // deveria ser 400
			.body("error", is("Arquivo não enviado"))
		;
	}
	
	@Test
	public void deveEnviarArquivo () {
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/HenriqueCerviCutri.pdf"))
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("HenriqueCerviCutri.pdf"))
			
		;
	}
	
	
	@Test
	public void naoDeveFazerUploadDeArquivoGrande() {
		given()
			.log().all()
			.multiPart("arquivo", "src/main/resources/HenriqueCerviCutri_2.docx")
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.time(lessThan(6000L))
			.statusCode(413)
			
		;
		
	}
}
