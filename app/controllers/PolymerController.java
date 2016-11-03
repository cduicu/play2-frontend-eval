package controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import utils.CmnUtils;
import views.html.polymer.appGroup;
import views.html.polymer.appPage;
import views.html.polymer.pTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for UI related work in the SPA case.
 *
 * Created by cristiand on 10/18/2016.
 */
public class PolymerController extends Controller {

    private static Logger logger = LoggerFactory.getLogger(PolymerController.class);

    public Result index() {
        return ok(pTemplate.render(views.html.index.render()));
    }

    public Result appGroup(String groupName) {
        // here I am cheating a bit. A puristic approach to SPA might also get this information via the rest service.
        // instead, I am using the old play template thing to improve on speed and number of server requests.
        List<String> appsInGroup = CmnUtils.getAppCfgMap().values().stream()
                .filter(filterAppCfg -> filterAppCfg.group.equals(groupName))
                .map(appCfg -> appCfg.name)
                .collect(Collectors.toList());
        return ok(pTemplate.render(appGroup.render(groupName, appsInGroup)));
    }

    public Result appPage(String groupName, String appName) {
        return ok(pTemplate.render(appPage.render(groupName, appName)));
    }

    public Result buildInfo(String appName, String buildId) {
        return ok(pTemplate.render(Html.apply("TODO")));
    }

}
