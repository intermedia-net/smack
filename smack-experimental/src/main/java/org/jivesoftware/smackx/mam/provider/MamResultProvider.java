/**
 *
 * Copyright 2016 Fernando Ramirez, 2020 Florian Schmaus
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
package org.jivesoftware.smackx.mam.provider;

import java.io.IOException;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.XmlEnvironment;
import org.jivesoftware.smack.parsing.SmackParsingException;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.jivesoftware.smack.xml.XmlPullParser;
import org.jivesoftware.smack.xml.XmlPullParserException;

import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.delay.packet.DelayInformation;
import org.jivesoftware.smackx.delay.provider.DelayInformationProvider;
import org.jivesoftware.smackx.forward.packet.Forwarded;
import org.jivesoftware.smackx.forward.provider.ForwardedProvider;
import org.jivesoftware.smackx.mam.element.MamElements.MamResultExtension;

/**
 * MAM Result Provider class.
 *
 * @see <a href="http://xmpp.org/extensions/xep-0313.html">XEP-0313: Message
 *      Archive Management</a>
 * @author Fernando Ramirez
 *
 */
public class MamResultProvider extends ExtensionElementProvider<MamResultExtension> {
    public static final MamResultProvider INSTANCE = new MamResultProvider();

    @Override
    public MamResultExtension parse(XmlPullParser parser, int initialDepth, XmlEnvironment xmlEnvironment) throws XmlPullParserException, IOException, SmackParsingException {
        Forwarded<Message> forwarded = null;
        Message message = null;
        DelayInformation di = null;
        String queryId = parser.getAttributeValue("", "queryid");
        String id = parser.getAttributeValue("", "id");

        outerloop: while (true) {
            final XmlPullParser.Event eventType = parser.next();
            final String name = parser.getName();
            String namespace = parser.getNamespace();
            switch (eventType) {
            case START_ELEMENT:
                switch (name) {
                case Forwarded.ELEMENT:
                    forwarded = ForwardedProvider.parseForwardedMessage(parser, xmlEnvironment);
                    break;
                }
                case DelayInformation.ELEMENT:
                    if (DelayInformation.NAMESPACE.equals(namespace)) {
                        di = DelayInformationProvider.INSTANCE.parse(parser, parser.getDepth());
                    }
                    break;
                case Message.ELEMENT:
                    message = PacketParserUtils.parseMessage(parser);
                    break;
                break;
            case END_ELEMENT:
                if (parser.getDepth() == initialDepth) {
                    break outerloop;
                }
                break;
            default:
                // Catch all for incomplete switch (MissingCasesInEnumSwitch) statement.
                break;
            }
        }

        if (message != null) {
            return new MamResultExtension(queryId, id, message, di);
        } else {
            return new MamResultExtension(queryId, id, forwarded);
        }
    }

}
