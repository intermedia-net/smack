package org.jivesoftware.smack.extensions;

import java.io.IOException;

import org.jivesoftware.smack.packet.XmlEnvironment;
import org.jivesoftware.smack.parsing.SmackParsingException;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.jivesoftware.smack.xml.XmlPullParser;
import org.jivesoftware.smack.xml.XmlPullParserException;

public class ChatArchivedExtensionProvider extends ExtensionElementProvider<ChatArchivedExtension> {

    static final String ATTR_ID = "id";
    static final String ATTR_TIMESTAMP = "timestamp";

    @Override
    public ChatArchivedExtension parse(XmlPullParser parser, int initialDepth, XmlEnvironment xmlEnvironment) throws XmlPullParserException, IOException, SmackParsingException {
        final String id = parser.getAttributeValue("", ATTR_ID);
        final String timestamp = parser.getAttributeValue("", ATTR_TIMESTAMP);
        return new ChatArchivedExtension(id, timestamp);
    }
}
