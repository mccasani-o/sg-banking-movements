package pe.com.nttdata.sgbankingmovements.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import pe.com.nttdata.sgbankingmovements.mapper.UtilMock;
import pe.com.nttdata.sgbankingmovements.model.MovementRequest;
import pe.com.nttdata.sgbankingmovements.model.MovementResponse;
import pe.com.nttdata.sgbankingmovements.service.MovementService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MovementControllerTest {
    public static final String URI = "/api/v1/movements";
    @Mock
    private MovementService movementService;

    WebTestClient client;

    private MovementRequest movementRequest;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToController(new MovementController(movementService)).build();
        movementRequest =new MovementRequest();
        movementRequest.setMovementType(MovementRequest.MovementTypeEnum.CONSUMO);
        movementRequest.setAmount(500.0);
        movementRequest.setDate("28/11/2024 16:06:20");
        movementRequest.setProductId("38rhd7");

    }

    @Test
    void insert() {
        when(this.movementService.insert(any(MovementRequest.class)))
                .thenReturn(Mono.empty());

        this.client.post().uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(this.movementRequest), MovementRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void getAll() {
        when(this.movementService.getAll())
                .thenReturn(Flux.just(UtilMock.buildMovementResponse()));

        client.get().uri("/api/v1/movements")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MovementResponse.class)
                .consumeWith(response -> {
                    List<MovementResponse> products = response.getResponseBody();
                    Assertions.assertNotNull(products);
                    assertFalse(products.isEmpty());
                });
    }


    @Test
    void getById() {
        given(this.movementService.findById(anyString()))
                .willReturn(Mono.just(UtilMock.buildMovementResponse()));

        client.get().uri(URI.concat("/".concat("374GGD33")))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MovementResponse.class)
                .consumeWith(response -> {
                    MovementResponse movementResponse = response.getResponseBody();
                    assertNotNull(movementResponse);
                    assertEquals("374GGD33", movementResponse.getId());
                    assertEquals("DEPOSITO", movementResponse.getMovementType());
                });
    }

    @Test
    void delete() {
        when(this.movementService.delete(anyString()))
                .thenReturn(Mono.empty());

        client.delete().uri(URI.concat("/437f8666"))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    void update() {
        given(this.movementService.update(any(String.class), any(MovementRequest.class)))
                .willAnswer(invocationOnMock -> Mono.empty());

        client.put().uri(URI.concat("/437f8666"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(this.movementRequest), MovementRequest.class)
                .exchange()
                .expectStatus()
                .isAccepted();
    }
}