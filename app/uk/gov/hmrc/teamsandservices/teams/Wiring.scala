/*
 * Copyright 2016 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.teamsandservices.teams

import uk.gov.hmrc.teamsandservices.github._
import uk.gov.hmrc.githubclient.GithubApiClient

trait GithubEnterpriseTeamsRepositoryDataSourceProvider {
  private val gitApiEnterpriseClient = new GithubApiClient(GithubConfig.githubApiEnterpriseConfig)

  val enterpriseTeamsRepositoryDataSource: TeamsRepositoryDataSource = new GithubV3TeamsRepositoryDataSource(gitApiEnterpriseClient, isInternal = true)  with GithubConfigProvider
}

trait GithubOpenTeamsRepositoryDataSourceProvider {
  private val gitOpenClient = new GithubApiClient(GithubConfig.githubApiOpenConfig)

  val openTeamsRepositoryDataSource: TeamsRepositoryDataSource = new GithubV3TeamsRepositoryDataSource(gitOpenClient, isInternal = false) with GithubConfigProvider
}