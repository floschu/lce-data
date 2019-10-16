# d{}

[![version](https://img.shields.io/github/v/tag/floschu/lce-data?color=blue&label=version)](https://bintray.com/floschu/lce-data) [![build](https://github.com/floschu/lce-data/workflows/build/badge.svg)](https://github.com/floschu/lce-data/actions) [![license](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

representation of an asynchronous load of a data with support of kotlin flow

## installation

``` groovy
repositories {
    jcenter()
}

dependencies {
    implementation("at.florianschuster.data:lce-data:$version")  // kotlin only module
}
```

## concept

``` kotlin
val uninitializedData = Data.Uninitialized
val loadingData = Data.Loading
val successData = Data.Success(/*some value*/)
val failureData = Data.Failure(/*some throwable*/)

val evaluatedData = Data { /*some operation*/ }
if (evaluatedData is Data.Success) {
    val dataValue = evaluatedData()
}

val suspendedData = dataOf { /*some suspending operation*/ }
val flowData = dataFlowOf { /*some suspending operation*/ }
```

## author

visit my [website](https://florianschuster.at/).