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

package kantan.codecs.scalaz.laws.discipline

import _root_.scalaz.Equal
import kantan.codecs._
import kantan.codecs.scalaz._
import org.scalacheck.Arbitrary

object equality extends EqualInstances

trait EqualInstances {
  implicit def decoderEqual[E: Arbitrary, D: Equal, F: Equal, T]: Equal[Decoder[E, D, F, T]] =
    new Equal[Decoder[E, D, F, T]] {
      override def equal(a1: Decoder[E, D, F, T], a2: Decoder[E, D, F, T]) =
        kantan.codecs.laws.discipline.equality.eq(a1.decode, a2.decode) { (d1, d2) ⇒
          implicitly[Equal[Result[F, D]]].equal(d1, d2)
        }
    }

  implicit def encoderEqual[E: Equal, D: Arbitrary, T]: Equal[Encoder[E, D, T]] = new Equal[Encoder[E, D, T]] {
    override def equal(a1: Encoder[E, D, T], a2: Encoder[E, D, T]) =
      kantan.codecs.laws.discipline.equality.eq(a1.encode, a2.encode) { (e1, e2) ⇒
        implicitly[Equal[E]].equal(e1, e2)
      }
  }
}
