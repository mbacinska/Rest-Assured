import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

public class JiraAttachmentTest {


    @Test
    public void addAttachment() {

        RestAssured.baseURI = "http://localhost:8888";


        //create session filter---------------------------

        SessionFilter session = new SessionFilter();

        given().log().all().header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"username\": \"monika.bacinska\",\n" +
                        "    \"password\": \"zaq12WSX\"\n" +
                        "}")
                .filter(session)
                .when().post("/rest/auth/1/session")
                .then().log().all()
                .assertThat().statusCode(200);


        //create new issue -------------------------

        String response1 = given().log().all().header("Content-Type", "application/json").filter(session)
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
                .when().post("/rest/api/2/issue")
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().response().asString();

        JsonPath js1 = new JsonPath(response1);
        Integer id = js1.getInt("id");

        System.out.println("Issue id equals to: " + id);


        //add comment to existing issue

        given().log().all().filter(session).header("Content-Type", "application/json").pathParam("issueID", +id + "")
                .body("{\n" +
                        "  \"visibility\": {\n" +
                        "    \"type\": \"role\",\n" +
                        "    \"value\": \"Administrators\"\n" +
                        "  },\n" +
                        "  \"body\": \"Adding comment.Wait for an attachment.\"\n" +
                        "}")
                .when().post("/rest/api/2/issue/{issueID}/comment")
                .then().log().all()
                .assertThat().statusCode(201);


        //add attachment to the issue
//
        given().log().all().pathParam("issueID", +id + "").header("X-Atlassian-Token", "no-check").header("Content-Type", "multipart/form-data").filter(session)
                .multiPart("file", new File("/home/darek/IdeaProjects/RestAssured/src/test/jiraAttachment.txt"))
                .when().post("/rest/api/2/issue/{issueID}/attachments")
                .then().log().all()
                .assertThat().statusCode(200);


        //get the issue

        String issueDetails = given().log().all().filter(session).pathParam("issueID", +id + "")
                .queryParam("fields", "comment")
                .when().get("/rest/api/2/issue/{issueID}")
                .then().log().all()
                .extract().body().asString();

        System.out.println(issueDetails);


        //delete issue

//        given().log().all().filter(session)
//                .when().delete("/rest/api/2/issue/" + id + "")
//                .then().log().all()
//                .assertThat().statusCode(204);
    }
}
