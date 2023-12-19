

//REM: For qualified classes naming on Testing and Application
public sealed class TaskConfig {
    public abstract val taskName: String?
    public abstract val targetClass: String

    public companion object {
        public fun create( taskName: String?, targetClass: String ): TaskConfig {
            require( !targetClass.isBlank() ) { "targetClass must not be null or empty." }
            return TaskConfigImpl(taskName, targetClass)
        }
    }

    private data class TaskConfigImpl(
        public override val taskName: String?,
        public override val targetClass: String
    ) : TaskConfig()
}


plugins {
    `java-library`
    `version-catalog`
    `maven-publish`
    `ivy-publish`
}

project.version = localLib.design.pattern.lib.get().version.takeIf { it.toString().isNotBlank() }?: "unknown-version"
project.group = localLib.design.pattern.lib.get().group.takeIf{ it.toString().isNotBlank() }?: "unknown-group"

val PROJECT_HASH_CODE: String = Integer.toHexString( project.hashCode() )
val PROJECT_NAME_COMPOUND: String = "${project.rootProject.name}-${project.name}"
//REM: JPMS, module name
val MODULE_NAME: String ="${project.group}."
    .replace( Regex("[\\\\ _.*/+-]+" ), "." )
    .plus( PROJECT_NAME_COMPOUND.replace( Regex( "[\\\\ \\._*/+-]+" ), "_" ) )
    .replace( Regex( "^[._]+|[._]+$" ), "" )
    .lowercase()

val TASK_CONFIG_TESTS: Set<TaskConfig> = setOf(
    TaskConfig.create( null, "$MODULE_NAME.creational.product.test.TestFruitFactory" )
)


println( String.format( "::: %s, project.name: '%s'", PROJECT_HASH_CODE, project.name ) )
println( String.format( "::: %s, project.name compounded: '%s'", PROJECT_HASH_CODE, PROJECT_NAME_COMPOUND ) )
println( String.format( "::: %s, project.version: '%s'", PROJECT_HASH_CODE, project.group ) )
println( String.format( "::: %s, project.group: '%s'", PROJECT_HASH_CODE, project.version ) )
println( String.format( "::: %s, project JPMS module name: '%s'", PROJECT_HASH_CODE, MODULE_NAME ) )

project.java {
    //REM: TODO-HERE...
}

project.sourceSets {
    this.main {
        this.java {
            this.setSrcDirs( listOf( "src/main/" ) )
            this.setExcludes( listOf( "src/main/resources/" ) )
        }
        this.resources {
            this.setSrcDirs( listOf( "src/main/resources/" ) )
        }
    }
    this.test {
        this.java {
            this.setSrcDirs( listOf( "src/test/" ) )
            this.setExcludes( listOf( "src/test/resources/" ) )
        }
        this.resources {
            this.setSrcDirs( listOf( "src/test/resources/" ) )
        }
    }
}

project.dependencies {
    //REM: TODO-HERE...
}

project.versionCatalogs {
    //REM: TODO-HERE...
}

project.publishing {
    this.publications {
        //REM: TODO-HERE...
    }

    this.repositories {
        //REM: TODO-HERE...
    }
}

fun Test.configureCustomDefaultTestTask( testTask: Test ) {
    testTask.group = "default__custom_test"
    testTask.description = "Custom TEST task"

    testTask.useJUnitPlatform()

    testTask.doLast {
        //REM: TODO-HERE; Your custom TEST creation actions go here
        println("::: Executed ${ testTask.name } TEST task!")
    }
}

fun Jar.configureCustomDefaultJarTask(jarTask: Jar) {
    jarTask.group = "default__custom_jar"
    jarTask.description = "Custom JAR task"

    jarTask.dependsOn( project.tasks.assemble )

    //REM: Set the JAR file name
//    jarTask.archiveBaseName.set( PROJECT_NAME_COMPOUND )

    //REM: Customize the manifest
    jarTask.manifest {
        this.attributes[ "Manifest-Version" ] = "1.0"
    }

    //REM: Add files or directories to the JAR if needed
    jarTask.from( project.sourceSets.main.get().output )
    jarTask.from( project.sourceSets.main.get().resources )

    jarTask.doLast {
        //REM: TODO-HERE; Your custom JAR creation actions go here
        println("::: Executed ${jarTask.name} JAR task!")
    }
}


TASK_CONFIG_TESTS.forEach { taskConfigTest ->
    project.tasks.register<Test>(
        "test__${ taskConfigTest.taskName?: taskConfigTest.targetClass }@[${ project.name }]"
    ) {
        this.configureCustomDefaultTestTask( this )
        this.group = "verification"
        this.filter.setIncludePatterns( taskConfigTest.targetClass )
    }
}

project.tasks.withType<Jar> {
    this.manifest.attributes[ "Manifest-Version" ] = "1.0";
}

//REM: TODO-HERE; properly implement it... work it properly, thanks...
project.tasks.register<Jar>( "jar__design_pattern_lib" ) {
    this.configureCustomDefaultJarTask( this )

    this.group = "build"
    this.manifest.attributes[ "Manifest-Version" ] = "2.0";
    this.archiveBaseName.set( PROJECT_NAME_COMPOUND )

}

project.tasks.register("build__all") {
    this.group = "build"
//    this.dependsOn( project.tasks.generateCatalogAsToml )
//    this.dependsOn( project.tasks.compileJava )
//    this.dependsOn( project.tasks.processResources )
//    this.dependsOn( project.tasks.classes )
//    this.dependsOn( project.tasks.jar )
    this.dependsOn( project.tasks.assemble )
//    this.dependsOn( project.tasks.compileTestJava )
//    this.dependsOn( project.tasks.processTestResources )
//    this.dependsOn( project.tasks.testClasses )
    this.dependsOn( project.tasks.test )
    this.dependsOn( project.tasks.check )
    this.dependsOn( project.tasks.getByName( "jar__design_pattern_lib" ) )
}
