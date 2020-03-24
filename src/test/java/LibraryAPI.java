import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class LibraryAPI {


    private static List<Arguments> addBook() {
        return Arrays.asList(
                Arguments.of("1", "2"),
                Arguments.of("11", "22"),
                Arguments.of("addd", "221111"),
                Arguments.of("adkd", 221111666L)

        );
    }

    @ParameterizedTest
    @MethodSource
    public void addBook(String isbn, Long aisle) {

        RestAssured.baseURI = "http://216.10.245.166";

        String response = given().header("Content-Type", "application/json")
                .body(Payload.addBook(isbn, aisle))
                .when().post("/Library/Addbook.php")
                .then().assertThat().statusCode(200)
                .body("Msg", equalTo("successfully added"))
                .extract().response().asString();

        JsonPath js = new JsonPath(response);
        String id = js.getString("ID");

        System.out.println(id);


    }

    @Test
    public void addBook2() {

        RestAssured.baseURI = "http://216.10.245.166";

        String isbn = UUID.randomUUID().toString().substring(0, 4);
        Long aisle = (long) (Math.random() * 1000 + 1000);
        String response = given().header("Content-Type", "application/json")
                .body(Payload.addBook(isbn, aisle))
                .when().post("/Library/Addbook.php")
                .then().assertThat().statusCode(200)
                .body("Msg", equalTo("successfully added"))
                .extract().response().asString();

        JsonPath js = new JsonPath(response);
        String id = js.getString("ID");

        System.out.println(id);
    }
}
