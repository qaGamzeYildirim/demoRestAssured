package restAssured;


import com.fasterxml.jackson.databind.ObjectMapper;
import enums.HttpStatusCode;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;

import static constants.EndpointsConstants.*;
import static constants.TestConstants.*;
import static constants.UrlConstants.*;
import static io.restassured.RestAssured.*;

import java.io.*;
import java.util.Map;


public class ApiTesting {


    @Test
    public void createTest() {

        create();

    }

    @Test
    public void createNegative() {

        File file = new File("resources/createBodyNegative.json");

        given()
                .baseUri(PET_STORE_BASE_URL)
                .contentType(ContentType.JSON)
                .body(file)
                .when()
                .post(PET_ENDPOINT)
                .then()
                .statusCode(HttpStatusCode.METHOD_NOT_ALLOWED.getCode())
                .contentType(ContentType.JSON)
                .extract().response()
                .print();

    }


    @Test
    public void read() {
        String id = create();
        readAndAssert(id);
        deleteAndValidate(id);
    }

    @Test
    public void update() throws IOException {
        File file = new File("resources/update.json");

        String id = create();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> requestMap = objectMapper.readValue(file, Map.class);
        requestMap.put(ID, id);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, requestMap);

        given()
                .baseUri(PET_STORE_BASE_URL)
                .contentType(ContentType.JSON)
                .body(file)
                .when()
                .put(PET_ENDPOINT)
                .then()
                .statusCode(HttpStatusCode.OK.getCode());

        JsonPath updatedJjsonPath = readBody(id);
        Assert.assertEquals(updatedJjsonPath.get(NAME), "karabas");
        deleteAndValidate(id);
    }

    @Test
    public void delete() throws FileNotFoundException {
        String id = create();
        deleteAndValidate(id);

    }

    public String create() {

        File file = new File("resources/createBody.json");

        return given()
                .baseUri(PET_STORE_BASE_URL)
                .contentType(ContentType.JSON)
                .body(file)
                .when()
                .post(PET_ENDPOINT)
                .then()
                .statusCode(HttpStatusCode.OK.getCode())
                .extract().body().path(ID).toString();

    }

    public void readAndAssert(String id) {
        Response response = given()
                .baseUri(PET_STORE_BASE_URL)
                .contentType(ContentType.JSON)
                .param(PET_ID, id)
                .when()
                .get(PET_ENDPOINT_WITH_PRAM + id)
                .then()
                .statusCode(HttpStatusCode.OK.getCode())
                .contentType(ContentType.JSON)
                .extract().response();
        Assert.assertEquals(response.path(ID).toString(), id);
    }

    public JsonPath readBody(String id) {
        Response response = given()
                .baseUri(PET_STORE_BASE_URL)
                .contentType(ContentType.JSON)
                .param(PET_ID, id)
                .when()
                .get(PET_ENDPOINT_WITH_PRAM + id)
                .then()
                .statusCode(HttpStatusCode.OK.getCode())
                .contentType(ContentType.JSON)
                .extract().response();
        return new JsonPath(response.asPrettyString());
    }

    public void deleteAndValidate(String id) {
        given()
                .baseUri(PET_STORE_BASE_URL)
                .contentType(ContentType.JSON)
                .param(PET_ID, id)
                .when()
                .delete(PET_ENDPOINT_WITH_PRAM + id)
                .then()
                .statusCode(HttpStatusCode.OK.getCode());

        given()
                .baseUri(PET_STORE_BASE_URL)
                .contentType(ContentType.JSON)
                .param(PET_ID, id)
                .when()
                .get(PET_ENDPOINT_WITH_PRAM + id)
                .then()
                .statusCode(HttpStatusCode.METHOD_NOT_ALLOWED.getCode());

    }


}
