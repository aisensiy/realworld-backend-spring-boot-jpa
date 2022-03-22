package io.spring.infrastructure.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import io.spring.application.CursorPageParameter;
import io.spring.application.Page;
import io.spring.application.data.ArticleData;
import io.spring.application.data.ArticleFavoriteCount;
import io.spring.application.data.QArticleData;
import io.spring.application.data.QProfileData;
import io.spring.core.user.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class JpaArticleReadService {
  private final JpaArticleQueryService articleQueryService;

  public ArticleData findBySlug(String slug) {
    return articleQueryService.findBySlug(slug);
  }

  public ArticleData findById(String id) {
    return articleQueryService.findById(id).orElse(null);
  }

  public org.springframework.data.domain.Page<ArticleData> queryArticles(String tag, String author, String favoritedBy, Page page) {
    return articleQueryService.findAll(tag, author, favoritedBy, PageRequest.of(page.getOffset() / page.getLimit(), page.getLimit()));
  }

  public List<ArticleData> findArticles(List<String> articleIds) {
    return articleQueryService.findAllById(articleIds);
  }

  public List<ArticleData> getFeed(String userId, Page page) {
    var articles =
        articleQueryService.findArticlesFromFollowings(
            userId, PageRequest.of(page.getOffset() / page.getLimit(), page.getLimit()));
    return articles.getContent();
  }

  public List<ArticleData> findArticlesOfAuthorsWithCursor(
      List<String> authors, CursorPageParameter page) {
    return new ArrayList<>();
  }

  public int countFeedSize(String userId, Page page) {
    var articles =
        articleQueryService.findArticlesFromFollowings(
            userId, PageRequest.of(page.getOffset() / page.getLimit(), page.getLimit()));
    return (int) articles.getTotalElements();
  }

  public List<String> findArticlesWithCursor(
      String tag, String author, String favoritedBy, CursorPageParameter page) {
    return new ArrayList<>();
  }

  public boolean isUserFavorite(String userId, String articleId) {
    return articleQueryService.isUserFavorite(userId, articleId);
  }

  public int articleFavoriteCount(String articleId) {
    List<ArticleFavoriteCount> articleFavoriteCounts = articleQueryService.articlesFavoriteCount(List.of(articleId));
    return articleFavoriteCounts.isEmpty() ? 0 : Math.toIntExact(articleFavoriteCounts.get(0).getCount());
  }

  public List<ArticleFavoriteCount> articlesFavoriteCount(List<String> ids) {
    return articleQueryService.articlesFavoriteCount(ids);
  }

  public Set<String> userFavorites(List<String> ids, User currentUser) {
    return articleQueryService.userFavorites(ids, currentUser.getId());
  }

  private BooleanExpression filterArticleBy(String tag, String author, String favoritedBy) {
    var predicates = new ArrayList<BooleanExpression>();
    if (tag != null) {
      predicates.add(QArticleData.articleData.tagList.any().name.eq(tag));
    }
    if (author != null) {
      predicates.add(QArticleData.articleData.profileData.id.eq(author));
    }
    //    if (favoritedBy != null) {
    //      predicates.add(QArticleData.articleData.favoritedBy.contains(new
    // ProfileData(favoritedBy)));
    //    }

    return Expressions.allOf(new BooleanExpression[predicates.size()]);
  }
}

interface JpaArticleQueryService
    extends JpaRepository<ArticleData, String>, QuerydslPredicateExecutor<ArticleData> {
  ArticleData findBySlug(String slug);

  @Query(
      "select DISTINCT a from ArticleData a "
        + "left join a.tagList t "
        + "left join a.profileData p "
        + "left join FavoriteArticle f on f.article.id = a.id "
        + "left join ProfileData u on u.id = f.user.id "
        + "where (:tag is null or t.name = :tag) and "
        + "(:author is null or p.username = :author) and "
        + "(:favoritedBy is null or u.username = :favoritedBy)")
  org.springframework.data.domain.Page<ArticleData> findAll(
      String tag,
      String author,
      String favoritedBy,
      Pageable pageable);


  @Query(
      "select a from ArticleData a where a.profileData.id in (select f.follow.id from FollowRelationData f where f.user.id = :userId)")
  org.springframework.data.domain.Page<ArticleData> findArticlesFromFollowings(
      String userId, org.springframework.data.domain.Pageable page);

  @Query(
      "select new io.spring.application.data.ArticleFavoriteCount(f.article.id, count(f.user.id)) from FavoriteArticle f where f.article.id in ?1 group by f.article.id")
  List<ArticleFavoriteCount> articlesFavoriteCount(List<String> ids);

  @Query(
      "select f.article.id from FavoriteArticle f left join f.user p where f.user.id = ?2 and f.article.id in ?1")
  Set<String> userFavorites(List<String> ids, String userId);

  @Query(
      "select case when (count(f) > 0) then true else false end from FavoriteArticle f where f.user.id = :userId and f.article.id = :articleId")
  boolean isUserFavorite(String userId, String articleId);
}
