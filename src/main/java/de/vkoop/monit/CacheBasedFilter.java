package de.vkoop.monit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Scope("prototype")
@Component
public class CacheBasedFilter<T> implements Filter<T> {

    @Setter
    private Cache<T, T> cache;

    public CacheBasedFilter(@Value("${health.reportRate}") int expirationInHours) {
        cache = CacheBuilder.newBuilder().expireAfterWrite(expirationInHours, TimeUnit.HOURS)
                .build();
    }

    @Override
    public boolean filter(T t) {
        boolean isNew = cache.getIfPresent(t) == null;
        if (isNew) {
            cache.put(t, t);
        }

        return isNew;
    }
}
