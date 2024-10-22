package com.skillspace.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Error response")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    @Schema(description = "Error message", example = "Invalid input provided")
    private String message;

    @Schema(description = "Error code or type", example = "VALIDATION_ERROR")
    private String error;


    // Constructor for message-only errors
    public ErrorResponse(String message) {
        this.message = message;
    }

    // Builder methods for fluent API
    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder();
    }

    // Custom builder class with validation
    public static class ErrorResponseBuilder {
        private String message;
        private String error;

        ErrorResponseBuilder() {
        }

        public ErrorResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ErrorResponseBuilder error(String error) {
            this.error = error;
            return this;
        }

        public ErrorResponse build() {
            if (message == null || message.trim().isEmpty()) {
                throw new IllegalStateException("Message cannot be null or empty");
            }
            return new ErrorResponse(message, error);
        }
    }
}