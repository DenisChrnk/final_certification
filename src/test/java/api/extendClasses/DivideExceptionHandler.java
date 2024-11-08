package api.extendClasses;

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

public class DivideExceptionHandler implements TestExecutionExceptionHandler {
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
       if (throwable.getClass().equals(OpenApiValidationFilter.OpenApiValidationException.class)){
           System.out.println("Тест: " + context.getDisplayName() + "; Результат теста - не прошли по схеме");
       } else {
           throw throwable;
       }
    }
}