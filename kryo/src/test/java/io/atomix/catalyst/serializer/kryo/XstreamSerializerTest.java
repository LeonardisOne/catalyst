package io.atomix.catalyst.serializer.kryo;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.util.Map;
import org.junit.Test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import io.atomix.catalyst.serializer.kryo.SerializeWithXstream.Boo;

public class XstreamSerializerTest {

	@Test
	public void test() {
		System.out.println("start test");
		Map<String, String> serializedObjects = SerializeWithXstream.serialize();

		Map<String, Object> deserializedObjects = SerializeWithXstream.deserialize(serializedObjects);
		Kryo kryo = (Kryo) deserializedObjects.get("kryo");
		Boo b = (Boo) deserializedObjects.get("boo");
		Output output = (Output) deserializedObjects.get("output");

		System.out.println("invoke writeObject()");
		kryo.writeObject(output, b);
        output.close();
        Input input = new Input(new ByteArrayInputStream(output.getBuffer()));
        System.out.println("invoke readObject()");
        Boo b2 = kryo.readObject(input, Boo.class);
        assertEquals(b.name, b2.name);
	}

}
