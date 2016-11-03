package utils;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * @version $Revision: 1.1.2.1 $
 * @author $Author: cristiand $
 * @since $Date: 2013/02/07 20:29:23 $
 */
public class Menu implements Serializable {

    private static final long serialVersionUID = -4415722317052916860L;
    public String tag;
    public String url;
    public String target;
    public boolean showIfAuthenticated = true; // show the menu only for authenticated users
    public boolean translateTag = true; // Whether or not to localize the tag value for the element.
    public String showIf;   // show if specific condition is satisfied (logic to be implemented elsewhere)
    public LinkedList<Menu> items;

    public Menu getMenu(String tag) {
        if (items == null) return null;
        for (Menu item : items) {
            if (item.tag.equals(tag)) {
                return item;
            }
        }
        return null;
    }

    public Menu getMenu(String tag1, String tag2) {
        Menu item = getMenu(tag1);
        return item == null ? null : item.getMenu(tag2);
    }

    public Menu getMenu(String tag1, String tag2, String tag3) {
        Menu item = getMenu(tag1);
        Menu subItem = item == null ? null : item.getMenu(tag2);
        return subItem == null ? null : subItem.getMenu(tag3);
    }

    @Override
    public String toString() {
        return "{tag:'" + tag + "',showIfAuthenticated:" + showIfAuthenticated +
                (url == null ? "" : ",url:'" + url + "'") +
                (showIf == null ? "" : ",showIf:'" + showIf + "'") +
                ",items:" + items + "}";
    }

}
