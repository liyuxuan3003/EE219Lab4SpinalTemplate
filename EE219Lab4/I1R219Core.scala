package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I1R219Core(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    val imem = slave(IMemPort(cfg))
    val dmem = slave(DMemPort(cfg))
  }

  val stageF = I1StageFetch(cfg)
  val stageD = I1StageDecode(cfg)
  val stageE = I1StageExcute(cfg)
  val stageM = I1StageMemory(cfg)
  val stageW = I1StageWriteback(cfg)

  io.imem <> stageF.io.imem
  io.dmem <> stageM.io.dmem

  stageF.io.pcSrc := stageE.io.pcSrc
  stageF.io.pcTarget := stageE.io.pcTarget

  stageD.io.instr := stageF.io.instr
  stageD.io.rdData := stageW.io.rdData

  stageE.io.rs1Data := stageD.io.rs1Data
  stageE.io.rs2Data := stageD.io.rs2Data
  stageE.io.immExt := stageD.io.immExt
  stageE.io.aluOp := stageD.io.aluOp
  stageE.io.aluSrcB := stageD.io.aluSrcB
  stageE.io.branchOp := stageD.io.branchOp
  stageE.io.pc := stageF.io.pc

  stageM.io.aluResult := stageE.io.aluResult
  stageM.io.rs2Data := stageE.io.rs2Data
  stageM.io.memWrite := stageD.io.memWrite

  stageW.io.aluResult := stageE.io.aluResult
  stageW.io.memResult := stageM.io.memResult
  stageW.io.pcPlus4 := stageF.io.pcPlus4
  stageW.io.resultSrc := stageD.io.resultSrc
}

object I1R219CoreSim extends App {
  Config.sim.compile(I1R219Core()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object I1R219CoreVerilog extends App {
  Config.spinal.generateVerilog(I1R219Core())
}

object I1R219CoreVhdl extends App {
  Config.spinal.generateVhdl(I1R219Core())
}
