/**
 * Copyright 2012 SnowPlow Analytics Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.snowplowanalytics.refererparser.scala

// Java
import java.net.URI

// RefererParser Java impl
import com.snowplowanalytics.refererparser.{Parser => JParser}
import com.snowplowanalytics.refererparser.{Medium => JMedium}

/**
 * Enumeration for supported mediums.
 *
 * Replacement for Java version's Enum.
 */
object Medium extends Enumeration {
  type Medium = Value

  val Unknown  = Value("unknown")
  val Search   = Value("search")
  val Internal = Value("internal")
  val Social   = Value("social")
  val Email    = Value("email")

  /**
   * Converts from our Java Medium Enum
   * to our Scala Enumeration values above.
   */
  def fromJava(medium: JMedium) = medium match {
    case JMedium.UNKNOWN  => Unknown
    case JMedium.INTERNAL => Internal
    case JMedium.SEARCH   => Search
    case JMedium.SOCIAL   => Social
    case JMedium.EMAIL    => Email
  }
}

/**
 * Immutable case class to hold a referer.
 *
 * Replacement for Java version's POJO.
 */
case class Referer(medium: Medium.Medium, source: String, term: Option[String])

/**
 * Parser object - contains one-time initialization
 * of the YAML database of referers, and parse()
 * methods to generate a Referer object from a
 * referer URL.
 *
 * In Java this had to be instantiated as a class.
 */
object Parser {

  private type MaybeReferer = Option[Referer]

  /**
   * Parses a `refererUri` String to return
   * either a Referal, or None.
   */
  def parse(refererUri: String): MaybeReferer =
    if (refererUri == null || refererUri == "")
      None
    else
      parse(new URI(refererUri))

  /**
   * Parses a `refererUri` URI to return
   * either a Referer, or None.
   */
  def parse(refererUri: URI): MaybeReferer = {
    
    val jp = new JParser()
    val jrefr = Option(jp.parse(refererUri))

    jrefr.map(jr =>
      Referer(Medium.fromJava(jr.medium), jr.source, Option(jr.term))
      )
  }
}