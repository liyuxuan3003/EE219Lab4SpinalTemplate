package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I1R219Top(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {}

  // R219 core for single issue
  val r219 = I1R219Core(cfg)
  val memory = Memory(cfg)

  r219.io.imem <> memory.io.imem
  r219.io.dmem <> memory.io.dmem
}

object I1R219TopSim extends App {
  // Create config with isVec disable
  val cfg = R219Config(isVec = false)
  // Default key = InstTest1
  val key = if (args.length > 0) args(0) else "InstTest1"
  // Simulation
  Config.sim.compile(I1R219Top(cfg)).doSim { dut =>
    // Get mem write / read function
    val memWr = (w, i) => dut.memory.mem.setBigInt(w, i)
    val memRd = (w) => dut.memory.mem.getBigInt(w)
    // Get test object by key match
    val test: Test = key match {
      case "InstTest1" => new InstTest1(cfg, memWr, memRd)
      case "MacScalar" => new MacScalar(cfg, memWr, memRd)
      case other       => new Test(other, cfg, memWr, memRd)
    }
    // Get program length
    val length = test.prSim()
    // Set clock
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()
    // Start running
    for (cycles <- 0 until 0x1000000) {
      // Detect if pc > program length
      if (dut.r219.stageF.ps.pcReg.toLong > cfg.baseInst + (length - 1) * 4) {
        // Show result
        test.poSim()
        // Stop running
        simSuccess()
      }
      // Next cycle
      dut.clockDomain.waitRisingEdge()
    }

  }
}

object I1R219TopVerilog extends App {
  Config.spinal.generateVerilog(I1R219Top())
}

object I1R219TopVhdl extends App {
  Config.spinal.generateVhdl(I1R219Top())
}
