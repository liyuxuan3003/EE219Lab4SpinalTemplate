package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I2StageFetch(cfg: R219Config = R219Config(isVec = true)) extends Component {
  val io = new Bundle {
    val pcSrc = in(PcSrc())
    val pcTarget = in(Bits(cfg.addrWidth bits))
    val pcPlus4 = out(Bits(cfg.addrWidth bits))
    val pc = out(Bits(cfg.addrWidth bits))
    val instr = out(Vec(Bits(cfg.dataWidth bits), cfg.issues))
    val imem = slave(IMemPort(cfg))
  }

  val ps = PcSelect(cfg)

  ps.io.pcSrc := io.pcSrc
  ps.io.pcTarget := io.pcTarget
  io.pcPlus4 := ps.io.pcPlus4
  io.pc := ps.io.pc

  io.imem.addr := ps.io.pc
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
