package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I2StageExcute(cfg: R219Config = R219Config(isVec = true)) extends Component {
  val io = new Bundle {
    val rs1Data = in(Bits(cfg.dataWidth bits))
    val rs2Data = in(Bits(cfg.dataWidth bits))
    val immExt = in(Bits(cfg.dataWidth bits))
    val vs1Data = in(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    val vs2Data = in(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    val vecExt = in(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    val aluOp = in(AluOp())
    val aluSrcB = in(AluSrcB())
    val aluResult = out(Bits(cfg.dataWidth bits))
    val branchOp = in(BranchOp())
    val vluOp = in(VluOp())
    val vluSrcA = in(VluSrcA())
    val vluResult = out(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    val pc = in(Bits(cfg.addrWidth bits))
    val pcTarget = out(Bits(cfg.addrWidth bits))
    val pcSrc = out(PcSrc())
  }

  val x = new Area {
    val alu = Alu(cfg)
    val bu = BranchUnit(cfg)

    // TODO
  }

  val v = new Area {
    val vlu = Vlu(cfg)

    // TODO
  }
}

object I2StageExcuteSim extends App {
  Config.sim.compile(I2StageExcute()).doSim { dut =>
    dut.clockDomain.forkStimulus(period = 10, resetCycles = 9)
    dut.clockDomain.waitRisingEdge()

  }
}

object I2StageExcuteVerilog extends App {
  Config.spinal.generateVerilog(I2StageExcute())
}

object I2StageExcuteVhdl extends App {
  Config.spinal.generateVhdl(I2StageExcute())
}
