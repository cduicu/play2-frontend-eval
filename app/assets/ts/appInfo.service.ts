/**
 * Created by cristiand on 11/1/2016.
 */

import {Injectable} from "@angular/core";
import {AppInfo} from "./models";
import {Http, Response} from "@angular/http";
import {MOCK_APPINFO} from "./mocks";
import "rxjs/add/operator/toPromise";
import {MOCK_URL, API_URL} from "./constants"; // needed for http stuff


@Injectable()
export class AppInfoService {

    // this connects to the inmem db. Path is the name of the directory where the db is.
    url:string = API_URL;

    constructor(private http: Http) {
        //this.url = MOCK_URL; // overwrite to mock
    }

    getAppInfo(appName: string): Promise<AppInfo> {
        var url = this.url + "/" + appName;
        console.log("[AppInfoService] - getAppInfo() " + url);
        // return this.http.get(url)
        //     .toPromise()
        //     .then(response => response.json().data as AppInfo)
        //     .catch(this.handleError);
        return Promise.resolve(MOCK_APPINFO);
    }

    private handleError(error: Response | any) {
        // In a real world app, we might use a remote logging infrastructure
        let errMsg: string;
        if (error instanceof Response) {
            const body = error.json() || '';
            const err = body.error || JSON.stringify(body);
            errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
        } else {
            errMsg = error.message ? error.message : error.toString();
        }
        console.error(errMsg);
        return Promise.reject(errMsg);
    }

}
