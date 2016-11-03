package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Created by cristiand on 8/23/2016.
 */
public class JsonUtils {

    public static String toString(Object value) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            //mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
