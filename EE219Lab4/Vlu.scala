package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class Vlu(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    // Vlu input 1
    val srca = in(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    // Vlu input 2
    val srcb = in(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    // Vlu operation
    val vluOp = in(VluOp())
    // Vlu output
    val result = out(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
  }

  // TODO
}

object VluSim extends App {
  Config.sim.compile(Vlu()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object VluVerilog extends App {
  Config.spinal.generateVerilog(Vlu())
}

object VluVhdl extends App {
  Config.spinal.generateVhdl(Vlu())
}
