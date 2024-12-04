package pe.com.nttdata.sgbankingmovements.mapper;

import org.springframework.stereotype.Component;
import pe.com.nttdata.sgbankingmovements.model.MovementRequest;
import pe.com.nttdata.sgbankingmovements.model.MovementResponse;
import pe.com.nttdata.sgbankingmovements.model.ProductResponse;
import pe.com.nttdata.sgbankingmovements.model.entity.Movement;
import pe.com.nttdata.sgbankingmovements.webclient.model.ProductDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class MovementsMapper {

    public Movement toMovement(MovementRequest movementRequest, ProductDto productResponse) {
        return Movement.builder()
                .movementType(movementRequest.getMovementType().getValue())
                .amount(movementRequest.getAmount())
                .date(LocalDateTime.now())
                .productId(productResponse.getId())
                .build();
    }

    public MovementResponse toMovementResponse(Movement movement) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        MovementResponse movementResponse=new MovementResponse();
        movementResponse.setId(movement.getId());
        movementResponse.setMovementType(movement.getMovementType());
        movementResponse.setAmount(movement.getAmount());
        movementResponse.setDate(movement.getDate().format(formatter));
        return movementResponse;
    }

    public MovementResponse toMovementResponse(Movement movement, ProductDto productResponse) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        MovementResponse movementResponse=new MovementResponse();
        movementResponse.setId(movement.getId());
        movementResponse.setMovementType(movement.getMovementType());
        movementResponse.setAmount(movement.getAmount());
        movementResponse.setDate(movement.getDate().format(formatter));
        movementResponse.setProduct(this.buildProductResponse(productResponse));
        return movementResponse;
    }

    public ProductResponse buildProductResponse(ProductDto productDto){
        ProductResponse response=new ProductResponse();
        response.setId(productDto.getId());
        response.productType(productDto.getProductType());
        response.balance(productDto.getBalance());
        response.limitMnthlyMovements(productDto.getLimitMnthlyMovements());
        response.dayMovement(productDto.getDayMovement());
        response.setCustomerId(productDto.getCustomerId());
        return response;
    }


    public Movement toMovementUpdate(MovementRequest movementRequest, MovementResponse movement) {
        Movement movementResponse=new Movement();
        movementResponse.setId(movement.getId());
        movementResponse.setMovementType(movementRequest.getMovementType().getValue());
        movementResponse.setAmount(movementRequest.getAmount());
        movementResponse.setDate(LocalDateTime.now());
        movementResponse.setProductId(movementRequest.getProductId());
        return movementResponse;
    }



}
