package com.vijay.RobustScalableApp.controller;

import com.vijay.RobustScalableApp.entity.Post;
import com.vijay.RobustScalableApp.model.PostRequest;
import com.vijay.RobustScalableApp.model.PostResponse;
import com.vijay.RobustScalableApp.service.Impl.PostServiceImpl;
import com.vijay.RobustScalableApp.service.PostService;
import com.vijay.RobustScalableApp.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostServiceImpl postService) {
        this.postService = postService;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<PostResponse>> createPost(@RequestBody PostRequest postRequest) {
        Post entity = Mapper.toEntity(postRequest, Post.class);
        return postService.create(entity)
                .thenApply(e -> ResponseEntity.ok(Mapper.toDto(e, PostResponse.class)));
    }


    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<PostResponse>> getPostById(@PathVariable long id) {
        return postService.getById(id)
                .thenApply(e -> ResponseEntity.ok(Mapper.toDto(e, PostResponse.class)));
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<PostResponse>> updatePost(@RequestBody PostRequest postRequest, @PathVariable long id) {
        Post entity = Mapper.toEntity(postRequest, Post.class);
        entity.setId(id);
        return postService.update(entity, id)
                .thenApply(e -> ResponseEntity.ok(Mapper.toDto(e,PostResponse.class)));
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deletePost(@PathVariable long id) {
        return postService.delete(id)
                .thenApply(aVoid -> ResponseEntity.noContent().build());
    }

    @GetMapping("/category/{categoryId}")
    public CompletableFuture<ResponseEntity<List<PostResponse>>> getPostsByCategory(@PathVariable Long categoryId) {
        return postService.getPostsByCategory(categoryId).thenApply(ResponseEntity::ok);
    }
    @GetMapping
    public CompletableFuture<ResponseEntity<List<PostResponse>>> getAllPosts() {
        return postService.getAll()
                .thenApply(list -> list.stream()
                        .map(e -> Mapper.toDto(e,PostResponse.class))
                        .collect(Collectors.toList()))
                .thenApply(ResponseEntity::ok);
    }
}
