# WEB+DB PRESS Vol.106 特集1「実践Android/iOSアプリ設計」サンプルコード

技術評論社刊「WEB+DB PRESS Vol.106」の特集1「実践Android/iOSアプリ設計」で設計パターンの実装例として解説したサンプルコードです。

## 構成

各プラットフォームの実装を`ios`、`android`ディレクトリに分けて配置しています。

3章で紹介したMVVMパターンの実装は`mvvm`ディレクトリに、4章で紹介したFluxアーキテクチャの実装は`flux`ディレクトリにそれぞれ配置しています。

```
├── android
│   ├── flux
│   └── mvvm
└── ios
    ├── flux
    └── mvvm
```

## 2章で解説しているサンプルコード

iOSのサンプルコードの`DI_Mock_Tests.swift`は`ios/{mvvm|flux}/ShoppingAppForiOS/ShoppingAppForiOSTests`に配置されています。
Androidの実装は`android/{mvvm|flux}`配下に、サンプルアプリの実装として組み込まれています。

## 実行方法

各プラットフォームのディレクトリに配置されているプロジェクトファイルを開いて、シミュレータ、あるいはエミュレータで実行してください。

