package pe.com.nttdata.sgbankingmovements.mapper;

import pe.com.nttdata.sgbankingmovements.model.MovementResponse;
import pe.com.nttdata.sgbankingmovements.model.ProductResponse;

public final class UtilMock {
    private UtilMock() {
    }

    public static MovementResponse buildMovementResponse() {
        MovementResponse movementResponse=new MovementResponse();
        movementResponse.setId("374GGD33");
        movementResponse.setMovementType("DEPOSITO");
        movementResponse.setAmount(5500.0);
        movementResponse.setDate("28/11/2024 16:06:20");
        movementResponse.setProduct(toProduct());

        return movementResponse;
    }

    public static ProductResponse toProduct() {
        ProductResponse productResponse=new ProductResponse();
        productResponse.setId("344448");
        productResponse.setProductType("");
        productResponse.setBalance(200.0);
        productResponse.dayMovement("");
        productResponse.setLimitMnthlyMovements(1);
        return productResponse;
    }

}
