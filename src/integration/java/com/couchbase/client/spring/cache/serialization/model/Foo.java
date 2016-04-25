package com.couchbase.client.spring.cache.serialization.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import static com.couchbase.client.spring.cache.serialization.model.SharedCache.SharedCache;

public class Foo implements Serializable {

    private String id;
    private String description;

    private String otherId;
    private transient Other other;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(final String otherId) {
        this.otherId = otherId;
    }

    public Other getOther() {
        return other;
    }

    public void setOther(final Other other) {
        this.otherId = other.getId();
        this.other = other;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Foo foo = (Foo) o;

        if (id != null ? !id.equals(foo.id) : foo.id != null) return false;
        if (description != null ? !description.equals(foo.description) : foo.description != null) return false;
        if (otherId != null ? !otherId.equals(foo.otherId) : foo.otherId != null) return false;
        return other != null ? other.equals(foo.other) : foo.other == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (otherId != null ? otherId.hashCode() : 0);
        result = 31 * result + (other != null ? other.hashCode() : 0);
        return result;
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        System.err.println("Reading Foo with ID: " + id);
        if (other == null && otherId != null) {
            other = SharedCache.getCache().get(otherId, Other.class);
        }
        System.err.println("Done Reading Foo with ID: " + id);
    }
}
