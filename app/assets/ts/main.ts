import { platformBrowserDynamic } from "@angular/platform-browser-dynamic"
import {BhubModule} from "./bhub.module"

/* This is the entry point in the n2 application.
   ng2 is a modular platform. It requires to know what module to launch first. */
const platform = platformBrowserDynamic()
platform.bootstrapModule(BhubModule)