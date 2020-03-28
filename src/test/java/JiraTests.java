import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class JiraTests {


    @Test
    public void addComment(){

        RestAssured.baseURI = "http://localhost:8888";


        //get sessionID ---------------------------

       String response = given().log().all().header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"username\": \"monika.bacinska\",\n" +
                        "    \"password\": \"zaq12WSX\"\n" +
                        "}")
                .when().post("rest/auth/1/session")
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath js = new JsonPath(response);
        String sessionId = js.getString("session.value");

        System.out.println("new sessionID equals: " +sessionId);


        //create new issue -------------------------

        String response1 = given().log().all().header("Content-Type", "application/json").header("Cookie", "JSESSIONID="+sessionId+"")
                .body("{\n" +
                        "\"fields\": {\n" +
                        "    \"project\": {\n" +
                        "      \"key\": \"RES\"\n" +
                        "    },\n" +
                        "    \"summary\": \"First task\",\n" +
                        "    \"description\": \"This is my first task.\",\n" +
                        "    \"issuetype\": {\n" +
                        "      \"name\": \"Task\"\n" +
                        "    }\n" +
                        "}\n" +
                        "}")
                .when().post("rest/api/2/issue")
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().response().asString();

        JsonPath js1 = new JsonPath(response1);
        Integer id = js1.getInt("id");

        System.out.println("Issue id equals to: "+id);


        //add comment to created issue

       String response2 = given().log().all().header("Content-Type", "application/json").header("Cookie", "JSESSIONID="+sessionId+"")
                .body("{\n" +
                        "  \"visibility\": {\n" +
                        "    \"type\": \"role\",\n" +
                        "    \"value\": \"Administrators\"\n" +
                        "  },\n" +
                        "  \"body\": \"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque eget venenatis elit. Duis eu justo eget augue iaculis fermentum. Sed semper quam laoreet nisi egestas at posuere augue semper.\"\n" +
                        "}")
                .when().post("rest/api/2/issue/"+id+"/comment")
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().response().asString();



        JsonPath js2 = new JsonPath(response2);
        Integer id2 = js2.getInt("id");


        System.out.println("Comment id equals to: "+id2);


        //update created comment

        given().log().all().header("Content-Type", "application/json").header("Cookie", "JSESSIONID="+sessionId+"")
                .body("{\n" +
                        "  \"visibility\": {\n" +
                        "    \"type\": \"role\",\n" +
                        "    \"value\": \"Administrators\"\n" +
                        "  },\n" +
                        "  \"body\": \"Comment updated.\"\n" +
                        "}")
                .when().put("rest/api/2/issue/"+id+"/comment/"+id2+"")
                .then().log().all()
                .assertThat().statusCode(200)
                .body("body", equalTo("Comment updated."));


        //delete issue

        given().log().all().header("Cookie", "JSESSIONID="+sessionId+"")
                .when().delete("rest/api/2/issue/"+id+"")
                .then().log().all()
                .assertThat().statusCode(204);
   }
}
