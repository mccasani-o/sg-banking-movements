package pe.com.nttdata.sgbankingmovements.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pe.com.nttdata.sgbankingmovements.model.entity.Movement;

public interface MovementRepository extends ReactiveMongoRepository<Movement, String> {
}
