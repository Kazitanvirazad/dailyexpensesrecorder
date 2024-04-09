package net.expenses.recorder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Kazi Tanvir Azad
 */
@AllArgsConstructor
@Getter
public class JwtTokenDto implements ResponseDto {
    @JsonProperty(value = "token_type")
    private String tokenType;

    @JsonProperty(value = "expires_in")
    private int expiresInSeconds;

    @JsonProperty(value = "access_token")
    private String accessToken;
}
