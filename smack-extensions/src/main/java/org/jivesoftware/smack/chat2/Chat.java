/**
 *
 * Copyright 2017 Florian Schmaus.
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
package org.jivesoftware.smack.chat2;

import org.jivesoftware.smack.Manager;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.extensions.ChatArchivedExtension;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.ExceptionCallback;
import org.jivesoftware.smack.util.SuccessCallback;

import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;

public final class Chat extends Manager {
    private static final long MESSAGE_TIMEOUT = 30000L;

    public interface StanzaAckListener {
        void onSuccess(Message message);

        void onError(Message message, Throwable throwable);
    }

    private final EntityBareJid jid;

    volatile Jid lockedResource;

    Presence lastPresenceOfLockedResource;

    Chat(final XMPPConnection connection, EntityBareJid jid) {
        super(connection);
        this.jid = jid;
    }

    public void send(CharSequence message) throws NotConnectedException, InterruptedException {
        Message stanza = connection()
                .getStanzaFactory()
                .buildMessageStanza()
                .ofType(Message.Type.chat)
                .setBody(message)
                .build();
        send(stanza);
    }

    public void send(Message message) throws NotConnectedException, InterruptedException {
        switch (message.getType()) {
            case normal:
            case chat:
                break;
            default:
                throw new IllegalArgumentException("Message must be of type 'normal' or 'chat'");
        }

        Jid to = lockedResource;
        if (to == null) {
            to = jid;
        }
        message.setTo(to);

        connection().sendStanza(message);
    }

    public void sendWithConfirmation(final Message message, final StanzaAckListener listener) {
        switch (message.getType()) {
        case normal:
        case chat:
            break;
        default:
            throw new IllegalArgumentException("Message must be of type 'normal' or 'chat'");
        }

        Jid to = lockedResource;
        if (to == null) {
            to = jid;
        }
        message.setTo(to);

        connection().sendAsync(message, new ChatArchivedExtension.ArchiveIdFilter(message.getStanzaId()), MESSAGE_TIMEOUT)
                .onSuccess(new SuccessCallback<Message>() {
                    @Override
                    public void onSuccess(Message result) {
                        if (listener != null) {
                            listener.onSuccess(result);
                        }
                    }
                })
                .onError(new ExceptionCallback<Exception>() {
                    @Override
                    public void processException(Exception exception) {
                        if (listener != null) {
                            listener.onError(message, exception);
                        }
                    }
                });
    }

    public EntityBareJid getXmppAddressOfChatPartner() {
        return jid;
    }

    void unlockResource() {
        lockedResource = null;
        lastPresenceOfLockedResource = null;
    }
}
