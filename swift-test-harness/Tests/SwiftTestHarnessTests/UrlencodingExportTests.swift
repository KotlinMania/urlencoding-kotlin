import XCTest
import Urlencoding

final class UrlencodingExportTests: XCTestCase {
    func testSwiftModuleLoads() throws {
        XCTAssertEqual(enc.encode(data: "this that"), "this%20that")
    }
}
