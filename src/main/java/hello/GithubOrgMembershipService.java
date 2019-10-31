package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.jcabi.github.Github;
import com.jcabi.github.Organization;
import com.jcabi.github.RtGithub;
import com.jcabi.github.User;
import com.jcabi.github.wire.RetryCarefulWire;
import com.jcabi.http.Request;
import com.jcabi.http.response.JsonResponse;

/**
 * Service object that wraps the UCSB Academic Curriculum API
 */
@Service
public class GithubOrgMembershipService implements MembershipService {

    private Logger logger = LoggerFactory.getLogger(GithubOrgMembershipService.class);

    private String githubOrg;

    @Autowired
    private OAuth2AuthorizedClientService clientService;

    public GithubOrgMembershipService(@Value("${app_github_org}") String githubOrg) {
        this.githubOrg = githubOrg;
        logger.info("githubOrg=" + githubOrg);
    }

    /**
     * is current logged in user a member but NOT an admin of the github org
     */
    public boolean isMember(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        return hasRole(oAuth2AuthenticationToken, "member");
    }

    /** is current logged in user a member of the github org */
    public boolean isAdmin(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        return hasRole(oAuth2AuthenticationToken, "admin");
    }

    /**
     * is current logged in user has role
     * 
     * @param roleToTest "member" or "admin"
     * @return if the current logged in user has that role
     */

    public boolean hasRole(OAuth2AuthenticationToken oauthToken, String roleToTest) {
        if (oauthToken == null) {
            return false;
        }
        OAuth2User oAuth2User = oauthToken.getPrincipal();
        String user = (String) oAuth2User.getAttributes().get("login");

        Github github = null;

        OAuth2AuthorizedClient client = clientService
                .loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());

        String accessToken = client.getAccessToken().getTokenValue();

        try {

            // I forget why we have Github wrapped like this
            // TODO: find the tutorial that explains it
            // I think it has something to do with respecting rate limits
            github = new RtGithub(new RtGithub(accessToken).entry()
                    .through(RetryCarefulWire.class, 50));

            logger.info("github=" + github);
            User ghuser = github.users().get(user);
            logger.info("ghuser=" + ghuser);

            JsonResponse jruser = github.entry().uri().path("/user").back().method(Request.GET).fetch()
                    .as(JsonResponse.class);

            logger.info("jruser =" + jruser);

            Organization org = github.organizations().get(githubOrg);

            logger.info("org =" + org);

            JsonResponse jr = github.entry().uri().path("/user/memberships/orgs/" + githubOrg).back()
                    .method(Request.GET).fetch().as(JsonResponse.class);

            String actualRole = jr.json().readObject().getString("role");

            logger.info("jr =" + jr);
            logger.info("actualRole =" + actualRole);
            logger.info("roleToTest =" + roleToTest);

            return actualRole.equals(roleToTest);
        } catch (Exception e) {
            logger.warn("Exception happened while trying to determine membership in github org");
            logger.warn(e.toString());
        }
        return false;
    }

}
