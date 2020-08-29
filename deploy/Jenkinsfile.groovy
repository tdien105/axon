#!/usr/bin/env groovy

pipeline {
    agent {
      node {
        label 'jenkins-slave'
      }
    }
    

    environment {
        ansible_deploy_path   = "${env.WORKSPACE}/socialhead/prod/PROD-K8S-Build-Publish-API"

        main_repo_url         = "git@github.com:YoungWorldTechnology/sohead_publish.git"
        main_repo_branch      = "master"

        local_config_path     = "${ansible_deploy_path}/build"
        docker_registry       = "655894223594.dkr.ecr.ap-southeast-1.amazonaws.com"
        container_name        = "publishapi-prod"

    }

    stages {
        stage('Fetch code') {
            steps {
                dir ("${local_config_path}") {
                    git credentialsId: '14a7131b-47e1-4a78-a0d8-95380dd629f6', url: "${main_repo_url}", branch: "${main_repo_branch}"
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    dir ("${local_config_path}") {
                        ansiColor('xterm') {
                            // shipping-app
                            sh """aws --region ap-southeast-1 ecr get-login --no-include-email | sudo bash ; \
                            mv ../.env ./ ;\

                            sudo docker run --rm -v \$(pwd):/app tdien105/php:7.3-pg php -d memory_limit=-1 /usr/local/bin/composer update ;\

                            sudo docker build -t ${docker_registry}/${container_name}:${env.BUILD_NUMBER} .; \
                            sudo docker push ${docker_registry}/${container_name}:${env.BUILD_NUMBER}; \
                            """


                            // build job: 'PROD-K8S-Deploy-Publish-API',
                            //       parameters: [string(name: 'Version', value: "$BUILD_NUMBER"), string(name: 'CONTAINER_NAME', value: "${container_name}"), ],quietPeriod: 0,wait: false
                        }
                    }
                }
            }
        }
    }
}
