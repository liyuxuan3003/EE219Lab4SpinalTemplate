# EE219Lab4SpinalTemplate

EE219Lab4SpinalTemplate针对EE219课程的Lab4，重新设计了一个完全基于SpinalHDL的代码框架。

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

运行3.2测试（尚未支持）
```
make Softmax
```

该框架仍在开发中。

## 关于v1.0至v1.1的更新内容概要
- 添加了双发射的硬件框架，现在除3.2外都可以运行
- 重构硬件架构，部分模块被重命名（例如`I1Alu`被重命名为`Alu`）
- 重构测试架构，使用更清晰的面向对象的方式创建测试代码。
