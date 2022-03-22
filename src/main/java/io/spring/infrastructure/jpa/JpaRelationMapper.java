package io.spring.infrastructure.jpa;

import io.spring.core.user.FollowRelation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaRelationMapper extends JpaRepository<FollowRelation, FollowRelation.FollowRelationId> {
  Optional<FollowRelation> findByIdUserIdAndIdTargetId(String userId, String targetId);
}
