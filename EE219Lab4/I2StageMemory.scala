package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I2StageMemory(cfg: R219Config = R219Config(isVec = true)) extends Component {
  val io = new Bundle {
    // Alu result (input as addr of Dmem)
    val aluResult = in(Bits(cfg.dataWidth bits))
    // Vlu result (input as addr of Vmem)
    val vluResult = in(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    // Data of rs2 (input as write data of Dmem)
    val rs2Data = in(Bits(cfg.dataWidth bits))
    // Data of vs2 (input as write data of Vmem)
    val vs2Data = in(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    // Dmem write enable
    val memWrite = in(Bool())
    // Dmem result (output as read data of Dmem)
    val memResult = out(Bits(cfg.dataWidth bits))
    // Vmem write enable
    val vmemWrite = in(Bool())
    // Vmem result (output as read data of Vmem)
    val vmemResult = out(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    // Dmem interface
    val dmem = slave(DMemPort(cfg))
    // Vmem interface
    val vmem = slave(VMemPort(cfg))
  }

  // Area of scalar
  val x = new Area {
    // Input of Dmem
    io.dmem.addr := io.aluResult
    io.dmem.dataWr := io.rs2Data
    io.dmem.enableWr := io.memWrite

    // Output
    io.memResult := io.dmem.dataRd
  }

  // Area of vector
  val v = new Area {
    // Input of Vmem
    io.vmem.addr := io.vluResult(0)
    io.vmem.dataWr := io.vs2Data.asBits
    io.vmem.enableWr := io.vmemWrite

    // Output
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
