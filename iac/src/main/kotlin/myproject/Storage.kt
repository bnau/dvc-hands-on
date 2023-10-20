package myproject

import com.pulumi.asset.FileArchive
import com.pulumi.asset.FileAsset
import com.pulumi.gcp.storage.*
import java.nio.file.Files
import java.nio.file.Paths

fun createStorage() {
    val bucketRemoteStorage = Bucket("dvc-remote-storage", BucketArgs.builder()
        .name("dvc-remote-storage-hands-on")
        .location("europe-west1")
        .forceDestroy(true)
        .build())

    BucketIAMBinding("participant-write-access", BucketIAMBindingArgs.builder()
        .bucket(bucketRemoteStorage.name())
        .role("roles/storage.objectAdmin")
        .members(listOf("user:naubertrand@gmail.com"))
        .build())

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
        .name("base.keras")
        .source(FileAsset("../resources/base.keras"))
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
