package EE219Lab4

import java.nio.file.Files
import java.nio.file.Paths
import scala.util.Random

case class Test(key: String, cfg: R219Config = R219Config(), memWr: (Long, BigInt) => Unit, memRd: (Long) => BigInt) {
  val build = "build" + "/" + "sw"

  val keys = Array("InstTest1", "InstTest2", "InstTest3", "MacScalar", "MacVector", "Softmax")

  val binary = keys.map { k => (k, build + "/" + k + ".bin") }.toMap

  val dataBase = Map(
    "InstTest1" -> 0x80900000L
  )

  val dataGolden = Map(
    "InstTest1" -> Array(100, -100, 115, -85, -115, 100, 3, 4, 1, 0, 64, 60)
  )

  val dataSize = dataGolden.map { case (k, v) => (k, v.length) }

  val matSize = 8

  val mrA = matRandom()
  val mrB = matRandom()
  val mrC = matRandom()
  val mrD = matSum(matDot(mrA, mrB), mrC)

  val mcA = matTranspose(mrA)
  val mcB = matTranspose(mrB)
  val mcC = matTranspose(mrC)
  val mcD = matTranspose(mrD)

  def prSim(): Int = {
    key match {
      case "InstTest1" => prSimInst()
      case "MacScalar" => prSimMac()
    }
  }

  def poSim(): Boolean = {
    key match {
      case "InstTest1" => poSimInst()
      case "MacScalar" => poSimMac()
    }
  }

  def prSimInst(): Int = {
    val length = loadProgram()
    length
  }

  def poSimInst(): Boolean = {
    val data = dumpData(base = dataBase(key), size = dataSize(key), 1)
    val result = eqArray(data, dataGolden(key))
    println("GT: " + strArray(dataGolden(key)))
    println("HW: " + strArray(data))
    println(strCheck(result))
    result
  }

  def prSimMac(): Int = {
    val length = loadProgram()
    loadMatrix(mat = mcA, base = cfg.baseMcA)
    loadMatrix(mat = mrA, base = cfg.baseMrA)
    loadMatrix(mat = mcB, base = cfg.baseMcB)
    loadMatrix(mat = mrB, base = cfg.baseMrB)
    loadMatrix(mat = mcC, base = cfg.baseMcC)
    loadMatrix(mat = mrC, base = cfg.baseMrC)
    println("Matrix A (row) GT: \n" + strMatrix(mrA))
    println("Matrix B (row) GT: \n" + strMatrix(mrB))
    println("Matrix C (row) GT: \n" + strMatrix(mrC))
    length
  }

  def poSimMac(): Boolean = {
    val hwmrD = dumpData(base = cfg.baseMrD, size = matSize * matSize, 1).grouped(matSize).toArray
    val hwmcD = dumpData(base = cfg.baseMcD, size = matSize * matSize, 1).grouped(matSize).toArray
    val result = eqMatrix(mrD, hwmrD) && eqMatrix(mcD, hwmcD)
    println("Matrix D (row) GT: \n" + strMatrix(mrD))
    println("Matrix D (row) HW: \n" + strMatrix(hwmrD))
    println("Matrix D (col) GT: \n" + strMatrix(mcD))
    println("Matrix D (col) HW: \n" + strMatrix(hwmcD))
    println(strCheck(result))
    result
  }

  def loadProgram(): Int = {
    val bytes = Files.readAllBytes(Paths.get(binary(key)))
    val words = bytes.grouped(4).map { g => BigInt(g.reverse.padTo(4, 0x00.toByte)) & 0xffffffffL }.toArray
    words.zipWithIndex.foreach { case (w, i) => memWr(i, w) }
    println("PROGRAM LENGTH = " + words.length.toString + "\n" + strProgram(words))
    words.length
  }

  def loadMatrix(mat: Array[Array[Int]], base: Long): Unit = {
    val words = mat.flatten.map { e => BigInt(e) & 0xffffffffL }.toArray
    words.zipWithIndex.foreach { case (w, i) => memWr(i + (base - cfg.baseMem) / 4, w) }
  }

  def matRandom(): Array[Array[Int]] = {
    Array.fill(matSize, matSize)(Random.nextInt(512 - 1) - 255)
  }

  def matTranspose(mat: Array[Array[Int]]): Array[Array[Int]] = {
    Array.tabulate(matSize, matSize) { case (i, j) => mat(j)(i) }
  }

  def matDot(mat1: Array[Array[Int]], mat2: Array[Array[Int]]): Array[Array[Int]] = {
    Array.tabulate(matSize, matSize) { case (i, j) => mat1(i).zip(matTranspose(mat2)(j)).map { case (e1, e2) => e1 * e2 }.reduce(_ + _) }
  }

  def matSum(mat1: Array[Array[Int]], mat2: Array[Array[Int]]): Array[Array[Int]] = {
    Array.tabulate(matSize, matSize) { case (i, j) => mat1(i)(j) + mat2(i)(j) }
  }

  def dumpData(base: Long, size: Int, step: Int): Array[Int] = {
    (0 until size).map { _ * step + (base - cfg.baseMem) / 4 }.map { addr => memRd(addr).toInt }.toArray
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

  def strCheck(result: Boolean): String = {
    if (result) "PASS" else "FAIL"
  }

  def eqArray(arr1: Array[Int], arr2: Array[Int]): Boolean = {
    arr1.zip(arr2).map { case (e1, e2) => e1 == e2 }.reduce(_ && _)
  }

  def eqMatrix(mat1: Array[Array[Int]], mat2: Array[Array[Int]]): Boolean = {
    mat1.zip(mat2).map { case (a1, a2) => eqArray(a1, a2) }.reduce(_ && _)
  }

}
