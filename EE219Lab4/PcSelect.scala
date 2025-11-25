package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class PcSelect(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    val pcSrc = in(PcSrc())
    val pcTarget = in(Bits(cfg.addrWidth bits))
    val pcPlus4 = out(Bits(cfg.addrWidth bits))
    val pc = out(Bits(cfg.addrWidth bits))
  }

  val shift = if (cfg.isVec) 8 else 4

  val pcReg = Reg(Bits(cfg.addrWidth bits)).simPublic() init (cfg.baseInst)

  val pcTarget = io.pcTarget
  val pcPlus4 = (pcReg.asUInt + shift).asBits

  switch(io.pcSrc) {
    is(PcSrc.pc4) { pcReg := pcPlus4 }
    is(PcSrc.pctarget) { pcReg := pcTarget }
  }

  io.pc := pcReg
  io.pcPlus4 := pcPlus4
}

object PcSelectSim extends App {
  Config.sim.compile(PcSelect()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object PcSelectVerilog extends App {
  Config.spinal.generateVerilog(PcSelect())
}

object PcSelectVhdl extends App {
  Config.spinal.generateVhdl(PcSelect())
}
