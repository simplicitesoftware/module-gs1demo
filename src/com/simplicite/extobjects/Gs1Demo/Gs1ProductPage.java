package com.simplicite.extobjects.Gs1Demo;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import com.simplicite.util.AppLog;
import com.simplicite.util.tools.GTINTool;
import com.simplicite.util.tools.HTMLTool;
import com.simplicite.util.tools.Parameters;
import com.simplicite.webapp.web.WebPage;

public class Gs1ProductPage extends com.simplicite.webapp.web.WebPageExternalObject {
    private static final long serialVersionUID = 1L;

    @Override
    public String displayBody(Parameters parameters) {
        JSONObject params = null;
        try {
            WebPage wp = getPage();

            wp.appendJQuery();
            wp.appendBootstrap();
            wp.appendSimpliciteClient();

            wp.appendCSSInclude(HTMLTool.getResourceCSSURL(this, "STYLES"));
            wp.appendJSInclude(HTMLTool.getResourceJSURL(this, "CLASS"));

            String digitalLinkURI = parameters.getLocationURL();
            params = GTINTool.parseDigitalLink(digitalLinkURI)
                .put("serverURL", parameters.getServerURL())
                .put("digitalLinkURI", digitalLinkURI);
        } catch (Exception e) {
            AppLog.error(null, e, getGrant());
            params = new JSONObject().put("error", e.getMessage());
        }

        return
            HTMLTool.getResourceHTMLContent(this, "HTML") +
            HTMLTool.jsBlock(this.getName() + ".render(" + params.toString() + ");");
    }
}
