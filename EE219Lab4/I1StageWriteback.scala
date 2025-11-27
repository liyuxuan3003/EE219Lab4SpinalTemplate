package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I1StageWriteback(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    // Alu result
    val aluResult = in(Bits(cfg.dataWidth bits))
    // Mem result
    val memResult = in(Bits(cfg.dataWidth bits))
    // Pc + 4
    val pcPlus4 = in(Bits(cfg.addrWidth bits))
    // Writeback select
    val resultSrc = in(ResultSrc())
    // Data of rd (writeback data)
    val rdData = out(Bits(cfg.dataWidth bits))
  }

  // Writeback select switch
  switch(io.resultSrc) {
    // If writeback alu result
    is(ResultSrc.alu) { io.rdData := io.aluResult }
    // If writeback mem result
    is(ResultSrc.mem) { io.rdData := io.memResult }
    // If writeback pc + 4
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
