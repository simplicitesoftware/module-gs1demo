package com.simplicite.objects.Gs1Demo;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import com.simplicite.util.AppLog;
import com.simplicite.util.Message;
import com.simplicite.util.ObjectDB;
import com.simplicite.util.ObjectField;
import com.simplicite.util.tools.GS1Tool;

public class Gs1UDI extends ObjectDB {
    private static final long serialVersionUID = 1L;

    @Override
    public List<String> postValidate() {
        List<String> msgs = new ArrayList<>();

        ObjectField id = getField("gs1UdiDeviceIdentifier");
        if (!GS1Tool.isValid(id.getValue()))
            msgs.add(Message.formatError("Invalid device identifier", null, "gs1UdiDeviceIdentifier"));

        GS1Tool.UDI udi = new GS1Tool.UDI(
            id.getValue(),
            getField("gs1UdiProductionDate").getDate(),
            getField("gs1UdiExpirationDate").getDate(),
            getFieldValue("gs1UdiLotNumber"),
            getFieldValue("gs1UdiSerialNumber"),
            getFieldValue("gs1UdiSoftwareVersion"));
        setFieldValue("gs1UdiHumanReadableInterpretation", udi.hri());
        setFieldValue("gs1UdiGs1Data", udi.gs1());
        setFieldValue("gs1UdiDigitalLink", udi.digitalLink(null));

        try {
            getField("gs1UdiEan13Image").setDocument(this, "ean13-" + id.getValue() + ".png", GS1Tool.ean13PNG(udi));
        } catch (IOException e) {
            AppLog.error(e);
        }
        try {
            getField("gs1UdiGs1128Image").setDocument(this, "gs1_128-" + id.getValue() + ".png", GS1Tool.gs1_128PNG(udi));
        } catch (IOException e) {
            AppLog.error(e);
        }
        try {
            getField("gs1UdiGs1DataMatrixImage").setDocument(this, "gs1-datamatrix-" + id.getValue() + ".png", GS1Tool.gs1DataMatrixPNG(udi));
        } catch (IOException e) {
            AppLog.error(e);
        }
        try {
            getField("gs1UdiGs1QrCodeImage").setDocument(this, "gs1-qrcode-" + id.getValue() + ".png", GS1Tool.gs1QrCodePNG(udi));
        } catch (IOException e) {
            AppLog.error(e);
        }
        try {
            getField("gs1UdiDigitalLinkQrCodeImage").setDocument(this, "gs1-qrcode-" + id.getValue() + ".png", GS1Tool.digitalLinkQrCodePNG(udi, null));
        } catch (IOException e) {
            AppLog.error(e);
        }

        return msgs;
    }
}
