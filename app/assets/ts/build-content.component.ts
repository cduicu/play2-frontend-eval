/**
 * Created by cristiand on 11/3/2016.
 */
import {Component, Injectable, Input, OnInit} from "@angular/core";
import {AppInfoService} from "./appInfo.service";
import {HTML_TEMPLATE} from "./constants";
import {AppInfo} from "./models";

@Component({
    selector: 'build-content',
    templateUrl: HTML_TEMPLATE + 'build-content.component.html'
})

@Injectable()
export class BuildContentComponent implements OnInit {

    @Input() id: number;
    @Input() appInfo: AppInfo;
    sonarUrl:string = "TODO";

    constructor(private appInfoService: AppInfoService) {
    }

    ngOnInit() {
        console.debug("[BuildContentComponent] - init. App=" + this.appInfo.name + "; BuildId=", this.id);
        //this.appInfoService.getAppInfo("hframe").then(o => this.appInfo = o);
        // TODO: get build content
    }
}