package com.example.tofi.common.service;

import com.example.tofi.common.persistance.domain.authservice.LockedUser;
import com.example.tofi.common.persistance.repository.LockedUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoginAttemptService {
    private static final int MAX_NUMBER_OF_ATTEMPT = 3;
    private static final int BLOCK_TIME_MINUTES = 15;

    private final LockedUserRepository repository;

    public Boolean isLocked(LockedUser user) {
        if (user == null || !user.getUserLocked()) return false;
        if (LocalDateTime.now().isBefore(user.getLockTime())) {
            return true;
        } else {
            deleteLockedUser(user.getEmail());
            return false;
        }
    }

    public void deleteLockedUser(String email) {
        repository.deleteByEmail(email);
    }

    public LockedUser getUserLocked(String email){
        return repository.findByEmail(email).orElse(null);
    }

    public LockedUser increaseAttempt(String email) {
        LockedUser user = repository.findByEmail(email).orElseGet(() -> addLockedUser(email));
        if (!user.getUserLocked()) {
            int attempt = user.getAttempt() + 1;
            user.setAttempt(attempt);
            if (attempt >= MAX_NUMBER_OF_ATTEMPT) {
                user.setLockTime(LocalDateTime.now().plusMinutes(BLOCK_TIME_MINUTES));
                user.setUserLocked(true);
            }
            return repository.save(user);
        }else{
            if (LocalDateTime.now().isBefore(user.getLockTime())) {
                return user;
            } else {
                repository.unLockUserById(user.getId());
                return addLockedUser(email);
            }
        }


    }

    private LockedUser addLockedUser(String email) {
        LockedUser newLockedUser = new LockedUser();
        newLockedUser.setUserLocked(false);
        newLockedUser.setAttempt(0);
        newLockedUser.setEmail(email);
        return newLockedUser;
    }

}
