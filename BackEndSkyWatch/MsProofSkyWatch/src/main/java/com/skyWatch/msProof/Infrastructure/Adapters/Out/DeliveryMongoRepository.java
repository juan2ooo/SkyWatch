package com.skyWatch.msProof.Infrastructure.Adapters.Out;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryMongoRepository extends MongoRepository<DeliveryEntity, String> {
}
