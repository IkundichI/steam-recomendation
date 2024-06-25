package com.technokratos.controller.simple;

import com.technokratos.entity.GameCommentEntity;
import com.technokratos.entity.UserCommentEntity;
import com.technokratos.entity.UserEntity;
import com.technokratos.service.GameCommentService;
import com.technokratos.service.UserCommentService;
import com.technokratos.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/admin")
@Tag(name = "Admin API", description = "Операции управления администратора")
public class AdminController {

    private final UserService userService;
    private final GameCommentService gameCommentService;
    private final UserCommentService userCommentService;

    @Operation(summary = "Получить всех пользователей", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<UserEntity> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("isAdmin", true);
        return "admin-all-users";
    }


    @Operation(summary = "Заблокировать пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно заблокирован"),
            @ApiResponse(responseCode = "400", description = "Неверный ID пользователя"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @PutMapping("/ban-user")
    @SneakyThrows
    public String banUser(@Parameter(description = "ID пользователя для блокировки") @RequestParam("userId") String userId, Model model) {
        String sanitizedUserId = userId.replaceAll("[^\\d]", "");
        Long userIdLong = Long.parseLong(sanitizedUserId);
        userService.banUser(userIdLong);
        return "redirect:/admin/users";
    }


    @Operation(summary = "Разблокировать пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно разблокирован"),
            @ApiResponse(responseCode = "400", description = "Неверный ID пользователя"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @PutMapping("/unban-user")
    @SneakyThrows
    public String unbanUser(@Parameter(description = "ID пользователя для разблокировки") @RequestParam("userId") String userId, Model model) {
        String sanitizedUserId = userId.replaceAll("[^\\d]", "");
        Long userIdLong = Long.parseLong(sanitizedUserId);
        userService.unbanUser(userIdLong);
        return "redirect:/admin/users";
    }


    @Operation(summary = "Получить все комментарии к игре", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список комментариев успешно получен"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping("/all-game-comments")
    public String getAllGameComments(Model model) {
        List<GameCommentEntity> comments = gameCommentService.getAllComments();
        model.addAttribute("comments", comments);
        model.addAttribute("isAdmin", true);

        return "all-game-comments";
    }


    @Operation(summary = "Удалить комментарий к игре", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно удален"),
            @ApiResponse(responseCode = "400", description = "Неверный ID комментария"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @DeleteMapping("/delete-game-comment")
    public String deleteGameComment(@Parameter(description = "ID комментария для удаления") @RequestParam("commentId") Long commentId) {
        gameCommentService.deleteComment(commentId);
        return "redirect:/admin/all-game-comments";
    }


    @Operation(summary = "Получить все комментарии пользователей", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список комментариев успешно получен"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping("/all-user-comments")
    public String getAllUserComments(Model model) {
        List<UserCommentEntity> comments = userCommentService.getAllComments();
        model.addAttribute("comments", comments);
        model.addAttribute("isAdmin", true);

        return "all-user-comments";
    }


    @Operation(summary = "Удалить комментарий у пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно удален"),
            @ApiResponse(responseCode = "400", description = "Неверный ID комментария"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @DeleteMapping("/delete-user-comment")
    public String deleteUserComment(@Parameter(description = "ID комментария для удаления") @RequestParam("commentId") Long commentId) {
        userCommentService.deleteComment(commentId);
        return "redirect:/admin/all-user-comments";
    }


    @Operation(summary = "Удалить пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно удален"),
            @ApiResponse(responseCode = "400", description = "Неверный ID пользователя"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @DeleteMapping("/delete-user")
    @SneakyThrows
    public String deleteUser(@Parameter(description = "ID пользователя для удаления") @RequestParam("userId") String userId, Model model) {
        String sanitizedUserId = userId.replaceAll("[^\\d]", "");
        Long userIdLong = Long.parseLong(sanitizedUserId);
        userService.deleteUser(userIdLong);
        return "redirect:/admin/users";
    }


    @GetMapping("")
    public String getAdminPage(ModelMap model) {
        model.addAttribute("isAdmin", true);

        return "adminka";
    }
}
