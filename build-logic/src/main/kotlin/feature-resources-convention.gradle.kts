plugins {
    id("multiplatform-library-convention")
    id("dev.icerock.mobile.multiplatform-resources")
}

multiplatformResources {
    resourcesPackage = getFeaturePackage()
    resourcesClassName = "MR"
}

dependencies {
    commonMainImplementation(libsCatalog.safeBundle("moko-resources"))
}
