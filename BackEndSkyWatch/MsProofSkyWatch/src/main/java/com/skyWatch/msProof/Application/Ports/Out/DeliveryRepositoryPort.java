package com.skyWatch.msProof.Application.Ports.Out;

import com.skyWatch.msProof.Domain.Delivery;

public interface DeliveryRepositoryPort {

    boolean save(Delivery delivery);
}