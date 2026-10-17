package chnu.edu.APILAB.model.dto;

import chnu.edu.APILAB.model.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * What the client sees (and sends) instead of the raw {@link Item} document.
 * Anything internal (audit fields, heavy data) stays out of this class.
 * The internal id is not shown: the client finds an item by its 5-digit code.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private String name;
    private String code;
    private int quantity;

    public static ItemDto from(Item item) {
        return new ItemDto(item.getName(), item.getCode(), item.getQuantity());
    }

    public Item toItem() {
        return new Item(name, code, quantity);
    }
}
