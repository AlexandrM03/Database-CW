package by.belstu.deliverty.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDTO {
    private Long id;
    private String startPoint;
    private String endPoint;
    private Double price;
    private Date startDate;
    private Date endDate;
    private String driverLastName;
    private String driverFirstName;
    private Double driverRating;
    private String status;
}
