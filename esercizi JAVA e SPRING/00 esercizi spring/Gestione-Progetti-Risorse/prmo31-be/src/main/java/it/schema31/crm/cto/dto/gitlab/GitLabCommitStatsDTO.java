package it.schema31.crm.cto.dto.gitlab;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitLabCommitStatsDTO {

    public int additions;

    public int deletions;

    public int total;
}
