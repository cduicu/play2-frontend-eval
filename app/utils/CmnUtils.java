package utils;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.WebJarAssets;
import models.AppCfg;
import models.AppInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Application;
import play.Environment;
import play.api.Play;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import services.BuildHubConfiguration;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.CompletionStage;

/**
 * Utility methods. Play 2.5 has moved to using Guice depy injection. But the templates do not have a way to be injected
 * yet. This provides an easy (albeit dirty) way to access the injected singletons in the application.
 *
 * Created by cristiand on 10/12/2016.
 */
public class CmnUtils {

    private static Logger logger = LoggerFactory.getLogger(CmnUtils.class);
    public static final String SELECTED_MENU = "selectedMenu";
    public static final String JENKINS_API = "/api/json";
    public static final String APPS_JSON = "applications";

    public static WebJarAssets getWebJarAssets() {
        return Play.current().injector().instanceOf(WebJarAssets.class);
    }

    public static Environment getPlayEnvironment() {
        return Play.current().injector().instanceOf(Environment.class);
    }

    public static Application getPlayApplication() {
        return Play.current().injector().instanceOf(Application.class);
    }

    public static JsonNode getHubCfg() {
        return Play.current().injector().instanceOf(BuildHubConfiguration.class).getHubCfg();
    }

    public static Menu getMenu() {
        return Play.current().injector().instanceOf(BuildHubConfiguration.class).getMenu();
    }

    public static String makeBadgeUrl(String text, String value, String color) {
        return "https://img.shields.io/badge/" + text.replace("-", "--") + "-" +
                value.replace("-", "--") + "-" + color + ".svg";
    }

    public static String getScoreColor(int score) {
        if (score == 100) return "green-text";
        if (score >= 80) return "blue-text";
        if (score < 50) return "red-text";
        return "";
    }

    public static Map<String, AppCfg> getAppCfgMap() {
        return Play.current().injector().instanceOf(BuildHubConfiguration.class).getAppCfgList();
    }

    public static AppCfg getAppCfg(String name) {
        Map<String, AppCfg> map = Play.current().injector().instanceOf(BuildHubConfiguration.class).getAppCfgList();
        return map.get(name);
    }

    public static String getDuration(long duration) {
        return new HumanTime(duration).getExactly();
    }

    public static String getTimeDiff(Date start, Date end) {
        if (start == null) return "N/A";
        if (end == null) end = new Date();
        long diff = end.getTime() - start.getTime();
        if (diff < 100) {
            return "< 1 ms";
        }
        return new HumanTime(diff).getApproximately();
    }

    public static AppInfo getAppInfoFromJenkins(WSClient wsClient, AppCfg appCfg, String jenkinsUrl) {
        String url = jenkinsUrl + "/" + appCfg.url;
        String apiUrl = url + CmnUtils.JENKINS_API;
        logger.debug("GET {}", apiUrl);
        CompletionStage<AppInfo> res = wsClient.url(apiUrl).get()
                .exceptionally(throwable -> {
                    logger.error("Failed connecting to: " + apiUrl, throwable);
                    return null;
                })
                // format result as json
                .thenApply(WSResponse::asJson)
                        // parse json into AppInfo object
                .thenApply(jenkinsNode -> AppInfo.build(appCfg, url, jenkinsNode));
        return res.toCompletableFuture().join();
    }

}
