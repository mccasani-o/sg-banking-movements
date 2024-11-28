package pe.com.nttdata.sgbankingmovements.controller;

import lombok.Data;

@Data
public class MovementRequest {

    private String movementType;
    private Double amount;
    private String productId;

}
