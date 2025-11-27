package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I2StageFetch(cfg: R219Config = R219Config(isVec = true)) extends Component {
  val io = new Bundle {
    // Pc next select
    val pcSrc = in(PcSrc())
    // Pc target (if jump)
    val pcTarget = in(Bits(cfg.addrWidth bits))
    // Pc + 4 (ouput for Mux in Stage W, lui required rd = pc + 4)
    val pcPlus4 = out(Bits(cfg.addrWidth bits))
    // Pc
    val pc = out(Bits(cfg.addrWidth bits))
    // Instruction (fetch 2 instruction in one cycle)
    val instr = out(Vec(Bits(cfg.dataWidth bits), cfg.issues))
    // Imem interface
    val imem = slave(IMemPort(cfg))
  }

  // PcSelect
  val ps = PcSelect(cfg)

  // Input of PcSelect
  ps.io.pcSrc := io.pcSrc
  ps.io.pcTarget := io.pcTarget

  // Input of Imem
  io.imem.addr := ps.io.pc

  // Output
  io.pcPlus4 := ps.io.pcPlus4
  io.pc := ps.io.pc
  // Split into 2 slices
  io.instr := io.imem.dataRd.subdivideIn(cfg.issues slices)
}

object I2StageFetchSim extends App {
  Config.sim.compile(I2StageFetch()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object I2StageFetchVerilog extends App {
  Config.spinal.generateVerilog(I2StageFetch())
}

object I2StageFetchVhdl extends App {
  Config.spinal.generateVhdl(I2StageFetch())
}
