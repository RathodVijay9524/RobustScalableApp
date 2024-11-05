package com.vijay.RobustScalableApp.service.Impl;

import com.vijay.RobustScalableApp.entity.Category;
import com.vijay.RobustScalableApp.entity.Post;
import com.vijay.RobustScalableApp.exception.ResourceNotFoundException;
import com.vijay.RobustScalableApp.model.CategoryResponse;
import com.vijay.RobustScalableApp.model.PostResponse;
import com.vijay.RobustScalableApp.repository.CategoryRepository;
import com.vijay.RobustScalableApp.repository.PostRepository;
import com.vijay.RobustScalableApp.service.AsyncCrudOperations;
import com.vijay.RobustScalableApp.service.PostService;
import com.vijay.RobustScalableApp.util.Mapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
@Log4j2
public class PostServiceImpl extends GenericAsyncService<Post,Long> implements PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    protected PostServiceImpl(PostRepository repository, Executor executor, PostRepository postRepository, CategoryRepository categoryRepository) {
        super(repository, Post.class, executor);

        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CompletableFuture<List<PostResponse>> getPostsByCategory(Long categoryId) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Fetching posts by category ID: {}", categoryId);
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
            List<Post> posts = postRepository.findByCategoryId(categoryId);
            return posts.stream()
                    .map(post -> Mapper.toDto(post,PostResponse.class))
                    .collect(Collectors.toList());
        });
    }
}
