package by.belstu.deliverty.entity;

import lombok.Data;

@Data
public class Orders {
    private Long id;
    private Long userId;
    private Long driverId;
    private Long orderStatusId;
    private String startPoint;
    private String endPoint;
    private Double price;
}
