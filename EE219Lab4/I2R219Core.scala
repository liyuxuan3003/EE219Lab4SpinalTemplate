package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I2R219Core(cfg: R219Config = R219Config(isVec = true)) extends Component {
  val io = new Bundle {
    val imem = slave(IMemPort(cfg))
    val dmem = slave(DMemPort(cfg))
    val vmem = slave(VMemPort(cfg))
  }

  val stageF = I2StageFetch(cfg)
  val stageD = I2StageDecode(cfg)
  val stageE = I2StageExcute(cfg)
  val stageM = I2StageMemory(cfg)
  val stageW = I2StageWriteback(cfg)

  // TODO
}

object I2R219CoreSim extends App {
  Config.sim.compile(I2R219Core()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object I2R219CoreVerilog extends App {
  Config.spinal.generateVerilog(I2R219Core())
}

object I2R219CoreVhdl extends App {
  Config.spinal.generateVhdl(I2R219Core())
}
