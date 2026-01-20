package it.schema31.crm.cto.dto.gitlab;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitLabProjectDTO {

    public Long id;

    public String name;

    @JsonProperty("name_with_namespace")
    public String nameWithNamespace;

    public String path;

    @JsonProperty("path_with_namespace")
    public String pathWithNamespace;

    public String description;

    @JsonProperty("web_url")
    public String webUrl;

    @JsonProperty("default_branch")
    public String defaultBranch;

    public String visibility;
}
