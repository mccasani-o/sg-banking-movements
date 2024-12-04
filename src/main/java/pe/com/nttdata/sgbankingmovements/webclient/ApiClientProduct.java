package pe.com.nttdata.sgbankingmovements.webclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pe.com.nttdata.sgbankingmovements.exception.CustomerException;
import pe.com.nttdata.sgbankingmovements.model.ApiErrorResponse;
import pe.com.nttdata.sgbankingmovements.model.ProductResponse;
import pe.com.nttdata.sgbankingmovements.webclient.model.ProductDto;
import pe.com.nttdata.sgbankingmovements.webclient.model.ProductRequest;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ApiClientProduct {

    private final WebClient webClient;

    public ApiClientProduct(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8882/api/v1").build();
    }

    public Mono<ProductDto> findByProductId(String id) {
        return this.webClient.get()
                .uri("/products/{id}", id)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode().is4xxClientError() || ex.getStatusCode().is5xxServerError()) {
                        // Extraer el cuerpo de error y convertirlo a ApiErrorResponse
                        return Mono.justOrEmpty(extractApiErrorResponse(ex))
                                .flatMap(errorResponse -> {
                                    log.error("Error: {}", errorResponse.getMessage());
                                    return Mono.error(new CustomerException(errorResponse.getMessage(), errorResponse.getCode(),HttpStatus.BAD_REQUEST));
                                });
                    }
                    return Mono.error(ex); // Propaga otros errores
                })
                .doOnNext(response ->
                        log.info("Product Response: {}", response)
                );
    }



    public Mono<Void> updateProduct(ProductRequest productRequest) {
        return this.webClient.put()
                .uri("/products/{id}", productRequest.getId())
                .bodyValue(productRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode().is4xxClientError() || ex.getStatusCode().is5xxServerError()) {
                        return Mono.justOrEmpty(extractApiErrorResponse(ex))
                                .flatMap(errorResponse -> {
                                    log.error("Error: {}", errorResponse.getMessage());
                                    return Mono.error(new CustomerException(errorResponse.getMessage(), errorResponse.getCode(),HttpStatus.BAD_REQUEST));
                                });
                    }
                    return Mono.error(ex);
                });
    }

    private ApiErrorResponse extractApiErrorResponse(WebClientResponseException ex) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(ex.getResponseBodyAsString(), ApiErrorResponse.class);
        } catch (Exception e) {
            log.error("Error parsing error response: {}", e.getMessage());
            throw new CustomerException("Error parsing error response","400", HttpStatus.BAD_REQUEST);
        }
    }

}
