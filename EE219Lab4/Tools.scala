package EE219Lab4

import java.nio.file.Files
import java.nio.file.Paths
import java.io.PrintWriter
import java.io.File
import scala.util.Random

object Tools {
  def loadProgram(name: String): Array[BigInt] = {
    val bytes = Files.readAllBytes(Paths.get("build/sw/" + name + ".bin"))
    val words = bytes.grouped(4).map { g => BigInt(g.reverse.padTo(4, 0x00.toByte)) & 0xffffffffL }.toArray
    words
  }

  def loadMatrix(mat: Array[Array[Int]]): Array[BigInt] = {
    val words = mat.flatten.map { e => BigInt(e) & 0xffffffffL }.toArray
    words
  }

  def loadArray(arr: Array[Int]): Array[BigInt] = {
    val words = arr.map { e => BigInt(e) & 0xffffffffL }.toArray
    words
  }

  def dataWr(memWr: (Long, BigInt) => Unit, words: Array[BigInt], baseData: Long, baseMem: Long): Unit = {
    words.zipWithIndex.foreach { case (w, i) => memWr(i + (baseData - baseMem) / 4, w) }
  }

  def dataRd(memRd: (Long) => BigInt, size: Int, step: Int, baseData: Long, baseMem: Long): Array[Int] = {
    (0 until size).map { _ * step + (baseData - baseMem) / 4 }.map { addr => memRd(addr).toInt }.toArray
  }

  def createLog(name: String): PrintWriter = {
    new PrintWriter(new File("build/" + name + ".txt"))
  }

  def logTitle(file: PrintWriter, name: String, result: Boolean): Unit = {
    println("@@@@@@@@@@@@@@@@@@")
    println("# " + name + ": " + strCheck(result))
    println("@@@@@@@@@@@@@@@@@@")
    file.println("@@@@@@@@@@@@@@@@@@")
    file.println("# " + name + ": " + strCheck(result))
    file.println("@@@@@@@@@@@@@@@@@@")
    file.println("")
  }

  def logProgram(file: PrintWriter, program: Array[BigInt]): Unit = {
    file.println("PROGRAM LENGTH = " + program.length.toString + "\n" + strProgram(program))
    file.println("")
  }

  def logArray(file: PrintWriter, prefix: String, arr: Array[Int]): Unit = {
    file.println(prefix + ": " + strArray(arr))
    file.println("")
  }

  def logVector(file: PrintWriter, prefix: String, arr: Array[Int]): Unit = {
    file.println(prefix + ": " + strVector(arr))
    file.println("")
  }

  def logMatrix(file: PrintWriter, prefix: String, mat: Array[Array[Int]]): Unit = {
    file.println(prefix + ": " + "\n" + strMatrix(mat))
    file.println("")
  }

  val matSize = 8

  def matRandom(): Array[Array[Int]] = {
    Array.fill(matSize, matSize)(Random.nextInt(512 - 2) - 255)
  }

  def matTranspose(mat: Array[Array[Int]]): Array[Array[Int]] = {
    Array.tabulate(matSize, matSize) { case (i, j) => mat(j)(i) }
  }

  def matMul(mat1: Array[Array[Int]], mat2: Array[Array[Int]]): Array[Array[Int]] = {
    Array.tabulate(matSize, matSize) { case (i, j) => mat1(i).zip(matTranspose(mat2)(j)).map { case (e1, e2) => e1 * e2 }.reduce(_ + _) }
  }

  def matAdd(mat1: Array[Array[Int]], mat2: Array[Array[Int]]): Array[Array[Int]] = {
    Array.tabulate(matSize, matSize) { case (i, j) => mat1(i)(j) + mat2(i)(j) }
  }

  val vecSize = 8

  val vecLutSize = 64

  val lut = Array.tabulate(vecLutSize) { i => q2i(Math.exp(-8.0 + i.toDouble * (8.0 / 63.0))) }

  def vecRandom(): Array[Int] = {
    Array.fill(vecSize)(Random.nextInt((16 << 16) - 2) - ((8 << 16) - 1))
  }

  def vecMax(vec: Array[Int]): Array[Int] = {
    Array.fill(vecSize)(vec.reduce(Math.max(_, _)))
  }

  def vecSub(vec1: Array[Int], vec2: Array[Int]): Array[Int] = {
    Array.tabulate(vecSize) { i => vec1(i) - vec2(i) }
  }

  def vecDiv(vec1: Array[Int], vec2: Array[Int]): Array[Int] = {
    Array.tabulate(vecSize) { i => ((vec1(i) >> 2) << 16) / (vec2(i) >> 2) }
  }

  def vecRedsum(vec: Array[Int]): Array[Int] = {
    Array.fill(vecSize)(vec.reduce(_ + _))
  }

  def vecExp(vec: Array[Int]): Array[Int] = {
    Array.tabulate(vecSize) { i =>
      val delta = clip(vec(i), -(8 << 16), 0)
      lut((delta + (8 << 16)) * (vecLutSize - 1) / (8 << 16))
    }
  }

  def clip(value: Int, min: Int, max: Int) = {
    value match {
      case v if v < min => min
      case v if v > max => max
      case v            => v
    }
  }

  def i2q(i: Int): Double = {
    (i.toDouble / (1 << 16).toDouble)
  }

  def q2i(f: Double): Int = {
    (f * (1 << 16).toDouble).toInt
  }

  def strProgram(program: Array[BigInt]) = {
    program.map { w => f"${w}%08X" }.mkString("\n")
  }

  def strArray(arr: Array[Int]): String = {
    "[" + arr.mkString(", ") + "]"
  }

  def strMatrix(mat: Array[Array[Int]]): String = {
    "[\n" + mat.map { arr => "\t" + strArray(arr) }.mkString(",\n") + "\n]"
  }

  def strVector(vec: Array[Int]): String = {
    "[" + vec.map { e => f"${i2q(e)}%.6f" }.mkString(", ") + "]"
  }

  def strCheck(result: Boolean): String = {
    if (result) "PASS" else "FAIL"
  }

  def eqArray(arr1: Array[Int], arr2: Array[Int]): Boolean = {
    arr1.zip(arr2).map { case (e1, e2) => e1 == e2 }.reduce(_ && _)
  }

  def eqMatrix(mat1: Array[Array[Int]], mat2: Array[Array[Int]]): Boolean = {
    mat1.zip(mat2).map { case (a1, a2) => eqArray(a1, a2) }.reduce(_ && _)
  }

  def eqVector(arr1: Array[Int], arr2: Array[Int]): Boolean = {
    arr1.zip(arr2).map { case (e1, e2) => Math.abs(e1 - e2) < 50 }.reduce(_ && _)
  }
}
