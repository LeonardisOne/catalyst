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

import io.atomix.catalyst.buffer.util.DirectMemoryAllocator;
import io.atomix.catalyst.buffer.util.HeapMemory;
import io.atomix.catalyst.buffer.util.Memory;

/**
 * Direct {@link java.nio.ByteBuffer} based buffer.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public class HeapBuffer extends ByteBufferBuffer {

  /**
   * Allocates a direct buffer with an initial capacity of {@code 4096} and a maximum capacity of {@link Long#MAX_VALUE}.
   * <p>
   * When the buffer is constructed, {@link DirectMemoryAllocator} will be used to allocate
   * {@code capacity} bytes of off-heap memory. The resulting buffer will be initialized with a capacity of {@code 4096}
   * and have a maximum capacity of {@link Long#MAX_VALUE}. The buffer's {@code capacity} will dynamically expand as
   * bytes are written to the buffer. The underlying {@link UnsafeDirectBytes} will be initialized to the next power of {@code 2}.
   *
   * @return The direct buffer.
   *
   * @see HeapBuffer#allocate(long)
   * @see HeapBuffer#allocate(long, long)
   */
  public static HeapBuffer allocate() {
    return allocate(DEFAULT_INITIAL_CAPACITY, HeapMemory.MAX_SIZE);
  }

  /**
   * Allocates a direct buffer with the given initial capacity.
   * <p>
   * When the buffer is constructed, {@link DirectMemoryAllocator} will be used to allocate
   * {@code capacity} bytes of off-heap memory. The resulting buffer will have an initial capacity of {@code capacity}.
   * The underlying {@link UnsafeDirectBytes} will be initialized to the next power of {@code 2}.
   *
   * @param initialCapacity The initial capacity of the buffer to allocate (in bytes).
   * @return The direct buffer.
   * @throws IllegalArgumentException If {@code capacity} is greater than the maximum allowed count for
   *         a {@link java.nio.ByteBuffer} - {@code Integer.MAX_VALUE - 5}
   *
   * @see HeapBuffer#allocate()
   * @see HeapBuffer#allocate(long, long)
   */
  public static HeapBuffer allocate(long initialCapacity) {
    return allocate(initialCapacity, HeapMemory.MAX_SIZE);
  }

  /**
   * Allocates a new direct buffer.
   * <p>
   * When the buffer is constructed, {@link DirectMemoryAllocator} will be used to allocate
   * {@code capacity} bytes of off-heap memory. The resulting buffer will have an initial capacity of {@code initialCapacity}
   * and will be doubled up to {@code maxCapacity} as bytes are written to the buffer. The underlying {@link UnsafeDirectBytes}
   * will be initialized to the next power of {@code 2}.
   *
   * @param initialCapacity The initial capacity of the buffer to allocate (in bytes).
   * @param maxCapacity The maximum capacity of the buffer.
   * @return The direct buffer.
   * @throws IllegalArgumentException If {@code capacity} or {@code maxCapacity} is greater than the maximum
   *         allowed count for a {@link java.nio.ByteBuffer} - {@code Integer.MAX_VALUE - 5}
   *
   * @see HeapBuffer#allocate()
   * @see HeapBuffer#allocate(long)
   */
  public static HeapBuffer allocate(long initialCapacity, long maxCapacity) {
    if (initialCapacity > maxCapacity)
      throw new IllegalArgumentException("initial capacity cannot be greater than maximum capacity");
    return new HeapBuffer(HeapBytes.allocate((int) Math.min(Memory.Util.toPow2(initialCapacity), HeapMemory.MAX_SIZE)), 0, initialCapacity, maxCapacity);
  }

  /**
   * Wraps the given bytes in a heap buffer.
   * <p>
   * The buffer will be created with an initial capacity and maximum capacity equal to the byte array count.
   *
   * @param bytes The bytes to wrap.
   * @return The wrapped bytes.
   */
  public static HeapBuffer wrap(byte[] bytes) {
    return new HeapBuffer(HeapBytes.wrap(bytes), 0, bytes.length, bytes.length);
  }

  protected HeapBuffer(HeapBytes bytes, long offset, long initialCapacity, long maxCapacity) {
    super(bytes, offset, initialCapacity, maxCapacity, null);
  }

  @Override
  public boolean hasArray() {
    return true;
  }
}
