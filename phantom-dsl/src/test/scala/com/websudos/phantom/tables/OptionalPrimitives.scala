/*
 * Copyright 2013 websudos ltd.
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
package com.websudos.phantom.tables

import java.net.InetAddress
import java.util.{Date, UUID}

import com.datastax.driver.core.Row
import com.newzly.util.testing.Sampler
import com.websudos.phantom.Implicits._
import com.websudos.phantom.helper.{ModelSampler, TestSampler}
import com.websudos.phantom.{CassandraTable, PhantomCassandraConnector}

case class OptionalPrimitive(
  pkey: String,
  string: Option[String],
  long: Option[Long],
  boolean: Option[Boolean],
  bDecimal: Option[BigDecimal],
  double: Option[Double],
  float: Option[Float],
  inet: Option[java.net.InetAddress],
  int: Option[Int],
  date: Option[java.util.Date],
  uuid: Option[java.util.UUID],
  bi: Option[BigInt]
)

object OptionalPrimitive extends ModelSampler[OptionalPrimitive] {
  def sample: OptionalPrimitive = {
    OptionalPrimitive(
      Sampler.getARandomString,
      Some(Sampler.getARandomString),
      Some(Sampler.getARandomInteger().toLong),
      Some(false),
      Some(BigDecimal(Sampler.getARandomInteger())),
      Some(Sampler.getARandomInteger().toDouble),
      Some(Sampler.getARandomInteger().toFloat),
      Some(InetAddress.getByName("127.0.0.1")),
      Some(Sampler.getARandomInteger()),
      Some(new Date()),
      Some(UUID.randomUUID()),
      Some(BigInt(Sampler.getARandomInteger()))
    )
  }

  def none: OptionalPrimitive = {
    OptionalPrimitive(
      Sampler.getARandomString,
      None, None, None, None, None, None, None, None, None, None, None
    )
  }
}

sealed class OptionalPrimitives extends CassandraTable[OptionalPrimitives, OptionalPrimitive] {
  override def fromRow(r: Row): OptionalPrimitive = {
    OptionalPrimitive(pkey(r), string(r), long(r), boolean(r), bDecimal(r), double(r), float(r), inet(r),
      int(r), date(r), uuid(r), bi(r))
  }

  object pkey extends StringColumn(this) with PartitionKey[String]

  object string extends OptionalStringColumn(this)

  object long extends OptionalLongColumn(this)

  object boolean extends OptionalBooleanColumn(this)

  object bDecimal extends OptionalBigDecimalColumn(this)

  object double extends OptionalDoubleColumn(this)

  object float extends OptionalFloatColumn(this)

  object inet extends OptionalInetAddressColumn(this)

  object int extends OptionalIntColumn(this)

  object date extends OptionalDateColumn(this)

  object uuid extends OptionalUUIDColumn(this)

  object bi extends OptionalBigIntColumn(this)
}

object OptionalPrimitives extends OptionalPrimitives with TestSampler[OptionalPrimitives, OptionalPrimitive] with PhantomCassandraConnector {

  override val tableName = "OptionalPrimitives"
}
