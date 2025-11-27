package com.liclist.crawlers.modules.commons;

import com.liclist.crawlers.modules.commons.enums.EnvEnum;

public class Env {
    public static void check() throws RuntimeException {
        for (EnvEnum env : EnvEnum.values()) {
            if (System.getenv(env.name()) != null) {
                continue;
            }

            throw new RuntimeException(String.format("Env var \"%s\" is missing", env.name()));
        }
    }

    public static String get(EnvEnum env) {
        return System.getenv(env.name());
    }
}
