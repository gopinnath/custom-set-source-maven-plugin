package com.example;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;

import java.io.File;

import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

/**
 * Goal that sets directory as source/resource.
 */
@Mojo(name = "set-some-dir-as-source", requiresDependencyResolution = ResolutionScope.TEST, defaultPhase = LifecyclePhase.GENERATE_RESOURCES, threadSafe = true)
public class SetDirectoryAsSourceMojo extends AbstractMojo {

	@Parameter( defaultValue = "${session}", readonly = true )
    private MavenSession mavenSession;

    @Component
    private BuildPluginManager pluginManager;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    /**
     * Output location.
     */
    @Parameter(property = "additionalResourceFolder", defaultValue = "${project.basedir}/some-other-resource")
    protected File additionalResourceFolder;
    
    @Parameter(property = "additionalSourceFolder", defaultValue = "${project.basedir}/some-other-src")
    protected File additionalSourceFolder;

    /**
     * Main entry into mojo.
     *
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Executing Mark Directory as Source");

        executeMojo(
                plugin(
                        groupId("org.codehaus.mojo"),
                        artifactId("build-helper-maven-plugin"),
                        version("3.3.0")
                ),
                goal("add-source"),
                configuration(
                        element(name("sources"),
                                element(name("source"), getAdditionalSourceFolder().getPath())
                        )
                ),
                executionEnvironment(
                        project,
                        mavenSession,
                        pluginManager
                )
        );

        getLog().info("Executing Mark Directory as Resource");

        executeMojo(
                plugin(
                        groupId("org.codehaus.mojo"),
                        artifactId("build-helper-maven-plugin"),
                        version("3.3.0")
                ),
                goal("add-resource"),
                configuration(
                        element(name("resources"),
                                element(name("resource"),
                                        element(name("directory"), getAdditionalResourceFolder().getPath())
                                )
                        )
                ),
                executionEnvironment(
                        project,
                        mavenSession,
                        pluginManager
                )
        );

        project.addCompileSourceRoot(getAdditionalSourceFolder().getPath());
        project.addCompileSourceRoot(getAdditionalSourceFolder().getAbsolutePath());
        if (getLog().isInfoEnabled()) {
            getLog().info("Source directory: " + getAdditionalSourceFolder().getPath() + " added.");
            getLog().info("Source directory: " + getAdditionalSourceFolder().getAbsolutePath() + " added.");
        }

        Resource resource = new Resource();
        resource.setTargetPath(getAdditionalResourceFolder().getPath());
        resource.setTargetPath(getAdditionalResourceFolder().getAbsolutePath());
        getProject().addCompileSourceRoot(getAdditionalResourceFolder().getAbsolutePath());
        if (getLog().isInfoEnabled()) {
            getLog().info("Resource directory: " + getAdditionalResourceFolder().getPath() + " added.");
            getLog().info("Resource directory: " + getAdditionalResourceFolder().getAbsolutePath() + " added.");
        }

    }

    /**
     * @return Returns the Maven project.
     */
    public MavenProject getProject() {
        return project;
    }

	public File getAdditionalResourceFolder() {
		return additionalResourceFolder;
	}

	public void setAdditionalResourceFolder(File additionalResourceFolder) {
		this.additionalResourceFolder = additionalResourceFolder;
	}

	public File getAdditionalSourceFolder() {
		return additionalSourceFolder;
	}

	public void setAdditionalSourceFolder(File additionalSourceFolder) {
		this.additionalSourceFolder = additionalSourceFolder;
	}



}