package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I1StageExcute(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    val rs1Data = in(Bits(cfg.dataWidth bits))
    val rs2Data = in(Bits(cfg.dataWidth bits))
    val immExt = in(Bits(cfg.dataWidth bits))
    val aluOp = in(AluOp())
    val aluSrcB = in(AluSrcB())
    val aluResult = out(Bits(cfg.dataWidth bits))
    val branchOp = in(BranchOp())
    val pc = in(Bits(cfg.addrWidth bits))
    val pcTarget = out(Bits(cfg.addrWidth bits))
    val pcSrc = out(PcSrc())
  }

  val alu = I1Alu(cfg)
  val bu = I1BranchUnit(cfg)

  // TODO
}

object I1StageExcuteSim extends App {
  Config.sim.compile(I1StageExcute()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object I1StageExcuteVerilog extends App {
  Config.spinal.generateVerilog(I1StageExcute())
}

object I1StageExcuteVhdl extends App {
  Config.spinal.generateVhdl(I1StageExcute())
}
