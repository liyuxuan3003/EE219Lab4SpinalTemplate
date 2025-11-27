package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class RegisterFileVec(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    // Addr of vs1
    val vs1 = in(Bits(cfg.rfWidth bits))
    // Addr of vs2
    val vs2 = in(Bits(cfg.rfWidth bits))
    // Addr of vd
    val vd = in(Bits(cfg.rfWidth bits))
    // Data of vs1
    val vs1Data = out(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    // Data of vs2
    val vs2Data = out(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    // Data of vd
    val vdData = in(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    // Write register file enable
    val vregWrite = in(Bool())
    // Write fisrt element in vector only
    val vregFirst = in(Bool())
  }

  // TODO
}

object RegisterFileVecSim extends App {
  Config.sim.compile(RegisterFileVec()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object RegisterFileVecVerilog extends App {
  Config.spinal.generateVerilog(RegisterFileVec())
}

object RegisterFileVecVhdl extends App {
  Config.spinal.generateVhdl(RegisterFileVec())
}
