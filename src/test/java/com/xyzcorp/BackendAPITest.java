package com.xyzcorp;

import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.json.JSONArray;
import org.json.JSONObject;

public class BackendAPITest {
    @Test
    public void testUserData() {
        given()
                .relaxedHTTPSValidation()
                .accept(ContentType.JSON)
                .when()
                .get("https://staging.tiered-planet.net/werk-it-back-end/weights/user/1")
                .then()
                .assertThat()
                .body("[0].id", equalTo(2))
                .and()
                .body("[0].name", equalTo("bench press"))
                .and()
                .body("[0].pounds", equalTo(40));
    }

    @Test
    public void testUserDataArray() {
        JSONArray objs = new JSONArray(
                given()
                        .relaxedHTTPSValidation()
                        .accept(ContentType.JSON)
                        .when()
                        .get("https://staging.tiered-planet.net/werk-it-back-end/weights/user/1")
                        .then()
                        .contentType(ContentType.JSON)
                        .extract().asPrettyString());
        Boolean flag_item_found = false;
        JSONObject target = new JSONObject()
                .put("id", 2)
                .put("name", "bench press")
                .put("sets", 2)
                .put("reps", 20)
                .put("pounds", 40)
                .put("userId", 1);
        for (int n = 0; n < objs.length(); n++) {
            JSONObject record = objs.getJSONObject(n);
            if (record.toString().equals(target.toString())) {
                flag_item_found = true;
            }
        }

        assert (flag_item_found);

    }

    @Test
    public void testUserProfile() {
        given()
                .relaxedHTTPSValidation()
                .accept(ContentType.JSON)
                .when()
                .get("https://staging.tiered-planet.net/werk-it-back-end/login/sallys/point234")
                .then()
                .assertThat()
                .body("id", equalTo(3))
                .and()
                .body("username", equalTo("sallys"))
                .and()
                .body("firstName", equalTo("Sally"))
                .and()
                .body("lastName", equalTo("Simpson"))
                .and()
                .body("password", equalTo("point234"))
                .and()
                .body("email", equalTo("sally@aol.com"));
    }

    @Test
    public void testPostAerobics() {
        JSONObject record = new JSONObject()
                .put("seconds", 1616)
                .put("name", "Dummy3");

        given()
                .relaxedHTTPSValidation()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(record.toString())
                .when()
                .post("https://staging.tiered-planet.net/werk-it-back-end/aerobics/user/11")
                .then()
                .assertThat()
                // Status code 201 would be better
                .statusCode(200);
    }

    @Test
    public void testPostWeight() {
        JSONObject record = new JSONObject()
                .put("pounds", 2462)
                .put("sets", 10)
                .put("name", "bench press")
                .put("reps", 15);
        JSONObject response = new JSONObject(
                given()
                        .relaxedHTTPSValidation()
                        .accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .body(record.toString())
                        .when()
                        .post("https://staging.tiered-planet.net/werk-it-back-end/weights/user/1")
                        .then()
                        .assertThat()
                        // Status code 201 would be better
                        .statusCode(200)
                        .extract()
                        .asPrettyString());
        assertEquals(response.getInt("pounds"), 2462);
        assertEquals(response.getInt("sets"), 10);
        assertEquals(response.getInt("reps"), 15);
        assertNotEquals(Integer.valueOf(response.getInt("userId")), "");
        assertNotEquals(Integer.valueOf(response.getInt("id")), "");
    }
}
