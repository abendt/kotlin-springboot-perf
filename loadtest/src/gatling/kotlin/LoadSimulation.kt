import io.gatling.javaapi.core.CoreDsl.atOnceUsers
import io.gatling.javaapi.core.CoreDsl.constantConcurrentUsers
import io.gatling.javaapi.core.CoreDsl.exec
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.core.PopulationBuilder
import io.gatling.javaapi.core.ScenarioBuilder
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.http
import io.gatling.javaapi.http.HttpDsl.status
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LoadSimulation : Simulation() {

    private val httpProtocol = http
        .baseUrl("http://localhost:8080") // Base URL

    val users = constantConcurrentUsers(300).during(60)

    init {
        setUp(
            buildSim("noop")
        ).protocols(httpProtocol)
    }

    private fun buildSim(vararg ids: String): PopulationBuilder =
        ids.map {
            scnAnnotate(it).injectOpen(atOnceUsers(1))
                .andThen(buildScenario(it).injectClosed(users))
        }.reduce { a, b ->
            a.andThen(b)
        }
}

val logger: Logger = LoggerFactory.getLogger(LoadSimulation::class.java)

private fun scnAnnotate(id: String): ScenarioBuilder {
    val tag = "annotate$id"

    return scenario("Annotate $id")
        .doIf { true != it.get<Boolean>(tag) }
        .then(exec {
            createGrafanaAnnotation(id)
            it
        }, exec {
            it.set(tag, "true")
        })
}

private fun createGrafanaAnnotation(id: String) {
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

    RestAssured.given()
        .auth().preemptive().basic("admin", "admin")
        .baseUri("http://localhost:3000")
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(
            """
            {
              "time": ${System.currentTimeMillis()},
              "tags":["gatling"],
              "text":"$id starting"
            }
        """.trimIndent()
        )
        .post("/api/annotations")
        .then()
        .statusCode(200)

    logger.info("annotated {}", id)
}

private fun buildScenario(op: String): ScenarioBuilder {
    return scenario("Scenario $op")
        .exec(
            http("GET /$op")
                .get("/$op")
                .check(status().`is`(200))
        )
        .pause(1)
}
