package hello;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class AuthControllerAdvice {

    @Autowired   
    private MembershipService membershipService;

    @ModelAttribute("isLoggedIn")
    public boolean setIsLoggedIn(OAuth2AuthenticationToken token){
        return token != null;
    }
    @ModelAttribute("isMember")
    public boolean setIsMember(OAuth2AuthenticationToken token){
        return membershipService.isMember(token);
    }
    @ModelAttribute("isAdmin")
    public boolean setIsAdmin(OAuth2AuthenticationToken token){
        return membershipService.isAdmin(token);
    }
}