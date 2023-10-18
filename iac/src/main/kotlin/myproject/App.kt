package myproject

import com.pulumi.Pulumi

class App {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Pulumi.run { context ->
                createDockerImage(context)
                createGitRepo()
                createStorage()
            }
        }
    }
}
