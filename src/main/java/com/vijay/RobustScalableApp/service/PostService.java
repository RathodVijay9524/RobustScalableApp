package com.vijay.RobustScalableApp.service;


import com.vijay.RobustScalableApp.entity.Post;

import com.vijay.RobustScalableApp.model.PostResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public interface PostService extends AsyncCrudOperations<Post,Long>  {
    CompletableFuture<List<PostResponse>> getPostsByCategory(Long categoryId);
}
