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
goog.ui = {};
goog.ui.IdGenerator = function () {
};
goog.addSingletonGetter(goog.ui.IdGenerator);
goog.ui.IdGenerator.prototype.nextId_ = 0;
goog.ui.IdGenerator.prototype.getNextUniqueId = function () {
    return":" + (this.nextId_++).toString(36)
};
goog.ui.IdGenerator.instance = goog.ui.IdGenerator.getInstance();
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
goog.ui.CustomButton = function (a, b, c) {
    goog.ui.Button.call(this, a, b || goog.ui.CustomButtonRenderer.getInstance(), c)
};
goog.inherits(goog.ui.CustomButton, goog.ui.Button);
goog.ui.registry.setDecoratorByClassName(goog.ui.CustomButtonRenderer.CSS_CLASS, function () {
    return new goog.ui.CustomButton(null)
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
goog.ui.Zippy = function (a, b, c, d, e) {
    function f(a) {
        a && (a.tabIndex = 0, goog.a11y.aria.setRole(a, g.getAriaRole()), goog.dom.classes.add(a, "goog-zippy-header"), g.enableMouseEventsHandling_(a), g.enableKeyboardEventsHandling_(a))
    }

    goog.events.EventTarget.call(this);
    this.dom_ = e || goog.dom.getDomHelper();
    this.elHeader_ = this.dom_.getElement(a) || null;
    this.elExpandedHeader_ = this.dom_.getElement(d || null);
    this.elContent_ = (this.lazyCreateFunc_ = goog.isFunction(b) ? b : null) || !b ? null : this.dom_.getElement(b);
    this.expanded_ =
        !0 == c;
    this.keyboardEventHandler_ = new goog.events.EventHandler(this);
    this.mouseEventHandler_ = new goog.events.EventHandler(this);
    var g = this;
    f(this.elHeader_);
    f(this.elExpandedHeader_);
    this.setExpanded(this.expanded_)
};
goog.inherits(goog.ui.Zippy, goog.events.EventTarget);
goog.ui.Zippy.Events = {ACTION: "action", TOGGLE: "toggle"};
goog.ui.Zippy.prototype.handleMouseEvents_ = !0;
goog.ui.Zippy.prototype.handleKeyEvents_ = !0;
goog.ui.Zippy.prototype.disposeInternal = function () {
    goog.ui.Zippy.superClass_.disposeInternal.call(this);
    goog.dispose(this.keyboardEventHandler_);
    goog.dispose(this.mouseEventHandler_)
};
goog.ui.Zippy.prototype.getAriaRole = function () {
    return goog.a11y.aria.Role.TAB
};
goog.ui.Zippy.prototype.getContentElement = function () {
    return this.elContent_
};
goog.ui.Zippy.prototype.getVisibleHeaderElement = function () {
    var a = this.elExpandedHeader_;
    return a && goog.style.isElementShown(a) ? a : this.elHeader_
};
goog.ui.Zippy.prototype.expand = function () {
    this.setExpanded(!0)
};
goog.ui.Zippy.prototype.collapse = function () {
    this.setExpanded(!1)
};
goog.ui.Zippy.prototype.toggle = function () {
    this.setExpanded(!this.expanded_)
};
goog.ui.Zippy.prototype.setExpanded = function (a) {
    this.elContent_ ? goog.style.setElementShown(this.elContent_, a) : a && this.lazyCreateFunc_ && (this.elContent_ = this.lazyCreateFunc_());
    this.elContent_ && goog.dom.classes.add(this.elContent_, "goog-zippy-content");
    this.elExpandedHeader_ ? (goog.style.setElementShown(this.elHeader_, !a), goog.style.setElementShown(this.elExpandedHeader_, a)) : this.updateHeaderClassName(a);
    this.setExpandedInternal(a);
    this.dispatchEvent(new goog.ui.ZippyEvent(goog.ui.Zippy.Events.TOGGLE,
        this, this.expanded_))
};
goog.ui.Zippy.prototype.setExpandedInternal = function (a) {
    this.expanded_ = a
};
goog.ui.Zippy.prototype.isExpanded = function () {
    return this.expanded_
};
goog.ui.Zippy.prototype.updateHeaderClassName = function (a) {
    this.elHeader_ && (goog.dom.classes.enable(this.elHeader_, "goog-zippy-expanded", a), goog.dom.classes.enable(this.elHeader_, "goog-zippy-collapsed", !a), goog.a11y.aria.setState(this.elHeader_, goog.a11y.aria.State.EXPANDED, a))
};
goog.ui.Zippy.prototype.isHandleKeyEvents = function () {
    return this.handleKeyEvents_
};
goog.ui.Zippy.prototype.isHandleMouseEvents = function () {
    return this.handleMouseEvents_
};
goog.ui.Zippy.prototype.setHandleKeyboardEvents = function (a) {
    this.handleKeyEvents_ != a && ((this.handleKeyEvents_ = a) ? (this.enableKeyboardEventsHandling_(this.elHeader_), this.enableKeyboardEventsHandling_(this.elExpandedHeader_)) : this.keyboardEventHandler_.removeAll())
};
goog.ui.Zippy.prototype.setHandleMouseEvents = function (a) {
    this.handleMouseEvents_ != a && ((this.handleMouseEvents_ = a) ? (this.enableMouseEventsHandling_(this.elHeader_), this.enableMouseEventsHandling_(this.elExpandedHeader_)) : this.mouseEventHandler_.removeAll())
};
goog.ui.Zippy.prototype.enableKeyboardEventsHandling_ = function (a) {
    a && this.keyboardEventHandler_.listen(a, goog.events.EventType.KEYDOWN, this.onHeaderKeyDown_)
};
goog.ui.Zippy.prototype.enableMouseEventsHandling_ = function (a) {
    a && this.mouseEventHandler_.listen(a, goog.events.EventType.CLICK, this.onHeaderClick_)
};
goog.ui.Zippy.prototype.onHeaderKeyDown_ = function (a) {
    if (a.keyCode == goog.events.KeyCodes.ENTER || a.keyCode == goog.events.KeyCodes.SPACE)this.toggle(), this.dispatchActionEvent_(), a.preventDefault(), a.stopPropagation()
};
goog.ui.Zippy.prototype.onHeaderClick_ = function (a) {
    this.toggle();
    this.dispatchActionEvent_()
};
goog.ui.Zippy.prototype.dispatchActionEvent_ = function () {
    this.dispatchEvent(new goog.events.Event(goog.ui.Zippy.Events.ACTION, this))
};
goog.ui.ZippyEvent = function (a, b, c) {
    goog.events.Event.call(this, a, b);
    this.expanded = c
};
goog.inherits(goog.ui.ZippyEvent, goog.events.Event);
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
goog.fx = {};
goog.fx.Transition = function () {
};
goog.fx.Transition.EventType = {PLAY: "play", BEGIN: "begin", RESUME: "resume", END: "end", STOP: "stop", FINISH: "finish", PAUSE: "pause"};
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
goog.positioning.ViewportPosition = function (a, b) {
    this.coordinate = a instanceof goog.math.Coordinate ? a : new goog.math.Coordinate(a, b)
};
goog.inherits(goog.positioning.ViewportPosition, goog.positioning.AbstractPosition);
goog.positioning.ViewportPosition.prototype.reposition = function (a, b, c, d) {
    goog.positioning.positionAtAnchor(goog.style.getClientViewportElement(a), goog.positioning.Corner.TOP_LEFT, a, b, this.coordinate, c, null, d)
};
goog.positioning.ClientPosition = function (a, b) {
    this.coordinate = a instanceof goog.math.Coordinate ? a : new goog.math.Coordinate(a, b)
};
goog.inherits(goog.positioning.ClientPosition, goog.positioning.AbstractPosition);
goog.positioning.ClientPosition.prototype.reposition = function (a, b, c, d) {
    goog.asserts.assert(a);
    var e = goog.style.getViewportPageOffset(goog.dom.getOwnerDocument(a)), f = this.coordinate.x + e.x, e = this.coordinate.y + e.y, g = goog.positioning.getOffsetParentPageOffset(a), f = f - g.x, e = e - g.y;
    goog.positioning.positionAtCoordinate(new goog.math.Coordinate(f, e), a, b, c, null, null, d)
};
goog.positioning.ViewportClientPosition = function (a, b) {
    goog.positioning.ClientPosition.call(this, a, b)
};
goog.inherits(goog.positioning.ViewportClientPosition, goog.positioning.ClientPosition);
goog.positioning.ViewportClientPosition.prototype.lastResortOverflow_ = 0;
goog.positioning.ViewportClientPosition.prototype.setLastResortOverflow = function (a) {
    this.lastResortOverflow_ = a
};
goog.positioning.ViewportClientPosition.prototype.reposition = function (a, b, c, d) {
    var e = goog.style.getClientViewportElement(a), e = goog.style.getVisibleRectForElement(e), f = goog.dom.getDomHelper(a).getDocumentScrollElement(), f = new goog.math.Coordinate(this.coordinate.x + f.scrollLeft, this.coordinate.y + f.scrollTop), g = goog.positioning.Overflow.FAIL_X | goog.positioning.Overflow.FAIL_Y, h = b, k = goog.positioning.positionAtCoordinate(f, a, h, c, e, g, d);
    if (0 != (k & goog.positioning.OverflowStatus.FAILED)) {
        if (k & goog.positioning.OverflowStatus.FAILED_LEFT ||
            k & goog.positioning.OverflowStatus.FAILED_RIGHT)h = goog.positioning.flipCornerHorizontal(h);
        if (k & goog.positioning.OverflowStatus.FAILED_TOP || k & goog.positioning.OverflowStatus.FAILED_BOTTOM)h = goog.positioning.flipCornerVertical(h);
        k = goog.positioning.positionAtCoordinate(f, a, h, c, e, g, d);
        0 != (k & goog.positioning.OverflowStatus.FAILED) && goog.positioning.positionAtCoordinate(f, a, b, c, e, this.lastResortOverflow_, d)
    }
};
goog.positioning.AbsolutePosition = function (a, b) {
    this.coordinate = a instanceof goog.math.Coordinate ? a : new goog.math.Coordinate(a, b)
};
goog.inherits(goog.positioning.AbsolutePosition, goog.positioning.AbstractPosition);
goog.positioning.AbsolutePosition.prototype.reposition = function (a, b, c, d) {
    goog.positioning.positionAtCoordinate(this.coordinate, a, b, c, null, null, d)
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
goog.ui.Popup = function (a, b) {
    this.popupCorner_ = goog.positioning.Corner.TOP_START;
    this.position_ = b || void 0;
    goog.ui.PopupBase.call(this, a)
};
goog.inherits(goog.ui.Popup, goog.ui.PopupBase);
goog.ui.Popup.Corner = goog.positioning.Corner;
goog.ui.Popup.Overflow = goog.positioning.Overflow;
goog.ui.Popup.prototype.getPinnedCorner = function () {
    return this.popupCorner_
};
goog.ui.Popup.prototype.setPinnedCorner = function (a) {
    this.popupCorner_ = a;
    this.isVisible() && this.reposition()
};
goog.ui.Popup.prototype.getPosition = function () {
    return this.position_ || null
};
goog.ui.Popup.prototype.setPosition = function (a) {
    this.position_ = a || void 0;
    this.isVisible() && this.reposition()
};
goog.ui.Popup.prototype.getMargin = function () {
    return this.margin_ || null
};
goog.ui.Popup.prototype.setMargin = function (a, b, c, d) {
    this.margin_ = null == a || a instanceof goog.math.Box ? a : new goog.math.Box(a, b, c, d);
    this.isVisible() && this.reposition()
};
goog.ui.Popup.prototype.reposition = function () {
    if (this.position_) {
        var a = !this.isVisible() && this.getType() != goog.ui.PopupBase.Type.MOVE_OFFSCREEN, b = this.getElement();
        a && (b.style.visibility = "hidden", goog.style.setElementShown(b, !0));
        this.position_.reposition(b, this.popupCorner_, this.margin_);
        a && goog.style.setElementShown(b, !1)
    }
};
goog.ui.Popup.AnchoredPosition = goog.positioning.AnchoredPosition;
goog.ui.Popup.AnchoredViewPortPosition = goog.positioning.AnchoredViewportPosition;
goog.ui.Popup.AbsolutePosition = goog.positioning.AbsolutePosition;
goog.ui.Popup.ViewPortPosition = goog.positioning.ViewportPosition;
goog.ui.Popup.ClientPosition = goog.positioning.ClientPosition;
goog.ui.Popup.ViewPortClientPosition = goog.positioning.ViewportClientPosition;
goog.structs = {};
goog.structs.Collection = function () {
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
goog.ui.Tooltip = function (a, b, c) {
    this.dom_ = c || (a ? goog.dom.getDomHelper(goog.dom.getElement(a)) : goog.dom.getDomHelper());
    goog.ui.Popup.call(this, this.dom_.createDom("div", {style: "position:absolute;display:none;"}));
    this.cursorPosition = new goog.math.Coordinate(1, 1);
    this.elements_ = new goog.structs.Set;
    a && this.attach(a);
    null != b && this.setText(b)
};
goog.inherits(goog.ui.Tooltip, goog.ui.Popup);
goog.ui.Tooltip.activeInstances_ = [];
goog.ui.Tooltip.prototype.activeEl_ = null;
goog.ui.Tooltip.prototype.className = "goog-tooltip";
goog.ui.Tooltip.prototype.showDelayMs_ = 500;
goog.ui.Tooltip.prototype.hideDelayMs_ = 0;
goog.ui.Tooltip.State = {INACTIVE: 0, WAITING_TO_SHOW: 1, SHOWING: 2, WAITING_TO_HIDE: 3, UPDATING: 4};
goog.ui.Tooltip.Activation = {CURSOR: 0, FOCUS: 1};
goog.ui.Tooltip.prototype.getDomHelper = function () {
    return this.dom_
};
goog.ui.Tooltip.prototype.getChildTooltip = function () {
    return this.childTooltip_
};
goog.ui.Tooltip.prototype.attach = function (a) {
    a = goog.dom.getElement(a);
    this.elements_.add(a);
    goog.events.listen(a, goog.events.EventType.MOUSEOVER, this.handleMouseOver, !1, this);
    goog.events.listen(a, goog.events.EventType.MOUSEOUT, this.handleMouseOutAndBlur, !1, this);
    goog.events.listen(a, goog.events.EventType.MOUSEMOVE, this.handleMouseMove, !1, this);
    goog.events.listen(a, goog.events.EventType.FOCUS, this.handleFocus, !1, this);
    goog.events.listen(a, goog.events.EventType.BLUR, this.handleMouseOutAndBlur, !1, this)
};
goog.ui.Tooltip.prototype.detach = function (a) {
    if (a)a = goog.dom.getElement(a), this.detachElement_(a), this.elements_.remove(a); else {
        for (var b = this.elements_.getValues(), c = 0; a = b[c]; c++)this.detachElement_(a);
        this.elements_.clear()
    }
};
goog.ui.Tooltip.prototype.detachElement_ = function (a) {
    goog.events.unlisten(a, goog.events.EventType.MOUSEOVER, this.handleMouseOver, !1, this);
    goog.events.unlisten(a, goog.events.EventType.MOUSEOUT, this.handleMouseOutAndBlur, !1, this);
    goog.events.unlisten(a, goog.events.EventType.MOUSEMOVE, this.handleMouseMove, !1, this);
    goog.events.unlisten(a, goog.events.EventType.FOCUS, this.handleFocus, !1, this);
    goog.events.unlisten(a, goog.events.EventType.BLUR, this.handleMouseOutAndBlur, !1, this)
};
goog.ui.Tooltip.prototype.setShowDelayMs = function (a) {
    this.showDelayMs_ = a
};
goog.ui.Tooltip.prototype.getShowDelayMs = function () {
    return this.showDelayMs_
};
goog.ui.Tooltip.prototype.setHideDelayMs = function (a) {
    this.hideDelayMs_ = a
};
goog.ui.Tooltip.prototype.getHideDelayMs = function () {
    return this.hideDelayMs_
};
goog.ui.Tooltip.prototype.setText = function (a) {
    goog.dom.setTextContent(this.getElement(), a)
};
goog.ui.Tooltip.prototype.setHtml = function (a) {
    this.getElement().innerHTML = a
};
goog.ui.Tooltip.prototype.setElement = function (a) {
    var b = this.getElement();
    b && goog.dom.removeNode(b);
    goog.ui.Tooltip.superClass_.setElement.call(this, a);
    a && (b = this.dom_.getDocument().body, b.insertBefore(a, b.lastChild))
};
goog.ui.Tooltip.prototype.getText = function () {
    return goog.dom.getTextContent(this.getElement())
};
goog.ui.Tooltip.prototype.getHtml = function () {
    return this.getElement().innerHTML
};
goog.ui.Tooltip.prototype.getState = function () {
    return this.showTimer ? this.isVisible() ? goog.ui.Tooltip.State.UPDATING : goog.ui.Tooltip.State.WAITING_TO_SHOW : this.hideTimer ? goog.ui.Tooltip.State.WAITING_TO_HIDE : this.isVisible() ? goog.ui.Tooltip.State.SHOWING : goog.ui.Tooltip.State.INACTIVE
};
goog.ui.Tooltip.prototype.setRequireInteraction = function (a) {
    this.requireInteraction_ = a
};
goog.ui.Tooltip.prototype.isCoordinateInTooltip = function (a) {
    if (!this.isVisible())return!1;
    var b = goog.style.getPageOffset(this.getElement()), c = goog.style.getSize(this.getElement());
    return b.x <= a.x && a.x <= b.x + c.width && b.y <= a.y && a.y <= b.y + c.height
};
goog.ui.Tooltip.prototype.onBeforeShow = function () {
    if (!goog.ui.PopupBase.prototype.onBeforeShow.call(this))return!1;
    if (this.anchor)for (var a, b = 0; a = goog.ui.Tooltip.activeInstances_[b]; b++)goog.dom.contains(a.getElement(), this.anchor) || a.setVisible(!1);
    goog.array.insert(goog.ui.Tooltip.activeInstances_, this);
    a = this.getElement();
    a.className = this.className;
    this.clearHideTimer();
    goog.events.listen(a, goog.events.EventType.MOUSEOVER, this.handleTooltipMouseOver, !1, this);
    goog.events.listen(a, goog.events.EventType.MOUSEOUT,
        this.handleTooltipMouseOut, !1, this);
    this.clearShowTimer();
    return!0
};
goog.ui.Tooltip.prototype.onHide_ = function () {
    goog.array.remove(goog.ui.Tooltip.activeInstances_, this);
    for (var a = this.getElement(), b, c = 0; b = goog.ui.Tooltip.activeInstances_[c]; c++)b.anchor && goog.dom.contains(a, b.anchor) && b.setVisible(!1);
    this.parentTooltip_ && this.parentTooltip_.startHideTimer();
    goog.events.unlisten(a, goog.events.EventType.MOUSEOVER, this.handleTooltipMouseOver, !1, this);
    goog.events.unlisten(a, goog.events.EventType.MOUSEOUT, this.handleTooltipMouseOut, !1, this);
    this.anchor = void 0;
    this.getState() ==
        goog.ui.Tooltip.State.INACTIVE && (this.seenInteraction_ = !1);
    goog.ui.PopupBase.prototype.onHide_.call(this)
};
goog.ui.Tooltip.prototype.maybeShow = function (a, b) {
    this.anchor == a && this.elements_.contains(this.anchor) && (this.seenInteraction_ || !this.requireInteraction_ ? (this.setVisible(!1), this.isVisible() || this.positionAndShow_(a, b)) : this.anchor = void 0);
    this.showTimer = void 0
};
goog.ui.Tooltip.prototype.getElements = function () {
    return this.elements_
};
goog.ui.Tooltip.prototype.getActiveElement = function () {
    return this.activeEl_
};
goog.ui.Tooltip.prototype.setActiveElement = function (a) {
    this.activeEl_ = a
};
goog.ui.Tooltip.prototype.showForElement = function (a, b) {
    this.attach(a);
    this.activeEl_ = a;
    this.positionAndShow_(a, b)
};
goog.ui.Tooltip.prototype.positionAndShow_ = function (a, b) {
    this.anchor = a;
    this.setPosition(b || this.getPositioningStrategy(goog.ui.Tooltip.Activation.CURSOR));
    this.setVisible(!0)
};
goog.ui.Tooltip.prototype.maybeHide = function (a) {
    this.hideTimer = void 0;
    a == this.anchor && (null != this.activeEl_ && (this.activeEl_ == this.getElement() || this.elements_.contains(this.activeEl_)) || this.hasActiveChild() || this.setVisible(!1))
};
goog.ui.Tooltip.prototype.hasActiveChild = function () {
    return!(!this.childTooltip_ || !this.childTooltip_.activeEl_)
};
goog.ui.Tooltip.prototype.saveCursorPosition_ = function (a) {
    var b = this.dom_.getDocumentScroll();
    this.cursorPosition.x = a.clientX + b.x;
    this.cursorPosition.y = a.clientY + b.y
};
goog.ui.Tooltip.prototype.handleMouseOver = function (a) {
    var b = this.getAnchorFromElement(a.target);
    this.activeEl_ = b;
    this.clearHideTimer();
    b != this.anchor && (this.anchor = b, this.startShowTimer(b), this.checkForParentTooltip_(), this.saveCursorPosition_(a))
};
goog.ui.Tooltip.prototype.getAnchorFromElement = function (a) {
    try {
        for (; a && !this.elements_.contains(a);)a = a.parentNode;
        return a
    } catch (b) {
        return null
    }
};
goog.ui.Tooltip.prototype.handleMouseMove = function (a) {
    this.saveCursorPosition_(a);
    this.seenInteraction_ = !0
};
goog.ui.Tooltip.prototype.handleFocus = function (a) {
    this.activeEl_ = a = this.getAnchorFromElement(a.target);
    this.seenInteraction_ = !0;
    if (this.anchor != a) {
        this.anchor = a;
        var b = this.getPositioningStrategy(goog.ui.Tooltip.Activation.FOCUS);
        this.clearHideTimer();
        this.startShowTimer(a, b);
        this.checkForParentTooltip_()
    }
};
goog.ui.Tooltip.prototype.getPositioningStrategy = function (a) {
    return a == goog.ui.Tooltip.Activation.CURSOR ? (a = this.cursorPosition.clone(), new goog.ui.Tooltip.CursorTooltipPosition(a)) : new goog.ui.Tooltip.ElementTooltipPosition(this.activeEl_)
};
goog.ui.Tooltip.prototype.checkForParentTooltip_ = function () {
    if (this.anchor)for (var a, b = 0; a = goog.ui.Tooltip.activeInstances_[b]; b++)goog.dom.contains(a.getElement(), this.anchor) && (a.childTooltip_ = this, this.parentTooltip_ = a)
};
goog.ui.Tooltip.prototype.handleMouseOutAndBlur = function (a) {
    var b = this.getAnchorFromElement(a.target), c = this.getAnchorFromElement(a.relatedTarget);
    b != c && (b == this.activeEl_ && (this.activeEl_ = null), this.clearShowTimer(), this.seenInteraction_ = !1, !this.isVisible() || a.relatedTarget && goog.dom.contains(this.getElement(), a.relatedTarget) ? this.anchor = void 0 : this.startHideTimer())
};
goog.ui.Tooltip.prototype.handleTooltipMouseOver = function (a) {
    a = this.getElement();
    this.activeEl_ != a && (this.clearHideTimer(), this.activeEl_ = a)
};
goog.ui.Tooltip.prototype.handleTooltipMouseOut = function (a) {
    var b = this.getElement();
    this.activeEl_ != b || a.relatedTarget && goog.dom.contains(b, a.relatedTarget) || (this.activeEl_ = null, this.startHideTimer())
};
goog.ui.Tooltip.prototype.startShowTimer = function (a, b) {
    this.showTimer || (this.showTimer = goog.Timer.callOnce(goog.bind(this.maybeShow, this, a, b), this.showDelayMs_))
};
goog.ui.Tooltip.prototype.clearShowTimer = function () {
    this.showTimer && (goog.Timer.clear(this.showTimer), this.showTimer = void 0)
};
goog.ui.Tooltip.prototype.startHideTimer = function () {
    this.getState() == goog.ui.Tooltip.State.SHOWING && (this.hideTimer = goog.Timer.callOnce(goog.bind(this.maybeHide, this, this.anchor), this.getHideDelayMs()))
};
goog.ui.Tooltip.prototype.clearHideTimer = function () {
    this.hideTimer && (goog.Timer.clear(this.hideTimer), this.hideTimer = void 0)
};
goog.ui.Tooltip.prototype.disposeInternal = function () {
    this.setVisible(!1);
    this.clearShowTimer();
    this.detach();
    this.getElement() && goog.dom.removeNode(this.getElement());
    this.activeEl_ = null;
    delete this.dom_;
    goog.ui.Tooltip.superClass_.disposeInternal.call(this)
};
goog.ui.Tooltip.CursorTooltipPosition = function (a, b) {
    goog.positioning.ViewportPosition.call(this, a, b)
};
goog.inherits(goog.ui.Tooltip.CursorTooltipPosition, goog.positioning.ViewportPosition);
goog.ui.Tooltip.CursorTooltipPosition.prototype.reposition = function (a, b, c) {
    b = goog.style.getClientViewportElement(a);
    b = goog.style.getVisibleRectForElement(b);
    c = c ? new goog.math.Box(c.top + 10, c.right, c.bottom, c.left + 10) : new goog.math.Box(10, 0, 0, 10);
    goog.positioning.positionAtCoordinate(this.coordinate, a, goog.positioning.Corner.TOP_START, c, b, goog.positioning.Overflow.ADJUST_X | goog.positioning.Overflow.FAIL_Y) & goog.positioning.OverflowStatus.FAILED && goog.positioning.positionAtCoordinate(this.coordinate,
        a, goog.positioning.Corner.TOP_START, c, b, goog.positioning.Overflow.ADJUST_X | goog.positioning.Overflow.ADJUST_Y)
};
goog.ui.Tooltip.ElementTooltipPosition = function (a) {
    goog.positioning.AnchoredPosition.call(this, a, goog.positioning.Corner.BOTTOM_RIGHT)
};
goog.inherits(goog.ui.Tooltip.ElementTooltipPosition, goog.positioning.AnchoredPosition);
goog.ui.Tooltip.ElementTooltipPosition.prototype.reposition = function (a, b, c) {
    var d = new goog.math.Coordinate(10, 0);
    goog.positioning.positionAtAnchor(this.element, this.corner, a, b, d, c, goog.positioning.Overflow.ADJUST_X | goog.positioning.Overflow.FAIL_Y) & goog.positioning.OverflowStatus.FAILED && goog.positioning.positionAtAnchor(this.element, goog.positioning.Corner.TOP_RIGHT, a, goog.positioning.Corner.BOTTOM_LEFT, d, c, goog.positioning.Overflow.ADJUST_X | goog.positioning.Overflow.ADJUST_Y)
};
goog.positioning.MenuAnchoredPosition = function (a, b, c, d) {
    goog.positioning.AnchoredViewportPosition.call(this, a, b, c || d);
    (c || d) && this.setLastResortOverflow(goog.positioning.Overflow.ADJUST_X_EXCEPT_OFFSCREEN | (d ? goog.positioning.Overflow.RESIZE_HEIGHT : goog.positioning.Overflow.ADJUST_Y_EXCEPT_OFFSCREEN))
};
goog.inherits(goog.positioning.MenuAnchoredPosition, goog.positioning.AnchoredViewportPosition);
goog.ui.ItemEvent = function (a, b, c) {
    goog.events.Event.call(this, a, b);
    this.item = c
};
goog.inherits(goog.ui.ItemEvent, goog.events.Event);
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
var widjdev = {Component: function (a) {
    goog.ui.Component.call(this);
    this.initialLabel_ = a || "Click Me";
    this.color_ = "#CCFF33";
    this.kh_ = null
}};
goog.inherits(widjdev.Component, goog.ui.Component);
widjdev.Component.prototype.fireEvent = function (a) {
    alert("events wired up" + this.initialLabel_)
};
widjdev.Component.prototype.createDom = function () {
    this.decorateInternal(this.dom_.createElement("span"))
};
widjdev.Component.prototype.decorateInternal = function (a) {
    widjdev.Component.superClass_.decorateInternal.call(this, a);
    this.getLabelText() || this.setLabelText(this.initialLabel_);
    a = this.getElement();
    goog.dom.classes.add(a, "widjdev-Component");
    this.kh_ = new goog.events.KeyHandler(a);
    this.getHandler().listen(this.kh_, goog.events.KeyHandler.EventType.KEY, this.onKey_)
};
widjdev.Component.prototype.onDivClicked_ = function (a) {
    this.fireEvent()
};
widjdev.Component.prototype.disposeInternal = function () {
    widjdev.Component.superClass_.disposeInternal.call(this);
    this.kh_ && this.kh_.dispose()
};
widjdev.Component.prototype.enterDocument = function () {
    widjdev.Component.superClass_.enterDocument.call(this);
    this.getHandler().listen(this.getElement(), goog.events.EventType.CLICK, this.onDivClicked_)
};
widjdev.Component.prototype.exitDocument = function () {
    widjdev.Component.superClass_.exitDocument.call(this)
};
widjdev.Component.prototype.getLabelText = function () {
    return this.getElement() ? goog.dom.getTextContent(this.getElement()) : ""
};
widjdev.Component.prototype.onKey_ = function (a) {
    var b = goog.events.KeyCodes;
    a.keyCode != b.SPACE && a.keyCode != b.ENTER || this.fireEvent()
};
widjdev.Component.prototype.setLabelText = function (a) {
    this.getElement() && goog.dom.setTextContent(this.getElement(), a)
};
widjdev.categories = [
    {name: "Compatibility", colorCode: "#9ce148", subcategories: ["Family Friends", "Parenting", "Finances", "Career Hobbies"]},
    {name: "Connection", colorCode: "#ffff00"},
    {name: "Exterior", colorCode: "#004cc0"},
    {name: "Eroticism", colorCode: "#f11515"},
    {name: "Seduction", colorCode: "#ed427d"},
    {name: "Psy Tendencies", colorCode: "#a32ad4", subcategories: "NPD;Depression;Post traumatic stress;Addiction;Compulsive lying;Codependency;Low self esteem;Obsessive compulsive disorder;Sulking/silent treatment;Hoarding;Passive aggressive;Autism;Aspergers;Rape;Dislexia".split(";")},
    {name: "Affairs", colorCode: "#ff8f00"},
    {name: "Abuse", colorCode: "#d69051"}
];
goog.exportSymbol("widjdev.categories", widjdev.categories);
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
widjdev.tabdev = {};
var topTab;
widjdev.tabdev.setTabs = function (a, b, c, d) {
    widjdev.tabdev.resetList(a, d);
    for (var e = 0; e < d; e++) {
        var f = "goog-tab";
        0 == e && (f = "goog-tab goog-tab-selected");
        f = goog.dom.createDom("div", {"class": f}, "List " + (e + 1));
        a.appendChild(f)
    }
    for (var f = Array(d), g = Array(d), e = 0; e < d; e++)f[e] = b.slice(c * e, c * (e + 1)), g[e] = goog.dom.createDom("div", {"class": "snipList"}), widjdev.tabdev.makeRows("active", f[e], g[e]);
    topTab = new goog.ui.TabBar;
    topTab.decorate(goog.dom.getElement(a));
    var h = goog.dom.getElement(topTab.getId() + "_content");
    0 != b.length ? h.appendChild(g[0]) : goog.dom.setTextContent(h, "no more snips");
    goog.events.listen(topTab, goog.ui.Component.EventType.SELECT, function (a) {
        a = a.target.id_[1];
        goog.dom.setTextContent(h, "");
        h.appendChild(g[a])
    })
};
widjdev.tabdev.resetList = function (a, b) {
    if (topTab) {
        var c = goog.dom.getElement(topTab.getId() + "_content");
        goog.dom.removeChildren(c);
        goog.dom.removeChildren(a)
    }
};
widjdev.tabdev.rowLists = {};
widjdev.tabdev.tabRow = function (a, b) {
    this.id = a.id;
    this.title = a.title;
    this.contentAsHtml = a.contentAsHtml;
    this.contentAsString = a.contentAsString;
    this.author = a.author;
    this.creationDate = a.creationDate;
    this.rep = a.rep;
    this.authorName = a.author;
    this.coreCat = a.coreCat;
    this.subCat = a.subCat;
    this.viewCount = a.views;
    this.posRef = a.posRef;
    this.neutralRef = a.neutralRef;
    this.negativeRef = a.negativeRef;
    this.userImg = a.avatarUrl;
    this.snipImg = "./img/Snip.gif";
    this.parent = b
};
widjdev.tabdev.tabRow.prototype.closeEditor = function () {
    this.editorContainer.style.display = "none"
};
widjdev.tabdev.tabRow.prototype.openEditor = function (a) {
    this.editorContainer.style.display = "inline"
};
widjdev.tabdev.makeRows = function (a, b, c) {
    for (var d = 0; d < b.length; d++)(new widjdev.tabdev.tabRow(b[d], c)).makeDom(b[d].title, b[d].author);
    widjdev.tabdev.rowLists[a] = c
};
widjdev.tabdev.tabRow.prototype.makeDom = function (a, b) {
    var c = getColorCodeByCat(this.coreCat);
    this.snipImg = goog.dom.createDom("img", {src: this.snipImg, "class": "snipImg"}, null);
    var d = goog.dom.createDom("div", {"class": "snipImgParent"}, this.snipImg), e = goog.dom.createDom("div", null, "Rep"), f = goog.dom.createDom("div", null, this.rep + ""), g = goog.dom.createDom("div", null, "Rep Level"), e = goog.dom.createDom("div", {"class": "firstColDiv"}, d, e, f, g);
    d.style.backgroundColor = c;
    this.userImg = goog.dom.createDom("img", {src: this.userImg,
        "class": "userImg"}, null);
    g = goog.dom.createDom("div", {"class": "userImgParent"}, this.userImg);
    d = this.title + " / " + this.coreCat;
    "" != this.subCat && (d += " / " + this.subCat);
    new goog.ui.Tooltip(this.snipImg, d);
    f = goog.dom.createDom("div", {"class": "userTitleDiv"}, "User Title (" + this.author + ")");
    e = goog.dom.createDom("div", {"class": "leftPanelTop"}, e, g);
    e = goog.dom.createDom("div", {"class": "leftPanel"}, e, f);
    f = goog.dom.createDom("table", {"class": "tableBadges", cellspacing: 2, cellpadding: 2}, null);
    for (g = 0; g < Math.ceil(3); g++) {
        for (var h =
            goog.dom.createDom("tr", null, null), k = 0; 3 > k; k++) {
            var l = goog.dom.createDom("img", {src: "./img/badge.png", "class": "imgBadge"}, null), l = goog.dom.createDom("td", null, l);
            h.appendChild(l)
        }
        f.appendChild(h)
    }
    var f = goog.dom.createDom("div", {"class": "secondColDiv"}, f), g = goog.dom.createDom("div", {"class": "titleLabel"}, "Title:"), h = goog.dom.createDom("div", {"class": "snipContentDiv"}, this.title), g = goog.dom.createDom("div", {"class": "thirdColDivChild1"}, g, h), h = goog.dom.createDom("div", {"class": "userNameDiv"}, this.authorName),
        k = goog.dom.createDom("div", null, this.posRef + " Positive Ref"), l = goog.dom.createDom("div", null, this.neutralRef + " Neutral Ref"), m = goog.dom.createDom("div", null, this.negativeRef + " Negative Ref"), k = goog.dom.createDom("div", {"class": "postCountDiv"}, k, l, m), l = goog.dom.createDom("div", {"class": "viewCountDiv"}, this.viewCount + " Views"), h = goog.dom.createDom("div", {"class": "thirdColDivChild2"}, h, k, l), g = goog.dom.createDom("div", {"class": "thirdColDiv"}, g, h), h = goog.dom.createDom("div", {"class": "viewButtonDiv"}),
        l = new goog.ui.Button("View");
    l.render(goog.dom.getElement(h));
    k = goog.dom.createDom("div", {"class": "editButtonDiv"});
    m = new goog.ui.Button("Edit");
    m.render(goog.dom.getElement(k));
    var n = this.id;
    goog.events.listen(l, goog.ui.Component.EventType.ACTION, function (a) {
        viewButtonJavaCallback(n)
    });
    goog.events.listen(m, goog.ui.Component.EventType.ACTION, function (a) {
        editButtonJavaCallback(n)
    });
    l = goog.dom.createDom("div", {"class": "creationDateDiv"}, this.creationDate);
    h = goog.dom.createDom("div", {"class": "forthColDiv"},
        h, k, l);
    e = goog.dom.createDom("div", {"class": "contentDiv"}, e, f, g, h);
    this.colorStripeDiv = goog.dom.createDom("div", {"class": "colorStripeDiv"});
    this.listContentDiv = goog.dom.createDom("div", {"class": "listContentDiv"}, e);
    e = goog.dom.createDom("div", {"class": "listRow"}, this.colorStripeDiv, this.listContentDiv);
    this.colorStripeDiv.style.backgroundColor = c;
    new goog.ui.Tooltip(this.colorStripeDiv, d);
    this.parent.appendChild(e)
};
function getColorCodeByCat(a) {
    for (var b = 0; b < widjdev.categories.length; b++)if (a == widjdev.categories[b].name)return widjdev.categories[b].colorCode;
    return""
}
function viewButtonJavaCallback(a) {
    viewButtonCallbackGWT(a)
}
function editButtonJavaCallback(a) {
    editButtonCallbackGWT(a)
}
goog.exportSymbol("widjdev.tabdev", widjdev.tabdev);
goog.exportSymbol("widjdev.tabdev.tabRow", widjdev.tabdev.tabRow);
goog.exportSymbol("widjdev.tabdev.setTabs", widjdev.tabdev.setTabs);
