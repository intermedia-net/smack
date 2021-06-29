/**
 * ChatArchivedExtensionProvider
 * MobileCallScape
 *
 * Created by Alexey Taranov 04/26/2019
 * Copyright 2019 Intermedia. All rights reserved.
 */
package org.jivesoftware.smack.extensions;

import org.jivesoftware.smack.provider.ExtensionElementProvider;

import org.xmlpull.v1.XmlPullParser;

public class ChatArchivedExtensionProvider extends ExtensionElementProvider<ChatArchivedExtension> {

    static final String ATTR_ID = "id";
    static final String ATTR_TIMESTAMP = "timestamp";

    @Override
    public ChatArchivedExtension parse(XmlPullParser parser, int initialDepth) throws Exception {
        final String id = parser.getAttributeValue("", ATTR_ID);
        final String timestamp = parser.getAttributeValue("", ATTR_TIMESTAMP);
        return new ChatArchivedExtension(id, timestamp);
    }

}
