package com.softuni.cuisineonline.util;


import com.softuni.cuisineonline.data.models.Profile;
import com.softuni.cuisineonline.data.models.Rank;
import com.softuni.cuisineonline.data.models.Role;
import com.softuni.cuisineonline.data.models.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class TestUtils {

    private static final Random RANDOM = new Random();
    private static final String[] USERNAMES = {"Pesho", "Gosho", "Koleto"};
    private static final int RANDOM_STRING_LENGTH = 8;

    public static final Map<String, Role> ALL_ROLES_MAP = new LinkedHashMap<>();

    static {
        ALL_ROLES_MAP.put("USER", new Role("USER"));
        ALL_ROLES_MAP.put("ADMIN", new Role("ADMIN"));
        ALL_ROLES_MAP.put("ROOT", new Role("ROOT"));
    }

    private TestUtils() {
    }

    public static List<User> getUsers() {
        List<User> result = new ArrayList<>();
        List<Role> roles = new ArrayList<>(ALL_ROLES_MAP.values());

        for (int i = 0; i < USERNAMES.length; i++) {
            User user = new User();
            user.setUsername(USERNAMES[i]);
            user.setPassword(getRandomString(RANDOM_STRING_LENGTH));
            user.setLastLogin(LocalDate.now().plus(i, ChronoUnit.DAYS));
            Set<Role> userRoles = new HashSet<>();
            for (int j = 0; j <= i; j++) {
                userRoles.add(roles.get(j));
            }
            user.setAuthorities(userRoles);

            user.setEmail(USERNAMES[i] + "@abv.bg");
            Profile profile = new Profile();
            profile.setRank(Rank.NOVICE);
            user.setProfile(profile);
            profile.setUser(user);

            result.add(user);
        }

        return Collections.unmodifiableList(result);
    }

    public static String getRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char symbol =
                    (char) (33 + RANDOM.nextInt(122 - 33)); // ASCII 33 to 122
            sb.append(symbol);
        }

        return sb.toString();
    }

    public static <T> T getRandomListValue(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Empty list");
        }

        return list.get(RANDOM.nextInt(list.size()));
    }
}
