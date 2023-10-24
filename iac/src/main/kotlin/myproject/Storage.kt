package myproject

import com.pulumi.asset.FileAsset
import com.pulumi.command.local.Command
import com.pulumi.command.local.CommandArgs
import com.pulumi.core.Output
import com.pulumi.gcp.storage.*
import com.pulumi.resources.CustomResourceOptions
import java.nio.file.Files
import java.nio.file.Paths

fun createStorage() {

    val buckets = listOf("naubertrand@gmail.com", "bertrand.nau@wescale.fr").map { usermail ->
        val username = """(?<name>.*)@.*""".toRegex().matchEntire(usermail)?.groups?.get("name")?.value
            ?.replace(".", "_")
            ?: error("Invalid email")

        val bucketRemoteStorage = Bucket("dvc-remote-storage-$username", BucketArgs.builder()
            .name("dvc-remote-storage-hands-on-$username")
            .location("europe-west1")
            .forceDestroy(true)
            .build())

        BucketIAMBinding("participant-write-access-$username", BucketIAMBindingArgs.builder()
            .bucket(bucketRemoteStorage.name())
            .role("roles/storage.objectAdmin")
            .members(listOf("user:$usermail"))
            .build())

        return@map bucketRemoteStorage
    }

    Output.all(buckets.map { it.name() }).applyValue {
        Command("push-dvc",
            CommandArgs.builder()
                .dir("../solutions")
                .create(
                    it.fold("") { acc, s -> "$acc dvc remote add -d $s gs://$s --force && dvc push -r $s && " }
                        .removeSuffix(" && ")
                )
                .build())
        return@applyValue it
    }

    val bucketInputDependencies = Bucket("dvc-input-dependencies", BucketArgs.builder()
        .name("dvc-input-dependencies-hands-on")
        .location("europe-west1")
        .build())

    BucketIAMBinding("input-dependencies-readonly", BucketIAMBindingArgs.builder()
        .bucket(bucketInputDependencies.name())
        .role("roles/storage.objectViewer")
        .members(listOf("allUsers"))
        .build())

    BucketObject("data", BucketObjectArgs.builder()
        .bucket(bucketInputDependencies.name())
        .name("data.zip")
        .source(FileAsset("../resources/data.zip"))
        .build())

    BucketObject("data-v2", BucketObjectArgs.builder()
        .bucket(bucketInputDependencies.name())
        .name("data-v2.zip")
        .source(FileAsset("../resources/data-v2.zip"))
        .build())

    BucketObject("base-model", BucketObjectArgs.builder()
        .bucket(bucketInputDependencies.name())
        .name("base.h5")
        .source(FileAsset("../resources/base.h5"))
        .build())

    Files.walk(Paths.get("../resources/prerequisites"))
        .use {
            it.filter { Files.isRegularFile(it) }
                .forEach { file ->
                    BucketObject(file.fileName.toString(), BucketObjectArgs.builder()
                        .bucket(bucketInputDependencies.name())
                        .name("prerequisites/${file.fileName}")
                        .source(FileAsset(file.toString()))
                        .build())
                }
        }
}
