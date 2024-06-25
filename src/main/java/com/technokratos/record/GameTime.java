package com.technokratos.record;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GameTime (
        Integer appid,
        @JsonProperty("playtime_forever") Integer time) {
}