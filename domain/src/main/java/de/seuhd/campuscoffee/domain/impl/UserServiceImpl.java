package de.seuhd.campuscoffee.domain.impl;

import de.seuhd.campuscoffee.domain.model.User;
import de.seuhd.campuscoffee.domain.ports.OsmDataService;
import de.seuhd.campuscoffee.domain.ports.PosDataService;
import de.seuhd.campuscoffee.domain.ports.UserDataService;
import de.seuhd.campuscoffee.domain.ports.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDataService userDataService;

    @Override
    public void clear() {
        userDataService.clear();
    }

    @Override
    public @NonNull List<User> getAll() {
        return userDataService.getAll();
    }

    @Override
    public @NonNull User getById(@NonNull Long id) {
        return userDataService.getById(id);
    }

    @Override
    public @NonNull User getByLoginName(@NonNull String loginName) {
        return userDataService.getByLoginName(loginName);
    }

    @Override
    public @NonNull User upsert(@NonNull User user) {
        if (user.id() == null) {
            // create a new User
            log.info("Creating new User: {}", user.loginName());
        } else {
            // update existing User
            log.info("Updating User with ID {}: {}", user.id(), user.loginName());
        }
        return userDataService.upsert(user);
    }

    @Override
    public void delete(@NonNull Long id) {
        log.info("Trying to delete User with ID: {}", id);
        userDataService.delete(id);
        log.info("Deleting User with ID: {}", id);
    }
}
