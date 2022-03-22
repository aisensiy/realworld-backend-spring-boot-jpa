package io.spring.infrastructure.jpa;

import io.spring.core.article.Article;
import io.spring.core.article.ArticleRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Primary
@Repository
@AllArgsConstructor
public class JpaArticleRepository implements ArticleRepository {
  private final JpaArticleMapper articleMapper;

  @Override
  public void save(Article article) {
    articleMapper.save(article);
  }

  @Override
  public Optional<Article> findById(String id) {
    return articleMapper.findById(id);
  }

  @Override
  public Optional<Article> findBySlug(String slug) {
    return articleMapper.findBySlug(slug);
  }

  @Override
  public void remove(Article article) {
    articleMapper.delete(article);
  }
}

interface JpaArticleMapper extends JpaRepository<Article, String> {
  Optional<Article> findBySlug(String slug);
}
