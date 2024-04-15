package t.reversdevelopment.testforreversedevelopment

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@SpringBootApplication
@EnableReactiveMongoRepositories("t.reversdevelopment.testforreversedevelopment.database.repositories")
class TestforreversedevelopmentApplication

fun main(args: Array<String>) {
    runApplication<TestforreversedevelopmentApplication>(*args)
}
