package site.lawmate.user.domain.dto;

import lombok.Builder;

@Builder
public record OAuth2UserDto(
        String id,
        String name,
        String email,
        String profile
) {}
