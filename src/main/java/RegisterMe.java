/**
 * Created by mengje on 5/11/17.
 */

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.google.appengine.repackaged.com.google.common.html.types.Html;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.List;
import java.util.ArrayList;

public class RegisterMe {

    private static final Logger log = Logger.getLogger(RegisterMe.class.getName());


    public String grepmail(String body) {
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("(https?://[^\\s<\"]+(?:signup|confirm|verify|activate)[^\\s<\"]+)").matcher(body);
        while (m.find()) {
            allMatches.add(m.group());
        }

        if (allMatches.size() > 0) {
            log.info("activation_url: " + allMatches.get(0));
            return allMatches.get(0);
        } else {
            log.info("no activation_url");
            return "";
        }
    }

    public void activate(String from_email, String from_name, String email, String text, String html) {
        String body = html+text;
        String activation_url = grepmail(body);

        if (!activation_url.equals("")) {
            log.info("Activation URL: "+activation_url);

            final WebClient webClient = new WebClient();
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setRedirectEnabled(true);
            webClient.getOptions().setTimeout(1000);

            try {
                int code = webClient.getPage(activation_url).getWebResponse().getStatusCode();
                if(code == 200 || code == 302) {
                    log.info("Activation URL successfully requested");

                    // Custom things for custom senders

                    if(from_email.equals("noreply@github.com")) {
                        final HtmlPage page = webClient.getPage(activation_url);

                        final HtmlInput username = page.getHtmlElementById("login_field");
                        final HtmlInput password = page.getHtmlElementById("password");
                        username.setValueAttribute(email);
                        password.setValueAttribute("Thusyou2");

                        final HtmlSubmitInput button = page.getElementByName("commit");
                        button.click();

                        log.info("Activated Github Account!");
                    }
                }
            } catch (IOException io) {
                io.printStackTrace();
            }


        }
    }

}
