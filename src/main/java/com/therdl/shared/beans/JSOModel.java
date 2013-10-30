package com.therdl.shared.beans;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;


/**
 * this class allows flexibility in parsing javascript arrays and object when they contain json data
 * allows map like get and set operations in GWT on the JSNI layer
 * encapsulates the boilerplate JSNI to keep the java layer
 * cleaner and more readable
 * <p/>
 * this class extends the GWT JavaScriptObject, the basic rational for this class is the
 * limitation of the JavaScriptObject class, that is that the GWT JavaScriptObject cannot be created
 * directly and should be declared as the return type of a JSNI method. This  JSOModel class
 * allows you to map between json formatted strings returned from a server to a
 * 1. JavaScriptObject
 * 2. JsonObject
 * 3. JsonArray
 * <p/>
 * so it is a better fit for a
 * model view design pattern, for example anywhere where you need a grid display
 * backed up by json data
 */

public class JSOModel extends JavaScriptObject {
    /**
     * Overlay types always have protected, zero-arg constructors
     */

    protected JSOModel() {
    }

    /**
     * Create an empty instance.
     *
     * @return new Object
     */
    public static native JSOModel create() /*-{
        return new Object();
    }-*/;

    /**
     * Convert a JSON encoded string into a JSOModel instance.
     * <p/>
     * Expects a JSON string structured like '{"foo":"bar","number":123}'
     *
     * @return a populated JSOModel object
     */
    public static native JSOModel fromJson(String jsonString) /*-{
        return eval('(' + jsonString + ')');
    }-*/;

    /**
     * Convert a JSON encoded string into an array of JSOModel instance.
     * <p/>
     * Expects a JSON string structured like '[{"foo":"bar","number":123}, {...}]'
     *
     * @return a populated JsArray
     */
    public static native JsArray<JSOModel> arrayFromJson(String jsonString) /*-{
        return eval('(' + jsonString + ')');
    }-*/;


    /**
     * javascript objects are key value maps, this method tests that a key exits
     * used before calling get("<key>")
     *
     * @param key
     * @return
     */
    public final native boolean hasKey(String key) /*-{
        return this[key] != undefined;
    }-*/;

    /**
     * javascript objects are key value maps, this method retruns all the
     * key values as a JSNI array
     * often used to match ona key value
     *
     * @param key
     * @return
     */
    public final native JsArrayString keys() /*-{
        var a = new Array();
        for (var p in this) { a.push(p); }
        return a;
    }-*/;

    /**
     * returns the keys for this object as a java.util.Set
     *
     * @return
     */

    @Deprecated
    public final Set<String> keySet() {
        JsArrayString array = keys();
        Set<String> set = new HashSet<String>();
        for (int i = 0; i < array.length(); i++) {
            set.add(array.get(i));
        }
        return set;
    }

    // only standard getters and setters below

    public final native String get(String key) /*-{
        return "" + this[key];
    }-*/;

    public final native String get(String key, String defaultValue) /*-{
        return this[key] ? ("" + this[key]) : defaultValue;
    }-*/;

    public final native void set(String key, String value) /*-{
        this[key] = value;
    }-*/;

    public final int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public final boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public final native JSOModel getObject(String key) /*-{
        return this[key];
    }-*/;

    public final native JsArray<JSOModel> getArray(String key) /*-{
        return this[key] ? this[key] : new Array();
    }-*/;

}
