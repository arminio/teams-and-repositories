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

package uk.gov.hmrc.teamsandrepositories

import java.time.LocalDateTime
import java.util.Date

import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mock.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.{ExecutionContext, Future}

class CompositeRepositoryDataSourceSpec extends WordSpec with MockitoSugar with ScalaFutures with Matchers with DefaultPatienceConfig {

  val now = new Date().getTime

  "Retrieving team repo mappings" should {

    "return the combination of all input sources"  in {

      val teamsList1 = List(
        TeamRepositories("A", List(Repository("A_r", "Some Description", "url_A", now, now))),
        TeamRepositories("B", List(Repository("B_r", "Some Description", "url_B", now, now))),
        TeamRepositories("C", List(Repository("C_r", "Some Description", "url_C", now, now))))

      val teamsList2 = List(
        TeamRepositories("D", List(Repository("D_r", "Some Description", "url_D", now, now))),
        TeamRepositories("E", List(Repository("E_r", "Some Description", "url_E", now, now))),
        TeamRepositories("F", List(Repository("F_r", "Some Description", "url_F", now, now))))

      val dataSource1 = mock[RepositoryDataSource]
      when(dataSource1.getTeamRepoMapping).thenReturn(Future.successful(teamsList1))

      val dataSource2 = mock[RepositoryDataSource]
      when(dataSource2.getTeamRepoMapping).thenReturn(Future.successful(teamsList2))

      val compositeDataSource = new CompositeRepositoryDataSource(List(dataSource1, dataSource2))
      val result = compositeDataSource.getTeamRepoMapping.futureValue

      result.length shouldBe 6
      result should contain (teamsList1.head)
      result should contain (teamsList1(1))
      result should contain (teamsList1(2))
      result should contain (teamsList2.head)
      result should contain (teamsList2(1))
      result should contain (teamsList2(2))
    }

    "combine teams that have the same names in both sources and sort repositories alphabetically"  in {

      val repoAA = Repository("A_A", "Some Description", "url_A_A", now, now)
      val repoAB = Repository("A_B", "Some Description", "url_A_B", now, now)
      val repoAC = Repository("A_C", "Some Description", "url_A_C", now, now)

      val teamsList1 = List(
        TeamRepositories("A", List(repoAC, repoAB)),
        TeamRepositories("B", List(Repository("B_r", "Some Description", "url_B", now, now))),
        TeamRepositories("C", List(Repository("C_r", "Some Description", "url_C", now, now))))

      val teamsList2 = List(
        TeamRepositories("A", List(repoAA)),
        TeamRepositories("D", List(Repository("D_r", "Some Description", "url_D", now, now))))

      val dataSource1 = mock[RepositoryDataSource]
      when(dataSource1.getTeamRepoMapping).thenReturn(Future.successful(teamsList1))

      val dataSource2 = mock[RepositoryDataSource]
      when(dataSource2.getTeamRepoMapping).thenReturn(Future.successful(teamsList2))

      val compositeDataSource = new CompositeRepositoryDataSource(List(dataSource1, dataSource2))
      val result = compositeDataSource.getTeamRepoMapping.futureValue

      result.length shouldBe 4
      result.find(_.teamName == "A").get.repositories should contain inOrderOnly (
        repoAA, repoAB, repoAC)

      result should contain (teamsList1(1))
      result should contain (teamsList1(2))
      result should contain (teamsList2(1))

    }
  }
}
