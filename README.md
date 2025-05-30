# Snapback ImageView
An Instagram-like pinch-to-zoom Android ImageView with snap-back-to-original-position mechanism.

https://github.com/user-attachments/assets/b12a53df-da9a-4426-b9f0-ef323e73f895

## Installation
Add the dependency below into your **module**'s `build.gradle` file:
### Gradle


```gradle
dependencies {
    implementation "io.github.vanxioussss:snapback-image-view:<latest_version>"
}
```

### Gradle Kotlin
```gradle
dependencies {
    implementation("io.github.vanxioussss:snapback-image-view:<latest_version>")
}
```

## Features

- Zoomable image views with pinch-to-zoom gestures.
- Snapback animation to return the image to its original position.
- Customizable attributes during zooming.
- Easy integration into existing projects. You just need to replace your current `ImageView` with `SnapbackImageView`.

## Usage

Add `SnapbackImageView` to your layout, or just replace your `ImageView` with `SnapbackImageView`:

```xml
<com.vanxiousss.snapback.SnapbackImageView
    android:id="@+id/snapbackImageView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:src="@drawable/sample_image"
    app:shadowColor="#80000000"
    app:shadowMaxAlpha="192"
    app:shadowFadeEnabled="true"
    app:minScaleFactor="1.0"
    app:maxScaleFactor="5.0"
    app:endAnimationEnabled="true"
    app:endAnimationDuration="300" />
```

### Customization
You can customize the following attributes in XML:
- `shadowColor`: The color of the shadow during zooming.
- `shadowMaxAlpha`: The maximum alpha value for the shadow.
- `shadowFadeEnabled`: Enable or disable shadow fading.
- `minScaleFactor`: The minimum scale factor for zooming.
- `maxScaleFactor`: The maximum scale factor for zooming.
- `endAnimationEnabled`: Enable or disable the snapback animation.
- `endAnimationDuration`: Duration of the snapback animation in milliseconds.

## License
```xml
Copyright 2025 vanxioussss (Van Luong)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## Contributing
Contributions are welcome! Feel free to open issues or submit pull requests to improve the library.

## Author
<a href="https://github.com/vanxioussss/snapback/graphs/contributors">
  <img src="https://contributors-img.web.app/image?repo=vanxioussss/snapback" />
</a>
