package dto;

public class CreateOrderResponseDto {
    private String name;
    private CreateOrderResponseOrderDto order;
    private boolean success;

    public CreateOrderResponseDto(String name, CreateOrderResponseOrderDto order, boolean success) {
        this.name = name;
        this.order = order;
        this.success = success;
    }

    public CreateOrderResponseDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CreateOrderResponseOrderDto getOrder() {
        return order;
    }

    public void setOrder(CreateOrderResponseOrderDto order) {
        this.order = order;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
