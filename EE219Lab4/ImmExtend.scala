package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class ImmExtend(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    // instruction (including immedaite)
    val instr = in(Bits(cfg.dataWidth bits))
    // immediate type
    val immSrc = in(ImmSrc())
    // immediate extended
    val immExt = out(Bits(cfg.dataWidth bits))
  }

  // TODO
}

object ImmExtendSim extends App {
  Config.sim.compile(ImmExtend()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object ImmExtendVerilog extends App {
  Config.spinal.generateVerilog(ImmExtend())
}

object ImmExtendVhdl extends App {
  Config.spinal.generateVhdl(ImmExtend())
}
