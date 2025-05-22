package service;

public record RegisterRequest(
        String username,
        String passwd,
        String email
) {

}
