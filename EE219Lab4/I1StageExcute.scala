package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I1StageExcute(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    // Data of rs1
    val rs1Data = in(Bits(cfg.dataWidth bits))
    // Data of rs2
    val rs2Data = in(Bits(cfg.dataWidth bits))
    // Extended imm
    val immExt = in(Bits(cfg.dataWidth bits))
    // Alu operation
    val aluOp = in(AluOp())
    // Alu srcb source select
    val aluSrcB = in(AluSrcB())
    // Alu calculation result
    val aluResult = out(Bits(cfg.dataWidth bits))
    // BranchUnit operation
    val branchOp = in(BranchOp())
    // Pc (to calculate pctarget = pc + imm)
    val pc = in(Bits(cfg.addrWidth bits))
    // Pc target
    val pcTarget = out(Bits(cfg.addrWidth bits))
    // Pc next select
    val pcSrc = out(PcSrc())
  }

  // Alu
  val alu = Alu(cfg)
  // BranchUnit
  val bu = BranchUnit(cfg)

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
