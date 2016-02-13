package kantan.codecs.laws

import kantan.codecs.laws.discipline.{EncoderTests, DecoderTests}
import kantan.codecs.{Encoder, DecodeResult, Decoder}
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class BooleanCodecTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  implicit val BooleanDecoder = Decoder[String, Boolean, Boolean](s ⇒ DecodeResult(s.toBoolean).leftMap(_ ⇒ true))
  implicit val BooleanEncoder = Encoder[String, Boolean](_.toString)

  checkAll("Decoder[String, Boolean, Boolean]", DecoderTests[String, Boolean, Boolean].decoder[Int, Int])
  checkAll("Encoder[String, Boolean]", EncoderTests[String, Boolean].encoder[Int, Int])
}
