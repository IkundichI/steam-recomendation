package com.technokratos.repository;

import com.technokratos.entity.GameCommentEntity;
import com.technokratos.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameCommentRepository extends JpaRepository<GameCommentEntity, Long> {

    @Query("SELECT c FROM GameCommentEntity c")
    List<GameCommentEntity> findAll();

    List<GameCommentEntity> findByGameAppid(Integer gameId);

    List<GameCommentEntity> deleteAllByUser(UserEntity userEntity);

}
