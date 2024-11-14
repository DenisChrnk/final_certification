package api.extendClasses;

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import org.junit.jupiter.api.extension.*;

import java.io.FileInputStream;
import java.util.Properties;

public class ValidationFilterResolver implements ParameterResolver, BeforeAllCallback {
    public static String swaggerURL;

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(OpenApiValidationFilter.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        String SWAGGER = swaggerURL;
        return new OpenApiValidationFilter(SWAGGER);
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        String appConfigPath = "src/test/resources/env.properties";

        Properties properties = new Properties();
        properties.load(new FileInputStream(appConfigPath));

        swaggerURL = properties.getProperty("swagger");
    }
}