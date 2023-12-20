@file:Suppress( "UNUSED_VARIABLE", "UNUSED", "UnstableApiUsage" )

import java.text.SimpleDateFormat
import java.util.Date

settings.rootProject.name = "design-pattern"

settings.include( "test-ignore" )
settings.include( "lib" )
settings.include( "app" )

val SETTINGS_HASH_CODE: String = Integer.toHexString( settings.hashCode() )

lateinit var javaPluginIds: Set<String>
lateinit var cppPluginIds: Set<String>
lateinit var rustPluginIds: Set<String>

pluginManagement {
    this.repositories {
        this.mavenCentral()
        this.gradlePluginPortal()
    }
    this.plugins {
        //REM: TODO-HERE...
    }
}

settings.dependencyResolutionManagement {
    this.repositories {
        this.mavenCentral()
        this.gradlePluginPortal()
    }
    this.versionCatalogs {
        this.register( "localLib" ) {
            this.from( files( "./gradle/localLib.versions.toml" ) )
        }
        this.register( "remoteLib" ) {
            this.from( files( "./gradle/remoteLib.versions.toml" ) )
        }
        //REM: TODO-HERE...
    }
}


settings.gradle.settingsEvaluated {
    println( String.format( "::: %s[ settings.gradle.kts, settings.gradle.settingsEvaluated ]", SETTINGS_HASH_CODE ) )

    javaPluginIds = setOf( "java", "java-application", "java-library", "application", "war", "kotlin", "groove" )
        .map { it.lowercase() }.toSet()
    cppPluginIds = setOf( "cpp", "cpp-application", "cpp-library" )
        .map { it.lowercase() }.toSet()
    rustPluginIds = setOf( "" )
        .map { it.lowercase() }.toSet()
}

fun pluginStrExtractor(plugins: PluginContainer): Set<String> =
    plugins.mapNotNull { it::class.simpleName?.substringBefore('$')?.takeIf { str -> str.isNotEmpty() } }.toSet()

settings.gradle.projectsEvaluated {
    println( String.format( "::: %s[ settings.gradle.kts, settings.gradle.projectsEvaluated. ]", SETTINGS_HASH_CODE ) )

    val VERSION_CATALOGS: VersionCatalogsExtension = this.rootProject.extensions
        .getByType<VersionCatalogsExtension>()
    val REMOTE_LIB: VersionCatalog = VERSION_CATALOGS.find( "remoteLib" ).get()
    val LOCAL_LIB: VersionCatalog = VERSION_CATALOGS.find( "localLib" ).get()

    fun Gradle.delete( path: String ) {
        val fileToDelete = file( path )
        if (fileToDelete.exists()) {
            fileToDelete.deleteRecursively()
            println("File or directory deleted: $fileToDelete")
        } else
            System.err.println("File or directory does not exist: $fileToDelete")
    }


    this.gradle.allprojects {
        val PROJECT = this.project
        val PROJECT_NAME: String = PROJECT.name
        val PROJECT_NAME_COMPOUND: String = "${PROJECT.rootProject.name}-${PROJECT_NAME}"

        lateinit var mavenPublish: PublishingExtension
        lateinit var sourceSets: SourceSetContainer

        println(
            String.format( "::: %s[ %s[ %s - %s ] ]", SETTINGS_HASH_CODE,
                Integer.toHexString( this.hashCode() ), PROJECT_NAME,
                PROJECT_NAME.equals( this.rootProject.name ).let {
                    if( it )
                        "Root Project - Plugins${ pluginStrExtractor( this.rootProject.plugins ) }"
                    else
                        "Sub Project - Plugins${ pluginStrExtractor( this.project.plugins )}"
                }
            )
        );
        if( cppPluginIds.any{ cppPluginId -> this.project.plugins.hasPlugin( cppPluginId ) } ) {
            //REM: TODO-HERE...
        }
        if( rustPluginIds.any{ rustPluginId -> this.project.plugins.hasPlugin( rustPluginId ) } ) {
            //REM: TODO-HERE...
        }
        if( javaPluginIds.any{ javaPluginId -> this.project.plugins.hasPlugin( javaPluginId ) } ) {

            sourceSets = this.project.extensions
                .getByType<SourceSetContainer>()

            sourceSets.apply {
                this.named( "main" ) {
                    this.java {
                        this.setSrcDirs( listOf( "src/main/" ) )
                        this.setExcludes( listOf( "src/main/resources/" ) )
                    }
                    this.resources {
                        this.setSrcDirs( listOf( "src/main/resources/" ) )
                    }
                }
                this.named( "test" ) {
                    this.java {
                        this.setSrcDirs( listOf( "src/test/" ) )
                        this.setExcludes( listOf( "src/test/resources/" ) )
                    }
                    this.resources {
                        this.setSrcDirs( listOf( "src/test/resources/" ) )
                    }
                }
            }

            if( this.project.plugins.hasPlugin( PublishingPlugin::class.java ) ) {

                mavenPublish = this.project.extensions
                    .getByType<PublishingExtension>()

                mavenPublish.apply {
                    this.publications {
                        this.register<MavenPublication>( "mvn_pub__$PROJECT_NAME_COMPOUND" ) {
                            this.from( components["java"] )
                            this.artifactId = PROJECT_NAME_COMPOUND
                        }
                        this.register<IvyPublication>( "ivy_pub__$PROJECT_NAME_COMPOUND" ) {
                            this.from( components["java"] )
                            this.module = PROJECT_NAME_COMPOUND
                        }
                        //REM: TODO-HERE...
                    }
                    this.repositories {
                        this.maven {
                            this.name = "mvn_repo__$PROJECT_NAME_COMPOUND"
                            this.url = uri( project.layout.projectDirectory.dir( "repo/mvn/" ) )
                        }
                        this.maven {
                            this.name = "mvn_repo__mono_repo"
                            this.url = uri( project.rootProject.layout.projectDirectory.dir( "repo/mvn/$PROJECT_NAME_COMPOUND" ) )
                        }
                        this.ivy {
                            this.name = "ivy_repo__$PROJECT_NAME_COMPOUND"
                            this.url = uri( project.layout.projectDirectory.dir( "repo/ivy" ) )
                        }
                        this.ivy {
                            this.name = "ivy_repo__mono_repo"
                            this.url = uri( project.rootProject.layout.projectDirectory.dir( "repo/ivy/$PROJECT_NAME_COMPOUND" ) )
                        }
                        //REM: TODO-HERE...
                    }
                }

                project.tasks.register( "delete__mvn_repo_$PROJECT_NAME_COMPOUND" ) {
                    this.group = "publishing"
                    this.doLast {
                        gradle.delete( project.layout.projectDirectory.dir("repo/mvn").toString() )
                    }
                }

                project.tasks.register( "delete__ivy_repo_$PROJECT_NAME_COMPOUND" ) {
                    this.group = "publishing"
                    this.doLast {
                        gradle.delete( project.layout.projectDirectory.dir("repo/ivy").toString() )
                    }
                }

                project.tasks.register( "delete__mvn_repo_mono_repo_$PROJECT_NAME_COMPOUND" ) {
                    this.group = "publishing"
                    this.doLast {
                        gradle.delete( project.rootProject.layout.projectDirectory.dir("repo/mvn/$PROJECT_NAME_COMPOUND").toString() )
                    }
                }

                project.tasks.register( "delete__ivy_repo_mono_repo_$PROJECT_NAME_COMPOUND" ) {
                    this.group = "publishing"
                    this.doLast {
                        gradle.delete( project.rootProject.layout.projectDirectory.dir("repo/ivy/$PROJECT_NAME_COMPOUND").toString() )
                    }
                }
            }

            this.project.dependencies {
                this.add(
                    "testImplementation",
                    REMOTE_LIB.findLibrary( "junit-jupiter-api" ).get()
                )?.because( "For Unit Testing." )
                this.add(
                    "testRuntimeOnly",
                    REMOTE_LIB.findLibrary( "junit-jupiter-engine" ).get()
                )?.because( "For Unit Testing" )
            }

            this.tasks.withType<Test> {
                this.useJUnitPlatform()
            }

            this.tasks.withType<Jar> {
                val ARCHIVE_BASE_NAME: String? = this.archiveBaseName.get().takeIf{ it.isNotBlank() }
                this.archiveBaseName.set( ARCHIVE_BASE_NAME ?: "$PROJECT_NAME_COMPOUND.jar" )
//                this.archiveVersion.set( PROJECT.version.toString() )

                this.manifest.attributes[ "whoami" ] = "Jayo, A.R. B."
                this.manifest {
                    this.attributes[ "Manifest-Version" ] = this.attributes[ "Manifest-Version" ]
                        .takeIf{ it.toString().isNotBlank() }
                        ?: "2.0"
                    this.attributes[ "Name" ] = this.attributes[ "Name" ]
                        .takeIf{ it.toString().isNotBlank() }
                        ?: archiveFileName.get()
                    this.attributes[ "Built-By" ] = this.attributes[ "Built-By" ]
                        .takeIf{ it.toString().isNotBlank() }
                        ?: ( PROJECT.providers.gradleProperty( "project.group" )
                            .orNull?.takeIf{ it.isNotBlank() }?: "jayo.arb.learnj"
                            ).split( ".", " " ).reversed().joinToString( " " ).uppercase()
                    this.attributes[ "Built-Time" ] = this.attributes[ "Build-Time" ]
                        .takeIf{ it.toString().isNotBlank() }
                        ?: SimpleDateFormat( "yyyy-MM-dd h:mm:ss-a" ).format( Date() )
                    this.attributes[ "Description" ] = this.attributes[ "Description" ]
                        .takeIf{ it.toString().isNotBlank() }
                        ?: "Move forward, learning and practicing makes perfect."
                    this.attributes[ "Implementation-Vendor" ] = this.attributes[ "Implementation-Vendor" ]
                        .takeIf{ it.toString().isNotBlank() }
                        ?: ( PROJECT.providers.gradleProperty( "project.group" )
                            .orNull?.takeIf{ it.isNotBlank() }?: "jayo.arb.learnj"
                            )
                    this.attributes[ "Implementation-Title" ] = this.attributes[ "Implementation-Title" ]
                        .takeIf{ it.toString().isNotBlank() }
                        ?: archiveBaseName.get()
                    this.attributes[ "Implementation-Version" ] = this.attributes[ "Implementation-Version" ]
                        .takeIf{ it.toString().isNotBlank() }
                        ?: PROJECT.version
                    this.attributes[ "Specification-Vendor" ] = this.attributes[ "Specification-Vendor" ]
                        .takeIf{ it.toString().isNotBlank() }
                        ?: "${
                            ( PROJECT.providers.gradleProperty( "project.group" )
                                .orNull?.takeIf{ it.isNotBlank() }?: "jayo.arb.learnj"
                            )
                        }, move forward."
                    this.attributes[ "Specification-Title" ] = this.attributes[ "Implementation-Title" ]
                        .takeIf{ it.toString().isNotBlank() }
                        ?: "${ archiveBaseName.get() }, learning and practicing makes perfect."
                    this.attributes[ "Specification-Version" ] = this.attributes[ "Specification-Version" ]
                        .takeIf{ it.toString().isNotBlank() }
                        ?: "${ PROJECT.version }, infinite potential and applications."
                }
            }
        }
    }
}
