package io.spring.application;

import static java.util.stream.Collectors.toList;

import io.spring.application.data.ArticleData;
import io.spring.application.data.ArticleDataList;
import io.spring.application.data.ArticleFavoriteCount;
import io.spring.core.user.User;
import io.spring.infrastructure.jpa.JpaArticleReadService;
import io.spring.infrastructure.jpa.JpaUserReadService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ArticleQueryService {
  private JpaArticleReadService articleReadService;
  private JpaUserReadService userReadService;

  @Autowired
  public ArticleQueryService(
      JpaArticleReadService articleReadService, JpaUserReadService userReadService) {
    this.articleReadService = articleReadService;
    this.userReadService = userReadService;
  }

  public Optional<ArticleData> findById(String id, User user) {
    ArticleData articleData = articleReadService.findById(id);
    if (articleData == null) {
      return Optional.empty();
    } else {
      if (user != null) {
        fillExtraInfo(id, user, articleData);
      }
      return Optional.of(articleData);
    }
  }

  public Optional<ArticleData> findBySlug(String slug, User user) {
    ArticleData articleData = articleReadService.findBySlug(slug);
    if (articleData == null) {
      return Optional.empty();
    } else {
      if (user != null) {
        fillExtraInfo(articleData.getId(), user, articleData);
      }
      return Optional.of(articleData);
    }
  }

  public CursorPager<ArticleData> findRecentArticlesWithCursor(
      String tag,
      String author,
      String favoritedBy,
      CursorPageParameter<DateTime> page,
      User currentUser) {
    List<String> articleIds =
        articleReadService.findArticlesWithCursor(tag, author, favoritedBy, page);
    if (articleIds.size() == 0) {
      return new CursorPager<>(new ArrayList<>(), page.getDirection(), false);
    } else {
      boolean hasExtra = articleIds.size() > page.getLimit();
      if (hasExtra) {
        articleIds.remove(page.getLimit());
      }
      if (!page.isNext()) {
        Collections.reverse(articleIds);
      }

      List<ArticleData> articles = articleReadService.findArticles(articleIds);
      fillExtraInfo(articles, currentUser);

      return new CursorPager<>(articles, page.getDirection(), hasExtra);
    }
  }

  public CursorPager<ArticleData> findUserFeedWithCursor(
      User user, CursorPageParameter<DateTime> page) {
    List<String> followdUsers = userReadService.followedUsers(user.getId());
    if (followdUsers.size() == 0) {
      return new CursorPager<>(new ArrayList<>(), page.getDirection(), false);
    } else {
      List<ArticleData> articles =
          articleReadService.findArticlesOfAuthorsWithCursor(followdUsers, page);
      boolean hasExtra = articles.size() > page.getLimit();
      if (hasExtra) {
        articles.remove(page.getLimit());
      }
      if (!page.isNext()) {
        Collections.reverse(articles);
      }
      fillExtraInfo(articles, user);
      return new CursorPager<>(articles, page.getDirection(), hasExtra);
    }
  }

  public ArticleDataList findRecentArticles(
      String tag, String author, String favoritedBy, Page page, User currentUser) {
    var articleList = articleReadService.queryArticles(tag, author, favoritedBy, page);
    fillExtraInfo(articleList.getContent(), currentUser);
    return new ArticleDataList(articleList.getContent(), (int) articleList.getTotalElements());
  }

  public ArticleDataList findUserFeed(User user, Page page) {
    List<ArticleData> articles = articleReadService.getFeed(user.getId(), page);
    if (!articles.isEmpty()) {
      fillExtraInfo(articles, user);
    }
    int count = articleReadService.countFeedSize(user.getId(), page);
    return new ArticleDataList(articles, count);
  }

  private void fillExtraInfo(List<ArticleData> articles, User currentUser) {
    setFavoriteCount(articles);
    if (currentUser != null) {
      setIsFavorite(articles, currentUser);
      setIsFollowingAuthor(articles, currentUser);
    }
  }

  private void setIsFollowingAuthor(List<ArticleData> articles, User currentUser) {
    Set<String> followingAuthors =
        userReadService.followingAuthors(
            currentUser.getId(),
            articles.stream()
                .map(articleData1 -> articleData1.getProfileData().getId())
                .collect(toList()));
    articles.forEach(
        articleData -> {
          if (followingAuthors.contains(articleData.getProfileData().getId())) {
            articleData.getProfileData().setFollowing(true);
          }
        });
  }

  private void setFavoriteCount(List<ArticleData> articles) {
    List<ArticleFavoriteCount> favoritesCounts =
        articleReadService.articlesFavoriteCount(
            articles.stream().map(ArticleData::getId).collect(toList()));
    Map<String, Long> countMap = new HashMap<>();
    favoritesCounts.forEach(
        item -> {
          countMap.put(item.getId(), item.getCount());
        });
    articles.forEach(
        articleData ->
            articleData.setFavoritesCount(countMap.getOrDefault(articleData.getId(), 0L)));
  }

  private void setIsFavorite(List<ArticleData> articles, User currentUser) {
    Set<String> favoritedArticles =
        articleReadService.userFavorites(
            articles.stream().map(ArticleData::getId).collect(toList()), currentUser);

    articles.forEach(
        articleData -> {
          if (favoritedArticles.contains(articleData.getId())) {
            articleData.setFavorited(true);
          }
        });
  }

  private void fillExtraInfo(String id, User user, ArticleData articleData) {
    articleData.setFavorited(articleReadService.isUserFavorite(user.getId(), id));
    articleData.setFavoritesCount(articleReadService.articleFavoriteCount(id));
    articleData
        .getProfileData()
        .setFollowing(
            userReadService.isUserFollowing(user.getId(), articleData.getProfileData().getId()));
  }
}
