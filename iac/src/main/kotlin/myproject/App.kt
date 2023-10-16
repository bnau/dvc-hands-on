package myproject;

import com.pulumi.Pulumi
import com.pulumi.gcp.sourcerepo.Repository
import com.pulumi.command.local.Command
import com.pulumi.command.local.CommandArgs
import com.pulumi.gcp.projects.Service
import com.pulumi.gcp.projects.ServiceArgs
import com.pulumi.gcp.sourcerepo.RepositoryArgs
import com.pulumi.resources.CustomResourceOptions

fun createGitRepo() {
    val api = Service("sourcerepo.googleapis.com", ServiceArgs.builder()
        .service("sourcerepo.googleapis.com")
        .build())
    val repo = Repository("git-repo", RepositoryArgs.Empty,
        CustomResourceOptions.builder()
            .dependsOn(api)
            .build())
    repo.url().applyValue {
        val configureAndPush = Command("git-configure-and-push",
            CommandArgs.builder()
                .dir("../solutions")
                .create("git remote add origin ${it}")
                .build(),
            CustomResourceOptions.builder()
                .parent(repo)
                .build())
        Command("git-push",
            CommandArgs.builder()
                .dir("../solutions")
                .create("git push -u origin master")
                .build(),
            CustomResourceOptions.builder()
                .parent(configureAndPush)
                .build())
    }
}

class App {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Pulumi.run { context ->
                createDockerImage(context)
                createGitRepo()
            }
        }
    }
}
