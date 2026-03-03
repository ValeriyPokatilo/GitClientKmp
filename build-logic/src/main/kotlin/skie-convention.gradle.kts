import co.touchlab.skie.configuration.DefaultArgumentInterop
import co.touchlab.skie.configuration.EnumInterop
import co.touchlab.skie.configuration.FunctionInterop
import co.touchlab.skie.configuration.SealedInterop

plugins {
    id("multiplatform-library-convention")
    id("co.touchlab.skie")
}

skie {
    swiftBundling {
        enabled = false
    }
    analytics {
        disableUpload.set(true)
    }
    features {
        group {
            EnumInterop.Enabled(true)
            SealedInterop.Enabled(true)
            DefaultArgumentInterop.Enabled(false)
            FunctionInterop.FileScopeConversion.Enabled(true)
            coroutinesInterop.set(true)
        }
        enableFlowCombineConvertorPreview = true
    }
}
