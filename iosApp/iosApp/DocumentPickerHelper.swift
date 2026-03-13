import UIKit
import UniformTypeIdentifiers
import PDFKit
import ComposeApp

/// iOS dosya seçici: PDF seçilince Kotlin tarafına base64 + dosya adı + PDFKit ile çıkarılmış metin gönderir.
final class DocumentPickerHelper: NSObject, UIDocumentPickerDelegate {
    static let shared = DocumentPickerHelper()

    private weak var viewController: UIViewController?

    func presentDocumentPicker(from viewController: UIViewController) {
        self.viewController = viewController
        let types: [UTType] = [.pdf]
        let picker = UIDocumentPickerViewController(forOpeningContentTypes: types, asCopy: true)
        picker.delegate = self
        picker.allowsMultipleSelection = false
        viewController.present(picker, animated: true)
    }

    func documentPicker(_ controller: UIDocumentPickerViewController, didPickDocumentsAt urls: [URL]) {
        guard let url = urls.first else { return }
        let name = url.lastPathComponent
        let didStartAccessing = url.startAccessingSecurityScopedResource()
        defer {
            if didStartAccessing {
                url.stopAccessingSecurityScopedResource()
            }
        }
        guard let data = try? Data(contentsOf: url) else { return }
        let base64 = data.base64EncodedString()

        let extractedText: String
        if let doc = PDFDocument(data: data) {
            extractedText = doc.string ?? ""
        } else {
            extractedText = ""
        }

        MainViewControllerKt.onDocumentPickedBase64WithExtractedText(
            base64: base64,
            name: name,
            extractedText: extractedText
        )
    }
}
