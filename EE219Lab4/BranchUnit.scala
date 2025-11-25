package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class BranchUnit(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    val less = in(Bool())
    val branchOp = in(BranchOp())
    val pcSrc = out(PcSrc())
  }

  // TODO
}

object BranchUnitSim extends App {
  Config.sim.compile(BranchUnit()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object BranchUnitVerilog extends App {
  Config.spinal.generateVerilog(BranchUnit())
}

object BranchUnitVhdl extends App {
  Config.spinal.generateVhdl(BranchUnit())
}
