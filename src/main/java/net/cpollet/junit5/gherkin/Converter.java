package net.cpollet.junit5.gherkin;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by cpollet on 20.10.16.
 */
public class Converter {
    public static final String ISO_DATETIME = "yyyy-MM-dd'T'HH:mm:ssZ";
    private final Map<Class<?>, Function<String, ?>> converters;

    public Converter() {
        converters = new HashMap<>();
        converters.put(Object.class, Function.identity());
        converters.put(String.class, Function.identity());
        converters.put(Integer.class, Integer::valueOf);
        converters.put(Long.class, Long::valueOf);
        converters.put(Float.class, Float::valueOf);
        converters.put(Double.class, Double::valueOf);
        converters.put(Boolean.class, Boolean::valueOf);
        converters.put(BigInteger.class, BigInteger::new);
        converters.put(BigDecimal.class, BigDecimal::new);
        converters.put(Date.class, s -> {
            try {
                return new SimpleDateFormat(ISO_DATETIME).parse(s);
            } catch (ParseException e) {
                throw new Error(e);
            }
        });
        converters.put(LocalDate.class, LocalDate::parse);
        converters.put(LocalTime.class, LocalTime::parse);
        converters.put(LocalDateTime.class, LocalDateTime::parse);
    }

    public <T> T convert(String value, Class<T> type) {
        return (T) converters.get(type).apply(value);
    }
}
