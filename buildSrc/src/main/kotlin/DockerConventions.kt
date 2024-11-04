import org.gradle.api.Project

import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering

/**
 * configure DockerBuildImage with some sensible defaults:
 * - apply the _docker-remote-api_ plugin
 * - use Dockerfile from _src/main/docker_
 * - the built image will be tagged with _imageTag_
 * - use application artifact created by the _bootJar_ task
 * - pass the current git sha (GITHUB_SHA) as build parameter
 * - create gradle configuration dockerImage and attach the image artifact id file
 */
fun Project.configureDockerBuildImage(imageTag: String) {

    apply("plugin" to "com.bmuschko.docker-remote-api")

    val copyDockerFiles by tasks.registering(Copy::class) {
        from("src/main/docker")
        from(tasks.getByPath("bootJar"))
        into("build/dockerImage")
    }

    val buildDockerImage by tasks.registering(DockerBuildImage::class) {
        dependsOn(copyDockerFiles)

        inputDir.fileProvider(copyDockerFiles.map { it.destinationDir })
        images.add(imageTag)

        val args =
            providers.environmentVariable("GITHUB_SHA")
                .orElse("local")
                .map {
                    mapOf("COMMIT" to it)
                }

        buildArgs.set(args)
    }

    val dockerImage by configurations.creating

    artifacts {
        add(dockerImage.name, buildDockerImage.map { it.imageIdFile })
    }
}
