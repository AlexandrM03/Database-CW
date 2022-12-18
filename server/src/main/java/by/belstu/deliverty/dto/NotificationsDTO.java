package by.belstu.deliverty.dto;

import lombok.Data;

import java.util.Date;

@Data
public class NotificationsDTO {
    private Long id;
    private Date operationDate;
    private String message;
}
