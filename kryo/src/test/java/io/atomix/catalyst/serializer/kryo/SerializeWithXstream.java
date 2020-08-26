package io.atomix.catalyst.serializer.kryo;

import java.util.HashMap;
import java.util.Map;
import java.io.File;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;

public class SerializeWithXstream {
	@SuppressWarnings("unchecked")
	public static Map<String, Object> deserialize(){
		XStream xstream = new XStream();
        
        // resolve error of Security framework of XStream not initialized, XStream is probably vulnerable.
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypesByWildcard(new String[] {
            "io.atomix.catalyst.serializer.kryo.**"
        });
        xstream.addPermission(AnyTypePermission.ANY);
        
        // deserialize input objects in HashMap & return as HashMap
        Map<String, Object> deserializedObjects = new HashMap<String, Object>();
        System.out.println("start deserializing");
        File treefile = new File("./serialized/rtree.xml");
        File entryfile = new File("./serialized/entry.xml");
        Object tree = xstream.fromXML(treefile);
        Object entry = xstream.fromXML(entryfile);
        
        deserializedObjects.put("rtree", tree);
        deserializedObjects.put("entry", entry);
        
        System.out.println("deserialized done");
        return deserializedObjects;
	}
}

