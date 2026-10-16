package com.simplicite.objects.Gs1Demo;

import java.util.ArrayList;
import java.util.List;

import com.simplicite.util.Message;
import com.simplicite.util.ObjectDB;
import com.simplicite.util.ObjectField;
import com.simplicite.util.tools.GS1Tool;

public class Gs1UDI extends ObjectDB {
    private static final long serialVersionUID = 1L;

    @Override
    public List<String> postValidate() {
        List<String> msgs = new ArrayList<>();

        ObjectField id = getField("gs1DeviceIdentifier");
        if (!GS1Tool.isValid(id.getValue()))
            msgs.add(Message.formatError("Invalid device identifier", null, "gs1DeviceIdentifier"));

        /*
        GS1Tool.UDI udi = new UDI(id.getValue(), getFieldValue(""));
        // String gtin14, String prod, String expiry, String lot, String serial, String software
        setFieldValue("gs1UdiHumanReadableInterpretation", udi.hri());
        setFieldValue("gs1UdiHumanReadableInterpretation", udi.hri());
        setFieldValue("gs1UdiHumanReadableInterpretation", udi.hri());
        */

        return msgs;
    }
}
