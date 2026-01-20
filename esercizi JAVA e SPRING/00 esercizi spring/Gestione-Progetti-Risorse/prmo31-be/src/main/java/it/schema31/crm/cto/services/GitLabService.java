package it.schema31.crm.cto.services;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.logging.Log;
import it.schema31.crm.cto.config.GitLabConfig;
import it.schema31.crm.cto.dto.gitlab.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GitLabService {

    @Inject
    GitLabConfig gitLabConfig;

    @Inject
    ObjectMapper objectMapper;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    private String getApiUrl() {
        return gitLabConfig.url() + "/api/" + gitLabConfig.apiVersion();
    }

    private HttpRequest.Builder createRequest(String endpoint) {
        return HttpRequest.newBuilder()
                .uri(URI.create(getApiUrl() + endpoint))
                .header("PRIVATE-TOKEN", gitLabConfig.token())
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(60));
    }

    public List<GitLabProjectDTO> getProjects() throws Exception {
        List<GitLabProjectDTO> allProjects = new ArrayList<>();
        int page = 1;
        int perPage = gitLabConfig.perPage();
        int totalPages = Integer.MAX_VALUE;

        Log.infof("GitLab: Fetching projects with per_page=%d", perPage);

        while (page <= totalPages) {
            String endpoint = "/projects?membership=true&per_page=" + perPage + "&page=" + page;
            HttpRequest request = createRequest(endpoint)
                    .GET()
                    .build();

            Log.debugf("GitLab: Requesting %s", endpoint);

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("GitLab API error: " + response.statusCode() + " - " + response.body());
            }

            // Read pagination headers from GitLab
            String totalPagesHeader = response.headers().firstValue("X-Total-Pages").orElse(null);
            String totalHeader = response.headers().firstValue("X-Total").orElse(null);

            if (totalPagesHeader != null && page == 1) {
                totalPages = Integer.parseInt(totalPagesHeader);
                Log.infof("GitLab: Total pages=%d, Total items=%s", totalPages, totalHeader);
            }

            List<GitLabProjectDTO> projects = objectMapper.readValue(
                    response.body(),
                    new TypeReference<List<GitLabProjectDTO>>() {
                    });

            Log.debugf("GitLab: Page %d returned %d projects", page, projects.size());

            if (projects.isEmpty()) {
                break;
            }

            allProjects.addAll(projects);
            page++;
        }

        Log.infof("GitLab: Total projects fetched: %d", allProjects.size());
        return allProjects;
    }

    public List<GitLabCommitDTO> getCommits(Long projectId, String since, String until) throws Exception {
        List<GitLabCommitDTO> allCommits = new ArrayList<>();
        int page = 1;
        int perPage = gitLabConfig.perPage();
        int totalPages = Integer.MAX_VALUE;

        StringBuilder baseEndpoint = new StringBuilder("/projects/" + projectId + "/repository/commits?with_stats=true");
        baseEndpoint.append("&per_page=").append(perPage);

        if (since != null && !since.isEmpty()) {
            baseEndpoint.append("&since=").append(URLEncoder.encode(since + "T00:00:00Z", StandardCharsets.UTF_8));
        }
        if (until != null && !until.isEmpty()) {
            baseEndpoint.append("&until=").append(URLEncoder.encode(until + "T23:59:59Z", StandardCharsets.UTF_8));
        }

        while (page <= totalPages) {
            HttpRequest request = createRequest(baseEndpoint.toString() + "&page=" + page)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                // Skip projects with no access or errors
                break;
            }

            // Read pagination headers
            String totalPagesHeader = response.headers().firstValue("X-Total-Pages").orElse(null);
            if (totalPagesHeader != null && page == 1) {
                totalPages = Integer.parseInt(totalPagesHeader);
            }

            List<GitLabCommitDTO> commits = objectMapper.readValue(
                    response.body(),
                    new TypeReference<List<GitLabCommitDTO>>() {
                    });

            if (commits.isEmpty()) {
                break;
            }

            allCommits.addAll(commits);
            page++;
        }

        return allCommits;
    }

    public List<GitLabContributorDTO> getContributors(Long projectId) throws Exception {
        HttpRequest request = createRequest("/projects/" + projectId + "/repository/contributors")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            return new ArrayList<>();
        }

        return objectMapper.readValue(
                response.body(),
                new TypeReference<List<GitLabContributorDTO>>() {
                });
    }

    public List<GitLabMergeRequestDTO> getMergeRequests(Long projectId, String state, String createdAfter,
            String createdBefore) throws Exception {
        List<GitLabMergeRequestDTO> allMRs = new ArrayList<>();
        int page = 1;
        int perPage = gitLabConfig.perPage();
        int totalPages = Integer.MAX_VALUE;

        StringBuilder baseEndpoint = new StringBuilder("/projects/" + projectId + "/merge_requests?per_page=" + perPage);

        if (state != null && !state.isEmpty()) {
            baseEndpoint.append("&state=").append(state);
        }
        if (createdAfter != null && !createdAfter.isEmpty()) {
            baseEndpoint.append("&created_after=").append(URLEncoder.encode(createdAfter + "T00:00:00Z", StandardCharsets.UTF_8));
        }
        if (createdBefore != null && !createdBefore.isEmpty()) {
            baseEndpoint.append("&created_before=").append(URLEncoder.encode(createdBefore + "T23:59:59Z", StandardCharsets.UTF_8));
        }

        while (page <= totalPages) {
            HttpRequest request = createRequest(baseEndpoint.toString() + "&page=" + page)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                break;
            }

            // Read pagination headers
            String totalPagesHeader = response.headers().firstValue("X-Total-Pages").orElse(null);
            if (totalPagesHeader != null && page == 1) {
                totalPages = Integer.parseInt(totalPagesHeader);
            }

            List<GitLabMergeRequestDTO> mrs = objectMapper.readValue(
                    response.body(),
                    new TypeReference<List<GitLabMergeRequestDTO>>() {
                    });

            if (mrs.isEmpty()) {
                break;
            }

            allMRs.addAll(mrs);
            page++;
        }

        return allMRs;
    }

    public List<GitLabIssueDTO> getIssues(Long projectId, String state, String createdAfter, String createdBefore)
            throws Exception {
        List<GitLabIssueDTO> allIssues = new ArrayList<>();
        int page = 1;
        int perPage = gitLabConfig.perPage();
        int totalPages = Integer.MAX_VALUE;

        StringBuilder baseEndpoint = new StringBuilder("/projects/" + projectId + "/issues?per_page=" + perPage);

        if (state != null && !state.isEmpty()) {
            baseEndpoint.append("&state=").append(state);
        }
        if (createdAfter != null && !createdAfter.isEmpty()) {
            baseEndpoint.append("&created_after=").append(URLEncoder.encode(createdAfter + "T00:00:00Z", StandardCharsets.UTF_8));
        }
        if (createdBefore != null && !createdBefore.isEmpty()) {
            baseEndpoint.append("&created_before=").append(URLEncoder.encode(createdBefore + "T23:59:59Z", StandardCharsets.UTF_8));
        }

        while (page <= totalPages) {
            HttpRequest request = createRequest(baseEndpoint.toString() + "&page=" + page)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                break;
            }

            // Read pagination headers
            String totalPagesHeader = response.headers().firstValue("X-Total-Pages").orElse(null);
            if (totalPagesHeader != null && page == 1) {
                totalPages = Integer.parseInt(totalPagesHeader);
            }

            List<GitLabIssueDTO> issues = objectMapper.readValue(
                    response.body(),
                    new TypeReference<List<GitLabIssueDTO>>() {
                    });

            if (issues.isEmpty()) {
                break;
            }

            allIssues.addAll(issues);
            page++;
        }

        return allIssues;
    }

    public GitLabStatsResponseDTO getCommitStats(String startDate, String endDate, String userEmail, Long projectId) throws Exception {
        List<GitLabProjectDTO> projects = getProjects();

        // Filter projects if projectId is specified
        if (projectId != null) {
            projects = projects.stream()
                    .filter(p -> p.id.equals(projectId))
                    .collect(java.util.stream.Collectors.toList());
        }

        // Map to aggregate stats by user email
        Map<String, UserStatsDTO> userStatsMap = new HashMap<>();
        // Map to aggregate stats by user+project
        Map<String, UserProjectStatsDTO> userProjectStatsMap = new HashMap<>();

        for (GitLabProjectDTO project : projects) {
            try {
                List<GitLabCommitDTO> commits = getCommits(project.id, startDate, endDate);

                for (GitLabCommitDTO commit : commits) {
                    if (commit.stats == null) {
                        continue;
                    }

                    String email = commit.authorEmail != null ? commit.authorEmail.toLowerCase() : "unknown";
                    String name = commit.authorName != null ? commit.authorName : "Unknown";

                    // Filter by userEmail if specified
                    if (userEmail != null && !userEmail.isEmpty() && !email.equalsIgnoreCase(userEmail)) {
                        continue;
                    }

                    // Aggregate by user
                    UserStatsDTO userStats = userStatsMap.computeIfAbsent(email,
                            k -> new UserStatsDTO(name, email, 0, 0, 0));
                    userStats.add(1, commit.stats.additions, commit.stats.deletions);

                    // Aggregate by user + project
                    String key = email + "|" + project.id;
                    UserProjectStatsDTO userProjectStats = userProjectStatsMap.computeIfAbsent(key,
                            k -> new UserProjectStatsDTO(name, email, project.id, project.name, 0, 0, 0));
                    userProjectStats.add(1, commit.stats.additions, commit.stats.deletions);
                }
            } catch (Exception e) {
                // Skip projects with errors (access denied, etc)
                System.err.println("Error processing project " + project.name + ": " + e.getMessage());
            }
        }

        List<UserStatsDTO> userStats = new ArrayList<>(userStatsMap.values());
        userStats.sort((a, b) -> Integer.compare(b.totalLines, a.totalLines));

        List<UserProjectStatsDTO> userProjectStats = new ArrayList<>(userProjectStatsMap.values());
        userProjectStats.sort((a, b) -> {
            int cmp = a.userName.compareToIgnoreCase(b.userName);
            if (cmp != 0)
                return cmp;
            return Integer.compare(b.totalLines, a.totalLines);
        });

        return new GitLabStatsResponseDTO(userStats, userProjectStats, startDate, endDate);
    }

    public List<MergeRequestStatsDTO> getMergeRequestStats(String startDate, String endDate, String userEmail, Long projectId) throws Exception {
        List<GitLabProjectDTO> projects = getProjects();

        // Filter projects if projectId is specified
        if (projectId != null) {
            projects = projects.stream()
                    .filter(p -> p.id.equals(projectId))
                    .collect(java.util.stream.Collectors.toList());
        }

        Map<String, MergeRequestStatsDTO> statsMap = new HashMap<>();

        for (GitLabProjectDTO project : projects) {
            try {
                List<GitLabMergeRequestDTO> mrs = getMergeRequests(project.id, "all", startDate, endDate);

                for (GitLabMergeRequestDTO mr : mrs) {
                    if (mr.author == null)
                        continue;

                    String authorEmail = mr.author.username + "@schema31.it";

                    // Filter by userEmail if specified
                    if (userEmail != null && !userEmail.isEmpty() && !authorEmail.equalsIgnoreCase(userEmail)) {
                        continue;
                    }

                    String key = mr.author.username + "|" + project.id;
                    MergeRequestStatsDTO stats = statsMap.computeIfAbsent(key,
                            k -> new MergeRequestStatsDTO(mr.author.name,
                                    authorEmail,
                                    project.id, project.name));

                    switch (mr.state) {
                        case "opened":
                            stats.incrementOpened();
                            break;
                        case "merged":
                            stats.incrementMerged();
                            break;
                        case "closed":
                            stats.incrementClosed();
                            break;
                    }
                }
            } catch (Exception e) {
                System.err.println("Error processing MRs for project " + project.name + ": " + e.getMessage());
            }
        }

        List<MergeRequestStatsDTO> result = new ArrayList<>(statsMap.values());
        result.sort((a, b) -> Integer.compare(b.total, a.total));
        return result;
    }

    public List<IssueStatsDTO> getIssueStats(String startDate, String endDate, String userEmail, Long projectId) throws Exception {
        List<GitLabProjectDTO> projects = getProjects();

        // Filter projects if projectId is specified
        if (projectId != null) {
            projects = projects.stream()
                    .filter(p -> p.id.equals(projectId))
                    .collect(java.util.stream.Collectors.toList());
        }

        Map<String, IssueStatsDTO> statsMap = new HashMap<>();

        for (GitLabProjectDTO project : projects) {
            try {
                List<GitLabIssueDTO> issues = getIssues(project.id, "all", startDate, endDate);

                for (GitLabIssueDTO issue : issues) {
                    if (issue.author == null)
                        continue;

                    String authorEmail = issue.author.username + "@schema31.it";

                    // Filter by userEmail if specified
                    if (userEmail != null && !userEmail.isEmpty() && !authorEmail.equalsIgnoreCase(userEmail)) {
                        continue;
                    }

                    String key = issue.author.username + "|" + project.id;
                    IssueStatsDTO stats = statsMap.computeIfAbsent(key,
                            k -> new IssueStatsDTO(issue.author.name,
                                    authorEmail,
                                    project.id, project.name));

                    stats.incrementOpened();
                    if ("closed".equals(issue.state)) {
                        stats.incrementClosed();
                    }
                }

                // Track assigned issues
                for (GitLabIssueDTO issue : issues) {
                    if (issue.assignees != null) {
                        for (GitLabUserDTO assignee : issue.assignees) {
                            String assigneeEmail = assignee.username + "@schema31.it";

                            // Filter by userEmail if specified
                            if (userEmail != null && !userEmail.isEmpty() && !assigneeEmail.equalsIgnoreCase(userEmail)) {
                                continue;
                            }

                            String key = assignee.username + "|" + project.id;
                            IssueStatsDTO stats = statsMap.computeIfAbsent(key,
                                    k -> new IssueStatsDTO(assignee.name,
                                            assigneeEmail,
                                            project.id, project.name));
                            stats.incrementAssigned();
                        }
                    } else if (issue.assignee != null) {
                        String assigneeEmail = issue.assignee.username + "@schema31.it";

                        // Filter by userEmail if specified
                        if (userEmail != null && !userEmail.isEmpty() && !assigneeEmail.equalsIgnoreCase(userEmail)) {
                            continue;
                        }

                        String key = issue.assignee.username + "|" + project.id;
                        IssueStatsDTO stats = statsMap.computeIfAbsent(key,
                                k -> new IssueStatsDTO(issue.assignee.name,
                                        assigneeEmail,
                                        project.id, project.name));
                        stats.incrementAssigned();
                    }
                }
            } catch (Exception e) {
                System.err.println("Error processing issues for project " + project.name + ": " + e.getMessage());
            }
        }

        List<IssueStatsDTO> result = new ArrayList<>(statsMap.values());
        result.sort((a, b) -> Integer.compare(b.total, a.total));
        return result;
    }
}
