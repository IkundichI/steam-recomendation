package com.technokratos.service;

import com.technokratos.entity.UserEntity;
import com.technokratos.enums.Role;
import com.technokratos.enums.State;
import com.technokratos.repository.GameCommentRepository;
import com.technokratos.repository.UserCommentRepository;
import com.technokratos.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserCommentRepository userCommentRepository;
    private final GameCommentRepository gameCommentRepository;

    public List<UserEntity> getAllUsers() {
        log.info("Fetching all users");
        List<UserEntity> users = userRepository.findAll();
        log.info("Fetched {} users", users.size());
        return users;
    }

    public UserEntity getUserById(Long userId) {
        log.info("Fetching user by id: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", userId);
                    return new IllegalArgumentException("User not found");
                });
    }

    public void banUser(Long userId) {
        log.info("Banning user with id: {}", userId);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", userId);
                    return new IllegalArgumentException("User not found");
                });

        if (!user.getRole().equals(Role.ADMIN)) {
            user.setState(State.BANNED);
            userRepository.save(user);
            log.info("User with id: {} has been banned", userId);
        } else {
            log.warn("Attempted to ban an admin with id: {}", userId);
        }
    }

    public void unbanUser(Long userId) {
        log.info("Unbanning user with id: {}", userId);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", userId);
                    return new IllegalArgumentException("User not found");
                });

        user.setState(State.ACTIVE);
        userRepository.save(user);
        log.info("User with id: {} has been unbanned", userId);
    }

    @Transactional
    public void deleteUser(Long userId) {
        log.info("Deleting user with id: {}", userId);
        UserEntity user = userRepository.findById(userId).get();

        if (!user.getRole().equals(Role.ADMIN)) {
            log.info("Deleting game comments for user id: {}", userId);
            gameCommentRepository.deleteAllByUser(user);

            log.info("Deleting user comments for user id: {}", userId);
            userCommentRepository.deleteAllByUser(user);
            userCommentRepository.deleteAllByProfile(user);

            log.info("Deleting user entity for user id: {}", userId);
            userRepository.delete(user);

            log.info("User with id: {} has been deleted", userId);
        } else {
            log.warn("Attempted to delete an admin with id: {}", userId);
        }
    }

}
