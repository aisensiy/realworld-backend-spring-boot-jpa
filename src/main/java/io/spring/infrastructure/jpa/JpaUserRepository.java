package io.spring.infrastructure.jpa;

import io.spring.core.user.FollowRelation;
import io.spring.core.user.User;
import io.spring.core.user.UserRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Primary
@Repository
@AllArgsConstructor
public class JpaUserRepository implements UserRepository {
  private final JpaUserMapper userMapper;
  private final JpaRelationMapper relationMapper;

  @Override
  public void save(User user) {
    userMapper.save(user);
  }

  @Override
  public Optional<User> findById(String id) {
    return userMapper.findById(id);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return userMapper.findByUsername(username);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return userMapper.findByEmail(email);
  }

  @Override
  public void saveRelation(FollowRelation followRelation) {
    relationMapper.save(followRelation);
  }

  @Override
  public Optional<FollowRelation> findRelation(String userId, String targetId) {
    return relationMapper.findByIdUserIdAndIdTargetId(userId, targetId);
  }

  @Override
  public void removeRelation(FollowRelation followRelation) {
    relationMapper.delete(followRelation);
  }
}
