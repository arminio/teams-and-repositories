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

package uk.gov.hmrc.catalogue.teams

import java.net.URLDecoder

import org.joda.time.DateTime
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{JsPath, Json, Writes}
import play.api.mvc._
import uk.gov.hmrc.catalogue.CachedList
import uk.gov.hmrc.catalogue.config.{CacheConfigProvider, CatalogueConfig, UrlTemplatesProvider}
import uk.gov.hmrc.catalogue.teams.ViewModels.Service
import uk.gov.hmrc.play.microservice.controller.BaseController

object TeamsRepositoryController extends TeamsRepositoryController
  with CatalogueConfig with GithubEnterpriseTeamsRepositoryDataSourceProvider
  with GithubOpenTeamsRepositoryDataSourceProvider
{
  val dataSource: CachingTeamsRepositoryDataSource = new CachingTeamsRepositoryDataSource(
    new CompositeTeamsRepositoryDataSource(List(enterpriseTeamsRepositoryDataSource, openTeamsRepositoryDataSource)),
    DateTime.now
  ) with CacheConfigProvider
}

trait TeamsRepositoryController extends BaseController {
  this: UrlTemplatesProvider =>

  protected def dataSource: CachingTeamsRepositoryDataSource

  def teamRepository() = Action.async { implicit request =>
    dataSource.getTeamRepoMapping.map {
      teams => Ok(Json.toJson(teams)(CachedList.cachedListFormats))
    }
  }

  def services(teamName:String) = Action.async { implicit request =>
    dataSource.getTeamRepoMapping.map { teams =>
      teams.find(_.teamName == URLDecoder.decode(teamName, "UTF-8")).map { team =>
        Ok(Json.toJson(team.repositories.flatMap(Service.fromRepository(_, ciUrlTemplates))))
      }.getOrElse(NotFound)
    }
  }

  def reloadCache() = Action { implicit request =>
    dataSource.reload()
    Ok("Cache reload triggered successfully")
  }
}
