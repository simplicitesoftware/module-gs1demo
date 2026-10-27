package com.simplicite.objects.Gs1Demo;

import java.util.ArrayList;
import java.util.List;

import com.simplicite.util.AppLog;
import com.simplicite.util.ObjectDB;
import com.simplicite.util.tools.GS1Tool;

public class Gs1Cpg extends ObjectDB {
    private static final long serialVersionUID = 1L;

    @Override
    public List<String> postValidate() {
        List<String> msgs = new ArrayList<>();

        String gtin = getFieldValue("gs1CpgGtinId.gs1GtinIdentifier");

        // Build UDI
        GS1Tool.CPG cpg = new GS1Tool.CPG(
            gtin,
            // TODO: getFieldValue("gs1GenVariant"),
            getField("gs1GenProductionDate").getDate(),
            getField("gs1GenBestBeforeDate").getDate(),
            getField("gs1GenExpirationDate").getDate(),
            getFieldValue("gs1GenLotNumber"),
            getFieldValue("gs1GenSerialNumber"),
            getField("gs1CpgWeight").getDouble(),
            getGrant().getServerURL() + "/ext/Gs1ProductPage");

        // Build strings
        setFieldValue("gs1GenHumanReadableInterpretation", cpg.hri());
        setFieldValue("gs1GenGs1Data", cpg.gs1Data());
        setFieldValue("gs1GenDigitalLink", cpg.digitalLink());

        // Build UDI images
        try {
            getField("gs1GenDigitalLinkQrCodeImage").setDocument(this, "gs1-qrcode-" + gtin + ".png", cpg.digitalLinkQrCodePNGWithGTIN(4));
        } catch (Exception e) {
            AppLog.warning("Digital link image error: " + e.getMessage());
            setFieldValue("gs1GenDigitalLinkQrCodeImage", "");
        }

        return msgs;
    }
}
