package io.spring.core.favorite;

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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "article_favorites")
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class ArticleFavorite {
  @EmbeddedId
  @AttributeOverrides({
      @AttributeOverride(name = "articleId", column = @Column(name = "article_id")),
      @AttributeOverride(name = "userId", column = @Column(name = "user_id"))
  })
  private ArticleFavoriteId id;

  public ArticleFavorite(String articleId, String userId) {
    this.id = new ArticleFavoriteId(articleId, userId);
  }

  @EqualsAndHashCode
  @Embeddable
  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor
  public static class ArticleFavoriteId implements Serializable {
    private static final long serialVersionUID = 5843726680934162396L;
    private String articleId;
    private String userId;
  }
}
