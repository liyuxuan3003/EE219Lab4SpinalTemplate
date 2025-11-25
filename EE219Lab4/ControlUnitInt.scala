package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class ControlUnitInt(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    val opcode = in(Bits(cfg.opcodeWidth bits))
    val funct3 = in(Bits(cfg.funct3Width bits))
    val funct7 = in(Bits(cfg.funct7Width bits))
    val resultSrc = out(ResultSrc())
    val regWrite = out(Bool())
    val memWrite = out(Bool())
    val branchOp = out(BranchOp())
    val aluOp = out(AluOp())
    val aluSrcB = out(AluSrcB())
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
