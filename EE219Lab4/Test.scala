package EE219Lab4

import EE219Lab4.Tools.loadProgram
import EE219Lab4.Tools.loadMatrix
import EE219Lab4.Tools.loadArray
import EE219Lab4.Tools.dataWr
import EE219Lab4.Tools.dataRd
import EE219Lab4.Tools.eqArray
import EE219Lab4.Tools.eqMatrix
import EE219Lab4.Tools.eqVector
import EE219Lab4.Tools.createLog
import EE219Lab4.Tools.strProgram
import EE219Lab4.Tools.logTitle
import EE219Lab4.Tools.logProgram
import EE219Lab4.Tools.logArray
import EE219Lab4.Tools.logMatrix
import EE219Lab4.Tools.logVector
import EE219Lab4.Tools.matSize
import EE219Lab4.Tools.matRandom
import EE219Lab4.Tools.matAdd
import EE219Lab4.Tools.matMul
import EE219Lab4.Tools.matTranspose
import EE219Lab4.Tools.vecSize
import EE219Lab4.Tools.vecRandom
import EE219Lab4.Tools.vecRedsum
import EE219Lab4.Tools.vecMax
import EE219Lab4.Tools.vecSub
import EE219Lab4.Tools.vecDiv
import EE219Lab4.Tools.vecExp
import EE219Lab4.Tools.lut

class Test(name: String, cfg: R219Config, memWr: (Long, BigInt) => Unit, memRd: (Long) => BigInt) {
  val file = createLog(name)
  val program = loadProgram(name)

  def prSim(): Int = {
    dataWr(memWr, words = program, baseData = cfg.baseInst, baseMem = cfg.baseMem)
    program.length
  }

  def poSim(): Unit = {
    file.close()
  }
}

class TestInst(name: String, cfg: R219Config, memWr: (Long, BigInt) => Unit, memRd: (Long) => BigInt, base: Long, step: Int, golden: Array[Int]) extends Test(name, cfg, memWr, memRd) {
  val size = golden.length

  override def prSim(): Int = {
    dataWr(memWr, words = program, baseData = cfg.baseInst, baseMem = cfg.baseMem)
    program.length
  }

  override def poSim(): Unit = {
    val data = dataRd(memRd, size = size, step = step, baseData = base, baseMem = cfg.baseMem)
    val result = eqArray(data, golden)
    logTitle(file, name, result)
    logProgram(file, program)
    logArray(file, "GT", golden)
    logArray(file, "HW", data)
    file.close()
  }
}

class TestMac(name: String, cfg: R219Config, memWr: (Long, BigInt) => Unit, memRd: (Long) => BigInt) extends Test(name, cfg, memWr, memRd) {
  val mrA = matRandom()
  val mrB = matRandom()
  val mrC = matRandom()
  val mrD = matAdd(matMul(mrA, mrB), mrC)

  val mcA = matTranspose(mrA)
  val mcB = matTranspose(mrB)
  val mcC = matTranspose(mrC)
  val mcD = matTranspose(mrD)

  val (wordsMrA, wordsMcA) = (loadMatrix(mrA), loadMatrix(mcA))
  val (wordsMrB, wordsMcB) = (loadMatrix(mrB), loadMatrix(mcB))
  val (wordsMrC, wordsMcC) = (loadMatrix(mrC), loadMatrix(mcC))

  override def prSim(): Int = {
    dataWr(memWr, words = program, baseData = cfg.baseInst, baseMem = cfg.baseMem)
    dataWr(memWr, words = wordsMrA, baseData = cfg.baseMrA, baseMem = cfg.baseMem)
    dataWr(memWr, words = wordsMcA, baseData = cfg.baseMcA, baseMem = cfg.baseMem)
    dataWr(memWr, words = wordsMrB, baseData = cfg.baseMrB, baseMem = cfg.baseMem)
    dataWr(memWr, words = wordsMcB, baseData = cfg.baseMcB, baseMem = cfg.baseMem)
    dataWr(memWr, words = wordsMrC, baseData = cfg.baseMrC, baseMem = cfg.baseMem)
    dataWr(memWr, words = wordsMcC, baseData = cfg.baseMcC, baseMem = cfg.baseMem)
    program.length
  }

  override def poSim(): Unit = {
    val hwmrD = dataRd(memRd, size = matSize * matSize, step = 1, baseData = cfg.baseMrD, baseMem = cfg.baseMem).grouped(matSize).toArray
    val hwmcD = dataRd(memRd, size = matSize * matSize, step = 1, baseData = cfg.baseMcD, baseMem = cfg.baseMem).grouped(matSize).toArray
    val result = eqMatrix(mrD, hwmrD) && eqMatrix(mcD, hwmcD)
    logTitle(file, name, result)
    logProgram(file, program)
    logMatrix(file, "GT (Matrix A ROW)", mrA)
    logMatrix(file, "GT (Matrix B ROW)", mrB)
    logMatrix(file, "GT (Matrix C ROW)", mrC)
    logMatrix(file, "GT (Matrix D ROW)", mrD)
    logMatrix(file, "HW (Matrix D ROW)", hwmrD)
    logMatrix(file, "GT (Matrix D COL)", mcD)
    logMatrix(file, "HW (Matrix D COL)", hwmcD)
    file.close()
  }
}

class TestSoftmax(name: String, cfg: R219Config, memWr: (Long, BigInt) => Unit, memRd: (Long) => BigInt) extends Test(name, cfg, memWr, memRd) {
  val vqInp = vecRandom()
  val vqExp = vecExp(vecSub(vqInp, vecMax(vqInp)))
  val vqOut = vecDiv(vqExp, vecRedsum(vqExp))

  val wordsInp = loadArray(vqInp)
  val wordsLut = loadArray(lut)

  override def prSim(): Int = {
    dataWr(memWr, words = program, baseData = cfg.baseInst, baseMem = cfg.baseMem)
    dataWr(memWr, words = wordsInp, baseData = cfg.baseSoftmaxInp, baseMem = cfg.baseMem)
    dataWr(memWr, words = wordsLut, baseData = cfg.baseSoftmaxExp, baseMem = cfg.baseMem)
    program.length
  }

  override def poSim(): Unit = {
    val hwvqOut = dataRd(memRd, size = vecSize, step = 1, baseData = cfg.baseSoftmaxOut, baseMem = cfg.baseMem)
    val result = eqVector(vqOut, hwvqOut)
    logTitle(file, name, result)
    logProgram(file, program)
    logVector(file, "GT (Q16, Exp LUT)", lut)
    logVector(file, "GT (Q16, Softmax Input)", vqInp)
    logVector(file, "GT (Q16, Softmax Output)", vqOut)
    logVector(file, "HW (Q16, Softmax Output)", hwvqOut)
    logArray(file, "GT (Int, Exp LUT)", lut)
    logArray(file, "GT (Int, Softmax Input)", vqInp)
    logArray(file, "GT (Int, Softmax Output)", vqOut)
    logArray(file, "HW (Int, Softmax Output)", hwvqOut)
    file.close()
  }
}

class InstTest1(cfg: R219Config, memWr: (Long, BigInt) => Unit, memRd: (Long) => BigInt) extends TestInst("InstTest1", cfg, memWr, memRd, base = cfg.baseDataI1, step = 1, golden = Array(100, -100, 115, -85, -115, 100, 3, 4, 1, 0, 64, 60))

class InstTest2(cfg: R219Config, memWr: (Long, BigInt) => Unit, memRd: (Long) => BigInt) extends TestInst("InstTest2", cfg, memWr, memRd, base = cfg.baseDataI2, step = 8, golden = Array(8, 9, 17, 26, -6, -5, 7, 100, 1, 2, 13))

class InstTest3(cfg: R219Config, memWr: (Long, BigInt) => Unit, memRd: (Long) => BigInt) extends TestInst("InstTest3", cfg, memWr, memRd, base = cfg.baseDataI2, step = 8, golden = Array(8, 9, 17, 26, 1, 2, 10, 7, 1, 3, 3, 120, 120))

class MacScalar(cfg: R219Config, memWr: (Long, BigInt) => Unit, memRd: (Long) => BigInt) extends TestMac("MacScalar", cfg, memWr, memRd)

class MacVector(cfg: R219Config, memWr: (Long, BigInt) => Unit, memRd: (Long) => BigInt) extends TestMac("MacVector", cfg, memWr, memRd)

class Softmax(cfg: R219Config, memWr: (Long, BigInt) => Unit, memRd: (Long) => BigInt) extends TestSoftmax("Softmax", cfg, memWr, memRd)
