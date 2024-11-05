package com.vijay.RobustScalableApp.model;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class PostRequest {
    private Long id;
    private String title;
    private String content;
    private String description;
    private Long categoryId;
    private Long userId;
}


