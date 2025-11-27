# EE219Lab4SpinalTemplate

EE219Lab4SpinalTemplate针对EE219课程的Lab4，重新设计了一个完全基于SpinalHDL的代码框架。

ComputerArchitecture笔记：https://github.com/liyuxuan3003/ComputerArchitecture

该模板中，标量部分的微架构设计基本参照了CA笔记中绘制的RISC-V单周期处理器的架构图（部分控制信号和模块划分来自流水线版本的架构图），你可以从Release中下载到编译好的PDF文件。

运行1.1测试
```
make InstTest1
```

运行1.2测试
```
make MacScalar
```

运行2.1测试
```
make InstTest2
```

运行2.2测试
```
make MacVector
```

运行3.1测试
```
make InstTest3
```

运行3.2测试
```
make Softmax
```

## 关于v1.1至v1.2的更新内容概要
- 现在3.2也完全可以支持了
- 添加了大量注释

## 关于v1.0至v1.1的更新内容概要
- 添加了双发射的硬件框架，现在除3.2外都可以运行
- 重构硬件架构，部分模块被重命名（例如`I1Alu`被重命名为`Alu`）
- 重构测试架构，使用更清晰的面向对象的方式创建测试代码。
