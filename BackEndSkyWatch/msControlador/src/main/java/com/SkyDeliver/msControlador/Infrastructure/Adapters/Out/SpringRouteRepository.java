package com.SkyDeliver.msControlador.Infrastructure.Adapters.Out;

import com.SkyDeliver.msControlador.Domain.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringRouteRepository extends JpaRepository<RouteEntity,Integer> {
}
