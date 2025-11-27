package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class ControlUnitInt(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    // opcode
    val opcode = in(Bits(cfg.opcodeWidth bits))
    // funct3
    val funct3 = in(Bits(cfg.funct3Width bits))
    // funct7
    val funct7 = in(Bits(cfg.funct7Width bits))
    // resultSrc (-> Stage W, ResultIntMux)
    val resultSrc = out(ResultSrc())
    // regWrite  (-> Stage D, RegfileInt)
    val regWrite = out(Bool())
    // memWrite  (-> Stage M, Dmem)
    val memWrite = out(Bool())
    // branchOp  (-> Stage E, BranchUnit)
    val branchOp = out(BranchOp())
    // aluOp     (-> Stage E, Alu)
    val aluOp = out(AluOp())
    // aluSrcB   (-> Stage E, AluSrcBMux)
    val aluSrcB = out(AluSrcB())
    // immSrc    (-> Stage E, ImmExtend)
    val immSrc = out(ImmSrc())
  }

  // TODO
}

object ControlUnitIntSim extends App {
  Config.sim.compile(ControlUnitInt()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object ControlUnitIntVerilog extends App {
  Config.spinal.generateVerilog(ControlUnitInt())
}

object ControlUnitIntVhdl extends App {
  Config.spinal.generateVhdl(ControlUnitInt())
}
