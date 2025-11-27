package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I2R219Core(cfg: R219Config = R219Config(isVec = true)) extends Component {
  val io = new Bundle {
    // Imem interface
    val imem = slave(IMemPort(cfg))
    // Dmem interface
    val dmem = slave(DMemPort(cfg))
    // Vmem interface
    val vmem = slave(VMemPort(cfg))
  }

  // Fetch
  val stageF = I2StageFetch(cfg)
  // Decode
  val stageD = I2StageDecode(cfg)
  // Excute
  val stageE = I2StageExcute(cfg)
  // Excute
  val stageM = I2StageMemory(cfg)
  // Writeback
  val stageW = I2StageWriteback(cfg)

  // Connect memory interface
  io.imem <> stageF.io.imem
  io.dmem <> stageM.io.dmem
  io.vmem <> stageM.io.vmem

  // Input of Stage F
  stageF.io.pcSrc := stageE.io.pcSrc
  stageF.io.pcTarget := stageE.io.pcTarget

  // Input of Stage D
  stageD.io.instr := stageF.io.instr
  stageD.io.rdData := stageW.io.rdData
  stageD.io.vdData := stageW.io.vdData

  // Input of Stage E
  stageE.io.rs1Data := stageD.io.rs1Data
  stageE.io.rs2Data := stageD.io.rs2Data
  stageE.io.immExt := stageD.io.immExt
  stageE.io.vs1Data := stageD.io.vs1Data
  stageE.io.vs2Data := stageD.io.vs2Data
  stageE.io.vecExt := stageD.io.vecExt
  stageE.io.aluOp := stageD.io.aluOp
  stageE.io.aluSrcB := stageD.io.aluSrcB
  stageE.io.vluOp := stageD.io.vluOp
  stageE.io.vluSrcA := stageD.io.vluSrcA
  stageE.io.branchOp := stageD.io.branchOp
  stageE.io.pc := stageF.io.pc

  // Input of Stage M
  stageM.io.aluResult := stageE.io.aluResult
  stageM.io.vluResult := stageE.io.vluResult
  stageM.io.rs2Data := stageE.io.rs2Data
  stageM.io.vs2Data := stageE.io.vs2Data
  stageM.io.memWrite := stageD.io.memWrite
  stageM.io.vmemWrite := stageD.io.vmemWrite

  // Input of Stage W
  stageW.io.aluResult := stageE.io.aluResult
  stageW.io.memResult := stageM.io.memResult
  stageW.io.pcPlus4 := stageF.io.pcPlus4
  stageW.io.resultSrc := stageD.io.resultSrc
  stageW.io.vluResult := stageE.io.vluResult
  stageW.io.vmemResult := stageM.io.vmemResult
  stageW.io.vresultSrc := stageD.io.vresultSrc
}

object I2R219CoreSim extends App {
  Config.sim.compile(I2R219Core()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object I2R219CoreVerilog extends App {
  Config.spinal.generateVerilog(I2R219Core())
}

object I2R219CoreVhdl extends App {
  Config.spinal.generateVhdl(I2R219Core())
}
