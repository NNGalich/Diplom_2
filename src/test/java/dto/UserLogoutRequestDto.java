package dto;

public class UserLogoutRequestDto {
    private String token;

    public UserLogoutRequestDto(String token) {
        this.token = token;
    }

    public UserLogoutRequestDto() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
