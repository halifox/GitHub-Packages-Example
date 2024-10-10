# 发布并使用私有 Android 库到 GitHub Packages

本指南将带您通过将私有 Android 库上传到 GitHub Packages，并在其他项目中使用它。确保您准备好 GitHub Token 以进行私有仓库的身份验证。

## 前提条件

在项目的根目录创建一个 `local.properties` 文件，用于存储 GitHub 身份验证信息：

```properties
GITHUB_URL=https://maven.pkg.github.com/{your_github_username}/{your_repository_name}
GITHUB_USERNAME=your_github_username
GITHUB_TOKEN=your_github_token
```

确保**不要将此文件提交到版本控制系统**。

## 将私有库发布到 GitHub Packages

在您的库项目的 `build.gradle.kts` 文件中添加以下配置：

### 模块的 `build.gradle.kts` 文件

```kotlin
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("maven-publish")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
val prop = Properties().apply {
    load(FileInputStream(File(rootProject.rootDir, "local.properties")))
}
afterEvaluate {
    publishing {
        repositories {
            maven {
                name = "GitHub-Packages-Example"
                setUrl(prop.getProperty("GITHUB_URL"))
                credentials {
                    username = prop.getProperty("GITHUB_USERNAME")
                    password = prop.getProperty("GITHUB_TOKEN")
                }
            }
        }
        publications {
            create<MavenPublication>("maven") {
                from(components["release"])
                groupId = "com.example"
                artifactId = "library"
                version = "1.0.0"
            }
        }
    }
}
```

### 发布库

在项目目录中运行以下命令，将库发布到 GitHub Packages：

```bash
./gradlew publish
```

## 在另一个项目中使用私有库

要在另一个项目中使用已发布的库，请在该项目的 `build.gradle.kts` 文件中配置 GitHub 仓库和依赖项。

### 根项目的 `build.gradle.kts` 文件

```kotlin
import java.io.FileInputStream
import java.util.Properties

val prop = Properties().apply {
    load(FileInputStream(File(rootProject.projectDir, "local.properties")))
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            setUrl(prop.getProperty("GITHUB_URL"))
            credentials {
                username = prop.getProperty("GITHUB_USERNAME")
                password = prop.getProperty("GITHUB_TOKEN")
            }
        }
    }
}
```

### 项目的 `build.gradle.kts` 文件

将库依赖添加到您想要使用该库的模块中：

```kotlin
dependencies {
    implementation("com.example:library:1.0.0")
}
```

## 常见问题

### 无法访问私有库

如果无法访问私有库，请检查以下内容：
- 确保 GitHub Token 具有 **`repo` 和 `packages`** 权限。
- 确保 `local.properties` 文件中的凭据正确。

### 发布失败

如果发布失败，通常是由于身份验证问题或 `local.properties` 文件的配置问题。确保您的 `local.properties` 文件存在且正确加载。

按照本指南操作，您应该能够成功将 Android 库上传到 GitHub Packages 并在其他项目中使用它。如需进一步帮助，请参阅 [GitHub 文档](https://docs.github.com/en/packages) 或 GitHub Discussions。