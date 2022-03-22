package io.spring.infrastructure.jpa;

import io.spring.application.data.ProfileData;
import io.spring.application.data.UserData;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JpaUserReadService {
  private final JpaUserQueryService jpaUserQueryService;

  public List<String> followedUsers(String id) {
    return jpaUserQueryService.followUsers(id);
  }

  public Set<String> followingAuthors(String id, List<String> authors) {
    return jpaUserQueryService.followingAuthors(id, authors);
  }

  public boolean isUserFollowing(String userId, String targetId) {
    return jpaUserQueryService.isUserFollowing(userId, targetId) > 0;
  }

  public ProfileData findByUsername(String username) {
    return jpaUserQueryService.findByUsername(username);
  }

  public UserData findById(String id) {
    return jpaUserQueryService.findUserDataById(id);
  }
}

interface JpaUserQueryService extends JpaRepository<ProfileData, String> {
  @Query("select u.id from FollowRelationData f left join f.follow u where f.user.id = ?1")
  List<String> followUsers(String id);

  @Query(
      value = "select follow_id from follows where user_id = ?1 and follow_id in ?2",
      nativeQuery = true)
  Set<String> followingAuthors(String id, List<String> authors);

  @Query(
      value = "select count(*) from follows where user_id = ?1 and follow_id = ?2",
      nativeQuery = true)
  int isUserFollowing(String userId, String targetId);

  ProfileData findByUsername(String username);

  @Query(
      "select new io.spring.application.data.UserData(u.id, u.email, u.username, u.bio, u.image) from ProfileData u where u.id = ?1")
  UserData findUserDataById(String id);
}
