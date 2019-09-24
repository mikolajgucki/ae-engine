Setting up Android project
====

Creating the project
----

1. Create a directory in which you would like to create the project. Enter the directory.
2. Run
    ```
    `android create project --path . --activity <activity> --package <package> --target <target>`.
    ```
    Replace `<activity>` with the name of the activity class. Replace `<package>` with the package containing the class. Run `android list targets` to list available targets in your SDK.
3. Create file `build-ae.xml` with the contents (replace `your.project.name` with your real project name):
    ```xml
    <?xml version="1.0"?>
    
    <project name="your.project.name" default="ae.update">
        <!-- Get the enviromnent variables -->
        <property environment="ae.env"/>
        
        <!-- Verify the envrionment variable AE_DIST is set. -->
        <condition property="ae.dist" value="${ae.env.AE_DIST}">
            <isset property="ae.env.AE_DIST"/>
        </condition>
        <fail unless="ae.dist" message="Could not determine the path to the AE distribution"/>
        
        <!-- The directory with the Android project -->
        <property name="ae.android.project.dir" value="${basedir}"/>
        
        <!-- Loads the properties -->
        <property file="build-ae.properties"/>
    
        <!-- Import the build file from the distribution -->
        <import file="${ae.dist}/ant/android/build-android.xml"/>
    </project>
    ```
4. Create file `build-ae.properties` with contents:
    ```properties
    # The directory with the project
    ae.project.dir=../project
    ```

5. Import `build-ae.xml` from `build.xml`.

Editing the activity source file
----

1. Extend `org.libsdl.app.SDLActivity`
2. 

Updating the project
----

Run `ant ae.update` from the Android project directory to udate the project.

Cleaning the project
----

Run `ant ae.clean` from the Android project directory to clean the project.

