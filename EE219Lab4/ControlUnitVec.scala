package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class ControlUnitVec(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    // opcode
    val opcode = in(Bits(cfg.opcodeWidth bits))
    // funct3
    val funct3 = in(Bits(cfg.funct3Width bits))
    // funct6
    val funct6 = in(Bits(cfg.funct6Width bits))
    // regWrite   (-> Stage D, RegfileInt)
    val regWrite = out(Bool())
    // vregWrite  (-> Stage D, RegfileVec)
    val vregWrite = out(Bool())
    // vregFirst  (-> Stage D, RegfileVec, only write first element if given)
    val vregFirst = out(Bool())
    // vmemWrite  (-> Stage M, Vmem)
    val vmemWrite = out(Bool())
    // vresultSrc (-> Stage W, ResultVecMux)
    val vresultSrc = out(VresultSrc())
    // vluOp      (-> Stage E, Vlu)
    val vluOp = out(VluOp())
    // vluSrcA    (-> Stage E, VluSrcA)
    val vluSrcA = out(VluSrcA())
    // vextSrc    (-> Stage E, VecExtend)
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
