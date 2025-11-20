package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I1StageWriteback(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    val aluResult = in(Bits(cfg.dataWidth bits))
    val memResult = in(Bits(cfg.dataWidth bits))
    val pcPlus4 = in(Bits(cfg.addrWidth bits))
    val resultSrc = in(ResultSrc())
    val rdData = out(Bits(cfg.dataWidth bits))
  }

  switch(io.resultSrc) {
    is(ResultSrc.alu) { io.rdData := io.aluResult }
    is(ResultSrc.mem) { io.rdData := io.memResult }
    is(ResultSrc.pc4) { io.rdData := io.pcPlus4 }
  }
}

object I1StageWritebackSim extends App {
  Config.sim.compile(I1StageWriteback()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object I1StageWritebackVerilog extends App {
  Config.spinal.generateVerilog(I1StageWriteback())
}

object I1StageWritebackVhdl extends App {
  Config.spinal.generateVhdl(I1StageWriteback())
}
