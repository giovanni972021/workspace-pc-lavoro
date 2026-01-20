package it.schema31.crm.cto.dto.gitlab;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitLabIssueDTO {

    public Long id;

    public Long iid;

    public String title;

    public String description;

    public String state;

    @JsonProperty("created_at")
    public String createdAt;

    @JsonProperty("updated_at")
    public String updatedAt;

    @JsonProperty("closed_at")
    public String closedAt;

    public GitLabUserDTO author;

    public GitLabUserDTO assignee;

    public List<GitLabUserDTO> assignees;

    public List<String> labels;

    @JsonProperty("web_url")
    public String webUrl;
}
