package de.vkoop.monit.checks;

import de.vkoop.monit.checks.result.HealthCheck;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class HealthCheckExecutorIntegrationTest {

    @Autowired
    private HealthCheckExecutor executor;

    @Autowired
    private Observable<Tuple2<String, Result>> checkSubject;

    @Test
    public void testIfAllChecksWereExecuted() {
        TestObserver<Tuple2<String, Result>> testObserver = new TestObserver<>();

        checkSubject.subscribe(testObserver);
        executor.check();

        testObserver.assertValueCount(3);
    }

    @TestConfiguration
    public static class MyTestConfiguration {
        @Bean
        Map<String, HealthCheck> healthChecks() {
            return HashMap.of(
                    "alwayshealthy", new AlwaysHealthyCheck(),
                    "nothealthy", new AlwaysUnhealthyCheck(),
                    "alwayshealthy2", new AlwaysHealthyCheck());
        }
    }

    public static class AlwaysHealthyCheck implements NamedHealthCheck {
        @Override
        public String getName() {
            return "alwayshealthy" + Math.random();
        }

        @Override
        public Result check() {
            return Result.healthy();
        }
    }

    public static class AlwaysUnhealthyCheck implements NamedHealthCheck {
        @Override
        public String getName() {
            return "unhealthy" + Math.random();
        }

        @Override
        public Result check() {
            return Result.unhealthy("i'm not healthy :( :(");
        }
    }
}