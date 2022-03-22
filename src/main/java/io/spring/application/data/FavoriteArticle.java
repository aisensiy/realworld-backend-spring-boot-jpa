package io.spring.application.data;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "FavoriteArticle")
@Table(name = "article_favorites")
@NoArgsConstructor
public class FavoriteArticle {
  @EmbeddedId private FavoriteId id;

  @ManyToOne
  @JoinColumn(name = "user_id", insertable = false, updatable = false)
  private ProfileData user;

  @ManyToOne
  @JoinColumn(name = "article_id", insertable = false, updatable = false)
  private ArticleData article;
}

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@Getter
class FavoriteId implements Serializable {
  @Column(name = "user_id")
  private String userId;

  @Column(name = "article_id")
  private String articleId;
}
