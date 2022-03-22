package io.spring;

import io.spring.application.data.ArticleData;
import io.spring.application.data.ProfileData;
import io.spring.core.user.User;
import java.util.ArrayList;
import org.joda.time.DateTime;

public class TestHelper {
  public static ArticleData articleDataFixture(String seed, User user) {
    DateTime now = new DateTime();
    return new ArticleData(
        seed + "id",
        "title-" + seed,
        "title " + seed,
        "desc " + seed,
        "body " + seed,
        false,
        0,
        now,
        now,
        new ArrayList<>(),
        new ProfileData(user.getId(), user.getUsername(), user.getBio(), user.getImage(), false));
  }
}
