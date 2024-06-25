package com.technokratos.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.technokratos.entity.GameEntity;
import com.technokratos.record.GameTime;
import com.technokratos.record.ProfileInfo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class SteamApiService {

    private static final String KEY = "7F591B46D0012FDE4B93CA0AB7CEA93B";

    private static final String API_URL_FOR_FRIEND_LIST =
            String.format("http://api.steampowered.com/ISteamUser/GetFriendList/v0001/?key=%s&steamid=", KEY);

    private static final String API_URL_FOR_PLAYER_GAMES =
            String.format("https://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=%s&steamid=", KEY);

    private static final String API_URL_FOR_GAME_ALL_INFO = "https://store.steampowered.com/api/appdetails?appids=";

    private static final String API_URL_FOR_PLAYER_SUMMARIES =
            String.format("http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=%s&steamids=", KEY);

    private static final String API_URL_FOR_STEAMID = "https://steamid.xyz/";

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public List<GameTime> getOwnedGames(String steamId) {
        log.info("Fetching owned games for steamId: {}", steamId);
        List<GameTime> games = new ArrayList<>();

        JsonNode root = objectMapper.readTree(new URL(API_URL_FOR_PLAYER_GAMES + steamId));
        var response = root.path("response").path("games");
        Iterator<JsonNode> iterator = response.elements();
        while (iterator.hasNext()) {
            JsonNode gameNode = iterator.next();
            games.add(objectMapper.treeToValue(gameNode, GameTime.class));
        }

        games.sort(Comparator.comparing(GameTime::time).reversed());
        log.info("Fetched {} owned games for steamId: {}", games.size(), steamId);
        return games;
    }

    @SneakyThrows
    public GameEntity getGameInformation(Integer appId) {
        log.info("Fetching game information for appId: {}", appId);
        int retryCount = 10;
        int currentAttempt = 0;

        while (currentAttempt < retryCount) {
            JsonNode root = objectMapper.readTree(new URL(API_URL_FOR_GAME_ALL_INFO + appId));
            JsonNode innerNode = root.elements().next();
            boolean success = innerNode.path("success").asBoolean();
            if (success) {
                JsonNode dataNode = innerNode.path("data");
                GameEntity response = objectMapper.treeToValue(dataNode, GameEntity.class);
                response.setCountOfComments(dataNode.path("recommendations").path("total").asInt());
                response.setMetacritic_score(dataNode.path("metacritic").path("score").asInt());
                response.setMetacritic_url(dataNode.path("metacritic").path("url").asText());
                log.info("Successfully fetched game information for appId: {}", appId);
                return response;
            } else {
                log.warn("Failed to fetch game information for appId: {}", appId);
                return null;
            }

        }
        return null;
    }

    @SneakyThrows
    public ProfileInfo getPlayerInformation(String steamId) {
        log.info("Fetching player information for steamId: {}", steamId);

        JsonNode response = objectMapper.readTree(new URL(API_URL_FOR_PLAYER_SUMMARIES + steamId))
                .path("response").path("players");
        JsonNode playerNode = response.elements().next();
        String avatar = playerNode.path("avatarfull").asText();
        String name = playerNode.path("personaname").asText();
        String steamID = playerNode.path("steamid").asText();
        log.info("Successfully fetched player information for steamId: {}", steamId);
        return new ProfileInfo(steamID, name, avatar);
    }

    @SneakyThrows
    public Long getSteamId(String login) {
        log.info("Fetching steamId for login: {}", login);
        WebClient webClient = WebClient.create();

        String html = webClient.get()
                .uri(API_URL_FOR_STEAMID + login)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        Pattern pattern = Pattern.compile("(\\d{17})");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            Long steamId = Long.parseLong(matcher.group(1));
            log.info("Successfully fetched steamId: {} for login: {}", steamId, login);
            return steamId;
        }
        log.error("SteamId not found for login: {}", login);
        throw new RuntimeException("SteamId not found");
    }

    @SneakyThrows
    public List<String> getAllFriends(String steamId) {
        log.info("Fetching all friends for steamId: {}", steamId);
        List<String> steamIds = new ArrayList<>();

        JsonNode rootNode = objectMapper.readTree(new URL(API_URL_FOR_FRIEND_LIST + steamId));
        JsonNode friendsNode = rootNode.path("friendslist").path("friends");

        Iterator<JsonNode> iterator = friendsNode.elements();
        while (iterator.hasNext()) {
            JsonNode friendNode = iterator.next();
            String friendSteamId = friendNode.path("steamid").asText();
            steamIds.add(friendSteamId);
        }

        log.info("Successfully fetched {} friends for steamId: {}", steamIds.size(), steamId);
        return steamIds;
    }
}
