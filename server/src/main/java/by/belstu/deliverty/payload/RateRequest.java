package by.belstu.deliverty.payload;

import lombok.Data;

@Data
public class RateRequest {
    private Long orderId;
    private Long rate;
    private String message;
}
