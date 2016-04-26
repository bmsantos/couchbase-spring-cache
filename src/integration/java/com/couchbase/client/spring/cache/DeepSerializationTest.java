/*
 * Copyright (C) 2015 Couchbase Inc., the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.couchbase.client.spring.cache;


import com.couchbase.client.java.Bucket;
import com.couchbase.client.spring.cache.serialization.model.Foo;
import com.couchbase.client.spring.cache.serialization.model.Other;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.couchbase.client.spring.cache.serialization.model.SharedCache.SharedCache;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class DeepSerializationTest {

  private final String cacheName = "test";

  private int DEEP_LEVEL = 4;

  @Autowired
  private Bucket client;
  private CouchbaseCache cache;

  @Before
  public void setUp() {
    cache = new CouchbaseCache(cacheName, client);
    SharedCache.setCache(cache);
  }

  /**
   * The following test will fail to deserialize when DEEP_LEVEL is larger than number of thread pool
   */
  @Test
  public void shouldDeserializeComplexObject() {
    // Given
    final Foo expected = getFooStructure();

    // When
    final Foo retrieved = cache.get(expected.getId(), Foo.class);

    // Then
    assertThat(retrieved, is(expected));
  }

  /**
   * Build Foo->Other object composition based on DEEP_LEVEL. For a level 3 you should see:
   *   Level 0    Level 1    Level 2    Level 3
   *  ----^----  ----^----  ----^----  ----^----
   * Foo->Other
   *        |-> Foo->Other
   *                   |-> Foo->Other
   *                              |-> Foo->Other
   */
  private Foo getFooStructure() {
    final Foo root = createFoo(0);
    Foo current = root;
    for (int id = 1; id <= DEEP_LEVEL; id++) {
      current.getOther().setFoo(createFoo(id));
      cache.put(current.getOther().getId(), current.getOther());

      current = current.getOther().getFoo();
    }
    return root;
  }

  private Foo createFoo(final int id) {
    final Other other = new Other();
    other.setId("Other" + id);
    other.setDescription("Other" + id + " description");

    cache.put(other.getId(), other);

    final Foo foo = new Foo();
    foo.setId("Foo" + id);
    foo.setDescription("Foo" + id + " description");
    foo.setOther(other);

    cache.put(foo.getId(), foo);

    return foo;
  }
}