package com.survivorsLabs.paymentService.mongoDB.mapper;

import com.survivorsLabs.paymentService.model.Transaction;
import com.survivorsLabs.paymentService.mongoDB.entity.TransactionMongoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionDBMapper {
    TransactionMongoEntity domainToEntity(Transaction transaction);
    Transaction entityToDomain(TransactionMongoEntity transactionMongoEntity);


}
