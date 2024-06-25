package com.technokratos.record;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ на комментарий")
public record CommentResponse(
        @Schema(description = "Содержимое комментария") String content,
        @Schema(description = "Логин пользователя") String login) {
}
