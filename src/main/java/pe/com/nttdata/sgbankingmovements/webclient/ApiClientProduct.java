package pe.com.nttdata.sgbankingmovements.webclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pe.com.nttdata.sgbankingmovements.exception.CustomerException;
import pe.com.nttdata.sgbankingmovements.model.ProductResponse;
import reactor.core.publisher.Mono;

@Slf4j
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
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    log.error("Client not found for ID: {}", id);
                    return Mono.error(new CustomerException("Product not found", "404", HttpStatus.NOT_FOUND));
                })
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    log.error("Server error while retrieving Product for ID: {}", id);
                    return Mono.error(new CustomerException("Server error occurred", "500", HttpStatus.INTERNAL_SERVER_ERROR));
                })
                .bodyToMono(ProductResponse.class);
    }

}
