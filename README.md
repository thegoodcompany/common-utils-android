# common-utils-android
__An effort to reduce re-writing similar codes across projects.__  
  
common-utils-android contains some of the commonly used android functions.

## Utilities
- [CalendarUtils](./common-utils/src/main/java/thegoodcompany/common/utils/CalendarUtils.java)
- [NumberUtils](./common-utils/src/main/java/thegoodcompany/common/utils/NumberUtils.java)
- [StringUtils](./common-utils/src/main/java/thegoodcompany/common/utils/StringUtils.java)
- [SystemUtils](./common-utils/src/main/java/thegoodcompany/common/utils/SystemUtils.java)
- [ArrayUtils](./common-utils/src/main/java/thegoodcompany/common/utils/ArrayUtils.java)
- [ObjectUtils](./common-utils/src/main/java/thegoodcompany/common/utils/ObjectUtils.java)
- [BaseListAdapter](./common-utils/src/main/java/thegoodcompany/common/utils/recyclerview/BaseListAdapter.java) - a recyclerview adapter w/ some predefined functionality
- [Flow](./common-utils/src/main/java/thegoodcompany/common/utils/miscellaneous/Flow.java) - null safe flow of operation

## Install and use
### Requirements
API 16+

### 1. Using Jitpack
* Add the JitPack maven repository to your project level build.gradle file:
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
        ...
    }
}
```

* Add this line inside the dependency block of your module level build.gradle file:
```
implementation 'com.github.thegoodcompany:common-utils-android:$version'
```
Replace `$version` with the latest version of common-utils-android. You can find a complete list of releases on the [releases](https://github.com/thegoodcompany/common-utils-android/releases) page.

### 2. Manual Install
1. Download the latest AAR file from the [releases](https://github.com/thegoodcompany/common-utils-android/releases) page.
1. Add the compiled AAR file to your project as dependency following these [instructions](https://developer.android.com/studio/projects/android-library#AddDependency).

## Demo App
A demo app is included in this repository. This demo app shows off some of the currently implemented utilities.  
Run the [common-utils-demo](https://github.com/thegoodcompany/common-utils-android/tree/main/common-utils-demo) module to get a preview of those implemented utilities.

## License
All files on the common-utils-android GitHub repository are subject to the MIT License. Please read the [LICENSE](https://github.com/thegoodcompany/common-utils-android/blob/main/LICENSE) file at the root of the project.
