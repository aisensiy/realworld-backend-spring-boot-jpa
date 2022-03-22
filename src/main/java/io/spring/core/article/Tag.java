package io.spring.core.article;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tags")
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = "name")
public class Tag {
  @Id private String id;
  private String name;

  public Tag(String name) {
    this.id = UUID.randomUUID().toString();
    this.name = name;
  }
}
