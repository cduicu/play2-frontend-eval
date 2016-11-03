/**
 * Created by cristiand on 11/2/2016.
 */

import {AppInfo} from "./models";

export const MOCK_APPINFO: AppInfo =
{
    name: "hframe",
    group: "foundation",
    latestVersion: "3.0.2-SNAPSHOT",
    releaseVersion: "3.0.1",
    jenkinsUrl: "http://torvm-bld01lx:8080/view/COMMON/job/COMMON_HFRAME",
    lastBuildId: 155,
    lastFailedBuild: 154,
    lastSuccessfulBuild: 155,
    lastUnsuccessfulBuild: 154,
    sonarId: "32877",
    healthReportBuild: {
        score: 80,
        description: "Build stability: 1 out of the last 5 builds failed."
    },
    healthReportTest: {
        score: 100,
        description: "Test Result: 0 tests failing out of a total of 96 tests."
    },
    lastCompletedBuild: 155,
    lastStableBuild: 155,
    appCfg: {
        group: "foundation",
        name: "hframe",
        latestVersion: "3.0.2-SNAPSHOT",
        releaseVersion: "3.0.1",
        url: "COMMON/job/COMMON_HFRAME",
        sonarId: "32877",
        testServer: null,
        description: "Java Horizontal Framework Library"
    },
    url: "foundation/hframe",
    lastBuildStatus: "passing",
    lastBuildColor: "green",
    buildBadge: "https://img.shields.io/badge/155-passing-green.svg"
}
