package com.technokratos.repository;

import com.technokratos.entity.UserCommentEntity;
import com.technokratos.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserCommentRepository extends JpaRepository<UserCommentEntity, Long> {

    UserCommentEntity save (UserCommentEntity userCommentEntity);

    @Query("SELECT c FROM UserCommentEntity c WHERE c.profile.steamId = :userId")
    List<UserCommentEntity> findAllByProfileId(Long userId);

    List<UserCommentEntity> deleteAllByUser(UserEntity userEntity);
    List<UserCommentEntity> deleteAllByProfile(UserEntity userEntity);
}
