package com.vijay.RobustScalableApp.model;

import lombok.*;

import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class PostResponse {
    private Long id;
    private String title;
    private String description;
    private String content;
    private Long categoryId;
    private String categoryName;
    private Long userId;
    private String username;
    private Set<CommentResponse> comments;
}

