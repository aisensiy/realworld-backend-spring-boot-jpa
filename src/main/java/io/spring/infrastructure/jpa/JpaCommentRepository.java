package io.spring.infrastructure.jpa;

import io.spring.core.comment.Comment;
import io.spring.core.comment.CommentRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Primary
@Repository
@AllArgsConstructor
public class JpaCommentRepository implements CommentRepository {
  private final JpaCommentMapper mapper;

  @Override
  public void save(Comment comment) {
    mapper.save(comment);
  }

  @Override
  public Optional<Comment> findById(String articleId, String id) {
    return mapper.findById(id);
  }

  @Override
  public void remove(Comment comment) {
    mapper.delete(comment);
  }
}

interface JpaCommentMapper extends JpaRepository<Comment, String> {}
