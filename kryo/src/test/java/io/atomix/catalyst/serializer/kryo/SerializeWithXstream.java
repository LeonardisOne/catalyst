package io.atomix.catalyst.serializer.kryo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.io.File;

import com.esotericsoftware.kryo.Kryo;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.security.AnyTypePermission;
import com.fyp.tryapp.KryoContainer;
import com.fyp.tryapp.ContainKryoContainer;

public class SerializeWithXstream {
	@SuppressWarnings("unchecked")
	public static Map<String, Object> deserialize(){
		XStream xstream = new XStream();
        
        // resolve error of Security framework of XStream not initialized, XStream is probably vulnerable.
        XStream.setupDefaultSecurity(xstream); //not actually needed
        xstream.addPermission(AnyTypePermission.ANY);
        
        // deserialize input objects in HashMap & return as HashMap
        Map<String, Object> deserializedObjects = new HashMap<String, Object>();
        System.out.println("try deserializing");
        File treefile = new File("./serialized/rtree.xml");
        File entryfile = new File("./serialized/entry.xml");
        if (!testDeserialize("./serialized/contain_container.xml")) {
        	System.out.println("Kryo container container error");
        }
        if (!testDeserialize("./serialized/container.xml")) {
        	System.out.println("Kryo container error");
        }
        if (!testDeserialize("./serialized/kryo.xml")) {
        	System.out.println("Kryo error");
        }
        Object tree = xstream.fromXML(treefile);
        Object entry = xstream.fromXML(entryfile);
        
        deserializedObjects.put("rtree", tree);
        deserializedObjects.put("entry", entry);
        
        System.out.println("deserialized done");
        return deserializedObjects;
	}
	
	public static boolean testDeserialize(String filename){
		XStream xstream = new XStream();
		xstream.alias("KryoContainer", KryoContainer.class);
        
        // allow any class to be deserialized
        xstream.addPermission(AnyTypePermission.ANY);
        xstream.ignoreUnknownElements();
        // omit can be used in deserialization alone
        //xstream.omitField(Kryo.class, "classLoader");
        
        try {
        	System.out.println(xstream.getReflectionProvider());
            xstream.fromXML(new File(filename));
        } catch (ConversionException e) {
        	Iterator iterate = e.keys();
        	if (e.get((String) iterate.next()) == "Failed calling method") {
        		System.out.println("Failed calling method");
//            	for (int i=1; i<7; i++) {
//                	iterate.next();
//            	}
            	
            	while(iterate.hasNext()) {
            		System.out.println((String) iterate.next() + ": " + e.get((String) iterate.next()));
            	}
            	
            	//System.out.println("path: " + e.get((String) iterate.next()));
        	} else {
        		e.printStackTrace();
        	}
        	return false;
        } catch (XStreamException e) {
        	e.printStackTrace();
        	return false;
        }
        
        return true;
	}
}

