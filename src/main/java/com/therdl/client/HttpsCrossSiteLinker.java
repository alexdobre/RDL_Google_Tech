package com.therdl.client;


import com.google.gwt.core.ext.LinkerContext;
import com.google.gwt.core.linker.CrossSiteIframeLinker;

/**
* Extend CrossSiteIframeLinker and override getJsDevModeRedirectHookPermitted to allow https;
*/
public class HttpsCrossSiteLinker extends CrossSiteIframeLinker {

    @Override
    protected String getJsDevModeRedirectHookPermitted(LinkerContext context) {
        return "$wnd.location.protocol == \"http:\" " +
                "|| $wnd.location.protocol == \"https:\" " +
                "|| $wnd.location.protocol == \"file:\"";
    }

}
