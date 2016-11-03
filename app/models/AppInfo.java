package models;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CmnUtils;
import utils.JsonUtils;

/**
 * Created by cristiand on 10/12/2016.
 */
public class AppInfo {

    private static Logger logger = LoggerFactory.getLogger(AppInfo.class);

    public String name;
    public String group;
    public String latestVersion;
    public String releaseVersion;
    public String jenkinsUrl;
    public int lastBuildId;
    public int lastFailedBuild;
    public int lastSuccessfulBuild;
    public int lastUnsuccessfulBuild;
    public String sonarId;

    public HealthReport healthReportBuild;
    public HealthReport healthReportTest;
    public int lastCompletedBuild;
    public int lastStableBuild;
    public AppCfg appCfg;

    public static AppInfo build(AppCfg appCfg, String url, JsonNode jenkinsNode) {
        AppInfo app = new AppInfo();
        app.jenkinsUrl = url;

        app.appCfg = appCfg; // TODO - remove what follows
        app.name = appCfg.name;
        app.group = appCfg.group;
        app.sonarId = appCfg.sonarId;
        app.latestVersion = appCfg.latestVersion;
        app.releaseVersion = appCfg.releaseVersion;

        JsonNode node = jenkinsNode.get("lastBuild");
        if (node != null && node.get("number") != null) {
            app.lastBuildId = node.get("number").asInt();
        }
        node = jenkinsNode.get("lastFailedBuild");
        if (node != null && node.get("number") != null) {
            app.lastFailedBuild = node.get("number").asInt();
        }
        node = jenkinsNode.get("lastCompletedBuild");
        if (node != null && node.get("number") != null) {
            app.lastCompletedBuild = node.get("number").asInt();
        }
        node = jenkinsNode.get("lastStableBuild");
        if (node != null && node.get("number") != null) {
            app.lastStableBuild = node.get("number").asInt();
        }
        node = jenkinsNode.get("lastSuccessfulBuild");
        if (node != null && node.get("number") != null) {
            app.lastSuccessfulBuild = node.get("number").asInt();
        }
        node = jenkinsNode.get("lastUnsuccessfulBuild");
        if (node != null && node.get("number") != null) {
            app.lastUnsuccessfulBuild = node.get("number").asInt();
        }
        node = jenkinsNode.get("healthReport");
        if (node != null) {
            JsonNode jsonNode = node.get(0);
            app.healthReportBuild = new HealthReport();
            app.healthReportBuild.score = jsonNode.get("score").asInt();
            app.healthReportBuild.description = jsonNode.get("description").asText();
            if (node.size() > 1) {
                jsonNode = node.get(1);
                app.healthReportTest = new HealthReport();
                app.healthReportTest.score = jsonNode.get("score").asInt();
                app.healthReportTest.description = jsonNode.get("description").asText();
            }
        }
        //app.url = getUrl(app.tag);

        logger.trace("appInfo: {}", JsonUtils.toString(app));
        return app;
    }

    public String getBuildBadge() {
        return CmnUtils.makeBadgeUrl(String.valueOf(lastBuildId), getLastBuildStatus(),
                getLastBuildColor());
    }

    public String getBuildBadgeUrl(int buildId) {
        return jenkinsUrl + buildId;
    }


    public String getLastBuildStatus() {
        if (lastBuildId == lastSuccessfulBuild) return "passing";
        if (lastBuildId == lastFailedBuild) return "failed";
        return "unknown";
    }

    public String getLastBuildColor() {
        if (lastBuildId == lastSuccessfulBuild) return "green";
        if (lastBuildId == lastFailedBuild) return "red";
        return "lightgray";
    }

    public String getUrl() {
        return group + "/" + name;
    }

    public static class HealthReport {
        public int score;
        public String description;
    }
}
