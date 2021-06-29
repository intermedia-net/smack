/**
 *
 * Copyright 2016 Fernando Ramirez
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

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.delay.packet.DelayInformation;
import org.jivesoftware.smackx.delay.provider.DelayInformationProvider;
import org.jivesoftware.smackx.forward.packet.Forwarded;
import org.jivesoftware.smackx.forward.provider.ForwardedProvider;
import org.jivesoftware.smackx.mam.element.MamElements.MamResultExtension;

import org.xmlpull.v1.XmlPullParser;

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
    public MamResultExtension parse(XmlPullParser parser, int initialDepth) throws Exception {
        Forwarded forwarded = null;
        Message message = null;
        DelayInformation di = null;
        String queryId = parser.getAttributeValue("", "queryid");
        String id = parser.getAttributeValue("", "id");

        outerloop: while (true) {
            final int eventType = parser.next();
            final String name = parser.getName();
            String namespace = parser.getNamespace();
            switch (eventType) {
            case XmlPullParser.START_TAG:
                switch (name) {
                    case Forwarded.ELEMENT:
                        forwarded = ForwardedProvider.INSTANCE.parse(parser);
                        break;
                    case DelayInformation.ELEMENT:
                        if (DelayInformation.NAMESPACE.equals(namespace)) {
                            di = DelayInformationProvider.INSTANCE.parse(parser, parser.getDepth());
                        }
                        break;
                    case Message.ELEMENT:
                        message = PacketParserUtils.parseMessage(parser);
                        break;
                }
                break;
            case XmlPullParser.END_TAG:
                if (parser.getDepth() == initialDepth) {
                    break outerloop;
                }
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
