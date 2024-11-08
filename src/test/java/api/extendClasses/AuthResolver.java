package api.extendClasses;

import api.models.AuthResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import api.models.AuthRequest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class AuthResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(String.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        String appConfigPath = "src/test/resources/env.properties";

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(appConfigPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String name = properties.getProperty("api.user");
        String pass = properties.getProperty("api.pass");
        RestAssured.baseURI = properties.getProperty("api.baseURI");

        AuthRequest authData = new AuthRequest(name, pass);
        return given()
                .basePath("auth/login")
                .body(authData)
                .contentType(ContentType.JSON)
                .when()
                .post()
                .body().as(AuthResponse.class).userToken();
    }

}
