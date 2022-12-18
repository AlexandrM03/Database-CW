package by.belstu.deliverty.payload;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private String startPoint;
    private String endPoint;
}
