package br.com.henrique.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.http.ContentType;

public class VerbosTest {
	
	@Test
	public void deveSalvarUsuario () {
		
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\" : \"Jose\",	\"age\": 50 }")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Jose"))
			.body("age", is(50))
		;
	}
	
	@Test
	public void deveSalvarUsuarioMapSerializando () {
		
		Map<String, Object> serializando = new HashMap<String, Object>();
		
		serializando.put("name", "Serializando");
		serializando.put("age", 30);
		
		given()
			.log().all()
			.contentType("application/json")
			.body(serializando)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Serializando"))
			.body("age", is(30))
		;
	}
	
	@Test
	public void deveSalvarUsuarioSerializadoComObjeto () {
		
		User user = new User("Henrique", 30);
		
		given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Henrique"))
			.body("age", is(30))
		;
	}
	
	@Test
	public void deveDeserializarObjetoAoSalvarUsuario () {
		
		User user = new User("Henrique Deserializado", 30);
		
		User usuarioInserido = given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class)
		;
		
		Assert.assertThat(usuarioInserido.getId(), notNullValue());
		Assert.assertEquals("Henrique Deserializado", usuarioInserido.getName());
		Assert.assertThat(usuarioInserido.getAge(), is(30));
		
		System.out.println(usuarioInserido);
	}
	
	@Test
	public void deveDeserializarXmlAoSalvarUsuario () {
		
		User user = new User("Henrique Xmll", 30);
		
		User usuarioInserido = given()
			.log().all()
			.contentType(ContentType.XML)
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class)
		;
		
		Assert.assertThat(usuarioInserido.getId(), notNullValue());
		Assert.assertEquals("Henrique Xmll", usuarioInserido.getName());
		Assert.assertThat(usuarioInserido.getAge(), is(30));
		Assert.assertThat(usuarioInserido.getSalary(), nullValue());
		
		System.out.println(usuarioInserido);
	}
	
	@Test
	public void naoDeveSalvarUsuarioSemNome() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"age\": 50 }")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(400)
			.body("id", is(nullValue()))
			.body("error", is("Name é um atributo obrigatório"))
	;
		
	}
	
	@Test
	public void DeveSalvarUsuarioViaXML() {
		given()
			.log().all()
			.contentType(ContentType.XML)
			.body("<user><name>Jose</name><age>50</age></user>")
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
		.log().all()	
		.statusCode(201)
		.body("user.@id", is(notNullValue()))
		.body("user.name", equalTo("Jose"))
		.body("user.age", equalTo("50"))
		
		;	
		
	}
	
	@Test
	public void DeveSalvarUsuarioViaXMLUsandoObjeto() {
		User user = new User("Usuario Xml", 40);
		
		given()
			.log().all()
			.contentType(ContentType.XML)
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
		.log().all()	
		.statusCode(201)
		.body("user.@id", is(notNullValue()))
		.body("user.name", equalTo("Usuario Xml"))
		.body("user.age", equalTo("40"))
		
		;	
		
	}
	
	@Test
	public void deveAlterarUsuario () {
		
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body("{\"name\" : \"Henrique\",	\"age\": 30 }")
		.when()
			.put("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", equalTo(1))
			.body("name", hasLength(8))
			.body("name", is("Henrique"))
			.body("age", is(30))
		;
	}
	
	@Test
	public void devoCustomizarURL () {
		
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body("{\"name\" : \"Henrique\",	\"age\": 30 }")
			.pathParam("entidade", "users")
			.pathParam("userId", 1)
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{userId}")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", equalTo(1))
			.body("name", hasLength(8))
			.body("name", is("Henrique"))
			.body("age", is(30))
		;
	}
	
	@Test
	public void deveRemoverUmUsuario() {
		given()
			.log().all()
		.when()
			.delete("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(204)
		;
		
	}
	
	@Test
	public void naoDeveDeletarUsuarioInexistente () {
		given()
			.log().all()
		.when()
			.delete("https://restapi.wcaquino.me/users/10")
		.then()
			.log().all()
			.statusCode(400)
			.body("error", is("Registro inexistente"))
			;
			
	}

}


