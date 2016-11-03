/**
 * Created by cristiand on 11/2/2016.
 */
import {Component, Injectable, Input, OnInit} from "@angular/core";
import {AppInfo} from "./models";
import {HTML_TEMPLATE} from "./constants";

@Component({
    selector: 'build-card',
    templateUrl: HTML_TEMPLATE + 'build-card.component.html'
})

@Injectable()
export class BuildCardComponent implements OnInit {

    @Input() appInfo: AppInfo;
    @Input() title: string;
    @Input() buildId: number;

    ngOnInit() {
        console.debug("[BuildCardComponent] - init. " + this.appInfo.name + "(", this.buildId + ") title=" + this.title);
    }

    public makeBadgeUrl(text: string, value: string, color: string): string {
        return "https://img.shields.io/badge/" + text.replace("-", "--") + "-" +
            value.replace("-", "--") + "-" + color + ".svg";
    }

}
