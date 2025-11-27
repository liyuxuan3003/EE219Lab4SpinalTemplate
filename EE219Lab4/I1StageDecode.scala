package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I1StageDecode(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    // Instruction
    val instr = in(Bits(cfg.dataWidth bits))
    // Data of rs1 (output)
    val rs1Data = out(Bits(cfg.dataWidth bits))
    // Data of rs2 (output)
    val rs2Data = out(Bits(cfg.dataWidth bits))
    // Data of rd (input)
    val rdData = in(Bits(cfg.dataWidth bits))
    // Extended imm (output)
    val immExt = out(Bits(cfg.dataWidth bits))
    // Writeback select
    val resultSrc = out(ResultSrc())
    // Memory write enable
    val memWrite = out(Bool())
    // BranchUnit operation
    val branchOp = out(BranchOp())
    // Alu operation
    val aluOp = out(AluOp())
    // Alu srcb source select
    val aluSrcB = out(AluSrcB())
  }

  // ControlUnit
  val cu = ControlUnitInt(cfg)
  // RegisterFile
  val rf = RegisterFileInt(cfg)
  // ImmExtend
  val ext = ImmExtend(cfg)

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
