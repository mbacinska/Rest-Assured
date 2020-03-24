import com.fasterxml.jackson.annotation.JsonIgnore;
import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;


public class JsonParse {


    @Test
    public void jsonParse() {

        JsonPath js = new JsonPath(Payload.getJsonData());


        //get number of courses
        int numOfCourses = js.getInt("courses.size()");

        System.out.println("Number of courses is: " + numOfCourses);

        //get purchase amount
        int purchaseAmount = js.getInt("dashboard.purchaseAmount");

        System.out.println("Purchase amount is: " + purchaseAmount);

        //get title of the first course

        String title = js.getString("courses[0].title");

        System.out.println("The title of first course is: " + title);

        //get all courses titles and theirs prices

        //1. simple for loop
        for (int i = 0; i < js.getInt("courses.size()"); i++) {

            System.out.println("title[" + i + "]: " + js.getString("courses[" + i + "].title"));
            System.out.println("price[" + i + "]: " + js.getInt("courses[" + i + "].price"));
        }

        //2. enhanced for loop (foreach)
        List<Course> courses = js.getList("courses", Course.class);

        for (Course course : courses) {
            System.out.println("title " + course.title);
            System.out.println("price " + course.price);
        }


        //check all elements in courses[] and if title is "RPA" then print no of its copies
        System.out.println("by using simple for loop");


        for (int i = 0; i < js.getInt("courses.size()"); i++) {

            if (js.getString("courses[" + i + "].title").equalsIgnoreCase("RPA")) {

                System.out.println(js.getInt("courses[" + i + "].copies"));
                break;
            }


        }

        System.out.println("by using enhanced for loop");
        for (Course course : courses) {


            if (course.title.equalsIgnoreCase("RPA")) {
                System.out.println(course.copies);
            }
        }

        //check if sum of all courses matches the purchase amount

        int sum = 0;

        for (int i = 0; i < js.getInt("courses.size()"); i++) {

            sum = sum + (js.getInt("courses[" + i + "].copies") * js.getInt("courses[" + i + "].price"));
        }
        System.out.println(sum);


        Assert.assertEquals(sum, js.getInt("dashboard.purchaseAmount"));
    }
}

class Course {
    public String title;
    public Integer price;
    public Integer copies;
}
