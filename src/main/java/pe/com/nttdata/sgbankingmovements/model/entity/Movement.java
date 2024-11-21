package pe.com.nttdata.sgbankingmovements.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "products")
public class Movement {

    @Id
    private String id;
    private String movementType;
    private double amount;
    private LocalDateTime date;
    private String productId;
}
