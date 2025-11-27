package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class R219Config(isVec: Boolean = false) {
  val dataWidth = 32
  val addrWidth = 32
  val vectWidth = 256
  val vectElements = vectWidth / dataWidth

  val rfWidth = 5

  val opcodeWidth = 7
  val funct3Width = 3
  val funct6Width = 6
  val funct7Width = 7

  val issues = 2
  val issueScalar = 0
  val issueVector = 1

  val baseMem = 0x80000000L
  val sizeMem = 0x02000000L

  val baseInst = 0x80000000L
  val baseMrA = 0x80100000L
  val baseMcA = 0x80200000L
  val baseMrB = 0x80300000L
  val baseMcB = 0x80400000L
  val baseMrC = 0x80500000L
  val baseMcC = 0x80600000L
  val baseMrD = 0x80700000L
  val baseMcD = 0x80800000L
  val baseDataI1 = 0x80900000L
  val baseDataI2 = 0x80a00000L
  val baseSoftmaxInp = 0x81000000L
  val baseSoftmaxExp = 0x81000020L
  val baseSoftmaxOut = 0x81100000L
}
