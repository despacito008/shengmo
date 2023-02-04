package com.aiwujie.shengmo.zdyview;

import android.text.Editable;
import android.text.Html;
import android.text.Spanned;

import org.xml.sax.XMLReader;

import java.lang.reflect.Field;
import java.util.HashMap;

public class GameTagHandler implements Html.TagHandler {
    private int startIndex = 0;
    private int stopIndex = 0;
    final HashMap<String, String> attributes = new HashMap<String, String>();

    @Override
    public void handleTag(boolean opening, String tag, Editable output,
                          XMLReader xmlReader) {
        processAttributes(xmlReader);
        if (tag.toLowerCase().equals("at")) {
            if (opening) {
                startGame(tag, output, xmlReader);
            } else {
                endGame(tag, output, xmlReader);
            }
        }

    }

    public void startGame(String tag, Editable output, XMLReader xmlReader) {
        startIndex = output.length();
    }

    public void endGame(String tag, Editable output, XMLReader xmlReader) {
        stopIndex = output.length();
        String userId = attributes.get("userid");
        output.setSpan(new ATSpan(userId), startIndex, stopIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void processAttributes(final XMLReader xmlReader) {
        try {
            Field elementField = xmlReader.getClass().getDeclaredField("theNewElement");
            elementField.setAccessible(true);
            Object element = elementField.get(xmlReader);
            if (element == null) {
                return;
            }
            Field attsField = element.getClass().getDeclaredField("theAtts");
            attsField.setAccessible(true);
            Object atts = attsField.get(element);
            Field dataField = atts.getClass().getDeclaredField("data");
            dataField.setAccessible(true);
            String[] data = (String[]) dataField.get(atts);
            Field lengthField = atts.getClass().getDeclaredField("length");
            lengthField.setAccessible(true);
            int len = (Integer) lengthField.get(atts);

            for (int i = 0; i < len; i++) {
                attributes.put(data[i * 5 + 1], data[i * 5 + 4]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}