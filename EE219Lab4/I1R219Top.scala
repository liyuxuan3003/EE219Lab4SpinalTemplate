package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I1R219Top(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {}

  val r219 = I1R219Core(cfg)
  val memory = Memory(cfg)

  r219.io.imem <> memory.io.imem
  r219.io.dmem <> memory.io.dmem
}

object I1R219TopSim extends App {
  val cfg = R219Config()
  val key = if (args.length > 0) args(0) else "MacScalar"
  Config.sim.compile(I1R219Top(cfg)).doSim { dut =>
    val test = Test(key, cfg, (w, i) => dut.memory.mem.setBigInt(w, i), (w) => dut.memory.mem.getBigInt(w))
    val length = test.prSim()

    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()
    for (cycles <- 0 until 0x1000000) {
      if (dut.r219.stageF.pcReg.toLong > cfg.baseMem + (length - 1) * 4) {
        val result = test.poSim()
        simSuccess()
      }
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
