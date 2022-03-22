package io.spring.core.favorite;

import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ArticleFavoriteRepository {
  void save(ArticleFavorite articleFavorite);

  Optional<ArticleFavorite> find(String articleId, String userId);

  void remove(ArticleFavorite favorite);
}
