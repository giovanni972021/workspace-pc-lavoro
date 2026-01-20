package it.schema31.crm.cto.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "gitlab")
public interface GitLabConfig {

    String url();

    String token();

    @WithDefault("v4")
    String apiVersion();

    @WithDefault("100")
    int perPage();
}
