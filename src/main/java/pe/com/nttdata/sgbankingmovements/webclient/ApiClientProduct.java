package pe.com.nttdata.sgbankingmovements.webclient;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pe.com.nttdata.sgbankingmovements.model.ProductResponse;
import reactor.core.publisher.Mono;

@Component
public class ApiClientProduct {

    private final WebClient webClient;

    public ApiClientProduct(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8882/api/v1").build();
    }

    public Mono<ProductResponse> findByProductId(String id) {
        return this.webClient.get()
                .uri("/products/{id}", id)

                .retrieve()

                .bodyToMono(ProductResponse.class);
    }

}
