package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I1StageFetch(cfg: R219Config = R219Config(), issue: Int = 1) extends Component {
  val io = new Bundle {
    val pcSrc = in(PcSrc())
    val pcTarget = in(Bits(cfg.addrWidth bits))
    val pcPlus4 = out(Bits(cfg.addrWidth bits))
    val pc = out(Bits(cfg.addrWidth bits))
    val instr = out(Bits(cfg.dataWidth bits))
    val imem = slave(IMemPort(cfg))
  }

  val pcReg = Reg(Bits(cfg.addrWidth bits)).simPublic() init (cfg.baseMem)

  val pcTarget = io.pcTarget
  val pcPlus4 = (pcReg.asUInt + 4).asBits

  switch(io.pcSrc) {
    is(PcSrc.pc4) { pcReg := pcPlus4 }
    is(PcSrc.pctarget) { pcReg := pcTarget }
  }

  io.imem.addr := pcReg

  io.pc := pcReg
  io.pcPlus4 := pcPlus4
  io.instr := io.imem.dataRd
}

object I1StageFetchSim extends App {
  Config.sim.compile(I1StageFetch()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object I1StageFetchVerilog extends App {
  Config.spinal.generateVerilog(I1StageFetch())
}

object I1StageFetchVhdl extends App {
  Config.spinal.generateVhdl(I1StageFetch())
}
