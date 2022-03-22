package io.spring.core.user;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

@Entity
@Table(name = "follows")
@NoArgsConstructor
@Data
public class FollowRelation {
  @EmbeddedId
  @AttributeOverrides({
      @AttributeOverride(name = "targetId", column = @Column(name = "follow_id")),
      @AttributeOverride(name = "userId", column = @Column(name = "user_id"))
  })
  private FollowRelationId id;

  public FollowRelation(String userId, String followId) {
    this.id = new FollowRelationId(userId, followId);
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Embeddable
  public static class FollowRelationId implements Serializable {
    private static final long serialVersionUID = 5449670643755651725L;
    private String userId;
    private String targetId;
  }
}
