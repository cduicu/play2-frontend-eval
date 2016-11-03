var AppGroup = React.createClass({
    render: function () {
        return (
            <div className="container">
                <div className="row">
                    {this.props.appsInGroup.map((app) => (
                        // WTF: key is required but cannot be accessed from within the child
                        <AppCard key={app} app={app}/>
                    ))}
                </div>
            </div>
        );
    }
});

var AppCard = React.createClass({

    componentDidMount() {
        this.serverRequest = $.get("/api/app/" + this.props.app, function (result) {
            this.setState({appInfo: result});
            console.log('[AppCard] got response for build : ' + this.props.buildId);
        }.bind(this));
    },

    componentWillUnmount() {
        console.log('[AppCard] aborting ajax request...')
        this.serverRequest.abort();
    },

    render() {
        if (!this.state) {
            return null;
        }

        // shortcuts
        var app = this.state.appInfo;
        var appCfg = this.state.appInfo.appCfg;

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
            <div className="col s6 m6">
                <div className="card grey lighten-3 hoverable">
                    <div className="card-content">
                        <div className="row">
                            <span className="col s10 card-title">
                                <a href={app.url}>{app.name}</a>&nbsp;&nbsp;
                                <a href={app.jenkinsUrl + "/" + app.lastBuildId}><img src={app.buildBadge}/></a>
                            </span>
                            <span className="col s2"> {testServer} </span>
                        </div>

                        <div className="row">
                            {divDescription}

                            <div className="card-stacked">
                                <span><i>Known Versions:</i></span>
                                <span><img src={makeBadgeUrl('latest', app.latestVersion, 'blue')}/></span>
                                <span><img src={makeBadgeUrl('release', app.releaseVersion, 'blue')}/></span>
                            </div>
                            <div className="card-stacked">
                                <i>Build Score</i><br/><span>&nbsp;</span>
                                {healthReportBuild}
                            </div>

                            <div className="card-stacked"><span>&nbsp;</span>
                                {healthReportTest}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
});
//ReactDOM.render(<AppGroup url="/api/group/foundation/list"/>,document.getElementById('appGroup'));