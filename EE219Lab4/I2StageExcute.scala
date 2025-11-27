package EE219Lab4

import spinal.core._
import spinal.core.sim._
import spinal.lib._

case class I2StageExcute(cfg: R219Config = R219Config(isVec = true)) extends Component {
  val io = new Bundle {
    // Data of rs1
    val rs1Data = in(Bits(cfg.dataWidth bits))
    // Data of rs2
    val rs2Data = in(Bits(cfg.dataWidth bits))
    // Extended imm
    val immExt = in(Bits(cfg.dataWidth bits))
    // Data of vs1
    val vs1Data = in(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    // Data of vs2
    val vs2Data = in(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    // Extended vext
    val vecExt = in(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    // Alu operation
    val aluOp = in(AluOp())
    // Alu srcb source select
    val aluSrcB = in(AluSrcB())
    // Alu calculation result
    val aluResult = out(Bits(cfg.dataWidth bits))
    // BranchUnit operation
    val branchOp = in(BranchOp())
    // Vlu operation
    val vluOp = in(VluOp())
    // Vlu srca source select
    val vluSrcA = in(VluSrcA())
    // Vlu calculation result
    val vluResult = out(Vec(Bits(cfg.dataWidth bits), cfg.vectElements))
    // Pc (to calculate pctarget = pc + imm)
    val pc = in(Bits(cfg.addrWidth bits))
    // Pc target
    val pcTarget = out(Bits(cfg.addrWidth bits))
    // Pc next select
    val pcSrc = out(PcSrc())
  }

  // Area of scalar
  val x = new Area {
    // Alu
    val alu = Alu(cfg)
    // BranchUnit
    val bu = BranchUnit(cfg)

    // TODO
  }

  // Area of vector
  val v = new Area {
    // Vlu
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
