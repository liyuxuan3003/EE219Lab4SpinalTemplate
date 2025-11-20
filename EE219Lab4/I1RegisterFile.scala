package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I1RegisterFile(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    val rs1 = in(Bits(cfg.rfWidth bits))
    val rs2 = in(Bits(cfg.rfWidth bits))
    val rd = in(Bits(cfg.rfWidth bits))
    val rs1Data = out(Bits(cfg.dataWidth bits))
    val rs2Data = out(Bits(cfg.dataWidth bits))
    val rdData = in(Bits(cfg.dataWidth bits))
    val regWrite = in(Bool())
  }

  val regs = Mem(Bits(cfg.dataWidth bits), 1 << cfg.rfWidth) init (Seq.fill(1 << cfg.rfWidth)(B(0x00000000, cfg.dataWidth bits)))

  when(io.regWrite) {
    regs(io.rd.asUInt) := io.rdData
  }

  when(io.rs1 === 0) {
    io.rs1Data := False.asBits(cfg.dataWidth bits)
  } otherwise {
    io.rs1Data := regs(io.rs1.asUInt)
  }

  when(io.rs2 === 0) {
    io.rs2Data := False.asBits(cfg.dataWidth bits)
  } otherwise {
    io.rs2Data := regs(io.rs2.asUInt)
  }
}

object I1RegisterFileSim extends App {
  Config.sim.compile(I1RegisterFile()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object I1RegisterFileVerilog extends App {
  Config.spinal.generateVerilog(I1RegisterFile())
}

object I1RegisterFileVhdl extends App {
  Config.spinal.generateVhdl(I1RegisterFile())
}
