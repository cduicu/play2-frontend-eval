package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.AppCfg;
import models.AppInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.cache.CacheApi;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import services.BuildHubConfiguration;
import utils.CmnUtils;
import views.html.app;
import views.html.appGroup;
import views.html.buildContent;
import views.html.mainTemplate;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is the standard way of building Play apps where the result is rendered via a play template
 * (ie. server side rendering).
 *
 * Created by cristiand on 10/12/2016.
 */
public class AppController extends Controller {

    private static Logger logger = LoggerFactory.getLogger(AppController.class);

    @Inject
    WSClient ws;
    @Inject
    HttpExecutionContext ec;
    @Inject
    CacheApi cache;
    @Inject
    BuildHubConfiguration hubCfg;

    public Result dashboard() {
        // TODO: need to figure out what should go into the dashboard
        return redirect(controllers.routes.AppController.index());
    }

    public Result index() {
        return ok(mainTemplate.render(views.html.index.render()));
    }

    public Result allApps() {
        flash().put(CmnUtils.SELECTED_MENU, "dashboard");

        // get list of apps in the same group
        List<AppInfo> groupList = CmnUtils.getAppCfgMap().values().stream()
                .map(appCfg -> cache.getOrElse(appCfg.name,
                                () -> CmnUtils.getAppInfoFromJenkins(ws, appCfg, hubCfg.getJenkinsUrl()),
                                hubCfg.getCacheTimeOut())
                ).collect(Collectors.toList());

        return ok(mainTemplate.render(appGroup.render(groupList)));
    }

    public Result appGroup(String groupName) {
        flash().put(CmnUtils.SELECTED_MENU, groupName);

        // get list of apps in the same group
        List<AppInfo> groupList = CmnUtils.getAppCfgMap().values().stream()
                .filter(filterAppCfg -> filterAppCfg.group.equals(groupName))
                .map(appCfg -> cache.getOrElse(appCfg.name,
                                () -> CmnUtils.getAppInfoFromJenkins(ws, appCfg, hubCfg.getJenkinsUrl()),
                                hubCfg.getCacheTimeOut())
                ).collect(Collectors.toList());

        return ok(mainTemplate.render(appGroup.render(groupList)));
    }

    public Result appPage(String groupName, String appName) {
        flash().put(CmnUtils.SELECTED_MENU, groupName);
        AppCfg appCfg = CmnUtils.getAppCfg(appName);

        // getting the appinfo
        AppInfo appInfo = cache.getOrElse(appCfg.name,
                () -> CmnUtils.getAppInfoFromJenkins(ws, appCfg, hubCfg.getJenkinsUrl()), hubCfg.getCacheTimeOut());

        // get list of apps in the same group
        List<String> groupList = CmnUtils.getAppCfgMap().values().stream()
                .filter(appCfg1 -> appCfg1.group.equals(appCfg.group))
                .map(appCfg2 -> appCfg2.name).collect(Collectors.toList());

        return ok(mainTemplate.render(app.render(appInfo, groupList)));
    }

    /**
     *
     * @param groupName
     * @param appName
     * @param buildId
     * @return
     */
    public Result buildInfo(String groupName, String appName, String buildId) {
        AppCfg appCfg = CmnUtils.getAppCfg(appName);

        AppInfo appInfo = cache.getOrElse(appCfg.name,
                () -> CmnUtils.getAppInfoFromJenkins(ws, appCfg, hubCfg.getJenkinsUrl()), hubCfg.getCacheTimeOut());

        // get the json for a specific build
        String buildInfoUrl = hubCfg.getJenkinsUrl() + "/" + appCfg.url + "/" + buildId + CmnUtils.JENKINS_API;
        logger.debug("Calling: {}", buildInfoUrl);
        JsonNode buildJson = ws.url(buildInfoUrl).get().thenApply(WSResponse::asJson).toCompletableFuture().join();

        return ok(buildContent.render(appInfo, buildJson, buildId));
    }


//    public List<CompletableFuture<AppInfo>> getAppInfoList(String group) {
//        ArrayList<CompletableFuture<AppInfo>> list = new ArrayList<>();
//
//        CmnUtils.getAppCfgMap().values().stream().forEach(appCfg -> {
//            if ((group == null) || (group != null && appCfg.group.startsWith(group))) {
//                String url = hubCfg.getJenkinsUrl() + "/" + appCfg.url;
//                String apiUrl = url + CmnUtils.JENKINS_API;
//                logger.debug("GET {}", apiUrl);
//                CompletionStage<AppInfo> res = ws.url(apiUrl).get()
//                        // format result as json
//                        .thenApply(WSResponse::asJson)
//                                // parse json into AppInfo object
//                        .thenApply(jenkinsNode -> AppInfo.build(appCfg, url, jenkinsNode));
//                list.add(res.toCompletableFuture());
//            }
//        });
//        return list;
//    }

}
