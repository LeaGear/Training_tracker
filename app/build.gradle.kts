plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") // Добавляем сюда
}

kotlin {
    sourceSets.all {
        languageSettings {
            // Это отключит строгие проверки нового компилятора для KSP
            optIn("org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI")
        }
    }
}

android {
    namespace = "com.example.sporttracker"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.sporttracker"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    ksp {
        arg("room.generateKotlin", "false")
    }
}

dependencies {
    // Room
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    ksp("androidx.room:room-compiler:$room_version")

    // Compose - ФИКСИРУЕМ ВЕРСИЮ 1.7.0+, чтобы был IndicationNodeFactory
    val compose_version = "1.7.0"
    implementation("androidx.compose.ui:ui:$compose_version")
    implementation("androidx.compose.foundation:foundation:$compose_version")
    implementation("androidx.compose.material3:material3:1.3.0") // Material 1.3 требует Foundation 1.7
    implementation("androidx.compose.ui:ui-tooling-preview:$compose_version")
    debugImplementation("androidx.compose.ui:ui-tooling:$compose_version")

    // Календарь - ставим актуальную версию для Compose 1.7
    implementation("com.kizitonwose.calendar:compose:2.6.0")

    // Остальное
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
    implementation("androidx.core:core-splashscreen:1.0.1")

    // ВАЖНО: Закомментируй или удали ВСЕ строки с libs.androidx.compose...
    // и ОБЯЗАТЕЛЬНО удали/закомментируй BOM:
    // implementation(platform(libs.androidx.compose.bom))
}
