import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class OAuthTestPOJO {


    @Test
    public void oAuthMechanism() throws InterruptedException {


//        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("-incognito");
//        DesiredCapabilities caps = new DesiredCapabilities();
//        caps.setCapability(ChromeOptions.CAPABILITY, options);
//
//
//        WebDriver driver = new ChromeDriver();
//        Thread.sleep(4000);
//        driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php&state=verifyfjdss");
//        driver.findElement(By.cssSelector("input[type='email']")).sendKeys("user.name@gmail.com");
//        Thread.sleep(4000);
//        driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER);
//        Thread.sleep(4000);
//        driver.findElement(By.cssSelector("input[type='password']")).sendKeys("password");
//        driver.findElement(By.cssSelector("input[type='password']")).sendKeys(Keys.ENTER);
//        Thread.sleep(4000);

        String url = "https://rahulshettyacademy.com/getCourse.php?state=verifyfjdss&code=4%2FyQGHMtoOK_tnmOujEvpUYpSSReHSY0zjihzgAwPvvbbD-Y03NJhZ3jU3r5i266wHHDR1Z-aME8_pSx9uHp-8VQc&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none#";

        // String url = driver.getCurrentUrl();

        //System.out.println(url);

        String splitUrl = url.split("code=")[1];
        String code = splitUrl.split("&scope")[0];

        System.out.println(code);


        String accessTokenResponse = given().urlEncodingEnabled(false).log().all().queryParams("code", code)
                .queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .queryParams("grant_type", "authorization_code")
                .queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
                //.queryParams("session_state", "ff4a89d1f7011eb34eef8cf02ce4353316d9744b..7eb8")
                // .queryParam("scope", "email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email")
                .when().log().all()
                .post("https://www.googleapis.com/oauth2/v4/token").asString();

        System.out.println(accessTokenResponse);

        JsonPath js = new JsonPath(accessTokenResponse);
        String token = js.getString("access_token");

        System.out.println(token);


        POJO resp = given().log().all().queryParam("access_token", token).expect().defaultParser(Parser.JSON)
                .when().get("https://rahulshettyacademy.com/getCourse.php")
                .then().extract().body().as(POJO.class);

        //get instructor name
        System.out.println(resp.getInstructor());
        //get linkedIn
        System.out.println(resp.getLinkedIn());

        //print "price" for courseTitle: "SoapUI Webservices Testing"
        for (int i = 0; i < resp.getCourses().getApi().size(); i++) {


            if (resp.getCourses().getApi().get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices Testing")) {

                System.out.println(resp.getCourses().getApi().get(i).getPrice());
            }
        }


        String[] expectedTitles = {"Selenium Webdriver Java", "Cypress", "Protractor"};

        List<String> expectedTitlesList = Arrays.asList(expectedTitles);

        List<String> actualTitlesList = new ArrayList<>();

        for (int i = 0; i < resp.getCourses().getWebAutomation().size(); i++) {

            actualTitlesList.add(resp.getCourses().getWebAutomation().get(i).getCourseTitle());
        }

        assertTrue(actualTitlesList.equals(expectedTitlesList));

    }
}
