package net.expenses.recorder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Kazi Tanvir Azad
 */
@AllArgsConstructor
@Getter
public final class JwtTokenDto implements ResponseDto, Serializable {
    @Serial
    private static final long serialVersionUID = -6357287078769829129L;
    @JsonProperty(value = "token_type")
    private String tokenType;

    @JsonProperty(value = "expires_in")
    private int expiresInSeconds;

    @JsonProperty(value = "access_token")
    private String accessToken;
}
