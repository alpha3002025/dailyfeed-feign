package click.dailyfeed.feign.config.feign;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 400:
                return new IllegalArgumentException("Bad Request: " + methodKey);
            case 401:
                return new SecurityException("Unauthorized: " + methodKey);
            case 404:
                return new RuntimeException("Not Found: " + methodKey);
            case 500:
                return new RuntimeException("Internal Server Error: " + methodKey);
            default:
                return defaultErrorDecoder.decode(methodKey, response);
        }
    }
}
