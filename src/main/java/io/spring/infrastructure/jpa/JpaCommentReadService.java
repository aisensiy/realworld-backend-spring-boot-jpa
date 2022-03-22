package io.spring.infrastructure.jpa;

import io.spring.application.CursorPageParameter;
import io.spring.application.data.CommentData;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class JpaCommentReadService {
  private final JpaCommentQueryService commentQueryService;

  public CommentData findById(String id) {
    return commentQueryService.getById(id);
  }

  public List<CommentData> findByArticleId(String articleId) {
    return commentQueryService.findByArticleId(articleId);
  }

  public List<CommentData> findByArticleIdWithCursor(
      String articleId, CursorPageParameter<DateTime> page) {
    return new ArrayList<>();
  }
}

interface JpaCommentQueryService extends JpaRepository<CommentData, String> {
  @Query("select c from CommentData c left join c.profileData where c.articleId = ?1")
  List<CommentData> findByArticleId(String articleId);

  @Query("select c from CommentData c left join c.profileData where c.id = ?1")
  CommentData getById(String id);
}
