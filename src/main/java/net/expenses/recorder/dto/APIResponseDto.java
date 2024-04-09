package net.expenses.recorder.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

/**
 * @author Kazi Tanvir Azad
 */
@JsonPropertyOrder(value = {"error", "message", "timestamp", "data"})
@Getter
public final class APIResponseDto implements ResponseDto, Serializable {
    @Serial
    private static final long serialVersionUID = -1872119470124665409L;
    private final String timestamp;
    private final boolean error;
    private final String message;
    private final Object data;

    private APIResponseDto(String timestamp, boolean error, String message, Object data) {
        this.timestamp = timestamp;
        this.error = error;
        this.message = message;
        this.data = data;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private boolean error;
        private String message;
        private Object data;
        private String timestamp;

        public Builder setError(boolean error) {
            this.error = error;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setData(Object data) {
            this.data = data;
            return this;
        }

        private void setTimestamp() {
            Instant instant = new Date().toInstant();
            this.timestamp = instant.toString();
        }

        public ResponseDto build() {
            setTimestamp();
            return new APIResponseDto(timestamp, error, message, data);
        }
    }
}
