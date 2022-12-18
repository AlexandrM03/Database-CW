package by.belstu.deliverty.entity;

import lombok.Data;

@Data
public class OrderItems {
    private Long id;
    private Long orderId;
    private String name;
    private Long weight;
    private Long volume;
}
