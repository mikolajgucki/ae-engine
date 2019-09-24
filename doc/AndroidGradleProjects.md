Android Gradle projects
====

Creating project
----

1. Create project:
```
android create project --gradle --activity TestActivity --package com.andcreations.test --target android-16 --path . --gradle-version 2.2.0
```
2. Update `gradle/wrapper/gradle-wrapper.properties` and set `distributionUrl=http\://services.gradle.org/distributions/gradle-2.14.1-all.zip`
3. Update `build.gradle`:
    1. Replace `runProguard` with `minifyEnabled`.
    2. Replace `mavenCentral` with `jcenter` in `repositories`.
4. Add `ndk.dir` to `local.properties`


Output
----

Java compiled classes are stored in `build/intermediates/classes/debug` and in `build/intermediates/classes/release`.

Links
----

- [http://www.linuxdeveloper.space/android-project-from-command-line]
- [https://github.com/googlesamples/android-ndk/tree/master/hello-libs]
