package controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Controller;
import play.mvc.Result;
import utils.CmnUtils;

import java.io.File;

/**
 * Created by cristiand on 10/12/2016.
 */
public class ExtAssets extends Controller {

    private static Logger logger = LoggerFactory.getLogger(ExtAssets.class);

    public Result at(Assets.Asset filePath) {
        String fullPath = CmnUtils.getHubCfg().get("mavenSitePath").asText() + filePath.name();
        logger.trace("File: {}", fullPath);
        File file = new File(fullPath);
        return ok(file, true);
    }

}
