package dto;

import java.util.List;

public class ListIngredientsResponseDto {
    private boolean success;
    private List<ListIngredientsResponseIngredientDto> data;

    public ListIngredientsResponseDto() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<ListIngredientsResponseIngredientDto> getData() {
        return data;
    }

    public void setData(List<ListIngredientsResponseIngredientDto> data) {
        this.data = data;
    }
}
