package com.vijay.RobustScalableApp.service.Impl;

import com.vijay.RobustScalableApp.entity.User;
import com.vijay.RobustScalableApp.repository.UserRepository;
import com.vijay.RobustScalableApp.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


@Log4j2
@EnableAsync
@Service
public class UserServiceImpl extends GenericAsyncService<User, Long> implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository, Executor executor) {
        super(userRepository, User.class,executor);
        this.userRepository = userRepository;
    }



}
