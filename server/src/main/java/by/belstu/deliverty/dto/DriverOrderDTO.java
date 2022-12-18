package by.belstu.deliverty.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DriverOrderDTO {
    private Long id;
    private String startPoint;
    private String endPoint;
    private Double price;
    private Date startDate;
    private String status;
    private String username;
}
