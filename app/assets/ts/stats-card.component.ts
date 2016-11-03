/**
 * Created by cristiand on 11/2/2016.
 */

import {Component, OnInit, Input} from "@angular/core";
import {HealthReport} from "./models";
import {HTML_TEMPLATE} from "./constants";

@Component({
    selector: 'stats-card',
    templateUrl: HTML_TEMPLATE + 'stats-card.component.html'
})

export class StatsCardComponent implements OnInit {

    color: string;
    @Input() healthReport: HealthReport;

    ngOnInit() {
        if (this.healthReport.score == 100) this.color = "green-text";
        else if (this.healthReport.score >= 80) this.color = "blue-text";
        else if (this.healthReport.score < 50) this.color = "red-text";
    }

}