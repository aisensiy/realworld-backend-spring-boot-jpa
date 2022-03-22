package io.spring.infrastructure.jpa;

import io.spring.core.favorite.ArticleFavorite;
import io.spring.core.favorite.ArticleFavoriteRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Primary
@AllArgsConstructor
@Repository
public class JpaArticleFavoriteRepository implements ArticleFavoriteRepository {
  private final JpaArticleFavoriteMapper mapper;

  @Override
  public void save(ArticleFavorite articleFavorite) {
    mapper.save(articleFavorite);
  }

  @Override
  public Optional<ArticleFavorite> find(String articleId, String userId) {
    return mapper.findByIdArticleIdAndIdUserId(articleId, userId);
  }

  @Override
  public void remove(ArticleFavorite favorite) {
    mapper.delete(favorite);
  }
}

interface JpaArticleFavoriteMapper extends JpaRepository<ArticleFavorite, ArticleFavorite.ArticleFavoriteId> {
  Optional<ArticleFavorite> findByIdArticleIdAndIdUserId(String articleId, String userId);
}
