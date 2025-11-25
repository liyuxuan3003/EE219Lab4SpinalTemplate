package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class IMemPort(cfg: R219Config = R219Config()) extends Bundle with IMasterSlave {
  val addr = Bits(cfg.addrWidth bits)
  val dataRd = if (cfg.isVec) Bits(cfg.dataWidth * cfg.issues bits) else Bits(cfg.dataWidth bits)

  def asMaster(): Unit = {
    in(addr)
    out(dataRd)
  }
}

case class DMemPort(cfg: R219Config = R219Config()) extends Bundle with IMasterSlave {
  val addr = Bits(cfg.addrWidth bits)
  val dataRd = Bits(cfg.dataWidth bits)
  val dataWr = Bits(cfg.dataWidth bits)
  val enableWr = Bool()

  def asMaster(): Unit = {
    in(addr, dataWr, enableWr)
    out(dataRd)
  }
}

case class VMemPort(cfg: R219Config = R219Config()) extends Bundle with IMasterSlave {
  val addr = Bits(cfg.addrWidth bits)
  val dataRd = Bits(cfg.vectWidth bits)
  val dataWr = Bits(cfg.vectWidth bits)
  val enableWr = Bool()

  def asMaster(): Unit = {
    in(addr, dataWr, enableWr)
    out(dataRd)
  }
}

case class Memory(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    val imem = master(IMemPort(cfg))
    val dmem = master(DMemPort(cfg))
    val vmem = if (cfg.isVec) master(VMemPort(cfg)) else null
  }

  val mem = Mem(Bits(cfg.dataWidth bits), cfg.sizeMem).simPublic()

  val words = cfg.vectWidth / cfg.dataWidth

  def convert(addr: Bits, offset: Int = 0): UInt = {
    (((addr.asUInt - cfg.baseMem) >> 2) + offset).resized
  }

  io.dmem.dataRd := mem(convert(io.dmem.addr))
  when(io.dmem.enableWr) {
    mem(convert(io.dmem.addr)) := io.dmem.dataWr
  }

  if (cfg.isVec) {
    val addrScalar = (io.imem.addr)
    val addrVector = (io.imem.addr.asUInt + 4).asBits
    io.imem.dataRd := mem(convert(addrVector)) ## mem(convert(addrScalar))
  } else {
    io.imem.dataRd := mem(convert(io.imem.addr))
  }

  if (cfg.isVec) {
    io.vmem.dataRd := Vec.tabulate(words) { i => mem(convert(io.vmem.addr, i)) }.asBits
    when(io.vmem.enableWr) {
      io.vmem.dataWr.subdivideIn(cfg.vectElements slices).zipWithIndex.foreach { case (data, i) => mem(convert(io.vmem.addr, i)) := data }
    }
  }
}

object MemorySim extends App {
  Config.sim.compile(Memory()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object MemoryVerilog extends App {
  Config.spinal.generateVerilog(Memory())
}

object MemoryVhdl extends App {
  Config.spinal.generateVhdl(Memory())
}
