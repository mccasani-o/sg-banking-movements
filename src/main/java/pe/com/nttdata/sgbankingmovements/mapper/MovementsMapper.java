package pe.com.nttdata.sgbankingmovements.mapper;

import org.springframework.stereotype.Component;
import pe.com.nttdata.sgbankingmovements.model.MovementRequest;
import pe.com.nttdata.sgbankingmovements.model.MovementResponse;
import pe.com.nttdata.sgbankingmovements.model.ProductResponse;
import pe.com.nttdata.sgbankingmovements.model.entity.Movement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class MovementsMapper {

    public Movement toMovement(MovementRequest movementRequest, ProductResponse productResponse) {
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

    public MovementResponse toMovementResponse(Movement movement, ProductResponse productResponse) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        MovementResponse movementResponse=new MovementResponse();
        movementResponse.setId(movement.getId());
        movementResponse.setMovementType(movement.getMovementType());
        movementResponse.setAmount(movement.getAmount());
        movementResponse.setDate(movement.getDate().format(formatter));
        movementResponse.setProduct(productResponse);
        return movementResponse;
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
