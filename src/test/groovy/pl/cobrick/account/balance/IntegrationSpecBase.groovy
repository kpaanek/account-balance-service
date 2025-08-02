package pl.cobrick.account.balance

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName
import spock.lang.Shared
import spock.lang.Specification

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationSpecBase extends Specification {

    @Shared
    static def mongoContainer = new MongoDBContainer(DockerImageName.parse('mongo:8.0.3'))

    @Autowired
    MongoTemplate mongoTemplate

    @Autowired
    MockMvc mockMvc

    @DynamicPropertySource
    static def registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add('spring.data.mongodb.uri', () -> mongoContainer.replicaSetUrl)
    }

    def setupSpec() {
        if (!mongoContainer.isRunning()) {
            mongoContainer.start()
        }
    }

    def cleanup() {
        mongoTemplate.dropCollection('accounts')
    }
}
