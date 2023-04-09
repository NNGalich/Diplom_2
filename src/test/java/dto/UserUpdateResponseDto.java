package dto;

public class UserUpdateResponseDto {
    private boolean success;
    private UserDto user;

    public UserUpdateResponseDto(boolean success, UserDto user) {
        this.success = success;
        this.user = user;
    }

    public UserUpdateResponseDto() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
