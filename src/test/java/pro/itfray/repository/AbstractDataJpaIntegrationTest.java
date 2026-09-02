package pro.itfray.repository;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pro.itfray.AbstractSpringIntegrationTest;

/**
 * Base class for JPA integration tests.
 */
@DataJpaTest
@ActiveProfiles("test")
public class AbstractDataJpaIntegrationTest extends AbstractSpringIntegrationTest {

}
