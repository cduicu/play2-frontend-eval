///<reference path="constants.ts"/>
/**
 * Created by cristiand on 11/1/2016.
 */

import {Component, Injectable, Input, OnInit} from "@angular/core";
import {AppInfoService} from "./appInfo.service";
import {AppInfo} from "./models";
import {HTML_TEMPLATE} from "./constants";

@Component({
    selector: 'app-page',
    templateUrl: HTML_TEMPLATE + 'app.component.html'
})

@Injectable()
export class AppComponent implements OnInit {

    @Input() group: string;
    @Input() app: string;
    appInfo: AppInfo;

    constructor(private appInfoService: AppInfoService) {
    }

    ngOnInit() {
        console.debug("[AppComponent] - init. appName=" + this.app);
        this.appInfoService.getAppInfo("hframe").then(o => this.appInfo = o);
    }

    public makeBadgeUrl(text: string, value: string, color: string): string {
        return "https://img.shields.io/badge/" + text.replace("-", "--") + "-" +
            value.replace("-", "--") + "-" + color + ".svg";
    }

}
