package com.vijay.RobustScalableApp.service;

import com.vijay.RobustScalableApp.entity.Comment;
import com.vijay.RobustScalableApp.model.CommentResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public interface CommentService extends AsyncCrudOperations<Comment,Long> {
    CompletableFuture<List<CommentResponse>> getCommentsByPostId(Long postId);
}
