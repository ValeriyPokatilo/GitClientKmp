import Foundation
import MultiPlatformLibrary

extension Data {
    func toKotlinByteArray() -> KotlinByteArray {
        let byteArray = KotlinByteArray(size: Int32(count))

        self.withUnsafeBytes { buffer in
            guard let baseAddress = buffer.baseAddress else { return }

            for index in 0 ..< count {
                byteArray.set(
                    index: Int32(index),
                    value: baseAddress.load(
                        fromByteOffset: index,
                        as: Int8.self
                    )
                )
            }
        }

        return byteArray
    }
}
