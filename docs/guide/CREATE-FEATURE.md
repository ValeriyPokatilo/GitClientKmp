# Создание нового feature модуля

В `mppLibrary/feature` создаем новую папку, с названием фичи.
Внутри создаем файл `build.gradle.kts`:

```kotlin
plugins {
    id("multiplatform-library-convention")
    id("feature-android-convention")
    id("feature-resources-convention")
    id("feature-dependencies-convention")
}

dependencies {
    commonMainApi(libs.moko.mvvm.flow)

    commonMainImplementation(projects.mppLibrary.utils)
    commonMainImplementation(projects.mppLibrary.entity)
}
```

Где:

- `id("feature-resources-convention")` - можно убрать, если ресурсов в фиче не будет
- `commonMainApi(libs.moko.mvvm.flow)` - можно убрать, если не нужны ViewModel'и
- `commonMainImplementation(projects.mppLibrary.utils)` - можно убрать, если не нужны утилиты общие
- `commonMainImplementation(projects.mppLibrary.entity)` - можно убрать, если не нужны сущности общие

В `src/commonMain` можно скопировать контент другой фичи как основу.

Что сделают плагины:

- `multiplatform-library-convention` - настроит компиляцию android, ios. подключит detekt.
- `feature-android-convention` - настроит `android.namespace` по имени фичи
- `feature-resources-convention` - настроит moko-resources и генерацию класса `MR`.
- `feature-dependencies-convention` - подключит зависимости нужные всем фичам (например koin).
