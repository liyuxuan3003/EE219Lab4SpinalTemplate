package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class RegisterFileVec(cfg: R219Config = R219Config()) extends Component {
  val io = new Bundle {
    val vs1 = in(Bits(cfg.rfWidth bits))
    val vs2 = in(Bits(cfg.rfWidth bits))
    val vd = in(Bits(cfg.rfWidth bits))
    val vs1Data = out(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    val vs2Data = out(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    val vdData = in(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    val vregWrite = in(Bool())
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
