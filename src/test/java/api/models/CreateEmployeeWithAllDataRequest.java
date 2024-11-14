package api.models;

public record CreateEmployeeWithAllDataRequest(int id, String firstName, String lastName, String middleName, int companyId, String email, String url, String phone, String birthdate, boolean isActive) {
}