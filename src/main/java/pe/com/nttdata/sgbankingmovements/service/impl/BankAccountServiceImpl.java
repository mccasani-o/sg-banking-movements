package pe.com.nttdata.sgbankingmovements.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pe.com.nttdata.sgbankingmovements.exception.CustomerException;
import pe.com.nttdata.sgbankingmovements.model.BalanceResponse;
import pe.com.nttdata.sgbankingmovements.model.MovementRequest;
import pe.com.nttdata.sgbankingmovements.model.MovementRequest.MovementTypeEnum;
import pe.com.nttdata.sgbankingmovements.model.ProductResponse;
import pe.com.nttdata.sgbankingmovements.service.BankingOperations;
import pe.com.nttdata.sgbankingmovements.service.MovementService;
import pe.com.nttdata.sgbankingmovements.util.DateUtils;
import pe.com.nttdata.sgbankingmovements.webclient.ApiClientProduct;
import pe.com.nttdata.sgbankingmovements.webclient.model.ProductRequest;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Service
public class BankAccountServiceImpl implements BankingOperations {

    private final ApiClientProduct clientProduct;
    private final MovementService movementService;

    @Override
    public Mono<Void> deposit(String productId, double amount) {
        return this.clientProduct.findByProductId(productId)
                .flatMap(productResponse ->
                        this.clientProduct.updateProduct(this.buildProductRequestDeposit(productResponse, amount))
                                .then(this.movementService.insert(buildMovementRequest(productResponse, amount, MovementTypeEnum.DEPOSITO))));

    }


    @Override
    public Mono<Void> withdrawMoney(String productId, double amount) {
        return this.clientProduct.findByProductId(productId)
                .flatMap(productResponse -> {
                    if (productResponse.getBalance() >= amount) {
                        return this.clientProduct.updateProduct(this.buildProductRequestWithdrawal(productResponse, amount))
                                .then(this.movementService.insert(buildMovementRequest(productResponse, amount, MovementTypeEnum.RETIRO)));
                    }

                    return Mono.error(new CustomerException("Saldo insufeciente", "400", HttpStatus.BAD_REQUEST));
                });

    }

    @Override
    public Mono<BalanceResponse> checkBalance(String productId) {
        return this.clientProduct.findByProductId(productId)
                .map(productResponse -> {
                    BalanceResponse response = new BalanceResponse();
                    response.setBalance(BigDecimal.valueOf(productResponse.getBalance()));
                    return response;
                });
    }


    private MovementRequest buildMovementRequest(ProductResponse productResponse, double amount, MovementTypeEnum movementTypeEnum) {
        MovementRequest movementRequest = new MovementRequest();
        movementRequest.setMovementType(movementTypeEnum);
        movementRequest.setDate(DateUtils.getCurrentDateTimeAsString());
        movementRequest.setProductId(productResponse.getId());
        movementRequest.setAmount(amount);
        return movementRequest;
    }

    private ProductRequest buildProductRequestDeposit(ProductResponse productResponse, double amount) {

        return ProductRequest.builder()
                .id(productResponse.getId())
                .productType(productResponse.getProductType())
                .balance(productResponse.getBalance() + amount)
                .limitMnthlyMovements(productResponse.getLimitMnthlyMovements())
                .dayMovement(productResponse.getDayMovement())
                .clientId(productResponse.getCustomerId())
                .build();
    }

    private ProductRequest buildProductRequestWithdrawal(ProductResponse productResponse, double amount) {

        return ProductRequest.builder()
                .id(productResponse.getId())
                .productType(productResponse.getProductType())
                .balance(productResponse.getBalance() - amount)
                .limitMnthlyMovements(productResponse.getLimitMnthlyMovements())
                .dayMovement(productResponse.getDayMovement())
                .clientId(productResponse.getCustomerId())
                .build();
    }

}
