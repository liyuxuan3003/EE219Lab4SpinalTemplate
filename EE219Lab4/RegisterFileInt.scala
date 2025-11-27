package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class RegisterFileInt(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    // Addr of rs1
    val rs1 = in(Bits(cfg.rfWidth bits))
    // Addr of rs2
    val rs2 = in(Bits(cfg.rfWidth bits))
    // Addr of rsv (access for rv32v)
    val rsv = if (cfg.isVec) in(Bits(cfg.rfWidth bits)) else null
    // Addr of rd
    val rd = in(Bits(cfg.rfWidth bits))
    // Data of rs1
    val rs1Data = out(Bits(cfg.dataWidth bits))
    // Data of rs2
    val rs2Data = out(Bits(cfg.dataWidth bits))
    // Data of rsv (access for rv32v)
    val rsvData = if (cfg.isVec) out(Bits(cfg.dataWidth bits)) else null
    // Data of rd
    val rdData = in(Bits(cfg.dataWidth bits))
    // Write register file enable
    val regWrite = in(Bool())
  }

  // Register file
  val regs = Mem(Bits(cfg.dataWidth bits), 1 << cfg.rfWidth) init (Seq.fill(1 << cfg.rfWidth)(B(0, cfg.dataWidth bits)))

  // Write rd
  when(io.regWrite && io.rd =/= 0) {
    regs(io.rd.asUInt) := io.rdData
  }

  // Read rs1
  when(io.rs1 === 0) {
    io.rs1Data := False.asBits(cfg.dataWidth bits)
  } otherwise {
    io.rs1Data := regs(io.rs1.asUInt)
  }

  // Read rs2
  when(io.rs2 === 0) {
    io.rs2Data := False.asBits(cfg.dataWidth bits)
  } otherwise {
    io.rs2Data := regs(io.rs2.asUInt)
  }

  // Read rsv
  if (cfg.isVec) {
    when(io.rsv === 0) {
      io.rsvData := False.asBits(cfg.dataWidth bits)
    } otherwise {
      io.rsvData := regs(io.rsv.asUInt)
    }
  }
}

object RegisterFileIntSim extends App {
  Config.sim.compile(RegisterFileInt()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object RegisterFileIntVerilog extends App {
  Config.spinal.generateVerilog(RegisterFileInt())
}

object RegisterFileIntVhdl extends App {
  Config.spinal.generateVhdl(RegisterFileInt())
}
