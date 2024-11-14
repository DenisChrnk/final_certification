package api.helpers;

import api.models.EmployeeDb;
import com.github.javafaker.Faker;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;

import java.sql.*;

public class DbHelper {

    private Connection connection;

    public DbHelper(Connection connection) {
        this.connection = connection;
    }

    Faker faker = new Faker();

    @Step("Добавление компании в БД")
    public int insertCompany() throws SQLException {
        String name = faker.company().name();
        String description = faker.commerce().department();

        String SQL = "insert into company(\"name\", description) values (?, ?)";
        PreparedStatement statement = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, name);
        statement.setString(2, description);
        sqlLog(SQL.replace("?, ?", name + ", " + description));

        statement.executeUpdate();
        ResultSet set = statement.getGeneratedKeys();
        set.next();
        return set.getInt("id");
    }

    @Step("Добавление сотрудника в БД")
    public int insertEmployeeToCompany(int compId) throws SQLException {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String phone = faker.phoneNumber().cellPhone();
        int companyId = compId;

        String SQL = "insert into employee(first_name, last_name, phone, company_id) values (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setString(3, phone);
        statement.setInt(4, companyId);
        sqlLog(SQL.replace("?, ?, ?, ?", firstName + ", " + lastName + ", " + phone + ", " + companyId));

        statement.execute();
        ResultSet set = statement.getGeneratedKeys();
        set.next();
        return set.getInt("id");
    }

    @Step("Подсчет сотрудников компании через БД")
    public int selectQuantityEmployeesOfComp(int companyId) throws SQLException {
        String SQL = "select count(*) from employee where company_id = ?";
        PreparedStatement statement = connection.prepareStatement(SQL);
        statement.setInt(1, companyId);
        sqlLog(SQL.replace("?", Integer.toString(companyId)));

        ResultSet result = statement.executeQuery();
        result.next();
        return result.getInt("count");
    }

    @Step("Отбор в БД всех сотрудников компании")
    public ResultSet selectSetEmployeesByCompanyId(int companyId) throws SQLException {
        String SQL = "select * from employee where company_id = ?";
        PreparedStatement statement = connection.prepareStatement(SQL);
        statement.setInt(1, companyId);
        sqlLog(SQL.replace("?", Integer.toString(companyId)));

        return statement.executeQuery();
    }

    @Step("Отбор в БД сотрудника по ID")
    public EmployeeDb selectEmployeeById(int employeeId) throws SQLException {
        String SQL = "select * from employee where id = ?";
        PreparedStatement statement = connection.prepareStatement(SQL);
        statement.setInt(1, employeeId);
        sqlLog(SQL.replace("?", Integer.toString(employeeId)));

        ResultSet result = statement.executeQuery();
        result.next();
        return new EmployeeDb(result.getInt("id"),
                result.getString("first_name"),
                result.getString("last_name"),
                result.getString("phone"),
                result.getInt("company_id"));
    }

    @Step("Добавление в БД сотрудника со всеми данными")
    public int insertEmployeeWithAllData(int compId) throws SQLException {
        String firstName = "Bob";
        String lastName = "Bob";
        String middleName = "Bob";
        int companyId = compId;
        String email = faker.internet().emailAddress();
        String url = faker.internet().url();
        String phone = faker.phoneNumber().cellPhone();
        boolean isActive = faker.bool().bool();

        String SQL = "insert into employee(is_active, first_name, last_name, middle_name, phone, email, avatar_url, company_id) values (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
        statement.setBoolean(1, isActive);
        statement.setString(2, firstName);
        statement.setString(3, lastName);
        statement.setString(4, middleName);
        statement.setString(5, phone);
        statement.setString(6, email);
        statement.setString(7, url);
        statement.setInt(8, companyId);
        sqlLog(SQL.replace("?, ?, ?, ?, ?, ?, ?, ?", isActive + ", " + firstName + ", " + lastName + ", " + middleName + ", " + phone + ", " + email + ", " + url + ", " + companyId));

        statement.executeUpdate();
        ResultSet set = statement.getGeneratedKeys();
        set.next();
        return set.getInt("id");
    }

    @Attachment(value = "sql", type = "text/plain")
    private String sqlLog(String query) {
        return query;
    }
}