
/**
 * The content of the build card.
 */
var StatsCard = React.createClass({
    render() {
        var color = "";
        if (this.props.score  == 100) color = "green-text";
        if (this.props.score >= 80) color = "blue-text";
        if (this.props.score < 50) color = "red-text";

        return (
            <div>
                <span className="stats-score {color}">{this.props.score}</span>
                <span>{this.props.description}</span>
            </div>
        )
    }
});
