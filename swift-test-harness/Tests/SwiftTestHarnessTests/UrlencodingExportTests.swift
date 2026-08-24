import Testing
import Urlencoding

@Suite struct UrlencodingExportTests {
    @Test func testSwiftModuleLoads() throws {
        #expect(encode(data: "this that") == "this%20that")
    }
}
