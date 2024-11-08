package api.tests;

import api.extendClasses.*;
import api.helpers.ApiTestHelper;
import api.helpers.DataForTests;
import api.helpers.DbHelper;
import api.models.CreateEmployeeRequest;
import api.models.CreateEmployeeWithAllDataRequest;
import api.models.UpdateEmployeeRequest;
import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import io.qameta.allure.Allure;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import api.extendClasses.*;
import api.models.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

@ExtendWith({ApiTestHelperResolver.class, ValidationFilterResolver.class, AuthResolver.class, DbHelperResolver.class, InsertCompanyResolver.class, DivideExceptionHandler.class})
public class EmployeeContractTests {

    @BeforeAll
    public static void setUp() throws IOException {
        String appConfigPath = "src/test/resources/env.properties";

        Properties properties = new Properties();
        properties.load(new FileInputStream(appConfigPath));

        RestAssured.baseURI = properties.getProperty("api.baseURI");
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @DisplayName("Получение списка сотрудников для компании")
    public void getEmployeesOfCompany(int companyId, DbHelper dbHelper, OpenApiValidationFilter openApiValidationFilter) throws SQLException {
        dbHelper.insertEmployeeWithAllData(companyId);

        step("Получение списка сотрудников для компании", () -> given()
                .filters(openApiValidationFilter, new AllureRestAssured())
                .basePath("employee")
                .when()
                .get("?company=" + companyId));
    }

    @Test
    @DisplayName("Получение списка сотрудников для несуществующей компании")
    public void getEmployeesOfCompanyWithInvalidId(OpenApiValidationFilter openApiValidationFilter) {
        Allure.step("Получение списка сотрудников для несуществующей компании", () -> given()
                .filters(openApiValidationFilter, new AllureRestAssured())
                .basePath("employee")
                .when()
                .get("?company=" + DataForTests.INVALID_ID));
    }

    @Test
    @DisplayName("Добавление нового сотрудника с минимальным набором полей")
    public void addNewEmployee(int companyID, ApiTestHelper apiHelper, String authToken, OpenApiValidationFilter openApiValidationFilter) {
        CreateEmployeeRequest body = apiHelper.createBodyForEmployeeWithCompanyId(companyID);

        step("Добавление нового сотрудника с минимальным набором полей", () -> given()
                .filters(openApiValidationFilter, new AllureRestAssured())
                .basePath("employee")
                .header("x-client-token", authToken)
                .body(body)
                .contentType(ContentType.JSON)
                .when()
                .post());
    }

    @Test
    @DisplayName("Добавление нового сотрудника со всеми полями по схеме")
    public void addNewEmployeeWithAllData(int companyId, ApiTestHelper apiHelper, String authToken, OpenApiValidationFilter openApiValidationFilter) {
        CreateEmployeeWithAllDataRequest body = apiHelper.createBodyForEmployeeWithCompanyIdAllData(companyId);

        step("Добавление нового сотрудника со всеми полями по схеме", () -> given()
                .filters(openApiValidationFilter, new AllureRestAssured())
                .basePath("employee")
                .header("x-client-token", authToken)
                .body(body)
                .contentType(ContentType.JSON)
                .when()
                .post());
    }

    @Test
    @DisplayName("Получение сотрудника по Id")
    public void getEmployeeById(int companyId, DbHelper dbHelper, OpenApiValidationFilter openApiValidationFilter) throws SQLException {
        int employeeId = dbHelper.insertEmployeeWithAllData(companyId);

        step("Получение сотрудника по Id", () -> given()
                .filters(openApiValidationFilter, new AllureRestAssured())
                .basePath("employee")
                .when()
                .get("/" + employeeId));
    }

    @Test
    @DisplayName("Получение сотрудника по несуществующему Id")
    public void getEmployeeByInvalidId(OpenApiValidationFilter openApiValidationFilter) {
        Allure.step("Получение сотрудника по несуществующему Id", () -> given()
                .filters(openApiValidationFilter, new AllureRestAssured())
                .basePath("employee")
                .when()
                .get("/" + DataForTests.INVALID_ID));
    }

    @Test
    @DisplayName("Изменить информацию о сотруднике. Тело запроса согласно схеме")
    public void updateEmployeeInfo(int companyId, DbHelper dbHelper, ApiTestHelper apiHelper, String authToken, OpenApiValidationFilter openApiValidationFilter) throws SQLException {
        int employeeId = dbHelper.insertEmployeeToCompany(companyId);
        UpdateEmployeeRequest newInfoForEmployee = apiHelper.createBodyForUpdate();

        step("Изменить информацию о сотруднике", () -> given()
                .filters(openApiValidationFilter, new AllureRestAssured())
                .basePath("employee")
                .header("x-client-token", authToken)
                .body(newInfoForEmployee)
                .contentType(ContentType.JSON)
                .when()
                .patch("/" + employeeId));
    }

    @Test
    @DisplayName("Изменить информацию о сотруднике. Тело запроса не по схеме")
    public void updateEmployeeInfoWithoutSchema(int companyId, DbHelper dbHelper, ApiTestHelper apiHelper, String authToken, OpenApiValidationFilter openApiValidationFilter) throws SQLException {
        int employeeId = dbHelper.insertEmployeeToCompany(companyId);
        CreateEmployeeRequest newInfoForEmployee = apiHelper.createBodyForEmployeeWithCompanyId(companyId);

        step("Изменить информацию о сотруднике", () -> given()
                .filters(openApiValidationFilter, new AllureRestAssured())
                .basePath("employee")
                .header("x-client-token", authToken)
                .body(newInfoForEmployee)
                .contentType(ContentType.JSON)
                .when()
                .patch("/" + employeeId));
    }

    @Test
    @DisplayName("Изменить информацию несуществующему сотруднику")
    public void updateInvalidEmployeeInfo(ApiTestHelper apiHelper, String authToken, OpenApiValidationFilter openApiValidationFilter) {
        UpdateEmployeeRequest newInfoForEmployee = apiHelper.createBodyForUpdate();

        Allure.step("Изменить информацию несуществующему сотруднику", () -> given()
                .filters(openApiValidationFilter, new AllureRestAssured())
                .basePath("employee")
                .header("x-client-token", authToken)
                .body(newInfoForEmployee)
                .contentType(ContentType.JSON)
                .when()
                .patch("/" + DataForTests.INVALID_ID));
    }
}
