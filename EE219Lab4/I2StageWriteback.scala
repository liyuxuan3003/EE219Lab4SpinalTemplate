package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I2StageWriteback(cfg: R219Config = R219Config(isVec = true)) extends Component {
  val io = new Bundle {
    val aluResult = in(Bits(cfg.dataWidth bits))
    val memResult = in(Bits(cfg.dataWidth bits))
    val pcPlus4 = in(Bits(cfg.addrWidth bits))
    val resultSrc = in(ResultSrc())
    val rdData = out(Bits(cfg.dataWidth bits))
    val vluResult = in(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    val vmemResult = in(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    val vresultSrc = in(VresultSrc())
    val vdData = out(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
  }

  val x = new Area {
    switch(io.resultSrc) {
      is(ResultSrc.alu) { io.rdData := io.aluResult }
      is(ResultSrc.mem) { io.rdData := io.memResult }
      is(ResultSrc.pc4) { io.rdData := io.pcPlus4 }
    }
  }

  val v = new Area {
    switch(io.vresultSrc) {
      is(VresultSrc.vlu) { io.vdData := io.vluResult }
      is(VresultSrc.mem) { io.vdData := io.vmemResult }
    }
  }
}

object I2StageWritebackSim extends App {
  Config.sim.compile(I2StageWriteback()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object I2StageWritebackVerilog extends App {
  Config.spinal.generateVerilog(I2StageWriteback())
}

object I2StageWritebackVhdl extends App {
  Config.spinal.generateVhdl(I2StageWriteback())
}
