import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.*;

public class AddPlace {

    @Test
    public void addPlaceID() {

        RestAssured.baseURI = "https://rahulshettyacademy.com";

        //create new place
        String response = given().log().all()
                .queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body("{\n" +
                        "  \"location\": {\n" +
                        "    \"lat\": -38.383494,\n" +
                        "    \"lng\": 33.427362\n" +
                        "  },\n" +
                        "  \"accuracy\": 50,\n" +
                        "  \"name\": \"Frontline house\",\n" +
                        "  \"phone_number\": \"(+91) 123 456 7890\",\n" +
                        "  \"address\": \"12, side layout, cohen 09\",\n" +
                        "  \"types\": [\n" +
                        "    \"shoe park\",\n" +
                        "    \"shop\"\n" +
                        "  ],\n" +
                        "  \"website\": \"http://google.com\",\n" +
                        "  \"language\": \"French-IN\"\n" +
                        "}")
                .when()
                .post("/maps/api/place/add/json")
                .then()
                .assertThat().statusCode(200).body("scope", equalTo("APP"))
                .header("Server", "Apache/2.4.18 (Ubuntu)").extract().response().asString();

        System.out.println("POST response: " +response);

    }


}
