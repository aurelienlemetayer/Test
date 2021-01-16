object Config {
    const val minSdk = 21
    const val compileSdk = 30
    const val targetSdk = 30
    const val buildTools = "30.0.2"
}

object Versions {

    // <editor-fold desc="tools">
    const val gradleandroid = "4.1.1"
    const val kotlin = "1.4.21"
    // </editor-fold>

    // <editor-fold desc="google">
    const val androidx_appcompat = "1.2.0"
    const val androidx_core_ktx = "1.3.2"
    const val androidx_constraintLayout = "2.0.4"
    const val androidx_fragment_ktx = "1.2.5"
    const val androidx_liveData = "2.2.0"
    const val androidx_material = "1.2.1"
    const val androidx_recyclerview = "1.1.0"
    const val androidx_viewModel = "2.2.0"

    const val hilt = "2.31-alpha"
    const val hiltCompiler = "1.0.0-alpha02"
    const val hiltViewModel = "1.0.0-alpha02"
    // </editor-fold>

    // <editor-fold desc="testing">
    const val junit = "4.13"
    const val androidx_ext_junit = "1.1.2"
    const val androidx_espresso = "3.3.0"
    //</editor-fold>

    const val kotlinCoroutines = "1.4.2"
    const val okHttp = "4.9.0"
    const val retrofit = "2.9.0"
}

object ClassPath {
    const val tools_gradleandroid = "com.android.tools.build:gradle:${Versions.gradleandroid}"
    const val tools_kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val tools_daggerHilt = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
}

object Dependencies {

    // <editor-fold desc="google">
    const val androidx_appcompat = "androidx.appcompat:appcompat:${Versions.androidx_appcompat}"
    const val androidx_core_ktx = "androidx.core:core-ktx:${Versions.androidx_core_ktx}"
    const val androidx_constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.androidx_constraintLayout}"
    const val androidx_fragment_ktx = "androidx.fragment:fragment-ktx:${Versions.androidx_fragment_ktx}"
    const val androidx_liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.androidx_liveData}"
    const val androidx_material = "com.google.android.material:material:${Versions.androidx_material}"
    const val androidx_recyclerview = "androidx.recyclerview:recyclerview:${Versions.androidx_recyclerview}"
    const val androidx_viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.androidx_viewModel}"

    const val daggerCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    const val hiltCompiler = "androidx.hilt:hilt-compiler:${Versions.hiltCompiler}"
    const val hiltViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.hiltViewModel}"
    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.hilt}"
    // </editor-fold>

    // <editor-fold desc="testing">
    const val testlib_junit = "junit:junit:${Versions.junit}"
    const val testandroidx_ext_junit = "androidx.test.ext:junit:${Versions.androidx_ext_junit}"
    const val testandroidx_espressoCore = "androidx.test.espresso:espresso-core:${Versions.androidx_espresso}"
    // </editor-fold>

    const val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}"
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    const val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okHttp}"
    const val okHttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
}