package myproject

import com.pulumi.command.local.Command
import com.pulumi.command.local.CommandArgs
import com.pulumi.github.Repository
import com.pulumi.github.RepositoryArgs
import com.pulumi.resources.CustomResourceOptions

fun createGitRepo() {
    val repo = Repository("dvc-project", RepositoryArgs.builder()
        .name("dvc-project")
        .build())
    repo.gitCloneUrl().applyValue {
        Command("git-configure-and-push",
            CommandArgs.builder()
                .dir("../solutions")
                .create("git remote rm origin && git remote add origin ${it.replace("git://","https://")} && git push --all origin")
                .archivePaths(("../solutions"))
                .triggers(listOf(System.currentTimeMillis().toString()))
                .build(),
            CustomResourceOptions.builder()
                .parent(repo)
                .build())
        return@applyValue it
    }
}

