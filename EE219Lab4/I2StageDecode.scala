package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I2StageDecode(cfg: R219Config = R219Config(isVec = true)) extends Component {
  val io = new Bundle {
    val instr = in(Vec(Bits(cfg.dataWidth bits), cfg.issues))
    val rs1Data = out(Bits(cfg.dataWidth bits))
    val rs2Data = out(Bits(cfg.dataWidth bits))
    val rdData = in(Bits(cfg.dataWidth bits))
    val vs1Data = out(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    val vs2Data = out(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    val vdData = in(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    val immExt = out(Bits(cfg.dataWidth bits))
    val vecExt = out(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    val resultSrc = out(ResultSrc())
    val vresultSrc = out(VresultSrc())
    val memWrite = out(Bool())
    val vmemWrite = out(Bool())
    val branchOp = out(BranchOp())
    val aluOp = out(AluOp())
    val aluSrcB = out(AluSrcB())
    val vluOp = out(VluOp())
    val vluSrcA = out(VluSrcA())
  }

  val x = new Area {
    val cu = ControlUnitInt(cfg)
    val rf = RegisterFileInt(cfg)
    val ext = ImmExtend(cfg)

    // TODO
  }

  val v = new Area {
    val cu = ControlUnitVec(cfg)
    val rf = RegisterFileVec(cfg)
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
