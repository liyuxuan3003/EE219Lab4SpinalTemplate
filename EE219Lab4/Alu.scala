package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class Alu(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    val srca = in(Bits(cfg.dataWidth bits))
    val srcb = in(Bits(cfg.dataWidth bits))
    val aluOp = in(AluOp())
    val result = out(Bits(cfg.dataWidth bits))
    val less = out(Bool())
  }

  // TODO
}

object AluSim extends App {
  Config.sim.compile(Alu()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object AluVerilog extends App {
  Config.spinal.generateVerilog(Alu())
}

object AluVhdl extends App {
  Config.spinal.generateVhdl(Alu())
}
