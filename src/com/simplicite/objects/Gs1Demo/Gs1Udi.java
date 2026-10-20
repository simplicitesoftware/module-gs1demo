package com.simplicite.objects.Gs1Demo;

import java.util.ArrayList;
import java.util.List;

import com.simplicite.util.AppLog;
import com.simplicite.util.ObjectDB;
import com.simplicite.util.tools.GS1Tool;

public class Gs1Udi extends ObjectDB {
    private static final long serialVersionUID = 1L;

    @Override
    public List<String> postValidate() {
        List<String> msgs = new ArrayList<>();

        String gtin = getFieldValue("gs1UdiGtinId.gs1GtinIdentifier");
        GS1Tool.UDI udi =
                new GS1Tool.UDI(
                        gtin,
                        getField("gs1UdiProductionDate").getDate(),
                        getField("gs1UdiExpirationDate").getDate(),
                        getFieldValue("gs1UdiLotNumber"),
                        getFieldValue("gs1UdiSerialNumber"),
                        getFieldValue("gs1UdiSoftwareVersion"));
        setFieldValue("gs1UdiHumanReadableInterpretation", udi.hri());
        setFieldValue("gs1UdiGs1Data", udi.gs1());
        setFieldValue("gs1UdiDigitalLink", udi.digitalLink(null));

        try {
            getField("gs1UdiGs1128Image").setDocument(this, "gs1-128-" + gtin + ".png", GS1Tool.gs1_128PNG(udi));
        } catch (Exception e) {
            AppLog.error(e);
        }

        try {
            getField("gs1UdiGs1DataMatrixImage").setDocument(this, "gs1-datamatrix-" + gtin + ".png", GS1Tool.gs1DataMatrixPNG(udi, 5));
        } catch (Exception e) {
            AppLog.error(e);
        }

        try {
            getField("gs1UdiGs1QrCodeImage").setDocument(this, "gs1-qrcode-" + gtin + ".png", GS1Tool.gs1QrCodePNG(udi, 5));
        } catch (Exception e) {
            AppLog.error(e);
        }

        try {
            getField("gs1UdiDigitalLinkQrCodeImage").setDocument(this, "gs1-qrcode-" + gtin + ".png", GS1Tool.digitalLinkQrCodePNG(udi, null, 5));
        } catch (Exception e) {
            AppLog.error(e);
        }

        return msgs;
    }
}
