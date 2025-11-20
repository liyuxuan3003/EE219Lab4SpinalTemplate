package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I1BranchUnit(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    val less = in(Bool())
    val branchOp = in(BranchOp())
    val pcSrc = out(PcSrc())
  }

  // TODO
}

object I1BranchUnitSim extends App {
  Config.sim.compile(I1BranchUnit()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object I1BranchUnitVerilog extends App {
  Config.spinal.generateVerilog(I1BranchUnit())
}

object I1BranchUnitVhdl extends App {
  Config.spinal.generateVhdl(I1BranchUnit())
}
