package com.skillsoft.restassured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Assignment_Restassured {

	
	/*******************************************************
	 * Send a GET request to
	 * http://openlibrary.org/authors/OL1A.json
	 * and check that the answer has HTTP status code 200 
	 ******************************************************/
	
	@Test
	public void checkResponseCodeForCorrectRequest() {
		
		given().
		when().
			get("http://openlibrary.org/authors/OL1A.json").
		then().
			assertThat().
			statusCode(200);
	}
	
	/*******************************************************
	 * Send a GET request to
	 * http://openlibrary.org/authors/incorrect.json
	 * and check that the answer has HTTP status code 400 
	 ******************************************************/
	
	@Test
	public void checkResponseCodeForIncorrectRequest() {
		
		given().
		when().
			get("http://openlibrary.org/authors/incorrect.json").
		then().
			assertThat().
			statusCode(400);
	}
	
	/*******************************************************
	 * Send a GET request to
	 * http://openlibrary.org/authors/OL1A.json
	 * and check that the response is in JSON format 
	 ******************************************************/
	
	@Test
	public void checkResponseContentTypeJson() {
		
		given().
		when().
			get("http://openlibrary.org/authors/OL1A.json").
		then().
			assertThat().
			contentType("application/json");
	}
	
	/***********************************************
	 * Retrieve comment[1] information 
	 * of the OL1M.json equals to
	 * reverted to revision 46
	 * Use http://openlibrary.org/books/OL1M.json?m=history&limit=2&offset=1
	 **********************************************/
	
	@Test
	public void checkTheSecondCommentOf2014WasAtAlbertPark() {
		
		given().
		when().
			get("http://openlibrary.org/books/OL1M.json?m=history&limit=2&offset=1").
		then().
			assertThat().
			body("comment[1]",equalTo("reverted to revision 46"));
	}
	
	/***********************************************
	 * Basic authentication 
	 * use https://openlibrary.org/account/login
	 **********************************************/
	
	@Test
	public void useBasicAuthentication() {
		
	
		given().
			params("grant_type","client_credentials").
			auth().
			preemptive().
			basic("joe","secret").
		when().
			post("https://openlibrary.org/account/login").
		then().
			log().cookies();
	}
	
	//http://openlibrary.org/query.json?type=/type/edition&authors=/authors/OL1A&limit=2
	@Test
	public void useMultiplePathParameters() {

		given().
			
		when().
			get("https://openlibrary.org/query?type=/{Driver}/edition&authors=/{constructor}/OL1A").
		then()
			.body("key[0]",equalTo("/books/OL22562084M"));			
	}	
	
	/***********************************************
	 * Retrieve the list of circuits for the 2014
	 * season and check that it contains silverstone
	 **********************************************/
	
	@Test
	public void checkThereWasARaceAtSilverstoneIn2014() {

		given().
		when().
			get("http://ergast.com/api/f1/2014/circuits.json").
		then().
			assertThat().
			body("MRData.CircuitTable.Circuits.circuitId",hasItem("silverstone"));
	}
	
	/***********************************************
	 * Retrieve the list of circuits for the 2014
	 * season and check that it does not contain
	 * nurburgring
	 **********************************************/
	
	@Test
	public void checkThereWasNoRaceAtNurburgringIn2014() {

		given().
		when().
			get("http://ergast.com/api/f1/2014/circuits.json").
		then().
			assertThat().
		body("MRData.CircuitTable.Circuits.circuitId",not(hasItem("nurburgring")));
	}
	
	
	/***********************************************
	 * Retrieve the list of books for OL6807502M.rdf
	 * season and check that it does contain
	 * publisher, title, issued, placeOfPublication
	 **********************************************/
	
	@Test
	public void getRdfAndCheck() {
		
		given().
		when().
			get("https://openlibrary.org/books/OL6807502M.rdf").
		then().
			assertThat().
			statusCode(200).
			and().
			body("RDF.Description.publisher",equalTo("Basic Books")).
			and().
			body("RDF.Description.title",equalTo("Code: and other laws of cyberspace")).
			and().
			body("RDF.Description.issued",equalTo("1999")).
			and().
			body("RDF.Description.placeOfPublication",equalTo("New York"));
	}
	
	
	/*******************************************************
	 * Send a GET request to
	 * https://openlibrary.org/books/OL6807502M.rdf
	 * and check that the response is in rdf+xml format 
	 ******************************************************/
	@Test
	public void checkResponseContectTypeRDF(){
		
	given().
			when().
				get("https://openlibrary.org/books/OL6807502M.rdf").
			then().
				assertThat().
				contentType("application/rdf+xml");
		}
	
	//http://openlibrary.org/authors/OL1A.json
	
	@Test
	public void checkauthours(){
		
		given().
		when().
		get("http://openlibrary.org/authors/OL1A.json").
		then(). 
		assertThat().
		statusCode(200). 
		and(). 
		body("name",equalTo("Sachi Rautroy")). 
		and(). 
		body("personal_name",equalTo("Sachi Rautroy")). 
		and(). 
		body("last_modified.type",equalTo("/type/datetime")).
		and(). 
		body("death_date",equalTo("2004")). 
		and(). 
		body("birth_date",equalTo("1916"));
		}
	
	/*******************************************************
	 * Send a GET request to
	 * http://openlibrary.org/authors/OL1A.json?callback=process
	 * and check that the response for callback 
	 ******************************************************/
	
		@Test
		public void callbackOpenLib(){
			given(). 
			when().
			get("http://openlibrary.org/authors/OL1A.json?callback=process"). 
			then(). 
			assertThat(). 
			statusCode(200);
		}
}
