package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class VecExtend(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    // instruction (including immediate)
    val instr = in(Bits(cfg.dataWidth bits))
    // vector extend from what (imm / scalar)
    val vextSrc = in(VextSrc())
    // scalar input
    val scalar = in(Bits(cfg.dataWidth bits))
    // vector output
    val vector = out(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
  }

  // TODO
}

object VecExtendSim extends App {
  Config.sim.compile(VecExtend()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object VecExtendVerilog extends App {
  Config.spinal.generateVerilog(VecExtend())
}

object VecExtendVhdl extends App {
  Config.spinal.generateVhdl(VecExtend())
}
