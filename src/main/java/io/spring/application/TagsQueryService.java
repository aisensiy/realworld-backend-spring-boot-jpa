package io.spring.application;

import io.spring.core.article.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TagsQueryService {
  private final JpaTagQueryService tagReadService;

  public List<String> allTags() {
    return tagReadService.all();
  }
}

interface JpaTagQueryService extends JpaRepository<Tag, Long> {
  @Query(value = "SELECT t.name FROM tags t", nativeQuery = true)
  List<String> all();
}
