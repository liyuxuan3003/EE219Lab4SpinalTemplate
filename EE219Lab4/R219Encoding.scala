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
  def vload  = B("0000111")
  def vstore = B("0100111")
  def vop    = B("1010111")
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

object F3Vec {
  def opivv = B("000")
  def opivi = B("011")
  def opivx = B("100")
  def opmvv = B("010")
  def opmvx = B("110")
}

object F7Alu {
  def i = B("0000000")
  def m = B("0000001")
}

object F6VecMem {
  def ve32 = B("000000")
}

object F6VecOp {
  def vadd  = B("000000")
  def vsub  = B("000010")
  def vmul  = B("100101")
  def vdiv  = B("100001")
  def vmvxs = B("010000")
  def vmvvx = B("010111")
  def vmin  = B("000101")
  def vmax  = B("000111")
  def vsra  = B("101001")
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

object VluOp extends SpinalEnum {
  val vadd, vsub, vmul, vdiv, vmin, vmax, vsra, vredsum, vredmax, vsrca, vsrcb = newElement()
}

object VluSrcA extends SpinalEnum {
  val vs1, vext = newElement()
}

object ImmSrc extends SpinalEnum {
  val i, s, b, j, u, r = newElement()
}

object VextSrc extends SpinalEnum {
  val x, i1, i2, v = newElement()
}

object ResultSrc extends SpinalEnum {
  val alu, mem, pc4 = newElement()
}

object VresultSrc extends SpinalEnum {
  val vlu, mem = newElement()
}

object PcSrc extends SpinalEnum {
  val pc4, pctarget = newElement()
}
