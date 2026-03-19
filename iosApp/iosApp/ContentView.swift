import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        let vc = MainViewControllerKt.MainViewController()
        MainViewControllerKt.setDocumentPickerLauncher(launcher: {
            DocumentPickerHelper.shared.presentDocumentPicker(from: vc)
        })
        // Tema ile uyumlu arka plan (light/dark otomatik).
        vc.view.backgroundColor = UIColor.systemBackground
        return vc
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    // Safe area üst/alt bölgeleri sistem bar renkleriyle uyumlu olsun.
    private static var surfaceColor: Color { Color(UIColor.systemBackground) }

    var body: some View {
        ZStack {
            ContentView.surfaceColor
                .ignoresSafeArea()
            ComposeView()
                .ignoresSafeArea()
        }
    }
}



