package com.couchbase.client.spring.cache.serialization.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import static com.couchbase.client.spring.cache.serialization.model.SharedCache.SharedCache;

public class Other implements Serializable {

    private String id;
    private String description;

    private String fooId;
    private transient Foo foo;

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

    public String getFooId() {
        return fooId;
    }

    public void setFooId(final String fooId) {
        this.fooId = fooId;
    }

    public Foo getFoo() {
        return foo;
    }

    public void setFoo(final Foo foo) {
        this.fooId = foo.getId();
        this.foo = foo;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Other other = (Other) o;

        if (id != null ? !id.equals(other.id) : other.id != null) return false;
        if (description != null ? !description.equals(other.description) : other.description != null) return false;
        if (fooId != null ? !fooId.equals(other.fooId) : other.fooId != null) return false;
        return foo != null ? foo.equals(other.foo) : other.foo == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (fooId != null ? fooId.hashCode() : 0);
        result = 31 * result + (foo != null ? foo.hashCode() : 0);
        return result;
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        System.err.println("Reading Other with ID: " + id);
        if (foo == null && fooId != null) {
            foo = SharedCache.getCache().get(fooId, Foo.class);
        }
        System.err.println("Done Reading Other with ID: " + id);
    }
}