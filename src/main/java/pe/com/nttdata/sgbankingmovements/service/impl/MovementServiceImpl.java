package pe.com.nttdata.sgbankingmovements.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.com.nttdata.sgbankingmovements.model.entity.Movement;
import pe.com.nttdata.sgbankingmovements.model.MovementRequest;
import pe.com.nttdata.sgbankingmovements.model.MovementResponse;
import pe.com.nttdata.sgbankingmovements.model.ProductResponse;
import pe.com.nttdata.sgbankingmovements.repository.MovementRepository;
import pe.com.nttdata.sgbankingmovements.service.MovementService;
import pe.com.nttdata.sgbankingmovements.webclient.ApiClientProduct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class MovementServiceImpl implements MovementService {

    private final MovementRepository movementRepository;
    private final ApiClientProduct clientProduct;

    public MovementServiceImpl(MovementRepository movementRepository, ApiClientProduct clientProduct) {
        this.movementRepository = movementRepository;
        this.clientProduct = clientProduct;
    }

    @Override
    public Mono<Void> insert(MovementRequest movementRequest) {
        return this.findByProductId(movementRequest.getProductId())
                .flatMap(productResponse -> this.movementRepository.save(toMovement(movementRequest, productResponse)))
                .then();
    }



    @Override
    public Flux<MovementResponse> getAll() {
        return this.movementRepository.findAll()
                .map(movement -> toMovementResp(movement));

    }

    private MovementResponse toMovementResp(Movement movement) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        MovementResponse movementResponse=new MovementResponse();
        movementResponse.setId(movement.getId());
        movementResponse.setMovementType(movement.getMovementType());
        movementResponse.setAmount(movement.getAmount());
        movementResponse.setDate(movement.getDate().format(formatter));
        return movementResponse;
    }


    @Override
    public Mono<MovementResponse> findById(String id) {
        return this.movementRepository.findById(id)
                .flatMap(movement -> this.findByProductId(movement.getId())
                        .map(productResponse->this.toMovementResponse(movement,productResponse)));
    }

    @Override
    public Mono<Void> update(String id, MovementRequest movementRequest) {
        return this.movementRepository.findById(id)
                .flatMap(movement -> this.movementRepository.save(this.toMovementUpdate(movementRequest,movement)))
                .then();
    }



    @Override
    public Mono<Void> delete(String id) {
        return this.movementRepository.deleteById(id);
    }



    private Mono<ProductResponse> findByProductId(String id){
        return this.clientProduct.findByProductId(id);
    }

    private MovementResponse toMovementResponse(Movement movement, ProductResponse productResponse) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        MovementResponse movementResponse=new MovementResponse();
        movementResponse.setId(movement.getId());
        movementResponse.setMovementType(movement.getMovementType());
        movementResponse.setAmount(movement.getAmount());
        movementResponse.setDate(movement.getDate().format(formatter));
        movementResponse.setProduct(productResponse);

        return movementResponse;
    }

    private Movement toMovement(MovementRequest movementRequest, ProductResponse productResponse) {
        return Movement.builder()
                .movementType(movementRequest.getMovementType().getValue())
                .amount(movementRequest.getAmount())
                .date(LocalDateTime.now())
                .productId(productResponse.getId())
                .build();
    }

    private Movement toMovementUpdate(MovementRequest movementRequest, Movement movement) {
        Movement movementResponse=new Movement();
        movementResponse.setId(movement.getId());
        movementResponse.setMovementType(movementRequest.getMovementType().getValue());
        movementResponse.setAmount(movementRequest.getAmount());
        movementResponse.setDate(movement.getDate());
        movementResponse.setProductId(movementRequest.getProductId());
        return movementResponse;
    }

}
