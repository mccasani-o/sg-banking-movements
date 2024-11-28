package pe.com.nttdata.sgbankingmovements.service.impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pe.com.nttdata.sgbankingmovements.exception.CustomerException;
import pe.com.nttdata.sgbankingmovements.mapper.MovementsMapper;
import pe.com.nttdata.sgbankingmovements.model.MovementRequest;
import pe.com.nttdata.sgbankingmovements.model.MovementResponse;
import pe.com.nttdata.sgbankingmovements.model.ProductResponse;
import pe.com.nttdata.sgbankingmovements.repository.MovementRepository;
import pe.com.nttdata.sgbankingmovements.service.MovementService;
import pe.com.nttdata.sgbankingmovements.webclient.ApiClientProduct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class MovementServiceImpl implements MovementService {

    private final MovementRepository movementRepository;
    private final ApiClientProduct clientProduct;
    private final MovementsMapper  movementsMapper;

    public MovementServiceImpl(MovementRepository movementRepository, ApiClientProduct clientProduct, MovementsMapper movementsMapper) {
        this.movementRepository = movementRepository;
        this.clientProduct = clientProduct;
        this.movementsMapper = movementsMapper;
    }

    @Override
    public Mono<Void> insert(MovementRequest movementRequest) {
        return this.findByProductId(movementRequest.getProductId())
                .flatMap(productResponse ->
                        this.movementRepository.save(this.movementsMapper.toMovement(movementRequest, productResponse)))
                .then();
    }



    @Override
    public Flux<MovementResponse> getAll() {
        return this.movementRepository.findAll()
                .switchIfEmpty(Mono.error(new CustomerException("No se encontró lista de movimiemientos ", "204", HttpStatus.OK)))
                .flatMap(movement -> this.findByProductId(movement.getProductId()).map(productResponse -> this.movementsMapper.toMovementResponse(movement, productResponse)));

    }



    @Override
    public Mono<MovementResponse> findById(String id) {
        return this.findByMovementId(id);
    }

    @Override
    public Mono<Void> update(String id, MovementRequest movementRequest) {
        return this.findByMovementId(id)
                .flatMap(movementResponse -> this.movementRepository.save(this.movementsMapper.toMovementUpdate(movementRequest,movementResponse)))
                .then();
    }



    @Override
    public Mono<Void> delete(String id) {
        return this.findByMovementId(id)
                .flatMap(movementResponse ->  this.movementRepository.deleteById(movementResponse.getId()));
    }

    private Mono<MovementResponse> findByMovementId(String id){
        return this.movementRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomerException("Operation not found with the id ".concat(id),"404", HttpStatus.NOT_FOUND)))
                .map(this.movementsMapper::toMovementResponse);
    }


    @CircuitBreaker(name="find-product", fallbackMethod = "getProductFallback")
    private Mono<ProductResponse> findByProductId(String id){
        return this.clientProduct.findByProductId(id);
    }

    private Mono<ProductResponse> getProductFallback(String id, Exception e) {
        log.error("Fallback activated for id: " + id + ", exception: " + e.getMessage());
        return Mono.error(new CustomerException("ERROR","500", HttpStatus.INTERNAL_SERVER_ERROR));
    }

}
