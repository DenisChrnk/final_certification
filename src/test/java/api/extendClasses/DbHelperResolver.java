package api.extendClasses;

import api.helpers.DbHelper;
import org.junit.jupiter.api.extension.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DbHelperResolver implements ParameterResolver, BeforeAllCallback, AfterAllCallback {
    private Connection connection;

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(DbHelper.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return new DbHelper(connection);
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        String appConfigPath = "src/test/resources/env.properties";

        Properties properties = new Properties();
        properties.load(new FileInputStream(appConfigPath));

        String connectionString = properties.getProperty("db.connection_string");
        String username = properties.getProperty("db.user");
        String password = properties.getProperty("db.pass");

        connection = DriverManager.getConnection(connectionString, username, password);
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        if (connection != null){
            connection.close();
        }
    }
}