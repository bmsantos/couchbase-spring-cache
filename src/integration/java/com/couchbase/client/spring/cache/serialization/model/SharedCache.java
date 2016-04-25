package com.couchbase.client.spring.cache.serialization.model;

import com.couchbase.client.spring.cache.CouchbaseCache;

public enum SharedCache {
    SharedCache;

    private CouchbaseCache cache;

    public CouchbaseCache getCache() {
        return cache;
    }

    public void setCache(final CouchbaseCache cache) {
        this.cache = cache;
    }
}
