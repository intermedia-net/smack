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

package org.jivesoftware.smackx.muc;

import org.jivesoftware.smack.packet.Presence;

import org.jivesoftware.smackx.muc.packet.MUCUser;

import org.jxmpp.jid.Jid;

/**
 * A listener that is fired anytime your participant's status in a room is changed, such as the user being kicked,
 * banned, or granted admin permissions or the room is destroyed.
 * <p>
 * Note that the methods {@link #kicked(Jid, String)}, {@link #banned(Jid, String)} and
 * {@link #roomDestroyed(MultiUserChat, String)} will be called before the generic {@link #removed(MUCUser, Presence)}
 * callback will be invoked. The generic {@link #removed(MUCUser, Presence)} callback will be invoked every time the user
 * was removed from the MUC involuntarily. It is hence the recommended callback to listen for and act upon.
 * </p>
 *
 * @author Gaston Dombiak
 */
public interface UserStatusListener {

    /**
     * Called when a moderator kicked your user from the room. This means that you are no longer
     * participanting in the room.
     *
     * @param actor the moderator that kicked your user from the room (e.g. user@host.org).
     * @param reason the reason provided by the actor to kick you from the room.
     * @see #removed(MUCUser, Presence)
     */
    default void kicked(Jid actor, String reason) {
    }

    /**
     * Called when a moderator grants voice to your user. This means that you were a visitor in
     * the moderated room before and now you can participate in the room by sending messages to
     * all occupants.
     *
     */
    default void voiceGranted() {
    }

    /**
     * Called when a moderator revokes voice from your user. This means that you were a
     * participant in the room able to speak and now you are a visitor that can't send
     * messages to the room occupants.
     *
     */
    default void voiceRevoked() {
    }

    /**
     * Called when an administrator or owner banned your user from the room. This means that you
     * will no longer be able to join the room unless the ban has been removed.
     *
     * @param actor the administrator that banned your user (e.g. user@host.org).
     * @param reason the reason provided by the administrator to banned you.
     * @see #removed(MUCUser, Presence)
     */
    default void banned(Jid actor, String reason) {
    }

     /**
      * Called when a user is involuntarily removed from the room.
      *
      * @param mucUser the optional muc#user extension element
      * @param presence the carrier presence
      * @since 4.4.0
      */
     default void removed(MUCUser mucUser, Presence presence) {
     }

     /**
     * Called when an administrator grants your user membership to the room. This means that you
     * will be able to join the members-only room.
     *
     */
    default void membershipGranted() {
    }

    /**
     * Called when an administrator revokes your user membership to the room. This means that you
     * will not be able to join the members-only room.
     *
     */
    default void membershipRevoked() {
    }

    /**
     * Called when an administrator grants moderator privileges to your user. This means that you
     * will be able to kick users, grant and revoke voice, invite other users, modify room's
     * subject plus all the partcipants privileges.
     *
     */
    default void moderatorGranted() {
    }

    /**
     * Called when an administrator revokes moderator privileges from your user. This means that
     * you will no longer be able to kick users, grant and revoke voice, invite other users,
     * modify room's subject plus all the partcipants privileges.
     *
     */
    default void moderatorRevoked() {
    }

    /**
     * Called when an owner grants to your user ownership on the room. This means that you
     * will be able to change defining room features as well as perform all administrative
     * functions.
     *
     */
    default void ownershipGranted() {
    }

    /**
     * Called when an owner revokes from your user ownership on the room. This means that you
     * will no longer be able to change defining room features as well as perform all
     * administrative functions.
     *
     */
    default void ownershipRevoked() {
    }

    /**
     * Called when an owner grants administrator privileges to your user. This means that you
     * will be able to perform administrative functions such as banning users and edit moderator
     * list.
     *
     */
    default void adminGranted() {
    }

    /**
     * Called when an owner revokes administrator privileges from your user. This means that you
     * will no longer be able to perform administrative functions such as banning users and edit
     * moderator list.
     *
     */
    default void adminRevoked() {
    }

    /**
     * Called when the room is destroyed.
     *
     * @param alternateMUC an alternate MultiUserChat, may be null.
     * @param reason the reason why the room was closed, may be null.
     * @see #removed(MUCUser, Presence)
     */
    default void roomDestroyed(MultiUserChat alternateMUC, String reason) {
    }

}
