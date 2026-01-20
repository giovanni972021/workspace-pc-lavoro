package it.schema31.crm.cto.dto.gitlab;

public class UserProjectStatsDTO {

    public String userName;
    public String userEmail;
    public Long projectId;
    public String projectName;
    public int commits;
    public int additions;
    public int deletions;
    public int totalLines;

    public UserProjectStatsDTO() {
    }

    public UserProjectStatsDTO(String userName, String userEmail, Long projectId, String projectName,
            int commits, int additions, int deletions) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.projectId = projectId;
        this.projectName = projectName;
        this.commits = commits;
        this.additions = additions;
        this.deletions = deletions;
        this.totalLines = additions + deletions;
    }

    public void add(int commits, int additions, int deletions) {
        this.commits += commits;
        this.additions += additions;
        this.deletions += deletions;
        this.totalLines = this.additions + this.deletions;
    }
}
