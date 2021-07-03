/*
 * DiscoItemsLookupShortcutMechanism
 * smack-extensions
 *
 * Created by Alexey Nikitin 02/24/2021
 * Copyright 2021 Intermedia. All rights reserved.
 */
package org.jivesoftware.smackx.disco;

import org.jivesoftware.smackx.disco.packet.DiscoverItems;

import org.jxmpp.jid.Jid;

public abstract class DiscoItemsLookupShortcutMechanism implements Comparable<DiscoItemsLookupShortcutMechanism> {

    private final String name;
    private final int priority;

    protected DiscoItemsLookupShortcutMechanism(
            final String name,
            final int priority
    ) {
        this.name = name;
        this.priority = priority;
    }

    public final String getName() {
        return name;
    }

    /**
     * Get the priority of this mechanism. Lower values mean higher priority.
     *
     * @return the priority of this mechanism.
     */
    public final int getPriority() {
        return priority;
    }

    public abstract DiscoverItems getDiscoverItemsByUser(
            ServiceDiscoveryManager serviceDiscoveryManager,
            Jid jid
    );

    @Override
    public final int compareTo(final DiscoItemsLookupShortcutMechanism other) {
        // Switch to Integer.compare(int, int) once Smack is on Android 19 or higher.
        Integer ourPriority = getPriority();
        return ourPriority.compareTo(other.getPriority());
    }
}
