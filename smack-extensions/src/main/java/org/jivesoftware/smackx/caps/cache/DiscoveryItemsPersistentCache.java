/*
 * DiscoveryItemsPersistentCache
 * smack-extensions
 *
 * Created by Alexey Nikitin 02/24/2021
 * Copyright 2021 Intermedia. All rights reserved.
 */
package org.jivesoftware.smackx.caps.cache;

import org.jivesoftware.smackx.disco.packet.DiscoverItems;

public interface DiscoveryItemsPersistentCache {
    /**
     * Add an DiscoverItems to the persistent Cache.
     *
     * @param nodeVer
     * @param info
     */
    void addDiscoverItemsByNodePersistent(String nodeVer, DiscoverItems info);

    /**
     * Lookup DiscoverItems by a Node string.
     * @param nodeVer
     *
     * @return DiscoverItems.
     */
    DiscoverItems lookup(String nodeVer);

    /**
     * Empty the Cache.
     */
    void emptyCache();
}
