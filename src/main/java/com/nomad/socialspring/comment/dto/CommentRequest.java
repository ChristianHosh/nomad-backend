package com.nomad.socialspring.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for {@link com.nomad.socialspring.comment.model.Comment}
 */
public record CommentRequest(
        @NotNull
        @NotBlank
        @Size(max = 255)
        String content
) {

}