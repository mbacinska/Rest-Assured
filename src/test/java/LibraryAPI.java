import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class LibraryAPI {


    @Test
    public void addBook() {


        RestAssured.baseURI = "http://216.10.245.166";

        String response = given().header("Content-Type", "application/json")
                .body(Payload.addBook())
                .when().post("/Library/Addbook.php")
                .then().assertThat().statusCode(200)
                .body("Msg", equalTo("successfully added"))
                .extract().response().asString();

        JsonPath js = new JsonPath(response);
        String id = js.getString("ID");

        System.out.println(id);


    }
}
