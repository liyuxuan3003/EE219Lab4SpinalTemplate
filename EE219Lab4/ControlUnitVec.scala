package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class ControlUnitVec(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    val opcode = in(Bits(cfg.opcodeWidth bits))
    val funct3 = in(Bits(cfg.funct3Width bits))
    val funct6 = in(Bits(cfg.funct6Width bits))
    val regWrite = out(Bool())
    val vluSrcA = out(VluSrcA())
    val vluOp = out(VluOp())
    val vregWrite = out(Bool())
    val vregFirst = out(Bool())
    val vmemWrite = out(Bool())
    val vresultSrc = out(VresultSrc())
    val vextSrc = out(VextSrc())
  }

  // TODO
}

object ControlUnitVecSim extends App {
  Config.sim.compile(ControlUnitVec()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object ControlUnitVecVerilog extends App {
  Config.spinal.generateVerilog(ControlUnitVec())
}

object ControlUnitVecVhdl extends App {
  Config.spinal.generateVhdl(ControlUnitVec())
}
