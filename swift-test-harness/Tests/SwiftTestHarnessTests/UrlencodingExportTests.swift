import Testing
import Urlencoding

@Suite struct UrlencodingExportTests {
    @Test func testSwiftModuleLoads() throws {
        #expect(enc.encode(data: "this that") == "this%20that")
    }
}
