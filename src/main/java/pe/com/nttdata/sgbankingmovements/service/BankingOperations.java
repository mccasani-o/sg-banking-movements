package pe.com.nttdata.sgbankingmovements.service;

import pe.com.nttdata.sgbankingmovements.model.BalanceResponse;
import reactor.core.publisher.Mono;

public interface BankingOperations {

    Mono<Void> deposit(String productId, double amount);
    Mono<Void> withdrawMoney(String productId, double amount);
    Mono<BalanceResponse> checkBalance(String productId);
}
