package controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import utils.CmnUtils;
import views.html.angular.appGroup;
import views.html.angular.appPage;
import views.html.angular.ngBrowserTemplate;
import views.html.angular.ngSvrTemplate;
import views.html.angular.ngTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for UI related work in the SPA case.
 *
 * Created by cristiand on 10/18/2016.
 */
public class AngularController extends Controller {

    private static Logger logger = LoggerFactory.getLogger(AngularController.class);
    public enum NgMode { FULL_SERVER_SIDE, SVR_COMPILED_TS, BROWSER_COMPILED_TS }
    private static NgMode NG_MODE = NgMode.SVR_COMPILED_TS;

    public Result index() {
        return ok(views.html.mainTemplate.render(views.html.index.render()));
    }

    public Result appGroup(String groupName) {
        // here I am cheating a bit. A puristic approach to SPA might also get this information via the rest service.
        // instead, I am using the old play template thing to improve on speed and number of server requests.
        List<String> appsInGroup = CmnUtils.getAppCfgMap().values().stream()
                .filter(filterAppCfg -> filterAppCfg.group.equals(groupName))
                .map(appCfg -> appCfg.name)
                .collect(Collectors.toList());
        return ok(renderInTemplate(appGroup.render(groupName, appsInGroup)));
    }

    public Result appPage(String groupName, String appName) {
        return ok(renderInTemplate(appPage.render(groupName, appName)));
    }

    public Result buildInfo(String appName, String buildId) {
        return ok(ngTemplate.render(Html.apply("TODO")));
    }

    /**
     * Ability to switch the way angular and typescript works.
     *
     * @param content
     * @return
     */
    private Html renderInTemplate(Html content) {
        logger.info("NG_MODE: " + NG_MODE);
        switch (NG_MODE) {
            case FULL_SERVER_SIDE: return ngSvrTemplate.render(content);
            case SVR_COMPILED_TS: return ngTemplate.render(content);
            case BROWSER_COMPILED_TS: return ngBrowserTemplate.render(content); // only this one works
            default: return ngSvrTemplate.render(content);
        }
    }

}
