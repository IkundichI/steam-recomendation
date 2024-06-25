package com.technokratos.controller.simple;

import com.technokratos.entity.GameCommentEntity;
import com.technokratos.record.ShortGameResponse;
import com.technokratos.security.UserDetailsImpl;
import com.technokratos.service.GameCommentService;
import com.technokratos.service.GameService;
import com.technokratos.service.SteamApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;

@Controller
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Game API", description = "API для управления и просмотра игр")
public class GamesController {

    private final SteamApiService steamApiService;
    private final GameCommentService gameCommentService;
    private final GameService gameService;

    @Operation(summary = "Получить топ игры", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Топ игры успешно получены"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping("/top-games")
    @PreAuthorize("isAuthenticated()")
    public String getTopGames(Model model, @RequestParam(required = false) String steamId,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (steamId == null || steamId.isEmpty()) {
            if (authentication != null && authentication.isAuthenticated()) {
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                steamId = String.valueOf(userDetails.getUser().getSteamId());
            } else {
                return "redirect:/signIn";
            }
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<ShortGameResponse> topGames = gameService.getTopGamesForUser(steamId, pageable);

        gameService.saveRecommendationActivityEvent(steamId, topGames.getContent());

        model.addAttribute("allGames", topGames.getContent());
        model.addAttribute("type", "Топ игры");
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", topGames.getTotalPages());

        return "games-list";
    }

    @Operation(summary = "Получить игры пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Игры пользователя успешно получены"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping("/my-games")
    @PreAuthorize("isAuthenticated()")
    public String getUserGames(Model model, @RequestParam(required = false) String steamId,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (steamId == null || steamId.isEmpty()) {
            if (authentication != null && authentication.isAuthenticated()) {
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                steamId = String.valueOf(userDetails.getUser().getSteamId());
            } else {
                return "redirect:/signIn";
            }
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<ShortGameResponse> userGames = gameService.getOwnedGames(steamId, pageable);

        model.addAttribute("allGames", userGames.getContent());
        model.addAttribute("type", "Игры пользователя");
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userGames.getTotalPages());

        return "games-list";
    }

    @Operation(summary = "Получить все игры")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Все игры успешно получены")
    })
    @GetMapping("/all-games")
    public String getAllGames(Model model, @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ShortGameResponse> allGames = gameService.getAllGames(pageable);

        model.addAttribute("type", "Все игры");
        model.addAttribute("allGames", allGames.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", allGames.getTotalPages());

        return "games-list";
    }

    @Operation(summary = "Получить детали игры")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Детали игры успешно получены"),
            @ApiResponse(responseCode = "404", description = "Игра не найдена")
    })
    @GetMapping("/game/{appid}")
    public String getGame(Model model, @Parameter(description = "App ID игры") @PathVariable Integer appid) {

        if (appid == null) {
            return "redirect:/all-games";
        }
        var gameDetails = steamApiService.getGameInformation(appid);
        var comments = gameCommentService.getCommentsByGame(appid)
                .stream()
                .sorted(Comparator.comparing(GameCommentEntity::getId).reversed())
                .toList();

        model.addAttribute("game", gameDetails);
        model.addAttribute("comments", comments);

        return "game-info";
    }
}
