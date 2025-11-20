package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class R219Config() {
  val dataWidth = 32
  val addrWidth = 32
  val vectWidth = 256

  val rfWidth = 5

  val opcodeWidth = 7
  val funct3Width = 3
  val funct7Width = 7

  val baseMem = 0x80000000L
  val sizeMem = 0x02000000L

  val baseMrA = 0x80100000L
  val baseMcA = 0x80200000L
  val baseMrB = 0x80300000L
  val baseMcB = 0x80400000L
  val baseMrC = 0x80500000L
  val baseMcC = 0x80600000L
  val baseMrD = 0x80700000L
  val baseMcD = 0x80800000L
}
