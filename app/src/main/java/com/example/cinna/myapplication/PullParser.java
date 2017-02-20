package com.example.cinna.myapplication;

/**
 * Created by cinna on 2017/2/3.
 */


import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;



public class PullParser implements Parser
{


    public List<item> parse (InputStream is)
            throws XmlPullParserException, IOException
    {

        XmlPullParser xpp = Xml.newPullParser();


        xpp.setInput(is, "utf-8");
        List<item> items = null;
        item item = null;
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    items = new ArrayList<item>();
                    break;
                case XmlPullParser.START_TAG:

                    if(xpp.getName().equals("element")){
                        item = new item();
                    }
                    if(xpp.getName().equals("title")){
                        eventType = xpp.next();
                        item.setTitle(xpp.getText());
                        //System.out.println(xpp.getText());
                        eventType = xpp.next();
                    }
                    if(xpp.getName().equals("href")){
                        eventType = xpp.next();
                        item.setHref(xpp.getText());
                        //System.out.println(item.getHref());
                        eventType = xpp.next();

                    }
                    if(xpp.getName().equals(("date"))){
                        eventType = xpp.next();
                        item.setDate(xpp.getText());
                        //System.out.println(xpp.getText());
                        eventType = xpp.next();

                    }
                    break;
                case XmlPullParser.END_TAG:
                    if(xpp.getName().equals("element")){
                        items.add(item);
                        item = null;
                    }

            }

            eventType = xpp.next();


        }
        return items;
    }
}
    



