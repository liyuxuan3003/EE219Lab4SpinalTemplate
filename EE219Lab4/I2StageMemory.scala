package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I2StageMemory(cfg: R219Config = R219Config(isVec = true)) extends Component {
  val io = new Bundle {
    val aluResult = in(Bits(cfg.dataWidth bits))
    val vluResult = in(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    val rs2Data = in(Bits(cfg.dataWidth bits))
    val vs2Data = in(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    val memWrite = in(Bool())
    val memResult = out(Bits(cfg.dataWidth bits))
    val vmemWrite = in(Bool())
    val vmemResult = out(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    val dmem = slave(DMemPort(cfg))
    val vmem = slave(VMemPort(cfg))
  }

  val x = new Area {
    io.dmem.addr := io.aluResult
    io.dmem.dataWr := io.rs2Data
    io.dmem.enableWr := io.memWrite

    io.memResult := io.dmem.dataRd
  }

  val v = new Area {
    io.vmem.addr := io.vluResult(0)
    io.vmem.dataWr := io.vs2Data.asBits
    io.vmem.enableWr := io.vmemWrite

    io.vmemResult := io.vmem.dataRd.subdivideIn(cfg.vectElements slices)
  }
}

object I2StageMemorySim extends App {
  Config.sim.compile(I2StageMemory()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object I2StageMemoryVerilog extends App {
  Config.spinal.generateVerilog(I2StageMemory())
}

object I2StageMemoryVhdl extends App {
  Config.spinal.generateVhdl(I2StageMemory())
}
