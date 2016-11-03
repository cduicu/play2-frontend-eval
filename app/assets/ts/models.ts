/**
 * Created by cristiand on 11/2/2016.
 */

export class AppInfo {
    name: string;
    group: string;
    latestVersion: string;
    releaseVersion: string;
    jenkinsUrl: string;
    lastBuildId: number;
    lastFailedBuild: number;
    lastSuccessfulBuild: number;
    lastUnsuccessfulBuild: number;
    sonarId: string;

    healthReportBuild: HealthReport;
    healthReportTest: HealthReport;
    lastCompletedBuild: number;
    lastStableBuild: number;
    appCfg: AppCfg;

    url: string;
    lastBuildStatus: string;
    lastBuildColor: string;
    buildBadge: string;
}

export class AppCfg {
    group: string;
    name: string;
    latestVersion: string;
    releaseVersion: string;
    url: string;
    sonarId: string;
    testServer: string;
    description: string;
}

export class HealthReport {
    score: number;
    description: string;
}

