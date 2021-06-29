/**
 *
 * Copyright 2003-2007 Jive Software.
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

package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;


/**
 * Filters for packets of a specific type of Message (e.g. CHAT).
 *
 * @see Type
 * @author Ward Harold
 */
public final class MessageSubTypeFilter extends FlexibleStanzaTypeFilter<Message> {

    public static final StanzaFilter REGULAR = new MessageSubTypeFilter(Message.SubType.regular);
    public static final StanzaFilter ATTACHMENT = new MessageSubTypeFilter(Message.SubType.attachment);
    public static final StanzaFilter SMS = new MessageSubTypeFilter(Message.SubType.sms);

    private final Message.SubType mSubType;

    /**
     * Creates a new message type filter using the specified message type.
     *
     * @param subType the message type.
     */
    private MessageSubTypeFilter(Message.SubType subType) {
        super(Message.class);
        mSubType = subType;
    }

    @Override
    protected boolean acceptSpecific(Message message) {
        return message.getSubType() == mSubType;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": subType=" + mSubType;
    }
}
