package hello.itemservice.domain.item;

import lombok.Data;

@Data //위험하다! 주의! 예측할 수 없다. 보통 DTO에만 쓴다.
public class Item {
    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
