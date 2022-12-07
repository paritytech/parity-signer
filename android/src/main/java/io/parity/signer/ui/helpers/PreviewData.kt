package io.parity.signer.ui.helpers


object PreviewData {
	/// Example valid identicon that can be used in view models to be rendered by `Identicon` view
	val exampleIdenticon: List<UByte> = listOf<Int>(137, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 0, 30, 0, 0, 0, 30, 8, 6, 0, 0, 0, 59, 48, 174, 162, 0, 0, 10, 172, 73, 68, 65, 84, 120, 156, 117, 87, 7, 116, 21, 101, 22, 254, 166, 189, 94, 66, 8, 129, 96, 104, 75, 175, 22, 64, 122, 11, 82, 18, 196, 208, 20, 17, 196, 69, 19, 86, 112, 1, 149, 85, 80, 92, 68, 151, 178, 40, 8, 43, 46, 210, 81, 215, 229, 80, 165, 9, 178, 74, 215, 13, 42, 134, 162, 18, 5, 116, 33, 16, 8, 201, 203, 75, 242, 146, 215, 231, 205, 204, 126, 243, 4, 142, 235, 234, 61, 231, 63, 111, 230, 205, 252, 247, 251, 239, 119, 235, 8, 134, 97, 224, 55, 68, 226, 210, 184, 146, 162, 105, 90, 103, 85, 85, 135, 197, 227, 241, 126, 130, 32, 100, 186, 221, 46, 62, 19, 16, 10, 135, 164, 132, 154, 240, 91, 44, 150, 2, 69, 81, 246, 203, 178, 124, 204, 124, 157, 203, 20, 153, 43, 193, 245, 127, 242, 91, 192, 183, 55, 16, 104, 92, 44, 22, 155, 158, 72, 36, 186, 235, 186, 14, 139, 69, 65, 36, 18, 195, 39, 123, 63, 69, 148, 191, 3, 134, 246, 64, 131, 59, 234, 33, 28, 138, 66, 146, 68, 46, 233, 123, 30, 98, 173, 213, 106, 93, 201, 3, 70, 169, 194, 52, 64, 231, 250, 31, 160, 95, 3, 78, 130, 82, 186, 132, 195, 225, 141, 180, 178, 131, 9, 40, 240, 79, 65, 20, 53, 81, 20, 133, 233, 19, 95, 22, 246, 127, 120, 216, 16, 33, 162, 93, 251, 150, 194, 198, 93, 75, 141, 180, 250, 169, 134, 26, 79, 136, 124, 81, 48, 33, 100, 89, 44, 183, 219, 29, 79, 241, 16, 219, 205, 173, 92, 166, 240, 201, 79, 242, 75, 224, 36, 40, 45, 204, 35, 232, 90, 19, 144, 27, 19, 118, 151, 67, 224, 181, 100, 145, 20, 28, 59, 88, 128, 241, 217, 211, 145, 146, 234, 129, 36, 74, 40, 189, 81, 134, 249, 203, 159, 199, 228, 25, 227, 80, 83, 89, 13, 73, 150, 116, 205, 128, 150, 208, 116, 133, 186, 96, 183, 219, 223, 224, 154, 201, 75, 83, 204, 3, 36, 1, 127, 14, 156, 4, 141, 68, 34, 243, 185, 230, 240, 26, 10, 65, 171, 124, 126, 121, 247, 186, 237, 40, 43, 185, 129, 1, 35, 6, 34, 179, 109, 75, 100, 119, 158, 96, 82, 10, 197, 170, 192, 87, 225, 199, 250, 205, 127, 69, 183, 97, 89, 56, 93, 92, 5, 171, 34, 161, 185, 87, 70, 138, 85, 208, 19, 6, 65, 12, 67, 226, 225, 119, 187, 92, 174, 17, 84, 105, 74, 18, 252, 22, 112, 18, 52, 26, 141, 230, 5, 131, 193, 181, 164, 147, 6, 210, 90, 171, 69, 124, 126, 244, 116, 28, 255, 228, 48, 236, 162, 29, 186, 110, 96, 205, 145, 119, 241, 89, 193, 57, 44, 127, 117, 45, 116, 238, 29, 54, 106, 0, 94, 94, 49, 27, 31, 93, 142, 34, 28, 215, 169, 17, 240, 90, 68, 12, 105, 98, 133, 66, 237, 16, 132, 4, 117, 41, 54, 155, 237, 239, 4, 255, 35, 113, 76, 159, 107, 124, 100, 36, 47, 232, 203, 46, 177, 104, 236, 164, 162, 88, 24, 80, 49, 221, 237, 113, 139, 223, 156, 60, 139, 39, 179, 30, 131, 205, 97, 131, 72, 11, 43, 203, 253, 184, 255, 177, 145, 120, 101, 253, 34, 156, 59, 125, 14, 145, 104, 28, 93, 122, 180, 67, 97, 113, 16, 39, 174, 71, 225, 84, 232, 94, 218, 17, 101, 76, 247, 202, 80, 208, 49, 211, 141, 112, 76, 163, 75, 68, 19, 92, 166, 107, 30, 231, 1, 54, 18, 79, 54, 129, 249, 178, 142, 88, 76, 253, 230, 82, 241, 197, 14, 139, 87, 189, 148, 40, 43, 191, 46, 103, 245, 206, 193, 227, 99, 166, 225, 241, 94, 15, 227, 202, 197, 98, 184, 83, 60, 240, 85, 151, 99, 241, 250, 215, 145, 222, 189, 7, 150, 109, 42, 128, 70, 243, 70, 245, 106, 134, 254, 221, 91, 99, 231, 249, 32, 100, 145, 28, 82, 171, 65, 102, 70, 119, 72, 197, 145, 173, 31, 97, 211, 250, 61, 176, 217, 173, 250, 148, 89, 143, 137, 61, 250, 119, 142, 74, 162, 210, 84, 150, 165, 178, 36, 48, 131, 105, 28, 35, 114, 211, 228, 217, 99, 18, 31, 29, 61, 32, 167, 122, 157, 168, 168, 12, 97, 197, 194, 85, 232, 88, 239, 94, 44, 152, 58, 7, 1, 127, 53, 134, 140, 205, 193, 200, 103, 242, 48, 234, 197, 109, 40, 173, 168, 133, 34, 75, 164, 27, 216, 60, 47, 23, 117, 152, 82, 133, 215, 84, 242, 104, 160, 75, 83, 39, 42, 207, 124, 131, 241, 67, 167, 67, 86, 100, 68, 99, 49, 52, 107, 222, 88, 221, 124, 228, 109, 165, 94, 122, 218, 18, 166, 218, 115, 130, 78, 107, 163, 225, 232, 137, 210, 242, 107, 221, 71, 228, 245, 97, 52, 170, 146, 34, 91, 80, 21, 240, 99, 112, 159, 7, 176, 110, 233, 54, 148, 151, 251, 80, 205, 131, 180, 106, 211, 8, 199, 10, 127, 192, 248, 121, 187, 81, 199, 99, 135, 72, 127, 150, 241, 255, 185, 147, 250, 96, 234, 152, 94, 40, 46, 47, 129, 72, 67, 26, 53, 104, 132, 101, 127, 89, 141, 101, 47, 175, 70, 189, 140, 52, 50, 160, 115, 127, 141, 177, 97, 207, 27, 66, 207, 129, 93, 253, 34, 228, 54, 2, 125, 219, 185, 166, 166, 230, 43, 171, 197, 134, 252, 89, 163, 177, 239, 200, 1, 212, 241, 58, 80, 85, 29, 230, 198, 229, 24, 210, 235, 73, 28, 58, 226, 35, 129, 10, 50, 26, 0, 109, 218, 11, 120, 112, 238, 14, 252, 88, 82, 9, 11, 35, 216, 44, 26, 187, 23, 140, 67, 105, 202, 191, 112, 180, 98, 23, 25, 208, 48, 162, 209, 20, 136, 167, 239, 192, 216, 161, 121, 144, 101, 5, 177, 120, 12, 191, 107, 222, 24, 91, 143, 174, 214, 28, 110, 135, 100, 179, 218, 38, 11, 76, 157, 185, 161, 80, 232, 21, 222, 104, 37, 101, 37, 210, 219, 239, 44, 196, 181, 235, 63, 160, 79, 143, 28, 76, 24, 53, 13, 251, 247, 133, 80, 83, 195, 8, 103, 86, 6, 195, 6, 238, 203, 114, 34, 38, 251, 177, 108, 219, 183, 8, 197, 226, 200, 31, 124, 39, 90, 116, 12, 96, 241, 165, 169, 176, 137, 14, 18, 205, 200, 38, 255, 179, 91, 111, 64, 225, 7, 63, 98, 203, 134, 237, 112, 121, 93, 200, 123, 110, 34, 238, 234, 218, 78, 15, 5, 35, 162, 205, 102, 221, 39, 208, 218, 67, 137, 68, 34, 139, 81, 167, 123, 157, 118, 209, 31, 147, 80, 76, 250, 58, 54, 244, 32, 64, 63, 238, 254, 48, 76, 95, 178, 28, 49, 112, 162, 17, 3, 45, 91, 43, 200, 26, 224, 69, 237, 181, 11, 44, 111, 17, 212, 109, 120, 47, 14, 250, 62, 192, 150, 27, 11, 225, 145, 211, 200, 12, 16, 208, 252, 248, 125, 250, 92, 244, 110, 56, 28, 87, 194, 1, 56, 104, 181, 151, 126, 169, 9, 70, 12, 22, 62, 83, 42, 132, 64, 32, 112, 62, 174, 38, 90, 185, 173, 146, 113, 224, 135, 128, 240, 183, 47, 125, 8, 49, 243, 155, 120, 100, 44, 30, 148, 137, 139, 167, 116, 20, 157, 87, 9, 206, 4, 100, 226, 13, 201, 118, 195, 184, 184, 6, 254, 194, 237, 44, 5, 58, 92, 77, 122, 195, 51, 112, 26, 222, 44, 157, 129, 242, 88, 49, 237, 213, 209, 204, 222, 1, 83, 26, 46, 199, 231, 65, 3, 87, 213, 4, 36, 198, 66, 123, 171, 130, 187, 157, 86, 168, 140, 1, 222, 66, 168, 174, 14, 20, 25, 186, 214, 86, 101, 148, 253, 97, 127, 137, 88, 26, 84, 225, 177, 74, 40, 11, 37, 48, 174, 125, 10, 102, 116, 169, 135, 83, 231, 98, 80, 99, 58, 50, 155, 186, 224, 197, 69, 20, 111, 157, 10, 81, 177, 145, 5, 25, 106, 168, 28, 153, 131, 23, 32, 218, 178, 19, 10, 43, 118, 179, 128, 136, 232, 237, 29, 10, 159, 150, 134, 163, 193, 90, 56, 69, 49, 217, 170, 24, 252, 200, 118, 219, 144, 194, 83, 208, 46, 35, 9, 12, 67, 107, 27, 82, 117, 61, 127, 95, 137, 24, 100, 245, 177, 179, 16, 84, 132, 53, 228, 180, 112, 99, 94, 223, 250, 184, 112, 69, 69, 60, 170, 163, 97, 99, 55, 116, 255, 215, 184, 186, 99, 6, 36, 155, 27, 172, 42, 72, 4, 203, 208, 96, 192, 11, 112, 117, 26, 129, 239, 42, 15, 81, 163, 132, 187, 156, 247, 226, 28, 11, 199, 231, 97, 22, 21, 82, 76, 151, 155, 96, 24, 236, 182, 162, 30, 147, 93, 77, 2, 147, 106, 245, 38, 213, 239, 156, 173, 20, 86, 21, 150, 39, 79, 151, 98, 149, 177, 44, 59, 19, 177, 43, 34, 190, 40, 140, 179, 250, 0, 46, 151, 136, 65, 131, 173, 8, 22, 188, 138, 170, 239, 143, 176, 26, 10, 176, 167, 183, 70, 198, 253, 139, 176, 46, 176, 8, 231, 131, 95, 112, 47, 243, 216, 61, 16, 99, 211, 95, 193, 199, 53, 65, 84, 168, 90, 146, 218, 22, 118, 11, 250, 184, 172, 44, 58, 188, 161, 152, 193, 85, 64, 224, 30, 108, 23, 186, 141, 189, 236, 66, 173, 140, 226, 128, 138, 59, 211, 21, 56, 227, 81, 236, 220, 203, 62, 203, 157, 52, 14, 225, 176, 142, 246, 157, 236, 232, 222, 69, 64, 244, 210, 9, 18, 21, 129, 43, 115, 32, 10, 162, 95, 224, 31, 215, 231, 220, 12, 46, 3, 181, 90, 21, 242, 235, 47, 64, 199, 180, 65, 184, 34, 68, 96, 39, 11, 117, 152, 1, 9, 85, 37, 34, 3, 85, 16, 106, 5, 182, 191, 37, 92, 51, 153, 111, 90, 92, 141, 73, 123, 15, 188, 135, 203, 87, 206, 163, 111, 207, 108, 220, 211, 105, 40, 118, 237, 172, 100, 244, 10, 204, 71, 166, 19, 131, 165, 71, 119, 43, 238, 104, 166, 97, 227, 129, 75, 8, 70, 99, 152, 216, 191, 3, 98, 41, 103, 240, 102, 241, 115, 4, 174, 67, 88, 3, 225, 68, 45, 102, 182, 90, 133, 216, 57, 27, 118, 252, 115, 39, 60, 94, 15, 198, 228, 229, 34, 173, 126, 93, 157, 253, 64, 100, 183, 58, 110, 22, 144, 172, 64, 160, 250, 144, 203, 233, 49, 102, 47, 154, 34, 172, 223, 178, 1, 14, 135, 200, 78, 164, 99, 195, 146, 205, 104, 193, 148, 56, 124, 216, 199, 64, 146, 80, 55, 85, 68, 223, 254, 54, 76, 94, 178, 23, 199, 79, 23, 51, 210, 69, 52, 76, 243, 98, 219, 252, 209, 56, 174, 175, 196, 201, 234, 195, 220, 167, 97, 72, 198, 4, 180, 43, 27, 130, 135, 6, 231, 163, 170, 50, 0, 149, 195, 76, 207, 158, 93, 176, 97, 239, 50, 242, 110, 72, 14, 187, 99, 86, 178, 193, 135, 195, 145, 111, 125, 254, 27, 109, 114, 159, 232, 173, 199, 226, 81, 209, 172, 98, 254, 106, 31, 114, 250, 143, 194, 154, 215, 183, 160, 220, 31, 66, 40, 168, 161, 89, 19, 23, 62, 61, 117, 17, 143, 204, 219, 3, 47, 253, 197, 148, 68, 89, 85, 16, 11, 242, 179, 144, 247, 192, 61, 40, 242, 157, 165, 94, 3, 109, 51, 186, 226, 205, 133, 235, 240, 218, 156, 183, 80, 63, 35, 61, 121, 152, 64, 85, 173, 241, 206, 190, 229, 66, 183, 126, 247, 68, 88, 50, 59, 242, 189, 100, 147, 120, 150, 213, 107, 233, 35, 211, 134, 170, 103, 138, 78, 42, 117, 188, 117, 113, 253, 134, 31, 115, 159, 157, 135, 113, 131, 38, 99, 249, 11, 11, 224, 43, 41, 67, 238, 164, 209, 104, 51, 176, 63, 178, 159, 121, 159, 202, 12, 90, 44, 161, 170, 54, 138, 141, 47, 230, 208, 45, 205, 240, 213, 85, 182, 64, 193, 64, 231, 102, 118, 20, 238, 63, 138, 252, 135, 103, 35, 213, 235, 69, 44, 26, 135, 203, 227, 76, 108, 59, 182, 70, 110, 218, 188, 209, 251, 22, 139, 245, 209, 36, 48, 39, 72, 155, 150, 208, 139, 11, 10, 143, 166, 255, 249, 245, 233, 122, 117, 77, 149, 216, 237, 238, 62, 88, 56, 235, 45, 204, 26, 249, 52, 190, 252, 119, 1, 92, 54, 15, 194, 209, 16, 86, 238, 89, 141, 171, 182, 116, 44, 121, 239, 83, 232, 180, 120, 68, 207, 230, 120, 242, 161, 238, 216, 115, 49, 4, 214, 2, 122, 152, 227, 14, 93, 144, 219, 202, 141, 213, 243, 215, 208, 199, 7, 204, 3, 26, 211, 94, 122, 2, 15, 78, 26, 206, 44, 151, 218, 115, 26, 45, 50, 129, 25, 54, 201, 57, 107, 12, 91, 211, 182, 80, 36, 164, 213, 212, 6, 196, 198, 141, 154, 10, 23, 206, 126, 135, 252, 126, 19, 192, 153, 11, 156, 74, 80, 197, 65, 32, 103, 98, 46, 94, 221, 176, 24, 255, 185, 116, 149, 237, 46, 129, 118, 109, 154, 160, 240, 114, 37, 78, 148, 198, 146, 131, 128, 41, 97, 38, 106, 223, 76, 27, 218, 53, 112, 160, 228, 90, 5, 231, 54, 69, 245, 214, 113, 43, 204, 222, 133, 14, 135, 99, 14, 95, 249, 105, 16, 160, 48, 89, 160, 133, 66, 193, 165, 241, 184, 250, 172, 44, 201, 106, 66, 75, 40, 172, 225, 120, 234, 190, 73, 248, 238, 235, 115, 112, 57, 220, 8, 132, 171, 177, 108, 203, 74, 68, 37, 43, 94, 123, 97, 5, 152, 134, 120, 120, 210, 112, 140, 157, 54, 1, 123, 206, 215, 80, 133, 153, 76, 236, 99, 44, 26, 217, 77, 173, 112, 177, 198, 139, 138, 28, 103, 28, 113, 24, 18, 15, 50, 186, 7, 241, 177, 41, 194, 45, 224, 159, 142, 202, 125, 156, 185, 118, 209, 250, 92, 94, 39, 236, 14, 187, 248, 253, 233, 34, 241, 221, 197, 107, 81, 81, 234, 67, 191, 7, 178, 48, 120, 124, 46, 114, 186, 78, 68, 45, 139, 3, 103, 50, 166, 88, 8, 91, 15, 172, 64, 70, 231, 187, 113, 234, 74, 13, 44, 164, 185, 117, 170, 140, 198, 110, 201, 80, 117, 22, 44, 206, 91, 28, 242, 47, 120, 60, 158, 59, 153, 191, 81, 234, 101, 41, 162, 151, 110, 2, 155, 98, 130, 39, 111, 152, 215, 111, 113, 240, 123, 202, 124, 102, 119, 218, 85, 81, 148, 100, 230, 159, 224, 245, 120, 112, 228, 227, 207, 240, 232, 176, 167, 145, 154, 150, 146, 164, 255, 198, 117, 31, 230, 46, 121, 26, 83, 103, 78, 64, 101, 69, 21, 39, 14, 137, 3, 145, 192, 144, 49, 36, 238, 23, 56, 195, 29, 116, 185, 156, 195, 111, 130, 38, 153, 229, 50, 163, 63, 137, 117, 75, 110, 131, 211, 234, 73, 236, 213, 43, 73, 183, 141, 138, 200, 141, 160, 9, 204, 159, 72, 56, 42, 76, 200, 153, 33, 156, 249, 186, 136, 163, 129, 140, 212, 84, 47, 54, 209, 226, 86, 237, 155, 235, 252, 178, 96, 143, 16, 36, 179, 31, 179, 253, 129, 131, 221, 66, 206, 212, 166, 79, 77, 185, 13, 106, 202, 47, 129, 77, 49, 193, 77, 58, 52, 250, 166, 62, 15, 240, 39, 174, 73, 188, 174, 107, 166, 144, 195, 105, 195, 213, 203, 165, 120, 127, 205, 7, 236, 207, 49, 140, 24, 55, 132, 147, 102, 39, 82, 31, 74, 130, 145, 133, 8, 163, 118, 7, 65, 23, 113, 246, 46, 162, 30, 83, 76, 125, 230, 103, 204, 109, 249, 53, 224, 91, 146, 140, 118, 46, 78, 161, 70, 26, 191, 161, 70, 178, 202, 229, 114, 117, 83, 20, 57, 205, 233, 114, 36, 55, 70, 233, 2, 178, 80, 171, 88, 148, 211, 138, 172, 236, 99, 57, 220, 65, 240, 31, 249, 200, 20, 83, 135, 198, 149, 124, 247, 231, 242, 95, 117, 113, 41, 181, 55, 217, 230, 253, 0, 0, 0, 0, 73, 69, 78, 68, 174, 66, 96, 130).map {it.toUByte()}

	val exampleQRCode: List<UByte>  = listOf<Int>(137, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 0, 196, 0, 0, 0, 196, 1, 3, 0, 0, 0, 250, 104, 28, 40, 0, 0, 0, 6, 80, 76, 84, 69, 0, 0, 0, 255, 255, 255, 165, 217, 159, 221, 0, 0, 2, 48, 73, 68, 65, 84, 120, 156, 237, 215, 205, 113, 237, 32, 12, 5, 96, 168, 0, 58, 112, 255, 101, 209, 129, 168, 192, 239, 147, 125, 243, 178, 201, 82, 204, 100, 17, 50, 215, 54, 58, 154, 209, 223, 65, 34, 253, 110, 63, 175, 253, 135, 252, 18, 164, 183, 54, 214, 140, 43, 230, 110, 45, 174, 117, 121, 141, 168, 71, 102, 52, 178, 152, 49, 246, 104, 123, 190, 130, 19, 8, 65, 164, 100, 183, 171, 177, 127, 191, 130, 35, 200, 30, 49, 87, 187, 118, 204, 214, 78, 34, 193, 188, 240, 218, 188, 211, 141, 20, 28, 64, 8, 218, 246, 184, 251, 136, 126, 243, 193, 247, 1, 36, 25, 130, 122, 223, 139, 51, 47, 67, 106, 145, 103, 205, 52, 187, 230, 8, 223, 207, 170, 71, 122, 67, 193, 164, 162, 160, 87, 166, 212, 82, 203, 122, 100, 72, 231, 224, 195, 154, 77, 82, 149, 141, 214, 19, 169, 77, 33, 50, 215, 245, 124, 62, 204, 192, 119, 193, 146, 214, 35, 194, 28, 83, 116, 177, 145, 68, 102, 251, 77, 227, 0, 130, 124, 236, 138, 19, 15, 213, 111, 116, 53, 164, 127, 0, 225, 131, 190, 183, 240, 124, 204, 157, 39, 88, 27, 172, 71, 216, 103, 89, 103, 109, 11, 144, 154, 132, 143, 7, 118, 133, 136, 252, 237, 225, 77, 37, 174, 204, 172, 223, 9, 164, 231, 188, 240, 39, 161, 116, 208, 63, 218, 21, 7, 16, 249, 116, 176, 166, 238, 96, 131, 135, 147, 19, 136, 105, 67, 94, 136, 128, 152, 206, 99, 101, 223, 180, 115, 113, 210, 47, 71, 236, 85, 44, 158, 40, 157, 178, 232, 236, 127, 24, 82, 138, 244, 48, 145, 146, 137, 182, 140, 231, 243, 139, 241, 165, 72, 150, 142, 221, 91, 219, 203, 150, 231, 132, 193, 31, 15, 106, 17, 6, 85, 77, 94, 169, 36, 233, 253, 64, 245, 136, 80, 133, 54, 201, 204, 116, 166, 245, 138, 172, 97, 57, 210, 25, 220, 99, 177, 234, 12, 107, 22, 62, 72, 235, 145, 249, 114, 2, 168, 96, 95, 187, 199, 131, 207, 119, 17, 98, 35, 204, 102, 98, 40, 160, 11, 138, 184, 131, 79, 229, 72, 150, 42, 57, 255, 148, 45, 12, 94, 252, 160, 82, 143, 152, 23, 110, 116, 146, 153, 124, 236, 20, 20, 242, 245, 160, 20, 113, 115, 200, 46, 78, 252, 166, 82, 175, 160, 90, 143, 232, 18, 230, 171, 137, 1, 200, 139, 49, 129, 199, 9, 228, 214, 33, 146, 230, 14, 239, 242, 54, 14, 15, 33, 170, 21, 218, 44, 166, 56, 90, 54, 95, 55, 226, 82, 36, 87, 26, 151, 212, 149, 23, 47, 243, 66, 212, 245, 200, 99, 81, 95, 50, 251, 132, 44, 175, 52, 53, 168, 114, 4, 203, 223, 146, 189, 98, 214, 9, 79, 32, 206, 148, 255, 140, 250, 59, 1, 167, 169, 203, 149, 67, 136, 229, 236, 178, 143, 34, 217, 54, 232, 28, 65, 120, 144, 252, 35, 48, 216, 245, 166, 79, 70, 107, 17, 18, 30, 232, 176, 38, 186, 226, 105, 27, 65, 183, 28, 249, 207, 16, 215, 98, 217, 12, 188, 252, 120, 80, 139, 252, 188, 254, 144, 95, 129, 252, 3, 117, 24, 116, 223, 174, 44, 31, 82, 0, 0, 0, 0, 73, 69, 78, 68, 174, 66, 96, 130).map {it.toUByte()}

	val exampleMarkdownDocs: String = "53616d6520617320746865205b607472616e73666572605d2063616c6c2c206275742077697468206120636865636b207468617420746865207472616e736665722077696c6c206e6f74206b696c6c207468650a6f726967696e206163636f756e742e0a0a393925206f66207468652074696d6520796f752077616e74205b607472616e73666572605d20696e73746561642e0a0a5b607472616e73666572605d3a207374727563742e50616c6c65742e68746d6c236d6574686f642e7472616e73666572"



}


