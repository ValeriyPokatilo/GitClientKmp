import UIKit

final class SceneDelegate: NSObject, UIWindowSceneDelegate {

    var window: UIWindow?
    var appCoordinator: AppCoordinator?

    func scene(
        _ scene: UIScene,
        willConnectTo session: UISceneSession,
        options connectionOptions: UIScene.ConnectionOptions
    ) {
        guard let windowScene = (scene as? UIWindowScene) else { return }

        let window = UIWindow(windowScene: windowScene)
        appCoordinator = AppCoordinator(window: window)
        appCoordinator?.start()
        self.window = window
        window.makeKeyAndVisible()
    }
}
