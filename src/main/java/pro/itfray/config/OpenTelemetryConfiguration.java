package pro.itfray.config;

import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.jvm.convention.otel.OpenTelemetryJvmClassLoadingMeterConventions;
import io.micrometer.core.instrument.binder.jvm.convention.otel.OpenTelemetryJvmCpuMeterConventions;
import io.micrometer.core.instrument.binder.jvm.convention.otel.OpenTelemetryJvmMemoryMeterConventions;
import io.micrometer.core.instrument.binder.jvm.convention.otel.OpenTelemetryJvmThreadMeterConventions;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.observation.OpenTelemetryServerRequestObservationConvention;

/**
 * Registers the {@link InstallOpenTelemetryAppender} and Micrometer conventions to get metrics
 * which adhere to the OpenTelemetry conventions.
 */
@Configuration(proxyBeanMethods = false)
public class OpenTelemetryConfiguration {

  /**
   * An {@link InitializingBean} (it gets called by Spring when the app starts) to register the
   * {@link OpenTelemetry} instance onto the Logback {@link OpenTelemetryAppender}.
   */
  static class InstallOpenTelemetryAppender implements InitializingBean {

    private final OpenTelemetry openTelemetry;

    InstallOpenTelemetryAppender(OpenTelemetry openTelemetry) {
      this.openTelemetry = openTelemetry;
    }

    @Override
    public void afterPropertiesSet() {
      OpenTelemetryAppender.install(this.openTelemetry);
    }
  }

  @Bean
  InstallOpenTelemetryAppender installOpenTelemetryAppender(OpenTelemetry openTelemetry) {
    return new InstallOpenTelemetryAppender(openTelemetry);
  }

  @Bean
  OpenTelemetryServerRequestObservationConvention
      openTelemetryServerRequestObservationConvention() {
    return new OpenTelemetryServerRequestObservationConvention();
  }

  @Bean
  OpenTelemetryJvmCpuMeterConventions openTelemetryJvmCpuMeterConventions() {
    return new OpenTelemetryJvmCpuMeterConventions(Tags.empty());
  }

  @Bean
  OpenTelemetryJvmMemoryMeterConventions openTelemetryJvmMemoryMeterConventions() {
    return new OpenTelemetryJvmMemoryMeterConventions(Tags.empty());
  }

  @Bean
  OpenTelemetryJvmThreadMeterConventions openTelemetryJvmThreadMeterConventions() {
    return new OpenTelemetryJvmThreadMeterConventions(Tags.empty());
  }

  @Bean
  OpenTelemetryJvmClassLoadingMeterConventions openTelemetryJvmClassLoadingMeterConventions() {
    return new OpenTelemetryJvmClassLoadingMeterConventions();
  }
}
