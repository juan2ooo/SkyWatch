package com.skyWatch.msProof.Infrastructure.Adapters.Out;

import com.skyWatch.msProof.Application.Ports.Out.DeliveryRepositoryPort;
import com.skyWatch.msProof.Domain.Delivery;


public class DeliveryRepositoryAdapter implements DeliveryRepositoryPort {

    private final DeliveryMongoRepository repository;

    public DeliveryRepositoryAdapter(DeliveryMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean save(Delivery delivery) {

        try {

            DeliveryEntity entity = mapToEntity(delivery);

            repository.save(entity);

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    private DeliveryEntity mapToEntity(Delivery delivery) {

        return new DeliveryEntity(
                delivery.getRouteId(),
                delivery.getDroneId(),
                delivery.getEmail(),
                delivery.getImage()
        );
    }
}
