package api.models;

public record UpdateEmployeeRequest(String lastName, String email, String url, String phone, boolean isActive) {
}