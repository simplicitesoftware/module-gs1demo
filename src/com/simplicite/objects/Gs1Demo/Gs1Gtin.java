package com.simplicite.objects.Gs1Demo;

import java.util.ArrayList;
import java.util.List;

import com.simplicite.util.AppLog;
import com.simplicite.util.Message;
import com.simplicite.util.ObjectDB;
import com.simplicite.util.ObjectField;
import com.simplicite.util.tools.GS1Tool;

public class Gs1Gtin extends ObjectDB {
    private static final long serialVersionUID = 1L;

    @Override
    public List<String> postValidate() {
        List<String> msgs = new ArrayList<>();

        ObjectField id = getField("gs1GtinIdentifier");
        if (!GS1Tool.isValid(id.getValue()))
            msgs.add(Message.formatError("GS1_GTIN_INVALID_IDENTIFIER", null, id.getName()));

        try { getField("gs1GtinEan13Image").setDocument(this, "ean13-" + id.getValue() + ".png", GS1Tool.ean13PNG(id.getValue())); }
        catch (Exception e) { AppLog.error(e); }

        try { getField("gs1GtinUpcaImage").setDocument(this, "upca-" + id.getValue() + ".png", GS1Tool.upcAPNG(id.getValue())); }
        catch (Exception e) { AppLog.error(e); }

        try { getField("gs1GtinItf14Image").setDocument(this, "itf14-" + id.getValue() + ".png", GS1Tool.itf14PNG(id.getValue())); }
        catch (Exception e) { AppLog.error(e); }

        return msgs;
    }
}
