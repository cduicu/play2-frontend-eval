package services;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import models.AppCfg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Environment;
import play.inject.ApplicationLifecycle;
import utils.CmnUtils;
import utils.Menu;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Created by cristiand on 10/13/2016.
 */
@Singleton
public class BuildHubConfiguration {

    private static Logger logger = LoggerFactory.getLogger(BuildHubConfiguration.class);
    private static final String CONF_FILE = "buildHub.conf";
    private static final String CFG_MENU = "appMenuCfg";
    private static final String CFG_APP = "hubCfg";
    private Menu menu;
    private Config conf;
    private JsonNode hubCfg;
    private final Environment env;
//    private List<AppInfo> appInfoList = new ArrayList<>();
    private Map<String, AppCfg> appCfgList = new HashMap<>();
    private int timeOut = -1;
    private String jenkinsUrl;

    @Inject
    public BuildHubConfiguration(ApplicationLifecycle lifecycle, Environment env) {
        logger.info("Initializing application ...");
        this.env = env;

        initConfig();
        //initAppInfo();

        // registering shutdown hook
        lifecycle.addStopHook(() -> {
            logger.info("Stopping the application...");
            return CompletableFuture.completedFuture(null);
        });

        logger.info("Application initialized!");
    }

//    private void initAppInfo() {
//        String jenkinsUrl = hubCfg.get("jenkinsUrl").asText();
//
//        // creating standalone wsclient
//        AsyncHttpClientConfig config = new DefaultAsyncHttpClientConfig.Builder()
//                .setMaxRequestRetry(0)
//                .setShutdownQuietPeriod(0)
//                .setShutdownTimeout(0).build();
//        ActorSystem system = ActorSystem.create("wsclient");
//        ActorMaterializerSettings settings = ActorMaterializerSettings.create(system);
//        ActorMaterializer materializer = ActorMaterializer.create(settings, system, system.name());
//        WSClient ws = new AhcWSClient(config, materializer);
//        appCfgList.values().stream()
//                .forEach(appCfg -> appInfoList.add(CmnUtils.getAppInfoFromJenkins(ws, appCfg, jenkinsUrl)));
//        logger.debug("AppInfo list loaded!");
//        // make sure to close the wsclient
//        try {
//            ws.close();
//        } catch (IOException e) {
//            logger.error("Failed closing the wsclient!", e);
//        }
//    }

    public void initConfig() {
        logger.debug("Loading configuration from {} ...", CONF_FILE);
        try {
            this.conf = ConfigFactory.parseFile(env.getFile("/conf/" + CONF_FILE));
        } catch (Exception e) {
            logger.error("Can't load configuration!!!", e);
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        ConfigRenderOptions r = ConfigRenderOptions.concise().setJson(true);

        logger.trace("Loading appconfig ...");
        try {
            this.hubCfg = mapper.readTree(conf.getObject(CFG_APP).render(r));
            logger.debug("Hub configuration loaded!");
            logger.trace("HubConfig: {}", hubCfg);

            for (JsonNode node : hubCfg.findValue(CmnUtils.APPS_JSON)) {
                AppCfg appCfg = mapper.treeToValue(node, AppCfg.class);
                appCfgList.put(appCfg.name, appCfg);
            }
            logger.trace("Applications: {}", appCfgList);
        } catch (Exception e) {
            logger.error("Can't load application configuration", e);
        }

        logger.trace("Loading menu ...");
        try {
            this.menu = mapper.readValue(conf.getObject(CFG_MENU).render(r), Menu.class);
            logger.debug("Menu configuration loaded!");
        } catch (Exception e) {
            logger.error("Can't load menu configuration", e);
        }
        logger.trace("Menu: {}", menu);
    }

    public Menu getMenu() {
        return this.menu;
    }

    public JsonNode getHubCfg() {
        return hubCfg;
    }

    public Map<String, AppCfg> getAppCfgList() {
        return appCfgList;
    }

    public int getCacheTimeOut() {
        if (timeOut == -1) {
            timeOut = hubCfg.findValue("cacheTimeOut").asInt();
        }
        return timeOut;
    }

    public String getJenkinsUrl() {
        if (jenkinsUrl == null) {
            jenkinsUrl = hubCfg.get("jenkinsUrl").asText();
        }
        return jenkinsUrl;
    }

}
