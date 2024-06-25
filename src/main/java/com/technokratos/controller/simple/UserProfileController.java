package com.technokratos.controller.simple;

import com.technokratos.entity.GameEntity;
import com.technokratos.entity.UserCommentEntity;
import com.technokratos.record.ProfileInfo;
import com.technokratos.security.UserDetailsImpl;
import com.technokratos.service.GameService;
import com.technokratos.service.SteamApiService;
import com.technokratos.service.UserCommentService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
@Slf4j
@RequiredArgsConstructor
@Tag(name = "MyCabinet API", description = "API для управления кабинетом пользователя")
public class UserProfileController {

    private final SteamApiService steamApiService;
    private final GameService gameService;
    private final UserCommentService userCommentService;

    @Operation(summary = "Получить профиль пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Профиль пользователя успешно получен"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping("/user/profile")
    @PreAuthorize("isAuthenticated()")
    @SneakyThrows
    public String getHello(Model model, @Parameter(description = "Steam ID пользователя", required = false) @RequestParam(required = false) String steamId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (steamId == null || steamId.isEmpty()) {
            if (authentication != null && authentication.isAuthenticated()) {
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                steamId = String.valueOf(userDetails.getUser().getSteamId());
                return "redirect:/user/profile?steamId=" + steamId;
            } else {
                return "redirect:/signIn";
            }
        }

        String finalSteamId = steamId;
        CompletableFuture<ProfileInfo> profileInfoFuture = CompletableFuture.supplyAsync(() -> steamApiService.getPlayerInformation(finalSteamId));
        CompletableFuture<List<String>> friendsFuture = CompletableFuture.supplyAsync(() -> steamApiService.getAllFriends(finalSteamId));
        CompletableFuture<List<GameEntity>> gamesFuture = CompletableFuture.supplyAsync(() -> gameService.getOwnedGames(finalSteamId));
        CompletableFuture<Integer> gamesCountFuture = CompletableFuture.supplyAsync(() -> gameService.countOfGames(finalSteamId));

        ProfileInfo profileInfo = profileInfoFuture.get();
        List<String> friends = friendsFuture.get();
        List<UserCommentEntity> comments = userCommentService.getCommentsByProfileId(Long.valueOf(steamId))
                .stream()
                .sorted(Comparator.comparing(UserCommentEntity::getId).reversed())
                .toList();
        List<GameEntity> games = gamesFuture.get();
        int gamesCount = gamesCountFuture.get();

        var friendsList = new ArrayList<ProfileInfo>();
        List<CompletableFuture<ProfileInfo>> friendsInfoFutures = new ArrayList<>();
        friends.stream().limit(5).forEach(s -> friendsInfoFutures.add(CompletableFuture.supplyAsync(() -> steamApiService.getPlayerInformation(s))));
        CompletableFuture.allOf(friendsInfoFutures.toArray(new CompletableFuture[0])).join();
        friendsInfoFutures.forEach(future -> {
            try {
                friendsList.add(future.get());
            } catch (Exception e) {
                log.error("Error fetching friend information", e);
            }
        });

        model.addAttribute("name", profileInfo.name());
        model.addAttribute("avatar", profileInfo.avatar());
        model.addAttribute("steamId", profileInfo.steamId());
        model.addAttribute("friendsCount", friends.size());
        model.addAttribute("friends", friendsList);
        model.addAttribute("games", games.stream().limit(5).toList());
        model.addAttribute("gamesCount", gamesCount);
        model.addAttribute("comments", comments);

        model.addAttribute("isAdmin", authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ADMIN")));

        return "profile";
    }

    @Operation(summary = "Получить список друзей пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список друзей успешно получен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/user/friends/{steamId}")
    @SneakyThrows
    public String getFriends(@Parameter(description = "Steam ID пользователя") @PathVariable String steamId, Model model) {
        List<ProfileInfo> friendsList = new ArrayList<>();
        List<String> friends = steamApiService.getAllFriends(steamId);
        List<CompletableFuture<ProfileInfo>> friendsInfoFutures = new ArrayList<>();
        friends.forEach(s -> friendsInfoFutures.add(CompletableFuture.supplyAsync(() -> steamApiService.getPlayerInformation(s))));
        CompletableFuture.allOf(friendsInfoFutures.toArray(new CompletableFuture[0])).join();
        friendsInfoFutures.forEach(future -> {
            try {
                friendsList.add(future.get());
            } catch (Exception e) {
                log.error("Error fetching friend information", e);
            }
        });

        model.addAttribute("friends", friendsList);
        return "all-friends";
    }
}
