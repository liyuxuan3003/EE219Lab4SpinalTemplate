package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I2R219Top(cfg: R219Config = R219Config(isVec = true)) extends Component {
  val io = new Bundle {}

  val r219 = I2R219Core(cfg)
  val memory = Memory(cfg)

  r219.io.imem <> memory.io.imem
  r219.io.dmem <> memory.io.dmem
  r219.io.vmem <> memory.io.vmem
}

object I2R219TopSim extends App {
  val cfg = R219Config(isVec = true)
  val key = if (args.length > 0) args(0) else "InstTest2"
  Config.sim.compile(I2R219Top(cfg)).doSim { dut =>
    val memWr = (w, i) => dut.memory.mem.setBigInt(w, i)
    val memRd = (w) => dut.memory.mem.getBigInt(w)
    val test: Test = key match {
      case "InstTest2" => new InstTest2(cfg, memWr, memRd)
      case "InstTest3" => new InstTest3(cfg, memWr, memRd)
      case other       => new Test(other, cfg, memWr, memRd)
    }
    val length = test.prSim()
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()
    for (cycles <- 0 until 0x1000000) {
      if (dut.r219.stageF.ps.pcReg.toLong > cfg.baseInst + (length - 1) * 4) {
        val result = test.poSim()
        simSuccess()
      }
      dut.clockDomain.waitRisingEdge()
    }

  }
}

object I2R219TopVerilog extends App {
  Config.spinal.generateVerilog(I2R219Top())
}

object I2R219TopVhdl extends App {
  Config.spinal.generateVhdl(I2R219Top())
}
