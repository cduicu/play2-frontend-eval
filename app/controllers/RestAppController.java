package controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.cache.CacheApi;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import services.BuildHubConfiguration;
import services.RestService;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * This controller provides JSON APIs that browsers can use to build the widgets/pages.
 * This approach is common for SPA. Note that the controllers is nothing more than a wrapper for the actual API.
 *
 * Created by cristiand on 10/18/2016.
 */
public class RestAppController extends Controller {

    private static Logger logger = LoggerFactory.getLogger(AppController.class);

    @Inject
    WSClient ws;
    @Inject
    HttpExecutionContext ec;
    @Inject
    CacheApi cache;
    @Inject
    BuildHubConfiguration hubCfg;

    public Result appInfoGroup(String groupName) {
        RestService restService = new RestService(ws, ec, cache, hubCfg);
        return ok(Json.toJson(restService.getAppInfoGroup(groupName))).as(Http.MimeTypes.JSON);
    }

    public Result appInfo(String appName) {
        RestService restService = new RestService(ws, ec, cache, hubCfg);
        return ok(Json.toJson(restService.getAppInfo(appName))).as(Http.MimeTypes.JSON);
    }

    public CompletionStage<Result> buildInfoFuture(String appName, String buildId) {
        RestService restService = new RestService(ws, ec, cache, hubCfg);
        return restService.getBuildInfoFuture(appName, buildId)
                .exceptionally(throwable -> {
                    logger.error("Failed to get the appinfo", throwable);
                    return null;
                })
                .thenApplyAsync(Results::ok);
    }

    public Result appsInGroup(String groupName) {
        RestService restService = new RestService(ws, ec, cache, hubCfg);
        return ok(Json.toJson(restService.getAppsInGroup(groupName))).as(Http.MimeTypes.JSON);
    }
}
