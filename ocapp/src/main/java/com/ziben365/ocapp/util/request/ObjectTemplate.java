package com.ziben365.ocapp.util.request;

import org.msgpack.MessageTypeException;
import org.msgpack.packer.Packer;
import org.msgpack.template.AbstractTemplate;
import org.msgpack.template.Templates;
import org.msgpack.type.ArrayValue;
import org.msgpack.type.FloatValue;
import org.msgpack.type.IntegerValue;
import org.msgpack.type.MapValue;
import org.msgpack.type.RawValue;
import org.msgpack.type.Value;
import org.msgpack.unpacker.Converter;
import org.msgpack.unpacker.Unpacker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ObjectTemplate extends AbstractTemplate<Object> {
	
	private static ObjectTemplate instance ;

	private ObjectTemplate() {
	}

	public static ObjectTemplate getInstance() {
		if(instance == null){
			instance = new ObjectTemplate();
		}
	    return instance;
	}
    

	@Override
	public void write(Packer pk, Object v, boolean required) throws IOException {
	    if (v == null) {
	        if (required) {
	            throw new MessageTypeException("Attempted to write null");
	        }
	        pk.writeNil();
	        return;
	    }
	    pk.write(v);
	}

	@Override
	public Object read(Unpacker u, Object to, boolean required) throws IOException {
	    if (!required && u.trySkipNil()) {
	        return "";
	    }

	    return toObject(u.readValue());
	}

	private static Object toObject(Value value) throws IOException {
	    Converter conv = new Converter(value);
	    if (value.isNilValue()) { // null
	        return "";
	    } else if (value.isRawValue()) { // byte[] or String or maybe Date?
	        // deserialize value to String object
	        RawValue v = value.asRawValue();
	        return conv.read(Templates.TString);
	    } else if (value.isBooleanValue()) { // boolean
	        return conv.read(Templates.TBoolean);
	    } else if (value.isIntegerValue()) { // int or long or BigInteger
	        // deserialize value to int
	        IntegerValue v = value.asIntegerValue();
	        return conv.read(Templates.TInteger);
	    } else if (value.isFloatValue()) { // float or double
	        // deserialize value to double
	        FloatValue v = value.asFloatValue();
	        return conv.read(Templates.TDouble);
	    } else if (value.isArrayValue()) { // List or Set
	        // deserialize value to List object
	        ArrayValue v = value.asArrayValue();
	        List<Object> ret = new ArrayList<Object>(v.size());
	        for (Value elementValue : v) {
	            ret.add(toObject(elementValue));
	        }
	        return ret;
	    } else if (value.isMapValue()) { // Map
	        MapValue v = value.asMapValue();


	        Map<Object, Object> map = new HashMap<>(v.size());
	        for (Map.Entry<Value, Value> entry : v.entrySet()) {
	            Value key = entry.getKey();
	            Value val = entry.getValue();

	            map.put(toObject(key), toObject(val));
	        }

	        return map;
	    } else {
	        throw new RuntimeException("fatal error");
	    }
	}

}
