package io.spring.application.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.spring.application.DateTimeCursor;
import io.spring.core.article.Tag;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import io.spring.core.helper.JodaTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@org.hibernate.annotations.Immutable
@Entity
@Table(name = "articles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ArticleData implements io.spring.application.Node {
  @Id private String id;
  private String slug;
  private String title;
  private String description;
  private String body;

  @Transient private boolean favorited;
  @Transient private long favoritesCount;

  @Convert(converter = JodaTimeConverter.class)
  private DateTime createdAt;

  @Convert(converter = JodaTimeConverter.class)
  private DateTime updatedAt;

  @ManyToMany(fetch = javax.persistence.FetchType.EAGER)
  @JoinTable(
      name = "article_tags",
      joinColumns = @javax.persistence.JoinColumn(name = "article_id"),
      inverseJoinColumns = @javax.persistence.JoinColumn(name = "tag_id"))
  private List<Tag> tagList = new ArrayList<>();

  @JsonProperty("author")
  @ManyToOne
  @JoinColumn(name = "user_id")
  private ProfileData profileData;

  public ArticleData(
      String id,
      String slug,
      String title,
      String description,
      String body,
      boolean favorited,
      int favoritesCount,
      DateTime createdAt,
      DateTime updatedAt,
      List<Tag> tagList,
      ProfileData profileData) {
    this.id = id;
    this.slug = slug;
    this.title = title;
    this.description = description;
    this.body = body;
    this.favorited = favorited;
    this.favoritesCount = favoritesCount;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.tagList = tagList;
    this.profileData = profileData;
  }

  public ArticleData(String id) {
    this.id = id;
  }

  @Override
  public DateTimeCursor getCursor() {
    return new DateTimeCursor(updatedAt);
  }
}
