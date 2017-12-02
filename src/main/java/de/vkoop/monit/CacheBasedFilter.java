package de.vkoop.monit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

public class CacheBasedFilter<T> implements Filter<T> {

    @Setter
    private Cache<T, T> cache = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS)
            .build();

    @Override
    public boolean filter(T t){
        boolean isNew =  cache.getIfPresent(t) == null;
        if(isNew){
            cache.put(t,t);
        }

        return isNew;
    }



}
