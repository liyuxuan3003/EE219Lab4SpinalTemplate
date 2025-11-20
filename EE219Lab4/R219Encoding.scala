package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

// scalafmt: { align.preset = more }

object OpCode {
  def op     = B("0110011")
  def opimm  = B("0010011")
  def load   = B("0000011")
  def store  = B("0100011")
  def branch = B("1100011")
  def jal    = B("1101111")
  def lui    = B("0110111")
}

object F3AluI {
  def add = B("000")
  def sll = B("001")
  def slt = B("010")
  def and = B("111")
}

object F3AluM {
  def mul = B("000")
}

object F3Mem {
  def w = B("010")
}

object F3Branch {
  def blt = B("100")
}

object F7Alu {
  def i = B("0000000")
  def m = B("0000001")
}

object BranchOp extends SpinalEnum {
  val pc4, jump, blt = newElement()
}

object AluOp extends SpinalEnum {
  val add, sll, slt, and, mul, srcb = newElement()
}

object AluSrcB extends SpinalEnum {
  val rs2, imm = newElement()
}

object ImmSrc extends SpinalEnum {
  val i1, s, b, j, u, r = newElement()
}

object ResultSrc extends SpinalEnum {
  val alu, mem, pc4 = newElement()
}

object PcSrc extends SpinalEnum {
  val pc4, pctarget = newElement()
}
