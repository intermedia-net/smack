/**
 *
 * Copyright the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jivesoftware.smackx.bytestreams.ibb.packet;

import javax.xml.namespace.QName;

import org.jivesoftware.smack.datatypes.UInt16;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.IQ.IQChildElementXmlStringBuilder;
import org.jivesoftware.smack.util.Objects;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.jivesoftware.smack.util.stringencoder.Base64;

/**
 * Represents a chunk of data of an In-Band Bytestream within an IQ stanza or a
 * message stanza.
 *
 * @author Henning Staib
 */
public class DataPacketExtension implements ExtensionElement {

    /**
     * The element name of the data stanza extension.
     */
    public static final String ELEMENT = "data";

    /**
     * The XMPP namespace of the In-Band Bytestream.
     */
    public static final String NAMESPACE = "http://jabber.org/protocol/ibb";

    public static final QName QNAME = new QName(NAMESPACE, ELEMENT);

    /* unique session ID identifying this In-Band Bytestream */
    private final String sessionID;

    /* sequence of this packet in regard to the other data packets */
    private final UInt16 seq;

    /* the data contained in this packet */
    private final String data;

    private byte[] decodedData;

    /**
     * Creates a new In-Band Bytestream data packet.
     *
     * @param sessionID unique session ID identifying this In-Band Bytestream
     * @param seq sequence of this stanza in regard to the other data packets
     * @param data the base64 encoded data contained in this packet
     * @throws IllegalArgumentException if seq is not within the range [0, 65535].
     */
    public DataPacketExtension(String sessionID, int seq, String data) {
        this(sessionID, UInt16.from(seq), data);
    }

    /**
     * Creates a new In-Band Bytestream data packet.
     *
     * @param sessionID unique session ID identifying this In-Band Bytestream
     * @param seq sequence of this stanza in regard to the other data packets
     * @param data the base64 encoded data contained in this packet
     */
    public DataPacketExtension(String sessionID, UInt16 seq, String data) {
        if (sessionID == null || "".equals(sessionID)) {
            throw new IllegalArgumentException("Session ID must not be null or empty");
        }
        if (data == null) {
            throw new IllegalArgumentException("Data must not be null");
        }
        this.sessionID = sessionID;
        this.seq = Objects.requireNonNull(seq);
        this.data = data;
    }

    /**
     * Returns the unique session ID identifying this In-Band Bytestream.
     *
     * @return the unique session ID identifying this In-Band Bytestream
     */
    public String getSessionID() {
        return sessionID;
    }

    /**
     * Returns the sequence of this stanza in regard to the other data packets.
     *
     * @return the sequence of this stanza in regard to the other data packets.
     */
    public UInt16 getSeq() {
        return seq;
    }

    /**
     * Returns the data contained in this packet.
     *
     * @return the data contained in this packet.
     */
    public String getData() {
        return data;
    }

    /**
     * Returns the decoded data or null if data could not be decoded.
     * <p>
     * The encoded data is invalid if it contains bad Base64 input characters or
     * if it contains the pad ('=') character on a position other than the last
     * character(s) of the data. See <a
     * href="http://xmpp.org/extensions/xep-0047.html#sec">XEP-0047</a> Section
     * 6.
     *
     * @return the decoded data
     */
    public byte[] getDecodedData() {
        // return cached decoded data
        if (this.decodedData != null) {
            return this.decodedData;
        }

        // data must not contain the pad (=) other than end of data
        if (data.matches(".*={1,2}+.+")) {
            return null;
        }

        // decodeBase64 will return null if bad characters are included
        this.decodedData = Base64.decode(data);
        return this.decodedData;
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
    public XmlStringBuilder toXML(org.jivesoftware.smack.packet.XmlEnvironment enclosingNamespace) {
        XmlStringBuilder xml = getIQChildElementBuilder(new IQChildElementXmlStringBuilder(this, enclosingNamespace));
        xml.closeElement(this);
        return xml;
    }

    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.attribute("seq", seq);
        xml.attribute("sid", sessionID);
        xml.rightAngleBracket();
        xml.append(data);
        return xml;
    }
}
