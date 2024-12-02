package pe.com.nttdata.sgbankingmovements.webclient.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {
    private String id;

    private String productType;

    private Double balance;

    private Integer limitMnthlyMovements;

    private Integer dayMovement;

    private CustomerDto customer;
}
