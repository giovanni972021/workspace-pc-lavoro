package it.schema31.crm.cto.dto.gitlab;

public class IssueStatsDTO {

    public String userName;
    public String userEmail;
    public Long projectId;
    public String projectName;
    public int opened;
    public int closed;
    public int assigned;
    public int total;

    public IssueStatsDTO() {
    }

    public IssueStatsDTO(String userName, String userEmail, Long projectId, String projectName) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.projectId = projectId;
        this.projectName = projectName;
        this.opened = 0;
        this.closed = 0;
        this.assigned = 0;
        this.total = 0;
    }

    public void incrementOpened() {
        this.opened++;
        this.total++;
    }

    public void incrementClosed() {
        this.closed++;
    }

    public void incrementAssigned() {
        this.assigned++;
    }
}
