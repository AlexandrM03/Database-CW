package by.belstu.deliverty.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AllDriverOrdersDTO {
    private Long id;
    private String startPoint;
    private String endPoint;
    private Double price;
    private Date startDate;
    private Date endDate;
    private String status;
}
