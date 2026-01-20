package it.schema31.crm.cto.dto.gitlab;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitLabMergeRequestDTO {

    public Long id;

    public Long iid;

    public String title;

    public String description;

    public String state;

    @JsonProperty("created_at")
    public String createdAt;

    @JsonProperty("updated_at")
    public String updatedAt;

    @JsonProperty("merged_at")
    public String mergedAt;

    @JsonProperty("closed_at")
    public String closedAt;

    @JsonProperty("source_branch")
    public String sourceBranch;

    @JsonProperty("target_branch")
    public String targetBranch;

    public GitLabUserDTO author;

    @JsonProperty("web_url")
    public String webUrl;
}
