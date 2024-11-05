package com.vijay.RobustScalableApp.controller;

import com.vijay.RobustScalableApp.entity.User;
import com.vijay.RobustScalableApp.service.Impl.UserServiceImpl;
import com.vijay.RobustScalableApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<User>> createUser(@RequestBody User user) {
        return userService.create(user)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<User>> getUserById(@PathVariable Long id) {
        return userService.getById(id)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<User>>> getAllUsers() {
        return userService.getAll()
                .thenApply(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<User>> updateUser(@RequestBody User user, @PathVariable Long id) {
        return userService.update(user, id)
                .thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteUser(@PathVariable Long id) {
        return userService.delete(id)
                .thenApply(aVoid -> ResponseEntity.noContent().build());
    }
}

