package it.schema31.crm.cto.dto.gitlab;

public class MergeRequestStatsDTO {

    public String userName;
    public String userEmail;
    public Long projectId;
    public String projectName;
    public int opened;
    public int merged;
    public int closed;
    public int total;

    public MergeRequestStatsDTO() {
    }

    public MergeRequestStatsDTO(String userName, String userEmail, Long projectId, String projectName) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.projectId = projectId;
        this.projectName = projectName;
        this.opened = 0;
        this.merged = 0;
        this.closed = 0;
        this.total = 0;
    }

    public void incrementOpened() {
        this.opened++;
        this.total++;
    }

    public void incrementMerged() {
        this.merged++;
        this.total++;
    }

    public void incrementClosed() {
        this.closed++;
        this.total++;
    }
}
