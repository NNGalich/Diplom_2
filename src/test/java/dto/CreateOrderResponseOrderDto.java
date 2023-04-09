package dto;

public class CreateOrderResponseOrderDto {
    private int number;

    public CreateOrderResponseOrderDto(int number) {
        this.number = number;
    }

    public CreateOrderResponseOrderDto() {
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
