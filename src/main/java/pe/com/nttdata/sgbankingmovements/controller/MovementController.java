package pe.com.nttdata.sgbankingmovements.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.com.nttdata.sgbankingmovements.exception.CustomerException;
import pe.com.nttdata.sgbankingmovements.model.MovementRequest;
import pe.com.nttdata.sgbankingmovements.model.MovementResponse;
import pe.com.nttdata.sgbankingmovements.service.MovementService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/movements")
public class MovementController {

    private final MovementService movementService;

    public MovementController(MovementService movementService) {
        this.movementService = movementService;
    }


    @PostMapping
    Mono<Void> insert(@RequestBody MovementRequest request) {
        return this.movementService.insert(request);
    }

    @GetMapping
    public Flux<MovementResponse> getAll() {
        return this.movementService.getAll()
                .switchIfEmpty(Mono.error(new CustomerException("No se encontró lista de movimiemientos ", "204", HttpStatus.OK)))
                .doOnNext(movementResponse -> System.out.println(movementResponse));
    }

    @GetMapping("/{id}")
    public Mono<MovementResponse> getById(@PathVariable String id) {
        return this.movementService.findById(id);


    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<Void> movementsIdPut(@PathVariable String operationId, @RequestBody MovementRequest request) {
        return this.movementService.update(operationId, request);


    }

    @DeleteMapping("/{operationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> movementsIdDelete(@PathVariable String operationId) {
        return this.movementService.delete(operationId);
    }




}
