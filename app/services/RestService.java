package services;

import com.fasterxml.jackson.databind.JsonNode;
import models.AppCfg;
import models.AppInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.cache.CacheApi;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import utils.CmnUtils;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Created by cristiand on 10/18/2016.
 */
public class RestService {

    private static Logger logger = LoggerFactory.getLogger(RestService.class);

    // TODO: can these be injected as fields instead of via constructor?
    private final WSClient ws;
    private final HttpExecutionContext ec;
    private final CacheApi cache;
    private final BuildHubConfiguration hubCfg;

    @Inject
    public RestService(WSClient ws, HttpExecutionContext ec, CacheApi cache, BuildHubConfiguration hubCfg) {
        this.ws = ws;
        this.ec = ec;
        this.cache = cache;
        this.hubCfg = hubCfg;
    }

    /**
     *
     * @param appName
     * @return
     */
    public AppInfo getAppInfo(String appName) {
        AppCfg appCfg = CmnUtils.getAppCfg(appName);
        AppInfo appInfo = cache.getOrElse(appCfg.name,
                () -> CmnUtils.getAppInfoFromJenkins(ws, appCfg, hubCfg.getJenkinsUrl()), hubCfg.getCacheTimeOut());
        return appInfo;
    }

    /**
     *
     * @param groupName
     * @return
     */
    public List<AppInfo> getAppInfoGroup(String groupName) {
        return getAppsInGroup(groupName).stream()
                .map(appName -> getAppInfo(appName))
                .collect(Collectors.toList());
    }

    /**
     * Makes a WS call, therefore this is going to return a future.
     *
     * @param appName
     * @param buildId
     * @return
     */
    public CompletionStage<JsonNode> getBuildInfoFuture(String appName, String buildId) {
        AppCfg appCfg = CmnUtils.getAppCfg(appName);
        // get the json for a specific build
        String buildInfoUrl = hubCfg.getJenkinsUrl() + "/" + appCfg.url + "/" + buildId + CmnUtils.JENKINS_API;
        logger.debug("Calling: {}", buildInfoUrl);
        return ws.url(buildInfoUrl).get().thenApply(WSResponse::asJson);
    }

    /**
     *
     * @param groupName
     * @return
     */
    public List<String> getAppsInGroup(String groupName) {
        return CmnUtils.getAppCfgMap().values().stream()
                .filter(filterAppCfg -> filterAppCfg.group.equals(groupName))
                .map(appCfg -> appCfg.name)
                .collect(Collectors.toList());
    }
}
