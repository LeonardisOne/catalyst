package io.atomix.catalyst.serializer.kryo;

import java.util.HashMap;
import java.util.Map;
import java.io.ByteArrayOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;

public class SerializeWithXstream {
	public static Map<String, String> serialize() {

        XStream xstream = new XStream();
        xstream.alias("boo", Boo.class);
        // avoid exception thrown by serializing classLoader
        xstream.omitField(Kryo.class, "classLoader");
        System.out.println("xstream created");
        
        Kryo kryo = new Kryo();

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Output output = new Output(bytes);
        Boo b = new Boo("hello1");
        kryo.writeObject(output, b);
        
        // serialize objects to XML strings and return as HashMap
        Map<String, String> serializedObjects = new HashMap<String, String>();
        String kryoXml = xstream.toXML(kryo);
        serializedObjects.put("kryo", kryoXml);
        
        String outputXml = xstream.toXML(output);
        serializedObjects.put("output", outputXml);
        
        String bXml = xstream.toXML(b);
        serializedObjects.put("boo", bXml);
        output.close();

        System.out.println("end of method");
        
        return serializedObjects;
	}
	
	public static Map<String, Object> deserialize(Map<String, String> serializedObjects){
		XStream xstream = new XStream();
        
        // resolve error of Security framework of XStream not initialized, XStream is probably vulnerable.
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypesByWildcard(new String[] {
            "io.atomix.catalyst.serializer.kryo.**"
        });
        xstream.addPermission(AnyTypePermission.ANY);
        
        xstream.alias("boo", Boo.class);
        
        // deserialize input objects in HashMap & return as HashMap
        Map<String, Object> deserializedObjects = new HashMap<String, Object>();
        System.out.println("start deserializing");
        Kryo kryo = (Kryo)xstream.fromXML(serializedObjects.get("kryo"));
        Boo b = (Boo)xstream.fromXML(serializedObjects.get("boo"));
        Output output = (Output)xstream.fromXML(serializedObjects.get("output"));
        
        deserializedObjects.put("kryo", kryo);
        deserializedObjects.put("boo", b);
        deserializedObjects.put("output", output);
        
        System.out.println("deserialized done");
        return deserializedObjects;
	}
	
	//class from Rtree
	public static class Boo {

		public final String name;

		private Boo() {
			this("boo");
		}

		public Boo(String name) {
			this.name = name;
		}
	}
}

