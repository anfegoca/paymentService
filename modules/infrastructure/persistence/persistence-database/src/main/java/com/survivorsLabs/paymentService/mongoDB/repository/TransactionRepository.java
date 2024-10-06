package com.survivorsLabs.paymentService.dynamoDB.repository;

import com.survivorsLabs.paymentService.dynamoDB.entity.TransactionDynamoEntity;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//@Repository
@Data
@Component
@RequiredArgsConstructor
public class TransactionRepository {
    private List<TransactionDynamoEntity> transactions = new ArrayList<>();

    public TransactionDynamoEntity save(TransactionDynamoEntity transactionDynamoEntity){
        transactions.add(transactionDynamoEntity);
    }


}
