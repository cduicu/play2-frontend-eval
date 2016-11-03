/**
 * Created by cristiand on 10/21/2016.
 */

function makeBadgeUrl(text, value, color) {
    return "https://img.shields.io/badge/" + text.replace("-", "--") + "-" +
           value.replace("-", "--") + "-" + color + ".svg";
}

/**
 * Finds a property in a JSON object recursively.
 *
 * @param object - the object searched.
 * @param name - the name of the property searched.
 * @returns {*}
 */
function find(object, name) {
    if (name in object) return object[name];
    for (key in object) {
        if ((typeof (object[key])) == 'object') {
            var t = find(object[key], name);
            if (t) return t;
        }
    }
    return null;
}