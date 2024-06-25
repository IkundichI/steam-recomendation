package com.technokratos.controller.rest;

import com.technokratos.entity.GameCommentEntity;
import com.technokratos.entity.GameEntity;
import com.technokratos.entity.UserEntity;
import com.technokratos.record.CommentResponse;
import com.technokratos.security.UserDetailsImpl;
import com.technokratos.service.GameCommentService;
import com.technokratos.service.UserCommentService;
import com.technokratos.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Tag(name = "Comment API", description = "API для управления комментариями")
public class CommentController {

    private final GameCommentService gameCommentService;
    private final UserCommentService userCommentService;
    private final UserService userService;

    @Operation(summary = "Добавить новый комментарий пользователю")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно добавлен",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Неверный profileId или содержание", content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content)
    })
    @PostMapping("/addUserComment")
    @SneakyThrows
    public ResponseEntity<?> addUserComment(@RequestParam("content") String content,
                                            @RequestParam("profileId") String profileId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getUser().getSteamId();
        UserEntity user = userService.getUserById(userId);

        if (profileId == null || profileId.isEmpty() || content == null || content.isEmpty()) {
            return ResponseEntity.badRequest().body("Неверный profileId или содержание");
        }
        try {
            userService.getUserById(Long.valueOf(profileId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User not found");

        }

        userCommentService.addComment(userId, Long.valueOf(profileId), content);

        return ResponseEntity.ok(new CommentResponse(content, user.getLogin()));
    }

    @Operation(summary = "Добавить новый комментарий к игре")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно добавлен",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Неверный gameId или содержание", content = @Content),
            @ApiResponse(responseCode = "403", description = "Пользователь заблокирован. Комментарии недоступны.", content = @Content)
    })
    @PostMapping("/addGameComment")
    @SneakyThrows
    public ResponseEntity<?> addGameComment(@RequestParam("content") String content,
                                            @RequestParam("gameId") String gameId) {

        int gameIdInt = Integer.parseInt(gameId.replaceAll("[^\\d]", ""));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getUser().getSteamId();
        UserEntity user = userService.getUserById(userId);

        GameCommentEntity comment = GameCommentEntity.builder()
                .content(content)
                .game(GameEntity.builder()
                        .appid(gameIdInt)
                        .build())
                .user(user)
                .build();

        gameCommentService.addComment(comment);

        return ResponseEntity.ok(new CommentResponse(content, user.getLogin()));
    }
}
