package hello;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class AuthControllerAdvice {

    @Autowired   
    private MembershipService membershipService;

    @ModelAttribute("isLoggedIn")
    public boolean getIsLoggedIn(OAuth2AuthenticationToken token){
        return token != null;
    }

    @ModelAttribute("id")
    public String getUid(OAuth2AuthenticationToken token){
        return "stub";
        // if (token == null) return "";
        // return token.getPrincipal().getAttributes().get("given_name").toString();
    }

    @ModelAttribute("fname")
    public String getFirstName(OAuth2AuthenticationToken token){
        if (token == null) return "";
        return token.getPrincipal().getAttributes().get("given_name").toString();
    }

    @ModelAttribute("picture")
    public String getPicture(OAuth2AuthenticationToken token){
        // return "stub";
        if (token == null) return "";
        return token.getPrincipal().getAttributes().get("picture").toString();
    }

    @ModelAttribute("login")
    public String getLogin(OAuth2AuthenticationToken token){
        // return "stub";
        if (token == null) return "";
        return token.getName();
    }

    @ModelAttribute("oauth")
    public Object getOauth(OAuth2AuthenticationToken token){
        return token;
    }
    
    @ModelAttribute("isMember")
    public boolean getIsMember(OAuth2AuthenticationToken token){
        return membershipService.isMember(token);
    }
    @ModelAttribute("isAdmin")
    public boolean getIsAdmin(OAuth2AuthenticationToken token){
        return membershipService.isAdmin(token);
    }

    @ModelAttribute("role")
    public String getRole(OAuth2AuthenticationToken token){
        return membershipService.role(token);
    }
}