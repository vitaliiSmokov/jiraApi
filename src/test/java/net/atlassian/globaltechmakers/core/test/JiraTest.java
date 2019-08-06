package net.atlassian.globaltechmakers.core.test;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import net.atlassian.globaltechmakers.core.GTMJiraClient;
import org.junit.Test;

/** @author Vitalii Smokov 06.08.2019 */
public class JiraTest {

  @Test
  public void testCanLogin() {
    GTMJiraClient client =
        new GTMJiraClient(
            "mail.for.testbase@yandex.ru",
            "12345678",
            "https://testbase.atlassian.net"
        );
    JiraRestClient restClient = client.getRestClient();
    System.out.println(restClient.getProjectClient().getAllProjects());
  }
}
