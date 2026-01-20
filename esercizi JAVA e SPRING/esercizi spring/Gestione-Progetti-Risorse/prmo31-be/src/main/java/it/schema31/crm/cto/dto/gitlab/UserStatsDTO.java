package it.schema31.crm.cto.dto.gitlab;

public class UserStatsDTO {

    public String name;
    public String email;
    public int commits;
    public int additions;
    public int deletions;
    public int totalLines;

    public UserStatsDTO() {
    }

    public UserStatsDTO(String name, String email, int commits, int additions, int deletions) {
        this.name = name;
        this.email = email;
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
