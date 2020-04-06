import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class AddPlaceWithSpecBuilders {


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


        RequestSpecification reqSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("key", "qaclick123").setContentType(ContentType.JSON).build();


        RequestSpecification req = given().spec(reqSpec).body(p);

        ResponseSpecification resSpec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();

        Response response = req.when().post("/maps/api/place/add/json").then().spec(resSpec).extract().response();


        String respString = response.asString();
        System.out.println(respString);

    }
}
