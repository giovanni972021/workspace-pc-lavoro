package it.schema31.crm.cto.dto.gitlab;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitLabContributorDTO {

    public String name;

    public String email;

    public int commits;

    public int additions;

    public int deletions;
}
