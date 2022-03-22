package io.spring.application;

import io.spring.application.data.UserData;
import io.spring.infrastructure.jpa.JpaUserReadService;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserQueryService {
  private JpaUserReadService userReadService;

  public UserQueryService(JpaUserReadService userReadService) {
    this.userReadService = userReadService;
  }

  public Optional<UserData> findById(String id) {
    return Optional.ofNullable(userReadService.findById(id));
  }
}
