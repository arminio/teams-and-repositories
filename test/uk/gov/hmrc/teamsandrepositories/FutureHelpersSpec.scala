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

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class FutureHelpersSpec extends WordSpec with Matchers with ScalaFutures with DefaultPatienceConfig {

  "FutureOfBoolean ||" should {
    "short circuit if needed" in {
      import uk.gov.hmrc.teamsandrepositories.FutureHelpers.FutureOfBoolean

      var counter = 0

      def delayedF1 = Future {
        Thread.sleep(50); counter += 1; false
      }

      def delayedF2 = Future {
        Thread.sleep(50); counter += 1; true
      }

      def delayedF3 = Future {
        Thread.sleep(50); counter += 1; false
      }

      (delayedF1 || delayedF2 || delayedF3 ).futureValue shouldBe true

      counter shouldBe 2
    }

    "execute all if needed" in {
      import uk.gov.hmrc.teamsandrepositories.FutureHelpers.FutureOfBoolean

      var counter = 0

      def delayedF1 = Future {
        Thread.sleep(50); counter += 1; false
      }

      def delayedF2 = Future {
        Thread.sleep(50); counter += 1; false
      }

      def delayedF3 = Future {
        Thread.sleep(50); counter += 1; false
      }

      (delayedF1 || delayedF2 || delayedF3 ).futureValue shouldBe false

      counter shouldBe 3
    }
  }


  "FutureOfBoolean &&" should {
    "short circuit if needed" in {
      import uk.gov.hmrc.teamsandrepositories.FutureHelpers.FutureOfBoolean

      var counter = 0

      def delayedF1 = Future {
        Thread.sleep(50); counter += 1; true
      }

      def delayedF2 = Future {
        Thread.sleep(50); counter += 1; false
      }

      def delayedF3 = Future {
        Thread.sleep(50); counter += 1; true
      }

      (delayedF1 && delayedF2 && delayedF3 ).futureValue shouldBe false

      counter shouldBe 2
    }

    "execute all if needed" in {
      import uk.gov.hmrc.teamsandrepositories.FutureHelpers.FutureOfBoolean

      var counter = 0

      def delayedF1 = Future {
        Thread.sleep(50); counter += 1; false
      }

      def delayedF2 = Future {
        Thread.sleep(50); counter += 1; false
      }

      def delayedF3 = Future {
        Thread.sleep(50); counter += 1; false
      }

      (delayedF1 || delayedF2 || delayedF3 ).futureValue shouldBe false

      counter shouldBe 3
    }
  }

}
