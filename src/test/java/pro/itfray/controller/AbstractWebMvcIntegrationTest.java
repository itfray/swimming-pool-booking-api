package pro.itfray.controller;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import pro.itfray.config.SecurityConfig;
import pro.itfray.filter.AddTraceIdFilter;

/** Base Web MVC test configuration exposing controllers under test. */
@ActiveProfiles("test")
@Import(SecurityConfig.class)
@WebMvcTest(
    controllers = {UserController.class},
    excludeFilters = {
      @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AddTraceIdFilter.class)
    })
public abstract class AbstractWebMvcIntegrationTest {}
