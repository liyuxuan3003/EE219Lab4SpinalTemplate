package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I1Alu(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    val srca = in(Bits(cfg.dataWidth bits))
    val srcb = in(Bits(cfg.dataWidth bits))
    val aluOp = in(AluOp())
    val result = out(Bits(cfg.dataWidth bits))
    val less = out(Bool())
  }

  // TODO
}

object I1AluSim extends App {
  Config.sim.compile(I1Alu()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object I1AluVerilog extends App {
  Config.spinal.generateVerilog(I1Alu())
}

object I1AluVhdl extends App {
  Config.spinal.generateVhdl(I1Alu())
}
