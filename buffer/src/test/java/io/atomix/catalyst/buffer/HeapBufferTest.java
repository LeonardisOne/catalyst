/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.atomix.catalyst.buffer;

import java.nio.ByteBuffer;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Heap buffer test.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
@Test
public class HeapBufferTest extends BufferTest {

  @Override
  protected Buffer createBuffer(long capacity) {
    return HeapBuffer.allocate(capacity);
  }

  @Override
  protected Buffer createBuffer(long capacity, long maxCapacity) {
    return HeapBuffer.allocate(capacity, maxCapacity);
  }

  public void testByteBufferToHeapBuffer() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(8);
    byteBuffer.putLong(10);
    byteBuffer.flip();

    DirectBuffer directBuffer = DirectBuffer.allocate(8);
    directBuffer.write(byteBuffer.array());
    directBuffer.flip();
    assertEquals(directBuffer.readLong(), byteBuffer.getLong());

    byteBuffer.rewind();
    HeapBuffer heapBuffer = HeapBuffer.wrap(byteBuffer.array());
    assertEquals(heapBuffer.readLong(), byteBuffer.getLong());
  }

  public void testDirectToHeapBuffer() {
    DirectBuffer directBuffer = DirectBuffer.allocate(8);
    directBuffer.writeLong(10);
    directBuffer.flip();

    byte[] bytes = new byte[8];
    directBuffer.read(bytes);
    directBuffer.rewind();

    HeapBuffer heapBuffer = HeapBuffer.wrap(bytes);
    assertEquals(directBuffer.readLong(), heapBuffer.readLong());
  }

  public void testHeapToDirectBuffer() {
    HeapBuffer heapBuffer = HeapBuffer.allocate(8);
    heapBuffer.writeLong(10);
    heapBuffer.flip();

    DirectBuffer directBuffer = DirectBuffer.allocate(8);
    directBuffer.write(heapBuffer.array());
    directBuffer.flip();

    assertEquals(directBuffer.readLong(), heapBuffer.readLong());
  }

}
