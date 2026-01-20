package it.schema31.crm.cto.dto.gitlab;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitLabUserDTO {

    public Long id;

    public String username;

    public String name;

    public String state;

    @JsonProperty("avatar_url")
    public String avatarUrl;

    @JsonProperty("web_url")
    public String webUrl;
}
