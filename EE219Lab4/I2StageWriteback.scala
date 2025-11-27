package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I2StageWriteback(cfg: R219Config = R219Config(isVec = true)) extends Component {
  val io = new Bundle {
    // Alu result
    val aluResult = in(Bits(cfg.dataWidth bits))
    // Dmem result
    val memResult = in(Bits(cfg.dataWidth bits))
    // Pc + 4
    val pcPlus4 = in(Bits(cfg.addrWidth bits))
    // Write back select of scalar
    val resultSrc = in(ResultSrc())
    // Data of rd (writeback data of scalar)
    val rdData = out(Bits(cfg.dataWidth bits))
    // Vlu result
    val vluResult = in(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    // Vmem result
    val vmemResult = in(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    // Write back select of vector
    val vresultSrc = in(VresultSrc())
    // Data of vd (writeback data of vector)
    val vdData = out(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
  }

  // Area of scalar
  val x = new Area {
    // Writeback select switch of scalar
    switch(io.resultSrc) {
      // If writeback alu result
      is(ResultSrc.alu) { io.rdData := io.aluResult }
      // If writeback dmem result
      is(ResultSrc.mem) { io.rdData := io.memResult }
      // If writeback pc + 4
      is(ResultSrc.pc4) { io.rdData := io.pcPlus4 }
    }
  }

  // Area of vector
  val v = new Area {
    // Writeback select switch of vector
    switch(io.vresultSrc) {
      // If writeback vlu result
      is(VresultSrc.vlu) { io.vdData := io.vluResult }
      // If writeback vmem result
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
