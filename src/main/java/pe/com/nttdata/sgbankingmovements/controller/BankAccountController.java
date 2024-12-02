package pe.com.nttdata.sgbankingmovements.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pe.com.nttdata.sgbankingmovements.model.BalanceResponse;
import pe.com.nttdata.sgbankingmovements.model.DepositRequest;
import pe.com.nttdata.sgbankingmovements.model.WithdrawalRequest;
import pe.com.nttdata.sgbankingmovements.service.BankingOperations;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/movements")

public class BankAccountController {

    private final BankingOperations bankingOperation;
    @PostMapping("/deposit")
    public Mono<Void> deposit(@RequestBody DepositRequest depositRequest){
        return this.bankingOperation.deposit(depositRequest.getProductId(), depositRequest.getAmount().doubleValue());
    }

    @PostMapping("/withdrawal")
    public Mono<Void> withdrawal(@RequestBody WithdrawalRequest request){
        return this.bankingOperation.withdrawMoney(request.getProductId(), request.getAmount().doubleValue());
    }

    @GetMapping("/check-balance/{productId}")
    public Mono<BalanceResponse> checkBalance(@PathVariable String productId){
        return this.bankingOperation.checkBalance(productId);
    }


}

