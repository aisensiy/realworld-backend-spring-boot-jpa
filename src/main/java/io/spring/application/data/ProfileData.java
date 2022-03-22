package io.spring.application.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@org.hibernate.annotations.Immutable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileData {
  @Id @JsonIgnore private String id;
  @JsonIgnore private String email;
  private String username;
  private String bio;
  private String image;

  @Transient private boolean following;

  public ProfileData(String id, String username, String bio, String image, boolean following) {
    this.id = id;
    this.username = username;
    this.bio = bio;
    this.image = image;
    this.following = following;
  }

  public ProfileData(String id) {
    this.id = id;
  }
}
