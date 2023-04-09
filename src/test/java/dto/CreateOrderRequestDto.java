package dto;

import java.util.List;

public class CreateOrderRequestDto {
    private List<String> ingredients;

    public CreateOrderRequestDto(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public CreateOrderRequestDto() {
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
