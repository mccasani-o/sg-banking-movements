package pe.com.nttdata.sgbankingmovements.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
    @ResponseStatus(HttpStatus.CREATED)
    Mono<Void> insert(@RequestBody MovementRequest request) {
        return this.movementService.insert(request);
    }

    @GetMapping
    public Flux<MovementResponse> getAll() {
        return this.movementService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<MovementResponse> getById(@PathVariable String id) {
        return this.movementService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        return this.movementService.delete(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<Void> update(@PathVariable String id, @RequestBody MovementRequest movementRequest) {
        return this.movementService.update(id, movementRequest);
    }

}
