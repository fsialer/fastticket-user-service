package com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.repositories;

import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.models.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserEntity u WHERE u.email = :email")
    Boolean existsByEmail(@Param("email") String email);

    @EntityGraph(attributePaths = {"roles","person"})
    UserEntity findByEmail(String email);
}
