package hello;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public interface MembershipService {

/** is current logged in user a member but NOT an admin
     * of the github org */
    public boolean isMember(OAuth2AuthenticationToken oAuth2AuthenticationToken);

    /** is current logged in user a member of the github org */
    public boolean isAdmin(OAuth2AuthenticationToken oAuth2AuthenticationToken);

    /** is current logged in user a member or admin of the
     * github org */
    default public boolean isMemberOrAdmin(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        return isMember(oAuth2AuthenticationToken) || isAdmin(oAuth2AuthenticationToken);
    }

}