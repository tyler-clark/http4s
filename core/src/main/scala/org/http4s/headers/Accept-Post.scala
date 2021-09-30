/*
 * Copyright 2013 http4s.org
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

package org.http4s
package headers

import cats.data.NonEmptyList
import cats.parse.Parser
import org.http4s.internal.parsing.Rfc7230.headerRep1
import org.typelevel.ci._
//Accept-Post response header.
//See https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept-Post
object `Accept-Post` {
  def apply(head: MediaType, tail: MediaType*): `Accept-Post` = apply(
    NonEmptyList(head, tail.toList))

  def parse(s: String): ParseResult[`Accept-Post`] =
    ParseResult.fromParser(parser, "Invalid Accept-Post header")(s)

  private[http4s] val parser: Parser[`Accept-Post`] =
    headerRep1(MediaType.parser).map(xs => `Accept-Post`(xs.head, xs.tail: _*))

  implicit val headerInstance: Header[`Accept-Post`, Header.Recurring] =
    Header.createRendered(
      ci"Accept-Post",
      _.values,
      parse
    )

  implicit val headerSemigroupInstance: cats.Semigroup[`Accept-Post`] =
    (a, b) => `Accept-Post`(a.values.concatNel(b.values))
}

final case class `Accept-Post`(values: NonEmptyList[MediaType])
