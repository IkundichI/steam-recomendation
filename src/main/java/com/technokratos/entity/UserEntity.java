package com.technokratos.entity;

import com.technokratos.enums.Role;
import com.technokratos.enums.State;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(unique = true)
    private Long steamId;

    @Column(unique = true)
    private String login;

    @Column(length = 300)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private State state;

    public Boolean isActive(){
        return this.state == State.ACTIVE;
    }

}