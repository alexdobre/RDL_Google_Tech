var COMPILED = !0, goog = goog || {};
goog.global = this;
goog.exportPath_ = function (a, b, c) {
    a = a.split(".");
    c = c || goog.global;
    a[0]in c || !c.execScript || c.execScript("var " + a[0]);
    for (var d; a.length && (d = a.shift());)a.length || void 0 === b ? c = c[d] ? c[d] : c[d] = {} : c[d] = b
};
goog.define = function (a, b) {
    var c = b;
    COMPILED || goog.global.CLOSURE_DEFINES && Object.prototype.hasOwnProperty.call(goog.global.CLOSURE_DEFINES, a) && (c = goog.global.CLOSURE_DEFINES[a]);
    goog.exportPath_(a, c)
};
goog.DEBUG = !0;
goog.LOCALE = "en";
goog.TRUSTED_SITE = !0;
goog.provide = function (a) {
    if (!COMPILED) {
        if (goog.isProvided_(a))throw Error('Namespace "' + a + '" already declared.');
        delete goog.implicitNamespaces_[a];
        for (var b = a; (b = b.substring(0, b.lastIndexOf("."))) && !goog.getObjectByName(b);)goog.implicitNamespaces_[b] = !0
    }
    goog.exportPath_(a)
};
goog.setTestOnly = function (a) {
    if (COMPILED && !goog.DEBUG)throw a = a || "", Error("Importing test-only code into non-debug environment" + a ? ": " + a : ".");
};
COMPILED || (goog.isProvided_ = function (a) {
    return!goog.implicitNamespaces_[a] && !!goog.getObjectByName(a)
}, goog.implicitNamespaces_ = {});
goog.getObjectByName = function (a, b) {
    for (var c = a.split("."), d = b || goog.global, e; e = c.shift();)if (goog.isDefAndNotNull(d[e]))d = d[e]; else return null;
    return d
};
goog.globalize = function (a, b) {
    var c = b || goog.global, d;
    for (d in a)c[d] = a[d]
};
goog.addDependency = function (a, b, c) {
    if (goog.DEPENDENCIES_ENABLED) {
        var d;
        a = a.replace(/\\/g, "/");
        for (var e = goog.dependencies_, f = 0; d = b[f]; f++)e.nameToPath[d] = a, a in e.pathToNames || (e.pathToNames[a] = {}), e.pathToNames[a][d] = !0;
        for (d = 0; b = c[d]; d++)a in e.requires || (e.requires[a] = {}), e.requires[a][b] = !0
    }
};
goog.ENABLE_DEBUG_LOADER = !0;
goog.require = function (a) {
    if (!COMPILED && !goog.isProvided_(a)) {
        if (goog.ENABLE_DEBUG_LOADER) {
            var b = goog.getPathFromDeps_(a);
            if (b) {
                goog.included_[b] = !0;
                goog.writeScripts_();
                return
            }
        }
        a = "goog.require could not find: " + a;
        goog.global.console && goog.global.console.error(a);
        throw Error(a);
    }
};
goog.basePath = "";
goog.nullFunction = function () {
};
goog.identityFunction = function (a, b) {
    return a
};
goog.abstractMethod = function () {
    throw Error("unimplemented abstract method");
};
goog.addSingletonGetter = function (a) {
    a.getInstance = function () {
        if (a.instance_)return a.instance_;
        goog.DEBUG && (goog.instantiatedSingletons_[goog.instantiatedSingletons_.length] = a);
        return a.instance_ = new a
    }
};
goog.instantiatedSingletons_ = [];
goog.DEPENDENCIES_ENABLED = !COMPILED && goog.ENABLE_DEBUG_LOADER;
goog.DEPENDENCIES_ENABLED && (goog.included_ = {}, goog.dependencies_ = {pathToNames: {}, nameToPath: {}, requires: {}, visited: {}, written: {}}, goog.inHtmlDocument_ = function () {
    var a = goog.global.document;
    return"undefined" != typeof a && "write"in a
}, goog.findBasePath_ = function () {
    if (goog.global.CLOSURE_BASE_PATH)goog.basePath = goog.global.CLOSURE_BASE_PATH; else if (goog.inHtmlDocument_())for (var a = goog.global.document.getElementsByTagName("script"), b = a.length - 1; 0 <= b; --b) {
        var c = a[b].src, d = c.lastIndexOf("?"), d = -1 == d ? c.length :
            d;
        if ("base.js" == c.substr(d - 7, 7)) {
            goog.basePath = c.substr(0, d - 7);
            break
        }
    }
}, goog.importScript_ = function (a) {
    var b = goog.global.CLOSURE_IMPORT_SCRIPT || goog.writeScriptTag_;
    !goog.dependencies_.written[a] && b(a) && (goog.dependencies_.written[a] = !0)
}, goog.writeScriptTag_ = function (a) {
    if (goog.inHtmlDocument_()) {
        var b = goog.global.document;
        if ("complete" == b.readyState) {
            if (/\bdeps.js$/.test(a))return!1;
            throw Error('Cannot write "' + a + '" after document load');
        }
        b.write('<script type="text/javascript" src="' + a + '">\x3c/script>');
        return!0
    }
    return!1
}, goog.writeScripts_ = function () {
    function a(e) {
        if (!(e in d.written)) {
            if (!(e in d.visited) && (d.visited[e] = !0, e in d.requires))for (var g in d.requires[e])if (!goog.isProvided_(g))if (g in d.nameToPath)a(d.nameToPath[g]); else throw Error("Undefined nameToPath for " + g);
            e in c || (c[e] = !0, b.push(e))
        }
    }

    var b = [], c = {}, d = goog.dependencies_, e;
    for (e in goog.included_)d.written[e] || a(e);
    for (e = 0; e < b.length; e++)if (b[e])goog.importScript_(goog.basePath + b[e]); else throw Error("Undefined script input");
}, goog.getPathFromDeps_ = function (a) {
    return a in goog.dependencies_.nameToPath ? goog.dependencies_.nameToPath[a] : null
}, goog.findBasePath_(), goog.global.CLOSURE_NO_DEPS || goog.importScript_(goog.basePath + "deps.js"));
goog.typeOf = function (a) {
    var b = typeof a;
    if ("object" == b)if (a) {
        if (a instanceof Array)return"array";
        if (a instanceof Object)return b;
        var c = Object.prototype.toString.call(a);
        if ("[object Window]" == c)return"object";
        if ("[object Array]" == c || "number" == typeof a.length && "undefined" != typeof a.splice && "undefined" != typeof a.propertyIsEnumerable && !a.propertyIsEnumerable("splice"))return"array";
        if ("[object Function]" == c || "undefined" != typeof a.call && "undefined" != typeof a.propertyIsEnumerable && !a.propertyIsEnumerable("call"))return"function"
    } else return"null";
    else if ("function" == b && "undefined" == typeof a.call)return"object";
    return b
};
goog.isDef = function (a) {
    return void 0 !== a
};
goog.isNull = function (a) {
    return null === a
};
goog.isDefAndNotNull = function (a) {
    return null != a
};
goog.isArray = function (a) {
    return"array" == goog.typeOf(a)
};
goog.isArrayLike = function (a) {
    var b = goog.typeOf(a);
    return"array" == b || "object" == b && "number" == typeof a.length
};
goog.isDateLike = function (a) {
    return goog.isObject(a) && "function" == typeof a.getFullYear
};
goog.isString = function (a) {
    return"string" == typeof a
};
goog.isBoolean = function (a) {
    return"boolean" == typeof a
};
goog.isNumber = function (a) {
    return"number" == typeof a
};
goog.isFunction = function (a) {
    return"function" == goog.typeOf(a)
};
goog.isObject = function (a) {
    var b = typeof a;
    return"object" == b && null != a || "function" == b
};
goog.getUid = function (a) {
    return a[goog.UID_PROPERTY_] || (a[goog.UID_PROPERTY_] = ++goog.uidCounter_)
};
goog.removeUid = function (a) {
    "removeAttribute"in a && a.removeAttribute(goog.UID_PROPERTY_);
    try {
        delete a[goog.UID_PROPERTY_]
    } catch (b) {
    }
};
goog.UID_PROPERTY_ = "closure_uid_" + (1E9 * Math.random() >>> 0);
goog.uidCounter_ = 0;
goog.getHashCode = goog.getUid;
goog.removeHashCode = goog.removeUid;
goog.cloneObject = function (a) {
    var b = goog.typeOf(a);
    if ("object" == b || "array" == b) {
        if (a.clone)return a.clone();
        var b = "array" == b ? [] : {}, c;
        for (c in a)b[c] = goog.cloneObject(a[c]);
        return b
    }
    return a
};
goog.bindNative_ = function (a, b, c) {
    return a.call.apply(a.bind, arguments)
};
goog.bindJs_ = function (a, b, c) {
    if (!a)throw Error();
    if (2 < arguments.length) {
        var d = Array.prototype.slice.call(arguments, 2);
        return function () {
            var c = Array.prototype.slice.call(arguments);
            Array.prototype.unshift.apply(c, d);
            return a.apply(b, c)
        }
    }
    return function () {
        return a.apply(b, arguments)
    }
};
goog.bind = function (a, b, c) {
    Function.prototype.bind && -1 != Function.prototype.bind.toString().indexOf("native code") ? goog.bind = goog.bindNative_ : goog.bind = goog.bindJs_;
    return goog.bind.apply(null, arguments)
};
goog.partial = function (a, b) {
    var c = Array.prototype.slice.call(arguments, 1);
    return function () {
        var b = Array.prototype.slice.call(arguments);
        b.unshift.apply(b, c);
        return a.apply(this, b)
    }
};
goog.mixin = function (a, b) {
    for (var c in b)a[c] = b[c]
};
goog.now = goog.TRUSTED_SITE && Date.now || function () {
    return+new Date
};
goog.globalEval = function (a) {
    if (goog.global.execScript)goog.global.execScript(a, "JavaScript"); else if (goog.global.eval)if (null == goog.evalWorksForGlobals_ && (goog.global.eval("var _et_ = 1;"), "undefined" != typeof goog.global._et_ ? (delete goog.global._et_, goog.evalWorksForGlobals_ = !0) : goog.evalWorksForGlobals_ = !1), goog.evalWorksForGlobals_)goog.global.eval(a); else {
        var b = goog.global.document, c = b.createElement("script");
        c.type = "text/javascript";
        c.defer = !1;
        c.appendChild(b.createTextNode(a));
        b.body.appendChild(c);
        b.body.removeChild(c)
    } else throw Error("goog.globalEval not available");
};
goog.evalWorksForGlobals_ = null;
goog.getCssName = function (a, b) {
    var c = function (a) {
        return goog.cssNameMapping_[a] || a
    }, d = function (a) {
        a = a.split("-");
        for (var b = [], d = 0; d < a.length; d++)b.push(c(a[d]));
        return b.join("-")
    }, d = goog.cssNameMapping_ ? "BY_WHOLE" == goog.cssNameMappingStyle_ ? c : d : function (a) {
        return a
    };
    return b ? a + "-" + d(b) : d(a)
};
goog.setCssNameMapping = function (a, b) {
    goog.cssNameMapping_ = a;
    goog.cssNameMappingStyle_ = b
};
!COMPILED && goog.global.CLOSURE_CSS_NAME_MAPPING && (goog.cssNameMapping_ = goog.global.CLOSURE_CSS_NAME_MAPPING);
goog.getMsg = function (a, b) {
    var c = b || {}, d;
    for (d in c) {
        var e = ("" + c[d]).replace(/\$/g, "$$$$");
        a = a.replace(RegExp("\\{\\$" + d + "\\}", "gi"), e)
    }
    return a
};
goog.getMsgWithFallback = function (a, b) {
    return a
};
goog.exportSymbol = function (a, b, c) {
    goog.exportPath_(a, b, c)
};
goog.exportProperty = function (a, b, c) {
    a[b] = c
};
goog.inherits = function (a, b) {
    function c() {
    }

    c.prototype = b.prototype;
    a.superClass_ = b.prototype;
    a.prototype = new c;
    a.prototype.constructor = a
};
goog.base = function (a, b, c) {
    var d = arguments.callee.caller;
    if (goog.DEBUG && !d)throw Error("arguments.caller not defined.  goog.base() expects not to be running in strict mode. See http://www.ecma-international.org/ecma-262/5.1/#sec-C");
    if (d.superClass_)return d.superClass_.constructor.apply(a, Array.prototype.slice.call(arguments, 1));
    for (var e = Array.prototype.slice.call(arguments, 2), f = !1, g = a.constructor; g; g = g.superClass_ && g.superClass_.constructor)if (g.prototype[b] === d)f = !0; else if (f)return g.prototype[b].apply(a,
        e);
    if (a[b] === d)return a.constructor.prototype[b].apply(a, e);
    throw Error("goog.base called from a method of one name to a method of a different name");
};
goog.scope = function (a) {
    a.call(goog.global)
};
goog.debug = {};
goog.debug.Error = function (a) {
    Error.captureStackTrace ? Error.captureStackTrace(this, goog.debug.Error) : this.stack = Error().stack || "";
    a && (this.message = String(a))
};
goog.inherits(goog.debug.Error, Error);
goog.debug.Error.prototype.name = "CustomError";
goog.string = {};
goog.string.Unicode = {NBSP: "\u00a0"};
goog.string.startsWith = function (a, b) {
    return 0 == a.lastIndexOf(b, 0)
};
goog.string.endsWith = function (a, b) {
    var c = a.length - b.length;
    return 0 <= c && a.indexOf(b, c) == c
};
goog.string.caseInsensitiveStartsWith = function (a, b) {
    return 0 == goog.string.caseInsensitiveCompare(b, a.substr(0, b.length))
};
goog.string.caseInsensitiveEndsWith = function (a, b) {
    return 0 == goog.string.caseInsensitiveCompare(b, a.substr(a.length - b.length, b.length))
};
goog.string.caseInsensitiveEquals = function (a, b) {
    return a.toLowerCase() == b.toLowerCase()
};
goog.string.subs = function (a, b) {
    for (var c = a.split("%s"), d = "", e = Array.prototype.slice.call(arguments, 1); e.length && 1 < c.length;)d += c.shift() + e.shift();
    return d + c.join("%s")
};
goog.string.collapseWhitespace = function (a) {
    return a.replace(/[\s\xa0]+/g, " ").replace(/^\s+|\s+$/g, "")
};
goog.string.isEmpty = function (a) {
    return/^[\s\xa0]*$/.test(a)
};
goog.string.isEmptySafe = function (a) {
    return goog.string.isEmpty(goog.string.makeSafe(a))
};
goog.string.isBreakingWhitespace = function (a) {
    return!/[^\t\n\r ]/.test(a)
};
goog.string.isAlpha = function (a) {
    return!/[^a-zA-Z]/.test(a)
};
goog.string.isNumeric = function (a) {
    return!/[^0-9]/.test(a)
};
goog.string.isAlphaNumeric = function (a) {
    return!/[^a-zA-Z0-9]/.test(a)
};
goog.string.isSpace = function (a) {
    return" " == a
};
goog.string.isUnicodeChar = function (a) {
    return 1 == a.length && " " <= a && "~" >= a || "\u0080" <= a && "\ufffd" >= a
};
goog.string.stripNewlines = function (a) {
    return a.replace(/(\r\n|\r|\n)+/g, " ")
};
goog.string.canonicalizeNewlines = function (a) {
    return a.replace(/(\r\n|\r|\n)/g, "\n")
};
goog.string.normalizeWhitespace = function (a) {
    return a.replace(/\xa0|\s/g, " ")
};
goog.string.normalizeSpaces = function (a) {
    return a.replace(/\xa0|[ \t]+/g, " ")
};
goog.string.collapseBreakingSpaces = function (a) {
    return a.replace(/[\t\r\n ]+/g, " ").replace(/^[\t\r\n ]+|[\t\r\n ]+$/g, "")
};
goog.string.trim = function (a) {
    return a.replace(/^[\s\xa0]+|[\s\xa0]+$/g, "")
};
goog.string.trimLeft = function (a) {
    return a.replace(/^[\s\xa0]+/, "")
};
goog.string.trimRight = function (a) {
    return a.replace(/[\s\xa0]+$/, "")
};
goog.string.caseInsensitiveCompare = function (a, b) {
    var c = String(a).toLowerCase(), d = String(b).toLowerCase();
    return c < d ? -1 : c == d ? 0 : 1
};
goog.string.numerateCompareRegExp_ = /(\.\d+)|(\d+)|(\D+)/g;
goog.string.numerateCompare = function (a, b) {
    if (a == b)return 0;
    if (!a)return-1;
    if (!b)return 1;
    for (var c = a.toLowerCase().match(goog.string.numerateCompareRegExp_), d = b.toLowerCase().match(goog.string.numerateCompareRegExp_), e = Math.min(c.length, d.length), f = 0; f < e; f++) {
        var g = c[f], h = d[f];
        if (g != h)return c = parseInt(g, 10), !isNaN(c) && (d = parseInt(h, 10), !isNaN(d) && c - d) ? c - d : g < h ? -1 : 1
    }
    return c.length != d.length ? c.length - d.length : a < b ? -1 : 1
};
goog.string.urlEncode = function (a) {
    return encodeURIComponent(String(a))
};
goog.string.urlDecode = function (a) {
    return decodeURIComponent(a.replace(/\+/g, " "))
};
goog.string.newLineToBr = function (a, b) {
    return a.replace(/(\r\n|\r|\n)/g, b ? "<br />" : "<br>")
};
goog.string.htmlEscape = function (a, b) {
    if (b)return a.replace(goog.string.amperRe_, "&amp;").replace(goog.string.ltRe_, "&lt;").replace(goog.string.gtRe_, "&gt;").replace(goog.string.quotRe_, "&quot;");
    if (!goog.string.allRe_.test(a))return a;
    -1 != a.indexOf("&") && (a = a.replace(goog.string.amperRe_, "&amp;"));
    -1 != a.indexOf("<") && (a = a.replace(goog.string.ltRe_, "&lt;"));
    -1 != a.indexOf(">") && (a = a.replace(goog.string.gtRe_, "&gt;"));
    -1 != a.indexOf('"') && (a = a.replace(goog.string.quotRe_, "&quot;"));
    return a
};
goog.string.amperRe_ = /&/g;
goog.string.ltRe_ = /</g;
goog.string.gtRe_ = />/g;
goog.string.quotRe_ = /\"/g;
goog.string.allRe_ = /[&<>\"]/;
goog.string.unescapeEntities = function (a) {
    return goog.string.contains(a, "&") ? "document"in goog.global ? goog.string.unescapeEntitiesUsingDom_(a) : goog.string.unescapePureXmlEntities_(a) : a
};
goog.string.unescapeEntitiesUsingDom_ = function (a) {
    var b = {"&amp;": "&", "&lt;": "<", "&gt;": ">", "&quot;": '"'}, c = document.createElement("div");
    return a.replace(goog.string.HTML_ENTITY_PATTERN_, function (a, e) {
        var f = b[a];
        if (f)return f;
        if ("#" == e.charAt(0)) {
            var g = Number("0" + e.substr(1));
            isNaN(g) || (f = String.fromCharCode(g))
        }
        f || (c.innerHTML = a + " ", f = c.firstChild.nodeValue.slice(0, -1));
        return b[a] = f
    })
};
goog.string.unescapePureXmlEntities_ = function (a) {
    return a.replace(/&([^;]+);/g, function (a, c) {
        switch (c) {
            case "amp":
                return"&";
            case "lt":
                return"<";
            case "gt":
                return">";
            case "quot":
                return'"';
            default:
                if ("#" == c.charAt(0)) {
                    var d = Number("0" + c.substr(1));
                    if (!isNaN(d))return String.fromCharCode(d)
                }
                return a
        }
    })
};
goog.string.HTML_ENTITY_PATTERN_ = /&([^;\s<&]+);?/g;
goog.string.whitespaceEscape = function (a, b) {
    return goog.string.newLineToBr(a.replace(/  /g, " &#160;"), b)
};
goog.string.stripQuotes = function (a, b) {
    for (var c = b.length, d = 0; d < c; d++) {
        var e = 1 == c ? b : b.charAt(d);
        if (a.charAt(0) == e && a.charAt(a.length - 1) == e)return a.substring(1, a.length - 1)
    }
    return a
};
goog.string.truncate = function (a, b, c) {
    c && (a = goog.string.unescapeEntities(a));
    a.length > b && (a = a.substring(0, b - 3) + "...");
    c && (a = goog.string.htmlEscape(a));
    return a
};
goog.string.truncateMiddle = function (a, b, c, d) {
    c && (a = goog.string.unescapeEntities(a));
    if (d && a.length > b) {
        d > b && (d = b);
        var e = a.length - d;
        a = a.substring(0, b - d) + "..." + a.substring(e)
    } else a.length > b && (d = Math.floor(b / 2), e = a.length - d, a = a.substring(0, d + b % 2) + "..." + a.substring(e));
    c && (a = goog.string.htmlEscape(a));
    return a
};
goog.string.specialEscapeChars_ = {"\x00": "\\0", "\b": "\\b", "\f": "\\f", "\n": "\\n", "\r": "\\r", "\t": "\\t", "\x0B": "\\x0B", '"': '\\"', "\\": "\\\\"};
goog.string.jsEscapeCache_ = {"'": "\\'"};
goog.string.quote = function (a) {
    a = String(a);
    if (a.quote)return a.quote();
    for (var b = ['"'], c = 0; c < a.length; c++) {
        var d = a.charAt(c), e = d.charCodeAt(0);
        b[c + 1] = goog.string.specialEscapeChars_[d] || (31 < e && 127 > e ? d : goog.string.escapeChar(d))
    }
    b.push('"');
    return b.join("")
};
goog.string.escapeString = function (a) {
    for (var b = [], c = 0; c < a.length; c++)b[c] = goog.string.escapeChar(a.charAt(c));
    return b.join("")
};
goog.string.escapeChar = function (a) {
    if (a in goog.string.jsEscapeCache_)return goog.string.jsEscapeCache_[a];
    if (a in goog.string.specialEscapeChars_)return goog.string.jsEscapeCache_[a] = goog.string.specialEscapeChars_[a];
    var b = a, c = a.charCodeAt(0);
    if (31 < c && 127 > c)b = a; else {
        if (256 > c) {
            if (b = "\\x", 16 > c || 256 < c)b += "0"
        } else b = "\\u", 4096 > c && (b += "0");
        b += c.toString(16).toUpperCase()
    }
    return goog.string.jsEscapeCache_[a] = b
};
goog.string.toMap = function (a) {
    for (var b = {}, c = 0; c < a.length; c++)b[a.charAt(c)] = !0;
    return b
};
goog.string.contains = function (a, b) {
    return-1 != a.indexOf(b)
};
goog.string.countOf = function (a, b) {
    return a && b ? a.split(b).length - 1 : 0
};
goog.string.removeAt = function (a, b, c) {
    var d = a;
    0 <= b && (b < a.length && 0 < c) && (d = a.substr(0, b) + a.substr(b + c, a.length - b - c));
    return d
};
goog.string.remove = function (a, b) {
    var c = RegExp(goog.string.regExpEscape(b), "");
    return a.replace(c, "")
};
goog.string.removeAll = function (a, b) {
    var c = RegExp(goog.string.regExpEscape(b), "g");
    return a.replace(c, "")
};
goog.string.regExpEscape = function (a) {
    return String(a).replace(/([-()\[\]{}+?*.$\^|,:#<!\\])/g, "\\$1").replace(/\x08/g, "\\x08")
};
goog.string.repeat = function (a, b) {
    return Array(b + 1).join(a)
};
goog.string.padNumber = function (a, b, c) {
    a = goog.isDef(c) ? a.toFixed(c) : String(a);
    c = a.indexOf(".");
    -1 == c && (c = a.length);
    return goog.string.repeat("0", Math.max(0, b - c)) + a
};
goog.string.makeSafe = function (a) {
    return null == a ? "" : String(a)
};
goog.string.buildString = function (a) {
    return Array.prototype.join.call(arguments, "")
};
goog.string.getRandomString = function () {
    return Math.floor(2147483648 * Math.random()).toString(36) + Math.abs(Math.floor(2147483648 * Math.random()) ^ goog.now()).toString(36)
};
goog.string.compareVersions = function (a, b) {
    for (var c = 0, d = goog.string.trim(String(a)).split("."), e = goog.string.trim(String(b)).split("."), f = Math.max(d.length, e.length), g = 0; 0 == c && g < f; g++) {
        var h = d[g] || "", k = e[g] || "", l = RegExp("(\\d*)(\\D*)", "g"), m = RegExp("(\\d*)(\\D*)", "g");
        do {
            var n = l.exec(h) || ["", "", ""], p = m.exec(k) || ["", "", ""];
            if (0 == n[0].length && 0 == p[0].length)break;
            var c = 0 == n[1].length ? 0 : parseInt(n[1], 10), q = 0 == p[1].length ? 0 : parseInt(p[1], 10), c = goog.string.compareElements_(c, q) || goog.string.compareElements_(0 ==
                n[2].length, 0 == p[2].length) || goog.string.compareElements_(n[2], p[2])
        } while (0 == c)
    }
    return c
};
goog.string.compareElements_ = function (a, b) {
    return a < b ? -1 : a > b ? 1 : 0
};
goog.string.HASHCODE_MAX_ = 4294967296;
goog.string.hashCode = function (a) {
    for (var b = 0, c = 0; c < a.length; ++c)b = 31 * b + a.charCodeAt(c), b %= goog.string.HASHCODE_MAX_;
    return b
};
goog.string.uniqueStringCounter_ = 2147483648 * Math.random() | 0;
goog.string.createUniqueString = function () {
    return"goog_" + goog.string.uniqueStringCounter_++
};
goog.string.toNumber = function (a) {
    var b = Number(a);
    return 0 == b && goog.string.isEmpty(a) ? NaN : b
};
goog.string.isLowerCamelCase = function (a) {
    return/^[a-z]+([A-Z][a-z]*)*$/.test(a)
};
goog.string.isUpperCamelCase = function (a) {
    return/^([A-Z][a-z]*)+$/.test(a)
};
goog.string.toCamelCase = function (a) {
    return String(a).replace(/\-([a-z])/g, function (a, c) {
        return c.toUpperCase()
    })
};
goog.string.toSelectorCase = function (a) {
    return String(a).replace(/([A-Z])/g, "-$1").toLowerCase()
};
goog.string.toTitleCase = function (a, b) {
    var c = goog.isString(b) ? goog.string.regExpEscape(b) : "\\s";
    return a.replace(RegExp("(^" + (c ? "|[" + c + "]+" : "") + ")([a-z])", "g"), function (a, b, c) {
        return b + c.toUpperCase()
    })
};
goog.string.parseInt = function (a) {
    isFinite(a) && (a = String(a));
    return goog.isString(a) ? /^\s*-?0x/i.test(a) ? parseInt(a, 16) : parseInt(a, 10) : NaN
};
goog.string.splitLimit = function (a, b, c) {
    a = a.split(b);
    for (var d = []; 0 < c && a.length;)d.push(a.shift()), c--;
    a.length && d.push(a.join(b));
    return d
};
goog.asserts = {};
goog.asserts.ENABLE_ASSERTS = goog.DEBUG;
goog.asserts.AssertionError = function (a, b) {
    b.unshift(a);
    goog.debug.Error.call(this, goog.string.subs.apply(null, b));
    b.shift();
    this.messagePattern = a
};
goog.inherits(goog.asserts.AssertionError, goog.debug.Error);
goog.asserts.AssertionError.prototype.name = "AssertionError";
goog.asserts.doAssertFailure_ = function (a, b, c, d) {
    var e = "Assertion failed";
    if (c)var e = e + (": " + c), f = d; else a && (e += ": " + a, f = b);
    throw new goog.asserts.AssertionError("" + e, f || []);
};
goog.asserts.assert = function (a, b, c) {
    goog.asserts.ENABLE_ASSERTS && !a && goog.asserts.doAssertFailure_("", null, b, Array.prototype.slice.call(arguments, 2));
    return a
};
goog.asserts.fail = function (a, b) {
    if (goog.asserts.ENABLE_ASSERTS)throw new goog.asserts.AssertionError("Failure" + (a ? ": " + a : ""), Array.prototype.slice.call(arguments, 1));
};
goog.asserts.assertNumber = function (a, b, c) {
    goog.asserts.ENABLE_ASSERTS && !goog.isNumber(a) && goog.asserts.doAssertFailure_("Expected number but got %s: %s.", [goog.typeOf(a), a], b, Array.prototype.slice.call(arguments, 2));
    return a
};
goog.asserts.assertString = function (a, b, c) {
    goog.asserts.ENABLE_ASSERTS && !goog.isString(a) && goog.asserts.doAssertFailure_("Expected string but got %s: %s.", [goog.typeOf(a), a], b, Array.prototype.slice.call(arguments, 2));
    return a
};
goog.asserts.assertFunction = function (a, b, c) {
    goog.asserts.ENABLE_ASSERTS && !goog.isFunction(a) && goog.asserts.doAssertFailure_("Expected function but got %s: %s.", [goog.typeOf(a), a], b, Array.prototype.slice.call(arguments, 2));
    return a
};
goog.asserts.assertObject = function (a, b, c) {
    goog.asserts.ENABLE_ASSERTS && !goog.isObject(a) && goog.asserts.doAssertFailure_("Expected object but got %s: %s.", [goog.typeOf(a), a], b, Array.prototype.slice.call(arguments, 2));
    return a
};
goog.asserts.assertArray = function (a, b, c) {
    goog.asserts.ENABLE_ASSERTS && !goog.isArray(a) && goog.asserts.doAssertFailure_("Expected array but got %s: %s.", [goog.typeOf(a), a], b, Array.prototype.slice.call(arguments, 2));
    return a
};
goog.asserts.assertBoolean = function (a, b, c) {
    goog.asserts.ENABLE_ASSERTS && !goog.isBoolean(a) && goog.asserts.doAssertFailure_("Expected boolean but got %s: %s.", [goog.typeOf(a), a], b, Array.prototype.slice.call(arguments, 2));
    return a
};
goog.asserts.assertInstanceof = function (a, b, c, d) {
    !goog.asserts.ENABLE_ASSERTS || a instanceof b || goog.asserts.doAssertFailure_("instanceof check failed.", null, c, Array.prototype.slice.call(arguments, 3));
    return a
};
goog.asserts.assertObjectPrototypeIsIntact = function () {
    for (var a in Object.prototype)goog.asserts.fail(a + " should not be enumerable in Object.prototype.")
};
goog.object = {};
goog.object.forEach = function (a, b, c) {
    for (var d in a)b.call(c, a[d], d, a)
};
goog.object.filter = function (a, b, c) {
    var d = {}, e;
    for (e in a)b.call(c, a[e], e, a) && (d[e] = a[e]);
    return d
};
goog.object.map = function (a, b, c) {
    var d = {}, e;
    for (e in a)d[e] = b.call(c, a[e], e, a);
    return d
};
goog.object.some = function (a, b, c) {
    for (var d in a)if (b.call(c, a[d], d, a))return!0;
    return!1
};
goog.object.every = function (a, b, c) {
    for (var d in a)if (!b.call(c, a[d], d, a))return!1;
    return!0
};
goog.object.getCount = function (a) {
    var b = 0, c;
    for (c in a)b++;
    return b
};
goog.object.getAnyKey = function (a) {
    for (var b in a)return b
};
goog.object.getAnyValue = function (a) {
    for (var b in a)return a[b]
};
goog.object.contains = function (a, b) {
    return goog.object.containsValue(a, b)
};
goog.object.getValues = function (a) {
    var b = [], c = 0, d;
    for (d in a)b[c++] = a[d];
    return b
};
goog.object.getKeys = function (a) {
    var b = [], c = 0, d;
    for (d in a)b[c++] = d;
    return b
};
goog.object.getValueByKeys = function (a, b) {
    for (var c = goog.isArrayLike(b), d = c ? b : arguments, c = c ? 0 : 1; c < d.length && (a = a[d[c]], goog.isDef(a)); c++);
    return a
};
goog.object.containsKey = function (a, b) {
    return b in a
};
goog.object.containsValue = function (a, b) {
    for (var c in a)if (a[c] == b)return!0;
    return!1
};
goog.object.findKey = function (a, b, c) {
    for (var d in a)if (b.call(c, a[d], d, a))return d
};
goog.object.findValue = function (a, b, c) {
    return(b = goog.object.findKey(a, b, c)) && a[b]
};
goog.object.isEmpty = function (a) {
    for (var b in a)return!1;
    return!0
};
goog.object.clear = function (a) {
    for (var b in a)delete a[b]
};
goog.object.remove = function (a, b) {
    var c;
    (c = b in a) && delete a[b];
    return c
};
goog.object.add = function (a, b, c) {
    if (b in a)throw Error('The object already contains the key "' + b + '"');
    goog.object.set(a, b, c)
};
goog.object.get = function (a, b, c) {
    return b in a ? a[b] : c
};
goog.object.set = function (a, b, c) {
    a[b] = c
};
goog.object.setIfUndefined = function (a, b, c) {
    return b in a ? a[b] : a[b] = c
};
goog.object.clone = function (a) {
    var b = {}, c;
    for (c in a)b[c] = a[c];
    return b
};
goog.object.unsafeClone = function (a) {
    var b = goog.typeOf(a);
    if ("object" == b || "array" == b) {
        if (a.clone)return a.clone();
        var b = "array" == b ? [] : {}, c;
        for (c in a)b[c] = goog.object.unsafeClone(a[c]);
        return b
    }
    return a
};
goog.object.transpose = function (a) {
    var b = {}, c;
    for (c in a)b[a[c]] = c;
    return b
};
goog.object.PROTOTYPE_FIELDS_ = "constructor hasOwnProperty isPrototypeOf propertyIsEnumerable toLocaleString toString valueOf".split(" ");
goog.object.extend = function (a, b) {
    for (var c, d, e = 1; e < arguments.length; e++) {
        d = arguments[e];
        for (c in d)a[c] = d[c];
        for (var f = 0; f < goog.object.PROTOTYPE_FIELDS_.length; f++)c = goog.object.PROTOTYPE_FIELDS_[f], Object.prototype.hasOwnProperty.call(d, c) && (a[c] = d[c])
    }
};
goog.object.create = function (a) {
    var b = arguments.length;
    if (1 == b && goog.isArray(arguments[0]))return goog.object.create.apply(null, arguments[0]);
    if (b % 2)throw Error("Uneven number of arguments");
    for (var c = {}, d = 0; d < b; d += 2)c[arguments[d]] = arguments[d + 1];
    return c
};
goog.object.createSet = function (a) {
    var b = arguments.length;
    if (1 == b && goog.isArray(arguments[0]))return goog.object.createSet.apply(null, arguments[0]);
    for (var c = {}, d = 0; d < b; d++)c[arguments[d]] = !0;
    return c
};
goog.object.createImmutableView = function (a) {
    var b = a;
    Object.isFrozen && !Object.isFrozen(a) && (b = Object.create(a), Object.freeze(b));
    return b
};
goog.object.isImmutableView = function (a) {
    return!!Object.isFrozen && Object.isFrozen(a)
};
goog.array = {};
goog.NATIVE_ARRAY_PROTOTYPES = goog.TRUSTED_SITE;
goog.array.peek = function (a) {
    return a[a.length - 1]
};
goog.array.ARRAY_PROTOTYPE_ = Array.prototype;
goog.array.indexOf = goog.NATIVE_ARRAY_PROTOTYPES && goog.array.ARRAY_PROTOTYPE_.indexOf ? function (a, b, c) {
    goog.asserts.assert(null != a.length);
    return goog.array.ARRAY_PROTOTYPE_.indexOf.call(a, b, c)
} : function (a, b, c) {
    c = null == c ? 0 : 0 > c ? Math.max(0, a.length + c) : c;
    if (goog.isString(a))return goog.isString(b) && 1 == b.length ? a.indexOf(b, c) : -1;
    for (; c < a.length; c++)if (c in a && a[c] === b)return c;
    return-1
};
goog.array.lastIndexOf = goog.NATIVE_ARRAY_PROTOTYPES && goog.array.ARRAY_PROTOTYPE_.lastIndexOf ? function (a, b, c) {
    goog.asserts.assert(null != a.length);
    return goog.array.ARRAY_PROTOTYPE_.lastIndexOf.call(a, b, null == c ? a.length - 1 : c)
} : function (a, b, c) {
    c = null == c ? a.length - 1 : c;
    0 > c && (c = Math.max(0, a.length + c));
    if (goog.isString(a))return goog.isString(b) && 1 == b.length ? a.lastIndexOf(b, c) : -1;
    for (; 0 <= c; c--)if (c in a && a[c] === b)return c;
    return-1
};
goog.array.forEach = goog.NATIVE_ARRAY_PROTOTYPES && goog.array.ARRAY_PROTOTYPE_.forEach ? function (a, b, c) {
    goog.asserts.assert(null != a.length);
    goog.array.ARRAY_PROTOTYPE_.forEach.call(a, b, c)
} : function (a, b, c) {
    for (var d = a.length, e = goog.isString(a) ? a.split("") : a, f = 0; f < d; f++)f in e && b.call(c, e[f], f, a)
};
goog.array.forEachRight = function (a, b, c) {
    for (var d = a.length, e = goog.isString(a) ? a.split("") : a, d = d - 1; 0 <= d; --d)d in e && b.call(c, e[d], d, a)
};
goog.array.filter = goog.NATIVE_ARRAY_PROTOTYPES && goog.array.ARRAY_PROTOTYPE_.filter ? function (a, b, c) {
    goog.asserts.assert(null != a.length);
    return goog.array.ARRAY_PROTOTYPE_.filter.call(a, b, c)
} : function (a, b, c) {
    for (var d = a.length, e = [], f = 0, g = goog.isString(a) ? a.split("") : a, h = 0; h < d; h++)if (h in g) {
        var k = g[h];
        b.call(c, k, h, a) && (e[f++] = k)
    }
    return e
};
goog.array.map = goog.NATIVE_ARRAY_PROTOTYPES && goog.array.ARRAY_PROTOTYPE_.map ? function (a, b, c) {
    goog.asserts.assert(null != a.length);
    return goog.array.ARRAY_PROTOTYPE_.map.call(a, b, c)
} : function (a, b, c) {
    for (var d = a.length, e = Array(d), f = goog.isString(a) ? a.split("") : a, g = 0; g < d; g++)g in f && (e[g] = b.call(c, f[g], g, a));
    return e
};
goog.array.reduce = function (a, b, c, d) {
    if (a.reduce)return d ? a.reduce(goog.bind(b, d), c) : a.reduce(b, c);
    var e = c;
    goog.array.forEach(a, function (c, g) {
        e = b.call(d, e, c, g, a)
    });
    return e
};
goog.array.reduceRight = function (a, b, c, d) {
    if (a.reduceRight)return d ? a.reduceRight(goog.bind(b, d), c) : a.reduceRight(b, c);
    var e = c;
    goog.array.forEachRight(a, function (c, g) {
        e = b.call(d, e, c, g, a)
    });
    return e
};
goog.array.some = goog.NATIVE_ARRAY_PROTOTYPES && goog.array.ARRAY_PROTOTYPE_.some ? function (a, b, c) {
    goog.asserts.assert(null != a.length);
    return goog.array.ARRAY_PROTOTYPE_.some.call(a, b, c)
} : function (a, b, c) {
    for (var d = a.length, e = goog.isString(a) ? a.split("") : a, f = 0; f < d; f++)if (f in e && b.call(c, e[f], f, a))return!0;
    return!1
};
goog.array.every = goog.NATIVE_ARRAY_PROTOTYPES && goog.array.ARRAY_PROTOTYPE_.every ? function (a, b, c) {
    goog.asserts.assert(null != a.length);
    return goog.array.ARRAY_PROTOTYPE_.every.call(a, b, c)
} : function (a, b, c) {
    for (var d = a.length, e = goog.isString(a) ? a.split("") : a, f = 0; f < d; f++)if (f in e && !b.call(c, e[f], f, a))return!1;
    return!0
};
goog.array.count = function (a, b, c) {
    var d = 0;
    goog.array.forEach(a, function (a, f, g) {
        b.call(c, a, f, g) && ++d
    }, c);
    return d
};
goog.array.find = function (a, b, c) {
    b = goog.array.findIndex(a, b, c);
    return 0 > b ? null : goog.isString(a) ? a.charAt(b) : a[b]
};
goog.array.findIndex = function (a, b, c) {
    for (var d = a.length, e = goog.isString(a) ? a.split("") : a, f = 0; f < d; f++)if (f in e && b.call(c, e[f], f, a))return f;
    return-1
};
goog.array.findRight = function (a, b, c) {
    b = goog.array.findIndexRight(a, b, c);
    return 0 > b ? null : goog.isString(a) ? a.charAt(b) : a[b]
};
goog.array.findIndexRight = function (a, b, c) {
    for (var d = a.length, e = goog.isString(a) ? a.split("") : a, d = d - 1; 0 <= d; d--)if (d in e && b.call(c, e[d], d, a))return d;
    return-1
};
goog.array.contains = function (a, b) {
    return 0 <= goog.array.indexOf(a, b)
};
goog.array.isEmpty = function (a) {
    return 0 == a.length
};
goog.array.clear = function (a) {
    if (!goog.isArray(a))for (var b = a.length - 1; 0 <= b; b--)delete a[b];
    a.length = 0
};
goog.array.insert = function (a, b) {
    goog.array.contains(a, b) || a.push(b)
};
goog.array.insertAt = function (a, b, c) {
    goog.array.splice(a, c, 0, b)
};
goog.array.insertArrayAt = function (a, b, c) {
    goog.partial(goog.array.splice, a, c, 0).apply(null, b)
};
goog.array.insertBefore = function (a, b, c) {
    var d;
    2 == arguments.length || 0 > (d = goog.array.indexOf(a, c)) ? a.push(b) : goog.array.insertAt(a, b, d)
};
goog.array.remove = function (a, b) {
    var c = goog.array.indexOf(a, b), d;
    (d = 0 <= c) && goog.array.removeAt(a, c);
    return d
};
goog.array.removeAt = function (a, b) {
    goog.asserts.assert(null != a.length);
    return 1 == goog.array.ARRAY_PROTOTYPE_.splice.call(a, b, 1).length
};
goog.array.removeIf = function (a, b, c) {
    b = goog.array.findIndex(a, b, c);
    return 0 <= b ? (goog.array.removeAt(a, b), !0) : !1
};
goog.array.concat = function (a) {
    return goog.array.ARRAY_PROTOTYPE_.concat.apply(goog.array.ARRAY_PROTOTYPE_, arguments)
};
goog.array.toArray = function (a) {
    var b = a.length;
    if (0 < b) {
        for (var c = Array(b), d = 0; d < b; d++)c[d] = a[d];
        return c
    }
    return[]
};
goog.array.clone = goog.array.toArray;
goog.array.extend = function (a, b) {
    for (var c = 1; c < arguments.length; c++) {
        var d = arguments[c], e;
        if (goog.isArray(d) || (e = goog.isArrayLike(d)) && Object.prototype.hasOwnProperty.call(d, "callee"))a.push.apply(a, d); else if (e)for (var f = a.length, g = d.length, h = 0; h < g; h++)a[f + h] = d[h]; else a.push(d)
    }
};
goog.array.splice = function (a, b, c, d) {
    goog.asserts.assert(null != a.length);
    return goog.array.ARRAY_PROTOTYPE_.splice.apply(a, goog.array.slice(arguments, 1))
};
goog.array.slice = function (a, b, c) {
    goog.asserts.assert(null != a.length);
    return 2 >= arguments.length ? goog.array.ARRAY_PROTOTYPE_.slice.call(a, b) : goog.array.ARRAY_PROTOTYPE_.slice.call(a, b, c)
};
goog.array.removeDuplicates = function (a, b) {
    for (var c = b || a, d = {}, e = 0, f = 0; f < a.length;) {
        var g = a[f++], h = goog.isObject(g) ? "o" + goog.getUid(g) : (typeof g).charAt(0) + g;
        Object.prototype.hasOwnProperty.call(d, h) || (d[h] = !0, c[e++] = g)
    }
    c.length = e
};
goog.array.binarySearch = function (a, b, c) {
    return goog.array.binarySearch_(a, c || goog.array.defaultCompare, !1, b)
};
goog.array.binarySelect = function (a, b, c) {
    return goog.array.binarySearch_(a, b, !0, void 0, c)
};
goog.array.binarySearch_ = function (a, b, c, d, e) {
    for (var f = 0, g = a.length, h; f < g;) {
        var k = f + g >> 1, l;
        l = c ? b.call(e, a[k], k, a) : b(d, a[k]);
        0 < l ? f = k + 1 : (g = k, h = !l)
    }
    return h ? f : ~f
};
goog.array.sort = function (a, b) {
    goog.asserts.assert(null != a.length);
    goog.array.ARRAY_PROTOTYPE_.sort.call(a, b || goog.array.defaultCompare)
};
goog.array.stableSort = function (a, b) {
    for (var c = 0; c < a.length; c++)a[c] = {index: c, value: a[c]};
    var d = b || goog.array.defaultCompare;
    goog.array.sort(a, function (a, b) {
        return d(a.value, b.value) || a.index - b.index
    });
    for (c = 0; c < a.length; c++)a[c] = a[c].value
};
goog.array.sortObjectsByKey = function (a, b, c) {
    var d = c || goog.array.defaultCompare;
    goog.array.sort(a, function (a, c) {
        return d(a[b], c[b])
    })
};
goog.array.isSorted = function (a, b, c) {
    b = b || goog.array.defaultCompare;
    for (var d = 1; d < a.length; d++) {
        var e = b(a[d - 1], a[d]);
        if (0 < e || 0 == e && c)return!1
    }
    return!0
};
goog.array.equals = function (a, b, c) {
    if (!goog.isArrayLike(a) || !goog.isArrayLike(b) || a.length != b.length)return!1;
    var d = a.length;
    c = c || goog.array.defaultCompareEquality;
    for (var e = 0; e < d; e++)if (!c(a[e], b[e]))return!1;
    return!0
};
goog.array.compare = function (a, b, c) {
    return goog.array.equals(a, b, c)
};
goog.array.compare3 = function (a, b, c) {
    c = c || goog.array.defaultCompare;
    for (var d = Math.min(a.length, b.length), e = 0; e < d; e++) {
        var f = c(a[e], b[e]);
        if (0 != f)return f
    }
    return goog.array.defaultCompare(a.length, b.length)
};
goog.array.defaultCompare = function (a, b) {
    return a > b ? 1 : a < b ? -1 : 0
};
goog.array.defaultCompareEquality = function (a, b) {
    return a === b
};
goog.array.binaryInsert = function (a, b, c) {
    c = goog.array.binarySearch(a, b, c);
    return 0 > c ? (goog.array.insertAt(a, b, -(c + 1)), !0) : !1
};
goog.array.binaryRemove = function (a, b, c) {
    b = goog.array.binarySearch(a, b, c);
    return 0 <= b ? goog.array.removeAt(a, b) : !1
};
goog.array.bucket = function (a, b, c) {
    for (var d = {}, e = 0; e < a.length; e++) {
        var f = a[e], g = b.call(c, f, e, a);
        goog.isDef(g) && (d[g] || (d[g] = [])).push(f)
    }
    return d
};
goog.array.toObject = function (a, b, c) {
    var d = {};
    goog.array.forEach(a, function (e, f) {
        d[b.call(c, e, f, a)] = e
    });
    return d
};
goog.array.range = function (a, b, c) {
    var d = [], e = 0, f = a;
    c = c || 1;
    void 0 !== b && (e = a, f = b);
    if (0 > c * (f - e))return[];
    if (0 < c)for (a = e; a < f; a += c)d.push(a); else for (a = e; a > f; a += c)d.push(a);
    return d
};
goog.array.repeat = function (a, b) {
    for (var c = [], d = 0; d < b; d++)c[d] = a;
    return c
};
goog.array.flatten = function (a) {
    for (var b = [], c = 0; c < arguments.length; c++) {
        var d = arguments[c];
        goog.isArray(d) ? b.push.apply(b, goog.array.flatten.apply(null, d)) : b.push(d)
    }
    return b
};
goog.array.rotate = function (a, b) {
    goog.asserts.assert(null != a.length);
    a.length && (b %= a.length, 0 < b ? goog.array.ARRAY_PROTOTYPE_.unshift.apply(a, a.splice(-b, b)) : 0 > b && goog.array.ARRAY_PROTOTYPE_.push.apply(a, a.splice(0, -b)));
    return a
};
goog.array.moveItem = function (a, b, c) {
    goog.asserts.assert(0 <= b && b < a.length);
    goog.asserts.assert(0 <= c && c < a.length);
    b = goog.array.ARRAY_PROTOTYPE_.splice.call(a, b, 1);
    goog.array.ARRAY_PROTOTYPE_.splice.call(a, c, 0, b[0])
};
goog.array.zip = function (a) {
    if (!arguments.length)return[];
    for (var b = [], c = 0; ; c++) {
        for (var d = [], e = 0; e < arguments.length; e++) {
            var f = arguments[e];
            if (c >= f.length)return b;
            d.push(f[c])
        }
        b.push(d)
    }
};
goog.array.shuffle = function (a, b) {
    for (var c = b || Math.random, d = a.length - 1; 0 < d; d--) {
        var e = Math.floor(c() * (d + 1)), f = a[d];
        a[d] = a[e];
        a[e] = f
    }
};
goog.dom = {};
goog.dom.classes = {};
goog.dom.classes.set = function (a, b) {
    a.className = b
};
goog.dom.classes.get = function (a) {
    a = a.className;
    return goog.isString(a) && a.match(/\S+/g) || []
};
goog.dom.classes.add = function (a, b) {
    var c = goog.dom.classes.get(a), d = goog.array.slice(arguments, 1), e = c.length + d.length;
    goog.dom.classes.add_(c, d);
    goog.dom.classes.set(a, c.join(" "));
    return c.length == e
};
goog.dom.classes.remove = function (a, b) {
    var c = goog.dom.classes.get(a), d = goog.array.slice(arguments, 1), e = goog.dom.classes.getDifference_(c, d);
    goog.dom.classes.set(a, e.join(" "));
    return e.length == c.length - d.length
};
goog.dom.classes.add_ = function (a, b) {
    for (var c = 0; c < b.length; c++)goog.array.contains(a, b[c]) || a.push(b[c])
};
goog.dom.classes.getDifference_ = function (a, b) {
    return goog.array.filter(a, function (a) {
        return!goog.array.contains(b, a)
    })
};
goog.dom.classes.swap = function (a, b, c) {
    for (var d = goog.dom.classes.get(a), e = !1, f = 0; f < d.length; f++)d[f] == b && (goog.array.splice(d, f--, 1), e = !0);
    e && (d.push(c), goog.dom.classes.set(a, d.join(" ")));
    return e
};
goog.dom.classes.addRemove = function (a, b, c) {
    var d = goog.dom.classes.get(a);
    goog.isString(b) ? goog.array.remove(d, b) : goog.isArray(b) && (d = goog.dom.classes.getDifference_(d, b));
    goog.isString(c) && !goog.array.contains(d, c) ? d.push(c) : goog.isArray(c) && goog.dom.classes.add_(d, c);
    goog.dom.classes.set(a, d.join(" "))
};
goog.dom.classes.has = function (a, b) {
    return goog.array.contains(goog.dom.classes.get(a), b)
};
goog.dom.classes.enable = function (a, b, c) {
    c ? goog.dom.classes.add(a, b) : goog.dom.classes.remove(a, b)
};
goog.dom.classes.toggle = function (a, b) {
    var c = !goog.dom.classes.has(a, b);
    goog.dom.classes.enable(a, b, c);
    return c
};
goog.dom.TagName = {A: "A", ABBR: "ABBR", ACRONYM: "ACRONYM", ADDRESS: "ADDRESS", APPLET: "APPLET", AREA: "AREA", ARTICLE: "ARTICLE", ASIDE: "ASIDE", AUDIO: "AUDIO", B: "B", BASE: "BASE", BASEFONT: "BASEFONT", BDI: "BDI", BDO: "BDO", BIG: "BIG", BLOCKQUOTE: "BLOCKQUOTE", BODY: "BODY", BR: "BR", BUTTON: "BUTTON", CANVAS: "CANVAS", CAPTION: "CAPTION", CENTER: "CENTER", CITE: "CITE", CODE: "CODE", COL: "COL", COLGROUP: "COLGROUP", COMMAND: "COMMAND", DATA: "DATA", DATALIST: "DATALIST", DD: "DD", DEL: "DEL", DETAILS: "DETAILS", DFN: "DFN", DIALOG: "DIALOG", DIR: "DIR", DIV: "DIV",
    DL: "DL", DT: "DT", EM: "EM", EMBED: "EMBED", FIELDSET: "FIELDSET", FIGCAPTION: "FIGCAPTION", FIGURE: "FIGURE", FONT: "FONT", FOOTER: "FOOTER", FORM: "FORM", FRAME: "FRAME", FRAMESET: "FRAMESET", H1: "H1", H2: "H2", H3: "H3", H4: "H4", H5: "H5", H6: "H6", HEAD: "HEAD", HEADER: "HEADER", HGROUP: "HGROUP", HR: "HR", HTML: "HTML", I: "I", IFRAME: "IFRAME", IMG: "IMG", INPUT: "INPUT", INS: "INS", ISINDEX: "ISINDEX", KBD: "KBD", KEYGEN: "KEYGEN", LABEL: "LABEL", LEGEND: "LEGEND", LI: "LI", LINK: "LINK", MAP: "MAP", MARK: "MARK", MATH: "MATH", MENU: "MENU", META: "META", METER: "METER",
    NAV: "NAV", NOFRAMES: "NOFRAMES", NOSCRIPT: "NOSCRIPT", OBJECT: "OBJECT", OL: "OL", OPTGROUP: "OPTGROUP", OPTION: "OPTION", OUTPUT: "OUTPUT", P: "P", PARAM: "PARAM", PRE: "PRE", PROGRESS: "PROGRESS", Q: "Q", RP: "RP", RT: "RT", RUBY: "RUBY", S: "S", SAMP: "SAMP", SCRIPT: "SCRIPT", SECTION: "SECTION", SELECT: "SELECT", SMALL: "SMALL", SOURCE: "SOURCE", SPAN: "SPAN", STRIKE: "STRIKE", STRONG: "STRONG", STYLE: "STYLE", SUB: "SUB", SUMMARY: "SUMMARY", SUP: "SUP", SVG: "SVG", TABLE: "TABLE", TBODY: "TBODY", TD: "TD", TEXTAREA: "TEXTAREA", TFOOT: "TFOOT", TH: "TH", THEAD: "THEAD",
    TIME: "TIME", TITLE: "TITLE", TR: "TR", TRACK: "TRACK", TT: "TT", U: "U", UL: "UL", VAR: "VAR", VIDEO: "VIDEO", WBR: "WBR"};
goog.userAgent = {};
goog.userAgent.ASSUME_IE = !1;
goog.userAgent.ASSUME_GECKO = !1;
goog.userAgent.ASSUME_WEBKIT = !1;
goog.userAgent.ASSUME_MOBILE_WEBKIT = !1;
goog.userAgent.ASSUME_OPERA = !1;
goog.userAgent.ASSUME_ANY_VERSION = !1;
goog.userAgent.BROWSER_KNOWN_ = goog.userAgent.ASSUME_IE || goog.userAgent.ASSUME_GECKO || goog.userAgent.ASSUME_MOBILE_WEBKIT || goog.userAgent.ASSUME_WEBKIT || goog.userAgent.ASSUME_OPERA;
goog.userAgent.getUserAgentString = function () {
    return goog.global.navigator ? goog.global.navigator.userAgent : null
};
goog.userAgent.getNavigator = function () {
    return goog.global.navigator
};
goog.userAgent.init_ = function () {
    goog.userAgent.detectedOpera_ = !1;
    goog.userAgent.detectedIe_ = !1;
    goog.userAgent.detectedWebkit_ = !1;
    goog.userAgent.detectedMobile_ = !1;
    goog.userAgent.detectedGecko_ = !1;
    var a;
    if (!goog.userAgent.BROWSER_KNOWN_ && (a = goog.userAgent.getUserAgentString())) {
        var b = goog.userAgent.getNavigator();
        goog.userAgent.detectedOpera_ = goog.string.startsWith(a, "Opera");
        goog.userAgent.detectedIe_ = !goog.userAgent.detectedOpera_ && (goog.string.contains(a, "MSIE") || goog.string.contains(a, "Trident"));
        goog.userAgent.detectedWebkit_ = !goog.userAgent.detectedOpera_ && goog.string.contains(a, "WebKit");
        goog.userAgent.detectedMobile_ = goog.userAgent.detectedWebkit_ && goog.string.contains(a, "Mobile");
        goog.userAgent.detectedGecko_ = !goog.userAgent.detectedOpera_ && !goog.userAgent.detectedWebkit_ && !goog.userAgent.detectedIe_ && "Gecko" == b.product
    }
};
goog.userAgent.BROWSER_KNOWN_ || goog.userAgent.init_();
goog.userAgent.OPERA = goog.userAgent.BROWSER_KNOWN_ ? goog.userAgent.ASSUME_OPERA : goog.userAgent.detectedOpera_;
goog.userAgent.IE = goog.userAgent.BROWSER_KNOWN_ ? goog.userAgent.ASSUME_IE : goog.userAgent.detectedIe_;
goog.userAgent.GECKO = goog.userAgent.BROWSER_KNOWN_ ? goog.userAgent.ASSUME_GECKO : goog.userAgent.detectedGecko_;
goog.userAgent.WEBKIT = goog.userAgent.BROWSER_KNOWN_ ? goog.userAgent.ASSUME_WEBKIT || goog.userAgent.ASSUME_MOBILE_WEBKIT : goog.userAgent.detectedWebkit_;
goog.userAgent.MOBILE = goog.userAgent.ASSUME_MOBILE_WEBKIT || goog.userAgent.detectedMobile_;
goog.userAgent.SAFARI = goog.userAgent.WEBKIT;
goog.userAgent.determinePlatform_ = function () {
    var a = goog.userAgent.getNavigator();
    return a && a.platform || ""
};
goog.userAgent.PLATFORM = goog.userAgent.determinePlatform_();
goog.userAgent.ASSUME_MAC = !1;
goog.userAgent.ASSUME_WINDOWS = !1;
goog.userAgent.ASSUME_LINUX = !1;
goog.userAgent.ASSUME_X11 = !1;
goog.userAgent.ASSUME_ANDROID = !1;
goog.userAgent.ASSUME_IPHONE = !1;
goog.userAgent.ASSUME_IPAD = !1;
goog.userAgent.PLATFORM_KNOWN_ = goog.userAgent.ASSUME_MAC || goog.userAgent.ASSUME_WINDOWS || goog.userAgent.ASSUME_LINUX || goog.userAgent.ASSUME_X11 || goog.userAgent.ASSUME_ANDROID || goog.userAgent.ASSUME_IPHONE || goog.userAgent.ASSUME_IPAD;
goog.userAgent.initPlatform_ = function () {
    goog.userAgent.detectedMac_ = goog.string.contains(goog.userAgent.PLATFORM, "Mac");
    goog.userAgent.detectedWindows_ = goog.string.contains(goog.userAgent.PLATFORM, "Win");
    goog.userAgent.detectedLinux_ = goog.string.contains(goog.userAgent.PLATFORM, "Linux");
    goog.userAgent.detectedX11_ = !!goog.userAgent.getNavigator() && goog.string.contains(goog.userAgent.getNavigator().appVersion || "", "X11");
    var a = goog.userAgent.getUserAgentString();
    goog.userAgent.detectedAndroid_ = !!a &&
        goog.string.contains(a, "Android");
    goog.userAgent.detectedIPhone_ = !!a && goog.string.contains(a, "iPhone");
    goog.userAgent.detectedIPad_ = !!a && goog.string.contains(a, "iPad")
};
goog.userAgent.PLATFORM_KNOWN_ || goog.userAgent.initPlatform_();
goog.userAgent.MAC = goog.userAgent.PLATFORM_KNOWN_ ? goog.userAgent.ASSUME_MAC : goog.userAgent.detectedMac_;
goog.userAgent.WINDOWS = goog.userAgent.PLATFORM_KNOWN_ ? goog.userAgent.ASSUME_WINDOWS : goog.userAgent.detectedWindows_;
goog.userAgent.LINUX = goog.userAgent.PLATFORM_KNOWN_ ? goog.userAgent.ASSUME_LINUX : goog.userAgent.detectedLinux_;
goog.userAgent.X11 = goog.userAgent.PLATFORM_KNOWN_ ? goog.userAgent.ASSUME_X11 : goog.userAgent.detectedX11_;
goog.userAgent.ANDROID = goog.userAgent.PLATFORM_KNOWN_ ? goog.userAgent.ASSUME_ANDROID : goog.userAgent.detectedAndroid_;
goog.userAgent.IPHONE = goog.userAgent.PLATFORM_KNOWN_ ? goog.userAgent.ASSUME_IPHONE : goog.userAgent.detectedIPhone_;
goog.userAgent.IPAD = goog.userAgent.PLATFORM_KNOWN_ ? goog.userAgent.ASSUME_IPAD : goog.userAgent.detectedIPad_;
goog.userAgent.determineVersion_ = function () {
    var a = "", b;
    goog.userAgent.OPERA && goog.global.opera ? (a = goog.global.opera.version, a = "function" == typeof a ? a() : a) : (goog.userAgent.GECKO ? b = /rv\:([^\);]+)(\)|;)/ : goog.userAgent.IE ? b = /\b(?:MSIE|rv)\s+([^\);]+)(\)|;)/ : goog.userAgent.WEBKIT && (b = /WebKit\/(\S+)/), b && (a = (a = b.exec(goog.userAgent.getUserAgentString())) ? a[1] : ""));
    return goog.userAgent.IE && (b = goog.userAgent.getDocumentMode_(), b > parseFloat(a)) ? String(b) : a
};
goog.userAgent.getDocumentMode_ = function () {
    var a = goog.global.document;
    return a ? a.documentMode : void 0
};
goog.userAgent.VERSION = goog.userAgent.determineVersion_();
goog.userAgent.compare = function (a, b) {
    return goog.string.compareVersions(a, b)
};
goog.userAgent.isVersionOrHigherCache_ = {};
goog.userAgent.isVersionOrHigher = function (a) {
    return goog.userAgent.ASSUME_ANY_VERSION || goog.userAgent.isVersionOrHigherCache_[a] || (goog.userAgent.isVersionOrHigherCache_[a] = 0 <= goog.string.compareVersions(goog.userAgent.VERSION, a))
};
goog.userAgent.isVersion = goog.userAgent.isVersionOrHigher;
goog.userAgent.isDocumentModeOrHigher = function (a) {
    return goog.userAgent.IE && goog.userAgent.DOCUMENT_MODE >= a
};
goog.userAgent.isDocumentMode = goog.userAgent.isDocumentModeOrHigher;
goog.userAgent.DOCUMENT_MODE = function () {
    var a = goog.global.document;
    return a && goog.userAgent.IE ? goog.userAgent.getDocumentMode_() || ("CSS1Compat" == a.compatMode ? parseInt(goog.userAgent.VERSION, 10) : 5) : void 0
}();
goog.math = {};
goog.math.Size = function (a, b) {
    this.width = a;
    this.height = b
};
goog.math.Size.equals = function (a, b) {
    return a == b ? !0 : a && b ? a.width == b.width && a.height == b.height : !1
};
goog.math.Size.prototype.clone = function () {
    return new goog.math.Size(this.width, this.height)
};
goog.DEBUG && (goog.math.Size.prototype.toString = function () {
    return"(" + this.width + " x " + this.height + ")"
});
goog.math.Size.prototype.getLongest = function () {
    return Math.max(this.width, this.height)
};
goog.math.Size.prototype.getShortest = function () {
    return Math.min(this.width, this.height)
};
goog.math.Size.prototype.area = function () {
    return this.width * this.height
};
goog.math.Size.prototype.perimeter = function () {
    return 2 * (this.width + this.height)
};
goog.math.Size.prototype.aspectRatio = function () {
    return this.width / this.height
};
goog.math.Size.prototype.isEmpty = function () {
    return!this.area()
};
goog.math.Size.prototype.ceil = function () {
    this.width = Math.ceil(this.width);
    this.height = Math.ceil(this.height);
    return this
};
goog.math.Size.prototype.fitsInside = function (a) {
    return this.width <= a.width && this.height <= a.height
};
goog.math.Size.prototype.floor = function () {
    this.width = Math.floor(this.width);
    this.height = Math.floor(this.height);
    return this
};
goog.math.Size.prototype.round = function () {
    this.width = Math.round(this.width);
    this.height = Math.round(this.height);
    return this
};
goog.math.Size.prototype.scale = function (a, b) {
    var c = goog.isNumber(b) ? b : a;
    this.width *= a;
    this.height *= c;
    return this
};
goog.math.Size.prototype.scaleToFit = function (a) {
    a = this.aspectRatio() > a.aspectRatio() ? a.width / this.width : a.height / this.height;
    return this.scale(a)
};
goog.dom.BrowserFeature = {CAN_ADD_NAME_OR_TYPE_ATTRIBUTES: !goog.userAgent.IE || goog.userAgent.isDocumentModeOrHigher(9), CAN_USE_CHILDREN_ATTRIBUTE: !goog.userAgent.GECKO && !goog.userAgent.IE || goog.userAgent.IE && goog.userAgent.isDocumentModeOrHigher(9) || goog.userAgent.GECKO && goog.userAgent.isVersionOrHigher("1.9.1"), CAN_USE_INNER_TEXT: goog.userAgent.IE && !goog.userAgent.isVersionOrHigher("9"), CAN_USE_PARENT_ELEMENT_PROPERTY: goog.userAgent.IE || goog.userAgent.OPERA || goog.userAgent.WEBKIT, INNER_HTML_NEEDS_SCOPED_ELEMENT: goog.userAgent.IE};
goog.math.randomInt = function (a) {
    return Math.floor(Math.random() * a)
};
goog.math.uniformRandom = function (a, b) {
    return a + Math.random() * (b - a)
};
goog.math.clamp = function (a, b, c) {
    return Math.min(Math.max(a, b), c)
};
goog.math.modulo = function (a, b) {
    var c = a % b;
    return 0 > c * b ? c + b : c
};
goog.math.lerp = function (a, b, c) {
    return a + c * (b - a)
};
goog.math.nearlyEquals = function (a, b, c) {
    return Math.abs(a - b) <= (c || 1E-6)
};
goog.math.standardAngle = function (a) {
    return goog.math.modulo(a, 360)
};
goog.math.toRadians = function (a) {
    return a * Math.PI / 180
};
goog.math.toDegrees = function (a) {
    return 180 * a / Math.PI
};
goog.math.angleDx = function (a, b) {
    return b * Math.cos(goog.math.toRadians(a))
};
goog.math.angleDy = function (a, b) {
    return b * Math.sin(goog.math.toRadians(a))
};
goog.math.angle = function (a, b, c, d) {
    return goog.math.standardAngle(goog.math.toDegrees(Math.atan2(d - b, c - a)))
};
goog.math.angleDifference = function (a, b) {
    var c = goog.math.standardAngle(b) - goog.math.standardAngle(a);
    180 < c ? c -= 360 : -180 >= c && (c = 360 + c);
    return c
};
goog.math.sign = function (a) {
    return 0 == a ? 0 : 0 > a ? -1 : 1
};
goog.math.longestCommonSubsequence = function (a, b, c, d) {
    c = c || function (a, b) {
        return a == b
    };
    d = d || function (b, c) {
        return a[b]
    };
    for (var e = a.length, f = b.length, g = [], h = 0; h < e + 1; h++)g[h] = [], g[h][0] = 0;
    for (var k = 0; k < f + 1; k++)g[0][k] = 0;
    for (h = 1; h <= e; h++)for (k = 1; k <= f; k++)c(a[h - 1], b[k - 1]) ? g[h][k] = g[h - 1][k - 1] + 1 : g[h][k] = Math.max(g[h - 1][k], g[h][k - 1]);
    for (var l = [], h = e, k = f; 0 < h && 0 < k;)c(a[h - 1], b[k - 1]) ? (l.unshift(d(h - 1, k - 1)), h--, k--) : g[h - 1][k] > g[h][k - 1] ? h-- : k--;
    return l
};
goog.math.sum = function (a) {
    return goog.array.reduce(arguments, function (a, c) {
        return a + c
    }, 0)
};
goog.math.average = function (a) {
    return goog.math.sum.apply(null, arguments) / arguments.length
};
goog.math.standardDeviation = function (a) {
    var b = arguments.length;
    if (2 > b)return 0;
    var c = goog.math.average.apply(null, arguments), b = goog.math.sum.apply(null, goog.array.map(arguments, function (a) {
        return Math.pow(a - c, 2)
    })) / (b - 1);
    return Math.sqrt(b)
};
goog.math.isInt = function (a) {
    return isFinite(a) && 0 == a % 1
};
goog.math.isFiniteNumber = function (a) {
    return isFinite(a) && !isNaN(a)
};
goog.math.safeFloor = function (a, b) {
    goog.asserts.assert(!goog.isDef(b) || 0 < b);
    return Math.floor(a + (b || 2E-15))
};
goog.math.safeCeil = function (a, b) {
    goog.asserts.assert(!goog.isDef(b) || 0 < b);
    return Math.ceil(a - (b || 2E-15))
};
goog.math.Coordinate = function (a, b) {
    this.x = goog.isDef(a) ? a : 0;
    this.y = goog.isDef(b) ? b : 0
};
goog.math.Coordinate.prototype.clone = function () {
    return new goog.math.Coordinate(this.x, this.y)
};
goog.DEBUG && (goog.math.Coordinate.prototype.toString = function () {
    return"(" + this.x + ", " + this.y + ")"
});
goog.math.Coordinate.equals = function (a, b) {
    return a == b ? !0 : a && b ? a.x == b.x && a.y == b.y : !1
};
goog.math.Coordinate.distance = function (a, b) {
    var c = a.x - b.x, d = a.y - b.y;
    return Math.sqrt(c * c + d * d)
};
goog.math.Coordinate.magnitude = function (a) {
    return Math.sqrt(a.x * a.x + a.y * a.y)
};
goog.math.Coordinate.azimuth = function (a) {
    return goog.math.angle(0, 0, a.x, a.y)
};
goog.math.Coordinate.squaredDistance = function (a, b) {
    var c = a.x - b.x, d = a.y - b.y;
    return c * c + d * d
};
goog.math.Coordinate.difference = function (a, b) {
    return new goog.math.Coordinate(a.x - b.x, a.y - b.y)
};
goog.math.Coordinate.sum = function (a, b) {
    return new goog.math.Coordinate(a.x + b.x, a.y + b.y)
};
goog.math.Coordinate.prototype.ceil = function () {
    this.x = Math.ceil(this.x);
    this.y = Math.ceil(this.y);
    return this
};
goog.math.Coordinate.prototype.floor = function () {
    this.x = Math.floor(this.x);
    this.y = Math.floor(this.y);
    return this
};
goog.math.Coordinate.prototype.round = function () {
    this.x = Math.round(this.x);
    this.y = Math.round(this.y);
    return this
};
goog.math.Coordinate.prototype.translate = function (a, b) {
    a instanceof goog.math.Coordinate ? (this.x += a.x, this.y += a.y) : (this.x += a, goog.isNumber(b) && (this.y += b));
    return this
};
goog.math.Coordinate.prototype.scale = function (a, b) {
    var c = goog.isNumber(b) ? b : a;
    this.x *= a;
    this.y *= c;
    return this
};
goog.dom.ASSUME_QUIRKS_MODE = !1;
goog.dom.ASSUME_STANDARDS_MODE = !1;
goog.dom.COMPAT_MODE_KNOWN_ = goog.dom.ASSUME_QUIRKS_MODE || goog.dom.ASSUME_STANDARDS_MODE;
goog.dom.NodeType = {ELEMENT: 1, ATTRIBUTE: 2, TEXT: 3, CDATA_SECTION: 4, ENTITY_REFERENCE: 5, ENTITY: 6, PROCESSING_INSTRUCTION: 7, COMMENT: 8, DOCUMENT: 9, DOCUMENT_TYPE: 10, DOCUMENT_FRAGMENT: 11, NOTATION: 12};
goog.dom.getDomHelper = function (a) {
    return a ? new goog.dom.DomHelper(goog.dom.getOwnerDocument(a)) : goog.dom.defaultDomHelper_ || (goog.dom.defaultDomHelper_ = new goog.dom.DomHelper)
};
goog.dom.getDocument = function () {
    return document
};
goog.dom.getElement = function (a) {
    return goog.isString(a) ? document.getElementById(a) : a
};
goog.dom.$ = goog.dom.getElement;
goog.dom.getElementsByTagNameAndClass = function (a, b, c) {
    return goog.dom.getElementsByTagNameAndClass_(document, a, b, c)
};
goog.dom.getElementsByClass = function (a, b) {
    var c = b || document;
    return goog.dom.canUseQuerySelector_(c) ? c.querySelectorAll("." + a) : c.getElementsByClassName ? c.getElementsByClassName(a) : goog.dom.getElementsByTagNameAndClass_(document, "*", a, b)
};
goog.dom.getElementByClass = function (a, b) {
    var c = b || document, d = null;
    return(d = goog.dom.canUseQuerySelector_(c) ? c.querySelector("." + a) : goog.dom.getElementsByClass(a, b)[0]) || null
};
goog.dom.canUseQuerySelector_ = function (a) {
    return!(!a.querySelectorAll || !a.querySelector)
};
goog.dom.getElementsByTagNameAndClass_ = function (a, b, c, d) {
    a = d || a;
    b = b && "*" != b ? b.toUpperCase() : "";
    if (goog.dom.canUseQuerySelector_(a) && (b || c))return a.querySelectorAll(b + (c ? "." + c : ""));
    if (c && a.getElementsByClassName) {
        a = a.getElementsByClassName(c);
        if (b) {
            d = {};
            for (var e = 0, f = 0, g; g = a[f]; f++)b == g.nodeName && (d[e++] = g);
            d.length = e;
            return d
        }
        return a
    }
    a = a.getElementsByTagName(b || "*");
    if (c) {
        d = {};
        for (f = e = 0; g = a[f]; f++)b = g.className, "function" == typeof b.split && goog.array.contains(b.split(/\s+/), c) && (d[e++] = g);
        d.length =
            e;
        return d
    }
    return a
};
goog.dom.$$ = goog.dom.getElementsByTagNameAndClass;
goog.dom.setProperties = function (a, b) {
    goog.object.forEach(b, function (b, d) {
        "style" == d ? a.style.cssText = b : "class" == d ? a.className = b : "for" == d ? a.htmlFor = b : d in goog.dom.DIRECT_ATTRIBUTE_MAP_ ? a.setAttribute(goog.dom.DIRECT_ATTRIBUTE_MAP_[d], b) : goog.string.startsWith(d, "aria-") || goog.string.startsWith(d, "data-") ? a.setAttribute(d, b) : a[d] = b
    })
};
goog.dom.DIRECT_ATTRIBUTE_MAP_ = {cellpadding: "cellPadding", cellspacing: "cellSpacing", colspan: "colSpan", frameborder: "frameBorder", height: "height", maxlength: "maxLength", role: "role", rowspan: "rowSpan", type: "type", usemap: "useMap", valign: "vAlign", width: "width"};
goog.dom.getViewportSize = function (a) {
    return goog.dom.getViewportSize_(a || window)
};
goog.dom.getViewportSize_ = function (a) {
    a = a.document;
    a = goog.dom.isCss1CompatMode_(a) ? a.documentElement : a.body;
    return new goog.math.Size(a.clientWidth, a.clientHeight)
};
goog.dom.getDocumentHeight = function () {
    return goog.dom.getDocumentHeight_(window)
};
goog.dom.getDocumentHeight_ = function (a) {
    var b = a.document, c = 0;
    if (b) {
        a = goog.dom.getViewportSize_(a).height;
        var c = b.body, d = b.documentElement;
        if (goog.dom.isCss1CompatMode_(b) && d.scrollHeight)c = d.scrollHeight != a ? d.scrollHeight : d.offsetHeight; else {
            var b = d.scrollHeight, e = d.offsetHeight;
            d.clientHeight != e && (b = c.scrollHeight, e = c.offsetHeight);
            c = b > a ? b > e ? b : e : b < e ? b : e
        }
    }
    return c
};
goog.dom.getPageScroll = function (a) {
    return goog.dom.getDomHelper((a || goog.global || window).document).getDocumentScroll()
};
goog.dom.getDocumentScroll = function () {
    return goog.dom.getDocumentScroll_(document)
};
goog.dom.getDocumentScroll_ = function (a) {
    var b = goog.dom.getDocumentScrollElement_(a);
    a = goog.dom.getWindow_(a);
    return goog.userAgent.IE && goog.userAgent.isVersionOrHigher("10") && a.pageYOffset != b.scrollTop ? new goog.math.Coordinate(b.scrollLeft, b.scrollTop) : new goog.math.Coordinate(a.pageXOffset || b.scrollLeft, a.pageYOffset || b.scrollTop)
};
goog.dom.getDocumentScrollElement = function () {
    return goog.dom.getDocumentScrollElement_(document)
};
goog.dom.getDocumentScrollElement_ = function (a) {
    return!goog.userAgent.WEBKIT && goog.dom.isCss1CompatMode_(a) ? a.documentElement : a.body
};
goog.dom.getWindow = function (a) {
    return a ? goog.dom.getWindow_(a) : window
};
goog.dom.getWindow_ = function (a) {
    return a.parentWindow || a.defaultView
};
goog.dom.createDom = function (a, b, c) {
    return goog.dom.createDom_(document, arguments)
};
goog.dom.createDom_ = function (a, b) {
    var c = b[0], d = b[1];
    if (!goog.dom.BrowserFeature.CAN_ADD_NAME_OR_TYPE_ATTRIBUTES && d && (d.name || d.type)) {
        c = ["<", c];
        d.name && c.push(' name="', goog.string.htmlEscape(d.name), '"');
        if (d.type) {
            c.push(' type="', goog.string.htmlEscape(d.type), '"');
            var e = {};
            goog.object.extend(e, d);
            delete e.type;
            d = e
        }
        c.push(">");
        c = c.join("")
    }
    c = a.createElement(c);
    d && (goog.isString(d) ? c.className = d : goog.isArray(d) ? goog.dom.classes.add.apply(null, [c].concat(d)) : goog.dom.setProperties(c, d));
    2 < b.length &&
    goog.dom.append_(a, c, b, 2);
    return c
};
goog.dom.append_ = function (a, b, c, d) {
    function e(c) {
        c && b.appendChild(goog.isString(c) ? a.createTextNode(c) : c)
    }

    for (; d < c.length; d++) {
        var f = c[d];
        goog.isArrayLike(f) && !goog.dom.isNodeLike(f) ? goog.array.forEach(goog.dom.isNodeList(f) ? goog.array.toArray(f) : f, e) : e(f)
    }
};
goog.dom.$dom = goog.dom.createDom;
goog.dom.createElement = function (a) {
    return document.createElement(a)
};
goog.dom.createTextNode = function (a) {
    return document.createTextNode(String(a))
};
goog.dom.createTable = function (a, b, c) {
    return goog.dom.createTable_(document, a, b, !!c)
};
goog.dom.createTable_ = function (a, b, c, d) {
    for (var e = ["<tr>"], f = 0; f < c; f++)e.push(d ? "<td>&nbsp;</td>" : "<td></td>");
    e.push("</tr>");
    e = e.join("");
    c = ["<table>"];
    for (f = 0; f < b; f++)c.push(e);
    c.push("</table>");
    a = a.createElement(goog.dom.TagName.DIV);
    a.innerHTML = c.join("");
    return a.removeChild(a.firstChild)
};
goog.dom.htmlToDocumentFragment = function (a) {
    return goog.dom.htmlToDocumentFragment_(document, a)
};
goog.dom.htmlToDocumentFragment_ = function (a, b) {
    var c = a.createElement("div");
    goog.dom.BrowserFeature.INNER_HTML_NEEDS_SCOPED_ELEMENT ? (c.innerHTML = "<br>" + b, c.removeChild(c.firstChild)) : c.innerHTML = b;
    if (1 == c.childNodes.length)return c.removeChild(c.firstChild);
    for (var d = a.createDocumentFragment(); c.firstChild;)d.appendChild(c.firstChild);
    return d
};
goog.dom.getCompatMode = function () {
    return goog.dom.isCss1CompatMode() ? "CSS1Compat" : "BackCompat"
};
goog.dom.isCss1CompatMode = function () {
    return goog.dom.isCss1CompatMode_(document)
};
goog.dom.isCss1CompatMode_ = function (a) {
    return goog.dom.COMPAT_MODE_KNOWN_ ? goog.dom.ASSUME_STANDARDS_MODE : "CSS1Compat" == a.compatMode
};
goog.dom.canHaveChildren = function (a) {
    if (a.nodeType != goog.dom.NodeType.ELEMENT)return!1;
    switch (a.tagName) {
        case goog.dom.TagName.APPLET:
        case goog.dom.TagName.AREA:
        case goog.dom.TagName.BASE:
        case goog.dom.TagName.BR:
        case goog.dom.TagName.COL:
        case goog.dom.TagName.COMMAND:
        case goog.dom.TagName.EMBED:
        case goog.dom.TagName.FRAME:
        case goog.dom.TagName.HR:
        case goog.dom.TagName.IMG:
        case goog.dom.TagName.INPUT:
        case goog.dom.TagName.IFRAME:
        case goog.dom.TagName.ISINDEX:
        case goog.dom.TagName.KEYGEN:
        case goog.dom.TagName.LINK:
        case goog.dom.TagName.NOFRAMES:
        case goog.dom.TagName.NOSCRIPT:
        case goog.dom.TagName.META:
        case goog.dom.TagName.OBJECT:
        case goog.dom.TagName.PARAM:
        case goog.dom.TagName.SCRIPT:
        case goog.dom.TagName.SOURCE:
        case goog.dom.TagName.STYLE:
        case goog.dom.TagName.TRACK:
        case goog.dom.TagName.WBR:
            return!1
    }
    return!0
};
goog.dom.appendChild = function (a, b) {
    a.appendChild(b)
};
goog.dom.append = function (a, b) {
    goog.dom.append_(goog.dom.getOwnerDocument(a), a, arguments, 1)
};
goog.dom.removeChildren = function (a) {
    for (var b; b = a.firstChild;)a.removeChild(b)
};
goog.dom.insertSiblingBefore = function (a, b) {
    b.parentNode && b.parentNode.insertBefore(a, b)
};
goog.dom.insertSiblingAfter = function (a, b) {
    b.parentNode && b.parentNode.insertBefore(a, b.nextSibling)
};
goog.dom.insertChildAt = function (a, b, c) {
    a.insertBefore(b, a.childNodes[c] || null)
};
goog.dom.removeNode = function (a) {
    return a && a.parentNode ? a.parentNode.removeChild(a) : null
};
goog.dom.replaceNode = function (a, b) {
    var c = b.parentNode;
    c && c.replaceChild(a, b)
};
goog.dom.flattenElement = function (a) {
    var b, c = a.parentNode;
    if (c && c.nodeType != goog.dom.NodeType.DOCUMENT_FRAGMENT) {
        if (a.removeNode)return a.removeNode(!1);
        for (; b = a.firstChild;)c.insertBefore(b, a);
        return goog.dom.removeNode(a)
    }
};
goog.dom.getChildren = function (a) {
    return goog.dom.BrowserFeature.CAN_USE_CHILDREN_ATTRIBUTE && void 0 != a.children ? a.children : goog.array.filter(a.childNodes, function (a) {
        return a.nodeType == goog.dom.NodeType.ELEMENT
    })
};
goog.dom.getFirstElementChild = function (a) {
    return void 0 != a.firstElementChild ? a.firstElementChild : goog.dom.getNextElementNode_(a.firstChild, !0)
};
goog.dom.getLastElementChild = function (a) {
    return void 0 != a.lastElementChild ? a.lastElementChild : goog.dom.getNextElementNode_(a.lastChild, !1)
};
goog.dom.getNextElementSibling = function (a) {
    return void 0 != a.nextElementSibling ? a.nextElementSibling : goog.dom.getNextElementNode_(a.nextSibling, !0)
};
goog.dom.getPreviousElementSibling = function (a) {
    return void 0 != a.previousElementSibling ? a.previousElementSibling : goog.dom.getNextElementNode_(a.previousSibling, !1)
};
goog.dom.getNextElementNode_ = function (a, b) {
    for (; a && a.nodeType != goog.dom.NodeType.ELEMENT;)a = b ? a.nextSibling : a.previousSibling;
    return a
};
goog.dom.getNextNode = function (a) {
    if (!a)return null;
    if (a.firstChild)return a.firstChild;
    for (; a && !a.nextSibling;)a = a.parentNode;
    return a ? a.nextSibling : null
};
goog.dom.getPreviousNode = function (a) {
    if (!a)return null;
    if (!a.previousSibling)return a.parentNode;
    for (a = a.previousSibling; a && a.lastChild;)a = a.lastChild;
    return a
};
goog.dom.isNodeLike = function (a) {
    return goog.isObject(a) && 0 < a.nodeType
};
goog.dom.isElement = function (a) {
    return goog.isObject(a) && a.nodeType == goog.dom.NodeType.ELEMENT
};
goog.dom.isWindow = function (a) {
    return goog.isObject(a) && a.window == a
};
goog.dom.getParentElement = function (a) {
    if (goog.dom.BrowserFeature.CAN_USE_PARENT_ELEMENT_PROPERTY && !(goog.userAgent.IE && goog.userAgent.isVersionOrHigher("9") && !goog.userAgent.isVersionOrHigher("10") && goog.global.SVGElement && a instanceof goog.global.SVGElement))return a.parentElement;
    a = a.parentNode;
    return goog.dom.isElement(a) ? a : null
};
goog.dom.contains = function (a, b) {
    if (a.contains && b.nodeType == goog.dom.NodeType.ELEMENT)return a == b || a.contains(b);
    if ("undefined" != typeof a.compareDocumentPosition)return a == b || Boolean(a.compareDocumentPosition(b) & 16);
    for (; b && a != b;)b = b.parentNode;
    return b == a
};
goog.dom.compareNodeOrder = function (a, b) {
    if (a == b)return 0;
    if (a.compareDocumentPosition)return a.compareDocumentPosition(b) & 2 ? 1 : -1;
    if (goog.userAgent.IE && !goog.userAgent.isDocumentModeOrHigher(9)) {
        if (a.nodeType == goog.dom.NodeType.DOCUMENT)return-1;
        if (b.nodeType == goog.dom.NodeType.DOCUMENT)return 1
    }
    if ("sourceIndex"in a || a.parentNode && "sourceIndex"in a.parentNode) {
        var c = a.nodeType == goog.dom.NodeType.ELEMENT, d = b.nodeType == goog.dom.NodeType.ELEMENT;
        if (c && d)return a.sourceIndex - b.sourceIndex;
        var e = a.parentNode,
            f = b.parentNode;
        return e == f ? goog.dom.compareSiblingOrder_(a, b) : !c && goog.dom.contains(e, b) ? -1 * goog.dom.compareParentsDescendantNodeIe_(a, b) : !d && goog.dom.contains(f, a) ? goog.dom.compareParentsDescendantNodeIe_(b, a) : (c ? a.sourceIndex : e.sourceIndex) - (d ? b.sourceIndex : f.sourceIndex)
    }
    d = goog.dom.getOwnerDocument(a);
    c = d.createRange();
    c.selectNode(a);
    c.collapse(!0);
    d = d.createRange();
    d.selectNode(b);
    d.collapse(!0);
    return c.compareBoundaryPoints(goog.global.Range.START_TO_END, d)
};
goog.dom.compareParentsDescendantNodeIe_ = function (a, b) {
    var c = a.parentNode;
    if (c == b)return-1;
    for (var d = b; d.parentNode != c;)d = d.parentNode;
    return goog.dom.compareSiblingOrder_(d, a)
};
goog.dom.compareSiblingOrder_ = function (a, b) {
    for (var c = b; c = c.previousSibling;)if (c == a)return-1;
    return 1
};
goog.dom.findCommonAncestor = function (a) {
    var b, c = arguments.length;
    if (!c)return null;
    if (1 == c)return arguments[0];
    var d = [], e = Infinity;
    for (b = 0; b < c; b++) {
        for (var f = [], g = arguments[b]; g;)f.unshift(g), g = g.parentNode;
        d.push(f);
        e = Math.min(e, f.length)
    }
    f = null;
    for (b = 0; b < e; b++) {
        for (var g = d[0][b], h = 1; h < c; h++)if (g != d[h][b])return f;
        f = g
    }
    return f
};
goog.dom.getOwnerDocument = function (a) {
    return a.nodeType == goog.dom.NodeType.DOCUMENT ? a : a.ownerDocument || a.document
};
goog.dom.getFrameContentDocument = function (a) {
    return a.contentDocument || a.contentWindow.document
};
goog.dom.getFrameContentWindow = function (a) {
    return a.contentWindow || goog.dom.getWindow_(goog.dom.getFrameContentDocument(a))
};
goog.dom.setTextContent = function (a, b) {
    if ("textContent"in a)a.textContent = b; else if (a.firstChild && a.firstChild.nodeType == goog.dom.NodeType.TEXT) {
        for (; a.lastChild != a.firstChild;)a.removeChild(a.lastChild);
        a.firstChild.data = b
    } else {
        goog.dom.removeChildren(a);
        var c = goog.dom.getOwnerDocument(a);
        a.appendChild(c.createTextNode(String(b)))
    }
};
goog.dom.getOuterHtml = function (a) {
    if ("outerHTML"in a)return a.outerHTML;
    var b = goog.dom.getOwnerDocument(a).createElement("div");
    b.appendChild(a.cloneNode(!0));
    return b.innerHTML
};
goog.dom.findNode = function (a, b) {
    var c = [];
    return goog.dom.findNodes_(a, b, c, !0) ? c[0] : void 0
};
goog.dom.findNodes = function (a, b) {
    var c = [];
    goog.dom.findNodes_(a, b, c, !1);
    return c
};
goog.dom.findNodes_ = function (a, b, c, d) {
    if (null != a)for (a = a.firstChild; a;) {
        if (b(a) && (c.push(a), d) || goog.dom.findNodes_(a, b, c, d))return!0;
        a = a.nextSibling
    }
    return!1
};
goog.dom.TAGS_TO_IGNORE_ = {SCRIPT: 1, STYLE: 1, HEAD: 1, IFRAME: 1, OBJECT: 1};
goog.dom.PREDEFINED_TAG_VALUES_ = {IMG: " ", BR: "\n"};
goog.dom.isFocusableTabIndex = function (a) {
    var b = a.getAttributeNode("tabindex");
    return b && b.specified ? (a = a.tabIndex, goog.isNumber(a) && 0 <= a && 32768 > a) : !1
};
goog.dom.setFocusableTabIndex = function (a, b) {
    b ? a.tabIndex = 0 : (a.tabIndex = -1, a.removeAttribute("tabIndex"))
};
goog.dom.getTextContent = function (a) {
    if (goog.dom.BrowserFeature.CAN_USE_INNER_TEXT && "innerText"in a)a = goog.string.canonicalizeNewlines(a.innerText); else {
        var b = [];
        goog.dom.getTextContent_(a, b, !0);
        a = b.join("")
    }
    a = a.replace(/ \xAD /g, " ").replace(/\xAD/g, "");
    a = a.replace(/\u200B/g, "");
    goog.dom.BrowserFeature.CAN_USE_INNER_TEXT || (a = a.replace(/ +/g, " "));
    " " != a && (a = a.replace(/^\s*/, ""));
    return a
};
goog.dom.getRawTextContent = function (a) {
    var b = [];
    goog.dom.getTextContent_(a, b, !1);
    return b.join("")
};
goog.dom.getTextContent_ = function (a, b, c) {
    if (!(a.nodeName in goog.dom.TAGS_TO_IGNORE_))if (a.nodeType == goog.dom.NodeType.TEXT)c ? b.push(String(a.nodeValue).replace(/(\r\n|\r|\n)/g, "")) : b.push(a.nodeValue); else if (a.nodeName in goog.dom.PREDEFINED_TAG_VALUES_)b.push(goog.dom.PREDEFINED_TAG_VALUES_[a.nodeName]); else for (a = a.firstChild; a;)goog.dom.getTextContent_(a, b, c), a = a.nextSibling
};
goog.dom.getNodeTextLength = function (a) {
    return goog.dom.getTextContent(a).length
};
goog.dom.getNodeTextOffset = function (a, b) {
    for (var c = b || goog.dom.getOwnerDocument(a).body, d = []; a && a != c;) {
        for (var e = a; e = e.previousSibling;)d.unshift(goog.dom.getTextContent(e));
        a = a.parentNode
    }
    return goog.string.trimLeft(d.join("")).replace(/ +/g, " ").length
};
goog.dom.getNodeAtOffset = function (a, b, c) {
    a = [a];
    for (var d = 0, e = null; 0 < a.length && d < b;)if (e = a.pop(), !(e.nodeName in goog.dom.TAGS_TO_IGNORE_))if (e.nodeType == goog.dom.NodeType.TEXT)var f = e.nodeValue.replace(/(\r\n|\r|\n)/g, "").replace(/ +/g, " "), d = d + f.length; else if (e.nodeName in goog.dom.PREDEFINED_TAG_VALUES_)d += goog.dom.PREDEFINED_TAG_VALUES_[e.nodeName].length; else for (f = e.childNodes.length - 1; 0 <= f; f--)a.push(e.childNodes[f]);
    goog.isObject(c) && (c.remainder = e ? e.nodeValue.length + b - d - 1 : 0, c.node = e);
    return e
};
goog.dom.isNodeList = function (a) {
    if (a && "number" == typeof a.length) {
        if (goog.isObject(a))return"function" == typeof a.item || "string" == typeof a.item;
        if (goog.isFunction(a))return"function" == typeof a.item
    }
    return!1
};
goog.dom.getAncestorByTagNameAndClass = function (a, b, c) {
    if (!b && !c)return null;
    var d = b ? b.toUpperCase() : null;
    return goog.dom.getAncestor(a, function (a) {
        return(!d || a.nodeName == d) && (!c || goog.dom.classes.has(a, c))
    }, !0)
};
goog.dom.getAncestorByClass = function (a, b) {
    return goog.dom.getAncestorByTagNameAndClass(a, null, b)
};
goog.dom.getAncestor = function (a, b, c, d) {
    c || (a = a.parentNode);
    c = null == d;
    for (var e = 0; a && (c || e <= d);) {
        if (b(a))return a;
        a = a.parentNode;
        e++
    }
    return null
};
goog.dom.getActiveElement = function (a) {
    try {
        return a && a.activeElement
    } catch (b) {
    }
    return null
};
goog.dom.DomHelper = function (a) {
    this.document_ = a || goog.global.document || document
};
goog.dom.DomHelper.prototype.getDomHelper = goog.dom.getDomHelper;
goog.dom.DomHelper.prototype.setDocument = function (a) {
    this.document_ = a
};
goog.dom.DomHelper.prototype.getDocument = function () {
    return this.document_
};
goog.dom.DomHelper.prototype.getElement = function (a) {
    return goog.isString(a) ? this.document_.getElementById(a) : a
};
goog.dom.DomHelper.prototype.$ = goog.dom.DomHelper.prototype.getElement;
goog.dom.DomHelper.prototype.getElementsByTagNameAndClass = function (a, b, c) {
    return goog.dom.getElementsByTagNameAndClass_(this.document_, a, b, c)
};
goog.dom.DomHelper.prototype.getElementsByClass = function (a, b) {
    return goog.dom.getElementsByClass(a, b || this.document_)
};
goog.dom.DomHelper.prototype.getElementByClass = function (a, b) {
    return goog.dom.getElementByClass(a, b || this.document_)
};
goog.dom.DomHelper.prototype.$$ = goog.dom.DomHelper.prototype.getElementsByTagNameAndClass;
goog.dom.DomHelper.prototype.setProperties = goog.dom.setProperties;
goog.dom.DomHelper.prototype.getViewportSize = function (a) {
    return goog.dom.getViewportSize(a || this.getWindow())
};
goog.dom.DomHelper.prototype.getDocumentHeight = function () {
    return goog.dom.getDocumentHeight_(this.getWindow())
};
goog.dom.DomHelper.prototype.createDom = function (a, b, c) {
    return goog.dom.createDom_(this.document_, arguments)
};
goog.dom.DomHelper.prototype.$dom = goog.dom.DomHelper.prototype.createDom;
goog.dom.DomHelper.prototype.createElement = function (a) {
    return this.document_.createElement(a)
};
goog.dom.DomHelper.prototype.createTextNode = function (a) {
    return this.document_.createTextNode(String(a))
};
goog.dom.DomHelper.prototype.createTable = function (a, b, c) {
    return goog.dom.createTable_(this.document_, a, b, !!c)
};
goog.dom.DomHelper.prototype.htmlToDocumentFragment = function (a) {
    return goog.dom.htmlToDocumentFragment_(this.document_, a)
};
goog.dom.DomHelper.prototype.getCompatMode = function () {
    return this.isCss1CompatMode() ? "CSS1Compat" : "BackCompat"
};
goog.dom.DomHelper.prototype.isCss1CompatMode = function () {
    return goog.dom.isCss1CompatMode_(this.document_)
};
goog.dom.DomHelper.prototype.getWindow = function () {
    return goog.dom.getWindow_(this.document_)
};
goog.dom.DomHelper.prototype.getDocumentScrollElement = function () {
    return goog.dom.getDocumentScrollElement_(this.document_)
};
goog.dom.DomHelper.prototype.getDocumentScroll = function () {
    return goog.dom.getDocumentScroll_(this.document_)
};
goog.dom.DomHelper.prototype.getActiveElement = function (a) {
    return goog.dom.getActiveElement(a || this.document_)
};
goog.dom.DomHelper.prototype.appendChild = goog.dom.appendChild;
goog.dom.DomHelper.prototype.append = goog.dom.append;
goog.dom.DomHelper.prototype.canHaveChildren = goog.dom.canHaveChildren;
goog.dom.DomHelper.prototype.removeChildren = goog.dom.removeChildren;
goog.dom.DomHelper.prototype.insertSiblingBefore = goog.dom.insertSiblingBefore;
goog.dom.DomHelper.prototype.insertSiblingAfter = goog.dom.insertSiblingAfter;
goog.dom.DomHelper.prototype.insertChildAt = goog.dom.insertChildAt;
goog.dom.DomHelper.prototype.removeNode = goog.dom.removeNode;
goog.dom.DomHelper.prototype.replaceNode = goog.dom.replaceNode;
goog.dom.DomHelper.prototype.flattenElement = goog.dom.flattenElement;
goog.dom.DomHelper.prototype.getChildren = goog.dom.getChildren;
goog.dom.DomHelper.prototype.getFirstElementChild = goog.dom.getFirstElementChild;
goog.dom.DomHelper.prototype.getLastElementChild = goog.dom.getLastElementChild;
goog.dom.DomHelper.prototype.getNextElementSibling = goog.dom.getNextElementSibling;
goog.dom.DomHelper.prototype.getPreviousElementSibling = goog.dom.getPreviousElementSibling;
goog.dom.DomHelper.prototype.getNextNode = goog.dom.getNextNode;
goog.dom.DomHelper.prototype.getPreviousNode = goog.dom.getPreviousNode;
goog.dom.DomHelper.prototype.isNodeLike = goog.dom.isNodeLike;
goog.dom.DomHelper.prototype.isElement = goog.dom.isElement;
goog.dom.DomHelper.prototype.isWindow = goog.dom.isWindow;
goog.dom.DomHelper.prototype.getParentElement = goog.dom.getParentElement;
goog.dom.DomHelper.prototype.contains = goog.dom.contains;
goog.dom.DomHelper.prototype.compareNodeOrder = goog.dom.compareNodeOrder;
goog.dom.DomHelper.prototype.findCommonAncestor = goog.dom.findCommonAncestor;
goog.dom.DomHelper.prototype.getOwnerDocument = goog.dom.getOwnerDocument;
goog.dom.DomHelper.prototype.getFrameContentDocument = goog.dom.getFrameContentDocument;
goog.dom.DomHelper.prototype.getFrameContentWindow = goog.dom.getFrameContentWindow;
goog.dom.DomHelper.prototype.setTextContent = goog.dom.setTextContent;
goog.dom.DomHelper.prototype.getOuterHtml = goog.dom.getOuterHtml;
goog.dom.DomHelper.prototype.findNode = goog.dom.findNode;
goog.dom.DomHelper.prototype.findNodes = goog.dom.findNodes;
goog.dom.DomHelper.prototype.isFocusableTabIndex = goog.dom.isFocusableTabIndex;
goog.dom.DomHelper.prototype.setFocusableTabIndex = goog.dom.setFocusableTabIndex;
goog.dom.DomHelper.prototype.getTextContent = goog.dom.getTextContent;
goog.dom.DomHelper.prototype.getNodeTextLength = goog.dom.getNodeTextLength;
goog.dom.DomHelper.prototype.getNodeTextOffset = goog.dom.getNodeTextOffset;
goog.dom.DomHelper.prototype.getNodeAtOffset = goog.dom.getNodeAtOffset;
goog.dom.DomHelper.prototype.isNodeList = goog.dom.isNodeList;
goog.dom.DomHelper.prototype.getAncestorByTagNameAndClass = goog.dom.getAncestorByTagNameAndClass;
goog.dom.DomHelper.prototype.getAncestorByClass = goog.dom.getAncestorByClass;
goog.dom.DomHelper.prototype.getAncestor = goog.dom.getAncestor;
goog.math.Box = function (a, b, c, d) {
    this.top = a;
    this.right = b;
    this.bottom = c;
    this.left = d
};
goog.math.Box.boundingBox = function (a) {
    for (var b = new goog.math.Box(arguments[0].y, arguments[0].x, arguments[0].y, arguments[0].x), c = 1; c < arguments.length; c++) {
        var d = arguments[c];
        b.top = Math.min(b.top, d.y);
        b.right = Math.max(b.right, d.x);
        b.bottom = Math.max(b.bottom, d.y);
        b.left = Math.min(b.left, d.x)
    }
    return b
};
goog.math.Box.prototype.clone = function () {
    return new goog.math.Box(this.top, this.right, this.bottom, this.left)
};
goog.DEBUG && (goog.math.Box.prototype.toString = function () {
    return"(" + this.top + "t, " + this.right + "r, " + this.bottom + "b, " + this.left + "l)"
});
goog.math.Box.prototype.contains = function (a) {
    return goog.math.Box.contains(this, a)
};
goog.math.Box.prototype.expand = function (a, b, c, d) {
    goog.isObject(a) ? (this.top -= a.top, this.right += a.right, this.bottom += a.bottom, this.left -= a.left) : (this.top -= a, this.right += b, this.bottom += c, this.left -= d);
    return this
};
goog.math.Box.prototype.expandToInclude = function (a) {
    this.left = Math.min(this.left, a.left);
    this.top = Math.min(this.top, a.top);
    this.right = Math.max(this.right, a.right);
    this.bottom = Math.max(this.bottom, a.bottom)
};
goog.math.Box.equals = function (a, b) {
    return a == b ? !0 : a && b ? a.top == b.top && a.right == b.right && a.bottom == b.bottom && a.left == b.left : !1
};
goog.math.Box.contains = function (a, b) {
    return a && b ? b instanceof goog.math.Box ? b.left >= a.left && b.right <= a.right && b.top >= a.top && b.bottom <= a.bottom : b.x >= a.left && b.x <= a.right && b.y >= a.top && b.y <= a.bottom : !1
};
goog.math.Box.relativePositionX = function (a, b) {
    return b.x < a.left ? b.x - a.left : b.x > a.right ? b.x - a.right : 0
};
goog.math.Box.relativePositionY = function (a, b) {
    return b.y < a.top ? b.y - a.top : b.y > a.bottom ? b.y - a.bottom : 0
};
goog.math.Box.distance = function (a, b) {
    var c = goog.math.Box.relativePositionX(a, b), d = goog.math.Box.relativePositionY(a, b);
    return Math.sqrt(c * c + d * d)
};
goog.math.Box.intersects = function (a, b) {
    return a.left <= b.right && b.left <= a.right && a.top <= b.bottom && b.top <= a.bottom
};
goog.math.Box.intersectsWithPadding = function (a, b, c) {
    return a.left <= b.right + c && b.left <= a.right + c && a.top <= b.bottom + c && b.top <= a.bottom + c
};
goog.math.Box.prototype.ceil = function () {
    this.top = Math.ceil(this.top);
    this.right = Math.ceil(this.right);
    this.bottom = Math.ceil(this.bottom);
    this.left = Math.ceil(this.left);
    return this
};
goog.math.Box.prototype.floor = function () {
    this.top = Math.floor(this.top);
    this.right = Math.floor(this.right);
    this.bottom = Math.floor(this.bottom);
    this.left = Math.floor(this.left);
    return this
};
goog.math.Box.prototype.round = function () {
    this.top = Math.round(this.top);
    this.right = Math.round(this.right);
    this.bottom = Math.round(this.bottom);
    this.left = Math.round(this.left);
    return this
};
goog.math.Box.prototype.translate = function (a, b) {
    a instanceof goog.math.Coordinate ? (this.left += a.x, this.right += a.x, this.top += a.y, this.bottom += a.y) : (this.left += a, this.right += a, goog.isNumber(b) && (this.top += b, this.bottom += b));
    return this
};
goog.math.Box.prototype.scale = function (a, b) {
    var c = goog.isNumber(b) ? b : a;
    this.left *= a;
    this.right *= a;
    this.top *= c;
    this.bottom *= c;
    return this
};
goog.math.Rect = function (a, b, c, d) {
    this.left = a;
    this.top = b;
    this.width = c;
    this.height = d
};
goog.math.Rect.prototype.clone = function () {
    return new goog.math.Rect(this.left, this.top, this.width, this.height)
};
goog.math.Rect.prototype.toBox = function () {
    return new goog.math.Box(this.top, this.left + this.width, this.top + this.height, this.left)
};
goog.math.Rect.createFromBox = function (a) {
    return new goog.math.Rect(a.left, a.top, a.right - a.left, a.bottom - a.top)
};
goog.DEBUG && (goog.math.Rect.prototype.toString = function () {
    return"(" + this.left + ", " + this.top + " - " + this.width + "w x " + this.height + "h)"
});
goog.math.Rect.equals = function (a, b) {
    return a == b ? !0 : a && b ? a.left == b.left && a.width == b.width && a.top == b.top && a.height == b.height : !1
};
goog.math.Rect.prototype.intersection = function (a) {
    var b = Math.max(this.left, a.left), c = Math.min(this.left + this.width, a.left + a.width);
    if (b <= c) {
        var d = Math.max(this.top, a.top);
        a = Math.min(this.top + this.height, a.top + a.height);
        if (d <= a)return this.left = b, this.top = d, this.width = c - b, this.height = a - d, !0
    }
    return!1
};
goog.math.Rect.intersection = function (a, b) {
    var c = Math.max(a.left, b.left), d = Math.min(a.left + a.width, b.left + b.width);
    if (c <= d) {
        var e = Math.max(a.top, b.top), f = Math.min(a.top + a.height, b.top + b.height);
        if (e <= f)return new goog.math.Rect(c, e, d - c, f - e)
    }
    return null
};
goog.math.Rect.intersects = function (a, b) {
    return a.left <= b.left + b.width && b.left <= a.left + a.width && a.top <= b.top + b.height && b.top <= a.top + a.height
};
goog.math.Rect.prototype.intersects = function (a) {
    return goog.math.Rect.intersects(this, a)
};
goog.math.Rect.difference = function (a, b) {
    var c = goog.math.Rect.intersection(a, b);
    if (!c || !c.height || !c.width)return[a.clone()];
    var c = [], d = a.top, e = a.height, f = a.left + a.width, g = a.top + a.height, h = b.left + b.width, k = b.top + b.height;
    b.top > a.top && (c.push(new goog.math.Rect(a.left, a.top, a.width, b.top - a.top)), d = b.top, e -= b.top - a.top);
    k < g && (c.push(new goog.math.Rect(a.left, k, a.width, g - k)), e = k - d);
    b.left > a.left && c.push(new goog.math.Rect(a.left, d, b.left - a.left, e));
    h < f && c.push(new goog.math.Rect(h, d, f - h, e));
    return c
};
goog.math.Rect.prototype.difference = function (a) {
    return goog.math.Rect.difference(this, a)
};
goog.math.Rect.prototype.boundingRect = function (a) {
    var b = Math.max(this.left + this.width, a.left + a.width), c = Math.max(this.top + this.height, a.top + a.height);
    this.left = Math.min(this.left, a.left);
    this.top = Math.min(this.top, a.top);
    this.width = b - this.left;
    this.height = c - this.top
};
goog.math.Rect.boundingRect = function (a, b) {
    if (!a || !b)return null;
    var c = a.clone();
    c.boundingRect(b);
    return c
};
goog.math.Rect.prototype.contains = function (a) {
    return a instanceof goog.math.Rect ? this.left <= a.left && this.left + this.width >= a.left + a.width && this.top <= a.top && this.top + this.height >= a.top + a.height : a.x >= this.left && a.x <= this.left + this.width && a.y >= this.top && a.y <= this.top + this.height
};
goog.math.Rect.prototype.squaredDistance = function (a) {
    var b = a.x < this.left ? this.left - a.x : Math.max(a.x - (this.left + this.width), 0);
    a = a.y < this.top ? this.top - a.y : Math.max(a.y - (this.top + this.height), 0);
    return b * b + a * a
};
goog.math.Rect.prototype.distance = function (a) {
    return Math.sqrt(this.squaredDistance(a))
};
goog.math.Rect.prototype.getSize = function () {
    return new goog.math.Size(this.width, this.height)
};
goog.math.Rect.prototype.getTopLeft = function () {
    return new goog.math.Coordinate(this.left, this.top)
};
goog.math.Rect.prototype.getCenter = function () {
    return new goog.math.Coordinate(this.left + this.width / 2, this.top + this.height / 2)
};
goog.math.Rect.prototype.getBottomRight = function () {
    return new goog.math.Coordinate(this.left + this.width, this.top + this.height)
};
goog.math.Rect.prototype.ceil = function () {
    this.left = Math.ceil(this.left);
    this.top = Math.ceil(this.top);
    this.width = Math.ceil(this.width);
    this.height = Math.ceil(this.height);
    return this
};
goog.math.Rect.prototype.floor = function () {
    this.left = Math.floor(this.left);
    this.top = Math.floor(this.top);
    this.width = Math.floor(this.width);
    this.height = Math.floor(this.height);
    return this
};
goog.math.Rect.prototype.round = function () {
    this.left = Math.round(this.left);
    this.top = Math.round(this.top);
    this.width = Math.round(this.width);
    this.height = Math.round(this.height);
    return this
};
goog.math.Rect.prototype.translate = function (a, b) {
    a instanceof goog.math.Coordinate ? (this.left += a.x, this.top += a.y) : (this.left += a, goog.isNumber(b) && (this.top += b));
    return this
};
goog.math.Rect.prototype.scale = function (a, b) {
    var c = goog.isNumber(b) ? b : a;
    this.left *= a;
    this.width *= a;
    this.top *= c;
    this.height *= c;
    return this
};
goog.dom.vendor = {};
goog.dom.vendor.getVendorJsPrefix = function () {
    return goog.userAgent.WEBKIT ? "Webkit" : goog.userAgent.GECKO ? "Moz" : goog.userAgent.IE ? "ms" : goog.userAgent.OPERA ? "O" : null
};
goog.dom.vendor.getVendorPrefix = function () {
    return goog.userAgent.WEBKIT ? "-webkit" : goog.userAgent.GECKO ? "-moz" : goog.userAgent.IE ? "-ms" : goog.userAgent.OPERA ? "-o" : null
};
goog.style = {};
goog.style.GET_BOUNDING_CLIENT_RECT_ALWAYS_EXISTS = !1;
goog.style.setStyle = function (a, b, c) {
    goog.isString(b) ? goog.style.setStyle_(a, c, b) : goog.object.forEach(b, goog.partial(goog.style.setStyle_, a))
};
goog.style.setStyle_ = function (a, b, c) {
    (c = goog.style.getVendorJsStyleName_(a, c)) && (a.style[c] = b)
};
goog.style.getVendorJsStyleName_ = function (a, b) {
    var c = goog.string.toCamelCase(b);
    if (void 0 === a.style[c]) {
        var d = goog.dom.vendor.getVendorJsPrefix() + goog.string.toTitleCase(b);
        if (void 0 !== a.style[d])return d
    }
    return c
};
goog.style.getVendorStyleName_ = function (a, b) {
    var c = goog.string.toCamelCase(b);
    return void 0 === a.style[c] && (c = goog.dom.vendor.getVendorJsPrefix() + goog.string.toTitleCase(b), void 0 !== a.style[c]) ? goog.dom.vendor.getVendorPrefix() + "-" + b : b
};
goog.style.getStyle = function (a, b) {
    var c = a.style[goog.string.toCamelCase(b)];
    return"undefined" !== typeof c ? c : a.style[goog.style.getVendorJsStyleName_(a, b)] || ""
};
goog.style.getComputedStyle = function (a, b) {
    var c = goog.dom.getOwnerDocument(a);
    return c.defaultView && c.defaultView.getComputedStyle && (c = c.defaultView.getComputedStyle(a, null)) ? c[b] || c.getPropertyValue(b) || "" : ""
};
goog.style.getCascadedStyle = function (a, b) {
    return a.currentStyle ? a.currentStyle[b] : null
};
goog.style.getStyle_ = function (a, b) {
    return goog.style.getComputedStyle(a, b) || goog.style.getCascadedStyle(a, b) || a.style && a.style[b]
};
goog.style.getComputedPosition = function (a) {
    return goog.style.getStyle_(a, "position")
};
goog.style.getBackgroundColor = function (a) {
    return goog.style.getStyle_(a, "backgroundColor")
};
goog.style.getComputedOverflowX = function (a) {
    return goog.style.getStyle_(a, "overflowX")
};
goog.style.getComputedOverflowY = function (a) {
    return goog.style.getStyle_(a, "overflowY")
};
goog.style.getComputedZIndex = function (a) {
    return goog.style.getStyle_(a, "zIndex")
};
goog.style.getComputedTextAlign = function (a) {
    return goog.style.getStyle_(a, "textAlign")
};
goog.style.getComputedCursor = function (a) {
    return goog.style.getStyle_(a, "cursor")
};
goog.style.setPosition = function (a, b, c) {
    var d, e = goog.userAgent.GECKO && (goog.userAgent.MAC || goog.userAgent.X11) && goog.userAgent.isVersionOrHigher("1.9");
    b instanceof goog.math.Coordinate ? (d = b.x, b = b.y) : (d = b, b = c);
    a.style.left = goog.style.getPixelStyleValue_(d, e);
    a.style.top = goog.style.getPixelStyleValue_(b, e)
};
goog.style.getPosition = function (a) {
    return new goog.math.Coordinate(a.offsetLeft, a.offsetTop)
};
goog.style.getClientViewportElement = function (a) {
    a = a ? goog.dom.getOwnerDocument(a) : goog.dom.getDocument();
    return!goog.userAgent.IE || goog.userAgent.isDocumentModeOrHigher(9) || goog.dom.getDomHelper(a).isCss1CompatMode() ? a.documentElement : a.body
};
goog.style.getViewportPageOffset = function (a) {
    var b = a.body;
    a = a.documentElement;
    return new goog.math.Coordinate(b.scrollLeft || a.scrollLeft, b.scrollTop || a.scrollTop)
};
goog.style.getBoundingClientRect_ = function (a) {
    var b;
    try {
        b = a.getBoundingClientRect()
    } catch (c) {
        return{left: 0, top: 0, right: 0, bottom: 0}
    }
    goog.userAgent.IE && (a = a.ownerDocument, b.left -= a.documentElement.clientLeft + a.body.clientLeft, b.top -= a.documentElement.clientTop + a.body.clientTop);
    return b
};
goog.style.getOffsetParent = function (a) {
    if (goog.userAgent.IE && !goog.userAgent.isDocumentModeOrHigher(8))return a.offsetParent;
    var b = goog.dom.getOwnerDocument(a), c = goog.style.getStyle_(a, "position"), d = "fixed" == c || "absolute" == c;
    for (a = a.parentNode; a && a != b; a = a.parentNode)if (c = goog.style.getStyle_(a, "position"), d = d && "static" == c && a != b.documentElement && a != b.body, !d && (a.scrollWidth > a.clientWidth || a.scrollHeight > a.clientHeight || "fixed" == c || "absolute" == c || "relative" == c))return a;
    return null
};
goog.style.getVisibleRectForElement = function (a) {
    for (var b = new goog.math.Box(0, Infinity, Infinity, 0), c = goog.dom.getDomHelper(a), d = c.getDocument().body, e = c.getDocument().documentElement, f = c.getDocumentScrollElement(); a = goog.style.getOffsetParent(a);)if (!(goog.userAgent.IE && 0 == a.clientWidth || goog.userAgent.WEBKIT && 0 == a.clientHeight && a == d || a == d || a == e || "visible" == goog.style.getStyle_(a, "overflow"))) {
        var g = goog.style.getPageOffset(a), h = goog.style.getClientLeftTop(a);
        g.x += h.x;
        g.y += h.y;
        b.top = Math.max(b.top,
            g.y);
        b.right = Math.min(b.right, g.x + a.clientWidth);
        b.bottom = Math.min(b.bottom, g.y + a.clientHeight);
        b.left = Math.max(b.left, g.x)
    }
    d = f.scrollLeft;
    f = f.scrollTop;
    b.left = Math.max(b.left, d);
    b.top = Math.max(b.top, f);
    c = c.getViewportSize();
    b.right = Math.min(b.right, d + c.width);
    b.bottom = Math.min(b.bottom, f + c.height);
    return 0 <= b.top && 0 <= b.left && b.bottom > b.top && b.right > b.left ? b : null
};
goog.style.getContainerOffsetToScrollInto = function (a, b, c) {
    var d = goog.style.getPageOffset(a), e = goog.style.getPageOffset(b), f = goog.style.getBorderBox(b), g = d.x - e.x - f.left, d = d.y - e.y - f.top, e = b.clientWidth - a.offsetWidth;
    a = b.clientHeight - a.offsetHeight;
    f = b.scrollLeft;
    b = b.scrollTop;
    c ? (f += g - e / 2, b += d - a / 2) : (f += Math.min(g, Math.max(g - e, 0)), b += Math.min(d, Math.max(d - a, 0)));
    return new goog.math.Coordinate(f, b)
};
goog.style.scrollIntoContainerView = function (a, b, c) {
    a = goog.style.getContainerOffsetToScrollInto(a, b, c);
    b.scrollLeft = a.x;
    b.scrollTop = a.y
};
goog.style.getClientLeftTop = function (a) {
    if (goog.userAgent.GECKO && !goog.userAgent.isVersionOrHigher("1.9")) {
        var b = parseFloat(goog.style.getComputedStyle(a, "borderLeftWidth"));
        if (goog.style.isRightToLeft(a))var c = a.offsetWidth - a.clientWidth - b - parseFloat(goog.style.getComputedStyle(a, "borderRightWidth")), b = b + c;
        return new goog.math.Coordinate(b, parseFloat(goog.style.getComputedStyle(a, "borderTopWidth")))
    }
    return new goog.math.Coordinate(a.clientLeft, a.clientTop)
};
goog.style.getPageOffset = function (a) {
    var b, c = goog.dom.getOwnerDocument(a), d = goog.style.getStyle_(a, "position");
    goog.asserts.assertObject(a, "Parameter is required");
    var e = !goog.style.GET_BOUNDING_CLIENT_RECT_ALWAYS_EXISTS && goog.userAgent.GECKO && c.getBoxObjectFor && !a.getBoundingClientRect && "absolute" == d && (b = c.getBoxObjectFor(a)) && (0 > b.screenX || 0 > b.screenY), f = new goog.math.Coordinate(0, 0), g = goog.style.getClientViewportElement(c);
    if (a == g)return f;
    if (goog.style.GET_BOUNDING_CLIENT_RECT_ALWAYS_EXISTS ||
        a.getBoundingClientRect)b = goog.style.getBoundingClientRect_(a), a = goog.dom.getDomHelper(c).getDocumentScroll(), f.x = b.left + a.x, f.y = b.top + a.y; else if (c.getBoxObjectFor && !e)b = c.getBoxObjectFor(a), a = c.getBoxObjectFor(g), f.x = b.screenX - a.screenX, f.y = b.screenY - a.screenY; else {
        b = a;
        do {
            f.x += b.offsetLeft;
            f.y += b.offsetTop;
            b != a && (f.x += b.clientLeft || 0, f.y += b.clientTop || 0);
            if (goog.userAgent.WEBKIT && "fixed" == goog.style.getComputedPosition(b)) {
                f.x += c.body.scrollLeft;
                f.y += c.body.scrollTop;
                break
            }
            b = b.offsetParent
        } while (b &&
            b != a);
        if (goog.userAgent.OPERA || goog.userAgent.WEBKIT && "absolute" == d)f.y -= c.body.offsetTop;
        for (b = a; (b = goog.style.getOffsetParent(b)) && b != c.body && b != g;)f.x -= b.scrollLeft, goog.userAgent.OPERA && "TR" == b.tagName || (f.y -= b.scrollTop)
    }
    return f
};
goog.style.getPageOffsetLeft = function (a) {
    return goog.style.getPageOffset(a).x
};
goog.style.getPageOffsetTop = function (a) {
    return goog.style.getPageOffset(a).y
};
goog.style.getFramedPageOffset = function (a, b) {
    var c = new goog.math.Coordinate(0, 0), d = goog.dom.getWindow(goog.dom.getOwnerDocument(a)), e = a;
    do {
        var f = d == b ? goog.style.getPageOffset(e) : goog.style.getClientPositionForElement_(goog.asserts.assert(e));
        c.x += f.x;
        c.y += f.y
    } while (d && d != b && (e = d.frameElement) && (d = d.parent));
    return c
};
goog.style.translateRectForAnotherFrame = function (a, b, c) {
    if (b.getDocument() != c.getDocument()) {
        var d = b.getDocument().body;
        c = goog.style.getFramedPageOffset(d, c.getWindow());
        c = goog.math.Coordinate.difference(c, goog.style.getPageOffset(d));
        goog.userAgent.IE && !b.isCss1CompatMode() && (c = goog.math.Coordinate.difference(c, b.getDocumentScroll()));
        a.left += c.x;
        a.top += c.y
    }
};
goog.style.getRelativePosition = function (a, b) {
    var c = goog.style.getClientPosition(a), d = goog.style.getClientPosition(b);
    return new goog.math.Coordinate(c.x - d.x, c.y - d.y)
};
goog.style.getClientPositionForElement_ = function (a) {
    var b;
    if (goog.style.GET_BOUNDING_CLIENT_RECT_ALWAYS_EXISTS || a.getBoundingClientRect)b = goog.style.getBoundingClientRect_(a), b = new goog.math.Coordinate(b.left, b.top); else {
        b = goog.dom.getDomHelper(a).getDocumentScroll();
        var c = goog.style.getPageOffset(a);
        b = new goog.math.Coordinate(c.x - b.x, c.y - b.y)
    }
    return goog.userAgent.GECKO && !goog.userAgent.isVersionOrHigher(12) ? goog.math.Coordinate.sum(b, goog.style.getCssTranslation(a)) : b
};
goog.style.getClientPosition = function (a) {
    goog.asserts.assert(a);
    if (a.nodeType == goog.dom.NodeType.ELEMENT)return goog.style.getClientPositionForElement_(a);
    var b = goog.isFunction(a.getBrowserEvent), c = a;
    a.targetTouches ? c = a.targetTouches[0] : b && a.getBrowserEvent().targetTouches && (c = a.getBrowserEvent().targetTouches[0]);
    return new goog.math.Coordinate(c.clientX, c.clientY)
};
goog.style.setPageOffset = function (a, b, c) {
    var d = goog.style.getPageOffset(a);
    b instanceof goog.math.Coordinate && (c = b.y, b = b.x);
    goog.style.setPosition(a, a.offsetLeft + (b - d.x), a.offsetTop + (c - d.y))
};
goog.style.setSize = function (a, b, c) {
    if (b instanceof goog.math.Size)c = b.height, b = b.width; else if (void 0 == c)throw Error("missing height argument");
    goog.style.setWidth(a, b);
    goog.style.setHeight(a, c)
};
goog.style.getPixelStyleValue_ = function (a, b) {
    "number" == typeof a && (a = (b ? Math.round(a) : a) + "px");
    return a
};
goog.style.setHeight = function (a, b) {
    a.style.height = goog.style.getPixelStyleValue_(b, !0)
};
goog.style.setWidth = function (a, b) {
    a.style.width = goog.style.getPixelStyleValue_(b, !0)
};
goog.style.getSize = function (a) {
    return goog.style.evaluateWithTemporaryDisplay_(goog.style.getSizeWithDisplay_, a)
};
goog.style.evaluateWithTemporaryDisplay_ = function (a, b) {
    if ("none" != goog.style.getStyle_(b, "display"))return a(b);
    var c = b.style, d = c.display, e = c.visibility, f = c.position;
    c.visibility = "hidden";
    c.position = "absolute";
    c.display = "inline";
    var g = a(b);
    c.display = d;
    c.position = f;
    c.visibility = e;
    return g
};
goog.style.getSizeWithDisplay_ = function (a) {
    var b = a.offsetWidth, c = a.offsetHeight, d = goog.userAgent.WEBKIT && !b && !c;
    return goog.isDef(b) && !d || !a.getBoundingClientRect ? new goog.math.Size(b, c) : (a = goog.style.getBoundingClientRect_(a), new goog.math.Size(a.right - a.left, a.bottom - a.top))
};
goog.style.getTransformedSize = function (a) {
    if (!a.getBoundingClientRect)return null;
    a = goog.style.evaluateWithTemporaryDisplay_(goog.style.getBoundingClientRect_, a);
    return new goog.math.Size(a.right - a.left, a.bottom - a.top)
};
goog.style.getBounds = function (a) {
    var b = goog.style.getPageOffset(a);
    a = goog.style.getSize(a);
    return new goog.math.Rect(b.x, b.y, a.width, a.height)
};
goog.style.toCamelCase = function (a) {
    return goog.string.toCamelCase(String(a))
};
goog.style.toSelectorCase = function (a) {
    return goog.string.toSelectorCase(a)
};
goog.style.getOpacity = function (a) {
    var b = a.style;
    a = "";
    "opacity"in b ? a = b.opacity : "MozOpacity"in b ? a = b.MozOpacity : "filter"in b && (b = b.filter.match(/alpha\(opacity=([\d.]+)\)/)) && (a = String(b[1] / 100));
    return"" == a ? a : Number(a)
};
goog.style.setOpacity = function (a, b) {
    var c = a.style;
    "opacity"in c ? c.opacity = b : "MozOpacity"in c ? c.MozOpacity = b : "filter"in c && (c.filter = "" === b ? "" : "alpha(opacity=" + 100 * b + ")")
};
goog.style.setTransparentBackgroundImage = function (a, b) {
    var c = a.style;
    goog.userAgent.IE && !goog.userAgent.isVersionOrHigher("8") ? c.filter = 'progid:DXImageTransform.Microsoft.AlphaImageLoader(src="' + b + '", sizingMethod="crop")' : (c.backgroundImage = "url(" + b + ")", c.backgroundPosition = "top left", c.backgroundRepeat = "no-repeat")
};
goog.style.clearTransparentBackgroundImage = function (a) {
    a = a.style;
    "filter"in a ? a.filter = "" : a.backgroundImage = "none"
};
goog.style.showElement = function (a, b) {
    goog.style.setElementShown(a, b)
};
goog.style.setElementShown = function (a, b) {
    a.style.display = b ? "" : "none"
};
goog.style.isElementShown = function (a) {
    return"none" != a.style.display
};
goog.style.installStyles = function (a, b) {
    var c = goog.dom.getDomHelper(b), d = null;
    if (goog.userAgent.IE)d = c.getDocument().createStyleSheet(), goog.style.setStyles(d, a); else {
        var e = c.getElementsByTagNameAndClass("head")[0];
        e || (d = c.getElementsByTagNameAndClass("body")[0], e = c.createDom("head"), d.parentNode.insertBefore(e, d));
        d = c.createDom("style");
        goog.style.setStyles(d, a);
        c.appendChild(e, d)
    }
    return d
};
goog.style.uninstallStyles = function (a) {
    goog.dom.removeNode(a.ownerNode || a.owningElement || a)
};
goog.style.setStyles = function (a, b) {
    goog.userAgent.IE ? a.cssText = b : a.innerHTML = b
};
goog.style.setPreWrap = function (a) {
    a = a.style;
    goog.userAgent.IE && !goog.userAgent.isVersionOrHigher("8") ? (a.whiteSpace = "pre", a.wordWrap = "break-word") : a.whiteSpace = goog.userAgent.GECKO ? "-moz-pre-wrap" : "pre-wrap"
};
goog.style.setInlineBlock = function (a) {
    a = a.style;
    a.position = "relative";
    goog.userAgent.IE && !goog.userAgent.isVersionOrHigher("8") ? (a.zoom = "1", a.display = "inline") : a.display = goog.userAgent.GECKO ? goog.userAgent.isVersionOrHigher("1.9a") ? "inline-block" : "-moz-inline-box" : "inline-block"
};
goog.style.isRightToLeft = function (a) {
    return"rtl" == goog.style.getStyle_(a, "direction")
};
goog.style.unselectableStyle_ = goog.userAgent.GECKO ? "MozUserSelect" : goog.userAgent.WEBKIT ? "WebkitUserSelect" : null;
goog.style.isUnselectable = function (a) {
    return goog.style.unselectableStyle_ ? "none" == a.style[goog.style.unselectableStyle_].toLowerCase() : goog.userAgent.IE || goog.userAgent.OPERA ? "on" == a.getAttribute("unselectable") : !1
};
goog.style.setUnselectable = function (a, b, c) {
    c = c ? null : a.getElementsByTagName("*");
    var d = goog.style.unselectableStyle_;
    if (d) {
        if (b = b ? "none" : "", a.style[d] = b, c) {
            a = 0;
            for (var e; e = c[a]; a++)e.style[d] = b
        }
    } else if (goog.userAgent.IE || goog.userAgent.OPERA)if (b = b ? "on" : "", a.setAttribute("unselectable", b), c)for (a = 0; e = c[a]; a++)e.setAttribute("unselectable", b)
};
goog.style.getBorderBoxSize = function (a) {
    return new goog.math.Size(a.offsetWidth, a.offsetHeight)
};
goog.style.setBorderBoxSize = function (a, b) {
    var c = goog.dom.getOwnerDocument(a), d = goog.dom.getDomHelper(c).isCss1CompatMode();
    if (!goog.userAgent.IE || d && goog.userAgent.isVersionOrHigher("8"))goog.style.setBoxSizingSize_(a, b, "border-box"); else if (c = a.style, d) {
        var d = goog.style.getPaddingBox(a), e = goog.style.getBorderBox(a);
        c.pixelWidth = b.width - e.left - d.left - d.right - e.right;
        c.pixelHeight = b.height - e.top - d.top - d.bottom - e.bottom
    } else c.pixelWidth = b.width, c.pixelHeight = b.height
};
goog.style.getContentBoxSize = function (a) {
    var b = goog.dom.getOwnerDocument(a), c = goog.userAgent.IE && a.currentStyle;
    if (c && goog.dom.getDomHelper(b).isCss1CompatMode() && "auto" != c.width && "auto" != c.height && !c.boxSizing)return b = goog.style.getIePixelValue_(a, c.width, "width", "pixelWidth"), a = goog.style.getIePixelValue_(a, c.height, "height", "pixelHeight"), new goog.math.Size(b, a);
    c = goog.style.getBorderBoxSize(a);
    b = goog.style.getPaddingBox(a);
    a = goog.style.getBorderBox(a);
    return new goog.math.Size(c.width - a.left -
        b.left - b.right - a.right, c.height - a.top - b.top - b.bottom - a.bottom)
};
goog.style.setContentBoxSize = function (a, b) {
    var c = goog.dom.getOwnerDocument(a), d = goog.dom.getDomHelper(c).isCss1CompatMode();
    if (!goog.userAgent.IE || d && goog.userAgent.isVersionOrHigher("8"))goog.style.setBoxSizingSize_(a, b, "content-box"); else if (c = a.style, d)c.pixelWidth = b.width, c.pixelHeight = b.height; else {
        var d = goog.style.getPaddingBox(a), e = goog.style.getBorderBox(a);
        c.pixelWidth = b.width + e.left + d.left + d.right + e.right;
        c.pixelHeight = b.height + e.top + d.top + d.bottom + e.bottom
    }
};
goog.style.setBoxSizingSize_ = function (a, b, c) {
    a = a.style;
    goog.userAgent.GECKO ? a.MozBoxSizing = c : goog.userAgent.WEBKIT ? a.WebkitBoxSizing = c : a.boxSizing = c;
    a.width = Math.max(b.width, 0) + "px";
    a.height = Math.max(b.height, 0) + "px"
};
goog.style.getIePixelValue_ = function (a, b, c, d) {
    if (/^\d+px?$/.test(b))return parseInt(b, 10);
    var e = a.style[c], f = a.runtimeStyle[c];
    a.runtimeStyle[c] = a.currentStyle[c];
    a.style[c] = b;
    b = a.style[d];
    a.style[c] = e;
    a.runtimeStyle[c] = f;
    return b
};
goog.style.getIePixelDistance_ = function (a, b) {
    var c = goog.style.getCascadedStyle(a, b);
    return c ? goog.style.getIePixelValue_(a, c, "left", "pixelLeft") : 0
};
goog.style.getBox_ = function (a, b) {
    if (goog.userAgent.IE) {
        var c = goog.style.getIePixelDistance_(a, b + "Left"), d = goog.style.getIePixelDistance_(a, b + "Right"), e = goog.style.getIePixelDistance_(a, b + "Top"), f = goog.style.getIePixelDistance_(a, b + "Bottom");
        return new goog.math.Box(e, d, f, c)
    }
    c = goog.style.getComputedStyle(a, b + "Left");
    d = goog.style.getComputedStyle(a, b + "Right");
    e = goog.style.getComputedStyle(a, b + "Top");
    f = goog.style.getComputedStyle(a, b + "Bottom");
    return new goog.math.Box(parseFloat(e), parseFloat(d), parseFloat(f),
        parseFloat(c))
};
goog.style.getPaddingBox = function (a) {
    return goog.style.getBox_(a, "padding")
};
goog.style.getMarginBox = function (a) {
    return goog.style.getBox_(a, "margin")
};
goog.style.ieBorderWidthKeywords_ = {thin: 2, medium: 4, thick: 6};
goog.style.getIePixelBorder_ = function (a, b) {
    if ("none" == goog.style.getCascadedStyle(a, b + "Style"))return 0;
    var c = goog.style.getCascadedStyle(a, b + "Width");
    return c in goog.style.ieBorderWidthKeywords_ ? goog.style.ieBorderWidthKeywords_[c] : goog.style.getIePixelValue_(a, c, "left", "pixelLeft")
};
goog.style.getBorderBox = function (a) {
    if (goog.userAgent.IE) {
        var b = goog.style.getIePixelBorder_(a, "borderLeft"), c = goog.style.getIePixelBorder_(a, "borderRight"), d = goog.style.getIePixelBorder_(a, "borderTop");
        a = goog.style.getIePixelBorder_(a, "borderBottom");
        return new goog.math.Box(d, c, a, b)
    }
    b = goog.style.getComputedStyle(a, "borderLeftWidth");
    c = goog.style.getComputedStyle(a, "borderRightWidth");
    d = goog.style.getComputedStyle(a, "borderTopWidth");
    a = goog.style.getComputedStyle(a, "borderBottomWidth");
    return new goog.math.Box(parseFloat(d),
        parseFloat(c), parseFloat(a), parseFloat(b))
};
goog.style.getFontFamily = function (a) {
    var b = goog.dom.getOwnerDocument(a), c = "";
    if (b.body.createTextRange) {
        b = b.body.createTextRange();
        b.moveToElementText(a);
        try {
            c = b.queryCommandValue("FontName")
        } catch (d) {
            c = ""
        }
    }
    c || (c = goog.style.getStyle_(a, "fontFamily"));
    a = c.split(",");
    1 < a.length && (c = a[0]);
    return goog.string.stripQuotes(c, "\"'")
};
goog.style.lengthUnitRegex_ = /[^\d]+$/;
goog.style.getLengthUnits = function (a) {
    return(a = a.match(goog.style.lengthUnitRegex_)) && a[0] || null
};
goog.style.ABSOLUTE_CSS_LENGTH_UNITS_ = {cm: 1, "in": 1, mm: 1, pc: 1, pt: 1};
goog.style.CONVERTIBLE_RELATIVE_CSS_UNITS_ = {em: 1, ex: 1};
goog.style.getFontSize = function (a) {
    var b = goog.style.getStyle_(a, "fontSize"), c = goog.style.getLengthUnits(b);
    if (b && "px" == c)return parseInt(b, 10);
    if (goog.userAgent.IE) {
        if (c in goog.style.ABSOLUTE_CSS_LENGTH_UNITS_)return goog.style.getIePixelValue_(a, b, "left", "pixelLeft");
        if (a.parentNode && a.parentNode.nodeType == goog.dom.NodeType.ELEMENT && c in goog.style.CONVERTIBLE_RELATIVE_CSS_UNITS_)return a = a.parentNode, c = goog.style.getStyle_(a, "fontSize"), goog.style.getIePixelValue_(a, b == c ? "1em" : b, "left", "pixelLeft")
    }
    c =
        goog.dom.createDom("span", {style: "visibility:hidden;position:absolute;line-height:0;padding:0;margin:0;border:0;height:1em;"});
    goog.dom.appendChild(a, c);
    b = c.offsetHeight;
    goog.dom.removeNode(c);
    return b
};
goog.style.parseStyleAttribute = function (a) {
    var b = {};
    goog.array.forEach(a.split(/\s*;\s*/), function (a) {
        a = a.split(/\s*:\s*/);
        2 == a.length && (b[goog.string.toCamelCase(a[0].toLowerCase())] = a[1])
    });
    return b
};
goog.style.toStyleAttribute = function (a) {
    var b = [];
    goog.object.forEach(a, function (a, d) {
        b.push(goog.string.toSelectorCase(d), ":", a, ";")
    });
    return b.join("")
};
goog.style.setFloat = function (a, b) {
    a.style[goog.userAgent.IE ? "styleFloat" : "cssFloat"] = b
};
goog.style.getFloat = function (a) {
    return a.style[goog.userAgent.IE ? "styleFloat" : "cssFloat"] || ""
};
goog.style.getScrollbarWidth = function (a) {
    var b = goog.dom.createElement("div");
    a && (b.className = a);
    b.style.cssText = "overflow:auto;position:absolute;top:0;width:100px;height:100px";
    a = goog.dom.createElement("div");
    goog.style.setSize(a, "200px", "200px");
    b.appendChild(a);
    goog.dom.appendChild(goog.dom.getDocument().body, b);
    a = b.offsetWidth - b.clientWidth;
    goog.dom.removeNode(b);
    return a
};
goog.style.MATRIX_TRANSLATION_REGEX_ = /matrix\([0-9\.\-]+, [0-9\.\-]+, [0-9\.\-]+, [0-9\.\-]+, ([0-9\.\-]+)p?x?, ([0-9\.\-]+)p?x?\)/;
goog.style.getCssTranslation = function (a) {
    var b;
    goog.userAgent.IE ? b = "-ms-transform" : goog.userAgent.WEBKIT ? b = "-webkit-transform" : goog.userAgent.OPERA ? b = "-o-transform" : goog.userAgent.GECKO && (b = "-moz-transform");
    var c;
    b && (c = goog.style.getStyle_(a, b));
    c || (c = goog.style.getStyle_(a, "transform"));
    return c ? (a = c.match(goog.style.MATRIX_TRANSLATION_REGEX_)) ? new goog.math.Coordinate(parseFloat(a[1]), parseFloat(a[2])) : new goog.math.Coordinate(0, 0) : new goog.math.Coordinate(0, 0)
};
goog.events = {};
goog.events.Listenable = function () {
};
goog.events.Listenable.IMPLEMENTED_BY_PROP = "closure_listenable_" + (1E6 * Math.random() | 0);
goog.events.Listenable.addImplementation = function (a) {
    a.prototype[goog.events.Listenable.IMPLEMENTED_BY_PROP] = !0
};
goog.events.Listenable.isImplementedBy = function (a) {
    return!(!a || !a[goog.events.Listenable.IMPLEMENTED_BY_PROP])
};
goog.events.ListenableKey = function () {
};
goog.events.ListenableKey.counter_ = 0;
goog.events.ListenableKey.reserveKey = function () {
    return++goog.events.ListenableKey.counter_
};
goog.events.Listener = function (a, b, c, d, e, f) {
    goog.events.Listener.ENABLE_MONITORING && (this.creationStack = Error().stack);
    this.listener = a;
    this.proxy = b;
    this.src = c;
    this.type = d;
    this.capture = !!e;
    this.handler = f;
    this.key = goog.events.ListenableKey.reserveKey();
    this.removed = this.callOnce = !1
};
goog.events.Listener.ENABLE_MONITORING = !1;
goog.events.Listener.prototype.markAsRemoved = function () {
    this.removed = !0;
    this.handler = this.src = this.proxy = this.listener = null
};
goog.events.BrowserFeature = {HAS_W3C_BUTTON: !goog.userAgent.IE || goog.userAgent.isDocumentModeOrHigher(9), HAS_W3C_EVENT_SUPPORT: !goog.userAgent.IE || goog.userAgent.isDocumentModeOrHigher(9), SET_KEY_CODE_TO_PREVENT_DEFAULT: goog.userAgent.IE && !goog.userAgent.isVersionOrHigher("9"), HAS_NAVIGATOR_ONLINE_PROPERTY: !goog.userAgent.WEBKIT || goog.userAgent.isVersionOrHigher("528"), HAS_HTML5_NETWORK_EVENT_SUPPORT: goog.userAgent.GECKO && goog.userAgent.isVersionOrHigher("1.9b") || goog.userAgent.IE && goog.userAgent.isVersionOrHigher("8") ||
    goog.userAgent.OPERA && goog.userAgent.isVersionOrHigher("9.5") || goog.userAgent.WEBKIT && goog.userAgent.isVersionOrHigher("528"), HTML5_NETWORK_EVENTS_FIRE_ON_BODY: goog.userAgent.GECKO && !goog.userAgent.isVersionOrHigher("8") || goog.userAgent.IE && !goog.userAgent.isVersionOrHigher("9"), TOUCH_ENABLED: "ontouchstart"in goog.global || !!(goog.global.document && document.documentElement && "ontouchstart"in document.documentElement) || !(!goog.global.navigator || !goog.global.navigator.msMaxTouchPoints)};
goog.debug.entryPointRegistry = {};
goog.debug.EntryPointMonitor = function () {
};
goog.debug.entryPointRegistry.refList_ = [];
goog.debug.entryPointRegistry.monitors_ = [];
goog.debug.entryPointRegistry.monitorsMayExist_ = !1;
goog.debug.entryPointRegistry.register = function (a) {
    goog.debug.entryPointRegistry.refList_[goog.debug.entryPointRegistry.refList_.length] = a;
    if (goog.debug.entryPointRegistry.monitorsMayExist_)for (var b = goog.debug.entryPointRegistry.monitors_, c = 0; c < b.length; c++)a(goog.bind(b[c].wrap, b[c]))
};
goog.debug.entryPointRegistry.monitorAll = function (a) {
    goog.debug.entryPointRegistry.monitorsMayExist_ = !0;
    for (var b = goog.bind(a.wrap, a), c = 0; c < goog.debug.entryPointRegistry.refList_.length; c++)goog.debug.entryPointRegistry.refList_[c](b);
    goog.debug.entryPointRegistry.monitors_.push(a)
};
goog.debug.entryPointRegistry.unmonitorAllIfPossible = function (a) {
    var b = goog.debug.entryPointRegistry.monitors_;
    goog.asserts.assert(a == b[b.length - 1], "Only the most recent monitor can be unwrapped.");
    a = goog.bind(a.unwrap, a);
    for (var c = 0; c < goog.debug.entryPointRegistry.refList_.length; c++)goog.debug.entryPointRegistry.refList_[c](a);
    b.length--
};
goog.events.EventType = {CLICK: "click", DBLCLICK: "dblclick", MOUSEDOWN: "mousedown", MOUSEUP: "mouseup", MOUSEOVER: "mouseover", MOUSEOUT: "mouseout", MOUSEMOVE: "mousemove", SELECTSTART: "selectstart", KEYPRESS: "keypress", KEYDOWN: "keydown", KEYUP: "keyup", BLUR: "blur", FOCUS: "focus", DEACTIVATE: "deactivate", FOCUSIN: goog.userAgent.IE ? "focusin" : "DOMFocusIn", FOCUSOUT: goog.userAgent.IE ? "focusout" : "DOMFocusOut", CHANGE: "change", SELECT: "select", SUBMIT: "submit", INPUT: "input", PROPERTYCHANGE: "propertychange", DRAGSTART: "dragstart",
    DRAG: "drag", DRAGENTER: "dragenter", DRAGOVER: "dragover", DRAGLEAVE: "dragleave", DROP: "drop", DRAGEND: "dragend", TOUCHSTART: "touchstart", TOUCHMOVE: "touchmove", TOUCHEND: "touchend", TOUCHCANCEL: "touchcancel", BEFOREUNLOAD: "beforeunload", CONSOLEMESSAGE: "consolemessage", CONTEXTMENU: "contextmenu", DOMCONTENTLOADED: "DOMContentLoaded", ERROR: "error", HELP: "help", LOAD: "load", LOSECAPTURE: "losecapture", READYSTATECHANGE: "readystatechange", RESIZE: "resize", SCROLL: "scroll", UNLOAD: "unload", HASHCHANGE: "hashchange", PAGEHIDE: "pagehide",
    PAGESHOW: "pageshow", POPSTATE: "popstate", COPY: "copy", PASTE: "paste", CUT: "cut", BEFORECOPY: "beforecopy", BEFORECUT: "beforecut", BEFOREPASTE: "beforepaste", ONLINE: "online", OFFLINE: "offline", MESSAGE: "message", CONNECT: "connect", TRANSITIONEND: goog.userAgent.WEBKIT ? "webkitTransitionEnd" : goog.userAgent.OPERA ? "oTransitionEnd" : "transitionend", MSGESTURECHANGE: "MSGestureChange", MSGESTUREEND: "MSGestureEnd", MSGESTUREHOLD: "MSGestureHold", MSGESTURESTART: "MSGestureStart", MSGESTURETAP: "MSGestureTap", MSGOTPOINTERCAPTURE: "MSGotPointerCapture",
    MSINERTIASTART: "MSInertiaStart", MSLOSTPOINTERCAPTURE: "MSLostPointerCapture", MSPOINTERCANCEL: "MSPointerCancel", MSPOINTERDOWN: "MSPointerDown", MSPOINTERMOVE: "MSPointerMove", MSPOINTEROVER: "MSPointerOver", MSPOINTEROUT: "MSPointerOut", MSPOINTERUP: "MSPointerUp", TEXTINPUT: "textinput", COMPOSITIONSTART: "compositionstart", COMPOSITIONUPDATE: "compositionupdate", COMPOSITIONEND: "compositionend", EXIT: "exit", LOADABORT: "loadabort", LOADCOMMIT: "loadcommit", LOADREDIRECT: "loadredirect", LOADSTART: "loadstart", LOADSTOP: "loadstop",
    RESPONSIVE: "responsive", SIZECHANGED: "sizechanged", UNRESPONSIVE: "unresponsive"};
goog.disposable = {};
goog.disposable.IDisposable = function () {
};
goog.Disposable = function () {
    goog.Disposable.MONITORING_MODE != goog.Disposable.MonitoringMode.OFF && (goog.Disposable.INCLUDE_STACK_ON_CREATION && (this.creationStack = Error().stack), goog.Disposable.instances_[goog.getUid(this)] = this)
};
goog.Disposable.MonitoringMode = {OFF: 0, PERMANENT: 1, INTERACTIVE: 2};
goog.Disposable.MONITORING_MODE = 0;
goog.Disposable.INCLUDE_STACK_ON_CREATION = !0;
goog.Disposable.instances_ = {};
goog.Disposable.getUndisposedObjects = function () {
    var a = [], b;
    for (b in goog.Disposable.instances_)goog.Disposable.instances_.hasOwnProperty(b) && a.push(goog.Disposable.instances_[Number(b)]);
    return a
};
goog.Disposable.clearUndisposedObjects = function () {
    goog.Disposable.instances_ = {}
};
goog.Disposable.prototype.disposed_ = !1;
goog.Disposable.prototype.isDisposed = function () {
    return this.disposed_
};
goog.Disposable.prototype.getDisposed = goog.Disposable.prototype.isDisposed;
goog.Disposable.prototype.dispose = function () {
    if (!this.disposed_ && (this.disposed_ = !0, this.disposeInternal(), goog.Disposable.MONITORING_MODE != goog.Disposable.MonitoringMode.OFF)) {
        var a = goog.getUid(this);
        if (goog.Disposable.MONITORING_MODE == goog.Disposable.MonitoringMode.PERMANENT && !goog.Disposable.instances_.hasOwnProperty(a))throw Error(this + " did not call the goog.Disposable base constructor or was disposed of after a clearUndisposedObjects call");
        delete goog.Disposable.instances_[a]
    }
};
goog.Disposable.prototype.registerDisposable = function (a) {
    this.addOnDisposeCallback(goog.partial(goog.dispose, a))
};
goog.Disposable.prototype.addOnDisposeCallback = function (a, b) {
    this.onDisposeCallbacks_ || (this.onDisposeCallbacks_ = []);
    this.onDisposeCallbacks_.push(goog.bind(a, b))
};
goog.Disposable.prototype.disposeInternal = function () {
    if (this.onDisposeCallbacks_)for (; this.onDisposeCallbacks_.length;)this.onDisposeCallbacks_.shift()()
};
goog.Disposable.isDisposed = function (a) {
    return a && "function" == typeof a.isDisposed ? a.isDisposed() : !1
};
goog.dispose = function (a) {
    a && "function" == typeof a.dispose && a.dispose()
};
goog.disposeAll = function (a) {
    for (var b = 0, c = arguments.length; b < c; ++b) {
        var d = arguments[b];
        goog.isArrayLike(d) ? goog.disposeAll.apply(null, d) : goog.dispose(d)
    }
};
goog.events.Event = function (a, b) {
    this.type = a;
    this.currentTarget = this.target = b
};
goog.events.Event.prototype.disposeInternal = function () {
};
goog.events.Event.prototype.dispose = function () {
};
goog.events.Event.prototype.propagationStopped_ = !1;
goog.events.Event.prototype.defaultPrevented = !1;
goog.events.Event.prototype.returnValue_ = !0;
goog.events.Event.prototype.stopPropagation = function () {
    this.propagationStopped_ = !0
};
goog.events.Event.prototype.preventDefault = function () {
    this.defaultPrevented = !0;
    this.returnValue_ = !1
};
goog.events.Event.stopPropagation = function (a) {
    a.stopPropagation()
};
goog.events.Event.preventDefault = function (a) {
    a.preventDefault()
};
goog.reflect = {};
goog.reflect.object = function (a, b) {
    return b
};
goog.reflect.sinkValue = function (a) {
    goog.reflect.sinkValue[" "](a);
    return a
};
goog.reflect.sinkValue[" "] = goog.nullFunction;
goog.reflect.canAccessProperty = function (a, b) {
    try {
        return goog.reflect.sinkValue(a[b]), !0
    } catch (c) {
    }
    return!1
};
goog.events.BrowserEvent = function (a, b) {
    a && this.init(a, b)
};
goog.inherits(goog.events.BrowserEvent, goog.events.Event);
goog.events.BrowserEvent.MouseButton = {LEFT: 0, MIDDLE: 1, RIGHT: 2};
goog.events.BrowserEvent.IEButtonMap = [1, 4, 2];
goog.events.BrowserEvent.prototype.target = null;
goog.events.BrowserEvent.prototype.relatedTarget = null;
goog.events.BrowserEvent.prototype.offsetX = 0;
goog.events.BrowserEvent.prototype.offsetY = 0;
goog.events.BrowserEvent.prototype.clientX = 0;
goog.events.BrowserEvent.prototype.clientY = 0;
goog.events.BrowserEvent.prototype.screenX = 0;
goog.events.BrowserEvent.prototype.screenY = 0;
goog.events.BrowserEvent.prototype.button = 0;
goog.events.BrowserEvent.prototype.keyCode = 0;
goog.events.BrowserEvent.prototype.charCode = 0;
goog.events.BrowserEvent.prototype.ctrlKey = !1;
goog.events.BrowserEvent.prototype.altKey = !1;
goog.events.BrowserEvent.prototype.shiftKey = !1;
goog.events.BrowserEvent.prototype.metaKey = !1;
goog.events.BrowserEvent.prototype.platformModifierKey = !1;
goog.events.BrowserEvent.prototype.event_ = null;
goog.events.BrowserEvent.prototype.init = function (a, b) {
    var c = this.type = a.type;
    goog.events.Event.call(this, c);
    this.target = a.target || a.srcElement;
    this.currentTarget = b;
    var d = a.relatedTarget;
    d ? goog.userAgent.GECKO && (goog.reflect.canAccessProperty(d, "nodeName") || (d = null)) : c == goog.events.EventType.MOUSEOVER ? d = a.fromElement : c == goog.events.EventType.MOUSEOUT && (d = a.toElement);
    this.relatedTarget = d;
    this.offsetX = goog.userAgent.WEBKIT || void 0 !== a.offsetX ? a.offsetX : a.layerX;
    this.offsetY = goog.userAgent.WEBKIT || void 0 !==
        a.offsetY ? a.offsetY : a.layerY;
    this.clientX = void 0 !== a.clientX ? a.clientX : a.pageX;
    this.clientY = void 0 !== a.clientY ? a.clientY : a.pageY;
    this.screenX = a.screenX || 0;
    this.screenY = a.screenY || 0;
    this.button = a.button;
    this.keyCode = a.keyCode || 0;
    this.charCode = a.charCode || ("keypress" == c ? a.keyCode : 0);
    this.ctrlKey = a.ctrlKey;
    this.altKey = a.altKey;
    this.shiftKey = a.shiftKey;
    this.metaKey = a.metaKey;
    this.platformModifierKey = goog.userAgent.MAC ? a.metaKey : a.ctrlKey;
    this.state = a.state;
    this.event_ = a;
    a.defaultPrevented && this.preventDefault();
    delete this.propagationStopped_
};
goog.events.BrowserEvent.prototype.isButton = function (a) {
    return goog.events.BrowserFeature.HAS_W3C_BUTTON ? this.event_.button == a : "click" == this.type ? a == goog.events.BrowserEvent.MouseButton.LEFT : !!(this.event_.button & goog.events.BrowserEvent.IEButtonMap[a])
};
goog.events.BrowserEvent.prototype.isMouseActionButton = function () {
    return this.isButton(goog.events.BrowserEvent.MouseButton.LEFT) && !(goog.userAgent.WEBKIT && goog.userAgent.MAC && this.ctrlKey)
};
goog.events.BrowserEvent.prototype.stopPropagation = function () {
    goog.events.BrowserEvent.superClass_.stopPropagation.call(this);
    this.event_.stopPropagation ? this.event_.stopPropagation() : this.event_.cancelBubble = !0
};
goog.events.BrowserEvent.prototype.preventDefault = function () {
    goog.events.BrowserEvent.superClass_.preventDefault.call(this);
    var a = this.event_;
    if (a.preventDefault)a.preventDefault(); else if (a.returnValue = !1, goog.events.BrowserFeature.SET_KEY_CODE_TO_PREVENT_DEFAULT)try {
        if (a.ctrlKey || 112 <= a.keyCode && 123 >= a.keyCode)a.keyCode = -1
    } catch (b) {
    }
};
goog.events.BrowserEvent.prototype.getBrowserEvent = function () {
    return this.event_
};
goog.events.BrowserEvent.prototype.disposeInternal = function () {
};
goog.events.listeners_ = {};
goog.events.listenerTree_ = {};
goog.events.sources_ = {};
goog.events.onString_ = "on";
goog.events.onStringMap_ = {};
goog.events.keySeparator_ = "_";
goog.events.listen = function (a, b, c, d, e) {
    if (goog.isArray(b)) {
        for (var f = 0; f < b.length; f++)goog.events.listen(a, b[f], c, d, e);
        return null
    }
    c = goog.events.wrapListener_(c);
    return goog.events.Listenable.isImplementedBy(a) ? a.listen(b, c, d, e) : goog.events.listen_(a, b, c, !1, d, e)
};
goog.events.listen_ = function (a, b, c, d, e, f) {
    if (!b)throw Error("Invalid event type");
    e = !!e;
    var g = goog.events.listenerTree_;
    b in g || (g[b] = {count_: 0});
    g = g[b];
    e in g || (g[e] = {count_: 0}, g.count_++);
    var g = g[e], h = goog.getUid(a), k;
    if (g[h]) {
        k = g[h];
        for (var l = 0; l < k.length; l++)if (g = k[l], g.listener == c && g.handler == f) {
            if (g.removed)break;
            d || (k[l].callOnce = !1);
            return k[l]
        }
    } else k = g[h] = [], g.count_++;
    l = goog.events.getProxy();
    g = new goog.events.Listener(c, l, a, b, e, f);
    g.callOnce = d;
    l.src = a;
    l.listener = g;
    k.push(g);
    goog.events.sources_[h] ||
    (goog.events.sources_[h] = []);
    goog.events.sources_[h].push(g);
    a.addEventListener ? a.addEventListener(b, l, e) : a.attachEvent(goog.events.getOnString_(b), l);
    return goog.events.listeners_[g.key] = g
};
goog.events.getProxy = function () {
    var a = goog.events.handleBrowserEvent_, b = goog.events.BrowserFeature.HAS_W3C_EVENT_SUPPORT ? function (c) {
        return a.call(b.src, b.listener, c)
    } : function (c) {
        c = a.call(b.src, b.listener, c);
        if (!c)return c
    };
    return b
};
goog.events.listenOnce = function (a, b, c, d, e) {
    if (goog.isArray(b)) {
        for (var f = 0; f < b.length; f++)goog.events.listenOnce(a, b[f], c, d, e);
        return null
    }
    c = goog.events.wrapListener_(c);
    return goog.events.Listenable.isImplementedBy(a) ? a.listenOnce(b, c, d, e) : goog.events.listen_(a, b, c, !0, d, e)
};
goog.events.listenWithWrapper = function (a, b, c, d, e) {
    b.listen(a, c, d, e)
};
goog.events.unlisten = function (a, b, c, d, e) {
    if (goog.isArray(b)) {
        for (var f = 0; f < b.length; f++)goog.events.unlisten(a, b[f], c, d, e);
        return null
    }
    c = goog.events.wrapListener_(c);
    if (goog.events.Listenable.isImplementedBy(a))return a.unlisten(b, c, d, e);
    d = !!d;
    a = goog.events.getListeners_(a, b, d);
    if (!a)return!1;
    for (f = 0; f < a.length; f++)if (a[f].listener == c && a[f].capture == d && a[f].handler == e)return goog.events.unlistenByKey(a[f]);
    return!1
};
goog.events.unlistenByKey = function (a) {
    if (goog.isNumber(a) || !a || a.removed)return!1;
    var b = a.src;
    if (goog.events.Listenable.isImplementedBy(b))return b.unlistenByKey(a);
    var c = a.type, d = a.proxy, e = a.capture;
    b.removeEventListener ? b.removeEventListener(c, d, e) : b.detachEvent && b.detachEvent(goog.events.getOnString_(c), d);
    b = goog.getUid(b);
    goog.events.sources_[b] && (d = goog.events.sources_[b], goog.array.remove(d, a), 0 == d.length && delete goog.events.sources_[b]);
    a.markAsRemoved();
    if (d = goog.events.listenerTree_[c][e][b])goog.array.remove(d,
        a), 0 == d.length && (delete goog.events.listenerTree_[c][e][b], goog.events.listenerTree_[c][e].count_--), 0 == goog.events.listenerTree_[c][e].count_ && (delete goog.events.listenerTree_[c][e], goog.events.listenerTree_[c].count_--), 0 == goog.events.listenerTree_[c].count_ && delete goog.events.listenerTree_[c];
    delete goog.events.listeners_[a.key];
    return!0
};
goog.events.unlistenWithWrapper = function (a, b, c, d, e) {
    b.unlisten(a, c, d, e)
};
goog.events.removeAll = function (a, b) {
    var c = 0, d = null == b;
    if (null != a) {
        if (a && goog.events.Listenable.isImplementedBy(a))return a.removeAllListeners(b);
        var e = goog.getUid(a);
        if (goog.events.sources_[e])for (var e = goog.events.sources_[e], f = e.length - 1; 0 <= f; f--) {
            var g = e[f];
            if (d || b == g.type)goog.events.unlistenByKey(g), c++
        }
    } else goog.object.forEach(goog.events.listeners_, function (a) {
        goog.events.unlistenByKey(a);
        c++
    });
    return c
};
goog.events.removeAllNativeListeners = function () {
    var a = 0;
    goog.object.forEach(goog.events.listeners_, function (b) {
        goog.events.unlistenByKey(b);
        a++
    });
    return a
};
goog.events.getListeners = function (a, b, c) {
    return goog.events.Listenable.isImplementedBy(a) ? a.getListeners(b, c) : goog.events.getListeners_(a, b, c) || []
};
goog.events.getListeners_ = function (a, b, c) {
    var d = goog.events.listenerTree_;
    return b in d && (d = d[b], c in d && (d = d[c], a = goog.getUid(a), d[a])) ? d[a] : null
};
goog.events.getListener = function (a, b, c, d, e) {
    d = !!d;
    c = goog.events.wrapListener_(c);
    if (goog.events.Listenable.isImplementedBy(a))return a.getListener(b, c, d, e);
    if (a = goog.events.getListeners_(a, b, d))for (b = 0; b < a.length; b++)if (!a[b].removed && a[b].listener == c && a[b].capture == d && a[b].handler == e)return a[b];
    return null
};
goog.events.hasListener = function (a, b, c) {
    if (goog.events.Listenable.isImplementedBy(a))return a.hasListener(b, c);
    a = goog.getUid(a);
    var d = goog.events.sources_[a];
    if (d) {
        var e = goog.isDef(b), f = goog.isDef(c);
        return e && f ? (d = goog.events.listenerTree_[b], !!d && !!d[c] && a in d[c]) : e || f ? goog.array.some(d, function (a) {
            return e && a.type == b || f && a.capture == c
        }) : !0
    }
    return!1
};
goog.events.expose = function (a) {
    var b = [], c;
    for (c in a)a[c] && a[c].id ? b.push(c + " = " + a[c] + " (" + a[c].id + ")") : b.push(c + " = " + a[c]);
    return b.join("\n")
};
goog.events.getOnString_ = function (a) {
    return a in goog.events.onStringMap_ ? goog.events.onStringMap_[a] : goog.events.onStringMap_[a] = goog.events.onString_ + a
};
goog.events.fireListeners = function (a, b, c, d) {
    if (goog.events.Listenable.isImplementedBy(a))return a.fireListeners(b, c, d);
    var e = goog.events.listenerTree_;
    return b in e && (e = e[b], c in e) ? goog.events.fireListeners_(e[c], a, b, c, d) : !0
};
goog.events.fireListeners_ = function (a, b, c, d, e) {
    c = 1;
    b = goog.getUid(b);
    if (a[b])for (a = goog.array.clone(a[b]), b = 0; b < a.length; b++)(d = a[b]) && !d.removed && (c &= !1 !== goog.events.fireListener(d, e));
    return Boolean(c)
};
goog.events.fireListener = function (a, b) {
    var c = a.listener, d = a.handler || a.src;
    a.callOnce && goog.events.unlistenByKey(a);
    return c.call(d, b)
};
goog.events.getTotalListenerCount = function () {
    return goog.object.getCount(goog.events.listeners_)
};
goog.events.dispatchEvent = function (a, b) {
    goog.asserts.assert(goog.events.Listenable.isImplementedBy(a), "Can not use goog.events.dispatchEvent with non-goog.events.Listenable instance.");
    return a.dispatchEvent(b)
};
goog.events.protectBrowserEventEntryPoint = function (a) {
    goog.events.handleBrowserEvent_ = a.protectEntryPoint(goog.events.handleBrowserEvent_)
};
goog.events.handleBrowserEvent_ = function (a, b) {
    if (a.removed)return!0;
    var c = a.type, d = goog.events.listenerTree_;
    if (!(c in d))return!0;
    var d = d[c], e, f;
    if (!goog.events.BrowserFeature.HAS_W3C_EVENT_SUPPORT) {
        e = b || goog.getObjectByName("window.event");
        var g = !0 in d, h = !1 in d;
        if (g) {
            if (goog.events.isMarkedIeEvent_(e))return!0;
            goog.events.markIeEvent_(e)
        }
        var k = new goog.events.BrowserEvent;
        k.init(e, this);
        e = !0;
        try {
            if (g) {
                for (var l = [], m = k.currentTarget; m; m = m.parentNode)l.push(m);
                f = d[!0];
                for (var n = l.length - 1; !k.propagationStopped_ &&
                    0 <= n; n--)k.currentTarget = l[n], e &= goog.events.fireListeners_(f, l[n], c, !0, k);
                if (h)for (f = d[!1], n = 0; !k.propagationStopped_ && n < l.length; n++)k.currentTarget = l[n], e &= goog.events.fireListeners_(f, l[n], c, !1, k)
            } else e = goog.events.fireListener(a, k)
        } finally {
            l && (l.length = 0)
        }
        return e
    }
    c = new goog.events.BrowserEvent(b, this);
    return e = goog.events.fireListener(a, c)
};
goog.events.markIeEvent_ = function (a) {
    var b = !1;
    if (0 == a.keyCode)try {
        a.keyCode = -1;
        return
    } catch (c) {
        b = !0
    }
    if (b || void 0 == a.returnValue)a.returnValue = !0
};
goog.events.isMarkedIeEvent_ = function (a) {
    return 0 > a.keyCode || void 0 != a.returnValue
};
goog.events.uniqueIdCounter_ = 0;
goog.events.getUniqueId = function (a) {
    return a + "_" + goog.events.uniqueIdCounter_++
};
goog.events.LISTENER_WRAPPER_PROP_ = "__closure_events_fn_" + (1E9 * Math.random() >>> 0);
goog.events.wrapListener_ = function (a) {
    goog.asserts.assert(a, "Listener can not be null.");
    if (goog.isFunction(a))return a;
    goog.asserts.assert(a.handleEvent, "An object listener must have handleEvent method.");
    return a[goog.events.LISTENER_WRAPPER_PROP_] || (a[goog.events.LISTENER_WRAPPER_PROP_] = function (b) {
        return a.handleEvent(b)
    })
};
goog.debug.entryPointRegistry.register(function (a) {
    goog.events.handleBrowserEvent_ = a(goog.events.handleBrowserEvent_)
});
goog.events.EventHandler = function (a) {
    goog.Disposable.call(this);
    this.handler_ = a;
    this.keys_ = {}
};
goog.inherits(goog.events.EventHandler, goog.Disposable);
goog.events.EventHandler.typeArray_ = [];
goog.events.EventHandler.prototype.listen = function (a, b, c, d, e) {
    goog.isArray(b) || (goog.events.EventHandler.typeArray_[0] = b, b = goog.events.EventHandler.typeArray_);
    for (var f = 0; f < b.length; f++) {
        var g = goog.events.listen(a, b[f], c || this, d || !1, e || this.handler_ || this);
        if (goog.DEBUG && !g)break;
        this.keys_[g.key] = g
    }
    return this
};
goog.events.EventHandler.prototype.listenOnce = function (a, b, c, d, e) {
    if (goog.isArray(b))for (var f = 0; f < b.length; f++)this.listenOnce(a, b[f], c, d, e); else a = goog.events.listenOnce(a, b, c || this, d, e || this.handler_ || this), this.keys_[a.key] = a;
    return this
};
goog.events.EventHandler.prototype.listenWithWrapper = function (a, b, c, d, e) {
    b.listen(a, c, d, e || this.handler_ || this, this);
    return this
};
goog.events.EventHandler.prototype.getListenerCount = function () {
    var a = 0, b;
    for (b in this.keys_)Object.prototype.hasOwnProperty.call(this.keys_, b) && a++;
    return a
};
goog.events.EventHandler.prototype.unlisten = function (a, b, c, d, e) {
    if (goog.isArray(b))for (var f = 0; f < b.length; f++)this.unlisten(a, b[f], c, d, e); else if (a = goog.events.getListener(a, b, c || this, d, e || this.handler_ || this))goog.events.unlistenByKey(a), delete this.keys_[a.key];
    return this
};
goog.events.EventHandler.prototype.unlistenWithWrapper = function (a, b, c, d, e) {
    b.unlisten(a, c, d, e || this.handler_ || this, this);
    return this
};
goog.events.EventHandler.prototype.removeAll = function () {
    goog.object.forEach(this.keys_, goog.events.unlistenByKey);
    this.keys_ = {}
};
goog.events.EventHandler.prototype.disposeInternal = function () {
    goog.events.EventHandler.superClass_.disposeInternal.call(this);
    this.removeAll()
};
goog.events.EventHandler.prototype.handleEvent = function (a) {
    throw Error("EventHandler.handleEvent not implemented");
};
goog.style.bidi = {};
goog.style.bidi.getScrollLeft = function (a) {
    var b = goog.style.isRightToLeft(a);
    return b && goog.userAgent.GECKO ? -a.scrollLeft : !b || goog.userAgent.IE && goog.userAgent.isVersionOrHigher("8") || "visible" == goog.style.getComputedOverflowX(a) ? a.scrollLeft : a.scrollWidth - a.clientWidth - a.scrollLeft
};
goog.style.bidi.getOffsetStart = function (a) {
    var b = a.offsetLeft, c = a.offsetParent;
    c || "fixed" != goog.style.getComputedPosition(a) || (c = goog.dom.getOwnerDocument(a).documentElement);
    if (!c)return b;
    if (goog.userAgent.GECKO)var d = goog.style.getBorderBox(c), b = b + d.left; else goog.userAgent.isDocumentModeOrHigher(8) && (d = goog.style.getBorderBox(c), b -= d.left);
    return goog.style.isRightToLeft(c) ? c.clientWidth - (b + a.offsetWidth) : b
};
goog.style.bidi.setScrollOffset = function (a, b) {
    b = Math.max(b, 0);
    goog.style.isRightToLeft(a) ? goog.userAgent.GECKO ? a.scrollLeft = -b : goog.userAgent.IE && goog.userAgent.isVersionOrHigher("8") ? a.scrollLeft = b : a.scrollLeft = a.scrollWidth - b - a.clientWidth : a.scrollLeft = b
};
goog.style.bidi.setPosition = function (a, b, c, d) {
    goog.isNull(c) || (a.style.top = c + "px");
    d ? (a.style.right = b + "px", a.style.left = "") : (a.style.left = b + "px", a.style.right = "")
};
goog.events.ListenerMap = function (a) {
    this.src = a;
    this.listeners = {};
    this.typeCount_ = 0
};
goog.events.ListenerMap.prototype.getTypeCount = function () {
    return this.typeCount_
};
goog.events.ListenerMap.prototype.getListenerCount = function () {
    var a = 0, b;
    for (b in this.listeners)a += this.listeners[b].length;
    return a
};
goog.events.ListenerMap.prototype.add = function (a, b, c, d, e) {
    var f = this.listeners[a];
    f || (f = this.listeners[a] = [], this.typeCount_++);
    var g = goog.events.ListenerMap.findListenerIndex_(f, b, d, e);
    -1 < g ? (a = f[g], c || (a.callOnce = !1)) : (a = new goog.events.Listener(b, null, this.src, a, !!d, e), a.callOnce = c, f.push(a));
    return a
};
goog.events.ListenerMap.prototype.remove = function (a, b, c, d) {
    if (!(a in this.listeners))return!1;
    var e = this.listeners[a];
    b = goog.events.ListenerMap.findListenerIndex_(e, b, c, d);
    return-1 < b ? (e[b].markAsRemoved(), goog.array.removeAt(e, b), 0 == e.length && (delete this.listeners[a], this.typeCount_--), !0) : !1
};
goog.events.ListenerMap.prototype.removeByKey = function (a) {
    var b = a.type;
    if (!(b in this.listeners))return!1;
    var c = goog.array.remove(this.listeners[b], a);
    c && (a.markAsRemoved(), 0 == this.listeners[b].length && (delete this.listeners[b], this.typeCount_--));
    return c
};
goog.events.ListenerMap.prototype.removeAll = function (a) {
    var b = 0, c;
    for (c in this.listeners)if (!a || c == a) {
        for (var d = this.listeners[c], e = 0; e < d.length; e++)++b, d[e].removed = !0;
        delete this.listeners[c];
        this.typeCount_--
    }
    return b
};
goog.events.ListenerMap.prototype.getListeners = function (a, b) {
    var c = this.listeners[a], d = [];
    if (c)for (var e = 0; e < c.length; ++e) {
        var f = c[e];
        f.capture == b && d.push(f)
    }
    return d
};
goog.events.ListenerMap.prototype.getListener = function (a, b, c, d) {
    a = this.listeners[a];
    var e = -1;
    a && (e = goog.events.ListenerMap.findListenerIndex_(a, b, c, d));
    return-1 < e ? a[e] : null
};
goog.events.ListenerMap.prototype.hasListener = function (a, b) {
    var c = goog.isDef(a), d = goog.isDef(b);
    return goog.object.some(this.listeners, function (e, f) {
        for (var g = 0; g < e.length; ++g)if (!(c && e[g].type != a || d && e[g].capture != b))return!0;
        return!1
    })
};
goog.events.ListenerMap.findListenerIndex_ = function (a, b, c, d) {
    for (var e = 0; e < a.length; ++e) {
        var f = a[e];
        if (!f.removed && f.listener == b && f.capture == !!c && f.handler == d)return e
    }
    return-1
};
goog.events.EventTarget = function () {
    goog.Disposable.call(this);
    this.eventTargetListeners_ = new goog.events.ListenerMap(this);
    this.actualEventTarget_ = this
};
goog.inherits(goog.events.EventTarget, goog.Disposable);
goog.events.Listenable.addImplementation(goog.events.EventTarget);
goog.events.EventTarget.MAX_ANCESTORS_ = 1E3;
goog.events.EventTarget.prototype.parentEventTarget_ = null;
goog.events.EventTarget.prototype.getParentEventTarget = function () {
    return this.parentEventTarget_
};
goog.events.EventTarget.prototype.setParentEventTarget = function (a) {
    this.parentEventTarget_ = a
};
goog.events.EventTarget.prototype.addEventListener = function (a, b, c, d) {
    goog.events.listen(this, a, b, c, d)
};
goog.events.EventTarget.prototype.removeEventListener = function (a, b, c, d) {
    goog.events.unlisten(this, a, b, c, d)
};
goog.events.EventTarget.prototype.dispatchEvent = function (a) {
    this.assertInitialized_();
    var b, c = this.getParentEventTarget();
    if (c) {
        b = [];
        for (var d = 1; c; c = c.getParentEventTarget())b.push(c), goog.asserts.assert(++d < goog.events.EventTarget.MAX_ANCESTORS_, "infinite loop")
    }
    return goog.events.EventTarget.dispatchEventInternal_(this.actualEventTarget_, a, b)
};
goog.events.EventTarget.prototype.disposeInternal = function () {
    goog.events.EventTarget.superClass_.disposeInternal.call(this);
    this.removeAllListeners();
    this.parentEventTarget_ = null
};
goog.events.EventTarget.prototype.listen = function (a, b, c, d) {
    this.assertInitialized_();
    return this.eventTargetListeners_.add(a, b, !1, c, d)
};
goog.events.EventTarget.prototype.listenOnce = function (a, b, c, d) {
    return this.eventTargetListeners_.add(a, b, !0, c, d)
};
goog.events.EventTarget.prototype.unlisten = function (a, b, c, d) {
    return this.eventTargetListeners_.remove(a, b, c, d)
};
goog.events.EventTarget.prototype.unlistenByKey = function (a) {
    return this.eventTargetListeners_.removeByKey(a)
};
goog.events.EventTarget.prototype.removeAllListeners = function (a) {
    return this.eventTargetListeners_ ? this.eventTargetListeners_.removeAll(a) : 0
};
goog.events.EventTarget.prototype.fireListeners = function (a, b, c) {
    a = this.eventTargetListeners_.listeners[a];
    if (!a)return!0;
    a = goog.array.clone(a);
    for (var d = !0, e = 0; e < a.length; ++e) {
        var f = a[e];
        if (f && !f.removed && f.capture == b) {
            var g = f.listener, h = f.handler || f.src;
            f.callOnce && this.unlistenByKey(f);
            d = !1 !== g.call(h, c) && d
        }
    }
    return d && !1 != c.returnValue_
};
goog.events.EventTarget.prototype.getListeners = function (a, b) {
    return this.eventTargetListeners_.getListeners(a, b)
};
goog.events.EventTarget.prototype.getListener = function (a, b, c, d) {
    return this.eventTargetListeners_.getListener(a, b, c, d)
};
goog.events.EventTarget.prototype.hasListener = function (a, b) {
    return this.eventTargetListeners_.hasListener(a, b)
};
goog.events.EventTarget.prototype.setTargetForTesting = function (a) {
    this.actualEventTarget_ = a
};
goog.events.EventTarget.prototype.assertInitialized_ = function () {
    goog.asserts.assert(this.eventTargetListeners_, "Event target is not initialized. Did you call the superclass (goog.events.EventTarget) constructor?")
};
goog.events.EventTarget.dispatchEventInternal_ = function (a, b, c) {
    var d = b.type || b;
    if (goog.isString(b))b = new goog.events.Event(b, a); else if (b instanceof goog.events.Event)b.target = b.target || a; else {
        var e = b;
        b = new goog.events.Event(d, a);
        goog.object.extend(b, e)
    }
    var e = !0, f;
    if (c)for (var g = c.length - 1; !b.propagationStopped_ && 0 <= g; g--)f = b.currentTarget = c[g], e = f.fireListeners(d, !0, b) && e;
    b.propagationStopped_ || (f = b.currentTarget = a, e = f.fireListeners(d, !0, b) && e, b.propagationStopped_ || (e = f.fireListeners(d, !1, b) && e));
    if (c)for (g = 0; !b.propagationStopped_ && g < c.length; g++)f = b.currentTarget = c[g], e = f.fireListeners(d, !1, b) && e;
    return e
};
goog.fx = {};
goog.fx.Dragger = function (a, b, c) {
    goog.events.EventTarget.call(this);
    this.target = a;
    this.handle = b || a;
    this.limits = c || new goog.math.Rect(NaN, NaN, NaN, NaN);
    this.document_ = goog.dom.getOwnerDocument(a);
    this.eventHandler_ = new goog.events.EventHandler(this);
    this.registerDisposable(this.eventHandler_);
    goog.events.listen(this.handle, [goog.events.EventType.TOUCHSTART, goog.events.EventType.MOUSEDOWN], this.startDrag, !1, this)
};
goog.inherits(goog.fx.Dragger, goog.events.EventTarget);
goog.fx.Dragger.HAS_SET_CAPTURE_ = goog.userAgent.IE || goog.userAgent.GECKO && goog.userAgent.isVersionOrHigher("1.9.3");
goog.fx.Dragger.EventType = {EARLY_CANCEL: "earlycancel", START: "start", BEFOREDRAG: "beforedrag", DRAG: "drag", END: "end"};
goog.fx.Dragger.prototype.clientX = 0;
goog.fx.Dragger.prototype.clientY = 0;
goog.fx.Dragger.prototype.screenX = 0;
goog.fx.Dragger.prototype.screenY = 0;
goog.fx.Dragger.prototype.startX = 0;
goog.fx.Dragger.prototype.startY = 0;
goog.fx.Dragger.prototype.deltaX = 0;
goog.fx.Dragger.prototype.deltaY = 0;
goog.fx.Dragger.prototype.enabled_ = !0;
goog.fx.Dragger.prototype.dragging_ = !1;
goog.fx.Dragger.prototype.hysteresisDistanceSquared_ = 0;
goog.fx.Dragger.prototype.mouseDownTime_ = 0;
goog.fx.Dragger.prototype.ieDragStartCancellingOn_ = !1;
goog.fx.Dragger.prototype.useRightPositioningForRtl_ = !1;
goog.fx.Dragger.prototype.enableRightPositioningForRtl = function (a) {
    this.useRightPositioningForRtl_ = a
};
goog.fx.Dragger.prototype.getHandler = function () {
    return this.eventHandler_
};
goog.fx.Dragger.prototype.setLimits = function (a) {
    this.limits = a || new goog.math.Rect(NaN, NaN, NaN, NaN)
};
goog.fx.Dragger.prototype.setHysteresis = function (a) {
    this.hysteresisDistanceSquared_ = Math.pow(a, 2)
};
goog.fx.Dragger.prototype.getHysteresis = function () {
    return Math.sqrt(this.hysteresisDistanceSquared_)
};
goog.fx.Dragger.prototype.setScrollTarget = function (a) {
    this.scrollTarget_ = a
};
goog.fx.Dragger.prototype.setCancelIeDragStart = function (a) {
    this.ieDragStartCancellingOn_ = a
};
goog.fx.Dragger.prototype.getEnabled = function () {
    return this.enabled_
};
goog.fx.Dragger.prototype.setEnabled = function (a) {
    this.enabled_ = a
};
goog.fx.Dragger.prototype.disposeInternal = function () {
    goog.fx.Dragger.superClass_.disposeInternal.call(this);
    goog.events.unlisten(this.handle, [goog.events.EventType.TOUCHSTART, goog.events.EventType.MOUSEDOWN], this.startDrag, !1, this);
    this.cleanUpAfterDragging_();
    this.handle = this.target = null
};
goog.fx.Dragger.prototype.isRightToLeft_ = function () {
    goog.isDef(this.rightToLeft_) || (this.rightToLeft_ = goog.style.isRightToLeft(this.target));
    return this.rightToLeft_
};
goog.fx.Dragger.prototype.startDrag = function (a) {
    var b = a.type == goog.events.EventType.MOUSEDOWN;
    if (!this.enabled_ || this.dragging_ || b && !a.isMouseActionButton())this.dispatchEvent(goog.fx.Dragger.EventType.EARLY_CANCEL); else {
        this.maybeReinitTouchEvent_(a);
        if (0 == this.hysteresisDistanceSquared_)if (this.fireDragStart_(a))this.dragging_ = !0, a.preventDefault(); else return; else a.preventDefault();
        this.setupDragHandlers();
        this.clientX = this.startX = a.clientX;
        this.clientY = this.startY = a.clientY;
        this.screenX = a.screenX;
        this.screenY = a.screenY;
        this.deltaX = this.useRightPositioningForRtl_ ? goog.style.bidi.getOffsetStart(this.target) : this.target.offsetLeft;
        this.deltaY = this.target.offsetTop;
        this.pageScroll = goog.dom.getDomHelper(this.document_).getDocumentScroll();
        this.mouseDownTime_ = goog.now()
    }
};
goog.fx.Dragger.prototype.setupDragHandlers = function () {
    var a = this.document_, b = a.documentElement, c = !goog.fx.Dragger.HAS_SET_CAPTURE_;
    this.eventHandler_.listen(a, [goog.events.EventType.TOUCHMOVE, goog.events.EventType.MOUSEMOVE], this.handleMove_, c);
    this.eventHandler_.listen(a, [goog.events.EventType.TOUCHEND, goog.events.EventType.MOUSEUP], this.endDrag, c);
    goog.fx.Dragger.HAS_SET_CAPTURE_ ? (b.setCapture(!1), this.eventHandler_.listen(b, goog.events.EventType.LOSECAPTURE, this.endDrag)) : this.eventHandler_.listen(goog.dom.getWindow(a),
        goog.events.EventType.BLUR, this.endDrag);
    goog.userAgent.IE && this.ieDragStartCancellingOn_ && this.eventHandler_.listen(a, goog.events.EventType.DRAGSTART, goog.events.Event.preventDefault);
    this.scrollTarget_ && this.eventHandler_.listen(this.scrollTarget_, goog.events.EventType.SCROLL, this.onScroll_, c)
};
goog.fx.Dragger.prototype.fireDragStart_ = function (a) {
    return this.dispatchEvent(new goog.fx.DragEvent(goog.fx.Dragger.EventType.START, this, a.clientX, a.clientY, a))
};
goog.fx.Dragger.prototype.cleanUpAfterDragging_ = function () {
    this.eventHandler_.removeAll();
    goog.fx.Dragger.HAS_SET_CAPTURE_ && this.document_.releaseCapture()
};
goog.fx.Dragger.prototype.endDrag = function (a, b) {
    this.cleanUpAfterDragging_();
    if (this.dragging_) {
        this.maybeReinitTouchEvent_(a);
        this.dragging_ = !1;
        var c = this.limitX(this.deltaX), d = this.limitY(this.deltaY);
        this.dispatchEvent(new goog.fx.DragEvent(goog.fx.Dragger.EventType.END, this, a.clientX, a.clientY, a, c, d, b || a.type == goog.events.EventType.TOUCHCANCEL))
    } else this.dispatchEvent(goog.fx.Dragger.EventType.EARLY_CANCEL)
};
goog.fx.Dragger.prototype.endDragCancel = function (a) {
    this.endDrag(a, !0)
};
goog.fx.Dragger.prototype.maybeReinitTouchEvent_ = function (a) {
    var b = a.type;
    b == goog.events.EventType.TOUCHSTART || b == goog.events.EventType.TOUCHMOVE ? a.init(a.getBrowserEvent().targetTouches[0], a.currentTarget) : b != goog.events.EventType.TOUCHEND && b != goog.events.EventType.TOUCHCANCEL || a.init(a.getBrowserEvent().changedTouches[0], a.currentTarget)
};
goog.fx.Dragger.prototype.handleMove_ = function (a) {
    if (this.enabled_) {
        this.maybeReinitTouchEvent_(a);
        var b = (this.useRightPositioningForRtl_ && this.isRightToLeft_() ? -1 : 1) * (a.clientX - this.clientX), c = a.clientY - this.clientY;
        this.clientX = a.clientX;
        this.clientY = a.clientY;
        this.screenX = a.screenX;
        this.screenY = a.screenY;
        if (!this.dragging_) {
            var d = this.startX - this.clientX, e = this.startY - this.clientY;
            if (d * d + e * e > this.hysteresisDistanceSquared_)if (this.fireDragStart_(a))this.dragging_ = !0; else {
                this.isDisposed() || this.endDrag(a);
                return
            }
        }
        c = this.calculatePosition_(b, c);
        b = c.x;
        c = c.y;
        this.dragging_ && this.dispatchEvent(new goog.fx.DragEvent(goog.fx.Dragger.EventType.BEFOREDRAG, this, a.clientX, a.clientY, a, b, c)) && (this.doDrag(a, b, c, !1), a.preventDefault())
    }
};
goog.fx.Dragger.prototype.calculatePosition_ = function (a, b) {
    var c = goog.dom.getDomHelper(this.document_).getDocumentScroll();
    a += c.x - this.pageScroll.x;
    b += c.y - this.pageScroll.y;
    this.pageScroll = c;
    this.deltaX += a;
    this.deltaY += b;
    var c = this.limitX(this.deltaX), d = this.limitY(this.deltaY);
    return new goog.math.Coordinate(c, d)
};
goog.fx.Dragger.prototype.onScroll_ = function (a) {
    var b = this.calculatePosition_(0, 0);
    a.clientX = this.clientX;
    a.clientY = this.clientY;
    this.doDrag(a, b.x, b.y, !0)
};
goog.fx.Dragger.prototype.doDrag = function (a, b, c, d) {
    this.defaultAction(b, c);
    this.dispatchEvent(new goog.fx.DragEvent(goog.fx.Dragger.EventType.DRAG, this, a.clientX, a.clientY, a, b, c))
};
goog.fx.Dragger.prototype.limitX = function (a) {
    var b = this.limits, c = isNaN(b.left) ? null : b.left, b = isNaN(b.width) ? 0 : b.width;
    return Math.min(null != c ? c + b : Infinity, Math.max(null != c ? c : -Infinity, a))
};
goog.fx.Dragger.prototype.limitY = function (a) {
    var b = this.limits, c = isNaN(b.top) ? null : b.top, b = isNaN(b.height) ? 0 : b.height;
    return Math.min(null != c ? c + b : Infinity, Math.max(null != c ? c : -Infinity, a))
};
goog.fx.Dragger.prototype.defaultAction = function (a, b) {
    this.useRightPositioningForRtl_ && this.isRightToLeft_() ? this.target.style.right = a + "px" : this.target.style.left = a + "px";
    this.target.style.top = b + "px"
};
goog.fx.Dragger.prototype.isDragging = function () {
    return this.dragging_
};
goog.fx.DragEvent = function (a, b, c, d, e, f, g, h) {
    goog.events.Event.call(this, a);
    this.clientX = c;
    this.clientY = d;
    this.browserEvent = e;
    this.left = goog.isDef(f) ? f : b.deltaX;
    this.top = goog.isDef(g) ? g : b.deltaY;
    this.dragger = b;
    this.dragCanceled = !!h
};
goog.inherits(goog.fx.DragEvent, goog.events.Event);
goog.Timer = function (a, b) {
    goog.events.EventTarget.call(this);
    this.interval_ = a || 1;
    this.timerObject_ = b || goog.Timer.defaultTimerObject;
    this.boundTick_ = goog.bind(this.tick_, this);
    this.last_ = goog.now()
};
goog.inherits(goog.Timer, goog.events.EventTarget);
goog.Timer.MAX_TIMEOUT_ = 2147483647;
goog.Timer.prototype.enabled = !1;
goog.Timer.defaultTimerObject = goog.global;
goog.Timer.intervalScale = 0.8;
goog.Timer.prototype.timer_ = null;
goog.Timer.prototype.getInterval = function () {
    return this.interval_
};
goog.Timer.prototype.setInterval = function (a) {
    this.interval_ = a;
    this.timer_ && this.enabled ? (this.stop(), this.start()) : this.timer_ && this.stop()
};
goog.Timer.prototype.tick_ = function () {
    if (this.enabled) {
        var a = goog.now() - this.last_;
        0 < a && a < this.interval_ * goog.Timer.intervalScale ? this.timer_ = this.timerObject_.setTimeout(this.boundTick_, this.interval_ - a) : (this.timer_ && (this.timerObject_.clearTimeout(this.timer_), this.timer_ = null), this.dispatchTick(), this.enabled && (this.timer_ = this.timerObject_.setTimeout(this.boundTick_, this.interval_), this.last_ = goog.now()))
    }
};
goog.Timer.prototype.dispatchTick = function () {
    this.dispatchEvent(goog.Timer.TICK)
};
goog.Timer.prototype.start = function () {
    this.enabled = !0;
    this.timer_ || (this.timer_ = this.timerObject_.setTimeout(this.boundTick_, this.interval_), this.last_ = goog.now())
};
goog.Timer.prototype.stop = function () {
    this.enabled = !1;
    this.timer_ && (this.timerObject_.clearTimeout(this.timer_), this.timer_ = null)
};
goog.Timer.prototype.disposeInternal = function () {
    goog.Timer.superClass_.disposeInternal.call(this);
    this.stop();
    delete this.timerObject_
};
goog.Timer.TICK = "tick";
goog.Timer.callOnce = function (a, b, c) {
    if (goog.isFunction(a))c && (a = goog.bind(a, c)); else if (a && "function" == typeof a.handleEvent)a = goog.bind(a.handleEvent, a); else throw Error("Invalid listener argument");
    return b > goog.Timer.MAX_TIMEOUT_ ? -1 : goog.Timer.defaultTimerObject.setTimeout(a, b || 0)
};
goog.Timer.clear = function (a) {
    goog.Timer.defaultTimerObject.clearTimeout(a)
};
goog.events.KeyCodes = {WIN_KEY_FF_LINUX: 0, MAC_ENTER: 3, BACKSPACE: 8, TAB: 9, NUM_CENTER: 12, ENTER: 13, SHIFT: 16, CTRL: 17, ALT: 18, PAUSE: 19, CAPS_LOCK: 20, ESC: 27, SPACE: 32, PAGE_UP: 33, PAGE_DOWN: 34, END: 35, HOME: 36, LEFT: 37, UP: 38, RIGHT: 39, DOWN: 40, PRINT_SCREEN: 44, INSERT: 45, DELETE: 46, ZERO: 48, ONE: 49, TWO: 50, THREE: 51, FOUR: 52, FIVE: 53, SIX: 54, SEVEN: 55, EIGHT: 56, NINE: 57, FF_SEMICOLON: 59, FF_EQUALS: 61, QUESTION_MARK: 63, A: 65, B: 66, C: 67, D: 68, E: 69, F: 70, G: 71, H: 72, I: 73, J: 74, K: 75, L: 76, M: 77, N: 78, O: 79, P: 80, Q: 81, R: 82, S: 83, T: 84, U: 85, V: 86, W: 87,
    X: 88, Y: 89, Z: 90, META: 91, WIN_KEY_RIGHT: 92, CONTEXT_MENU: 93, NUM_ZERO: 96, NUM_ONE: 97, NUM_TWO: 98, NUM_THREE: 99, NUM_FOUR: 100, NUM_FIVE: 101, NUM_SIX: 102, NUM_SEVEN: 103, NUM_EIGHT: 104, NUM_NINE: 105, NUM_MULTIPLY: 106, NUM_PLUS: 107, NUM_MINUS: 109, NUM_PERIOD: 110, NUM_DIVISION: 111, F1: 112, F2: 113, F3: 114, F4: 115, F5: 116, F6: 117, F7: 118, F8: 119, F9: 120, F10: 121, F11: 122, F12: 123, NUMLOCK: 144, SCROLL_LOCK: 145, FIRST_MEDIA_KEY: 166, LAST_MEDIA_KEY: 183, SEMICOLON: 186, DASH: 189, EQUALS: 187, COMMA: 188, PERIOD: 190, SLASH: 191, APOSTROPHE: 192, TILDE: 192,
    SINGLE_QUOTE: 222, OPEN_SQUARE_BRACKET: 219, BACKSLASH: 220, CLOSE_SQUARE_BRACKET: 221, WIN_KEY: 224, MAC_FF_META: 224, WIN_IME: 229, PHANTOM: 255};
goog.events.KeyCodes.isTextModifyingKeyEvent = function (a) {
    if (a.altKey && !a.ctrlKey || a.metaKey || a.keyCode >= goog.events.KeyCodes.F1 && a.keyCode <= goog.events.KeyCodes.F12)return!1;
    switch (a.keyCode) {
        case goog.events.KeyCodes.ALT:
        case goog.events.KeyCodes.CAPS_LOCK:
        case goog.events.KeyCodes.CONTEXT_MENU:
        case goog.events.KeyCodes.CTRL:
        case goog.events.KeyCodes.DOWN:
        case goog.events.KeyCodes.END:
        case goog.events.KeyCodes.ESC:
        case goog.events.KeyCodes.HOME:
        case goog.events.KeyCodes.INSERT:
        case goog.events.KeyCodes.LEFT:
        case goog.events.KeyCodes.MAC_FF_META:
        case goog.events.KeyCodes.META:
        case goog.events.KeyCodes.NUMLOCK:
        case goog.events.KeyCodes.NUM_CENTER:
        case goog.events.KeyCodes.PAGE_DOWN:
        case goog.events.KeyCodes.PAGE_UP:
        case goog.events.KeyCodes.PAUSE:
        case goog.events.KeyCodes.PHANTOM:
        case goog.events.KeyCodes.PRINT_SCREEN:
        case goog.events.KeyCodes.RIGHT:
        case goog.events.KeyCodes.SCROLL_LOCK:
        case goog.events.KeyCodes.SHIFT:
        case goog.events.KeyCodes.UP:
        case goog.events.KeyCodes.WIN_KEY:
        case goog.events.KeyCodes.WIN_KEY_RIGHT:
            return!1;
        case goog.events.KeyCodes.WIN_KEY_FF_LINUX:
            return!goog.userAgent.GECKO;
        default:
            return a.keyCode < goog.events.KeyCodes.FIRST_MEDIA_KEY || a.keyCode > goog.events.KeyCodes.LAST_MEDIA_KEY
    }
};
goog.events.KeyCodes.firesKeyPressEvent = function (a, b, c, d, e) {
    if (!(goog.userAgent.IE || goog.userAgent.WEBKIT && goog.userAgent.isVersionOrHigher("525")))return!0;
    if (goog.userAgent.MAC && e)return goog.events.KeyCodes.isCharacterKey(a);
    if (e && !d || !c && (b == goog.events.KeyCodes.CTRL || b == goog.events.KeyCodes.ALT || goog.userAgent.MAC && b == goog.events.KeyCodes.META))return!1;
    if (goog.userAgent.WEBKIT && d && c)switch (a) {
        case goog.events.KeyCodes.BACKSLASH:
        case goog.events.KeyCodes.OPEN_SQUARE_BRACKET:
        case goog.events.KeyCodes.CLOSE_SQUARE_BRACKET:
        case goog.events.KeyCodes.TILDE:
        case goog.events.KeyCodes.SEMICOLON:
        case goog.events.KeyCodes.DASH:
        case goog.events.KeyCodes.EQUALS:
        case goog.events.KeyCodes.COMMA:
        case goog.events.KeyCodes.PERIOD:
        case goog.events.KeyCodes.SLASH:
        case goog.events.KeyCodes.APOSTROPHE:
        case goog.events.KeyCodes.SINGLE_QUOTE:
            return!1
    }
    if (goog.userAgent.IE &&
        d && b == a)return!1;
    switch (a) {
        case goog.events.KeyCodes.ENTER:
            return!(goog.userAgent.IE && goog.userAgent.isDocumentModeOrHigher(9));
        case goog.events.KeyCodes.ESC:
            return!goog.userAgent.WEBKIT
    }
    return goog.events.KeyCodes.isCharacterKey(a)
};
goog.events.KeyCodes.isCharacterKey = function (a) {
    if (a >= goog.events.KeyCodes.ZERO && a <= goog.events.KeyCodes.NINE || a >= goog.events.KeyCodes.NUM_ZERO && a <= goog.events.KeyCodes.NUM_MULTIPLY || a >= goog.events.KeyCodes.A && a <= goog.events.KeyCodes.Z || goog.userAgent.WEBKIT && 0 == a)return!0;
    switch (a) {
        case goog.events.KeyCodes.SPACE:
        case goog.events.KeyCodes.QUESTION_MARK:
        case goog.events.KeyCodes.NUM_PLUS:
        case goog.events.KeyCodes.NUM_MINUS:
        case goog.events.KeyCodes.NUM_PERIOD:
        case goog.events.KeyCodes.NUM_DIVISION:
        case goog.events.KeyCodes.SEMICOLON:
        case goog.events.KeyCodes.FF_SEMICOLON:
        case goog.events.KeyCodes.DASH:
        case goog.events.KeyCodes.EQUALS:
        case goog.events.KeyCodes.FF_EQUALS:
        case goog.events.KeyCodes.COMMA:
        case goog.events.KeyCodes.PERIOD:
        case goog.events.KeyCodes.SLASH:
        case goog.events.KeyCodes.APOSTROPHE:
        case goog.events.KeyCodes.SINGLE_QUOTE:
        case goog.events.KeyCodes.OPEN_SQUARE_BRACKET:
        case goog.events.KeyCodes.BACKSLASH:
        case goog.events.KeyCodes.CLOSE_SQUARE_BRACKET:
            return!0;
        default:
            return!1
    }
};
goog.events.KeyCodes.normalizeGeckoKeyCode = function (a) {
    switch (a) {
        case goog.events.KeyCodes.FF_EQUALS:
            return goog.events.KeyCodes.EQUALS;
        case goog.events.KeyCodes.FF_SEMICOLON:
            return goog.events.KeyCodes.SEMICOLON;
        case goog.events.KeyCodes.MAC_FF_META:
            return goog.events.KeyCodes.META;
        case goog.events.KeyCodes.WIN_KEY_FF_LINUX:
            return goog.events.KeyCodes.WIN_KEY;
        default:
            return a
    }
};
goog.fx.Transition = function () {
};
goog.fx.Transition.EventType = {PLAY: "play", BEGIN: "begin", RESUME: "resume", END: "end", STOP: "stop", FINISH: "finish", PAUSE: "pause"};
goog.ui = {};
goog.ui.PopupBase = function (a, b) {
    goog.events.EventTarget.call(this);
    this.handler_ = new goog.events.EventHandler(this);
    this.setElement(a || null);
    b && this.setType(b)
};
goog.inherits(goog.ui.PopupBase, goog.events.EventTarget);
goog.ui.PopupBase.Type = {TOGGLE_DISPLAY: "toggle_display", MOVE_OFFSCREEN: "move_offscreen"};
goog.ui.PopupBase.prototype.element_ = null;
goog.ui.PopupBase.prototype.autoHide_ = !0;
goog.ui.PopupBase.prototype.autoHideRegion_ = null;
goog.ui.PopupBase.prototype.isVisible_ = !1;
goog.ui.PopupBase.prototype.shouldHideAsync_ = !1;
goog.ui.PopupBase.prototype.lastShowTime_ = -1;
goog.ui.PopupBase.prototype.lastHideTime_ = -1;
goog.ui.PopupBase.prototype.hideOnEscape_ = !1;
goog.ui.PopupBase.prototype.enableCrossIframeDismissal_ = !0;
goog.ui.PopupBase.prototype.type_ = goog.ui.PopupBase.Type.TOGGLE_DISPLAY;
goog.ui.PopupBase.EventType = {BEFORE_SHOW: "beforeshow", SHOW: "show", BEFORE_HIDE: "beforehide", HIDE: "hide"};
goog.ui.PopupBase.DEBOUNCE_DELAY_MS = 150;
goog.ui.PopupBase.prototype.getType = function () {
    return this.type_
};
goog.ui.PopupBase.prototype.setType = function (a) {
    this.type_ = a
};
goog.ui.PopupBase.prototype.shouldHideAsync = function () {
    return this.shouldHideAsync_
};
goog.ui.PopupBase.prototype.setShouldHideAsync = function (a) {
    this.shouldHideAsync_ = a
};
goog.ui.PopupBase.prototype.getElement = function () {
    return this.element_
};
goog.ui.PopupBase.prototype.setElement = function (a) {
    this.ensureNotVisible_();
    this.element_ = a
};
goog.ui.PopupBase.prototype.getAutoHide = function () {
    return this.autoHide_
};
goog.ui.PopupBase.prototype.setAutoHide = function (a) {
    this.ensureNotVisible_();
    this.autoHide_ = a
};
goog.ui.PopupBase.prototype.getHideOnEscape = function () {
    return this.hideOnEscape_
};
goog.ui.PopupBase.prototype.setHideOnEscape = function (a) {
    this.ensureNotVisible_();
    this.hideOnEscape_ = a
};
goog.ui.PopupBase.prototype.getEnableCrossIframeDismissal = function () {
    return this.enableCrossIframeDismissal_
};
goog.ui.PopupBase.prototype.setEnableCrossIframeDismissal = function (a) {
    this.enableCrossIframeDismissal_ = a
};
goog.ui.PopupBase.prototype.getAutoHideRegion = function () {
    return this.autoHideRegion_
};
goog.ui.PopupBase.prototype.setAutoHideRegion = function (a) {
    this.autoHideRegion_ = a
};
goog.ui.PopupBase.prototype.setTransition = function (a, b) {
    this.showTransition_ = a;
    this.hideTransition_ = b
};
goog.ui.PopupBase.prototype.getLastShowTime = function () {
    return this.lastShowTime_
};
goog.ui.PopupBase.prototype.getLastHideTime = function () {
    return this.lastHideTime_
};
goog.ui.PopupBase.prototype.getHandler = function () {
    return this.handler_
};
goog.ui.PopupBase.prototype.ensureNotVisible_ = function () {
    if (this.isVisible_)throw Error("Can not change this state of the popup while showing.");
};
goog.ui.PopupBase.prototype.isVisible = function () {
    return this.isVisible_
};
goog.ui.PopupBase.prototype.isOrWasRecentlyVisible = function () {
    return this.isVisible_ || goog.now() - this.lastHideTime_ < goog.ui.PopupBase.DEBOUNCE_DELAY_MS
};
goog.ui.PopupBase.prototype.setVisible = function (a) {
    this.showTransition_ && this.showTransition_.stop();
    this.hideTransition_ && this.hideTransition_.stop();
    a ? this.show_() : this.hide_()
};
goog.ui.PopupBase.prototype.reposition = goog.nullFunction;
goog.ui.PopupBase.prototype.show_ = function () {
    if (!this.isVisible_ && this.onBeforeShow()) {
        if (!this.element_)throw Error("Caller must call setElement before trying to show the popup");
        this.reposition();
        var a = goog.dom.getOwnerDocument(this.element_);
        this.hideOnEscape_ && this.handler_.listen(a, goog.events.EventType.KEYDOWN, this.onDocumentKeyDown_, !0);
        if (this.autoHide_)if (this.handler_.listen(a, goog.events.EventType.MOUSEDOWN, this.onDocumentMouseDown_, !0), goog.userAgent.IE) {
            var b;
            try {
                b = a.activeElement
            } catch (c) {
            }
            for (; b &&
                       "IFRAME" == b.nodeName;) {
                try {
                    var d = goog.dom.getFrameContentDocument(b)
                } catch (e) {
                    break
                }
                a = d;
                b = a.activeElement
            }
            this.handler_.listen(a, goog.events.EventType.MOUSEDOWN, this.onDocumentMouseDown_, !0);
            this.handler_.listen(a, goog.events.EventType.DEACTIVATE, this.onDocumentBlur_)
        } else this.handler_.listen(a, goog.events.EventType.BLUR, this.onDocumentBlur_);
        this.type_ == goog.ui.PopupBase.Type.TOGGLE_DISPLAY ? this.showPopupElement() : this.type_ == goog.ui.PopupBase.Type.MOVE_OFFSCREEN && this.reposition();
        this.isVisible_ = !0;
        if (this.showTransition_)goog.events.listenOnce(this.showTransition_, goog.fx.Transition.EventType.END, this.onShow_, !1, this), this.showTransition_.play(); else this.onShow_()
    }
};
goog.ui.PopupBase.prototype.hide_ = function (a) {
    if (!this.isVisible_ || !this.onBeforeHide_(a))return!1;
    this.handler_ && this.handler_.removeAll();
    this.isVisible_ = !1;
    this.lastHideTime_ = goog.now();
    this.hideTransition_ ? (goog.events.listenOnce(this.hideTransition_, goog.fx.Transition.EventType.END, goog.partial(this.continueHidingPopup_, a), !1, this), this.hideTransition_.play()) : this.continueHidingPopup_(a);
    return!0
};
goog.ui.PopupBase.prototype.continueHidingPopup_ = function (a) {
    this.type_ == goog.ui.PopupBase.Type.TOGGLE_DISPLAY ? this.shouldHideAsync_ ? goog.Timer.callOnce(this.hidePopupElement_, 0, this) : this.hidePopupElement_() : this.type_ == goog.ui.PopupBase.Type.MOVE_OFFSCREEN && this.moveOffscreen_();
    this.onHide_(a)
};
goog.ui.PopupBase.prototype.showPopupElement = function () {
    this.element_.style.visibility = "visible";
    goog.style.setElementShown(this.element_, !0)
};
goog.ui.PopupBase.prototype.hidePopupElement_ = function () {
    this.element_.style.visibility = "hidden";
    goog.style.setElementShown(this.element_, !1)
};
goog.ui.PopupBase.prototype.moveOffscreen_ = function () {
    this.element_.style.top = "-10000px"
};
goog.ui.PopupBase.prototype.onBeforeShow = function () {
    return this.dispatchEvent(goog.ui.PopupBase.EventType.BEFORE_SHOW)
};
goog.ui.PopupBase.prototype.onShow_ = function () {
    this.lastShowTime_ = goog.now();
    this.lastHideTime_ = -1;
    this.dispatchEvent(goog.ui.PopupBase.EventType.SHOW)
};
goog.ui.PopupBase.prototype.onBeforeHide_ = function (a) {
    return this.dispatchEvent({type: goog.ui.PopupBase.EventType.BEFORE_HIDE, target: a})
};
goog.ui.PopupBase.prototype.onHide_ = function (a) {
    this.dispatchEvent({type: goog.ui.PopupBase.EventType.HIDE, target: a})
};
goog.ui.PopupBase.prototype.onDocumentMouseDown_ = function (a) {
    a = a.target;
    goog.dom.contains(this.element_, a) || (this.autoHideRegion_ && !goog.dom.contains(this.autoHideRegion_, a) || this.shouldDebounce_()) || this.hide_(a)
};
goog.ui.PopupBase.prototype.onDocumentKeyDown_ = function (a) {
    a.keyCode == goog.events.KeyCodes.ESC && this.hide_(a.target) && (a.preventDefault(), a.stopPropagation())
};
goog.ui.PopupBase.prototype.onDocumentBlur_ = function (a) {
    if (this.enableCrossIframeDismissal_) {
        var b = goog.dom.getOwnerDocument(this.element_);
        if ("undefined" != typeof document.activeElement) {
            if (a = b.activeElement, !a || goog.dom.contains(this.element_, a) || "BODY" == a.tagName)return
        } else if (a.target != b)return;
        this.shouldDebounce_() || this.hide_()
    }
};
goog.ui.PopupBase.prototype.shouldDebounce_ = function () {
    return goog.now() - this.lastShowTime_ < goog.ui.PopupBase.DEBOUNCE_DELAY_MS
};
goog.ui.PopupBase.prototype.disposeInternal = function () {
    goog.ui.PopupBase.superClass_.disposeInternal.call(this);
    this.handler_.dispose();
    goog.dispose(this.showTransition_);
    goog.dispose(this.hideTransition_);
    delete this.element_;
    delete this.handler_
};
goog.ui.IdGenerator = function () {
};
goog.addSingletonGetter(goog.ui.IdGenerator);
goog.ui.IdGenerator.prototype.nextId_ = 0;
goog.ui.IdGenerator.prototype.getNextUniqueId = function () {
    return":" + (this.nextId_++).toString(36)
};
goog.ui.IdGenerator.instance = goog.ui.IdGenerator.getInstance();
goog.ui.Component = function (a) {
    goog.events.EventTarget.call(this);
    this.dom_ = a || goog.dom.getDomHelper();
    this.rightToLeft_ = goog.ui.Component.defaultRightToLeft_
};
goog.inherits(goog.ui.Component, goog.events.EventTarget);
goog.ui.Component.ALLOW_DETACHED_DECORATION = !1;
goog.ui.Component.prototype.idGenerator_ = goog.ui.IdGenerator.getInstance();
goog.ui.Component.defaultRightToLeft_ = null;
goog.ui.Component.EventType = {BEFORE_SHOW: "beforeshow", SHOW: "show", HIDE: "hide", DISABLE: "disable", ENABLE: "enable", HIGHLIGHT: "highlight", UNHIGHLIGHT: "unhighlight", ACTIVATE: "activate", DEACTIVATE: "deactivate", SELECT: "select", UNSELECT: "unselect", CHECK: "check", UNCHECK: "uncheck", FOCUS: "focus", BLUR: "blur", OPEN: "open", CLOSE: "close", ENTER: "enter", LEAVE: "leave", ACTION: "action", CHANGE: "change"};
goog.ui.Component.Error = {NOT_SUPPORTED: "Method not supported", DECORATE_INVALID: "Invalid element to decorate", ALREADY_RENDERED: "Component already rendered", PARENT_UNABLE_TO_BE_SET: "Unable to set parent component", CHILD_INDEX_OUT_OF_BOUNDS: "Child component index out of bounds", NOT_OUR_CHILD: "Child is not in parent component", NOT_IN_DOCUMENT: "Operation not supported while component is not in document", STATE_INVALID: "Invalid component state"};
goog.ui.Component.State = {ALL: 255, DISABLED: 1, HOVER: 2, ACTIVE: 4, SELECTED: 8, CHECKED: 16, FOCUSED: 32, OPENED: 64};
goog.ui.Component.getStateTransitionEvent = function (a, b) {
    switch (a) {
        case goog.ui.Component.State.DISABLED:
            return b ? goog.ui.Component.EventType.DISABLE : goog.ui.Component.EventType.ENABLE;
        case goog.ui.Component.State.HOVER:
            return b ? goog.ui.Component.EventType.HIGHLIGHT : goog.ui.Component.EventType.UNHIGHLIGHT;
        case goog.ui.Component.State.ACTIVE:
            return b ? goog.ui.Component.EventType.ACTIVATE : goog.ui.Component.EventType.DEACTIVATE;
        case goog.ui.Component.State.SELECTED:
            return b ? goog.ui.Component.EventType.SELECT :
                goog.ui.Component.EventType.UNSELECT;
        case goog.ui.Component.State.CHECKED:
            return b ? goog.ui.Component.EventType.CHECK : goog.ui.Component.EventType.UNCHECK;
        case goog.ui.Component.State.FOCUSED:
            return b ? goog.ui.Component.EventType.FOCUS : goog.ui.Component.EventType.BLUR;
        case goog.ui.Component.State.OPENED:
            return b ? goog.ui.Component.EventType.OPEN : goog.ui.Component.EventType.CLOSE
    }
    throw Error(goog.ui.Component.Error.STATE_INVALID);
};
goog.ui.Component.setDefaultRightToLeft = function (a) {
    goog.ui.Component.defaultRightToLeft_ = a
};
goog.ui.Component.prototype.id_ = null;
goog.ui.Component.prototype.inDocument_ = !1;
goog.ui.Component.prototype.element_ = null;
goog.ui.Component.prototype.rightToLeft_ = null;
goog.ui.Component.prototype.model_ = null;
goog.ui.Component.prototype.parent_ = null;
goog.ui.Component.prototype.children_ = null;
goog.ui.Component.prototype.childIndex_ = null;
goog.ui.Component.prototype.wasDecorated_ = !1;
goog.ui.Component.prototype.getId = function () {
    return this.id_ || (this.id_ = this.idGenerator_.getNextUniqueId())
};
goog.ui.Component.prototype.setId = function (a) {
    this.parent_ && this.parent_.childIndex_ && (goog.object.remove(this.parent_.childIndex_, this.id_), goog.object.add(this.parent_.childIndex_, a, this));
    this.id_ = a
};
goog.ui.Component.prototype.getElement = function () {
    return this.element_
};
goog.ui.Component.prototype.getElementStrict = function () {
    var a = this.element_;
    goog.asserts.assert(a, "Can not call getElementStrict before rendering/decorating.");
    return a
};
goog.ui.Component.prototype.setElementInternal = function (a) {
    this.element_ = a
};
goog.ui.Component.prototype.getElementsByClass = function (a) {
    return this.element_ ? this.dom_.getElementsByClass(a, this.element_) : []
};
goog.ui.Component.prototype.getElementByClass = function (a) {
    return this.element_ ? this.dom_.getElementByClass(a, this.element_) : null
};
goog.ui.Component.prototype.getRequiredElementByClass = function (a) {
    var b = this.getElementByClass(a);
    goog.asserts.assert(b, "Expected element in component with class: %s", a);
    return b
};
goog.ui.Component.prototype.getHandler = function () {
    return this.googUiComponentHandler_ || (this.googUiComponentHandler_ = new goog.events.EventHandler(this))
};
goog.ui.Component.prototype.setParent = function (a) {
    if (this == a)throw Error(goog.ui.Component.Error.PARENT_UNABLE_TO_BE_SET);
    if (a && this.parent_ && this.id_ && this.parent_.getChild(this.id_) && this.parent_ != a)throw Error(goog.ui.Component.Error.PARENT_UNABLE_TO_BE_SET);
    this.parent_ = a;
    goog.ui.Component.superClass_.setParentEventTarget.call(this, a)
};
goog.ui.Component.prototype.getParent = function () {
    return this.parent_
};
goog.ui.Component.prototype.setParentEventTarget = function (a) {
    if (this.parent_ && this.parent_ != a)throw Error(goog.ui.Component.Error.NOT_SUPPORTED);
    goog.ui.Component.superClass_.setParentEventTarget.call(this, a)
};
goog.ui.Component.prototype.getDomHelper = function () {
    return this.dom_
};
goog.ui.Component.prototype.isInDocument = function () {
    return this.inDocument_
};
goog.ui.Component.prototype.createDom = function () {
    this.element_ = this.dom_.createElement("div")
};
goog.ui.Component.prototype.render = function (a) {
    this.render_(a)
};
goog.ui.Component.prototype.renderBefore = function (a) {
    this.render_(a.parentNode, a)
};
goog.ui.Component.prototype.render_ = function (a, b) {
    if (this.inDocument_)throw Error(goog.ui.Component.Error.ALREADY_RENDERED);
    this.element_ || this.createDom();
    a ? a.insertBefore(this.element_, b || null) : this.dom_.getDocument().body.appendChild(this.element_);
    this.parent_ && !this.parent_.isInDocument() || this.enterDocument()
};
goog.ui.Component.prototype.decorate = function (a) {
    if (this.inDocument_)throw Error(goog.ui.Component.Error.ALREADY_RENDERED);
    if (a && this.canDecorate(a)) {
        this.wasDecorated_ = !0;
        var b = goog.dom.getOwnerDocument(a);
        this.dom_ && this.dom_.getDocument() == b || (this.dom_ = goog.dom.getDomHelper(a));
        this.decorateInternal(a);
        goog.ui.Component.ALLOW_DETACHED_DECORATION && !goog.dom.contains(b, a) || this.enterDocument()
    } else throw Error(goog.ui.Component.Error.DECORATE_INVALID);
};
goog.ui.Component.prototype.canDecorate = function (a) {
    return!0
};
goog.ui.Component.prototype.wasDecorated = function () {
    return this.wasDecorated_
};
goog.ui.Component.prototype.decorateInternal = function (a) {
    this.element_ = a
};
goog.ui.Component.prototype.enterDocument = function () {
    this.inDocument_ = !0;
    this.forEachChild(function (a) {
        !a.isInDocument() && a.getElement() && a.enterDocument()
    })
};
goog.ui.Component.prototype.exitDocument = function () {
    this.forEachChild(function (a) {
        a.isInDocument() && a.exitDocument()
    });
    this.googUiComponentHandler_ && this.googUiComponentHandler_.removeAll();
    this.inDocument_ = !1
};
goog.ui.Component.prototype.disposeInternal = function () {
    this.inDocument_ && this.exitDocument();
    this.googUiComponentHandler_ && (this.googUiComponentHandler_.dispose(), delete this.googUiComponentHandler_);
    this.forEachChild(function (a) {
        a.dispose()
    });
    !this.wasDecorated_ && this.element_ && goog.dom.removeNode(this.element_);
    this.parent_ = this.model_ = this.element_ = this.childIndex_ = this.children_ = null;
    goog.ui.Component.superClass_.disposeInternal.call(this)
};
goog.ui.Component.prototype.makeId = function (a) {
    return this.getId() + "." + a
};
goog.ui.Component.prototype.makeIds = function (a) {
    var b = {}, c;
    for (c in a)b[c] = this.makeId(a[c]);
    return b
};
goog.ui.Component.prototype.getModel = function () {
    return this.model_
};
goog.ui.Component.prototype.setModel = function (a) {
    this.model_ = a
};
goog.ui.Component.prototype.getFragmentFromId = function (a) {
    return a.substring(this.getId().length + 1)
};
goog.ui.Component.prototype.getElementByFragment = function (a) {
    if (!this.inDocument_)throw Error(goog.ui.Component.Error.NOT_IN_DOCUMENT);
    return this.dom_.getElement(this.makeId(a))
};
goog.ui.Component.prototype.addChild = function (a, b) {
    this.addChildAt(a, this.getChildCount(), b)
};
goog.ui.Component.prototype.addChildAt = function (a, b, c) {
    if (a.inDocument_ && (c || !this.inDocument_))throw Error(goog.ui.Component.Error.ALREADY_RENDERED);
    if (0 > b || b > this.getChildCount())throw Error(goog.ui.Component.Error.CHILD_INDEX_OUT_OF_BOUNDS);
    this.childIndex_ && this.children_ || (this.childIndex_ = {}, this.children_ = []);
    a.getParent() == this ? (goog.object.set(this.childIndex_, a.getId(), a), goog.array.remove(this.children_, a)) : goog.object.add(this.childIndex_, a.getId(), a);
    a.setParent(this);
    goog.array.insertAt(this.children_,
        a, b);
    a.inDocument_ && this.inDocument_ && a.getParent() == this ? (c = this.getContentElement(), c.insertBefore(a.getElement(), c.childNodes[b] || null)) : c ? (this.element_ || this.createDom(), b = this.getChildAt(b + 1), a.render_(this.getContentElement(), b ? b.element_ : null)) : this.inDocument_ && (!a.inDocument_ && a.element_ && a.element_.parentNode && a.element_.parentNode.nodeType == goog.dom.NodeType.ELEMENT) && a.enterDocument()
};
goog.ui.Component.prototype.getContentElement = function () {
    return this.element_
};
goog.ui.Component.prototype.isRightToLeft = function () {
    null == this.rightToLeft_ && (this.rightToLeft_ = goog.style.isRightToLeft(this.inDocument_ ? this.element_ : this.dom_.getDocument().body));
    return this.rightToLeft_
};
goog.ui.Component.prototype.setRightToLeft = function (a) {
    if (this.inDocument_)throw Error(goog.ui.Component.Error.ALREADY_RENDERED);
    this.rightToLeft_ = a
};
goog.ui.Component.prototype.hasChildren = function () {
    return!!this.children_ && 0 != this.children_.length
};
goog.ui.Component.prototype.getChildCount = function () {
    return this.children_ ? this.children_.length : 0
};
goog.ui.Component.prototype.getChildIds = function () {
    var a = [];
    this.forEachChild(function (b) {
        a.push(b.getId())
    });
    return a
};
goog.ui.Component.prototype.getChild = function (a) {
    return this.childIndex_ && a ? goog.object.get(this.childIndex_, a) || null : null
};
goog.ui.Component.prototype.getChildAt = function (a) {
    return this.children_ ? this.children_[a] || null : null
};
goog.ui.Component.prototype.forEachChild = function (a, b) {
    this.children_ && goog.array.forEach(this.children_, a, b)
};
goog.ui.Component.prototype.indexOfChild = function (a) {
    return this.children_ && a ? goog.array.indexOf(this.children_, a) : -1
};
goog.ui.Component.prototype.removeChild = function (a, b) {
    if (a) {
        var c = goog.isString(a) ? a : a.getId();
        a = this.getChild(c);
        c && a && (goog.object.remove(this.childIndex_, c), goog.array.remove(this.children_, a), b && (a.exitDocument(), a.element_ && goog.dom.removeNode(a.element_)), a.setParent(null))
    }
    if (!a)throw Error(goog.ui.Component.Error.NOT_OUR_CHILD);
    return a
};
goog.ui.Component.prototype.removeChildAt = function (a, b) {
    return this.removeChild(this.getChildAt(a), b)
};
goog.ui.Component.prototype.removeChildren = function (a) {
    for (var b = []; this.hasChildren();)b.push(this.removeChildAt(0, a));
    return b
};
goog.a11y = {};
goog.a11y.aria = {};
goog.a11y.aria.State = {ACTIVEDESCENDANT: "activedescendant", ATOMIC: "atomic", AUTOCOMPLETE: "autocomplete", BUSY: "busy", CHECKED: "checked", CONTROLS: "controls", DESCRIBEDBY: "describedby", DISABLED: "disabled", DROPEFFECT: "dropeffect", EXPANDED: "expanded", FLOWTO: "flowto", GRABBED: "grabbed", HASPOPUP: "haspopup", HIDDEN: "hidden", INVALID: "invalid", LABEL: "label", LABELLEDBY: "labelledby", LEVEL: "level", LIVE: "live", MULTILINE: "multiline", MULTISELECTABLE: "multiselectable", ORIENTATION: "orientation", OWNS: "owns", POSINSET: "posinset",
    PRESSED: "pressed", READONLY: "readonly", RELEVANT: "relevant", REQUIRED: "required", SELECTED: "selected", SETSIZE: "setsize", SORT: "sort", VALUEMAX: "valuemax", VALUEMIN: "valuemin", VALUENOW: "valuenow", VALUETEXT: "valuetext"};
goog.a11y.aria.AutoCompleteValues = {INLINE: "inline", LIST: "list", BOTH: "both", NONE: "none"};
goog.a11y.aria.DropEffectValues = {COPY: "copy", MOVE: "move", LINK: "link", EXECUTE: "execute", POPUP: "popup", NONE: "none"};
goog.a11y.aria.LivePriority = {OFF: "off", POLITE: "polite", ASSERTIVE: "assertive"};
goog.a11y.aria.OrientationValues = {VERTICAL: "vertical", HORIZONTAL: "horizontal"};
goog.a11y.aria.RelevantValues = {ADDITIONS: "additions", REMOVALS: "removals", TEXT: "text", ALL: "all"};
goog.a11y.aria.SortValues = {ASCENDING: "ascending", DESCENDING: "descending", NONE: "none", OTHER: "other"};
goog.a11y.aria.CheckedValues = {TRUE: "true", FALSE: "false", MIXED: "mixed", UNDEFINED: "undefined"};
goog.a11y.aria.ExpandedValues = {TRUE: "true", FALSE: "false", UNDEFINED: "undefined"};
goog.a11y.aria.GrabbedValues = {TRUE: "true", FALSE: "false", UNDEFINED: "undefined"};
goog.a11y.aria.InvalidValues = {FALSE: "false", TRUE: "true", GRAMMAR: "grammar", SPELLING: "spelling"};
goog.a11y.aria.PressedValues = {TRUE: "true", FALSE: "false", MIXED: "mixed", UNDEFINED: "undefined"};
goog.a11y.aria.SelectedValues = {TRUE: "true", FALSE: "false", UNDEFINED: "undefined"};
goog.a11y.aria.datatables = {};
goog.a11y.aria.datatables.getDefaultValuesMap = function () {
    goog.a11y.aria.DefaultStateValueMap_ || (goog.a11y.aria.DefaultStateValueMap_ = goog.object.create(goog.a11y.aria.State.ATOMIC, !1, goog.a11y.aria.State.AUTOCOMPLETE, "none", goog.a11y.aria.State.DROPEFFECT, "none", goog.a11y.aria.State.HASPOPUP, !1, goog.a11y.aria.State.LIVE, "off", goog.a11y.aria.State.MULTILINE, !1, goog.a11y.aria.State.MULTISELECTABLE, !1, goog.a11y.aria.State.ORIENTATION, "vertical", goog.a11y.aria.State.READONLY, !1, goog.a11y.aria.State.RELEVANT,
        "additions text", goog.a11y.aria.State.REQUIRED, !1, goog.a11y.aria.State.SORT, "none", goog.a11y.aria.State.BUSY, !1, goog.a11y.aria.State.DISABLED, !1, goog.a11y.aria.State.HIDDEN, !1, goog.a11y.aria.State.INVALID, "false"));
    return goog.a11y.aria.DefaultStateValueMap_
};
goog.a11y.aria.Role = {ALERT: "alert", ALERTDIALOG: "alertdialog", APPLICATION: "application", ARTICLE: "article", BANNER: "banner", BUTTON: "button", CHECKBOX: "checkbox", COLUMNHEADER: "columnheader", COMBOBOX: "combobox", COMPLEMENTARY: "complementary", CONTENTINFO: "contentinfo", DEFINITION: "definition", DIALOG: "dialog", DIRECTORY: "directory", DOCUMENT: "document", FORM: "form", GRID: "grid", GRIDCELL: "gridcell", GROUP: "group", HEADING: "heading", IMG: "img", LINK: "link", LIST: "list", LISTBOX: "listbox", LISTITEM: "listitem", LOG: "log",
    MAIN: "main", MARQUEE: "marquee", MATH: "math", MENU: "menu", MENUBAR: "menubar", MENU_ITEM: "menuitem", MENU_ITEM_CHECKBOX: "menuitemcheckbox", MENU_ITEM_RADIO: "menuitemradio", NAVIGATION: "navigation", NOTE: "note", OPTION: "option", PRESENTATION: "presentation", PROGRESSBAR: "progressbar", RADIO: "radio", RADIOGROUP: "radiogroup", REGION: "region", ROW: "row", ROWGROUP: "rowgroup", ROWHEADER: "rowheader", SCROLLBAR: "scrollbar", SEARCH: "search", SEPARATOR: "separator", SLIDER: "slider", SPINBUTTON: "spinbutton", STATUS: "status", TAB: "tab",
    TAB_LIST: "tablist", TAB_PANEL: "tabpanel", TEXTBOX: "textbox", TIMER: "timer", TOOLBAR: "toolbar", TOOLTIP: "tooltip", TREE: "tree", TREEGRID: "treegrid", TREEITEM: "treeitem"};
goog.a11y.aria.ARIA_PREFIX_ = "aria-";
goog.a11y.aria.ROLE_ATTRIBUTE_ = "role";
goog.a11y.aria.TAGS_WITH_ASSUMED_ROLES_ = [goog.dom.TagName.A, goog.dom.TagName.AREA, goog.dom.TagName.BUTTON, goog.dom.TagName.HEAD, goog.dom.TagName.INPUT, goog.dom.TagName.LINK, goog.dom.TagName.MENU, goog.dom.TagName.META, goog.dom.TagName.OPTGROUP, goog.dom.TagName.OPTION, goog.dom.TagName.PROGRESS, goog.dom.TagName.STYLE, goog.dom.TagName.SELECT, goog.dom.TagName.SOURCE, goog.dom.TagName.TEXTAREA, goog.dom.TagName.TITLE, goog.dom.TagName.TRACK];
goog.a11y.aria.setRole = function (a, b) {
    b ? (goog.asserts.ENABLE_ASSERTS && goog.asserts.assert(goog.object.containsValue(goog.a11y.aria.Role, b), "No such ARIA role " + b), a.setAttribute(goog.a11y.aria.ROLE_ATTRIBUTE_, b)) : goog.a11y.aria.removeRole(a)
};
goog.a11y.aria.getRole = function (a) {
    return a.getAttribute(goog.a11y.aria.ROLE_ATTRIBUTE_) || null
};
goog.a11y.aria.removeRole = function (a) {
    a.removeAttribute(goog.a11y.aria.ROLE_ATTRIBUTE_)
};
goog.a11y.aria.setState = function (a, b, c) {
    goog.isArrayLike(c) && (c = c.join(" "));
    var d = goog.a11y.aria.getAriaAttributeName_(b);
    "" === c || void 0 == c ? (c = goog.a11y.aria.datatables.getDefaultValuesMap(), b in c ? a.setAttribute(d, c[b]) : a.removeAttribute(d)) : a.setAttribute(d, c)
};
goog.a11y.aria.removeState = function (a, b) {
    a.removeAttribute(goog.a11y.aria.getAriaAttributeName_(b))
};
goog.a11y.aria.getState = function (a, b) {
    var c = a.getAttribute(goog.a11y.aria.getAriaAttributeName_(b));
    return null == c || void 0 == c ? "" : String(c)
};
goog.a11y.aria.getActiveDescendant = function (a) {
    var b = goog.a11y.aria.getState(a, goog.a11y.aria.State.ACTIVEDESCENDANT);
    return goog.dom.getOwnerDocument(a).getElementById(b)
};
goog.a11y.aria.setActiveDescendant = function (a, b) {
    var c = "";
    b && (c = b.id, goog.asserts.assert(c, "The active element should have an id."));
    goog.a11y.aria.setState(a, goog.a11y.aria.State.ACTIVEDESCENDANT, c)
};
goog.a11y.aria.getLabel = function (a) {
    return goog.a11y.aria.getState(a, goog.a11y.aria.State.LABEL)
};
goog.a11y.aria.setLabel = function (a, b) {
    goog.a11y.aria.setState(a, goog.a11y.aria.State.LABEL, b)
};
goog.a11y.aria.assertRoleIsSetInternalUtil = function (a, b) {
    if (!goog.array.contains(goog.a11y.aria.TAGS_WITH_ASSUMED_ROLES_, a.tagName)) {
        var c = goog.a11y.aria.getRole(a);
        goog.asserts.assert(null != c, "The element ARIA role cannot be null.");
        goog.asserts.assert(goog.array.contains(b, c), 'Non existing or incorrect role set for element.The role set is "' + c + '". The role should be any of "' + b + '". Check the ARIA specification for more details http://www.w3.org/TR/wai-aria/roles.')
    }
};
goog.a11y.aria.getBooleanStateInternalUtil = function (a, b) {
    var c = goog.a11y.aria.getState(a, b);
    return"true" == c ? !0 : "false" == c ? !1 : null
};
goog.a11y.aria.getNumberStateInternalUtil = function (a, b) {
    var c = goog.a11y.aria.getState(a, b);
    return goog.string.isNumeric(c) ? goog.string.toNumber(c) : null
};
goog.a11y.aria.getStringArrayStateInternalUtil = function (a, b) {
    var c = a.getAttribute(goog.a11y.aria.getAriaAttributeName_(b));
    return goog.a11y.aria.splitStringOnWhitespace_(c)
};
goog.a11y.aria.getStringStateInternalUtil = function (a, b) {
    return goog.a11y.aria.getState(a, b) || null
};
goog.a11y.aria.splitStringOnWhitespace_ = function (a) {
    return a ? a.split(/\s+/) : []
};
goog.a11y.aria.getAriaAttributeName_ = function (a) {
    goog.asserts.ENABLE_ASSERTS && (goog.asserts.assert(a, "ARIA attribute cannot be empty."), goog.asserts.assert(goog.object.containsValue(goog.a11y.aria.State, a), "No such ARIA attribute " + a));
    return goog.a11y.aria.ARIA_PREFIX_ + a
};
goog.events.FocusHandler = function (a) {
    goog.events.EventTarget.call(this);
    this.element_ = a;
    a = goog.userAgent.IE ? "focusout" : "blur";
    this.listenKeyIn_ = goog.events.listen(this.element_, goog.userAgent.IE ? "focusin" : "focus", this, !goog.userAgent.IE);
    this.listenKeyOut_ = goog.events.listen(this.element_, a, this, !goog.userAgent.IE)
};
goog.inherits(goog.events.FocusHandler, goog.events.EventTarget);
goog.events.FocusHandler.EventType = {FOCUSIN: "focusin", FOCUSOUT: "focusout"};
goog.events.FocusHandler.prototype.handleEvent = function (a) {
    var b = a.getBrowserEvent(), b = new goog.events.BrowserEvent(b);
    b.type = "focusin" == a.type || "focus" == a.type ? goog.events.FocusHandler.EventType.FOCUSIN : goog.events.FocusHandler.EventType.FOCUSOUT;
    this.dispatchEvent(b)
};
goog.events.FocusHandler.prototype.disposeInternal = function () {
    goog.events.FocusHandler.superClass_.disposeInternal.call(this);
    goog.events.unlistenByKey(this.listenKeyIn_);
    goog.events.unlistenByKey(this.listenKeyOut_);
    delete this.element_
};
goog.dom.iframe = {};
goog.dom.iframe.BLANK_SOURCE = 'javascript:""';
goog.dom.iframe.BLANK_SOURCE_NEW_FRAME = goog.userAgent.IE ? 'javascript:""' : "javascript:undefined";
goog.dom.iframe.STYLES_ = "border:0;vertical-align:bottom;";
goog.dom.iframe.createBlank = function (a, b) {
    return a.createDom("iframe", {frameborder: 0, style: goog.dom.iframe.STYLES_ + (b || ""), src: goog.dom.iframe.BLANK_SOURCE})
};
goog.dom.iframe.writeContent = function (a, b) {
    var c = goog.dom.getFrameContentDocument(a);
    c.open();
    c.write(b);
    c.close()
};
goog.dom.iframe.createWithContent = function (a, b, c, d, e) {
    var f = goog.dom.getDomHelper(a), g = [];
    e || g.push("<!DOCTYPE html>");
    g.push("<html><head>", b, "</head><body>", c, "</body></html>");
    b = goog.dom.iframe.createBlank(f, d);
    a.appendChild(b);
    goog.dom.iframe.writeContent(b, g.join(""));
    return b
};
goog.ui.ModalPopup = function (a, b) {
    goog.ui.Component.call(this, b);
    this.useIframeMask_ = !!a;
    this.lastFocus_ = null
};
goog.inherits(goog.ui.ModalPopup, goog.ui.Component);
goog.ui.ModalPopup.prototype.focusHandler_ = null;
goog.ui.ModalPopup.prototype.visible_ = !1;
goog.ui.ModalPopup.prototype.bgEl_ = null;
goog.ui.ModalPopup.prototype.bgIframeEl_ = null;
goog.ui.ModalPopup.prototype.tabCatcherElement_ = null;
goog.ui.ModalPopup.prototype.backwardTabWrapInProgress_ = !1;
goog.ui.ModalPopup.prototype.getCssClass = function () {
    return"goog-modalpopup"
};
goog.ui.ModalPopup.prototype.getBackgroundIframe = function () {
    return this.bgIframeEl_
};
goog.ui.ModalPopup.prototype.getBackgroundElement = function () {
    return this.bgEl_
};
goog.ui.ModalPopup.prototype.createDom = function () {
    goog.ui.ModalPopup.superClass_.createDom.call(this);
    var a = this.getElement();
    goog.dom.classes.add(a, this.getCssClass());
    goog.dom.setFocusableTabIndex(a, !0);
    goog.style.setElementShown(a, !1);
    this.manageBackgroundDom_();
    this.createTabCatcher_()
};
goog.ui.ModalPopup.prototype.manageBackgroundDom_ = function () {
    this.useIframeMask_ && !this.bgIframeEl_ && (this.bgIframeEl_ = goog.dom.iframe.createBlank(this.getDomHelper()), this.bgIframeEl_.className = this.getCssClass() + "-bg", goog.style.setElementShown(this.bgIframeEl_, !1), goog.style.setOpacity(this.bgIframeEl_, 0));
    this.bgEl_ || (this.bgEl_ = this.getDomHelper().createDom("div", this.getCssClass() + "-bg"), goog.style.setElementShown(this.bgEl_, !1))
};
goog.ui.ModalPopup.prototype.createTabCatcher_ = function () {
    this.tabCatcherElement_ || (this.tabCatcherElement_ = this.getDomHelper().createElement("span"), goog.style.setElementShown(this.tabCatcherElement_, !1), goog.dom.setFocusableTabIndex(this.tabCatcherElement_, !0), this.tabCatcherElement_.style.position = "absolute")
};
goog.ui.ModalPopup.prototype.setupBackwardTabWrap = function () {
    this.backwardTabWrapInProgress_ = !0;
    try {
        this.tabCatcherElement_.focus()
    } catch (a) {
    }
    goog.Timer.callOnce(this.resetBackwardTabWrap_, 0, this)
};
goog.ui.ModalPopup.prototype.resetBackwardTabWrap_ = function () {
    this.backwardTabWrapInProgress_ = !1
};
goog.ui.ModalPopup.prototype.renderBackground_ = function () {
    goog.asserts.assert(!!this.bgEl_, "Background element must not be null.");
    this.bgIframeEl_ && goog.dom.insertSiblingBefore(this.bgIframeEl_, this.getElement());
    goog.dom.insertSiblingBefore(this.bgEl_, this.getElement())
};
goog.ui.ModalPopup.prototype.canDecorate = function (a) {
    return!!a && a.tagName == goog.dom.TagName.DIV
};
goog.ui.ModalPopup.prototype.decorateInternal = function (a) {
    goog.ui.ModalPopup.superClass_.decorateInternal.call(this, a);
    goog.dom.classes.add(this.getElement(), this.getCssClass());
    this.manageBackgroundDom_();
    this.createTabCatcher_();
    goog.style.setElementShown(this.getElement(), !1)
};
goog.ui.ModalPopup.prototype.enterDocument = function () {
    this.renderBackground_();
    goog.ui.ModalPopup.superClass_.enterDocument.call(this);
    goog.dom.insertSiblingAfter(this.tabCatcherElement_, this.getElement());
    this.focusHandler_ = new goog.events.FocusHandler(this.getDomHelper().getDocument());
    this.getHandler().listen(this.focusHandler_, goog.events.FocusHandler.EventType.FOCUSIN, this.onFocus_);
    this.setA11YDetectBackground_(!1)
};
goog.ui.ModalPopup.prototype.exitDocument = function () {
    this.isVisible() && this.setVisible(!1);
    goog.dispose(this.focusHandler_);
    goog.ui.ModalPopup.superClass_.exitDocument.call(this);
    goog.dom.removeNode(this.bgIframeEl_);
    goog.dom.removeNode(this.bgEl_);
    goog.dom.removeNode(this.tabCatcherElement_)
};
goog.ui.ModalPopup.prototype.setVisible = function (a) {
    goog.asserts.assert(this.isInDocument(), "ModalPopup must be rendered first.");
    a != this.visible_ && (this.popupShowTransition_ && this.popupShowTransition_.stop(), this.bgShowTransition_ && this.bgShowTransition_.stop(), this.popupHideTransition_ && this.popupHideTransition_.stop(), this.bgHideTransition_ && this.bgHideTransition_.stop(), this.isInDocument() && this.setA11YDetectBackground_(a), a ? this.show_() : this.hide_())
};
goog.ui.ModalPopup.setAriaHidden_ = function (a, b) {
    b ? goog.a11y.aria.setState(a, goog.a11y.aria.State.HIDDEN, b) : goog.a11y.aria.removeState(a, goog.a11y.aria.State.HIDDEN)
};
goog.ui.ModalPopup.prototype.setA11YDetectBackground_ = function (a) {
    for (var b = this.getDomHelper().getDocument().body.firstChild; b; b = b.nextSibling)b.nodeType == goog.dom.NodeType.ELEMENT && goog.ui.ModalPopup.setAriaHidden_(b, a);
    goog.ui.ModalPopup.setAriaHidden_(this.getElementStrict(), !a)
};
goog.ui.ModalPopup.prototype.setTransition = function (a, b, c, d) {
    this.popupShowTransition_ = a;
    this.popupHideTransition_ = b;
    this.bgShowTransition_ = c;
    this.bgHideTransition_ = d
};
goog.ui.ModalPopup.prototype.show_ = function () {
    if (this.dispatchEvent(goog.ui.PopupBase.EventType.BEFORE_SHOW)) {
        try {
            this.lastFocus_ = this.getDomHelper().getDocument().activeElement
        } catch (a) {
        }
        this.resizeBackground_();
        this.reposition();
        this.getHandler().listen(this.getDomHelper().getWindow(), goog.events.EventType.RESIZE, this.resizeBackground_);
        this.showPopupElement_(!0);
        this.focus();
        this.visible_ = !0;
        if (this.popupShowTransition_ && this.bgShowTransition_)goog.events.listenOnce(this.popupShowTransition_, goog.fx.Transition.EventType.END,
            this.onShow, !1, this), this.bgShowTransition_.play(), this.popupShowTransition_.play(); else this.onShow()
    }
};
goog.ui.ModalPopup.prototype.hide_ = function () {
    if (this.dispatchEvent(goog.ui.PopupBase.EventType.BEFORE_HIDE)) {
        this.getHandler().unlisten(this.getDomHelper().getWindow(), goog.events.EventType.RESIZE, this.resizeBackground_);
        this.visible_ = !1;
        if (this.popupHideTransition_ && this.bgHideTransition_)goog.events.listenOnce(this.popupHideTransition_, goog.fx.Transition.EventType.END, this.onHide, !1, this), this.bgHideTransition_.play(), this.popupHideTransition_.play(); else this.onHide();
        try {
            var a = this.getDomHelper().getDocument().body,
                b = this.getDomHelper().getDocument().activeElement || a;
            this.lastFocus_ && (b == a && this.lastFocus_ != a) && this.lastFocus_.focus()
        } catch (c) {
        }
        this.lastFocus_ = null
    }
};
goog.ui.ModalPopup.prototype.showPopupElement_ = function (a) {
    this.bgIframeEl_ && goog.style.setElementShown(this.bgIframeEl_, a);
    this.bgEl_ && goog.style.setElementShown(this.bgEl_, a);
    goog.style.setElementShown(this.getElement(), a);
    goog.style.setElementShown(this.tabCatcherElement_, a)
};
goog.ui.ModalPopup.prototype.onShow = function () {
    this.dispatchEvent(goog.ui.PopupBase.EventType.SHOW)
};
goog.ui.ModalPopup.prototype.onHide = function () {
    this.showPopupElement_(!1);
    this.dispatchEvent(goog.ui.PopupBase.EventType.HIDE)
};
goog.ui.ModalPopup.prototype.isVisible = function () {
    return this.visible_
};
goog.ui.ModalPopup.prototype.focus = function () {
    this.focusElement_()
};
goog.ui.ModalPopup.prototype.resizeBackground_ = function () {
    this.bgIframeEl_ && goog.style.setElementShown(this.bgIframeEl_, !1);
    this.bgEl_ && goog.style.setElementShown(this.bgEl_, !1);
    var a = this.getDomHelper().getDocument(), b = goog.dom.getWindow(a) || window, c = goog.dom.getViewportSize(b), b = Math.max(c.width, Math.max(a.body.scrollWidth, a.documentElement.scrollWidth)), a = Math.max(c.height, Math.max(a.body.scrollHeight, a.documentElement.scrollHeight));
    this.bgIframeEl_ && (goog.style.setElementShown(this.bgIframeEl_,
        !0), goog.style.setSize(this.bgIframeEl_, b, a));
    this.bgEl_ && (goog.style.setElementShown(this.bgEl_, !0), goog.style.setSize(this.bgEl_, b, a))
};
goog.ui.ModalPopup.prototype.reposition = function () {
    var a = this.getDomHelper().getDocument(), b = goog.dom.getWindow(a) || window;
    if ("fixed" == goog.style.getComputedPosition(this.getElement()))var c = a = 0; else c = this.getDomHelper().getDocumentScroll(), a = c.x, c = c.y;
    var d = goog.style.getSize(this.getElement()), b = goog.dom.getViewportSize(b), a = Math.max(a + b.width / 2 - d.width / 2, 0), c = Math.max(c + b.height / 2 - d.height / 2, 0);
    goog.style.setPosition(this.getElement(), a, c);
    goog.style.setPosition(this.tabCatcherElement_, a, c)
};
goog.ui.ModalPopup.prototype.onFocus_ = function (a) {
    this.backwardTabWrapInProgress_ ? this.resetBackwardTabWrap_() : a.target == this.tabCatcherElement_ && goog.Timer.callOnce(this.focusElement_, 0, this)
};
goog.ui.ModalPopup.prototype.focusElement_ = function () {
    try {
        goog.userAgent.IE && this.getDomHelper().getDocument().body.focus(), this.getElement().focus()
    } catch (a) {
    }
};
goog.ui.ModalPopup.prototype.disposeInternal = function () {
    goog.dispose(this.popupShowTransition_);
    this.popupShowTransition_ = null;
    goog.dispose(this.popupHideTransition_);
    this.popupHideTransition_ = null;
    goog.dispose(this.bgShowTransition_);
    this.bgShowTransition_ = null;
    goog.dispose(this.bgHideTransition_);
    this.bgHideTransition_ = null;
    goog.ui.ModalPopup.superClass_.disposeInternal.call(this)
};
goog.iter = {};
goog.iter.StopIteration = "StopIteration"in goog.global ? goog.global.StopIteration : Error("StopIteration");
goog.iter.Iterator = function () {
};
goog.iter.Iterator.prototype.next = function () {
    throw goog.iter.StopIteration;
};
goog.iter.Iterator.prototype.__iterator__ = function (a) {
    return this
};
goog.iter.toIterator = function (a) {
    if (a instanceof goog.iter.Iterator)return a;
    if ("function" == typeof a.__iterator__)return a.__iterator__(!1);
    if (goog.isArrayLike(a)) {
        var b = 0, c = new goog.iter.Iterator;
        c.next = function () {
            for (; ;) {
                if (b >= a.length)throw goog.iter.StopIteration;
                if (b in a)return a[b++];
                b++
            }
        };
        return c
    }
    throw Error("Not implemented");
};
goog.iter.forEach = function (a, b, c) {
    if (goog.isArrayLike(a))try {
        goog.array.forEach(a, b, c)
    } catch (d) {
        if (d !== goog.iter.StopIteration)throw d;
    } else {
        a = goog.iter.toIterator(a);
        try {
            for (; ;)b.call(c, a.next(), void 0, a)
        } catch (e) {
            if (e !== goog.iter.StopIteration)throw e;
        }
    }
};
goog.iter.filter = function (a, b, c) {
    var d = goog.iter.toIterator(a);
    a = new goog.iter.Iterator;
    a.next = function () {
        for (; ;) {
            var a = d.next();
            if (b.call(c, a, void 0, d))return a
        }
    };
    return a
};
goog.iter.range = function (a, b, c) {
    var d = 0, e = a, f = c || 1;
    1 < arguments.length && (d = a, e = b);
    if (0 == f)throw Error("Range step argument must not be zero");
    var g = new goog.iter.Iterator;
    g.next = function () {
        if (0 < f && d >= e || 0 > f && d <= e)throw goog.iter.StopIteration;
        var a = d;
        d += f;
        return a
    };
    return g
};
goog.iter.join = function (a, b) {
    return goog.iter.toArray(a).join(b)
};
goog.iter.map = function (a, b, c) {
    var d = goog.iter.toIterator(a);
    a = new goog.iter.Iterator;
    a.next = function () {
        for (; ;) {
            var a = d.next();
            return b.call(c, a, void 0, d)
        }
    };
    return a
};
goog.iter.reduce = function (a, b, c, d) {
    var e = c;
    goog.iter.forEach(a, function (a) {
        e = b.call(d, e, a)
    });
    return e
};
goog.iter.some = function (a, b, c) {
    a = goog.iter.toIterator(a);
    try {
        for (; ;)if (b.call(c, a.next(), void 0, a))return!0
    } catch (d) {
        if (d !== goog.iter.StopIteration)throw d;
    }
    return!1
};
goog.iter.every = function (a, b, c) {
    a = goog.iter.toIterator(a);
    try {
        for (; ;)if (!b.call(c, a.next(), void 0, a))return!1
    } catch (d) {
        if (d !== goog.iter.StopIteration)throw d;
    }
    return!0
};
goog.iter.chain = function (a) {
    var b = arguments, c = b.length, d = 0, e = new goog.iter.Iterator;
    e.next = function () {
        try {
            if (d >= c)throw goog.iter.StopIteration;
            return goog.iter.toIterator(b[d]).next()
        } catch (a) {
            if (a !== goog.iter.StopIteration || d >= c)throw a;
            d++;
            return this.next()
        }
    };
    return e
};
goog.iter.dropWhile = function (a, b, c) {
    var d = goog.iter.toIterator(a);
    a = new goog.iter.Iterator;
    var e = !0;
    a.next = function () {
        for (; ;) {
            var a = d.next();
            if (!e || !b.call(c, a, void 0, d))return e = !1, a
        }
    };
    return a
};
goog.iter.takeWhile = function (a, b, c) {
    var d = goog.iter.toIterator(a);
    a = new goog.iter.Iterator;
    var e = !0;
    a.next = function () {
        for (; ;)if (e) {
            var a = d.next();
            if (b.call(c, a, void 0, d))return a;
            e = !1
        } else throw goog.iter.StopIteration;
    };
    return a
};
goog.iter.toArray = function (a) {
    if (goog.isArrayLike(a))return goog.array.toArray(a);
    a = goog.iter.toIterator(a);
    var b = [];
    goog.iter.forEach(a, function (a) {
        b.push(a)
    });
    return b
};
goog.iter.equals = function (a, b) {
    a = goog.iter.toIterator(a);
    b = goog.iter.toIterator(b);
    var c, d;
    try {
        for (; ;) {
            c = d = !1;
            var e = a.next();
            c = !0;
            var f = b.next();
            d = !0;
            if (e != f)break
        }
    } catch (g) {
        if (g !== goog.iter.StopIteration)throw g;
        if (c && !d)return!1;
        if (!d)try {
            b.next()
        } catch (h) {
            if (h !== goog.iter.StopIteration)throw h;
            return!0
        }
    }
    return!1
};
goog.iter.nextOrValue = function (a, b) {
    try {
        return goog.iter.toIterator(a).next()
    } catch (c) {
        if (c != goog.iter.StopIteration)throw c;
        return b
    }
};
goog.iter.product = function (a) {
    if (goog.array.some(arguments, function (a) {
        return!a.length
    }) || !arguments.length)return new goog.iter.Iterator;
    var b = new goog.iter.Iterator, c = arguments, d = goog.array.repeat(0, c.length);
    b.next = function () {
        if (d) {
            for (var a = goog.array.map(d, function (a, b) {
                return c[b][a]
            }), b = d.length - 1; 0 <= b; b--) {
                goog.asserts.assert(d);
                if (d[b] < c[b].length - 1) {
                    d[b]++;
                    break
                }
                if (0 == b) {
                    d = null;
                    break
                }
                d[b] = 0
            }
            return a
        }
        throw goog.iter.StopIteration;
    };
    return b
};
goog.iter.cycle = function (a) {
    var b = goog.iter.toIterator(a), c = [], d = 0;
    a = new goog.iter.Iterator;
    var e = !1;
    a.next = function () {
        var a = null;
        if (!e)try {
            return a = b.next(), c.push(a), a
        } catch (g) {
            if (g != goog.iter.StopIteration || goog.array.isEmpty(c))throw g;
            e = !0
        }
        a = c[d];
        d = (d + 1) % c.length;
        return a
    };
    return a
};
goog.structs = {};
goog.structs.Map = function (a, b) {
    this.map_ = {};
    this.keys_ = [];
    var c = arguments.length;
    if (1 < c) {
        if (c % 2)throw Error("Uneven number of arguments");
        for (var d = 0; d < c; d += 2)this.set(arguments[d], arguments[d + 1])
    } else a && this.addAll(a)
};
goog.structs.Map.prototype.count_ = 0;
goog.structs.Map.prototype.version_ = 0;
goog.structs.Map.prototype.getCount = function () {
    return this.count_
};
goog.structs.Map.prototype.getValues = function () {
    this.cleanupKeysArray_();
    for (var a = [], b = 0; b < this.keys_.length; b++)a.push(this.map_[this.keys_[b]]);
    return a
};
goog.structs.Map.prototype.getKeys = function () {
    this.cleanupKeysArray_();
    return this.keys_.concat()
};
goog.structs.Map.prototype.containsKey = function (a) {
    return goog.structs.Map.hasKey_(this.map_, a)
};
goog.structs.Map.prototype.containsValue = function (a) {
    for (var b = 0; b < this.keys_.length; b++) {
        var c = this.keys_[b];
        if (goog.structs.Map.hasKey_(this.map_, c) && this.map_[c] == a)return!0
    }
    return!1
};
goog.structs.Map.prototype.equals = function (a, b) {
    if (this === a)return!0;
    if (this.count_ != a.getCount())return!1;
    var c = b || goog.structs.Map.defaultEquals;
    this.cleanupKeysArray_();
    for (var d, e = 0; d = this.keys_[e]; e++)if (!c(this.get(d), a.get(d)))return!1;
    return!0
};
goog.structs.Map.defaultEquals = function (a, b) {
    return a === b
};
goog.structs.Map.prototype.isEmpty = function () {
    return 0 == this.count_
};
goog.structs.Map.prototype.clear = function () {
    this.map_ = {};
    this.version_ = this.count_ = this.keys_.length = 0
};
goog.structs.Map.prototype.remove = function (a) {
    return goog.structs.Map.hasKey_(this.map_, a) ? (delete this.map_[a], this.count_--, this.version_++, this.keys_.length > 2 * this.count_ && this.cleanupKeysArray_(), !0) : !1
};
goog.structs.Map.prototype.cleanupKeysArray_ = function () {
    if (this.count_ != this.keys_.length) {
        for (var a = 0, b = 0; a < this.keys_.length;) {
            var c = this.keys_[a];
            goog.structs.Map.hasKey_(this.map_, c) && (this.keys_[b++] = c);
            a++
        }
        this.keys_.length = b
    }
    if (this.count_ != this.keys_.length) {
        for (var d = {}, b = a = 0; a < this.keys_.length;)c = this.keys_[a], goog.structs.Map.hasKey_(d, c) || (this.keys_[b++] = c, d[c] = 1), a++;
        this.keys_.length = b
    }
};
goog.structs.Map.prototype.get = function (a, b) {
    return goog.structs.Map.hasKey_(this.map_, a) ? this.map_[a] : b
};
goog.structs.Map.prototype.set = function (a, b) {
    goog.structs.Map.hasKey_(this.map_, a) || (this.count_++, this.keys_.push(a), this.version_++);
    this.map_[a] = b
};
goog.structs.Map.prototype.addAll = function (a) {
    var b;
    a instanceof goog.structs.Map ? (b = a.getKeys(), a = a.getValues()) : (b = goog.object.getKeys(a), a = goog.object.getValues(a));
    for (var c = 0; c < b.length; c++)this.set(b[c], a[c])
};
goog.structs.Map.prototype.clone = function () {
    return new goog.structs.Map(this)
};
goog.structs.Map.prototype.transpose = function () {
    for (var a = new goog.structs.Map, b = 0; b < this.keys_.length; b++) {
        var c = this.keys_[b];
        a.set(this.map_[c], c)
    }
    return a
};
goog.structs.Map.prototype.toObject = function () {
    this.cleanupKeysArray_();
    for (var a = {}, b = 0; b < this.keys_.length; b++) {
        var c = this.keys_[b];
        a[c] = this.map_[c]
    }
    return a
};
goog.structs.Map.prototype.getKeyIterator = function () {
    return this.__iterator__(!0)
};
goog.structs.Map.prototype.getValueIterator = function () {
    return this.__iterator__(!1)
};
goog.structs.Map.prototype.__iterator__ = function (a) {
    this.cleanupKeysArray_();
    var b = 0, c = this.keys_, d = this.map_, e = this.version_, f = this, g = new goog.iter.Iterator;
    g.next = function () {
        for (; ;) {
            if (e != f.version_)throw Error("The map has changed since the iterator was created");
            if (b >= c.length)throw goog.iter.StopIteration;
            var g = c[b++];
            return a ? g : d[g]
        }
    };
    return g
};
goog.structs.Map.hasKey_ = function (a, b) {
    return Object.prototype.hasOwnProperty.call(a, b)
};
goog.structs.getCount = function (a) {
    return"function" == typeof a.getCount ? a.getCount() : goog.isArrayLike(a) || goog.isString(a) ? a.length : goog.object.getCount(a)
};
goog.structs.getValues = function (a) {
    if ("function" == typeof a.getValues)return a.getValues();
    if (goog.isString(a))return a.split("");
    if (goog.isArrayLike(a)) {
        for (var b = [], c = a.length, d = 0; d < c; d++)b.push(a[d]);
        return b
    }
    return goog.object.getValues(a)
};
goog.structs.getKeys = function (a) {
    if ("function" == typeof a.getKeys)return a.getKeys();
    if ("function" != typeof a.getValues) {
        if (goog.isArrayLike(a) || goog.isString(a)) {
            var b = [];
            a = a.length;
            for (var c = 0; c < a; c++)b.push(c);
            return b
        }
        return goog.object.getKeys(a)
    }
};
goog.structs.contains = function (a, b) {
    return"function" == typeof a.contains ? a.contains(b) : "function" == typeof a.containsValue ? a.containsValue(b) : goog.isArrayLike(a) || goog.isString(a) ? goog.array.contains(a, b) : goog.object.containsValue(a, b)
};
goog.structs.isEmpty = function (a) {
    return"function" == typeof a.isEmpty ? a.isEmpty() : goog.isArrayLike(a) || goog.isString(a) ? goog.array.isEmpty(a) : goog.object.isEmpty(a)
};
goog.structs.clear = function (a) {
    "function" == typeof a.clear ? a.clear() : goog.isArrayLike(a) ? goog.array.clear(a) : goog.object.clear(a)
};
goog.structs.forEach = function (a, b, c) {
    if ("function" == typeof a.forEach)a.forEach(b, c); else if (goog.isArrayLike(a) || goog.isString(a))goog.array.forEach(a, b, c); else for (var d = goog.structs.getKeys(a), e = goog.structs.getValues(a), f = e.length, g = 0; g < f; g++)b.call(c, e[g], d && d[g], a)
};
goog.structs.filter = function (a, b, c) {
    if ("function" == typeof a.filter)return a.filter(b, c);
    if (goog.isArrayLike(a) || goog.isString(a))return goog.array.filter(a, b, c);
    var d, e = goog.structs.getKeys(a), f = goog.structs.getValues(a), g = f.length;
    if (e) {
        d = {};
        for (var h = 0; h < g; h++)b.call(c, f[h], e[h], a) && (d[e[h]] = f[h])
    } else for (d = [], h = 0; h < g; h++)b.call(c, f[h], void 0, a) && d.push(f[h]);
    return d
};
goog.structs.map = function (a, b, c) {
    if ("function" == typeof a.map)return a.map(b, c);
    if (goog.isArrayLike(a) || goog.isString(a))return goog.array.map(a, b, c);
    var d, e = goog.structs.getKeys(a), f = goog.structs.getValues(a), g = f.length;
    if (e) {
        d = {};
        for (var h = 0; h < g; h++)d[e[h]] = b.call(c, f[h], e[h], a)
    } else for (d = [], h = 0; h < g; h++)d[h] = b.call(c, f[h], void 0, a);
    return d
};
goog.structs.some = function (a, b, c) {
    if ("function" == typeof a.some)return a.some(b, c);
    if (goog.isArrayLike(a) || goog.isString(a))return goog.array.some(a, b, c);
    for (var d = goog.structs.getKeys(a), e = goog.structs.getValues(a), f = e.length, g = 0; g < f; g++)if (b.call(c, e[g], d && d[g], a))return!0;
    return!1
};
goog.structs.every = function (a, b, c) {
    if ("function" == typeof a.every)return a.every(b, c);
    if (goog.isArrayLike(a) || goog.isString(a))return goog.array.every(a, b, c);
    for (var d = goog.structs.getKeys(a), e = goog.structs.getValues(a), f = e.length, g = 0; g < f; g++)if (!b.call(c, e[g], d && d[g], a))return!1;
    return!0
};
goog.ui.Dialog = function (a, b, c) {
    goog.ui.ModalPopup.call(this, b, c);
    this.class_ = a || "modal-dialog";
    this.buttons_ = goog.ui.Dialog.ButtonSet.createOkCancel()
};
goog.inherits(goog.ui.Dialog, goog.ui.ModalPopup);
goog.ui.Dialog.prototype.escapeToCancel_ = !0;
goog.ui.Dialog.prototype.hasTitleCloseButton_ = !0;
goog.ui.Dialog.prototype.modal_ = !0;
goog.ui.Dialog.prototype.draggable_ = !0;
goog.ui.Dialog.prototype.backgroundElementOpacity_ = 0.5;
goog.ui.Dialog.prototype.title_ = "";
goog.ui.Dialog.prototype.content_ = "";
goog.ui.Dialog.prototype.dragger_ = null;
goog.ui.Dialog.prototype.disposeOnHide_ = !1;
goog.ui.Dialog.prototype.titleEl_ = null;
goog.ui.Dialog.prototype.titleTextEl_ = null;
goog.ui.Dialog.prototype.titleId_ = null;
goog.ui.Dialog.prototype.titleCloseEl_ = null;
goog.ui.Dialog.prototype.contentEl_ = null;
goog.ui.Dialog.prototype.buttonEl_ = null;
goog.ui.Dialog.prototype.preferredAriaRole_ = goog.a11y.aria.Role.DIALOG;
goog.ui.Dialog.prototype.getCssClass = function () {
    return this.class_
};
goog.ui.Dialog.prototype.setTitle = function (a) {
    this.title_ = a;
    this.titleTextEl_ && goog.dom.setTextContent(this.titleTextEl_, a)
};
goog.ui.Dialog.prototype.getTitle = function () {
    return this.title_
};
goog.ui.Dialog.prototype.setContent = function (a) {
    this.content_ = a;
    this.contentEl_ && (this.contentEl_.innerHTML = a)
};
goog.ui.Dialog.prototype.getContent = function () {
    return this.content_
};
goog.ui.Dialog.prototype.getPreferredAriaRole = function () {
    return this.preferredAriaRole_
};
goog.ui.Dialog.prototype.setPreferredAriaRole = function (a) {
    this.preferredAriaRole_ = a
};
goog.ui.Dialog.prototype.renderIfNoDom_ = function () {
    this.getElement() || this.render()
};
goog.ui.Dialog.prototype.getContentElement = function () {
    this.renderIfNoDom_();
    return this.contentEl_
};
goog.ui.Dialog.prototype.getTitleElement = function () {
    this.renderIfNoDom_();
    return this.titleEl_
};
goog.ui.Dialog.prototype.getTitleTextElement = function () {
    this.renderIfNoDom_();
    return this.titleTextEl_
};
goog.ui.Dialog.prototype.getTitleCloseElement = function () {
    this.renderIfNoDom_();
    return this.titleCloseEl_
};
goog.ui.Dialog.prototype.getButtonElement = function () {
    this.renderIfNoDom_();
    return this.buttonEl_
};
goog.ui.Dialog.prototype.getDialogElement = function () {
    this.renderIfNoDom_();
    return this.getElement()
};
goog.ui.Dialog.prototype.getBackgroundElement = function () {
    this.renderIfNoDom_();
    return goog.ui.Dialog.superClass_.getBackgroundElement.call(this)
};
goog.ui.Dialog.prototype.getBackgroundElementOpacity = function () {
    return this.backgroundElementOpacity_
};
goog.ui.Dialog.prototype.setBackgroundElementOpacity = function (a) {
    this.backgroundElementOpacity_ = a;
    this.getElement() && (a = this.getBackgroundElement()) && goog.style.setOpacity(a, this.backgroundElementOpacity_)
};
goog.ui.Dialog.prototype.setModal = function (a) {
    a != this.modal_ && this.setModalInternal_(a)
};
goog.ui.Dialog.prototype.setModalInternal_ = function (a) {
    this.modal_ = a;
    if (this.isInDocument()) {
        var b = this.getDomHelper(), c = this.getBackgroundElement(), d = this.getBackgroundIframe();
        a ? (d && b.insertSiblingBefore(d, this.getElement()), b.insertSiblingBefore(c, this.getElement())) : (b.removeNode(d), b.removeNode(c))
    }
};
goog.ui.Dialog.prototype.getModal = function () {
    return this.modal_
};
goog.ui.Dialog.prototype.getClass = function () {
    return this.getCssClass()
};
goog.ui.Dialog.prototype.setDraggable = function (a) {
    this.draggable_ = a;
    this.setDraggingEnabled_(a && this.isInDocument())
};
goog.ui.Dialog.prototype.createDragger = function () {
    return new goog.fx.Dragger(this.getElement(), this.titleEl_)
};
goog.ui.Dialog.prototype.getDraggable = function () {
    return this.draggable_
};
goog.ui.Dialog.prototype.setDraggingEnabled_ = function (a) {
    this.getElement() && goog.dom.classes.enable(this.titleEl_, this.class_ + "-title-draggable", a);
    a && !this.dragger_ ? (this.dragger_ = this.createDragger(), goog.dom.classes.add(this.titleEl_, this.class_ + "-title-draggable"), goog.events.listen(this.dragger_, goog.fx.Dragger.EventType.START, this.setDraggerLimits_, !1, this)) : !a && this.dragger_ && (this.dragger_.dispose(), this.dragger_ = null)
};
goog.ui.Dialog.prototype.createDom = function () {
    goog.ui.Dialog.superClass_.createDom.call(this);
    var a = this.getElement();
    goog.asserts.assert(a, "getElement() returns null");
    var b = this.getDomHelper();
    this.titleEl_ = b.createDom("div", {className: this.class_ + "-title", id: this.getId()}, this.titleTextEl_ = b.createDom("span", this.class_ + "-title-text", this.title_), this.titleCloseEl_ = b.createDom("span", this.class_ + "-title-close"));
    goog.dom.append(a, this.titleEl_, this.contentEl_ = b.createDom("div", this.class_ + "-content"),
        this.buttonEl_ = b.createDom("div", this.class_ + "-buttons"));
    this.titleId_ = this.titleEl_.id;
    goog.a11y.aria.setRole(a, this.getPreferredAriaRole());
    goog.a11y.aria.setState(a, goog.a11y.aria.State.LABELLEDBY, this.titleId_ || "");
    this.content_ && (this.contentEl_.innerHTML = this.content_);
    goog.style.setElementShown(this.titleCloseEl_, this.hasTitleCloseButton_);
    this.buttons_ && this.buttons_.attachToElement(this.buttonEl_);
    goog.style.setElementShown(this.buttonEl_, !!this.buttons_);
    this.setBackgroundElementOpacity(this.backgroundElementOpacity_)
};
goog.ui.Dialog.prototype.decorateInternal = function (a) {
    goog.ui.Dialog.superClass_.decorateInternal.call(this, a);
    a = this.getElement();
    goog.asserts.assert(a, "The DOM element for dialog cannot be null.");
    var b = this.class_ + "-content";
    (this.contentEl_ = goog.dom.getElementsByTagNameAndClass(null, b, a)[0]) ? this.content_ = this.contentEl_.innerHTML : (this.contentEl_ = this.getDomHelper().createDom("div", b), this.content_ && (this.contentEl_.innerHTML = this.content_), a.appendChild(this.contentEl_));
    var b = this.class_ + "-title",
        c = this.class_ + "-title-text", d = this.class_ + "-title-close";
    (this.titleEl_ = goog.dom.getElementsByTagNameAndClass(null, b, a)[0]) ? (this.titleTextEl_ = goog.dom.getElementsByTagNameAndClass(null, c, this.titleEl_)[0], this.titleCloseEl_ = goog.dom.getElementsByTagNameAndClass(null, d, this.titleEl_)[0], this.titleEl_.id || (this.titleEl_.id = this.getId())) : (this.titleEl_ = this.getDomHelper().createDom("div", {className: b, id: this.getId()}), a.insertBefore(this.titleEl_, this.contentEl_));
    this.titleId_ = this.titleEl_.id;
    this.titleTextEl_ ?
        this.title_ = goog.dom.getTextContent(this.titleTextEl_) : (this.titleTextEl_ = this.getDomHelper().createDom("span", c, this.title_), this.titleEl_.appendChild(this.titleTextEl_));
    goog.a11y.aria.setState(a, goog.a11y.aria.State.LABELLEDBY, this.titleId_ || "");
    this.titleCloseEl_ || (this.titleCloseEl_ = this.getDomHelper().createDom("span", d), this.titleEl_.appendChild(this.titleCloseEl_));
    goog.style.setElementShown(this.titleCloseEl_, this.hasTitleCloseButton_);
    b = this.class_ + "-buttons";
    (this.buttonEl_ = goog.dom.getElementsByTagNameAndClass(null,
        b, a)[0]) ? (this.buttons_ = new goog.ui.Dialog.ButtonSet(this.getDomHelper()), this.buttons_.decorate(this.buttonEl_)) : (this.buttonEl_ = this.getDomHelper().createDom("div", b), a.appendChild(this.buttonEl_), this.buttons_ && this.buttons_.attachToElement(this.buttonEl_), goog.style.setElementShown(this.buttonEl_, !!this.buttons_));
    this.setBackgroundElementOpacity(this.backgroundElementOpacity_)
};
goog.ui.Dialog.prototype.enterDocument = function () {
    goog.ui.Dialog.superClass_.enterDocument.call(this);
    this.getHandler().listen(this.getElement(), goog.events.EventType.KEYDOWN, this.onKey_).listen(this.getElement(), goog.events.EventType.KEYPRESS, this.onKey_);
    this.getHandler().listen(this.buttonEl_, goog.events.EventType.CLICK, this.onButtonClick_);
    this.setDraggingEnabled_(this.draggable_);
    this.getHandler().listen(this.titleCloseEl_, goog.events.EventType.CLICK, this.onTitleCloseClick_);
    var a = this.getElement();
    goog.asserts.assert(a, "The DOM element for dialog cannot be null");
    goog.a11y.aria.setRole(a, this.getPreferredAriaRole());
    "" !== this.titleTextEl_.id && goog.a11y.aria.setState(a, goog.a11y.aria.State.LABELLEDBY, this.titleTextEl_.id);
    this.modal_ || this.setModalInternal_(!1)
};
goog.ui.Dialog.prototype.exitDocument = function () {
    this.isVisible() && this.setVisible(!1);
    this.setDraggingEnabled_(!1);
    goog.ui.Dialog.superClass_.exitDocument.call(this)
};
goog.ui.Dialog.prototype.setVisible = function (a) {
    a != this.isVisible() && (this.isInDocument() || this.render(), goog.ui.Dialog.superClass_.setVisible.call(this, a))
};
goog.ui.Dialog.prototype.onShow = function () {
    goog.ui.Dialog.superClass_.onShow.call(this);
    this.dispatchEvent(goog.ui.Dialog.EventType.AFTER_SHOW)
};
goog.ui.Dialog.prototype.onHide = function () {
    goog.ui.Dialog.superClass_.onHide.call(this);
    this.dispatchEvent(goog.ui.Dialog.EventType.AFTER_HIDE);
    this.disposeOnHide_ && this.dispose()
};
goog.ui.Dialog.prototype.focus = function () {
    goog.ui.Dialog.superClass_.focus.call(this);
    if (this.getButtonSet()) {
        var a = this.getButtonSet().getDefault();
        if (a)for (var b = this.getDomHelper().getDocument(), c = this.buttonEl_.getElementsByTagName("button"), d = 0, e; e = c[d]; d++)if (e.name == a && !e.disabled) {
            try {
                if (goog.userAgent.WEBKIT || goog.userAgent.OPERA) {
                    var f = b.createElement("input");
                    f.style.cssText = "position:fixed;width:0;height:0;left:0;top:0;";
                    this.getElement().appendChild(f);
                    f.focus();
                    this.getElement().removeChild(f)
                }
                e.focus()
            } catch (g) {
            }
            break
        }
    }
};
goog.ui.Dialog.prototype.setDraggerLimits_ = function (a) {
    var b = this.getDomHelper().getDocument();
    a = goog.dom.getWindow(b) || window;
    a = goog.dom.getViewportSize(a);
    var c = Math.max(b.body.scrollWidth, a.width), b = Math.max(b.body.scrollHeight, a.height), d = goog.style.getSize(this.getElement());
    "fixed" == goog.style.getComputedPosition(this.getElement()) ? this.dragger_.setLimits(new goog.math.Rect(0, 0, Math.max(0, a.width - d.width), Math.max(0, a.height - d.height))) : this.dragger_.setLimits(new goog.math.Rect(0, 0, c - d.width,
        b - d.height))
};
goog.ui.Dialog.prototype.onTitleCloseClick_ = function (a) {
    if (this.hasTitleCloseButton_) {
        var b = this.getButtonSet();
        (a = b && b.getCancel()) ? (b = b.get(a), this.dispatchEvent(new goog.ui.Dialog.Event(a, b)) && this.setVisible(!1)) : this.setVisible(!1)
    }
};
goog.ui.Dialog.prototype.getHasTitleCloseButton = function () {
    return this.hasTitleCloseButton_
};
goog.ui.Dialog.prototype.setHasTitleCloseButton = function (a) {
    this.hasTitleCloseButton_ = a;
    this.titleCloseEl_ && goog.style.setElementShown(this.titleCloseEl_, this.hasTitleCloseButton_)
};
goog.ui.Dialog.prototype.isEscapeToCancel = function () {
    return this.escapeToCancel_
};
goog.ui.Dialog.prototype.setEscapeToCancel = function (a) {
    this.escapeToCancel_ = a
};
goog.ui.Dialog.prototype.setDisposeOnHide = function (a) {
    this.disposeOnHide_ = a
};
goog.ui.Dialog.prototype.getDisposeOnHide = function () {
    return this.disposeOnHide_
};
goog.ui.Dialog.prototype.disposeInternal = function () {
    this.buttonEl_ = this.titleCloseEl_ = null;
    goog.ui.Dialog.superClass_.disposeInternal.call(this)
};
goog.ui.Dialog.prototype.setButtonSet = function (a) {
    this.buttons_ = a;
    this.buttonEl_ && (this.buttons_ ? this.buttons_.attachToElement(this.buttonEl_) : this.buttonEl_.innerHTML = "", goog.style.setElementShown(this.buttonEl_, !!this.buttons_))
};
goog.ui.Dialog.prototype.getButtonSet = function () {
    return this.buttons_
};
goog.ui.Dialog.prototype.onButtonClick_ = function (a) {
    if ((a = this.findParentButton_(a.target)) && !a.disabled) {
        a = a.name;
        var b = this.getButtonSet().get(a);
        this.dispatchEvent(new goog.ui.Dialog.Event(a, b)) && this.setVisible(!1)
    }
};
goog.ui.Dialog.prototype.findParentButton_ = function (a) {
    for (; null != a && a != this.buttonEl_;) {
        if ("BUTTON" == a.tagName)return a;
        a = a.parentNode
    }
    return null
};
goog.ui.Dialog.prototype.onKey_ = function (a) {
    var b = !1, c = !1, d = this.getButtonSet(), e = a.target;
    if (a.type == goog.events.EventType.KEYDOWN)if (this.escapeToCancel_ && a.keyCode == goog.events.KeyCodes.ESC) {
        var f = d && d.getCancel(), e = "SELECT" == e.tagName && !e.disabled;
        f && !e ? (c = !0, b = d.get(f), b = this.dispatchEvent(new goog.ui.Dialog.Event(f, b))) : e || (b = !0)
    } else a.keyCode == goog.events.KeyCodes.TAB && (a.shiftKey && e == this.getElement()) && this.setupBackwardTabWrap(); else if (a.keyCode == goog.events.KeyCodes.ENTER) {
        if ("BUTTON" ==
            e.tagName && !e.disabled)f = e.name; else if (d) {
            var g = d.getDefault(), h = g && d.getButton(g), e = ("TEXTAREA" == e.tagName || "SELECT" == e.tagName || "A" == e.tagName) && !e.disabled;
            !h || (h.disabled || e) || (f = g)
        }
        f && d && (c = !0, b = this.dispatchEvent(new goog.ui.Dialog.Event(f, String(d.get(f)))))
    }
    if (b || c)a.stopPropagation(), a.preventDefault();
    b && this.setVisible(!1)
};
goog.ui.Dialog.Event = function (a, b) {
    this.type = goog.ui.Dialog.EventType.SELECT;
    this.key = a;
    this.caption = b
};
goog.inherits(goog.ui.Dialog.Event, goog.events.Event);
goog.ui.Dialog.SELECT_EVENT = "dialogselect";
goog.ui.Dialog.EventType = {SELECT: "dialogselect", AFTER_HIDE: "afterhide", AFTER_SHOW: "aftershow"};
goog.ui.Dialog.ButtonSet = function (a) {
    this.dom_ = a || goog.dom.getDomHelper();
    goog.structs.Map.call(this)
};
goog.inherits(goog.ui.Dialog.ButtonSet, goog.structs.Map);
goog.ui.Dialog.ButtonSet.prototype.class_ = "goog-buttonset";
goog.ui.Dialog.ButtonSet.prototype.defaultButton_ = null;
goog.ui.Dialog.ButtonSet.prototype.element_ = null;
goog.ui.Dialog.ButtonSet.prototype.cancelButton_ = null;
goog.ui.Dialog.ButtonSet.prototype.set = function (a, b, c, d) {
    goog.structs.Map.prototype.set.call(this, a, b);
    c && (this.defaultButton_ = a);
    d && (this.cancelButton_ = a);
    return this
};
goog.ui.Dialog.ButtonSet.prototype.addButton = function (a, b, c) {
    return this.set(a.key, a.caption, b, c)
};
goog.ui.Dialog.ButtonSet.prototype.attachToElement = function (a) {
    this.element_ = a;
    this.render()
};
goog.ui.Dialog.ButtonSet.prototype.render = function () {
    if (this.element_) {
        this.element_.innerHTML = "";
        var a = goog.dom.getDomHelper(this.element_);
        goog.structs.forEach(this, function (b, c) {
            var d = a.createDom("button", {name: c}, b);
            c == this.defaultButton_ && (d.className = this.class_ + "-default");
            this.element_.appendChild(d)
        }, this)
    }
};
goog.ui.Dialog.ButtonSet.prototype.decorate = function (a) {
    if (a && a.nodeType == goog.dom.NodeType.ELEMENT) {
        this.element_ = a;
        a = this.element_.getElementsByTagName("button");
        for (var b = 0, c, d, e; c = a[b]; b++)if (d = c.name || c.id, e = goog.dom.getTextContent(c) || c.value, d) {
            var f = 0 == b;
            this.set(d, e, f, c.name == goog.ui.Dialog.DefaultButtonKeys.CANCEL);
            f && goog.dom.classes.add(c, this.class_ + "-default")
        }
    }
};
goog.ui.Dialog.ButtonSet.prototype.getElement = function () {
    return this.element_
};
goog.ui.Dialog.ButtonSet.prototype.getDomHelper = function () {
    return this.dom_
};
goog.ui.Dialog.ButtonSet.prototype.setDefault = function (a) {
    this.defaultButton_ = a
};
goog.ui.Dialog.ButtonSet.prototype.getDefault = function () {
    return this.defaultButton_
};
goog.ui.Dialog.ButtonSet.prototype.setCancel = function (a) {
    this.cancelButton_ = a
};
goog.ui.Dialog.ButtonSet.prototype.getCancel = function () {
    return this.cancelButton_
};
goog.ui.Dialog.ButtonSet.prototype.getButton = function (a) {
    for (var b = this.getAllButtons(), c = 0, d; d = b[c]; c++)if (d.name == a || d.id == a)return d;
    return null
};
goog.ui.Dialog.ButtonSet.prototype.getAllButtons = function () {
    return this.element_.getElementsByTagName(goog.dom.TagName.BUTTON)
};
goog.ui.Dialog.ButtonSet.prototype.setButtonEnabled = function (a, b) {
    var c = this.getButton(a);
    c && (c.disabled = !b)
};
goog.ui.Dialog.ButtonSet.prototype.setAllButtonsEnabled = function (a) {
    for (var b = this.getAllButtons(), c = 0, d; d = b[c]; c++)d.disabled = !a
};
goog.ui.Dialog.DefaultButtonKeys = {OK: "ok", CANCEL: "cancel", YES: "yes", NO: "no", SAVE: "save", CONTINUE: "continue"};
goog.ui.Dialog.MSG_DIALOG_OK_ = goog.getMsg("OK");
goog.ui.Dialog.MSG_DIALOG_CANCEL_ = goog.getMsg("Cancel");
goog.ui.Dialog.MSG_DIALOG_YES_ = goog.getMsg("Yes");
goog.ui.Dialog.MSG_DIALOG_NO_ = goog.getMsg("No");
goog.ui.Dialog.MSG_DIALOG_SAVE_ = goog.getMsg("Save");
goog.ui.Dialog.MSG_DIALOG_CONTINUE_ = goog.getMsg("Continue");
goog.ui.Dialog.DefaultButtonCaptions = {OK: goog.ui.Dialog.MSG_DIALOG_OK_, CANCEL: goog.ui.Dialog.MSG_DIALOG_CANCEL_, YES: goog.ui.Dialog.MSG_DIALOG_YES_, NO: goog.ui.Dialog.MSG_DIALOG_NO_, SAVE: goog.ui.Dialog.MSG_DIALOG_SAVE_, CONTINUE: goog.ui.Dialog.MSG_DIALOG_CONTINUE_};
goog.ui.Dialog.ButtonSet.DefaultButtons = {OK: {key: goog.ui.Dialog.DefaultButtonKeys.OK, caption: goog.ui.Dialog.DefaultButtonCaptions.OK}, CANCEL: {key: goog.ui.Dialog.DefaultButtonKeys.CANCEL, caption: goog.ui.Dialog.DefaultButtonCaptions.CANCEL}, YES: {key: goog.ui.Dialog.DefaultButtonKeys.YES, caption: goog.ui.Dialog.DefaultButtonCaptions.YES}, NO: {key: goog.ui.Dialog.DefaultButtonKeys.NO, caption: goog.ui.Dialog.DefaultButtonCaptions.NO}, SAVE: {key: goog.ui.Dialog.DefaultButtonKeys.SAVE, caption: goog.ui.Dialog.DefaultButtonCaptions.SAVE},
    CONTINUE: {key: goog.ui.Dialog.DefaultButtonKeys.CONTINUE, caption: goog.ui.Dialog.DefaultButtonCaptions.CONTINUE}};
goog.ui.Dialog.ButtonSet.createOk = function () {
    return(new goog.ui.Dialog.ButtonSet).addButton(goog.ui.Dialog.ButtonSet.DefaultButtons.OK, !0, !0)
};
goog.ui.Dialog.ButtonSet.createOkCancel = function () {
    return(new goog.ui.Dialog.ButtonSet).addButton(goog.ui.Dialog.ButtonSet.DefaultButtons.OK, !0).addButton(goog.ui.Dialog.ButtonSet.DefaultButtons.CANCEL, !1, !0)
};
goog.ui.Dialog.ButtonSet.createYesNo = function () {
    return(new goog.ui.Dialog.ButtonSet).addButton(goog.ui.Dialog.ButtonSet.DefaultButtons.YES, !0).addButton(goog.ui.Dialog.ButtonSet.DefaultButtons.NO, !1, !0)
};
goog.ui.Dialog.ButtonSet.createYesNoCancel = function () {
    return(new goog.ui.Dialog.ButtonSet).addButton(goog.ui.Dialog.ButtonSet.DefaultButtons.YES).addButton(goog.ui.Dialog.ButtonSet.DefaultButtons.NO, !0).addButton(goog.ui.Dialog.ButtonSet.DefaultButtons.CANCEL, !1, !0)
};
goog.ui.Dialog.ButtonSet.createContinueSaveCancel = function () {
    return(new goog.ui.Dialog.ButtonSet).addButton(goog.ui.Dialog.ButtonSet.DefaultButtons.CONTINUE).addButton(goog.ui.Dialog.ButtonSet.DefaultButtons.SAVE).addButton(goog.ui.Dialog.ButtonSet.DefaultButtons.CANCEL, !0, !0)
};
(function () {
    "undefined" != typeof document && (goog.ui.Dialog.ButtonSet.OK = goog.ui.Dialog.ButtonSet.createOk(), goog.ui.Dialog.ButtonSet.OK_CANCEL = goog.ui.Dialog.ButtonSet.createOkCancel(), goog.ui.Dialog.ButtonSet.YES_NO = goog.ui.Dialog.ButtonSet.createYesNo(), goog.ui.Dialog.ButtonSet.YES_NO_CANCEL = goog.ui.Dialog.ButtonSet.createYesNoCancel(), goog.ui.Dialog.ButtonSet.CONTINUE_SAVE_CANCEL = goog.ui.Dialog.ButtonSet.createContinueSaveCancel())
})();
goog.ui.ControlRenderer = function () {
};
goog.addSingletonGetter(goog.ui.ControlRenderer);
goog.ui.ControlRenderer.getCustomRenderer = function (a, b) {
    var c = new a;
    c.getCssClass = function () {
        return b
    };
    return c
};
goog.ui.ControlRenderer.CSS_CLASS = "goog-control";
goog.ui.ControlRenderer.IE6_CLASS_COMBINATIONS = [];
goog.ui.ControlRenderer.prototype.getAriaRole = function () {
};
goog.ui.ControlRenderer.prototype.createDom = function (a) {
    var b = a.getDomHelper().createDom("div", this.getClassNames(a).join(" "), a.getContent());
    this.setAriaStates(a, b);
    return b
};
goog.ui.ControlRenderer.prototype.getContentElement = function (a) {
    return a
};
goog.ui.ControlRenderer.prototype.enableClassName = function (a, b, c) {
    if (a = a.getElement ? a.getElement() : a)if (goog.userAgent.IE && !goog.userAgent.isVersionOrHigher("7")) {
        var d = this.getAppliedCombinedClassNames_(goog.dom.classes.get(a), b);
        d.push(b);
        goog.partial(c ? goog.dom.classes.add : goog.dom.classes.remove, a).apply(null, d)
    } else goog.dom.classes.enable(a, b, c)
};
goog.ui.ControlRenderer.prototype.enableExtraClassName = function (a, b, c) {
    this.enableClassName(a, b, c)
};
goog.ui.ControlRenderer.prototype.canDecorate = function (a) {
    return!0
};
goog.ui.ControlRenderer.prototype.decorate = function (a, b) {
    b.id && a.setId(b.id);
    var c = this.getContentElement(b);
    c && c.firstChild ? a.setContentInternal(c.firstChild.nextSibling ? goog.array.clone(c.childNodes) : c.firstChild) : a.setContentInternal(null);
    var d = 0, e = this.getCssClass(), f = this.getStructuralCssClass(), g = !1, h = !1, c = !1, k = goog.dom.classes.get(b);
    goog.array.forEach(k, function (a) {
        g || a != e ? h || a != f ? d |= this.getStateFromClass(a) : h = !0 : (g = !0, f == e && (h = !0))
    }, this);
    a.setStateInternal(d);
    g || (k.push(e), f == e && (h = !0));
    h || k.push(f);
    var l = a.getExtraClassNames();
    l && k.push.apply(k, l);
    if (goog.userAgent.IE && !goog.userAgent.isVersionOrHigher("7")) {
        var m = this.getAppliedCombinedClassNames_(k);
        0 < m.length && (k.push.apply(k, m), c = !0)
    }
    g && h && !l && !c || goog.dom.classes.set(b, k.join(" "));
    this.setAriaStates(a, b);
    return b
};
goog.ui.ControlRenderer.prototype.initializeDom = function (a) {
    a.isRightToLeft() && this.setRightToLeft(a.getElement(), !0);
    a.isEnabled() && this.setFocusable(a, a.isVisible())
};
goog.ui.ControlRenderer.prototype.setAriaRole = function (a, b) {
    var c = b || this.getAriaRole();
    c && (goog.asserts.assert(a, "The element passed as a first parameter cannot be null."), goog.a11y.aria.setRole(a, c))
};
goog.ui.ControlRenderer.prototype.setAriaStates = function (a, b) {
    goog.asserts.assert(a);
    goog.asserts.assert(b);
    a.isVisible() || goog.a11y.aria.setState(b, goog.a11y.aria.State.HIDDEN, !a.isVisible());
    a.isEnabled() || this.updateAriaState(b, goog.ui.Component.State.DISABLED, !a.isEnabled());
    a.isSupportedState(goog.ui.Component.State.SELECTED) && this.updateAriaState(b, goog.ui.Component.State.SELECTED, a.isSelected());
    a.isSupportedState(goog.ui.Component.State.CHECKED) && this.updateAriaState(b, goog.ui.Component.State.CHECKED,
        a.isChecked());
    a.isSupportedState(goog.ui.Component.State.OPENED) && this.updateAriaState(b, goog.ui.Component.State.OPENED, a.isOpen())
};
goog.ui.ControlRenderer.prototype.setAllowTextSelection = function (a, b) {
    goog.style.setUnselectable(a, !b, !goog.userAgent.IE && !goog.userAgent.OPERA)
};
goog.ui.ControlRenderer.prototype.setRightToLeft = function (a, b) {
    this.enableClassName(a, this.getStructuralCssClass() + "-rtl", b)
};
goog.ui.ControlRenderer.prototype.isFocusable = function (a) {
    var b;
    return a.isSupportedState(goog.ui.Component.State.FOCUSED) && (b = a.getKeyEventTarget()) ? goog.dom.isFocusableTabIndex(b) : !1
};
goog.ui.ControlRenderer.prototype.setFocusable = function (a, b) {
    var c;
    if (a.isSupportedState(goog.ui.Component.State.FOCUSED) && (c = a.getKeyEventTarget())) {
        if (!b && a.isFocused()) {
            try {
                c.blur()
            } catch (d) {
            }
            a.isFocused() && a.handleBlur(null)
        }
        goog.dom.isFocusableTabIndex(c) != b && goog.dom.setFocusableTabIndex(c, b)
    }
};
goog.ui.ControlRenderer.prototype.setVisible = function (a, b) {
    goog.style.setElementShown(a, b);
    a && goog.a11y.aria.setState(a, goog.a11y.aria.State.HIDDEN, !b)
};
goog.ui.ControlRenderer.prototype.setState = function (a, b, c) {
    var d = a.getElement();
    if (d) {
        var e = this.getClassForState(b);
        e && this.enableClassName(a, e, c);
        this.updateAriaState(d, b, c)
    }
};
goog.ui.ControlRenderer.prototype.updateAriaState = function (a, b, c) {
    goog.ui.ControlRenderer.ARIA_STATE_MAP_ || (goog.ui.ControlRenderer.ARIA_STATE_MAP_ = goog.object.create(goog.ui.Component.State.DISABLED, goog.a11y.aria.State.DISABLED, goog.ui.Component.State.SELECTED, goog.a11y.aria.State.SELECTED, goog.ui.Component.State.CHECKED, goog.a11y.aria.State.CHECKED, goog.ui.Component.State.OPENED, goog.a11y.aria.State.EXPANDED));
    if (b = goog.ui.ControlRenderer.ARIA_STATE_MAP_[b])goog.asserts.assert(a, "The element passed as a first parameter cannot be null."),
        goog.a11y.aria.setState(a, b, c)
};
goog.ui.ControlRenderer.prototype.setContent = function (a, b) {
    var c = this.getContentElement(a);
    if (c && (goog.dom.removeChildren(c), b))if (goog.isString(b))goog.dom.setTextContent(c, b); else {
        var d = function (a) {
            if (a) {
                var b = goog.dom.getOwnerDocument(c);
                c.appendChild(goog.isString(a) ? b.createTextNode(a) : a)
            }
        };
        goog.isArray(b) ? goog.array.forEach(b, d) : !goog.isArrayLike(b) || "nodeType"in b ? d(b) : goog.array.forEach(goog.array.clone(b), d)
    }
};
goog.ui.ControlRenderer.prototype.getKeyEventTarget = function (a) {
    return a.getElement()
};
goog.ui.ControlRenderer.prototype.getCssClass = function () {
    return goog.ui.ControlRenderer.CSS_CLASS
};
goog.ui.ControlRenderer.prototype.getIe6ClassCombinations = function () {
    return[]
};
goog.ui.ControlRenderer.prototype.getStructuralCssClass = function () {
    return this.getCssClass()
};
goog.ui.ControlRenderer.prototype.getClassNames = function (a) {
    var b = this.getCssClass(), c = [b], d = this.getStructuralCssClass();
    d != b && c.push(d);
    b = this.getClassNamesForState(a.getState());
    c.push.apply(c, b);
    (a = a.getExtraClassNames()) && c.push.apply(c, a);
    goog.userAgent.IE && !goog.userAgent.isVersionOrHigher("7") && c.push.apply(c, this.getAppliedCombinedClassNames_(c));
    return c
};
goog.ui.ControlRenderer.prototype.getAppliedCombinedClassNames_ = function (a, b) {
    var c = [];
    b && (a = a.concat([b]));
    goog.array.forEach(this.getIe6ClassCombinations(), function (d) {
        !goog.array.every(d, goog.partial(goog.array.contains, a)) || b && !goog.array.contains(d, b) || c.push(d.join("_"))
    });
    return c
};
goog.ui.ControlRenderer.prototype.getClassNamesForState = function (a) {
    for (var b = []; a;) {
        var c = a & -a;
        b.push(this.getClassForState(c));
        a &= ~c
    }
    return b
};
goog.ui.ControlRenderer.prototype.getClassForState = function (a) {
    this.classByState_ || this.createClassByStateMap_();
    return this.classByState_[a]
};
goog.ui.ControlRenderer.prototype.getStateFromClass = function (a) {
    this.stateByClass_ || this.createStateByClassMap_();
    a = parseInt(this.stateByClass_[a], 10);
    return isNaN(a) ? 0 : a
};
goog.ui.ControlRenderer.prototype.createClassByStateMap_ = function () {
    var a = this.getStructuralCssClass();
    this.classByState_ = goog.object.create(goog.ui.Component.State.DISABLED, a + "-disabled", goog.ui.Component.State.HOVER, a + "-hover", goog.ui.Component.State.ACTIVE, a + "-active", goog.ui.Component.State.SELECTED, a + "-selected", goog.ui.Component.State.CHECKED, a + "-checked", goog.ui.Component.State.FOCUSED, a + "-focused", goog.ui.Component.State.OPENED, a + "-open")
};
goog.ui.ControlRenderer.prototype.createStateByClassMap_ = function () {
    this.classByState_ || this.createClassByStateMap_();
    this.stateByClass_ = goog.object.transpose(this.classByState_)
};
goog.ui.ButtonSide = {NONE: 0, START: 1, END: 2, BOTH: 3};
goog.ui.ButtonRenderer = function () {
    goog.ui.ControlRenderer.call(this)
};
goog.inherits(goog.ui.ButtonRenderer, goog.ui.ControlRenderer);
goog.addSingletonGetter(goog.ui.ButtonRenderer);
goog.ui.ButtonRenderer.CSS_CLASS = "goog-button";
goog.ui.ButtonRenderer.prototype.getAriaRole = function () {
    return goog.a11y.aria.Role.BUTTON
};
goog.ui.ButtonRenderer.prototype.updateAriaState = function (a, b, c) {
    switch (b) {
        case goog.ui.Component.State.SELECTED:
        case goog.ui.Component.State.CHECKED:
            goog.asserts.assert(a, "The button DOM element cannot be null.");
            goog.a11y.aria.setState(a, goog.a11y.aria.State.PRESSED, c);
            break;
        default:
        case goog.ui.Component.State.OPENED:
        case goog.ui.Component.State.DISABLED:
            goog.ui.ButtonRenderer.superClass_.updateAriaState.call(this, a, b, c)
    }
};
goog.ui.ButtonRenderer.prototype.createDom = function (a) {
    var b = goog.ui.ButtonRenderer.superClass_.createDom.call(this, a);
    this.setTooltip(b, a.getTooltip());
    var c = a.getValue();
    c && this.setValue(b, c);
    a.isSupportedState(goog.ui.Component.State.CHECKED) && this.updateAriaState(b, goog.ui.Component.State.CHECKED, a.isChecked());
    return b
};
goog.ui.ButtonRenderer.prototype.decorate = function (a, b) {
    b = goog.ui.ButtonRenderer.superClass_.decorate.call(this, a, b);
    a.setValueInternal(this.getValue(b));
    a.setTooltipInternal(this.getTooltip(b));
    a.isSupportedState(goog.ui.Component.State.CHECKED) && this.updateAriaState(b, goog.ui.Component.State.CHECKED, a.isChecked());
    return b
};
goog.ui.ButtonRenderer.prototype.getValue = goog.nullFunction;
goog.ui.ButtonRenderer.prototype.setValue = goog.nullFunction;
goog.ui.ButtonRenderer.prototype.getTooltip = function (a) {
    return a.title
};
goog.ui.ButtonRenderer.prototype.setTooltip = function (a, b) {
    a && b && (a.title = b)
};
goog.ui.ButtonRenderer.prototype.setCollapsed = function (a, b) {
    var c = a.isRightToLeft(), d = this.getStructuralCssClass() + "-collapse-left", e = this.getStructuralCssClass() + "-collapse-right";
    a.enableClassName(c ? e : d, !!(b & goog.ui.ButtonSide.START));
    a.enableClassName(c ? d : e, !!(b & goog.ui.ButtonSide.END))
};
goog.ui.ButtonRenderer.prototype.getCssClass = function () {
    return goog.ui.ButtonRenderer.CSS_CLASS
};
goog.events.ActionHandler = function (a) {
    goog.events.EventTarget.call(this);
    this.element_ = a;
    goog.events.listen(a, goog.events.ActionHandler.KEY_EVENT_TYPE_, this.handleKeyDown_, !1, this);
    goog.events.listen(a, goog.events.EventType.CLICK, this.handleClick_, !1, this)
};
goog.inherits(goog.events.ActionHandler, goog.events.EventTarget);
goog.events.ActionHandler.EventType = {ACTION: "action", BEFOREACTION: "beforeaction"};
goog.events.ActionHandler.KEY_EVENT_TYPE_ = goog.userAgent.GECKO ? goog.events.EventType.KEYPRESS : goog.events.EventType.KEYDOWN;
goog.events.ActionHandler.prototype.handleKeyDown_ = function (a) {
    (a.keyCode == goog.events.KeyCodes.ENTER || goog.userAgent.WEBKIT && a.keyCode == goog.events.KeyCodes.MAC_ENTER) && this.dispatchEvents_(a)
};
goog.events.ActionHandler.prototype.handleClick_ = function (a) {
    this.dispatchEvents_(a)
};
goog.events.ActionHandler.prototype.dispatchEvents_ = function (a) {
    var b = new goog.events.BeforeActionEvent(a);
    if (this.dispatchEvent(b)) {
        b = new goog.events.ActionEvent(a);
        try {
            this.dispatchEvent(b)
        } finally {
            a.stopPropagation()
        }
    }
};
goog.events.ActionHandler.prototype.disposeInternal = function () {
    goog.events.ActionHandler.superClass_.disposeInternal.call(this);
    goog.events.unlisten(this.element_, goog.events.ActionHandler.KEY_EVENT_TYPE_, this.handleKeyDown_, !1, this);
    goog.events.unlisten(this.element_, goog.events.EventType.CLICK, this.handleClick_, !1, this);
    delete this.element_
};
goog.events.ActionEvent = function (a) {
    goog.events.BrowserEvent.call(this, a.getBrowserEvent());
    this.type = goog.events.ActionHandler.EventType.ACTION
};
goog.inherits(goog.events.ActionEvent, goog.events.BrowserEvent);
goog.events.BeforeActionEvent = function (a) {
    goog.events.BrowserEvent.call(this, a.getBrowserEvent());
    this.type = goog.events.ActionHandler.EventType.BEFOREACTION
};
goog.inherits(goog.events.BeforeActionEvent, goog.events.BrowserEvent);
var widjdev = {categories: [
    {name: "Compatibility", colorCode: "#9ce148", subcategories: ["Family Friends", "Parenting", "Finances", "Career Hobbies"]},
    {name: "Connection", colorCode: "#ffff00"},
    {name: "Exterior", colorCode: "#004cc0"},
    {name: "Eroticism", colorCode: "#f11515"},
    {name: "Seduction", colorCode: "#ed427d"},
    {name: "Psy Tendencies", colorCode: "#a32ad4", subcategories: "NPD;Depression;Post traumatic stress;Addiction;Compulsive lying;Codependency;Low self esteem;Obsessive compulsive disorder;Sulking/silent treatment;Hoarding;Passive aggressive;Autism;Aspergers;Rape;Dislexia".split(";")},
    {name: "Affairs", colorCode: "#ff8f00"},
    {name: "Abuse", colorCode: "#d69051"}
]};
goog.exportSymbol("widjdev.categories", widjdev.categories);
goog.ui.NativeButtonRenderer = function () {
    goog.ui.ButtonRenderer.call(this)
};
goog.inherits(goog.ui.NativeButtonRenderer, goog.ui.ButtonRenderer);
goog.addSingletonGetter(goog.ui.NativeButtonRenderer);
goog.ui.NativeButtonRenderer.prototype.getAriaRole = function () {
};
goog.ui.NativeButtonRenderer.prototype.createDom = function (a) {
    this.setUpNativeButton_(a);
    return a.getDomHelper().createDom("button", {"class": this.getClassNames(a).join(" "), disabled: !a.isEnabled(), title: a.getTooltip() || "", value: a.getValue() || ""}, a.getCaption() || "")
};
goog.ui.NativeButtonRenderer.prototype.canDecorate = function (a) {
    return"BUTTON" == a.tagName || "INPUT" == a.tagName && ("button" == a.type || "submit" == a.type || "reset" == a.type)
};
goog.ui.NativeButtonRenderer.prototype.decorate = function (a, b) {
    this.setUpNativeButton_(a);
    b.disabled && goog.dom.classes.add(b, this.getClassForState(goog.ui.Component.State.DISABLED));
    return goog.ui.NativeButtonRenderer.superClass_.decorate.call(this, a, b)
};
goog.ui.NativeButtonRenderer.prototype.initializeDom = function (a) {
    a.getHandler().listen(a.getElement(), goog.events.EventType.CLICK, a.performActionInternal)
};
goog.ui.NativeButtonRenderer.prototype.setAllowTextSelection = goog.nullFunction;
goog.ui.NativeButtonRenderer.prototype.setRightToLeft = goog.nullFunction;
goog.ui.NativeButtonRenderer.prototype.isFocusable = function (a) {
    return a.isEnabled()
};
goog.ui.NativeButtonRenderer.prototype.setFocusable = goog.nullFunction;
goog.ui.NativeButtonRenderer.prototype.setState = function (a, b, c) {
    goog.ui.NativeButtonRenderer.superClass_.setState.call(this, a, b, c);
    (a = a.getElement()) && b == goog.ui.Component.State.DISABLED && (a.disabled = c)
};
goog.ui.NativeButtonRenderer.prototype.getValue = function (a) {
    return a.value
};
goog.ui.NativeButtonRenderer.prototype.setValue = function (a, b) {
    a && (a.value = b)
};
goog.ui.NativeButtonRenderer.prototype.updateAriaState = goog.nullFunction;
goog.ui.NativeButtonRenderer.prototype.setUpNativeButton_ = function (a) {
    a.setHandleMouseEvents(!1);
    a.setAutoStates(goog.ui.Component.State.ALL, !1);
    a.setSupportedState(goog.ui.Component.State.FOCUSED, !1)
};
goog.ui.registry = {};
goog.ui.registry.getDefaultRenderer = function (a) {
    for (var b; a;) {
        b = goog.getUid(a);
        if (b = goog.ui.registry.defaultRenderers_[b])break;
        a = a.superClass_ ? a.superClass_.constructor : null
    }
    return b ? goog.isFunction(b.getInstance) ? b.getInstance() : new b : null
};
goog.ui.registry.setDefaultRenderer = function (a, b) {
    if (!goog.isFunction(a))throw Error("Invalid component class " + a);
    if (!goog.isFunction(b))throw Error("Invalid renderer class " + b);
    var c = goog.getUid(a);
    goog.ui.registry.defaultRenderers_[c] = b
};
goog.ui.registry.getDecoratorByClassName = function (a) {
    return a in goog.ui.registry.decoratorFunctions_ ? goog.ui.registry.decoratorFunctions_[a]() : null
};
goog.ui.registry.setDecoratorByClassName = function (a, b) {
    if (!a)throw Error("Invalid class name " + a);
    if (!goog.isFunction(b))throw Error("Invalid decorator function " + b);
    goog.ui.registry.decoratorFunctions_[a] = b
};
goog.ui.registry.getDecorator = function (a) {
    for (var b = goog.dom.classes.get(a), c = 0, d = b.length; c < d; c++)if (a = goog.ui.registry.getDecoratorByClassName(b[c]))return a;
    return null
};
goog.ui.registry.reset = function () {
    goog.ui.registry.defaultRenderers_ = {};
    goog.ui.registry.decoratorFunctions_ = {}
};
goog.ui.registry.defaultRenderers_ = {};
goog.ui.registry.decoratorFunctions_ = {};
goog.events.KeyHandler = function (a, b) {
    goog.events.EventTarget.call(this);
    a && this.attach(a, b)
};
goog.inherits(goog.events.KeyHandler, goog.events.EventTarget);
goog.events.KeyHandler.prototype.element_ = null;
goog.events.KeyHandler.prototype.keyPressKey_ = null;
goog.events.KeyHandler.prototype.keyDownKey_ = null;
goog.events.KeyHandler.prototype.keyUpKey_ = null;
goog.events.KeyHandler.prototype.lastKey_ = -1;
goog.events.KeyHandler.prototype.keyCode_ = -1;
goog.events.KeyHandler.prototype.altKey_ = !1;
goog.events.KeyHandler.EventType = {KEY: "key"};
goog.events.KeyHandler.safariKey_ = {3: goog.events.KeyCodes.ENTER, 12: goog.events.KeyCodes.NUMLOCK, 63232: goog.events.KeyCodes.UP, 63233: goog.events.KeyCodes.DOWN, 63234: goog.events.KeyCodes.LEFT, 63235: goog.events.KeyCodes.RIGHT, 63236: goog.events.KeyCodes.F1, 63237: goog.events.KeyCodes.F2, 63238: goog.events.KeyCodes.F3, 63239: goog.events.KeyCodes.F4, 63240: goog.events.KeyCodes.F5, 63241: goog.events.KeyCodes.F6, 63242: goog.events.KeyCodes.F7, 63243: goog.events.KeyCodes.F8, 63244: goog.events.KeyCodes.F9, 63245: goog.events.KeyCodes.F10,
    63246: goog.events.KeyCodes.F11, 63247: goog.events.KeyCodes.F12, 63248: goog.events.KeyCodes.PRINT_SCREEN, 63272: goog.events.KeyCodes.DELETE, 63273: goog.events.KeyCodes.HOME, 63275: goog.events.KeyCodes.END, 63276: goog.events.KeyCodes.PAGE_UP, 63277: goog.events.KeyCodes.PAGE_DOWN, 63289: goog.events.KeyCodes.NUMLOCK, 63302: goog.events.KeyCodes.INSERT};
goog.events.KeyHandler.keyIdentifier_ = {Up: goog.events.KeyCodes.UP, Down: goog.events.KeyCodes.DOWN, Left: goog.events.KeyCodes.LEFT, Right: goog.events.KeyCodes.RIGHT, Enter: goog.events.KeyCodes.ENTER, F1: goog.events.KeyCodes.F1, F2: goog.events.KeyCodes.F2, F3: goog.events.KeyCodes.F3, F4: goog.events.KeyCodes.F4, F5: goog.events.KeyCodes.F5, F6: goog.events.KeyCodes.F6, F7: goog.events.KeyCodes.F7, F8: goog.events.KeyCodes.F8, F9: goog.events.KeyCodes.F9, F10: goog.events.KeyCodes.F10, F11: goog.events.KeyCodes.F11, F12: goog.events.KeyCodes.F12,
    "U+007F": goog.events.KeyCodes.DELETE, Home: goog.events.KeyCodes.HOME, End: goog.events.KeyCodes.END, PageUp: goog.events.KeyCodes.PAGE_UP, PageDown: goog.events.KeyCodes.PAGE_DOWN, Insert: goog.events.KeyCodes.INSERT};
goog.events.KeyHandler.USES_KEYDOWN_ = goog.userAgent.IE || goog.userAgent.WEBKIT && goog.userAgent.isVersionOrHigher("525");
goog.events.KeyHandler.SAVE_ALT_FOR_KEYPRESS_ = goog.userAgent.MAC && goog.userAgent.GECKO;
goog.events.KeyHandler.prototype.handleKeyDown_ = function (a) {
    goog.userAgent.WEBKIT && (this.lastKey_ == goog.events.KeyCodes.CTRL && !a.ctrlKey || this.lastKey_ == goog.events.KeyCodes.ALT && !a.altKey || goog.userAgent.MAC && this.lastKey_ == goog.events.KeyCodes.META && !a.metaKey) && (this.keyCode_ = this.lastKey_ = -1);
    -1 == this.lastKey_ && (a.ctrlKey && a.keyCode != goog.events.KeyCodes.CTRL ? this.lastKey_ = goog.events.KeyCodes.CTRL : a.altKey && a.keyCode != goog.events.KeyCodes.ALT ? this.lastKey_ = goog.events.KeyCodes.ALT : a.metaKey &&
        a.keyCode != goog.events.KeyCodes.META && (this.lastKey_ = goog.events.KeyCodes.META));
    goog.events.KeyHandler.USES_KEYDOWN_ && !goog.events.KeyCodes.firesKeyPressEvent(a.keyCode, this.lastKey_, a.shiftKey, a.ctrlKey, a.altKey) ? this.handleEvent(a) : (this.keyCode_ = goog.userAgent.GECKO ? goog.events.KeyCodes.normalizeGeckoKeyCode(a.keyCode) : a.keyCode, goog.events.KeyHandler.SAVE_ALT_FOR_KEYPRESS_ && (this.altKey_ = a.altKey))
};
goog.events.KeyHandler.prototype.resetState = function () {
    this.keyCode_ = this.lastKey_ = -1
};
goog.events.KeyHandler.prototype.handleKeyup_ = function (a) {
    this.resetState();
    this.altKey_ = a.altKey
};
goog.events.KeyHandler.prototype.handleEvent = function (a) {
    var b = a.getBrowserEvent(), c, d, e = b.altKey;
    goog.userAgent.IE && a.type == goog.events.EventType.KEYPRESS ? (c = this.keyCode_, d = c != goog.events.KeyCodes.ENTER && c != goog.events.KeyCodes.ESC ? b.keyCode : 0) : goog.userAgent.WEBKIT && a.type == goog.events.EventType.KEYPRESS ? (c = this.keyCode_, d = 0 <= b.charCode && 63232 > b.charCode && goog.events.KeyCodes.isCharacterKey(c) ? b.charCode : 0) : goog.userAgent.OPERA ? (c = this.keyCode_, d = goog.events.KeyCodes.isCharacterKey(c) ? b.keyCode :
        0) : (c = b.keyCode || this.keyCode_, d = b.charCode || 0, goog.events.KeyHandler.SAVE_ALT_FOR_KEYPRESS_ && (e = this.altKey_), goog.userAgent.MAC && (d == goog.events.KeyCodes.QUESTION_MARK && c == goog.events.KeyCodes.WIN_KEY) && (c = goog.events.KeyCodes.SLASH));
    var f = c, g = b.keyIdentifier;
    c ? 63232 <= c && c in goog.events.KeyHandler.safariKey_ ? f = goog.events.KeyHandler.safariKey_[c] : 25 == c && a.shiftKey && (f = 9) : g && g in goog.events.KeyHandler.keyIdentifier_ && (f = goog.events.KeyHandler.keyIdentifier_[g]);
    a = f == this.lastKey_;
    this.lastKey_ =
        f;
    b = new goog.events.KeyEvent(f, d, a, b);
    b.altKey = e;
    this.dispatchEvent(b)
};
goog.events.KeyHandler.prototype.getElement = function () {
    return this.element_
};
goog.events.KeyHandler.prototype.attach = function (a, b) {
    this.keyUpKey_ && this.detach();
    this.element_ = a;
    this.keyPressKey_ = goog.events.listen(this.element_, goog.events.EventType.KEYPRESS, this, b);
    this.keyDownKey_ = goog.events.listen(this.element_, goog.events.EventType.KEYDOWN, this.handleKeyDown_, b, this);
    this.keyUpKey_ = goog.events.listen(this.element_, goog.events.EventType.KEYUP, this.handleKeyup_, b, this)
};
goog.events.KeyHandler.prototype.detach = function () {
    this.keyPressKey_ && (goog.events.unlistenByKey(this.keyPressKey_), goog.events.unlistenByKey(this.keyDownKey_), goog.events.unlistenByKey(this.keyUpKey_), this.keyUpKey_ = this.keyDownKey_ = this.keyPressKey_ = null);
    this.element_ = null;
    this.keyCode_ = this.lastKey_ = -1
};
goog.events.KeyHandler.prototype.disposeInternal = function () {
    goog.events.KeyHandler.superClass_.disposeInternal.call(this);
    this.detach()
};
goog.events.KeyEvent = function (a, b, c, d) {
    goog.events.BrowserEvent.call(this, d);
    this.type = goog.events.KeyHandler.EventType.KEY;
    this.keyCode = a;
    this.charCode = b;
    this.repeat = c
};
goog.inherits(goog.events.KeyEvent, goog.events.BrowserEvent);
goog.ui.decorate = function (a) {
    var b = goog.ui.registry.getDecorator(a);
    b && b.decorate(a);
    return b
};
goog.ui.Control = function (a, b, c) {
    goog.ui.Component.call(this, c);
    this.renderer_ = b || goog.ui.registry.getDefaultRenderer(this.constructor);
    this.setContentInternal(a)
};
goog.inherits(goog.ui.Control, goog.ui.Component);
goog.ui.Control.registerDecorator = goog.ui.registry.setDecoratorByClassName;
goog.ui.Control.getDecorator = goog.ui.registry.getDecorator;
goog.ui.Control.decorate = goog.ui.decorate;
goog.ui.Control.prototype.content_ = null;
goog.ui.Control.prototype.state_ = 0;
goog.ui.Control.prototype.supportedStates_ = goog.ui.Component.State.DISABLED | goog.ui.Component.State.HOVER | goog.ui.Component.State.ACTIVE | goog.ui.Component.State.FOCUSED;
goog.ui.Control.prototype.autoStates_ = goog.ui.Component.State.ALL;
goog.ui.Control.prototype.statesWithTransitionEvents_ = 0;
goog.ui.Control.prototype.visible_ = !0;
goog.ui.Control.prototype.extraClassNames_ = null;
goog.ui.Control.prototype.handleMouseEvents_ = !0;
goog.ui.Control.prototype.allowTextSelection_ = !1;
goog.ui.Control.prototype.preferredAriaRole_ = null;
goog.ui.Control.prototype.isHandleMouseEvents = function () {
    return this.handleMouseEvents_
};
goog.ui.Control.prototype.setHandleMouseEvents = function (a) {
    this.isInDocument() && a != this.handleMouseEvents_ && this.enableMouseEventHandling_(a);
    this.handleMouseEvents_ = a
};
goog.ui.Control.prototype.getKeyEventTarget = function () {
    return this.renderer_.getKeyEventTarget(this)
};
goog.ui.Control.prototype.getKeyHandler = function () {
    return this.keyHandler_ || (this.keyHandler_ = new goog.events.KeyHandler)
};
goog.ui.Control.prototype.getRenderer = function () {
    return this.renderer_
};
goog.ui.Control.prototype.setRenderer = function (a) {
    if (this.isInDocument())throw Error(goog.ui.Component.Error.ALREADY_RENDERED);
    this.getElement() && this.setElementInternal(null);
    this.renderer_ = a
};
goog.ui.Control.prototype.getExtraClassNames = function () {
    return this.extraClassNames_
};
goog.ui.Control.prototype.addClassName = function (a) {
    a && (this.extraClassNames_ ? goog.array.contains(this.extraClassNames_, a) || this.extraClassNames_.push(a) : this.extraClassNames_ = [a], this.renderer_.enableExtraClassName(this, a, !0))
};
goog.ui.Control.prototype.removeClassName = function (a) {
    a && (this.extraClassNames_ && goog.array.remove(this.extraClassNames_, a)) && (0 == this.extraClassNames_.length && (this.extraClassNames_ = null), this.renderer_.enableExtraClassName(this, a, !1))
};
goog.ui.Control.prototype.enableClassName = function (a, b) {
    b ? this.addClassName(a) : this.removeClassName(a)
};
goog.ui.Control.prototype.createDom = function () {
    var a = this.renderer_.createDom(this);
    this.setElementInternal(a);
    this.renderer_.setAriaRole(a, this.getPreferredAriaRole());
    this.isAllowTextSelection() || this.renderer_.setAllowTextSelection(a, !1);
    this.isVisible() || this.renderer_.setVisible(a, !1)
};
goog.ui.Control.prototype.getPreferredAriaRole = function () {
    return this.preferredAriaRole_
};
goog.ui.Control.prototype.setPreferredAriaRole = function (a) {
    this.preferredAriaRole_ = a
};
goog.ui.Control.prototype.getContentElement = function () {
    return this.renderer_.getContentElement(this.getElement())
};
goog.ui.Control.prototype.canDecorate = function (a) {
    return this.renderer_.canDecorate(a)
};
goog.ui.Control.prototype.decorateInternal = function (a) {
    a = this.renderer_.decorate(this, a);
    this.setElementInternal(a);
    this.renderer_.setAriaRole(a, this.getPreferredAriaRole());
    this.isAllowTextSelection() || this.renderer_.setAllowTextSelection(a, !1);
    this.visible_ = "none" != a.style.display
};
goog.ui.Control.prototype.enterDocument = function () {
    goog.ui.Control.superClass_.enterDocument.call(this);
    this.renderer_.initializeDom(this);
    if (this.supportedStates_ & ~goog.ui.Component.State.DISABLED && (this.isHandleMouseEvents() && this.enableMouseEventHandling_(!0), this.isSupportedState(goog.ui.Component.State.FOCUSED))) {
        var a = this.getKeyEventTarget();
        if (a) {
            var b = this.getKeyHandler();
            b.attach(a);
            this.getHandler().listen(b, goog.events.KeyHandler.EventType.KEY, this.handleKeyEvent).listen(a, goog.events.EventType.FOCUS,
                this.handleFocus).listen(a, goog.events.EventType.BLUR, this.handleBlur)
        }
    }
};
goog.ui.Control.prototype.enableMouseEventHandling_ = function (a) {
    var b = this.getHandler(), c = this.getElement();
    a ? (b.listen(c, goog.events.EventType.MOUSEOVER, this.handleMouseOver).listen(c, goog.events.EventType.MOUSEDOWN, this.handleMouseDown).listen(c, goog.events.EventType.MOUSEUP, this.handleMouseUp).listen(c, goog.events.EventType.MOUSEOUT, this.handleMouseOut), this.handleContextMenu != goog.nullFunction && b.listen(c, goog.events.EventType.CONTEXTMENU, this.handleContextMenu), goog.userAgent.IE && b.listen(c,
        goog.events.EventType.DBLCLICK, this.handleDblClick)) : (b.unlisten(c, goog.events.EventType.MOUSEOVER, this.handleMouseOver).unlisten(c, goog.events.EventType.MOUSEDOWN, this.handleMouseDown).unlisten(c, goog.events.EventType.MOUSEUP, this.handleMouseUp).unlisten(c, goog.events.EventType.MOUSEOUT, this.handleMouseOut), this.handleContextMenu != goog.nullFunction && b.unlisten(c, goog.events.EventType.CONTEXTMENU, this.handleContextMenu), goog.userAgent.IE && b.unlisten(c, goog.events.EventType.DBLCLICK, this.handleDblClick))
};
goog.ui.Control.prototype.exitDocument = function () {
    goog.ui.Control.superClass_.exitDocument.call(this);
    this.keyHandler_ && this.keyHandler_.detach();
    this.isVisible() && this.isEnabled() && this.renderer_.setFocusable(this, !1)
};
goog.ui.Control.prototype.disposeInternal = function () {
    goog.ui.Control.superClass_.disposeInternal.call(this);
    this.keyHandler_ && (this.keyHandler_.dispose(), delete this.keyHandler_);
    delete this.renderer_;
    this.extraClassNames_ = this.content_ = null
};
goog.ui.Control.prototype.getContent = function () {
    return this.content_
};
goog.ui.Control.prototype.setContent = function (a) {
    this.renderer_.setContent(this.getElement(), a);
    this.setContentInternal(a)
};
goog.ui.Control.prototype.setContentInternal = function (a) {
    this.content_ = a
};
goog.ui.Control.prototype.getCaption = function () {
    var a = this.getContent();
    if (!a)return"";
    a = goog.isString(a) ? a : goog.isArray(a) ? goog.array.map(a, goog.dom.getRawTextContent).join("") : goog.dom.getTextContent(a);
    return goog.string.collapseBreakingSpaces(a)
};
goog.ui.Control.prototype.setCaption = function (a) {
    this.setContent(a)
};
goog.ui.Control.prototype.setRightToLeft = function (a) {
    goog.ui.Control.superClass_.setRightToLeft.call(this, a);
    var b = this.getElement();
    b && this.renderer_.setRightToLeft(b, a)
};
goog.ui.Control.prototype.isAllowTextSelection = function () {
    return this.allowTextSelection_
};
goog.ui.Control.prototype.setAllowTextSelection = function (a) {
    this.allowTextSelection_ = a;
    var b = this.getElement();
    b && this.renderer_.setAllowTextSelection(b, a)
};
goog.ui.Control.prototype.isVisible = function () {
    return this.visible_
};
goog.ui.Control.prototype.setVisible = function (a, b) {
    if (b || this.visible_ != a && this.dispatchEvent(a ? goog.ui.Component.EventType.SHOW : goog.ui.Component.EventType.HIDE)) {
        var c = this.getElement();
        c && this.renderer_.setVisible(c, a);
        this.isEnabled() && this.renderer_.setFocusable(this, a);
        this.visible_ = a;
        return!0
    }
    return!1
};
goog.ui.Control.prototype.isEnabled = function () {
    return!this.hasState(goog.ui.Component.State.DISABLED)
};
goog.ui.Control.prototype.isParentDisabled_ = function () {
    var a = this.getParent();
    return!!a && "function" == typeof a.isEnabled && !a.isEnabled()
};
goog.ui.Control.prototype.setEnabled = function (a) {
    !this.isParentDisabled_() && this.isTransitionAllowed(goog.ui.Component.State.DISABLED, !a) && (a || (this.setActive(!1), this.setHighlighted(!1)), this.isVisible() && this.renderer_.setFocusable(this, a), this.setState(goog.ui.Component.State.DISABLED, !a))
};
goog.ui.Control.prototype.isHighlighted = function () {
    return this.hasState(goog.ui.Component.State.HOVER)
};
goog.ui.Control.prototype.setHighlighted = function (a) {
    this.isTransitionAllowed(goog.ui.Component.State.HOVER, a) && this.setState(goog.ui.Component.State.HOVER, a)
};
goog.ui.Control.prototype.isActive = function () {
    return this.hasState(goog.ui.Component.State.ACTIVE)
};
goog.ui.Control.prototype.setActive = function (a) {
    this.isTransitionAllowed(goog.ui.Component.State.ACTIVE, a) && this.setState(goog.ui.Component.State.ACTIVE, a)
};
goog.ui.Control.prototype.isSelected = function () {
    return this.hasState(goog.ui.Component.State.SELECTED)
};
goog.ui.Control.prototype.setSelected = function (a) {
    this.isTransitionAllowed(goog.ui.Component.State.SELECTED, a) && this.setState(goog.ui.Component.State.SELECTED, a)
};
goog.ui.Control.prototype.isChecked = function () {
    return this.hasState(goog.ui.Component.State.CHECKED)
};
goog.ui.Control.prototype.setChecked = function (a) {
    this.isTransitionAllowed(goog.ui.Component.State.CHECKED, a) && this.setState(goog.ui.Component.State.CHECKED, a)
};
goog.ui.Control.prototype.isFocused = function () {
    return this.hasState(goog.ui.Component.State.FOCUSED)
};
goog.ui.Control.prototype.setFocused = function (a) {
    this.isTransitionAllowed(goog.ui.Component.State.FOCUSED, a) && this.setState(goog.ui.Component.State.FOCUSED, a)
};
goog.ui.Control.prototype.isOpen = function () {
    return this.hasState(goog.ui.Component.State.OPENED)
};
goog.ui.Control.prototype.setOpen = function (a) {
    this.isTransitionAllowed(goog.ui.Component.State.OPENED, a) && this.setState(goog.ui.Component.State.OPENED, a)
};
goog.ui.Control.prototype.getState = function () {
    return this.state_
};
goog.ui.Control.prototype.hasState = function (a) {
    return!!(this.state_ & a)
};
goog.ui.Control.prototype.setState = function (a, b) {
    this.isSupportedState(a) && b != this.hasState(a) && (this.renderer_.setState(this, a, b), this.state_ = b ? this.state_ | a : this.state_ & ~a)
};
goog.ui.Control.prototype.setStateInternal = function (a) {
    this.state_ = a
};
goog.ui.Control.prototype.isSupportedState = function (a) {
    return!!(this.supportedStates_ & a)
};
goog.ui.Control.prototype.setSupportedState = function (a, b) {
    if (this.isInDocument() && this.hasState(a) && !b)throw Error(goog.ui.Component.Error.ALREADY_RENDERED);
    !b && this.hasState(a) && this.setState(a, !1);
    this.supportedStates_ = b ? this.supportedStates_ | a : this.supportedStates_ & ~a
};
goog.ui.Control.prototype.isAutoState = function (a) {
    return!!(this.autoStates_ & a) && this.isSupportedState(a)
};
goog.ui.Control.prototype.setAutoStates = function (a, b) {
    this.autoStates_ = b ? this.autoStates_ | a : this.autoStates_ & ~a
};
goog.ui.Control.prototype.isDispatchTransitionEvents = function (a) {
    return!!(this.statesWithTransitionEvents_ & a) && this.isSupportedState(a)
};
goog.ui.Control.prototype.setDispatchTransitionEvents = function (a, b) {
    this.statesWithTransitionEvents_ = b ? this.statesWithTransitionEvents_ | a : this.statesWithTransitionEvents_ & ~a
};
goog.ui.Control.prototype.isTransitionAllowed = function (a, b) {
    return this.isSupportedState(a) && this.hasState(a) != b && (!(this.statesWithTransitionEvents_ & a) || this.dispatchEvent(goog.ui.Component.getStateTransitionEvent(a, b))) && !this.isDisposed()
};
goog.ui.Control.prototype.handleMouseOver = function (a) {
    !goog.ui.Control.isMouseEventWithinElement_(a, this.getElement()) && (this.dispatchEvent(goog.ui.Component.EventType.ENTER) && this.isEnabled() && this.isAutoState(goog.ui.Component.State.HOVER)) && this.setHighlighted(!0)
};
goog.ui.Control.prototype.handleMouseOut = function (a) {
    !goog.ui.Control.isMouseEventWithinElement_(a, this.getElement()) && this.dispatchEvent(goog.ui.Component.EventType.LEAVE) && (this.isAutoState(goog.ui.Component.State.ACTIVE) && this.setActive(!1), this.isAutoState(goog.ui.Component.State.HOVER) && this.setHighlighted(!1))
};
goog.ui.Control.prototype.handleContextMenu = goog.nullFunction;
goog.ui.Control.isMouseEventWithinElement_ = function (a, b) {
    return!!a.relatedTarget && goog.dom.contains(b, a.relatedTarget)
};
goog.ui.Control.prototype.handleMouseDown = function (a) {
    this.isEnabled() && (this.isAutoState(goog.ui.Component.State.HOVER) && this.setHighlighted(!0), a.isMouseActionButton() && (this.isAutoState(goog.ui.Component.State.ACTIVE) && this.setActive(!0), this.renderer_.isFocusable(this) && this.getKeyEventTarget().focus()));
    !this.isAllowTextSelection() && a.isMouseActionButton() && a.preventDefault()
};
goog.ui.Control.prototype.handleMouseUp = function (a) {
    this.isEnabled() && (this.isAutoState(goog.ui.Component.State.HOVER) && this.setHighlighted(!0), this.isActive() && (this.performActionInternal(a) && this.isAutoState(goog.ui.Component.State.ACTIVE)) && this.setActive(!1))
};
goog.ui.Control.prototype.handleDblClick = function (a) {
    this.isEnabled() && this.performActionInternal(a)
};
goog.ui.Control.prototype.performActionInternal = function (a) {
    this.isAutoState(goog.ui.Component.State.CHECKED) && this.setChecked(!this.isChecked());
    this.isAutoState(goog.ui.Component.State.SELECTED) && this.setSelected(!0);
    this.isAutoState(goog.ui.Component.State.OPENED) && this.setOpen(!this.isOpen());
    var b = new goog.events.Event(goog.ui.Component.EventType.ACTION, this);
    a && (b.altKey = a.altKey, b.ctrlKey = a.ctrlKey, b.metaKey = a.metaKey, b.shiftKey = a.shiftKey, b.platformModifierKey = a.platformModifierKey);
    return this.dispatchEvent(b)
};
goog.ui.Control.prototype.handleFocus = function (a) {
    this.isAutoState(goog.ui.Component.State.FOCUSED) && this.setFocused(!0)
};
goog.ui.Control.prototype.handleBlur = function (a) {
    this.isAutoState(goog.ui.Component.State.ACTIVE) && this.setActive(!1);
    this.isAutoState(goog.ui.Component.State.FOCUSED) && this.setFocused(!1)
};
goog.ui.Control.prototype.handleKeyEvent = function (a) {
    return this.isVisible() && this.isEnabled() && this.handleKeyEventInternal(a) ? (a.preventDefault(), a.stopPropagation(), !0) : !1
};
goog.ui.Control.prototype.handleKeyEventInternal = function (a) {
    return a.keyCode == goog.events.KeyCodes.ENTER && this.performActionInternal(a)
};
goog.ui.registry.setDefaultRenderer(goog.ui.Control, goog.ui.ControlRenderer);
goog.ui.registry.setDecoratorByClassName(goog.ui.ControlRenderer.CSS_CLASS, function () {
    return new goog.ui.Control(null)
});
goog.ui.Button = function (a, b, c) {
    goog.ui.Control.call(this, a, b || goog.ui.NativeButtonRenderer.getInstance(), c)
};
goog.inherits(goog.ui.Button, goog.ui.Control);
goog.ui.Button.Side = goog.ui.ButtonSide;
goog.ui.Button.prototype.getValue = function () {
    return this.value_
};
goog.ui.Button.prototype.setValue = function (a) {
    this.value_ = a;
    this.getRenderer().setValue(this.getElement(), a)
};
goog.ui.Button.prototype.setValueInternal = function (a) {
    this.value_ = a
};
goog.ui.Button.prototype.getTooltip = function () {
    return this.tooltip_
};
goog.ui.Button.prototype.setTooltip = function (a) {
    this.tooltip_ = a;
    this.getRenderer().setTooltip(this.getElement(), a)
};
goog.ui.Button.prototype.setTooltipInternal = function (a) {
    this.tooltip_ = a
};
goog.ui.Button.prototype.setCollapsed = function (a) {
    this.getRenderer().setCollapsed(this, a)
};
goog.ui.Button.prototype.disposeInternal = function () {
    goog.ui.Button.superClass_.disposeInternal.call(this);
    delete this.value_;
    delete this.tooltip_
};
goog.ui.Button.prototype.enterDocument = function () {
    goog.ui.Button.superClass_.enterDocument.call(this);
    if (this.isSupportedState(goog.ui.Component.State.FOCUSED)) {
        var a = this.getKeyEventTarget();
        a && this.getHandler().listen(a, goog.events.EventType.KEYUP, this.handleKeyEventInternal)
    }
};
goog.ui.Button.prototype.handleKeyEventInternal = function (a) {
    return a.keyCode == goog.events.KeyCodes.ENTER && a.type == goog.events.KeyHandler.EventType.KEY || a.keyCode == goog.events.KeyCodes.SPACE && a.type == goog.events.EventType.KEYUP ? this.performActionInternal(a) : a.keyCode == goog.events.KeyCodes.SPACE
};
goog.ui.registry.setDecoratorByClassName(goog.ui.ButtonRenderer.CSS_CLASS, function () {
    return new goog.ui.Button(null)
});
goog.ui.INLINE_BLOCK_CLASSNAME = "goog-inline-block";
goog.ui.FlatButtonRenderer = function () {
    goog.ui.ButtonRenderer.call(this)
};
goog.inherits(goog.ui.FlatButtonRenderer, goog.ui.ButtonRenderer);
goog.addSingletonGetter(goog.ui.FlatButtonRenderer);
goog.ui.FlatButtonRenderer.CSS_CLASS = "goog-flat-button";
goog.ui.FlatButtonRenderer.prototype.createDom = function (a) {
    var b = this.getClassNames(a), b = {"class": goog.ui.INLINE_BLOCK_CLASSNAME + " " + b.join(" ")}, b = a.getDomHelper().createDom("div", b, a.getContent());
    this.setTooltip(b, a.getTooltip());
    this.setAriaStates(a, b);
    return b
};
goog.ui.FlatButtonRenderer.prototype.getAriaRole = function () {
    return goog.a11y.aria.Role.BUTTON
};
goog.ui.FlatButtonRenderer.prototype.canDecorate = function (a) {
    return"DIV" == a.tagName
};
goog.ui.FlatButtonRenderer.prototype.decorate = function (a, b) {
    goog.dom.classes.add(b, goog.ui.INLINE_BLOCK_CLASSNAME);
    return goog.ui.FlatButtonRenderer.superClass_.decorate.call(this, a, b)
};
goog.ui.FlatButtonRenderer.prototype.getValue = function (a) {
    return""
};
goog.ui.FlatButtonRenderer.prototype.getCssClass = function () {
    return goog.ui.FlatButtonRenderer.CSS_CLASS
};
goog.ui.registry.setDecoratorByClassName(goog.ui.FlatButtonRenderer.CSS_CLASS, function () {
    return new goog.ui.Button(null, goog.ui.FlatButtonRenderer.getInstance())
});
goog.ui.LinkButtonRenderer = function () {
    goog.ui.FlatButtonRenderer.call(this)
};
goog.inherits(goog.ui.LinkButtonRenderer, goog.ui.FlatButtonRenderer);
goog.addSingletonGetter(goog.ui.LinkButtonRenderer);
goog.ui.LinkButtonRenderer.CSS_CLASS = "goog-link-button";
goog.ui.LinkButtonRenderer.prototype.getCssClass = function () {
    return goog.ui.LinkButtonRenderer.CSS_CLASS
};
goog.ui.registry.setDecoratorByClassName(goog.ui.LinkButtonRenderer.CSS_CLASS, function () {
    return new goog.ui.Button(null, goog.ui.LinkButtonRenderer.getInstance())
});
goog.editor = {};
goog.editor.defines = {};
goog.editor.defines.USE_CONTENTEDITABLE_IN_FIREFOX_3 = !1;
goog.userAgent.product = {};
goog.userAgent.product.ASSUME_FIREFOX = !1;
goog.userAgent.product.ASSUME_CAMINO = !1;
goog.userAgent.product.ASSUME_IPHONE = !1;
goog.userAgent.product.ASSUME_IPAD = !1;
goog.userAgent.product.ASSUME_ANDROID = !1;
goog.userAgent.product.ASSUME_CHROME = !1;
goog.userAgent.product.ASSUME_SAFARI = !1;
goog.userAgent.product.PRODUCT_KNOWN_ = goog.userAgent.ASSUME_IE || goog.userAgent.ASSUME_OPERA || goog.userAgent.product.ASSUME_FIREFOX || goog.userAgent.product.ASSUME_CAMINO || goog.userAgent.product.ASSUME_IPHONE || goog.userAgent.product.ASSUME_IPAD || goog.userAgent.product.ASSUME_ANDROID || goog.userAgent.product.ASSUME_CHROME || goog.userAgent.product.ASSUME_SAFARI;
goog.userAgent.product.init_ = function () {
    goog.userAgent.product.detectedFirefox_ = !1;
    goog.userAgent.product.detectedCamino_ = !1;
    goog.userAgent.product.detectedIphone_ = !1;
    goog.userAgent.product.detectedIpad_ = !1;
    goog.userAgent.product.detectedAndroid_ = !1;
    goog.userAgent.product.detectedChrome_ = !1;
    goog.userAgent.product.detectedSafari_ = !1;
    var a = goog.userAgent.getUserAgentString();
    a && (-1 != a.indexOf("Firefox") ? goog.userAgent.product.detectedFirefox_ = !0 : -1 != a.indexOf("Camino") ? goog.userAgent.product.detectedCamino_ = !0 : -1 != a.indexOf("iPhone") || -1 != a.indexOf("iPod") ? goog.userAgent.product.detectedIphone_ = !0 : -1 != a.indexOf("iPad") ? goog.userAgent.product.detectedIpad_ = !0 : -1 != a.indexOf("Android") ? goog.userAgent.product.detectedAndroid_ = !0 : -1 != a.indexOf("Chrome") ? goog.userAgent.product.detectedChrome_ = !0 : -1 != a.indexOf("Safari") && (goog.userAgent.product.detectedSafari_ = !0))
};
goog.userAgent.product.PRODUCT_KNOWN_ || goog.userAgent.product.init_();
goog.userAgent.product.OPERA = goog.userAgent.OPERA;
goog.userAgent.product.IE = goog.userAgent.IE;
goog.userAgent.product.FIREFOX = goog.userAgent.product.PRODUCT_KNOWN_ ? goog.userAgent.product.ASSUME_FIREFOX : goog.userAgent.product.detectedFirefox_;
goog.userAgent.product.CAMINO = goog.userAgent.product.PRODUCT_KNOWN_ ? goog.userAgent.product.ASSUME_CAMINO : goog.userAgent.product.detectedCamino_;
goog.userAgent.product.IPHONE = goog.userAgent.product.PRODUCT_KNOWN_ ? goog.userAgent.product.ASSUME_IPHONE : goog.userAgent.product.detectedIphone_;
goog.userAgent.product.IPAD = goog.userAgent.product.PRODUCT_KNOWN_ ? goog.userAgent.product.ASSUME_IPAD : goog.userAgent.product.detectedIpad_;
goog.userAgent.product.ANDROID = goog.userAgent.product.PRODUCT_KNOWN_ ? goog.userAgent.product.ASSUME_ANDROID : goog.userAgent.product.detectedAndroid_;
goog.userAgent.product.CHROME = goog.userAgent.product.PRODUCT_KNOWN_ ? goog.userAgent.product.ASSUME_CHROME : goog.userAgent.product.detectedChrome_;
goog.userAgent.product.SAFARI = goog.userAgent.product.PRODUCT_KNOWN_ ? goog.userAgent.product.ASSUME_SAFARI : goog.userAgent.product.detectedSafari_;
goog.userAgent.product.determineVersion_ = function () {
    if (goog.userAgent.product.FIREFOX)return goog.userAgent.product.getFirstRegExpGroup_(/Firefox\/([0-9.]+)/);
    if (goog.userAgent.product.IE || goog.userAgent.product.OPERA)return goog.userAgent.VERSION;
    if (goog.userAgent.product.CHROME)return goog.userAgent.product.getFirstRegExpGroup_(/Chrome\/([0-9.]+)/);
    if (goog.userAgent.product.SAFARI)return goog.userAgent.product.getFirstRegExpGroup_(/Version\/([0-9.]+)/);
    if (goog.userAgent.product.IPHONE || goog.userAgent.product.IPAD) {
        var a =
            goog.userAgent.product.execRegExp_(/Version\/(\S+).*Mobile\/(\S+)/);
        if (a)return a[1] + "." + a[2]
    } else {
        if (goog.userAgent.product.ANDROID)return(a = goog.userAgent.product.getFirstRegExpGroup_(/Android\s+([0-9.]+)/)) ? a : goog.userAgent.product.getFirstRegExpGroup_(/Version\/([0-9.]+)/);
        if (goog.userAgent.product.CAMINO)return goog.userAgent.product.getFirstRegExpGroup_(/Camino\/([0-9.]+)/)
    }
    return""
};
goog.userAgent.product.getFirstRegExpGroup_ = function (a) {
    return(a = goog.userAgent.product.execRegExp_(a)) ? a[1] : ""
};
goog.userAgent.product.execRegExp_ = function (a) {
    return a.exec(goog.userAgent.getUserAgentString())
};
goog.userAgent.product.VERSION = goog.userAgent.product.determineVersion_();
goog.userAgent.product.isVersion = function (a) {
    return 0 <= goog.string.compareVersions(goog.userAgent.product.VERSION, a)
};
goog.editor.BrowserFeature = {HAS_IE_RANGES: goog.userAgent.IE && !goog.userAgent.isDocumentModeOrHigher(9), HAS_W3C_RANGES: goog.userAgent.GECKO || goog.userAgent.WEBKIT || goog.userAgent.OPERA || goog.userAgent.IE && goog.userAgent.isDocumentModeOrHigher(9), HAS_CONTENT_EDITABLE: goog.userAgent.IE || goog.userAgent.WEBKIT || goog.userAgent.OPERA || goog.editor.defines.USE_CONTENTEDITABLE_IN_FIREFOX_3 && goog.userAgent.GECKO && goog.userAgent.isVersionOrHigher("1.9"), USE_MUTATION_EVENTS: goog.userAgent.GECKO, HAS_DOM_SUBTREE_MODIFIED_EVENT: goog.userAgent.WEBKIT ||
    goog.editor.defines.USE_CONTENTEDITABLE_IN_FIREFOX_3 && goog.userAgent.GECKO && goog.userAgent.isVersionOrHigher("1.9"), HAS_DOCUMENT_INDEPENDENT_NODES: goog.userAgent.GECKO, PUTS_CURSOR_BEFORE_FIRST_BLOCK_ELEMENT_ON_FOCUS: goog.userAgent.GECKO, CLEARS_SELECTION_WHEN_FOCUS_LEAVES: goog.userAgent.IE || goog.userAgent.WEBKIT || goog.userAgent.OPERA, HAS_UNSELECTABLE_STYLE: goog.userAgent.GECKO || goog.userAgent.WEBKIT, FORMAT_BLOCK_WORKS_FOR_BLOCKQUOTES: goog.userAgent.GECKO || goog.userAgent.WEBKIT || goog.userAgent.OPERA,
    CREATES_MULTIPLE_BLOCKQUOTES: goog.userAgent.WEBKIT && !goog.userAgent.isVersionOrHigher("534.16") || goog.userAgent.OPERA, WRAPS_BLOCKQUOTE_IN_DIVS: goog.userAgent.OPERA, PREFERS_READY_STATE_CHANGE_EVENT: goog.userAgent.IE, TAB_FIRES_KEYPRESS: !goog.userAgent.IE, NEEDS_99_WIDTH_IN_STANDARDS_MODE: goog.userAgent.IE, USE_DOCUMENT_FOR_KEY_EVENTS: goog.userAgent.GECKO && !goog.editor.defines.USE_CONTENTEDITABLE_IN_FIREFOX_3, SHOWS_CUSTOM_ATTRS_IN_INNER_HTML: goog.userAgent.IE, COLLAPSES_EMPTY_NODES: goog.userAgent.GECKO ||
        goog.userAgent.WEBKIT || goog.userAgent.OPERA, CONVERT_TO_B_AND_I_TAGS: goog.userAgent.GECKO || goog.userAgent.OPERA, TABS_THROUGH_IMAGES: goog.userAgent.IE, UNESCAPES_URLS_WITHOUT_ASKING: goog.userAgent.IE && !goog.userAgent.isVersionOrHigher("7.0"), HAS_STYLE_WITH_CSS: goog.userAgent.GECKO && goog.userAgent.isVersionOrHigher("1.8") || goog.userAgent.WEBKIT || goog.userAgent.OPERA, FOLLOWS_EDITABLE_LINKS: goog.userAgent.WEBKIT || goog.userAgent.IE && goog.userAgent.isVersionOrHigher("9"), HAS_ACTIVE_ELEMENT: goog.userAgent.IE ||
        goog.userAgent.OPERA || goog.userAgent.GECKO && goog.userAgent.isVersionOrHigher("1.9"), HAS_SET_CAPTURE: goog.userAgent.IE, EATS_EMPTY_BACKGROUND_COLOR: goog.userAgent.GECKO || goog.userAgent.WEBKIT && !goog.userAgent.isVersionOrHigher("527"), SUPPORTS_FOCUSIN: goog.userAgent.IE || goog.userAgent.OPERA, SELECTS_IMAGES_ON_CLICK: goog.userAgent.IE || goog.userAgent.OPERA, MOVES_STYLE_TO_HEAD: goog.userAgent.WEBKIT, COLLAPSES_SELECTION_ONMOUSEDOWN: !1, CARET_INSIDE_SELECTION: goog.userAgent.OPERA, FOCUSES_EDITABLE_BODY_ON_HTML_CLICK: !0,
    USES_KEYDOWN: goog.userAgent.IE || goog.userAgent.WEBKIT && goog.userAgent.isVersionOrHigher("525"), ADDS_NBSPS_IN_REMOVE_FORMAT: goog.userAgent.WEBKIT && !goog.userAgent.isVersionOrHigher("531"), GETS_STUCK_IN_LINKS: goog.userAgent.WEBKIT && !goog.userAgent.isVersionOrHigher("528"), NORMALIZE_CORRUPTS_EMPTY_TEXT_NODES: goog.userAgent.GECKO && goog.userAgent.isVersionOrHigher("1.9") || goog.userAgent.IE || goog.userAgent.OPERA || goog.userAgent.WEBKIT && goog.userAgent.isVersionOrHigher("531"), NORMALIZE_CORRUPTS_ALL_TEXT_NODES: goog.userAgent.IE,
    NESTS_SUBSCRIPT_SUPERSCRIPT: goog.userAgent.IE || goog.userAgent.GECKO || goog.userAgent.OPERA, CAN_SELECT_EMPTY_ELEMENT: !goog.userAgent.IE && !goog.userAgent.WEBKIT, FORGETS_FORMATTING_WHEN_LISTIFYING: goog.userAgent.GECKO || goog.userAgent.WEBKIT && !goog.userAgent.isVersionOrHigher("526"), LEAVES_P_WHEN_REMOVING_LISTS: goog.userAgent.IE || goog.userAgent.OPERA, CAN_LISTIFY_BR: !goog.userAgent.IE && !goog.userAgent.OPERA, DOESNT_OVERRIDE_FONT_SIZE_IN_STYLE_ATTR: !goog.userAgent.WEBKIT, SUPPORTS_HTML5_FILE_DRAGGING: goog.userAgent.product.CHROME &&
        goog.userAgent.product.isVersion("4") || goog.userAgent.product.SAFARI && goog.userAgent.isVersionOrHigher("533") || goog.userAgent.GECKO && goog.userAgent.isVersionOrHigher("2.0") || goog.userAgent.IE && goog.userAgent.isVersionOrHigher("10"), SUPPORTS_OPERA_DEFAULTBLOCK_COMMAND: goog.userAgent.OPERA && goog.userAgent.isVersionOrHigher("11.10"), SUPPORTS_FILE_PASTING: goog.userAgent.product.CHROME && goog.userAgent.product.isVersion("12")};
goog.ui.CustomButtonRenderer = function () {
    goog.ui.ButtonRenderer.call(this)
};
goog.inherits(goog.ui.CustomButtonRenderer, goog.ui.ButtonRenderer);
goog.addSingletonGetter(goog.ui.CustomButtonRenderer);
goog.ui.CustomButtonRenderer.CSS_CLASS = "goog-custom-button";
goog.ui.CustomButtonRenderer.prototype.createDom = function (a) {
    var b = this.getClassNames(a), b = {"class": goog.ui.INLINE_BLOCK_CLASSNAME + " " + b.join(" ")}, b = a.getDomHelper().createDom("div", b, this.createButton(a.getContent(), a.getDomHelper()));
    this.setTooltip(b, a.getTooltip());
    this.setAriaStates(a, b);
    return b
};
goog.ui.CustomButtonRenderer.prototype.getAriaRole = function () {
    return goog.a11y.aria.Role.BUTTON
};
goog.ui.CustomButtonRenderer.prototype.getContentElement = function (a) {
    return a && a.firstChild.firstChild
};
goog.ui.CustomButtonRenderer.prototype.createButton = function (a, b) {
    return b.createDom("div", goog.ui.INLINE_BLOCK_CLASSNAME + " " + (this.getCssClass() + "-outer-box"), b.createDom("div", goog.ui.INLINE_BLOCK_CLASSNAME + " " + (this.getCssClass() + "-inner-box"), a))
};
goog.ui.CustomButtonRenderer.prototype.canDecorate = function (a) {
    return"DIV" == a.tagName
};
goog.ui.CustomButtonRenderer.prototype.hasBoxStructure = function (a, b) {
    var c = a.getDomHelper().getFirstElementChild(b), d = this.getCssClass() + "-outer-box";
    return c && goog.dom.classes.has(c, d) && (c = a.getDomHelper().getFirstElementChild(c), d = this.getCssClass() + "-inner-box", c && goog.dom.classes.has(c, d)) ? !0 : !1
};
goog.ui.CustomButtonRenderer.prototype.decorate = function (a, b) {
    goog.ui.CustomButtonRenderer.trimTextNodes_(b, !0);
    goog.ui.CustomButtonRenderer.trimTextNodes_(b, !1);
    this.hasBoxStructure(a, b) || b.appendChild(this.createButton(b.childNodes, a.getDomHelper()));
    goog.dom.classes.add(b, goog.ui.INLINE_BLOCK_CLASSNAME, this.getCssClass());
    return goog.ui.CustomButtonRenderer.superClass_.decorate.call(this, a, b)
};
goog.ui.CustomButtonRenderer.prototype.getCssClass = function () {
    return goog.ui.CustomButtonRenderer.CSS_CLASS
};
goog.ui.CustomButtonRenderer.trimTextNodes_ = function (a, b) {
    if (a)for (var c = b ? a.firstChild : a.lastChild, d; c && c.parentNode == a;) {
        d = b ? c.nextSibling : c.previousSibling;
        if (c.nodeType == goog.dom.NodeType.TEXT) {
            var e = c.nodeValue;
            if ("" == goog.string.trim(e))a.removeChild(c); else {
                c.nodeValue = b ? goog.string.trimLeft(e) : goog.string.trimRight(e);
                break
            }
        } else break;
        c = d
    }
};
goog.ui.CustomButton = function (a, b, c) {
    goog.ui.Button.call(this, a, b || goog.ui.CustomButtonRenderer.getInstance(), c)
};
goog.inherits(goog.ui.CustomButton, goog.ui.Button);
goog.ui.registry.setDecoratorByClassName(goog.ui.CustomButtonRenderer.CSS_CLASS, function () {
    return new goog.ui.CustomButton(null)
});
goog.uri = {};
goog.uri.utils = {};
goog.uri.utils.CharCode_ = {AMPERSAND: 38, EQUAL: 61, HASH: 35, QUESTION: 63};
goog.uri.utils.buildFromEncodedParts = function (a, b, c, d, e, f, g) {
    var h = "";
    a && (h += a + ":");
    c && (h += "//", b && (h += b + "@"), h += c, d && (h += ":" + d));
    e && (h += e);
    f && (h += "?" + f);
    g && (h += "#" + g);
    return h
};
goog.uri.utils.splitRe_ = RegExp("^(?:([^:/?#.]+):)?(?://(?:([^/?#]*)@)?([^/#?]*?)(?::([0-9]+))?(?=[/#?]|$))?([^?#]+)?(?:\\?([^#]*))?(?:#(.*))?$");
goog.uri.utils.ComponentIndex = {SCHEME: 1, USER_INFO: 2, DOMAIN: 3, PORT: 4, PATH: 5, QUERY_DATA: 6, FRAGMENT: 7};
goog.uri.utils.split = function (a) {
    goog.uri.utils.phishingProtection_();
    return a.match(goog.uri.utils.splitRe_)
};
goog.uri.utils.needsPhishingProtection_ = goog.userAgent.WEBKIT;
goog.uri.utils.phishingProtection_ = function () {
    if (goog.uri.utils.needsPhishingProtection_) {
        goog.uri.utils.needsPhishingProtection_ = !1;
        var a = goog.global.location;
        if (a) {
            var b = a.href;
            if (b && (b = goog.uri.utils.getDomain(b)) && b != a.hostname)throw goog.uri.utils.needsPhishingProtection_ = !0, Error();
        }
    }
};
goog.uri.utils.decodeIfPossible_ = function (a) {
    return a && decodeURIComponent(a)
};
goog.uri.utils.getComponentByIndex_ = function (a, b) {
    return goog.uri.utils.split(b)[a] || null
};
goog.uri.utils.getScheme = function (a) {
    return goog.uri.utils.getComponentByIndex_(goog.uri.utils.ComponentIndex.SCHEME, a)
};
goog.uri.utils.getEffectiveScheme = function (a) {
    a = goog.uri.utils.getScheme(a);
    !a && self.location && (a = self.location.protocol, a = a.substr(0, a.length - 1));
    return a ? a.toLowerCase() : ""
};
goog.uri.utils.getUserInfoEncoded = function (a) {
    return goog.uri.utils.getComponentByIndex_(goog.uri.utils.ComponentIndex.USER_INFO, a)
};
goog.uri.utils.getUserInfo = function (a) {
    return goog.uri.utils.decodeIfPossible_(goog.uri.utils.getUserInfoEncoded(a))
};
goog.uri.utils.getDomainEncoded = function (a) {
    return goog.uri.utils.getComponentByIndex_(goog.uri.utils.ComponentIndex.DOMAIN, a)
};
goog.uri.utils.getDomain = function (a) {
    return goog.uri.utils.decodeIfPossible_(goog.uri.utils.getDomainEncoded(a))
};
goog.uri.utils.getPort = function (a) {
    return Number(goog.uri.utils.getComponentByIndex_(goog.uri.utils.ComponentIndex.PORT, a)) || null
};
goog.uri.utils.getPathEncoded = function (a) {
    return goog.uri.utils.getComponentByIndex_(goog.uri.utils.ComponentIndex.PATH, a)
};
goog.uri.utils.getPath = function (a) {
    return goog.uri.utils.decodeIfPossible_(goog.uri.utils.getPathEncoded(a))
};
goog.uri.utils.getQueryData = function (a) {
    return goog.uri.utils.getComponentByIndex_(goog.uri.utils.ComponentIndex.QUERY_DATA, a)
};
goog.uri.utils.getFragmentEncoded = function (a) {
    var b = a.indexOf("#");
    return 0 > b ? null : a.substr(b + 1)
};
goog.uri.utils.setFragmentEncoded = function (a, b) {
    return goog.uri.utils.removeFragment(a) + (b ? "#" + b : "")
};
goog.uri.utils.getFragment = function (a) {
    return goog.uri.utils.decodeIfPossible_(goog.uri.utils.getFragmentEncoded(a))
};
goog.uri.utils.getHost = function (a) {
    a = goog.uri.utils.split(a);
    return goog.uri.utils.buildFromEncodedParts(a[goog.uri.utils.ComponentIndex.SCHEME], a[goog.uri.utils.ComponentIndex.USER_INFO], a[goog.uri.utils.ComponentIndex.DOMAIN], a[goog.uri.utils.ComponentIndex.PORT])
};
goog.uri.utils.getPathAndAfter = function (a) {
    a = goog.uri.utils.split(a);
    return goog.uri.utils.buildFromEncodedParts(null, null, null, null, a[goog.uri.utils.ComponentIndex.PATH], a[goog.uri.utils.ComponentIndex.QUERY_DATA], a[goog.uri.utils.ComponentIndex.FRAGMENT])
};
goog.uri.utils.removeFragment = function (a) {
    var b = a.indexOf("#");
    return 0 > b ? a : a.substr(0, b)
};
goog.uri.utils.haveSameDomain = function (a, b) {
    var c = goog.uri.utils.split(a), d = goog.uri.utils.split(b);
    return c[goog.uri.utils.ComponentIndex.DOMAIN] == d[goog.uri.utils.ComponentIndex.DOMAIN] && c[goog.uri.utils.ComponentIndex.SCHEME] == d[goog.uri.utils.ComponentIndex.SCHEME] && c[goog.uri.utils.ComponentIndex.PORT] == d[goog.uri.utils.ComponentIndex.PORT]
};
goog.uri.utils.assertNoFragmentsOrQueries_ = function (a) {
    if (goog.DEBUG && (0 <= a.indexOf("#") || 0 <= a.indexOf("?")))throw Error("goog.uri.utils: Fragment or query identifiers are not supported: [" + a + "]");
};
goog.uri.utils.appendQueryData_ = function (a) {
    if (a[1]) {
        var b = a[0], c = b.indexOf("#");
        0 <= c && (a.push(b.substr(c)), a[0] = b = b.substr(0, c));
        c = b.indexOf("?");
        0 > c ? a[1] = "?" : c == b.length - 1 && (a[1] = void 0)
    }
    return a.join("")
};
goog.uri.utils.appendKeyValuePairs_ = function (a, b, c) {
    if (goog.isArray(b)) {
        goog.asserts.assertArray(b);
        for (var d = 0; d < b.length; d++)goog.uri.utils.appendKeyValuePairs_(a, String(b[d]), c)
    } else null != b && c.push("&", a, "" === b ? "" : "=", goog.string.urlEncode(b))
};
goog.uri.utils.buildQueryDataBuffer_ = function (a, b, c) {
    goog.asserts.assert(0 == Math.max(b.length - (c || 0), 0) % 2, "goog.uri.utils: Key/value lists must be even in length.");
    for (c = c || 0; c < b.length; c += 2)goog.uri.utils.appendKeyValuePairs_(b[c], b[c + 1], a);
    return a
};
goog.uri.utils.buildQueryData = function (a, b) {
    var c = goog.uri.utils.buildQueryDataBuffer_([], a, b);
    c[0] = "";
    return c.join("")
};
goog.uri.utils.buildQueryDataBufferFromMap_ = function (a, b) {
    for (var c in b)goog.uri.utils.appendKeyValuePairs_(c, b[c], a);
    return a
};
goog.uri.utils.buildQueryDataFromMap = function (a) {
    a = goog.uri.utils.buildQueryDataBufferFromMap_([], a);
    a[0] = "";
    return a.join("")
};
goog.uri.utils.appendParams = function (a, b) {
    return goog.uri.utils.appendQueryData_(2 == arguments.length ? goog.uri.utils.buildQueryDataBuffer_([a], arguments[1], 0) : goog.uri.utils.buildQueryDataBuffer_([a], arguments, 1))
};
goog.uri.utils.appendParamsFromMap = function (a, b) {
    return goog.uri.utils.appendQueryData_(goog.uri.utils.buildQueryDataBufferFromMap_([a], b))
};
goog.uri.utils.appendParam = function (a, b, c) {
    a = [a, "&", b];
    goog.isDefAndNotNull(c) && a.push("=", goog.string.urlEncode(c));
    return goog.uri.utils.appendQueryData_(a)
};
goog.uri.utils.findParam_ = function (a, b, c, d) {
    for (var e = c.length; 0 <= (b = a.indexOf(c, b)) && b < d;) {
        var f = a.charCodeAt(b - 1);
        if (f == goog.uri.utils.CharCode_.AMPERSAND || f == goog.uri.utils.CharCode_.QUESTION)if (f = a.charCodeAt(b + e), !f || f == goog.uri.utils.CharCode_.EQUAL || f == goog.uri.utils.CharCode_.AMPERSAND || f == goog.uri.utils.CharCode_.HASH)return b;
        b += e + 1
    }
    return-1
};
goog.uri.utils.hashOrEndRe_ = /#|$/;
goog.uri.utils.hasParam = function (a, b) {
    return 0 <= goog.uri.utils.findParam_(a, 0, b, a.search(goog.uri.utils.hashOrEndRe_))
};
goog.uri.utils.getParamValue = function (a, b) {
    var c = a.search(goog.uri.utils.hashOrEndRe_), d = goog.uri.utils.findParam_(a, 0, b, c);
    if (0 > d)return null;
    var e = a.indexOf("&", d);
    if (0 > e || e > c)e = c;
    d += b.length + 1;
    return goog.string.urlDecode(a.substr(d, e - d))
};
goog.uri.utils.getParamValues = function (a, b) {
    for (var c = a.search(goog.uri.utils.hashOrEndRe_), d = 0, e, f = []; 0 <= (e = goog.uri.utils.findParam_(a, d, b, c));) {
        d = a.indexOf("&", e);
        if (0 > d || d > c)d = c;
        e += b.length + 1;
        f.push(goog.string.urlDecode(a.substr(e, d - e)))
    }
    return f
};
goog.uri.utils.trailingQueryPunctuationRe_ = /[?&]($|#)/;
goog.uri.utils.removeParam = function (a, b) {
    for (var c = a.search(goog.uri.utils.hashOrEndRe_), d = 0, e, f = []; 0 <= (e = goog.uri.utils.findParam_(a, d, b, c));)f.push(a.substring(d, e)), d = Math.min(a.indexOf("&", e) + 1 || c, c);
    f.push(a.substr(d));
    return f.join("").replace(goog.uri.utils.trailingQueryPunctuationRe_, "$1")
};
goog.uri.utils.setParam = function (a, b, c) {
    return goog.uri.utils.appendParam(goog.uri.utils.removeParam(a, b), b, c)
};
goog.uri.utils.appendPath = function (a, b) {
    goog.uri.utils.assertNoFragmentsOrQueries_(a);
    goog.string.endsWith(a, "/") && (a = a.substr(0, a.length - 1));
    goog.string.startsWith(b, "/") && (b = b.substr(1));
    return goog.string.buildString(a, "/", b)
};
goog.uri.utils.StandardQueryParam = {RANDOM: "zx"};
goog.uri.utils.makeUnique = function (a) {
    return goog.uri.utils.setParam(a, goog.uri.utils.StandardQueryParam.RANDOM, goog.string.getRandomString())
};
goog.Uri = function (a, b) {
    var c;
    a instanceof goog.Uri ? (this.ignoreCase_ = goog.isDef(b) ? b : a.getIgnoreCase(), this.setScheme(a.getScheme()), this.setUserInfo(a.getUserInfo()), this.setDomain(a.getDomain()), this.setPort(a.getPort()), this.setPath(a.getPath()), this.setQueryData(a.getQueryData().clone()), this.setFragment(a.getFragment())) : a && (c = goog.uri.utils.split(String(a))) ? (this.ignoreCase_ = !!b, this.setScheme(c[goog.uri.utils.ComponentIndex.SCHEME] || "", !0), this.setUserInfo(c[goog.uri.utils.ComponentIndex.USER_INFO] ||
        "", !0), this.setDomain(c[goog.uri.utils.ComponentIndex.DOMAIN] || "", !0), this.setPort(c[goog.uri.utils.ComponentIndex.PORT]), this.setPath(c[goog.uri.utils.ComponentIndex.PATH] || "", !0), this.setQueryData(c[goog.uri.utils.ComponentIndex.QUERY_DATA] || "", !0), this.setFragment(c[goog.uri.utils.ComponentIndex.FRAGMENT] || "", !0)) : (this.ignoreCase_ = !!b, this.queryData_ = new goog.Uri.QueryData(null, null, this.ignoreCase_))
};
goog.Uri.preserveParameterTypesCompatibilityFlag = !1;
goog.Uri.RANDOM_PARAM = goog.uri.utils.StandardQueryParam.RANDOM;
goog.Uri.prototype.scheme_ = "";
goog.Uri.prototype.userInfo_ = "";
goog.Uri.prototype.domain_ = "";
goog.Uri.prototype.port_ = null;
goog.Uri.prototype.path_ = "";
goog.Uri.prototype.fragment_ = "";
goog.Uri.prototype.isReadOnly_ = !1;
goog.Uri.prototype.ignoreCase_ = !1;
goog.Uri.prototype.toString = function () {
    var a = [], b = this.getScheme();
    b && a.push(goog.Uri.encodeSpecialChars_(b, goog.Uri.reDisallowedInSchemeOrUserInfo_), ":");
    if (b = this.getDomain()) {
        a.push("//");
        var c = this.getUserInfo();
        c && a.push(goog.Uri.encodeSpecialChars_(c, goog.Uri.reDisallowedInSchemeOrUserInfo_), "@");
        a.push(goog.string.urlEncode(b));
        b = this.getPort();
        null != b && a.push(":", String(b))
    }
    if (b = this.getPath())this.hasDomain() && "/" != b.charAt(0) && a.push("/"), a.push(goog.Uri.encodeSpecialChars_(b, "/" == b.charAt(0) ?
        goog.Uri.reDisallowedInAbsolutePath_ : goog.Uri.reDisallowedInRelativePath_));
    (b = this.getEncodedQuery()) && a.push("?", b);
    (b = this.getFragment()) && a.push("#", goog.Uri.encodeSpecialChars_(b, goog.Uri.reDisallowedInFragment_));
    return a.join("")
};
goog.Uri.prototype.resolve = function (a) {
    var b = this.clone(), c = a.hasScheme();
    c ? b.setScheme(a.getScheme()) : c = a.hasUserInfo();
    c ? b.setUserInfo(a.getUserInfo()) : c = a.hasDomain();
    c ? b.setDomain(a.getDomain()) : c = a.hasPort();
    var d = a.getPath();
    if (c)b.setPort(a.getPort()); else if (c = a.hasPath()) {
        if ("/" != d.charAt(0))if (this.hasDomain() && !this.hasPath())d = "/" + d; else {
            var e = b.getPath().lastIndexOf("/");
            -1 != e && (d = b.getPath().substr(0, e + 1) + d)
        }
        d = goog.Uri.removeDotSegments(d)
    }
    c ? b.setPath(d) : c = a.hasQuery();
    c ? b.setQueryData(a.getDecodedQuery()) :
        c = a.hasFragment();
    c && b.setFragment(a.getFragment());
    return b
};
goog.Uri.prototype.clone = function () {
    return new goog.Uri(this)
};
goog.Uri.prototype.getScheme = function () {
    return this.scheme_
};
goog.Uri.prototype.setScheme = function (a, b) {
    this.enforceReadOnly();
    if (this.scheme_ = b ? goog.Uri.decodeOrEmpty_(a) : a)this.scheme_ = this.scheme_.replace(/:$/, "");
    return this
};
goog.Uri.prototype.hasScheme = function () {
    return!!this.scheme_
};
goog.Uri.prototype.getUserInfo = function () {
    return this.userInfo_
};
goog.Uri.prototype.setUserInfo = function (a, b) {
    this.enforceReadOnly();
    this.userInfo_ = b ? goog.Uri.decodeOrEmpty_(a) : a;
    return this
};
goog.Uri.prototype.hasUserInfo = function () {
    return!!this.userInfo_
};
goog.Uri.prototype.getDomain = function () {
    return this.domain_
};
goog.Uri.prototype.setDomain = function (a, b) {
    this.enforceReadOnly();
    this.domain_ = b ? goog.Uri.decodeOrEmpty_(a) : a;
    return this
};
goog.Uri.prototype.hasDomain = function () {
    return!!this.domain_
};
goog.Uri.prototype.getPort = function () {
    return this.port_
};
goog.Uri.prototype.setPort = function (a) {
    this.enforceReadOnly();
    if (a) {
        a = Number(a);
        if (isNaN(a) || 0 > a)throw Error("Bad port number " + a);
        this.port_ = a
    } else this.port_ = null;
    return this
};
goog.Uri.prototype.hasPort = function () {
    return null != this.port_
};
goog.Uri.prototype.getPath = function () {
    return this.path_
};
goog.Uri.prototype.setPath = function (a, b) {
    this.enforceReadOnly();
    this.path_ = b ? goog.Uri.decodeOrEmpty_(a) : a;
    return this
};
goog.Uri.prototype.hasPath = function () {
    return!!this.path_
};
goog.Uri.prototype.hasQuery = function () {
    return"" !== this.queryData_.toString()
};
goog.Uri.prototype.setQueryData = function (a, b) {
    this.enforceReadOnly();
    a instanceof goog.Uri.QueryData ? (this.queryData_ = a, this.queryData_.setIgnoreCase(this.ignoreCase_)) : (b || (a = goog.Uri.encodeSpecialChars_(a, goog.Uri.reDisallowedInQuery_)), this.queryData_ = new goog.Uri.QueryData(a, null, this.ignoreCase_));
    return this
};
goog.Uri.prototype.setQuery = function (a, b) {
    return this.setQueryData(a, b)
};
goog.Uri.prototype.getEncodedQuery = function () {
    return this.queryData_.toString()
};
goog.Uri.prototype.getDecodedQuery = function () {
    return this.queryData_.toDecodedString()
};
goog.Uri.prototype.getQueryData = function () {
    return this.queryData_
};
goog.Uri.prototype.getQuery = function () {
    return this.getEncodedQuery()
};
goog.Uri.prototype.setParameterValue = function (a, b) {
    this.enforceReadOnly();
    this.queryData_.set(a, b);
    return this
};
goog.Uri.prototype.setParameterValues = function (a, b) {
    this.enforceReadOnly();
    goog.isArray(b) || (b = [String(b)]);
    this.queryData_.setValues(a, b);
    return this
};
goog.Uri.prototype.getParameterValues = function (a) {
    return this.queryData_.getValues(a)
};
goog.Uri.prototype.getParameterValue = function (a) {
    return this.queryData_.get(a)
};
goog.Uri.prototype.getFragment = function () {
    return this.fragment_
};
goog.Uri.prototype.setFragment = function (a, b) {
    this.enforceReadOnly();
    this.fragment_ = b ? goog.Uri.decodeOrEmpty_(a) : a;
    return this
};
goog.Uri.prototype.hasFragment = function () {
    return!!this.fragment_
};
goog.Uri.prototype.hasSameDomainAs = function (a) {
    return(!this.hasDomain() && !a.hasDomain() || this.getDomain() == a.getDomain()) && (!this.hasPort() && !a.hasPort() || this.getPort() == a.getPort())
};
goog.Uri.prototype.makeUnique = function () {
    this.enforceReadOnly();
    this.setParameterValue(goog.Uri.RANDOM_PARAM, goog.string.getRandomString());
    return this
};
goog.Uri.prototype.removeParameter = function (a) {
    this.enforceReadOnly();
    this.queryData_.remove(a);
    return this
};
goog.Uri.prototype.setReadOnly = function (a) {
    this.isReadOnly_ = a;
    return this
};
goog.Uri.prototype.isReadOnly = function () {
    return this.isReadOnly_
};
goog.Uri.prototype.enforceReadOnly = function () {
    if (this.isReadOnly_)throw Error("Tried to modify a read-only Uri");
};
goog.Uri.prototype.setIgnoreCase = function (a) {
    this.ignoreCase_ = a;
    this.queryData_ && this.queryData_.setIgnoreCase(a);
    return this
};
goog.Uri.prototype.getIgnoreCase = function () {
    return this.ignoreCase_
};
goog.Uri.parse = function (a, b) {
    return a instanceof goog.Uri ? a.clone() : new goog.Uri(a, b)
};
goog.Uri.create = function (a, b, c, d, e, f, g, h) {
    h = new goog.Uri(null, h);
    a && h.setScheme(a);
    b && h.setUserInfo(b);
    c && h.setDomain(c);
    d && h.setPort(d);
    e && h.setPath(e);
    f && h.setQueryData(f);
    g && h.setFragment(g);
    return h
};
goog.Uri.resolve = function (a, b) {
    a instanceof goog.Uri || (a = goog.Uri.parse(a));
    b instanceof goog.Uri || (b = goog.Uri.parse(b));
    return a.resolve(b)
};
goog.Uri.removeDotSegments = function (a) {
    if (".." == a || "." == a)return"";
    if (goog.string.contains(a, "./") || goog.string.contains(a, "/.")) {
        var b = goog.string.startsWith(a, "/");
        a = a.split("/");
        for (var c = [], d = 0; d < a.length;) {
            var e = a[d++];
            "." == e ? b && d == a.length && c.push("") : ".." == e ? ((1 < c.length || 1 == c.length && "" != c[0]) && c.pop(), b && d == a.length && c.push("")) : (c.push(e), b = !0)
        }
        return c.join("/")
    }
    return a
};
goog.Uri.decodeOrEmpty_ = function (a) {
    return a ? decodeURIComponent(a) : ""
};
goog.Uri.encodeSpecialChars_ = function (a, b) {
    return goog.isString(a) ? encodeURI(a).replace(b, goog.Uri.encodeChar_) : null
};
goog.Uri.encodeChar_ = function (a) {
    a = a.charCodeAt(0);
    return"%" + (a >> 4 & 15).toString(16) + (a & 15).toString(16)
};
goog.Uri.reDisallowedInSchemeOrUserInfo_ = /[#\/\?@]/g;
goog.Uri.reDisallowedInRelativePath_ = /[\#\?:]/g;
goog.Uri.reDisallowedInAbsolutePath_ = /[\#\?]/g;
goog.Uri.reDisallowedInQuery_ = /[\#\?@]/g;
goog.Uri.reDisallowedInFragment_ = /#/g;
goog.Uri.haveSameDomain = function (a, b) {
    var c = goog.uri.utils.split(a), d = goog.uri.utils.split(b);
    return c[goog.uri.utils.ComponentIndex.DOMAIN] == d[goog.uri.utils.ComponentIndex.DOMAIN] && c[goog.uri.utils.ComponentIndex.PORT] == d[goog.uri.utils.ComponentIndex.PORT]
};
goog.Uri.QueryData = function (a, b, c) {
    this.encodedQuery_ = a || null;
    this.ignoreCase_ = !!c
};
goog.Uri.QueryData.prototype.ensureKeyMapInitialized_ = function () {
    if (!this.keyMap_ && (this.keyMap_ = new goog.structs.Map, this.count_ = 0, this.encodedQuery_))for (var a = this.encodedQuery_.split("&"), b = 0; b < a.length; b++) {
        var c = a[b].indexOf("="), d = null, e = null;
        0 <= c ? (d = a[b].substring(0, c), e = a[b].substring(c + 1)) : d = a[b];
        d = goog.string.urlDecode(d);
        d = this.getKeyName_(d);
        this.add(d, e ? goog.string.urlDecode(e) : "")
    }
};
goog.Uri.QueryData.createFromMap = function (a, b, c) {
    b = goog.structs.getKeys(a);
    if ("undefined" == typeof b)throw Error("Keys are undefined");
    c = new goog.Uri.QueryData(null, null, c);
    a = goog.structs.getValues(a);
    for (var d = 0; d < b.length; d++) {
        var e = b[d], f = a[d];
        goog.isArray(f) ? c.setValues(e, f) : c.add(e, f)
    }
    return c
};
goog.Uri.QueryData.createFromKeysValues = function (a, b, c, d) {
    if (a.length != b.length)throw Error("Mismatched lengths for keys/values");
    c = new goog.Uri.QueryData(null, null, d);
    for (d = 0; d < a.length; d++)c.add(a[d], b[d]);
    return c
};
goog.Uri.QueryData.prototype.keyMap_ = null;
goog.Uri.QueryData.prototype.count_ = null;
goog.Uri.QueryData.prototype.getCount = function () {
    this.ensureKeyMapInitialized_();
    return this.count_
};
goog.Uri.QueryData.prototype.add = function (a, b) {
    this.ensureKeyMapInitialized_();
    this.invalidateCache_();
    a = this.getKeyName_(a);
    var c = this.keyMap_.get(a);
    c || this.keyMap_.set(a, c = []);
    c.push(b);
    this.count_++;
    return this
};
goog.Uri.QueryData.prototype.remove = function (a) {
    this.ensureKeyMapInitialized_();
    a = this.getKeyName_(a);
    return this.keyMap_.containsKey(a) ? (this.invalidateCache_(), this.count_ -= this.keyMap_.get(a).length, this.keyMap_.remove(a)) : !1
};
goog.Uri.QueryData.prototype.clear = function () {
    this.invalidateCache_();
    this.keyMap_ = null;
    this.count_ = 0
};
goog.Uri.QueryData.prototype.isEmpty = function () {
    this.ensureKeyMapInitialized_();
    return 0 == this.count_
};
goog.Uri.QueryData.prototype.containsKey = function (a) {
    this.ensureKeyMapInitialized_();
    a = this.getKeyName_(a);
    return this.keyMap_.containsKey(a)
};
goog.Uri.QueryData.prototype.containsValue = function (a) {
    var b = this.getValues();
    return goog.array.contains(b, a)
};
goog.Uri.QueryData.prototype.getKeys = function () {
    this.ensureKeyMapInitialized_();
    for (var a = this.keyMap_.getValues(), b = this.keyMap_.getKeys(), c = [], d = 0; d < b.length; d++)for (var e = a[d], f = 0; f < e.length; f++)c.push(b[d]);
    return c
};
goog.Uri.QueryData.prototype.getValues = function (a) {
    this.ensureKeyMapInitialized_();
    var b = [];
    if (a)this.containsKey(a) && (b = goog.array.concat(b, this.keyMap_.get(this.getKeyName_(a)))); else {
        a = this.keyMap_.getValues();
        for (var c = 0; c < a.length; c++)b = goog.array.concat(b, a[c])
    }
    return b
};
goog.Uri.QueryData.prototype.set = function (a, b) {
    this.ensureKeyMapInitialized_();
    this.invalidateCache_();
    a = this.getKeyName_(a);
    this.containsKey(a) && (this.count_ -= this.keyMap_.get(a).length);
    this.keyMap_.set(a, [b]);
    this.count_++;
    return this
};
goog.Uri.QueryData.prototype.get = function (a, b) {
    var c = a ? this.getValues(a) : [];
    return goog.Uri.preserveParameterTypesCompatibilityFlag ? 0 < c.length ? c[0] : b : 0 < c.length ? String(c[0]) : b
};
goog.Uri.QueryData.prototype.setValues = function (a, b) {
    this.remove(a);
    0 < b.length && (this.invalidateCache_(), this.keyMap_.set(this.getKeyName_(a), goog.array.clone(b)), this.count_ += b.length)
};
goog.Uri.QueryData.prototype.toString = function () {
    if (this.encodedQuery_)return this.encodedQuery_;
    if (!this.keyMap_)return"";
    for (var a = [], b = this.keyMap_.getKeys(), c = 0; c < b.length; c++)for (var d = b[c], e = goog.string.urlEncode(d), d = this.getValues(d), f = 0; f < d.length; f++) {
        var g = e;
        "" !== d[f] && (g += "=" + goog.string.urlEncode(d[f]));
        a.push(g)
    }
    return this.encodedQuery_ = a.join("&")
};
goog.Uri.QueryData.prototype.toDecodedString = function () {
    return goog.Uri.decodeOrEmpty_(this.toString())
};
goog.Uri.QueryData.prototype.invalidateCache_ = function () {
    this.encodedQuery_ = null
};
goog.Uri.QueryData.prototype.filterKeys = function (a) {
    this.ensureKeyMapInitialized_();
    goog.structs.forEach(this.keyMap_, function (b, c, d) {
        goog.array.contains(a, c) || this.remove(c)
    }, this);
    return this
};
goog.Uri.QueryData.prototype.clone = function () {
    var a = new goog.Uri.QueryData;
    a.encodedQuery_ = this.encodedQuery_;
    this.keyMap_ && (a.keyMap_ = this.keyMap_.clone(), a.count_ = this.count_);
    return a
};
goog.Uri.QueryData.prototype.getKeyName_ = function (a) {
    a = String(a);
    this.ignoreCase_ && (a = a.toLowerCase());
    return a
};
goog.Uri.QueryData.prototype.setIgnoreCase = function (a) {
    a && !this.ignoreCase_ && (this.ensureKeyMapInitialized_(), this.invalidateCache_(), goog.structs.forEach(this.keyMap_, function (a, c) {
        var d = c.toLowerCase();
        c != d && (this.remove(c), this.setValues(d, a))
    }, this));
    this.ignoreCase_ = a
};
goog.Uri.QueryData.prototype.extend = function (a) {
    for (var b = 0; b < arguments.length; b++)goog.structs.forEach(arguments[b], function (a, b) {
        this.add(b, a)
    }, this)
};
goog.net = {};
goog.net.ErrorCode = {NO_ERROR: 0, ACCESS_DENIED: 1, FILE_NOT_FOUND: 2, FF_SILENT_ERROR: 3, CUSTOM_ERROR: 4, EXCEPTION: 5, HTTP_ERROR: 6, ABORT: 7, TIMEOUT: 8, OFFLINE: 9};
goog.net.ErrorCode.getDebugMessage = function (a) {
    switch (a) {
        case goog.net.ErrorCode.NO_ERROR:
            return"No Error";
        case goog.net.ErrorCode.ACCESS_DENIED:
            return"Access denied to content document";
        case goog.net.ErrorCode.FILE_NOT_FOUND:
            return"File not found";
        case goog.net.ErrorCode.FF_SILENT_ERROR:
            return"Firefox silently errored";
        case goog.net.ErrorCode.CUSTOM_ERROR:
            return"Application custom error";
        case goog.net.ErrorCode.EXCEPTION:
            return"An exception occurred";
        case goog.net.ErrorCode.HTTP_ERROR:
            return"Http response at 400 or 500 level";
        case goog.net.ErrorCode.ABORT:
            return"Request was aborted";
        case goog.net.ErrorCode.TIMEOUT:
            return"Request timed out";
        case goog.net.ErrorCode.OFFLINE:
            return"The resource is not available offline";
        default:
            return"Unrecognized error code"
    }
};
goog.structs.Collection = function () {
};
goog.structs.Set = function (a) {
    this.map_ = new goog.structs.Map;
    a && this.addAll(a)
};
goog.structs.Set.getKey_ = function (a) {
    var b = typeof a;
    return"object" == b && a || "function" == b ? "o" + goog.getUid(a) : b.substr(0, 1) + a
};
goog.structs.Set.prototype.getCount = function () {
    return this.map_.getCount()
};
goog.structs.Set.prototype.add = function (a) {
    this.map_.set(goog.structs.Set.getKey_(a), a)
};
goog.structs.Set.prototype.addAll = function (a) {
    a = goog.structs.getValues(a);
    for (var b = a.length, c = 0; c < b; c++)this.add(a[c])
};
goog.structs.Set.prototype.removeAll = function (a) {
    a = goog.structs.getValues(a);
    for (var b = a.length, c = 0; c < b; c++)this.remove(a[c])
};
goog.structs.Set.prototype.remove = function (a) {
    return this.map_.remove(goog.structs.Set.getKey_(a))
};
goog.structs.Set.prototype.clear = function () {
    this.map_.clear()
};
goog.structs.Set.prototype.isEmpty = function () {
    return this.map_.isEmpty()
};
goog.structs.Set.prototype.contains = function (a) {
    return this.map_.containsKey(goog.structs.Set.getKey_(a))
};
goog.structs.Set.prototype.containsAll = function (a) {
    return goog.structs.every(a, this.contains, this)
};
goog.structs.Set.prototype.intersection = function (a) {
    var b = new goog.structs.Set;
    a = goog.structs.getValues(a);
    for (var c = 0; c < a.length; c++) {
        var d = a[c];
        this.contains(d) && b.add(d)
    }
    return b
};
goog.structs.Set.prototype.difference = function (a) {
    var b = this.clone();
    b.removeAll(a);
    return b
};
goog.structs.Set.prototype.getValues = function () {
    return this.map_.getValues()
};
goog.structs.Set.prototype.clone = function () {
    return new goog.structs.Set(this)
};
goog.structs.Set.prototype.equals = function (a) {
    return this.getCount() == goog.structs.getCount(a) && this.isSubsetOf(a)
};
goog.structs.Set.prototype.isSubsetOf = function (a) {
    var b = goog.structs.getCount(a);
    if (this.getCount() > b)return!1;
    !(a instanceof goog.structs.Set) && 5 < b && (a = new goog.structs.Set(a));
    return goog.structs.every(this, function (b) {
        return goog.structs.contains(a, b)
    })
};
goog.structs.Set.prototype.__iterator__ = function (a) {
    return this.map_.__iterator__(!1)
};
goog.debug.LOGGING_ENABLED = goog.DEBUG;
goog.debug.catchErrors = function (a, b, c) {
    c = c || goog.global;
    var d = c.onerror, e = !!b;
    goog.userAgent.WEBKIT && !goog.userAgent.isVersionOrHigher("535.3") && (e = !e);
    c.onerror = function (b, c, h) {
        d && d(b, c, h);
        a({message: b, fileName: c, line: h});
        return e
    }
};
goog.debug.expose = function (a, b) {
    if ("undefined" == typeof a)return"undefined";
    if (null == a)return"NULL";
    var c = [], d;
    for (d in a)if (b || !goog.isFunction(a[d])) {
        var e = d + " = ";
        try {
            e += a[d]
        } catch (f) {
            e += "*** " + f + " ***"
        }
        c.push(e)
    }
    return c.join("\n")
};
goog.debug.deepExpose = function (a, b) {
    var c = new goog.structs.Set, d = [], e = function (a, g) {
        var h = g + "  ";
        try {
            if (goog.isDef(a))if (goog.isNull(a))d.push("NULL"); else if (goog.isString(a))d.push('"' + a.replace(/\n/g, "\n" + g) + '"'); else if (goog.isFunction(a))d.push(String(a).replace(/\n/g, "\n" + g)); else if (goog.isObject(a))if (c.contains(a))d.push("*** reference loop detected ***"); else {
                c.add(a);
                d.push("{");
                for (var k in a)if (b || !goog.isFunction(a[k]))d.push("\n"), d.push(h), d.push(k + " = "), e(a[k], h);
                d.push("\n" + g + "}")
            } else d.push(a);
            else d.push("undefined")
        } catch (l) {
            d.push("*** " + l + " ***")
        }
    };
    e(a, "");
    return d.join("")
};
goog.debug.exposeArray = function (a) {
    for (var b = [], c = 0; c < a.length; c++)goog.isArray(a[c]) ? b.push(goog.debug.exposeArray(a[c])) : b.push(a[c]);
    return"[ " + b.join(", ") + " ]"
};
goog.debug.exposeException = function (a, b) {
    try {
        var c = goog.debug.normalizeErrorObject(a);
        return"Message: " + goog.string.htmlEscape(c.message) + '\nUrl: <a href="view-source:' + c.fileName + '" target="_new">' + c.fileName + "</a>\nLine: " + c.lineNumber + "\n\nBrowser stack:\n" + goog.string.htmlEscape(c.stack + "-> ") + "[end]\n\nJS stack traversal:\n" + goog.string.htmlEscape(goog.debug.getStacktrace(b) + "-> ")
    } catch (d) {
        return"Exception trying to expose exception! You win, we lose. " + d
    }
};
goog.debug.normalizeErrorObject = function (a) {
    var b = goog.getObjectByName("window.location.href");
    if (goog.isString(a))return{message: a, name: "Unknown error", lineNumber: "Not available", fileName: b, stack: "Not available"};
    var c, d, e = !1;
    try {
        c = a.lineNumber || a.line || "Not available"
    } catch (f) {
        c = "Not available", e = !0
    }
    try {
        d = a.fileName || a.filename || a.sourceURL || goog.global.$googDebugFname || b
    } catch (g) {
        d = "Not available", e = !0
    }
    return!e && a.lineNumber && a.fileName && a.stack && a.message && a.name ? a : {message: a.message || "Not available",
        name: a.name || "UnknownError", lineNumber: c, fileName: d, stack: a.stack || "Not available"}
};
goog.debug.enhanceError = function (a, b) {
    var c = "string" == typeof a ? Error(a) : a;
    c.stack || (c.stack = goog.debug.getStacktrace(arguments.callee.caller));
    if (b) {
        for (var d = 0; c["message" + d];)++d;
        c["message" + d] = String(b)
    }
    return c
};
goog.debug.getStacktraceSimple = function (a) {
    for (var b = [], c = arguments.callee.caller, d = 0; c && (!a || d < a);) {
        b.push(goog.debug.getFunctionName(c));
        b.push("()\n");
        try {
            c = c.caller
        } catch (e) {
            b.push("[exception trying to get caller]\n");
            break
        }
        d++;
        if (d >= goog.debug.MAX_STACK_DEPTH) {
            b.push("[...long stack...]");
            break
        }
    }
    a && d >= a ? b.push("[...reached max depth limit...]") : b.push("[end]");
    return b.join("")
};
goog.debug.MAX_STACK_DEPTH = 50;
goog.debug.getStacktrace = function (a) {
    return goog.debug.getStacktraceHelper_(a || arguments.callee.caller, [])
};
goog.debug.getStacktraceHelper_ = function (a, b) {
    var c = [];
    if (goog.array.contains(b, a))c.push("[...circular reference...]"); else if (a && b.length < goog.debug.MAX_STACK_DEPTH) {
        c.push(goog.debug.getFunctionName(a) + "(");
        for (var d = a.arguments, e = 0; e < d.length; e++) {
            0 < e && c.push(", ");
            var f;
            f = d[e];
            switch (typeof f) {
                case "object":
                    f = f ? "object" : "null";
                    break;
                case "string":
                    break;
                case "number":
                    f = String(f);
                    break;
                case "boolean":
                    f = f ? "true" : "false";
                    break;
                case "function":
                    f = (f = goog.debug.getFunctionName(f)) ? f : "[fn]";
                    break;
                default:
                    f = typeof f
            }
            40 < f.length && (f = f.substr(0, 40) + "...");
            c.push(f)
        }
        b.push(a);
        c.push(")\n");
        try {
            c.push(goog.debug.getStacktraceHelper_(a.caller, b))
        } catch (g) {
            c.push("[exception trying to get caller]\n")
        }
    } else a ? c.push("[...long stack...]") : c.push("[end]");
    return c.join("")
};
goog.debug.setFunctionResolver = function (a) {
    goog.debug.fnNameResolver_ = a
};
goog.debug.getFunctionName = function (a) {
    if (goog.debug.fnNameCache_[a])return goog.debug.fnNameCache_[a];
    if (goog.debug.fnNameResolver_) {
        var b = goog.debug.fnNameResolver_(a);
        if (b)return goog.debug.fnNameCache_[a] = b
    }
    a = String(a);
    goog.debug.fnNameCache_[a] || (b = /function ([^\(]+)/.exec(a), goog.debug.fnNameCache_[a] = b ? b[1] : "[Anonymous]");
    return goog.debug.fnNameCache_[a]
};
goog.debug.makeWhitespaceVisible = function (a) {
    return a.replace(/ /g, "[_]").replace(/\f/g, "[f]").replace(/\n/g, "[n]\n").replace(/\r/g, "[r]").replace(/\t/g, "[t]")
};
goog.debug.fnNameCache_ = {};
goog.json = {};
goog.json.isValid_ = function (a) {
    return/^\s*$/.test(a) ? !1 : /^[\],:{}\s\u2028\u2029]*$/.test(a.replace(/\\["\\\/bfnrtu]/g, "@").replace(/"[^"\\\n\r\u2028\u2029\x00-\x08\x0a-\x1f]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, "]").replace(/(?:^|:|,)(?:[\s\u2028\u2029]*\[)+/g, ""))
};
goog.json.parse = function (a) {
    a = String(a);
    if (goog.json.isValid_(a))try {
        return eval("(" + a + ")")
    } catch (b) {
    }
    throw Error("Invalid JSON string: " + a);
};
goog.json.unsafeParse = function (a) {
    return eval("(" + a + ")")
};
goog.json.serialize = function (a, b) {
    return(new goog.json.Serializer(b)).serialize(a)
};
goog.json.Serializer = function (a) {
    this.replacer_ = a
};
goog.json.Serializer.prototype.serialize = function (a) {
    var b = [];
    this.serialize_(a, b);
    return b.join("")
};
goog.json.Serializer.prototype.serialize_ = function (a, b) {
    switch (typeof a) {
        case "string":
            this.serializeString_(a, b);
            break;
        case "number":
            this.serializeNumber_(a, b);
            break;
        case "boolean":
            b.push(a);
            break;
        case "undefined":
            b.push("null");
            break;
        case "object":
            if (null == a) {
                b.push("null");
                break
            }
            if (goog.isArray(a)) {
                this.serializeArray(a, b);
                break
            }
            this.serializeObject_(a, b);
            break;
        case "function":
            break;
        default:
            throw Error("Unknown type: " + typeof a);
    }
};
goog.json.Serializer.charToJsonCharCache_ = {'"': '\\"', "\\": "\\\\", "/": "\\/", "\b": "\\b", "\f": "\\f", "\n": "\\n", "\r": "\\r", "\t": "\\t", "\x0B": "\\u000b"};
goog.json.Serializer.charsToReplace_ = /\uffff/.test("\uffff") ? /[\\\"\x00-\x1f\x7f-\uffff]/g : /[\\\"\x00-\x1f\x7f-\xff]/g;
goog.json.Serializer.prototype.serializeString_ = function (a, b) {
    b.push('"', a.replace(goog.json.Serializer.charsToReplace_, function (a) {
        if (a in goog.json.Serializer.charToJsonCharCache_)return goog.json.Serializer.charToJsonCharCache_[a];
        var b = a.charCodeAt(0), e = "\\u";
        16 > b ? e += "000" : 256 > b ? e += "00" : 4096 > b && (e += "0");
        return goog.json.Serializer.charToJsonCharCache_[a] = e + b.toString(16)
    }), '"')
};
goog.json.Serializer.prototype.serializeNumber_ = function (a, b) {
    b.push(isFinite(a) && !isNaN(a) ? a : "null")
};
goog.json.Serializer.prototype.serializeArray = function (a, b) {
    var c = a.length;
    b.push("[");
    for (var d = "", e = 0; e < c; e++)b.push(d), d = a[e], this.serialize_(this.replacer_ ? this.replacer_.call(a, String(e), d) : d, b), d = ",";
    b.push("]")
};
goog.json.Serializer.prototype.serializeObject_ = function (a, b) {
    b.push("{");
    var c = "", d;
    for (d in a)if (Object.prototype.hasOwnProperty.call(a, d)) {
        var e = a[d];
        "function" != typeof e && (b.push(c), this.serializeString_(d, b), b.push(":"), this.serialize_(this.replacer_ ? this.replacer_.call(a, d, e) : e, b), c = ",")
    }
    b.push("}")
};
goog.net.EventType = {COMPLETE: "complete", SUCCESS: "success", ERROR: "error", ABORT: "abort", READY: "ready", READY_STATE_CHANGE: "readystatechange", TIMEOUT: "timeout", INCREMENTAL_DATA: "incrementaldata", PROGRESS: "progress"};
goog.debug.LogRecord = function (a, b, c, d, e) {
    this.reset(a, b, c, d, e)
};
goog.debug.LogRecord.prototype.sequenceNumber_ = 0;
goog.debug.LogRecord.prototype.exception_ = null;
goog.debug.LogRecord.prototype.exceptionText_ = null;
goog.debug.LogRecord.ENABLE_SEQUENCE_NUMBERS = !0;
goog.debug.LogRecord.nextSequenceNumber_ = 0;
goog.debug.LogRecord.prototype.reset = function (a, b, c, d, e) {
    goog.debug.LogRecord.ENABLE_SEQUENCE_NUMBERS && (this.sequenceNumber_ = "number" == typeof e ? e : goog.debug.LogRecord.nextSequenceNumber_++);
    this.time_ = d || goog.now();
    this.level_ = a;
    this.msg_ = b;
    this.loggerName_ = c;
    delete this.exception_;
    delete this.exceptionText_
};
goog.debug.LogRecord.prototype.getLoggerName = function () {
    return this.loggerName_
};
goog.debug.LogRecord.prototype.getException = function () {
    return this.exception_
};
goog.debug.LogRecord.prototype.setException = function (a) {
    this.exception_ = a
};
goog.debug.LogRecord.prototype.getExceptionText = function () {
    return this.exceptionText_
};
goog.debug.LogRecord.prototype.setExceptionText = function (a) {
    this.exceptionText_ = a
};
goog.debug.LogRecord.prototype.setLoggerName = function (a) {
    this.loggerName_ = a
};
goog.debug.LogRecord.prototype.getLevel = function () {
    return this.level_
};
goog.debug.LogRecord.prototype.setLevel = function (a) {
    this.level_ = a
};
goog.debug.LogRecord.prototype.getMessage = function () {
    return this.msg_
};
goog.debug.LogRecord.prototype.setMessage = function (a) {
    this.msg_ = a
};
goog.debug.LogRecord.prototype.getMillis = function () {
    return this.time_
};
goog.debug.LogRecord.prototype.setMillis = function (a) {
    this.time_ = a
};
goog.debug.LogRecord.prototype.getSequenceNumber = function () {
    return this.sequenceNumber_
};
goog.debug.LogBuffer = function () {
    goog.asserts.assert(goog.debug.LogBuffer.isBufferingEnabled(), "Cannot use goog.debug.LogBuffer without defining goog.debug.LogBuffer.CAPACITY.");
    this.clear()
};
goog.debug.LogBuffer.getInstance = function () {
    goog.debug.LogBuffer.instance_ || (goog.debug.LogBuffer.instance_ = new goog.debug.LogBuffer);
    return goog.debug.LogBuffer.instance_
};
goog.debug.LogBuffer.CAPACITY = 0;
goog.debug.LogBuffer.prototype.addRecord = function (a, b, c) {
    var d = (this.curIndex_ + 1) % goog.debug.LogBuffer.CAPACITY;
    this.curIndex_ = d;
    if (this.isFull_)return d = this.buffer_[d], d.reset(a, b, c), d;
    this.isFull_ = d == goog.debug.LogBuffer.CAPACITY - 1;
    return this.buffer_[d] = new goog.debug.LogRecord(a, b, c)
};
goog.debug.LogBuffer.isBufferingEnabled = function () {
    return 0 < goog.debug.LogBuffer.CAPACITY
};
goog.debug.LogBuffer.prototype.clear = function () {
    this.buffer_ = Array(goog.debug.LogBuffer.CAPACITY);
    this.curIndex_ = -1;
    this.isFull_ = !1
};
goog.debug.LogBuffer.prototype.forEachRecord = function (a) {
    var b = this.buffer_;
    if (b[0]) {
        var c = this.curIndex_, d = this.isFull_ ? c : -1;
        do d = (d + 1) % goog.debug.LogBuffer.CAPACITY, a(b[d]); while (d != c)
    }
};
goog.debug.Logger = function (a) {
    this.name_ = a
};
goog.debug.Logger.prototype.parent_ = null;
goog.debug.Logger.prototype.level_ = null;
goog.debug.Logger.prototype.children_ = null;
goog.debug.Logger.prototype.handlers_ = null;
goog.debug.Logger.ENABLE_HIERARCHY = !0;
goog.debug.Logger.ENABLE_HIERARCHY || (goog.debug.Logger.rootHandlers_ = []);
goog.debug.Logger.Level = function (a, b) {
    this.name = a;
    this.value = b
};
goog.debug.Logger.Level.prototype.toString = function () {
    return this.name
};
goog.debug.Logger.Level.OFF = new goog.debug.Logger.Level("OFF", Infinity);
goog.debug.Logger.Level.SHOUT = new goog.debug.Logger.Level("SHOUT", 1200);
goog.debug.Logger.Level.SEVERE = new goog.debug.Logger.Level("SEVERE", 1E3);
goog.debug.Logger.Level.WARNING = new goog.debug.Logger.Level("WARNING", 900);
goog.debug.Logger.Level.INFO = new goog.debug.Logger.Level("INFO", 800);
goog.debug.Logger.Level.CONFIG = new goog.debug.Logger.Level("CONFIG", 700);
goog.debug.Logger.Level.FINE = new goog.debug.Logger.Level("FINE", 500);
goog.debug.Logger.Level.FINER = new goog.debug.Logger.Level("FINER", 400);
goog.debug.Logger.Level.FINEST = new goog.debug.Logger.Level("FINEST", 300);
goog.debug.Logger.Level.ALL = new goog.debug.Logger.Level("ALL", 0);
goog.debug.Logger.Level.PREDEFINED_LEVELS = [goog.debug.Logger.Level.OFF, goog.debug.Logger.Level.SHOUT, goog.debug.Logger.Level.SEVERE, goog.debug.Logger.Level.WARNING, goog.debug.Logger.Level.INFO, goog.debug.Logger.Level.CONFIG, goog.debug.Logger.Level.FINE, goog.debug.Logger.Level.FINER, goog.debug.Logger.Level.FINEST, goog.debug.Logger.Level.ALL];
goog.debug.Logger.Level.predefinedLevelsCache_ = null;
goog.debug.Logger.Level.createPredefinedLevelsCache_ = function () {
    goog.debug.Logger.Level.predefinedLevelsCache_ = {};
    for (var a = 0, b; b = goog.debug.Logger.Level.PREDEFINED_LEVELS[a]; a++)goog.debug.Logger.Level.predefinedLevelsCache_[b.value] = b, goog.debug.Logger.Level.predefinedLevelsCache_[b.name] = b
};
goog.debug.Logger.Level.getPredefinedLevel = function (a) {
    goog.debug.Logger.Level.predefinedLevelsCache_ || goog.debug.Logger.Level.createPredefinedLevelsCache_();
    return goog.debug.Logger.Level.predefinedLevelsCache_[a] || null
};
goog.debug.Logger.Level.getPredefinedLevelByValue = function (a) {
    goog.debug.Logger.Level.predefinedLevelsCache_ || goog.debug.Logger.Level.createPredefinedLevelsCache_();
    if (a in goog.debug.Logger.Level.predefinedLevelsCache_)return goog.debug.Logger.Level.predefinedLevelsCache_[a];
    for (var b = 0; b < goog.debug.Logger.Level.PREDEFINED_LEVELS.length; ++b) {
        var c = goog.debug.Logger.Level.PREDEFINED_LEVELS[b];
        if (c.value <= a)return c
    }
    return null
};
goog.debug.Logger.getLogger = function (a) {
    return goog.debug.LogManager.getLogger(a)
};
goog.debug.Logger.logToProfilers = function (a) {
    goog.global.console && (goog.global.console.timeStamp ? goog.global.console.timeStamp(a) : goog.global.console.markTimeline && goog.global.console.markTimeline(a));
    goog.global.msWriteProfilerMark && goog.global.msWriteProfilerMark(a)
};
goog.debug.Logger.prototype.getName = function () {
    return this.name_
};
goog.debug.Logger.prototype.addHandler = function (a) {
    goog.debug.LOGGING_ENABLED && (goog.debug.Logger.ENABLE_HIERARCHY ? (this.handlers_ || (this.handlers_ = []), this.handlers_.push(a)) : (goog.asserts.assert(!this.name_, "Cannot call addHandler on a non-root logger when goog.debug.Logger.ENABLE_HIERARCHY is false."), goog.debug.Logger.rootHandlers_.push(a)))
};
goog.debug.Logger.prototype.removeHandler = function (a) {
    if (goog.debug.LOGGING_ENABLED) {
        var b = goog.debug.Logger.ENABLE_HIERARCHY ? this.handlers_ : goog.debug.Logger.rootHandlers_;
        return!!b && goog.array.remove(b, a)
    }
    return!1
};
goog.debug.Logger.prototype.getParent = function () {
    return this.parent_
};
goog.debug.Logger.prototype.getChildren = function () {
    this.children_ || (this.children_ = {});
    return this.children_
};
goog.debug.Logger.prototype.setLevel = function (a) {
    goog.debug.LOGGING_ENABLED && (goog.debug.Logger.ENABLE_HIERARCHY ? this.level_ = a : (goog.asserts.assert(!this.name_, "Cannot call setLevel() on a non-root logger when goog.debug.Logger.ENABLE_HIERARCHY is false."), goog.debug.Logger.rootLevel_ = a))
};
goog.debug.Logger.prototype.getLevel = function () {
    return goog.debug.LOGGING_ENABLED ? this.level_ : goog.debug.Logger.Level.OFF
};
goog.debug.Logger.prototype.getEffectiveLevel = function () {
    if (!goog.debug.LOGGING_ENABLED)return goog.debug.Logger.Level.OFF;
    if (!goog.debug.Logger.ENABLE_HIERARCHY)return goog.debug.Logger.rootLevel_;
    if (this.level_)return this.level_;
    if (this.parent_)return this.parent_.getEffectiveLevel();
    goog.asserts.fail("Root logger has no level set.");
    return null
};
goog.debug.Logger.prototype.isLoggable = function (a) {
    return goog.debug.LOGGING_ENABLED && a.value >= this.getEffectiveLevel().value
};
goog.debug.Logger.prototype.log = function (a, b, c) {
    goog.debug.LOGGING_ENABLED && this.isLoggable(a) && this.doLogRecord_(this.getLogRecord(a, b, c))
};
goog.debug.Logger.prototype.getLogRecord = function (a, b, c) {
    var d = goog.debug.LogBuffer.isBufferingEnabled() ? goog.debug.LogBuffer.getInstance().addRecord(a, b, this.name_) : new goog.debug.LogRecord(a, String(b), this.name_);
    c && (d.setException(c), d.setExceptionText(goog.debug.exposeException(c, arguments.callee.caller)));
    return d
};
goog.debug.Logger.prototype.shout = function (a, b) {
    goog.debug.LOGGING_ENABLED && this.log(goog.debug.Logger.Level.SHOUT, a, b)
};
goog.debug.Logger.prototype.severe = function (a, b) {
    goog.debug.LOGGING_ENABLED && this.log(goog.debug.Logger.Level.SEVERE, a, b)
};
goog.debug.Logger.prototype.warning = function (a, b) {
    goog.debug.LOGGING_ENABLED && this.log(goog.debug.Logger.Level.WARNING, a, b)
};
goog.debug.Logger.prototype.info = function (a, b) {
    goog.debug.LOGGING_ENABLED && this.log(goog.debug.Logger.Level.INFO, a, b)
};
goog.debug.Logger.prototype.config = function (a, b) {
    goog.debug.LOGGING_ENABLED && this.log(goog.debug.Logger.Level.CONFIG, a, b)
};
goog.debug.Logger.prototype.fine = function (a, b) {
    goog.debug.LOGGING_ENABLED && this.log(goog.debug.Logger.Level.FINE, a, b)
};
goog.debug.Logger.prototype.finer = function (a, b) {
    goog.debug.LOGGING_ENABLED && this.log(goog.debug.Logger.Level.FINER, a, b)
};
goog.debug.Logger.prototype.finest = function (a, b) {
    goog.debug.LOGGING_ENABLED && this.log(goog.debug.Logger.Level.FINEST, a, b)
};
goog.debug.Logger.prototype.logRecord = function (a) {
    goog.debug.LOGGING_ENABLED && this.isLoggable(a.getLevel()) && this.doLogRecord_(a)
};
goog.debug.Logger.prototype.doLogRecord_ = function (a) {
    goog.debug.Logger.logToProfilers("log:" + a.getMessage());
    if (goog.debug.Logger.ENABLE_HIERARCHY)for (var b = this; b;)b.callPublish_(a), b = b.getParent(); else for (var b = 0, c; c = goog.debug.Logger.rootHandlers_[b++];)c(a)
};
goog.debug.Logger.prototype.callPublish_ = function (a) {
    if (this.handlers_)for (var b = 0, c; c = this.handlers_[b]; b++)c(a)
};
goog.debug.Logger.prototype.setParent_ = function (a) {
    this.parent_ = a
};
goog.debug.Logger.prototype.addChild_ = function (a, b) {
    this.getChildren()[a] = b
};
goog.debug.LogManager = {};
goog.debug.LogManager.loggers_ = {};
goog.debug.LogManager.rootLogger_ = null;
goog.debug.LogManager.initialize = function () {
    goog.debug.LogManager.rootLogger_ || (goog.debug.LogManager.rootLogger_ = new goog.debug.Logger(""), goog.debug.LogManager.loggers_[""] = goog.debug.LogManager.rootLogger_, goog.debug.LogManager.rootLogger_.setLevel(goog.debug.Logger.Level.CONFIG))
};
goog.debug.LogManager.getLoggers = function () {
    return goog.debug.LogManager.loggers_
};
goog.debug.LogManager.getRoot = function () {
    goog.debug.LogManager.initialize();
    return goog.debug.LogManager.rootLogger_
};
goog.debug.LogManager.getLogger = function (a) {
    goog.debug.LogManager.initialize();
    return goog.debug.LogManager.loggers_[a] || goog.debug.LogManager.createLogger_(a)
};
goog.debug.LogManager.createFunctionForCatchErrors = function (a) {
    return function (b) {
        (a || goog.debug.LogManager.getRoot()).severe("Error: " + b.message + " (" + b.fileName + " @ Line: " + b.line + ")")
    }
};
goog.debug.LogManager.createLogger_ = function (a) {
    var b = new goog.debug.Logger(a);
    if (goog.debug.Logger.ENABLE_HIERARCHY) {
        var c = a.lastIndexOf("."), d = a.substr(0, c), c = a.substr(c + 1), d = goog.debug.LogManager.getLogger(d);
        d.addChild_(c, b);
        b.setParent_(d)
    }
    return goog.debug.LogManager.loggers_[a] = b
};
goog.log = {};
goog.log.ENABLED = goog.debug.LOGGING_ENABLED;
goog.log.Logger = goog.debug.Logger;
goog.log.Level = goog.debug.Logger.Level;
goog.log.LogRecord = goog.debug.LogRecord;
goog.log.getLogger = function (a, b) {
    if (goog.log.ENABLED) {
        var c = goog.debug.Logger.getLogger(a);
        b && c && c.setLevel(b);
        return c
    }
    return null
};
goog.log.addHandler = function (a, b) {
    goog.log.ENABLED && a && a.addHandler(b)
};
goog.log.removeHandler = function (a, b) {
    return goog.log.ENABLED && a ? a.removeHandler(b) : !1
};
goog.log.log = function (a, b, c, d) {
    goog.log.ENABLED && a && a.log(b, c, d)
};
goog.log.error = function (a, b, c) {
    goog.log.ENABLED && a && a.severe(b, c)
};
goog.log.warning = function (a, b, c) {
    goog.log.ENABLED && a && a.warning(b, c)
};
goog.log.info = function (a, b, c) {
    goog.log.ENABLED && a && a.info(b, c)
};
goog.log.fine = function (a, b, c) {
    goog.log.ENABLED && a && a.fine(b, c)
};
goog.net.IframeIo = function () {
    goog.events.EventTarget.call(this);
    this.name_ = goog.net.IframeIo.getNextName_();
    this.iframesForDisposal_ = [];
    goog.net.IframeIo.instances_[this.name_] = this
};
goog.inherits(goog.net.IframeIo, goog.events.EventTarget);
goog.net.IframeIo.instances_ = {};
goog.net.IframeIo.FRAME_NAME_PREFIX = "closure_frame";
goog.net.IframeIo.INNER_FRAME_SUFFIX = "_inner";
goog.net.IframeIo.IFRAME_DISPOSE_DELAY_MS = 2E3;
goog.net.IframeIo.counter_ = 0;
goog.net.IframeIo.send = function (a, b, c, d, e) {
    var f = new goog.net.IframeIo;
    goog.events.listen(f, goog.net.EventType.READY, f.dispose, !1, f);
    b && goog.events.listen(f, goog.net.EventType.COMPLETE, b);
    f.send(a, c, d, e)
};
goog.net.IframeIo.getIframeByName = function (a) {
    return window.frames[a]
};
goog.net.IframeIo.getInstanceByName = function (a) {
    return goog.net.IframeIo.instances_[a]
};
goog.net.IframeIo.handleIncrementalData = function (a, b) {
    var c = goog.string.endsWith(a.name, goog.net.IframeIo.INNER_FRAME_SUFFIX) ? a.parent.name : a.name, d = c.substring(0, c.lastIndexOf("_"));
    (d = goog.net.IframeIo.getInstanceByName(d)) && c == d.iframeName_ ? d.handleIncrementalData_(b) : goog.log.getLogger("goog.net.IframeIo").info("Incremental iframe data routed for unknown iframe")
};
goog.net.IframeIo.getNextName_ = function () {
    return goog.net.IframeIo.FRAME_NAME_PREFIX + goog.net.IframeIo.counter_++
};
goog.net.IframeIo.getForm_ = function () {
    if (!goog.net.IframeIo.form_) {
        goog.net.IframeIo.form_ = goog.dom.createDom("form");
        goog.net.IframeIo.form_.acceptCharset = "utf-8";
        var a = goog.net.IframeIo.form_.style;
        a.position = "absolute";
        a.visibility = "hidden";
        a.top = a.left = "-10px";
        a.width = a.height = "10px";
        a.overflow = "hidden";
        goog.dom.getDocument().body.appendChild(goog.net.IframeIo.form_)
    }
    return goog.net.IframeIo.form_
};
goog.net.IframeIo.addFormInputs_ = function (a, b) {
    var c = goog.dom.getDomHelper(a);
    goog.structs.forEach(b, function (b, e) {
        var f = c.createDom("input", {type: "hidden", name: e, value: b});
        a.appendChild(f)
    })
};
goog.net.IframeIo.prototype.logger_ = goog.log.getLogger("goog.net.IframeIo");
goog.net.IframeIo.prototype.form_ = null;
goog.net.IframeIo.prototype.iframe_ = null;
goog.net.IframeIo.prototype.iframeName_ = null;
goog.net.IframeIo.prototype.nextIframeId_ = 0;
goog.net.IframeIo.prototype.active_ = !1;
goog.net.IframeIo.prototype.complete_ = !1;
goog.net.IframeIo.prototype.success_ = !1;
goog.net.IframeIo.prototype.lastUri_ = null;
goog.net.IframeIo.prototype.lastContent_ = null;
goog.net.IframeIo.prototype.lastErrorCode_ = goog.net.ErrorCode.NO_ERROR;
goog.net.IframeIo.prototype.timeoutInterval_ = 0;
goog.net.IframeIo.prototype.timeoutId_ = null;
goog.net.IframeIo.prototype.firefoxSilentErrorTimeout_ = null;
goog.net.IframeIo.prototype.iframeDisposalTimer_ = null;
goog.net.IframeIo.prototype.ignoreResponse_ = !1;
goog.net.IframeIo.prototype.send = function (a, b, c, d) {
    if (this.active_)throw Error("[goog.net.IframeIo] Unable to send, already active.");
    this.lastUri_ = a = new goog.Uri(a);
    b = b ? b.toUpperCase() : "GET";
    c && a.makeUnique();
    goog.log.info(this.logger_, "Sending iframe request: " + a + " [" + b + "]");
    this.form_ = goog.net.IframeIo.getForm_();
    "GET" == b && goog.net.IframeIo.addFormInputs_(this.form_, a.getQueryData());
    d && goog.net.IframeIo.addFormInputs_(this.form_, d);
    this.form_.action = a.toString();
    this.form_.method = b;
    this.sendFormInternal_()
};
goog.net.IframeIo.prototype.sendFromForm = function (a, b, c) {
    if (this.active_)throw Error("[goog.net.IframeIo] Unable to send, already active.");
    b = new goog.Uri(b || a.action);
    c && b.makeUnique();
    goog.log.info(this.logger_, "Sending iframe request from form: " + b);
    this.lastUri_ = b;
    this.form_ = a;
    this.form_.action = b.toString();
    this.sendFormInternal_()
};
goog.net.IframeIo.prototype.abort = function (a) {
    this.active_ && (goog.log.info(this.logger_, "Request aborted"), goog.events.removeAll(this.getRequestIframe()), this.success_ = this.active_ = this.complete_ = !1, this.lastErrorCode_ = a || goog.net.ErrorCode.ABORT, this.dispatchEvent(goog.net.EventType.ABORT), this.makeReady_())
};
goog.net.IframeIo.prototype.disposeInternal = function () {
    goog.log.fine(this.logger_, "Disposing iframeIo instance");
    this.active_ && (goog.log.fine(this.logger_, "Aborting active request"), this.abort());
    goog.net.IframeIo.superClass_.disposeInternal.call(this);
    this.iframe_ && this.scheduleIframeDisposal_();
    this.disposeForm_();
    delete this.errorChecker_;
    this.lastUri_ = this.lastCustomError_ = this.lastContent_ = this.lastContentHtml_ = this.form_ = null;
    this.lastErrorCode_ = goog.net.ErrorCode.NO_ERROR;
    delete goog.net.IframeIo.instances_[this.name_]
};
goog.net.IframeIo.prototype.isComplete = function () {
    return this.complete_
};
goog.net.IframeIo.prototype.isSuccess = function () {
    return this.success_
};
goog.net.IframeIo.prototype.isActive = function () {
    return this.active_
};
goog.net.IframeIo.prototype.getResponseText = function () {
    return this.lastContent_
};
goog.net.IframeIo.prototype.getResponseHtml = function () {
    return this.lastContentHtml_
};
goog.net.IframeIo.prototype.getResponseJson = function () {
    return goog.json.parse(this.lastContent_)
};
goog.net.IframeIo.prototype.getResponseXml = function () {
    return this.iframe_ ? this.getContentDocument_() : null
};
goog.net.IframeIo.prototype.getLastUri = function () {
    return this.lastUri_
};
goog.net.IframeIo.prototype.getLastErrorCode = function () {
    return this.lastErrorCode_
};
goog.net.IframeIo.prototype.getLastError = function () {
    return goog.net.ErrorCode.getDebugMessage(this.lastErrorCode_)
};
goog.net.IframeIo.prototype.getLastCustomError = function () {
    return this.lastCustomError_
};
goog.net.IframeIo.prototype.setErrorChecker = function (a) {
    this.errorChecker_ = a
};
goog.net.IframeIo.prototype.getErrorChecker = function () {
    return this.errorChecker_
};
goog.net.IframeIo.prototype.getTimeoutInterval = function () {
    return this.timeoutInterval_
};
goog.net.IframeIo.prototype.setTimeoutInterval = function (a) {
    this.timeoutInterval_ = Math.max(0, a)
};
goog.net.IframeIo.prototype.isIgnoringResponse = function () {
    return this.ignoreResponse_
};
goog.net.IframeIo.prototype.setIgnoreResponse = function (a) {
    this.ignoreResponse_ = a
};
goog.net.IframeIo.prototype.sendFormInternal_ = function () {
    this.active_ = !0;
    this.complete_ = !1;
    this.lastErrorCode_ = goog.net.ErrorCode.NO_ERROR;
    this.createIframe_();
    if (goog.userAgent.IE) {
        this.form_.target = this.iframeName_ || "";
        this.appendIframe_();
        this.ignoreResponse_ || goog.events.listen(this.iframe_, goog.events.EventType.READYSTATECHANGE, this.onIeReadyStateChange_, !1, this);
        try {
            this.errorHandled_ = !1, this.form_.submit()
        } catch (a) {
            this.ignoreResponse_ || goog.events.unlisten(this.iframe_, goog.events.EventType.READYSTATECHANGE,
                this.onIeReadyStateChange_, !1, this), this.handleError_(goog.net.ErrorCode.ACCESS_DENIED)
        }
    } else {
        goog.log.fine(this.logger_, "Setting up iframes and cloning form");
        this.appendIframe_();
        var b = this.iframeName_ + goog.net.IframeIo.INNER_FRAME_SUFFIX, c = goog.dom.getFrameContentDocument(this.iframe_), d = "<body><iframe id=" + b + " name=" + b + "></iframe>";
        document.baseURI && (d = '<head><base href="' + goog.string.htmlEscape(document.baseURI) + '"></head>' + d);
        goog.userAgent.OPERA ? c.documentElement.innerHTML = d : c.write(d);
        this.ignoreResponse_ ||
        goog.events.listen(c.getElementById(b), goog.events.EventType.LOAD, this.onIframeLoaded_, !1, this);
        for (var e = this.form_.getElementsByTagName("textarea"), d = 0, f = e.length; d < f; d++) {
            var g = e[d].value;
            goog.dom.getRawTextContent(e[d]) != g && (goog.dom.setTextContent(e[d], g), e[d].value = g)
        }
        e = c.importNode(this.form_, !0);
        e.target = b;
        e.action = this.form_.action;
        c.body.appendChild(e);
        for (var g = this.form_.getElementsByTagName("select"), h = e.getElementsByTagName("select"), d = 0, f = g.length; d < f; d++)for (var k = g[d].getElementsByTagName("option"),
                                                                                                                                               l = h[d].getElementsByTagName("option"), m = 0, n = k.length; m < n; m++)l[m].selected = k[m].selected;
        g = this.form_.getElementsByTagName("input");
        h = e.getElementsByTagName("input");
        d = 0;
        for (f = g.length; d < f; d++)if ("file" == g[d].type && g[d].value != h[d].value) {
            goog.log.fine(this.logger_, "File input value not cloned properly.  Will submit using original form.");
            this.form_.target = b;
            e = this.form_;
            break
        }
        goog.log.fine(this.logger_, "Submitting form");
        try {
            this.errorHandled_ = !1, e.submit(), c.close(), goog.userAgent.GECKO && (this.firefoxSilentErrorTimeout_ =
                goog.Timer.callOnce(this.testForFirefoxSilentError_, 250, this))
        } catch (p) {
            goog.log.error(this.logger_, "Error when submitting form: " + goog.debug.exposeException(p)), this.ignoreResponse_ || goog.events.unlisten(c.getElementById(b), goog.events.EventType.LOAD, this.onIframeLoaded_, !1, this), c.close(), this.handleError_(goog.net.ErrorCode.FILE_NOT_FOUND)
        }
    }
};
goog.net.IframeIo.prototype.onIeReadyStateChange_ = function (a) {
    if ("complete" == this.iframe_.readyState) {
        goog.events.unlisten(this.iframe_, goog.events.EventType.READYSTATECHANGE, this.onIeReadyStateChange_, !1, this);
        var b;
        try {
            if (b = goog.dom.getFrameContentDocument(this.iframe_), goog.userAgent.IE && "about:blank" == b.location && !navigator.onLine) {
                this.handleError_(goog.net.ErrorCode.OFFLINE);
                return
            }
        } catch (c) {
            this.handleError_(goog.net.ErrorCode.ACCESS_DENIED);
            return
        }
        this.handleLoad_(b)
    }
};
goog.net.IframeIo.prototype.onIframeLoaded_ = function (a) {
    goog.userAgent.OPERA && "about:blank" == this.getContentDocument_().location || (goog.events.unlisten(this.getRequestIframe(), goog.events.EventType.LOAD, this.onIframeLoaded_, !1, this), this.handleLoad_(this.getContentDocument_()))
};
goog.net.IframeIo.prototype.handleLoad_ = function (a) {
    goog.log.fine(this.logger_, "Iframe loaded");
    this.complete_ = !0;
    this.active_ = !1;
    var b;
    try {
        var c = a.body;
        this.lastContent_ = c.textContent || c.innerText;
        this.lastContentHtml_ = c.innerHTML
    } catch (d) {
        b = goog.net.ErrorCode.ACCESS_DENIED
    }
    var e;
    !b && "function" == typeof this.errorChecker_ && (e = this.errorChecker_(a)) && (b = goog.net.ErrorCode.CUSTOM_ERROR);
    goog.log.log(this.logger_, goog.log.Level.FINER, "Last content: " + this.lastContent_);
    goog.log.log(this.logger_, goog.log.Level.FINER,
        "Last uri: " + this.lastUri_);
    b ? (goog.log.fine(this.logger_, "Load event occurred but failed"), this.handleError_(b, e)) : (goog.log.fine(this.logger_, "Load succeeded"), this.success_ = !0, this.lastErrorCode_ = goog.net.ErrorCode.NO_ERROR, this.dispatchEvent(goog.net.EventType.COMPLETE), this.dispatchEvent(goog.net.EventType.SUCCESS), this.makeReady_())
};
goog.net.IframeIo.prototype.handleError_ = function (a, b) {
    this.errorHandled_ || (this.active_ = this.success_ = !1, this.complete_ = !0, this.lastErrorCode_ = a, a == goog.net.ErrorCode.CUSTOM_ERROR && (this.lastCustomError_ = b), this.dispatchEvent(goog.net.EventType.COMPLETE), this.dispatchEvent(goog.net.EventType.ERROR), this.makeReady_(), this.errorHandled_ = !0)
};
goog.net.IframeIo.prototype.handleIncrementalData_ = function (a) {
    this.dispatchEvent(new goog.net.IframeIo.IncrementalDataEvent(a))
};
goog.net.IframeIo.prototype.makeReady_ = function () {
    goog.log.info(this.logger_, "Ready for new requests");
    this.scheduleIframeDisposal_();
    this.disposeForm_();
    this.dispatchEvent(goog.net.EventType.READY)
};
goog.net.IframeIo.prototype.createIframe_ = function () {
    goog.log.fine(this.logger_, "Creating iframe");
    this.iframeName_ = this.name_ + "_" + (this.nextIframeId_++).toString(36);
    var a = {name: this.iframeName_, id: this.iframeName_};
    goog.userAgent.IE && 7 > goog.userAgent.VERSION && (a.src = 'javascript:""');
    this.iframe_ = goog.dom.getDomHelper(this.form_).createDom("iframe", a);
    a = this.iframe_.style;
    a.visibility = "hidden";
    a.width = a.height = "10px";
    a.display = "none";
    goog.userAgent.WEBKIT ? a.marginTop = a.marginLeft = "-10px" : (a.position =
        "absolute", a.top = a.left = "-10px")
};
goog.net.IframeIo.prototype.appendIframe_ = function () {
    goog.dom.getDomHelper(this.form_).getDocument().body.appendChild(this.iframe_)
};
goog.net.IframeIo.prototype.scheduleIframeDisposal_ = function () {
    var a = this.iframe_;
    a && (a.onreadystatechange = null, a.onload = null, a.onerror = null, this.iframesForDisposal_.push(a));
    this.iframeDisposalTimer_ && (goog.Timer.clear(this.iframeDisposalTimer_), this.iframeDisposalTimer_ = null);
    goog.userAgent.GECKO || goog.userAgent.OPERA ? this.iframeDisposalTimer_ = goog.Timer.callOnce(this.disposeIframes_, goog.net.IframeIo.IFRAME_DISPOSE_DELAY_MS, this) : this.disposeIframes_();
    this.iframeName_ = this.iframe_ = null
};
goog.net.IframeIo.prototype.disposeIframes_ = function () {
    this.iframeDisposalTimer_ && (goog.Timer.clear(this.iframeDisposalTimer_), this.iframeDisposalTimer_ = null);
    for (; 0 != this.iframesForDisposal_.length;) {
        var a = this.iframesForDisposal_.pop();
        goog.log.info(this.logger_, "Disposing iframe");
        goog.dom.removeNode(a)
    }
};
goog.net.IframeIo.prototype.disposeForm_ = function () {
    this.form_ && this.form_ == goog.net.IframeIo.form_ && goog.dom.removeChildren(this.form_);
    this.form_ = null
};
goog.net.IframeIo.prototype.getContentDocument_ = function () {
    return this.iframe_ ? goog.dom.getFrameContentDocument(this.getRequestIframe()) : null
};
goog.net.IframeIo.prototype.getRequestIframe = function () {
    return this.iframe_ ? goog.userAgent.IE ? this.iframe_ : goog.dom.getFrameContentDocument(this.iframe_).getElementById(this.iframeName_ + goog.net.IframeIo.INNER_FRAME_SUFFIX) : null
};
goog.net.IframeIo.prototype.testForFirefoxSilentError_ = function () {
    if (this.active_) {
        var a = this.getContentDocument_();
        a && !goog.reflect.canAccessProperty(a, "documentUri") ? (this.ignoreResponse_ || goog.events.unlisten(this.getRequestIframe(), goog.events.EventType.LOAD, this.onIframeLoaded_, !1, this), navigator.onLine ? (goog.log.warning(this.logger_, "Silent Firefox error detected"), this.handleError_(goog.net.ErrorCode.FF_SILENT_ERROR)) : (goog.log.warning(this.logger_, "Firefox is offline so report offline error instead of silent error"),
            this.handleError_(goog.net.ErrorCode.OFFLINE))) : this.firefoxSilentErrorTimeout_ = goog.Timer.callOnce(this.testForFirefoxSilentError_, 250, this)
    }
};
goog.net.IframeIo.IncrementalDataEvent = function (a) {
    goog.events.Event.call(this, goog.net.EventType.INCREMENTAL_DATA);
    this.data = a
};
goog.inherits(goog.net.IframeIo.IncrementalDataEvent, goog.events.Event);
goog.ui.ToggleButton = function (a, b, c) {
    goog.ui.Button.call(this, a, b || goog.ui.CustomButtonRenderer.getInstance(), c);
    this.setSupportedState(goog.ui.Component.State.CHECKED, !0)
};
goog.inherits(goog.ui.ToggleButton, goog.ui.Button);
goog.ui.registry.setDecoratorByClassName("goog-toggle-button", function () {
    return new goog.ui.ToggleButton(null)
});
goog.ui.ac = {};
goog.ui.ac.AutoComplete = function (a, b, c) {
    goog.events.EventTarget.call(this);
    this.matcher_ = a;
    this.selectionHandler_ = c;
    this.renderer_ = b;
    goog.events.listen(b, [goog.ui.ac.AutoComplete.EventType.HILITE, goog.ui.ac.AutoComplete.EventType.SELECT, goog.ui.ac.AutoComplete.EventType.CANCEL_DISMISS, goog.ui.ac.AutoComplete.EventType.DISMISS], this.handleEvent, !1, this);
    this.token_ = null;
    this.rows_ = [];
    this.hiliteId_ = -1;
    this.firstRowId_ = 0;
    this.dismissTimer_ = this.target_ = null;
    this.inputToAnchorMap_ = {}
};
goog.inherits(goog.ui.ac.AutoComplete, goog.events.EventTarget);
goog.ui.ac.AutoComplete.prototype.maxMatches_ = 10;
goog.ui.ac.AutoComplete.prototype.autoHilite_ = !0;
goog.ui.ac.AutoComplete.prototype.allowFreeSelect_ = !1;
goog.ui.ac.AutoComplete.prototype.wrap_ = !1;
goog.ui.ac.AutoComplete.prototype.triggerSuggestionsOnUpdate_ = !1;
goog.ui.ac.AutoComplete.EventType = {ROW_HILITE: "rowhilite", HILITE: "hilite", SELECT: "select", DISMISS: "dismiss", CANCEL_DISMISS: "canceldismiss", UPDATE: "update", SUGGESTIONS_UPDATE: "suggestionsupdate"};
goog.ui.ac.AutoComplete.prototype.getMatcher = function () {
    return goog.asserts.assert(this.matcher_)
};
goog.ui.ac.AutoComplete.prototype.setMatcher = function (a) {
    this.matcher_ = a
};
goog.ui.ac.AutoComplete.prototype.getSelectionHandler = function () {
    return goog.asserts.assert(this.selectionHandler_)
};
goog.ui.ac.AutoComplete.prototype.getRenderer = function () {
    return this.renderer_
};
goog.ui.ac.AutoComplete.prototype.setRenderer = function (a) {
    this.renderer_ = a
};
goog.ui.ac.AutoComplete.prototype.getToken = function () {
    return this.token_
};
goog.ui.ac.AutoComplete.prototype.setTokenInternal = function (a) {
    this.token_ = a
};
goog.ui.ac.AutoComplete.prototype.getSuggestion = function (a) {
    return this.rows_[a]
};
goog.ui.ac.AutoComplete.prototype.getAllSuggestions = function () {
    return goog.asserts.assert(this.rows_)
};
goog.ui.ac.AutoComplete.prototype.getSuggestionCount = function () {
    return this.rows_.length
};
goog.ui.ac.AutoComplete.prototype.getHighlightedId = function () {
    return this.hiliteId_
};
goog.ui.ac.AutoComplete.prototype.setHighlightedIdInternal = function (a) {
    this.hiliteId_ = a
};
goog.ui.ac.AutoComplete.prototype.handleEvent = function (a) {
    if (a.target == this.renderer_)switch (a.type) {
        case goog.ui.ac.AutoComplete.EventType.HILITE:
            this.hiliteId(a.row);
            break;
        case goog.ui.ac.AutoComplete.EventType.SELECT:
            a = a.row;
            var b = this.getIndexOfId(a), b = this.rows_[b], c = !!b && this.matcher_.isRowDisabled && this.matcher_.isRowDisabled(b);
            a && (b && !c && this.hiliteId_ != a) && this.hiliteId(a);
            c || this.selectHilited();
            break;
        case goog.ui.ac.AutoComplete.EventType.CANCEL_DISMISS:
            this.cancelDelayedDismiss();
            break;
        case goog.ui.ac.AutoComplete.EventType.DISMISS:
            this.dismissOnDelay()
    }
};
goog.ui.ac.AutoComplete.prototype.setMaxMatches = function (a) {
    this.maxMatches_ = a
};
goog.ui.ac.AutoComplete.prototype.setAutoHilite = function (a) {
    this.autoHilite_ = a
};
goog.ui.ac.AutoComplete.prototype.setAllowFreeSelect = function (a) {
    this.allowFreeSelect_ = a
};
goog.ui.ac.AutoComplete.prototype.setWrap = function (a) {
    this.wrap_ = a
};
goog.ui.ac.AutoComplete.prototype.setTriggerSuggestionsOnUpdate = function (a) {
    this.triggerSuggestionsOnUpdate_ = a
};
goog.ui.ac.AutoComplete.prototype.setToken = function (a, b) {
    this.token_ != a && (this.token_ = a, this.matcher_.requestMatchingRows(this.token_, this.maxMatches_, goog.bind(this.matchListener_, this), b), this.cancelDelayedDismiss())
};
goog.ui.ac.AutoComplete.prototype.getTarget = function () {
    return this.target_
};
goog.ui.ac.AutoComplete.prototype.setTarget = function (a) {
    this.target_ = a
};
goog.ui.ac.AutoComplete.prototype.isOpen = function () {
    return this.renderer_.isVisible()
};
goog.ui.ac.AutoComplete.prototype.getRowCount = function () {
    return this.getSuggestionCount()
};
goog.ui.ac.AutoComplete.prototype.hiliteNext = function () {
    for (var a = this.firstRowId_ + this.rows_.length - 1, b = this.hiliteId_, c = 0; c < this.rows_.length; c++) {
        if (b >= this.firstRowId_ && b < a)b++; else if (-1 == b)b = this.firstRowId_; else if (this.allowFreeSelect_ && b == a) {
            this.hiliteId(-1);
            break
        } else if (this.wrap_ && b == a)b = this.firstRowId_; else break;
        if (this.hiliteId(b))return!0
    }
    return!1
};
goog.ui.ac.AutoComplete.prototype.hilitePrev = function () {
    for (var a = this.firstRowId_ + this.rows_.length - 1, b = this.hiliteId_, c = 0; c < this.rows_.length; c++) {
        if (b > this.firstRowId_)b--; else if (this.allowFreeSelect_ && b == this.firstRowId_) {
            this.hiliteId(-1);
            break
        } else if (!this.wrap_ || -1 != b && b != this.firstRowId_)break; else b = a;
        if (this.hiliteId(b))return!0
    }
    return!1
};
goog.ui.ac.AutoComplete.prototype.hiliteId = function (a) {
    var b = this.getIndexOfId(a), c = this.rows_[b];
    return c && this.matcher_.isRowDisabled && this.matcher_.isRowDisabled(c) ? !1 : (this.hiliteId_ = a, this.renderer_.hiliteId(a), -1 != b)
};
goog.ui.ac.AutoComplete.prototype.hiliteIndex = function (a) {
    return this.hiliteId(this.getIdOfIndex_(a))
};
goog.ui.ac.AutoComplete.prototype.selectHilited = function () {
    var a = this.getIndexOfId(this.hiliteId_);
    if (-1 != a) {
        var a = this.rows_[a], b = this.selectionHandler_.selectRow(a);
        this.triggerSuggestionsOnUpdate_ ? (this.token_ = null, this.dismissOnDelay()) : this.dismiss();
        b || (this.dispatchEvent({type: goog.ui.ac.AutoComplete.EventType.UPDATE, row: a}), this.triggerSuggestionsOnUpdate_ && this.selectionHandler_.update(!0));
        return!0
    }
    this.dismiss();
    this.dispatchEvent({type: goog.ui.ac.AutoComplete.EventType.UPDATE, row: null});
    return!1
};
goog.ui.ac.AutoComplete.prototype.hasHighlight = function () {
    return this.isOpen() && -1 != this.getIndexOfId(this.hiliteId_)
};
goog.ui.ac.AutoComplete.prototype.dismiss = function () {
    this.hiliteId_ = -1;
    this.token_ = null;
    this.firstRowId_ += this.rows_.length;
    this.rows_ = [];
    window.clearTimeout(this.dismissTimer_);
    this.dismissTimer_ = null;
    this.renderer_.dismiss();
    this.dispatchEvent(goog.ui.ac.AutoComplete.EventType.SUGGESTIONS_UPDATE);
    this.dispatchEvent(goog.ui.ac.AutoComplete.EventType.DISMISS)
};
goog.ui.ac.AutoComplete.prototype.dismissOnDelay = function () {
    this.dismissTimer_ || (this.dismissTimer_ = window.setTimeout(goog.bind(this.dismiss, this), 100))
};
goog.ui.ac.AutoComplete.prototype.immediatelyCancelDelayedDismiss_ = function () {
    return this.dismissTimer_ ? (window.clearTimeout(this.dismissTimer_), this.dismissTimer_ = null, !0) : !1
};
goog.ui.ac.AutoComplete.prototype.cancelDelayedDismiss = function () {
    this.immediatelyCancelDelayedDismiss_() || window.setTimeout(goog.bind(this.immediatelyCancelDelayedDismiss_, this), 10)
};
goog.ui.ac.AutoComplete.prototype.disposeInternal = function () {
    goog.ui.ac.AutoComplete.superClass_.disposeInternal.call(this);
    delete this.inputToAnchorMap_;
    this.renderer_.dispose();
    this.selectionHandler_.dispose();
    this.matcher_ = null
};
goog.ui.ac.AutoComplete.prototype.matchListener_ = function (a, b, c) {
    this.token_ == a && this.renderRows(b, c)
};
goog.ui.ac.AutoComplete.prototype.renderRows = function (a, b) {
    var c = "object" == goog.typeOf(b) && b, d = (c ? c.getPreserveHilited() : b) ? this.getIndexOfId(this.hiliteId_) : -1;
    this.firstRowId_ += this.rows_.length;
    this.rows_ = a;
    for (var e = [], f = 0; f < a.length; ++f)e.push({id: this.getIdOfIndex_(f), data: a[f]});
    f = null;
    this.target_ && (f = this.inputToAnchorMap_[goog.getUid(this.target_)] || this.target_);
    this.renderer_.setAnchorElement(f);
    this.renderer_.renderRows(e, this.token_, this.target_);
    f = this.autoHilite_;
    c && void 0 !== c.getAutoHilite() &&
    (f = c.getAutoHilite());
    this.hiliteId_ = -1;
    (f || 0 <= d) && (0 != e.length && this.token_) && (0 <= d ? this.hiliteId(this.getIdOfIndex_(d)) : this.hiliteNext());
    this.dispatchEvent(goog.ui.ac.AutoComplete.EventType.SUGGESTIONS_UPDATE)
};
goog.ui.ac.AutoComplete.prototype.getIndexOfId = function (a) {
    a -= this.firstRowId_;
    return 0 > a || a >= this.rows_.length ? -1 : a
};
goog.ui.ac.AutoComplete.prototype.getIdOfIndex_ = function (a) {
    return this.firstRowId_ + a
};
goog.ui.ac.AutoComplete.prototype.attachInputs = function (a) {
    var b = this.selectionHandler_;
    b.attachInputs.apply(b, arguments)
};
goog.ui.ac.AutoComplete.prototype.detachInputs = function (a) {
    var b = this.selectionHandler_;
    b.detachInputs.apply(b, arguments);
    goog.array.forEach(arguments, function (a) {
        goog.object.remove(this.inputToAnchorMap_, goog.getUid(a))
    }, this)
};
goog.ui.ac.AutoComplete.prototype.attachInputWithAnchor = function (a, b) {
    this.inputToAnchorMap_[goog.getUid(a)] = b;
    this.attachInputs(a)
};
goog.ui.ac.AutoComplete.prototype.update = function (a) {
    this.selectionHandler_.update(a)
};
goog.ui.ac.ArrayMatcher = function (a, b) {
    this.rows_ = a;
    this.useSimilar_ = !b
};
goog.ui.ac.ArrayMatcher.prototype.setRows = function (a) {
    this.rows_ = a
};
goog.ui.ac.ArrayMatcher.prototype.requestMatchingRows = function (a, b, c, d) {
    d = this.getPrefixMatches(a, b);
    0 == d.length && this.useSimilar_ && (d = this.getSimilarRows(a, b));
    c(a, d)
};
goog.ui.ac.ArrayMatcher.prototype.getPrefixMatches = function (a, b) {
    var c = [];
    if ("" != a)for (var d = goog.string.regExpEscape(a), d = RegExp("(^|\\W+)" + d, "i"), e = 0; e < this.rows_.length && c.length < b; e++) {
        var f = this.rows_[e];
        String(f).match(d) && c.push(f)
    }
    return c
};
goog.ui.ac.ArrayMatcher.prototype.getSimilarRows = function (a, b) {
    for (var c = [], d = 0; d < this.rows_.length; d++) {
        var e = this.rows_[d], f = a.toLowerCase(), g = String(e).toLowerCase(), h = 0;
        if (-1 != g.indexOf(f))h = parseInt((g.indexOf(f) / 4).toString(), 10); else for (var k = f.split(""), l = -1, m = 10, n = 0, p; p = k[n]; n++)p = g.indexOf(p), p > l ? (l = p - l - 1, l > m - 5 && (l = m - 5), h += l, l = p) : (h += m, m += 5);
        h < 6 * f.length && c.push({str: e, score: h, index: d})
    }
    c.sort(function (a, b) {
        var c = a.score - b.score;
        return 0 != c ? c : a.index - b.index
    });
    d = [];
    for (n = 0; n < b && n < c.length; n++)d.push(c[n].str);
    return d
};
goog.positioning = {};
goog.positioning.Corner = {TOP_LEFT: 0, TOP_RIGHT: 2, BOTTOM_LEFT: 1, BOTTOM_RIGHT: 3, TOP_START: 4, TOP_END: 6, BOTTOM_START: 5, BOTTOM_END: 7};
goog.positioning.CornerBit = {BOTTOM: 1, RIGHT: 2, FLIP_RTL: 4};
goog.positioning.Overflow = {IGNORE: 0, ADJUST_X: 1, FAIL_X: 2, ADJUST_Y: 4, FAIL_Y: 8, RESIZE_WIDTH: 16, RESIZE_HEIGHT: 32, ADJUST_X_EXCEPT_OFFSCREEN: 65, ADJUST_Y_EXCEPT_OFFSCREEN: 132};
goog.positioning.OverflowStatus = {NONE: 0, ADJUSTED_X: 1, ADJUSTED_Y: 2, WIDTH_ADJUSTED: 4, HEIGHT_ADJUSTED: 8, FAILED_LEFT: 16, FAILED_RIGHT: 32, FAILED_TOP: 64, FAILED_BOTTOM: 128, FAILED_OUTSIDE_VIEWPORT: 256};
goog.positioning.OverflowStatus.FAILED = goog.positioning.OverflowStatus.FAILED_LEFT | goog.positioning.OverflowStatus.FAILED_RIGHT | goog.positioning.OverflowStatus.FAILED_TOP | goog.positioning.OverflowStatus.FAILED_BOTTOM | goog.positioning.OverflowStatus.FAILED_OUTSIDE_VIEWPORT;
goog.positioning.OverflowStatus.FAILED_HORIZONTAL = goog.positioning.OverflowStatus.FAILED_LEFT | goog.positioning.OverflowStatus.FAILED_RIGHT;
goog.positioning.OverflowStatus.FAILED_VERTICAL = goog.positioning.OverflowStatus.FAILED_TOP | goog.positioning.OverflowStatus.FAILED_BOTTOM;
goog.positioning.positionAtAnchor = function (a, b, c, d, e, f, g, h, k) {
    goog.asserts.assert(c);
    var l = goog.positioning.getOffsetParentPageOffset(c), m = goog.positioning.getVisiblePart_(a);
    goog.style.translateRectForAnotherFrame(m, goog.dom.getDomHelper(a), goog.dom.getDomHelper(c));
    a = goog.positioning.getEffectiveCorner(a, b);
    m = new goog.math.Coordinate(a & goog.positioning.CornerBit.RIGHT ? m.left + m.width : m.left, a & goog.positioning.CornerBit.BOTTOM ? m.top + m.height : m.top);
    m = goog.math.Coordinate.difference(m, l);
    e && (m.x +=
        (a & goog.positioning.CornerBit.RIGHT ? -1 : 1) * e.x, m.y += (a & goog.positioning.CornerBit.BOTTOM ? -1 : 1) * e.y);
    var n;
    if (g)if (k)n = k; else if (n = goog.style.getVisibleRectForElement(c))n.top -= l.y, n.right -= l.x, n.bottom -= l.y, n.left -= l.x;
    return goog.positioning.positionAtCoordinate(m, c, d, f, n, g, h)
};
goog.positioning.getOffsetParentPageOffset = function (a) {
    var b;
    if (a = a.offsetParent) {
        var c = a.tagName == goog.dom.TagName.HTML || a.tagName == goog.dom.TagName.BODY;
        c && "static" == goog.style.getComputedPosition(a) || (b = goog.style.getPageOffset(a), c || (b = goog.math.Coordinate.difference(b, new goog.math.Coordinate(goog.style.bidi.getScrollLeft(a), a.scrollTop))))
    }
    return b || new goog.math.Coordinate
};
goog.positioning.getVisiblePart_ = function (a) {
    var b = goog.style.getBounds(a);
    (a = goog.style.getVisibleRectForElement(a)) && b.intersection(goog.math.Rect.createFromBox(a));
    return b
};
goog.positioning.positionAtCoordinate = function (a, b, c, d, e, f, g) {
    a = a.clone();
    var h = goog.positioning.OverflowStatus.NONE;
    c = goog.positioning.getEffectiveCorner(b, c);
    var k = goog.style.getSize(b);
    g = g ? g.clone() : k.clone();
    if (d || c != goog.positioning.Corner.TOP_LEFT)c & goog.positioning.CornerBit.RIGHT ? a.x -= g.width + (d ? d.right : 0) : d && (a.x += d.left), c & goog.positioning.CornerBit.BOTTOM ? a.y -= g.height + (d ? d.bottom : 0) : d && (a.y += d.top);
    if (f && (h = e ? goog.positioning.adjustForViewport_(a, g, e, f) : goog.positioning.OverflowStatus.FAILED_OUTSIDE_VIEWPORT,
        h & goog.positioning.OverflowStatus.FAILED))return h;
    goog.style.setPosition(b, a);
    goog.math.Size.equals(k, g) || goog.style.setBorderBoxSize(b, g);
    return h
};
goog.positioning.adjustForViewport_ = function (a, b, c, d) {
    var e = goog.positioning.OverflowStatus.NONE, f = goog.positioning.Overflow.ADJUST_X_EXCEPT_OFFSCREEN, g = goog.positioning.Overflow.ADJUST_Y_EXCEPT_OFFSCREEN;
    (d & f) == f && (a.x < c.left || a.x >= c.right) && (d &= ~goog.positioning.Overflow.ADJUST_X);
    (d & g) == g && (a.y < c.top || a.y >= c.bottom) && (d &= ~goog.positioning.Overflow.ADJUST_Y);
    a.x < c.left && d & goog.positioning.Overflow.ADJUST_X && (a.x = c.left, e |= goog.positioning.OverflowStatus.ADJUSTED_X);
    a.x < c.left && (a.x + b.width > c.right &&
        d & goog.positioning.Overflow.RESIZE_WIDTH) && (b.width = Math.max(b.width - (a.x + b.width - c.right), 0), e |= goog.positioning.OverflowStatus.WIDTH_ADJUSTED);
    a.x + b.width > c.right && d & goog.positioning.Overflow.ADJUST_X && (a.x = Math.max(c.right - b.width, c.left), e |= goog.positioning.OverflowStatus.ADJUSTED_X);
    d & goog.positioning.Overflow.FAIL_X && (e |= (a.x < c.left ? goog.positioning.OverflowStatus.FAILED_LEFT : 0) | (a.x + b.width > c.right ? goog.positioning.OverflowStatus.FAILED_RIGHT : 0));
    a.y < c.top && d & goog.positioning.Overflow.ADJUST_Y &&
    (a.y = c.top, e |= goog.positioning.OverflowStatus.ADJUSTED_Y);
    a.y <= c.top && (a.y + b.height < c.bottom && d & goog.positioning.Overflow.RESIZE_HEIGHT) && (b.height = Math.max(b.height - (c.top - a.y), 0), a.y = c.top, e |= goog.positioning.OverflowStatus.HEIGHT_ADJUSTED);
    a.y >= c.top && (a.y + b.height > c.bottom && d & goog.positioning.Overflow.RESIZE_HEIGHT) && (b.height = Math.max(b.height - (a.y + b.height - c.bottom), 0), e |= goog.positioning.OverflowStatus.HEIGHT_ADJUSTED);
    a.y + b.height > c.bottom && d & goog.positioning.Overflow.ADJUST_Y && (a.y = Math.max(c.bottom -
        b.height, c.top), e |= goog.positioning.OverflowStatus.ADJUSTED_Y);
    d & goog.positioning.Overflow.FAIL_Y && (e |= (a.y < c.top ? goog.positioning.OverflowStatus.FAILED_TOP : 0) | (a.y + b.height > c.bottom ? goog.positioning.OverflowStatus.FAILED_BOTTOM : 0));
    return e
};
goog.positioning.getEffectiveCorner = function (a, b) {
    return(b & goog.positioning.CornerBit.FLIP_RTL && goog.style.isRightToLeft(a) ? b ^ goog.positioning.CornerBit.RIGHT : b) & ~goog.positioning.CornerBit.FLIP_RTL
};
goog.positioning.flipCornerHorizontal = function (a) {
    return a ^ goog.positioning.CornerBit.RIGHT
};
goog.positioning.flipCornerVertical = function (a) {
    return a ^ goog.positioning.CornerBit.BOTTOM
};
goog.positioning.flipCorner = function (a) {
    return a ^ goog.positioning.CornerBit.BOTTOM ^ goog.positioning.CornerBit.RIGHT
};
goog.async = {};
goog.async.Delay = function (a, b, c) {
    goog.Disposable.call(this);
    this.listener_ = a;
    this.interval_ = b || 0;
    this.handler_ = c;
    this.callback_ = goog.bind(this.doAction_, this)
};
goog.inherits(goog.async.Delay, goog.Disposable);
goog.Delay = goog.async.Delay;
goog.async.Delay.prototype.id_ = 0;
goog.async.Delay.prototype.disposeInternal = function () {
    goog.async.Delay.superClass_.disposeInternal.call(this);
    this.stop();
    delete this.listener_;
    delete this.handler_
};
goog.async.Delay.prototype.start = function (a) {
    this.stop();
    this.id_ = goog.Timer.callOnce(this.callback_, goog.isDef(a) ? a : this.interval_)
};
goog.async.Delay.prototype.stop = function () {
    this.isActive() && goog.Timer.clear(this.id_);
    this.id_ = 0
};
goog.async.Delay.prototype.fire = function () {
    this.stop();
    this.doAction_()
};
goog.async.Delay.prototype.fireIfActive = function () {
    this.isActive() && this.fire()
};
goog.async.Delay.prototype.isActive = function () {
    return 0 != this.id_
};
goog.async.Delay.prototype.doAction_ = function () {
    this.id_ = 0;
    this.listener_ && this.listener_.call(this.handler_)
};
goog.functions = {};
goog.functions.constant = function (a) {
    return function () {
        return a
    }
};
goog.functions.FALSE = goog.functions.constant(!1);
goog.functions.TRUE = goog.functions.constant(!0);
goog.functions.NULL = goog.functions.constant(null);
goog.functions.identity = function (a, b) {
    return a
};
goog.functions.error = function (a) {
    return function () {
        throw Error(a);
    }
};
goog.functions.fail = function (a) {
    return function () {
        throw a;
    }
};
goog.functions.lock = function (a, b) {
    b = b || 0;
    return function () {
        return a.apply(this, Array.prototype.slice.call(arguments, 0, b))
    }
};
goog.functions.withReturnValue = function (a, b) {
    return goog.functions.sequence(a, goog.functions.constant(b))
};
goog.functions.compose = function (a, b) {
    var c = arguments, d = c.length;
    return function () {
        var a;
        d && (a = c[d - 1].apply(this, arguments));
        for (var b = d - 2; 0 <= b; b--)a = c[b].call(this, a);
        return a
    }
};
goog.functions.sequence = function (a) {
    var b = arguments, c = b.length;
    return function () {
        for (var a, e = 0; e < c; e++)a = b[e].apply(this, arguments);
        return a
    }
};
goog.functions.and = function (a) {
    var b = arguments, c = b.length;
    return function () {
        for (var a = 0; a < c; a++)if (!b[a].apply(this, arguments))return!1;
        return!0
    }
};
goog.functions.or = function (a) {
    var b = arguments, c = b.length;
    return function () {
        for (var a = 0; a < c; a++)if (b[a].apply(this, arguments))return!0;
        return!1
    }
};
goog.functions.not = function (a) {
    return function () {
        return!a.apply(this, arguments)
    }
};
goog.functions.create = function (a, b) {
    var c = function () {
    };
    c.prototype = a.prototype;
    c = new c;
    a.apply(c, Array.prototype.slice.call(arguments, 1));
    return c
};
goog.async.AnimationDelay = function (a, b, c) {
    goog.Disposable.call(this);
    this.listener_ = a;
    this.handler_ = c;
    this.win_ = b || window;
    this.callback_ = goog.bind(this.doAction_, this)
};
goog.inherits(goog.async.AnimationDelay, goog.Disposable);
goog.async.AnimationDelay.prototype.id_ = null;
goog.async.AnimationDelay.prototype.usingListeners_ = !1;
goog.async.AnimationDelay.TIMEOUT = 20;
goog.async.AnimationDelay.MOZ_BEFORE_PAINT_EVENT_ = "MozBeforePaint";
goog.async.AnimationDelay.prototype.start = function () {
    this.stop();
    this.usingListeners_ = !1;
    var a = this.getRaf_(), b = this.getCancelRaf_();
    a && !b && this.win_.mozRequestAnimationFrame ? (this.id_ = goog.events.listen(this.win_, goog.async.AnimationDelay.MOZ_BEFORE_PAINT_EVENT_, this.callback_), this.win_.mozRequestAnimationFrame(null), this.usingListeners_ = !0) : this.id_ = a && b ? a.call(this.win_, this.callback_) : this.win_.setTimeout(goog.functions.lock(this.callback_), goog.async.AnimationDelay.TIMEOUT)
};
goog.async.AnimationDelay.prototype.stop = function () {
    if (this.isActive()) {
        var a = this.getRaf_(), b = this.getCancelRaf_();
        a && !b && this.win_.mozRequestAnimationFrame ? goog.events.unlistenByKey(this.id_) : a && b ? b.call(this.win_, this.id_) : this.win_.clearTimeout(this.id_)
    }
    this.id_ = null
};
goog.async.AnimationDelay.prototype.fire = function () {
    this.stop();
    this.doAction_()
};
goog.async.AnimationDelay.prototype.fireIfActive = function () {
    this.isActive() && this.fire()
};
goog.async.AnimationDelay.prototype.isActive = function () {
    return null != this.id_
};
goog.async.AnimationDelay.prototype.doAction_ = function () {
    this.usingListeners_ && this.id_ && goog.events.unlistenByKey(this.id_);
    this.id_ = null;
    this.listener_.call(this.handler_, goog.now())
};
goog.async.AnimationDelay.prototype.disposeInternal = function () {
    this.stop();
    goog.async.AnimationDelay.superClass_.disposeInternal.call(this)
};
goog.async.AnimationDelay.prototype.getRaf_ = function () {
    var a = this.win_;
    return a.requestAnimationFrame || a.webkitRequestAnimationFrame || a.mozRequestAnimationFrame || a.oRequestAnimationFrame || a.msRequestAnimationFrame || null
};
goog.async.AnimationDelay.prototype.getCancelRaf_ = function () {
    var a = this.win_;
    return a.cancelRequestAnimationFrame || a.webkitCancelRequestAnimationFrame || a.mozCancelRequestAnimationFrame || a.oCancelRequestAnimationFrame || a.msCancelRequestAnimationFrame || null
};
goog.fx.anim = {};
goog.fx.anim.Animated = function () {
};
goog.fx.anim.TIMEOUT = goog.async.AnimationDelay.TIMEOUT;
goog.fx.anim.activeAnimations_ = {};
goog.fx.anim.animationWindow_ = null;
goog.fx.anim.animationDelay_ = null;
goog.fx.anim.registerAnimation = function (a) {
    var b = goog.getUid(a);
    b in goog.fx.anim.activeAnimations_ || (goog.fx.anim.activeAnimations_[b] = a);
    goog.fx.anim.requestAnimationFrame_()
};
goog.fx.anim.unregisterAnimation = function (a) {
    a = goog.getUid(a);
    delete goog.fx.anim.activeAnimations_[a];
    goog.object.isEmpty(goog.fx.anim.activeAnimations_) && goog.fx.anim.cancelAnimationFrame_()
};
goog.fx.anim.tearDown = function () {
    goog.fx.anim.animationWindow_ = null;
    goog.dispose(goog.fx.anim.animationDelay_);
    goog.fx.anim.animationDelay_ = null;
    goog.fx.anim.activeAnimations_ = {}
};
goog.fx.anim.setAnimationWindow = function (a) {
    var b = goog.fx.anim.animationDelay_ && goog.fx.anim.animationDelay_.isActive();
    goog.dispose(goog.fx.anim.animationDelay_);
    goog.fx.anim.animationDelay_ = null;
    goog.fx.anim.animationWindow_ = a;
    b && goog.fx.anim.requestAnimationFrame_()
};
goog.fx.anim.requestAnimationFrame_ = function () {
    goog.fx.anim.animationDelay_ || (goog.fx.anim.animationDelay_ = goog.fx.anim.animationWindow_ ? new goog.async.AnimationDelay(function (a) {
        goog.fx.anim.cycleAnimations_(a)
    }, goog.fx.anim.animationWindow_) : new goog.async.Delay(function () {
        goog.fx.anim.cycleAnimations_(goog.now())
    }, goog.fx.anim.TIMEOUT));
    var a = goog.fx.anim.animationDelay_;
    a.isActive() || a.start()
};
goog.fx.anim.cancelAnimationFrame_ = function () {
    goog.fx.anim.animationDelay_ && goog.fx.anim.animationDelay_.stop()
};
goog.fx.anim.cycleAnimations_ = function (a) {
    goog.object.forEach(goog.fx.anim.activeAnimations_, function (b) {
        b.onAnimationFrame(a)
    });
    goog.object.isEmpty(goog.fx.anim.activeAnimations_) || goog.fx.anim.requestAnimationFrame_()
};
goog.fx.TransitionBase = function () {
    goog.events.EventTarget.call(this);
    this.state_ = goog.fx.TransitionBase.State.STOPPED;
    this.endTime = this.startTime = null
};
goog.inherits(goog.fx.TransitionBase, goog.events.EventTarget);
goog.fx.TransitionBase.State = {STOPPED: 0, PAUSED: -1, PLAYING: 1};
goog.fx.TransitionBase.prototype.getStateInternal = function () {
    return this.state_
};
goog.fx.TransitionBase.prototype.setStatePlaying = function () {
    this.state_ = goog.fx.TransitionBase.State.PLAYING
};
goog.fx.TransitionBase.prototype.setStatePaused = function () {
    this.state_ = goog.fx.TransitionBase.State.PAUSED
};
goog.fx.TransitionBase.prototype.setStateStopped = function () {
    this.state_ = goog.fx.TransitionBase.State.STOPPED
};
goog.fx.TransitionBase.prototype.isPlaying = function () {
    return this.state_ == goog.fx.TransitionBase.State.PLAYING
};
goog.fx.TransitionBase.prototype.isPaused = function () {
    return this.state_ == goog.fx.TransitionBase.State.PAUSED
};
goog.fx.TransitionBase.prototype.isStopped = function () {
    return this.state_ == goog.fx.TransitionBase.State.STOPPED
};
goog.fx.TransitionBase.prototype.onBegin = function () {
    this.dispatchAnimationEvent(goog.fx.Transition.EventType.BEGIN)
};
goog.fx.TransitionBase.prototype.onEnd = function () {
    this.dispatchAnimationEvent(goog.fx.Transition.EventType.END)
};
goog.fx.TransitionBase.prototype.onFinish = function () {
    this.dispatchAnimationEvent(goog.fx.Transition.EventType.FINISH)
};
goog.fx.TransitionBase.prototype.onPause = function () {
    this.dispatchAnimationEvent(goog.fx.Transition.EventType.PAUSE)
};
goog.fx.TransitionBase.prototype.onPlay = function () {
    this.dispatchAnimationEvent(goog.fx.Transition.EventType.PLAY)
};
goog.fx.TransitionBase.prototype.onResume = function () {
    this.dispatchAnimationEvent(goog.fx.Transition.EventType.RESUME)
};
goog.fx.TransitionBase.prototype.onStop = function () {
    this.dispatchAnimationEvent(goog.fx.Transition.EventType.STOP)
};
goog.fx.TransitionBase.prototype.dispatchAnimationEvent = function (a) {
    this.dispatchEvent(a)
};
goog.fx.Animation = function (a, b, c, d) {
    goog.fx.TransitionBase.call(this);
    if (!goog.isArray(a) || !goog.isArray(b))throw Error("Start and end parameters must be arrays");
    if (a.length != b.length)throw Error("Start and end points must be the same length");
    this.startPoint = a;
    this.endPoint = b;
    this.duration = c;
    this.accel_ = d;
    this.coords = [];
    this.useRightPositioningForRtl_ = !1
};
goog.inherits(goog.fx.Animation, goog.fx.TransitionBase);
goog.fx.Animation.prototype.enableRightPositioningForRtl = function (a) {
    this.useRightPositioningForRtl_ = a
};
goog.fx.Animation.prototype.isRightPositioningForRtlEnabled = function () {
    return this.useRightPositioningForRtl_
};
goog.fx.Animation.EventType = {PLAY: goog.fx.Transition.EventType.PLAY, BEGIN: goog.fx.Transition.EventType.BEGIN, RESUME: goog.fx.Transition.EventType.RESUME, END: goog.fx.Transition.EventType.END, STOP: goog.fx.Transition.EventType.STOP, FINISH: goog.fx.Transition.EventType.FINISH, PAUSE: goog.fx.Transition.EventType.PAUSE, ANIMATE: "animate", DESTROY: "destroy"};
goog.fx.Animation.TIMEOUT = goog.fx.anim.TIMEOUT;
goog.fx.Animation.State = goog.fx.TransitionBase.State;
goog.fx.Animation.setAnimationWindow = function (a) {
    goog.fx.anim.setAnimationWindow(a)
};
goog.fx.Animation.prototype.fps_ = 0;
goog.fx.Animation.prototype.progress = 0;
goog.fx.Animation.prototype.lastFrame = null;
goog.fx.Animation.prototype.play = function (a) {
    if (a || this.isStopped())this.progress = 0, this.coords = this.startPoint; else if (this.isPlaying())return!1;
    goog.fx.anim.unregisterAnimation(this);
    this.startTime = a = goog.now();
    this.isPaused() && (this.startTime -= this.duration * this.progress);
    this.endTime = this.startTime + this.duration;
    this.lastFrame = this.startTime;
    if (!this.progress)this.onBegin();
    this.onPlay();
    if (this.isPaused())this.onResume();
    this.setStatePlaying();
    goog.fx.anim.registerAnimation(this);
    this.cycle(a);
    return!0
};
goog.fx.Animation.prototype.stop = function (a) {
    goog.fx.anim.unregisterAnimation(this);
    this.setStateStopped();
    a && (this.progress = 1);
    this.updateCoords_(this.progress);
    this.onStop();
    this.onEnd()
};
goog.fx.Animation.prototype.pause = function () {
    this.isPlaying() && (goog.fx.anim.unregisterAnimation(this), this.setStatePaused(), this.onPause())
};
goog.fx.Animation.prototype.getProgress = function () {
    return this.progress
};
goog.fx.Animation.prototype.setProgress = function (a) {
    this.progress = a;
    this.isPlaying() && (this.startTime = goog.now() - this.duration * this.progress, this.endTime = this.startTime + this.duration)
};
goog.fx.Animation.prototype.disposeInternal = function () {
    this.isStopped() || this.stop(!1);
    this.onDestroy();
    goog.fx.Animation.superClass_.disposeInternal.call(this)
};
goog.fx.Animation.prototype.destroy = function () {
    this.dispose()
};
goog.fx.Animation.prototype.onAnimationFrame = function (a) {
    this.cycle(a)
};
goog.fx.Animation.prototype.cycle = function (a) {
    this.progress = (a - this.startTime) / (this.endTime - this.startTime);
    1 <= this.progress && (this.progress = 1);
    this.fps_ = 1E3 / (a - this.lastFrame);
    this.lastFrame = a;
    this.updateCoords_(this.progress);
    if (1 == this.progress)this.setStateStopped(), goog.fx.anim.unregisterAnimation(this), this.onFinish(), this.onEnd(); else if (this.isPlaying())this.onAnimate()
};
goog.fx.Animation.prototype.updateCoords_ = function (a) {
    goog.isFunction(this.accel_) && (a = this.accel_(a));
    this.coords = Array(this.startPoint.length);
    for (var b = 0; b < this.startPoint.length; b++)this.coords[b] = (this.endPoint[b] - this.startPoint[b]) * a + this.startPoint[b]
};
goog.fx.Animation.prototype.onAnimate = function () {
    this.dispatchAnimationEvent(goog.fx.Animation.EventType.ANIMATE)
};
goog.fx.Animation.prototype.onDestroy = function () {
    this.dispatchAnimationEvent(goog.fx.Animation.EventType.DESTROY)
};
goog.fx.Animation.prototype.dispatchAnimationEvent = function (a) {
    this.dispatchEvent(new goog.fx.AnimationEvent(a, this))
};
goog.fx.AnimationEvent = function (a, b) {
    goog.events.Event.call(this, a);
    this.coords = b.coords;
    this.x = b.coords[0];
    this.y = b.coords[1];
    this.z = b.coords[2];
    this.duration = b.duration;
    this.progress = b.getProgress();
    this.fps = b.fps_;
    this.state = b.getStateInternal();
    this.anim = b
};
goog.inherits(goog.fx.AnimationEvent, goog.events.Event);
goog.fx.AnimationEvent.prototype.coordsAsInts = function () {
    return goog.array.map(this.coords, Math.round)
};
goog.color = {};
goog.color.names = {aliceblue: "#f0f8ff", antiquewhite: "#faebd7", aqua: "#00ffff", aquamarine: "#7fffd4", azure: "#f0ffff", beige: "#f5f5dc", bisque: "#ffe4c4", black: "#000000", blanchedalmond: "#ffebcd", blue: "#0000ff", blueviolet: "#8a2be2", brown: "#a52a2a", burlywood: "#deb887", cadetblue: "#5f9ea0", chartreuse: "#7fff00", chocolate: "#d2691e", coral: "#ff7f50", cornflowerblue: "#6495ed", cornsilk: "#fff8dc", crimson: "#dc143c", cyan: "#00ffff", darkblue: "#00008b", darkcyan: "#008b8b", darkgoldenrod: "#b8860b", darkgray: "#a9a9a9", darkgreen: "#006400",
    darkgrey: "#a9a9a9", darkkhaki: "#bdb76b", darkmagenta: "#8b008b", darkolivegreen: "#556b2f", darkorange: "#ff8c00", darkorchid: "#9932cc", darkred: "#8b0000", darksalmon: "#e9967a", darkseagreen: "#8fbc8f", darkslateblue: "#483d8b", darkslategray: "#2f4f4f", darkslategrey: "#2f4f4f", darkturquoise: "#00ced1", darkviolet: "#9400d3", deeppink: "#ff1493", deepskyblue: "#00bfff", dimgray: "#696969", dimgrey: "#696969", dodgerblue: "#1e90ff", firebrick: "#b22222", floralwhite: "#fffaf0", forestgreen: "#228b22", fuchsia: "#ff00ff", gainsboro: "#dcdcdc",
    ghostwhite: "#f8f8ff", gold: "#ffd700", goldenrod: "#daa520", gray: "#808080", green: "#008000", greenyellow: "#adff2f", grey: "#808080", honeydew: "#f0fff0", hotpink: "#ff69b4", indianred: "#cd5c5c", indigo: "#4b0082", ivory: "#fffff0", khaki: "#f0e68c", lavender: "#e6e6fa", lavenderblush: "#fff0f5", lawngreen: "#7cfc00", lemonchiffon: "#fffacd", lightblue: "#add8e6", lightcoral: "#f08080", lightcyan: "#e0ffff", lightgoldenrodyellow: "#fafad2", lightgray: "#d3d3d3", lightgreen: "#90ee90", lightgrey: "#d3d3d3", lightpink: "#ffb6c1", lightsalmon: "#ffa07a",
    lightseagreen: "#20b2aa", lightskyblue: "#87cefa", lightslategray: "#778899", lightslategrey: "#778899", lightsteelblue: "#b0c4de", lightyellow: "#ffffe0", lime: "#00ff00", limegreen: "#32cd32", linen: "#faf0e6", magenta: "#ff00ff", maroon: "#800000", mediumaquamarine: "#66cdaa", mediumblue: "#0000cd", mediumorchid: "#ba55d3", mediumpurple: "#9370db", mediumseagreen: "#3cb371", mediumslateblue: "#7b68ee", mediumspringgreen: "#00fa9a", mediumturquoise: "#48d1cc", mediumvioletred: "#c71585", midnightblue: "#191970", mintcream: "#f5fffa", mistyrose: "#ffe4e1",
    moccasin: "#ffe4b5", navajowhite: "#ffdead", navy: "#000080", oldlace: "#fdf5e6", olive: "#808000", olivedrab: "#6b8e23", orange: "#ffa500", orangered: "#ff4500", orchid: "#da70d6", palegoldenrod: "#eee8aa", palegreen: "#98fb98", paleturquoise: "#afeeee", palevioletred: "#db7093", papayawhip: "#ffefd5", peachpuff: "#ffdab9", peru: "#cd853f", pink: "#ffc0cb", plum: "#dda0dd", powderblue: "#b0e0e6", purple: "#800080", red: "#ff0000", rosybrown: "#bc8f8f", royalblue: "#4169e1", saddlebrown: "#8b4513", salmon: "#fa8072", sandybrown: "#f4a460", seagreen: "#2e8b57",
    seashell: "#fff5ee", sienna: "#a0522d", silver: "#c0c0c0", skyblue: "#87ceeb", slateblue: "#6a5acd", slategray: "#708090", slategrey: "#708090", snow: "#fffafa", springgreen: "#00ff7f", steelblue: "#4682b4", tan: "#d2b48c", teal: "#008080", thistle: "#d8bfd8", tomato: "#ff6347", turquoise: "#40e0d0", violet: "#ee82ee", wheat: "#f5deb3", white: "#ffffff", whitesmoke: "#f5f5f5", yellow: "#ffff00", yellowgreen: "#9acd32"};
goog.color.parse = function (a) {
    var b = {};
    a = String(a);
    var c = goog.color.prependHashIfNecessaryHelper(a);
    if (goog.color.isValidHexColor_(c))return b.hex = goog.color.normalizeHex(c), b.type = "hex", b;
    c = goog.color.isValidRgbColor_(a);
    if (c.length)return b.hex = goog.color.rgbArrayToHex(c), b.type = "rgb", b;
    if (goog.color.names && (c = goog.color.names[a.toLowerCase()]))return b.hex = c, b.type = "named", b;
    throw Error(a + " is not a valid color string");
};
goog.color.isValidColor = function (a) {
    var b = goog.color.prependHashIfNecessaryHelper(a);
    return!!(goog.color.isValidHexColor_(b) || goog.color.isValidRgbColor_(a).length || goog.color.names && goog.color.names[a.toLowerCase()])
};
goog.color.parseRgb = function (a) {
    var b = goog.color.isValidRgbColor_(a);
    if (!b.length)throw Error(a + " is not a valid RGB color");
    return b
};
goog.color.hexToRgbStyle = function (a) {
    return goog.color.rgbStyle_(goog.color.hexToRgb(a))
};
goog.color.hexTripletRe_ = /#(.)(.)(.)/;
goog.color.normalizeHex = function (a) {
    if (!goog.color.isValidHexColor_(a))throw Error("'" + a + "' is not a valid hex color");
    4 == a.length && (a = a.replace(goog.color.hexTripletRe_, "#$1$1$2$2$3$3"));
    return a.toLowerCase()
};
goog.color.hexToRgb = function (a) {
    a = goog.color.normalizeHex(a);
    var b = parseInt(a.substr(1, 2), 16), c = parseInt(a.substr(3, 2), 16);
    a = parseInt(a.substr(5, 2), 16);
    return[b, c, a]
};
goog.color.rgbToHex = function (a, b, c) {
    a = Number(a);
    b = Number(b);
    c = Number(c);
    if (isNaN(a) || 0 > a || 255 < a || isNaN(b) || 0 > b || 255 < b || isNaN(c) || 0 > c || 255 < c)throw Error('"(' + a + "," + b + "," + c + '") is not a valid RGB color');
    a = goog.color.prependZeroIfNecessaryHelper(a.toString(16));
    b = goog.color.prependZeroIfNecessaryHelper(b.toString(16));
    c = goog.color.prependZeroIfNecessaryHelper(c.toString(16));
    return"#" + a + b + c
};
goog.color.rgbArrayToHex = function (a) {
    return goog.color.rgbToHex(a[0], a[1], a[2])
};
goog.color.rgbToHsl = function (a, b, c) {
    a /= 255;
    b /= 255;
    c /= 255;
    var d = Math.max(a, b, c), e = Math.min(a, b, c), f = 0, g = 0, h = 0.5 * (d + e);
    d != e && (d == a ? f = 60 * (b - c) / (d - e) : d == b ? f = 60 * (c - a) / (d - e) + 120 : d == c && (f = 60 * (a - b) / (d - e) + 240), g = 0 < h && 0.5 >= h ? (d - e) / (2 * h) : (d - e) / (2 - 2 * h));
    return[Math.round(f + 360) % 360, g, h]
};
goog.color.rgbArrayToHsl = function (a) {
    return goog.color.rgbToHsl(a[0], a[1], a[2])
};
goog.color.hueToRgb_ = function (a, b, c) {
    0 > c ? c += 1 : 1 < c && (c -= 1);
    return 1 > 6 * c ? a + 6 * (b - a) * c : 1 > 2 * c ? b : 2 > 3 * c ? a + 6 * (b - a) * (2 / 3 - c) : a
};
goog.color.hslToRgb = function (a, b, c) {
    var d = 0, e = 0, f = 0;
    a /= 360;
    if (0 == b)d = e = f = 255 * c; else var g = f = 0, g = 0.5 > c ? c * (1 + b) : c + b - b * c, f = 2 * c - g, d = 255 * goog.color.hueToRgb_(f, g, a + 1 / 3), e = 255 * goog.color.hueToRgb_(f, g, a), f = 255 * goog.color.hueToRgb_(f, g, a - 1 / 3);
    return[Math.round(d), Math.round(e), Math.round(f)]
};
goog.color.hslArrayToRgb = function (a) {
    return goog.color.hslToRgb(a[0], a[1], a[2])
};
goog.color.validHexColorRe_ = /^#(?:[0-9a-f]{3}){1,2}$/i;
goog.color.isValidHexColor_ = function (a) {
    return goog.color.validHexColorRe_.test(a)
};
goog.color.normalizedHexColorRe_ = /^#[0-9a-f]{6}$/;
goog.color.isNormalizedHexColor_ = function (a) {
    return goog.color.normalizedHexColorRe_.test(a)
};
goog.color.rgbColorRe_ = /^(?:rgb)?\((0|[1-9]\d{0,2}),\s?(0|[1-9]\d{0,2}),\s?(0|[1-9]\d{0,2})\)$/i;
goog.color.isValidRgbColor_ = function (a) {
    var b = a.match(goog.color.rgbColorRe_);
    if (b) {
        a = Number(b[1]);
        var c = Number(b[2]), b = Number(b[3]);
        if (0 <= a && 255 >= a && 0 <= c && 255 >= c && 0 <= b && 255 >= b)return[a, c, b]
    }
    return[]
};
goog.color.prependZeroIfNecessaryHelper = function (a) {
    return 1 == a.length ? "0" + a : a
};
goog.color.prependHashIfNecessaryHelper = function (a) {
    return"#" == a.charAt(0) ? a : "#" + a
};
goog.color.rgbStyle_ = function (a) {
    return"rgb(" + a.join(",") + ")"
};
goog.color.hsvToRgb = function (a, b, c) {
    var d = 0, e = 0, f = 0;
    if (0 == b)f = e = d = c; else {
        var g = Math.floor(a / 60), h = a / 60 - g;
        a = c * (1 - b);
        var k = c * (1 - b * h);
        b = c * (1 - b * (1 - h));
        switch (g) {
            case 1:
                d = k;
                e = c;
                f = a;
                break;
            case 2:
                d = a;
                e = c;
                f = b;
                break;
            case 3:
                d = a;
                e = k;
                f = c;
                break;
            case 4:
                d = b;
                e = a;
                f = c;
                break;
            case 5:
                d = c;
                e = a;
                f = k;
                break;
            case 6:
            case 0:
                d = c, e = b, f = a
        }
    }
    return[Math.floor(d), Math.floor(e), Math.floor(f)]
};
goog.color.rgbToHsv = function (a, b, c) {
    var d = Math.max(Math.max(a, b), c), e = Math.min(Math.min(a, b), c);
    if (e == d)e = a = 0; else {
        var f = d - e, e = f / d;
        a = 60 * (a == d ? (b - c) / f : b == d ? 2 + (c - a) / f : 4 + (a - b) / f);
        0 > a && (a += 360);
        360 < a && (a -= 360)
    }
    return[a, e, d]
};
goog.color.rgbArrayToHsv = function (a) {
    return goog.color.rgbToHsv(a[0], a[1], a[2])
};
goog.color.hsvArrayToRgb = function (a) {
    return goog.color.hsvToRgb(a[0], a[1], a[2])
};
goog.color.hexToHsl = function (a) {
    a = goog.color.hexToRgb(a);
    return goog.color.rgbToHsl(a[0], a[1], a[2])
};
goog.color.hslToHex = function (a, b, c) {
    return goog.color.rgbArrayToHex(goog.color.hslToRgb(a, b, c))
};
goog.color.hslArrayToHex = function (a) {
    return goog.color.rgbArrayToHex(goog.color.hslToRgb(a[0], a[1], a[2]))
};
goog.color.hexToHsv = function (a) {
    return goog.color.rgbArrayToHsv(goog.color.hexToRgb(a))
};
goog.color.hsvToHex = function (a, b, c) {
    return goog.color.rgbArrayToHex(goog.color.hsvToRgb(a, b, c))
};
goog.color.hsvArrayToHex = function (a) {
    return goog.color.hsvToHex(a[0], a[1], a[2])
};
goog.color.hslDistance = function (a, b) {
    var c, d;
    c = 0.5 >= a[2] ? a[1] * a[2] : a[1] * (1 - a[2]);
    d = 0.5 >= b[2] ? b[1] * b[2] : b[1] * (1 - b[2]);
    return(a[2] - b[2]) * (a[2] - b[2]) + c * c + d * d - 2 * c * d * Math.cos(2 * (a[0] / 360 - b[0] / 360) * Math.PI)
};
goog.color.blend = function (a, b, c) {
    c = goog.math.clamp(c, 0, 1);
    return[Math.round(c * a[0] + (1 - c) * b[0]), Math.round(c * a[1] + (1 - c) * b[1]), Math.round(c * a[2] + (1 - c) * b[2])]
};
goog.color.darken = function (a, b) {
    return goog.color.blend([0, 0, 0], a, b)
};
goog.color.lighten = function (a, b) {
    return goog.color.blend([255, 255, 255], a, b)
};
goog.color.highContrast = function (a, b) {
    for (var c = [], d = 0; d < b.length; d++)c.push({color: b[d], diff: goog.color.yiqBrightnessDiff_(b[d], a) + goog.color.colorDiff_(b[d], a)});
    c.sort(function (a, b) {
        return b.diff - a.diff
    });
    return c[0].color
};
goog.color.yiqBrightness_ = function (a) {
    return Math.round((299 * a[0] + 587 * a[1] + 114 * a[2]) / 1E3)
};
goog.color.yiqBrightnessDiff_ = function (a, b) {
    return Math.abs(goog.color.yiqBrightness_(a) - goog.color.yiqBrightness_(b))
};
goog.color.colorDiff_ = function (a, b) {
    return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]) + Math.abs(a[2] - b[2])
};
goog.fx.dom = {};
goog.fx.dom.PredefinedEffect = function (a, b, c, d, e) {
    goog.fx.Animation.call(this, b, c, d, e);
    this.element = a
};
goog.inherits(goog.fx.dom.PredefinedEffect, goog.fx.Animation);
goog.fx.dom.PredefinedEffect.prototype.updateStyle = goog.nullFunction;
goog.fx.dom.PredefinedEffect.prototype.isRightToLeft = function () {
    goog.isDef(this.rightToLeft_) || (this.rightToLeft_ = goog.style.isRightToLeft(this.element));
    return this.rightToLeft_
};
goog.fx.dom.PredefinedEffect.prototype.onAnimate = function () {
    this.updateStyle();
    goog.fx.dom.PredefinedEffect.superClass_.onAnimate.call(this)
};
goog.fx.dom.PredefinedEffect.prototype.onEnd = function () {
    this.updateStyle();
    goog.fx.dom.PredefinedEffect.superClass_.onEnd.call(this)
};
goog.fx.dom.PredefinedEffect.prototype.onBegin = function () {
    this.updateStyle();
    goog.fx.dom.PredefinedEffect.superClass_.onBegin.call(this)
};
goog.fx.dom.Slide = function (a, b, c, d, e) {
    if (2 != b.length || 2 != c.length)throw Error("Start and end points must be 2D");
    goog.fx.dom.PredefinedEffect.apply(this, arguments)
};
goog.inherits(goog.fx.dom.Slide, goog.fx.dom.PredefinedEffect);
goog.fx.dom.Slide.prototype.updateStyle = function () {
    var a = this.isRightPositioningForRtlEnabled() && this.isRightToLeft() ? "right" : "left";
    this.element.style[a] = Math.round(this.coords[0]) + "px";
    this.element.style.top = Math.round(this.coords[1]) + "px"
};
goog.fx.dom.SlideFrom = function (a, b, c, d) {
    var e = [this.isRightPositioningForRtlEnabled() ? goog.style.bidi.getOffsetStart(a) : a.offsetLeft, a.offsetTop];
    goog.fx.dom.Slide.call(this, a, e, b, c, d)
};
goog.inherits(goog.fx.dom.SlideFrom, goog.fx.dom.Slide);
goog.fx.dom.SlideFrom.prototype.onBegin = function () {
    this.startPoint = [this.isRightPositioningForRtlEnabled() ? goog.style.bidi.getOffsetStart(this.element) : this.element.offsetLeft, this.element.offsetTop];
    goog.fx.dom.SlideFrom.superClass_.onBegin.call(this)
};
goog.fx.dom.Swipe = function (a, b, c, d, e) {
    if (2 != b.length || 2 != c.length)throw Error("Start and end points must be 2D");
    goog.fx.dom.PredefinedEffect.apply(this, arguments);
    this.maxWidth_ = Math.max(this.endPoint[0], this.startPoint[0]);
    this.maxHeight_ = Math.max(this.endPoint[1], this.startPoint[1])
};
goog.inherits(goog.fx.dom.Swipe, goog.fx.dom.PredefinedEffect);
goog.fx.dom.Swipe.prototype.updateStyle = function () {
    var a = this.coords[0], b = this.coords[1];
    this.clip_(Math.round(a), Math.round(b), this.maxWidth_, this.maxHeight_);
    this.element.style.width = Math.round(a) + "px";
    var c = this.isRightPositioningForRtlEnabled() && this.isRightToLeft() ? "marginRight" : "marginLeft";
    this.element.style[c] = Math.round(a) - this.maxWidth_ + "px";
    this.element.style.marginTop = Math.round(b) - this.maxHeight_ + "px"
};
goog.fx.dom.Swipe.prototype.clip_ = function (a, b, c, d) {
    this.element.style.clip = "rect(" + (d - b) + "px " + c + "px " + d + "px " + (c - a) + "px)"
};
goog.fx.dom.Scroll = function (a, b, c, d, e) {
    if (2 != b.length || 2 != c.length)throw Error("Start and end points must be 2D");
    goog.fx.dom.PredefinedEffect.apply(this, arguments)
};
goog.inherits(goog.fx.dom.Scroll, goog.fx.dom.PredefinedEffect);
goog.fx.dom.Scroll.prototype.updateStyle = function () {
    this.isRightPositioningForRtlEnabled() ? goog.style.bidi.setScrollOffset(this.element, Math.round(this.coords[0])) : this.element.scrollLeft = Math.round(this.coords[0]);
    this.element.scrollTop = Math.round(this.coords[1])
};
goog.fx.dom.Resize = function (a, b, c, d, e) {
    if (2 != b.length || 2 != c.length)throw Error("Start and end points must be 2D");
    goog.fx.dom.PredefinedEffect.apply(this, arguments)
};
goog.inherits(goog.fx.dom.Resize, goog.fx.dom.PredefinedEffect);
goog.fx.dom.Resize.prototype.updateStyle = function () {
    this.element.style.width = Math.round(this.coords[0]) + "px";
    this.element.style.height = Math.round(this.coords[1]) + "px"
};
goog.fx.dom.ResizeWidth = function (a, b, c, d, e) {
    goog.fx.dom.PredefinedEffect.call(this, a, [b], [c], d, e)
};
goog.inherits(goog.fx.dom.ResizeWidth, goog.fx.dom.PredefinedEffect);
goog.fx.dom.ResizeWidth.prototype.updateStyle = function () {
    this.element.style.width = Math.round(this.coords[0]) + "px"
};
goog.fx.dom.ResizeHeight = function (a, b, c, d, e) {
    goog.fx.dom.PredefinedEffect.call(this, a, [b], [c], d, e)
};
goog.inherits(goog.fx.dom.ResizeHeight, goog.fx.dom.PredefinedEffect);
goog.fx.dom.ResizeHeight.prototype.updateStyle = function () {
    this.element.style.height = Math.round(this.coords[0]) + "px"
};
goog.fx.dom.Fade = function (a, b, c, d, e) {
    goog.isNumber(b) && (b = [b]);
    goog.isNumber(c) && (c = [c]);
    goog.fx.dom.PredefinedEffect.call(this, a, b, c, d, e);
    if (1 != b.length || 1 != c.length)throw Error("Start and end points must be 1D");
};
goog.inherits(goog.fx.dom.Fade, goog.fx.dom.PredefinedEffect);
goog.fx.dom.Fade.prototype.updateStyle = function () {
    goog.style.setOpacity(this.element, this.coords[0])
};
goog.fx.dom.Fade.prototype.show = function () {
    this.element.style.display = ""
};
goog.fx.dom.Fade.prototype.hide = function () {
    this.element.style.display = "none"
};
goog.fx.dom.FadeOut = function (a, b, c) {
    goog.fx.dom.Fade.call(this, a, 1, 0, b, c)
};
goog.inherits(goog.fx.dom.FadeOut, goog.fx.dom.Fade);
goog.fx.dom.FadeIn = function (a, b, c) {
    goog.fx.dom.Fade.call(this, a, 0, 1, b, c)
};
goog.inherits(goog.fx.dom.FadeIn, goog.fx.dom.Fade);
goog.fx.dom.FadeOutAndHide = function (a, b, c) {
    goog.fx.dom.Fade.call(this, a, 1, 0, b, c)
};
goog.inherits(goog.fx.dom.FadeOutAndHide, goog.fx.dom.Fade);
goog.fx.dom.FadeOutAndHide.prototype.onBegin = function () {
    this.show();
    goog.fx.dom.FadeOutAndHide.superClass_.onBegin.call(this)
};
goog.fx.dom.FadeOutAndHide.prototype.onEnd = function () {
    this.hide();
    goog.fx.dom.FadeOutAndHide.superClass_.onEnd.call(this)
};
goog.fx.dom.FadeInAndShow = function (a, b, c) {
    goog.fx.dom.Fade.call(this, a, 0, 1, b, c)
};
goog.inherits(goog.fx.dom.FadeInAndShow, goog.fx.dom.Fade);
goog.fx.dom.FadeInAndShow.prototype.onBegin = function () {
    this.show();
    goog.fx.dom.FadeInAndShow.superClass_.onBegin.call(this)
};
goog.fx.dom.BgColorTransform = function (a, b, c, d, e) {
    if (3 != b.length || 3 != c.length)throw Error("Start and end points must be 3D");
    goog.fx.dom.PredefinedEffect.apply(this, arguments)
};
goog.inherits(goog.fx.dom.BgColorTransform, goog.fx.dom.PredefinedEffect);
goog.fx.dom.BgColorTransform.prototype.setColor = function () {
    for (var a = [], b = 0; b < this.coords.length; b++)a[b] = Math.round(this.coords[b]);
    a = "rgb(" + a.join(",") + ")";
    this.element.style.backgroundColor = a
};
goog.fx.dom.BgColorTransform.prototype.updateStyle = function () {
    this.setColor()
};
goog.fx.dom.bgColorFadeIn = function (a, b, c, d) {
    function e() {
        a.style.backgroundColor = f
    }

    var f = a.style.backgroundColor || "", g = goog.style.getBackgroundColor(a), g = g && "transparent" != g && "rgba(0, 0, 0, 0)" != g ? goog.color.hexToRgb(goog.color.parse(g).hex) : [255, 255, 255];
    b = new goog.fx.dom.BgColorTransform(a, b, g, c);
    d ? d.listen(b, goog.fx.Transition.EventType.END, e) : goog.events.listen(b, goog.fx.Transition.EventType.END, e);
    b.play()
};
goog.fx.dom.ColorTransform = function (a, b, c, d, e) {
    if (3 != b.length || 3 != c.length)throw Error("Start and end points must be 3D");
    goog.fx.dom.PredefinedEffect.apply(this, arguments)
};
goog.inherits(goog.fx.dom.ColorTransform, goog.fx.dom.PredefinedEffect);
goog.fx.dom.ColorTransform.prototype.updateStyle = function () {
    for (var a = [], b = 0; b < this.coords.length; b++)a[b] = Math.round(this.coords[b]);
    a = "rgb(" + a.join(",") + ")";
    this.element.style.color = a
};
goog.ui.ac.Renderer = function (a, b, c, d) {
    goog.events.EventTarget.call(this);
    this.parent_ = a || goog.dom.getDocument().body;
    this.dom_ = goog.dom.getDomHelper(this.parent_);
    this.reposition_ = !a;
    this.element_ = null;
    this.token_ = "";
    this.rows_ = [];
    this.rowDivs_ = [];
    this.startRenderingRows_ = this.hilitedRow_ = -1;
    this.visible_ = !1;
    this.className = "ac-renderer";
    this.rowClassName = "ac-row";
    this.legacyActiveClassName_ = "active";
    this.activeClassName = "ac-active";
    this.highlightedClassName = "ac-highlighted";
    this.customRenderer_ = b ||
        null;
    this.useStandardHighlighting_ = null != d ? d : !0;
    this.matchWordBoundary_ = !0;
    this.highlightAllTokens_ = !1;
    this.rightAlign_ = !!c;
    this.topAlign_ = !1;
    this.menuFadeDuration_ = 0
};
goog.inherits(goog.ui.ac.Renderer, goog.events.EventTarget);
goog.ui.ac.Renderer.DELAY_BEFORE_MOUSEOVER = 300;
goog.ui.ac.Renderer.prototype.getElement = function () {
    return this.element_
};
goog.ui.ac.Renderer.prototype.setWidthProvider = function (a) {
    this.widthProvider_ = a
};
goog.ui.ac.Renderer.prototype.setTopAlign = function (a) {
    this.topAlign_ = a
};
goog.ui.ac.Renderer.prototype.getTopAlign = function () {
    return this.topAlign_
};
goog.ui.ac.Renderer.prototype.setRightAlign = function (a) {
    this.rightAlign_ = a
};
goog.ui.ac.Renderer.prototype.getRightAlign = function () {
    return this.rightAlign_
};
goog.ui.ac.Renderer.prototype.setUseStandardHighlighting = function (a) {
    this.useStandardHighlighting_ = a
};
goog.ui.ac.Renderer.prototype.setMatchWordBoundary = function (a) {
    this.matchWordBoundary_ = a
};
goog.ui.ac.Renderer.prototype.setHighlightAllTokens = function (a) {
    this.highlightAllTokens_ = a
};
goog.ui.ac.Renderer.prototype.setMenuFadeDuration = function (a) {
    this.menuFadeDuration_ = a
};
goog.ui.ac.Renderer.prototype.setAnchorElement = function (a) {
    this.anchorElement_ = a
};
goog.ui.ac.Renderer.prototype.getAnchorElement = function () {
    return this.anchorElement_
};
goog.ui.ac.Renderer.prototype.renderRows = function (a, b, c) {
    this.token_ = b;
    this.rows_ = a;
    this.hilitedRow_ = -1;
    this.startRenderingRows_ = goog.now();
    this.target_ = c;
    this.rowDivs_ = [];
    this.redraw()
};
goog.ui.ac.Renderer.prototype.dismiss = function () {
    this.target_ && goog.a11y.aria.setActiveDescendant(this.target_, null);
    this.visible_ && (this.visible_ = !1, this.target_ && goog.a11y.aria.setState(this.target_, goog.a11y.aria.State.HASPOPUP, !1), 0 < this.menuFadeDuration_ ? (goog.dispose(this.animation_), this.animation_ = new goog.fx.dom.FadeOutAndHide(this.element_, this.menuFadeDuration_), this.animation_.play()) : goog.style.setElementShown(this.element_, !1))
};
goog.ui.ac.Renderer.prototype.show = function () {
    this.visible_ || (this.visible_ = !0, this.target_ && (goog.a11y.aria.setRole(this.target_, goog.a11y.aria.Role.COMBOBOX), goog.a11y.aria.setState(this.target_, goog.a11y.aria.State.AUTOCOMPLETE, "list"), goog.a11y.aria.setState(this.target_, goog.a11y.aria.State.HASPOPUP, !0)), 0 < this.menuFadeDuration_ ? (goog.dispose(this.animation_), this.animation_ = new goog.fx.dom.FadeInAndShow(this.element_, this.menuFadeDuration_), this.animation_.play()) : goog.style.setElementShown(this.element_,
        !0))
};
goog.ui.ac.Renderer.prototype.isVisible = function () {
    return this.visible_
};
goog.ui.ac.Renderer.prototype.hiliteRow = function (a) {
    var b = 0 <= a && a < this.rowDivs_.length ? this.rowDivs_[a] : void 0;
    this.dispatchEvent({type: goog.ui.ac.AutoComplete.EventType.ROW_HILITE, rowNode: b}) && (this.hiliteNone(), this.hilitedRow_ = a, b && (goog.dom.classes.add(b, this.activeClassName, this.legacyActiveClassName_), this.target_ && goog.a11y.aria.setActiveDescendant(this.target_, b), goog.style.scrollIntoContainerView(b, this.element_)))
};
goog.ui.ac.Renderer.prototype.hiliteNone = function () {
    0 <= this.hilitedRow_ && goog.dom.classes.remove(this.rowDivs_[this.hilitedRow_], this.activeClassName, this.legacyActiveClassName_)
};
goog.ui.ac.Renderer.prototype.hiliteId = function (a) {
    if (-1 == a)this.hiliteRow(-1); else for (var b = 0; b < this.rows_.length; b++)if (this.rows_[b].id == a) {
        this.hiliteRow(b);
        break
    }
};
goog.ui.ac.Renderer.prototype.setMenuClasses_ = function (a) {
    goog.dom.classes.add(a, this.className)
};
goog.ui.ac.Renderer.prototype.maybeCreateElement_ = function () {
    if (!this.element_) {
        var a = this.dom_.createDom("div", {style: "display:none"});
        this.element_ = a;
        this.setMenuClasses_(a);
        goog.a11y.aria.setRole(a, goog.a11y.aria.Role.LISTBOX);
        a.id = goog.ui.IdGenerator.getInstance().getNextUniqueId();
        this.dom_.appendChild(this.parent_, a);
        goog.events.listen(a, goog.events.EventType.CLICK, this.handleClick_, !1, this);
        goog.events.listen(a, goog.events.EventType.MOUSEDOWN, this.handleMouseDown_, !1, this);
        goog.events.listen(a,
            goog.events.EventType.MOUSEOVER, this.handleMouseOver_, !1, this)
    }
};
goog.ui.ac.Renderer.prototype.redraw = function () {
    this.maybeCreateElement_();
    this.topAlign_ && (this.element_.style.visibility = "hidden");
    this.widthProvider_ && (this.element_.style.minWidth = this.widthProvider_.clientWidth + "px");
    this.rowDivs_.length = 0;
    this.dom_.removeChildren(this.element_);
    if (this.customRenderer_ && this.customRenderer_.render)this.customRenderer_.render(this, this.element_, this.rows_, this.token_); else {
        var a = null;
        goog.array.forEach(this.rows_, function (b) {
            b = this.renderRowHtml(b, this.token_);
            this.topAlign_ ? this.element_.insertBefore(b, a) : this.dom_.appendChild(this.element_, b);
            a = b
        }, this)
    }
    0 == this.rows_.length ? this.dismiss() : (this.show(), this.reposition(), goog.style.setUnselectable(this.element_, !0))
};
goog.ui.ac.Renderer.prototype.getAnchorCorner = function () {
    var a = this.rightAlign_ ? goog.positioning.Corner.BOTTOM_RIGHT : goog.positioning.Corner.BOTTOM_LEFT;
    this.topAlign_ && (a = goog.positioning.flipCornerVertical(a));
    return a
};
goog.ui.ac.Renderer.prototype.reposition = function () {
    if (this.target_ && this.reposition_) {
        var a = this.anchorElement_ || this.target_, b = this.getAnchorCorner();
        goog.positioning.positionAtAnchor(a, b, this.element_, goog.positioning.flipCornerVertical(b), null, null, goog.positioning.Overflow.ADJUST_X_EXCEPT_OFFSCREEN);
        this.topAlign_ && (this.element_.style.visibility = "visible")
    }
};
goog.ui.ac.Renderer.prototype.setAutoPosition = function (a) {
    this.reposition_ = a
};
goog.ui.ac.Renderer.prototype.getAutoPosition = function () {
    return this.reposition_
};
goog.ui.ac.Renderer.prototype.getTarget = function () {
    return this.target_
};
goog.ui.ac.Renderer.prototype.disposeInternal = function () {
    this.element_ && (goog.events.unlisten(this.element_, goog.events.EventType.CLICK, this.handleClick_, !1, this), goog.events.unlisten(this.element_, goog.events.EventType.MOUSEDOWN, this.handleMouseDown_, !1, this), goog.events.unlisten(this.element_, goog.events.EventType.MOUSEOVER, this.handleMouseOver_, !1, this), this.dom_.removeNode(this.element_), this.element_ = null, this.visible_ = !1);
    goog.dispose(this.animation_);
    this.parent_ = null;
    goog.ui.ac.Renderer.superClass_.disposeInternal.call(this)
};
goog.ui.ac.Renderer.prototype.renderRowContents_ = function (a, b, c) {
    c.innerHTML = goog.string.htmlEscape(a.data.toString())
};
goog.ui.ac.Renderer.prototype.hiliteMatchingText_ = function (a, b) {
    if (a.nodeType == goog.dom.NodeType.TEXT) {
        var c = null;
        goog.isArray(b) && (1 < b.length && !this.highlightAllTokens_) && (c = goog.array.slice(b, 1));
        var d = this.getTokenRegExp_(b);
        if (0 != d.length) {
            for (var e = a.nodeValue, f = this.matchWordBoundary_ ? RegExp("([\\s\\S]*?)\\b(" + d + ")", "gi") : RegExp("([\\s\\S]*?)(" + d + ")", "gi"), d = [], g = 0, h = f.exec(e), k = 0; h;)k++, d.push(h[1]), d.push(h[2]), g = f.lastIndex, h = f.exec(e);
            d.push(e.substring(g));
            if (1 < d.length) {
                c = this.highlightAllTokens_ ?
                    k : 1;
                for (e = 0; e < c; e++)f = 2 * e, a.nodeValue = d[f], g = this.dom_.createElement("b"), g.className = this.highlightedClassName, this.dom_.appendChild(g, this.dom_.createTextNode(d[f + 1])), g = a.parentNode.insertBefore(g, a.nextSibling), a.parentNode.insertBefore(this.dom_.createTextNode(""), g.nextSibling), a = g.nextSibling;
                d = goog.array.slice(d, 2 * c);
                a.nodeValue = d.join("")
            } else c && this.hiliteMatchingText_(a, c)
        }
    } else for (d = a.firstChild; d;)c = d.nextSibling, this.hiliteMatchingText_(d, b), d = c
};
goog.ui.ac.Renderer.prototype.getTokenRegExp_ = function (a) {
    var b = "";
    if (!a)return b;
    goog.isArray(a) && (a = goog.array.filter(a, function (a) {
        return!goog.string.isEmptySafe(a)
    }));
    this.highlightAllTokens_ ? goog.isArray(a) ? b = goog.array.map(a, goog.string.regExpEscape).join("|") : (b = goog.string.collapseWhitespace(a), b = goog.string.regExpEscape(b), b = b.replace(/ /g, "|")) : goog.isArray(a) ? b = 0 < a.length ? goog.string.regExpEscape(a[0]) : "" : /^\W/.test(a) || (b = goog.string.regExpEscape(a));
    return b
};
goog.ui.ac.Renderer.prototype.renderRowHtml = function (a, b) {
    var c = this.dom_.createDom("div", {className: this.rowClassName, id: goog.ui.IdGenerator.getInstance().getNextUniqueId()});
    goog.a11y.aria.setRole(c, goog.a11y.aria.Role.OPTION);
    this.customRenderer_ && this.customRenderer_.renderRow ? this.customRenderer_.renderRow(a, b, c) : this.renderRowContents_(a, b, c);
    b && this.useStandardHighlighting_ && this.hiliteMatchingText_(c, b);
    goog.dom.classes.add(c, this.rowClassName);
    this.rowDivs_.push(c);
    return c
};
goog.ui.ac.Renderer.prototype.getRowFromEventTarget_ = function (a) {
    for (; a && a != this.element_ && !goog.dom.classes.has(a, this.rowClassName);)a = a.parentNode;
    return a ? goog.array.indexOf(this.rowDivs_, a) : -1
};
goog.ui.ac.Renderer.prototype.handleClick_ = function (a) {
    var b = this.getRowFromEventTarget_(a.target);
    0 <= b && this.dispatchEvent({type: goog.ui.ac.AutoComplete.EventType.SELECT, row: this.rows_[b].id});
    a.stopPropagation()
};
goog.ui.ac.Renderer.prototype.handleMouseDown_ = function (a) {
    a.stopPropagation();
    a.preventDefault()
};
goog.ui.ac.Renderer.prototype.handleMouseOver_ = function (a) {
    a = this.getRowFromEventTarget_(a.target);
    0 <= a && !(goog.now() - this.startRenderingRows_ < goog.ui.ac.Renderer.DELAY_BEFORE_MOUSEOVER) && this.dispatchEvent({type: goog.ui.ac.AutoComplete.EventType.HILITE, row: this.rows_[a].id})
};
goog.ui.ac.Renderer.CustomRenderer = function () {
};
goog.ui.ac.Renderer.CustomRenderer.prototype.render = function (a, b, c, d) {
};
goog.ui.ac.Renderer.CustomRenderer.prototype.renderRow = function (a, b, c) {
};
goog.dom.selection = {};
goog.dom.selection.setStart = function (a, b) {
    if (goog.dom.selection.useSelectionProperties_(a))a.selectionStart = b; else if (goog.userAgent.IE) {
        var c = goog.dom.selection.getRangeIe_(a), d = c[0];
        d.inRange(c[1]) && (b = goog.dom.selection.canonicalizePositionIe_(a, b), d.collapse(!0), d.move("character", b), d.select())
    }
};
goog.dom.selection.getStart = function (a) {
    return goog.dom.selection.getEndPoints_(a, !0)[0]
};
goog.dom.selection.getEndPointsTextareaIe_ = function (a, b, c) {
    b = b.duplicate();
    for (var d = a.text, e = d, f = b.text, g = f, h = !1; !h;)0 == a.compareEndPoints("StartToEnd", a) ? h = !0 : (a.moveEnd("character", -1), a.text == d ? e += "\r\n" : h = !0);
    if (c)return[e.length, -1];
    for (a = !1; !a;)0 == b.compareEndPoints("StartToEnd", b) ? a = !0 : (b.moveEnd("character", -1), b.text == f ? g += "\r\n" : a = !0);
    return[e.length, e.length + g.length]
};
goog.dom.selection.getEndPoints = function (a) {
    return goog.dom.selection.getEndPoints_(a, !1)
};
goog.dom.selection.getEndPoints_ = function (a, b) {
    var c = 0, d = 0;
    if (goog.dom.selection.useSelectionProperties_(a))c = a.selectionStart, d = b ? -1 : a.selectionEnd; else if (goog.userAgent.IE) {
        var e = goog.dom.selection.getRangeIe_(a), f = e[0], e = e[1];
        if (f.inRange(e)) {
            f.setEndPoint("EndToStart", e);
            if ("textarea" == a.type)return goog.dom.selection.getEndPointsTextareaIe_(f, e, b);
            c = f.text.length;
            d = b ? -1 : f.text.length + e.text.length
        }
    }
    return[c, d]
};
goog.dom.selection.setEnd = function (a, b) {
    if (goog.dom.selection.useSelectionProperties_(a))a.selectionEnd = b; else if (goog.userAgent.IE) {
        var c = goog.dom.selection.getRangeIe_(a), d = c[1];
        c[0].inRange(d) && (b = goog.dom.selection.canonicalizePositionIe_(a, b), c = goog.dom.selection.canonicalizePositionIe_(a, goog.dom.selection.getStart(a)), d.collapse(!0), d.moveEnd("character", b - c), d.select())
    }
};
goog.dom.selection.getEnd = function (a) {
    return goog.dom.selection.getEndPoints_(a, !1)[1]
};
goog.dom.selection.setCursorPosition = function (a, b) {
    if (goog.dom.selection.useSelectionProperties_(a))a.selectionStart = b, a.selectionEnd = b; else if (goog.userAgent.IE) {
        b = goog.dom.selection.canonicalizePositionIe_(a, b);
        var c = a.createTextRange();
        c.collapse(!0);
        c.move("character", b);
        c.select()
    }
};
goog.dom.selection.setText = function (a, b) {
    if (goog.dom.selection.useSelectionProperties_(a)) {
        var c = a.value, d = a.selectionStart, e = c.substr(0, d), c = c.substr(a.selectionEnd);
        a.value = e + b + c;
        a.selectionStart = d;
        a.selectionEnd = d + b.length
    } else if (goog.userAgent.IE)e = goog.dom.selection.getRangeIe_(a), d = e[1], e[0].inRange(d) && (e = d.duplicate(), d.text = b, d.setEndPoint("StartToStart", e), d.select()); else throw Error("Cannot set the selection end");
};
goog.dom.selection.getText = function (a) {
    if (goog.dom.selection.useSelectionProperties_(a))return a.value.substring(a.selectionStart, a.selectionEnd);
    if (goog.userAgent.IE) {
        var b = goog.dom.selection.getRangeIe_(a), c = b[1];
        return b[0].inRange(c) ? "textarea" == a.type ? goog.dom.selection.getSelectionRangeText_(c) : c.text : ""
    }
    throw Error("Cannot get the selection text");
};
goog.dom.selection.getSelectionRangeText_ = function (a) {
    a = a.duplicate();
    for (var b = a.text, c = b, d = !1; !d;)0 == a.compareEndPoints("StartToEnd", a) ? d = !0 : (a.moveEnd("character", -1), a.text == b ? c += "\r\n" : d = !0);
    return c
};
goog.dom.selection.getRangeIe_ = function (a) {
    var b = a.ownerDocument || a.document, c = b.selection.createRange();
    "textarea" == a.type ? (b = b.body.createTextRange(), b.moveToElementText(a)) : b = a.createTextRange();
    return[b, c]
};
goog.dom.selection.canonicalizePositionIe_ = function (a, b) {
    if ("textarea" == a.type) {
        var c = a.value.substring(0, b);
        b = goog.string.canonicalizeNewlines(c).length
    }
    return b
};
goog.dom.selection.useSelectionProperties_ = function (a) {
    try {
        return"number" == typeof a.selectionStart
    } catch (b) {
        return!1
    }
};
goog.ui.ac.InputHandler = function (a, b, c, d) {
    goog.Disposable.call(this);
    d = d || 150;
    this.multi_ = null != c ? c : !0;
    this.setSeparators(a || goog.ui.ac.InputHandler.STANDARD_LIST_SEPARATORS);
    this.literals_ = b || "";
    this.preventDefaultOnTab_ = this.multi_;
    this.timer_ = 0 < d ? new goog.Timer(d) : null;
    this.eh_ = new goog.events.EventHandler(this);
    this.activateHandler_ = new goog.events.EventHandler(this);
    this.keyHandler_ = new goog.events.KeyHandler;
    this.lastKeyCode_ = -1
};
goog.inherits(goog.ui.ac.InputHandler, goog.Disposable);
goog.ui.ac.InputHandler.REQUIRES_ASYNC_BLUR_ = (goog.userAgent.product.IPHONE || goog.userAgent.product.IPAD) && !goog.userAgent.isVersionOrHigher("533.17.9");
goog.ui.ac.InputHandler.STANDARD_LIST_SEPARATORS = ",;";
goog.ui.ac.InputHandler.QUOTE_LITERALS = '"';
goog.ui.ac.InputHandler.prototype.whitespaceWrapEntries_ = !0;
goog.ui.ac.InputHandler.prototype.generateNewTokenOnLiteral_ = !0;
goog.ui.ac.InputHandler.prototype.upsideDown_ = !1;
goog.ui.ac.InputHandler.prototype.separatorUpdates_ = !0;
goog.ui.ac.InputHandler.prototype.separatorSelects_ = !0;
goog.ui.ac.InputHandler.prototype.activeTimeoutId_ = null;
goog.ui.ac.InputHandler.prototype.activeElement_ = null;
goog.ui.ac.InputHandler.prototype.lastValue_ = "";
goog.ui.ac.InputHandler.prototype.waitingForIme_ = !1;
goog.ui.ac.InputHandler.prototype.rowJustSelected_ = !1;
goog.ui.ac.InputHandler.prototype.updateDuringTyping_ = !0;
goog.ui.ac.InputHandler.prototype.attachAutoComplete = function (a) {
    this.ac_ = a
};
goog.ui.ac.InputHandler.prototype.getAutoComplete = function () {
    return this.ac_
};
goog.ui.ac.InputHandler.prototype.getActiveElement = function () {
    return this.activeElement_
};
goog.ui.ac.InputHandler.prototype.getValue = function () {
    return this.activeElement_.value
};
goog.ui.ac.InputHandler.prototype.setValue = function (a) {
    this.activeElement_.value = a
};
goog.ui.ac.InputHandler.prototype.getCursorPosition = function () {
    return goog.dom.selection.getStart(this.activeElement_)
};
goog.ui.ac.InputHandler.prototype.setCursorPosition = function (a) {
    goog.dom.selection.setStart(this.activeElement_, a);
    goog.dom.selection.setEnd(this.activeElement_, a)
};
goog.ui.ac.InputHandler.prototype.attachInput = function (a) {
    goog.dom.isElement(a) && goog.a11y.aria.setState(a, "haspopup", !0);
    this.eh_.listen(a, goog.events.EventType.FOCUS, this.handleFocus);
    this.eh_.listen(a, goog.events.EventType.BLUR, this.handleBlur);
    if (!this.activeElement_ && (this.activateHandler_.listen(a, goog.events.EventType.KEYDOWN, this.onKeyDownOnInactiveElement_), goog.dom.isElement(a))) {
        var b = goog.dom.getOwnerDocument(a);
        goog.dom.getActiveElement(b) == a && this.processFocus(a)
    }
};
goog.ui.ac.InputHandler.prototype.detachInput = function (a) {
    a == this.activeElement_ && this.handleBlur();
    this.eh_.unlisten(a, goog.events.EventType.FOCUS, this.handleFocus);
    this.eh_.unlisten(a, goog.events.EventType.BLUR, this.handleBlur);
    this.activeElement_ || this.activateHandler_.unlisten(a, goog.events.EventType.KEYDOWN, this.onKeyDownOnInactiveElement_)
};
goog.ui.ac.InputHandler.prototype.attachInputs = function (a) {
    for (var b = 0; b < arguments.length; b++)this.attachInput(arguments[b])
};
goog.ui.ac.InputHandler.prototype.detachInputs = function (a) {
    for (var b = 0; b < arguments.length; b++)this.detachInput(arguments[b])
};
goog.ui.ac.InputHandler.prototype.selectRow = function (a, b) {
    this.setTokenText(a.toString(), b);
    return!1
};
goog.ui.ac.InputHandler.prototype.setTokenText = function (a, b) {
    if (goog.isDef(b) ? b : this.multi_) {
        var c = this.getTokenIndex_(this.getValue(), this.getCursorPosition()), d = this.splitInput_(this.getValue()), e = a;
        this.separatorCheck_.test(e) || (e = goog.string.trimRight(e) + this.defaultSeparator_);
        this.whitespaceWrapEntries_ && (0 == c || goog.string.isEmpty(d[c - 1]) || (e = " " + e), c == d.length - 1 && (e += " "));
        if (e != d[c]) {
            d[c] = e;
            e = this.activeElement_;
            (goog.userAgent.GECKO || goog.userAgent.IE && goog.userAgent.isVersionOrHigher("9")) &&
            e.blur();
            e.value = d.join("");
            for (var f = 0, g = 0; g <= c; g++)f += d[g].length;
            e.focus();
            this.setCursorPosition(f)
        }
    } else this.setValue(a);
    this.rowJustSelected_ = !0
};
goog.ui.ac.InputHandler.prototype.disposeInternal = function () {
    goog.ui.ac.InputHandler.superClass_.disposeInternal.call(this);
    null != this.activeTimeoutId_ && window.clearTimeout(this.activeTimeoutId_);
    this.eh_.dispose();
    delete this.eh_;
    this.activateHandler_.dispose();
    this.keyHandler_.dispose();
    goog.dispose(this.timer_)
};
goog.ui.ac.InputHandler.prototype.setSeparators = function (a, b) {
    this.separators_ = a;
    this.defaultSeparator_ = goog.isDefAndNotNull(b) ? b : this.separators_.substring(0, 1);
    var c = this.multi_ ? "[\\s" + this.separators_ + "]+" : "[\\s]+";
    this.trimmer_ = RegExp("^" + c + "|" + c + "$", "g");
    this.separatorCheck_ = RegExp("\\s*[" + this.separators_ + "]$")
};
goog.ui.ac.InputHandler.prototype.setUpsideDown = function (a) {
    this.upsideDown_ = a
};
goog.ui.ac.InputHandler.prototype.setWhitespaceWrapEntries = function (a) {
    this.whitespaceWrapEntries_ = a
};
goog.ui.ac.InputHandler.prototype.setGenerateNewTokenOnLiteral = function (a) {
    this.generateNewTokenOnLiteral_ = a
};
goog.ui.ac.InputHandler.prototype.setTrimmingRegExp = function (a) {
    this.trimmer_ = a
};
goog.ui.ac.InputHandler.prototype.setPreventDefaultOnTab = function (a) {
    this.preventDefaultOnTab_ = a
};
goog.ui.ac.InputHandler.prototype.setSeparatorCompletes = function (a) {
    this.separatorSelects_ = this.separatorUpdates_ = a
};
goog.ui.ac.InputHandler.prototype.setSeparatorSelects = function (a) {
    this.separatorSelects_ = a
};
goog.ui.ac.InputHandler.prototype.getThrottleTime = function () {
    return this.timer_ ? this.timer_.getInterval() : -1
};
goog.ui.ac.InputHandler.prototype.setRowJustSelected = function (a) {
    this.rowJustSelected_ = a
};
goog.ui.ac.InputHandler.prototype.setThrottleTime = function (a) {
    0 > a ? (this.timer_.dispose(), this.timer_ = null) : this.timer_ ? this.timer_.setInterval(a) : this.timer_ = new goog.Timer(a)
};
goog.ui.ac.InputHandler.prototype.getUpdateDuringTyping = function () {
    return this.updateDuringTyping_
};
goog.ui.ac.InputHandler.prototype.setUpdateDuringTyping = function (a) {
    this.updateDuringTyping_ = a
};
goog.ui.ac.InputHandler.prototype.handleKeyEvent = function (a) {
    switch (a.keyCode) {
        case goog.events.KeyCodes.DOWN:
            if (this.ac_.isOpen())return this.moveDown_(), a.preventDefault(), !0;
            if (!this.multi_)return this.update(!0), a.preventDefault(), !0;
            break;
        case goog.events.KeyCodes.UP:
            if (this.ac_.isOpen())return this.moveUp_(), a.preventDefault(), !0;
            break;
        case goog.events.KeyCodes.TAB:
            if (this.ac_.isOpen() && !a.shiftKey) {
                if (this.update(), this.ac_.selectHilited() && this.preventDefaultOnTab_)return a.preventDefault(),
                    !0
            } else this.ac_.dismiss();
            break;
        case goog.events.KeyCodes.ENTER:
            if (this.ac_.isOpen()) {
                if (this.update(), this.ac_.selectHilited())return a.preventDefault(), a.stopPropagation(), !0
            } else this.ac_.dismiss();
            break;
        case goog.events.KeyCodes.ESC:
            if (this.ac_.isOpen())return this.ac_.dismiss(), a.preventDefault(), a.stopPropagation(), !0;
            break;
        case goog.events.KeyCodes.WIN_IME:
            if (!this.waitingForIme_)return this.startWaitingForIme_(), !0;
            break;
        default:
            this.timer_ && !this.updateDuringTyping_ && (this.timer_.stop(),
                this.timer_.start())
    }
    return this.handleSeparator_(a)
};
goog.ui.ac.InputHandler.prototype.handleSeparator_ = function (a) {
    var b = this.multi_ && a.charCode && -1 != this.separators_.indexOf(String.fromCharCode(a.charCode));
    this.separatorUpdates_ && b && this.update();
    return this.separatorSelects_ && b && this.ac_.selectHilited() ? (a.preventDefault(), !0) : !1
};
goog.ui.ac.InputHandler.prototype.needKeyUpListener = function () {
    return!1
};
goog.ui.ac.InputHandler.prototype.handleKeyUp = function (a) {
    return!1
};
goog.ui.ac.InputHandler.prototype.addEventHandlers_ = function () {
    this.keyHandler_.attach(this.activeElement_);
    this.eh_.listen(this.keyHandler_, goog.events.KeyHandler.EventType.KEY, this.onKey_);
    this.needKeyUpListener() && this.eh_.listen(this.activeElement_, goog.events.EventType.KEYUP, this.handleKeyUp);
    this.eh_.listen(this.activeElement_, goog.events.EventType.MOUSEDOWN, this.onMouseDown_);
    goog.userAgent.IE && this.eh_.listen(this.activeElement_, goog.events.EventType.KEYPRESS, this.onIeKeyPress_)
};
goog.ui.ac.InputHandler.prototype.removeEventHandlers_ = function () {
    this.eh_.unlisten(this.keyHandler_, goog.events.KeyHandler.EventType.KEY, this.onKey_);
    this.keyHandler_.detach();
    this.eh_.unlisten(this.activeElement_, goog.events.EventType.KEYUP, this.handleKeyUp);
    this.eh_.unlisten(this.activeElement_, goog.events.EventType.MOUSEDOWN, this.onMouseDown_);
    goog.userAgent.IE && this.eh_.unlisten(this.activeElement_, goog.events.EventType.KEYPRESS, this.onIeKeyPress_);
    this.waitingForIme_ && this.stopWaitingForIme_()
};
goog.ui.ac.InputHandler.prototype.handleFocus = function (a) {
    this.processFocus(a.target || null)
};
goog.ui.ac.InputHandler.prototype.processFocus = function (a) {
    this.activateHandler_.removeAll();
    this.ac_ && this.ac_.cancelDelayedDismiss();
    a != this.activeElement_ && (this.activeElement_ = a, this.timer_ && (this.timer_.start(), this.eh_.listen(this.timer_, goog.Timer.TICK, this.onTick_)), this.lastValue_ = this.getValue(), this.addEventHandlers_())
};
goog.ui.ac.InputHandler.prototype.handleBlur = function (a) {
    goog.ui.ac.InputHandler.REQUIRES_ASYNC_BLUR_ ? this.activeTimeoutId_ = window.setTimeout(goog.bind(this.processBlur_, this), 0) : this.processBlur_()
};
goog.ui.ac.InputHandler.prototype.processBlur_ = function () {
    this.activeElement_ && (this.removeEventHandlers_(), this.activeElement_ = null, this.timer_ && (this.timer_.stop(), this.eh_.unlisten(this.timer_, goog.Timer.TICK, this.onTick_)), this.ac_ && this.ac_.dismissOnDelay())
};
goog.ui.ac.InputHandler.prototype.onTick_ = function (a) {
    this.update()
};
goog.ui.ac.InputHandler.prototype.onKeyDownOnInactiveElement_ = function (a) {
    this.handleFocus(a)
};
goog.ui.ac.InputHandler.prototype.onKey_ = function (a) {
    this.lastKeyCode_ = a.keyCode;
    this.ac_ && this.handleKeyEvent(a)
};
goog.ui.ac.InputHandler.prototype.onKeyPress_ = function (a) {
    this.waitingForIme_ && this.lastKeyCode_ != goog.events.KeyCodes.WIN_IME && this.stopWaitingForIme_()
};
goog.ui.ac.InputHandler.prototype.onKeyUp_ = function (a) {
    this.waitingForIme_ && (a.keyCode == goog.events.KeyCodes.ENTER || a.keyCode == goog.events.KeyCodes.M && a.ctrlKey) && this.stopWaitingForIme_()
};
goog.ui.ac.InputHandler.prototype.onMouseDown_ = function (a) {
    this.ac_ && this.handleMouseDown(a)
};
goog.ui.ac.InputHandler.prototype.handleMouseDown = function (a) {
};
goog.ui.ac.InputHandler.prototype.startWaitingForIme_ = function () {
    this.waitingForIme_ || (this.eh_.listen(this.activeElement_, goog.events.EventType.KEYUP, this.onKeyUp_), this.eh_.listen(this.activeElement_, goog.events.EventType.KEYPRESS, this.onKeyPress_), this.waitingForIme_ = !0)
};
goog.ui.ac.InputHandler.prototype.stopWaitingForIme_ = function () {
    this.waitingForIme_ && (this.waitingForIme_ = !1, this.eh_.unlisten(this.activeElement_, goog.events.EventType.KEYPRESS, this.onKeyPress_), this.eh_.unlisten(this.activeElement_, goog.events.EventType.KEYUP, this.onKeyUp_))
};
goog.ui.ac.InputHandler.prototype.onIeKeyPress_ = function (a) {
    this.handleSeparator_(a)
};
goog.ui.ac.InputHandler.prototype.update = function (a) {
    if (this.activeElement_ && (a || this.getValue() != this.lastValue_)) {
        if (a || !this.rowJustSelected_)a = this.parseToken(), this.ac_ && (this.ac_.setTarget(this.activeElement_), this.ac_.setToken(a, this.getValue()));
        this.lastValue_ = this.getValue()
    }
    this.rowJustSelected_ = !1
};
goog.ui.ac.InputHandler.prototype.parseToken = function () {
    return this.parseToken_()
};
goog.ui.ac.InputHandler.prototype.moveUp_ = function () {
    return this.upsideDown_ ? this.ac_.hiliteNext() : this.ac_.hilitePrev()
};
goog.ui.ac.InputHandler.prototype.moveDown_ = function () {
    return this.upsideDown_ ? this.ac_.hilitePrev() : this.ac_.hiliteNext()
};
goog.ui.ac.InputHandler.prototype.parseToken_ = function () {
    var a = this.getCursorPosition(), b = this.getValue();
    return this.trim_(this.splitInput_(b)[this.getTokenIndex_(b, a)])
};
goog.ui.ac.InputHandler.prototype.trim_ = function (a) {
    return this.trimmer_ ? String(a).replace(this.trimmer_, "") : a
};
goog.ui.ac.InputHandler.prototype.getTokenIndex_ = function (a, b) {
    var c = this.splitInput_(a);
    if (b == a.length)return c.length - 1;
    for (var d = 0, e = 0, f = 0; e < c.length && f <= b; e++)f += c[e].length, d = e;
    return d
};
goog.ui.ac.InputHandler.prototype.splitInput_ = function (a) {
    if (!this.multi_)return[a];
    a = String(a).split("");
    for (var b = [], c = [], d = 0, e = !1; d < a.length; d++)this.literals_ && -1 != this.literals_.indexOf(a[d]) ? (this.generateNewTokenOnLiteral_ && !e && (b.push(c.join("")), c.length = 0), c.push(a[d]), e = !e) : e || -1 == this.separators_.indexOf(a[d]) ? c.push(a[d]) : (c.push(a[d]), b.push(c.join("")), c.length = 0);
    b.push(c.join(""));
    return b
};
goog.ui.ac.createSimpleAutoComplete = function (a, b, c, d) {
    a = new goog.ui.ac.ArrayMatcher(a, !d);
    d = new goog.ui.ac.Renderer;
    c = new goog.ui.ac.InputHandler(null, null, !!c);
    a = new goog.ui.ac.AutoComplete(a, d, c);
    c.attachAutoComplete(a);
    c.attachInputs(b);
    return a
};
goog.editor.Command = {UNDO: "+undo", REDO: "+redo", LINK: "+link", FORMAT_BLOCK: "+formatBlock", INDENT: "+indent", OUTDENT: "+outdent", REMOVE_FORMAT: "+removeFormat", STRIKE_THROUGH: "+strikeThrough", HORIZONTAL_RULE: "+insertHorizontalRule", SUBSCRIPT: "+subscript", SUPERSCRIPT: "+superscript", UNDERLINE: "+underline", BOLD: "+bold", ITALIC: "+italic", FONT_SIZE: "+fontSize", FONT_FACE: "+fontName", FONT_COLOR: "+foreColor", EMOTICON: "+emoticon", EQUATION: "+equation", BACKGROUND_COLOR: "+backColor", ORDERED_LIST: "+insertOrderedList",
    UNORDERED_LIST: "+insertUnorderedList", TABLE: "+table", JUSTIFY_CENTER: "+justifyCenter", JUSTIFY_FULL: "+justifyFull", JUSTIFY_RIGHT: "+justifyRight", JUSTIFY_LEFT: "+justifyLeft", BLOCKQUOTE: "+BLOCKQUOTE", DIR_LTR: "ltr", DIR_RTL: "rtl", IMAGE: "image", EDIT_HTML: "editHtml", UPDATE_LINK_BUBBLE: "updateLinkBubble", DEFAULT_TAG: "+defaultTag", CLEAR_LOREM: "clearlorem", UPDATE_LOREM: "updatelorem", USING_LOREM: "usinglorem", MODAL_LINK_EDITOR: "link"};
goog.editor.Plugin = function () {
    goog.events.EventTarget.call(this);
    this.enabled_ = this.activeOnUneditableFields()
};
goog.inherits(goog.editor.Plugin, goog.events.EventTarget);
goog.editor.Plugin.prototype.fieldObject = null;
goog.editor.Plugin.prototype.getFieldDomHelper = function () {
    return this.getFieldObject() && this.getFieldObject().getEditableDomHelper()
};
goog.editor.Plugin.prototype.autoDispose_ = !0;
goog.editor.Plugin.prototype.logger = goog.log.getLogger("goog.editor.Plugin");
goog.editor.Plugin.prototype.getFieldObject = function () {
    return this.fieldObject
};
goog.editor.Plugin.prototype.setFieldObject = function (a) {
    this.fieldObject = a
};
goog.editor.Plugin.prototype.registerFieldObject = function (a) {
    this.setFieldObject(a)
};
goog.editor.Plugin.prototype.unregisterFieldObject = function (a) {
    this.getFieldObject() && (this.disable(this.getFieldObject()), this.setFieldObject(null))
};
goog.editor.Plugin.prototype.enable = function (a) {
    this.getFieldObject() == a ? this.enabled_ = !0 : goog.log.error(this.logger, "Trying to enable an unregistered field with this plugin.")
};
goog.editor.Plugin.prototype.disable = function (a) {
    this.getFieldObject() == a ? this.enabled_ = !1 : goog.log.error(this.logger, "Trying to disable an unregistered field with this plugin.")
};
goog.editor.Plugin.prototype.isEnabled = function (a) {
    return this.getFieldObject() == a ? this.enabled_ : !1
};
goog.editor.Plugin.prototype.setAutoDispose = function (a) {
    this.autoDispose_ = a
};
goog.editor.Plugin.prototype.isAutoDispose = function () {
    return this.autoDispose_
};
goog.editor.Plugin.prototype.activeOnUneditableFields = goog.functions.FALSE;
goog.editor.Plugin.prototype.isSilentCommand = goog.functions.FALSE;
goog.editor.Plugin.prototype.disposeInternal = function () {
    this.getFieldObject() && this.unregisterFieldObject(this.getFieldObject());
    goog.editor.Plugin.superClass_.disposeInternal.call(this)
};
goog.editor.Plugin.Op = {KEYDOWN: 1, KEYPRESS: 2, KEYUP: 3, SELECTION: 4, SHORTCUT: 5, EXEC_COMMAND: 6, QUERY_COMMAND: 7, PREPARE_CONTENTS_HTML: 8, CLEAN_CONTENTS_HTML: 10, CLEAN_CONTENTS_DOM: 11};
goog.editor.Plugin.OPCODE = goog.object.transpose(goog.reflect.object(goog.editor.Plugin, {handleKeyDown: goog.editor.Plugin.Op.KEYDOWN, handleKeyPress: goog.editor.Plugin.Op.KEYPRESS, handleKeyUp: goog.editor.Plugin.Op.KEYUP, handleSelectionChange: goog.editor.Plugin.Op.SELECTION, handleKeyboardShortcut: goog.editor.Plugin.Op.SHORTCUT, execCommand: goog.editor.Plugin.Op.EXEC_COMMAND, queryCommandValue: goog.editor.Plugin.Op.QUERY_COMMAND, prepareContentsHtml: goog.editor.Plugin.Op.PREPARE_CONTENTS_HTML, cleanContentsHtml: goog.editor.Plugin.Op.CLEAN_CONTENTS_HTML,
    cleanContentsDom: goog.editor.Plugin.Op.CLEAN_CONTENTS_DOM}));
goog.editor.Plugin.IRREPRESSIBLE_OPS = goog.object.createSet(goog.editor.Plugin.Op.PREPARE_CONTENTS_HTML, goog.editor.Plugin.Op.CLEAN_CONTENTS_HTML, goog.editor.Plugin.Op.CLEAN_CONTENTS_DOM);
goog.editor.Plugin.prototype.execCommand = function (a, b) {
    var c = this.isSilentCommand(a);
    c || (goog.userAgent.GECKO && this.getFieldObject().stopChangeEvents(!0, !0), this.getFieldObject().dispatchBeforeChange());
    try {
        var d = this.execCommandInternal.apply(this, arguments)
    } finally {
        c || (this.getFieldObject().dispatchChange(), this.getFieldObject().dispatchSelectionChangeEvent())
    }
    return d
};
goog.editor.Plugin.prototype.isSupportedCommand = function (a) {
    return!1
};
goog.editor.plugins = {};
goog.editor.plugins.HeaderFormatter = function () {
    goog.editor.Plugin.call(this)
};
goog.inherits(goog.editor.plugins.HeaderFormatter, goog.editor.Plugin);
goog.editor.plugins.HeaderFormatter.prototype.getTrogClassId = function () {
    return"HeaderFormatter"
};
goog.editor.plugins.HeaderFormatter.HEADER_COMMAND = {H1: "H1", H2: "H2", H3: "H3", H4: "H4"};
goog.editor.plugins.HeaderFormatter.prototype.handleKeyboardShortcut = function (a, b, c) {
    if (!c)return!1;
    c = null;
    switch (b) {
        case "1":
            c = goog.editor.plugins.HeaderFormatter.HEADER_COMMAND.H1;
            break;
        case "2":
            c = goog.editor.plugins.HeaderFormatter.HEADER_COMMAND.H2;
            break;
        case "3":
            c = goog.editor.plugins.HeaderFormatter.HEADER_COMMAND.H3;
            break;
        case "4":
            c = goog.editor.plugins.HeaderFormatter.HEADER_COMMAND.H4
    }
    return c ? (this.getFieldObject().execCommand(goog.editor.Command.FORMAT_BLOCK, c), goog.userAgent.GECKO && a.stopPropagation(),
        !0) : !1
};
goog.ui.editor = {};
goog.ui.editor.AbstractDialog = function (a) {
    goog.events.EventTarget.call(this);
    this.dom = a
};
goog.inherits(goog.ui.editor.AbstractDialog, goog.events.EventTarget);
goog.ui.editor.AbstractDialog.prototype.show = function () {
    this.dialogInternal_ || (this.dialogInternal_ = this.createDialogControl(), this.dialogInternal_.addEventListener(goog.ui.Dialog.EventType.AFTER_HIDE, this.handleAfterHide_, !1, this));
    this.dialogInternal_.setVisible(!0)
};
goog.ui.editor.AbstractDialog.prototype.hide = function () {
    this.dialogInternal_ && this.dialogInternal_.setVisible(!1)
};
goog.ui.editor.AbstractDialog.prototype.isOpen = function () {
    return!!this.dialogInternal_ && this.dialogInternal_.isVisible()
};
goog.ui.editor.AbstractDialog.prototype.processOkAndClose = function () {
    var a = new goog.ui.Dialog.Event(goog.ui.Dialog.DefaultButtonKeys.OK, null);
    this.handleOk(a) && this.hide()
};
goog.ui.editor.AbstractDialog.EventType = {AFTER_HIDE: "afterhide", CANCEL: "cancel", OK: "ok"};
goog.ui.editor.AbstractDialog.Builder = function (a) {
    this.editorDialog_ = a;
    this.wrappedDialog_ = new goog.ui.Dialog("", !0, this.editorDialog_.dom);
    this.buttonSet_ = new goog.ui.Dialog.ButtonSet(this.editorDialog_.dom);
    this.buttonHandlers_ = {};
    this.addClassName("tr-dialog")
};
goog.ui.editor.AbstractDialog.Builder.prototype.setTitle = function (a) {
    this.wrappedDialog_.setTitle(a);
    return this
};
goog.ui.editor.AbstractDialog.Builder.prototype.addOkButton = function (a) {
    var b = goog.ui.Dialog.DefaultButtonKeys.OK, c = goog.getMsg("OK");
    this.buttonSet_.set(b, a || c, !0);
    this.buttonHandlers_[b] = goog.bind(this.editorDialog_.handleOk, this.editorDialog_);
    return this
};
goog.ui.editor.AbstractDialog.Builder.prototype.addCancelButton = function (a) {
    var b = goog.ui.Dialog.DefaultButtonKeys.CANCEL, c = goog.getMsg("Cancel");
    this.buttonSet_.set(b, a || c, !1, !0);
    this.buttonHandlers_[b] = goog.bind(this.editorDialog_.handleCancel, this.editorDialog_);
    return this
};
goog.ui.editor.AbstractDialog.Builder.prototype.addButton = function (a, b, c) {
    c = c || goog.string.createUniqueString();
    this.buttonSet_.set(c, a);
    this.buttonHandlers_[c] = b;
    return this
};
goog.ui.editor.AbstractDialog.Builder.prototype.addClassName = function (a) {
    goog.dom.classes.add(this.wrappedDialog_.getDialogElement(), a);
    return this
};
goog.ui.editor.AbstractDialog.Builder.prototype.setContent = function (a) {
    goog.dom.appendChild(this.wrappedDialog_.getContentElement(), a);
    return this
};
goog.ui.editor.AbstractDialog.Builder.prototype.build = function () {
    this.buttonSet_.isEmpty() && (this.addOkButton(), this.addCancelButton());
    this.wrappedDialog_.setButtonSet(this.buttonSet_);
    var a = this.buttonHandlers_;
    this.buttonHandlers_ = null;
    this.wrappedDialog_.addEventListener(goog.ui.Dialog.EventType.SELECT, function (b) {
        if (a[b.key])return a[b.key](b)
    });
    this.wrappedDialog_.setModal(!0);
    var b = this.wrappedDialog_;
    this.wrappedDialog_ = null;
    return b
};
goog.ui.editor.AbstractDialog.prototype.getOkButtonElement = function () {
    return this.getButtonElement(goog.ui.Dialog.DefaultButtonKeys.OK)
};
goog.ui.editor.AbstractDialog.prototype.getCancelButtonElement = function () {
    return this.getButtonElement(goog.ui.Dialog.DefaultButtonKeys.CANCEL)
};
goog.ui.editor.AbstractDialog.prototype.getButtonElement = function (a) {
    return this.dialogInternal_.getButtonSet().getButton(a)
};
goog.ui.editor.AbstractDialog.prototype.handleOk = function (a) {
    return(a = this.createOkEvent(a)) ? this.dispatchEvent(a) : !1
};
goog.ui.editor.AbstractDialog.prototype.handleCancel = function () {
    return this.dispatchEvent(goog.ui.editor.AbstractDialog.EventType.CANCEL)
};
goog.ui.editor.AbstractDialog.prototype.disposeInternal = function () {
    this.dialogInternal_ && (this.hide(), this.dialogInternal_.dispose(), this.dialogInternal_ = null);
    goog.ui.editor.AbstractDialog.superClass_.disposeInternal.call(this)
};
goog.ui.editor.AbstractDialog.prototype.handleAfterHide_ = function () {
    this.dispatchEvent(goog.ui.editor.AbstractDialog.EventType.AFTER_HIDE)
};
goog.events.InputHandler = function (a) {
    goog.events.EventTarget.call(this);
    this.element_ = a;
    a = goog.userAgent.IE || goog.userAgent.WEBKIT && !goog.userAgent.isVersionOrHigher("531") && "TEXTAREA" == a.tagName;
    this.eventHandler_ = new goog.events.EventHandler(this);
    this.eventHandler_.listen(this.element_, a ? ["keydown", "paste", "cut", "drop", "input"] : "input", this)
};
goog.inherits(goog.events.InputHandler, goog.events.EventTarget);
goog.events.InputHandler.EventType = {INPUT: "input"};
goog.events.InputHandler.prototype.timer_ = null;
goog.events.InputHandler.prototype.handleEvent = function (a) {
    if ("input" == a.type)this.cancelTimerIfSet_(), goog.userAgent.OPERA && this.element_ != goog.dom.getOwnerDocument(this.element_).activeElement || this.dispatchEvent(this.createInputEvent_(a)); else if ("keydown" != a.type || goog.events.KeyCodes.isTextModifyingKeyEvent(a)) {
        var b = "keydown" == a.type ? this.element_.value : null;
        goog.userAgent.IE && a.keyCode == goog.events.KeyCodes.WIN_IME && (b = null);
        var c = this.createInputEvent_(a);
        this.cancelTimerIfSet_();
        this.timer_ =
            goog.Timer.callOnce(function () {
                this.timer_ = null;
                this.element_.value != b && this.dispatchEvent(c)
            }, 0, this)
    }
};
goog.events.InputHandler.prototype.cancelTimerIfSet_ = function () {
    null != this.timer_ && (goog.Timer.clear(this.timer_), this.timer_ = null)
};
goog.events.InputHandler.prototype.createInputEvent_ = function (a) {
    a = new goog.events.BrowserEvent(a.getBrowserEvent());
    a.type = goog.events.InputHandler.EventType.INPUT;
    return a
};
goog.events.InputHandler.prototype.disposeInternal = function () {
    goog.events.InputHandler.superClass_.disposeInternal.call(this);
    this.eventHandler_.dispose();
    this.cancelTimerIfSet_();
    delete this.element_
};
goog.editor.style = {};
goog.editor.style.getComputedOrCascadedStyle_ = function (a, b) {
    return a.nodeType != goog.dom.NodeType.ELEMENT ? null : goog.userAgent.IE ? goog.style.getCascadedStyle(a, b) : goog.style.getComputedStyle(a, b)
};
goog.editor.style.isDisplayBlock = function (a) {
    return"block" == goog.editor.style.getComputedOrCascadedStyle_(a, "display")
};
goog.editor.style.isContainer = function (a) {
    var b = a && a.nodeName.toLowerCase();
    return!(!a || !goog.editor.style.isDisplayBlock(a) && "td" != b && "table" != b && "li" != b)
};
goog.editor.style.getContainer = function (a) {
    return goog.dom.getAncestor(a, goog.editor.style.isContainer, !0)
};
goog.editor.style.SELECTABLE_INPUT_TYPES_ = goog.object.createSet("text", "file", "url");
goog.editor.style.cancelMouseDownHelper_ = function (a) {
    var b = a.target.tagName;
    b != goog.dom.TagName.TEXTAREA && b != goog.dom.TagName.INPUT && a.preventDefault()
};
goog.editor.style.makeUnselectable = function (a, b) {
    goog.editor.BrowserFeature.HAS_UNSELECTABLE_STYLE && b.listen(a, goog.events.EventType.MOUSEDOWN, goog.editor.style.cancelMouseDownHelper_, !0);
    goog.style.setUnselectable(a, !0);
    for (var c = a.getElementsByTagName(goog.dom.TagName.INPUT), d = 0, e = c.length; d < e; d++) {
        var f = c[d];
        f.type in goog.editor.style.SELECTABLE_INPUT_TYPES_ && goog.editor.style.makeSelectable(f)
    }
    goog.array.forEach(a.getElementsByTagName(goog.dom.TagName.TEXTAREA), goog.editor.style.makeSelectable)
};
goog.editor.style.makeSelectable = function (a) {
    goog.style.setUnselectable(a, !1);
    if (goog.editor.BrowserFeature.HAS_UNSELECTABLE_STYLE) {
        var b = a;
        for (a = a.parentNode; a && a.tagName != goog.dom.TagName.HTML;) {
            if (goog.style.isUnselectable(a)) {
                goog.style.setUnselectable(a, !1, !0);
                for (var c = 0, d = a.childNodes.length; c < d; c++) {
                    var e = a.childNodes[c];
                    e != b && e.nodeType == goog.dom.NodeType.ELEMENT && goog.style.setUnselectable(a.childNodes[c], !0)
                }
            }
            b = a;
            a = a.parentNode
        }
    }
};
goog.dom.RangeEndpoint = {START: 1, END: 0};
goog.dom.iter = {};
goog.dom.iter.SiblingIterator = function (a, b, c) {
    this.node_ = a;
    this.reverse_ = !!c;
    a && !b && this.next()
};
goog.inherits(goog.dom.iter.SiblingIterator, goog.iter.Iterator);
goog.dom.iter.SiblingIterator.prototype.next = function () {
    var a = this.node_;
    if (!a)throw goog.iter.StopIteration;
    this.node_ = this.reverse_ ? a.previousSibling : a.nextSibling;
    return a
};
goog.dom.iter.ChildIterator = function (a, b, c) {
    goog.isDef(c) || (c = b && a.childNodes.length ? a.childNodes.length - 1 : 0);
    goog.dom.iter.SiblingIterator.call(this, a.childNodes[c], !0, b)
};
goog.inherits(goog.dom.iter.ChildIterator, goog.dom.iter.SiblingIterator);
goog.dom.iter.AncestorIterator = function (a, b) {
    (this.node_ = a) && !b && this.next()
};
goog.inherits(goog.dom.iter.AncestorIterator, goog.iter.Iterator);
goog.dom.iter.AncestorIterator.prototype.next = function () {
    var a = this.node_;
    if (!a)throw goog.iter.StopIteration;
    this.node_ = a.parentNode;
    return a
};
goog.editor.node = {};
goog.editor.node.BLOCK_TAG_NAMES_ = goog.object.createSet(goog.dom.TagName.ADDRESS, goog.dom.TagName.ARTICLE, goog.dom.TagName.ASIDE, goog.dom.TagName.BLOCKQUOTE, goog.dom.TagName.BODY, goog.dom.TagName.CAPTION, goog.dom.TagName.CENTER, goog.dom.TagName.COL, goog.dom.TagName.COLGROUP, goog.dom.TagName.DETAILS, goog.dom.TagName.DIR, goog.dom.TagName.DIV, goog.dom.TagName.DL, goog.dom.TagName.DD, goog.dom.TagName.DT, goog.dom.TagName.FIELDSET, goog.dom.TagName.FIGCAPTION, goog.dom.TagName.FIGURE, goog.dom.TagName.FOOTER, goog.dom.TagName.FORM,
    goog.dom.TagName.H1, goog.dom.TagName.H2, goog.dom.TagName.H3, goog.dom.TagName.H4, goog.dom.TagName.H5, goog.dom.TagName.H6, goog.dom.TagName.HEADER, goog.dom.TagName.HGROUP, goog.dom.TagName.HR, goog.dom.TagName.ISINDEX, goog.dom.TagName.OL, goog.dom.TagName.LI, goog.dom.TagName.MAP, goog.dom.TagName.MENU, goog.dom.TagName.NAV, goog.dom.TagName.OPTGROUP, goog.dom.TagName.OPTION, goog.dom.TagName.P, goog.dom.TagName.PRE, goog.dom.TagName.SECTION, goog.dom.TagName.SUMMARY, goog.dom.TagName.TABLE, goog.dom.TagName.TBODY,
    goog.dom.TagName.TD, goog.dom.TagName.TFOOT, goog.dom.TagName.TH, goog.dom.TagName.THEAD, goog.dom.TagName.TR, goog.dom.TagName.UL);
goog.editor.node.NON_EMPTY_TAGS_ = goog.object.createSet(goog.dom.TagName.IMG, goog.dom.TagName.IFRAME, goog.dom.TagName.EMBED);
goog.editor.node.isStandardsMode = function (a) {
    return goog.dom.getDomHelper(a).isCss1CompatMode()
};
goog.editor.node.getRightMostLeaf = function (a) {
    for (var b; b = goog.editor.node.getLastChild(a);)a = b;
    return a
};
goog.editor.node.getLeftMostLeaf = function (a) {
    for (var b; b = goog.editor.node.getFirstChild(a);)a = b;
    return a
};
goog.editor.node.getFirstChild = function (a) {
    return goog.editor.node.getChildHelper_(a, !1)
};
goog.editor.node.getLastChild = function (a) {
    return goog.editor.node.getChildHelper_(a, !0)
};
goog.editor.node.getPreviousSibling = function (a) {
    return goog.editor.node.getFirstValue_(goog.iter.filter(new goog.dom.iter.SiblingIterator(a, !1, !0), goog.editor.node.isImportant))
};
goog.editor.node.getNextSibling = function (a) {
    return goog.editor.node.getFirstValue_(goog.iter.filter(new goog.dom.iter.SiblingIterator(a), goog.editor.node.isImportant))
};
goog.editor.node.getChildHelper_ = function (a, b) {
    return a && a.nodeType == goog.dom.NodeType.ELEMENT ? goog.editor.node.getFirstValue_(goog.iter.filter(new goog.dom.iter.ChildIterator(a, b), goog.editor.node.isImportant)) : null
};
goog.editor.node.getFirstValue_ = function (a) {
    try {
        return a.next()
    } catch (b) {
        return null
    }
};
goog.editor.node.isImportant = function (a) {
    return a.nodeType == goog.dom.NodeType.ELEMENT || a.nodeType == goog.dom.NodeType.TEXT && !goog.editor.node.isAllNonNbspWhiteSpace(a)
};
goog.editor.node.isAllNonNbspWhiteSpace = function (a) {
    return goog.string.isBreakingWhitespace(a.nodeValue)
};
goog.editor.node.isEmpty = function (a, b) {
    var c = goog.dom.getRawTextContent(a);
    if (a.getElementsByTagName)for (var d in goog.editor.node.NON_EMPTY_TAGS_)if (a.tagName == d || 0 < a.getElementsByTagName(d).length)return!1;
    return!b && c == goog.string.Unicode.NBSP || goog.string.isBreakingWhitespace(c)
};
goog.editor.node.getLength = function (a) {
    return a.length || a.childNodes.length
};
goog.editor.node.findInChildren = function (a, b) {
    for (var c = 0, d = a.childNodes.length; c < d; c++)if (b(a.childNodes[c]))return c;
    return null
};
goog.editor.node.findHighestMatchingAncestor = function (a, b) {
    for (var c = a.parentNode, d = null; c && b(c);)d = c, c = c.parentNode;
    return d
};
goog.editor.node.isBlockTag = function (a) {
    return!!goog.editor.node.BLOCK_TAG_NAMES_[a.tagName]
};
goog.editor.node.skipEmptyTextNodes = function (a) {
    for (; a && a.nodeType == goog.dom.NodeType.TEXT && !a.nodeValue;)a = a.nextSibling;
    return a
};
goog.editor.node.isEditableContainer = function (a) {
    return a.getAttribute && "true" == a.getAttribute("g_editable")
};
goog.editor.node.isEditable = function (a) {
    return!!goog.dom.getAncestor(a, goog.editor.node.isEditableContainer)
};
goog.editor.node.findTopMostEditableAncestor = function (a, b) {
    for (var c = null; a && !goog.editor.node.isEditableContainer(a);)b(a) && (c = a), a = a.parentNode;
    return c
};
goog.editor.node.splitDomTreeAt = function (a, b, c) {
    for (var d; a != c && (d = a.parentNode);)b = goog.editor.node.getSecondHalfOfNode_(d, a, b), a = d;
    return b
};
goog.editor.node.getSecondHalfOfNode_ = function (a, b, c) {
    for (a = a.cloneNode(!1); b.nextSibling;)goog.dom.appendChild(a, b.nextSibling);
    c && a.insertBefore(c, a.firstChild);
    return a
};
goog.editor.node.transferChildren = function (a, b) {
    goog.dom.append(a, b.childNodes)
};
goog.editor.node.replaceInnerHtml = function (a, b) {
    goog.userAgent.IE && goog.dom.removeChildren(a);
    a.innerHTML = b
};
goog.dom.SavedRange = function () {
    goog.Disposable.call(this)
};
goog.inherits(goog.dom.SavedRange, goog.Disposable);
goog.dom.SavedRange.logger_ = goog.log.getLogger("goog.dom.SavedRange");
goog.dom.SavedRange.prototype.restore = function (a) {
    this.isDisposed() && goog.log.error(goog.dom.SavedRange.logger_, "Disposed SavedRange objects cannot be restored.");
    var b = this.restoreInternal();
    a || this.dispose();
    return b
};
goog.dom.SavedCaretRange = function (a) {
    goog.dom.SavedRange.call(this);
    this.startCaretId_ = goog.string.createUniqueString();
    this.endCaretId_ = goog.string.createUniqueString();
    this.reversed_ = a.isReversed();
    this.dom_ = goog.dom.getDomHelper(a.getDocument());
    a.surroundWithNodes(this.createCaret_(!0), this.createCaret_(!1))
};
goog.inherits(goog.dom.SavedCaretRange, goog.dom.SavedRange);
goog.dom.SavedCaretRange.prototype.toAbstractRange = function () {
    var a = null, b = this.getCaret(!0), c = this.getCaret(!1);
    b && c && (a = goog.dom.Range.createFromNodes(b, 0, c, 0));
    return a
};
goog.dom.SavedCaretRange.prototype.getCaret = function (a) {
    return this.dom_.getElement(a ? this.startCaretId_ : this.endCaretId_)
};
goog.dom.SavedCaretRange.prototype.removeCarets = function (a) {
    goog.dom.removeNode(this.getCaret(!0));
    goog.dom.removeNode(this.getCaret(!1));
    return a
};
goog.dom.SavedCaretRange.prototype.setRestorationDocument = function (a) {
    this.dom_.setDocument(a)
};
goog.dom.SavedCaretRange.prototype.restoreInternal = function () {
    var a = null, b = this.getCaret(!this.reversed_), c = this.getCaret(this.reversed_);
    if (b && c) {
        var a = b.parentNode, b = goog.array.indexOf(a.childNodes, b), d = c.parentNode, c = goog.array.indexOf(d.childNodes, c);
        d == a && (this.reversed_ ? b-- : c--);
        a = goog.dom.Range.createFromNodes(a, b, d, c);
        a = this.removeCarets(a);
        a.select()
    } else this.removeCarets();
    return a
};
goog.dom.SavedCaretRange.prototype.disposeInternal = function () {
    this.removeCarets();
    this.dom_ = null
};
goog.dom.SavedCaretRange.prototype.createCaret_ = function (a) {
    return this.dom_.createDom(goog.dom.TagName.SPAN, {id: a ? this.startCaretId_ : this.endCaretId_})
};
goog.dom.SavedCaretRange.CARET_REGEX = /<span\s+id="?goog_\d+"?><\/span>/ig;
goog.dom.SavedCaretRange.htmlEqual = function (a, b) {
    return a == b || a.replace(goog.dom.SavedCaretRange.CARET_REGEX, "") == b.replace(goog.dom.SavedCaretRange.CARET_REGEX, "")
};
goog.dom.TagWalkType = {START_TAG: 1, OTHER: 0, END_TAG: -1};
goog.dom.TagIterator = function (a, b, c, d, e) {
    this.reversed = !!b;
    a && this.setPosition(a, d);
    this.depth = void 0 != e ? e : this.tagType || 0;
    this.reversed && (this.depth *= -1);
    this.constrained = !c
};
goog.inherits(goog.dom.TagIterator, goog.iter.Iterator);
goog.dom.TagIterator.prototype.node = null;
goog.dom.TagIterator.prototype.tagType = goog.dom.TagWalkType.OTHER;
goog.dom.TagIterator.prototype.started_ = !1;
goog.dom.TagIterator.prototype.setPosition = function (a, b, c) {
    if (this.node = a)goog.isNumber(b) ? this.tagType = b : this.tagType = this.node.nodeType != goog.dom.NodeType.ELEMENT ? goog.dom.TagWalkType.OTHER : this.reversed ? goog.dom.TagWalkType.END_TAG : goog.dom.TagWalkType.START_TAG;
    goog.isNumber(c) && (this.depth = c)
};
goog.dom.TagIterator.prototype.copyFrom = function (a) {
    this.node = a.node;
    this.tagType = a.tagType;
    this.depth = a.depth;
    this.reversed = a.reversed;
    this.constrained = a.constrained
};
goog.dom.TagIterator.prototype.clone = function () {
    return new goog.dom.TagIterator(this.node, this.reversed, !this.constrained, this.tagType, this.depth)
};
goog.dom.TagIterator.prototype.skipTag = function () {
    var a = this.reversed ? goog.dom.TagWalkType.END_TAG : goog.dom.TagWalkType.START_TAG;
    this.tagType == a && (this.tagType = -1 * a, this.depth += this.tagType * (this.reversed ? -1 : 1))
};
goog.dom.TagIterator.prototype.restartTag = function () {
    var a = this.reversed ? goog.dom.TagWalkType.START_TAG : goog.dom.TagWalkType.END_TAG;
    this.tagType == a && (this.tagType = -1 * a, this.depth += this.tagType * (this.reversed ? -1 : 1))
};
goog.dom.TagIterator.prototype.next = function () {
    var a;
    if (this.started_) {
        if (!this.node || this.constrained && 0 == this.depth)throw goog.iter.StopIteration;
        a = this.node;
        var b = this.reversed ? goog.dom.TagWalkType.END_TAG : goog.dom.TagWalkType.START_TAG;
        if (this.tagType == b) {
            var c = this.reversed ? a.lastChild : a.firstChild;
            c ? this.setPosition(c) : this.setPosition(a, -1 * b)
        } else(c = this.reversed ? a.previousSibling : a.nextSibling) ? this.setPosition(c) : this.setPosition(a.parentNode, -1 * b);
        this.depth += this.tagType * (this.reversed ?
            -1 : 1)
    } else this.started_ = !0;
    a = this.node;
    if (!this.node)throw goog.iter.StopIteration;
    return a
};
goog.dom.TagIterator.prototype.isStarted = function () {
    return this.started_
};
goog.dom.TagIterator.prototype.isStartTag = function () {
    return this.tagType == goog.dom.TagWalkType.START_TAG
};
goog.dom.TagIterator.prototype.isEndTag = function () {
    return this.tagType == goog.dom.TagWalkType.END_TAG
};
goog.dom.TagIterator.prototype.isNonElement = function () {
    return this.tagType == goog.dom.TagWalkType.OTHER
};
goog.dom.TagIterator.prototype.equals = function (a) {
    return a.node == this.node && (!this.node || a.tagType == this.tagType)
};
goog.dom.TagIterator.prototype.splice = function (a) {
    var b = this.node;
    this.restartTag();
    this.reversed = !this.reversed;
    goog.dom.TagIterator.prototype.next.call(this);
    this.reversed = !this.reversed;
    for (var c = goog.isArrayLike(arguments[0]) ? arguments[0] : arguments, d = c.length - 1; 0 <= d; d--)goog.dom.insertSiblingAfter(c[d], b);
    goog.dom.removeNode(b)
};
goog.dom.RangeType = {TEXT: "text", CONTROL: "control", MULTI: "mutli"};
goog.dom.AbstractRange = function () {
};
goog.dom.AbstractRange.getBrowserSelectionForWindow = function (a) {
    if (a.getSelection)return a.getSelection();
    a = a.document;
    var b = a.selection;
    if (b) {
        try {
            var c = b.createRange();
            if (c.parentElement) {
                if (c.parentElement().document != a)return null
            } else if (!c.length || c.item(0).document != a)return null
        } catch (d) {
            return null
        }
        return b
    }
    return null
};
goog.dom.AbstractRange.isNativeControlRange = function (a) {
    return!!a && !!a.addElement
};
goog.dom.AbstractRange.prototype.setBrowserRangeObject = function (a) {
    return!1
};
goog.dom.AbstractRange.prototype.getTextRanges = function () {
    for (var a = [], b = 0, c = this.getTextRangeCount(); b < c; b++)a.push(this.getTextRange(b));
    return a
};
goog.dom.AbstractRange.prototype.getContainerElement = function () {
    var a = this.getContainer();
    return a.nodeType == goog.dom.NodeType.ELEMENT ? a : a.parentNode
};
goog.dom.AbstractRange.prototype.getAnchorNode = function () {
    return this.isReversed() ? this.getEndNode() : this.getStartNode()
};
goog.dom.AbstractRange.prototype.getAnchorOffset = function () {
    return this.isReversed() ? this.getEndOffset() : this.getStartOffset()
};
goog.dom.AbstractRange.prototype.getFocusNode = function () {
    return this.isReversed() ? this.getStartNode() : this.getEndNode()
};
goog.dom.AbstractRange.prototype.getFocusOffset = function () {
    return this.isReversed() ? this.getStartOffset() : this.getEndOffset()
};
goog.dom.AbstractRange.prototype.isReversed = function () {
    return!1
};
goog.dom.AbstractRange.prototype.getDocument = function () {
    return goog.dom.getOwnerDocument(goog.userAgent.IE ? this.getContainer() : this.getStartNode())
};
goog.dom.AbstractRange.prototype.getWindow = function () {
    return goog.dom.getWindow(this.getDocument())
};
goog.dom.AbstractRange.prototype.containsNode = function (a, b) {
    return this.containsRange(goog.dom.Range.createFromNodeContents(a), b)
};
goog.dom.AbstractRange.prototype.replaceContentsWithNode = function (a) {
    this.isCollapsed() || this.removeContents();
    return this.insertNode(a, !0)
};
goog.dom.AbstractRange.prototype.saveUsingCarets = function () {
    return this.getStartNode() && this.getEndNode() ? new goog.dom.SavedCaretRange(this) : null
};
goog.dom.RangeIterator = function (a, b) {
    goog.dom.TagIterator.call(this, a, b, !0)
};
goog.inherits(goog.dom.RangeIterator, goog.dom.TagIterator);
goog.dom.AbstractMultiRange = function () {
};
goog.inherits(goog.dom.AbstractMultiRange, goog.dom.AbstractRange);
goog.dom.AbstractMultiRange.prototype.containsRange = function (a, b) {
    var c = this.getTextRanges(), d = a.getTextRanges();
    return(b ? goog.array.some : goog.array.every)(d, function (a) {
        return goog.array.some(c, function (c) {
            return c.containsRange(a, b)
        })
    })
};
goog.dom.AbstractMultiRange.prototype.insertNode = function (a, b) {
    b ? goog.dom.insertSiblingBefore(a, this.getStartNode()) : goog.dom.insertSiblingAfter(a, this.getEndNode());
    return a
};
goog.dom.AbstractMultiRange.prototype.surroundWithNodes = function (a, b) {
    this.insertNode(a, !0);
    this.insertNode(b, !1)
};
goog.dom.TextRangeIterator = function (a, b, c, d, e) {
    var f;
    a && (this.startNode_ = a, this.startOffset_ = b, this.endNode_ = c, this.endOffset_ = d, a.nodeType == goog.dom.NodeType.ELEMENT && a.tagName != goog.dom.TagName.BR && (a = a.childNodes, (b = a[b]) ? (this.startNode_ = b, this.startOffset_ = 0) : (a.length && (this.startNode_ = goog.array.peek(a)), f = !0)), c.nodeType == goog.dom.NodeType.ELEMENT && ((this.endNode_ = c.childNodes[d]) ? this.endOffset_ = 0 : this.endNode_ = c));
    goog.dom.RangeIterator.call(this, e ? this.endNode_ : this.startNode_, e);
    if (f)try {
        this.next()
    } catch (g) {
        if (g !=
            goog.iter.StopIteration)throw g;
    }
};
goog.inherits(goog.dom.TextRangeIterator, goog.dom.RangeIterator);
goog.dom.TextRangeIterator.prototype.startNode_ = null;
goog.dom.TextRangeIterator.prototype.endNode_ = null;
goog.dom.TextRangeIterator.prototype.startOffset_ = 0;
goog.dom.TextRangeIterator.prototype.endOffset_ = 0;
goog.dom.TextRangeIterator.prototype.getStartTextOffset = function () {
    return this.node.nodeType != goog.dom.NodeType.TEXT ? -1 : this.node == this.startNode_ ? this.startOffset_ : 0
};
goog.dom.TextRangeIterator.prototype.getEndTextOffset = function () {
    return this.node.nodeType != goog.dom.NodeType.TEXT ? -1 : this.node == this.endNode_ ? this.endOffset_ : this.node.nodeValue.length
};
goog.dom.TextRangeIterator.prototype.getStartNode = function () {
    return this.startNode_
};
goog.dom.TextRangeIterator.prototype.setStartNode = function (a) {
    this.isStarted() || this.setPosition(a);
    this.startNode_ = a;
    this.startOffset_ = 0
};
goog.dom.TextRangeIterator.prototype.getEndNode = function () {
    return this.endNode_
};
goog.dom.TextRangeIterator.prototype.setEndNode = function (a) {
    this.endNode_ = a;
    this.endOffset_ = 0
};
goog.dom.TextRangeIterator.prototype.isLast = function () {
    return this.isStarted() && this.node == this.endNode_ && (!this.endOffset_ || !this.isStartTag())
};
goog.dom.TextRangeIterator.prototype.next = function () {
    if (this.isLast())throw goog.iter.StopIteration;
    return goog.dom.TextRangeIterator.superClass_.next.call(this)
};
goog.dom.TextRangeIterator.prototype.skipTag = function () {
    goog.dom.TextRangeIterator.superClass_.skipTag.apply(this);
    if (goog.dom.contains(this.node, this.endNode_))throw goog.iter.StopIteration;
};
goog.dom.TextRangeIterator.prototype.copyFrom = function (a) {
    this.startNode_ = a.startNode_;
    this.endNode_ = a.endNode_;
    this.startOffset_ = a.startOffset_;
    this.endOffset_ = a.endOffset_;
    this.isReversed_ = a.isReversed_;
    goog.dom.TextRangeIterator.superClass_.copyFrom.call(this, a)
};
goog.dom.TextRangeIterator.prototype.clone = function () {
    var a = new goog.dom.TextRangeIterator(this.startNode_, this.startOffset_, this.endNode_, this.endOffset_, this.isReversed_);
    a.copyFrom(this);
    return a
};
goog.string.StringBuffer = function (a, b) {
    null != a && this.append.apply(this, arguments)
};
goog.string.StringBuffer.prototype.buffer_ = "";
goog.string.StringBuffer.prototype.set = function (a) {
    this.buffer_ = "" + a
};
goog.string.StringBuffer.prototype.append = function (a, b, c) {
    this.buffer_ += a;
    if (null != b)for (var d = 1; d < arguments.length; d++)this.buffer_ += arguments[d];
    return this
};
goog.string.StringBuffer.prototype.clear = function () {
    this.buffer_ = ""
};
goog.string.StringBuffer.prototype.getLength = function () {
    return this.buffer_.length
};
goog.string.StringBuffer.prototype.toString = function () {
    return this.buffer_
};
goog.dom.browserrange = {};
goog.dom.browserrange.AbstractRange = function () {
};
goog.dom.browserrange.AbstractRange.prototype.getStartPosition = function () {
    goog.asserts.assert(this.range_.getClientRects, "Getting selection coordinates is not supported.");
    var a = this.range_.getClientRects();
    return a.length ? new goog.math.Coordinate(a[0].left, a[0].top) : null
};
goog.dom.browserrange.AbstractRange.prototype.getEndPosition = function () {
    goog.asserts.assert(this.range_.getClientRects, "Getting selection coordinates is not supported.");
    var a = this.range_.getClientRects();
    return a.length ? (a = goog.array.peek(a), new goog.math.Coordinate(a.right, a.bottom)) : null
};
goog.dom.browserrange.AbstractRange.prototype.containsRange = function (a, b) {
    var c = b && !a.isCollapsed(), d = a.getBrowserRange(), e = goog.dom.RangeEndpoint.START, f = goog.dom.RangeEndpoint.END;
    try {
        return c ? 0 <= this.compareBrowserRangeEndpoints(d, f, e) && 0 >= this.compareBrowserRangeEndpoints(d, e, f) : 0 <= this.compareBrowserRangeEndpoints(d, f, f) && 0 >= this.compareBrowserRangeEndpoints(d, e, e)
    } catch (g) {
        if (!goog.userAgent.IE)throw g;
        return!1
    }
};
goog.dom.browserrange.AbstractRange.prototype.containsNode = function (a, b) {
    return this.containsRange(goog.dom.browserrange.createRangeFromNodeContents(a), b)
};
goog.dom.browserrange.AbstractRange.prototype.getHtmlFragment = function () {
    var a = new goog.string.StringBuffer;
    goog.iter.forEach(this, function (b, c, d) {
        b.nodeType == goog.dom.NodeType.TEXT ? a.append(goog.string.htmlEscape(b.nodeValue.substring(d.getStartTextOffset(), d.getEndTextOffset()))) : b.nodeType == goog.dom.NodeType.ELEMENT && (d.isEndTag() ? goog.dom.canHaveChildren(b) && a.append("</" + b.tagName + ">") : (c = b.cloneNode(!1), c = goog.dom.getOuterHtml(c), goog.userAgent.IE && b.tagName == goog.dom.TagName.LI ? a.append(c) :
            (b = c.lastIndexOf("<"), a.append(b ? c.substr(0, b) : c))))
    }, this);
    return a.toString()
};
goog.dom.browserrange.AbstractRange.prototype.__iterator__ = function (a) {
    return new goog.dom.TextRangeIterator(this.getStartNode(), this.getStartOffset(), this.getEndNode(), this.getEndOffset())
};
goog.dom.browserrange.W3cRange = function (a) {
    this.range_ = a
};
goog.inherits(goog.dom.browserrange.W3cRange, goog.dom.browserrange.AbstractRange);
goog.dom.browserrange.W3cRange.getBrowserRangeForNode = function (a) {
    var b = goog.dom.getOwnerDocument(a).createRange();
    if (a.nodeType == goog.dom.NodeType.TEXT)b.setStart(a, 0), b.setEnd(a, a.length); else if (goog.dom.browserrange.canContainRangeEndpoint(a)) {
        for (var c, d = a; (c = d.firstChild) && goog.dom.browserrange.canContainRangeEndpoint(c);)d = c;
        b.setStart(d, 0);
        for (d = a; (c = d.lastChild) && goog.dom.browserrange.canContainRangeEndpoint(c);)d = c;
        b.setEnd(d, d.nodeType == goog.dom.NodeType.ELEMENT ? d.childNodes.length : d.length)
    } else c =
        a.parentNode, a = goog.array.indexOf(c.childNodes, a), b.setStart(c, a), b.setEnd(c, a + 1);
    return b
};
goog.dom.browserrange.W3cRange.getBrowserRangeForNodes = function (a, b, c, d) {
    var e = goog.dom.getOwnerDocument(a).createRange();
    e.setStart(a, b);
    e.setEnd(c, d);
    return e
};
goog.dom.browserrange.W3cRange.createFromNodeContents = function (a) {
    return new goog.dom.browserrange.W3cRange(goog.dom.browserrange.W3cRange.getBrowserRangeForNode(a))
};
goog.dom.browserrange.W3cRange.createFromNodes = function (a, b, c, d) {
    return new goog.dom.browserrange.W3cRange(goog.dom.browserrange.W3cRange.getBrowserRangeForNodes(a, b, c, d))
};
goog.dom.browserrange.W3cRange.prototype.clone = function () {
    return new this.constructor(this.range_.cloneRange())
};
goog.dom.browserrange.W3cRange.prototype.getBrowserRange = function () {
    return this.range_
};
goog.dom.browserrange.W3cRange.prototype.getContainer = function () {
    return this.range_.commonAncestorContainer
};
goog.dom.browserrange.W3cRange.prototype.getStartNode = function () {
    return this.range_.startContainer
};
goog.dom.browserrange.W3cRange.prototype.getStartOffset = function () {
    return this.range_.startOffset
};
goog.dom.browserrange.W3cRange.prototype.getEndNode = function () {
    return this.range_.endContainer
};
goog.dom.browserrange.W3cRange.prototype.getEndOffset = function () {
    return this.range_.endOffset
};
goog.dom.browserrange.W3cRange.prototype.compareBrowserRangeEndpoints = function (a, b, c) {
    return this.range_.compareBoundaryPoints(c == goog.dom.RangeEndpoint.START ? b == goog.dom.RangeEndpoint.START ? goog.global.Range.START_TO_START : goog.global.Range.START_TO_END : b == goog.dom.RangeEndpoint.START ? goog.global.Range.END_TO_START : goog.global.Range.END_TO_END, a)
};
goog.dom.browserrange.W3cRange.prototype.isCollapsed = function () {
    return this.range_.collapsed
};
goog.dom.browserrange.W3cRange.prototype.getText = function () {
    return this.range_.toString()
};
goog.dom.browserrange.W3cRange.prototype.getValidHtml = function () {
    var a = goog.dom.getDomHelper(this.range_.startContainer).createDom("div");
    a.appendChild(this.range_.cloneContents());
    a = a.innerHTML;
    if (goog.string.startsWith(a, "<") || !this.isCollapsed() && !goog.string.contains(a, "<"))return a;
    var b = this.getContainer(), b = b.nodeType == goog.dom.NodeType.ELEMENT ? b : b.parentNode;
    return goog.dom.getOuterHtml(b.cloneNode(!1)).replace(">", ">" + a)
};
goog.dom.browserrange.W3cRange.prototype.select = function (a) {
    var b = goog.dom.getWindow(goog.dom.getOwnerDocument(this.getStartNode()));
    this.selectInternal(b.getSelection(), a)
};
goog.dom.browserrange.W3cRange.prototype.selectInternal = function (a, b) {
    a.removeAllRanges();
    a.addRange(this.range_)
};
goog.dom.browserrange.W3cRange.prototype.removeContents = function () {
    var a = this.range_;
    a.extractContents();
    if (a.startContainer.hasChildNodes() && (a = a.startContainer.childNodes[a.startOffset])) {
        var b = a.previousSibling;
        "" == goog.dom.getRawTextContent(a) && goog.dom.removeNode(a);
        b && "" == goog.dom.getRawTextContent(b) && goog.dom.removeNode(b)
    }
};
goog.dom.browserrange.W3cRange.prototype.surroundContents = function (a) {
    this.range_.surroundContents(a);
    return a
};
goog.dom.browserrange.W3cRange.prototype.insertNode = function (a, b) {
    var c = this.range_.cloneRange();
    c.collapse(b);
    c.insertNode(a);
    c.detach();
    return a
};
goog.dom.browserrange.W3cRange.prototype.surroundWithNodes = function (a, b) {
    var c = goog.dom.getWindow(goog.dom.getOwnerDocument(this.getStartNode()));
    if (c = goog.dom.Range.createFromWindow(c))var d = c.getStartNode(), e = c.getEndNode(), f = c.getStartOffset(), g = c.getEndOffset();
    var h = this.range_.cloneRange(), k = this.range_.cloneRange();
    h.collapse(!1);
    k.collapse(!0);
    h.insertNode(b);
    k.insertNode(a);
    h.detach();
    k.detach();
    if (c) {
        if (d.nodeType == goog.dom.NodeType.TEXT)for (; f > d.length;) {
            f -= d.length;
            do d = d.nextSibling;
            while (d == a || d == b)
        }
        if (e.nodeType == goog.dom.NodeType.TEXT)for (; g > e.length;) {
            g -= e.length;
            do e = e.nextSibling; while (e == a || e == b)
        }
        goog.dom.Range.createFromNodes(d, f, e, g).select()
    }
};
goog.dom.browserrange.W3cRange.prototype.collapse = function (a) {
    this.range_.collapse(a)
};
goog.dom.browserrange.WebKitRange = function (a) {
    goog.dom.browserrange.W3cRange.call(this, a)
};
goog.inherits(goog.dom.browserrange.WebKitRange, goog.dom.browserrange.W3cRange);
goog.dom.browserrange.WebKitRange.createFromNodeContents = function (a) {
    return new goog.dom.browserrange.WebKitRange(goog.dom.browserrange.W3cRange.getBrowserRangeForNode(a))
};
goog.dom.browserrange.WebKitRange.createFromNodes = function (a, b, c, d) {
    return new goog.dom.browserrange.WebKitRange(goog.dom.browserrange.W3cRange.getBrowserRangeForNodes(a, b, c, d))
};
goog.dom.browserrange.WebKitRange.prototype.compareBrowserRangeEndpoints = function (a, b, c) {
    return goog.userAgent.isVersionOrHigher("528") ? goog.dom.browserrange.WebKitRange.superClass_.compareBrowserRangeEndpoints.call(this, a, b, c) : this.range_.compareBoundaryPoints(c == goog.dom.RangeEndpoint.START ? b == goog.dom.RangeEndpoint.START ? goog.global.Range.START_TO_START : goog.global.Range.END_TO_START : b == goog.dom.RangeEndpoint.START ? goog.global.Range.START_TO_END : goog.global.Range.END_TO_END, a)
};
goog.dom.browserrange.WebKitRange.prototype.selectInternal = function (a, b) {
    a.removeAllRanges();
    b ? a.setBaseAndExtent(this.getEndNode(), this.getEndOffset(), this.getStartNode(), this.getStartOffset()) : a.setBaseAndExtent(this.getStartNode(), this.getStartOffset(), this.getEndNode(), this.getEndOffset())
};
goog.dom.browserrange.IeRange = function (a, b) {
    this.range_ = a;
    this.doc_ = b
};
goog.inherits(goog.dom.browserrange.IeRange, goog.dom.browserrange.AbstractRange);
goog.dom.browserrange.IeRange.logger_ = goog.log.getLogger("goog.dom.browserrange.IeRange");
goog.dom.browserrange.IeRange.getBrowserRangeForNode_ = function (a) {
    var b = goog.dom.getOwnerDocument(a).body.createTextRange();
    if (a.nodeType == goog.dom.NodeType.ELEMENT)b.moveToElementText(a), goog.dom.browserrange.canContainRangeEndpoint(a) && !a.childNodes.length && b.collapse(!1); else {
        for (var c = 0, d = a; d = d.previousSibling;) {
            var e = d.nodeType;
            if (e == goog.dom.NodeType.TEXT)c += d.length; else if (e == goog.dom.NodeType.ELEMENT) {
                b.moveToElementText(d);
                break
            }
        }
        d || b.moveToElementText(a.parentNode);
        b.collapse(!d);
        c && b.move("character",
            c);
        b.moveEnd("character", a.length)
    }
    return b
};
goog.dom.browserrange.IeRange.getBrowserRangeForNodes_ = function (a, b, c, d) {
    var e = !1;
    a.nodeType == goog.dom.NodeType.ELEMENT && (b > a.childNodes.length && goog.log.error(goog.dom.browserrange.IeRange.logger_, "Cannot have startOffset > startNode child count"), b = a.childNodes[b], e = !b, a = b || a.lastChild || a, b = 0);
    var f = goog.dom.browserrange.IeRange.getBrowserRangeForNode_(a);
    b && f.move("character", b);
    if (a == c && b == d)return f.collapse(!0), f;
    e && f.collapse(!1);
    e = !1;
    c.nodeType == goog.dom.NodeType.ELEMENT && (d > c.childNodes.length &&
        goog.log.error(goog.dom.browserrange.IeRange.logger_, "Cannot have endOffset > endNode child count"), c = (b = c.childNodes[d]) || c.lastChild || c, d = 0, e = !b);
    a = goog.dom.browserrange.IeRange.getBrowserRangeForNode_(c);
    a.collapse(!e);
    d && a.moveEnd("character", d);
    f.setEndPoint("EndToEnd", a);
    return f
};
goog.dom.browserrange.IeRange.createFromNodeContents = function (a) {
    var b = new goog.dom.browserrange.IeRange(goog.dom.browserrange.IeRange.getBrowserRangeForNode_(a), goog.dom.getOwnerDocument(a));
    if (goog.dom.browserrange.canContainRangeEndpoint(a)) {
        for (var c, d = a; (c = d.firstChild) && goog.dom.browserrange.canContainRangeEndpoint(c);)d = c;
        b.startNode_ = d;
        b.startOffset_ = 0;
        for (d = a; (c = d.lastChild) && goog.dom.browserrange.canContainRangeEndpoint(c);)d = c;
        b.endNode_ = d;
        b.endOffset_ = d.nodeType == goog.dom.NodeType.ELEMENT ?
            d.childNodes.length : d.length;
        b.parentNode_ = a
    } else b.startNode_ = b.endNode_ = b.parentNode_ = a.parentNode, b.startOffset_ = goog.array.indexOf(b.parentNode_.childNodes, a), b.endOffset_ = b.startOffset_ + 1;
    return b
};
goog.dom.browserrange.IeRange.createFromNodes = function (a, b, c, d) {
    var e = new goog.dom.browserrange.IeRange(goog.dom.browserrange.IeRange.getBrowserRangeForNodes_(a, b, c, d), goog.dom.getOwnerDocument(a));
    e.startNode_ = a;
    e.startOffset_ = b;
    e.endNode_ = c;
    e.endOffset_ = d;
    return e
};
goog.dom.browserrange.IeRange.prototype.parentNode_ = null;
goog.dom.browserrange.IeRange.prototype.startNode_ = null;
goog.dom.browserrange.IeRange.prototype.endNode_ = null;
goog.dom.browserrange.IeRange.prototype.startOffset_ = -1;
goog.dom.browserrange.IeRange.prototype.endOffset_ = -1;
goog.dom.browserrange.IeRange.prototype.clone = function () {
    var a = new goog.dom.browserrange.IeRange(this.range_.duplicate(), this.doc_);
    a.parentNode_ = this.parentNode_;
    a.startNode_ = this.startNode_;
    a.endNode_ = this.endNode_;
    return a
};
goog.dom.browserrange.IeRange.prototype.getBrowserRange = function () {
    return this.range_
};
goog.dom.browserrange.IeRange.prototype.clearCachedValues_ = function () {
    this.parentNode_ = this.startNode_ = this.endNode_ = null;
    this.startOffset_ = this.endOffset_ = -1
};
goog.dom.browserrange.IeRange.prototype.getContainer = function () {
    if (!this.parentNode_) {
        var a = this.range_.text, b = this.range_.duplicate(), c = a.replace(/ +$/, "");
        (c = a.length - c.length) && b.moveEnd("character", -c);
        c = b.parentElement();
        b = goog.string.stripNewlines(b.htmlText).length;
        if (this.isCollapsed() && 0 < b)return this.parentNode_ = c;
        for (; b > goog.string.stripNewlines(c.outerHTML).length;)c = c.parentNode;
        for (; 1 == c.childNodes.length && c.innerText == goog.dom.browserrange.IeRange.getNodeText_(c.firstChild) && goog.dom.browserrange.canContainRangeEndpoint(c.firstChild);)c =
            c.firstChild;
        0 == a.length && (c = this.findDeepestContainer_(c));
        this.parentNode_ = c
    }
    return this.parentNode_
};
goog.dom.browserrange.IeRange.prototype.findDeepestContainer_ = function (a) {
    for (var b = a.childNodes, c = 0, d = b.length; c < d; c++) {
        var e = b[c];
        if (goog.dom.browserrange.canContainRangeEndpoint(e)) {
            var f = goog.dom.browserrange.IeRange.getBrowserRangeForNode_(e), g = goog.dom.RangeEndpoint.START, h = goog.dom.RangeEndpoint.END, k = f.htmlText != e.outerHTML;
            if (this.isCollapsed() && k ? 0 <= this.compareBrowserRangeEndpoints(f, g, g) && 0 >= this.compareBrowserRangeEndpoints(f, g, h) : this.range_.inRange(f))return this.findDeepestContainer_(e)
        }
    }
    return a
};
goog.dom.browserrange.IeRange.prototype.getStartNode = function () {
    this.startNode_ || (this.startNode_ = this.getEndpointNode_(goog.dom.RangeEndpoint.START), this.isCollapsed() && (this.endNode_ = this.startNode_));
    return this.startNode_
};
goog.dom.browserrange.IeRange.prototype.getStartOffset = function () {
    0 > this.startOffset_ && (this.startOffset_ = this.getOffset_(goog.dom.RangeEndpoint.START), this.isCollapsed() && (this.endOffset_ = this.startOffset_));
    return this.startOffset_
};
goog.dom.browserrange.IeRange.prototype.getEndNode = function () {
    if (this.isCollapsed())return this.getStartNode();
    this.endNode_ || (this.endNode_ = this.getEndpointNode_(goog.dom.RangeEndpoint.END));
    return this.endNode_
};
goog.dom.browserrange.IeRange.prototype.getEndOffset = function () {
    if (this.isCollapsed())return this.getStartOffset();
    0 > this.endOffset_ && (this.endOffset_ = this.getOffset_(goog.dom.RangeEndpoint.END), this.isCollapsed() && (this.startOffset_ = this.endOffset_));
    return this.endOffset_
};
goog.dom.browserrange.IeRange.prototype.compareBrowserRangeEndpoints = function (a, b, c) {
    return this.range_.compareEndPoints((b == goog.dom.RangeEndpoint.START ? "Start" : "End") + "To" + (c == goog.dom.RangeEndpoint.START ? "Start" : "End"), a)
};
goog.dom.browserrange.IeRange.prototype.getEndpointNode_ = function (a, b) {
    var c = b || this.getContainer();
    if (!c || !c.firstChild)return c;
    for (var d = goog.dom.RangeEndpoint.START, e = goog.dom.RangeEndpoint.END, f = a == d, g = 0, h = c.childNodes.length; g < h; g++) {
        var k = f ? g : h - g - 1, l = c.childNodes[k], m;
        try {
            m = goog.dom.browserrange.createRangeFromNodeContents(l)
        } catch (n) {
            continue
        }
        var p = m.getBrowserRange();
        if (this.isCollapsed())if (!goog.dom.browserrange.canContainRangeEndpoint(l)) {
            if (0 == this.compareBrowserRangeEndpoints(p, d, d)) {
                this.startOffset_ =
                    this.endOffset_ = k;
                break
            }
        } else {
            if (m.containsRange(this))return this.getEndpointNode_(a, l)
        } else {
            if (this.containsRange(m)) {
                if (!goog.dom.browserrange.canContainRangeEndpoint(l)) {
                    f ? this.startOffset_ = k : this.endOffset_ = k + 1;
                    break
                }
                return this.getEndpointNode_(a, l)
            }
            if (0 > this.compareBrowserRangeEndpoints(p, d, e) && 0 < this.compareBrowserRangeEndpoints(p, e, d))return this.getEndpointNode_(a, l)
        }
    }
    return c
};
goog.dom.browserrange.IeRange.prototype.compareNodeEndpoints_ = function (a, b, c) {
    return this.range_.compareEndPoints((b == goog.dom.RangeEndpoint.START ? "Start" : "End") + "To" + (c == goog.dom.RangeEndpoint.START ? "Start" : "End"), goog.dom.browserrange.createRangeFromNodeContents(a).getBrowserRange())
};
goog.dom.browserrange.IeRange.prototype.getOffset_ = function (a, b) {
    var c = a == goog.dom.RangeEndpoint.START, d = b || (c ? this.getStartNode() : this.getEndNode());
    if (d.nodeType == goog.dom.NodeType.ELEMENT) {
        for (var d = d.childNodes, e = d.length, f = c ? 1 : -1, g = c ? 0 : e - 1; 0 <= g && g < e; g += f) {
            var h = d[g];
            if (!goog.dom.browserrange.canContainRangeEndpoint(h) && 0 == this.compareNodeEndpoints_(h, a, a))return c ? g : g + 1
        }
        return-1 == g ? 0 : g
    }
    e = this.range_.duplicate();
    f = goog.dom.browserrange.IeRange.getBrowserRangeForNode_(d);
    e.setEndPoint(c ? "EndToEnd" :
        "StartToStart", f);
    e = e.text.length;
    return c ? d.length - e : e
};
goog.dom.browserrange.IeRange.getNodeText_ = function (a) {
    return a.nodeType == goog.dom.NodeType.TEXT ? a.nodeValue : a.innerText
};
goog.dom.browserrange.IeRange.prototype.isRangeInDocument = function () {
    var a = this.doc_.body.createTextRange();
    a.moveToElementText(this.doc_.body);
    return this.containsRange(new goog.dom.browserrange.IeRange(a, this.doc_), !0)
};
goog.dom.browserrange.IeRange.prototype.isCollapsed = function () {
    return 0 == this.range_.compareEndPoints("StartToEnd", this.range_)
};
goog.dom.browserrange.IeRange.prototype.getText = function () {
    return this.range_.text
};
goog.dom.browserrange.IeRange.prototype.getValidHtml = function () {
    return this.range_.htmlText
};
goog.dom.browserrange.IeRange.prototype.select = function (a) {
    this.range_.select()
};
goog.dom.browserrange.IeRange.prototype.removeContents = function () {
    if (!this.isCollapsed() && this.range_.htmlText) {
        var a = this.getStartNode(), b = this.getEndNode(), c = this.range_.text, d = this.range_.duplicate();
        d.moveStart("character", 1);
        d.moveStart("character", -1);
        d.text == c && (this.range_ = d);
        this.range_.text = "";
        this.clearCachedValues_();
        c = this.getStartNode();
        d = this.getStartOffset();
        try {
            var e = a.nextSibling;
            a == b && (a.parentNode && a.nodeType == goog.dom.NodeType.TEXT && e && e.nodeType == goog.dom.NodeType.TEXT) && (a.nodeValue +=
                e.nodeValue, goog.dom.removeNode(e), this.range_ = goog.dom.browserrange.IeRange.getBrowserRangeForNode_(c), this.range_.move("character", d), this.clearCachedValues_())
        } catch (f) {
        }
    }
};
goog.dom.browserrange.IeRange.getDomHelper_ = function (a) {
    return goog.dom.getDomHelper(a.parentElement())
};
goog.dom.browserrange.IeRange.pasteElement_ = function (a, b, c) {
    c = c || goog.dom.browserrange.IeRange.getDomHelper_(a);
    var d, e = d = b.id;
    d || (d = b.id = goog.string.createUniqueString());
    a.pasteHTML(b.outerHTML);
    (b = c.getElement(d)) && (e || b.removeAttribute("id"));
    return b
};
goog.dom.browserrange.IeRange.prototype.surroundContents = function (a) {
    goog.dom.removeNode(a);
    a.innerHTML = this.range_.htmlText;
    (a = goog.dom.browserrange.IeRange.pasteElement_(this.range_, a)) && this.range_.moveToElementText(a);
    this.clearCachedValues_();
    return a
};
goog.dom.browserrange.IeRange.insertNode_ = function (a, b, c, d) {
    d = d || goog.dom.browserrange.IeRange.getDomHelper_(a);
    var e;
    b.nodeType != goog.dom.NodeType.ELEMENT && (e = !0, b = d.createDom(goog.dom.TagName.DIV, null, b));
    a.collapse(c);
    b = goog.dom.browserrange.IeRange.pasteElement_(a, b, d);
    e && (a = b.firstChild, d.flattenElement(b), b = a);
    return b
};
goog.dom.browserrange.IeRange.prototype.insertNode = function (a, b) {
    var c = goog.dom.browserrange.IeRange.insertNode_(this.range_.duplicate(), a, b);
    this.clearCachedValues_();
    return c
};
goog.dom.browserrange.IeRange.prototype.surroundWithNodes = function (a, b) {
    var c = this.range_.duplicate(), d = this.range_.duplicate();
    goog.dom.browserrange.IeRange.insertNode_(c, a, !0);
    goog.dom.browserrange.IeRange.insertNode_(d, b, !1);
    this.clearCachedValues_()
};
goog.dom.browserrange.IeRange.prototype.collapse = function (a) {
    this.range_.collapse(a);
    a ? (this.endNode_ = this.startNode_, this.endOffset_ = this.startOffset_) : (this.startNode_ = this.endNode_, this.startOffset_ = this.endOffset_)
};
goog.dom.browserrange.GeckoRange = function (a) {
    goog.dom.browserrange.W3cRange.call(this, a)
};
goog.inherits(goog.dom.browserrange.GeckoRange, goog.dom.browserrange.W3cRange);
goog.dom.browserrange.GeckoRange.createFromNodeContents = function (a) {
    return new goog.dom.browserrange.GeckoRange(goog.dom.browserrange.W3cRange.getBrowserRangeForNode(a))
};
goog.dom.browserrange.GeckoRange.createFromNodes = function (a, b, c, d) {
    return new goog.dom.browserrange.GeckoRange(goog.dom.browserrange.W3cRange.getBrowserRangeForNodes(a, b, c, d))
};
goog.dom.browserrange.GeckoRange.prototype.selectInternal = function (a, b) {
    !b || this.isCollapsed() ? goog.dom.browserrange.GeckoRange.superClass_.selectInternal.call(this, a, b) : (a.collapse(this.getEndNode(), this.getEndOffset()), a.extend(this.getStartNode(), this.getStartOffset()))
};
goog.dom.browserrange.OperaRange = function (a) {
    goog.dom.browserrange.W3cRange.call(this, a)
};
goog.inherits(goog.dom.browserrange.OperaRange, goog.dom.browserrange.W3cRange);
goog.dom.browserrange.OperaRange.createFromNodeContents = function (a) {
    return new goog.dom.browserrange.OperaRange(goog.dom.browserrange.W3cRange.getBrowserRangeForNode(a))
};
goog.dom.browserrange.OperaRange.createFromNodes = function (a, b, c, d) {
    return new goog.dom.browserrange.OperaRange(goog.dom.browserrange.W3cRange.getBrowserRangeForNodes(a, b, c, d))
};
goog.dom.browserrange.OperaRange.prototype.selectInternal = function (a, b) {
    a.collapse(this.getStartNode(), this.getStartOffset());
    this.getEndNode() == this.getStartNode() && this.getEndOffset() == this.getStartOffset() || a.extend(this.getEndNode(), this.getEndOffset());
    0 == a.rangeCount && a.addRange(this.range_)
};
goog.dom.browserrange.Error = {NOT_IMPLEMENTED: "Not Implemented"};
goog.dom.browserrange.createRange = function (a) {
    return goog.userAgent.IE && !goog.userAgent.isDocumentModeOrHigher(9) ? new goog.dom.browserrange.IeRange(a, goog.dom.getOwnerDocument(a.parentElement())) : goog.userAgent.WEBKIT ? new goog.dom.browserrange.WebKitRange(a) : goog.userAgent.GECKO ? new goog.dom.browserrange.GeckoRange(a) : goog.userAgent.OPERA ? new goog.dom.browserrange.OperaRange(a) : new goog.dom.browserrange.W3cRange(a)
};
goog.dom.browserrange.createRangeFromNodeContents = function (a) {
    return goog.userAgent.IE && !goog.userAgent.isDocumentModeOrHigher(9) ? goog.dom.browserrange.IeRange.createFromNodeContents(a) : goog.userAgent.WEBKIT ? goog.dom.browserrange.WebKitRange.createFromNodeContents(a) : goog.userAgent.GECKO ? goog.dom.browserrange.GeckoRange.createFromNodeContents(a) : goog.userAgent.OPERA ? goog.dom.browserrange.OperaRange.createFromNodeContents(a) : goog.dom.browserrange.W3cRange.createFromNodeContents(a)
};
goog.dom.browserrange.createRangeFromNodes = function (a, b, c, d) {
    return goog.userAgent.IE && !goog.userAgent.isDocumentModeOrHigher(9) ? goog.dom.browserrange.IeRange.createFromNodes(a, b, c, d) : goog.userAgent.WEBKIT ? goog.dom.browserrange.WebKitRange.createFromNodes(a, b, c, d) : goog.userAgent.GECKO ? goog.dom.browserrange.GeckoRange.createFromNodes(a, b, c, d) : goog.userAgent.OPERA ? goog.dom.browserrange.OperaRange.createFromNodes(a, b, c, d) : goog.dom.browserrange.W3cRange.createFromNodes(a, b, c, d)
};
goog.dom.browserrange.canContainRangeEndpoint = function (a) {
    return goog.dom.canHaveChildren(a) || a.nodeType == goog.dom.NodeType.TEXT
};
goog.dom.TextRange = function () {
};
goog.inherits(goog.dom.TextRange, goog.dom.AbstractRange);
goog.dom.TextRange.createFromBrowserRange = function (a, b) {
    return goog.dom.TextRange.createFromBrowserRangeWrapper_(goog.dom.browserrange.createRange(a), b)
};
goog.dom.TextRange.createFromBrowserRangeWrapper_ = function (a, b) {
    var c = new goog.dom.TextRange;
    c.browserRangeWrapper_ = a;
    c.isReversed_ = !!b;
    return c
};
goog.dom.TextRange.createFromNodeContents = function (a, b) {
    return goog.dom.TextRange.createFromBrowserRangeWrapper_(goog.dom.browserrange.createRangeFromNodeContents(a), b)
};
goog.dom.TextRange.createFromNodes = function (a, b, c, d) {
    var e = new goog.dom.TextRange;
    e.isReversed_ = goog.dom.Range.isReversed(a, b, c, d);
    if (goog.dom.isElement(a) && !goog.dom.canHaveChildren(a)) {
        var f = a.parentNode;
        b = goog.array.indexOf(f.childNodes, a);
        a = f
    }
    goog.dom.isElement(c) && !goog.dom.canHaveChildren(c) && (f = c.parentNode, d = goog.array.indexOf(f.childNodes, c), c = f);
    e.isReversed_ ? (e.startNode_ = c, e.startOffset_ = d, e.endNode_ = a, e.endOffset_ = b) : (e.startNode_ = a, e.startOffset_ = b, e.endNode_ = c, e.endOffset_ = d);
    return e
};
goog.dom.TextRange.prototype.browserRangeWrapper_ = null;
goog.dom.TextRange.prototype.startNode_ = null;
goog.dom.TextRange.prototype.startOffset_ = null;
goog.dom.TextRange.prototype.endNode_ = null;
goog.dom.TextRange.prototype.endOffset_ = null;
goog.dom.TextRange.prototype.isReversed_ = !1;
goog.dom.TextRange.prototype.clone = function () {
    var a = new goog.dom.TextRange;
    a.browserRangeWrapper_ = this.browserRangeWrapper_;
    a.startNode_ = this.startNode_;
    a.startOffset_ = this.startOffset_;
    a.endNode_ = this.endNode_;
    a.endOffset_ = this.endOffset_;
    a.isReversed_ = this.isReversed_;
    return a
};
goog.dom.TextRange.prototype.getType = function () {
    return goog.dom.RangeType.TEXT
};
goog.dom.TextRange.prototype.getBrowserRangeObject = function () {
    return this.getBrowserRangeWrapper_().getBrowserRange()
};
goog.dom.TextRange.prototype.setBrowserRangeObject = function (a) {
    if (goog.dom.AbstractRange.isNativeControlRange(a))return!1;
    this.browserRangeWrapper_ = goog.dom.browserrange.createRange(a);
    this.clearCachedValues_();
    return!0
};
goog.dom.TextRange.prototype.clearCachedValues_ = function () {
    this.startNode_ = this.startOffset_ = this.endNode_ = this.endOffset_ = null
};
goog.dom.TextRange.prototype.getTextRangeCount = function () {
    return 1
};
goog.dom.TextRange.prototype.getTextRange = function (a) {
    return this
};
goog.dom.TextRange.prototype.getBrowserRangeWrapper_ = function () {
    return this.browserRangeWrapper_ || (this.browserRangeWrapper_ = goog.dom.browserrange.createRangeFromNodes(this.getStartNode(), this.getStartOffset(), this.getEndNode(), this.getEndOffset()))
};
goog.dom.TextRange.prototype.getContainer = function () {
    return this.getBrowserRangeWrapper_().getContainer()
};
goog.dom.TextRange.prototype.getStartNode = function () {
    return this.startNode_ || (this.startNode_ = this.getBrowserRangeWrapper_().getStartNode())
};
goog.dom.TextRange.prototype.getStartOffset = function () {
    return null != this.startOffset_ ? this.startOffset_ : this.startOffset_ = this.getBrowserRangeWrapper_().getStartOffset()
};
goog.dom.TextRange.prototype.getStartPosition = function () {
    return this.isReversed() ? this.getBrowserRangeWrapper_().getEndPosition() : this.getBrowserRangeWrapper_().getStartPosition()
};
goog.dom.TextRange.prototype.getEndNode = function () {
    return this.endNode_ || (this.endNode_ = this.getBrowserRangeWrapper_().getEndNode())
};
goog.dom.TextRange.prototype.getEndOffset = function () {
    return null != this.endOffset_ ? this.endOffset_ : this.endOffset_ = this.getBrowserRangeWrapper_().getEndOffset()
};
goog.dom.TextRange.prototype.getEndPosition = function () {
    return this.isReversed() ? this.getBrowserRangeWrapper_().getStartPosition() : this.getBrowserRangeWrapper_().getEndPosition()
};
goog.dom.TextRange.prototype.moveToNodes = function (a, b, c, d, e) {
    this.startNode_ = a;
    this.startOffset_ = b;
    this.endNode_ = c;
    this.endOffset_ = d;
    this.isReversed_ = e;
    this.browserRangeWrapper_ = null
};
goog.dom.TextRange.prototype.isReversed = function () {
    return this.isReversed_
};
goog.dom.TextRange.prototype.containsRange = function (a, b) {
    var c = a.getType();
    return c == goog.dom.RangeType.TEXT ? this.getBrowserRangeWrapper_().containsRange(a.getBrowserRangeWrapper_(), b) : c == goog.dom.RangeType.CONTROL ? (c = a.getElements(), (b ? goog.array.some : goog.array.every)(c, function (a) {
        return this.containsNode(a, b)
    }, this)) : !1
};
goog.dom.TextRange.isAttachedNode = function (a) {
    if (goog.userAgent.IE && !goog.userAgent.isDocumentModeOrHigher(9)) {
        var b = !1;
        try {
            b = a.parentNode
        } catch (c) {
        }
        return!!b
    }
    return goog.dom.contains(a.ownerDocument.body, a)
};
goog.dom.TextRange.prototype.isRangeInDocument = function () {
    return(!this.startNode_ || goog.dom.TextRange.isAttachedNode(this.startNode_)) && (!this.endNode_ || goog.dom.TextRange.isAttachedNode(this.endNode_)) && (!(goog.userAgent.IE && !goog.userAgent.isDocumentModeOrHigher(9)) || this.getBrowserRangeWrapper_().isRangeInDocument())
};
goog.dom.TextRange.prototype.isCollapsed = function () {
    return this.getBrowserRangeWrapper_().isCollapsed()
};
goog.dom.TextRange.prototype.getText = function () {
    return this.getBrowserRangeWrapper_().getText()
};
goog.dom.TextRange.prototype.getHtmlFragment = function () {
    return this.getBrowserRangeWrapper_().getHtmlFragment()
};
goog.dom.TextRange.prototype.getValidHtml = function () {
    return this.getBrowserRangeWrapper_().getValidHtml()
};
goog.dom.TextRange.prototype.getPastableHtml = function () {
    var a = this.getValidHtml();
    if (a.match(/^\s*<td\b/i))a = "<table><tbody><tr>" + a + "</tr></tbody></table>"; else if (a.match(/^\s*<tr\b/i))a = "<table><tbody>" + a + "</tbody></table>"; else if (a.match(/^\s*<tbody\b/i))a = "<table>" + a + "</table>"; else if (a.match(/^\s*<li\b/i)) {
        for (var b = this.getContainer(), c = goog.dom.TagName.UL; b;) {
            if (b.tagName == goog.dom.TagName.OL) {
                c = goog.dom.TagName.OL;
                break
            } else if (b.tagName == goog.dom.TagName.UL)break;
            b = b.parentNode
        }
        a = goog.string.buildString("<",
            c, ">", a, "</", c, ">")
    }
    return a
};
goog.dom.TextRange.prototype.__iterator__ = function (a) {
    return new goog.dom.TextRangeIterator(this.getStartNode(), this.getStartOffset(), this.getEndNode(), this.getEndOffset())
};
goog.dom.TextRange.prototype.select = function () {
    this.getBrowserRangeWrapper_().select(this.isReversed_)
};
goog.dom.TextRange.prototype.removeContents = function () {
    this.getBrowserRangeWrapper_().removeContents();
    this.clearCachedValues_()
};
goog.dom.TextRange.prototype.surroundContents = function (a) {
    a = this.getBrowserRangeWrapper_().surroundContents(a);
    this.clearCachedValues_();
    return a
};
goog.dom.TextRange.prototype.insertNode = function (a, b) {
    var c = this.getBrowserRangeWrapper_().insertNode(a, b);
    this.clearCachedValues_();
    return c
};
goog.dom.TextRange.prototype.surroundWithNodes = function (a, b) {
    this.getBrowserRangeWrapper_().surroundWithNodes(a, b);
    this.clearCachedValues_()
};
goog.dom.TextRange.prototype.saveUsingDom = function () {
    return new goog.dom.DomSavedTextRange_(this)
};
goog.dom.TextRange.prototype.collapse = function (a) {
    a = this.isReversed() ? !a : a;
    this.browserRangeWrapper_ && this.browserRangeWrapper_.collapse(a);
    a ? (this.endNode_ = this.startNode_, this.endOffset_ = this.startOffset_) : (this.startNode_ = this.endNode_, this.startOffset_ = this.endOffset_);
    this.isReversed_ = !1
};
goog.dom.DomSavedTextRange_ = function (a) {
    this.anchorNode_ = a.getAnchorNode();
    this.anchorOffset_ = a.getAnchorOffset();
    this.focusNode_ = a.getFocusNode();
    this.focusOffset_ = a.getFocusOffset()
};
goog.inherits(goog.dom.DomSavedTextRange_, goog.dom.SavedRange);
goog.dom.DomSavedTextRange_.prototype.restoreInternal = function () {
    return goog.dom.Range.createFromNodes(this.anchorNode_, this.anchorOffset_, this.focusNode_, this.focusOffset_)
};
goog.dom.DomSavedTextRange_.prototype.disposeInternal = function () {
    goog.dom.DomSavedTextRange_.superClass_.disposeInternal.call(this);
    this.focusNode_ = this.anchorNode_ = null
};
goog.dom.ControlRange = function () {
};
goog.inherits(goog.dom.ControlRange, goog.dom.AbstractMultiRange);
goog.dom.ControlRange.createFromBrowserRange = function (a) {
    var b = new goog.dom.ControlRange;
    b.range_ = a;
    return b
};
goog.dom.ControlRange.createFromElements = function (a) {
    for (var b = goog.dom.getOwnerDocument(arguments[0]).body.createControlRange(), c = 0, d = arguments.length; c < d; c++)b.addElement(arguments[c]);
    return goog.dom.ControlRange.createFromBrowserRange(b)
};
goog.dom.ControlRange.prototype.range_ = null;
goog.dom.ControlRange.prototype.elements_ = null;
goog.dom.ControlRange.prototype.sortedElements_ = null;
goog.dom.ControlRange.prototype.clearCachedValues_ = function () {
    this.sortedElements_ = this.elements_ = null
};
goog.dom.ControlRange.prototype.clone = function () {
    return goog.dom.ControlRange.createFromElements.apply(this, this.getElements())
};
goog.dom.ControlRange.prototype.getType = function () {
    return goog.dom.RangeType.CONTROL
};
goog.dom.ControlRange.prototype.getBrowserRangeObject = function () {
    return this.range_ || document.body.createControlRange()
};
goog.dom.ControlRange.prototype.setBrowserRangeObject = function (a) {
    if (!goog.dom.AbstractRange.isNativeControlRange(a))return!1;
    this.range_ = a;
    return!0
};
goog.dom.ControlRange.prototype.getTextRangeCount = function () {
    return this.range_ ? this.range_.length : 0
};
goog.dom.ControlRange.prototype.getTextRange = function (a) {
    return goog.dom.TextRange.createFromNodeContents(this.range_.item(a))
};
goog.dom.ControlRange.prototype.getContainer = function () {
    return goog.dom.findCommonAncestor.apply(null, this.getElements())
};
goog.dom.ControlRange.prototype.getStartNode = function () {
    return this.getSortedElements()[0]
};
goog.dom.ControlRange.prototype.getStartOffset = function () {
    return 0
};
goog.dom.ControlRange.prototype.getEndNode = function () {
    var a = this.getSortedElements(), b = goog.array.peek(a);
    return goog.array.find(a, function (a) {
        return goog.dom.contains(a, b)
    })
};
goog.dom.ControlRange.prototype.getEndOffset = function () {
    return this.getEndNode().childNodes.length
};
goog.dom.ControlRange.prototype.getElements = function () {
    if (!this.elements_ && (this.elements_ = [], this.range_))for (var a = 0; a < this.range_.length; a++)this.elements_.push(this.range_.item(a));
    return this.elements_
};
goog.dom.ControlRange.prototype.getSortedElements = function () {
    this.sortedElements_ || (this.sortedElements_ = this.getElements().concat(), this.sortedElements_.sort(function (a, b) {
        return a.sourceIndex - b.sourceIndex
    }));
    return this.sortedElements_
};
goog.dom.ControlRange.prototype.isRangeInDocument = function () {
    var a = !1;
    try {
        a = goog.array.every(this.getElements(), function (a) {
            return goog.userAgent.IE ? !!a.parentNode : goog.dom.contains(a.ownerDocument.body, a)
        })
    } catch (b) {
    }
    return a
};
goog.dom.ControlRange.prototype.isCollapsed = function () {
    return!this.range_ || !this.range_.length
};
goog.dom.ControlRange.prototype.getText = function () {
    return""
};
goog.dom.ControlRange.prototype.getHtmlFragment = function () {
    return goog.array.map(this.getSortedElements(), goog.dom.getOuterHtml).join("")
};
goog.dom.ControlRange.prototype.getValidHtml = function () {
    return this.getHtmlFragment()
};
goog.dom.ControlRange.prototype.getPastableHtml = goog.dom.ControlRange.prototype.getValidHtml;
goog.dom.ControlRange.prototype.__iterator__ = function (a) {
    return new goog.dom.ControlRangeIterator(this)
};
goog.dom.ControlRange.prototype.select = function () {
    this.range_ && this.range_.select()
};
goog.dom.ControlRange.prototype.removeContents = function () {
    if (this.range_) {
        for (var a = [], b = 0, c = this.range_.length; b < c; b++)a.push(this.range_.item(b));
        goog.array.forEach(a, goog.dom.removeNode);
        this.collapse(!1)
    }
};
goog.dom.ControlRange.prototype.replaceContentsWithNode = function (a) {
    a = this.insertNode(a, !0);
    this.isCollapsed() || this.removeContents();
    return a
};
goog.dom.ControlRange.prototype.saveUsingDom = function () {
    return new goog.dom.DomSavedControlRange_(this)
};
goog.dom.ControlRange.prototype.collapse = function (a) {
    this.range_ = null;
    this.clearCachedValues_()
};
goog.dom.DomSavedControlRange_ = function (a) {
    this.elements_ = a.getElements()
};
goog.inherits(goog.dom.DomSavedControlRange_, goog.dom.SavedRange);
goog.dom.DomSavedControlRange_.prototype.restoreInternal = function () {
    for (var a = (this.elements_.length ? goog.dom.getOwnerDocument(this.elements_[0]) : document).body.createControlRange(), b = 0, c = this.elements_.length; b < c; b++)a.addElement(this.elements_[b]);
    return goog.dom.ControlRange.createFromBrowserRange(a)
};
goog.dom.DomSavedControlRange_.prototype.disposeInternal = function () {
    goog.dom.DomSavedControlRange_.superClass_.disposeInternal.call(this);
    delete this.elements_
};
goog.dom.ControlRangeIterator = function (a) {
    a && (this.elements_ = a.getSortedElements(), this.startNode_ = this.elements_.shift(), this.endNode_ = goog.array.peek(this.elements_) || this.startNode_);
    goog.dom.RangeIterator.call(this, this.startNode_, !1)
};
goog.inherits(goog.dom.ControlRangeIterator, goog.dom.RangeIterator);
goog.dom.ControlRangeIterator.prototype.startNode_ = null;
goog.dom.ControlRangeIterator.prototype.endNode_ = null;
goog.dom.ControlRangeIterator.prototype.elements_ = null;
goog.dom.ControlRangeIterator.prototype.getStartTextOffset = function () {
    return 0
};
goog.dom.ControlRangeIterator.prototype.getEndTextOffset = function () {
    return 0
};
goog.dom.ControlRangeIterator.prototype.getStartNode = function () {
    return this.startNode_
};
goog.dom.ControlRangeIterator.prototype.getEndNode = function () {
    return this.endNode_
};
goog.dom.ControlRangeIterator.prototype.isLast = function () {
    return!this.depth && !this.elements_.length
};
goog.dom.ControlRangeIterator.prototype.next = function () {
    if (this.isLast())throw goog.iter.StopIteration;
    if (!this.depth) {
        var a = this.elements_.shift();
        this.setPosition(a, goog.dom.TagWalkType.START_TAG, goog.dom.TagWalkType.START_TAG);
        return a
    }
    return goog.dom.ControlRangeIterator.superClass_.next.call(this)
};
goog.dom.ControlRangeIterator.prototype.copyFrom = function (a) {
    this.elements_ = a.elements_;
    this.startNode_ = a.startNode_;
    this.endNode_ = a.endNode_;
    goog.dom.ControlRangeIterator.superClass_.copyFrom.call(this, a)
};
goog.dom.ControlRangeIterator.prototype.clone = function () {
    var a = new goog.dom.ControlRangeIterator(null);
    a.copyFrom(this);
    return a
};
goog.dom.MultiRange = function () {
    this.browserRanges_ = [];
    this.ranges_ = [];
    this.container_ = this.sortedRanges_ = null
};
goog.inherits(goog.dom.MultiRange, goog.dom.AbstractMultiRange);
goog.dom.MultiRange.createFromBrowserSelection = function (a) {
    for (var b = new goog.dom.MultiRange, c = 0, d = a.rangeCount; c < d; c++)b.browserRanges_.push(a.getRangeAt(c));
    return b
};
goog.dom.MultiRange.createFromBrowserRanges = function (a) {
    var b = new goog.dom.MultiRange;
    b.browserRanges_ = goog.array.clone(a);
    return b
};
goog.dom.MultiRange.createFromTextRanges = function (a) {
    var b = new goog.dom.MultiRange;
    b.ranges_ = a;
    b.browserRanges_ = goog.array.map(a, function (a) {
        return a.getBrowserRangeObject()
    });
    return b
};
goog.dom.MultiRange.prototype.logger_ = goog.log.getLogger("goog.dom.MultiRange");
goog.dom.MultiRange.prototype.clearCachedValues_ = function () {
    this.ranges_ = [];
    this.container_ = this.sortedRanges_ = null
};
goog.dom.MultiRange.prototype.clone = function () {
    return goog.dom.MultiRange.createFromBrowserRanges(this.browserRanges_)
};
goog.dom.MultiRange.prototype.getType = function () {
    return goog.dom.RangeType.MULTI
};
goog.dom.MultiRange.prototype.getBrowserRangeObject = function () {
    1 < this.browserRanges_.length && goog.log.warning(this.logger_, "getBrowserRangeObject called on MultiRange with more than 1 range");
    return this.browserRanges_[0]
};
goog.dom.MultiRange.prototype.setBrowserRangeObject = function (a) {
    return!1
};
goog.dom.MultiRange.prototype.getTextRangeCount = function () {
    return this.browserRanges_.length
};
goog.dom.MultiRange.prototype.getTextRange = function (a) {
    this.ranges_[a] || (this.ranges_[a] = goog.dom.TextRange.createFromBrowserRange(this.browserRanges_[a]));
    return this.ranges_[a]
};
goog.dom.MultiRange.prototype.getContainer = function () {
    if (!this.container_) {
        for (var a = [], b = 0, c = this.getTextRangeCount(); b < c; b++)a.push(this.getTextRange(b).getContainer());
        this.container_ = goog.dom.findCommonAncestor.apply(null, a)
    }
    return this.container_
};
goog.dom.MultiRange.prototype.getSortedRanges = function () {
    this.sortedRanges_ || (this.sortedRanges_ = this.getTextRanges(), this.sortedRanges_.sort(function (a, b) {
        var c = a.getStartNode(), d = a.getStartOffset(), e = b.getStartNode(), f = b.getStartOffset();
        return c == e && d == f ? 0 : goog.dom.Range.isReversed(c, d, e, f) ? 1 : -1
    }));
    return this.sortedRanges_
};
goog.dom.MultiRange.prototype.getStartNode = function () {
    return this.getSortedRanges()[0].getStartNode()
};
goog.dom.MultiRange.prototype.getStartOffset = function () {
    return this.getSortedRanges()[0].getStartOffset()
};
goog.dom.MultiRange.prototype.getEndNode = function () {
    return goog.array.peek(this.getSortedRanges()).getEndNode()
};
goog.dom.MultiRange.prototype.getEndOffset = function () {
    return goog.array.peek(this.getSortedRanges()).getEndOffset()
};
goog.dom.MultiRange.prototype.isRangeInDocument = function () {
    return goog.array.every(this.getTextRanges(), function (a) {
        return a.isRangeInDocument()
    })
};
goog.dom.MultiRange.prototype.isCollapsed = function () {
    return 0 == this.browserRanges_.length || 1 == this.browserRanges_.length && this.getTextRange(0).isCollapsed()
};
goog.dom.MultiRange.prototype.getText = function () {
    return goog.array.map(this.getTextRanges(),function (a) {
        return a.getText()
    }).join("")
};
goog.dom.MultiRange.prototype.getHtmlFragment = function () {
    return this.getValidHtml()
};
goog.dom.MultiRange.prototype.getValidHtml = function () {
    return goog.array.map(this.getTextRanges(),function (a) {
        return a.getValidHtml()
    }).join("")
};
goog.dom.MultiRange.prototype.getPastableHtml = function () {
    return this.getValidHtml()
};
goog.dom.MultiRange.prototype.__iterator__ = function (a) {
    return new goog.dom.MultiRangeIterator(this)
};
goog.dom.MultiRange.prototype.select = function () {
    var a = goog.dom.AbstractRange.getBrowserSelectionForWindow(this.getWindow());
    a.removeAllRanges();
    for (var b = 0, c = this.getTextRangeCount(); b < c; b++)a.addRange(this.getTextRange(b).getBrowserRangeObject())
};
goog.dom.MultiRange.prototype.removeContents = function () {
    goog.array.forEach(this.getTextRanges(), function (a) {
        a.removeContents()
    })
};
goog.dom.MultiRange.prototype.saveUsingDom = function () {
    return new goog.dom.DomSavedMultiRange_(this)
};
goog.dom.MultiRange.prototype.collapse = function (a) {
    if (!this.isCollapsed()) {
        var b = a ? this.getTextRange(0) : this.getTextRange(this.getTextRangeCount() - 1);
        this.clearCachedValues_();
        b.collapse(a);
        this.ranges_ = [b];
        this.sortedRanges_ = [b];
        this.browserRanges_ = [b.getBrowserRangeObject()]
    }
};
goog.dom.DomSavedMultiRange_ = function (a) {
    this.savedRanges_ = goog.array.map(a.getTextRanges(), function (a) {
        return a.saveUsingDom()
    })
};
goog.inherits(goog.dom.DomSavedMultiRange_, goog.dom.SavedRange);
goog.dom.DomSavedMultiRange_.prototype.restoreInternal = function () {
    var a = goog.array.map(this.savedRanges_, function (a) {
        return a.restore()
    });
    return goog.dom.MultiRange.createFromTextRanges(a)
};
goog.dom.DomSavedMultiRange_.prototype.disposeInternal = function () {
    goog.dom.DomSavedMultiRange_.superClass_.disposeInternal.call(this);
    goog.array.forEach(this.savedRanges_, function (a) {
        a.dispose()
    });
    delete this.savedRanges_
};
goog.dom.MultiRangeIterator = function (a) {
    a && (this.iterators_ = goog.array.map(a.getSortedRanges(), function (a) {
        return goog.iter.toIterator(a)
    }));
    goog.dom.RangeIterator.call(this, a ? this.getStartNode() : null, !1)
};
goog.inherits(goog.dom.MultiRangeIterator, goog.dom.RangeIterator);
goog.dom.MultiRangeIterator.prototype.iterators_ = null;
goog.dom.MultiRangeIterator.prototype.currentIdx_ = 0;
goog.dom.MultiRangeIterator.prototype.getStartTextOffset = function () {
    return this.iterators_[this.currentIdx_].getStartTextOffset()
};
goog.dom.MultiRangeIterator.prototype.getEndTextOffset = function () {
    return this.iterators_[this.currentIdx_].getEndTextOffset()
};
goog.dom.MultiRangeIterator.prototype.getStartNode = function () {
    return this.iterators_[0].getStartNode()
};
goog.dom.MultiRangeIterator.prototype.getEndNode = function () {
    return goog.array.peek(this.iterators_).getEndNode()
};
goog.dom.MultiRangeIterator.prototype.isLast = function () {
    return this.iterators_[this.currentIdx_].isLast()
};
goog.dom.MultiRangeIterator.prototype.next = function () {
    try {
        var a = this.iterators_[this.currentIdx_], b = a.next();
        this.setPosition(a.node, a.tagType, a.depth);
        return b
    } catch (c) {
        if (c !== goog.iter.StopIteration || this.iterators_.length - 1 == this.currentIdx_)throw c;
        this.currentIdx_++;
        return this.next()
    }
};
goog.dom.MultiRangeIterator.prototype.copyFrom = function (a) {
    this.iterators_ = goog.array.clone(a.iterators_);
    goog.dom.MultiRangeIterator.superClass_.copyFrom.call(this, a)
};
goog.dom.MultiRangeIterator.prototype.clone = function () {
    var a = new goog.dom.MultiRangeIterator(null);
    a.copyFrom(this);
    return a
};
goog.dom.Range = {};
goog.dom.Range.createFromWindow = function (a) {
    return(a = goog.dom.AbstractRange.getBrowserSelectionForWindow(a || window)) && goog.dom.Range.createFromBrowserSelection(a)
};
goog.dom.Range.createFromBrowserSelection = function (a) {
    var b, c = !1;
    if (a.createRange)try {
        b = a.createRange()
    } catch (d) {
        return null
    } else if (a.rangeCount) {
        if (1 < a.rangeCount)return goog.dom.MultiRange.createFromBrowserSelection(a);
        b = a.getRangeAt(0);
        c = goog.dom.Range.isReversed(a.anchorNode, a.anchorOffset, a.focusNode, a.focusOffset)
    } else return null;
    return goog.dom.Range.createFromBrowserRange(b, c)
};
goog.dom.Range.createFromBrowserRange = function (a, b) {
    return goog.dom.AbstractRange.isNativeControlRange(a) ? goog.dom.ControlRange.createFromBrowserRange(a) : goog.dom.TextRange.createFromBrowserRange(a, b)
};
goog.dom.Range.createFromNodeContents = function (a, b) {
    return goog.dom.TextRange.createFromNodeContents(a, b)
};
goog.dom.Range.createCaret = function (a, b) {
    return goog.dom.TextRange.createFromNodes(a, b, a, b)
};
goog.dom.Range.createFromNodes = function (a, b, c, d) {
    return goog.dom.TextRange.createFromNodes(a, b, c, d)
};
goog.dom.Range.clearSelection = function (a) {
    if (a = goog.dom.AbstractRange.getBrowserSelectionForWindow(a || window))if (a.empty)try {
        a.empty()
    } catch (b) {
    } else try {
        a.removeAllRanges()
    } catch (c) {
    }
};
goog.dom.Range.hasSelection = function (a) {
    a = goog.dom.AbstractRange.getBrowserSelectionForWindow(a || window);
    return!!a && (goog.userAgent.IE ? "None" != a.type : !!a.rangeCount)
};
goog.dom.Range.isReversed = function (a, b, c, d) {
    if (a == c)return d < b;
    var e;
    if (a.nodeType == goog.dom.NodeType.ELEMENT && b)if (e = a.childNodes[b])a = e, b = 0; else if (goog.dom.contains(a, c))return!0;
    if (c.nodeType == goog.dom.NodeType.ELEMENT && d)if (e = c.childNodes[d])c = e, d = 0; else if (goog.dom.contains(c, a))return!1;
    return 0 < (goog.dom.compareNodeOrder(a, c) || b - d)
};
goog.editor.range = {};
goog.editor.range.narrow = function (a, b) {
    var c = a.getStartNode(), d = a.getEndNode();
    if (c && d) {
        var e = function (a) {
            return a == b
        }, c = goog.dom.getAncestor(c, e, !0), d = goog.dom.getAncestor(d, e, !0);
        if (c && d)return a.clone();
        if (c)return d = goog.editor.node.getRightMostLeaf(b), goog.dom.Range.createFromNodes(a.getStartNode(), a.getStartOffset(), d, goog.editor.node.getLength(d));
        if (d)return goog.dom.Range.createFromNodes(goog.editor.node.getLeftMostLeaf(b), 0, a.getEndNode(), a.getEndOffset())
    }
    return null
};
goog.editor.range.expand = function (a, b) {
    var c = goog.editor.range.expandEndPointToContainer_(a, goog.dom.RangeEndpoint.START, b), c = goog.editor.range.expandEndPointToContainer_(c, goog.dom.RangeEndpoint.END, b), d = c.getStartNode(), e = c.getEndNode(), f = c.getStartOffset(), c = c.getEndOffset();
    if (d == e) {
        for (; e != b && 0 == f && c == goog.editor.node.getLength(e);)d = e.parentNode, f = goog.array.indexOf(d.childNodes, e), c = f + 1, e = d;
        d = e
    }
    return goog.dom.Range.createFromNodes(d, f, e, c)
};
goog.editor.range.expandEndPointToContainer_ = function (a, b, c) {
    for (var d = (b = b == goog.dom.RangeEndpoint.START) ? a.getStartNode() : a.getEndNode(), e = b ? a.getStartOffset() : a.getEndOffset(), f = a.getContainerElement(); d != f && d != c && !(b && 0 != e || !b && e != goog.editor.node.getLength(d));)var g = d.parentNode, d = goog.array.indexOf(g.childNodes, d), e = b ? d : d + 1, d = g;
    return goog.dom.Range.createFromNodes(b ? d : a.getStartNode(), b ? e : a.getStartOffset(), b ? a.getEndNode() : d, b ? a.getEndOffset() : e)
};
goog.editor.range.selectNodeStart = function (a) {
    goog.dom.Range.createCaret(goog.editor.node.getLeftMostLeaf(a), 0).select()
};
goog.editor.range.placeCursorNextTo = function (a, b) {
    var c = a.parentNode, d = goog.array.indexOf(c.childNodes, a) + (b ? 0 : 1), c = goog.editor.range.Point.createDeepestPoint(c, d, b, !0), c = goog.dom.Range.createCaret(c.node, c.offset);
    c.select();
    return c
};
goog.editor.range.selectionPreservingNormalize = function (a) {
    var b = goog.dom.getOwnerDocument(a), b = goog.dom.Range.createFromWindow(goog.dom.getWindow(b));
    (a = goog.editor.range.rangePreservingNormalize(a, b)) && a.select()
};
goog.editor.range.normalizeNodeIe_ = function (a) {
    for (var b = null, c = a.firstChild; c;) {
        var d = c.nextSibling;
        c.nodeType == goog.dom.NodeType.TEXT ? "" == c.nodeValue ? a.removeChild(c) : b ? (b.nodeValue += c.nodeValue, a.removeChild(c)) : b = c : (goog.editor.range.normalizeNodeIe_(c), b = null);
        c = d
    }
};
goog.editor.range.normalizeNode = function (a) {
    goog.userAgent.IE ? goog.editor.range.normalizeNodeIe_(a) : a.normalize()
};
goog.editor.range.rangePreservingNormalize = function (a, b) {
    if (b)var c = goog.editor.range.normalize(b), d = goog.editor.style.getContainer(b.getContainerElement());
    d ? goog.editor.range.normalizeNode(goog.dom.findCommonAncestor(d, a)) : a && goog.editor.range.normalizeNode(a);
    return c ? c() : null
};
goog.editor.range.getDeepEndPoint = function (a, b) {
    return b ? goog.editor.range.Point.createDeepestPoint(a.getStartNode(), a.getStartOffset()) : goog.editor.range.Point.createDeepestPoint(a.getEndNode(), a.getEndOffset())
};
goog.editor.range.normalize = function (a) {
    var b = a.isReversed(), c = goog.editor.range.normalizePoint_(goog.editor.range.getDeepEndPoint(a, !b)), d = c.getParentPoint(), e = c.node.previousSibling;
    c.node.nodeType == goog.dom.NodeType.TEXT && (c.node = null);
    var f = goog.editor.range.normalizePoint_(goog.editor.range.getDeepEndPoint(a, b)), g = f.getParentPoint(), h = f.node.previousSibling;
    f.node.nodeType == goog.dom.NodeType.TEXT && (f.node = null);
    return function () {
        !c.node && e && (c.node = e.nextSibling, c.node || (c = goog.editor.range.Point.getPointAtEndOfNode(e)));
        !f.node && h && (f.node = h.nextSibling, f.node || (f = goog.editor.range.Point.getPointAtEndOfNode(h)));
        return goog.dom.Range.createFromNodes(c.node || d.node.firstChild || d.node, c.offset, f.node || g.node.firstChild || g.node, f.offset)
    }
};
goog.editor.range.normalizePoint_ = function (a) {
    var b;
    if (a.node.nodeType == goog.dom.NodeType.TEXT)for (b = a.node.previousSibling; b && b.nodeType == goog.dom.NodeType.TEXT; b = b.previousSibling)a.offset += goog.editor.node.getLength(b); else b = a.node.previousSibling;
    var c = a.node.parentNode;
    a.node = b ? b.nextSibling : c.firstChild;
    return a
};
goog.editor.range.isEditable = function (a) {
    var b = a.getContainerElement();
    return a.getStartNode() != b.parentElement && goog.editor.node.isEditableContainer(b) || goog.editor.node.isEditable(b)
};
goog.editor.range.intersectsTag = function (a, b) {
    return goog.dom.getAncestorByTagNameAndClass(a.getContainerElement(), b) ? !0 : goog.iter.some(a, function (a) {
        return a.tagName == b
    })
};
goog.editor.range.Point = function (a, b) {
    this.node = a;
    this.offset = b
};
goog.editor.range.Point.prototype.getParentPoint = function () {
    var a = this.node.parentNode;
    return new goog.editor.range.Point(a, goog.array.indexOf(a.childNodes, this.node))
};
goog.editor.range.Point.createDeepestPoint = function (a, b, c, d) {
    for (; a.nodeType == goog.dom.NodeType.ELEMENT;) {
        var e = a.childNodes[b];
        if (e || a.lastChild)if (e) {
            var f = e.previousSibling;
            if (c && f) {
                if (d && goog.editor.range.Point.isTerminalElement_(f))break;
                a = f;
                b = goog.editor.node.getLength(a)
            } else {
                if (d && goog.editor.range.Point.isTerminalElement_(e))break;
                a = e;
                b = 0
            }
        } else {
            if (d && goog.editor.range.Point.isTerminalElement_(a.lastChild))break;
            a = a.lastChild;
            b = goog.editor.node.getLength(a)
        } else break
    }
    return new goog.editor.range.Point(a,
        b)
};
goog.editor.range.Point.isTerminalElement_ = function (a) {
    return a.nodeType == goog.dom.NodeType.ELEMENT && !goog.dom.canHaveChildren(a)
};
goog.editor.range.Point.getPointAtEndOfNode = function (a) {
    return new goog.editor.range.Point(a, goog.editor.node.getLength(a))
};
goog.editor.range.saveUsingNormalizedCarets = function (a) {
    return new goog.editor.range.NormalizedCaretRange_(a)
};
goog.editor.range.NormalizedCaretRange_ = function (a) {
    goog.dom.SavedCaretRange.call(this, a)
};
goog.inherits(goog.editor.range.NormalizedCaretRange_, goog.dom.SavedCaretRange);
goog.editor.range.NormalizedCaretRange_.prototype.removeCarets = function (a) {
    var b = this.getCaret(!0), c = this.getCaret(!1), b = b && c ? goog.dom.findCommonAncestor(b, c) : b || c;
    goog.editor.range.NormalizedCaretRange_.superClass_.removeCarets.call(this);
    if (a)return goog.editor.range.rangePreservingNormalize(b, a);
    b && goog.editor.range.selectionPreservingNormalize(b)
};
goog.editor.Link = function (a, b) {
    this.anchor_ = a;
    this.isNew_ = b;
    this.extraAnchors_ = []
};
goog.editor.Link.prototype.getAnchor = function () {
    return this.anchor_
};
goog.editor.Link.prototype.getExtraAnchors = function () {
    return this.extraAnchors_
};
goog.editor.Link.prototype.getCurrentText = function () {
    this.currentText_ || (this.currentText_ = goog.dom.getRawTextContent(this.getAnchor()));
    return this.currentText_
};
goog.editor.Link.prototype.isNew = function () {
    return this.isNew_
};
goog.editor.Link.prototype.initializeUrl = function (a) {
    this.getAnchor().href = a
};
goog.editor.Link.prototype.removeLink = function () {
    goog.dom.flattenElement(this.anchor_);
    for (this.anchor_ = null; this.extraAnchors_.length;)goog.dom.flattenElement(this.extraAnchors_.pop())
};
goog.editor.Link.prototype.setTextAndUrl = function (a, b) {
    var c = this.getAnchor();
    c.href = b;
    var d = this.getCurrentText();
    if (a != d) {
        var e = goog.editor.node.getLeftMostLeaf(c);
        e.nodeType == goog.dom.NodeType.TEXT && (e = e.parentNode);
        goog.dom.getRawTextContent(e) != d && (e = c);
        goog.dom.removeChildren(e);
        c = goog.dom.getDomHelper(e);
        goog.dom.appendChild(e, c.createTextNode(a));
        this.currentText_ = null
    }
    this.isNew_ = !1
};
goog.editor.Link.prototype.placeCursorRightOf = function () {
    var a = this.getAnchor();
    if (goog.editor.BrowserFeature.GETS_STUCK_IN_LINKS) {
        var b;
        b = a.nextSibling;
        b && b.nodeType == goog.dom.NodeType.TEXT && (goog.string.startsWith(b.data, goog.string.Unicode.NBSP) || goog.string.startsWith(b.data, " ")) || (b = goog.dom.getDomHelper(a).createTextNode(goog.string.Unicode.NBSP), goog.dom.insertSiblingAfter(b, a));
        goog.dom.Range.createCaret(b, 1).select()
    } else goog.editor.range.placeCursorNextTo(a, !1)
};
goog.editor.Link.prototype.updateLinkDisplay_ = function (a, b) {
    this.initializeUrl(b);
    this.placeCursorRightOf();
    a.execCommand(goog.editor.Command.UPDATE_LINK_BUBBLE)
};
goog.editor.Link.prototype.getValidLinkFromText = function () {
    var a = this.getCurrentText();
    return goog.editor.Link.isLikelyUrl(a) ? 0 > a.search(/:/) ? "http://" + goog.string.trimLeft(a) : a : goog.editor.Link.isLikelyEmailAddress(a) ? "mailto:" + a : null
};
goog.editor.Link.prototype.finishLinkCreation = function (a) {
    var b = this.getValidLinkFromText();
    b ? this.updateLinkDisplay_(a, b) : a.execCommand(goog.editor.Command.MODAL_LINK_EDITOR, this)
};
goog.editor.Link.createNewLink = function (a, b, c, d) {
    var e = new goog.editor.Link(a, !0);
    e.initializeUrl(b);
    c && (a.target = c);
    d && (e.extraAnchors_ = d);
    return e
};
goog.editor.Link.isLikelyUrl = function (a) {
    if (/\s/.test(a) || goog.editor.Link.isLikelyEmailAddress(a))return!1;
    var b = !1;
    /^[^:\/?#.]+:/.test(a) || (a = "http://" + a, b = !0);
    a = goog.uri.utils.split(a);
    if (-1 != goog.array.indexOf(["mailto", "aim"], a[goog.uri.utils.ComponentIndex.SCHEME]))return!0;
    var c = a[goog.uri.utils.ComponentIndex.DOMAIN];
    if (!c || b && -1 == c.indexOf(".") || /[^\w\d\-\u0100-\uffff.%]/.test(c))return!1;
    b = a[goog.uri.utils.ComponentIndex.PATH];
    return!b || 0 == b.indexOf("/")
};
goog.editor.Link.LIKELY_EMAIL_ADDRESS_ = /^[\w-]+(\.[\w-]+)*\@([\w-]+\.)+(\d+|\w\w+)$/i;
goog.editor.Link.isLikelyEmailAddress = function (a) {
    return goog.editor.Link.LIKELY_EMAIL_ADDRESS_.test(a)
};
goog.editor.Link.isMailto = function (a) {
    return!!a && goog.string.startsWith(a, "mailto:")
};
goog.ui.TabRenderer = function () {
    goog.ui.ControlRenderer.call(this)
};
goog.inherits(goog.ui.TabRenderer, goog.ui.ControlRenderer);
goog.addSingletonGetter(goog.ui.TabRenderer);
goog.ui.TabRenderer.CSS_CLASS = "goog-tab";
goog.ui.TabRenderer.prototype.getCssClass = function () {
    return goog.ui.TabRenderer.CSS_CLASS
};
goog.ui.TabRenderer.prototype.getAriaRole = function () {
    return goog.a11y.aria.Role.TAB
};
goog.ui.TabRenderer.prototype.createDom = function (a) {
    var b = goog.ui.TabRenderer.superClass_.createDom.call(this, a);
    (a = a.getTooltip()) && this.setTooltip(b, a);
    return b
};
goog.ui.TabRenderer.prototype.decorate = function (a, b) {
    b = goog.ui.TabRenderer.superClass_.decorate.call(this, a, b);
    var c = this.getTooltip(b);
    c && a.setTooltipInternal(c);
    a.isSelected() && (c = a.getParent()) && goog.isFunction(c.setSelectedTab) && (a.setState(goog.ui.Component.State.SELECTED, !1), c.setSelectedTab(a));
    return b
};
goog.ui.TabRenderer.prototype.getTooltip = function (a) {
    return a.title || ""
};
goog.ui.TabRenderer.prototype.setTooltip = function (a, b) {
    a && (a.title = b || "")
};
goog.ui.Tab = function (a, b, c) {
    goog.ui.Control.call(this, a, b || goog.ui.TabRenderer.getInstance(), c);
    this.setSupportedState(goog.ui.Component.State.SELECTED, !0);
    this.setDispatchTransitionEvents(goog.ui.Component.State.DISABLED | goog.ui.Component.State.SELECTED, !0)
};
goog.inherits(goog.ui.Tab, goog.ui.Control);
goog.ui.Tab.prototype.getTooltip = function () {
    return this.tooltip_
};
goog.ui.Tab.prototype.setTooltip = function (a) {
    this.getRenderer().setTooltip(this.getElement(), a);
    this.setTooltipInternal(a)
};
goog.ui.Tab.prototype.setTooltipInternal = function (a) {
    this.tooltip_ = a
};
goog.ui.registry.setDecoratorByClassName(goog.ui.TabRenderer.CSS_CLASS, function () {
    return new goog.ui.Tab(null)
});
goog.ui.ContainerRenderer = function () {
};
goog.addSingletonGetter(goog.ui.ContainerRenderer);
goog.ui.ContainerRenderer.getCustomRenderer = function (a, b) {
    var c = new a;
    c.getCssClass = function () {
        return b
    };
    return c
};
goog.ui.ContainerRenderer.CSS_CLASS = "goog-container";
goog.ui.ContainerRenderer.prototype.getAriaRole = function () {
};
goog.ui.ContainerRenderer.prototype.enableTabIndex = function (a, b) {
    a && (a.tabIndex = b ? 0 : -1)
};
goog.ui.ContainerRenderer.prototype.createDom = function (a) {
    return a.getDomHelper().createDom("div", this.getClassNames(a).join(" "))
};
goog.ui.ContainerRenderer.prototype.getContentElement = function (a) {
    return a
};
goog.ui.ContainerRenderer.prototype.canDecorate = function (a) {
    return"DIV" == a.tagName
};
goog.ui.ContainerRenderer.prototype.decorate = function (a, b) {
    b.id && a.setId(b.id);
    var c = this.getCssClass(), d = !1, e = goog.dom.classes.get(b);
    e && goog.array.forEach(e, function (b) {
        b == c ? d = !0 : b && this.setStateFromClassName(a, b, c)
    }, this);
    d || goog.dom.classes.add(b, c);
    this.decorateChildren(a, this.getContentElement(b));
    return b
};
goog.ui.ContainerRenderer.prototype.setStateFromClassName = function (a, b, c) {
    b == c + "-disabled" ? a.setEnabled(!1) : b == c + "-horizontal" ? a.setOrientation(goog.ui.Container.Orientation.HORIZONTAL) : b == c + "-vertical" && a.setOrientation(goog.ui.Container.Orientation.VERTICAL)
};
goog.ui.ContainerRenderer.prototype.decorateChildren = function (a, b, c) {
    if (b) {
        c = c || b.firstChild;
        for (var d; c && c.parentNode == b;) {
            d = c.nextSibling;
            if (c.nodeType == goog.dom.NodeType.ELEMENT) {
                var e = this.getDecoratorForChild(c);
                e && (e.setElementInternal(c), a.isEnabled() || e.setEnabled(!1), a.addChild(e), e.decorate(c))
            } else c.nodeValue && "" != goog.string.trim(c.nodeValue) || b.removeChild(c);
            c = d
        }
    }
};
goog.ui.ContainerRenderer.prototype.getDecoratorForChild = function (a) {
    return goog.ui.registry.getDecorator(a)
};
goog.ui.ContainerRenderer.prototype.initializeDom = function (a) {
    a = a.getElement();
    goog.asserts.assert(a, "The container DOM element cannot be null.");
    goog.style.setUnselectable(a, !0, goog.userAgent.GECKO);
    goog.userAgent.IE && (a.hideFocus = !0);
    var b = this.getAriaRole();
    b && goog.a11y.aria.setRole(a, b)
};
goog.ui.ContainerRenderer.prototype.getKeyEventTarget = function (a) {
    return a.getElement()
};
goog.ui.ContainerRenderer.prototype.getCssClass = function () {
    return goog.ui.ContainerRenderer.CSS_CLASS
};
goog.ui.ContainerRenderer.prototype.getClassNames = function (a) {
    var b = this.getCssClass(), c = a.getOrientation() == goog.ui.Container.Orientation.HORIZONTAL, c = [b, c ? b + "-horizontal" : b + "-vertical"];
    a.isEnabled() || c.push(b + "-disabled");
    return c
};
goog.ui.ContainerRenderer.prototype.getDefaultOrientation = function () {
    return goog.ui.Container.Orientation.VERTICAL
};
goog.ui.TabBarRenderer = function () {
    goog.ui.ContainerRenderer.call(this)
};
goog.inherits(goog.ui.TabBarRenderer, goog.ui.ContainerRenderer);
goog.addSingletonGetter(goog.ui.TabBarRenderer);
goog.ui.TabBarRenderer.CSS_CLASS = "goog-tab-bar";
goog.ui.TabBarRenderer.prototype.getCssClass = function () {
    return goog.ui.TabBarRenderer.CSS_CLASS
};
goog.ui.TabBarRenderer.prototype.getAriaRole = function () {
    return goog.a11y.aria.Role.TAB_LIST
};
goog.ui.TabBarRenderer.prototype.setStateFromClassName = function (a, b, c) {
    this.locationByClass_ || this.createLocationByClassMap_();
    var d = this.locationByClass_[b];
    d ? a.setLocation(d) : goog.ui.TabBarRenderer.superClass_.setStateFromClassName.call(this, a, b, c)
};
goog.ui.TabBarRenderer.prototype.getClassNames = function (a) {
    var b = goog.ui.TabBarRenderer.superClass_.getClassNames.call(this, a);
    this.classByLocation_ || this.createClassByLocationMap_();
    b.push(this.classByLocation_[a.getLocation()]);
    return b
};
goog.ui.TabBarRenderer.prototype.createClassByLocationMap_ = function () {
    var a = this.getCssClass();
    this.classByLocation_ = goog.object.create(goog.ui.TabBar.Location.TOP, a + "-top", goog.ui.TabBar.Location.BOTTOM, a + "-bottom", goog.ui.TabBar.Location.START, a + "-start", goog.ui.TabBar.Location.END, a + "-end")
};
goog.ui.TabBarRenderer.prototype.createLocationByClassMap_ = function () {
    this.classByLocation_ || this.createClassByLocationMap_();
    this.locationByClass_ = goog.object.transpose(this.classByLocation_)
};
goog.ui.Container = function (a, b, c) {
    goog.ui.Component.call(this, c);
    this.renderer_ = b || goog.ui.ContainerRenderer.getInstance();
    this.orientation_ = a || this.renderer_.getDefaultOrientation()
};
goog.inherits(goog.ui.Container, goog.ui.Component);
goog.ui.Container.EventType = {AFTER_SHOW: "aftershow", AFTER_HIDE: "afterhide"};
goog.ui.Container.Orientation = {HORIZONTAL: "horizontal", VERTICAL: "vertical"};
goog.ui.Container.prototype.keyEventTarget_ = null;
goog.ui.Container.prototype.keyHandler_ = null;
goog.ui.Container.prototype.renderer_ = null;
goog.ui.Container.prototype.orientation_ = null;
goog.ui.Container.prototype.visible_ = !0;
goog.ui.Container.prototype.enabled_ = !0;
goog.ui.Container.prototype.focusable_ = !0;
goog.ui.Container.prototype.highlightedIndex_ = -1;
goog.ui.Container.prototype.openItem_ = null;
goog.ui.Container.prototype.mouseButtonPressed_ = !1;
goog.ui.Container.prototype.allowFocusableChildren_ = !1;
goog.ui.Container.prototype.openFollowsHighlight_ = !0;
goog.ui.Container.prototype.childElementIdMap_ = null;
goog.ui.Container.prototype.getKeyEventTarget = function () {
    return this.keyEventTarget_ || this.renderer_.getKeyEventTarget(this)
};
goog.ui.Container.prototype.setKeyEventTarget = function (a) {
    if (this.focusable_) {
        var b = this.getKeyEventTarget(), c = this.isInDocument();
        this.keyEventTarget_ = a;
        var d = this.getKeyEventTarget();
        c && (this.keyEventTarget_ = b, this.enableFocusHandling_(!1), this.keyEventTarget_ = a, this.getKeyHandler().attach(d), this.enableFocusHandling_(!0))
    } else throw Error("Can't set key event target for container that doesn't support keyboard focus!");
};
goog.ui.Container.prototype.getKeyHandler = function () {
    return this.keyHandler_ || (this.keyHandler_ = new goog.events.KeyHandler(this.getKeyEventTarget()))
};
goog.ui.Container.prototype.getRenderer = function () {
    return this.renderer_
};
goog.ui.Container.prototype.setRenderer = function (a) {
    if (this.getElement())throw Error(goog.ui.Component.Error.ALREADY_RENDERED);
    this.renderer_ = a
};
goog.ui.Container.prototype.createDom = function () {
    this.setElementInternal(this.renderer_.createDom(this))
};
goog.ui.Container.prototype.getContentElement = function () {
    return this.renderer_.getContentElement(this.getElement())
};
goog.ui.Container.prototype.canDecorate = function (a) {
    return this.renderer_.canDecorate(a)
};
goog.ui.Container.prototype.decorateInternal = function (a) {
    this.setElementInternal(this.renderer_.decorate(this, a));
    "none" == a.style.display && (this.visible_ = !1)
};
goog.ui.Container.prototype.enterDocument = function () {
    goog.ui.Container.superClass_.enterDocument.call(this);
    this.forEachChild(function (a) {
        a.isInDocument() && this.registerChildId_(a)
    }, this);
    var a = this.getElement();
    this.renderer_.initializeDom(this);
    this.setVisible(this.visible_, !0);
    this.getHandler().listen(this, goog.ui.Component.EventType.ENTER, this.handleEnterItem).listen(this, goog.ui.Component.EventType.HIGHLIGHT, this.handleHighlightItem).listen(this, goog.ui.Component.EventType.UNHIGHLIGHT, this.handleUnHighlightItem).listen(this,
        goog.ui.Component.EventType.OPEN, this.handleOpenItem).listen(this, goog.ui.Component.EventType.CLOSE, this.handleCloseItem).listen(a, goog.events.EventType.MOUSEDOWN, this.handleMouseDown).listen(goog.dom.getOwnerDocument(a), goog.events.EventType.MOUSEUP, this.handleDocumentMouseUp).listen(a, [goog.events.EventType.MOUSEDOWN, goog.events.EventType.MOUSEUP, goog.events.EventType.MOUSEOVER, goog.events.EventType.MOUSEOUT, goog.events.EventType.CONTEXTMENU], this.handleChildMouseEvents);
    this.isFocusable() && this.enableFocusHandling_(!0)
};
goog.ui.Container.prototype.enableFocusHandling_ = function (a) {
    var b = this.getHandler(), c = this.getKeyEventTarget();
    a ? b.listen(c, goog.events.EventType.FOCUS, this.handleFocus).listen(c, goog.events.EventType.BLUR, this.handleBlur).listen(this.getKeyHandler(), goog.events.KeyHandler.EventType.KEY, this.handleKeyEvent) : b.unlisten(c, goog.events.EventType.FOCUS, this.handleFocus).unlisten(c, goog.events.EventType.BLUR, this.handleBlur).unlisten(this.getKeyHandler(), goog.events.KeyHandler.EventType.KEY, this.handleKeyEvent)
};
goog.ui.Container.prototype.exitDocument = function () {
    this.setHighlightedIndex(-1);
    this.openItem_ && this.openItem_.setOpen(!1);
    this.mouseButtonPressed_ = !1;
    goog.ui.Container.superClass_.exitDocument.call(this)
};
goog.ui.Container.prototype.disposeInternal = function () {
    goog.ui.Container.superClass_.disposeInternal.call(this);
    this.keyHandler_ && (this.keyHandler_.dispose(), this.keyHandler_ = null);
    this.renderer_ = this.openItem_ = this.childElementIdMap_ = this.keyEventTarget_ = null
};
goog.ui.Container.prototype.handleEnterItem = function (a) {
    return!0
};
goog.ui.Container.prototype.handleHighlightItem = function (a) {
    var b = this.indexOfChild(a.target);
    if (-1 < b && b != this.highlightedIndex_) {
        var c = this.getHighlighted();
        c && c.setHighlighted(!1);
        this.highlightedIndex_ = b;
        c = this.getHighlighted();
        this.isMouseButtonPressed() && c.setActive(!0);
        this.openFollowsHighlight_ && (this.openItem_ && c != this.openItem_) && (c.isSupportedState(goog.ui.Component.State.OPENED) ? c.setOpen(!0) : this.openItem_.setOpen(!1))
    }
    b = this.getElement();
    goog.asserts.assert(b, "The DOM element for the container cannot be null.");
    null != a.target.getElement() && goog.a11y.aria.setState(b, goog.a11y.aria.State.ACTIVEDESCENDANT, a.target.getElement().id)
};
goog.ui.Container.prototype.handleUnHighlightItem = function (a) {
    a.target == this.getHighlighted() && (this.highlightedIndex_ = -1);
    a = this.getElement();
    goog.asserts.assert(a, "The DOM element for the container cannot be null.");
    goog.a11y.aria.removeState(a, goog.a11y.aria.State.ACTIVEDESCENDANT)
};
goog.ui.Container.prototype.handleOpenItem = function (a) {
    (a = a.target) && (a != this.openItem_ && a.getParent() == this) && (this.openItem_ && this.openItem_.setOpen(!1), this.openItem_ = a)
};
goog.ui.Container.prototype.handleCloseItem = function (a) {
    a.target == this.openItem_ && (this.openItem_ = null)
};
goog.ui.Container.prototype.handleMouseDown = function (a) {
    this.enabled_ && this.setMouseButtonPressed(!0);
    var b = this.getKeyEventTarget();
    b && goog.dom.isFocusableTabIndex(b) ? b.focus() : a.preventDefault()
};
goog.ui.Container.prototype.handleDocumentMouseUp = function (a) {
    this.setMouseButtonPressed(!1)
};
goog.ui.Container.prototype.handleChildMouseEvents = function (a) {
    var b = this.getOwnerControl(a.target);
    if (b)switch (a.type) {
        case goog.events.EventType.MOUSEDOWN:
            b.handleMouseDown(a);
            break;
        case goog.events.EventType.MOUSEUP:
            b.handleMouseUp(a);
            break;
        case goog.events.EventType.MOUSEOVER:
            b.handleMouseOver(a);
            break;
        case goog.events.EventType.MOUSEOUT:
            b.handleMouseOut(a);
            break;
        case goog.events.EventType.CONTEXTMENU:
            b.handleContextMenu(a)
    }
};
goog.ui.Container.prototype.getOwnerControl = function (a) {
    if (this.childElementIdMap_)for (var b = this.getElement(); a && a !== b;) {
        var c = a.id;
        if (c in this.childElementIdMap_)return this.childElementIdMap_[c];
        a = a.parentNode
    }
    return null
};
goog.ui.Container.prototype.handleFocus = function (a) {
};
goog.ui.Container.prototype.handleBlur = function (a) {
    this.setHighlightedIndex(-1);
    this.setMouseButtonPressed(!1);
    this.openItem_ && this.openItem_.setOpen(!1)
};
goog.ui.Container.prototype.handleKeyEvent = function (a) {
    return this.isEnabled() && this.isVisible() && (0 != this.getChildCount() || this.keyEventTarget_) && this.handleKeyEventInternal(a) ? (a.preventDefault(), a.stopPropagation(), !0) : !1
};
goog.ui.Container.prototype.handleKeyEventInternal = function (a) {
    var b = this.getHighlighted();
    if (b && "function" == typeof b.handleKeyEvent && b.handleKeyEvent(a) || this.openItem_ && this.openItem_ != b && "function" == typeof this.openItem_.handleKeyEvent && this.openItem_.handleKeyEvent(a))return!0;
    if (a.shiftKey || a.ctrlKey || a.metaKey || a.altKey)return!1;
    switch (a.keyCode) {
        case goog.events.KeyCodes.ESC:
            if (this.isFocusable())this.getKeyEventTarget().blur(); else return!1;
            break;
        case goog.events.KeyCodes.HOME:
            this.highlightFirst();
            break;
        case goog.events.KeyCodes.END:
            this.highlightLast();
            break;
        case goog.events.KeyCodes.UP:
            if (this.orientation_ == goog.ui.Container.Orientation.VERTICAL)this.highlightPrevious(); else return!1;
            break;
        case goog.events.KeyCodes.LEFT:
            if (this.orientation_ == goog.ui.Container.Orientation.HORIZONTAL)this.isRightToLeft() ? this.highlightNext() : this.highlightPrevious(); else return!1;
            break;
        case goog.events.KeyCodes.DOWN:
            if (this.orientation_ == goog.ui.Container.Orientation.VERTICAL)this.highlightNext(); else return!1;
            break;
        case goog.events.KeyCodes.RIGHT:
            if (this.orientation_ == goog.ui.Container.Orientation.HORIZONTAL)this.isRightToLeft() ? this.highlightPrevious() : this.highlightNext(); else return!1;
            break;
        default:
            return!1
    }
    return!0
};
goog.ui.Container.prototype.registerChildId_ = function (a) {
    var b = a.getElement(), b = b.id || (b.id = a.getId());
    this.childElementIdMap_ || (this.childElementIdMap_ = {});
    this.childElementIdMap_[b] = a
};
goog.ui.Container.prototype.addChild = function (a, b) {
    goog.asserts.assertInstanceof(a, goog.ui.Control, "The child of a container must be a control");
    goog.ui.Container.superClass_.addChild.call(this, a, b)
};
goog.ui.Container.prototype.addChildAt = function (a, b, c) {
    a.setDispatchTransitionEvents(goog.ui.Component.State.HOVER, !0);
    a.setDispatchTransitionEvents(goog.ui.Component.State.OPENED, !0);
    !this.isFocusable() && this.isFocusableChildrenAllowed() || a.setSupportedState(goog.ui.Component.State.FOCUSED, !1);
    a.setHandleMouseEvents(!1);
    goog.ui.Container.superClass_.addChildAt.call(this, a, b, c);
    a.isInDocument() && this.isInDocument() && this.registerChildId_(a);
    b <= this.highlightedIndex_ && this.highlightedIndex_++
};
goog.ui.Container.prototype.removeChild = function (a, b) {
    if (a = goog.isString(a) ? this.getChild(a) : a) {
        var c = this.indexOfChild(a);
        -1 != c && (c == this.highlightedIndex_ ? a.setHighlighted(!1) : c < this.highlightedIndex_ && this.highlightedIndex_--);
        (c = a.getElement()) && (c.id && this.childElementIdMap_) && goog.object.remove(this.childElementIdMap_, c.id)
    }
    a = goog.ui.Container.superClass_.removeChild.call(this, a, b);
    a.setHandleMouseEvents(!0);
    return a
};
goog.ui.Container.prototype.getOrientation = function () {
    return this.orientation_
};
goog.ui.Container.prototype.setOrientation = function (a) {
    if (this.getElement())throw Error(goog.ui.Component.Error.ALREADY_RENDERED);
    this.orientation_ = a
};
goog.ui.Container.prototype.isVisible = function () {
    return this.visible_
};
goog.ui.Container.prototype.setVisible = function (a, b) {
    if (b || this.visible_ != a && this.dispatchEvent(a ? goog.ui.Component.EventType.SHOW : goog.ui.Component.EventType.HIDE)) {
        this.visible_ = a;
        var c = this.getElement();
        c && (goog.style.setElementShown(c, a), this.isFocusable() && this.renderer_.enableTabIndex(this.getKeyEventTarget(), this.enabled_ && this.visible_), b || this.dispatchEvent(this.visible_ ? goog.ui.Container.EventType.AFTER_SHOW : goog.ui.Container.EventType.AFTER_HIDE));
        return!0
    }
    return!1
};
goog.ui.Container.prototype.isEnabled = function () {
    return this.enabled_
};
goog.ui.Container.prototype.setEnabled = function (a) {
    this.enabled_ != a && this.dispatchEvent(a ? goog.ui.Component.EventType.ENABLE : goog.ui.Component.EventType.DISABLE) && (a ? (this.enabled_ = !0, this.forEachChild(function (a) {
        a.wasDisabled ? delete a.wasDisabled : a.setEnabled(!0)
    })) : (this.forEachChild(function (a) {
        a.isEnabled() ? a.setEnabled(!1) : a.wasDisabled = !0
    }), this.enabled_ = !1, this.setMouseButtonPressed(!1)), this.isFocusable() && this.renderer_.enableTabIndex(this.getKeyEventTarget(), a && this.visible_))
};
goog.ui.Container.prototype.isFocusable = function () {
    return this.focusable_
};
goog.ui.Container.prototype.setFocusable = function (a) {
    a != this.focusable_ && this.isInDocument() && this.enableFocusHandling_(a);
    this.focusable_ = a;
    this.enabled_ && this.visible_ && this.renderer_.enableTabIndex(this.getKeyEventTarget(), a)
};
goog.ui.Container.prototype.isFocusableChildrenAllowed = function () {
    return this.allowFocusableChildren_
};
goog.ui.Container.prototype.setFocusableChildrenAllowed = function (a) {
    this.allowFocusableChildren_ = a
};
goog.ui.Container.prototype.isOpenFollowsHighlight = function () {
    return this.openFollowsHighlight_
};
goog.ui.Container.prototype.setOpenFollowsHighlight = function (a) {
    this.openFollowsHighlight_ = a
};
goog.ui.Container.prototype.getHighlightedIndex = function () {
    return this.highlightedIndex_
};
goog.ui.Container.prototype.setHighlightedIndex = function (a) {
    (a = this.getChildAt(a)) ? a.setHighlighted(!0) : -1 < this.highlightedIndex_ && this.getHighlighted().setHighlighted(!1)
};
goog.ui.Container.prototype.setHighlighted = function (a) {
    this.setHighlightedIndex(this.indexOfChild(a))
};
goog.ui.Container.prototype.getHighlighted = function () {
    return this.getChildAt(this.highlightedIndex_)
};
goog.ui.Container.prototype.highlightFirst = function () {
    this.highlightHelper(function (a, b) {
        return(a + 1) % b
    }, this.getChildCount() - 1)
};
goog.ui.Container.prototype.highlightLast = function () {
    this.highlightHelper(function (a, b) {
        a--;
        return 0 > a ? b - 1 : a
    }, 0)
};
goog.ui.Container.prototype.highlightNext = function () {
    this.highlightHelper(function (a, b) {
        return(a + 1) % b
    }, this.highlightedIndex_)
};
goog.ui.Container.prototype.highlightPrevious = function () {
    this.highlightHelper(function (a, b) {
        a--;
        return 0 > a ? b - 1 : a
    }, this.highlightedIndex_)
};
goog.ui.Container.prototype.highlightHelper = function (a, b) {
    for (var c = 0 > b ? this.indexOfChild(this.openItem_) : b, d = this.getChildCount(), c = a.call(this, c, d), e = 0; e <= d;) {
        var f = this.getChildAt(c);
        if (f && this.canHighlightItem(f))return this.setHighlightedIndexFromKeyEvent(c), !0;
        e++;
        c = a.call(this, c, d)
    }
    return!1
};
goog.ui.Container.prototype.canHighlightItem = function (a) {
    return a.isVisible() && a.isEnabled() && a.isSupportedState(goog.ui.Component.State.HOVER)
};
goog.ui.Container.prototype.setHighlightedIndexFromKeyEvent = function (a) {
    this.setHighlightedIndex(a)
};
goog.ui.Container.prototype.getOpenItem = function () {
    return this.openItem_
};
goog.ui.Container.prototype.isMouseButtonPressed = function () {
    return this.mouseButtonPressed_
};
goog.ui.Container.prototype.setMouseButtonPressed = function (a) {
    this.mouseButtonPressed_ = a
};
goog.ui.TabBar = function (a, b, c) {
    this.setLocation(a || goog.ui.TabBar.Location.TOP);
    goog.ui.Container.call(this, this.getOrientation(), b || goog.ui.TabBarRenderer.getInstance(), c);
    this.listenToTabEvents_()
};
goog.inherits(goog.ui.TabBar, goog.ui.Container);
goog.ui.TabBar.Location = {TOP: "top", BOTTOM: "bottom", START: "start", END: "end"};
goog.ui.TabBar.prototype.autoSelectTabs_ = !0;
goog.ui.TabBar.prototype.selectedTab_ = null;
goog.ui.TabBar.prototype.enterDocument = function () {
    goog.ui.TabBar.superClass_.enterDocument.call(this);
    this.listenToTabEvents_()
};
goog.ui.TabBar.prototype.disposeInternal = function () {
    goog.ui.TabBar.superClass_.disposeInternal.call(this);
    this.selectedTab_ = null
};
goog.ui.TabBar.prototype.removeChild = function (a, b) {
    this.deselectIfSelected(a);
    return goog.ui.TabBar.superClass_.removeChild.call(this, a, b)
};
goog.ui.TabBar.prototype.getLocation = function () {
    return this.location_
};
goog.ui.TabBar.prototype.setLocation = function (a) {
    this.setOrientation(goog.ui.TabBar.getOrientationFromLocation(a));
    this.location_ = a
};
goog.ui.TabBar.prototype.isAutoSelectTabs = function () {
    return this.autoSelectTabs_
};
goog.ui.TabBar.prototype.setAutoSelectTabs = function (a) {
    this.autoSelectTabs_ = a
};
goog.ui.TabBar.prototype.setHighlightedIndexFromKeyEvent = function (a) {
    goog.ui.TabBar.superClass_.setHighlightedIndexFromKeyEvent.call(this, a);
    this.autoSelectTabs_ && this.setSelectedTabIndex(a)
};
goog.ui.TabBar.prototype.getSelectedTab = function () {
    return this.selectedTab_
};
goog.ui.TabBar.prototype.setSelectedTab = function (a) {
    a ? a.setSelected(!0) : this.getSelectedTab() && this.getSelectedTab().setSelected(!1)
};
goog.ui.TabBar.prototype.getSelectedTabIndex = function () {
    return this.indexOfChild(this.getSelectedTab())
};
goog.ui.TabBar.prototype.setSelectedTabIndex = function (a) {
    this.setSelectedTab(this.getChildAt(a))
};
goog.ui.TabBar.prototype.deselectIfSelected = function (a) {
    if (a && a == this.getSelectedTab()) {
        for (var b = this.indexOfChild(a), c = b - 1; a = this.getChildAt(c); c--)if (this.isSelectableTab(a)) {
            this.setSelectedTab(a);
            return
        }
        for (b += 1; a = this.getChildAt(b); b++)if (this.isSelectableTab(a)) {
            this.setSelectedTab(a);
            return
        }
        this.setSelectedTab(null)
    }
};
goog.ui.TabBar.prototype.isSelectableTab = function (a) {
    return a.isVisible() && a.isEnabled()
};
goog.ui.TabBar.prototype.handleTabSelect = function (a) {
    this.selectedTab_ && this.selectedTab_ != a.target && this.selectedTab_.setSelected(!1);
    this.selectedTab_ = a.target
};
goog.ui.TabBar.prototype.handleTabUnselect = function (a) {
    a.target == this.selectedTab_ && (this.selectedTab_ = null)
};
goog.ui.TabBar.prototype.handleTabDisable = function (a) {
    this.deselectIfSelected(a.target)
};
goog.ui.TabBar.prototype.handleTabHide = function (a) {
    this.deselectIfSelected(a.target)
};
goog.ui.TabBar.prototype.handleFocus = function (a) {
    this.getHighlighted() || this.setHighlighted(this.getSelectedTab() || this.getChildAt(0))
};
goog.ui.TabBar.prototype.listenToTabEvents_ = function () {
    this.getHandler().listen(this, goog.ui.Component.EventType.SELECT, this.handleTabSelect).listen(this, goog.ui.Component.EventType.UNSELECT, this.handleTabUnselect).listen(this, goog.ui.Component.EventType.DISABLE, this.handleTabDisable).listen(this, goog.ui.Component.EventType.HIDE, this.handleTabHide)
};
goog.ui.TabBar.getOrientationFromLocation = function (a) {
    return a == goog.ui.TabBar.Location.START || a == goog.ui.TabBar.Location.END ? goog.ui.Container.Orientation.VERTICAL : goog.ui.Container.Orientation.HORIZONTAL
};
goog.ui.registry.setDecoratorByClassName(goog.ui.TabBarRenderer.CSS_CLASS, function () {
    return new goog.ui.TabBar
});
goog.ui.editor.TabPane = function (a, b) {
    goog.ui.Component.call(this, a);
    this.eventHandler_ = new goog.events.EventHandler(this);
    this.registerDisposable(this.eventHandler_);
    this.tabBar_ = new goog.ui.TabBar(goog.ui.TabBar.Location.START, void 0, this.dom_);
    this.tabBar_.setFocusable(!1);
    this.tabContent_ = this.dom_.createDom(goog.dom.TagName.DIV, {className: "goog-tab-content"});
    this.visibleContent_ = this.selectedRadio_ = null;
    if (b) {
        var c = new goog.ui.Control(b, void 0, this.dom_);
        c.addClassName("tr-tabpane-caption");
        c.setEnabled(!1);
        this.tabBar_.addChild(c, !0)
    }
};
goog.inherits(goog.ui.editor.TabPane, goog.ui.Component);
goog.ui.editor.TabPane.prototype.getCurrentTabId = function () {
    return this.tabBar_.getSelectedTab().getId()
};
goog.ui.editor.TabPane.prototype.setSelectedTabId = function (a) {
    this.tabBar_.setSelectedTab(this.tabBar_.getChild(a))
};
goog.ui.editor.TabPane.prototype.addTab = function (a, b, c, d, e) {
    d = this.dom_.createDom(goog.dom.TagName.INPUT, {name: d, type: "radio"});
    b = new goog.ui.Tab([d, this.dom_.createTextNode(b)], void 0, this.dom_);
    b.setId(a);
    b.setTooltip(c);
    this.tabBar_.addChild(b, !0);
    this.eventHandler_.listen(d, [goog.events.EventType.SELECT, goog.events.EventType.CLICK], goog.bind(this.tabBar_.setSelectedTab, this.tabBar_, b));
    e.id = a + "-tab";
    this.tabContent_.appendChild(e);
    goog.style.setElementShown(e, !1)
};
goog.ui.editor.TabPane.prototype.enterDocument = function () {
    goog.ui.editor.TabPane.superClass_.enterDocument.call(this);
    var a = this.getElement();
    goog.dom.classes.add(a, "tr-tabpane");
    this.addChild(this.tabBar_, !0);
    this.eventHandler_.listen(this.tabBar_, goog.ui.Component.EventType.SELECT, this.handleTabSelect_);
    a.appendChild(this.tabContent_);
    a.appendChild(this.dom_.createDom(goog.dom.TagName.DIV, {className: "goog-tab-bar-clear"}))
};
goog.ui.editor.TabPane.prototype.handleTabSelect_ = function (a) {
    a = a.target;
    this.visibleContent_ && goog.style.setElementShown(this.visibleContent_, !1);
    this.visibleContent_ = this.dom_.getElement(a.getId() + "-tab");
    goog.style.setElementShown(this.visibleContent_, !0);
    this.selectedRadio_ && (this.selectedRadio_.checked = !1);
    this.selectedRadio_ = a.getElement().getElementsByTagName(goog.dom.TagName.INPUT)[0];
    this.selectedRadio_.checked = !0
};
goog.ui.editor.messages = {};
goog.ui.editor.messages.MSG_LINK_CAPTION = goog.getMsg("Link");
goog.ui.editor.messages.MSG_EDIT_LINK = goog.getMsg("Edit Link");
goog.ui.editor.messages.MSG_TEXT_TO_DISPLAY = goog.getMsg("Text to display:");
goog.ui.editor.messages.MSG_LINK_TO = goog.getMsg("Link to:");
goog.ui.editor.messages.MSG_ON_THE_WEB = goog.getMsg("Web address");
goog.ui.editor.messages.MSG_ON_THE_WEB_TIP = goog.getMsg("Link to a page or file somewhere else on the web");
goog.ui.editor.messages.MSG_TEST_THIS_LINK = goog.getMsg("Test this link");
goog.ui.editor.messages.MSG_TR_LINK_EXPLANATION = goog.getMsg("{$startBold}Not sure what to put in the box?{$endBold} First, find the page on the web that you want to link to. (A {$searchEngineLink}search engine{$endLink} might be useful.) Then, copy the web address from the box in your browser's address bar, and paste it into the box above.", {startBold: "<b>", endBold: "</b>", searchEngineLink: "<a href='http://www.google.com/' target='_new'>", endLink: "</a>"});
goog.ui.editor.messages.MSG_WHAT_URL = goog.getMsg("To what URL should this link go?");
goog.ui.editor.messages.MSG_EMAIL_ADDRESS = goog.getMsg("Email address");
goog.ui.editor.messages.MSG_EMAIL_ADDRESS_TIP = goog.getMsg("Link to an email address");
goog.ui.editor.messages.MSG_INVALID_EMAIL = goog.getMsg("Invalid email address");
goog.ui.editor.messages.MSG_WHAT_EMAIL = goog.getMsg("To what email address should this link?");
goog.ui.editor.messages.MSG_EMAIL_EXPLANATION = goog.getMsg("{$preb}Be careful.{$postb} Remember that any time you include an email address on a web page, nasty spammers can find it too.", {preb: "<b>", postb: "</b>"});
goog.ui.editor.messages.MSG_OPEN_IN_NEW_WINDOW = goog.getMsg("Open this link in a new window");
goog.ui.editor.messages.MSG_IMAGE_CAPTION = goog.getMsg("Image");
goog.editor.focus = {};
goog.editor.focus.focusInputField = function (a) {
    a.focus();
    goog.dom.selection.setCursorPosition(a, a.value.length)
};
goog.window = {};
goog.window.DEFAULT_POPUP_HEIGHT = 500;
goog.window.DEFAULT_POPUP_WIDTH = 690;
goog.window.DEFAULT_POPUP_TARGET = "google_popup";
goog.window.open = function (a, b, c) {
    b || (b = {});
    var d = c || window;
    c = "undefined" != typeof a.href ? a.href : String(a);
    a = b.target || a.target;
    var e = [], f;
    for (f in b)switch (f) {
        case "width":
        case "height":
        case "top":
        case "left":
            e.push(f + "=" + b[f]);
            break;
        case "target":
        case "noreferrer":
            break;
        default:
            e.push(f + "=" + (b[f] ? 1 : 0))
    }
    f = e.join(",");
    if (b.noreferrer) {
        if (b = d.open("", a, f))goog.userAgent.IE && -1 != c.indexOf(";") && (c = "'" + c.replace(/'/g, "%27") + "'"), b.opener = null, c = goog.string.htmlEscape(c), b.document.write('<META HTTP-EQUIV="refresh" content="0; url=' +
            c + '">'), b.document.close()
    } else b = d.open(c, a, f);
    return b
};
goog.window.openBlank = function (a, b, c) {
    a = a ? goog.string.htmlEscape(a) : "";
    return goog.window.open('javascript:"' + encodeURI(a) + '"', b, c)
};
goog.window.popup = function (a, b) {
    b || (b = {});
    b.target = b.target || a.target || goog.window.DEFAULT_POPUP_TARGET;
    b.width = b.width || goog.window.DEFAULT_POPUP_WIDTH;
    b.height = b.height || goog.window.DEFAULT_POPUP_HEIGHT;
    var c = goog.window.open(a, b);
    if (!c)return!0;
    c.focus();
    return!1
};
goog.ui.editor.LinkDialog = function (a, b) {
    goog.ui.editor.AbstractDialog.call(this, a);
    this.targetLink_ = b;
    this.eventHandler_ = new goog.events.EventHandler(this)
};
goog.inherits(goog.ui.editor.LinkDialog, goog.ui.editor.AbstractDialog);
goog.ui.editor.LinkDialog.EventType = {BEFORE_TEST_LINK: "beforetestlink"};
goog.ui.editor.LinkDialog.OkEvent = function (a, b, c, d) {
    goog.events.Event.call(this, goog.ui.editor.AbstractDialog.EventType.OK);
    this.linkText = a;
    this.linkUrl = b;
    this.openInNewWindow = c;
    this.noFollow = d
};
goog.inherits(goog.ui.editor.LinkDialog.OkEvent, goog.events.Event);
goog.ui.editor.LinkDialog.BeforeTestLinkEvent = function (a) {
    goog.events.Event.call(this, goog.ui.editor.LinkDialog.EventType.BEFORE_TEST_LINK);
    this.url = a
};
goog.inherits(goog.ui.editor.LinkDialog.BeforeTestLinkEvent, goog.events.Event);
goog.ui.editor.LinkDialog.prototype.showOpenLinkInNewWindow_ = !1;
goog.ui.editor.LinkDialog.prototype.isOpenLinkInNewWindowChecked_ = !1;
goog.ui.editor.LinkDialog.prototype.showRelNoFollow_ = !1;
goog.ui.editor.LinkDialog.prototype.setEmailWarning = function (a) {
    this.emailWarning_ = a
};
goog.ui.editor.LinkDialog.prototype.showOpenLinkInNewWindow = function (a) {
    this.showOpenLinkInNewWindow_ = !0;
    this.isOpenLinkInNewWindowChecked_ = a
};
goog.ui.editor.LinkDialog.prototype.showRelNoFollow = function () {
    this.showRelNoFollow_ = !0
};
goog.ui.editor.LinkDialog.prototype.show = function () {
    goog.ui.editor.LinkDialog.superClass_.show.call(this);
    this.selectAppropriateTab_(this.textToDisplayInput_.value, this.getTargetUrl_());
    this.syncOkButton_();
    this.showOpenLinkInNewWindow_ && (this.targetLink_.isNew() || (this.isOpenLinkInNewWindowChecked_ = "_blank" == this.targetLink_.getAnchor().target), this.openInNewWindowCheckbox_.checked = this.isOpenLinkInNewWindowChecked_);
    this.showRelNoFollow_ && (this.relNoFollowCheckbox_.checked = goog.ui.editor.LinkDialog.hasNoFollow(this.targetLink_.getAnchor().rel))
};
goog.ui.editor.LinkDialog.prototype.hide = function () {
    this.disableAutogenFlag_(!1);
    goog.ui.editor.LinkDialog.superClass_.hide.call(this)
};
goog.ui.editor.LinkDialog.prototype.setTextToDisplayVisible = function (a) {
    this.textToDisplayDiv_ && goog.style.setStyle(this.textToDisplayDiv_, "display", a ? "block" : "none")
};
goog.ui.editor.LinkDialog.prototype.setStopReferrerLeaks = function (a) {
    this.stopReferrerLeaks_ = a
};
goog.ui.editor.LinkDialog.prototype.setAutogenFeatureEnabled = function (a) {
    this.autogenFeatureEnabled_ = a
};
goog.ui.editor.LinkDialog.hasNoFollow = function (a) {
    return goog.ui.editor.LinkDialog.NO_FOLLOW_REGEX_.test(a)
};
goog.ui.editor.LinkDialog.removeNoFollow = function (a) {
    return a.replace(goog.ui.editor.LinkDialog.NO_FOLLOW_REGEX_, "")
};
goog.ui.editor.LinkDialog.prototype.createDialogControl = function () {
    var a = new goog.ui.editor.AbstractDialog.Builder(this);
    a.setTitle(goog.ui.editor.messages.MSG_EDIT_LINK).setContent(this.createDialogContent_());
    return a.build()
};
goog.ui.editor.LinkDialog.prototype.createOkEvent = function () {
    return this.tabPane_.getCurrentTabId() == goog.ui.editor.LinkDialog.Id_.EMAIL_ADDRESS_TAB ? this.createOkEventFromEmailTab_() : this.createOkEventFromWebTab_()
};
goog.ui.editor.LinkDialog.prototype.disposeInternal = function () {
    this.eventHandler_.dispose();
    this.eventHandler_ = null;
    this.tabPane_.dispose();
    this.tabPane_ = null;
    this.urlInputHandler_.dispose();
    this.urlInputHandler_ = null;
    this.emailInputHandler_.dispose();
    this.emailInputHandler_ = null;
    goog.ui.editor.LinkDialog.superClass_.disposeInternal.call(this)
};
goog.ui.editor.LinkDialog.NO_FOLLOW_REGEX_ = /\bnofollow\b/i;
goog.ui.editor.LinkDialog.prototype.autogenFeatureEnabled_ = !0;
goog.ui.editor.LinkDialog.prototype.stopReferrerLeaks_ = !1;
goog.ui.editor.LinkDialog.prototype.createDialogContent_ = function () {
    this.textToDisplayDiv_ = this.buildTextToDisplayDiv_();
    var a = this.dom.createDom(goog.dom.TagName.DIV, null, this.textToDisplayDiv_);
    this.tabPane_ = new goog.ui.editor.TabPane(this.dom, goog.ui.editor.messages.MSG_LINK_TO);
    this.tabPane_.addTab(goog.ui.editor.LinkDialog.Id_.ON_WEB_TAB, goog.ui.editor.messages.MSG_ON_THE_WEB, goog.ui.editor.messages.MSG_ON_THE_WEB_TIP, goog.ui.editor.LinkDialog.BUTTON_GROUP_, this.buildTabOnTheWeb_());
    this.tabPane_.addTab(goog.ui.editor.LinkDialog.Id_.EMAIL_ADDRESS_TAB,
        goog.ui.editor.messages.MSG_EMAIL_ADDRESS, goog.ui.editor.messages.MSG_EMAIL_ADDRESS_TIP, goog.ui.editor.LinkDialog.BUTTON_GROUP_, this.buildTabEmailAddress_());
    this.tabPane_.render(a);
    this.eventHandler_.listen(this.tabPane_, goog.ui.Component.EventType.SELECT, this.onChangeTab_);
    this.showOpenLinkInNewWindow_ && a.appendChild(this.buildOpenInNewWindowDiv_());
    this.showRelNoFollow_ && a.appendChild(this.buildRelNoFollowDiv_());
    return a
};
goog.ui.editor.LinkDialog.prototype.buildTextToDisplayDiv_ = function () {
    var a = this.dom.createTable(1, 2);
    a.cellSpacing = "0";
    a.cellPadding = "0";
    a.style.fontSize = "10pt";
    var b = this.dom.createDom(goog.dom.TagName.DIV);
    a.rows[0].cells[0].innerHTML = '<span style="position: relative; bottom: 2px; padding-right: 1px; white-space: nowrap;">' + goog.ui.editor.messages.MSG_TEXT_TO_DISPLAY + "&nbsp;</span>";
    var c = this.textToDisplayInput_ = this.dom.createDom(goog.dom.TagName.INPUT, {id: goog.ui.editor.LinkDialog.Id_.TEXT_TO_DISPLAY});
    goog.style.setStyle(c, "width", "98%");
    goog.style.setStyle(a.rows[0].cells[1], "width", "100%");
    goog.dom.appendChild(a.rows[0].cells[1], c);
    c.value = this.targetLink_.getCurrentText();
    this.eventHandler_.listen(c, goog.events.EventType.KEYUP, goog.bind(this.onTextToDisplayEdit_, this));
    goog.dom.appendChild(b, a);
    return b
};
goog.ui.editor.LinkDialog.prototype.buildOpenInNewWindowDiv_ = function () {
    this.openInNewWindowCheckbox_ = this.dom.createDom(goog.dom.TagName.INPUT, {type: "checkbox"});
    return this.dom.createDom(goog.dom.TagName.DIV, null, this.dom.createDom(goog.dom.TagName.LABEL, null, this.openInNewWindowCheckbox_, goog.ui.editor.messages.MSG_OPEN_IN_NEW_WINDOW))
};
goog.ui.editor.LinkDialog.prototype.buildRelNoFollowDiv_ = function () {
    var a = goog.getMsg("Add '{$relNoFollow}' attribute ({$linkStart}Learn more{$linkEnd})", {relNoFollow: "rel=nofollow", linkStart: '<a href="http://support.google.com/webmasters/bin/answer.py?hl=en&answer=96569" target="_blank">', linkEnd: "</a>"});
    this.relNoFollowCheckbox_ = this.dom.createDom(goog.dom.TagName.INPUT, {type: "checkbox"});
    return this.dom.createDom(goog.dom.TagName.DIV, null, this.dom.createDom(goog.dom.TagName.LABEL, null, this.relNoFollowCheckbox_,
        goog.dom.htmlToDocumentFragment(a)))
};
goog.ui.editor.LinkDialog.prototype.buildTabOnTheWeb_ = function () {
    var a = this.dom.createElement(goog.dom.TagName.DIV), b = this.dom.createDom(goog.dom.TagName.DIV, {innerHTML: "<b>" + goog.ui.editor.messages.MSG_WHAT_URL + "</b>"}), c = this.dom.createDom(goog.dom.TagName.INPUT, {id: goog.ui.editor.LinkDialog.Id_.ON_WEB_INPUT, className: goog.ui.editor.LinkDialog.TARGET_INPUT_CLASSNAME_});
    goog.userAgent.IE || (c.type = "url");
    goog.editor.BrowserFeature.NEEDS_99_WIDTH_IN_STANDARDS_MODE && goog.editor.node.isStandardsMode(c) &&
    (c.style.width = "99%");
    var d = this.dom.createDom(goog.dom.TagName.DIV, null, c);
    this.urlInputHandler_ = new goog.events.InputHandler(c);
    this.eventHandler_.listen(this.urlInputHandler_, goog.events.InputHandler.EventType.INPUT, this.onUrlOrEmailInputChange_);
    c = new goog.ui.Button(goog.ui.editor.messages.MSG_TEST_THIS_LINK, goog.ui.LinkButtonRenderer.getInstance(), this.dom);
    c.render(d);
    c.getElement().style.marginTop = "1em";
    this.eventHandler_.listen(c, goog.ui.Component.EventType.ACTION, this.onWebTestLink_);
    c = this.dom.createDom(goog.dom.TagName.DIV,
        {className: goog.ui.editor.LinkDialog.EXPLANATION_TEXT_CLASSNAME_, innerHTML: goog.ui.editor.messages.MSG_TR_LINK_EXPLANATION});
    a.appendChild(b);
    a.appendChild(d);
    a.appendChild(c);
    return a
};
goog.ui.editor.LinkDialog.prototype.buildTabEmailAddress_ = function () {
    var a = this.dom.createDom(goog.dom.TagName.DIV), b = this.dom.createDom(goog.dom.TagName.DIV, {innerHTML: "<b>" + goog.ui.editor.messages.MSG_WHAT_EMAIL + "</b>"});
    goog.dom.appendChild(a, b);
    b = this.dom.createDom(goog.dom.TagName.INPUT, {id: goog.ui.editor.LinkDialog.Id_.EMAIL_ADDRESS_INPUT, className: goog.ui.editor.LinkDialog.TARGET_INPUT_CLASSNAME_});
    goog.editor.BrowserFeature.NEEDS_99_WIDTH_IN_STANDARDS_MODE && goog.editor.node.isStandardsMode(b) &&
    (b.style.width = "99%");
    goog.dom.appendChild(a, b);
    this.emailInputHandler_ = new goog.events.InputHandler(b);
    this.eventHandler_.listen(this.emailInputHandler_, goog.events.InputHandler.EventType.INPUT, this.onUrlOrEmailInputChange_);
    goog.dom.appendChild(a, this.dom.createDom(goog.dom.TagName.DIV, {id: goog.ui.editor.LinkDialog.Id_.EMAIL_WARNING, className: goog.ui.editor.LinkDialog.EMAIL_WARNING_CLASSNAME_, style: "visibility:hidden"}, goog.ui.editor.messages.MSG_INVALID_EMAIL));
    this.emailWarning_ && (b = this.dom.createDom(goog.dom.TagName.DIV,
        {className: goog.ui.editor.LinkDialog.EXPLANATION_TEXT_CLASSNAME_, innerHTML: this.emailWarning_}), goog.dom.appendChild(a, b));
    return a
};
goog.ui.editor.LinkDialog.prototype.getTargetUrl_ = function () {
    return this.targetLink_.getAnchor().getAttribute("href") || ""
};
goog.ui.editor.LinkDialog.prototype.selectAppropriateTab_ = function (a, b) {
    this.isNewLink_() ? this.guessUrlAndSelectTab_(a) : (goog.editor.Link.isMailto(b) ? (this.tabPane_.setSelectedTabId(goog.ui.editor.LinkDialog.Id_.EMAIL_ADDRESS_TAB), this.dom.getElement(goog.ui.editor.LinkDialog.Id_.EMAIL_ADDRESS_INPUT).value = b.substring(b.indexOf(":") + 1)) : (this.tabPane_.setSelectedTabId(goog.ui.editor.LinkDialog.Id_.ON_WEB_TAB), this.dom.getElement(goog.ui.editor.LinkDialog.Id_.ON_WEB_INPUT).value = this.isNewLink_() ?
        "http://" : b), this.setAutogenFlagFromCurInput_())
};
goog.ui.editor.LinkDialog.prototype.guessUrlAndSelectTab_ = function (a) {
    goog.editor.Link.isLikelyEmailAddress(a) ? (this.tabPane_.setSelectedTabId(goog.ui.editor.LinkDialog.Id_.EMAIL_ADDRESS_TAB), this.dom.getElement(goog.ui.editor.LinkDialog.Id_.EMAIL_ADDRESS_INPUT).value = a, this.setAutogenFlag_(!0), this.disableAutogenFlag_(!0)) : goog.editor.Link.isLikelyUrl(a) ? (this.tabPane_.setSelectedTabId(goog.ui.editor.LinkDialog.Id_.ON_WEB_TAB), this.dom.getElement(goog.ui.editor.LinkDialog.Id_.ON_WEB_INPUT).value = a,
        this.setAutogenFlag_(!0), this.disableAutogenFlag_(!0)) : (this.targetLink_.getCurrentText() || this.setAutogenFlag_(!0), this.tabPane_.setSelectedTabId(goog.ui.editor.LinkDialog.Id_.ON_WEB_TAB))
};
goog.ui.editor.LinkDialog.prototype.syncOkButton_ = function () {
    var a;
    if (this.tabPane_.getCurrentTabId() == goog.ui.editor.LinkDialog.Id_.EMAIL_ADDRESS_TAB)a = this.dom.getElement(goog.ui.editor.LinkDialog.Id_.EMAIL_ADDRESS_INPUT).value, this.toggleInvalidEmailWarning_("" != a && !goog.editor.Link.isLikelyEmailAddress(a)); else if (this.tabPane_.getCurrentTabId() == goog.ui.editor.LinkDialog.Id_.ON_WEB_TAB)a = this.dom.getElement(goog.ui.editor.LinkDialog.Id_.ON_WEB_INPUT).value; else return;
    this.getOkButtonElement().disabled =
        goog.string.isEmpty(a)
};
goog.ui.editor.LinkDialog.prototype.toggleInvalidEmailWarning_ = function (a) {
    this.dom.getElement(goog.ui.editor.LinkDialog.Id_.EMAIL_WARNING).style.visibility = a ? "visible" : "hidden"
};
goog.ui.editor.LinkDialog.prototype.onTextToDisplayEdit_ = function () {
    "" == this.textToDisplayInput_.value ? this.setAutogenFlag_(!0) : this.setAutogenFlagFromCurInput_()
};
goog.ui.editor.LinkDialog.prototype.createOkEventFromWebTab_ = function () {
    var a = this.dom.getElement(goog.ui.editor.LinkDialog.Id_.ON_WEB_INPUT).value;
    if (goog.editor.Link.isLikelyEmailAddress(a))return this.createOkEventFromEmailTab_(goog.ui.editor.LinkDialog.Id_.ON_WEB_INPUT);
    0 > a.search(/:/) && (a = "http://" + goog.string.trimLeft(a));
    return this.createOkEventFromUrl_(a)
};
goog.ui.editor.LinkDialog.prototype.createOkEventFromEmailTab_ = function (a) {
    a = this.dom.getElement(a || goog.ui.editor.LinkDialog.Id_.EMAIL_ADDRESS_INPUT).value;
    return this.createOkEventFromUrl_("mailto:" + a)
};
goog.ui.editor.LinkDialog.prototype.onWebTestLink_ = function () {
    var a = this.dom.getElement(goog.ui.editor.LinkDialog.Id_.ON_WEB_INPUT).value;
    0 > a.search(/:/) && (a = "http://" + goog.string.trimLeft(a));
    if (this.dispatchEvent(new goog.ui.editor.LinkDialog.BeforeTestLinkEvent(a))) {
        var b = this.dom.getWindow(), c = goog.dom.getViewportSize(b), c = {target: "_blank", width: Math.max(c.width - 50, 50), height: Math.max(c.height - 50, 50), toolbar: !0, scrollbars: !0, location: !0, statusbar: !1, menubar: !0, resizable: !0, noreferrer: this.stopReferrerLeaks_};
        goog.window.open(a, c, b)
    }
};
goog.ui.editor.LinkDialog.prototype.onUrlOrEmailInputChange_ = function () {
    this.autogenerateTextToDisplay_ ? this.setTextToDisplayFromAuto_() : "" == this.textToDisplayInput_.value && this.setAutogenFlagFromCurInput_();
    this.syncOkButton_()
};
goog.ui.editor.LinkDialog.prototype.onChangeTab_ = function (a) {
    a = this.dom.getElement(a.target.getId() + goog.ui.editor.LinkDialog.Id_.TAB_INPUT_SUFFIX);
    goog.editor.focus.focusInputField(a);
    a.style.width = "";
    a.style.width = a.offsetWidth + "px";
    this.syncOkButton_();
    this.setTextToDisplayFromAuto_()
};
goog.ui.editor.LinkDialog.prototype.setTextToDisplayFromAuto_ = function () {
    if (this.autogenFeatureEnabled_ && this.autogenerateTextToDisplay_) {
        var a = this.tabPane_.getCurrentTabId() + goog.ui.editor.LinkDialog.Id_.TAB_INPUT_SUFFIX;
        this.textToDisplayInput_.value = this.dom.getElement(a).value
    }
};
goog.ui.editor.LinkDialog.prototype.setAutogenFlag_ = function (a) {
    this.autogenerateTextToDisplay_ = a
};
goog.ui.editor.LinkDialog.prototype.disableAutogenFlag_ = function (a) {
    this.setAutogenFlag_(!a);
    this.disableAutogen_ = a
};
goog.ui.editor.LinkDialog.prototype.createOkEventFromUrl_ = function (a) {
    this.setTextToDisplayFromAuto_();
    this.showOpenLinkInNewWindow_ && (this.isOpenLinkInNewWindowChecked_ = this.openInNewWindowCheckbox_.checked);
    return new goog.ui.editor.LinkDialog.OkEvent(this.textToDisplayInput_.value, a, this.showOpenLinkInNewWindow_ && this.isOpenLinkInNewWindowChecked_, this.showRelNoFollow_ && this.relNoFollowCheckbox_.checked)
};
goog.ui.editor.LinkDialog.prototype.setAutogenFlagFromCurInput_ = function () {
    var a = !1;
    this.disableAutogen_ || (a = this.dom.getElement(this.tabPane_.getCurrentTabId() + goog.ui.editor.LinkDialog.Id_.TAB_INPUT_SUFFIX).value == this.textToDisplayInput_.value);
    this.setAutogenFlag_(a)
};
goog.ui.editor.LinkDialog.prototype.isNewLink_ = function () {
    return this.targetLink_.isNew()
};
goog.ui.editor.LinkDialog.Id_ = {TEXT_TO_DISPLAY: "linkdialog-text", ON_WEB_TAB: "linkdialog-onweb", ON_WEB_INPUT: "linkdialog-onweb-tab-input", EMAIL_ADDRESS_TAB: "linkdialog-email", EMAIL_ADDRESS_INPUT: "linkdialog-email-tab-input", EMAIL_WARNING: "linkdialog-email-warning", TAB_INPUT_SUFFIX: "-tab-input"};
goog.ui.editor.LinkDialog.BUTTON_GROUP_ = "linkdialog-buttons";
goog.ui.editor.LinkDialog.TARGET_INPUT_CLASSNAME_ = "tr-link-dialog-target-input";
goog.ui.editor.LinkDialog.EMAIL_WARNING_CLASSNAME_ = "tr-link-dialog-email-warning";
goog.ui.editor.LinkDialog.EXPLANATION_TEXT_CLASSNAME_ = "tr-link-dialog-explanation-text";
goog.editor.icontent = {};
goog.editor.icontent.FieldFormatInfo = function (a, b, c, d, e) {
    this.fieldId_ = a;
    this.standards_ = b;
    this.blended_ = c;
    this.fixedHeight_ = d;
    this.extraStyles_ = e || {}
};
goog.editor.icontent.FieldStyleInfo = function (a, b) {
    this.wrapper_ = a;
    this.css_ = b
};
goog.editor.icontent.useStandardsModeIframes_ = !1;
goog.editor.icontent.forceStandardsModeIframes = function () {
    goog.editor.icontent.useStandardsModeIframes_ = !0
};
goog.editor.icontent.getInitialIframeContent_ = function (a, b, c) {
    var d = [];
    (a.blended_ && a.standards_ || goog.editor.icontent.useStandardsModeIframes_) && d.push("<!DOCTYPE HTML>");
    d.push('<html style="background:none transparent;min-width:0;');
    a.blended_ && d.push("height:", a.fixedHeight_ ? "100%" : "auto");
    d.push('">');
    d.push("<head><style>");
    c && c.css_ && d.push(c.css_);
    goog.userAgent.GECKO && a.standards_ && d.push(" img {-moz-force-broken-image-icon: 1;}");
    d.push("</style></head>");
    d.push('<body g_editable="true" hidefocus="true" ');
    goog.editor.BrowserFeature.HAS_CONTENT_EDITABLE && d.push("contentEditable ");
    d.push('class="editable ');
    d.push('" id="', a.fieldId_, '" style="min-width:0;');
    goog.userAgent.GECKO && a.blended_ && (d.push(";width:100%;border:0;margin:0;background:none transparent;", ";height:", a.standards_ ? "100%" : "auto"), a.fixedHeight_ ? d.push(";overflow:auto") : d.push(";overflow-y:hidden;overflow-x:auto"));
    goog.userAgent.OPERA && d.push(";outline:hidden");
    for (var e in a.extraStyles_)d.push(";" + e + ":" + a.extraStyles_[e]);
    d.push('">',
        b, "</body></html>");
    return d.join("")
};
goog.editor.icontent.writeNormalInitialBlendedIframe = function (a, b, c, d) {
    if (a.blended_) {
        var e = goog.style.getPaddingBox(c.wrapper_);
        (e.top || e.left || e.right || e.bottom) && goog.style.setStyle(d, "margin", -e.top + "px " + -e.right + "px " + -e.bottom + "px " + -e.left + "px")
    }
    goog.editor.icontent.writeNormalInitialIframe(a, b, c, d)
};
goog.editor.icontent.writeNormalInitialIframe = function (a, b, c, d) {
    a = goog.editor.icontent.getInitialIframeContent_(a, b, c);
    d = goog.dom.getFrameContentDocument(d);
    d.open();
    d.write(a);
    d.close()
};
goog.editor.icontent.writeHttpsInitialIframe = function (a, b, c) {
    b = b.body;
    goog.editor.BrowserFeature.HAS_CONTENT_EDITABLE && (b.contentEditable = !0);
    b.className = "editable";
    b.setAttribute("g_editable", !0);
    b.hideFocus = !0;
    b.id = a.fieldId_;
    goog.style.setStyle(b, a.extraStyles_);
    b.innerHTML = c
};
goog.editor.Field = function (a, b) {
    goog.events.EventTarget.call(this);
    this.hashCode_ = this.id = a;
    this.editableDomHelper = null;
    this.plugins_ = {};
    this.indexedPlugins_ = {};
    for (var c in goog.editor.Plugin.OPCODE)this.indexedPlugins_[c] = [];
    this.cssStyles = "";
    this.stoppedEvents_ = {};
    this.stopEvent(goog.editor.Field.EventType.CHANGE);
    this.stopEvent(goog.editor.Field.EventType.DELAYEDCHANGE);
    this.isEverModified_ = this.isModified_ = !1;
    this.delayedChangeTimer_ = new goog.async.Delay(this.dispatchDelayedChange_, goog.editor.Field.DELAYED_CHANGE_FREQUENCY,
        this);
    this.debouncedEvents_ = {};
    for (var d in goog.editor.Field.EventType)this.debouncedEvents_[goog.editor.Field.EventType[d]] = 0;
    goog.editor.BrowserFeature.USE_MUTATION_EVENTS && (this.changeTimerGecko_ = new goog.async.Delay(this.handleChange, goog.editor.Field.CHANGE_FREQUENCY, this));
    this.eventRegister = new goog.events.EventHandler(this);
    this.wrappers_ = [];
    this.loadState_ = goog.editor.Field.LoadState_.UNEDITABLE;
    this.originalDomHelper = goog.dom.getDomHelper(b || document);
    this.originalElement = this.originalDomHelper.getElement(this.id);
    this.appWindow_ = this.originalDomHelper.getWindow()
};
goog.inherits(goog.editor.Field, goog.events.EventTarget);
goog.editor.Field.prototype.field = null;
goog.editor.Field.prototype.originalElement = null;
goog.editor.Field.prototype.logger = goog.log.getLogger("goog.editor.Field");
goog.editor.Field.EventType = {COMMAND_VALUE_CHANGE: "cvc", LOAD: "load", UNLOAD: "unload", BEFORECHANGE: "beforechange", CHANGE: "change", DELAYEDCHANGE: "delayedchange", BEFOREFOCUS: "beforefocus", FOCUS: "focus", BLUR: "blur", BEFORETAB: "beforetab", IFRAME_RESIZED: "ifrsz", SELECTIONCHANGE: "selectionchange"};
goog.editor.Field.LoadState_ = {UNEDITABLE: 0, LOADING: 1, EDITABLE: 2};
goog.editor.Field.DEBOUNCE_TIME_MS_ = 500;
goog.editor.Field.activeFieldId_ = null;
goog.editor.Field.prototype.inModalMode_ = !1;
goog.editor.Field.prototype.useWindowMouseUp_ = !1;
goog.editor.Field.prototype.waitingForMouseUp_ = !1;
goog.editor.Field.setActiveFieldId = function (a) {
    goog.editor.Field.activeFieldId_ = a
};
goog.editor.Field.getActiveFieldId = function () {
    return goog.editor.Field.activeFieldId_
};
goog.editor.Field.prototype.setUseWindowMouseUp = function (a) {
    goog.asserts.assert(!a || !this.usesIframe(), "procssing window mouse up should only be enabled when not using iframe");
    this.useWindowMouseUp_ = a
};
goog.editor.Field.prototype.inModalMode = function () {
    return this.inModalMode_
};
goog.editor.Field.prototype.setModalMode = function (a) {
    this.inModalMode_ = a
};
goog.editor.Field.prototype.getHashCode = function () {
    return this.hashCode_
};
goog.editor.Field.prototype.getElement = function () {
    return this.field
};
goog.editor.Field.prototype.getOriginalElement = function () {
    return this.originalElement
};
goog.editor.Field.prototype.addListener = function (a, b, c, d) {
    var e = this.getElement();
    goog.editor.BrowserFeature.USE_DOCUMENT_FOR_KEY_EVENTS && (e && this.usesIframe()) && (e = e.ownerDocument);
    this.eventRegister.listen(e, a, b, c, d)
};
goog.editor.Field.prototype.getPluginByClassId = function (a) {
    return this.plugins_[a]
};
goog.editor.Field.prototype.registerPlugin = function (a) {
    var b = a.getTrogClassId();
    this.plugins_[b] && goog.log.error(this.logger, "Cannot register the same class of plugin twice.");
    this.plugins_[b] = a;
    for (var c in goog.editor.Plugin.OPCODE)a[goog.editor.Plugin.OPCODE[c]] && this.indexedPlugins_[c].push(a);
    a.registerFieldObject(this);
    this.isLoaded() && a.enable(this)
};
goog.editor.Field.prototype.unregisterPlugin = function (a) {
    var b = a.getTrogClassId();
    this.plugins_[b] || goog.log.error(this.logger, "Cannot unregister a plugin that isn't registered.");
    delete this.plugins_[b];
    for (var c in goog.editor.Plugin.OPCODE)a[goog.editor.Plugin.OPCODE[c]] && goog.array.remove(this.indexedPlugins_[c], a);
    a.unregisterFieldObject(this)
};
goog.editor.Field.prototype.setInitialStyle = function (a) {
    this.cssText = a
};
goog.editor.Field.prototype.resetOriginalElemProperties = function () {
    var a = this.getOriginalElement();
    a.removeAttribute("contentEditable");
    a.removeAttribute("g_editable");
    a.removeAttribute("role");
    this.id ? a.id = this.id : a.removeAttribute("id");
    a.className = this.savedClassName_ || "";
    var b = this.cssText;
    b ? goog.dom.setProperties(a, {style: b}) : a.removeAttribute("style");
    goog.isString(this.originalFieldLineHeight_) && (goog.style.setStyle(a, "lineHeight", this.originalFieldLineHeight_), this.originalFieldLineHeight_ =
        null)
};
goog.editor.Field.prototype.isModified = function (a) {
    return a ? this.isEverModified_ : this.isModified_
};
goog.editor.Field.CHANGE_FREQUENCY = 15;
goog.editor.Field.DELAYED_CHANGE_FREQUENCY = 250;
goog.editor.Field.prototype.usesIframe = goog.functions.TRUE;
goog.editor.Field.prototype.isFixedHeight = goog.functions.TRUE;
goog.editor.Field.prototype.shouldRefocusOnInputMobileSafari = goog.functions.FALSE;
goog.editor.Field.KEYS_CAUSING_CHANGES_ = {46: !0, 8: !0};
goog.userAgent.IE || (goog.editor.Field.KEYS_CAUSING_CHANGES_[9] = !0);
goog.editor.Field.CTRL_KEYS_CAUSING_CHANGES_ = {86: !0, 88: !0};
goog.userAgent.WINDOWS && !goog.userAgent.GECKO && (goog.editor.Field.KEYS_CAUSING_CHANGES_[229] = !0);
goog.editor.Field.isGeneratingKey_ = function (a, b) {
    return goog.editor.Field.isSpecialGeneratingKey_(a) ? !0 : !(!b || a.ctrlKey || a.metaKey || goog.userAgent.GECKO && !a.charCode)
};
goog.editor.Field.isSpecialGeneratingKey_ = function (a) {
    var b = !(a.ctrlKey || a.metaKey) && a.keyCode in goog.editor.Field.KEYS_CAUSING_CHANGES_;
    return(a.ctrlKey || a.metaKey) && a.keyCode in goog.editor.Field.CTRL_KEYS_CAUSING_CHANGES_ || b
};
goog.editor.Field.prototype.setAppWindow = function (a) {
    this.appWindow_ = a
};
goog.editor.Field.prototype.getAppWindow = function () {
    return this.appWindow_
};
goog.editor.Field.prototype.setBaseZindex = function (a) {
    this.baseZindex_ = a
};
goog.editor.Field.prototype.getBaseZindex = function () {
    return this.baseZindex_ || 0
};
goog.editor.Field.prototype.setupFieldObject = function (a) {
    this.loadState_ = goog.editor.Field.LoadState_.EDITABLE;
    this.field = a;
    this.editableDomHelper = goog.dom.getDomHelper(a);
    this.isEverModified_ = this.isModified_ = !1;
    a.setAttribute("g_editable", "true");
    goog.a11y.aria.setRole(a, goog.a11y.aria.Role.TEXTBOX)
};
goog.editor.Field.prototype.tearDownFieldObject_ = function () {
    this.loadState_ = goog.editor.Field.LoadState_.UNEDITABLE;
    for (var a in this.plugins_) {
        var b = this.plugins_[a];
        b.activeOnUneditableFields() || b.disable(this)
    }
    this.editableDomHelper = this.field = null
};
goog.editor.Field.prototype.setupChangeListeners_ = function () {
    if ((goog.userAgent.product.IPHONE || goog.userAgent.product.IPAD) && this.usesIframe() && this.shouldRefocusOnInputMobileSafari()) {
        var a = this.getEditableDomHelper().getWindow();
        this.boundRefocusListenerMobileSafari_ = goog.bind(a.focus, a);
        a.addEventListener(goog.events.EventType.KEYDOWN, this.boundRefocusListenerMobileSafari_, !1);
        a.addEventListener(goog.events.EventType.TOUCHEND, this.boundRefocusListenerMobileSafari_, !1)
    }
    goog.userAgent.OPERA && this.usesIframe() ?
        (this.boundFocusListenerOpera_ = goog.bind(this.dispatchFocusAndBeforeFocus_, this), this.boundBlurListenerOpera_ = goog.bind(this.dispatchBlur, this), a = this.getEditableDomHelper().getWindow(), a.addEventListener(goog.events.EventType.FOCUS, this.boundFocusListenerOpera_, !1), a.addEventListener(goog.events.EventType.BLUR, this.boundBlurListenerOpera_, !1)) : (goog.editor.BrowserFeature.SUPPORTS_FOCUSIN ? (this.addListener(goog.events.EventType.FOCUS, this.dispatchFocus_), this.addListener(goog.events.EventType.FOCUSIN,
        this.dispatchBeforeFocus_)) : this.addListener(goog.events.EventType.FOCUS, this.dispatchFocusAndBeforeFocus_), this.addListener(goog.events.EventType.BLUR, this.dispatchBlur, goog.editor.BrowserFeature.USE_MUTATION_EVENTS));
    goog.editor.BrowserFeature.USE_MUTATION_EVENTS ? this.setupMutationEventHandlersGecko() : (this.addListener(["beforecut", "beforepaste", "drop", "dragend"], this.dispatchBeforeChange), this.addListener(["cut", "paste"], goog.functions.lock(this.dispatchChange)), this.addListener("drop", this.handleDrop_));
    this.addListener(goog.userAgent.WEBKIT ? "dragend" : "dragdrop", this.handleDrop_);
    this.addListener(goog.events.EventType.KEYDOWN, this.handleKeyDown_);
    this.addListener(goog.events.EventType.KEYPRESS, this.handleKeyPress_);
    this.addListener(goog.events.EventType.KEYUP, this.handleKeyUp_);
    this.selectionChangeTimer_ = new goog.async.Delay(this.handleSelectionChangeTimer_, goog.editor.Field.SELECTION_CHANGE_FREQUENCY_, this);
    goog.editor.BrowserFeature.FOLLOWS_EDITABLE_LINKS && this.addListener(goog.events.EventType.CLICK,
        goog.editor.Field.cancelLinkClick_);
    this.addListener(goog.events.EventType.MOUSEDOWN, this.handleMouseDown_);
    this.useWindowMouseUp_ ? (this.eventRegister.listen(this.editableDomHelper.getDocument(), goog.events.EventType.MOUSEUP, this.handleMouseUp_), this.addListener(goog.events.EventType.DRAGSTART, this.handleDragStart_)) : this.addListener(goog.events.EventType.MOUSEUP, this.handleMouseUp_)
};
goog.editor.Field.SELECTION_CHANGE_FREQUENCY_ = 250;
goog.editor.Field.prototype.clearListeners = function () {
    this.eventRegister && this.eventRegister.removeAll();
    if ((goog.userAgent.product.IPHONE || goog.userAgent.product.IPAD) && this.usesIframe() && this.shouldRefocusOnInputMobileSafari()) {
        try {
            var a = this.getEditableDomHelper().getWindow();
            a.removeEventListener(goog.events.EventType.KEYDOWN, this.boundRefocusListenerMobileSafari_, !1);
            a.removeEventListener(goog.events.EventType.TOUCHEND, this.boundRefocusListenerMobileSafari_, !1)
        } catch (b) {
        }
        delete this.boundRefocusListenerMobileSafari_
    }
    if (goog.userAgent.OPERA &&
        this.usesIframe()) {
        try {
            a = this.getEditableDomHelper().getWindow(), a.removeEventListener(goog.events.EventType.FOCUS, this.boundFocusListenerOpera_, !1), a.removeEventListener(goog.events.EventType.BLUR, this.boundBlurListenerOpera_, !1)
        } catch (c) {
        }
        delete this.boundFocusListenerOpera_;
        delete this.boundBlurListenerOpera_
    }
    this.changeTimerGecko_ && this.changeTimerGecko_.stop();
    this.delayedChangeTimer_.stop()
};
goog.editor.Field.prototype.disposeInternal = function () {
    (this.isLoading() || this.isLoaded()) && goog.log.warning(this.logger, "Disposing a field that is in use.");
    this.getOriginalElement() && this.execCommand(goog.editor.Command.CLEAR_LOREM);
    this.tearDownFieldObject_();
    this.clearListeners();
    this.clearFieldLoadListener_();
    this.originalDomHelper = null;
    this.eventRegister && (this.eventRegister.dispose(), this.eventRegister = null);
    this.removeAllWrappers();
    goog.editor.Field.getActiveFieldId() == this.id && goog.editor.Field.setActiveFieldId(null);
    for (var a in this.plugins_) {
        var b = this.plugins_[a];
        b.isAutoDispose() && b.dispose()
    }
    delete this.plugins_;
    goog.editor.Field.superClass_.disposeInternal.call(this)
};
goog.editor.Field.prototype.attachWrapper = function (a) {
    this.wrappers_.push(a)
};
goog.editor.Field.prototype.removeAllWrappers = function () {
    for (var a; a = this.wrappers_.pop();)a.dispose()
};
goog.editor.Field.MUTATION_EVENTS_GECKO = ["DOMNodeInserted", "DOMNodeRemoved", "DOMNodeRemovedFromDocument", "DOMNodeInsertedIntoDocument", "DOMCharacterDataModified"];
goog.editor.Field.prototype.setupMutationEventHandlersGecko = function () {
    if (goog.editor.BrowserFeature.HAS_DOM_SUBTREE_MODIFIED_EVENT || !this.usesIframe())this.eventRegister.listen(this.getElement(), "DOMSubtreeModified", this.handleMutationEventGecko_); else {
        var a = this.getEditableDomHelper().getDocument();
        this.eventRegister.listen(a, goog.editor.Field.MUTATION_EVENTS_GECKO, this.handleMutationEventGecko_, !0);
        this.eventRegister.listen(a, "DOMAttrModified", goog.bind(this.handleDomAttrChange, this, this.handleMutationEventGecko_),
            !0)
    }
};
goog.editor.Field.prototype.handleBeforeChangeKeyEvent_ = function (a) {
    if (a.keyCode == goog.events.KeyCodes.TAB && !this.dispatchBeforeTab_(a) || goog.userAgent.GECKO && a.metaKey && (a.keyCode == goog.events.KeyCodes.LEFT || a.keyCode == goog.events.KeyCodes.RIGHT))return a.preventDefault(), !1;
    (this.gotGeneratingKey_ = a.charCode || goog.editor.Field.isGeneratingKey_(a, goog.userAgent.GECKO)) && this.dispatchBeforeChange();
    return!0
};
goog.editor.Field.SELECTION_CHANGE_KEYCODES_ = {8: 1, 9: 1, 13: 1, 33: 1, 34: 1, 35: 1, 36: 1, 37: 1, 38: 1, 39: 1, 40: 1, 46: 1};
goog.editor.Field.CTRL_KEYS_CAUSING_SELECTION_CHANGES_ = {65: !0, 86: !0, 88: !0};
goog.editor.Field.POTENTIAL_SHORTCUT_KEYCODES_ = {8: 1, 9: 1, 13: 1, 27: 1, 33: 1, 34: 1, 37: 1, 38: 1, 39: 1, 40: 1};
goog.editor.Field.prototype.invokeShortCircuitingOp_ = function (a, b) {
    for (var c = this.indexedPlugins_[a], d = goog.array.slice(arguments, 1), e = 0; e < c.length; ++e) {
        var f = c[e];
        if ((f.isEnabled(this) || goog.editor.Plugin.IRREPRESSIBLE_OPS[a]) && f[goog.editor.Plugin.OPCODE[a]].apply(f, d))return!0
    }
    return!1
};
goog.editor.Field.prototype.invokeOp_ = function (a, b) {
    for (var c = this.indexedPlugins_[a], d = goog.array.slice(arguments, 1), e = 0; e < c.length; ++e) {
        var f = c[e];
        (f.isEnabled(this) || goog.editor.Plugin.IRREPRESSIBLE_OPS[a]) && f[goog.editor.Plugin.OPCODE[a]].apply(f, d)
    }
};
goog.editor.Field.prototype.reduceOp_ = function (a, b, c) {
    for (var d = this.indexedPlugins_[a], e = goog.array.slice(arguments, 1), f = 0; f < d.length; ++f) {
        var g = d[f];
        if (g.isEnabled(this) || goog.editor.Plugin.IRREPRESSIBLE_OPS[a])e[0] = g[goog.editor.Plugin.OPCODE[a]].apply(g, e)
    }
    return e[0]
};
goog.editor.Field.prototype.injectContents = function (a, b) {
    var c = {}, d = this.getInjectableContents(a, c);
    goog.style.setStyle(b, c);
    goog.editor.node.replaceInnerHtml(b, d)
};
goog.editor.Field.prototype.getInjectableContents = function (a, b) {
    return this.reduceOp_(goog.editor.Plugin.Op.PREPARE_CONTENTS_HTML, a || "", b)
};
goog.editor.Field.prototype.handleKeyDown_ = function (a) {
    (goog.editor.BrowserFeature.USE_MUTATION_EVENTS || this.handleBeforeChangeKeyEvent_(a)) && !this.invokeShortCircuitingOp_(goog.editor.Plugin.Op.KEYDOWN, a) && goog.editor.BrowserFeature.USES_KEYDOWN && this.handleKeyboardShortcut_(a)
};
goog.editor.Field.prototype.handleKeyPress_ = function (a) {
    if (goog.editor.BrowserFeature.USE_MUTATION_EVENTS) {
        if (!this.handleBeforeChangeKeyEvent_(a))return
    } else this.gotGeneratingKey_ = !0, this.dispatchBeforeChange();
    this.invokeShortCircuitingOp_(goog.editor.Plugin.Op.KEYPRESS, a) || goog.editor.BrowserFeature.USES_KEYDOWN || this.handleKeyboardShortcut_(a)
};
goog.editor.Field.prototype.handleKeyUp_ = function (a) {
    goog.editor.BrowserFeature.USE_MUTATION_EVENTS || !this.gotGeneratingKey_ && !goog.editor.Field.isSpecialGeneratingKey_(a) || this.handleChange();
    this.invokeShortCircuitingOp_(goog.editor.Plugin.Op.KEYUP, a);
    this.isEventStopped(goog.editor.Field.EventType.SELECTIONCHANGE) || (goog.editor.Field.SELECTION_CHANGE_KEYCODES_[a.keyCode] || (a.ctrlKey || a.metaKey) && goog.editor.Field.CTRL_KEYS_CAUSING_SELECTION_CHANGES_[a.keyCode]) && this.selectionChangeTimer_.start()
};
goog.editor.Field.prototype.handleKeyboardShortcut_ = function (a) {
    if (!a.altKey) {
        var b = goog.userAgent.MAC ? a.metaKey : a.ctrlKey;
        if (b || goog.editor.Field.POTENTIAL_SHORTCUT_KEYCODES_[a.keyCode]) {
            var c = a.charCode || a.keyCode;
            17 != c && (c = String.fromCharCode(c).toLowerCase(), this.invokeShortCircuitingOp_(goog.editor.Plugin.Op.SHORTCUT, a, c, b) && a.preventDefault())
        }
    }
};
goog.editor.Field.prototype.execCommand = function (a, b) {
    for (var c = arguments, d, e = this.indexedPlugins_[goog.editor.Plugin.Op.EXEC_COMMAND], f = 0; f < e.length; ++f) {
        var g = e[f];
        if (g.isEnabled(this) && g.isSupportedCommand(a)) {
            d = g.execCommand.apply(g, c);
            break
        }
    }
    return d
};
goog.editor.Field.prototype.queryCommandValue = function (a) {
    var b = this.isLoaded() && this.isSelectionEditable();
    if (goog.isString(a))return this.queryCommandValueInternal_(a, b);
    for (var c = {}, d = 0; d < a.length; d++)c[a[d]] = this.queryCommandValueInternal_(a[d], b);
    return c
};
goog.editor.Field.prototype.queryCommandValueInternal_ = function (a, b) {
    for (var c = this.indexedPlugins_[goog.editor.Plugin.Op.QUERY_COMMAND], d = 0; d < c.length; ++d) {
        var e = c[d];
        if (e.isEnabled(this) && e.isSupportedCommand(a) && (b || e.activeOnUneditableFields()))return e.queryCommandValue(a)
    }
    return b ? null : !1
};
goog.editor.Field.prototype.handleDomAttrChange = function (a, b) {
    if (!this.isEventStopped(goog.editor.Field.EventType.CHANGE)) {
        var c = b.getBrowserEvent();
        try {
            if (c.originalTarget.prefix || "scrollbar" == c.originalTarget.nodeName)return
        } catch (d) {
            return
        }
        c.prevValue != c.newValue && a.call(this, c)
    }
};
goog.editor.Field.prototype.handleMutationEventGecko_ = function (a) {
    this.isEventStopped(goog.editor.Field.EventType.CHANGE) || (a = a.getBrowserEvent ? a.getBrowserEvent() : a, a.target.firebugIgnore || (this.isEverModified_ = this.isModified_ = !0, this.changeTimerGecko_.start()))
};
goog.editor.Field.prototype.handleDrop_ = function (a) {
    goog.userAgent.IE && this.execCommand(goog.editor.Command.CLEAR_LOREM, !0);
    goog.editor.BrowserFeature.USE_MUTATION_EVENTS && this.dispatchFocusAndBeforeFocus_();
    this.dispatchChange()
};
goog.editor.Field.prototype.getEditableIframe = function () {
    var a;
    return this.usesIframe() && (a = this.getEditableDomHelper()) ? (a = a.getWindow()) && a.frameElement : null
};
goog.editor.Field.prototype.getEditableDomHelper = function () {
    return this.editableDomHelper
};
goog.editor.Field.prototype.getRange = function () {
    var a = this.editableDomHelper && this.editableDomHelper.getWindow();
    return a && goog.dom.Range.createFromWindow(a)
};
goog.editor.Field.prototype.dispatchSelectionChangeEvent = function (a, b) {
    if (!this.isEventStopped(goog.editor.Field.EventType.SELECTIONCHANGE)) {
        var c = this.getRange(), c = c && c.getContainerElement();
        this.isSelectionEditable_ = !!c && goog.dom.contains(this.getElement(), c);
        this.dispatchCommandValueChange();
        this.dispatchEvent({type: goog.editor.Field.EventType.SELECTIONCHANGE, originalType: a && a.type});
        this.invokeShortCircuitingOp_(goog.editor.Plugin.Op.SELECTION, a, b)
    }
};
goog.editor.Field.prototype.handleSelectionChangeTimer_ = function () {
    var a = this.selectionChangeTarget_;
    this.selectionChangeTarget_ = null;
    this.dispatchSelectionChangeEvent(void 0, a)
};
goog.editor.Field.prototype.dispatchBeforeChange = function () {
    this.isEventStopped(goog.editor.Field.EventType.BEFORECHANGE) || this.dispatchEvent(goog.editor.Field.EventType.BEFORECHANGE)
};
goog.editor.Field.prototype.dispatchBeforeTab_ = function (a) {
    return this.dispatchEvent({type: goog.editor.Field.EventType.BEFORETAB, shiftKey: a.shiftKey, altKey: a.altKey, ctrlKey: a.ctrlKey})
};
goog.editor.Field.prototype.stopChangeEvents = function (a, b) {
    a && (this.changeTimerGecko_ && this.changeTimerGecko_.fireIfActive(), this.stopEvent(goog.editor.Field.EventType.CHANGE));
    b && (this.clearDelayedChange(), this.stopEvent(goog.editor.Field.EventType.DELAYEDCHANGE))
};
goog.editor.Field.prototype.startChangeEvents = function (a, b) {
    !a && this.changeTimerGecko_ && this.changeTimerGecko_.fireIfActive();
    this.startEvent(goog.editor.Field.EventType.CHANGE);
    this.startEvent(goog.editor.Field.EventType.DELAYEDCHANGE);
    a && this.handleChange();
    b && this.dispatchDelayedChange_()
};
goog.editor.Field.prototype.stopEvent = function (a) {
    this.stoppedEvents_[a] = 1
};
goog.editor.Field.prototype.startEvent = function (a) {
    this.stoppedEvents_[a] = 0
};
goog.editor.Field.prototype.debounceEvent = function (a) {
    this.debouncedEvents_[a] = goog.now()
};
goog.editor.Field.prototype.isEventStopped = function (a) {
    return!!this.stoppedEvents_[a] || this.debouncedEvents_[a] && goog.now() - this.debouncedEvents_[a] <= goog.editor.Field.DEBOUNCE_TIME_MS_
};
goog.editor.Field.prototype.manipulateDom = function (a, b, c) {
    this.stopChangeEvents(!0, !0);
    try {
        a.call(c)
    } finally {
        this.isLoaded() && (b ? (this.startEvent(goog.editor.Field.EventType.CHANGE), this.handleChange(), this.startEvent(goog.editor.Field.EventType.DELAYEDCHANGE)) : this.dispatchChange())
    }
};
goog.editor.Field.prototype.dispatchCommandValueChange = function (a) {
    a ? this.dispatchEvent({type: goog.editor.Field.EventType.COMMAND_VALUE_CHANGE, commands: a}) : this.dispatchEvent(goog.editor.Field.EventType.COMMAND_VALUE_CHANGE)
};
goog.editor.Field.prototype.dispatchChange = function (a) {
    this.startChangeEvents(!0, a)
};
goog.editor.Field.prototype.handleChange = function () {
    this.isEventStopped(goog.editor.Field.EventType.CHANGE) || (this.changeTimerGecko_ && this.changeTimerGecko_.stop(), this.isEverModified_ = this.isModified_ = !0, this.isEventStopped(goog.editor.Field.EventType.DELAYEDCHANGE) || this.delayedChangeTimer_.start())
};
goog.editor.Field.prototype.dispatchDelayedChange_ = function () {
    this.isEventStopped(goog.editor.Field.EventType.DELAYEDCHANGE) || (this.delayedChangeTimer_.stop(), this.isModified_ = !1, this.dispatchEvent(goog.editor.Field.EventType.DELAYEDCHANGE))
};
goog.editor.Field.prototype.clearDelayedChange = function () {
    this.changeTimerGecko_ && this.changeTimerGecko_.fireIfActive();
    this.delayedChangeTimer_.fireIfActive()
};
goog.editor.Field.prototype.dispatchFocusAndBeforeFocus_ = function () {
    this.dispatchBeforeFocus_();
    this.dispatchFocus_()
};
goog.editor.Field.prototype.dispatchBeforeFocus_ = function () {
    this.isEventStopped(goog.editor.Field.EventType.BEFOREFOCUS) || (this.execCommand(goog.editor.Command.CLEAR_LOREM, !0), this.dispatchEvent(goog.editor.Field.EventType.BEFOREFOCUS))
};
goog.editor.Field.prototype.dispatchFocus_ = function () {
    if (!this.isEventStopped(goog.editor.Field.EventType.FOCUS)) {
        goog.editor.Field.setActiveFieldId(this.id);
        this.isSelectionEditable_ = !0;
        this.dispatchEvent(goog.editor.Field.EventType.FOCUS);
        if (goog.editor.BrowserFeature.PUTS_CURSOR_BEFORE_FIRST_BLOCK_ELEMENT_ON_FOCUS) {
            var a = this.getElement(), b = this.getRange();
            if (b) {
                var c = b.getFocusNode();
                0 != b.getFocusOffset() || c && c != a && c.tagName != goog.dom.TagName.BODY || goog.editor.range.selectNodeStart(a)
            }
        }
        !goog.editor.BrowserFeature.CLEARS_SELECTION_WHEN_FOCUS_LEAVES &&
            this.usesIframe() && this.getEditableDomHelper().getWindow().parent.getSelection().removeAllRanges()
    }
};
goog.editor.Field.prototype.dispatchBlur = function () {
    this.isEventStopped(goog.editor.Field.EventType.BLUR) || (goog.editor.Field.getActiveFieldId() == this.id && goog.editor.Field.setActiveFieldId(null), this.isSelectionEditable_ = !1, this.dispatchEvent(goog.editor.Field.EventType.BLUR))
};
goog.editor.Field.prototype.isSelectionEditable = function () {
    return this.isSelectionEditable_
};
goog.editor.Field.cancelLinkClick_ = function (a) {
    goog.dom.getAncestorByTagNameAndClass(a.target, goog.dom.TagName.A) && a.preventDefault()
};
goog.editor.Field.prototype.handleMouseDown_ = function (a) {
    goog.editor.Field.setActiveFieldId(this.id);
    if (goog.userAgent.IE) {
        var b = a.target;
        b && (b.tagName == goog.dom.TagName.A && a.ctrlKey) && this.originalDomHelper.getWindow().open(b.href)
    }
    this.waitingForMouseUp_ = !0
};
goog.editor.Field.prototype.handleDragStart_ = function (a) {
    this.waitingForMouseUp_ = !1
};
goog.editor.Field.prototype.handleMouseUp_ = function (a) {
    if (!this.useWindowMouseUp_ || this.waitingForMouseUp_)this.waitingForMouseUp_ = !1, this.dispatchSelectionChangeEvent(a), goog.userAgent.IE && (this.selectionChangeTarget_ = a.target, this.selectionChangeTimer_.start())
};
goog.editor.Field.prototype.getCleanContents = function () {
    if (this.queryCommandValue(goog.editor.Command.USING_LOREM))return goog.string.Unicode.NBSP;
    if (!this.isLoaded()) {
        var a = this.getOriginalElement();
        a || goog.log.log(this.logger, goog.log.Level.SHOUT, "Couldn't get the field element to read the contents");
        return a.innerHTML
    }
    a = this.getFieldCopy();
    this.invokeOp_(goog.editor.Plugin.Op.CLEAN_CONTENTS_DOM, a);
    return this.reduceOp_(goog.editor.Plugin.Op.CLEAN_CONTENTS_HTML, a.innerHTML)
};
goog.editor.Field.prototype.getFieldCopy = function () {
    var a = this.getElement(), b = a.cloneNode(!1), a = a.innerHTML;
    goog.userAgent.IE && a.match(/^\s*<script/i) && (a = goog.string.Unicode.NBSP + a);
    b.innerHTML = a;
    return b
};
goog.editor.Field.prototype.setHtml = function (a, b, c, d) {
    this.isLoading() ? goog.log.error(this.logger, "Can't set html while loading Trogedit") : (d && this.execCommand(goog.editor.Command.CLEAR_LOREM), b && a && (b = "<p>" + b + "</p>"), c && this.stopChangeEvents(!1, !0), this.setInnerHtml_(b), d && this.execCommand(goog.editor.Command.UPDATE_LOREM), this.isLoaded() && (c ? (goog.editor.BrowserFeature.USE_MUTATION_EVENTS && this.changeTimerGecko_.fireIfActive(), this.startChangeEvents()) : this.dispatchChange()))
};
goog.editor.Field.prototype.setInnerHtml_ = function (a) {
    var b = this.getElement();
    if (b) {
        if (this.usesIframe() && goog.editor.BrowserFeature.MOVES_STYLE_TO_HEAD)for (var c = b.ownerDocument.getElementsByTagName("HEAD"), d = c.length - 1; 1 <= d; --d)c[d].parentNode.removeChild(c[d])
    } else b = this.getOriginalElement();
    b && this.injectContents(a, b)
};
goog.editor.Field.prototype.turnOnDesignModeGecko = function () {
    var a = this.getEditableDomHelper().getDocument();
    a.designMode = "on";
    goog.editor.BrowserFeature.HAS_STYLE_WITH_CSS && a.execCommand("styleWithCSS", !1, !1)
};
goog.editor.Field.prototype.installStyles = function () {
    this.cssStyles && this.shouldLoadAsynchronously() && goog.style.installStyles(this.cssStyles, this.getElement())
};
goog.editor.Field.prototype.dispatchLoadEvent_ = function () {
    this.getElement();
    this.installStyles();
    this.startChangeEvents();
    goog.log.info(this.logger, "Dispatching load " + this.id);
    this.dispatchEvent(goog.editor.Field.EventType.LOAD)
};
goog.editor.Field.prototype.isUneditable = function () {
    return this.loadState_ == goog.editor.Field.LoadState_.UNEDITABLE
};
goog.editor.Field.prototype.isLoaded = function () {
    return this.loadState_ == goog.editor.Field.LoadState_.EDITABLE
};
goog.editor.Field.prototype.isLoading = function () {
    return this.loadState_ == goog.editor.Field.LoadState_.LOADING
};
goog.editor.Field.prototype.focus = function () {
    if (!goog.editor.BrowserFeature.HAS_CONTENT_EDITABLE && this.usesIframe())this.getEditableDomHelper().getWindow().focus(); else {
        if (goog.userAgent.OPERA)var a = this.appWindow_.pageXOffset, b = this.appWindow_.pageYOffset;
        this.getElement().focus();
        goog.userAgent.OPERA && this.appWindow_.scrollTo(a, b)
    }
};
goog.editor.Field.prototype.focusAndPlaceCursorAtStart = function () {
    (goog.editor.BrowserFeature.HAS_IE_RANGES || goog.userAgent.WEBKIT) && this.placeCursorAtStart();
    this.focus()
};
goog.editor.Field.prototype.placeCursorAtStart = function () {
    this.placeCursorAtStartOrEnd_(!0)
};
goog.editor.Field.prototype.placeCursorAtEnd = function () {
    this.placeCursorAtStartOrEnd_(!1)
};
goog.editor.Field.prototype.placeCursorAtStartOrEnd_ = function (a) {
    var b = this.getElement();
    if (b) {
        var c = a ? goog.editor.node.getLeftMostLeaf(b) : goog.editor.node.getRightMostLeaf(b);
        b == c ? goog.dom.Range.createCaret(b, 0).select() : goog.editor.range.placeCursorNextTo(c, a);
        this.dispatchSelectionChangeEvent()
    }
};
goog.editor.Field.prototype.restoreSavedRange = function (a) {
    goog.userAgent.IE && this.focus();
    a && a.restore();
    goog.userAgent.IE || this.focus()
};
goog.editor.Field.prototype.makeEditable = function (a) {
    this.loadState_ = goog.editor.Field.LoadState_.LOADING;
    var b = this.getOriginalElement();
    this.nodeName = b.nodeName;
    this.savedClassName_ = b.className;
    this.setInitialStyle(b.style.cssText);
    b.className += " editable";
    this.makeEditableInternal(a)
};
goog.editor.Field.prototype.makeEditableInternal = function (a) {
    this.makeIframeField_(a)
};
goog.editor.Field.prototype.handleFieldLoad = function () {
    goog.userAgent.IE && goog.dom.Range.clearSelection(this.editableDomHelper.getWindow());
    goog.editor.Field.getActiveFieldId() != this.id && this.execCommand(goog.editor.Command.UPDATE_LOREM);
    this.setupChangeListeners_();
    this.dispatchLoadEvent_();
    for (var a in this.plugins_)this.plugins_[a].enable(this)
};
goog.editor.Field.prototype.makeUneditable = function (a) {
    if (this.isUneditable())throw Error("makeUneditable: Field is already uneditable");
    this.clearDelayedChange();
    this.selectionChangeTimer_.fireIfActive();
    this.execCommand(goog.editor.Command.CLEAR_LOREM);
    var b = null;
    !a && this.getElement() && (b = this.getCleanContents());
    this.clearFieldLoadListener_();
    a = this.getOriginalElement();
    goog.editor.Field.getActiveFieldId() == a.id && goog.editor.Field.setActiveFieldId(null);
    this.clearListeners();
    goog.isString(b) &&
    (goog.editor.node.replaceInnerHtml(a, b), this.resetOriginalElemProperties());
    this.restoreDom();
    this.tearDownFieldObject_();
    goog.userAgent.WEBKIT && a.blur();
    this.execCommand(goog.editor.Command.UPDATE_LOREM);
    this.dispatchEvent(goog.editor.Field.EventType.UNLOAD)
};
goog.editor.Field.prototype.restoreDom = function () {
    var a = this.getOriginalElement();
    if (a) {
        var b = this.getEditableIframe();
        b && goog.dom.replaceNode(a, b)
    }
};
goog.editor.Field.prototype.shouldLoadAsynchronously = function () {
    if (!goog.isDef(this.isHttps_) && (this.isHttps_ = !1, goog.userAgent.IE && this.usesIframe())) {
        for (var a = this.originalDomHelper.getWindow(); a != a.parent;)try {
            a = a.parent
        } catch (b) {
            break
        }
        a = a.location;
        this.isHttps_ = "https:" == a.protocol && -1 == a.search.indexOf("nocheckhttps")
    }
    return this.isHttps_
};
goog.editor.Field.prototype.makeIframeField_ = function (a) {
    var b = this.getOriginalElement();
    if (b) {
        var b = b.innerHTML, c = {}, b = this.reduceOp_(goog.editor.Plugin.Op.PREPARE_CONTENTS_HTML, b, c), d = this.originalDomHelper.createDom(goog.dom.TagName.IFRAME, this.getIframeAttributes());
        if (this.shouldLoadAsynchronously()) {
            var e = goog.bind(this.iframeFieldLoadHandler, this, d, b, c);
            this.fieldLoadListenerKey_ = goog.events.listen(d, goog.events.EventType.LOAD, e, !0);
            a && (d.src = a)
        }
        this.attachIframe(d);
        this.shouldLoadAsynchronously() ||
        this.iframeFieldLoadHandler(d, b, c)
    }
};
goog.editor.Field.prototype.attachIframe = function (a) {
    var b = this.getOriginalElement();
    a.className = b.className;
    a.id = b.id;
    goog.dom.replaceNode(a, b)
};
goog.editor.Field.prototype.getFieldFormatInfo = function (a) {
    var b = this.getOriginalElement(), b = goog.editor.node.isStandardsMode(b);
    return new goog.editor.icontent.FieldFormatInfo(this.id, b, !1, !1, a)
};
goog.editor.Field.prototype.writeIframeContent = function (a, b, c) {
    c = this.getFieldFormatInfo(c);
    if (this.shouldLoadAsynchronously())a = goog.dom.getFrameContentDocument(a), goog.editor.icontent.writeHttpsInitialIframe(c, a, b); else {
        var d = new goog.editor.icontent.FieldStyleInfo(this.getElement(), this.cssStyles);
        goog.editor.icontent.writeNormalInitialIframe(c, b, d, a)
    }
};
goog.editor.Field.prototype.iframeFieldLoadHandler = function (a, b, c) {
    this.clearFieldLoadListener_();
    a.allowTransparency = "true";
    this.writeIframeContent(a, b, c);
    a = goog.dom.getFrameContentDocument(a).body;
    this.setupFieldObject(a);
    !goog.editor.BrowserFeature.HAS_CONTENT_EDITABLE && this.usesIframe() && this.turnOnDesignModeGecko();
    this.handleFieldLoad()
};
goog.editor.Field.prototype.clearFieldLoadListener_ = function () {
    this.fieldLoadListenerKey_ && (goog.events.unlistenByKey(this.fieldLoadListenerKey_), this.fieldLoadListenerKey_ = null)
};
goog.editor.Field.prototype.getIframeAttributes = function () {
    var a = "padding:0;" + this.getOriginalElement().style.cssText;
    goog.string.endsWith(a, ";") || (a += ";");
    a += "background-color:white;";
    goog.userAgent.IE && (a += "overflow:visible;");
    return{frameBorder: 0, style: a}
};
goog.editor.plugins.AbstractDialogPlugin = function (a) {
    goog.editor.Plugin.call(this);
    this.command_ = a
};
goog.inherits(goog.editor.plugins.AbstractDialogPlugin, goog.editor.Plugin);
goog.editor.plugins.AbstractDialogPlugin.prototype.isSupportedCommand = function (a) {
    return a == this.command_
};
goog.editor.plugins.AbstractDialogPlugin.prototype.execCommand = function (a, b) {
    return this.execCommandInternal.apply(this, arguments)
};
goog.editor.plugins.AbstractDialogPlugin.EventType = {OPENED: "dialogOpened", CLOSED: "dialogClosed"};
goog.editor.plugins.AbstractDialogPlugin.prototype.getDialog = function () {
    return this.dialog_
};
goog.editor.plugins.AbstractDialogPlugin.prototype.setReuseDialog = function (a) {
    this.reuseDialog_ = a
};
goog.editor.plugins.AbstractDialogPlugin.prototype.execCommandInternal = function (a, b) {
    this.reuseDialog_ || this.disposeDialog_();
    this.dialog_ || (this.dialog_ = this.createDialog(goog.dom.getDomHelper(this.getFieldObject().getAppWindow()), b));
    var c = this.getFieldObject().getRange();
    this.savedRange_ = c && goog.editor.range.saveUsingNormalizedCarets(c);
    goog.dom.Range.clearSelection(this.getFieldObject().getEditableDomHelper().getWindow());
    goog.events.listenOnce(this.dialog_, goog.ui.editor.AbstractDialog.EventType.AFTER_HIDE,
        this.handleAfterHide, !1, this);
    this.getFieldObject().setModalMode(!0);
    this.dialog_.show();
    this.dispatchEvent(goog.editor.plugins.AbstractDialogPlugin.EventType.OPENED);
    this.getFieldObject().dispatchSelectionChangeEvent();
    return!0
};
goog.editor.plugins.AbstractDialogPlugin.prototype.handleAfterHide = function (a) {
    this.getFieldObject().setModalMode(!1);
    this.restoreOriginalSelection();
    this.reuseDialog_ || this.disposeDialog_();
    this.dispatchEvent(goog.editor.plugins.AbstractDialogPlugin.EventType.CLOSED);
    this.getFieldObject().dispatchSelectionChangeEvent();
    this.getFieldObject().debounceEvent(goog.editor.Field.EventType.SELECTIONCHANGE)
};
goog.editor.plugins.AbstractDialogPlugin.prototype.restoreOriginalSelection = function () {
    this.getFieldObject().restoreSavedRange(this.savedRange_);
    this.savedRange_ = null
};
goog.editor.plugins.AbstractDialogPlugin.prototype.disposeOriginalSelection = function () {
    this.savedRange_ && (this.savedRange_.dispose(), this.savedRange_ = null)
};
goog.editor.plugins.AbstractDialogPlugin.prototype.disposeInternal = function () {
    this.disposeDialog_();
    goog.editor.plugins.AbstractDialogPlugin.superClass_.disposeInternal.call(this)
};
goog.editor.plugins.AbstractDialogPlugin.prototype.reuseDialog_ = !1;
goog.editor.plugins.AbstractDialogPlugin.prototype.isDisposingDialog_ = !1;
goog.editor.plugins.AbstractDialogPlugin.prototype.disposeDialog_ = function () {
    this.dialog_ && !this.isDisposingDialog_ && (this.isDisposingDialog_ = !0, this.dialog_.dispose(), this.dialog_ = null, this.isDisposingDialog_ = !1)
};
goog.editor.plugins.LinkDialogPlugin = function () {
    goog.editor.plugins.AbstractDialogPlugin.call(this, goog.editor.Command.MODAL_LINK_EDITOR);
    this.eventHandler_ = new goog.events.EventHandler(this);
    this.safeToOpenSchemes_ = ["http", "https", "ftp"]
};
goog.inherits(goog.editor.plugins.LinkDialogPlugin, goog.editor.plugins.AbstractDialogPlugin);
goog.editor.plugins.LinkDialogPlugin.prototype.showOpenLinkInNewWindow_ = !1;
goog.editor.plugins.LinkDialogPlugin.prototype.isOpenLinkInNewWindowChecked_ = !1;
goog.editor.plugins.LinkDialogPlugin.prototype.showRelNoFollow_ = !1;
goog.editor.plugins.LinkDialogPlugin.prototype.stopReferrerLeaks_ = !1;
goog.editor.plugins.LinkDialogPlugin.prototype.blockOpeningUnsafeSchemes_ = !0;
goog.editor.plugins.LinkDialogPlugin.prototype.getTrogClassId = goog.functions.constant("LinkDialogPlugin");
goog.editor.plugins.LinkDialogPlugin.prototype.setBlockOpeningUnsafeSchemes = function (a) {
    this.blockOpeningUnsafeSchemes_ = a
};
goog.editor.plugins.LinkDialogPlugin.prototype.setSafeToOpenSchemes = function (a) {
    this.safeToOpenSchemes_ = a
};
goog.editor.plugins.LinkDialogPlugin.prototype.showOpenLinkInNewWindow = function (a) {
    this.showOpenLinkInNewWindow_ = !0;
    this.isOpenLinkInNewWindowChecked_ = a
};
goog.editor.plugins.LinkDialogPlugin.prototype.showRelNoFollow = function () {
    this.showRelNoFollow_ = !0
};
goog.editor.plugins.LinkDialogPlugin.prototype.getOpenLinkInNewWindowCheckedState = function () {
    return this.isOpenLinkInNewWindowChecked_
};
goog.editor.plugins.LinkDialogPlugin.prototype.stopReferrerLeaks = function () {
    this.stopReferrerLeaks_ = !0
};
goog.editor.plugins.LinkDialogPlugin.prototype.setEmailWarning = function (a) {
    this.emailWarning_ = a
};
goog.editor.plugins.LinkDialogPlugin.prototype.execCommandInternal = function (a, b) {
    this.currentLink_ = b;
    return goog.editor.plugins.LinkDialogPlugin.superClass_.execCommandInternal.call(this, a, b)
};
goog.editor.plugins.LinkDialogPlugin.prototype.handleAfterHide = function (a) {
    goog.editor.plugins.LinkDialogPlugin.superClass_.handleAfterHide.call(this, a);
    this.currentLink_ = null
};
goog.editor.plugins.LinkDialogPlugin.prototype.getEventHandler = function () {
    return this.eventHandler_
};
goog.editor.plugins.LinkDialogPlugin.prototype.getCurrentLink = function () {
    return this.currentLink_
};
goog.editor.plugins.LinkDialogPlugin.prototype.createDialog = function (a, b) {
    var c = new goog.ui.editor.LinkDialog(a, b);
    this.emailWarning_ && c.setEmailWarning(this.emailWarning_);
    this.showOpenLinkInNewWindow_ && c.showOpenLinkInNewWindow(this.isOpenLinkInNewWindowChecked_);
    this.showRelNoFollow_ && c.showRelNoFollow();
    c.setStopReferrerLeaks(this.stopReferrerLeaks_);
    this.eventHandler_.listen(c, goog.ui.editor.AbstractDialog.EventType.OK, this.handleOk).listen(c, goog.ui.editor.AbstractDialog.EventType.CANCEL, this.handleCancel_).listen(c,
        goog.ui.editor.LinkDialog.EventType.BEFORE_TEST_LINK, this.handleBeforeTestLink);
    return c
};
goog.editor.plugins.LinkDialogPlugin.prototype.disposeInternal = function () {
    goog.editor.plugins.LinkDialogPlugin.superClass_.disposeInternal.call(this);
    this.eventHandler_.dispose()
};
goog.editor.plugins.LinkDialogPlugin.prototype.handleOk = function (a) {
    this.disposeOriginalSelection();
    this.currentLink_.setTextAndUrl(a.linkText, a.linkUrl);
    this.showOpenLinkInNewWindow_ && (this.isOpenLinkInNewWindowChecked_ = a.openInNewWindow);
    var b = this.currentLink_.getAnchor();
    this.touchUpAnchorOnOk_(b, a);
    for (var c = this.currentLink_.getExtraAnchors(), d = 0; d < c.length; ++d)c[d].href = b.href, this.touchUpAnchorOnOk_(c[d], a);
    this.currentLink_.placeCursorRightOf();
    this.getFieldObject().focus();
    this.getFieldObject().dispatchSelectionChangeEvent();
    this.getFieldObject().dispatchChange();
    this.eventHandler_.removeAll()
};
goog.editor.plugins.LinkDialogPlugin.prototype.touchUpAnchorOnOk_ = function (a, b) {
    this.showOpenLinkInNewWindow_ && (b.openInNewWindow ? a.target = "_blank" : "_blank" == a.target && (a.target = ""));
    if (this.showRelNoFollow_) {
        var c = goog.ui.editor.LinkDialog.hasNoFollow(a.rel);
        c && !b.noFollow ? a.rel = goog.ui.editor.LinkDialog.removeNoFollow(a.rel) : !c && b.noFollow && (a.rel = a.rel ? a.rel + " nofollow" : "nofollow")
    }
};
goog.editor.plugins.LinkDialogPlugin.prototype.handleCancel_ = function (a) {
    if (this.currentLink_.isNew()) {
        goog.dom.flattenElement(this.currentLink_.getAnchor());
        a = this.currentLink_.getExtraAnchors();
        for (var b = 0; b < a.length; ++b)goog.dom.flattenElement(a[b]);
        this.getFieldObject().dispatchChange()
    }
    this.eventHandler_.removeAll()
};
goog.editor.plugins.LinkDialogPlugin.prototype.handleBeforeTestLink = function (a) {
    if (!this.shouldOpenUrl(a.url)) {
        var b = goog.getMsg("This link cannot be tested.");
        alert(b);
        a.preventDefault()
    }
};
goog.editor.plugins.LinkDialogPlugin.prototype.shouldOpenUrl = function (a) {
    return!this.blockOpeningUnsafeSchemes_ || this.isSafeSchemeToOpen_(a)
};
goog.editor.plugins.LinkDialogPlugin.prototype.isSafeSchemeToOpen_ = function (a) {
    a = goog.uri.utils.getScheme(a) || "http";
    return goog.array.contains(this.safeToOpenSchemes_, a.toLowerCase())
};
goog.editor.plugins.BasicTextFormatter = function () {
    goog.editor.Plugin.call(this)
};
goog.inherits(goog.editor.plugins.BasicTextFormatter, goog.editor.Plugin);
goog.editor.plugins.BasicTextFormatter.prototype.getTrogClassId = function () {
    return"BTF"
};
goog.editor.plugins.BasicTextFormatter.prototype.logger = goog.log.getLogger("goog.editor.plugins.BasicTextFormatter");
goog.editor.plugins.BasicTextFormatter.COMMAND = {LINK: "+link", FORMAT_BLOCK: "+formatBlock", INDENT: "+indent", OUTDENT: "+outdent", STRIKE_THROUGH: "+strikeThrough", HORIZONTAL_RULE: "+insertHorizontalRule", SUBSCRIPT: "+subscript", SUPERSCRIPT: "+superscript", UNDERLINE: "+underline", BOLD: "+bold", ITALIC: "+italic", FONT_SIZE: "+fontSize", FONT_FACE: "+fontName", FONT_COLOR: "+foreColor", BACKGROUND_COLOR: "+backColor", ORDERED_LIST: "+insertOrderedList", UNORDERED_LIST: "+insertUnorderedList", JUSTIFY_CENTER: "+justifyCenter",
    JUSTIFY_FULL: "+justifyFull", JUSTIFY_RIGHT: "+justifyRight", JUSTIFY_LEFT: "+justifyLeft"};
goog.editor.plugins.BasicTextFormatter.SUPPORTED_COMMANDS_ = goog.object.transpose(goog.editor.plugins.BasicTextFormatter.COMMAND);
goog.editor.plugins.BasicTextFormatter.prototype.isSupportedCommand = function (a) {
    return a in goog.editor.plugins.BasicTextFormatter.SUPPORTED_COMMANDS_
};
goog.editor.plugins.BasicTextFormatter.prototype.getRange_ = function () {
    return this.getFieldObject().getRange()
};
goog.editor.plugins.BasicTextFormatter.prototype.getDocument_ = function () {
    return this.getFieldDomHelper().getDocument()
};
goog.editor.plugins.BasicTextFormatter.prototype.execCommandInternal = function (a, b) {
    var c, d, e, f, g, h = b;
    switch (a) {
        case goog.editor.plugins.BasicTextFormatter.COMMAND.BACKGROUND_COLOR:
            goog.isNull(h) || (goog.editor.BrowserFeature.EATS_EMPTY_BACKGROUND_COLOR ? this.applyBgColorManually_(h) : goog.userAgent.OPERA ? this.execCommandHelper_("hiliteColor", h) : this.execCommandHelper_(a, h));
            break;
        case goog.editor.plugins.BasicTextFormatter.COMMAND.LINK:
            g = this.toggleLink_(h);
            break;
        case goog.editor.plugins.BasicTextFormatter.COMMAND.JUSTIFY_CENTER:
        case goog.editor.plugins.BasicTextFormatter.COMMAND.JUSTIFY_FULL:
        case goog.editor.plugins.BasicTextFormatter.COMMAND.JUSTIFY_RIGHT:
        case goog.editor.plugins.BasicTextFormatter.COMMAND.JUSTIFY_LEFT:
            this.justify_(a);
            break;
        default:
            goog.userAgent.IE && (a == goog.editor.plugins.BasicTextFormatter.COMMAND.FORMAT_BLOCK && h) && (h = "<" + h + ">");
            if (a == goog.editor.plugins.BasicTextFormatter.COMMAND.FONT_COLOR && goog.isNull(h))break;
            switch (a) {
                case goog.editor.plugins.BasicTextFormatter.COMMAND.INDENT:
                case goog.editor.plugins.BasicTextFormatter.COMMAND.OUTDENT:
                    goog.editor.BrowserFeature.HAS_STYLE_WITH_CSS && (goog.userAgent.GECKO && (d = !0), goog.userAgent.OPERA && (d = a == goog.editor.plugins.BasicTextFormatter.COMMAND.OUTDENT ? !this.getDocument_().queryCommandEnabled("outdent") :
                        !0));
                case goog.editor.plugins.BasicTextFormatter.COMMAND.ORDERED_LIST:
                case goog.editor.plugins.BasicTextFormatter.COMMAND.UNORDERED_LIST:
                    goog.editor.BrowserFeature.LEAVES_P_WHEN_REMOVING_LISTS && this.queryCommandStateInternal_(this.getDocument_(), a) ? e = this.getFieldObject().queryCommandValue(goog.editor.Command.DEFAULT_TAG) != goog.dom.TagName.P : goog.editor.BrowserFeature.CAN_LISTIFY_BR || this.convertBreaksToDivs_(), goog.userAgent.GECKO && (goog.editor.BrowserFeature.FORGETS_FORMATTING_WHEN_LISTIFYING && !this.queryCommandValue(a)) && (f |= this.beforeInsertListGecko_());
                case goog.editor.plugins.BasicTextFormatter.COMMAND.FORMAT_BLOCK:
                    c = !!this.getFieldObject().getPluginByClassId("Bidi");
                    break;
                case goog.editor.plugins.BasicTextFormatter.COMMAND.SUBSCRIPT:
                case goog.editor.plugins.BasicTextFormatter.COMMAND.SUPERSCRIPT:
                    goog.editor.BrowserFeature.NESTS_SUBSCRIPT_SUPERSCRIPT && this.applySubscriptSuperscriptWorkarounds_(a);
                    break;
                case goog.editor.plugins.BasicTextFormatter.COMMAND.UNDERLINE:
                case goog.editor.plugins.BasicTextFormatter.COMMAND.BOLD:
                case goog.editor.plugins.BasicTextFormatter.COMMAND.ITALIC:
                    d =
                        goog.userAgent.GECKO && goog.editor.BrowserFeature.HAS_STYLE_WITH_CSS && this.queryCommandValue(a);
                    break;
                case goog.editor.plugins.BasicTextFormatter.COMMAND.FONT_COLOR:
                case goog.editor.plugins.BasicTextFormatter.COMMAND.FONT_FACE:
                    d = goog.editor.BrowserFeature.HAS_STYLE_WITH_CSS && goog.userAgent.GECKO
            }
            this.execCommandHelper_(a, h, c, !!d);
            f && this.getDocument_().execCommand("Delete", !1, !0);
            e && this.getDocument_().execCommand("FormatBlock", !1, "<div>")
    }
    goog.userAgent.GECKO && !this.getFieldObject().inModalMode() &&
    this.focusField_();
    return g
};
goog.editor.plugins.BasicTextFormatter.prototype.focusField_ = function () {
    this.getFieldDomHelper().getWindow().focus()
};
goog.editor.plugins.BasicTextFormatter.prototype.queryCommandValue = function (a) {
    var b;
    switch (a) {
        case goog.editor.plugins.BasicTextFormatter.COMMAND.LINK:
            return this.isNodeInState_(goog.dom.TagName.A);
        case goog.editor.plugins.BasicTextFormatter.COMMAND.JUSTIFY_CENTER:
        case goog.editor.plugins.BasicTextFormatter.COMMAND.JUSTIFY_FULL:
        case goog.editor.plugins.BasicTextFormatter.COMMAND.JUSTIFY_RIGHT:
        case goog.editor.plugins.BasicTextFormatter.COMMAND.JUSTIFY_LEFT:
            return this.isJustification_(a);
        case goog.editor.plugins.BasicTextFormatter.COMMAND.FORMAT_BLOCK:
            return goog.editor.plugins.BasicTextFormatter.getSelectionBlockState_(this.getFieldObject().getRange());
        case goog.editor.plugins.BasicTextFormatter.COMMAND.INDENT:
        case goog.editor.plugins.BasicTextFormatter.COMMAND.OUTDENT:
        case goog.editor.plugins.BasicTextFormatter.COMMAND.HORIZONTAL_RULE:
            return!1;
        case goog.editor.plugins.BasicTextFormatter.COMMAND.FONT_SIZE:
        case goog.editor.plugins.BasicTextFormatter.COMMAND.FONT_FACE:
        case goog.editor.plugins.BasicTextFormatter.COMMAND.FONT_COLOR:
        case goog.editor.plugins.BasicTextFormatter.COMMAND.BACKGROUND_COLOR:
            return this.queryCommandValueInternal_(this.getDocument_(),
                a, goog.editor.BrowserFeature.HAS_STYLE_WITH_CSS && goog.userAgent.GECKO);
        case goog.editor.plugins.BasicTextFormatter.COMMAND.UNDERLINE:
        case goog.editor.plugins.BasicTextFormatter.COMMAND.BOLD:
        case goog.editor.plugins.BasicTextFormatter.COMMAND.ITALIC:
            b = goog.editor.BrowserFeature.HAS_STYLE_WITH_CSS && goog.userAgent.GECKO;
        default:
            return this.queryCommandStateInternal_(this.getDocument_(), a, b)
    }
};
goog.editor.plugins.BasicTextFormatter.prototype.prepareContentsHtml = function (a) {
    goog.editor.BrowserFeature.COLLAPSES_EMPTY_NODES && a.match(/^\s*<script/i) && (a = "&nbsp;" + a);
    goog.editor.BrowserFeature.CONVERT_TO_B_AND_I_TAGS && (a = a.replace(/<(\/?)strong([^\w])/gi, "<$1b$2"), a = a.replace(/<(\/?)em([^\w])/gi, "<$1i$2"));
    return a
};
goog.editor.plugins.BasicTextFormatter.prototype.cleanContentsDom = function (a) {
    a = a.getElementsByTagName(goog.dom.TagName.IMG);
    for (var b = 0, c; c = a[b]; b++)goog.editor.BrowserFeature.SHOWS_CUSTOM_ATTRS_IN_INNER_HTML && (c.removeAttribute("tabIndex"), c.removeAttribute("tabIndexSet"), goog.removeUid(c), c.oldTabIndex && (c.tabIndex = c.oldTabIndex))
};
goog.editor.plugins.BasicTextFormatter.prototype.cleanContentsHtml = function (a) {
    if (goog.editor.BrowserFeature.MOVES_STYLE_TO_HEAD) {
        for (var b = this.getFieldObject().getEditableDomHelper().getElementsByTagNameAndClass(goog.dom.TagName.HEAD), c = [], d = b.length, e = 1; e < d; ++e)for (var f = b[e].getElementsByTagName(goog.dom.TagName.STYLE), g = f.length, h = 0; h < g; ++h)c.push(f[h].outerHTML);
        return c.join("") + a
    }
    return a
};
goog.editor.plugins.BasicTextFormatter.prototype.handleKeyboardShortcut = function (a, b, c) {
    if (!c)return!1;
    var d;
    switch (b) {
        case "b":
            d = goog.editor.plugins.BasicTextFormatter.COMMAND.BOLD;
            break;
        case "i":
            d = goog.editor.plugins.BasicTextFormatter.COMMAND.ITALIC;
            break;
        case "u":
            d = goog.editor.plugins.BasicTextFormatter.COMMAND.UNDERLINE;
            break;
        case "s":
            return!0
    }
    return d ? (this.getFieldObject().execCommand(d), !0) : !1
};
goog.editor.plugins.BasicTextFormatter.BR_REGEXP_ = goog.userAgent.IE ? /<br([^\/>]*)\/?>/gi : /<br([^\/>]*)\/?>(?!<\/(div|p)>)/gi;
goog.editor.plugins.BasicTextFormatter.prototype.convertBreaksToDivs_ = function () {
    if (!goog.userAgent.IE && !goog.userAgent.OPERA)return!1;
    var a = this.getRange_(), b = a.getContainerElement(), c = this.getDocument_();
    goog.editor.plugins.BasicTextFormatter.BR_REGEXP_.lastIndex = 0;
    if (goog.editor.plugins.BasicTextFormatter.BR_REGEXP_.test(b.innerHTML)) {
        a = a.saveUsingCarets();
        if (b.tagName == goog.dom.TagName.P)goog.editor.plugins.BasicTextFormatter.convertParagraphToDiv_(b, !0); else {
            var d = b.innerHTML.replace(goog.editor.plugins.BasicTextFormatter.BR_REGEXP_,
                '<p$1 trtempbr="temp_br">');
            goog.editor.node.replaceInnerHtml(b, d);
            b = goog.array.toArray(b.getElementsByTagName(goog.dom.TagName.P));
            goog.iter.forEach(b, function (a) {
                if ("temp_br" == a.getAttribute("trtempbr")) {
                    a.removeAttribute("trtempbr");
                    if (goog.string.isBreakingWhitespace(goog.dom.getTextContent(a))) {
                        var b = goog.userAgent.IE ? c.createTextNode(goog.string.Unicode.NBSP) : c.createElement(goog.dom.TagName.BR);
                        a.appendChild(b)
                    }
                    goog.editor.plugins.BasicTextFormatter.convertParagraphToDiv_(a)
                }
            })
        }
        a.restore();
        return!0
    }
    return!1
};
goog.editor.plugins.BasicTextFormatter.convertParagraphToDiv_ = function (a, b) {
    if (goog.userAgent.IE || goog.userAgent.OPERA) {
        var c = a.outerHTML.replace(/<(\/?)p/gi, "<$1div");
        b && (c = c.replace(goog.editor.plugins.BasicTextFormatter.BR_REGEXP_, "</div><div$1>"));
        goog.userAgent.OPERA && !/<\/div>$/i.test(c) && (c += "</div>");
        a.outerHTML = c
    }
};
goog.editor.plugins.BasicTextFormatter.convertToRealExecCommand_ = function (a) {
    return 0 == a.indexOf("+") ? a.substring(1) : a
};
goog.editor.plugins.BasicTextFormatter.prototype.justify_ = function (a) {
    this.execCommandHelper_(a, null, !1, !0);
    goog.userAgent.GECKO && this.execCommandHelper_(a, null, !1, !0);
    goog.editor.BrowserFeature.HAS_STYLE_WITH_CSS && goog.userAgent.GECKO || goog.iter.forEach(this.getFieldObject().getRange(), goog.editor.plugins.BasicTextFormatter.convertContainerToTextAlign_)
};
goog.editor.plugins.BasicTextFormatter.convertContainerToTextAlign_ = function (a) {
    a = goog.editor.style.getContainer(a);
    a.align && (a.style.textAlign = a.align, a.removeAttribute("align"))
};
goog.editor.plugins.BasicTextFormatter.prototype.execCommandHelper_ = function (a, b, c, d) {
    var e = null;
    c && (e = this.getFieldObject().queryCommandValue(goog.editor.Command.DIR_RTL) ? "rtl" : this.getFieldObject().queryCommandValue(goog.editor.Command.DIR_LTR) ? "ltr" : null);
    a = goog.editor.plugins.BasicTextFormatter.convertToRealExecCommand_(a);
    var f, g;
    goog.userAgent.IE && (g = this.applyExecCommandIEFixes_(a), f = g[0], g = g[1]);
    goog.userAgent.WEBKIT && (f = this.applyExecCommandSafariFixes_(a));
    goog.userAgent.GECKO && this.applyExecCommandGeckoFixes_(a);
    goog.editor.BrowserFeature.DOESNT_OVERRIDE_FONT_SIZE_IN_STYLE_ATTR && "fontsize" == a.toLowerCase() && this.removeFontSizeFromStyleAttrs_();
    c = this.getDocument_();
    d && goog.editor.BrowserFeature.HAS_STYLE_WITH_CSS && (c.execCommand("styleWithCSS", !1, !0), goog.userAgent.OPERA && this.invalidateInlineCss_());
    c.execCommand(a, !1, b);
    d && goog.editor.BrowserFeature.HAS_STYLE_WITH_CSS && c.execCommand("styleWithCSS", !1, !1);
    goog.userAgent.WEBKIT && (!goog.userAgent.isVersionOrHigher("526") && "formatblock" == a.toLowerCase() && b &&
        /^[<]?h\d[>]?$/i.test(b)) && this.cleanUpSafariHeadings_();
    /insert(un)?orderedlist/i.test(a) && (goog.userAgent.WEBKIT && this.fixSafariLists_(), goog.userAgent.IE && (this.fixIELists_(), g && goog.dom.removeNode(g)));
    f && goog.dom.removeNode(f);
    e && this.getFieldObject().execCommand(e)
};
goog.editor.plugins.BasicTextFormatter.prototype.applyBgColorManually_ = function (a) {
    var b = goog.userAgent.GECKO, c = this.getFieldObject().getRange(), d, e;
    c && c.isCollapsed() && (d = this.getFieldDomHelper().createTextNode(b ? " " : ""), e = c.getStartNode(), e = e.nodeType == goog.dom.NodeType.ELEMENT ? e : e.parentNode, "" == e.innerHTML ? (e.style.textIndent = "-10000px", e.appendChild(d)) : (e = this.getFieldDomHelper().createDom("span", {style: "text-indent:-10000px"}, d), c.replaceContentsWithNode(e)), goog.dom.Range.createFromNodeContents(d).select());
    this.execCommandHelper_("hiliteColor", a, !1, !0);
    d && (b && (d.data = ""), e.style.textIndent = "")
};
goog.editor.plugins.BasicTextFormatter.prototype.toggleLink_ = function (a) {
    this.getFieldObject().isSelectionEditable() || this.focusField_();
    var b = this.getRange_(), c = b && b.getContainerElement();
    if ((c = goog.dom.getAncestorByTagNameAndClass(c, goog.dom.TagName.A)) && goog.editor.node.isEditable(c))goog.dom.flattenElement(c); else if (a = this.createLink_(b, "/", a)) {
        if (!this.getFieldObject().execCommand(goog.editor.Command.MODAL_LINK_EDITOR, a))if (b = this.getFieldObject().getAppWindow().prompt(goog.ui.editor.messages.MSG_LINK_TO,
            "http://"))a.setTextAndUrl(a.getCurrentText() || b, b), a.placeCursorRightOf(); else return b = goog.editor.range.saveUsingNormalizedCarets(goog.dom.Range.createFromNodeContents(a.getAnchor())), a.removeLink(), b.restore().select(), null;
        return a
    }
    return null
};
goog.editor.plugins.BasicTextFormatter.prototype.createLink_ = function (a, b, c) {
    var d = null, e = [], f = a && a.getContainerElement();
    if (f && f.tagName == goog.dom.TagName.IMG)return null;
    if (a && a.isCollapsed())a = a.getTextRange(0).getBrowserRangeObject(), goog.editor.BrowserFeature.HAS_W3C_RANGES ? (d = this.getFieldDomHelper().createElement(goog.dom.TagName.A), a.insertNode(d)) : goog.editor.BrowserFeature.HAS_IE_RANGES && (a.pasteHTML("<a id='newLink'></a>"), d = this.getFieldDomHelper().getElement("newLink"), d.removeAttribute("id"));
    else {
        var g = goog.string.createUniqueString();
        this.execCommandHelper_("CreateLink", g);
        goog.array.forEach(this.getFieldObject().getElement().getElementsByTagName(goog.dom.TagName.A), function (a, b, c) {
            goog.string.endsWith(a.href, g) && e.push(a)
        });
        e.length && (d = e.pop())
    }
    return goog.editor.Link.createNewLink(d, b, c, e)
};
goog.editor.plugins.BasicTextFormatter.brokenExecCommandsIE_ = {indent: 1, outdent: 1, insertOrderedList: 1, insertUnorderedList: 1, justifyCenter: 1, justifyFull: 1, justifyRight: 1, justifyLeft: 1, ltr: 1, rtl: 1};
goog.editor.plugins.BasicTextFormatter.blockquoteHatingCommandsIE_ = {insertOrderedList: 1, insertUnorderedList: 1};
goog.editor.plugins.BasicTextFormatter.prototype.applySubscriptSuperscriptWorkarounds_ = function (a) {
    if (!this.queryCommandValue(a)) {
        a = a == goog.editor.plugins.BasicTextFormatter.COMMAND.SUBSCRIPT ? goog.editor.plugins.BasicTextFormatter.COMMAND.SUPERSCRIPT : goog.editor.plugins.BasicTextFormatter.COMMAND.SUBSCRIPT;
        var b = goog.editor.plugins.BasicTextFormatter.convertToRealExecCommand_(a);
        this.queryCommandValue(a) || this.getDocument_().execCommand(b, !1, null);
        this.getDocument_().execCommand(b, !1, null)
    }
};
goog.editor.plugins.BasicTextFormatter.prototype.removeFontSizeFromStyleAttrs_ = function () {
    var a = goog.editor.range.expand(this.getFieldObject().getRange(), this.getFieldObject().getElement());
    goog.iter.forEach(goog.iter.filter(a, function (b, c, d) {
        return d.isStartTag() && a.containsNode(b)
    }), function (a) {
        goog.style.setStyle(a, "font-size", "");
        goog.userAgent.GECKO && (0 == a.style.length && null != a.getAttribute("style")) && a.removeAttribute("style")
    })
};
goog.editor.plugins.BasicTextFormatter.prototype.applyExecCommandIEFixes_ = function (a) {
    var b = [], c = null, d = this.getRange_(), e = this.getFieldDomHelper();
    if (a in goog.editor.plugins.BasicTextFormatter.blockquoteHatingCommandsIE_) {
        var f = d && d.getContainerElement();
        if (f) {
            for (var g = goog.dom.getElementsByTagNameAndClass(goog.dom.TagName.BLOCKQUOTE, null, f), h, k = 0; k < g.length; k++)if (d.containsNode(g[k])) {
                h = g[k];
                break
            }
            if (f = h || goog.dom.getAncestorByTagNameAndClass(f, "BLOCKQUOTE"))c = e.createDom("div", {style: "height:0"}),
                goog.dom.appendChild(f, c), b.push(c), h ? d = goog.dom.Range.createFromNodes(h, 0, c, 0) : d.containsNode(c) && (d = goog.dom.Range.createFromNodes(d.getStartNode(), d.getStartOffset(), c, 0)), d.select()
        }
    }
    h = this.getFieldObject();
    !h.usesIframe() && !c && a in goog.editor.plugins.BasicTextFormatter.brokenExecCommandsIE_ && (a = h.getElement(), d && (d.isCollapsed() && !goog.dom.getFirstElementChild(a)) && (c = d.getTextRange(0).getBrowserRangeObject(), d = c.duplicate(), d.moveToElementText(a), d.collapse(!1), d.isEqual(c) && (d = e.createTextNode(goog.string.Unicode.NBSP),
        a.appendChild(d), c.move("character", 1), c.move("character", -1), c.select(), b.push(d))), c = e.createDom("div", {style: "height:0"}), goog.dom.appendChild(a, c), b.push(c));
    return b
};
goog.editor.plugins.BasicTextFormatter.prototype.cleanUpSafariHeadings_ = function () {
    goog.iter.forEach(this.getRange_(), function (a) {
        "Apple-style-span" == a.className && (a.style.fontSize = "", a.style.fontWeight = "")
    })
};
goog.editor.plugins.BasicTextFormatter.prototype.fixSafariLists_ = function () {
    var a = !1;
    goog.iter.forEach(this.getRange_(), function (b) {
        var c = b.tagName;
        if (c == goog.dom.TagName.UL || c == goog.dom.TagName.OL)if (a) {
            if (c = goog.dom.getPreviousElementSibling(b)) {
                var d = b.ownerDocument.createRange();
                d.setStartAfter(c);
                d.setEndBefore(b);
                if (goog.string.isEmpty(d.toString()) && c.nodeName == b.nodeName) {
                    for (; c.lastChild;)b.insertBefore(c.lastChild, b.firstChild);
                    c.parentNode.removeChild(c)
                }
            }
        } else a = !0
    })
};
goog.editor.plugins.BasicTextFormatter.orderedListTypes_ = {1: 1, a: 1, A: 1, i: 1, I: 1};
goog.editor.plugins.BasicTextFormatter.unorderedListTypes_ = {disc: 1, circle: 1, square: 1};
goog.editor.plugins.BasicTextFormatter.prototype.fixIELists_ = function () {
    for (var a = this.getRange_(), a = a && a.getContainer(); a && a.tagName != goog.dom.TagName.UL && a.tagName != goog.dom.TagName.OL;)a = a.parentNode;
    a && (a = a.parentNode);
    if (a) {
        var b = goog.array.toArray(a.getElementsByTagName(goog.dom.TagName.UL));
        goog.array.extend(b, goog.array.toArray(a.getElementsByTagName(goog.dom.TagName.OL)));
        goog.array.forEach(b, function (a) {
            var b = a.type;
            b && !(a.tagName == goog.dom.TagName.UL ? goog.editor.plugins.BasicTextFormatter.unorderedListTypes_ :
                goog.editor.plugins.BasicTextFormatter.orderedListTypes_)[b] && (a.type = "")
        })
    }
};
goog.editor.plugins.BasicTextFormatter.brokenExecCommandsSafari_ = {justifyCenter: 1, justifyFull: 1, justifyRight: 1, justifyLeft: 1, formatBlock: 1};
goog.editor.plugins.BasicTextFormatter.hangingExecCommandWebkit_ = {insertOrderedList: 1, insertUnorderedList: 1};
goog.editor.plugins.BasicTextFormatter.prototype.applyExecCommandSafariFixes_ = function (a) {
    var b;
    goog.editor.plugins.BasicTextFormatter.brokenExecCommandsSafari_[a] && (b = this.getFieldDomHelper().createDom("div", {style: "height: 0"}, "x"), goog.dom.appendChild(this.getFieldObject().getElement(), b));
    goog.editor.plugins.BasicTextFormatter.hangingExecCommandWebkit_[a] && (a = this.getFieldObject().getElement(), b = this.getFieldDomHelper().createDom("div", {style: "height: 0"}, "x"), a.insertBefore(b, a.firstChild));
    return b
};
goog.editor.plugins.BasicTextFormatter.prototype.applyExecCommandGeckoFixes_ = function (a) {
    if (goog.userAgent.isVersionOrHigher("1.9") && "formatblock" == a.toLowerCase()) {
        a = this.getRange_();
        var b = a.getStartNode();
        if (a.isCollapsed() && b && b.tagName == goog.dom.TagName.BODY) {
            var c = a.getStartOffset();
            (b = b.childNodes[c]) && b.tagName == goog.dom.TagName.BR && (a = a.getBrowserRangeObject(), a.setStart(b, 0), a.setEnd(b, 0))
        }
    }
};
goog.editor.plugins.BasicTextFormatter.prototype.invalidateInlineCss_ = function () {
    var a = [], b = this.getFieldObject().getRange().getContainerElement();
    do a.push(b); while (b = b.parentNode);
    a = goog.iter.chain(goog.iter.toIterator(this.getFieldObject().getRange()), goog.iter.toIterator(a));
    a = goog.iter.filter(a, goog.editor.style.isContainer);
    goog.iter.forEach(a, function (a) {
        var b = a.style.outline;
        a.style.outline = "0px solid red";
        a.style.outline = b
    })
};
goog.editor.plugins.BasicTextFormatter.prototype.beforeInsertListGecko_ = function () {
    var a = this.getFieldObject().queryCommandValue(goog.editor.Command.DEFAULT_TAG);
    if (a == goog.dom.TagName.P || a == goog.dom.TagName.DIV)return!1;
    a = this.getRange_();
    if (a.isCollapsed() && a.getContainer().nodeType != goog.dom.NodeType.TEXT) {
        var b = this.getFieldDomHelper().createTextNode(goog.string.Unicode.NBSP);
        a.insertNode(b, !1);
        goog.dom.Range.createFromNodeContents(b).select();
        return!0
    }
    return!1
};
goog.editor.plugins.BasicTextFormatter.getSelectionBlockState_ = function (a) {
    var b = null;
    goog.iter.forEach(a, function (a, d, e) {
        if (!e.isEndTag()) {
            a = goog.editor.style.getContainer(a).tagName;
            b = b || a;
            if (b != a)throw b = null, goog.iter.StopIteration;
            e.skipTag()
        }
    });
    return b
};
goog.editor.plugins.BasicTextFormatter.SUPPORTED_JUSTIFICATIONS_ = {center: 1, justify: 1, right: 1, left: 1};
goog.editor.plugins.BasicTextFormatter.prototype.isJustification_ = function (a) {
    a = a.replace("+justify", "").toLowerCase();
    "full" == a && (a = "justify");
    var b = this.getFieldObject().getPluginByClassId("Bidi");
    if (b)return a == b.getSelectionAlignment();
    var c = this.getRange_();
    if (!c)return!1;
    for (var d = c.getContainerElement(), b = goog.array.filter(d.childNodes, function (a) {
        return goog.editor.node.isImportant(a) && c.containsNode(a, !0)
    }), b = b.length ? b : [d], d = 0; d < b.length; d++) {
        var e = goog.editor.style.getContainer(b[d]);
        if (a !=
            goog.editor.plugins.BasicTextFormatter.getNodeJustification_(e))return!1
    }
    return!0
};
goog.editor.plugins.BasicTextFormatter.getNodeJustification_ = function (a) {
    var b = goog.style.getComputedTextAlign(a), b = b.replace(/^-(moz|webkit)-/, "");
    goog.editor.plugins.BasicTextFormatter.SUPPORTED_JUSTIFICATIONS_[b] || (b = a.align || "left");
    return b
};
goog.editor.plugins.BasicTextFormatter.prototype.isNodeInState_ = function (a) {
    var b = this.getRange_(), b = b && b.getContainerElement();
    a = goog.dom.getAncestorByTagNameAndClass(b, a);
    return!!a && goog.editor.node.isEditable(a)
};
goog.editor.plugins.BasicTextFormatter.prototype.queryCommandStateInternal_ = function (a, b, c) {
    return this.queryCommandHelper_(!0, a, b, c)
};
goog.editor.plugins.BasicTextFormatter.prototype.queryCommandValueInternal_ = function (a, b, c) {
    return this.queryCommandHelper_(!1, a, b, c)
};
goog.editor.plugins.BasicTextFormatter.prototype.queryCommandHelper_ = function (a, b, c, d) {
    c = goog.editor.plugins.BasicTextFormatter.convertToRealExecCommand_(c);
    if (d) {
        var e = this.getDocument_();
        e.execCommand("styleWithCSS", !1, !0)
    }
    a = a ? b.queryCommandState(c) : b.queryCommandValue(c);
    d && e.execCommand("styleWithCSS", !1, !1);
    return a
};
goog.ui.MenuSeparatorRenderer = function () {
    goog.ui.ControlRenderer.call(this)
};
goog.inherits(goog.ui.MenuSeparatorRenderer, goog.ui.ControlRenderer);
goog.addSingletonGetter(goog.ui.MenuSeparatorRenderer);
goog.ui.MenuSeparatorRenderer.CSS_CLASS = "goog-menuseparator";
goog.ui.MenuSeparatorRenderer.prototype.createDom = function (a) {
    return a.getDomHelper().createDom("div", this.getCssClass())
};
goog.ui.MenuSeparatorRenderer.prototype.decorate = function (a, b) {
    b.id && a.setId(b.id);
    if ("HR" == b.tagName) {
        var c = b;
        b = this.createDom(a);
        goog.dom.insertSiblingBefore(b, c);
        goog.dom.removeNode(c)
    } else goog.dom.classes.add(b, this.getCssClass());
    return b
};
goog.ui.MenuSeparatorRenderer.prototype.setContent = function (a, b) {
};
goog.ui.MenuSeparatorRenderer.prototype.getCssClass = function () {
    return goog.ui.MenuSeparatorRenderer.CSS_CLASS
};
goog.ui.Separator = function (a, b) {
    goog.ui.Control.call(this, null, a || goog.ui.MenuSeparatorRenderer.getInstance(), b);
    this.setSupportedState(goog.ui.Component.State.DISABLED, !1);
    this.setSupportedState(goog.ui.Component.State.HOVER, !1);
    this.setSupportedState(goog.ui.Component.State.ACTIVE, !1);
    this.setSupportedState(goog.ui.Component.State.FOCUSED, !1);
    this.setStateInternal(goog.ui.Component.State.DISABLED)
};
goog.inherits(goog.ui.Separator, goog.ui.Control);
goog.ui.Separator.prototype.enterDocument = function () {
    goog.ui.Separator.superClass_.enterDocument.call(this);
    var a = this.getElement();
    goog.asserts.assert(a, "The DOM element for the separator cannot be null.");
    goog.a11y.aria.setRole(a, "separator")
};
goog.ui.registry.setDecoratorByClassName(goog.ui.MenuSeparatorRenderer.CSS_CLASS, function () {
    return new goog.ui.Separator
});
goog.ui.MenuRenderer = function () {
    goog.ui.ContainerRenderer.call(this)
};
goog.inherits(goog.ui.MenuRenderer, goog.ui.ContainerRenderer);
goog.addSingletonGetter(goog.ui.MenuRenderer);
goog.ui.MenuRenderer.CSS_CLASS = "goog-menu";
goog.ui.MenuRenderer.prototype.getAriaRole = function () {
    return goog.a11y.aria.Role.MENU
};
goog.ui.MenuRenderer.prototype.canDecorate = function (a) {
    return"UL" == a.tagName || goog.ui.MenuRenderer.superClass_.canDecorate.call(this, a)
};
goog.ui.MenuRenderer.prototype.getDecoratorForChild = function (a) {
    return"HR" == a.tagName ? new goog.ui.Separator : goog.ui.MenuRenderer.superClass_.getDecoratorForChild.call(this, a)
};
goog.ui.MenuRenderer.prototype.containsElement = function (a, b) {
    return goog.dom.contains(a.getElement(), b)
};
goog.ui.MenuRenderer.prototype.getCssClass = function () {
    return goog.ui.MenuRenderer.CSS_CLASS
};
goog.ui.MenuRenderer.prototype.initializeDom = function (a) {
    goog.ui.MenuRenderer.superClass_.initializeDom.call(this, a);
    a = a.getElement();
    goog.asserts.assert(a, "The menu DOM element cannot be null.");
    goog.a11y.aria.setState(a, goog.a11y.aria.State.HASPOPUP, "true")
};
goog.ui.MenuSeparator = function (a) {
    goog.ui.Separator.call(this, goog.ui.MenuSeparatorRenderer.getInstance(), a)
};
goog.inherits(goog.ui.MenuSeparator, goog.ui.Separator);
goog.ui.registry.setDecoratorByClassName(goog.ui.MenuSeparatorRenderer.CSS_CLASS, function () {
    return new goog.ui.Separator
});
goog.ui.MenuItemRenderer = function () {
    goog.ui.ControlRenderer.call(this);
    this.classNameCache_ = []
};
goog.inherits(goog.ui.MenuItemRenderer, goog.ui.ControlRenderer);
goog.addSingletonGetter(goog.ui.MenuItemRenderer);
goog.ui.MenuItemRenderer.CSS_CLASS = "goog-menuitem";
goog.ui.MenuItemRenderer.CompositeCssClassIndex_ = {HOVER: 0, CHECKBOX: 1, CONTENT: 2};
goog.ui.MenuItemRenderer.prototype.getCompositeCssClass_ = function (a) {
    var b = this.classNameCache_[a];
    if (!b) {
        switch (a) {
            case goog.ui.MenuItemRenderer.CompositeCssClassIndex_.HOVER:
                b = this.getStructuralCssClass() + "-highlight";
                break;
            case goog.ui.MenuItemRenderer.CompositeCssClassIndex_.CHECKBOX:
                b = this.getStructuralCssClass() + "-checkbox";
                break;
            case goog.ui.MenuItemRenderer.CompositeCssClassIndex_.CONTENT:
                b = this.getStructuralCssClass() + "-content"
        }
        this.classNameCache_[a] = b
    }
    return b
};
goog.ui.MenuItemRenderer.prototype.getAriaRole = function () {
    return goog.a11y.aria.Role.MENU_ITEM
};
goog.ui.MenuItemRenderer.prototype.createDom = function (a) {
    var b = a.getDomHelper().createDom("div", this.getClassNames(a).join(" "), this.createContent(a.getContent(), a.getDomHelper()));
    this.setEnableCheckBoxStructure(a, b, a.isSupportedState(goog.ui.Component.State.SELECTED) || a.isSupportedState(goog.ui.Component.State.CHECKED));
    this.setAriaStates(a, b);
    return b
};
goog.ui.MenuItemRenderer.prototype.getContentElement = function (a) {
    return a && a.firstChild
};
goog.ui.MenuItemRenderer.prototype.decorate = function (a, b) {
    this.hasContentStructure(b) || b.appendChild(this.createContent(b.childNodes, a.getDomHelper()));
    goog.dom.classes.has(b, "goog-option") && (a.setCheckable(!0), this.setCheckable(a, b, !0));
    return goog.ui.MenuItemRenderer.superClass_.decorate.call(this, a, b)
};
goog.ui.MenuItemRenderer.prototype.setContent = function (a, b) {
    var c = this.getContentElement(a), d = this.hasCheckBoxStructure(a) ? c.firstChild : null;
    goog.ui.MenuItemRenderer.superClass_.setContent.call(this, a, b);
    d && !this.hasCheckBoxStructure(a) && c.insertBefore(d, c.firstChild || null)
};
goog.ui.MenuItemRenderer.prototype.hasContentStructure = function (a) {
    a = goog.dom.getFirstElementChild(a);
    var b = this.getCompositeCssClass_(goog.ui.MenuItemRenderer.CompositeCssClassIndex_.CONTENT);
    return!!a && goog.dom.classes.has(a, b)
};
goog.ui.MenuItemRenderer.prototype.createContent = function (a, b) {
    var c = this.getCompositeCssClass_(goog.ui.MenuItemRenderer.CompositeCssClassIndex_.CONTENT);
    return b.createDom("div", c, a)
};
goog.ui.MenuItemRenderer.prototype.setSelectable = function (a, b, c) {
    b && (goog.a11y.aria.setRole(b, c ? goog.a11y.aria.Role.MENU_ITEM_RADIO : this.getAriaRole()), this.setEnableCheckBoxStructure(a, b, c))
};
goog.ui.MenuItemRenderer.prototype.setCheckable = function (a, b, c) {
    b && (goog.a11y.aria.setRole(b, c ? goog.a11y.aria.Role.MENU_ITEM_CHECKBOX : this.getAriaRole()), this.setEnableCheckBoxStructure(a, b, c))
};
goog.ui.MenuItemRenderer.prototype.hasCheckBoxStructure = function (a) {
    if (a = this.getContentElement(a)) {
        a = a.firstChild;
        var b = this.getCompositeCssClass_(goog.ui.MenuItemRenderer.CompositeCssClassIndex_.CHECKBOX);
        return!!a && goog.dom.classes.has(a, b)
    }
    return!1
};
goog.ui.MenuItemRenderer.prototype.setEnableCheckBoxStructure = function (a, b, c) {
    c != this.hasCheckBoxStructure(b) && (goog.dom.classes.enable(b, "goog-option", c), b = this.getContentElement(b), c ? (c = this.getCompositeCssClass_(goog.ui.MenuItemRenderer.CompositeCssClassIndex_.CHECKBOX), b.insertBefore(a.getDomHelper().createDom("div", c), b.firstChild || null)) : b.removeChild(b.firstChild))
};
goog.ui.MenuItemRenderer.prototype.getClassForState = function (a) {
    switch (a) {
        case goog.ui.Component.State.HOVER:
            return this.getCompositeCssClass_(goog.ui.MenuItemRenderer.CompositeCssClassIndex_.HOVER);
        case goog.ui.Component.State.CHECKED:
        case goog.ui.Component.State.SELECTED:
            return"goog-option-selected";
        default:
            return goog.ui.MenuItemRenderer.superClass_.getClassForState.call(this, a)
    }
};
goog.ui.MenuItemRenderer.prototype.getStateFromClass = function (a) {
    var b = this.getCompositeCssClass_(goog.ui.MenuItemRenderer.CompositeCssClassIndex_.HOVER);
    switch (a) {
        case "goog-option-selected":
            return goog.ui.Component.State.CHECKED;
        case b:
            return goog.ui.Component.State.HOVER;
        default:
            return goog.ui.MenuItemRenderer.superClass_.getStateFromClass.call(this, a)
    }
};
goog.ui.MenuItemRenderer.prototype.getCssClass = function () {
    return goog.ui.MenuItemRenderer.CSS_CLASS
};
goog.ui.MenuItem = function (a, b, c, d) {
    goog.ui.Control.call(this, a, d || goog.ui.MenuItemRenderer.getInstance(), c);
    this.setValue(b)
};
goog.inherits(goog.ui.MenuItem, goog.ui.Control);
goog.ui.MenuItem.MNEMONIC_WRAPPER_CLASS_ = "goog-menuitem-mnemonic-separator";
goog.ui.MenuItem.ACCELERATOR_CLASS_ = "goog-menuitem-accel";
goog.ui.MenuItem.prototype.getValue = function () {
    var a = this.getModel();
    return null != a ? a : this.getCaption()
};
goog.ui.MenuItem.prototype.setValue = function (a) {
    this.setModel(a)
};
goog.ui.MenuItem.prototype.setSelectable = function (a) {
    this.setSupportedState(goog.ui.Component.State.SELECTED, a);
    this.isChecked() && !a && this.setChecked(!1);
    var b = this.getElement();
    b && this.getRenderer().setSelectable(this, b, a)
};
goog.ui.MenuItem.prototype.setCheckable = function (a) {
    this.setSupportedState(goog.ui.Component.State.CHECKED, a);
    var b = this.getElement();
    b && this.getRenderer().setCheckable(this, b, a)
};
goog.ui.MenuItem.prototype.getCaption = function () {
    var a = this.getContent();
    if (goog.isArray(a)) {
        var b = goog.ui.MenuItem.ACCELERATOR_CLASS_, c = goog.ui.MenuItem.MNEMONIC_WRAPPER_CLASS_, a = goog.array.map(a,function (a) {
            var e = goog.dom.classes.get(a);
            return goog.array.contains(e, b) || goog.array.contains(e, c) ? "" : goog.dom.getRawTextContent(a)
        }).join("");
        return goog.string.collapseBreakingSpaces(a)
    }
    return goog.ui.MenuItem.superClass_.getCaption.call(this)
};
goog.ui.MenuItem.prototype.handleMouseUp = function (a) {
    var b = this.getParent();
    if (b) {
        var c = b.openingCoords;
        b.openingCoords = null;
        if (c && goog.isNumber(a.clientX) && (b = new goog.math.Coordinate(a.clientX, a.clientY), goog.math.Coordinate.equals(c, b)))return
    }
    goog.ui.MenuItem.superClass_.handleMouseUp.call(this, a)
};
goog.ui.MenuItem.prototype.handleKeyEventInternal = function (a) {
    return a.keyCode == this.getMnemonic() && this.performActionInternal(a) ? !0 : goog.ui.MenuItem.superClass_.handleKeyEventInternal.call(this, a)
};
goog.ui.MenuItem.prototype.setMnemonic = function (a) {
    this.mnemonicKey_ = a
};
goog.ui.MenuItem.prototype.getMnemonic = function () {
    return this.mnemonicKey_
};
goog.ui.registry.setDecoratorByClassName(goog.ui.MenuItemRenderer.CSS_CLASS, function () {
    return new goog.ui.MenuItem(null)
});
goog.ui.MenuHeaderRenderer = function () {
    goog.ui.ControlRenderer.call(this)
};
goog.inherits(goog.ui.MenuHeaderRenderer, goog.ui.ControlRenderer);
goog.addSingletonGetter(goog.ui.MenuHeaderRenderer);
goog.ui.MenuHeaderRenderer.CSS_CLASS = "goog-menuheader";
goog.ui.MenuHeaderRenderer.prototype.getCssClass = function () {
    return goog.ui.MenuHeaderRenderer.CSS_CLASS
};
goog.ui.MenuHeader = function (a, b, c) {
    goog.ui.Control.call(this, a, c || goog.ui.MenuHeaderRenderer.getInstance(), b);
    this.setSupportedState(goog.ui.Component.State.DISABLED, !1);
    this.setSupportedState(goog.ui.Component.State.HOVER, !1);
    this.setSupportedState(goog.ui.Component.State.ACTIVE, !1);
    this.setSupportedState(goog.ui.Component.State.FOCUSED, !1);
    this.setStateInternal(goog.ui.Component.State.DISABLED)
};
goog.inherits(goog.ui.MenuHeader, goog.ui.Control);
goog.ui.registry.setDecoratorByClassName(goog.ui.MenuHeaderRenderer.CSS_CLASS, function () {
    return new goog.ui.MenuHeader(null)
});
goog.ui.Menu = function (a, b) {
    goog.ui.Container.call(this, goog.ui.Container.Orientation.VERTICAL, b || goog.ui.MenuRenderer.getInstance(), a);
    this.setFocusable(!1)
};
goog.inherits(goog.ui.Menu, goog.ui.Container);
goog.ui.Menu.EventType = {BEFORE_SHOW: goog.ui.Component.EventType.BEFORE_SHOW, SHOW: goog.ui.Component.EventType.SHOW, BEFORE_HIDE: goog.ui.Component.EventType.HIDE, HIDE: goog.ui.Component.EventType.HIDE};
goog.ui.Menu.CSS_CLASS = goog.ui.MenuRenderer.CSS_CLASS;
goog.ui.Menu.prototype.allowAutoFocus_ = !0;
goog.ui.Menu.prototype.allowHighlightDisabled_ = !1;
goog.ui.Menu.prototype.getCssClass = function () {
    return this.getRenderer().getCssClass()
};
goog.ui.Menu.prototype.containsElement = function (a) {
    if (this.getRenderer().containsElement(this, a))return!0;
    for (var b = 0, c = this.getChildCount(); b < c; b++) {
        var d = this.getChildAt(b);
        if ("function" == typeof d.containsElement && d.containsElement(a))return!0
    }
    return!1
};
goog.ui.Menu.prototype.addItem = function (a) {
    this.addChild(a, !0)
};
goog.ui.Menu.prototype.addItemAt = function (a, b) {
    this.addChildAt(a, b, !0)
};
goog.ui.Menu.prototype.removeItem = function (a) {
    (a = this.removeChild(a, !0)) && a.dispose()
};
goog.ui.Menu.prototype.removeItemAt = function (a) {
    (a = this.removeChildAt(a, !0)) && a.dispose()
};
goog.ui.Menu.prototype.getItemAt = function (a) {
    return this.getChildAt(a)
};
goog.ui.Menu.prototype.getItemCount = function () {
    return this.getChildCount()
};
goog.ui.Menu.prototype.getItems = function () {
    var a = [];
    this.forEachChild(function (b) {
        a.push(b)
    });
    return a
};
goog.ui.Menu.prototype.setPosition = function (a, b) {
    var c = this.isVisible();
    c || goog.style.setElementShown(this.getElement(), !0);
    goog.style.setPageOffset(this.getElement(), a, b);
    c || goog.style.setElementShown(this.getElement(), !1)
};
goog.ui.Menu.prototype.getPosition = function () {
    return this.isVisible() ? goog.style.getPageOffset(this.getElement()) : null
};
goog.ui.Menu.prototype.setAllowAutoFocus = function (a) {
    (this.allowAutoFocus_ = a) && this.setFocusable(!0)
};
goog.ui.Menu.prototype.getAllowAutoFocus = function () {
    return this.allowAutoFocus_
};
goog.ui.Menu.prototype.setAllowHighlightDisabled = function (a) {
    this.allowHighlightDisabled_ = a
};
goog.ui.Menu.prototype.getAllowHighlightDisabled = function () {
    return this.allowHighlightDisabled_
};
goog.ui.Menu.prototype.setVisible = function (a, b, c) {
    (b = goog.ui.Menu.superClass_.setVisible.call(this, a, b)) && (a && this.isInDocument() && this.allowAutoFocus_) && this.getKeyEventTarget().focus();
    a && c && goog.isNumber(c.clientX) ? this.openingCoords = new goog.math.Coordinate(c.clientX, c.clientY) : this.openingCoords = null;
    return b
};
goog.ui.Menu.prototype.handleEnterItem = function (a) {
    this.allowAutoFocus_ && this.getKeyEventTarget().focus();
    return goog.ui.Menu.superClass_.handleEnterItem.call(this, a)
};
goog.ui.Menu.prototype.highlightNextPrefix = function (a) {
    var b = RegExp("^" + goog.string.regExpEscape(a), "i");
    return this.highlightHelper(function (a, d) {
        var e = 0 > a ? 0 : a, f = !1;
        do {
            ++a;
            a == d && (a = 0, f = !0);
            var g = this.getChildAt(a).getCaption();
            if (g && g.match(b))return a
        } while (!f || a != e);
        return this.getHighlightedIndex()
    }, this.getHighlightedIndex())
};
goog.ui.Menu.prototype.canHighlightItem = function (a) {
    return(this.allowHighlightDisabled_ || a.isEnabled()) && a.isVisible() && a.isSupportedState(goog.ui.Component.State.HOVER)
};
goog.ui.Menu.prototype.decorateInternal = function (a) {
    this.decorateContent(a);
    goog.ui.Menu.superClass_.decorateInternal.call(this, a)
};
goog.ui.Menu.prototype.handleKeyEventInternal = function (a) {
    var b = goog.ui.Menu.superClass_.handleKeyEventInternal.call(this, a);
    b || this.forEachChild(function (c) {
        !b && (c.getMnemonic && c.getMnemonic() == a.keyCode) && (this.isEnabled() && this.setHighlighted(c), b = c.handleKeyEvent(a))
    }, this);
    return b
};
goog.ui.Menu.prototype.setHighlightedIndex = function (a) {
    goog.ui.Menu.superClass_.setHighlightedIndex.call(this, a);
    (a = this.getChildAt(a)) && goog.style.scrollIntoContainerView(a.getElement(), this.getElement())
};
goog.ui.Menu.prototype.decorateContent = function (a) {
    var b = this.getRenderer();
    a = this.getDomHelper().getElementsByTagNameAndClass("div", b.getCssClass() + "-content", a);
    for (var c = a.length, d = 0; d < c; d++)b.decorateChildren(this, a[d])
};
goog.positioning.AbstractPosition = function () {
};
goog.positioning.AbstractPosition.prototype.reposition = function (a, b, c, d) {
};
goog.positioning.AnchoredPosition = function (a, b, c) {
    this.element = a;
    this.corner = b;
    this.overflow_ = c
};
goog.inherits(goog.positioning.AnchoredPosition, goog.positioning.AbstractPosition);
goog.positioning.AnchoredPosition.prototype.reposition = function (a, b, c, d) {
    goog.positioning.positionAtAnchor(this.element, this.corner, a, b, void 0, c, this.overflow_)
};
goog.positioning.AnchoredViewportPosition = function (a, b, c, d) {
    goog.positioning.AnchoredPosition.call(this, a, b);
    this.lastResortOverflow_ = c ? goog.positioning.Overflow.ADJUST_X | goog.positioning.Overflow.ADJUST_Y : goog.positioning.Overflow.IGNORE;
    this.overflowConstraint_ = d || void 0
};
goog.inherits(goog.positioning.AnchoredViewportPosition, goog.positioning.AnchoredPosition);
goog.positioning.AnchoredViewportPosition.prototype.getOverflowConstraint = function () {
    return this.overflowConstraint_
};
goog.positioning.AnchoredViewportPosition.prototype.setOverflowConstraint = function (a) {
    this.overflowConstraint_ = a
};
goog.positioning.AnchoredViewportPosition.prototype.getLastResortOverflow = function () {
    return this.lastResortOverflow_
};
goog.positioning.AnchoredViewportPosition.prototype.setLastResortOverflow = function (a) {
    this.lastResortOverflow_ = a
};
goog.positioning.AnchoredViewportPosition.prototype.reposition = function (a, b, c, d) {
    var e = goog.positioning.positionAtAnchor(this.element, this.corner, a, b, null, c, goog.positioning.Overflow.FAIL_X | goog.positioning.Overflow.FAIL_Y, d, this.overflowConstraint_);
    if (e & goog.positioning.OverflowStatus.FAILED) {
        var f = this.adjustCorner(e, this.corner);
        b = this.adjustCorner(e, b);
        e = goog.positioning.positionAtAnchor(this.element, f, a, b, null, c, goog.positioning.Overflow.FAIL_X | goog.positioning.Overflow.FAIL_Y, d, this.overflowConstraint_);
        e & goog.positioning.OverflowStatus.FAILED && (f = this.adjustCorner(e, f), b = this.adjustCorner(e, b), goog.positioning.positionAtAnchor(this.element, f, a, b, null, c, this.getLastResortOverflow(), d, this.overflowConstraint_))
    }
};
goog.positioning.AnchoredViewportPosition.prototype.adjustCorner = function (a, b) {
    a & goog.positioning.OverflowStatus.FAILED_HORIZONTAL && (b = goog.positioning.flipCornerHorizontal(b));
    a & goog.positioning.OverflowStatus.FAILED_VERTICAL && (b = goog.positioning.flipCornerVertical(b));
    return b
};
goog.positioning.MenuAnchoredPosition = function (a, b, c, d) {
    goog.positioning.AnchoredViewportPosition.call(this, a, b, c || d);
    (c || d) && this.setLastResortOverflow(goog.positioning.Overflow.ADJUST_X_EXCEPT_OFFSCREEN | (d ? goog.positioning.Overflow.RESIZE_HEIGHT : goog.positioning.Overflow.ADJUST_Y_EXCEPT_OFFSCREEN))
};
goog.inherits(goog.positioning.MenuAnchoredPosition, goog.positioning.AnchoredViewportPosition);
goog.ui.MenuButtonRenderer = function () {
    goog.ui.CustomButtonRenderer.call(this)
};
goog.inherits(goog.ui.MenuButtonRenderer, goog.ui.CustomButtonRenderer);
goog.addSingletonGetter(goog.ui.MenuButtonRenderer);
goog.ui.MenuButtonRenderer.CSS_CLASS = "goog-menu-button";
goog.ui.MenuButtonRenderer.WRAPPER_PROP_ = "__goog_wrapper_div";
goog.userAgent.GECKO && (goog.ui.MenuButtonRenderer.prototype.setContent = function (a, b) {
    var c = goog.ui.MenuButtonRenderer.superClass_.getContentElement.call(this, a && a.firstChild);
    c && goog.dom.replaceNode(this.createCaption(b, goog.dom.getDomHelper(a)), c)
});
goog.ui.MenuButtonRenderer.prototype.getContentElement = function (a) {
    a = goog.ui.MenuButtonRenderer.superClass_.getContentElement.call(this, a && a.firstChild);
    goog.userAgent.GECKO && (a && a[goog.ui.MenuButtonRenderer.WRAPPER_PROP_]) && (a = a.firstChild);
    return a
};
goog.ui.MenuButtonRenderer.prototype.updateAriaState = function (a, b, c) {
    goog.asserts.assert(a, "The menu button DOM element cannot be null.");
    goog.asserts.assert(goog.string.isEmpty(goog.a11y.aria.getState(a, goog.a11y.aria.State.EXPANDED)), "Menu buttons do not support the ARIA expanded attribute. Please use ARIA disabled instead." + goog.a11y.aria.getState(a, goog.a11y.aria.State.EXPANDED).length);
    b != goog.ui.Component.State.OPENED && goog.ui.MenuButtonRenderer.superClass_.updateAriaState.call(this, a, b, c)
};
goog.ui.MenuButtonRenderer.prototype.decorate = function (a, b) {
    var c = goog.dom.getElementsByTagNameAndClass("*", goog.ui.MenuRenderer.CSS_CLASS, b)[0];
    if (c) {
        goog.style.setElementShown(c, !1);
        goog.dom.appendChild(goog.dom.getOwnerDocument(c).body, c);
        var d = new goog.ui.Menu;
        d.decorate(c);
        a.setMenu(d)
    }
    return goog.ui.MenuButtonRenderer.superClass_.decorate.call(this, a, b)
};
goog.ui.MenuButtonRenderer.prototype.createButton = function (a, b) {
    return goog.ui.MenuButtonRenderer.superClass_.createButton.call(this, [this.createCaption(a, b), this.createDropdown(b)], b)
};
goog.ui.MenuButtonRenderer.prototype.createCaption = function (a, b) {
    return goog.ui.MenuButtonRenderer.wrapCaption(a, this.getCssClass(), b)
};
goog.ui.MenuButtonRenderer.wrapCaption = function (a, b, c) {
    return c.createDom("div", goog.ui.INLINE_BLOCK_CLASSNAME + " " + (b + "-caption"), a)
};
goog.ui.MenuButtonRenderer.prototype.createDropdown = function (a) {
    return a.createDom("div", goog.ui.INLINE_BLOCK_CLASSNAME + " " + (this.getCssClass() + "-dropdown"), "\u00a0")
};
goog.ui.MenuButtonRenderer.prototype.getCssClass = function () {
    return goog.ui.MenuButtonRenderer.CSS_CLASS
};
goog.ui.MenuButton = function (a, b, c, d) {
    goog.ui.Button.call(this, a, c || goog.ui.MenuButtonRenderer.getInstance(), d);
    this.setSupportedState(goog.ui.Component.State.OPENED, !0);
    this.menuPosition_ = new goog.positioning.MenuAnchoredPosition(null, goog.positioning.Corner.BOTTOM_START);
    b && this.setMenu(b);
    this.menuMargin_ = null;
    this.timer_ = new goog.Timer(500);
    !goog.userAgent.product.IPHONE && !goog.userAgent.product.IPAD || goog.userAgent.isVersionOrHigher("533.17.9") || this.setFocusablePopupMenu(!0)
};
goog.inherits(goog.ui.MenuButton, goog.ui.Button);
goog.ui.MenuButton.prototype.isFocusablePopupMenu_ = !1;
goog.ui.MenuButton.prototype.renderMenuAsSibling_ = !1;
goog.ui.MenuButton.prototype.enterDocument = function () {
    goog.ui.MenuButton.superClass_.enterDocument.call(this);
    this.menu_ && this.attachMenuEventListeners_(this.menu_, !0);
    goog.a11y.aria.setState(this.getElementStrict(), goog.a11y.aria.State.HASPOPUP, !!this.menu_)
};
goog.ui.MenuButton.prototype.exitDocument = function () {
    goog.ui.MenuButton.superClass_.exitDocument.call(this);
    if (this.menu_) {
        this.setOpen(!1);
        this.menu_.exitDocument();
        this.attachMenuEventListeners_(this.menu_, !1);
        var a = this.menu_.getElement();
        a && goog.dom.removeNode(a)
    }
};
goog.ui.MenuButton.prototype.disposeInternal = function () {
    goog.ui.MenuButton.superClass_.disposeInternal.call(this);
    this.menu_ && (this.menu_.dispose(), delete this.menu_);
    delete this.positionElement_;
    this.timer_.dispose()
};
goog.ui.MenuButton.prototype.handleMouseDown = function (a) {
    goog.ui.MenuButton.superClass_.handleMouseDown.call(this, a);
    this.isActive() && (this.setOpen(!this.isOpen(), a), this.menu_ && this.menu_.setMouseButtonPressed(this.isOpen()))
};
goog.ui.MenuButton.prototype.handleMouseUp = function (a) {
    goog.ui.MenuButton.superClass_.handleMouseUp.call(this, a);
    this.menu_ && !this.isActive() && this.menu_.setMouseButtonPressed(!1)
};
goog.ui.MenuButton.prototype.performActionInternal = function (a) {
    this.setActive(!1);
    return!0
};
goog.ui.MenuButton.prototype.handleDocumentMouseDown = function (a) {
    this.menu_ && (this.menu_.isVisible() && !this.containsElement(a.target)) && this.setOpen(!1)
};
goog.ui.MenuButton.prototype.containsElement = function (a) {
    return a && goog.dom.contains(this.getElement(), a) || this.menu_ && this.menu_.containsElement(a) || !1
};
goog.ui.MenuButton.prototype.handleKeyEventInternal = function (a) {
    if (a.keyCode == goog.events.KeyCodes.SPACE) {
        if (a.preventDefault(), a.type != goog.events.EventType.KEYUP)return!0
    } else if (a.type != goog.events.KeyHandler.EventType.KEY)return!1;
    if (this.menu_ && this.menu_.isVisible()) {
        var b = this.menu_.handleKeyEvent(a);
        return a.keyCode == goog.events.KeyCodes.ESC ? (this.setOpen(!1), !0) : b
    }
    return a.keyCode == goog.events.KeyCodes.DOWN || a.keyCode == goog.events.KeyCodes.UP || a.keyCode == goog.events.KeyCodes.SPACE || a.keyCode ==
        goog.events.KeyCodes.ENTER ? (this.setOpen(!0), !0) : !1
};
goog.ui.MenuButton.prototype.handleMenuAction = function (a) {
    this.setOpen(!1)
};
goog.ui.MenuButton.prototype.handleMenuBlur = function (a) {
    this.isActive() || this.setOpen(!1)
};
goog.ui.MenuButton.prototype.handleBlur = function (a) {
    this.isFocusablePopupMenu() || this.setOpen(!1);
    goog.ui.MenuButton.superClass_.handleBlur.call(this, a)
};
goog.ui.MenuButton.prototype.getMenu = function () {
    this.menu_ || this.setMenu(new goog.ui.Menu(this.getDomHelper()));
    return this.menu_ || null
};
goog.ui.MenuButton.prototype.setMenu = function (a) {
    var b = this.menu_;
    a != b && (b && (this.setOpen(!1), this.isInDocument() && this.attachMenuEventListeners_(b, !1), delete this.menu_), this.isInDocument() && goog.a11y.aria.setState(this.getElementStrict(), goog.a11y.aria.State.HASPOPUP, !!a), a && (this.menu_ = a, a.setParent(this), a.setVisible(!1), a.setAllowAutoFocus(this.isFocusablePopupMenu()), this.isInDocument() && this.attachMenuEventListeners_(a, !0)));
    return b
};
goog.ui.MenuButton.prototype.setMenuPosition = function (a) {
    a && (this.menuPosition_ = a, this.positionElement_ = a.element)
};
goog.ui.MenuButton.prototype.setPositionElement = function (a) {
    this.positionElement_ = a;
    this.positionMenu()
};
goog.ui.MenuButton.prototype.setMenuMargin = function (a) {
    this.menuMargin_ = a
};
goog.ui.MenuButton.prototype.addItem = function (a) {
    this.getMenu().addChild(a, !0)
};
goog.ui.MenuButton.prototype.addItemAt = function (a, b) {
    this.getMenu().addChildAt(a, b, !0)
};
goog.ui.MenuButton.prototype.removeItem = function (a) {
    (a = this.getMenu().removeChild(a, !0)) && a.dispose()
};
goog.ui.MenuButton.prototype.removeItemAt = function (a) {
    (a = this.getMenu().removeChildAt(a, !0)) && a.dispose()
};
goog.ui.MenuButton.prototype.getItemAt = function (a) {
    return this.menu_ ? this.menu_.getChildAt(a) : null
};
goog.ui.MenuButton.prototype.getItemCount = function () {
    return this.menu_ ? this.menu_.getChildCount() : 0
};
goog.ui.MenuButton.prototype.setVisible = function (a, b) {
    var c = goog.ui.MenuButton.superClass_.setVisible.call(this, a, b);
    c && !this.isVisible() && this.setOpen(!1);
    return c
};
goog.ui.MenuButton.prototype.setEnabled = function (a) {
    goog.ui.MenuButton.superClass_.setEnabled.call(this, a);
    this.isEnabled() || this.setOpen(!1)
};
goog.ui.MenuButton.prototype.isAlignMenuToStart = function () {
    var a = this.menuPosition_.corner;
    return a == goog.positioning.Corner.BOTTOM_START || a == goog.positioning.Corner.TOP_START
};
goog.ui.MenuButton.prototype.setAlignMenuToStart = function (a) {
    this.menuPosition_.corner = a ? goog.positioning.Corner.BOTTOM_START : goog.positioning.Corner.BOTTOM_END
};
goog.ui.MenuButton.prototype.setScrollOnOverflow = function (a) {
    this.menuPosition_.setLastResortOverflow && this.menuPosition_.setLastResortOverflow(goog.positioning.Overflow.ADJUST_X | (a ? goog.positioning.Overflow.RESIZE_HEIGHT : goog.positioning.Overflow.ADJUST_Y))
};
goog.ui.MenuButton.prototype.isScrollOnOverflow = function () {
    return this.menuPosition_.getLastResortOverflow && !!(this.menuPosition_.getLastResortOverflow() & goog.positioning.Overflow.RESIZE_HEIGHT)
};
goog.ui.MenuButton.prototype.isFocusablePopupMenu = function () {
    return this.isFocusablePopupMenu_
};
goog.ui.MenuButton.prototype.setFocusablePopupMenu = function (a) {
    this.isFocusablePopupMenu_ = a
};
goog.ui.MenuButton.prototype.setRenderMenuAsSibling = function (a) {
    this.renderMenuAsSibling_ = a
};
goog.ui.MenuButton.prototype.showMenu = function () {
    this.setOpen(!0)
};
goog.ui.MenuButton.prototype.hideMenu = function () {
    this.setOpen(!1)
};
goog.ui.MenuButton.prototype.setOpen = function (a, b) {
    goog.ui.MenuButton.superClass_.setOpen.call(this, a);
    if (this.menu_ && this.hasState(goog.ui.Component.State.OPENED) == a) {
        if (a)this.menu_.isInDocument() || (this.renderMenuAsSibling_ ? this.menu_.render(this.getElement().parentNode) : this.menu_.render()), this.viewportBox_ = goog.style.getVisibleRectForElement(this.getElement()), this.buttonRect_ = goog.style.getBounds(this.getElement()), this.positionMenu(), this.menu_.setHighlightedIndex(-1); else {
            this.setActive(!1);
            this.menu_.setMouseButtonPressed(!1);
            var c = this.getElement();
            c && goog.a11y.aria.setState(c, goog.a11y.aria.State.ACTIVEDESCENDANT, "");
            goog.isDefAndNotNull(this.originalSize_) && (this.originalSize_ = void 0, (c = this.menu_.getElement()) && goog.style.setSize(c, "", ""))
        }
        this.menu_.setVisible(a, !1, b);
        this.isDisposed() || this.attachPopupListeners_(a)
    }
};
goog.ui.MenuButton.prototype.invalidateMenuSize = function () {
    this.originalSize_ = void 0
};
goog.ui.MenuButton.prototype.positionMenu = function () {
    if (this.menu_.isInDocument()) {
        var a = this.positionElement_ || this.getElement(), b = this.menuPosition_;
        this.menuPosition_.element = a;
        a = this.menu_.getElement();
        this.menu_.isVisible() || (a.style.visibility = "hidden", goog.style.setElementShown(a, !0));
        !this.originalSize_ && this.isScrollOnOverflow() && (this.originalSize_ = goog.style.getSize(a));
        var c = goog.positioning.flipCornerVertical(b.corner);
        b.reposition(a, c, this.menuMargin_, this.originalSize_);
        this.menu_.isVisible() ||
        (goog.style.setElementShown(a, !1), a.style.visibility = "visible")
    }
};
goog.ui.MenuButton.prototype.onTick_ = function (a) {
    a = goog.style.getBounds(this.getElement());
    var b = goog.style.getVisibleRectForElement(this.getElement());
    goog.math.Rect.equals(this.buttonRect_, a) && goog.math.Box.equals(this.viewportBox_, b) || (this.buttonRect_ = a, this.viewportBox_ = b, this.positionMenu())
};
goog.ui.MenuButton.prototype.attachMenuEventListeners_ = function (a, b) {
    var c = this.getHandler(), d = b ? c.listen : c.unlisten;
    d.call(c, a, goog.ui.Component.EventType.ACTION, this.handleMenuAction);
    d.call(c, a, goog.ui.Component.EventType.HIGHLIGHT, this.handleHighlightItem);
    d.call(c, a, goog.ui.Component.EventType.UNHIGHLIGHT, this.handleUnHighlightItem)
};
goog.ui.MenuButton.prototype.handleHighlightItem = function (a) {
    var b = this.getElement();
    goog.asserts.assert(b, "The menu button DOM element cannot be null.");
    null != a.target.getElement() && goog.a11y.aria.setState(b, goog.a11y.aria.State.ACTIVEDESCENDANT, a.target.getElement().id)
};
goog.ui.MenuButton.prototype.handleUnHighlightItem = function (a) {
    this.menu_.getHighlighted() || (a = this.getElement(), goog.asserts.assert(a, "The menu button DOM element cannot be null."), goog.a11y.aria.setState(a, goog.a11y.aria.State.ACTIVEDESCENDANT, ""))
};
goog.ui.MenuButton.prototype.attachPopupListeners_ = function (a) {
    var b = this.getHandler(), c = a ? b.listen : b.unlisten;
    c.call(b, this.getDomHelper().getDocument(), goog.events.EventType.MOUSEDOWN, this.handleDocumentMouseDown, !0);
    this.isFocusablePopupMenu() && c.call(b, this.menu_, goog.ui.Component.EventType.BLUR, this.handleMenuBlur);
    c.call(b, this.timer_, goog.Timer.TICK, this.onTick_);
    a ? this.timer_.start() : this.timer_.stop()
};
goog.ui.registry.setDecoratorByClassName(goog.ui.MenuButtonRenderer.CSS_CLASS, function () {
    return new goog.ui.MenuButton(null)
});
goog.ui.ToolbarMenuButtonRenderer = function () {
    goog.ui.MenuButtonRenderer.call(this)
};
goog.inherits(goog.ui.ToolbarMenuButtonRenderer, goog.ui.MenuButtonRenderer);
goog.addSingletonGetter(goog.ui.ToolbarMenuButtonRenderer);
goog.ui.ToolbarMenuButtonRenderer.CSS_CLASS = "goog-toolbar-menu-button";
goog.ui.ToolbarMenuButtonRenderer.prototype.getCssClass = function () {
    return goog.ui.ToolbarMenuButtonRenderer.CSS_CLASS
};
goog.ui.ToolbarMenuButton = function (a, b, c, d) {
    goog.ui.MenuButton.call(this, a, b, c || goog.ui.ToolbarMenuButtonRenderer.getInstance(), d)
};
goog.inherits(goog.ui.ToolbarMenuButton, goog.ui.MenuButton);
goog.ui.registry.setDecoratorByClassName(goog.ui.ToolbarMenuButtonRenderer.CSS_CLASS, function () {
    return new goog.ui.ToolbarMenuButton(null)
});
goog.ui.ToolbarSeparatorRenderer = function () {
    goog.ui.MenuSeparatorRenderer.call(this)
};
goog.inherits(goog.ui.ToolbarSeparatorRenderer, goog.ui.MenuSeparatorRenderer);
goog.addSingletonGetter(goog.ui.ToolbarSeparatorRenderer);
goog.ui.ToolbarSeparatorRenderer.CSS_CLASS = "goog-toolbar-separator";
goog.ui.ToolbarSeparatorRenderer.prototype.createDom = function (a) {
    return a.getDomHelper().createDom("div", this.getCssClass() + " " + goog.ui.INLINE_BLOCK_CLASSNAME, "\u00a0")
};
goog.ui.ToolbarSeparatorRenderer.prototype.decorate = function (a, b) {
    b = goog.ui.ToolbarSeparatorRenderer.superClass_.decorate.call(this, a, b);
    goog.dom.classes.add(b, goog.ui.INLINE_BLOCK_CLASSNAME);
    return b
};
goog.ui.ToolbarSeparatorRenderer.prototype.getCssClass = function () {
    return goog.ui.ToolbarSeparatorRenderer.CSS_CLASS
};
goog.ui.ToolbarRenderer = function () {
    goog.ui.ContainerRenderer.call(this)
};
goog.inherits(goog.ui.ToolbarRenderer, goog.ui.ContainerRenderer);
goog.addSingletonGetter(goog.ui.ToolbarRenderer);
goog.ui.ToolbarRenderer.CSS_CLASS = "goog-toolbar";
goog.ui.ToolbarRenderer.prototype.getAriaRole = function () {
    return goog.a11y.aria.Role.TOOLBAR
};
goog.ui.ToolbarRenderer.prototype.getDecoratorForChild = function (a) {
    return"HR" == a.tagName ? new goog.ui.Separator(goog.ui.ToolbarSeparatorRenderer.getInstance()) : goog.ui.ToolbarRenderer.superClass_.getDecoratorForChild.call(this, a)
};
goog.ui.ToolbarRenderer.prototype.getCssClass = function () {
    return goog.ui.ToolbarRenderer.CSS_CLASS
};
goog.ui.ToolbarRenderer.prototype.getDefaultOrientation = function () {
    return goog.ui.Container.Orientation.HORIZONTAL
};
goog.ui.Toolbar = function (a, b, c) {
    goog.ui.Container.call(this, b, a || goog.ui.ToolbarRenderer.getInstance(), c)
};
goog.inherits(goog.ui.Toolbar, goog.ui.Container);
goog.ui.SelectionModel = function (a) {
    goog.events.EventTarget.call(this);
    this.items_ = [];
    this.addItems(a)
};
goog.inherits(goog.ui.SelectionModel, goog.events.EventTarget);
goog.ui.SelectionModel.prototype.selectedItem_ = null;
goog.ui.SelectionModel.prototype.selectionHandler_ = null;
goog.ui.SelectionModel.prototype.getSelectionHandler = function () {
    return this.selectionHandler_
};
goog.ui.SelectionModel.prototype.setSelectionHandler = function (a) {
    this.selectionHandler_ = a
};
goog.ui.SelectionModel.prototype.getItemCount = function () {
    return this.items_.length
};
goog.ui.SelectionModel.prototype.indexOfItem = function (a) {
    return a ? goog.array.indexOf(this.items_, a) : -1
};
goog.ui.SelectionModel.prototype.getFirst = function () {
    return this.items_[0]
};
goog.ui.SelectionModel.prototype.getLast = function () {
    return this.items_[this.items_.length - 1]
};
goog.ui.SelectionModel.prototype.getItemAt = function (a) {
    return this.items_[a] || null
};
goog.ui.SelectionModel.prototype.addItems = function (a) {
    a && (goog.array.forEach(a, function (a) {
        this.selectItem_(a, !1)
    }, this), goog.array.extend(this.items_, a))
};
goog.ui.SelectionModel.prototype.addItem = function (a) {
    this.addItemAt(a, this.getItemCount())
};
goog.ui.SelectionModel.prototype.addItemAt = function (a, b) {
    a && (this.selectItem_(a, !1), goog.array.insertAt(this.items_, a, b))
};
goog.ui.SelectionModel.prototype.removeItem = function (a) {
    a && goog.array.remove(this.items_, a) && a == this.selectedItem_ && (this.selectedItem_ = null, this.dispatchEvent(goog.events.EventType.SELECT))
};
goog.ui.SelectionModel.prototype.removeItemAt = function (a) {
    this.removeItem(this.getItemAt(a))
};
goog.ui.SelectionModel.prototype.getSelectedItem = function () {
    return this.selectedItem_
};
goog.ui.SelectionModel.prototype.getItems = function () {
    return goog.array.clone(this.items_)
};
goog.ui.SelectionModel.prototype.setSelectedItem = function (a) {
    a != this.selectedItem_ && (this.selectItem_(this.selectedItem_, !1), this.selectedItem_ = a, this.selectItem_(a, !0));
    this.dispatchEvent(goog.events.EventType.SELECT)
};
goog.ui.SelectionModel.prototype.getSelectedIndex = function () {
    return this.indexOfItem(this.selectedItem_)
};
goog.ui.SelectionModel.prototype.setSelectedIndex = function (a) {
    this.setSelectedItem(this.getItemAt(a))
};
goog.ui.SelectionModel.prototype.clear = function () {
    goog.array.clear(this.items_);
    this.selectedItem_ = null
};
goog.ui.SelectionModel.prototype.disposeInternal = function () {
    goog.ui.SelectionModel.superClass_.disposeInternal.call(this);
    delete this.items_;
    this.selectedItem_ = null
};
goog.ui.SelectionModel.prototype.selectItem_ = function (a, b) {
    a && ("function" == typeof this.selectionHandler_ ? this.selectionHandler_(a, b) : "function" == typeof a.setSelected && a.setSelected(b))
};
goog.ui.Select = function (a, b, c, d) {
    goog.ui.MenuButton.call(this, a, b, c, d);
    this.setDefaultCaption(a)
};
goog.inherits(goog.ui.Select, goog.ui.MenuButton);
goog.ui.Select.prototype.selectionModel_ = null;
goog.ui.Select.prototype.defaultCaption_ = null;
goog.ui.Select.prototype.enterDocument = function () {
    goog.ui.Select.superClass_.enterDocument.call(this);
    this.updateCaption();
    this.listenToSelectionModelEvents_()
};
goog.ui.Select.prototype.decorateInternal = function (a) {
    goog.ui.Select.superClass_.decorateInternal.call(this, a);
    (a = this.getCaption()) ? this.setDefaultCaption(a) : this.setSelectedIndex(0)
};
goog.ui.Select.prototype.disposeInternal = function () {
    goog.ui.Select.superClass_.disposeInternal.call(this);
    this.selectionModel_ && (this.selectionModel_.dispose(), this.selectionModel_ = null);
    this.defaultCaption_ = null
};
goog.ui.Select.prototype.handleMenuAction = function (a) {
    this.setSelectedItem(a.target);
    goog.ui.Select.superClass_.handleMenuAction.call(this, a);
    a.stopPropagation();
    this.dispatchEvent(goog.ui.Component.EventType.ACTION)
};
goog.ui.Select.prototype.handleSelectionChange = function (a) {
    a = this.getSelectedItem();
    goog.ui.Select.superClass_.setValue.call(this, a && a.getValue());
    this.updateCaption()
};
goog.ui.Select.prototype.setMenu = function (a) {
    var b = goog.ui.Select.superClass_.setMenu.call(this, a);
    a != b && (this.selectionModel_ && this.selectionModel_.clear(), a && (this.selectionModel_ ? a.forEachChild(function (a, b) {
        this.setCorrectAriaRole_(a);
        this.selectionModel_.addItem(a)
    }, this) : this.createSelectionModel_(a)));
    return b
};
goog.ui.Select.prototype.getDefaultCaption = function () {
    return this.defaultCaption_
};
goog.ui.Select.prototype.setDefaultCaption = function (a) {
    this.defaultCaption_ = a;
    this.updateCaption()
};
goog.ui.Select.prototype.addItem = function (a) {
    this.setCorrectAriaRole_(a);
    goog.ui.Select.superClass_.addItem.call(this, a);
    this.selectionModel_ ? this.selectionModel_.addItem(a) : this.createSelectionModel_(this.getMenu())
};
goog.ui.Select.prototype.addItemAt = function (a, b) {
    this.setCorrectAriaRole_(a);
    goog.ui.Select.superClass_.addItemAt.call(this, a, b);
    this.selectionModel_ ? this.selectionModel_.addItemAt(a, b) : this.createSelectionModel_(this.getMenu())
};
goog.ui.Select.prototype.removeItem = function (a) {
    goog.ui.Select.superClass_.removeItem.call(this, a);
    this.selectionModel_ && this.selectionModel_.removeItem(a)
};
goog.ui.Select.prototype.removeItemAt = function (a) {
    goog.ui.Select.superClass_.removeItemAt.call(this, a);
    this.selectionModel_ && this.selectionModel_.removeItemAt(a)
};
goog.ui.Select.prototype.setSelectedItem = function (a) {
    if (this.selectionModel_) {
        var b = this.getSelectedItem();
        this.selectionModel_.setSelectedItem(a);
        a != b && this.dispatchEvent(goog.ui.Component.EventType.CHANGE)
    }
};
goog.ui.Select.prototype.setSelectedIndex = function (a) {
    this.selectionModel_ && this.setSelectedItem(this.selectionModel_.getItemAt(a))
};
goog.ui.Select.prototype.setValue = function (a) {
    if (goog.isDefAndNotNull(a) && this.selectionModel_)for (var b = 0, c; c = this.selectionModel_.getItemAt(b); b++)if (c && "function" == typeof c.getValue && c.getValue() == a) {
        this.setSelectedItem(c);
        return
    }
    this.setSelectedItem(null)
};
goog.ui.Select.prototype.getSelectedItem = function () {
    return this.selectionModel_ ? this.selectionModel_.getSelectedItem() : null
};
goog.ui.Select.prototype.getSelectedIndex = function () {
    return this.selectionModel_ ? this.selectionModel_.getSelectedIndex() : -1
};
goog.ui.Select.prototype.getSelectionModel = function () {
    return this.selectionModel_
};
goog.ui.Select.prototype.createSelectionModel_ = function (a) {
    this.selectionModel_ = new goog.ui.SelectionModel;
    a && a.forEachChild(function (a, c) {
        this.setCorrectAriaRole_(a);
        this.selectionModel_.addItem(a)
    }, this);
    this.listenToSelectionModelEvents_()
};
goog.ui.Select.prototype.listenToSelectionModelEvents_ = function () {
    this.selectionModel_ && this.getHandler().listen(this.selectionModel_, goog.events.EventType.SELECT, this.handleSelectionChange)
};
goog.ui.Select.prototype.updateCaption = function () {
    var a = this.getSelectedItem();
    this.setContent(a ? a.getCaption() : this.defaultCaption_)
};
goog.ui.Select.prototype.setCorrectAriaRole_ = function (a) {
    a.setPreferredAriaRole(a instanceof goog.ui.MenuItem ? goog.a11y.aria.Role.OPTION : goog.a11y.aria.Role.SEPARATOR)
};
goog.ui.Select.prototype.setOpen = function (a, b) {
    goog.ui.Select.superClass_.setOpen.call(this, a, b);
    this.isOpen() && this.getMenu().setHighlightedIndex(this.getSelectedIndex())
};
goog.ui.registry.setDecoratorByClassName("goog-select", function () {
    return new goog.ui.Select(null)
});
goog.ui.ToolbarSelect = function (a, b, c, d) {
    goog.ui.Select.call(this, a, b, c || goog.ui.ToolbarMenuButtonRenderer.getInstance(), d)
};
goog.inherits(goog.ui.ToolbarSelect, goog.ui.Select);
goog.ui.registry.setDecoratorByClassName("goog-toolbar-select", function () {
    return new goog.ui.ToolbarSelect(null)
});
goog.ui.ColorMenuButtonRenderer = function () {
    goog.ui.MenuButtonRenderer.call(this)
};
goog.inherits(goog.ui.ColorMenuButtonRenderer, goog.ui.MenuButtonRenderer);
goog.addSingletonGetter(goog.ui.ColorMenuButtonRenderer);
goog.ui.ColorMenuButtonRenderer.CSS_CLASS = "goog-color-menu-button";
goog.ui.ColorMenuButtonRenderer.prototype.createCaption = function (a, b) {
    return goog.ui.ColorMenuButtonRenderer.superClass_.createCaption.call(this, goog.ui.ColorMenuButtonRenderer.wrapCaption(a, b), b)
};
goog.ui.ColorMenuButtonRenderer.wrapCaption = function (a, b) {
    return b.createDom("div", goog.ui.ColorMenuButtonRenderer.CSS_CLASS + "-indicator", a)
};
goog.ui.ColorMenuButtonRenderer.prototype.setValue = function (a, b) {
    a && goog.ui.ColorMenuButtonRenderer.setCaptionValue(this.getContentElement(a), b)
};
goog.ui.ColorMenuButtonRenderer.setCaptionValue = function (a, b) {
    if (a && a.firstChild) {
        var c;
        c = b && goog.color.isValidColor(b) ? goog.color.parse(b).hex : null;
        a.firstChild.style.borderBottomColor = c || (goog.userAgent.IE ? "" : "transparent")
    }
};
goog.ui.ColorMenuButtonRenderer.prototype.initializeDom = function (a) {
    this.setValue(a.getElement(), a.getValue());
    goog.dom.classes.add(a.getElement(), goog.ui.ColorMenuButtonRenderer.CSS_CLASS);
    goog.ui.ColorMenuButtonRenderer.superClass_.initializeDom.call(this, a)
};
goog.dom.NodeIterator = function (a, b, c, d) {
    goog.dom.TagIterator.call(this, a, b, c, null, d)
};
goog.inherits(goog.dom.NodeIterator, goog.dom.TagIterator);
goog.dom.NodeIterator.prototype.next = function () {
    do goog.dom.NodeIterator.superClass_.next.call(this); while (this.isEndTag());
    return this.node
};
goog.ui.PaletteRenderer = function () {
    goog.ui.ControlRenderer.call(this)
};
goog.inherits(goog.ui.PaletteRenderer, goog.ui.ControlRenderer);
goog.addSingletonGetter(goog.ui.PaletteRenderer);
goog.ui.PaletteRenderer.cellId_ = 0;
goog.ui.PaletteRenderer.CSS_CLASS = "goog-palette";
goog.ui.PaletteRenderer.prototype.createDom = function (a) {
    var b = this.getClassNames(a);
    a = a.getDomHelper().createDom(goog.dom.TagName.DIV, b ? b.join(" ") : null, this.createGrid(a.getContent(), a.getSize(), a.getDomHelper()));
    goog.a11y.aria.setRole(a, goog.a11y.aria.Role.GRID);
    return a
};
goog.ui.PaletteRenderer.prototype.createGrid = function (a, b, c) {
    for (var d = [], e = 0, f = 0; e < b.height; e++) {
        for (var g = [], h = 0; h < b.width; h++) {
            var k = a && a[f++];
            g.push(this.createCell(k, c))
        }
        d.push(this.createRow(g, c))
    }
    return this.createTable(d, c)
};
goog.ui.PaletteRenderer.prototype.createTable = function (a, b) {
    var c = b.createDom(goog.dom.TagName.TABLE, this.getCssClass() + "-table", b.createDom(goog.dom.TagName.TBODY, this.getCssClass() + "-body", a));
    c.cellSpacing = 0;
    c.cellPadding = 0;
    return c
};
goog.ui.PaletteRenderer.prototype.createRow = function (a, b) {
    var c = b.createDom(goog.dom.TagName.TR, this.getCssClass() + "-row", a);
    goog.a11y.aria.setRole(c, goog.a11y.aria.Role.ROW);
    return c
};
goog.ui.PaletteRenderer.prototype.createCell = function (a, b) {
    var c = b.createDom(goog.dom.TagName.TD, {"class": this.getCssClass() + "-cell", id: this.getCssClass() + "-cell-" + goog.ui.PaletteRenderer.cellId_++}, a);
    goog.a11y.aria.setRole(c, goog.a11y.aria.Role.GRIDCELL);
    if (!goog.dom.getTextContent(c) && !goog.a11y.aria.getLabel(c)) {
        var d = this.findAriaLabelForCell_(c);
        d && goog.a11y.aria.setLabel(c, d)
    }
    return c
};
goog.ui.PaletteRenderer.prototype.findAriaLabelForCell_ = function (a) {
    a = new goog.dom.NodeIterator(a);
    for (var b = "", c; !b && (c = goog.iter.nextOrValue(a, null));)c.nodeType == goog.dom.NodeType.ELEMENT && (b = goog.a11y.aria.getLabel(c) || c.title);
    return b
};
goog.ui.PaletteRenderer.prototype.canDecorate = function (a) {
    return!1
};
goog.ui.PaletteRenderer.prototype.decorate = function (a, b) {
    return null
};
goog.ui.PaletteRenderer.prototype.setContent = function (a, b) {
    if (a) {
        var c = goog.dom.getElementsByTagNameAndClass(goog.dom.TagName.TBODY, this.getCssClass() + "-body", a)[0];
        if (c) {
            var d = 0;
            goog.array.forEach(c.rows, function (a) {
                goog.array.forEach(a.cells, function (a) {
                    goog.dom.removeChildren(a);
                    if (b) {
                        var c = b[d++];
                        c && goog.dom.appendChild(a, c)
                    }
                })
            });
            if (d < b.length) {
                for (var e = [], f = goog.dom.getDomHelper(a), g = c.rows[0].cells.length; d < b.length;) {
                    var h = b[d++];
                    e.push(this.createCell(h, f));
                    e.length == g && (h = this.createRow(e,
                        f), goog.dom.appendChild(c, h), e.length = 0)
                }
                if (0 < e.length) {
                    for (; e.length < g;)e.push(this.createCell("", f));
                    h = this.createRow(e, f);
                    goog.dom.appendChild(c, h)
                }
            }
        }
        goog.style.setUnselectable(a, !0, goog.userAgent.GECKO)
    }
};
goog.ui.PaletteRenderer.prototype.getContainingItem = function (a, b) {
    for (var c = a.getElement(); b && b.nodeType == goog.dom.NodeType.ELEMENT && b != c;) {
        if (b.tagName == goog.dom.TagName.TD && goog.dom.classes.has(b, this.getCssClass() + "-cell"))return b.firstChild;
        b = b.parentNode
    }
    return null
};
goog.ui.PaletteRenderer.prototype.highlightCell = function (a, b, c) {
    b && (b = this.getCellForItem(b), goog.dom.classes.enable(b, this.getCssClass() + "-cell-hover", c), goog.a11y.aria.setState(a.getElementStrict(), goog.a11y.aria.State.ACTIVEDESCENDANT, b.id))
};
goog.ui.PaletteRenderer.prototype.getCellForItem = function (a) {
    return a ? a.parentNode : null
};
goog.ui.PaletteRenderer.prototype.selectCell = function (a, b, c) {
    b && goog.dom.classes.enable(b.parentNode, this.getCssClass() + "-cell-selected", c)
};
goog.ui.PaletteRenderer.prototype.getCssClass = function () {
    return goog.ui.PaletteRenderer.CSS_CLASS
};
goog.ui.Palette = function (a, b, c) {
    goog.ui.Control.call(this, a, b || goog.ui.PaletteRenderer.getInstance(), c);
    this.setAutoStates(goog.ui.Component.State.CHECKED | goog.ui.Component.State.SELECTED | goog.ui.Component.State.OPENED, !1);
    this.currentCellControl_ = new goog.ui.Palette.CurrentCell_;
    this.currentCellControl_.setParentEventTarget(this)
};
goog.inherits(goog.ui.Palette, goog.ui.Control);
goog.ui.Palette.EventType = {AFTER_HIGHLIGHT: goog.events.getUniqueId("afterhighlight")};
goog.ui.Palette.prototype.size_ = null;
goog.ui.Palette.prototype.highlightedIndex_ = -1;
goog.ui.Palette.prototype.selectionModel_ = null;
goog.ui.Palette.prototype.disposeInternal = function () {
    goog.ui.Palette.superClass_.disposeInternal.call(this);
    this.selectionModel_ && (this.selectionModel_.dispose(), this.selectionModel_ = null);
    this.size_ = null;
    this.currentCellControl_.dispose()
};
goog.ui.Palette.prototype.setContentInternal = function (a) {
    goog.ui.Palette.superClass_.setContentInternal.call(this, a);
    this.adjustSize_();
    this.selectionModel_ ? (this.selectionModel_.clear(), this.selectionModel_.addItems(a)) : (this.selectionModel_ = new goog.ui.SelectionModel(a), this.selectionModel_.setSelectionHandler(goog.bind(this.selectItem_, this)), this.getHandler().listen(this.selectionModel_, goog.events.EventType.SELECT, this.handleSelectionChange));
    this.highlightedIndex_ = -1
};
goog.ui.Palette.prototype.getCaption = function () {
    return""
};
goog.ui.Palette.prototype.setCaption = function (a) {
};
goog.ui.Palette.prototype.handleMouseOver = function (a) {
    goog.ui.Palette.superClass_.handleMouseOver.call(this, a);
    var b = this.getRenderer().getContainingItem(this, a.target);
    b && a.relatedTarget && goog.dom.contains(b, a.relatedTarget) || b != this.getHighlightedItem() && this.setHighlightedItem(b)
};
goog.ui.Palette.prototype.handleMouseOut = function (a) {
    goog.ui.Palette.superClass_.handleMouseOut.call(this, a);
    var b = this.getRenderer().getContainingItem(this, a.target);
    b && a.relatedTarget && goog.dom.contains(b, a.relatedTarget) || b == this.getHighlightedItem() && this.setHighlightedItem(null)
};
goog.ui.Palette.prototype.handleMouseDown = function (a) {
    goog.ui.Palette.superClass_.handleMouseDown.call(this, a);
    this.isActive() && (a = this.getRenderer().getContainingItem(this, a.target), a != this.getHighlightedItem() && this.setHighlightedItem(a))
};
goog.ui.Palette.prototype.performActionInternal = function (a) {
    var b = this.getHighlightedItem();
    return b ? (this.setSelectedItem(b), goog.ui.Palette.superClass_.performActionInternal.call(this, a)) : !1
};
goog.ui.Palette.prototype.handleKeyEvent = function (a) {
    var b = this.getContent(), b = b ? b.length : 0, c = this.size_.width;
    if (0 == b || !this.isEnabled())return!1;
    if (a.keyCode == goog.events.KeyCodes.ENTER || a.keyCode == goog.events.KeyCodes.SPACE)return this.performActionInternal(a);
    if (a.keyCode == goog.events.KeyCodes.HOME)return this.setHighlightedIndex(0), !0;
    if (a.keyCode == goog.events.KeyCodes.END)return this.setHighlightedIndex(b - 1), !0;
    var d = 0 > this.highlightedIndex_ ? this.getSelectedIndex() : this.highlightedIndex_;
    switch (a.keyCode) {
        case goog.events.KeyCodes.LEFT:
            -1 ==
                d && (d = b);
            if (0 < d)return this.setHighlightedIndex(d - 1), a.preventDefault(), !0;
            break;
        case goog.events.KeyCodes.RIGHT:
            if (d < b - 1)return this.setHighlightedIndex(d + 1), a.preventDefault(), !0;
            break;
        case goog.events.KeyCodes.UP:
            -1 == d && (d = b + c - 1);
            if (d >= c)return this.setHighlightedIndex(d - c), a.preventDefault(), !0;
            break;
        case goog.events.KeyCodes.DOWN:
            if (-1 == d && (d = -c), d < b - c)return this.setHighlightedIndex(d + c), a.preventDefault(), !0
    }
    return!1
};
goog.ui.Palette.prototype.handleSelectionChange = function (a) {
};
goog.ui.Palette.prototype.getSize = function () {
    return this.size_
};
goog.ui.Palette.prototype.setSize = function (a, b) {
    if (this.getElement())throw Error(goog.ui.Component.Error.ALREADY_RENDERED);
    this.size_ = goog.isNumber(a) ? new goog.math.Size(a, b) : a;
    this.adjustSize_()
};
goog.ui.Palette.prototype.getHighlightedIndex = function () {
    return this.highlightedIndex_
};
goog.ui.Palette.prototype.getHighlightedItem = function () {
    var a = this.getContent();
    return a && a[this.highlightedIndex_]
};
goog.ui.Palette.prototype.getHighlightedCellElement_ = function () {
    return this.getRenderer().getCellForItem(this.getHighlightedItem())
};
goog.ui.Palette.prototype.setHighlightedIndex = function (a) {
    a != this.highlightedIndex_ && (this.highlightIndex_(this.highlightedIndex_, !1), this.highlightedIndex_ = a, this.highlightIndex_(a, !0), this.dispatchEvent(goog.ui.Palette.EventType.AFTER_HIGHLIGHT))
};
goog.ui.Palette.prototype.setHighlightedItem = function (a) {
    var b = this.getContent();
    this.setHighlightedIndex(b ? goog.array.indexOf(b, a) : -1)
};
goog.ui.Palette.prototype.getSelectedIndex = function () {
    return this.selectionModel_ ? this.selectionModel_.getSelectedIndex() : -1
};
goog.ui.Palette.prototype.getSelectedItem = function () {
    return this.selectionModel_ ? this.selectionModel_.getSelectedItem() : null
};
goog.ui.Palette.prototype.setSelectedIndex = function (a) {
    this.selectionModel_ && this.selectionModel_.setSelectedIndex(a)
};
goog.ui.Palette.prototype.setSelectedItem = function (a) {
    this.selectionModel_ && this.selectionModel_.setSelectedItem(a)
};
goog.ui.Palette.prototype.highlightIndex_ = function (a, b) {
    if (this.getElement()) {
        var c = this.getContent();
        if (c && 0 <= a && a < c.length) {
            var d = this.getHighlightedCellElement_();
            this.currentCellControl_.getElement() != d && this.currentCellControl_.setElementInternal(d);
            this.currentCellControl_.tryHighlight(b) && this.getRenderer().highlightCell(this, c[a], b)
        }
    }
};
goog.ui.Palette.prototype.selectItem_ = function (a, b) {
    this.getElement() && this.getRenderer().selectCell(this, a, b)
};
goog.ui.Palette.prototype.adjustSize_ = function () {
    var a = this.getContent();
    if (a)if (this.size_ && this.size_.width) {
        if (a = Math.ceil(a.length / this.size_.width), !goog.isNumber(this.size_.height) || this.size_.height < a)this.size_.height = a
    } else a = Math.ceil(Math.sqrt(a.length)), this.size_ = new goog.math.Size(a, a); else this.size_ = new goog.math.Size(0, 0)
};
goog.ui.Palette.CurrentCell_ = function () {
    goog.ui.Control.call(this, null);
    this.setDispatchTransitionEvents(goog.ui.Component.State.HOVER, !0)
};
goog.inherits(goog.ui.Palette.CurrentCell_, goog.ui.Control);
goog.ui.Palette.CurrentCell_.prototype.tryHighlight = function (a) {
    this.setHighlighted(a);
    return this.isHighlighted() == a
};
goog.ui.ColorPalette = function (a, b, c) {
    this.colors_ = a || [];
    goog.ui.Palette.call(this, null, b || goog.ui.PaletteRenderer.getInstance(), c);
    this.setColors(this.colors_)
};
goog.inherits(goog.ui.ColorPalette, goog.ui.Palette);
goog.ui.ColorPalette.prototype.normalizedColors_ = null;
goog.ui.ColorPalette.prototype.labels_ = null;
goog.ui.ColorPalette.prototype.getColors = function () {
    return this.colors_
};
goog.ui.ColorPalette.prototype.setColors = function (a, b) {
    this.colors_ = a;
    this.labels_ = b || null;
    this.normalizedColors_ = null;
    this.setContent(this.createColorNodes())
};
goog.ui.ColorPalette.prototype.getSelectedColor = function () {
    var a = this.getSelectedItem();
    return a ? (a = goog.style.getStyle(a, "background-color"), goog.ui.ColorPalette.parseColor_(a)) : null
};
goog.ui.ColorPalette.prototype.setSelectedColor = function (a) {
    a = goog.ui.ColorPalette.parseColor_(a);
    this.normalizedColors_ || (this.normalizedColors_ = goog.array.map(this.colors_, function (a) {
        return goog.ui.ColorPalette.parseColor_(a)
    }));
    this.setSelectedIndex(a ? goog.array.indexOf(this.normalizedColors_, a) : -1)
};
goog.ui.ColorPalette.prototype.createColorNodes = function () {
    return goog.array.map(this.colors_, function (a, b) {
        var c = this.getDomHelper().createDom("div", {"class": this.getRenderer().getCssClass() + "-colorswatch", style: "background-color:" + a});
        c.title = this.labels_ && this.labels_[b] ? this.labels_[b] : "#" == a.charAt(0) ? "RGB (" + goog.color.hexToRgb(a).join(", ") + ")" : a;
        return c
    }, this)
};
goog.ui.ColorPalette.parseColor_ = function (a) {
    if (a)try {
        return goog.color.parse(a).hex
    } catch (b) {
    }
    return null
};
goog.ui.ColorMenuButton = function (a, b, c, d) {
    goog.ui.MenuButton.call(this, a, b, c || goog.ui.ColorMenuButtonRenderer.getInstance(), d)
};
goog.inherits(goog.ui.ColorMenuButton, goog.ui.MenuButton);
goog.ui.ColorMenuButton.PALETTES = {GRAYSCALE: "#000 #444 #666 #999 #ccc #eee #f3f3f3 #fff".split(" "), SOLID: "#f00 #f90 #ff0 #0f0 #0ff #00f #90f #f0f".split(" "), PASTEL: "#f4cccc #fce5cd #fff2cc #d9ead3 #d0e0e3 #cfe2f3 #d9d2e9 #ead1dc #ea9999 #f9cb9c #ffe599 #b6d7a8 #a2c4c9 #9fc5e8 #b4a7d6 #d5a6bd #e06666 #f6b26b #ffd966 #93c47d #76a5af #6fa8dc #8e7cc3 #c27ba0 #cc0000 #e69138 #f1c232 #6aa84f #45818e #3d85c6 #674ea7 #a64d79 #990000 #b45f06 #bf9000 #38761d #134f5c #0b5394 #351c75 #741b47 #660000 #783f04 #7f6000 #274e13 #0c343d #073763 #20124d #4c1130".split(" ")};
goog.ui.ColorMenuButton.NO_COLOR = "none";
goog.ui.ColorMenuButton.newColorMenu = function (a, b) {
    var c = new goog.ui.Menu(b);
    a && goog.array.forEach(a, function (a) {
        c.addChild(a, !0)
    });
    goog.object.forEach(goog.ui.ColorMenuButton.PALETTES, function (a) {
        a = new goog.ui.ColorPalette(a, null, b);
        a.setSize(8);
        c.addChild(a, !0)
    });
    return c
};
goog.ui.ColorMenuButton.prototype.getSelectedColor = function () {
    return this.getValue()
};
goog.ui.ColorMenuButton.prototype.setSelectedColor = function (a) {
    this.setValue(a)
};
goog.ui.ColorMenuButton.prototype.setValue = function (a) {
    for (var b = 0, c; c = this.getItemAt(b); b++)"function" == typeof c.setSelectedColor && c.setSelectedColor(a);
    goog.ui.ColorMenuButton.superClass_.setValue.call(this, a)
};
goog.ui.ColorMenuButton.prototype.handleMenuAction = function (a) {
    "function" == typeof a.target.getSelectedColor ? this.setValue(a.target.getSelectedColor()) : a.target.getValue() == goog.ui.ColorMenuButton.NO_COLOR && this.setValue(null);
    goog.ui.ColorMenuButton.superClass_.handleMenuAction.call(this, a);
    a.stopPropagation();
    this.dispatchEvent(goog.ui.Component.EventType.ACTION)
};
goog.ui.ColorMenuButton.prototype.setOpen = function (a, b) {
    a && 0 == this.getItemCount() && (this.setMenu(goog.ui.ColorMenuButton.newColorMenu(null, this.getDomHelper())), this.setValue(this.getValue()));
    goog.ui.ColorMenuButton.superClass_.setOpen.call(this, a, b)
};
goog.ui.registry.setDecoratorByClassName(goog.ui.ColorMenuButtonRenderer.CSS_CLASS, function () {
    return new goog.ui.ColorMenuButton(null)
});
goog.ui.ToolbarColorMenuButtonRenderer = function () {
    goog.ui.ToolbarMenuButtonRenderer.call(this)
};
goog.inherits(goog.ui.ToolbarColorMenuButtonRenderer, goog.ui.ToolbarMenuButtonRenderer);
goog.addSingletonGetter(goog.ui.ToolbarColorMenuButtonRenderer);
goog.ui.ToolbarColorMenuButtonRenderer.prototype.createCaption = function (a, b) {
    return goog.ui.MenuButtonRenderer.wrapCaption(goog.ui.ColorMenuButtonRenderer.wrapCaption(a, b), this.getCssClass(), b)
};
goog.ui.ToolbarColorMenuButtonRenderer.prototype.setValue = function (a, b) {
    a && goog.ui.ColorMenuButtonRenderer.setCaptionValue(this.getContentElement(a), b)
};
goog.ui.ToolbarColorMenuButtonRenderer.prototype.initializeDom = function (a) {
    this.setValue(a.getElement(), a.getValue());
    goog.dom.classes.add(a.getElement(), "goog-toolbar-color-menu-button");
    goog.ui.ToolbarColorMenuButtonRenderer.superClass_.initializeDom.call(this, a)
};
goog.ui.ToolbarColorMenuButton = function (a, b, c, d) {
    goog.ui.ColorMenuButton.call(this, a, b, c || goog.ui.ToolbarColorMenuButtonRenderer.getInstance(), d)
};
goog.inherits(goog.ui.ToolbarColorMenuButton, goog.ui.ColorMenuButton);
goog.ui.registry.setDecoratorByClassName("goog-toolbar-color-menu-button", function () {
    return new goog.ui.ToolbarColorMenuButton(null)
});
goog.ui.ToolbarButtonRenderer = function () {
    goog.ui.CustomButtonRenderer.call(this)
};
goog.inherits(goog.ui.ToolbarButtonRenderer, goog.ui.CustomButtonRenderer);
goog.addSingletonGetter(goog.ui.ToolbarButtonRenderer);
goog.ui.ToolbarButtonRenderer.CSS_CLASS = "goog-toolbar-button";
goog.ui.ToolbarButtonRenderer.prototype.getCssClass = function () {
    return goog.ui.ToolbarButtonRenderer.CSS_CLASS
};
goog.ui.ToolbarButton = function (a, b, c) {
    goog.ui.Button.call(this, a, b || goog.ui.ToolbarButtonRenderer.getInstance(), c)
};
goog.inherits(goog.ui.ToolbarButton, goog.ui.Button);
goog.ui.registry.setDecoratorByClassName(goog.ui.ToolbarButtonRenderer.CSS_CLASS, function () {
    return new goog.ui.ToolbarButton(null)
});
goog.ui.Option = function (a, b, c) {
    goog.ui.MenuItem.call(this, a, b, c);
    this.setSelectable(!0)
};
goog.inherits(goog.ui.Option, goog.ui.MenuItem);
goog.ui.Option.prototype.performActionInternal = function (a) {
    return this.dispatchEvent(goog.ui.Component.EventType.ACTION)
};
goog.ui.registry.setDecoratorByClassName("goog-option", function () {
    return new goog.ui.Option(null)
});
goog.ui.editor.ToolbarFactory = {};
goog.ui.editor.ToolbarFactory.getPrimaryFont = function (a) {
    var b = a.indexOf(",");
    a = (-1 != b ? a.substring(0, b) : a).toLowerCase();
    return goog.string.stripQuotes(a, "\"'")
};
goog.ui.editor.ToolbarFactory.addFonts = function (a, b) {
    goog.array.forEach(b, function (b) {
        goog.ui.editor.ToolbarFactory.addFont(a, b.caption, b.value)
    })
};
goog.ui.editor.ToolbarFactory.addFont = function (a, b, c) {
    var d = goog.ui.editor.ToolbarFactory.getPrimaryFont(c);
    b = new goog.ui.Option(b, c, a.getDomHelper());
    b.setId(d);
    a.addItem(b);
    b.getContentElement().style.fontFamily = c
};
goog.ui.editor.ToolbarFactory.addFontSizes = function (a, b) {
    goog.array.forEach(b, function (b) {
        goog.ui.editor.ToolbarFactory.addFontSize(a, b.caption, b.value)
    })
};
goog.ui.editor.ToolbarFactory.addFontSize = function (a, b, c) {
    b = new goog.ui.Option(b, c, a.getDomHelper());
    a.addItem(b);
    a = b.getContentElement();
    a.style.fontSize = goog.ui.editor.ToolbarFactory.getPxFromLegacySize(c) + "px";
    a.firstChild.style.height = "1.1em"
};
goog.ui.editor.ToolbarFactory.getPxFromLegacySize = function (a) {
    return goog.ui.editor.ToolbarFactory.LEGACY_SIZE_TO_PX_MAP_[a] || 10
};
goog.ui.editor.ToolbarFactory.getLegacySizeFromPx = function (a) {
    return goog.array.lastIndexOf(goog.ui.editor.ToolbarFactory.LEGACY_SIZE_TO_PX_MAP_, a)
};
goog.ui.editor.ToolbarFactory.LEGACY_SIZE_TO_PX_MAP_ = [10, 10, 13, 16, 18, 24, 32, 48];
goog.ui.editor.ToolbarFactory.addFormatOptions = function (a, b) {
    goog.array.forEach(b, function (b) {
        goog.ui.editor.ToolbarFactory.addFormatOption(a, b.caption, b.command)
    })
};
goog.ui.editor.ToolbarFactory.addFormatOption = function (a, b, c) {
    var d = a.getDomHelper();
    b = new goog.ui.Option(d.createDom(goog.dom.TagName.DIV, null, b), c, d);
    b.setId(c);
    a.addItem(b)
};
goog.ui.editor.ToolbarFactory.makeToolbar = function (a, b, c) {
    var d = goog.dom.getDomHelper(b), d = new goog.ui.Toolbar(goog.ui.ToolbarRenderer.getInstance(), goog.ui.Container.Orientation.HORIZONTAL, d);
    c = c || goog.style.isRightToLeft(b);
    d.setRightToLeft(c);
    d.setFocusable(!1);
    for (var e = 0, f; f = a[e]; e++)f.setSupportedState(goog.ui.Component.State.FOCUSED, !1), f.setRightToLeft(c), d.addChild(f, !0);
    d.render(b);
    return d
};
goog.ui.editor.ToolbarFactory.makeButton = function (a, b, c, d, e, f) {
    c = new goog.ui.ToolbarButton(goog.ui.editor.ToolbarFactory.createContent_(c, d, f), e, f);
    c.setId(a);
    c.setTooltip(b);
    return c
};
goog.ui.editor.ToolbarFactory.makeToggleButton = function (a, b, c, d, e, f) {
    a = goog.ui.editor.ToolbarFactory.makeButton(a, b, c, d, e, f);
    a.setSupportedState(goog.ui.Component.State.CHECKED, !0);
    return a
};
goog.ui.editor.ToolbarFactory.makeMenuButton = function (a, b, c, d, e, f) {
    c = new goog.ui.ToolbarMenuButton(goog.ui.editor.ToolbarFactory.createContent_(c, d, f), null, e, f);
    c.setId(a);
    c.setTooltip(b);
    return c
};
goog.ui.editor.ToolbarFactory.makeSelectButton = function (a, b, c, d, e, f) {
    e = new goog.ui.ToolbarSelect(null, null, e, f);
    d && goog.array.forEach(d.split(/\s+/), e.addClassName, e);
    e.addClassName("goog-toolbar-select");
    e.setDefaultCaption(c);
    e.setId(a);
    e.setTooltip(b);
    return e
};
goog.ui.editor.ToolbarFactory.makeColorMenuButton = function (a, b, c, d, e, f) {
    c = new goog.ui.ToolbarColorMenuButton(goog.ui.editor.ToolbarFactory.createContent_(c, d, f), null, e, f);
    c.setId(a);
    c.setTooltip(b);
    return c
};
goog.ui.editor.ToolbarFactory.createContent_ = function (a, b, c) {
    a && "" != a || (!goog.userAgent.GECKO || goog.userAgent.isVersionOrHigher("1.9a")) || (a = goog.string.Unicode.NBSP);
    return(c || goog.dom.getDomHelper()).createDom(goog.dom.TagName.DIV, b ? {"class": b} : null, a)
};
goog.ui.editor.DefaultToolbar = {};
goog.ui.editor.DefaultToolbar.MSG_FONT_NORMAL = goog.getMsg("Normal");
goog.ui.editor.DefaultToolbar.MSG_FONT_NORMAL_SERIF = goog.getMsg("Normal / serif");
goog.ui.editor.DefaultToolbar.FONTS_ = [
    {caption: goog.ui.editor.DefaultToolbar.MSG_FONT_NORMAL, value: "arial,sans-serif"},
    {caption: goog.ui.editor.DefaultToolbar.MSG_FONT_NORMAL_SERIF, value: "times new roman,serif"},
    {caption: "Courier New", value: "courier new,monospace"},
    {caption: "Georgia", value: "georgia,serif"},
    {caption: "Trebuchet", value: "trebuchet ms,sans-serif"},
    {caption: "Verdana", value: "verdana,sans-serif"}
];
goog.ui.editor.DefaultToolbar.I18N_FONTS_ = {ja: [
    {caption: "\uff2d\uff33 \uff30\u30b4\u30b7\u30c3\u30af", value: "ms pgothic,sans-serif"},
    {caption: "\uff2d\uff33 \uff30\u660e\u671d", value: "ms pmincho,serif"},
    {caption: "\uff2d\uff33 \u30b4\u30b7\u30c3\u30af", value: "ms gothic,monospace"}
], ko: [
    {caption: "\uad74\ub9bc", value: "gulim,sans-serif"},
    {caption: "\ubc14\ud0d5", value: "batang,serif"},
    {caption: "\uad74\ub9bc\uccb4", value: "gulimche,monospace"}
], "zh-tw": [
    {caption: "\u65b0\u7d30\u660e\u9ad4", value: "pmingliu,serif"},
    {caption: "\u7d30\u660e\u9ad4", value: "mingliu,serif"}
], "zh-cn": [
    {caption: "\u5b8b\u4f53", value: "simsun,serif"},
    {caption: "\u9ed1\u4f53", value: "simhei,sans-serif"},
    {caption: "MS Song", value: "ms song,monospace"}
]};
goog.ui.editor.DefaultToolbar.locale_ = "en-us";
goog.ui.editor.DefaultToolbar.setLocale = function (a) {
    goog.ui.editor.DefaultToolbar.locale_ = a
};
goog.ui.editor.DefaultToolbar.addDefaultFonts = function (a) {
    var b = goog.ui.editor.DefaultToolbar.locale_.replace(/_/, "-").toLowerCase(), c = [];
    b in goog.ui.editor.DefaultToolbar.I18N_FONTS_ && (c = goog.ui.editor.DefaultToolbar.I18N_FONTS_[b]);
    c.length && goog.ui.editor.ToolbarFactory.addFonts(a, c);
    goog.ui.editor.ToolbarFactory.addFonts(a, goog.ui.editor.DefaultToolbar.FONTS_)
};
goog.ui.editor.DefaultToolbar.MSG_FONT_SIZE_SMALL = goog.getMsg("Small");
goog.ui.editor.DefaultToolbar.MSG_FONT_SIZE_NORMAL = goog.getMsg("Normal");
goog.ui.editor.DefaultToolbar.MSG_FONT_SIZE_LARGE = goog.getMsg("Large");
goog.ui.editor.DefaultToolbar.MSG_FONT_SIZE_HUGE = goog.getMsg("Huge");
goog.ui.editor.DefaultToolbar.FONT_SIZES_ = [
    {caption: goog.ui.editor.DefaultToolbar.MSG_FONT_SIZE_SMALL, value: 1},
    {caption: goog.ui.editor.DefaultToolbar.MSG_FONT_SIZE_NORMAL, value: 2},
    {caption: goog.ui.editor.DefaultToolbar.MSG_FONT_SIZE_LARGE, value: 4},
    {caption: goog.ui.editor.DefaultToolbar.MSG_FONT_SIZE_HUGE, value: 6}
];
goog.ui.editor.DefaultToolbar.addDefaultFontSizes = function (a) {
    goog.ui.editor.ToolbarFactory.addFontSizes(a, goog.ui.editor.DefaultToolbar.FONT_SIZES_)
};
goog.ui.editor.DefaultToolbar.MSG_FORMAT_HEADING = goog.getMsg("Heading");
goog.ui.editor.DefaultToolbar.MSG_FORMAT_SUBHEADING = goog.getMsg("Subheading");
goog.ui.editor.DefaultToolbar.MSG_FORMAT_MINOR_HEADING = goog.getMsg("Minor heading");
goog.ui.editor.DefaultToolbar.MSG_FORMAT_NORMAL = goog.getMsg("Normal");
goog.ui.editor.DefaultToolbar.FORMAT_OPTIONS_ = [
    {caption: goog.ui.editor.DefaultToolbar.MSG_FORMAT_HEADING, command: goog.dom.TagName.H2},
    {caption: goog.ui.editor.DefaultToolbar.MSG_FORMAT_SUBHEADING, command: goog.dom.TagName.H3},
    {caption: goog.ui.editor.DefaultToolbar.MSG_FORMAT_MINOR_HEADING, command: goog.dom.TagName.H4},
    {caption: goog.ui.editor.DefaultToolbar.MSG_FORMAT_NORMAL, command: goog.dom.TagName.P}
];
goog.ui.editor.DefaultToolbar.addDefaultFormatOptions = function (a) {
    goog.ui.editor.ToolbarFactory.addFormatOptions(a, goog.ui.editor.DefaultToolbar.FORMAT_OPTIONS_)
};
goog.ui.editor.DefaultToolbar.makeDefaultToolbar = function (a, b) {
    var c = b || goog.style.isRightToLeft(a) ? goog.ui.editor.DefaultToolbar.DEFAULT_BUTTONS_RTL : goog.ui.editor.DefaultToolbar.DEFAULT_BUTTONS;
    return goog.ui.editor.DefaultToolbar.makeToolbar(c, a, b)
};
goog.ui.editor.DefaultToolbar.makeToolbar = function (a, b, c) {
    for (var d = goog.dom.getDomHelper(b), e = [], f = 0, g; g = a[f]; f++)goog.isString(g) && (g = goog.ui.editor.DefaultToolbar.makeBuiltInToolbarButton(g, d)), g && e.push(g);
    return goog.ui.editor.ToolbarFactory.makeToolbar(e, b, c)
};
goog.ui.editor.DefaultToolbar.makeBuiltInToolbarButton = function (a, b) {
    var c, d = goog.ui.editor.DefaultToolbar.buttons_[a];
    if (d) {
        c = d.factory || goog.ui.editor.ToolbarFactory.makeToggleButton;
        var e = d.command, f = d.tooltip, g = d.caption, h = d.classes, k = b || goog.dom.getDomHelper();
        c = c(e, f, g, h, null, k);
        d.queryable && (c.queryable = !0)
    }
    return c
};
goog.ui.editor.DefaultToolbar.DEFAULT_BUTTONS = [goog.editor.Command.IMAGE, goog.editor.Command.LINK, goog.editor.Command.BOLD, goog.editor.Command.ITALIC, goog.editor.Command.UNORDERED_LIST, goog.editor.Command.FONT_COLOR, goog.editor.Command.FONT_FACE, goog.editor.Command.FONT_SIZE, goog.editor.Command.JUSTIFY_LEFT, goog.editor.Command.JUSTIFY_CENTER, goog.editor.Command.JUSTIFY_RIGHT, goog.editor.Command.EDIT_HTML];
goog.ui.editor.DefaultToolbar.DEFAULT_BUTTONS_RTL = [goog.editor.Command.IMAGE, goog.editor.Command.LINK, goog.editor.Command.BOLD, goog.editor.Command.ITALIC, goog.editor.Command.UNORDERED_LIST, goog.editor.Command.FONT_COLOR, goog.editor.Command.FONT_FACE, goog.editor.Command.FONT_SIZE, goog.editor.Command.JUSTIFY_RIGHT, goog.editor.Command.JUSTIFY_CENTER, goog.editor.Command.JUSTIFY_LEFT, goog.editor.Command.DIR_RTL, goog.editor.Command.DIR_LTR, goog.editor.Command.EDIT_HTML];
goog.ui.editor.DefaultToolbar.rtlButtonFactory_ = function (a, b, c, d, e, f) {
    var g = goog.ui.editor.ToolbarFactory.makeToggleButton(a, b, c, d, e, f);
    g.updateFromValue = function (a) {
        a = !!a;
        goog.dom.classes.enable(g.getParent().getElement(), "tr-rtl-mode", a);
        g.setChecked(a)
    };
    return g
};
goog.ui.editor.DefaultToolbar.undoRedoButtonFactory_ = function (a, b, c, d, e, f) {
    var g = goog.ui.editor.ToolbarFactory.makeButton(a, b, c, d, e, f);
    g.updateFromValue = function (a) {
        g.setEnabled(a)
    };
    return g
};
goog.ui.editor.DefaultToolbar.fontFaceFactory_ = function (a, b, c, d, e, f) {
    var g = goog.ui.editor.ToolbarFactory.makeSelectButton(a, b, c, d, e, f);
    goog.ui.editor.DefaultToolbar.addDefaultFonts(g);
    g.setDefaultCaption(goog.ui.editor.DefaultToolbar.MSG_FONT_NORMAL);
    goog.dom.classes.add(g.getMenu().getContentElement(), "goog-menu-noaccel");
    g.updateFromValue = function (a) {
        var b = null;
        a && 0 < a.length && (b = g.getMenu().getChild(goog.ui.editor.ToolbarFactory.getPrimaryFont(a)));
        a = g.getSelectedItem();
        b != a && g.setSelectedItem(b)
    };
    return g
};
goog.ui.editor.DefaultToolbar.fontSizeFactory_ = function (a, b, c, d, e, f) {
    var g = goog.ui.editor.ToolbarFactory.makeSelectButton(a, b, c, d, e, f);
    goog.ui.editor.DefaultToolbar.addDefaultFontSizes(g);
    g.setDefaultCaption(goog.ui.editor.DefaultToolbar.MSG_FONT_SIZE_NORMAL);
    goog.dom.classes.add(g.getMenu().getContentElement(), "goog-menu-noaccel");
    g.updateFromValue = function (a) {
        goog.isString(a) && "px" == goog.style.getLengthUnits(a) && (a = goog.ui.editor.ToolbarFactory.getLegacySizeFromPx(parseInt(a, 10)));
        a = 0 < a ? a : null;
        a != g.getValue() && g.setValue(a)
    };
    return g
};
goog.ui.editor.DefaultToolbar.colorUpdateFromValue_ = function (a, b) {
    var c = b;
    try {
        if (goog.userAgent.IE)var d = "000000" + c.toString(16), e = d.substr(d.length - 6, 6), c = "#" + e.substring(4, 6) + e.substring(2, 4) + e.substring(0, 2);
        c != a.getValue() && a.setValue(c)
    } catch (f) {
    }
};
goog.ui.editor.DefaultToolbar.fontColorFactory_ = function (a, b, c, d, e, f) {
    a = goog.ui.editor.ToolbarFactory.makeColorMenuButton(a, b, c, d, e, f);
    a.setSelectedColor("#000");
    a.updateFromValue = goog.partial(goog.ui.editor.DefaultToolbar.colorUpdateFromValue_, a);
    return a
};
goog.ui.editor.DefaultToolbar.backgroundColorFactory_ = function (a, b, c, d, e, f) {
    a = goog.ui.editor.ToolbarFactory.makeColorMenuButton(a, b, c, d, e, f);
    a.setSelectedColor("#FFF");
    a.updateFromValue = goog.partial(goog.ui.editor.DefaultToolbar.colorUpdateFromValue_, a);
    return a
};
goog.ui.editor.DefaultToolbar.formatBlockFactory_ = function (a, b, c, d, e, f) {
    var g = goog.ui.editor.ToolbarFactory.makeSelectButton(a, b, c, d, e, f);
    goog.ui.editor.DefaultToolbar.addDefaultFormatOptions(g);
    g.setDefaultCaption(goog.ui.editor.DefaultToolbar.MSG_FORMAT_NORMAL);
    goog.dom.classes.add(g.getMenu().getContentElement(), "goog-menu-noaccel");
    g.updateFromValue = function (a) {
        a = a && 0 < a.length ? a : null;
        a != g.getValue() && g.setValue(a)
    };
    return g
};
goog.ui.editor.DefaultToolbar.MSG_FORMAT_BLOCK_TITLE = goog.getMsg("Format");
goog.ui.editor.DefaultToolbar.MSG_FORMAT_BLOCK_CAPTION = goog.getMsg("Format");
goog.ui.editor.DefaultToolbar.MSG_UNDO_TITLE = goog.getMsg("Undo");
goog.ui.editor.DefaultToolbar.MSG_REDO_TITLE = goog.getMsg("Redo");
goog.ui.editor.DefaultToolbar.MSG_FONT_FACE_TITLE = goog.getMsg("Font");
goog.ui.editor.DefaultToolbar.MSG_FONT_SIZE_TITLE = goog.getMsg("Font size");
goog.ui.editor.DefaultToolbar.MSG_FONT_COLOR_TITLE = goog.getMsg("Text color");
goog.ui.editor.DefaultToolbar.MSG_BOLD_TITLE = goog.getMsg("Bold");
goog.ui.editor.DefaultToolbar.MSG_ITALIC_TITLE = goog.getMsg("Italic");
goog.ui.editor.DefaultToolbar.MSG_UNDERLINE_TITLE = goog.getMsg("Underline");
goog.ui.editor.DefaultToolbar.MSG_BACKGROUND_COLOR_TITLE = goog.getMsg("Text background color");
goog.ui.editor.DefaultToolbar.MSG_LINK_TITLE = goog.getMsg("Add or remove link");
goog.ui.editor.DefaultToolbar.MSG_ORDERED_LIST_TITLE = goog.getMsg("Numbered list");
goog.ui.editor.DefaultToolbar.MSG_UNORDERED_LIST_TITLE = goog.getMsg("Bullet list");
goog.ui.editor.DefaultToolbar.MSG_OUTDENT_TITLE = goog.getMsg("Decrease indent");
goog.ui.editor.DefaultToolbar.MSG_INDENT_TITLE = goog.getMsg("Increase indent");
goog.ui.editor.DefaultToolbar.MSG_ALIGN_LEFT_TITLE = goog.getMsg("Align left");
goog.ui.editor.DefaultToolbar.MSG_ALIGN_CENTER_TITLE = goog.getMsg("Align center");
goog.ui.editor.DefaultToolbar.MSG_ALIGN_RIGHT_TITLE = goog.getMsg("Align right");
goog.ui.editor.DefaultToolbar.MSG_JUSTIFY_TITLE = goog.getMsg("Justify");
goog.ui.editor.DefaultToolbar.MSG_REMOVE_FORMAT_TITLE = goog.getMsg("Remove formatting");
goog.ui.editor.DefaultToolbar.MSG_IMAGE_TITLE = goog.getMsg("Insert image");
goog.ui.editor.DefaultToolbar.MSG_STRIKE_THROUGH_TITLE = goog.getMsg("Strikethrough");
goog.ui.editor.DefaultToolbar.MSG_DIR_LTR_TITLE = goog.getMsg("Left-to-right");
goog.ui.editor.DefaultToolbar.MSG_DIR_RTL_TITLE = goog.getMsg("Right-to-left");
goog.ui.editor.DefaultToolbar.MSG_BLOCKQUOTE_TITLE = goog.getMsg("Quote");
goog.ui.editor.DefaultToolbar.MSG_EDIT_HTML_TITLE = goog.getMsg("Edit HTML source");
goog.ui.editor.DefaultToolbar.MSG_SUBSCRIPT = goog.getMsg("Subscript");
goog.ui.editor.DefaultToolbar.MSG_SUPERSCRIPT = goog.getMsg("Superscript");
goog.ui.editor.DefaultToolbar.MSG_EDIT_HTML_CAPTION = goog.getMsg("Edit HTML");
goog.ui.editor.DefaultToolbar.buttons_ = {};
goog.ui.editor.DefaultToolbar.BUTTONS_ = [
    {command: goog.editor.Command.UNDO, tooltip: goog.ui.editor.DefaultToolbar.MSG_UNDO_TITLE, classes: "tr-icon tr-undo", factory: goog.ui.editor.DefaultToolbar.undoRedoButtonFactory_, queryable: !0},
    {command: goog.editor.Command.REDO, tooltip: goog.ui.editor.DefaultToolbar.MSG_REDO_TITLE, classes: "tr-icon tr-redo", factory: goog.ui.editor.DefaultToolbar.undoRedoButtonFactory_, queryable: !0},
    {command: goog.editor.Command.FONT_FACE, tooltip: goog.ui.editor.DefaultToolbar.MSG_FONT_FACE_TITLE,
        classes: "tr-fontName", factory: goog.ui.editor.DefaultToolbar.fontFaceFactory_, queryable: !0},
    {command: goog.editor.Command.FONT_SIZE, tooltip: goog.ui.editor.DefaultToolbar.MSG_FONT_SIZE_TITLE, classes: "tr-fontSize", factory: goog.ui.editor.DefaultToolbar.fontSizeFactory_, queryable: !0},
    {command: goog.editor.Command.BOLD, tooltip: goog.ui.editor.DefaultToolbar.MSG_BOLD_TITLE, classes: "tr-icon tr-bold", queryable: !0},
    {command: goog.editor.Command.ITALIC, tooltip: goog.ui.editor.DefaultToolbar.MSG_ITALIC_TITLE, classes: "tr-icon tr-italic",
        queryable: !0},
    {command: goog.editor.Command.UNDERLINE, tooltip: goog.ui.editor.DefaultToolbar.MSG_UNDERLINE_TITLE, classes: "tr-icon tr-underline", queryable: !0},
    {command: goog.editor.Command.FONT_COLOR, tooltip: goog.ui.editor.DefaultToolbar.MSG_FONT_COLOR_TITLE, classes: "tr-icon tr-foreColor", factory: goog.ui.editor.DefaultToolbar.fontColorFactory_, queryable: !0},
    {command: goog.editor.Command.BACKGROUND_COLOR, tooltip: goog.ui.editor.DefaultToolbar.MSG_BACKGROUND_COLOR_TITLE, classes: "tr-icon tr-backColor", factory: goog.ui.editor.DefaultToolbar.backgroundColorFactory_,
        queryable: !0},
    {command: goog.editor.Command.LINK, tooltip: goog.ui.editor.DefaultToolbar.MSG_LINK_TITLE, caption: goog.ui.editor.messages.MSG_LINK_CAPTION, classes: "tr-link", queryable: !0},
    {command: goog.editor.Command.ORDERED_LIST, tooltip: goog.ui.editor.DefaultToolbar.MSG_ORDERED_LIST_TITLE, classes: "tr-icon tr-insertOrderedList", queryable: !0},
    {command: goog.editor.Command.UNORDERED_LIST, tooltip: goog.ui.editor.DefaultToolbar.MSG_UNORDERED_LIST_TITLE, classes: "tr-icon tr-insertUnorderedList", queryable: !0},
    {command: goog.editor.Command.OUTDENT,
        tooltip: goog.ui.editor.DefaultToolbar.MSG_OUTDENT_TITLE, classes: "tr-icon tr-outdent", factory: goog.ui.editor.ToolbarFactory.makeButton},
    {command: goog.editor.Command.INDENT, tooltip: goog.ui.editor.DefaultToolbar.MSG_INDENT_TITLE, classes: "tr-icon tr-indent", factory: goog.ui.editor.ToolbarFactory.makeButton},
    {command: goog.editor.Command.JUSTIFY_LEFT, tooltip: goog.ui.editor.DefaultToolbar.MSG_ALIGN_LEFT_TITLE, classes: "tr-icon tr-justifyLeft", queryable: !0},
    {command: goog.editor.Command.JUSTIFY_CENTER, tooltip: goog.ui.editor.DefaultToolbar.MSG_ALIGN_CENTER_TITLE,
        classes: "tr-icon tr-justifyCenter", queryable: !0},
    {command: goog.editor.Command.JUSTIFY_RIGHT, tooltip: goog.ui.editor.DefaultToolbar.MSG_ALIGN_RIGHT_TITLE, classes: "tr-icon tr-justifyRight", queryable: !0},
    {command: goog.editor.Command.JUSTIFY_FULL, tooltip: goog.ui.editor.DefaultToolbar.MSG_JUSTIFY_TITLE, classes: "tr-icon tr-justifyFull", queryable: !0},
    {command: goog.editor.Command.REMOVE_FORMAT, tooltip: goog.ui.editor.DefaultToolbar.MSG_REMOVE_FORMAT_TITLE, classes: "tr-icon tr-removeFormat", factory: goog.ui.editor.ToolbarFactory.makeButton},
    {command: goog.editor.Command.IMAGE, tooltip: goog.ui.editor.DefaultToolbar.MSG_IMAGE_TITLE, classes: "tr-icon tr-image", factory: goog.ui.editor.ToolbarFactory.makeButton},
    {command: goog.editor.Command.STRIKE_THROUGH, tooltip: goog.ui.editor.DefaultToolbar.MSG_STRIKE_THROUGH_TITLE, classes: "tr-icon tr-strikeThrough", queryable: !0},
    {command: goog.editor.Command.SUBSCRIPT, tooltip: goog.ui.editor.DefaultToolbar.MSG_SUBSCRIPT, classes: "tr-icon tr-subscript", queryable: !0},
    {command: goog.editor.Command.SUPERSCRIPT, tooltip: goog.ui.editor.DefaultToolbar.MSG_SUPERSCRIPT,
        classes: "tr-icon tr-superscript", queryable: !0},
    {command: goog.editor.Command.DIR_LTR, tooltip: goog.ui.editor.DefaultToolbar.MSG_DIR_LTR_TITLE, classes: "tr-icon tr-ltr", queryable: !0},
    {command: goog.editor.Command.DIR_RTL, tooltip: goog.ui.editor.DefaultToolbar.MSG_DIR_RTL_TITLE, classes: "tr-icon tr-rtl", factory: goog.ui.editor.DefaultToolbar.rtlButtonFactory_, queryable: !0},
    {command: goog.editor.Command.BLOCKQUOTE, tooltip: goog.ui.editor.DefaultToolbar.MSG_BLOCKQUOTE_TITLE, classes: "tr-icon tr-BLOCKQUOTE", queryable: !0},
    {command: goog.editor.Command.FORMAT_BLOCK, tooltip: goog.ui.editor.DefaultToolbar.MSG_FORMAT_BLOCK_TITLE, caption: goog.ui.editor.DefaultToolbar.MSG_FORMAT_BLOCK_CAPTION, classes: "tr-formatBlock", factory: goog.ui.editor.DefaultToolbar.formatBlockFactory_, queryable: !0},
    {command: goog.editor.Command.EDIT_HTML, tooltip: goog.ui.editor.DefaultToolbar.MSG_EDIT_HTML_TITLE, caption: goog.ui.editor.DefaultToolbar.MSG_EDIT_HTML_CAPTION, classes: "tr-editHtml", factory: goog.ui.editor.ToolbarFactory.makeButton}
];
(function () {
    for (var a = 0, b; b = goog.ui.editor.DefaultToolbar.BUTTONS_[a]; a++)goog.ui.editor.DefaultToolbar.buttons_[b.command] = b;
    delete goog.ui.editor.DefaultToolbar.BUTTONS_
})();
goog.ui.editor.ToolbarController = function (a, b) {
    goog.events.EventTarget.call(this);
    this.handler_ = new goog.events.EventHandler(this);
    this.field_ = a;
    this.toolbar_ = b;
    this.queryCommands_ = [];
    this.toolbar_.forEachChild(function (a) {
        a.queryable && this.queryCommands_.push(this.getComponentId(a.getId()))
    }, this);
    this.toolbar_.setFocusable(!1);
    this.handler_.listen(this.field_, goog.editor.Field.EventType.COMMAND_VALUE_CHANGE, this.updateToolbar).listen(this.toolbar_, goog.ui.Component.EventType.ACTION, this.handleAction)
};
goog.inherits(goog.ui.editor.ToolbarController, goog.events.EventTarget);
goog.ui.editor.ToolbarController.prototype.getComponentId = function (a) {
    return a
};
goog.ui.editor.ToolbarController.prototype.getCommand = function (a) {
    return a
};
goog.ui.editor.ToolbarController.prototype.getHandler = function () {
    return this.handler_
};
goog.ui.editor.ToolbarController.prototype.getField = function () {
    return this.field_
};
goog.ui.editor.ToolbarController.prototype.getToolbar = function () {
    return this.toolbar_
};
goog.ui.editor.ToolbarController.prototype.isVisible = function () {
    return this.toolbar_.isVisible()
};
goog.ui.editor.ToolbarController.prototype.setVisible = function (a) {
    this.toolbar_.setVisible(a)
};
goog.ui.editor.ToolbarController.prototype.isEnabled = function () {
    return this.toolbar_.isEnabled()
};
goog.ui.editor.ToolbarController.prototype.setEnabled = function (a) {
    this.toolbar_.setEnabled(a)
};
goog.ui.editor.ToolbarController.prototype.blur = function () {
    this.toolbar_.handleBlur(null)
};
goog.ui.editor.ToolbarController.prototype.disposeInternal = function () {
    goog.ui.editor.ToolbarController.superClass_.disposeInternal.call(this);
    this.handler_ && (this.handler_.dispose(), delete this.handler_);
    this.toolbar_ && (this.toolbar_.dispose(), delete this.toolbar_);
    delete this.field_;
    delete this.queryCommands_
};
goog.ui.editor.ToolbarController.prototype.updateToolbar = function (a) {
    if (this.toolbar_.isEnabled() && this.dispatchEvent(goog.ui.Component.EventType.CHANGE)) {
        var b;
        try {
            b = this.field_.queryCommandValue(a.commands || this.queryCommands_)
        } catch (c) {
            b = {}
        }
        this.updateToolbarFromState(b)
    }
};
goog.ui.editor.ToolbarController.prototype.updateToolbarFromState = function (a) {
    for (var b in a) {
        var c = this.toolbar_.getChild(this.getComponentId(b));
        if (c) {
            var d = a[b];
            c.updateFromValue ? c.updateFromValue(d) : c.setChecked(!!d)
        }
    }
};
goog.ui.editor.ToolbarController.prototype.handleAction = function (a) {
    var b = this.getCommand(a.target.getId());
    this.field_.execCommand(b, a.target.getValue())
};
goog.editor.plugins.AbstractTabHandler = function () {
    goog.editor.Plugin.call(this)
};
goog.inherits(goog.editor.plugins.AbstractTabHandler, goog.editor.Plugin);
goog.editor.plugins.AbstractTabHandler.prototype.handleKeyboardShortcut = function (a, b, c) {
    return goog.userAgent.GECKO && this.getFieldObject().inModalMode() ? !1 : a.keyCode != goog.events.KeyCodes.TAB || a.metaKey || a.ctrlKey ? !1 : this.handleTabKey(a)
};
goog.editor.plugins.ListTabHandler = function () {
    goog.editor.plugins.AbstractTabHandler.call(this)
};
goog.inherits(goog.editor.plugins.ListTabHandler, goog.editor.plugins.AbstractTabHandler);
goog.editor.plugins.ListTabHandler.prototype.getTrogClassId = function () {
    return"ListTabHandler"
};
goog.editor.plugins.ListTabHandler.prototype.handleTabKey = function (a) {
    var b = this.getFieldObject().getRange();
    return goog.dom.getAncestorByTagNameAndClass(b.getContainerElement(), goog.dom.TagName.LI) || goog.iter.some(b, function (a) {
        return a.tagName == goog.dom.TagName.LI
    }) ? (this.getFieldObject().execCommand(a.shiftKey ? goog.editor.Command.OUTDENT : goog.editor.Command.INDENT), a.preventDefault(), !0) : !1
};
goog.editor.plugins.RemoveFormatting = function () {
    goog.editor.Plugin.call(this);
    this.optRemoveFormattingFunc_ = null
};
goog.inherits(goog.editor.plugins.RemoveFormatting, goog.editor.Plugin);
goog.editor.plugins.RemoveFormatting.REMOVE_FORMATTING_COMMAND = "+removeFormat";
goog.editor.plugins.RemoveFormatting.BLOCK_RE_ = /^(DIV|TR|LI|BLOCKQUOTE|H\d|PRE|XMP)/;
goog.editor.plugins.RemoveFormatting.appendNewline_ = function (a) {
    a.push("<br>")
};
goog.editor.plugins.RemoveFormatting.createRangeDelimitedByRanges_ = function (a, b) {
    return goog.dom.Range.createFromNodes(a.getStartNode(), a.getStartOffset(), b.getEndNode(), b.getEndOffset())
};
goog.editor.plugins.RemoveFormatting.prototype.getTrogClassId = function () {
    return"RemoveFormatting"
};
goog.editor.plugins.RemoveFormatting.prototype.isSupportedCommand = function (a) {
    return a == goog.editor.plugins.RemoveFormatting.REMOVE_FORMATTING_COMMAND
};
goog.editor.plugins.RemoveFormatting.prototype.execCommandInternal = function (a, b) {
    a == goog.editor.plugins.RemoveFormatting.REMOVE_FORMATTING_COMMAND && this.removeFormatting_()
};
goog.editor.plugins.RemoveFormatting.prototype.handleKeyboardShortcut = function (a, b, c) {
    return c ? " " == b ? (this.getFieldObject().execCommand(goog.editor.plugins.RemoveFormatting.REMOVE_FORMATTING_COMMAND), !0) : !1 : !1
};
goog.editor.plugins.RemoveFormatting.prototype.removeFormatting_ = function () {
    if (!this.getFieldObject().getRange().isCollapsed()) {
        var a = this.optRemoveFormattingFunc_ || goog.bind(this.removeFormattingWorker_, this);
        this.convertSelectedHtmlText_(a);
        this.getFieldDomHelper().getDocument().execCommand("RemoveFormat", !1, void 0);
        goog.editor.BrowserFeature.ADDS_NBSPS_IN_REMOVE_FORMAT && this.convertSelectedHtmlText_(function (a) {
            var c = goog.userAgent.isVersionOrHigher("528") ? /&nbsp;/g : /\u00A0/g;
            return a.replace(c,
                " ")
        })
    }
};
goog.editor.plugins.RemoveFormatting.prototype.getTableAncestor_ = function (a) {
    for (var b = this.getFieldObject().getElement(); a && a != b;) {
        if (a.tagName == goog.dom.TagName.TABLE)return a;
        a = a.parentNode
    }
    return null
};
goog.editor.plugins.RemoveFormatting.prototype.pasteHtml_ = function (a) {
    var b = this.getFieldObject().getRange(), c = this.getFieldDomHelper(), d = goog.string.createUniqueString(), e = goog.string.createUniqueString();
    a = '<span id="' + d + '"></span>' + a + '<span id="' + e + '"></span>';
    var f = goog.string.createUniqueString(), g = '<span id="' + f + '"></span>';
    if (goog.editor.BrowserFeature.HAS_IE_RANGES) {
        var h = b.getTextRange(0).getBrowserRangeObject();
        h.pasteHTML(g);
        for (var k; (k = h.parentElement()) && goog.editor.node.isEmpty(k) && !goog.editor.node.isEditableContainer(k);) {
            b = k.nodeName;
            if (b == goog.dom.TagName.TD || b == goog.dom.TagName.TR || b == goog.dom.TagName.TH)break;
            goog.dom.removeNode(k)
        }
        h.pasteHTML(a);
        (f = c.getElement(f)) && goog.dom.removeNode(f)
    } else if (goog.editor.BrowserFeature.HAS_W3C_RANGES) {
        c.getDocument().execCommand("insertImage", !1, f);
        b = RegExp("<[^<]*" + f + "[^>]*>");
        k = this.getFieldObject().getRange().getContainerElement();
        k.nodeType == goog.dom.NodeType.TEXT && (k = k.parentNode);
        for (; !b.test(k.innerHTML);)k = k.parentNode;
        if (goog.userAgent.GECKO)goog.editor.node.replaceInnerHtml(k,
            k.innerHTML.replace(b, a)); else {
            goog.editor.node.replaceInnerHtml(k, k.innerHTML.replace(b, g));
            for (k = f = c.getElement(f); (k = f.parentNode) && goog.editor.node.isEmpty(k) && !goog.editor.node.isEditableContainer(k);) {
                b = k.nodeName;
                if (b == goog.dom.TagName.TD || b == goog.dom.TagName.TR || b == goog.dom.TagName.TH)break;
                goog.dom.insertSiblingAfter(f, k);
                goog.dom.removeNode(k)
            }
            goog.editor.node.replaceInnerHtml(k, k.innerHTML.replace(RegExp(g, "i"), a))
        }
    }
    a = c.getElement(d);
    c = c.getElement(e);
    goog.dom.Range.createFromNodes(a, 0,
        c, c.childNodes.length).select();
    goog.dom.removeNode(a);
    goog.dom.removeNode(c)
};
goog.editor.plugins.RemoveFormatting.prototype.getHtmlText_ = function (a) {
    var b = this.getFieldDomHelper().createDom("div"), c = a.getBrowserRangeObject();
    if (goog.editor.BrowserFeature.HAS_W3C_RANGES)b.appendChild(c.cloneContents()); else if (goog.editor.BrowserFeature.HAS_IE_RANGES) {
        var d = a.getText(), d = d.replace(/\r\n/g, "\r"), e = d.length;
        a = e - goog.string.trimLeft(d).length;
        d = e - goog.string.trimRight(d).length;
        c.moveStart("character", a);
        c.moveEnd("character", -d);
        a = c.htmlText;
        "Formatted" == c.queryCommandValue("formatBlock") &&
        (a = goog.string.newLineToBr(c.htmlText));
        b.innerHTML = a
    }
    return b.innerHTML
};
goog.editor.plugins.RemoveFormatting.prototype.adjustRangeForTables_ = function (a, b, c) {
    var d = goog.editor.range.saveUsingNormalizedCarets(a), e = a.getStartNode(), f = a.getStartOffset(), g = a.getEndNode();
    a = a.getEndOffset();
    var h = this.getFieldDomHelper();
    if (b) {
        var k = h.createTextNode("");
        goog.dom.insertSiblingAfter(k, b);
        e = k;
        f = 0
    }
    c && (k = h.createTextNode(""), goog.dom.insertSiblingBefore(k, c), g = k, a = 0);
    goog.dom.Range.createFromNodes(e, f, g, a).select();
    return d
};
goog.editor.plugins.RemoveFormatting.prototype.putCaretInCave_ = function (a, b) {
    var c = goog.dom.removeNode(a.getCaret(b));
    b ? this.startCaretInCave_ = c : this.endCaretInCave_ = c
};
goog.editor.plugins.RemoveFormatting.prototype.restoreCaretsFromCave_ = function () {
    var a = this.getFieldObject().getElement();
    this.startCaretInCave_ && (a.insertBefore(this.startCaretInCave_, a.firstChild), this.startCaretInCave_ = null);
    this.endCaretInCave_ && (a.appendChild(this.endCaretInCave_), this.endCaretInCave_ = null)
};
goog.editor.plugins.RemoveFormatting.prototype.convertSelectedHtmlText_ = function (a) {
    var b = this.getFieldObject().getRange();
    if (!(1 < b.getTextRangeCount())) {
        if (goog.userAgent.GECKO) {
            var c = goog.editor.range.expand(b, this.getFieldObject().getElement()), d = this.getTableAncestor_(c.getStartNode()), e = this.getTableAncestor_(c.getEndNode());
            if (d || e) {
                if (d == e)return;
                var f = this.adjustRangeForTables_(b, d, e);
                d || this.putCaretInCave_(f, !0);
                e || this.putCaretInCave_(f, !1);
                b = this.getFieldObject().getRange();
                c = goog.editor.range.expand(b,
                    this.getFieldObject().getElement())
            }
            c.select();
            b = c
        }
        b = this.getHtmlText_(b);
        this.pasteHtml_(a(b));
        goog.userAgent.GECKO && f && (b = this.getFieldObject().getRange(), this.restoreCaretsFromCave_(), a = f.toAbstractRange(), goog.editor.plugins.RemoveFormatting.createRangeDelimitedByRanges_(d ? a : b, e ? a : b).select(), f.dispose())
    }
};
goog.editor.plugins.RemoveFormatting.prototype.removeFormattingWorker_ = function (a) {
    var b = goog.dom.createElement("div");
    b.innerHTML = a;
    a = [];
    for (var b = [b.childNodes, 0], c = [], d = 0, e = [], f = 0, g = 0; 0 <= g; g -= 2) {
        for (var h = !1; 0 < f && g <= e[f - 1];)f--, h = !0;
        h && goog.editor.plugins.RemoveFormatting.appendNewline_(a);
        for (h = !1; 0 < d && g <= c[d - 1];)d--, h = !0;
        h && goog.editor.plugins.RemoveFormatting.appendNewline_(a);
        for (var h = b[g], k = b[g + 1]; k < h.length;) {
            var l = h[k++], m = l.nodeName, n = this.getValueForNode(l);
            if (goog.isDefAndNotNull(n))a.push(n);
            else {
                switch (m) {
                    case "#text":
                        l = 0 < d ? l.nodeValue : goog.string.stripNewlines(l.nodeValue);
                        l = goog.string.htmlEscape(l);
                        a.push(l);
                        continue;
                    case goog.dom.TagName.P:
                        goog.editor.plugins.RemoveFormatting.appendNewline_(a);
                        goog.editor.plugins.RemoveFormatting.appendNewline_(a);
                        break;
                    case goog.dom.TagName.BR:
                        goog.editor.plugins.RemoveFormatting.appendNewline_(a);
                        continue;
                    case goog.dom.TagName.TABLE:
                        goog.editor.plugins.RemoveFormatting.appendNewline_(a);
                        e[f++] = g;
                        break;
                    case goog.dom.TagName.PRE:
                    case "XMP":
                        c[d++] =
                            g;
                        break;
                    case goog.dom.TagName.STYLE:
                    case goog.dom.TagName.SCRIPT:
                    case goog.dom.TagName.SELECT:
                        continue;
                    case goog.dom.TagName.A:
                        if (l.href && "" != l.href) {
                            a.push("<a href='");
                            a.push(l.href);
                            a.push("'>");
                            a.push(this.removeFormattingWorker_(l.innerHTML));
                            a.push("</a>");
                            continue
                        } else break;
                    case goog.dom.TagName.IMG:
                        a.push("<img src='");
                        a.push(l.src);
                        a.push("'");
                        "0" == l.border && a.push(" border='0'");
                        a.push(">");
                        continue;
                    case goog.dom.TagName.TD:
                        l.previousSibling && a.push(" ");
                        break;
                    case goog.dom.TagName.TR:
                        l.previousSibling &&
                        goog.editor.plugins.RemoveFormatting.appendNewline_(a);
                        break;
                    case goog.dom.TagName.DIV:
                        if (n = l.parentNode, n.firstChild == l && goog.editor.plugins.RemoveFormatting.BLOCK_RE_.test(n.tagName))break;
                    default:
                        goog.editor.plugins.RemoveFormatting.BLOCK_RE_.test(m) && goog.editor.plugins.RemoveFormatting.appendNewline_(a)
                }
                l = l.childNodes;
                0 < l.length && (b[g++] = h, b[g++] = k, h = l, k = 0)
            }
        }
    }
    return goog.string.normalizeSpaces(a.join(""))
};
goog.editor.plugins.RemoveFormatting.prototype.getValueForNode = function (a) {
    return null
};
goog.editor.plugins.RemoveFormatting.prototype.setRemoveFormattingFunc = function (a) {
    this.optRemoveFormattingFunc_ = a
};
goog.events.EventWrapper = function () {
};
goog.events.EventWrapper.prototype.listen = function (a, b, c, d, e) {
};
goog.events.EventWrapper.prototype.unlisten = function (a, b, c, d, e) {
};
goog.events.ActionEventWrapper_ = function () {
};
goog.events.actionEventWrapper = new goog.events.ActionEventWrapper_;
goog.events.ActionEventWrapper_.EVENT_TYPES_ = [goog.events.EventType.CLICK, goog.userAgent.GECKO ? goog.events.EventType.KEYPRESS : goog.events.EventType.KEYDOWN];
goog.events.ActionEventWrapper_.prototype.listen = function (a, b, c, d, e) {
    var f = function (a) {
        if (a.type == goog.events.EventType.CLICK && a.isMouseActionButton())b.call(d, a); else if (a.keyCode == goog.events.KeyCodes.ENTER || a.keyCode == goog.events.KeyCodes.MAC_ENTER)a.type = goog.events.EventType.KEYPRESS, b.call(d, a)
    };
    f.listener_ = b;
    f.scope_ = d;
    e ? e.listen(a, goog.events.ActionEventWrapper_.EVENT_TYPES_, f, c) : goog.events.listen(a, goog.events.ActionEventWrapper_.EVENT_TYPES_, f, c)
};
goog.events.ActionEventWrapper_.prototype.unlisten = function (a, b, c, d, e) {
    for (var f, g = 0; f = goog.events.ActionEventWrapper_.EVENT_TYPES_[g]; g++)for (var h = goog.events.getListeners(a, f, !!c), k, l = 0; k = h[l]; l++)if (k.listener.listener_ == b && k.listener.scope_ == d) {
        e ? e.unlisten(a, f, k.listener, c, d) : goog.events.unlisten(a, f, k.listener, c, d);
        break
    }
};
goog.dom.ViewportSizeMonitor = function (a) {
    goog.events.EventTarget.call(this);
    this.window_ = a || window;
    this.listenerKey_ = goog.events.listen(this.window_, goog.events.EventType.RESIZE, this.handleResize_, !1, this);
    this.size_ = goog.dom.getViewportSize(this.window_)
};
goog.inherits(goog.dom.ViewportSizeMonitor, goog.events.EventTarget);
goog.dom.ViewportSizeMonitor.getInstanceForWindow = function (a) {
    a = a || window;
    var b = goog.getUid(a);
    return goog.dom.ViewportSizeMonitor.windowInstanceMap_[b] = goog.dom.ViewportSizeMonitor.windowInstanceMap_[b] || new goog.dom.ViewportSizeMonitor(a)
};
goog.dom.ViewportSizeMonitor.removeInstanceForWindow = function (a) {
    a = goog.getUid(a || window);
    goog.dispose(goog.dom.ViewportSizeMonitor.windowInstanceMap_[a]);
    delete goog.dom.ViewportSizeMonitor.windowInstanceMap_[a]
};
goog.dom.ViewportSizeMonitor.windowInstanceMap_ = {};
goog.dom.ViewportSizeMonitor.prototype.listenerKey_ = null;
goog.dom.ViewportSizeMonitor.prototype.window_ = null;
goog.dom.ViewportSizeMonitor.prototype.size_ = null;
goog.dom.ViewportSizeMonitor.prototype.getSize = function () {
    return this.size_ ? this.size_.clone() : null
};
goog.dom.ViewportSizeMonitor.prototype.disposeInternal = function () {
    goog.dom.ViewportSizeMonitor.superClass_.disposeInternal.call(this);
    this.listenerKey_ && (goog.events.unlistenByKey(this.listenerKey_), this.listenerKey_ = null);
    this.size_ = this.window_ = null
};
goog.dom.ViewportSizeMonitor.prototype.handleResize_ = function (a) {
    a = goog.dom.getViewportSize(this.window_);
    goog.math.Size.equals(a, this.size_) || (this.size_ = a, this.dispatchEvent(goog.events.EventType.RESIZE))
};
goog.ui.editor.Bubble = function (a, b) {
    goog.events.EventTarget.call(this);
    this.dom_ = goog.dom.getDomHelper(a);
    this.eventHandler_ = new goog.events.EventHandler(this);
    this.viewPortSizeMonitor_ = new goog.dom.ViewportSizeMonitor(this.dom_.getWindow());
    this.panels_ = {};
    this.bubbleContainer_ = this.dom_.createDom(goog.dom.TagName.DIV, {className: goog.ui.editor.Bubble.BUBBLE_CLASSNAME});
    goog.style.setElementShown(this.bubbleContainer_, !1);
    goog.dom.appendChild(a, this.bubbleContainer_);
    goog.style.setStyle(this.bubbleContainer_,
        "zIndex", b);
    this.bubbleContents_ = this.createBubbleDom(this.dom_, this.bubbleContainer_);
    this.closeBox_ = this.dom_.createDom(goog.dom.TagName.DIV, {className: "tr_bubble_closebox", innerHTML: "&nbsp;"});
    this.bubbleContents_.appendChild(this.closeBox_);
    goog.editor.style.makeUnselectable(this.bubbleContainer_, this.eventHandler_);
    this.popup_ = new goog.ui.PopupBase(this.bubbleContainer_)
};
goog.inherits(goog.ui.editor.Bubble, goog.events.EventTarget);
goog.ui.editor.Bubble.BUBBLE_CLASSNAME = "tr_bubble";
goog.ui.editor.Bubble.prototype.createBubbleDom = function (a, b) {
    return b
};
goog.ui.editor.Bubble.prototype.logger = goog.log.getLogger("goog.ui.editor.Bubble");
goog.ui.editor.Bubble.prototype.disposeInternal = function () {
    goog.ui.editor.Bubble.superClass_.disposeInternal.call(this);
    goog.dom.removeNode(this.bubbleContainer_);
    this.bubbleContainer_ = null;
    this.eventHandler_.dispose();
    this.eventHandler_ = null;
    this.viewPortSizeMonitor_.dispose();
    this.viewPortSizeMonitor_ = null
};
goog.ui.editor.Bubble.prototype.getContentElement = function () {
    return this.bubbleContents_
};
goog.ui.editor.Bubble.prototype.getContainerElement = function () {
    return this.bubbleContainer_
};
goog.ui.editor.Bubble.prototype.getEventHandler = function () {
    return this.eventHandler_
};
goog.ui.editor.Bubble.prototype.handleWindowResize_ = function () {
    this.isVisible() && this.reposition()
};
goog.ui.editor.Bubble.prototype.hasPanelOfType = function (a) {
    return goog.object.some(this.panels_, function (b) {
        return b.type == a
    })
};
goog.ui.editor.Bubble.prototype.addPanel = function (a, b, c, d, e) {
    var f = goog.string.createUniqueString();
    b = new goog.ui.editor.Bubble.Panel_(this.dom_, f, a, b, c, !e);
    this.panels_[f] = b;
    var g;
    c = 0;
    for (e = this.bubbleContents_.childNodes.length - 1; c < e; c++) {
        var h = this.bubbleContents_.childNodes[c];
        if (this.panels_[h.id].type > a) {
            g = h;
            break
        }
    }
    goog.dom.insertSiblingBefore(b.element, g || this.bubbleContents_.lastChild);
    d(b.getContentElement());
    goog.editor.style.makeUnselectable(b.element, this.eventHandler_);
    a = goog.object.getCount(this.panels_);
    1 == a ? this.openBubble_() : 2 == a && goog.dom.classes.add(this.bubbleContainer_, "tr_multi_bubble");
    this.reposition();
    return f
};
goog.ui.editor.Bubble.prototype.removePanel = function (a) {
    goog.dom.removeNode(this.panels_[a].element);
    delete this.panels_[a];
    a = goog.object.getCount(this.panels_);
    1 >= a && goog.dom.classes.remove(this.bubbleContainer_, "tr_multi_bubble");
    0 == a ? this.closeBubble_() : this.reposition()
};
goog.ui.editor.Bubble.prototype.openBubble_ = function () {
    this.eventHandler_.listen(this.closeBox_, goog.events.EventType.CLICK, this.closeBubble_).listen(this.viewPortSizeMonitor_, goog.events.EventType.RESIZE, this.handleWindowResize_).listen(this.popup_, goog.ui.PopupBase.EventType.HIDE, this.handlePopupHide);
    this.popup_.setVisible(!0);
    this.reposition()
};
goog.ui.editor.Bubble.prototype.closeBubble_ = function () {
    this.popup_.setVisible(!1)
};
goog.ui.editor.Bubble.prototype.handlePopupHide = function () {
    for (var a in this.panels_)goog.dom.removeNode(this.panels_[a].element);
    this.panels_ = {};
    goog.dom.classes.remove(this.bubbleContainer_, "tr_multi_bubble");
    this.eventHandler_.removeAll();
    this.dispatchEvent(goog.ui.Component.EventType.HIDE)
};
goog.ui.editor.Bubble.prototype.isVisible = function () {
    return this.popup_.isVisible()
};
goog.ui.editor.Bubble.VERTICAL_CLEARANCE_ = goog.userAgent.IE ? 4 : 2;
goog.ui.editor.Bubble.MARGIN_BOX_ = new goog.math.Box(goog.ui.editor.Bubble.VERTICAL_CLEARANCE_, 0, goog.ui.editor.Bubble.VERTICAL_CLEARANCE_, 0);
goog.ui.editor.Bubble.prototype.reposition = function () {
    var a = null, b = !0, c;
    for (c in this.panels_)var d = this.panels_[c], a = d.targetElement, b = b && d.preferBottomPosition;
    c = goog.positioning.OverflowStatus.FAILED;
    a = goog.style.isRightToLeft(this.bubbleContainer_) != goog.style.isRightToLeft(a);
    b && (c = this.positionAtAnchor_(a ? goog.positioning.Corner.BOTTOM_END : goog.positioning.Corner.BOTTOM_START, goog.positioning.Corner.TOP_START, goog.positioning.Overflow.ADJUST_X | goog.positioning.Overflow.FAIL_Y));
    c & goog.positioning.OverflowStatus.FAILED &&
    (c = this.positionAtAnchor_(a ? goog.positioning.Corner.TOP_END : goog.positioning.Corner.TOP_START, goog.positioning.Corner.BOTTOM_START, goog.positioning.Overflow.ADJUST_X | goog.positioning.Overflow.FAIL_Y));
    c & goog.positioning.OverflowStatus.FAILED && (c = this.positionAtAnchor_(a ? goog.positioning.Corner.BOTTOM_END : goog.positioning.Corner.BOTTOM_START, goog.positioning.Corner.TOP_START, goog.positioning.Overflow.ADJUST_X | goog.positioning.Overflow.ADJUST_Y), c & goog.positioning.OverflowStatus.FAILED && goog.log.warning(this.logger,
        "reposition(): positionAtAnchor() failed with " + c))
};
goog.ui.editor.Bubble.prototype.positionAtAnchor_ = function (a, b, c) {
    var d = null, e;
    for (e in this.panels_) {
        var f = this.panels_[e].targetElement;
        if (!d || goog.dom.contains(f, d))d = this.panels_[e].targetElement
    }
    return goog.positioning.positionAtAnchor(d, a, this.bubbleContainer_, b, null, goog.ui.editor.Bubble.MARGIN_BOX_, c)
};
goog.ui.editor.Bubble.Panel_ = function (a, b, c, d, e, f) {
    this.type = c;
    this.targetElement = e;
    this.preferBottomPosition = f;
    this.element = a.createDom(goog.dom.TagName.DIV, {className: "tr_bubble_panel", id: b}, a.createDom(goog.dom.TagName.DIV, {className: "tr_bubble_panel_title"}, d + ":"), a.createDom(goog.dom.TagName.DIV, {className: "tr_bubble_panel_content"}))
};
goog.ui.editor.Bubble.Panel_.prototype.getContentElement = function () {
    return this.element.lastChild
};
goog.editor.plugins.AbstractBubblePlugin = function () {
    goog.editor.Plugin.call(this);
    this.eventRegister = new goog.events.EventHandler(this)
};
goog.inherits(goog.editor.plugins.AbstractBubblePlugin, goog.editor.Plugin);
goog.editor.plugins.AbstractBubblePlugin.OPTION_LINK_CLASSNAME_ = "tr_option-link";
goog.editor.plugins.AbstractBubblePlugin.LINK_CLASSNAME_ = "tr_bubble_link";
goog.editor.plugins.AbstractBubblePlugin.DASH_NBSP_STRING = goog.string.Unicode.NBSP + "-" + goog.string.Unicode.NBSP;
goog.editor.plugins.AbstractBubblePlugin.defaultBubbleFactory_ = function (a, b) {
    return new goog.ui.editor.Bubble(a, b)
};
goog.editor.plugins.AbstractBubblePlugin.bubbleFactory_ = goog.editor.plugins.AbstractBubblePlugin.defaultBubbleFactory_;
goog.editor.plugins.AbstractBubblePlugin.setBubbleFactory = function (a) {
    goog.editor.plugins.AbstractBubblePlugin.bubbleFactory_ = a
};
goog.editor.plugins.AbstractBubblePlugin.bubbleMap_ = {};
goog.editor.plugins.AbstractBubblePlugin.prototype.panelId_ = null;
goog.editor.plugins.AbstractBubblePlugin.prototype.keyboardNavigationEnabled_ = !1;
goog.editor.plugins.AbstractBubblePlugin.prototype.enableKeyboardNavigation = function (a) {
    this.keyboardNavigationEnabled_ = a
};
goog.editor.plugins.AbstractBubblePlugin.prototype.setBubbleParent = function (a) {
    this.bubbleParent_ = a
};
goog.editor.plugins.AbstractBubblePlugin.prototype.getBubbleDom = function () {
    return this.dom_
};
goog.editor.plugins.AbstractBubblePlugin.prototype.getTrogClassId = goog.functions.constant("AbstractBubblePlugin");
goog.editor.plugins.AbstractBubblePlugin.prototype.getTargetElement = function () {
    return this.targetElement_
};
goog.editor.plugins.AbstractBubblePlugin.prototype.handleKeyUp = function (a) {
    this.isVisible() && this.handleSelectionChange();
    return!1
};
goog.editor.plugins.AbstractBubblePlugin.prototype.handleSelectionChange = function (a, b) {
    var c;
    if (a)c = a.target; else if (b)c = b; else {
        var d = this.getFieldObject().getRange();
        if (d) {
            var e = d.getStartNode(), f = d.getEndNode(), g = d.getStartOffset(), h = d.getEndOffset();
            goog.userAgent.IE && (d.isCollapsed() && e != f) && (d = goog.dom.Range.createCaret(e, g));
            e.nodeType == goog.dom.NodeType.ELEMENT && (e == f && g == h - 1) && (e = e.childNodes[g], e.nodeType == goog.dom.NodeType.ELEMENT && (c = e))
        }
        c = c || d && d.getContainerElement()
    }
    return this.handleSelectionChangeInternal(c)
};
goog.editor.plugins.AbstractBubblePlugin.prototype.handleSelectionChangeInternal = function (a) {
    if (a && (a = this.getBubbleTargetFromSelection(a)))return a == this.targetElement_ && this.panelId_ || (this.panelId_ && this.closeBubble(), this.createBubble(a)), !1;
    this.panelId_ && this.closeBubble();
    return!1
};
goog.editor.plugins.AbstractBubblePlugin.prototype.disable = function (a) {
    if (a.isUneditable()) {
        var b = goog.editor.plugins.AbstractBubblePlugin.bubbleMap_[a.id];
        b && (b.dispose(), delete goog.editor.plugins.AbstractBubblePlugin.bubbleMap_[a.id])
    }
};
goog.editor.plugins.AbstractBubblePlugin.prototype.getSharedBubble_ = function () {
    var a = this.bubbleParent_ || this.getFieldObject().getAppWindow().document.body;
    this.dom_ = goog.dom.getDomHelper(a);
    var b = goog.editor.plugins.AbstractBubblePlugin.bubbleMap_[this.getFieldObject().id];
    b || (b = goog.editor.plugins.AbstractBubblePlugin.bubbleFactory_.call(null, a, this.getFieldObject().getBaseZindex()), goog.editor.plugins.AbstractBubblePlugin.bubbleMap_[this.getFieldObject().id] = b);
    return b
};
goog.editor.plugins.AbstractBubblePlugin.prototype.createBubble = function (a) {
    var b = this.getSharedBubble_();
    b.hasPanelOfType(this.getBubbleType()) || (this.targetElement_ = a, this.panelId_ = b.addPanel(this.getBubbleType(), this.getBubbleTitle(), a, goog.bind(this.createBubbleContents, this), this.shouldPreferBubbleAboveElement()), this.eventRegister.listen(b, goog.ui.Component.EventType.HIDE, this.handlePanelClosed_), this.onShow(), this.keyboardNavigationEnabled_ && this.eventRegister.listen(b.getContentElement(),
        goog.events.EventType.KEYDOWN, this.onBubbleKey_))
};
goog.editor.plugins.AbstractBubblePlugin.prototype.getBubbleType = function () {
    return""
};
goog.editor.plugins.AbstractBubblePlugin.prototype.getBubbleTitle = function () {
    return""
};
goog.editor.plugins.AbstractBubblePlugin.prototype.shouldPreferBubbleAboveElement = goog.functions.FALSE;
goog.editor.plugins.AbstractBubblePlugin.prototype.registerClickHandler = function (a, b) {
    this.registerActionHandler(a, b)
};
goog.editor.plugins.AbstractBubblePlugin.prototype.registerActionHandler = function (a, b) {
    this.eventRegister.listenWithWrapper(a, goog.events.actionEventWrapper, b)
};
goog.editor.plugins.AbstractBubblePlugin.prototype.closeBubble = function () {
    this.panelId_ && (this.getSharedBubble_().removePanel(this.panelId_), this.handlePanelClosed_())
};
goog.editor.plugins.AbstractBubblePlugin.prototype.onShow = goog.nullFunction;
goog.editor.plugins.AbstractBubblePlugin.prototype.handlePanelClosed_ = function () {
    this.panelId_ = this.targetElement_ = null;
    this.eventRegister.removeAll()
};
goog.editor.plugins.AbstractBubblePlugin.prototype.handleKeyDown = function (a) {
    if (this.keyboardNavigationEnabled_ && this.isVisible() && a.keyCode == goog.events.KeyCodes.TAB && !a.shiftKey) {
        var b = this.getSharedBubble_().getContentElement();
        if (b = goog.dom.getElementByClass(goog.editor.plugins.AbstractBubblePlugin.LINK_CLASSNAME_, b))return b.focus(), a.preventDefault(), !0
    }
    return!1
};
goog.editor.plugins.AbstractBubblePlugin.prototype.onBubbleKey_ = function (a) {
    if (this.isVisible() && a.keyCode == goog.events.KeyCodes.TAB) {
        var b = this.getSharedBubble_().getContentElement(), b = goog.dom.getElementsByClass(goog.editor.plugins.AbstractBubblePlugin.LINK_CLASSNAME_, b);
        if (a.shiftKey ? b[0] == a.target : b.length && b[b.length - 1] == a.target)this.getFieldObject().focus(), a.preventDefault()
    }
};
goog.editor.plugins.AbstractBubblePlugin.prototype.isVisible = function () {
    return!!this.panelId_
};
goog.editor.plugins.AbstractBubblePlugin.prototype.reposition = function () {
    var a = this.getSharedBubble_();
    a && a.reposition()
};
goog.editor.plugins.AbstractBubblePlugin.prototype.createLinkOption = function (a) {
    return this.dom_.createDom(goog.dom.TagName.SPAN, {id: a, className: goog.editor.plugins.AbstractBubblePlugin.OPTION_LINK_CLASSNAME_}, this.dom_.createTextNode(goog.editor.plugins.AbstractBubblePlugin.DASH_NBSP_STRING))
};
goog.editor.plugins.AbstractBubblePlugin.prototype.createLink = function (a, b, c, d) {
    a = this.createLinkHelper(a, b, !1, d);
    c && this.registerActionHandler(a, c);
    return a
};
goog.editor.plugins.AbstractBubblePlugin.prototype.createLinkHelper = function (a, b, c, d) {
    b = this.dom_.createDom(c ? goog.dom.TagName.A : goog.dom.TagName.SPAN, {className: goog.editor.plugins.AbstractBubblePlugin.LINK_CLASSNAME_}, b);
    this.keyboardNavigationEnabled_ && b.setAttribute("tabindex", 0);
    b.setAttribute("role", "link");
    this.setupLink(b, a, d);
    goog.editor.style.makeUnselectable(b, this.eventRegister);
    return b
};
goog.editor.plugins.AbstractBubblePlugin.prototype.setupLink = function (a, b, c) {
    c ? c.appendChild(a) : (c = this.dom_.getElement(b)) && goog.dom.replaceNode(a, c);
    a.id = b
};
goog.editor.plugins.LinkBubble = function (a) {
    goog.editor.plugins.AbstractBubblePlugin.call(this);
    this.extraActions_ = goog.array.toArray(arguments);
    this.actionSpans_ = [];
    this.safeToOpenSchemes_ = ["http", "https", "ftp"]
};
goog.inherits(goog.editor.plugins.LinkBubble, goog.editor.plugins.AbstractBubblePlugin);
goog.editor.plugins.LinkBubble.LINK_TEXT_ID_ = "tr_link-text";
goog.editor.plugins.LinkBubble.TEST_LINK_SPAN_ID_ = "tr_test-link-span";
goog.editor.plugins.LinkBubble.TEST_LINK_ID_ = "tr_test-link";
goog.editor.plugins.LinkBubble.CHANGE_LINK_SPAN_ID_ = "tr_change-link-span";
goog.editor.plugins.LinkBubble.CHANGE_LINK_ID_ = "tr_change-link";
goog.editor.plugins.LinkBubble.DELETE_LINK_SPAN_ID_ = "tr_delete-link-span";
goog.editor.plugins.LinkBubble.DELETE_LINK_ID_ = "tr_delete-link";
goog.editor.plugins.LinkBubble.LINK_DIV_ID_ = "tr_link-div";
var MSG_LINK_BUBBLE_TEST_LINK = goog.getMsg("Go to link: "), MSG_LINK_BUBBLE_CHANGE = goog.getMsg("Change"), MSG_LINK_BUBBLE_REMOVE = goog.getMsg("Remove");
goog.editor.plugins.LinkBubble.prototype.stopReferrerLeaks_ = !1;
goog.editor.plugins.LinkBubble.prototype.blockOpeningUnsafeSchemes_ = !0;
goog.editor.plugins.LinkBubble.prototype.stopReferrerLeaks = function () {
    this.stopReferrerLeaks_ = !0
};
goog.editor.plugins.LinkBubble.prototype.setBlockOpeningUnsafeSchemes = function (a) {
    this.blockOpeningUnsafeSchemes_ = a
};
goog.editor.plugins.LinkBubble.prototype.setSafeToOpenSchemes = function (a) {
    this.safeToOpenSchemes_ = a
};
goog.editor.plugins.LinkBubble.prototype.getTrogClassId = function () {
    return"LinkBubble"
};
goog.editor.plugins.LinkBubble.prototype.isSupportedCommand = function (a) {
    return a == goog.editor.Command.UPDATE_LINK_BUBBLE
};
goog.editor.plugins.LinkBubble.prototype.execCommandInternal = function (a, b) {
    a == goog.editor.Command.UPDATE_LINK_BUBBLE && this.updateLink_()
};
goog.editor.plugins.LinkBubble.prototype.updateLink_ = function () {
    var a = this.getTargetElement();
    this.closeBubble();
    this.createBubble(a)
};
goog.editor.plugins.LinkBubble.prototype.getBubbleTargetFromSelection = function (a) {
    a = goog.dom.getAncestorByTagNameAndClass(a, goog.dom.TagName.A);
    if (!a) {
        var b = this.getFieldObject().getRange();
        b && b.isCollapsed() && 0 == b.getStartOffset() && (b = b.getStartNode().previousSibling) && b.tagName == goog.dom.TagName.A && (a = b)
    }
    return a
};
goog.editor.plugins.LinkBubble.prototype.setTestLinkUrlFn = function (a) {
    this.testLinkUrlFn_ = a
};
goog.editor.plugins.LinkBubble.prototype.getTargetUrl = function () {
    return this.getTargetElement().getAttribute("href") || ""
};
goog.editor.plugins.LinkBubble.prototype.getBubbleType = function () {
    return goog.dom.TagName.A
};
goog.editor.plugins.LinkBubble.prototype.getBubbleTitle = function () {
    return goog.ui.editor.messages.MSG_LINK_CAPTION
};
goog.editor.plugins.LinkBubble.prototype.createBubbleContents = function (a) {
    var b = this.getLinkToTextObj_(), c = b.valid ? "black" : "red", d = this.shouldOpenUrl(b.linkText);
    if (!goog.editor.Link.isLikelyEmailAddress(b.linkText) && b.valid && d) {
        var e = this.dom_.createDom(goog.dom.TagName.SPAN, {id: goog.editor.plugins.LinkBubble.TEST_LINK_SPAN_ID_}, MSG_LINK_BUBBLE_TEST_LINK), c = this.dom_.createDom(goog.dom.TagName.SPAN, {id: goog.editor.plugins.LinkBubble.LINK_TEXT_ID_, style: "color:" + c}, ""), b = goog.string.truncateMiddle(b.linkText,
            48);
        this.createLink(goog.editor.plugins.LinkBubble.TEST_LINK_ID_, this.dom_.createTextNode(b).data, this.testLink, c)
    } else c = this.dom_.createDom(goog.dom.TagName.SPAN, {id: goog.editor.plugins.LinkBubble.LINK_TEXT_ID_, style: "color:" + c}, this.dom_.createTextNode(b.linkText));
    d = this.createLinkOption(goog.editor.plugins.LinkBubble.CHANGE_LINK_SPAN_ID_);
    this.createLink(goog.editor.plugins.LinkBubble.CHANGE_LINK_ID_, MSG_LINK_BUBBLE_CHANGE, this.showLinkDialog_, d);
    this.actionSpans_ = [];
    for (b = 0; b < this.extraActions_.length; b++) {
        var f =
            this.extraActions_[b], g = this.createLinkOption(f.spanId_);
        this.actionSpans_.push(g);
        this.createLink(f.linkId_, f.message_, function () {
            f.actionFn_(this.getTargetUrl())
        }, g)
    }
    g = this.createLinkOption(goog.editor.plugins.LinkBubble.DELETE_LINK_SPAN_ID_);
    this.createLink(goog.editor.plugins.LinkBubble.DELETE_LINK_ID_, MSG_LINK_BUBBLE_REMOVE, this.deleteLink_, g);
    this.onShow();
    e = this.dom_.createDom(goog.dom.TagName.DIV, {id: goog.editor.plugins.LinkBubble.LINK_DIV_ID_}, e || "", c, d);
    for (b = 0; b < this.actionSpans_.length; b++)e.appendChild(this.actionSpans_[b]);
    e.appendChild(g);
    goog.dom.appendChild(a, e)
};
goog.editor.plugins.LinkBubble.prototype.testLink = function () {
    goog.window.open(this.getTestLinkAction_(), {target: "_blank", noreferrer: this.stopReferrerLeaks_}, this.getFieldObject().getAppWindow())
};
goog.editor.plugins.LinkBubble.prototype.isInvalidUrl = goog.functions.FALSE;
goog.editor.plugins.LinkBubble.prototype.getLinkToTextObj_ = function () {
    var a, b = this.getTargetUrl();
    this.isInvalidUrl(b) ? (b = goog.getMsg("invalid url"), a = !0) : goog.editor.Link.isMailto(b) && (b = b.substring(7));
    return{linkText: b, valid: !a}
};
goog.editor.plugins.LinkBubble.prototype.showLinkDialog_ = function (a) {
    a.preventDefault();
    this.getFieldObject().execCommand(goog.editor.Command.MODAL_LINK_EDITOR, new goog.editor.Link(this.getTargetElement(), !1));
    this.closeBubble()
};
goog.editor.plugins.LinkBubble.prototype.deleteLink_ = function () {
    this.getFieldObject().dispatchBeforeChange();
    var a = this.getTargetElement(), b = a.lastChild;
    goog.dom.flattenElement(a);
    goog.editor.range.placeCursorNextTo(b, !1);
    this.closeBubble();
    this.getFieldObject().dispatchChange();
    this.getFieldObject().focus()
};
goog.editor.plugins.LinkBubble.prototype.onShow = function () {
    if (this.dom_.getElement(goog.editor.plugins.LinkBubble.LINK_DIV_ID_)) {
        var a = this.dom_.getElement(goog.editor.plugins.LinkBubble.TEST_LINK_SPAN_ID_);
        if (a) {
            var b = this.getTargetUrl();
            goog.style.setElementShown(a, !goog.editor.Link.isMailto(b))
        }
        for (a = 0; a < this.extraActions_.length; a++) {
            var b = this.extraActions_[a], c = this.dom_.getElement(b.spanId_);
            c && goog.style.setElementShown(c, b.toShowFn_(this.getTargetUrl()))
        }
    }
};
goog.editor.plugins.LinkBubble.prototype.getTestLinkAction_ = function () {
    var a = this.getTargetUrl();
    return this.testLinkUrlFn_ ? this.testLinkUrlFn_(a) : a
};
goog.editor.plugins.LinkBubble.prototype.shouldOpenUrl = function (a) {
    return!this.blockOpeningUnsafeSchemes_ || this.isSafeSchemeToOpen_(a)
};
goog.editor.plugins.LinkBubble.prototype.isSafeSchemeToOpen_ = function (a) {
    a = goog.uri.utils.getScheme(a) || "http";
    return goog.array.contains(this.safeToOpenSchemes_, a.toLowerCase())
};
goog.editor.plugins.LinkBubble.Action = function (a, b, c, d, e) {
    this.spanId_ = a;
    this.linkId_ = b;
    this.message_ = c;
    this.toShowFn_ = d;
    this.actionFn_ = e
};
goog.dom.NodeOffset = function (a, b) {
    goog.Disposable.call(this);
    this.offsetStack_ = [];
    for (this.nameStack_ = []; a && a.nodeName != goog.dom.TagName.BODY && a != b;) {
        for (var c = 0, d = a.previousSibling; d;)d = d.previousSibling, ++c;
        this.offsetStack_.unshift(c);
        this.nameStack_.unshift(a.nodeName);
        a = a.parentNode
    }
};
goog.inherits(goog.dom.NodeOffset, goog.Disposable);
goog.dom.NodeOffset.prototype.toString = function () {
    for (var a = [], b, c = 0; b = this.nameStack_[c]; c++)a.push(this.offsetStack_[c] + "," + b);
    return a.join("\n")
};
goog.dom.NodeOffset.prototype.findTargetNode = function (a) {
    for (var b = a, c = 0; a = this.nameStack_[c]; ++c)if (b = b.childNodes[this.offsetStack_[c]], !b || b.nodeName != a)return null;
    return b
};
goog.dom.NodeOffset.prototype.disposeInternal = function () {
    delete this.offsetStack_;
    delete this.nameStack_
};
goog.editor.plugins.Blockquote = function (a, b) {
    goog.editor.Plugin.call(this);
    this.requiresClassNameToSplit_ = a;
    this.className_ = b || "tr_bq"
};
goog.inherits(goog.editor.plugins.Blockquote, goog.editor.Plugin);
goog.editor.plugins.Blockquote.SPLIT_COMMAND = "+splitBlockquote";
goog.editor.plugins.Blockquote.CLASS_ID = "Blockquote";
goog.editor.plugins.Blockquote.prototype.logger = goog.log.getLogger("goog.editor.plugins.Blockquote");
goog.editor.plugins.Blockquote.prototype.getTrogClassId = function () {
    return goog.editor.plugins.Blockquote.CLASS_ID
};
goog.editor.plugins.Blockquote.prototype.isSilentCommand = goog.functions.TRUE;
goog.editor.plugins.Blockquote.isBlockquote = function (a, b, c, d) {
    if (a.tagName != goog.dom.TagName.BLOCKQUOTE)return!1;
    if (!c)return b;
    a = goog.dom.classes.has(a, d);
    return b ? a : !a
};
goog.editor.plugins.Blockquote.prototype.isSplittableBlockquote = function (a) {
    return a.tagName != goog.dom.TagName.BLOCKQUOTE ? !1 : this.requiresClassNameToSplit_ ? goog.dom.classes.has(a, this.className_) : !0
};
goog.editor.plugins.Blockquote.prototype.isSetupBlockquote = function (a) {
    return a.tagName == goog.dom.TagName.BLOCKQUOTE && goog.dom.classes.has(a, this.className_)
};
goog.editor.plugins.Blockquote.prototype.isUnsetupBlockquote = function (a) {
    return a.tagName == goog.dom.TagName.BLOCKQUOTE && !this.isSetupBlockquote(a)
};
goog.editor.plugins.Blockquote.prototype.getBlockquoteClassName = function () {
    return this.className_
};
goog.editor.plugins.Blockquote.findAndRemoveSingleChildAncestor_ = function (a, b) {
    var c = goog.editor.node.findHighestMatchingAncestor(a, function (a) {
        return a != b && 1 == a.childNodes.length
    });
    c || (c = a);
    goog.dom.removeNode(c)
};
goog.editor.plugins.Blockquote.removeAllWhiteSpaceNodes_ = function (a) {
    for (var b = 0; b < a.length; ++b)goog.editor.node.isEmpty(a[b], !0) && goog.dom.removeNode(a[b])
};
goog.editor.plugins.Blockquote.prototype.isSupportedCommand = function (a) {
    return a == goog.editor.plugins.Blockquote.SPLIT_COMMAND
};
goog.editor.plugins.Blockquote.prototype.execCommandInternal = function (a, b) {
    if (a == goog.editor.plugins.Blockquote.SPLIT_COMMAND && b && (this.className_ || !this.requiresClassNameToSplit_))return goog.editor.BrowserFeature.HAS_W3C_RANGES ? this.splitQuotedBlockW3C_(b) : this.splitQuotedBlockIE_(b)
};
goog.editor.plugins.Blockquote.prototype.splitQuotedBlockW3C_ = function (a) {
    var b = a.node, c = goog.editor.node.findTopMostEditableAncestor(b.parentNode, goog.bind(this.isSplittableBlockquote, this)), d, e, f = !1;
    c ? b.nodeType == goog.dom.NodeType.TEXT ? a.offset == b.length ? (d = b.nextSibling) && d.tagName == goog.dom.TagName.BR ? (b = d, d = d.nextSibling) : d = e = b.splitText(a.offset) : d = b.splitText(a.offset) : b.tagName == goog.dom.TagName.BR ? d = b.nextSibling : f = !0 : this.isSetupBlockquote(b) && (c = b, f = !0);
    f && (b = this.insertEmptyTextNodeBeforeRange_(),
        d = this.insertEmptyTextNodeBeforeRange_());
    if (!c)return!1;
    d = goog.editor.node.splitDomTreeAt(b, d, c);
    goog.dom.insertSiblingAfter(d, c);
    a = this.getFieldDomHelper();
    b = this.getFieldObject().queryCommandValue(goog.editor.Command.DEFAULT_TAG) || goog.dom.TagName.DIV;
    b = a.createElement(b);
    b.innerHTML = "&nbsp;";
    c.parentNode.insertBefore(b, d);
    a.getWindow().getSelection().collapse(b, 0);
    e && goog.editor.plugins.Blockquote.findAndRemoveSingleChildAncestor_(e, d);
    goog.editor.plugins.Blockquote.removeAllWhiteSpaceNodes_([c,
        d]);
    return!0
};
goog.editor.plugins.Blockquote.prototype.insertEmptyTextNodeBeforeRange_ = function () {
    var a = this.getFieldObject().getRange(), b = this.getFieldDomHelper().createTextNode("");
    a.insertNode(b, !0);
    return b
};
goog.editor.plugins.Blockquote.prototype.splitQuotedBlockIE_ = function (a) {
    var b = this.getFieldDomHelper(), c = goog.editor.node.findTopMostEditableAncestor(a.parentNode, goog.bind(this.isSplittableBlockquote, this));
    if (!c)return!1;
    var d = a.cloneNode(!1);
    a.nextSibling && a.nextSibling.tagName == goog.dom.TagName.BR && (a = a.nextSibling);
    var e = goog.editor.node.splitDomTreeAt(a, d, c);
    goog.dom.insertSiblingAfter(e, c);
    var f = this.getFieldObject().queryCommandValue(goog.editor.Command.DEFAULT_TAG) || goog.dom.TagName.DIV,
        f = b.createElement(f);
    c.parentNode.insertBefore(f, e);
    f.innerHTML = "&nbsp;";
    b = b.getDocument().selection.createRange();
    b.moveToElementText(a);
    b.move("character", 2);
    b.select();
    f.innerHTML = "";
    b.pasteHTML("");
    goog.editor.plugins.Blockquote.findAndRemoveSingleChildAncestor_(d, e);
    goog.editor.plugins.Blockquote.removeAllWhiteSpaceNodes_([c, e]);
    return!0
};
goog.editor.plugins.EnterHandler = function () {
    goog.editor.Plugin.call(this)
};
goog.inherits(goog.editor.plugins.EnterHandler, goog.editor.Plugin);
goog.editor.plugins.EnterHandler.prototype.tag = goog.dom.TagName.DIV;
goog.editor.plugins.EnterHandler.prototype.getTrogClassId = function () {
    return"EnterHandler"
};
goog.editor.plugins.EnterHandler.prototype.enable = function (a) {
    goog.editor.plugins.EnterHandler.superClass_.enable.call(this, a);
    !goog.editor.BrowserFeature.SUPPORTS_OPERA_DEFAULTBLOCK_COMMAND || this.tag != goog.dom.TagName.P && this.tag != goog.dom.TagName.DIV || this.getFieldDomHelper().getDocument().execCommand("opera-defaultBlock", !1, this.tag)
};
goog.editor.plugins.EnterHandler.prototype.prepareContentsHtml = function (a) {
    return!a || goog.string.isBreakingWhitespace(a) ? goog.editor.BrowserFeature.COLLAPSES_EMPTY_NODES ? this.getNonCollapsingBlankHtml() : "" : a
};
goog.editor.plugins.EnterHandler.prototype.getNonCollapsingBlankHtml = goog.functions.constant("<br>");
goog.editor.plugins.EnterHandler.prototype.handleBackspaceInternal = function (a, b) {
    var c = this.getFieldObject().getElement(), d = b && b.getStartNode();
    c.firstChild == d && goog.editor.node.isEmpty(d) && (a.preventDefault(), a.stopPropagation())
};
goog.editor.plugins.EnterHandler.prototype.processParagraphTagsInternal = function (a, b) {
    if (goog.userAgent.IE || goog.userAgent.OPERA)this.ensureBlockIeOpera(goog.dom.TagName.DIV); else if (!b && goog.userAgent.WEBKIT) {
        var c = this.getFieldObject().getRange();
        if (c && goog.editor.plugins.EnterHandler.isDirectlyInBlockquote(c.getContainerElement())) {
            var d = this.getFieldDomHelper(), e = d.createElement(goog.dom.TagName.BR);
            c.insertNode(e, !0);
            goog.editor.node.isBlockTag(e.parentNode) && !goog.editor.node.skipEmptyTextNodes(e.nextSibling) &&
            goog.dom.insertSiblingBefore(d.createElement(goog.dom.TagName.BR), e);
            goog.editor.range.placeCursorNextTo(e, !1);
            a.preventDefault()
        }
    }
};
goog.editor.plugins.EnterHandler.isDirectlyInBlockquote = function (a) {
    for (; a; a = a.parentNode)if (goog.editor.node.isBlockTag(a))return a.tagName == goog.dom.TagName.BLOCKQUOTE;
    return!1
};
goog.editor.plugins.EnterHandler.prototype.handleDeleteGecko = function (a) {
    this.deleteBrGecko(a)
};
goog.editor.plugins.EnterHandler.prototype.deleteBrGecko = function (a) {
    var b = this.getFieldObject().getRange();
    if (b.isCollapsed()) {
        var c = b.getEndNode();
        if (c.nodeType == goog.dom.NodeType.ELEMENT && (b = c.childNodes[b.getEndOffset()]) && b.tagName == goog.dom.TagName.BR) {
            var d = goog.editor.node.getPreviousSibling(b), e = b.nextSibling;
            c.removeChild(b);
            a.preventDefault();
            e && goog.editor.node.isBlockTag(e) && (d && d.tagName != goog.dom.TagName.BR && !goog.editor.node.isBlockTag(d) ? goog.dom.Range.createCaret(d, goog.editor.node.getLength(d)).select() :
                (a = goog.editor.node.getLeftMostLeaf(e), goog.dom.Range.createCaret(a, 0).select()))
        }
    }
};
goog.editor.plugins.EnterHandler.prototype.handleKeyPress = function (a) {
    if (goog.userAgent.GECKO && this.getFieldObject().inModalMode())return!1;
    if (a.keyCode == goog.events.KeyCodes.BACKSPACE)this.handleBackspaceInternal(a, this.getFieldObject().getRange()); else if (a.keyCode == goog.events.KeyCodes.ENTER)if (goog.userAgent.GECKO)a.shiftKey || this.handleEnterGecko_(a); else {
        this.getFieldObject().dispatchBeforeChange();
        var b = this.deleteCursorSelection_(), c = !!this.getFieldObject().execCommand(goog.editor.plugins.Blockquote.SPLIT_COMMAND,
            b);
        c && (a.preventDefault(), a.stopPropagation());
        this.releasePositionObject_(b);
        goog.userAgent.WEBKIT && this.handleEnterWebkitInternal(a);
        this.processParagraphTagsInternal(a, c);
        this.getFieldObject().dispatchChange()
    } else goog.userAgent.GECKO && a.keyCode == goog.events.KeyCodes.DELETE && this.handleDeleteGecko(a);
    return!1
};
goog.editor.plugins.EnterHandler.prototype.handleKeyUp = function (a) {
    if (goog.userAgent.GECKO && this.getFieldObject().inModalMode())return!1;
    this.handleKeyUpInternal(a);
    return!1
};
goog.editor.plugins.EnterHandler.prototype.handleKeyUpInternal = function (a) {
    (goog.userAgent.IE || goog.userAgent.OPERA) && a.keyCode == goog.events.KeyCodes.ENTER && this.ensureBlockIeOpera(goog.dom.TagName.DIV, !0)
};
goog.editor.plugins.EnterHandler.prototype.handleEnterGecko_ = function (a) {
    var b = this.getFieldObject().getRange(), c = !b || b.isCollapsed(), d = this.deleteCursorSelection_(), e = this.getFieldObject().execCommand(goog.editor.plugins.Blockquote.SPLIT_COMMAND, d);
    e && (a.preventDefault(), a.stopPropagation());
    this.releasePositionObject_(d);
    e || this.handleEnterAtCursorGeckoInternal(a, c, b)
};
goog.editor.plugins.EnterHandler.prototype.handleEnterWebkitInternal = goog.nullFunction;
goog.editor.plugins.EnterHandler.prototype.handleEnterAtCursorGeckoInternal = goog.nullFunction;
goog.editor.plugins.EnterHandler.DO_NOT_ENSURE_BLOCK_NODES_ = goog.object.createSet(goog.dom.TagName.LI, goog.dom.TagName.DIV, goog.dom.TagName.H1, goog.dom.TagName.H2, goog.dom.TagName.H3, goog.dom.TagName.H4, goog.dom.TagName.H5, goog.dom.TagName.H6);
goog.editor.plugins.EnterHandler.isBrElem = function (a) {
    return goog.editor.node.isEmpty(a) && 1 == a.getElementsByTagName(goog.dom.TagName.BR).length
};
goog.editor.plugins.EnterHandler.prototype.ensureBlockIeOpera = function (a, b) {
    for (var c = this.getFieldObject().getRange(), d = c.getContainer(), e = this.getFieldObject().getElement(), f; d && d != e;) {
        var g = d.nodeName;
        if (g == a || goog.editor.plugins.EnterHandler.DO_NOT_ENSURE_BLOCK_NODES_[g] && (!b || !goog.editor.plugins.EnterHandler.isBrElem(d))) {
            if (goog.userAgent.OPERA && f) {
                g == a && (f == d.lastChild && goog.editor.node.isEmpty(f)) && (goog.dom.insertSiblingAfter(f, d), goog.dom.Range.createFromNodeContents(f).select());
                break
            }
            return
        }
        goog.userAgent.OPERA &&
            (b && g == goog.dom.TagName.P && g != a) && (f = d);
        d = d.parentNode
    }
    if (goog.userAgent.IE && !goog.userAgent.isVersionOrHigher(9)) {
        var h = !1, c = c.getBrowserRangeObject(), d = c.duplicate();
        d.moveEnd("character", 1);
        d.text.length && (h = d.parentElement(), d = d.duplicate(), d.collapse(!1), d = d.parentElement(), h = h != d && d != c.parentElement()) && (c.move("character", -1), c.select())
    }
    this.getFieldObject().getEditableDomHelper().getDocument().execCommand("FormatBlock", !1, "<" + a + ">");
    h && (c.move("character", 1), c.select())
};
goog.editor.plugins.EnterHandler.prototype.deleteCursorSelection_ = function () {
    return goog.editor.BrowserFeature.HAS_W3C_RANGES ? this.deleteCursorSelectionW3C_() : this.deleteCursorSelectionIE_()
};
goog.editor.plugins.EnterHandler.prototype.releasePositionObject_ = function (a) {
    goog.editor.BrowserFeature.HAS_W3C_RANGES || a.removeNode(!0)
};
goog.editor.plugins.EnterHandler.prototype.deleteCursorSelectionIE_ = function () {
    var a = this.getFieldDomHelper().getDocument(), b = a.selection.createRange(), c = goog.string.createUniqueString();
    b.pasteHTML('<span id="' + c + '"></span>');
    a = a.getElementById(c);
    a.id = "";
    return a
};
goog.editor.plugins.EnterHandler.prototype.deleteCursorSelectionW3C_ = function () {
    var a = this.getFieldObject().getRange();
    if (!a.isCollapsed()) {
        var b = !0;
        if (goog.userAgent.OPERA) {
            var c = a.getStartNode(), d = a.getStartOffset();
            c == a.getEndNode() && (c.lastChild && c.lastChild.tagName == goog.dom.TagName.BR && d == c.childNodes.length - 1) && (b = !1)
        }
        b && goog.editor.plugins.EnterHandler.deleteW3cRange_(a)
    }
    return goog.editor.range.getDeepEndPoint(a, !0)
};
goog.editor.plugins.EnterHandler.deleteW3cRange_ = function (a) {
    if (a && !a.isCollapsed()) {
        var b = !0, c = a.getContainerElement(), d = new goog.dom.NodeOffset(a.getStartNode(), c), e = a.getStartOffset(), f = goog.editor.plugins.EnterHandler.isInOneContainerW3c_(a), g = !f && goog.editor.plugins.EnterHandler.isPartialEndW3c_(a);
        a.removeContents();
        (a = d.findTargetNode(c)) ? a = goog.dom.Range.createCaret(a, e) : (a = goog.dom.Range.createCaret(c, c.childNodes.length), b = !1);
        a.select();
        f && (f = goog.editor.style.getContainer(a.getStartNode()),
            goog.editor.node.isEmpty(f, !0) && (b = "&nbsp;", goog.userAgent.OPERA && f.tagName == goog.dom.TagName.LI && (b = "<br>"), goog.editor.node.replaceInnerHtml(f, b), goog.editor.range.selectNodeStart(f.firstChild), b = !1));
        g && (g = goog.editor.style.getContainer(a.getStartNode()), f = goog.editor.node.getNextSibling(g), g && f && (goog.dom.append(g, f.childNodes), goog.dom.removeNode(f)));
        b && (a = goog.dom.Range.createCaret(d.findTargetNode(c), e), a.select())
    }
    return a
};
goog.editor.plugins.EnterHandler.isInOneContainerW3c_ = function (a) {
    var b = a.getStartNode();
    goog.editor.style.isContainer(b) && (b = b.childNodes[a.getStartOffset()] || b);
    var b = goog.editor.style.getContainer(b), c = a.getEndNode();
    goog.editor.style.isContainer(c) && (c = c.childNodes[a.getEndOffset()] || c);
    c = goog.editor.style.getContainer(c);
    return b == c
};
goog.editor.plugins.EnterHandler.isPartialEndW3c_ = function (a) {
    var b = a.getEndNode();
    a = a.getEndOffset();
    var c = b;
    if (goog.editor.style.isContainer(c)) {
        var d = c.childNodes[a];
        if (!d || d.nodeType == goog.dom.NodeType.ELEMENT && goog.editor.style.isContainer(d))return!1
    }
    for (d = goog.editor.style.getContainer(c); d != c;) {
        if (goog.editor.node.getNextSibling(c))return!0;
        c = c.parentNode
    }
    return a != goog.editor.node.getLength(b)
};
goog.editor.plugins.LoremIpsum = function (a) {
    goog.editor.Plugin.call(this);
    this.message_ = a
};
goog.inherits(goog.editor.plugins.LoremIpsum, goog.editor.Plugin);
goog.editor.plugins.LoremIpsum.prototype.getTrogClassId = goog.functions.constant("LoremIpsum");
goog.editor.plugins.LoremIpsum.prototype.activeOnUneditableFields = goog.functions.TRUE;
goog.editor.plugins.LoremIpsum.prototype.usingLorem_ = !1;
goog.editor.plugins.LoremIpsum.prototype.queryCommandValue = function (a) {
    return a == goog.editor.Command.USING_LOREM && this.usingLorem_
};
goog.editor.plugins.LoremIpsum.prototype.execCommand = function (a, b) {
    a == goog.editor.Command.CLEAR_LOREM ? this.clearLorem_(!!b) : a == goog.editor.Command.UPDATE_LOREM && this.updateLorem_()
};
goog.editor.plugins.LoremIpsum.prototype.isSupportedCommand = function (a) {
    return a == goog.editor.Command.CLEAR_LOREM || a == goog.editor.Command.UPDATE_LOREM || a == goog.editor.Command.USING_LOREM
};
goog.editor.plugins.LoremIpsum.prototype.updateLorem_ = function () {
    var a = this.getFieldObject();
    if (!this.usingLorem_ && !a.inModalMode() && goog.editor.Field.getActiveFieldId() != a.id) {
        var b = a.getElement();
        b || (b = a.getOriginalElement());
        goog.asserts.assert(b);
        goog.editor.node.isEmpty(b) && (this.usingLorem_ = !0, this.oldFontStyle_ = b.style.fontStyle, b.style.fontStyle = "italic", a.setHtml(!0, this.message_, !0))
    }
};
goog.editor.plugins.LoremIpsum.prototype.clearLorem_ = function (a) {
    var b = this.getFieldObject();
    if (this.usingLorem_ && !b.inModalMode()) {
        var c = b.getElement();
        c || (c = b.getOriginalElement());
        goog.asserts.assert(c);
        this.usingLorem_ = !1;
        c.style.fontStyle = this.oldFontStyle_;
        b.setHtml(!0, null, !0);
        a && b.isLoaded() && (goog.userAgent.WEBKIT ? (goog.dom.getOwnerDocument(b.getElement()).body.focus(), b.focusAndPlaceCursorAtStart()) : goog.userAgent.OPERA && b.placeCursorAtStart())
    }
};
goog.editor.plugins.UndoRedoState = function (a) {
    goog.events.EventTarget.call(this);
    this.asynchronous_ = a
};
goog.inherits(goog.editor.plugins.UndoRedoState, goog.events.EventTarget);
goog.editor.plugins.UndoRedoState.ACTION_COMPLETED = "action_completed";
goog.editor.plugins.UndoRedoState.prototype.isAsynchronous = function () {
    return this.asynchronous_
};
goog.editor.plugins.UndoRedoManager = function () {
    goog.events.EventTarget.call(this);
    this.maxUndoDepth_ = 100;
    this.undoStack_ = [];
    this.redoStack_ = [];
    this.pendingActions_ = []
};
goog.inherits(goog.editor.plugins.UndoRedoManager, goog.events.EventTarget);
goog.editor.plugins.UndoRedoManager.EventType = {STATE_CHANGE: "state_change", STATE_ADDED: "state_added", BEFORE_UNDO: "before_undo", BEFORE_REDO: "before_redo"};
goog.editor.plugins.UndoRedoManager.prototype.inProgressActionKey_ = null;
goog.editor.plugins.UndoRedoManager.prototype.setMaxUndoDepth = function (a) {
    this.maxUndoDepth_ = a
};
goog.editor.plugins.UndoRedoManager.prototype.addState = function (a) {
    if (0 == this.undoStack_.length || !a.equals(this.undoStack_[this.undoStack_.length - 1])) {
        this.undoStack_.push(a);
        this.undoStack_.length > this.maxUndoDepth_ && this.undoStack_.shift();
        var b = this.redoStack_.length;
        this.redoStack_.length = 0;
        this.dispatchEvent({type: goog.editor.plugins.UndoRedoManager.EventType.STATE_ADDED, state: a});
        (1 == this.undoStack_.length || b) && this.dispatchStateChange_()
    }
};
goog.editor.plugins.UndoRedoManager.prototype.dispatchStateChange_ = function () {
    this.dispatchEvent(goog.editor.plugins.UndoRedoManager.EventType.STATE_CHANGE)
};
goog.editor.plugins.UndoRedoManager.prototype.undo = function () {
    this.shiftState_(this.undoStack_, this.redoStack_)
};
goog.editor.plugins.UndoRedoManager.prototype.redo = function () {
    this.shiftState_(this.redoStack_, this.undoStack_)
};
goog.editor.plugins.UndoRedoManager.prototype.hasUndoState = function () {
    return 0 < this.undoStack_.length
};
goog.editor.plugins.UndoRedoManager.prototype.hasRedoState = function () {
    return 0 < this.redoStack_.length
};
goog.editor.plugins.UndoRedoManager.prototype.shiftState_ = function (a, b) {
    if (a.length) {
        var c = a.pop();
        b.push(c);
        this.addAction_({type: a == this.undoStack_ ? goog.editor.plugins.UndoRedoManager.EventType.BEFORE_UNDO : goog.editor.plugins.UndoRedoManager.EventType.BEFORE_REDO, func: a == this.undoStack_ ? c.undo : c.redo, state: c});
        0 != a.length && 1 != b.length || this.dispatchStateChange_()
    }
};
goog.editor.plugins.UndoRedoManager.prototype.addAction_ = function (a) {
    this.pendingActions_.push(a);
    1 == this.pendingActions_.length && this.doAction_()
};
goog.editor.plugins.UndoRedoManager.prototype.doAction_ = function () {
    if (!this.inProgressActionKey_ && 0 != this.pendingActions_.length) {
        var a = this.pendingActions_.shift();
        this.dispatchEvent({type: a.type, state: a.state}) && (a.state.isAsynchronous() ? (this.inProgressActionKey_ = goog.events.listen(a.state, goog.editor.plugins.UndoRedoState.ACTION_COMPLETED, this.finishAction_, !1, this), a.func.call(a.state)) : (a.func.call(a.state), this.doAction_()))
    }
};
goog.editor.plugins.UndoRedoManager.prototype.finishAction_ = function () {
    goog.events.unlistenByKey(this.inProgressActionKey_);
    this.inProgressActionKey_ = null;
    this.doAction_()
};
goog.editor.plugins.UndoRedoManager.prototype.clearHistory = function () {
    if (0 < this.undoStack_.length || 0 < this.redoStack_.length)this.undoStack_.length = 0, this.redoStack_.length = 0, this.dispatchStateChange_()
};
goog.editor.plugins.UndoRedoManager.prototype.undoPeek = function () {
    return this.undoStack_[this.undoStack_.length - 1]
};
goog.editor.plugins.UndoRedoManager.prototype.redoPeek = function () {
    return this.redoStack_[this.redoStack_.length - 1]
};
goog.editor.plugins.UndoRedo = function (a) {
    goog.editor.Plugin.call(this);
    this.setUndoRedoManager(a || new goog.editor.plugins.UndoRedoManager);
    this.eventHandlers_ = {};
    this.currentStates_ = {};
    this.initialFieldChange_ = null;
    this.boundRestoreState_ = goog.bind(this.restoreState, this)
};
goog.inherits(goog.editor.plugins.UndoRedo, goog.editor.Plugin);
goog.editor.plugins.UndoRedo.prototype.logger = goog.log.getLogger("goog.editor.plugins.UndoRedo");
goog.editor.plugins.UndoRedo.prototype.inProgressUndo_ = null;
goog.editor.plugins.UndoRedo.COMMAND = {UNDO: "+undo", REDO: "+redo"};
goog.editor.plugins.UndoRedo.SUPPORTED_COMMANDS_ = goog.object.transpose(goog.editor.plugins.UndoRedo.COMMAND);
goog.editor.plugins.UndoRedo.prototype.setMaxUndoDepth = function (a) {
    this.undoManager_.setMaxUndoDepth(a)
};
goog.editor.plugins.UndoRedo.prototype.setUndoRedoManager = function (a) {
    this.managerStateChangeKey_ && goog.events.unlistenByKey(this.managerStateChangeKey_);
    this.undoManager_ = a;
    this.managerStateChangeKey_ = goog.events.listen(this.undoManager_, goog.editor.plugins.UndoRedoManager.EventType.STATE_CHANGE, this.dispatchCommandValueChange_, !1, this)
};
goog.editor.plugins.UndoRedo.prototype.isSupportedCommand = function (a) {
    return a in goog.editor.plugins.UndoRedo.SUPPORTED_COMMANDS_
};
goog.editor.plugins.UndoRedo.prototype.unregisterFieldObject = function (a) {
    this.disable(a);
    this.setFieldObject(null)
};
goog.editor.plugins.UndoRedo.prototype.getCurrentFieldObject = function () {
    return this.getFieldObject()
};
goog.editor.plugins.UndoRedo.prototype.getFieldObjectForHash = function (a) {
    return this.getFieldObject()
};
goog.editor.plugins.UndoRedo.prototype.getCurrentEventTarget = function () {
    return this.getFieldObject()
};
goog.editor.plugins.UndoRedo.prototype.enable = function (a) {
    if (!this.isEnabled(a)) {
        a.clearDelayedChange();
        var b = new goog.events.EventHandler(this);
        goog.editor.BrowserFeature.USE_MUTATION_EVENTS || b.listen(a, goog.editor.Field.EventType.BEFORECHANGE, this.handleBeforeChange_);
        b.listen(a, goog.editor.Field.EventType.DELAYEDCHANGE, this.handleDelayedChange_);
        b.listen(a, goog.editor.Field.EventType.BLUR, this.handleBlur_);
        this.eventHandlers_[a.getHashCode()] = b;
        this.updateCurrentState_(a)
    }
};
goog.editor.plugins.UndoRedo.prototype.disable = function (a) {
    a.clearDelayedChange();
    var b = this.eventHandlers_[a.getHashCode()];
    b && (b.dispose(), delete this.eventHandlers_[a.getHashCode()]);
    this.currentStates_[a.getHashCode()] && delete this.currentStates_[a.getHashCode()]
};
goog.editor.plugins.UndoRedo.prototype.isEnabled = function (a) {
    return!!this.eventHandlers_[a.getHashCode()]
};
goog.editor.plugins.UndoRedo.prototype.disposeInternal = function () {
    goog.editor.plugins.UndoRedo.superClass_.disposeInternal.call(this);
    for (var a in this.eventHandlers_)this.eventHandlers_[a].dispose(), delete this.eventHandlers_[a];
    this.setFieldObject(null);
    this.undoManager_ && (this.undoManager_.dispose(), delete this.undoManager_)
};
goog.editor.plugins.UndoRedo.prototype.getTrogClassId = function () {
    return"UndoRedo"
};
goog.editor.plugins.UndoRedo.prototype.execCommand = function (a, b) {
    a == goog.editor.plugins.UndoRedo.COMMAND.UNDO ? this.undoManager_.undo() : a == goog.editor.plugins.UndoRedo.COMMAND.REDO && this.undoManager_.redo()
};
goog.editor.plugins.UndoRedo.prototype.queryCommandValue = function (a) {
    var b = null;
    a == goog.editor.plugins.UndoRedo.COMMAND.UNDO ? b = this.undoManager_.hasUndoState() : a == goog.editor.plugins.UndoRedo.COMMAND.REDO && (b = this.undoManager_.hasRedoState());
    return b
};
goog.editor.plugins.UndoRedo.prototype.dispatchCommandValueChange_ = function () {
    this.getCurrentEventTarget().dispatchEvent({type: goog.editor.Field.EventType.COMMAND_VALUE_CHANGE, commands: [goog.editor.plugins.UndoRedo.COMMAND.REDO, goog.editor.plugins.UndoRedo.COMMAND.UNDO]})
};
goog.editor.plugins.UndoRedo.prototype.restoreState = function (a, b, c) {
    var d = this.getFieldObjectForHash(a.fieldHashCode);
    if (d) {
        d.stopChangeEvents(!0, !0);
        try {
            d.dispatchBeforeChange();
            d.execCommand(goog.editor.Command.CLEAR_LOREM, !0);
            goog.editor.node.replaceInnerHtml(d.getElement(), b);
            c && c.select();
            var e = this.getCurrentFieldObject();
            d.focus();
            e && e.getHashCode() != a.fieldHashCode && e.execCommand(goog.editor.Command.UPDATE_LOREM);
            this.currentStates_[a.fieldHashCode].setUndoState(b, c)
        } catch (f) {
            goog.log.error(this.logger,
                "Error while restoring undo state", f)
        } finally {
            this.inProgressUndo_ = a, d.dispatchChange(), d.dispatchSelectionChangeEvent()
        }
    }
};
goog.editor.plugins.UndoRedo.prototype.handleKeyboardShortcut = function (a, b, c) {
    if (c) {
        var d;
        "z" == b ? d = a.shiftKey ? goog.editor.plugins.UndoRedo.COMMAND.REDO : goog.editor.plugins.UndoRedo.COMMAND.UNDO : "y" == b && (d = goog.editor.plugins.UndoRedo.COMMAND.REDO);
        if (d)return(a = d == goog.editor.plugins.UndoRedo.COMMAND.UNDO ? this.undoManager_.undoPeek() : this.undoManager_.redoPeek()) && a.fieldHashCode ? this.getCurrentFieldObject().execCommand(d) : this.execCommand(d), !0
    }
    return!1
};
goog.editor.plugins.UndoRedo.prototype.clearHistory = function () {
    this.getFieldObject().stopChangeEvents(!0, !0);
    this.undoManager_.clearHistory();
    this.getFieldObject().startChangeEvents()
};
goog.editor.plugins.UndoRedo.prototype.refreshCurrentState = function (a) {
    this.isEnabled(a) && (this.currentStates_[a.getHashCode()] && delete this.currentStates_[a.getHashCode()], this.updateCurrentState_(a))
};
goog.editor.plugins.UndoRedo.prototype.handleBeforeChange_ = function (a) {
    if (!this.inProgressUndo_) {
        a = a.target;
        var b = a.getHashCode();
        this.initialFieldChange_ != b && (this.initialFieldChange_ = b, this.updateCurrentState_(a))
    }
};
goog.editor.plugins.UndoRedo.prototype.handleDelayedChange_ = function (a) {
    this.inProgressUndo_ ? (a = this.inProgressUndo_, this.inProgressUndo_ = null, a.dispatchEvent(goog.editor.plugins.UndoRedoState.ACTION_COMPLETED)) : this.updateCurrentState_(a.target)
};
goog.editor.plugins.UndoRedo.prototype.handleBlur_ = function (a) {
    (a = a.target) && a.clearDelayedChange()
};
goog.editor.plugins.UndoRedo.prototype.getCursorPosition_ = function (a) {
    a = new goog.editor.plugins.UndoRedo.CursorPosition_(a);
    return a.isValid() ? a : null
};
goog.editor.plugins.UndoRedo.prototype.updateCurrentState_ = function (a) {
    var b = a.getHashCode(), c, d;
    a.queryCommandValue(goog.editor.Command.USING_LOREM) ? (c = "", d = null) : (c = a.getElement().innerHTML, d = this.getCursorPosition_(a));
    var e = this.currentStates_[b];
    if (e) {
        if (e.undoContent_ == c)return;
        if ("" == c || "" == e.undoContent_)if (a = a.getInjectableContents("", {}), c == a && "" == e.undoContent_ || e.undoContent_ == a && "" == c)return;
        e.setRedoState(c, d);
        this.undoManager_.addState(e)
    }
    this.currentStates_[b] = new goog.editor.plugins.UndoRedo.UndoState_(b,
        c, d, this.boundRestoreState_)
};
goog.editor.plugins.UndoRedo.UndoState_ = function (a, b, c, d) {
    goog.editor.plugins.UndoRedoState.call(this, !0);
    this.fieldHashCode = a;
    this.restore_ = d;
    this.setUndoState(b, c)
};
goog.inherits(goog.editor.plugins.UndoRedo.UndoState_, goog.editor.plugins.UndoRedoState);
goog.editor.plugins.UndoRedo.UndoState_.prototype.undo = function () {
    this.restore_(this, this.undoContent_, this.undoCursorPosition_)
};
goog.editor.plugins.UndoRedo.UndoState_.prototype.redo = function () {
    this.restore_(this, this.redoContent_, this.redoCursorPosition_)
};
goog.editor.plugins.UndoRedo.UndoState_.prototype.setUndoState = function (a, b) {
    this.undoContent_ = a;
    this.undoCursorPosition_ = b
};
goog.editor.plugins.UndoRedo.UndoState_.prototype.setRedoState = function (a, b) {
    this.redoContent_ = a;
    this.redoCursorPosition_ = b
};
goog.editor.plugins.UndoRedo.UndoState_.prototype.equals = function (a) {
    return this.fieldHashCode == a.fieldHashCode && this.undoContent_ == a.undoContent_ && this.redoContent_ == a.redoContent_
};
goog.editor.plugins.UndoRedo.CursorPosition_ = function (a) {
    this.field_ = a;
    var b = a.getEditableDomHelper().getWindow();
    a = (a = a.getRange()) && a.isRangeInDocument() && a.getWindow() == b ? a : null;
    goog.editor.BrowserFeature.HAS_W3C_RANGES ? this.initW3C_(a) : goog.editor.BrowserFeature.HAS_IE_RANGES && this.initIE_(a)
};
goog.editor.plugins.UndoRedo.CursorPosition_.prototype.initW3C_ = function (a) {
    this.isValid_ = !1;
    if (a) {
        var b = a.getAnchorNode(), c = a.getFocusNode();
        if (b && c) {
            var d = a.getAnchorOffset(), b = new goog.dom.NodeOffset(b, this.field_.getElement()), e = a.getFocusOffset(), c = new goog.dom.NodeOffset(c, this.field_.getElement());
            a.isReversed() ? (this.startOffset_ = c, this.startChildOffset_ = e, this.endOffset_ = b, this.endChildOffset_ = d) : (this.startOffset_ = b, this.startChildOffset_ = d, this.endOffset_ = c, this.endChildOffset_ = e);
            this.isValid_ = !0
        }
    }
};
goog.editor.plugins.UndoRedo.CursorPosition_.prototype.initIE_ = function (a) {
    this.isValid_ = !1;
    if (a) {
        var b = a.getTextRange(0).getBrowserRangeObject();
        if (goog.dom.contains(this.field_.getElement(), b.parentElement())) {
            a = this.field_.getEditableDomHelper().getDocument().body.createTextRange();
            a.moveToElementText(this.field_.getElement());
            var c = b.duplicate();
            c.collapse(!0);
            c.setEndPoint("StartToStart", a);
            this.startOffset_ = goog.editor.plugins.UndoRedo.CursorPosition_.computeEndOffsetIE_(c);
            b = b.duplicate();
            b.setEndPoint("StartToStart",
                a);
            this.endOffset_ = goog.editor.plugins.UndoRedo.CursorPosition_.computeEndOffsetIE_(b);
            this.isValid_ = !0
        }
    }
};
goog.editor.plugins.UndoRedo.CursorPosition_.prototype.isValid = function () {
    return this.isValid_
};
goog.editor.plugins.UndoRedo.CursorPosition_.prototype.toString = function () {
    return goog.editor.BrowserFeature.HAS_W3C_RANGES ? "W3C:" + this.startOffset_.toString() + "\n" + this.startChildOffset_ + ":" + this.endOffset_.toString() + "\n" + this.endChildOffset_ : "IE:" + this.startOffset_ + "," + this.endOffset_
};
goog.editor.plugins.UndoRedo.CursorPosition_.prototype.select = function () {
    var a = this.getRange_(this.field_.getElement());
    a && (goog.editor.BrowserFeature.HAS_IE_RANGES && this.field_.getElement().focus(), goog.dom.Range.createFromBrowserRange(a).select())
};
goog.editor.plugins.UndoRedo.CursorPosition_.prototype.getRange_ = function (a) {
    if (goog.editor.BrowserFeature.HAS_W3C_RANGES) {
        var b = this.startOffset_.findTargetNode(a);
        a = this.endOffset_.findTargetNode(a);
        return b && a ? goog.dom.Range.createFromNodes(b, this.startChildOffset_, a, this.endChildOffset_).getBrowserRangeObject() : null
    }
    b = a.ownerDocument.body.createTextRange();
    b.moveToElementText(a);
    b.collapse(!0);
    b.moveEnd("character", this.endOffset_);
    b.moveStart("character", this.startOffset_);
    return b
};
goog.editor.plugins.UndoRedo.CursorPosition_.computeEndOffsetIE_ = function (a) {
    var b = a.duplicate(), c = a.text, d = c.length;
    b.collapse(!0);
    b.moveEnd("character", d);
    for (var e, f = 10; (e = b.compareEndPoints("EndToEnd", a)) && (d -= e, b.moveEnd("character", -e), --f, 0 != f););
    a = 0;
    for (b = c.indexOf("\n\r"); -1 != b;)++a, b = c.indexOf("\n\r", b + 1);
    return d + a
};
goog.editor.plugins.SpacesTabHandler = function () {
    goog.editor.plugins.AbstractTabHandler.call(this)
};
goog.inherits(goog.editor.plugins.SpacesTabHandler, goog.editor.plugins.AbstractTabHandler);
goog.editor.plugins.SpacesTabHandler.prototype.getTrogClassId = function () {
    return"SpacesTabHandler"
};
goog.editor.plugins.SpacesTabHandler.prototype.handleTabKey = function (a) {
    var b = this.getFieldDomHelper(), c = this.getFieldObject().getRange();
    return goog.editor.range.intersectsTag(c, goog.dom.TagName.LI) ? !1 : (a.shiftKey || (this.getFieldObject().stopChangeEvents(!0, !0), c.isCollapsed() || (b.getDocument().execCommand("delete", !1, null), c = this.getFieldObject().getRange()), b = b.createDom("span", null, "\u00a0\u00a0 \u00a0"), b = c.insertNode(b, !1), this.getFieldObject().dispatchChange(), goog.editor.range.placeCursorNextTo(b,
        !1), this.getFieldObject().dispatchSelectionChangeEvent()), a.preventDefault(), !0)
};
var myField;
widjdev.setEditor = function () {
    myField = new goog.editor.Field("editorDiv");
    myField.registerPlugin(new goog.editor.plugins.BasicTextFormatter);
    myField.registerPlugin(new goog.editor.plugins.RemoveFormatting);
    myField.registerPlugin(new goog.editor.plugins.UndoRedo);
    myField.registerPlugin(new goog.editor.plugins.ListTabHandler);
    myField.registerPlugin(new goog.editor.plugins.SpacesTabHandler);
    myField.registerPlugin(new goog.editor.plugins.EnterHandler);
    myField.registerPlugin(new goog.editor.plugins.HeaderFormatter);
    myField.registerPlugin(new goog.editor.plugins.LoremIpsum("Click here to edit"));
    myField.registerPlugin(new goog.editor.plugins.LinkDialogPlugin);
    myField.registerPlugin(new goog.editor.plugins.LinkBubble);
    var a = goog.ui.editor.DefaultToolbar.makeToolbar([goog.editor.Command.BOLD, goog.editor.Command.ITALIC, goog.editor.Command.UNDERLINE, goog.editor.Command.FONT_COLOR, goog.editor.Command.BACKGROUND_COLOR, goog.editor.Command.FONT_FACE, goog.editor.Command.FONT_SIZE, goog.editor.Command.LINK, goog.editor.Command.UNDO, goog.editor.Command.REDO, goog.editor.Command.UNORDERED_LIST, goog.editor.Command.ORDERED_LIST,
        goog.editor.Command.INDENT, goog.editor.Command.OUTDENT, goog.editor.Command.JUSTIFY_LEFT, goog.editor.Command.JUSTIFY_CENTER, goog.editor.Command.JUSTIFY_RIGHT, goog.editor.Command.SUBSCRIPT, goog.editor.Command.SUPERSCRIPT, goog.editor.Command.STRIKE_THROUGH, goog.editor.Command.REMOVE_FORMAT], goog.dom.getElement("toolbar"));
    new goog.ui.editor.ToolbarController(myField, a);
    myField.makeEditable()
};
widjdev.setEditor.leftPanelsetup = function (a, b) {
    var c = goog.ui.decorate(goog.dom.getElement(a));
    c.setDispatchTransitionEvents(goog.ui.Component.State.ACTION, !0);
    goog.events.listen(c, goog.ui.Component.EventType.ACTION, b)
};
widjdev.setEditor.getcontents = function () {
    myField || alert("contents from widgdev myField is null");
    var a = myField.getElement();
    myField.getCleanContents();
    return a.innerHTML
};
widjdev.setEditor.setcontents = function (a) {
    myField.setHtml(!1, a, !0, !1)
};
goog.exportSymbol("widjdev.setEditor", widjdev.setEditor);
goog.exportSymbol("widjdev.setEditor.getcontents", widjdev.setEditor.getcontents);
goog.exportSymbol("widjdev.setEditor.setcontents", widjdev.setEditor.setcontents);
goog.exportSymbol("widjdev.setEditor.leftPanelsetup", widjdev.setEditor.leftPanelsetup);
goog.ui.ItemEvent = function (a, b, c) {
    goog.events.Event.call(this, a, b);
    this.item = c
};
goog.inherits(goog.ui.ItemEvent, goog.events.Event);
goog.dom.classlist = {};
goog.dom.classlist.ALWAYS_USE_DOM_TOKEN_LIST = !1;
goog.dom.classlist.NATIVE_DOM_TOKEN_LIST_ = goog.dom.classlist.ALWAYS_USE_DOM_TOKEN_LIST || !!goog.global.DOMTokenList;
goog.dom.classlist.get = goog.dom.classlist.NATIVE_DOM_TOKEN_LIST_ ? function (a) {
    return a.classList
} : function (a) {
    a = a.className;
    return goog.isString(a) && a.match(/\S+/g) || []
};
goog.dom.classlist.set = function (a, b) {
    a.className = b
};
goog.dom.classlist.contains = goog.dom.classlist.NATIVE_DOM_TOKEN_LIST_ ? function (a, b) {
    goog.asserts.assert(!!a.classList);
    return a.classList.contains(b)
} : function (a, b) {
    return goog.array.contains(goog.dom.classlist.get(a), b)
};
goog.dom.classlist.add = goog.dom.classlist.NATIVE_DOM_TOKEN_LIST_ ? function (a, b) {
    a.classList.add(b)
} : function (a, b) {
    goog.dom.classlist.contains(a, b) || (a.className += 0 < a.className.length ? " " + b : b)
};
goog.dom.classlist.addAll = goog.dom.classlist.NATIVE_DOM_TOKEN_LIST_ ? function (a, b) {
    goog.array.forEach(b, function (b) {
        goog.dom.classlist.add(a, b)
    })
} : function (a, b) {
    var c = {};
    goog.array.forEach(goog.dom.classlist.get(a), function (a) {
        c[a] = !0
    });
    goog.array.forEach(b, function (a) {
        c[a] = !0
    });
    a.className = "";
    for (var d in c)a.className += 0 < a.className.length ? " " + d : d
};
goog.dom.classlist.remove = goog.dom.classlist.NATIVE_DOM_TOKEN_LIST_ ? function (a, b) {
    a.classList.remove(b)
} : function (a, b) {
    goog.dom.classlist.contains(a, b) && (a.className = goog.array.filter(goog.dom.classlist.get(a),function (a) {
        return a != b
    }).join(" "))
};
goog.dom.classlist.removeAll = goog.dom.classlist.NATIVE_DOM_TOKEN_LIST_ ? function (a, b) {
    goog.array.forEach(b, function (b) {
        goog.dom.classlist.remove(a, b)
    })
} : function (a, b) {
    a.className = goog.array.filter(goog.dom.classlist.get(a),function (a) {
        return!goog.array.contains(b, a)
    }).join(" ")
};
goog.dom.classlist.enable = function (a, b, c) {
    c ? goog.dom.classlist.add(a, b) : goog.dom.classlist.remove(a, b)
};
goog.dom.classlist.swap = function (a, b, c) {
    return goog.dom.classlist.contains(a, b) ? (goog.dom.classlist.remove(a, b), goog.dom.classlist.add(a, c), !0) : !1
};
goog.dom.classlist.toggle = function (a, b) {
    var c = !goog.dom.classlist.contains(a, b);
    goog.dom.classlist.enable(a, b, c);
    return c
};
goog.dom.classlist.addRemove = function (a, b, c) {
    goog.dom.classlist.remove(a, b);
    goog.dom.classlist.add(a, c)
};
goog.ui.LabelInput = function (a, b) {
    goog.ui.Component.call(this, b);
    this.label_ = a || ""
};
goog.inherits(goog.ui.LabelInput, goog.ui.Component);
goog.ui.LabelInput.prototype.ffKeyRestoreValue_ = null;
goog.ui.LabelInput.prototype.labelRestoreDelayMs = 10;
goog.ui.LabelInput.SUPPORTS_PLACEHOLDER_ = "placeholder"in document.createElement("input");
goog.ui.LabelInput.prototype.hasFocus_ = !1;
goog.ui.LabelInput.prototype.createDom = function () {
    this.setElementInternal(this.getDomHelper().createDom("input", {type: "text"}))
};
goog.ui.LabelInput.prototype.decorateInternal = function (a) {
    goog.ui.LabelInput.superClass_.decorateInternal.call(this, a);
    this.label_ || (this.label_ = a.getAttribute("label") || "");
    goog.dom.getActiveElement(goog.dom.getOwnerDocument(a)) == a && (this.hasFocus_ = !0, a = this.getElement(), goog.asserts.assert(a), goog.dom.classlist.remove(a, this.LABEL_CLASS_NAME));
    goog.ui.LabelInput.SUPPORTS_PLACEHOLDER_ ? this.getElement().placeholder = this.label_ : (a = this.getElement(), goog.asserts.assert(a, "The label input element cannot be null."),
        goog.a11y.aria.setState(a, goog.a11y.aria.State.LABEL, this.label_))
};
goog.ui.LabelInput.prototype.enterDocument = function () {
    goog.ui.LabelInput.superClass_.enterDocument.call(this);
    this.attachEvents_();
    this.check_();
    this.getElement().labelInput_ = this
};
goog.ui.LabelInput.prototype.exitDocument = function () {
    goog.ui.LabelInput.superClass_.exitDocument.call(this);
    this.detachEvents_();
    this.getElement().labelInput_ = null
};
goog.ui.LabelInput.prototype.attachEvents_ = function () {
    var a = new goog.events.EventHandler(this);
    a.listen(this.getElement(), goog.events.EventType.FOCUS, this.handleFocus_);
    a.listen(this.getElement(), goog.events.EventType.BLUR, this.handleBlur_);
    if (goog.ui.LabelInput.SUPPORTS_PLACEHOLDER_)this.eventHandler_ = a; else {
        goog.userAgent.GECKO && a.listen(this.getElement(), [goog.events.EventType.KEYPRESS, goog.events.EventType.KEYDOWN, goog.events.EventType.KEYUP], this.handleEscapeKeys_);
        var b = goog.dom.getOwnerDocument(this.getElement()),
            b = goog.dom.getWindow(b);
        a.listen(b, goog.events.EventType.LOAD, this.handleWindowLoad_);
        this.eventHandler_ = a;
        this.attachEventsToForm_()
    }
};
goog.ui.LabelInput.prototype.attachEventsToForm_ = function () {
    !this.formAttached_ && (this.eventHandler_ && this.getElement().form) && (this.eventHandler_.listen(this.getElement().form, goog.events.EventType.SUBMIT, this.handleFormSubmit_), this.formAttached_ = !0)
};
goog.ui.LabelInput.prototype.detachEvents_ = function () {
    this.eventHandler_ && (this.eventHandler_.dispose(), this.eventHandler_ = null)
};
goog.ui.LabelInput.prototype.disposeInternal = function () {
    goog.ui.LabelInput.superClass_.disposeInternal.call(this);
    this.detachEvents_()
};
goog.ui.LabelInput.prototype.LABEL_CLASS_NAME = "label-input-label";
goog.ui.LabelInput.prototype.handleFocus_ = function (a) {
    this.hasFocus_ = !0;
    a = this.getElement();
    goog.asserts.assert(a);
    goog.dom.classlist.remove(a, this.LABEL_CLASS_NAME);
    if (!goog.ui.LabelInput.SUPPORTS_PLACEHOLDER_ && !this.hasChanged() && !this.inFocusAndSelect_) {
        var b = this;
        a = function () {
            b.getElement() && (b.getElement().value = "")
        };
        goog.userAgent.IE ? goog.Timer.callOnce(a, 10) : a()
    }
};
goog.ui.LabelInput.prototype.handleBlur_ = function (a) {
    goog.ui.LabelInput.SUPPORTS_PLACEHOLDER_ || (this.eventHandler_.unlisten(this.getElement(), goog.events.EventType.CLICK, this.handleFocus_), this.ffKeyRestoreValue_ = null);
    this.hasFocus_ = !1;
    this.check_()
};
goog.ui.LabelInput.prototype.handleEscapeKeys_ = function (a) {
    27 == a.keyCode && (a.type == goog.events.EventType.KEYDOWN ? this.ffKeyRestoreValue_ = this.getElement().value : a.type == goog.events.EventType.KEYPRESS ? this.getElement().value = this.ffKeyRestoreValue_ : a.type == goog.events.EventType.KEYUP && (this.ffKeyRestoreValue_ = null), a.preventDefault())
};
goog.ui.LabelInput.prototype.handleFormSubmit_ = function (a) {
    this.hasChanged() || (this.getElement().value = "", goog.Timer.callOnce(this.handleAfterSubmit_, 10, this))
};
goog.ui.LabelInput.prototype.handleAfterSubmit_ = function (a) {
    this.hasChanged() || (this.getElement().value = this.label_)
};
goog.ui.LabelInput.prototype.handleWindowLoad_ = function (a) {
    this.check_()
};
goog.ui.LabelInput.prototype.hasFocus = function () {
    return this.hasFocus_
};
goog.ui.LabelInput.prototype.hasChanged = function () {
    return!!this.getElement() && "" != this.getElement().value && this.getElement().value != this.label_
};
goog.ui.LabelInput.prototype.clear = function () {
    this.getElement().value = "";
    null != this.ffKeyRestoreValue_ && (this.ffKeyRestoreValue_ = "")
};
goog.ui.LabelInput.prototype.reset = function () {
    this.hasChanged() && (this.clear(), this.check_())
};
goog.ui.LabelInput.prototype.setValue = function (a) {
    null != this.ffKeyRestoreValue_ && (this.ffKeyRestoreValue_ = a);
    this.getElement().value = a;
    this.check_()
};
goog.ui.LabelInput.prototype.getValue = function () {
    return null != this.ffKeyRestoreValue_ ? this.ffKeyRestoreValue_ : this.hasChanged() ? this.getElement().value : ""
};
goog.ui.LabelInput.prototype.setLabel = function (a) {
    goog.ui.LabelInput.SUPPORTS_PLACEHOLDER_ ? (this.label_ = a, this.getElement() && (this.getElement().placeholder = this.label_)) : (this.getElement() && !this.hasChanged() && (this.getElement().value = ""), this.label_ = a, this.restoreLabel_(), (a = this.getElement()) && goog.a11y.aria.setState(a, goog.a11y.aria.State.LABEL, this.label_))
};
goog.ui.LabelInput.prototype.getLabel = function () {
    return this.label_
};
goog.ui.LabelInput.prototype.check_ = function () {
    var a = this.getElement();
    goog.asserts.assert(a, "The label input element cannot be null.");
    goog.ui.LabelInput.SUPPORTS_PLACEHOLDER_ ? this.getElement().placeholder != this.label_ && (this.getElement().placeholder = this.label_) : (this.attachEventsToForm_(), goog.a11y.aria.setState(a, goog.a11y.aria.State.LABEL, this.label_));
    this.hasChanged() ? (a = this.getElement(), goog.asserts.assert(a), goog.dom.classlist.remove(a, this.LABEL_CLASS_NAME)) : (this.inFocusAndSelect_ || this.hasFocus_ ||
        (a = this.getElement(), goog.asserts.assert(a), goog.dom.classlist.add(a, this.LABEL_CLASS_NAME)), goog.ui.LabelInput.SUPPORTS_PLACEHOLDER_ || goog.Timer.callOnce(this.restoreLabel_, this.labelRestoreDelayMs, this))
};
goog.ui.LabelInput.prototype.focusAndSelect = function () {
    var a = this.hasChanged();
    this.inFocusAndSelect_ = !0;
    this.getElement().focus();
    a || goog.ui.LabelInput.SUPPORTS_PLACEHOLDER_ || (this.getElement().value = this.label_);
    this.getElement().select();
    goog.ui.LabelInput.SUPPORTS_PLACEHOLDER_ || (this.eventHandler_ && this.eventHandler_.listenOnce(this.getElement(), goog.events.EventType.CLICK, this.handleFocus_), goog.Timer.callOnce(this.focusAndSelect_, 10, this))
};
goog.ui.LabelInput.prototype.setEnabled = function (a) {
    this.getElement().disabled = !a;
    var b = this.getElement();
    goog.asserts.assert(b);
    goog.dom.classlist.enable(b, this.LABEL_CLASS_NAME + "-disabled", !a)
};
goog.ui.LabelInput.prototype.isEnabled = function () {
    return!this.getElement().disabled
};
goog.ui.LabelInput.prototype.focusAndSelect_ = function () {
    this.inFocusAndSelect_ = !1
};
goog.ui.LabelInput.prototype.restoreLabel_ = function () {
    !this.getElement() || (this.hasChanged() || this.hasFocus_) || (this.getElement().value = this.label_)
};
goog.ui.ComboBox = function (a, b) {
    goog.ui.Component.call(this, a);
    this.labelInput_ = new goog.ui.LabelInput;
    this.enabled_ = !0;
    this.menu_ = b || new goog.ui.Menu(this.getDomHelper());
    this.setupMenu_()
};
goog.inherits(goog.ui.ComboBox, goog.ui.Component);
goog.ui.ComboBox.BLUR_DISMISS_TIMER_MS = 250;
goog.ui.ComboBox.prototype.logger_ = goog.log.getLogger("goog.ui.ComboBox");
goog.ui.ComboBox.prototype.inputHandler_ = null;
goog.ui.ComboBox.prototype.lastToken_ = null;
goog.ui.ComboBox.prototype.labelInput_ = null;
goog.ui.ComboBox.prototype.menu_ = null;
goog.ui.ComboBox.prototype.visibleCount_ = -1;
goog.ui.ComboBox.prototype.input_ = null;
goog.ui.ComboBox.prototype.matchFunction_ = goog.string.startsWith;
goog.ui.ComboBox.prototype.button_ = null;
goog.ui.ComboBox.prototype.defaultText_ = "";
goog.ui.ComboBox.prototype.fieldName_ = "";
goog.ui.ComboBox.prototype.dismissTimer_ = null;
goog.ui.ComboBox.prototype.useDropdownArrow_ = !1;
goog.ui.ComboBox.prototype.createDom = function () {
    this.input_ = this.getDomHelper().createDom("input", {name: this.fieldName_, type: "text", autocomplete: "off"});
    this.button_ = this.getDomHelper().createDom("span", "goog-combobox-button");
    this.setElementInternal(this.getDomHelper().createDom("span", "goog-combobox", this.input_, this.button_));
    this.useDropdownArrow_ && (this.button_.innerHTML = "&#x25BC;", goog.style.setUnselectable(this.button_, !0));
    this.input_.setAttribute("label", this.defaultText_);
    this.labelInput_.decorate(this.input_);
    this.menu_.setFocusable(!1);
    this.menu_.isInDocument() || this.addChild(this.menu_, !0)
};
goog.ui.ComboBox.prototype.setEnabled = function (a) {
    this.enabled_ = a;
    this.labelInput_.setEnabled(a);
    goog.dom.classlist.enable(this.getElement(), "goog-combobox-disabled", !a)
};
goog.ui.ComboBox.prototype.enterDocument = function () {
    goog.ui.ComboBox.superClass_.enterDocument.call(this);
    var a = this.getHandler();
    a.listen(this.getElement(), goog.events.EventType.MOUSEDOWN, this.onComboMouseDown_);
    a.listen(this.getDomHelper().getDocument(), goog.events.EventType.MOUSEDOWN, this.onDocClicked_);
    a.listen(this.input_, goog.events.EventType.BLUR, this.onInputBlur_);
    this.keyHandler_ = new goog.events.KeyHandler(this.input_);
    a.listen(this.keyHandler_, goog.events.KeyHandler.EventType.KEY, this.handleKeyEvent);
    this.inputHandler_ = new goog.events.InputHandler(this.input_);
    a.listen(this.inputHandler_, goog.events.InputHandler.EventType.INPUT, this.onInputEvent_);
    a.listen(this.menu_, goog.ui.Component.EventType.ACTION, this.onMenuSelected_)
};
goog.ui.ComboBox.prototype.exitDocument = function () {
    this.keyHandler_.dispose();
    delete this.keyHandler_;
    this.inputHandler_.dispose();
    this.inputHandler_ = null;
    goog.ui.ComboBox.superClass_.exitDocument.call(this)
};
goog.ui.ComboBox.prototype.canDecorate = function () {
    return!1
};
goog.ui.ComboBox.prototype.disposeInternal = function () {
    goog.ui.ComboBox.superClass_.disposeInternal.call(this);
    this.clearDismissTimer_();
    this.labelInput_.dispose();
    this.menu_.dispose();
    this.button_ = this.input_ = this.menu_ = this.labelInput_ = null
};
goog.ui.ComboBox.prototype.dismiss = function () {
    this.clearDismissTimer_();
    this.hideMenu_();
    this.menu_.setHighlightedIndex(-1)
};
goog.ui.ComboBox.prototype.addItem = function (a) {
    this.menu_.addChild(a, !0);
    this.visibleCount_ = -1
};
goog.ui.ComboBox.prototype.addItemAt = function (a, b) {
    this.menu_.addChildAt(a, b, !0);
    this.visibleCount_ = -1
};
goog.ui.ComboBox.prototype.removeItem = function (a) {
    if (a = this.menu_.removeChild(a, !0))a.dispose(), this.visibleCount_ = -1
};
goog.ui.ComboBox.prototype.removeAllItems = function () {
    for (var a = this.getItemCount() - 1; 0 <= a; --a)this.removeItem(this.getItemAt(a))
};
goog.ui.ComboBox.prototype.removeItemAt = function (a) {
    if (a = this.menu_.removeChildAt(a, !0))a.dispose(), this.visibleCount_ = -1
};
goog.ui.ComboBox.prototype.getItemAt = function (a) {
    return this.menu_.getChildAt(a)
};
goog.ui.ComboBox.prototype.getItemCount = function () {
    return this.menu_.getChildCount()
};
goog.ui.ComboBox.prototype.getMenu = function () {
    return this.menu_
};
goog.ui.ComboBox.prototype.getInputElement = function () {
    return this.input_
};
goog.ui.ComboBox.prototype.getNumberOfVisibleItems_ = function () {
    if (-1 == this.visibleCount_) {
        for (var a = 0, b = 0, c = this.menu_.getChildCount(); b < c; b++) {
            var d = this.menu_.getChildAt(b);
            d instanceof goog.ui.MenuSeparator || !d.isVisible() || a++
        }
        this.visibleCount_ = a
    }
    goog.log.info(this.logger_, "getNumberOfVisibleItems() - " + this.visibleCount_);
    return this.visibleCount_
};
goog.ui.ComboBox.prototype.setMatchFunction = function (a) {
    this.matchFunction_ = a
};
goog.ui.ComboBox.prototype.getMatchFunction = function () {
    return this.matchFunction_
};
goog.ui.ComboBox.prototype.setDefaultText = function (a) {
    this.defaultText_ = a;
    this.labelInput_ && this.labelInput_.setLabel(this.defaultText_)
};
goog.ui.ComboBox.prototype.getDefaultText = function () {
    return this.defaultText_
};
goog.ui.ComboBox.prototype.setFieldName = function (a) {
    this.fieldName_ = a
};
goog.ui.ComboBox.prototype.getFieldName = function () {
    return this.fieldName_
};
goog.ui.ComboBox.prototype.setUseDropdownArrow = function (a) {
    this.useDropdownArrow_ = !!a
};
goog.ui.ComboBox.prototype.setValue = function (a) {
    goog.log.info(this.logger_, "setValue() - " + a);
    this.labelInput_.getValue() != a && (this.labelInput_.setValue(a), this.handleInputChange_())
};
goog.ui.ComboBox.prototype.getValue = function () {
    return this.labelInput_.getValue()
};
goog.ui.ComboBox.prototype.getToken = function () {
    return goog.string.htmlEscape(this.getTokenText_())
};
goog.ui.ComboBox.prototype.getTokenText_ = function () {
    return goog.string.trim(this.labelInput_.getValue().toLowerCase())
};
goog.ui.ComboBox.prototype.setupMenu_ = function () {
    var a = this.menu_;
    a.setVisible(!1);
    a.setAllowAutoFocus(!1);
    a.setAllowHighlightDisabled(!0)
};
goog.ui.ComboBox.prototype.maybeShowMenu_ = function (a) {
    var b = this.menu_.isVisible(), c = this.getNumberOfVisibleItems_();
    b && 0 == c ? (goog.log.fine(this.logger_, "no matching items, hiding"), this.hideMenu_()) : !b && 0 < c && (a && (goog.log.fine(this.logger_, "showing menu"), this.setItemVisibilityFromToken_(""), this.setItemHighlightFromToken_(this.getTokenText_())), goog.Timer.callOnce(this.clearDismissTimer_, 1, this), this.showMenu_());
    this.positionMenu()
};
goog.ui.ComboBox.prototype.positionMenu = function () {
    this.menu_ && this.menu_.isVisible() && (new goog.positioning.MenuAnchoredPosition(this.getElement(), goog.positioning.Corner.BOTTOM_START, !0)).reposition(this.menu_.getElement(), goog.positioning.Corner.TOP_START)
};
goog.ui.ComboBox.prototype.showMenu_ = function () {
    this.menu_.setVisible(!0);
    goog.dom.classlist.add(this.getElement(), "goog-combobox-active")
};
goog.ui.ComboBox.prototype.hideMenu_ = function () {
    this.menu_.setVisible(!1);
    goog.dom.classlist.remove(this.getElement(), "goog-combobox-active")
};
goog.ui.ComboBox.prototype.clearDismissTimer_ = function () {
    this.dismissTimer_ && (goog.Timer.clear(this.dismissTimer_), this.dismissTimer_ = null)
};
goog.ui.ComboBox.prototype.onComboMouseDown_ = function (a) {
    this.enabled_ && (a.target == this.getElement() || a.target == this.input_ || goog.dom.contains(this.button_, a.target)) && (this.menu_.isVisible() ? (goog.log.fine(this.logger_, "Menu is visible, dismissing"), this.dismiss()) : (goog.log.fine(this.logger_, "Opening dropdown"), this.maybeShowMenu_(!0), goog.userAgent.OPERA && this.input_.focus(), this.input_.select(), this.menu_.setMouseButtonPressed(!0), a.preventDefault()));
    a.stopPropagation()
};
goog.ui.ComboBox.prototype.onDocClicked_ = function (a) {
    goog.dom.contains(this.menu_.getElement(), a.target) || (goog.log.info(this.logger_, "onDocClicked_() - dismissing immediately"), this.dismiss())
};
goog.ui.ComboBox.prototype.onMenuSelected_ = function (a) {
    goog.log.info(this.logger_, "onMenuSelected_()");
    var b = a.target;
    this.dispatchEvent(new goog.ui.ItemEvent(goog.ui.Component.EventType.ACTION, this, b)) && (b = b.getCaption(), goog.log.fine(this.logger_, "Menu selection: " + b + ". Dismissing menu"), this.labelInput_.getValue() != b && (this.labelInput_.setValue(b), this.dispatchEvent(goog.ui.Component.EventType.CHANGE)), this.dismiss());
    a.stopPropagation()
};
goog.ui.ComboBox.prototype.onInputBlur_ = function (a) {
    goog.log.info(this.logger_, "onInputBlur_() - delayed dismiss");
    this.clearDismissTimer_();
    this.dismissTimer_ = goog.Timer.callOnce(this.dismiss, goog.ui.ComboBox.BLUR_DISMISS_TIMER_MS, this)
};
goog.ui.ComboBox.prototype.handleKeyEvent = function (a) {
    var b = this.menu_.isVisible();
    if (b && this.menu_.handleKeyEvent(a))return!0;
    var c = !1;
    switch (a.keyCode) {
        case goog.events.KeyCodes.ESC:
            b && (goog.log.fine(this.logger_, "Dismiss on Esc: " + this.labelInput_.getValue()), this.dismiss(), c = !0);
            break;
        case goog.events.KeyCodes.TAB:
            b && (b = this.menu_.getHighlighted()) && (goog.log.fine(this.logger_, "Select on Tab: " + this.labelInput_.getValue()), b.performActionInternal(a), c = !0);
            break;
        case goog.events.KeyCodes.UP:
        case goog.events.KeyCodes.DOWN:
            b ||
            (goog.log.fine(this.logger_, "Up/Down - maybe show menu"), this.maybeShowMenu_(!0), c = !0)
    }
    c && a.preventDefault();
    return c
};
goog.ui.ComboBox.prototype.onInputEvent_ = function (a) {
    goog.log.fine(this.logger_, "Key is modifying: " + this.labelInput_.getValue());
    this.handleInputChange_()
};
goog.ui.ComboBox.prototype.handleInputChange_ = function () {
    var a = this.getTokenText_();
    this.setItemVisibilityFromToken_(a);
    goog.dom.getActiveElement(this.getDomHelper().getDocument()) == this.input_ && this.maybeShowMenu_(!1);
    var b = this.menu_.getHighlighted();
    "" != a && b && b.isVisible() || this.setItemHighlightFromToken_(a);
    this.lastToken_ = a;
    this.dispatchEvent(goog.ui.Component.EventType.CHANGE)
};
goog.ui.ComboBox.prototype.setItemVisibilityFromToken_ = function (a) {
    goog.log.info(this.logger_, "setItemVisibilityFromToken_() - " + a);
    for (var b = !1, c = 0, d = !this.matchFunction_(a, this.lastToken_), e = 0, f = this.menu_.getChildCount(); e < f; e++) {
        var g = this.menu_.getChildAt(e);
        if (g instanceof goog.ui.MenuSeparator)g.setVisible(b), b = !1; else if (g instanceof goog.ui.MenuItem) {
            if (!g.isVisible() && !d)continue;
            var h = g.getCaption(), h = this.isItemSticky_(g) || h && this.matchFunction_(h.toLowerCase(), a);
            "function" == typeof g.setFormatFromToken &&
            g.setFormatFromToken(a);
            g.setVisible(!!h);
            b = h || b
        } else b = g.isVisible() || b;
        g instanceof goog.ui.MenuSeparator || !g.isVisible() || c++
    }
    this.visibleCount_ = c
};
goog.ui.ComboBox.prototype.setItemHighlightFromToken_ = function (a) {
    goog.log.info(this.logger_, "setItemHighlightFromToken_() - " + a);
    if ("" != a)for (var b = 0, c = this.menu_.getChildCount(); b < c; b++) {
        var d = this.menu_.getChildAt(b), e = d.getCaption();
        if (e && this.matchFunction_(e.toLowerCase(), a)) {
            this.menu_.setHighlightedIndex(b);
            d.setFormatFromToken && d.setFormatFromToken(a);
            return
        }
    }
    this.menu_.setHighlightedIndex(-1)
};
goog.ui.ComboBox.prototype.isItemSticky_ = function (a) {
    return"function" == typeof a.isSticky && a.isSticky()
};
goog.ui.ComboBoxItem = function (a, b, c, d) {
    goog.ui.MenuItem.call(this, a, b, c, d)
};
goog.inherits(goog.ui.ComboBoxItem, goog.ui.MenuItem);
goog.ui.registry.setDecoratorByClassName("goog-combobox-item", function () {
    return new goog.ui.ComboBoxItem(null)
});
goog.ui.ComboBoxItem.prototype.isSticky_ = !1;
goog.ui.ComboBoxItem.prototype.setSticky = function (a) {
    this.isSticky_ = a
};
goog.ui.ComboBoxItem.prototype.isSticky = function () {
    return this.isSticky_
};
goog.ui.ComboBoxItem.prototype.setFormatFromToken = function (a) {
    if (this.isEnabled()) {
        var b = this.getCaption(), c = b.toLowerCase().indexOf(a);
        if (0 <= c) {
            var d = this.getDomHelper();
            this.setContent([d.createTextNode(b.substr(0, c)), d.createDom("b", null, b.substr(c, a.length)), d.createTextNode(b.substr(c + a.length))])
        }
    }
};
widjdev.SnipEditor = function (a) {
    widjdev.SnipEditor.createSnipEditorDom();
    widjdev.setEditor();
    "undefined" != typeof a ? (console.log(a), this.selectedSnip = a, widjdev.SnipEditor.setEditorValues(a)) : this.selectedSnip = {}
};
widjdev.SnipEditor.createSnipEditorDom = function () {
    var a = new goog.ui.ToggleButton([goog.dom.createDom("div", "icon insert-image-icon goog-inline-block"), goog.dom.createDom("span", {style: "vertical-align:middle"}, "Insert Image")]), b = goog.dom.createDom("span", {"class": "toprow3"}, null);
    a.render(b);
    var a = goog.dom.createDom("div", {"class": "toprowContainer"}, b), c = goog.dom.createDom("span", {"class": "snipLabel"}, "Category:"), d = goog.dom.createDom("span", {"class": "catComboParent"}, null);
    this.catCombo = createCategoryComboBox();
    this.catCombo.render(d);
    var e = goog.dom.createDom("span", {"class": "snipLabel"}, "Subcategory:"), f = goog.dom.createDom("span", {"class": "subCatComboParent"}, null);
    this.subCatCombo = createSubCategoryComboBox();
    this.subCatCombo.render(f);
    this.subCatCombo.setEnabled(!1);
    var g = goog.dom.createDom("span", {"class": "snipLabel"}, "Tag:"), h = goog.dom.createDom("input", {id: "tagInput"}, null), b = goog.dom.createDom("div", {"class": "secondrowContainer"}, null);
    goog.dom.appendChild(b, c);
    goog.dom.appendChild(b, d);
    goog.dom.appendChild(b,
        e);
    goog.dom.appendChild(b, f);
    goog.dom.appendChild(b, g);
    goog.dom.appendChild(b, h);
    c = goog.dom.createDom("div", {"class": "titlerowContainer"}, null);
    d = goog.dom.createDom("span", {"class": "snipLabel"}, "Title:");
    goog.dom.appendChild(c, d);
    this.inputTitle = goog.dom.createDom("input", {"class": "snipTitle"}, null);
    goog.dom.appendChild(c, this.inputTitle);
    d = goog.dom.createDom("div", {id: "editorDiv", "class": "editorDiv"}, null);
    e = goog.dom.createDom("div", {id: "toolbar", "class": "toolbar"}, null);
    f = goog.dom.createDom("div",
        {"class": "editorToolbarDiv"}, null);
    g = goog.dom.getElement("editorContainer");
    goog.dom.appendChild(f, a);
    goog.dom.appendChild(f, b);
    goog.dom.appendChild(f, c);
    goog.dom.appendChild(f, e);
    goog.dom.appendChild(g, f);
    goog.dom.appendChild(g, d);
    this.tagAc = createTagAutoComplete()
};
widjdev.SnipEditor.setSnipComboBox = function (a) {
    this.snipData = a;
    var b = goog.dom.createDom("span", {"class": "clearEditorBtnParent"}, null), c = new goog.ui.CustomButton("New Snip");
    c.render(b);
    this.snipCombo = new goog.ui.ComboBox;
    this.snipCombo.setUseDropdownArrow(!0);
    this.snipCombo.setDefaultText("Select a snip to edit");
    for (var d = 0; d < a.length; d++) {
        var e = eval("(" + a[d][d] + ")"), f = new goog.ui.ComboBoxItem(e.title);
        f.setValue(d);
        f.setCaption(e.title);
        this.snipCombo.addItem(f)
    }
    a = goog.dom.getElement("snipComboParent");
    a.appendChild(b);
    this.snipCombo.render(a);
    goog.events.listen(this.snipCombo, "change", handleSnipComboChangeEvent);
    goog.events.listen(c, "action", handleClearBtnEvent)
};
function handleSnipComboChangeEvent(a) {
    a = a.target.menu_.getHighlighted().getValue();
    a = eval("(" + widjdev.SnipEditor.snipData[a][a] + ")");
    widjdev.setEditor.setcontents(a.content);
    widjdev.SnipEditor.catCombo.setValue(a.coreCat);
    widjdev.SnipEditor.subCatCombo.setValue(a.subCat);
    widjdev.SnipEditor.inputTitle.value = a.title;
    widjdev.SnipEditor.selectedSnip = a
}
widjdev.SnipEditor.setEditorValues = function (a) {
    widjdev.setEditor.setcontents(a.content);
    widjdev.SnipEditor.catCombo.setValue(a.coreCat);
    widjdev.SnipEditor.subCatCombo.setValue(a.subCat);
    widjdev.SnipEditor.inputTitle.value = a.title
};
function handleClearBtnEvent(a) {
    widjdev.SnipEditor.clearEditor()
}
widjdev.SnipEditor.clearEditor = function () {
    widjdev.setEditor.setcontents("");
    widjdev.SnipEditor.catCombo.setValue("");
    widjdev.SnipEditor.subCatCombo.setValue("");
    widjdev.SnipEditor.snipCombo.setValue("");
    widjdev.SnipEditor.inputTitle.value = "";
    widjdev.SnipEditor.selectedSnip = {}
};
widjdev.SnipEditor.getSnipData = function () {
    var a = {};
    a.title = goog.dom.getElementByClass("snipTitle").value;
    a.contentAsHtml = widjdev.setEditor.getcontents();
    a.contentAsString = widjdev.setEditor.getcontents();
    a.coreCat = this.catCombo.getValue();
    a.subCat = this.subCatCombo.getValue();
    var b = "";
    "undefined" != typeof this.selectedSnip && "undefined" != typeof this.selectedSnip.id && (b = this.selectedSnip.id);
    a.currentSnipId = b;
    return a
};
function createCategoryComboBox() {
    var a = new goog.ui.ComboBox;
    a.setUseDropdownArrow(!0);
    a.setDefaultText("Select a category");
    var b = new goog.ui.ComboBoxItem("Select a category");
    b.setSticky(!0);
    a.addItem(b);
    for (var c = 0; c < widjdev.categories.length; c++)b = new goog.ui.ComboBoxItem(widjdev.categories[c].name), b.setValue(c), a.addItem(b), b.element_.style.backgroundColor = widjdev.categories[c].colorCode;
    goog.events.listen(a, "change", handleCategoryComboChangeEvent);
    return a
}
function handleCategoryComboChangeEvent(a) {
    var b = a.target.menu_.getHighlighted().getValue();
    if ("undefined" != typeof widjdev.categories[b].subcategories) {
        widjdev.SnipEditor.subCatCombo.removeAllItems();
        a = new goog.ui.ComboBoxItem("Select a subcategory");
        a.setSticky(!0);
        widjdev.SnipEditor.subCatCombo.addItem(a);
        widjdev.SnipEditor.subCatCombo.setEnabled(!0);
        for (var b = widjdev.categories[b].subcategories, c = 0; c < b.length; c++)a = new goog.ui.ComboBoxItem(b[c]), widjdev.SnipEditor.subCatCombo.addItem(a)
    } else widjdev.SnipEditor.subCatCombo.setEnabled(!1)
}
function createSubCategoryComboBox() {
    var a = new goog.ui.ComboBox;
    a.setUseDropdownArrow(!0);
    a.setDefaultText("Select a subcategory");
    var b = new goog.ui.ComboBoxItem("Select a subcategory");
    b.setSticky(!0);
    a.addItem(b);
    return a
}
function createTagAutoComplete() {
    return goog.ui.ac.createSimpleAutoComplete("test data tags autocomplete auto tag1 habit".split(" "), document.getElementById("tagInput"), !1)
}
goog.exportSymbol("widjdev.SnipEditor", widjdev.SnipEditor);
goog.exportSymbol("widjdev.SnipEditor.setSnipComboBox", widjdev.SnipEditor.setSnipComboBox);
goog.exportSymbol("widjdev.SnipEditor.getSnipData", widjdev.SnipEditor.getSnipData);
goog.exportSymbol("widjdev.SnipEditor.clearEditor", widjdev.SnipEditor.clearEditor);
goog.exportSymbol("widjdev.SnipEditor.setEditorValues", widjdev.SnipEditor.setEditorValues);
