import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {HttpModule} from "@angular/http";
import {AppComponent} from "./app.component";
import {AppInfoService} from "./appInfo.service";
import {StatsCardComponent} from "./stats-card.component";
import {BuildCardComponent} from "./build-card.component";
import {BuildContentComponent} from "./build-content.component";

// Imports for loading & configuring the in-memory web api
// import { InMemoryWebApiModule } from 'angular-in-memory-web-api';
// import { InMemoryDataService }  from './in-memory-data.service';

/* The module puts together the components of the application. */
@NgModule({
    imports: [
        BrowserModule,
        HttpModule
        //InMemoryWebApiModule.forRoot(InMemoryDataService)
    ],
    declarations: [AppComponent, BuildCardComponent, BuildContentComponent, StatsCardComponent],
    bootstrap: [AppComponent],
    providers: [AppInfoService]
})

export class BhubModule {
}
