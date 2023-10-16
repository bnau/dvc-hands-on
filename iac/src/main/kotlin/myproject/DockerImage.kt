package myproject

import com.pulumi.Context
import com.pulumi.command.local.Command
import com.pulumi.command.local.CommandArgs
import com.pulumi.core.Output
import com.pulumi.docker.Image
import com.pulumi.docker.ImageArgs
import com.pulumi.docker.inputs.DockerBuildArgs
import com.pulumi.gcp.artifactregistry.Repository
import com.pulumi.gcp.artifactregistry.RepositoryArgs
import com.pulumi.gcp.artifactregistry.RepositoryIamBinding
import com.pulumi.gcp.artifactregistry.RepositoryIamBindingArgs
import com.pulumi.gcp.artifactregistry.inputs.RepositoryDockerConfigArgs
import com.pulumi.resources.CustomResourceOptions

fun createDockerImage(context: Context) {
    val myRepo = Repository("my-repo", RepositoryArgs.builder()
        .dockerConfig(RepositoryDockerConfigArgs.builder()
            .immutableTags(true)
            .build())
        .location("europe-west1")
        .repositoryId("my-repo")
        .format("DOCKER").build())

    RepositoryIamBinding("my-repo-binding", RepositoryIamBindingArgs.builder()
        .location(myRepo.location())
        .repository(myRepo.repositoryId())
        .members(listOf("allUsers"))
        .role("roles/artifactregistry.reader")
        .build())

    Output.all(myRepo.location(), myRepo.repositoryId()).applyValue {
        val configureDocker = Command("configure-docker",
            CommandArgs.builder()
                .create("gcloud auth configure-docker ${it[0]}-docker.pkg.dev --quiet")
                .build())
        val imageName = "${it[0]}-docker.pkg.dev/${context.projectName()}/${it[1]}/hands-on-dvc:init"
        Image(imageName, ImageArgs.builder()
            .build(
                DockerBuildArgs.builder()
                    .dockerfile("../resources/Dockerfile")
                    .context("../resources")
                    .build())
            .imageName(imageName)
            .build(),
            CustomResourceOptions.builder()
                .dependsOn(configureDocker)
                .build())
        return@applyValue it
    }
}
