/**
 * Copyright Javatar LLC 2018 ©
 * Licensed under the License located in the root of this repository (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://github.com/JavatarPro/pipeline-utils/blob/master/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pro.javatar.pipeline.service.orchestration

import pro.javatar.pipeline.service.vcs.HgService

import static pro.javatar.pipeline.service.PipelineDslHolder.dsl

/**
 * Author : Borys Zora
 * Date Created: 3/22/18 22:21
 */
class MesosService implements DockerOrchestrationService {

    def setup() {
        HgService.checkoutRepo("mesos-services-configuration", "default",
                "mesos-services-configuration", true)
    }

    @Override
    def dockerDeployContainer(String imageName, String imageVersion, String dockerRepositoryUrl, String environment) {
        dsl.echo "dockerDeployContainer(imageName: ${imageName}, imageVersion: ${imageVersion}, " +
                "dockerRepositoryUrl: ${dockerRepositoryUrl}, environment: ${environment})"

        dsl.sh "pwd; ls -l; ls -l .. "

        dsl.withEnv(["SERVICE=${imageName}", "DOCKER_REPOSITORY=${dockerRepositoryUrl}",
                     "RELEASE_VERSION=${imageVersion}", "LABEL_ENVIRONMENT=${environment}"]) {

            dsl.sh "../mesos-services-configuration/bin/mm-deploy -e ${environment} ${imageName} || " +
                    " (depcon -e ${environment} app rollback /${imageName} --wait; echo 'Deploy failed!'; exit 2)"
        }
    }
}
