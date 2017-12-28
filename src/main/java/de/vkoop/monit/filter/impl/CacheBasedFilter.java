package de.vkoop.monit.filter.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.vkoop.monit.filter.StatefulFilter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Scope("prototype")
@Component
public class CacheBasedFilter<T> implements StatefulFilter<T> {

    @Setter
    private Cache<T, T> cache;

    public CacheBasedFilter(@Value("${monit.reportRate}") int expirationInHours) {
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expirationInHours, TimeUnit.HOURS)
                .build();
    }

    @Override
    public boolean isNewItem(T item) {
        return cache.getIfPresent(item) == null;
    }

    @Override
    public void blockItem(T item) {
        cache.put(item, item);
    }

    @Override
    public void unblockItem(T item) {
        cache.invalidate(item);
    }

}
