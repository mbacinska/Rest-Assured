import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;

import static io.restassured.RestAssured.given;


public class OAuthTest {


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

        String url = "https://rahulshettyacademy.com/getCourse.php?state=verifyfjdss&code=4%2FyAEAys1bXsiFyvnRrNYdpe0jOY7g4BbNESCL-yytRsbmAFtnpNf9MIM9GspbsLZ3K_nLqhK8FbhJMc8_TZC3TTo&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none#";

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


        String response3 = given().log().all().queryParam("access_token", token)
                .when().get("https://rahulshettyacademy.com/getCourse.php")
                .then().log().all().extract().body().asString();

        System.out.println(response3);

    }
}
