package com.technokratos.service;

import com.technokratos.entity.*;
import com.technokratos.record.ShortGameResponse;
import com.technokratos.record.GameTime;
import com.technokratos.repository.ActivityEventRepository;
import com.technokratos.repository.GameRepository;
import org.springframework.cache.annotation.Cacheable;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

    private final SteamApiService steamApiService;
    private final GameRepository gameRepository;
    private final ActivityEventRepository activityEventRepository;

    public Integer countOfGames(String steamId) {
        log.info("Fetching owned games count for steamId: {}", steamId);
        int count = steamApiService.getOwnedGames(steamId).size();
        log.info("User with steamId: {} owns {} games", steamId, count);
        return count;
    }

    public Page<ShortGameResponse> getTopGamesForUser(String steamId, Pageable pageable) {
        log.info("Fetching top games for user with steamId: {}", steamId);

        var ownedGames = steamApiService.getOwnedGames(steamId);
        log.debug("Owned games fetched: {}", ownedGames.size());

        var genreWeights = calculateGenreWeights(ownedGames);
        var categoryWeights = calculateCategoryWeights(ownedGames);

        normalizeWeights(genreWeights);
        normalizeWeights(categoryWeights);

        List<GameEntity> topGames = calculateTopGames(ownedGames, genreWeights, categoryWeights);

        List<ShortGameResponse> topGameResponses = topGames.stream()
                .map(GameEntity::toShortResponse)
                .collect(Collectors.toList());

        return getPage(topGameResponses, pageable);
    }

    public Page<ShortGameResponse> getAllGames(Pageable pageable) {
        log.info("Fetching all games sorted by the count of comments");
        List<ShortGameResponse> games = gameRepository.findAll().stream()
                .sorted(Comparator.comparingInt(GameEntity::getCountOfComments).reversed())
                .map(GameEntity::toShortResponse)
                .toList();
        log.info("Fetched {} games", games.size());
        return getPage(games, pageable);
    }

    public Page<ShortGameResponse> getOwnedGames(String steamId, Pageable pageable) {
        log.info("Fetching owned games for user with steamId: {}", steamId);
        var ownedGameIds = steamApiService.getOwnedGames(steamId).stream()
                .map(GameTime::appid)
                .toList();
        log.debug("Owned game IDs: {}", ownedGameIds);

        List<ShortGameResponse> ownedGames = gameRepository.findAllByAppid(ownedGameIds).stream()
                .sorted(Comparator.comparingInt(GameEntity::getCountOfComments).reversed())
                .map(GameEntity::toShortResponse)
                .toList();
        log.info("Fetched {} owned games", ownedGames.size());
        return getPage(ownedGames, pageable);
    }

    public List<GameEntity> getOwnedGames(String steamId) {
        log.info("Fetching owned games for user with steamId: {}", steamId);
        var ownedGameIds = steamApiService.getOwnedGames(steamId).stream()
                .map(GameTime::appid)
                .toList();
        log.debug("Owned game IDs: {}", ownedGameIds);

        List<GameEntity> ownedGames = gameRepository.findAllByAppid(ownedGameIds).stream()
                .sorted(Comparator.comparingInt(GameEntity::getCountOfComments).reversed())
                .toList();
        log.info("Fetched {} owned games", ownedGames.size());
        return ownedGames;
    }

    @SneakyThrows
    public GameEntity getInfoAboutGame(String appId) {
        log.info("Fetching info about game with appId: {}", appId);
        Optional<GameEntity> gameEntityOpt = gameRepository.findById(Integer.valueOf(appId));
        log.info("Game found: {}", gameEntityOpt.get().getName());
        return gameEntityOpt.get();
    }

    private Map<String, Double> calculateGenreWeights(List<GameTime> ownedGames) {
        Map<String, Double> genreWeights = new HashMap<>();

        List<Integer> appIds = ownedGames.stream()
                .map(GameTime::appid)
                .collect(Collectors.toList());

        List<GameEntity> gameEntities = gameRepository.findAllByAppid(appIds);

        for (GameTime game : ownedGames) {
            var gameEntityOpt = gameEntities.stream()
                    .filter(gameEntity -> gameEntity.getAppid().equals(game.appid()))
                    .findFirst();

            if (gameEntityOpt.isEmpty()) {
                log.debug("Game with appid: {} not found in repository", game.appid());
                continue;
            }

            var gameEntity = gameEntityOpt.get();
            var time = game.time();
            Set<GenreEntity> genresList = gameEntity.getGenres();

            genresList.forEach(genre -> genreWeights.put(genre.getDescription(),
                    genreWeights.getOrDefault(genre.getDescription(), 0.0) + time));
        }

        return genreWeights;
    }

    private Map<String, Double> calculateCategoryWeights(List<GameTime> ownedGames) {
        Map<String, Double> categoryWeights = new HashMap<>();

        List<Integer> appIds = ownedGames.stream()
                .map(GameTime::appid)
                .collect(Collectors.toList());

        List<GameEntity> gameEntities = gameRepository.findAllByAppid(appIds);

        for (GameTime game : ownedGames) {
            var gameEntityOpt = gameEntities.stream()
                    .filter(gameEntity -> gameEntity.getAppid().equals(game.appid()))
                    .findFirst();

            if (gameEntityOpt.isEmpty()) {
                log.debug("Game with appid: {} not found in repository", game.appid());
                continue;
            }

            var gameEntity = gameEntityOpt.get();
            var time = game.time();
            Set<CategoryEntity> categoryList = gameEntity.getCategories();

            categoryList.forEach(category -> categoryWeights.put(category.getDescription(),
                    categoryWeights.getOrDefault(category.getDescription(), 0.0) + time));
        }

        return categoryWeights;
    }

    private void normalizeWeights(Map<String, Double> weights) {
        double maxWeight = weights.values().stream().max(Double::compare).orElse(0.0);
        weights.replaceAll((k, v) -> v / maxWeight);
    }

    private List<GameEntity> calculateTopGames(List<GameTime> ownedGames, Map<String, Double> genreWeights, Map<String, Double> categoryWeights) {
        List<GameEntity> gameEntities = gameRepository.findAll();
        log.debug("Total games in repository: {}", gameEntities.size());

        ownedGames.stream()
                .map(GameTime::appid)
                .forEach(appid -> gameEntities.removeIf(gameEntity -> gameEntity.getAppid().equals(appid)));

        log.debug("Filtered games count after removing owned games: {}", gameEntities.size());

        List<GameEntity> topGames = new ArrayList<>();
        gameEntities.forEach(game -> {
            double genreWeight = game.getGenres().stream()
                    .mapToDouble(genre -> genreWeights.getOrDefault(genre.getDescription(), 0.0))
                    .sum();
            double categoryWeight = game.getCategories().stream()
                    .mapToDouble(category -> categoryWeights.getOrDefault(category.getDescription(), 0.0))
                    .sum();
            double score = 0.4 * genreWeight + 0.3 * categoryWeight + 0.07 * game.getMetacritic_score() + 0.05 * Math.log(game.getCountOfComments() + 1);
            game.setScore(score);
            topGames.add(game);
        });

        topGames.sort(Comparator.comparingDouble(GameEntity::getScore).reversed());
        log.info("Top games sorted by score");

        return topGames;
    }

    public void saveRecommendationActivityEvent(String steamId, List<ShortGameResponse> topGames) {
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setUserId(steamId != null ? steamId : "unknown");
        activityEntity.setEventType("RECOMMENDATION");
        activityEntity.setDetails(topGames.stream()
                .map(ShortGameResponse::name)
                .limit(3)
                .collect(Collectors.joining(", ")));
        activityEntity.setTimestamp(LocalDateTime.now());
        activityEventRepository.save(activityEntity);
        log.info("Saved recommendation activity event for user: {}", steamId);
    }

    private <T> Page<T> getPage(List<T> list, Pageable pageable) {
        int start = Math.min((int) pageable.getOffset(), list.size());
        int end = Math.min((start + pageable.getPageSize()), list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }
}
