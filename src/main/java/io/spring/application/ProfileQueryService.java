package io.spring.application;

import io.spring.application.data.ProfileData;
import io.spring.core.user.User;
import io.spring.infrastructure.jpa.JpaUserReadService;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ProfileQueryService {
  private final JpaUserReadService userReadService;

  public Optional<ProfileData> findByUsername(String username, User currentUser) {
    ProfileData profileData = userReadService.findByUsername(username);
    if (profileData == null) {
      return Optional.empty();
    } else {
      profileData.setFollowing(
          userReadService.isUserFollowing(currentUser.getId(), profileData.getId()));
      return Optional.of(profileData);
    }
  }
}
