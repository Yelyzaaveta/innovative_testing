package chnu.edu.APILAB.model;


/*
 @author  Orynchuk Yelyzaveta
  @project   APILAB
  @class  Item
  @version  1.0.0 
  @since 27/09/2026 - 18.59
*/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Item {
    private String id;
    private String name;
    private String code;
    private  int quantity;

    public Item(String name, String code, int quantity) {
        this.name = name;
        this.code = code;
        this.quantity = quantity;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Item item)) return false;

        return getId().equals(item.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
