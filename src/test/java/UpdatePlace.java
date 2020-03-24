import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class UpdatePlace {


    @Test
    public void updatePlaceId() {

        RestAssured.baseURI = "https://rahulshettyacademy.com";

        //create new place
        String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body("{\n" +
                        "  \"location\": {\n" +
                        "    \"lat\": -38.383494,\n" +
                        "    \"lng\": 33.427362\n" +
                        "  },\n" +
                        "  \"accuracy\": 30,\n" +
                        "  \"name\": \"My house\",\n" +
                        "  \"phone_number\": \"(+11) 111 222 3333\",\n" +
                        "  \"address\": \"35, any street, cohen 11\",\n" +
                        "  \"types\": [\n" +
                        "    \"shoe park\",\n" +
                        "    \"shop\"\n" +
                        "  ],\n" +
                        "  \"website\": \"http://google.com\",\n" +
                        "  \"language\": \"French-IN\"\n" +
                        "}")
                .when().post("/maps/api/place/add/json")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();

        JsonPath js = new JsonPath(response);
        String placeId = js.getString("place_id");

        //update address in created place
        String newAddress = "100 Winter Street, Kanada";

        given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body("{\n" +
                        "\"place_id\":\"" + placeId + "\",\n" +
                        "\"address\":\"" + newAddress + "\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}")
                .when().put("maps/api/place/update/json")
                .then().log().all().assertThat()
                .body("msg", equalTo("Address successfully updated"));


        //check if address was updated
        given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
                .when().get("maps/api/place/get/json")
                .then().log().all()
                .assertThat().body("address", equalTo(newAddress));


    }
}
