package pe.com.nttdata.sgbankingmovements.service;

import pe.com.nttdata.sgbankingmovements.model.MovementRequest;
import pe.com.nttdata.sgbankingmovements.model.MovementResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovementService {

    Mono<Void> insert(MovementRequest movementRequest);

    Flux<MovementResponse>getAll();

    Mono<MovementResponse> findById(String id);

    Mono<Void> update(String id, MovementRequest productRequest);

    Mono<Void> delete(String id);
}
