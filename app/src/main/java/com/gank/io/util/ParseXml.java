package com.gank.io.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by lucifer on 16-1-4.
 */
public class ParseXml {


    public class ParseMeizhiXml extends DefaultHandler{

        private String nodeName;

        public ParseMeizhiXml(String nodeName) {
            this.nodeName = nodeName;
        }

        // 开始解析文档
        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
        }
    }
}
