import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
}

android {
    namespace = "com.example.library"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
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