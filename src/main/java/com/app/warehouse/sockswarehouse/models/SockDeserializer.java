package com.app.warehouse.sockswarehouse.models;

import com.app.warehouse.sockswarehouse.models.enums.Color;
import com.app.warehouse.sockswarehouse.models.enums.Size;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class SockDeserializer extends StdDeserializer {

    public SockDeserializer() {
        this(null);
    }

    protected SockDeserializer(Class vc) {
        super(vc);
    }

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Size sizeAsEnum;
        String sizeAsText = node.get("size").asText();
        if (sizeAsText.contains("S")) {
            sizeAsEnum = Size.valueOf(sizeAsText);
        } else {
            sizeAsEnum = Size.sizeAsEnum(sizeAsText);
        }
        Color colorAsEnum = Color.valueOf(node.get("color").asText().toUpperCase());
        int compositionAsInt = node.get("composition").asInt();
        return new Sock(colorAsEnum, sizeAsEnum, compositionAsInt);
    }


}
