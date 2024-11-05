package com.vijay.RobustScalableApp.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CommentResponse {
    private Long id;
    private String name;
    private String email;
    private String body;
    private Long postId;
}


