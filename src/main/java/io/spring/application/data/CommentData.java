package io.spring.application.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.spring.application.DateTimeCursor;
import io.spring.application.Node;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.spring.core.helper.JodaTimeConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Immutable;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "comments")
@NoArgsConstructor
public class CommentData implements Node {
  @Id private String id;

  private String body;

  @Column(name = "article_id", insertable = false, updatable = false)
  @JsonIgnore
  private String articleId;

  @Convert(converter = JodaTimeConverter.class)
  private DateTime createdAt;

  @Convert(converter = JodaTimeConverter.class)
  private DateTime updatedAt;

  @JsonProperty("author")
  @ManyToOne
  @JoinColumn(name = "user_id", updatable = false, insertable = false)
  private ProfileData profileData;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "article_id", updatable = false, insertable = false)
  private ArticleData article;

  @Override
  public DateTimeCursor getCursor() {
    return new DateTimeCursor(createdAt);
  }
}