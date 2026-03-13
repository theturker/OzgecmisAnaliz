import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        let vc = MainViewControllerKt.MainViewController()
        MainViewControllerKt.setDocumentPickerLauncher(launcher: {
            DocumentPickerHelper.shared.presentDocumentPicker(from: vc)
        })
        vc.view.backgroundColor = UIColor(red: 250/255, green: 250/255, blue: 250/255, alpha: 1)
        return vc
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    // Uygulama arka plan rengi (Surface #FAFAFA) – safe area üst/alt siyah görünmesin
    private static let surfaceColor = Color(red: 250/255, green: 250/255, blue: 250/255)

    var body: some View {
        ZStack {
            ContentView.surfaceColor
                .ignoresSafeArea()
            ComposeView()
                .ignoresSafeArea()
        }
    }
}



