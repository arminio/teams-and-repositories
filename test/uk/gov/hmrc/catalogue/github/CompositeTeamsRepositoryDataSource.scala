package uk.gov.hmrc.catalogue.github

import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{WordSpec, Matchers}
import uk.gov.hmrc.catalogue.teams.TeamsRepositoryDataSource
import uk.gov.hmrc.catalogue.teams.ViewModels.{Team, Repository}

import scala.concurrent.Future

class CompositeTeamsRepositoryDataSource extends WordSpec with MockitoSugar  with Matchers {

  "Retrieving team repo mappings" should {

    "return the combination of all input sources"  in {

      val teamsList1 = List(
        Team("A", List(Repository("A_r", "url_A"))),
        Team("B", List(Repository("B_r", "url_B"))),
        Team("C", List(Repository("C_r", "url_C"))))

      val teamsList2 = List(
        Team("D", List(Repository("D_r", "url_D"))),
        Team("E", List(Repository("E_r", "url_E"))),
        Team("F", List(Repository("F_r", "url_F"))))

      val dataSource1 = mock[TeamsRepositoryDataSource]
      when(dataSource1.getTeamRepoMapping).thenReturn(Future.successful(teamsList1))



      val compositeDataSource = new CompositeTeamsRepositoryDataSource()

    }

  }

}
