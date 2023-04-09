package dto;

import java.util.List;

public class ListOrdersResponseDto {
    private boolean success;
    private List<ListOrdersResponseOrderDto> orders;
    private int total;
    private int totalToday;

    public ListOrdersResponseDto(boolean success, List<ListOrdersResponseOrderDto> orders, int total, int totalToday) {
        this.success = success;
        this.orders = orders;
        this.total = total;
        this.totalToday = totalToday;
    }

    public ListOrdersResponseDto() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<ListOrdersResponseOrderDto> getOrders() {
        return orders;
    }

    public void setOrders(List<ListOrdersResponseOrderDto> orders) {
        this.orders = orders;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalToday() {
        return totalToday;
    }

    public void setTotalToday(int totalToday) {
        this.totalToday = totalToday;
    }
}
