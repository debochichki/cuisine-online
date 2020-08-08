package com.softuni.cuisineonline.service.models.user;

import com.softuni.cuisineonline.errors.ServerException;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Role standing is represented by the enum ordinal value.
 * Greater ordinal value means greater standing.
 */
public enum RoleStanding {
    USER,
    ADMIN,
    ROOT;

    public static RoleStanding resolve(List<String> userAuthorities) {
        if (userAuthorities == null || userAuthorities.isEmpty()) {
            throw new IllegalArgumentException(
                    "User authority list argument cannot be null or empty");
        }

        List<String> standings =
                Arrays.stream(RoleStanding.values())
                        .map(Enum::toString).collect(toList());
        if (!standings.containsAll(userAuthorities)) {
            userAuthorities.removeAll(standings);
            throw new ServerException("Unknown user authorities: " + userAuthorities);
        }

        RoleStanding roleStanding = null;
        for (String userAuthority : userAuthorities) {
            RoleStanding current = RoleStanding.valueOf(userAuthority);
            if (roleStanding == null || current.compareTo(roleStanding) > 0) {
                roleStanding = current;
            }
        }

        return roleStanding;
    }
}
