package click.dailyfeed.feign.config.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Param;

public class JacksonExpander implements Param.Expander {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String expand(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }
}