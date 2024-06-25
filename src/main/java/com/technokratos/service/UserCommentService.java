package com.technokratos.service;

import com.technokratos.entity.ActivityEntity;
import com.technokratos.entity.UserCommentEntity;
import com.technokratos.entity.UserEntity;
import com.technokratos.repository.ActivityEventRepository;
import com.technokratos.repository.UserCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCommentService {

    private final UserCommentRepository userCommentRepository;
    private final UserService userService;
    private final ActivityEventRepository activityEventRepository;

    public UserCommentEntity addComment(Long authorId, Long toUserId, String content) {
        log.info("Adding a new comment from user with id: {} to user with id: {}", authorId, toUserId);

        UserEntity user = userService.getUserById(authorId);
        UserEntity profile = userService.getUserById(toUserId);

        log.debug("Fetched user info: {}", user);
        log.debug("Fetched profile info: {}", profile);

        UserCommentEntity comment = UserCommentEntity
                .builder()
                .user(user)
                .profile(profile)
                .content(content)
                .build();

        ActivityEntity activityEntity = createActivityEvent(comment);
        activityEventRepository.save(activityEntity);
        log.debug("Saved activity event: {}", activityEntity);

        UserCommentEntity savedComment = userCommentRepository.save(comment);
        log.info("Successfully added comment with id: {}", savedComment.getId());
        return savedComment;
    }

    public List<UserCommentEntity> getCommentsByProfileId(Long userId) {
        log.info("Fetching comments for user with id: {}", userId);
        List<UserCommentEntity> comments = userCommentRepository.findAllByProfileId(userId);
        log.info("Fetched {} comments for user with id: {}", comments.size(), userId);
        return comments;
    }

    public List<UserCommentEntity> getAllComments() {
        log.info("Fetching all comments");
        List<UserCommentEntity> comments = userCommentRepository.findAll();
        log.info("Fetched {} comments", comments.size());
        return comments;
    }

    @SneakyThrows
    public void deleteComment(Long commentId) {
        log.info("Deleting comment with id: {}", commentId);
        userCommentRepository.deleteById(commentId);
        log.info("Successfully deleted comment with id: {}", commentId);
    }

    private ActivityEntity createActivityEvent(UserCommentEntity comment) {
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setUserId(comment.getUser().getSteamId().toString());
        activityEntity.setEventType("COMMENT TO");
        activityEntity.setDetails(comment.getContent());
        activityEntity.setTimestamp(LocalDateTime.now());
        return activityEntity;
    }
}
