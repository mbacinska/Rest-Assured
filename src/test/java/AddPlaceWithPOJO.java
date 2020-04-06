import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class AddPlaceWithPOJO {


    @Test

    public void addPlace() {


        RestAssured.baseURI = "https://rahulshettyacademy.com";


        Location location = new Location();
        location.setLat(-38.383494);
        location.setLng(33.427362);
        Place p = new Place();
        p.setLocation(location);
        p.setAccuracy(30);
        p.setAddress("12, front layout, cohen 10");
        p.setName("Frontline house second");
        p.setPhone_number("(+91) 321 654 0987");
        p.setWebsite("http://google.com");
        p.setLanguage("English-IN");
        List<String> types = new ArrayList<>();
        types.add("shoe park");
        types.add("shop");
        p.setTypes(types);


        Response response = (Response) given().log().all().queryParams("key", "qaclick123").header("Content-Type", "application/json")
                .body(p)
                .when().post("/maps/api/place/add/json")
                .then().log().all()
                .statusCode(200).extract().body();

        String respString = response.asString();
        System.out.println(respString);

    }
}
