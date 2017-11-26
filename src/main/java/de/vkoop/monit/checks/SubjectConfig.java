package de.vkoop.monit.checks;

import com.codahale.metrics.health.HealthCheck;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Map;

@Configuration
public class SubjectConfig {

    @Scope("singleton")
    @Bean
    public Subject<Map<String, HealthCheck.Result>> checkSubject(){
        Subject<Map<String, HealthCheck.Result>> subject = PublishSubject.create();
        subject.publish();

        return subject;
    }


}
