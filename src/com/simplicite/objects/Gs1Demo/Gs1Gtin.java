package com.simplicite.objects.Gs1Demo;

import java.util.ArrayList;
import java.util.List;

import com.simplicite.util.AppLog;
import com.simplicite.util.Message;
import com.simplicite.util.ObjectDB;
import com.simplicite.util.ObjectField;
import com.simplicite.util.Tool;
import com.simplicite.util.tools.GTINTool;

public class Gs1Gtin extends ObjectDB {
    private static final long serialVersionUID = 1L;

    @Override
    public List<String> postValidate() {
        List<String> msgs = new ArrayList<>();

        ObjectField id = getField("gs1GtinIdentifier");
        // Add check digit to GTIN identifier if not present
        if (id.getValue().length() == 13)
            id.setValue(id.getValue() + GTINTool.getGTINCheckDigit(id.getValue()));
        // Check validity of GTIN identifier
        if (!GTINTool.isValid(id.getValue()))
            msgs.add(Message.formatError("GS1_GTIN_INVALID_IDENTIFIER", null, id.getName()));

        // Build strings
        String gtin = id.getValue();
        setFieldValue("gs1GenHumanReadableInterpretation", GTINTool.getHRI(gtin));
        String gs1data = GTINTool.getGs1Data(gtin);
        setFieldValue("gs1GenGs1Data", gs1data);
        String digitalLink = GTINTool.getDigitalLink(gtin, getGrant().getServerURL() + "/ext/Gs1ProductPage");
        setFieldValue("gs1GenDigitalLink", digitalLink);

        // Link to prefix = digits 2–4 (digit 1 is the packaging indicator). e.g. 13001234567897 => 300 => FRA
        String prefix = gtin.substring(1, 4);
        AppLog.info("Prefix = " + prefix);
        if (!prefix.equals(getFieldValue("gs1GtinPreId.gs1PrePrefix"))) {
            // TODO: refactor as object search
            String prefixId = getGrant().simpleQuery("select row_id from gs1_prefix where gs1_pre_prefix = '" + Tool.toSQL(prefix) + "'");
            AppLog.info("Prefix ID = " + prefixId);
            setFieldValue("gs1GtinPreId", prefixId);
        }

        // Build images
        try {
            getField("gs1GtinEan13Image").setDocument(this, "ean13-" + gtin + ".png", GTINTool.ean13PNG(gtin));
        } catch (Exception e) {
            AppLog.warning("EAN13 image error: " + e.getMessage());
            setFieldValue("gs1GtinEan13Image", "");
        }
        try {
            getField("gs1GtinUpcaImage").setDocument(this, "upca-" + gtin + ".png", GTINTool.upcAPNG(gtin));
        } catch (Exception e) {
            AppLog.warning("UPC-A image error: " + e.getMessage());
            setFieldValue("gs1GtinUpcaImage", "");
        }
        try {
            getField("gs1GenGs1DataMatrixImage").setDocument(this, "gs1-datamatrix-" + gtin + ".png", GTINTool.gs1DataMatrixPNGWithHRI(gs1data, GTINTool.getHRILines(gtin), 5));
        } catch (Exception e) {
            AppLog.warning("GS1 data matrix image error: " + e.getMessage());
            setFieldValue("gs1GtinGs1DataMatrixImage", "");
        }
        try {
            getField("gs1GenDigitalLinkQrCodeImage").setDocument(this, "gs1-qrcode-" + gtin + ".png", GTINTool.digitalLinkQrCodePNGWithGTIN(digitalLink, gtin, 4));
        } catch (Exception e) {
            AppLog.warning("Digital link image error: " + e.getMessage());
            setFieldValue("gs1GtinDigitalLinkQrCodeImage", "");
        }

        return msgs;
    }

    @Override
    public boolean canReference(String objectName, String fieldName) {
        if ("Gs1Udi".equals(objectName))
            return "MEDICAL_DEVICE".equals(getFieldValue("gs1GtinType"));
        if ("Gs1Cpg".equals(objectName))
            return "CONSUMER_PRODUCT".equals(getFieldValue("gs1GtinType"));

        return super.canReference(objectName, fieldName);
    }

}
