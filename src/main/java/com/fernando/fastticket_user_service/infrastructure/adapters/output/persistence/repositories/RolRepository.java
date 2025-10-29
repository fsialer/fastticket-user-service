package com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.repositories;

import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.models.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<RolEntity,Integer> {
    RolEntity findByCode(String code);
}
