package io.atomix.catalyst.serializer.kryo;

import static com.github.davidmoten.rtree.geometry.Geometries.rectangle;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Map;
import org.junit.Test;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Rectangle;

public class XstreamSerializerTest {

	@SuppressWarnings("unchecked")
	@Test
	public void test() {
		System.out.println("start test");

		Map<String, Object> deserializedObjects = SerializeWithXstream.deserialize();
		RTree<Object, Rectangle> tree = (RTree<Object, Rectangle>) deserializedObjects.get("rtree");
		Entry<Object, Rectangle> entry = (Entry<Object, Rectangle>) deserializedObjects.get("entry");

		System.out.println("use objects");
        assertEquals(Arrays.asList(entry), tree.search(rectangle(1, 1, 2, 2)).toList().toBlocking().single());
        System.out.println("end test");
	}

}
