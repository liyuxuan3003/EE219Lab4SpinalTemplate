package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I1StageMemory(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    val aluResult = in(Bits(cfg.dataWidth bits))
    val rs2Data = in(Bits(cfg.dataWidth bits))
    val memResult = out(Bits(cfg.dataWidth bits))
    val memWrite = in(Bool())
    val dmem = slave(DMemPort(cfg))
  }

  io.dmem.addr := io.aluResult
  io.dmem.dataWr := io.rs2Data
  io.dmem.enableWr := io.memWrite

  io.memResult := io.dmem.dataRd
}

object I1StageMemorySim extends App {
  Config.sim.compile(I1StageMemory()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object I1StageMemoryVerilog extends App {
  Config.spinal.generateVerilog(I1StageMemory())
}

object I1StageMemoryVhdl extends App {
  Config.spinal.generateVhdl(I1StageMemory())
}
