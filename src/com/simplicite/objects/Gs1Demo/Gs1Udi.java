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

        // Build UDI
        GS1Tool.UDI udi =
                new GS1Tool.UDI(
                        gtin,
                        getField("gs1UdiProductionDate").getDate(),
                        getField("gs1UdiExpirationDate").getDate(),
                        getFieldValue("gs1UdiLotNumber"),
                        getFieldValue("gs1UdiSerialNumber"),
                        getFieldValue("gs1UdiSoftwareVersion"),
                        getGrant().getServerURL() + "/ext/Gs1ProductPage");
        setFieldValue("gs1UdiHumanReadableInterpretation", udi.hri());
        setFieldValue("gs1UdiGs1Data", udi.gs1Data());
        setFieldValue("gs1UdiDigitalLink", udi.digitalLink());

        // Build images
        try {
            getField("gs1UdiGs1128Image").setDocument(this, "gs1-128-" + gtin + ".png", udi.gs1128PNG());
        } catch (Exception e) {
            AppLog.warning("GS1 128 image error: " + e.getMessage());
            setFieldValue("gs1UdiGs1128Image", "");
        }
        try {
            getField("gs1UdiGs1DataMatrixImage").setDocument(this, "gs1-datamatrix-" + gtin + ".png", udi.gs1DataMatrixPNG());
        } catch (Exception e) {
            AppLog.warning("GS1 data matrix image error: " + e.getMessage());
            setFieldValue("gs1UdiGs1DataMatrixImage", "");
        }
        try {
            getField("gs1UdiGs1QrCodeImage").setDocument(this, "gs1-qrcode-" + gtin + ".png", udi.gs1QrCodePNG());
        } catch (Exception e) {
            AppLog.warning("GS1 QRCode error: " + e.getMessage());
            setFieldValue("gs1UdiGs1QrCodeImage", "");
        }
        try {
            getField("gs1UdiDigitalLinkQrCodeImage").setDocument(this, "gs1-qrcode-" + gtin + ".png", udi.digitalLinkQrCodePNG());
        } catch (Exception e) {
            AppLog.warning("Digital link image error: " + e.getMessage());
            setFieldValue("gs1UdiDigitalLinkQrCodeImage", "");
        }

        return msgs;
    }
}
