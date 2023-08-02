package io.parity.signer.components.networkicon.dot

import io.parity.signer.components.networkicon.dot.DotIconColors.SchemeElement


internal object DotIconConstants {
	/// Function to set default coloring schemes, taken as is from js code
	fun defaultSchemes() = listOf(
			SchemeElement(
				// "target"
				freq =  1u,
				colors = listOf(0, 28, 0, 0, 28, 0, 0, 28, 0, 0, 28, 0, 0, 28, 0, 0, 28, 0, 1)
					.map { it.toUByte() },
			),
			SchemeElement(
				// "cube",
				freq =  20u,
				colors = listOf(0, 1, 3, 2, 4, 3, 0, 1, 3, 2, 4, 3, 0, 1, 3, 2, 4, 3, 5)
					.map { it.toUByte() },
			),
			SchemeElement(
				// "quazar",
				freq =  16u,
				colors = listOf(1, 2, 3, 1, 2, 4, 5, 5, 4, 1, 2, 3, 1, 2, 4, 5, 5, 4, 0)
					.map { it.toUByte() },
			),
			SchemeElement(
				// "flower",
				freq =  32u,
				colors = listOf(0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 3)
					.map { it.toUByte() },
			),
			SchemeElement(
				// "cyclic",
				freq =  32u,
				colors = listOf(0, 1, 2, 3, 4, 5, 0, 1, 2, 3, 4, 5, 0, 1, 2, 3, 4, 5, 6)
					.map { it.toUByte() },
			),
			SchemeElement(
				// "vmirror",
				freq = 128u,
				colors = listOf(0, 1, 2, 3, 4, 5, 3, 4, 2, 0, 1, 6, 7, 8, 9, 7, 8, 6, 10)
					.map { it.toUByte() },
			),
			SchemeElement(
				// "hmirror",
				freq =  128u,
				colors = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 8, 6, 7, 5, 3, 4, 2, 11)
					.map { it.toUByte() },
			),
		)
}
