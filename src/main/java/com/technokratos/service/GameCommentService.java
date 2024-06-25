package com.technokratos.service;

import com.technokratos.entity.ActivityEntity;
import com.technokratos.entity.GameCommentEntity;
import com.technokratos.entity.GameEntity;
import com.technokratos.repository.ActivityEventRepository;
import com.technokratos.repository.GameCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameCommentService {

    private final GameCommentRepository gameCommentRepository;
    private final ActivityEventRepository activityEventRepository;
    private final GameService gameService;

    public GameCommentEntity addComment(GameCommentEntity comment) {
        log.info("Adding a new comment for game with appid: {}", comment.getGame().getAppid());

        GameEntity game = gameService.getInfoAboutGame(String.valueOf(comment.getGame().getAppid()));
        log.debug("Fetched game info: {}", game);

        ActivityEntity activityEntity = createActivityEvent(comment, game);
        activityEventRepository.save(activityEntity);
        log.debug("Saved activity event: {}", activityEntity);

        GameCommentEntity savedComment = gameCommentRepository.save(comment);
        log.info("Successfully added comment with id: {}", savedComment.getId());
        return savedComment;
    }

    public List<GameCommentEntity> getCommentsByGame(Integer gameId) {
        log.info("Fetching comments for game with appid: {}", gameId);
        List<GameCommentEntity> comments = gameCommentRepository.findByGameAppid(gameId);
        log.info("Fetched {} comments for game with appid: {}", comments.size(), gameId);
        return comments;
    }

    public List<GameCommentEntity> getAllComments() {
        log.info("Fetching all comments");
        List<GameCommentEntity> comments = gameCommentRepository.findAll();
        log.info("Fetched {} comments", comments.size());
        return comments;
    }

    @SneakyThrows
    public void deleteComment(Long commentId) {
        log.info("Deleting comment with id: {}", commentId);
        gameCommentRepository.deleteById(commentId);
        log.info("Successfully deleted comment with id: {}", commentId);
    }

    private ActivityEntity createActivityEvent(GameCommentEntity comment, GameEntity game) {
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setUserId(comment.getUser().getSteamId().toString());
        activityEntity.setEventType("COMMENT TO " + game.name);
        activityEntity.setDetails(comment.getContent());
        activityEntity.setTimestamp(LocalDateTime.now());
        return activityEntity;
    }
}
