/*
 * ChatArchivedExtension
 * MobileCallScape
 *
 * Created by Alexey Taranov 04/26/2019
 * Copyright 2019 Intermedia. All rights reserved.
 */
package org.jivesoftware.smack.extensions;

import org.jivesoftware.smack.filter.StanzaExtensionFilter;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.packet.XmlEnvironment;
import org.jivesoftware.smack.util.XmlStringBuilder;

public class ChatArchivedExtension implements ExtensionElement {
    public static final String ELEMENT = "archived";
    public static final String NAMESPACE = "ips:xmpp:xmam";

    private final String id;
    private final String timestamp;

    public ChatArchivedExtension(final String id, final String timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    /**
     * Get the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String getElementName() {
        return ELEMENT;
    }

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

    @Override
    public CharSequence toXML(XmlEnvironment xmlEnvironment) {
        XmlStringBuilder xml = new XmlStringBuilder(this);
        xml.attribute(ChatArchivedExtensionProvider.ATTR_ID, id);
        xml.optAttribute(ChatArchivedExtensionProvider.ATTR_TIMESTAMP, timestamp);
        xml.closeEmptyElement();
        return xml;
    }

    public static ChatArchivedExtension from(Stanza message) {
        return (ChatArchivedExtension) message.getExtensionElement(ELEMENT, NAMESPACE);
    }

    public static class ArchiveIdFilter extends StanzaExtensionFilter {
        private final String id;

        public ArchiveIdFilter(String id) {
            super(ChatArchivedExtension.ELEMENT, ChatArchivedExtension.NAMESPACE);
            this.id = id;
        }

        @Override
        public boolean accept(final Stanza packet) {
            if (super.accept(packet)) {
                final ChatArchivedExtension ext = ChatArchivedExtension.from(packet);
                return ext.getId() != null && ext.getId().equals(id);
            }
            return false;
        }
    }
}
