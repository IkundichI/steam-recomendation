package com.technokratos.repository;

import com.technokratos.entity.GameEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Integer> {

    @EntityGraph(attributePaths = {"categories", "genres"})
    Optional<GameEntity> findById(Integer appid);


    @EntityGraph(attributePaths = {"categories", "genres"})
    List<GameEntity> findAll();


    @Query("SELECT g FROM GameEntity g WHERE g.appid IN :appids")
    @EntityGraph(attributePaths = {"categories", "genres"})
    List<GameEntity> findAllByAppid(List<Integer> appids);

}
