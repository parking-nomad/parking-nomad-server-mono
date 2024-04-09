package parkingnomad.parkingnomadservermono.member.adaptor.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "AccessToken 응답")
public record AccessTokenResponse(

        @Schema(description = "access token")
        String accessToken
) {
}
