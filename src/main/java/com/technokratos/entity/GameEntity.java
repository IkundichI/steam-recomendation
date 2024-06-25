package com.technokratos.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.technokratos.record.ShortGameResponse;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "game")
public class GameEntity {

    @JsonProperty("steam_appid")
    @Id
    public Integer appid;

    @Transient
    public String type;

    @Transient
    public Double score;

    public String name;

    public boolean is_free;

    @Transient
    public String detailed_description;

    @Column(length = 10000)
    public String short_description;

    @Column(length = 10000)
    public String supported_languages;

    @Column(length = 10000)
    public String header_image;

    @Column(length = 10000)
    public String background_raw;

    public String website;

    public Integer countOfComments;

    public Integer metacritic_score;
    public String metacritic_url;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "game_categorie",
            joinColumns = @JoinColumn(name = "appid"),
            inverseJoinColumns = @JoinColumn(name = "category_id")

    )
    public Set<CategoryEntity> categories;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "game_genre",
            joinColumns = @JoinColumn(name = "appid"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    public Set<GenreEntity> genres;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GameCommentEntity> comments;

    public ShortGameResponse toShortResponse() {
        return new ShortGameResponse(appid.toString(), name, header_image, short_description);
    }
}
