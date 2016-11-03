/**
 * The application component. Renders the information for an application. It gets its content via AJAX.
 */
var AppPage = React.createClass({

    //getInitialState() {
    //    return null;
    //},

    componentDidMount() {
        this.serverRequest = $.get(this.props.source, function (result) {
            //console.trace('[AppPage] got response: ' + JSON.stringify(result, null, 2));
            this.setState({appInfo: result});
            console.log('[AppPage] got response for app: ' + this.state.appInfo.name);
        }.bind(this));
    },

    componentWillUnmount() {
        console.log('[AppPage] aborting ajax request...')
        this.serverRequest.abort();
    },

    render() {
        console.trace("[AppPage] render()");
        // only render the content if the AJAX call has returned. This is to avoid initialization of the
        // objects used in the rendering.
        if (!this.state) {
            return null;
        }

        // shortcuts
        var appInfo = this.state.appInfo;
        var appCfg = this.state.appInfo.appCfg;

        // WTF: there is no if in JSX so I have to do this crap here.
        var testServer;
        if (appCfg.testServer) {
            testServer =
                <a className="btn-floating waves-effect waves-light" title="Test Server" href="@app.appCfg.testServer"
                   target="_blank"><i className="material-icons">launch</i></a>
        }

        var divDescription;
        if (appCfg.description) {
            divDescription =
                <div className="row">
                    <div className="col s12"><i>Description:</i> {appCfg.description} </div>
                </div>
        }

        // WTF: inline style must be defined as a JSON outside the JSX
        var myInlineStyle = {"backgroundColor": "darkcyan"};

        var lastFailedBuild;
        if (appInfo.lastFailedBuild != 0 && appInfo.lastBuildId != appInfo.lastFailedBuild) {
            lastFailedBuild = <BuildCard appInfo={appInfo} title="Last Failed Build" buildId={appInfo.lastFailedBuild}/>
        }

        var lastSuccessfulBuild;
        if (appInfo.lastSuccessfulBuild != 0 && appInfo.lastBuildId != appInfo.lastSuccessfulBuild) {
            lastSuccessfulBuild =
                <BuildCard appInfo={appInfo} title="Last Successful Build" buildId={appInfo.lastSuccessfulBuild}/>
        }

        return (
            <div>
                {/*WTF: must enclose the entire render() in one div of React balks. */}
                <ul id="slide-out" className="side-nav">
                    <li>
                        <div className="userView">
                            <img className="background" style={myInlineStyle} width="300" height="200"/>
                            <a href="#!name"><span className="white-text name">Messages(app.group + ".projects")</span></a>
                        </div>
                    </li>
                    {/*
                     @for(item <- appsInSameGroup) {
                     <li><a href="@item">@item</a></li>
                     }*/}
                </ul>
                <div className="container">
                    <div className="row">
                        <div className="col s10">
                            <h3><a href={appInfo.url}>{appInfo.name}</a>
                                <span><a href={appInfo.jenkinsUrl + "/" + appInfo.lastBuildId}><img
                                    src={appInfo.buildBadge}/></a></span>
                                <span><img src={makeBadgeUrl('latest', appInfo.latestVersion, 'blue')}/></span>
                                <span><img src={makeBadgeUrl('release', appInfo.releaseVersion, 'blue')}/></span>
                            </h3>
                        </div>
                        <div className="col s2 app-title right-align">
                            {testServer}
                            <a href="#" className="btn-floating button-collapse" data-activates="slide-out">
                                <i className="material-icons">list</i></a>
                        </div>
                    </div>

                    {divDescription}

                    <div className="row">
                        <div className="col s12">
                            <BuildCard appInfo={appInfo} title="Last Build" buildId={appInfo.lastBuildId}/>

                            {lastFailedBuild}

                            {lastSuccessfulBuild}
                        </div>
                    </div>

                    {console.trace("[AppPage] render() - DONE!")}
                </div>
            </div>
        );
    }
});

/**
 * The build card component. Gets its content via AJAX.
 */
var BuildCard = React.createClass({

    componentDidMount() {
        var buildInfoSource = "/api/build/" + this.props.appInfo.name + "/" + this.props.buildId;
        this.serverRequest = $.get(buildInfoSource, function (result) {
            //console.trace('[BuildCard] got response: ' + JSON.stringify(result, null, 2));
            this.setState({buildInfo: result});
            console.log('[BuildCard] got response for build : ' + this.props.buildId);
        }.bind(this));
    },

    componentWillUnmount() {
        console.log('[BuildCard] aborting ajax request...')
        this.serverRequest.abort();
    },

    render() {
        if (!this.state) {
            return null;
        }
        var app = this.props.appInfo;
        var buildInfo = this.state.buildInfo;
        var sonarUrl = "TODO-getSonarUrl";

        var healthReportBuild;
        if (app.healthReportBuild) {
            healthReportBuild =
                <StatsCard score={app.healthReportBuild.score} description={app.healthReportBuild.description}/>
        }

        var healthReportTest;
        if (app.healthReportTest) {
            healthReportTest =
                <StatsCard score={app.healthReportTest.score} description={app.healthReportTest.description}/>
        }

        return (
            <div className="card grey lighten-4">
                <div className="card-content">
                    <span className="card-title">{this.props.title}: {this.props.buildId}
                        &nbsp;&nbsp;&nbsp;
                        <a href={app.jenkinsUrl + "/" + this.props.buildId}>
                            <img src={app.jenkinsUrl + "/" + this.props.buildId + "/badge/icon"}/>
                        </a>
                    </span>

                    <div className="build-details" id={this.props.buildId}>
                        <div className="card-stacked">
                            <span className="col s12"><b>Build Name:</b> {buildInfo.displayName}</span>
                        </div>
                        <div className="card-stacked">
                            <span className="col s1"><b>Jenkins:</b></span>
                            <span><a href={app.jenkinsUrl + "/" + this.props.buildId + "/testReport"} target="_blank">Test
                                Report</a></span>
                            <span>&nbsp;|&nbsp;<a href={app.jenkinsUrl +"/" + this.props.buildId + "/console"}
                                                  target="_blank">Build Log</a></span>
                            <span>&nbsp;|&nbsp;<a href={app.jenkinsUrl + "/" + this.props.buildId + "/changes"}
                                                  target="_blank">Changes</a></span>
                        </div>

                        <div className="card-stacked">
                            <span className="col s1"><b>Maven:</b></span>
                            <span><a target="_blank"
                                     href={"/ext/" + app.group + "/" + app.name + "/" + app.latestVersion + "/index.html"}>About</a></span>
                            <span>&nbsp;|&nbsp;<a target="_blank"
                                                  href={"/ext/" + app.group + "/" + app.name + "/" + app.latestVersion + "/dependencies.html"}>Dependencies</a></span>
                            <span>&nbsp;|&nbsp;<a target="_blank"
                                                  href={"/ext/" + app.group + "/" + app.name + "/" + app.latestVersion + "/apidocs/index.html"}>JavaDocs</a></span>
                        </div>

                        <div className="card-stacked">
                            <span className="col s1"><b>Sonar:</b></span>
                            <span><a href="@sonarUrl/dashboard/index/@app.sonarId" target="_blank">Dashboard</a></span>
                            <span>&nbsp;|&nbsp;<a href={sonarUrl + "/dashboard/index/" + app.sonarId + "?did=2"}
                                                  target="_blank">Hotspots</a></span>
                            <span>&nbsp;|&nbsp;<a href={sonarUrl + "/dashboard/index/" + app.sonarId + "?did=3"}
                                                  target="_blank">Issues</a></span>
                        </div>

                        <div className="card-stacked">
                            <span className="col s12"><b>Build Score:</b></span><br/>
                            <span>&nbsp;</span>
                            {healthReportBuild}
                        </div>

                        <div className="card-stacked"><span>&nbsp;</span>
                            {healthReportTest}
                        </div>
                        <div className="card-stacked">
                            <span><b>Duration:</b> {moment.duration(buildInfo.duration).humanize()} (estimated:
                                {moment.duration(buildInfo.estimatedDuration).humanize()})</span>
                        </div>
                        <div className="card-stacked">
                            <span><b>Branch:</b> { JSON.stringify(find(buildInfo.actions, "lastBuiltRevision").branch, null, 2)}</span>
                        </div>
                        <div className="card-stacked">
                            <span><b>Git:</b> { JSON.stringify(find(buildInfo.actions, "remoteUrls"), null, 2)}</span>
                        </div>

                    </div>
                </div>
                {console.trace("[BuildCard] render() - DONE!")}
            </div>
        )
    }

});
