package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I1ImmExtend(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    val instr = in(Bits(cfg.dataWidth bits))
    val immSrc = in(ImmSrc())
    val immExt = out(Bits(cfg.dataWidth bits))
  }

  // TODO
}

object I1ImmExtendSim extends App {
  Config.sim.compile(I1ImmExtend()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object I1ImmExtendVerilog extends App {
  Config.spinal.generateVerilog(I1ImmExtend())
}

object I1ImmExtendVhdl extends App {
  Config.spinal.generateVhdl(I1ImmExtend())
}
