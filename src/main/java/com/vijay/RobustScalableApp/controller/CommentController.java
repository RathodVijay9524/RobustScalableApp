package com.vijay.RobustScalableApp.controller;



import com.vijay.RobustScalableApp.entity.Comment;
import com.vijay.RobustScalableApp.model.CommentRequest;
import com.vijay.RobustScalableApp.model.CommentResponse;
import com.vijay.RobustScalableApp.service.CommentService;
import com.vijay.RobustScalableApp.service.Impl.CommentServiceImpl;
import com.vijay.RobustScalableApp.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private CommentService commentService;
    @Autowired
    public CommentController(CommentServiceImpl commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<CommentResponse>> createComment(@RequestBody CommentRequest commentRequest) {
        Comment entity= Mapper.toEntity(commentRequest, Comment.class);
        return commentService.create(entity)
                .thenApply(savedEntity -> ResponseEntity.ok(Mapper.toDto(savedEntity,CommentResponse.class)));
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<CommentResponse>> getCommentById(@PathVariable Long id) {
        return commentService.getById(id)
                .thenApply(e -> ResponseEntity.ok(Mapper.toDto(e,CommentResponse.class)));
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<CommentResponse>>> getAllComments() {
        return commentService.getAll()
                .thenApply(list -> list.stream()
                        .map(e -> Mapper.toDto(e,CommentResponse.class))
                        .collect(Collectors.toList()))
                .thenApply(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<CommentResponse>> updateComment(@PathVariable Long id, @RequestBody CommentRequest commentRequest) {
        Comment entity = Mapper.toEntity(commentRequest, Comment.class);
        entity.setId(id);
        return commentService.update(entity, id)
                .thenApply(e -> ResponseEntity.ok(Mapper.toDto(e,CommentResponse.class)));
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteComment(@PathVariable Long id) {
        return commentService.delete(id)
                .thenApply(aVoid -> ResponseEntity.noContent().build());
    }

    @GetMapping("/post/{postId}")
    public CompletableFuture<ResponseEntity<List<CommentResponse>>> getCommentsByPostId(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId)
                .thenApply(ResponseEntity::ok);
    }
}

