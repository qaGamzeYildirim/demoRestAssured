package restAssured;


import org.testng.annotations.Test;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;

import java.io.*;
import java.util.Scanner;



public class ApiTesting {


    @Test
    public void create() {

        File file = new File("resources/createBody.json");

        String id = given()
                .baseUri("https://petstore.swagger.io/v2")
                .basePath("/pet")
                .contentType(ContentType.JSON)
                .body(file).
                when()
                .post()
                .then()
                .statusCode(200)
                .extract().path("id", "status", "name").toString();

        System.out.println(id);
        write(id, "resources/a.txt");


    }

    @Test
    public void createNegative() {

        File file = new File("resources/createBodyNegative.json");

        given()
                .baseUri("https://petstore.swagger.io/v2")
                .basePath("/pet")
                .contentType(ContentType.JSON)
                .body(file).
                when()
                .post()
                .then()
                .statusCode(405)
                .contentType(ContentType.JSON)
                .extract().response()
                .print();

    }


    @Test
    public void read() throws FileNotFoundException {

        String petId = readA();
        System.out.println(petId);
        String response = given()
                .baseUri("https://petstore.swagger.io/v2")
                .contentType(ContentType.JSON)
                .param("petId", petId)
                .when()
                .get("/pet/" + petId)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response()
                .print();


    }

    @Test
    public void update() throws FileNotFoundException {
        File file = new File("resources/update.json");

        given()
                .baseUri("https://petstore.swagger.io/v2")
                .basePath("/pet")
                .contentType(ContentType.JSON)
                .body(file)
                .when()
                .put()
                .then()
                .statusCode(200);

    }

    @Test
    public void delete() throws FileNotFoundException {
        String petId = readA();
        System.out.println(petId);
        given()
                .baseUri("https://petstore.swagger.io/v2")
                .contentType(ContentType.JSON)
                .param("petId", petId)
                .when()
                .delete("/pet/" + petId)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response()
                .print();

    }


    private static void write(String txt, String filePath) {
        try {
            File file = new File(filePath);
            FileWriter writer = new FileWriter(file);
            BufferedWriter writer1 = new BufferedWriter(writer);
            writer1.write(txt);
            writer1.close();
            System.out.println("success");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static String readA() throws FileNotFoundException {
        File f = new File("resources/a.txt");
        Scanner file = new Scanner(f);

        return file.nextLine();
    }

}