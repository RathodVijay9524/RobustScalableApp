package com.vijay.RobustScalableApp.service.Impl;

import com.vijay.RobustScalableApp.entity.Comment;
import com.vijay.RobustScalableApp.entity.Post;
import com.vijay.RobustScalableApp.exception.ResourceNotFoundException;
import com.vijay.RobustScalableApp.model.CommentResponse;
import com.vijay.RobustScalableApp.repository.CommentRepository;
import com.vijay.RobustScalableApp.repository.PostRepository;
import com.vijay.RobustScalableApp.service.CommentService;
import com.vijay.RobustScalableApp.util.Mapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
@Log4j2
public class CommentServiceImpl extends GenericAsyncService<Comment, Long> implements CommentService {


    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    @Autowired
    protected CommentServiceImpl(CommentRepository commentRepository, Executor executor, PostRepository postRepository, CommentRepository commentRepository1) {
        super(commentRepository, Comment.class, executor);
        this.postRepository = postRepository;
        this.commentRepository = commentRepository1;
    }

    @Override
    public CompletableFuture<List<CommentResponse>> getCommentsByPostId(Long postId) {
        return CompletableFuture.supplyAsync(()->{
            log.info("Fetching comments for post ID: {}", postId);
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
            List<Comment> list= commentRepository.findByPostId(postId);
            return list.stream()
                    .map(comment -> Mapper.toDto(comment,CommentResponse.class))
                    .collect(Collectors.toList());
        });
    }
}
