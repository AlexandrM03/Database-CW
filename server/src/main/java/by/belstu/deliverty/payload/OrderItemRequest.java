package by.belstu.deliverty.payload;

import lombok.Data;

@Data
public class OrderItemRequest {
    private String name;
    private Long orderId;
    private Long weight;
    private Long volume;
}
