package tests;

import dto.CreateUserRequest;
import dto.ErrorMessageResponse;
import dto.UnsuccessfulUserCreateRequest;
import dto.User;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class CreateUserTest extends ApiBaseTest{

    String endpoint = "/users";
    String endpointDelete = "/users/";


    @Test
    public void successfulCreateUserWithBuilder()  {
        CreateUserRequest requestBody = CreateUserRequest.builder()
                .email("hkkdsddffdsdasjh@gmail.com")
                .full_name("Vladimir")
                .password("123456")
                .generate_magic_link(false)
                .build();

        Response response = postRequest(endpoint, 201, requestBody);
        Response responseDelete = deleteRequest(endpointDelete+requestBody.getEmail() ,200);
    }

    @Test
    public void successfulCreateUser() {
        CreateUserRequest requestBody = new CreateUserRequest(fullName, generateRandomEmail(), password, false);
        User response = postRequest(endpoint, 201, requestBody)
                .body().jsonPath().getObject("", User.class);
        assertEquals(requestBody.getEmail(), response.getEmail());
        assertEquals(requestBody.getFull_name(), response.getFull_name());
        assertFalse(response.getFull_name().isEmpty());
        assertFalse(response.getEmail().isEmpty());
        assertEquals(response.getCreated(), response.getUpdated());

        Response responseDelete = deleteRequest(endpointDelete+requestBody.getEmail() ,200);

    }

    @Test
    public void unsuccessfulCreateUserWithoutMagicLink() {
        UnsuccessfulUserCreateRequest requestBody = new UnsuccessfulUserCreateRequest(fullName, generateRandomEmail(), password, "");
        ErrorMessageResponse response = postRequest(endpoint, 400, requestBody)
                .body().jsonPath().getObject("", ErrorMessageResponse.class);
    }

    @Test
    public void unsuccessfulCreateUserWithMagicLinkTrue() {
        CreateUserRequest requestBody = new CreateUserRequest(fullName, generateRandomEmail(), password, true);
        ErrorMessageResponse response = postRequest(endpoint, 400, requestBody)
                .body().jsonPath().getObject("", ErrorMessageResponse.class);
    }

    @Test
    public void unsuccessfulCreateUserWithInvalidPassword() {
        CreateUserRequest requestBody = new CreateUserRequest(fullName, generateRandomEmail(), "111", false);
        ErrorMessageResponse response = postRequest(endpoint, 400, requestBody)
                .body().jsonPath().getObject("", ErrorMessageResponse.class);
    }

    @Test
    public void unsuccessfulCreateUserWithInvalidEmail() {
        CreateUserRequest requestBody = new CreateUserRequest(fullName, "test_email", password, false);
        ErrorMessageResponse response = postRequest(endpoint, 400, requestBody)
                .body().jsonPath().getObject("", ErrorMessageResponse.class);
    }

    @Test
    public void unsuccessfulCreateUserWithPutRequest() {
        CreateUserRequest requestBody = new CreateUserRequest(fullName, generateRandomEmail(), password, false);
        ErrorMessageResponse response = putRequest(endpoint, 404, requestBody)
                .body().jsonPath().getObject("", ErrorMessageResponse.class);
        Assert.assertEquals("Not Found", response.getCode());
    }

    @Test
    public void unsuccessfulCreateUserWithGetRequest() {
        CreateUserRequest requestBody = new CreateUserRequest(fullName, generateRandomEmail(), password, false);
        ErrorMessageResponse response = getRequest(endpoint, 400)
                .body().jsonPath().getObject("", ErrorMessageResponse.class);

    }

    @Test
    public void unsuccessfulCreateCreatedUser() {
        CreateUserRequest requestBody = new CreateUserRequest(fullName, "test_email@gmail.com", password, false);
        User response = postRequest(endpoint, 201, requestBody)
                .body().jsonPath().getObject("", User.class);
        CreateUserRequest requestBody1 = new CreateUserRequest(fullName, "test_email@gmail.com", password, false);
        ErrorMessageResponse response1 = postRequest(endpoint, 400, requestBody1)
                .body().jsonPath().getObject("", ErrorMessageResponse.class);

    }

    @Test
    public void unsuccessfulCreateUserWithoutFullName() {
        CreateUserRequest requestBody = new CreateUserRequest("", generateRandomEmail(), password, false);
        ErrorMessageResponse response = postRequest(endpoint, 400, requestBody)
                .body().jsonPath().getObject("", ErrorMessageResponse.class);
    }

    @Test
    public void unsuccessfulCreateUserWithoutEmail() {
        CreateUserRequest requestBody = new CreateUserRequest(fullName, "", password, false);
        ErrorMessageResponse response = postRequest(endpoint, 400, requestBody)
                .body().jsonPath().getObject("", ErrorMessageResponse.class);
        Assert.assertEquals("Bad Request", response.getCode());
        Assert.assertEquals("Something went wrong, please try again.", response.getMessage());
    }

    @Test
    public void unsuccessfulCreateUserWithoutPassword() {
        CreateUserRequest requestBody = new CreateUserRequest(fullName, generateRandomEmail(), "", false);
        ErrorMessageResponse response = postRequest(endpoint, 400, requestBody)
                .body().jsonPath().getObject("", ErrorMessageResponse.class);
    }
}
