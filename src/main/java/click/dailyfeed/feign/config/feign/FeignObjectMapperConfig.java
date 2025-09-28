package click.dailyfeed.feign.config.feign;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class FeignObjectMapperConfig {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Primary
    @Bean("plainObjectMapper")
    public ObjectMapper plainObjectMapper() {
        return new ObjectMapper()
                // JSR310 모듈 등록 (Java 8 시간 API 지원)
                .registerModule(createJavaTimeModule())

                // 직렬화 설정
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // 타임스탬프 대신 문자열로 직렬화
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS) // 빈 객체 직렬화 허용
//                .enable(SerializationFeature.INDENT_OUTPUT) // JSON 들여쓰기 (개발용, 운영에서는 비활성화 고려)

                // 역직렬화 설정
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) // 알 수 없는 속성 무시
                .disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES) // primitive 타입에 null 허용
                .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) // 빈 문자열을 null로 처리
                .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY); // 단일 값을 배열로 처리

        // 네이밍 전략 설정 (필요에 따라 주석 해제)
        // .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    private JavaTimeModule createJavaTimeModule() {
        JavaTimeModule module = new JavaTimeModule();

        // LocalDateTime 직렬화/역직렬화 설정
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));

        // LocalDate 직렬화/역직렬화 설정
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        module.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));

        return module;
    }

    @Bean("feignObjectMapper")
    public ObjectMapper feignObjectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
}
