package pl.anime.scraper.utils.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import java.io.IOException;

public class DoubleZeroDecimalSerializer extends JsonSerializer<Double> implements ContextualSerializer {

    @Override
    public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value % 1 == 0) {
            gen.writeNumber(value.intValue());
        } else {
            gen.writeNumber(value);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        return this;
    }
}
