package com.technokratos.service;

import com.technokratos.entity.UserEntity;
import com.technokratos.enums.Role;
import com.technokratos.enums.State;
import com.technokratos.record.SignUpForm;
import com.technokratos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignUpServiceImpl implements SignUpService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SteamApiService steamApiService;

    @Override
    @SneakyThrows
    public boolean signUp(SignUpForm form) {
        var steamId = steamApiService.getSteamId(form.steamId());

        UserEntity user = UserEntity.builder()
                .login(form.login())
                .steamId(steamId)
                .passwordHash(passwordEncoder.encode(form.password()))
                .role(Role.USER)
                .state(State.ACTIVE)
                .build();

        userRepository.save(user);

        return true;
    }

}