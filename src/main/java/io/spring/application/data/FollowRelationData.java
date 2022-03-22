package io.spring.application.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity(name = "FollowRelationData")
@Table(name = "follows")
public class FollowRelationData {
  @Id
  @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private ProfileData user;

  @ManyToOne
  @JoinColumn(name = "follow_id")
  private ProfileData follow;
}
