package com.nomad.socialspring.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for {@link Comment}
 */
public record CommentRequest(
        @NotNull
        @NotBlank
        @Size(max = 255)
        String content
) {

}