package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I1StageDecode(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    val instr = in(Bits(cfg.dataWidth bits))
    val rs1Data = out(Bits(cfg.dataWidth bits))
    val rs2Data = out(Bits(cfg.dataWidth bits))
    val rdData = in(Bits(cfg.dataWidth bits))
    val immExt = out(Bits(cfg.dataWidth bits))
    val resultSrc = out(ResultSrc())
    val memWrite = out(Bool())
    val branchOp = out(BranchOp())
    val aluOp = out(AluOp())
    val aluSrcB = out(AluSrcB())
  }

  val cu = I1ControlUnit(cfg)
  val rf = I1RegisterFile(cfg)
  val ext = I1ImmExtend(cfg)

  // TODO
}

object I1StageDecodeSim extends App {
  Config.sim.compile(I1StageDecode()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object I1StageDecodeVerilog extends App {
  Config.spinal.generateVerilog(I1StageDecode())
}

object I1StageDecodeVhdl extends App {
  Config.spinal.generateVhdl(I1StageDecode())
}
