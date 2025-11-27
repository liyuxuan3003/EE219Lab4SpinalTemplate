package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class PcSelect(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    // Pc next select
    val pcSrc = in(PcSrc())
    // Pc target (if jump)
    val pcTarget = in(Bits(cfg.addrWidth bits))
    // Pc + 4 (ouput for Mux in Stage W, lui required rd = pc + 4)
    val pcPlus4 = out(Bits(cfg.addrWidth bits))
    // Pc
    val pc = out(Bits(cfg.addrWidth bits))
  }

  // If issue = 2, read two instruction per time
  val shift = if (cfg.isVec) 8 else 4

  // Pc register
  val pcReg = Reg(Bits(cfg.addrWidth bits)).simPublic() init (cfg.baseInst)

  // Pc target
  val pcTarget = io.pcTarget
  // Pc + 4
  val pcPlus4 = (pcReg.asUInt + shift).asBits

  // Update register
  switch(io.pcSrc) {
    is(PcSrc.pc4) { pcReg := pcPlus4 }
    is(PcSrc.pctarget) { pcReg := pcTarget }
  }

  // Output pc
  io.pc := pcReg
  // Output pc + 4
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
