package it.schema31.crm.cto.dto.gitlab;

import java.util.List;

public class GitLabStatsResponseDTO {

    public List<UserStatsDTO> userStats;
    public List<UserProjectStatsDTO> userProjectStats;
    public String startDate;
    public String endDate;
    public int totalProjects;
    public int totalCommits;
    public int totalAdditions;
    public int totalDeletions;
    public int totalLines;

    public GitLabStatsResponseDTO() {
    }

    public GitLabStatsResponseDTO(List<UserStatsDTO> userStats, List<UserProjectStatsDTO> userProjectStats,
            String startDate, String endDate) {
        this.userStats = userStats;
        this.userProjectStats = userProjectStats;
        this.startDate = startDate;
        this.endDate = endDate;

        this.totalProjects = (int) userProjectStats.stream()
                .map(s -> s.projectId)
                .distinct()
                .count();

        this.totalCommits = userStats.stream()
                .mapToInt(s -> s.commits)
                .sum();

        this.totalAdditions = userStats.stream()
                .mapToInt(s -> s.additions)
                .sum();

        this.totalDeletions = userStats.stream()
                .mapToInt(s -> s.deletions)
                .sum();

        this.totalLines = this.totalAdditions + this.totalDeletions;
    }
}
