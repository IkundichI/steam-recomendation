package com.technokratos.repository;

import com.technokratos.entity.UserEntity;
import com.technokratos.enums.Role;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    static Specification<UserEntity> hasRole(Role role) {
        return (user, cq, cb) -> cb.equal(user.get("role"), role);
    }

    Optional<UserEntity> findByLogin(String login);

}