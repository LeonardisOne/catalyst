package io.atomix.catalyst.serializer.kryo;

import java.util.HashMap;
import java.util.Map;
import java.io.File;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Rectangle;
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
        RTree<Object, Rectangle> tree = (RTree<Object, Rectangle>)xstream.fromXML(treefile);
        Entry<Object, Rectangle> entry = (Entry<Object, Rectangle>)xstream.fromXML(entryfile);
        
        deserializedObjects.put("rtree", tree);
        deserializedObjects.put("entry", entry);
        
        System.out.println("deserialized done");
        return deserializedObjects;
	}
}

