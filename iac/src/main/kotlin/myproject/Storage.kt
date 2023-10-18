package myproject

import com.pulumi.asset.FileAsset
import com.pulumi.gcp.storage.*

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
        .forceDestroy(true)
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

    BucketObject("base-model", BucketObjectArgs.builder()
        .bucket(bucketInputDependencies.name())
        .name("base.keras")
        .source(FileAsset("../resources/base.keras"))
        .build())
}
