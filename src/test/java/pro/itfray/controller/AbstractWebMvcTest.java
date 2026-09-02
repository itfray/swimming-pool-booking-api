package pro.itfray.controller;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import pro.itfray.config.SecurityConfig;

/**
 * Base Web MVC test configuration exposing controllers under test.
 */
@ActiveProfiles("test")
@Import(SecurityConfig.class)
@WebMvcTest(controllers = {
    UserController.class})
public abstract class AbstractWebMvcTest {

}
