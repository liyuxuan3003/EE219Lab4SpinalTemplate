PROJECT:=EE219Lab4

TOP:=TopLevel

SRCS_XDC_EXTRA:=
SRCS_SPINAL_EXTRA:=
PLATFORM:=
FLASH:=

include makefile-spinal/makefile-spinal.mk

BUILD_DIR_ASM:=${BUILD_DIR}/sw

TASK_INST_1:=InstTest1
TASK_INST_2:=InstTest2
TASK_INST_3:=InstTest3
TASK_MAC_SCALAR:=MacScalar
TASK_MAC_VECTOR:=MacVector
TASK_SOFTMAX:=Softmax

ISSUE1:=I1R219Top
ISSUE2:=I2R219Top

TASKS_I1:=${TASK_INST_1} ${TASK_MAC_SCALAR}
TASKS_I2:=${TASK_INST_2} ${TASK_INST_3} ${TASK_MAC_VECTOR} ${TASK_SOFTMAX}

TASKS:=${TASKS_I1} ${TASKS_I2}

PYTHON:=python

PY_ASSEMBLER:=Compile.py

.PHONY: ${TASKS} all

SRCS_ASM_I1:=$(addsuffix .asm,${TASKS_I1})
SRCS_ASM_I2:=$(addsuffix .asm,${TASKS_I2})
SRCS_ASM:=${SRCS_ASM_I1} ${SRCS_ASM_I2}

BINS_I1:=$(addprefix ${BUILD_DIR_ASM}/,$(addsuffix .bin,${TASKS_I1}))
BINS_I2:=$(addprefix ${BUILD_DIR_ASM}/,$(addsuffix .bin,${TASKS_I2}))
BINS:=${BINS_I1} ${BINS_I2}

TXTS_I1:=$(addprefix ${BUILD_DIR}/,$(addsuffix .txt,${TASKS_I1}))
TXTS_I2:=$(addprefix ${BUILD_DIR}/,$(addsuffix .txt,${TASKS_I2}))
TXTS:=${TXTS_I1} ${TXTS_I2}

all: ${TASKS}
	${NOTIFY_DONE}

${TASKS}: %: ${BUILD_DIR}/%.txt
	${NOTIFY_DONE}

${BINS_I1}: ${BUILD_DIR_ASM}/%.bin: %.asm | ${BUILD_DIR_ASM}
	${PYTHON} ${PY_ASSEMBLER} -n 1 -i $< -o $@
	${NOTIFY_DONE}

${BINS_I2}: ${BUILD_DIR_ASM}/%.bin: %.asm | ${BUILD_DIR_ASM}
	${PYTHON} ${PY_ASSEMBLER} -n 2 -i $< -o $@
	${NOTIFY_DONE}

${TXTS_I1}: ${BUILD_DIR}/%.txt: ${BUILD_DIR_ASM}/%.bin ${SRCS_SPINAL} | ${BUILD_DIR} ${BUILD_DIR_SIM}
	${SBT} "runMain ${PROJECT}.${ISSUE1}Sim $*"
	${NOTIFY_DONE}

${TXTS_I2}: ${BUILD_DIR}/%.txt: ${BUILD_DIR_ASM}/%.bin ${SRCS_SPINAL} | ${BUILD_DIR} ${BUILD_DIR_SIM}
	${SBT} "runMain ${PROJECT}.${ISSUE2}Sim $*"
	${NOTIFY_DONE}

${BUILD_DIR_ASM}: | ${BUILD_DIR}
	mkdir -p ${BUILD_DIR_ASM}
	${NOTIFY_DONE}
