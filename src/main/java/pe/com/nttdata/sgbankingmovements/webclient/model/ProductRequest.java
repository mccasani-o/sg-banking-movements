package pe.com.nttdata.sgbankingmovements.webclient.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductRequest {


    private String id;

    private String productType;

    private Double balance;

    private Integer limitMnthlyMovements;

    private String dayMovement;

    private BigDecimal limitCredit;

    private String clientId;
}
