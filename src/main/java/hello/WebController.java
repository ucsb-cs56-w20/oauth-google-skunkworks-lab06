package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Map;
import java.util.HashMap;

import com.nimbusds.oauth2.sdk.client.ClientReadRequest;

@Controller
public class WebController {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @GetMapping("/")
    public String getHomepage(Model model, OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        model.addAttribute("isLoggedIn", oAuth2AuthenticationToken != null);
        return "index";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        Map<String, String> urls = new HashMap<>();

        Iterable<ClientRegistration> iterable;
        iterable = ((Iterable<ClientRegistration>) clientRegistrationRepository);
        iterable.forEach(clientRegistration -> urls.put(clientRegistration.getClientName(),
                "/oauth2/authorization/" + clientRegistration.getRegistrationId()));

        model.addAttribute("urls", urls);
        return "login";
    }

    @GetMapping("/info")
    public String getInfo(Model model, OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();

        // https://developer.github.com/v3/users/#get-the-authenticated-user

        model.addAttribute("name", oAuth2User.getAttributes().get("name"));
        model.addAttribute("login", oAuth2User.getAttributes().get("login"));
        model.addAttribute("id", oAuth2User.getAttributes().get("id"));
        model.addAttribute("avatar_url", oAuth2User.getAttributes().get("avatar_url"));

        return "info";
    }

}
