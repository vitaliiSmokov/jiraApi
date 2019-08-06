package net.atlassian.globaltechmakers.core;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicVotes;
import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author Vitalii Smokov 06.08.2019
 */
public class GTMJiraClient {
  private String username;
  private String password;
  private String jiraUrl;
  private JiraRestClient restClient;

  public GTMJiraClient(String username, String password, String jiraUrl) {
    this.username = username;
    this.password = password;
    this.jiraUrl = jiraUrl;
    this.restClient = getJiraRestClient();
  }

  private JiraRestClient getJiraRestClient() {
    return new AsynchronousJiraRestClientFactory()
        .createWithBasicHttpAuthentication(getJiraUrl(), this.username, this.password);
  }

  public String createIssue(String projectKey, Long issueType, String issueSummary) {
    IssueRestClient issueClient = restClient.getIssueClient();
    IssueInput newIssue = new IssueInputBuilder(
        projectKey, issueType, issueSummary).build();
    return issueClient.createIssue(newIssue).claim().getKey();
  }

  public void updateIssueDescription(String issueKey, String newDescription) {
    IssueInput input = new IssueInputBuilder()
        .setDescription(newDescription)
        .build();
    restClient.getIssueClient()
        .updateIssue(issueKey, input)
        .claim();
  }

  public Issue getIssue(String issueKey) {
    return restClient.getIssueClient()
        .getIssue(issueKey)
        .claim();
  }

  public void voteForAnIssue(Issue issue) {
    restClient.getIssueClient()
        .vote(issue.getVotesUri())
        .claim();
  }

  public int getTotalVotesCount(String issueKey) {
    BasicVotes votes = getIssue(issueKey).getVotes();
    return votes == null ? 0 : votes.getVotes();
  }

  public void addComment(Issue issue, String commentBody) {
    restClient.getIssueClient()
        .addComment(issue.getCommentsUri(), Comment.valueOf(commentBody));
  }

  public List<Comment> getAllComments(String issueKey) {
    return StreamSupport.stream(getIssue(issueKey).getComments().spliterator(), false)
        .collect(Collectors.toList());
  }

  public void deleteIssue(String issueKey, boolean deleteSubtasks) {
    restClient.getIssueClient()
        .deleteIssue(issueKey, deleteSubtasks)
        .claim();
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  private URI getJiraUrl() {
    return URI.create(this.jiraUrl);
  }

  public void setJiraUrl(String jiraUrl) {
    this.jiraUrl = jiraUrl;
  }

  public JiraRestClient getRestClient() {
    return restClient;
  }

  public void setRestClient(JiraRestClient restClient) {
    this.restClient = restClient;
  }
}
