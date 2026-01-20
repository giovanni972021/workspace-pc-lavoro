package it.schema31.crm.cto.dto.gitlab;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitLabCommitDTO {

    public String id;

    @JsonProperty("short_id")
    public String shortId;

    public String title;

    public String message;

    @JsonProperty("author_name")
    public String authorName;

    @JsonProperty("author_email")
    public String authorEmail;

    @JsonProperty("authored_date")
    public String authoredDate;

    @JsonProperty("committer_name")
    public String committerName;

    @JsonProperty("committer_email")
    public String committerEmail;

    @JsonProperty("committed_date")
    public String committedDate;

    @JsonProperty("web_url")
    public String webUrl;

    public GitLabCommitStatsDTO stats;
}
