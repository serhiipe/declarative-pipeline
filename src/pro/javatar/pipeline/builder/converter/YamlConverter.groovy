package pro.javatar.pipeline.builder.converter

import pro.javatar.pipeline.builder.model.Docker
import pro.javatar.pipeline.builder.model.Maven
import pro.javatar.pipeline.builder.Npm
import pro.javatar.pipeline.builder.model.Pipeline
import pro.javatar.pipeline.builder.model.Service
import pro.javatar.pipeline.builder.model.YamlConfig
import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

class YamlConverter {

    YamlConfig toYamlModel(def yml) {
        return new YamlConfig()
                .withService(retrieveService(yml))
                .withPipeline(retrievePipeline(yml))
                .withNpm(retrieveNpm(yml))
                .withMaven(retrieveMaven(yml))
                .withDocker(retrieveDockerList(yml))
    }

    Service retrieveService(def yml) {
        def service = yml.service
        dsl.echo "retrieveService: service: ${service}"
        return new Service()
                .withName(service.name)
                .withBuildType(service.buildType)
                .withUseBuildNumberForVersion(service.useBuildNumberForVersion)
                // TODO getVcsRepoByName
                .withRepo(service.vcs.repo)
    }

    Pipeline retrievePipeline(def yml) {
        def pipeline = yml.pipeline
        dsl.echo "retrievePipeline: pipeline: ${pipeline}"
        List<String> stages = new ArrayList<>()
        pipeline.stages.each{stage -> stages.add(stage)}
        return new Pipeline()
                .withPipelineSuit(pipeline.pipelineSuit)
                .withStages(stages)
    }

    List<Docker> retrieveDockerList(def yml) {
        def docker = yml.docker
        dsl.echo "retrieveDockerList: docker: ${docker}"
        List<Docker> dockers = new ArrayList<>()
        docker.each{dockerItem -> dockers.add(retrieveDocker(dockerItem))}
        return dockers
    }

    Docker retrieveDocker(def dockerItem) {
        dsl.echo "retrieveDocker: dockerItem: ${dockerItem}"
        Docker docker = new Docker()
                .withCredentialsId(dockerItem.credentialsId)
                .withRegistry(dockerItem.registry)
                .withEnv(retrieveEnvList(dockerItem.env))
        return docker
    }

    List<String> retrieveEnvList(def env) {
        dsl.echo "retrieveEnvList: env: ${env}"
        List<String> envList = new ArrayList<>()
        env.each{envItem -> envList.add(envItem)}
        return envList
    }

    Maven retrieveMaven(def yml) {
        def maven = yml.maven
        dsl.echo "retrieveMaven: maven: ${maven}"
        return new Maven()
                .withRepositoryId(maven.repository.id)
                .withRepositoryUrl(maven.repository.url)
                .withParams(maven.params)
    }

    Npm retrieveNpm(def yml) {
        def npm = yml.npm
        dsl.echo "retrieveNpm: npm: ${npm}"
        return new Npm()
                .withNpmType(npm.type)
                .withNpmVersion(npm.version)
                .withDistributionFolder(npm.distributionFolder)
                .withModuleRepository(npm.moduleRepository)
    }

}