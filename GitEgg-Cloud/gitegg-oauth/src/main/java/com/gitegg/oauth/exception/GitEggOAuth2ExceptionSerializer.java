package com.gitegg.oauth.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class GitEggOAuth2ExceptionSerializer extends StdSerializer<GitEggOAuth2Exception> {

    protected GitEggOAuth2ExceptionSerializer() {
        super(GitEggOAuth2Exception.class);
    }

    @Override
    public void serialize(GitEggOAuth2Exception e, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("code", e.getHttpErrorCode());
        jsonGenerator.writeStringField("msg", e.getOAuth2ErrorCode());
        jsonGenerator.writeEndObject();
    }
}
