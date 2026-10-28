package com.simplicite.objects.Gs1Demo;

import java.util.ArrayList;
import java.util.List;

import com.simplicite.util.AppLog;
import com.simplicite.util.ObjectDB;
import com.simplicite.util.tools.GTINTool;

public class Gs1Udi extends ObjectDB {
    private static final long serialVersionUID = 1L;

    @Override
    public List<String> postValidate() {
        List<String> msgs = new ArrayList<>();

        String gtin = getFieldValue("gs1UdiGtinId.gs1GtinIdentifier");

        // Build UDI
        GTINTool.UDI udi = new GTINTool.UDI(
            gtin,
            getField("gs1GenProductionDate").getDate(),
            getField("gs1GenExpirationDate").getDate(),
            getFieldValue("gs1GenLotNumber"),
            getFieldValue("gs1GenSerialNumber"),
            getFieldValue("gs1UdiSoftwareVersion"),
            getGrant().getServerURL() + "/ext/Gs1ProductPage");

        // Build strings
        setFieldValue("gs1GenHumanReadableInterpretation", udi.hri());
        setFieldValue("gs1GenGs1Data", udi.gs1Data());
        setFieldValue("gs1GenDigitalLink", udi.digitalLink());

        // Build UDI images
        try {
            getField("gs1GenGs1128Image").setDocument(this, "gs1-128-" + gtin + ".png", udi.gs1128PNG());
        } catch (Exception e) {
            AppLog.warning("GS1 128 image error: " + e.getMessage());
            setFieldValue("gs1GenGs1128Image", "");
        }
        try {
            getField("gs1GenGs1DataMatrixImage").setDocument(this, "gs1-datamatrix-" + gtin + ".png", udi.gs1DataMatrixPNGWithHRI(5));
        } catch (Exception e) {
            AppLog.warning("GS1 data matrix image error: " + e.getMessage());
            setFieldValue("gs1GenGs1DataMatrixImage", "");
        }
        try {
            getField("gs1GenDigitalLinkQrCodeImage").setDocument(this, "gs1-qrcode-" + gtin + ".png", udi.digitalLinkQrCodePNGWithGTIN(4));
        } catch (Exception e) {
            AppLog.warning("Digital link image error: " + e.getMessage());
            setFieldValue("gs1GenDigitalLinkQrCodeImage", "");
        }

        return msgs;
    }
}
