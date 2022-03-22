package io.spring.core.comment;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.spring.core.helper.JodaTimeConverter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Comment {
  @Id private String id;

  private String body;

  @Column(name = "user_id")
  private String userId;

  @Column(name = "article_id")
  private String articleId;

  @Convert(converter = JodaTimeConverter.class)
  private DateTime createdAt;

  public Comment(String body, String userId, String articleId) {
    this.id = UUID.randomUUID().toString();
    this.body = body;
    this.userId = userId;
    this.articleId = articleId;
    this.createdAt = new DateTime();
  }
}
