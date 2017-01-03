/*
 * Copyright 2017 Nicolas Rinaudo
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

package kantan.codecs.strings

import kantan.codecs.laws.discipline.arbitrary._
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class ErrorsTests extends FunSuite with GeneratorDrivenPropertyChecks {
  test("DecodeErrors should be equal if the underlying exceptions are the same") {
    forAll { (e1: DecodeError, e2: Exception) ⇒
      assert((e1 == e2) == ((e1, e2) match {
        case (DecodeError(t1), DecodeError(t2)) ⇒ t1 == t2
        case _                                  ⇒ false
      }))
    }
  }

  test("DecodeErrors should have identical hashCodes if the underlying exceptions are the same") {
    forAll { (e1: DecodeError, e2: Exception) ⇒
      assert((e1.hashCode() == e2.hashCode()) == ((e1, e2) match {
        case (DecodeError(t1), DecodeError(t2)) ⇒ t1 == t2
        case _                                  ⇒ false
      }))
    }
  }

}
