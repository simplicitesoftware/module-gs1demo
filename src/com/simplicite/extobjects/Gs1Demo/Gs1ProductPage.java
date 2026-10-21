package com.simplicite.extobjects.Gs1Demo;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import com.simplicite.util.AppLog;
import com.simplicite.util.tools.HTMLTool;
import com.simplicite.util.tools.Parameters;
import com.simplicite.webapp.web.BootstrapWebPage;

public class Gs1ProductPage extends com.simplicite.webapp.web.WebPageExternalObject {
    private static final long serialVersionUID = 1L;

    @Override
    public String displayBody(Parameters parameters) {
        try {
            BootstrapWebPage wp = new BootstrapWebPage(parameters.getRoot(), getDisplay());

            wp.appendHTML(HTMLTool.getResourceHTMLContent(this, "HTML"));
            wp.appendCSSInclude(HTMLTool.getResourceCSSURL(this, "STYLES"));
            wp.appendJSInclude(HTMLTool.getResourceJSURL(this, "CLASS"));
            wp.appendJSInclude(HTMLTool.simpliciteClientJS());

            // e.g. /01/00000096385074/10/LOT-A/21/SERIAL-AAA?17=320101&11=260101&8012=2.0
            int i = 0;
            List<String> parts = parameters.getLocationParts(getName());
            for (String part : parts)
                AppLog.info("Part " + i++ + " = " + part);
            if (parts.size() < 2)
                return "Not found";

            String type = parts.size() == 2 ? "gtin" : "udi";
            String gtin = "01".equals(parts.get(0)) ? parts.get(1) : null;
            String lot = parts.size() >= 4 && "10".equals(parts.get(2)) ? parts.get(3) : null;
            String serial = parts.size() >= 6 && "21".equals(parts.get(4)) ? parts.get(5) : null;

            Map<String, String> params = parameters.getParameters();
            for (Entry<String, String> param :  params.entrySet())
                AppLog.info("Param " + param.getKey() + " = " + param.getValue());

            JSONObject p = new JSONObject()
                .put("serverURL", parameters.getServerURL())
                .put("type", type)
                .put("gtin", gtin)
                .put("lot", lot)
                .put("serial", serial)
                .put("productionDate", params.get("11"))
                .put("expiryDate", params.get("17"))
                .put("softwareVersion", params.get("12"));
            AppLog.info(p.toString(2));
            wp.setReady(this.getName() + ".render(" + p.toString() + ");");

            return wp.toString();
        } catch (Exception e) {
            AppLog.error(null, e, getGrant());
            return e.getMessage();
        }
    }
}
