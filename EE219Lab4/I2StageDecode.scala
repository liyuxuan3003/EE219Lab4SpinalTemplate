package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I2StageDecode(cfg: R219Config = R219Config(isVec = true)) extends Component {
  val io = new Bundle {
    // Instruction
    val instr = in(Vec(Bits(cfg.dataWidth bits), cfg.issues))
    // Data of rs1 (output)
    val rs1Data = out(Bits(cfg.dataWidth bits))
    // Data of rs2 (output)
    val rs2Data = out(Bits(cfg.dataWidth bits))
    // Data of rd (input)
    val rdData = in(Bits(cfg.dataWidth bits))
    // Data of vs1 (output)
    val vs1Data = out(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    // Data of vs2 (output)
    val vs2Data = out(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    // Data of rd (input)
    val vdData = in(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    // Extended imm (output)
    val immExt = out(Bits(cfg.dataWidth bits))
    // Extended vector (output, from scalar or imm)
    val vecExt = out(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    // Writeback select for scalar
    val resultSrc = out(ResultSrc())
    // Writeback select for vector
    val vresultSrc = out(VresultSrc())
    // Memory write enable for scalar
    val memWrite = out(Bool())
    // Memory write enable for vector
    val vmemWrite = out(Bool())
    // BranchUnit operation
    val branchOp = out(BranchOp())
    // Alu operation
    val aluOp = out(AluOp())
    // Alu srcb source select
    val aluSrcB = out(AluSrcB())
    // Vlu operation
    val vluOp = out(VluOp())
    // Vlu srca source select
    val vluSrcA = out(VluSrcA())
  }

  // Area of scalar
  val x = new Area {
    // ControlUnit for scalar
    val cu = ControlUnitInt(cfg)
    // RegisterFile for scalar
    val rf = RegisterFileInt(cfg)
    // ImmExtend
    val ext = ImmExtend(cfg)

    // TODO
  }

  // Area for vector
  val v = new Area {
    // ControlUnit for vector
    val cu = ControlUnitVec(cfg)
    // RegisterFile for vector
    val rf = RegisterFileVec(cfg)
    // VecExtend
    val ext = VecExtend(cfg)

    // TODO
  }
}

object I2StageDecodeSim extends App {
  Config.sim.compile(I2StageDecode()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object I2StageDecodeVerilog extends App {
  Config.spinal.generateVerilog(I2StageDecode())
}

object I2StageDecodeVhdl extends App {
  Config.spinal.generateVhdl(I2StageDecode())
}
